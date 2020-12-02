#!/usr/bin/env python3

"""
To run tests with ``pytest``, run::

    cd src/
    pytest

To run tests without ``pytest``, run::

    cd src/
    python3 test_algorithm.py

"""

import csv
import logging
from collections import defaultdict
from typing import *

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


from yaml import safe_load

from algorithm import Deck, CardId, Rarity, generate_booster_pack, summarize_deck, evaluate_deck, parse_cards_csv, basic_land_info


# Describe test case schema
class TestCard(NamedTuple):
    set: str
    card_number: int
    quantity: int = 1


class TestCase(NamedTuple):
    cards: Sequence[TestCard]
    expected_performance: str


# Document root
class TestFileSchema(NamedTuple):
    cases: Sequence[TestCase]


def load_card_csv():
    # Load in CSV
    with open('RNA.csv') as cards_csv:
        cards_csv: Iterator[List[str]] = csv.reader(cards_csv)
        _ = next(cards_csv)  # Skip header row
        set_infos = parse_cards_csv(cards_csv)

    set_infos.update({
        None: basic_land_info,
    })

    return set_infos


def test_generate_booster_packs():
    set_infos = load_card_csv()
    for mtg_set in set_infos.keys():
        if mtg_set is None:
            continue

        booster_pack = tuple(generate_booster_pack(set_infos[mtg_set]))
        assert len(booster_pack) == 14
        card_rarities: Counter[Rarity] = Counter(set_infos[mtg_set].cards[card_number].rarity
                                                 for card_number in booster_pack)
        assert 9 <= card_rarities[Rarity.COMMON] <= 10
        assert 3 <= card_rarities[Rarity.UNCOMMON] <= 4
        assert (1 <= card_rarities[Rarity.RARE] <= 2) ^ (1 <= card_rarities[Rarity.MYTHIC_RARE] <= 2)


def test_evaluate_deck():
    set_infos = load_card_csv()

    for test_number, (expected_performance, test_deck) in enumerate(load_test_cases(), start=1):
        print(colorful.dimmed_blue(f'Evaluating test deck {test_number} ') +
              colorful.cyan(f'(Expected to be "{expected_performance}")'))
        deck_summary = summarize_deck(test_deck, set_infos=set_infos)
        print(f'Deck summary: {colorful.yellow(deck_summary)}')
        penalties = evaluate_deck(deck_summary)
        print(f'Evaluation: {colorful.orange(penalties)}')
        print(f'Total Penalty: {colorful.bold_red(sum(penalties))}')
        print()


# noinspection PyArgumentList
def load_test_cases() -> Iterator[Deck]:
    # Read in test cases
    with open('test_algorithm_cases.yml') as file:
        file = safe_load(file)
        file = TestFileSchema(**file)
        for test_case in file.cases:
            test_case = TestCase(**test_case)

            deck: DefaultDict[CardId] = defaultdict(int)
            for card in test_case.cards:
                card = TestCard(**card)
                deck[card.set, card.card_number] += card.quantity

            yield test_case.expected_performance, deck


if __name__ == '__main__':
    # Run tests without pytest
    logging.basicConfig(level=logging.DEBUG)

    test_evaluate_deck()
