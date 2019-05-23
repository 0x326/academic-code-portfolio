#!/usr/bin/env python3
"""
Requires ``hypothesis``
"""

from typing import List, Iterable

from hypothesis import given, strategies as strats

from sorting import insertion_sorted, merge_sorted, combo_sorted


@given(strats.lists(strats.integers()))
def test_insertion_sorted(array: List[int]):
    assert insertion_sorted(array) == sorted(array)


@given(strats.iterables(strats.integers()), strats.iterables(strats.integers()))
def test_merge_sorted(array1: Iterable[int], array2: Iterable[int]):
    array1 = sorted(array1)
    array2 = sorted(array2)

    sorted_array = list(merge_sorted(array1, array2))
    assert sorted_array == sorted(sorted_array)


@given(strats.lists(strats.integers()), strats.integers(min_value=0))
def test_combo_sorted(array: List[int], block_size: int):
    sorted_array = list(combo_sorted(array, block_size=block_size))

    assert sorted_array == sorted(array)


if __name__ == '__main__':
    test_insertion_sorted()
    test_merge_sorted()
    test_combo_sorted()
