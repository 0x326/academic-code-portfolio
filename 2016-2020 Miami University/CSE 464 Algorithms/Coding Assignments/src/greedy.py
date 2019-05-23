#!/usr/bin/env python3
"""
2019-04-10 Greedy Algorithm
"""

from typing import NamedTuple, List, Iterable

from sorting import combo_sorted


class Activity(NamedTuple):
    start_time: int
    end_time: int


def select_activities(activities: List[Activity]) -> Iterable[Activity]:
    """
    Given a list of activity start and end times,
    select the maximum number of activities that can be performed by a single person, one at a time.

    :param activities: The activities to choose from
    :return: The activities chosen
    """
    activities = combo_sorted(activities, key=lambda activity: activity.end_time)
    current_time = 0
    for activity in filter(lambda item: item.start_time >= current_time, activities):
        yield activity
        current_time = activity.end_time


if __name__ == '__main__':
    # Test case 1
    start_times = (1, 3, 0, 5, 8, 5)
    end_times = (2, 4, 6, 7, 9, 9)
    activities = [Activity(start_time=start, end_time=end) for start, end in zip(start_times, end_times)]
    expected_activities = activities[0], activities[1], activities[3], activities[4]
    activities = tuple(select_activities(activities))

    print(f'Test case 1 ({"PASS" if activities == expected_activities else "FAILS"}):')
    print(f'Expected: {expected_activities}')
    print(f'Actual:   {activities}')
    print()

    # Test case 2
    start_times = (10, 12, 30)
    end_times = (20, 25, 30)
    activities = [Activity(start_time=start, end_time=end) for start, end in zip(start_times, end_times)]
    expected_activities = activities[0], activities[2]
    activities = tuple(select_activities(activities))

    print(f'Test case 2 ({"PASS" if activities == expected_activities else "FAILS"}):')
    print(f'Expected: {expected_activities}')
    print(f'Actual:   {activities}')
