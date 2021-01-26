#!/usr/bin/env python3
"""
2019-02-11 Sorting Assignment
"""

import argparse
import random
from collections import deque
from typing import Iterable, Sequence, Deque, List, Union, Callable, TypeVar


T = TypeVar('V')


def insertion_sorted(array: Union[Sequence[T], List[T]], in_place=True,
                     key: Callable[[T], int] = lambda item: item) -> Iterable[T]:
    """
    Performs an insertion sort on a list. Can either be in-place or out-of-place

    :param array: The array to sort
    :param in_place: Whether to perform the sort in place
    :param key: A function to extract the value each element is to be sorted by
    :return: The sorted array
    """
    if len(array) <= 1:
        return array

    if in_place:
        # Use non-Pythonic for loop for low-level insertion sort
        # First item is already sorted
        for index in range(1, len(array)):
            item = array[index]
            item_key = key(item)
            for sorted_index in range(0, index):
                sorted_item_key = key(array[sorted_index])

                if item_key < sorted_item_key:
                    array.pop(index)
                    array.insert(sorted_index, item)
                    break

        return array

    else:
        sorted_list: Deque[int] = deque(maxlen=len(array))
        for item in array:
            item_key = key(item)
            for sorted_index, sorted_item_key in enumerate(map(key, sorted_list)):
                if item_key < sorted_item_key:
                    sorted_list.insert(sorted_index, item)
                    break

        return sorted_list


def merge_sorted(iter1: Iterable[T], iter2: Iterable[T], key: Callable[[T], int] = lambda item: item) -> Iterable[T]:
    """
    Merges two sorted iterables while maintaining sorted order

    :param iter1: A sorted iterable
    :param iter2: Another sorted iterables
    :param key: A function to extract the value each element is to be sorted by
    :return: A single sorted iterable
    """
    iter1 = iter(iter1)
    iter2 = iter(iter2)

    item1 = None
    item2 = None

    try:
        item1 = next(iter1)
        item1_key = key(item1)
        item2 = next(iter2)
        item2_key = key(item2)

        while True:
            if item1_key < item2_key:
                yield item1
                item1 = None
                item1 = next(iter1)
                item1_key = key(item1)

            else:
                yield item2
                item2 = None
                item2 = next(iter2)
                item2_key = key(item2)

    except StopIteration:
        # Don't forget an item from a non-empty iterable
        if item1 is not None:
            yield item1
        elif item2 is not None:
            yield item2

        # We can now just empty them out
        yield from iter1
        yield from iter2
        return


def combo_sorted(array: List[T], block_size: int = 32, key: Callable[[T], int] = lambda item: item) -> Iterable[T]:
    """
    A combination of insertion sort and merge sort. When length is below ``block_size``, uses insertion sort.
    Otherwise, merge sort.

    :param array: The list to sort
    :param block_size: The maximum threshold for insertion sort
    :param key: A function to extract the value each element is to be sorted by
    :return: A sorted iterable
    """
    if len(array) <= 1:
        yield from array
        return

    if len(array) < block_size:
        yield from insertion_sorted(array, key=key)

    else:
        # Divide and conquer
        sorted_left = combo_sorted(array[:len(array) // 2], block_size=block_size, key=key)
        sorted_right = combo_sorted(array[len(array) // 2:], block_size=block_size, key=key)
        yield from merge_sorted(sorted_left, sorted_right, key=key)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Sort a random array')
    parser.add_argument('array_length', metavar='N', type=int, nargs='?', default=320,
                        help='Length of the array to generate')
    parser.add_argument('--block-size', metavar='R', type=int, default=32,
                        help='Length of the array to generate')
    parser.add_argument('--min-value', metavar='R', type=int, default=0,
                        help='Minimum value for random array (inclusive)')
    parser.add_argument('--max-value', metavar='R', type=int, default=5000,
                        help='Maximum value for random array (inclusive)')
    args = parser.parse_args()

    for _ in range(20):
        # Generate array
        array = [random.randint(args.min_value, args.max_value) for _ in range(args.array_length)]

        # Sort array
        expected_array = tuple(sorted(array))
        array = tuple(combo_sorted(array, block_size=args.block_size))

        if array != expected_array:
            print(f'Expected: {repr(expected_array)}')
            print(f'Actual: {repr(array)}')
            print()
            break

    else:
        print('All tests pass')
