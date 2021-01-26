#!/usr/bin/env python3
"""
4/26 Assignment
"""

from abc import abstractmethod
from typing import *

K = TypeVar('K')
V = TypeVar('V')


def memoize(funct: Callable[[K], V]) -> Callable[[K], V]:
    """
    Memoizes the given function. Works as a decorator::

        @memoize
        def foo(arg: K) -> V:
            pass  # Some work

    :param funct: The function to memoize
    :return: The memoized function
    """
    memos: Dict[K, V] = {}

    def decorator(*args: K) -> V:
        try:
            return memos[args]

        except KeyError:
            return_value = funct(*args)
            memos[args] = return_value
            return return_value

    return decorator


T = TypeVar('T')


class SequenceView(Sequence[T]):
    """
    An immutable view of a sequence.

    Prevents slices from creating additional objects in memory
    """
    def __init__(self, seq: Sequence[T], visible_indexes: Optional[range] = None):
        self.seq = seq

        if visible_indexes is None:
            visible_indexes = range(len(self.seq))

        self.visible_indexes = visible_indexes

    @overload
    @abstractmethod
    def __getitem__(self, i: int) -> T: ...

    @overload
    @abstractmethod
    def __getitem__(self, s: slice) -> 'SequenceView[T]': ...

    def __getitem__(self, i: int) -> T:
        if isinstance(i, slice):
            virtual_start, virtual_stop, virtual_step = i.start, i.stop, i.step

            actual_start, actual_stop, actual_step = \
                self.visible_indexes.start, self.visible_indexes.stop, self.visible_indexes.step

            if virtual_start is not None:
                if 0 <= virtual_start <= len(self):
                    # E.g. (1, 2, 3)[1:]
                    actual_start = self.visible_indexes[virtual_start]

                elif virtual_start < 0:
                    # E.g. (1, 2, 3)[-5:]
                    pass

                else:
                    # E.g. (1, 2, 3)[5:]
                    # TODO: Handle edge case with negative step (e.g. (1, 2, 3)[5:-5:-1])
                    actual_start = actual_stop

            if virtual_stop is not None:
                if 0 <= virtual_stop <= len(self):
                    # E.g. (1, 2, 3)[:1]
                    actual_stop = self.visible_indexes[virtual_stop]

                elif virtual_stop > len(self):
                    # E.g. (1, 2, 3)[:5]
                    pass

                else:
                    # E.g. (1, 2, 3)[:-5]
                    # TODO: Handle edge case with negative step (e.g. (1, 2, 3)[5:-5:-1])
                    actual_stop = actual_start

            if virtual_step is not None:
                actual_step *= virtual_step

            return SequenceView(self.seq, visible_indexes=range(actual_start, actual_stop, actual_step))

        else:
            return self.seq[self.visible_indexes[i]]

    def __len__(self) -> int:
        return len(self.visible_indexes)

    def __hash__(self) -> int:
        return hash((self.seq, self.visible_indexes))

    def __eq__(self, other):
        try:
            assert isinstance(other, SequenceView)
            assert self.seq is other.seq or self.seq == other.seq
            assert self.visible_indexes == other.visible_indexes
            return True

        except AssertionError:
            return False

    def __reversed__(self):
        return SequenceView(self.seq, visible_indexes=range(self.visible_indexes.stop, self.visible_indexes.start,
                            self.visible_indexes.step))

    def __repr__(self):
        return f'{self.__class__.__name__}(' \
            f'seq={repr(self.seq)}, ' \
            f'visible_indexes={repr(self.visible_indexes)})'


Money = int


@memoize
def maximum_robbing_potential(houses: Sequence[Money]) -> int:
    """
    Computes the maximum value a robber could steal, according to the problem definition.

    :param houses: The houses to consider
    :return: The maximum value
    """
    try:
        # 3+ houses

        # Manually unpack tuple to avoid generating a mutable list
        # Instead of:
        #   first_house, *other_houses = houses
        first_house, other_houses = houses[0], houses[1:]
        second_house, last_houses = other_houses[0], other_houses[1:]
        return max(first_house + maximum_robbing_potential(last_houses), maximum_robbing_potential(other_houses))

    except IndexError:
        try:
            # 2 houses
            first_house, last_house = houses
            return max(first_house, last_house)

        except ValueError:
            # 1 house
            only_house, = houses
            return only_house


if __name__ == '__main__':
    # Test case 1
    houses = SequenceView((2, 7, 9, 3, 1))
    expected_output = 12
    actual_output = maximum_robbing_potential(houses)

    print(f'Test case 1 ({"PASS" if actual_output == expected_output else "FAILS"}):')
    print(f'Expected: {expected_output}')
    print(f'Actual:   {actual_output}')
    print()

    # Test case 2
    houses = SequenceView((5, 6, 8, 2, 3, 6, 3, 7, 3, 8, 11, 2, 4, 7, 2, 13, 6, 9, 9, 2))
    expected_output = 68
    actual_output = maximum_robbing_potential(houses)

    print(f'Test case 2 ({"PASS" if actual_output == expected_output else "FAILS"}):')
    print(f'Expected: {expected_output}')
    print(f'Actual:   {actual_output}')
