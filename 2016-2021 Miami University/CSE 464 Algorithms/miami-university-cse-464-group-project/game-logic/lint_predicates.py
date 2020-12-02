#!/usr/bin/env python3

import re
import sys
from pathlib import Path
from typing import *
from typing import TextIO

try:
    import colorful

except ImportError:
    def identity(string: str) -> str:
        return string

    class ColorfulDummy:
        def __getattr__(self, item):
            return identity

    colorful = ColorfulDummy()

else:
    colorful.use_style('solarized')


predicate_re = re.compile(r'([a-z]\S*)\((.+)\)')
predicate_annotation_re = re.compile(r'^%\s*([a-z]\S*)\((.+)\)\.')
predicate_argument_re = re.compile(r'(".*?[^\\]"|".*?\\"|\d+|\w+),?')


class Predicate(NamedTuple):
    name: str
    arguments: Sequence[str]


def match_predicate_annotation(string: str) -> Optional[Predicate]:
    """
    Finds a single predicate annotation

    :param string: The string to search
    :return: The parsed annotation
    """
    match = predicate_annotation_re.search(string)
    if match:
        name, arguments = match.groups()
        arguments = predicate_argument_re.finditer(arguments)
        arguments = (argument.group(1) for argument in arguments)
        return Predicate(name=name, arguments=tuple(arguments))
    else:
        return None


def match_predicates(file: TextIO) -> Iterator[Tuple[int, Predicate]]:
    """
    Finds predicates

    :param file: The file to search
    :return: The predicates and the line number on which they are found
    """
    for line_number, line in enumerate(file, start=1):
        for match in predicate_re.finditer(line):
            name, arguments = match.groups()
            arguments = predicate_argument_re.finditer(arguments)
            arguments = (argument.group(1) for argument in arguments)
            yield line_number, Predicate(name=name, arguments=tuple(arguments))


def print_message(message: str, file_name: str, line_number: int):
    """
    Convenience function to prepend a file name and line number before printing

    :param message: The message to print
    :param file_name: The name of the file that generated this message
    :param line_number: The line number that generated this message
    """
    print(f'{colorful.violet(file_name)}:{colorful.blue(line_number)}: {message}')


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Compute an optimal deck given a set of booster packs')
    parser.add_argument('files', metavar='FILES', nargs='+', type=Path,
                        help='The .lp files to lint')

    args = parser.parse_args()

    # Since this program's purpose is to check predicate arity,
    # assume there is only one predicate per identifier
    predicate_signatures: Dict[str, Sequence[str]] = {}

    # Scan for predicate annotations
    for file_path in args.files:
        with open(file_path) as file:
            for line in file:
                predicate = match_predicate_annotation(line)
                if predicate:
                    predicate_signatures[predicate.name] = predicate.arguments

    # Scan codebase for violations
    violations_found = False
    for file_path in args.files:
        with open(file_path) as file:
            for line_number, predicate in match_predicates(file):
                actual_arity = len(predicate.arguments)
                try:
                    expected_arity = len(predicate_signatures[predicate.name])
                    assert expected_arity == actual_arity

                except KeyError:
                    # Missing annotation
                    predicate_signature = colorful.bold_yellow(f'{predicate.name}/{actual_arity}')
                    print_message(colorful.yellow(f'Missing annotation for {predicate_signature}'),
                                  file_name=file_path, line_number=line_number)
                    violations_found = True

                except AssertionError:
                    # Annotation violation
                    actual_signature = colorful.bold_red(f'{predicate.name}/{actual_arity}')
                    expected_signature = colorful.bold_red(f'{predicate.name}/{expected_arity}')
                    print_message(colorful.red(f'{actual_signature} should be {expected_signature}'),
                                  file_name=file_path, line_number=line_number)
                    violations_found = True

    if violations_found:
        sys.exit(1)
