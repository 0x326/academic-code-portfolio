#!/usr/bin/env python3
"""
2019-03-01 Hashing Assignment
"""

import logging
from itertools import chain
from typing import NamedTuple, List, Iterator


class Entry(NamedTuple):
    present_as_positive_value: bool
    present_as_negative_value: bool


class TrivialSet:
    def __init__(self, max_range: int):
        self.array: List[Entry] = [Entry(present_as_positive_value=False, present_as_negative_value=False)
                                   for _ in range(max_range + 1)]

    def add(self, item):
        present_as_positive_value, present_as_negative_value = self.array[item]
        if item >= 0:
            present_as_positive_value = True
        else:
            present_as_negative_value = True
        self.array[abs(item)] = Entry(present_as_positive_value, present_as_negative_value)

    def discard(self, item):
        present_as_positive_value, present_as_negative_value = self.array[item]
        if item >= 0:
            present_as_positive_value = False
        else:
            present_as_negative_value = False
        self.array[abs(item)] = Entry(present_as_positive_value, present_as_negative_value)

    def __contains__(self, item):
        if item >= 0:
            return self.array[abs(item)].present_as_positive_value
        else:
            return self.array[abs(item)].present_as_negative_value

    def __iter__(self) -> Iterator[int]:
        negative_items = (-index for index, entry in enumerate(self.array) if entry.present_as_negative_value)
        positive_items = (index for index, entry in enumerate(self.array) if entry.present_as_positive_value)

        return chain(negative_items, positive_items)

    def __str__(self):
        return '{%s}' % ', '.join(map(str, iter(self)))

    def __repr__(self):
        return f'{self.__class__.__name__}({str(self)})'


if __name__ == '__main__':
    import argparse
    import sys
    from random import randint

    parser = argparse.ArgumentParser(description='Sort a random array')
    parser.add_argument('--max-range', metavar='MAX', type=int, default=10)
    parser.add_argument('--test-count', metavar='MAX', type=int, default=10)
    args = parser.parse_args()

    logging.basicConfig(level=logging.DEBUG, stream=sys.stdout)

    trivial_set = TrivialSet(max_range=args.max_range)

    for _ in range(args.max_range):
        num = randint(-args.max_range, args.max_range)
        logging.debug(f'Adding value: {num}')
        trivial_set.add(num)
        logging.debug(f'Set after add: {trivial_set}')
