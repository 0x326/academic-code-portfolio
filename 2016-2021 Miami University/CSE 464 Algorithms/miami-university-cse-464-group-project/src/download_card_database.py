#!/usr/bin/env python3

from pathlib import Path
from typing import Mapping, Any

from mtgsdk import Card
from yaml import safe_load, safe_dump


def generate_card_entry(card: Card) -> Mapping[str, Any]:
    entry = {
        'Name': card.name,
        'Rarity': card.rarity,
        'CMC': int(card.cmc),
        'Mana cost': card.mana_cost,
        'Image URL': card.image_url,
    }

    card_type: str = card.types[0]
    card_type = card_type.capitalize()
    entry.update({
        'Type': card_type,
    })

    if 'Land' in card_type:
        entry.update({
            # Lands-only
            'Land colors': card.color_identity,
        })

    elif 'Enchantment' in card_type:
        pass

    elif 'Artifact' in card_type:
        pass

    elif 'Planeswalker' in card_type:
        pass

    elif 'Creature' in card_type:
        try:
            entry.update({
                'Creature power': int(card.power),
            })
        except ValueError:
            pass

        try:
            entry.update({
                'Creature toughness': int(card.toughness),
            })
        except ValueError:
            pass

    elif 'Sorcery' in card_type:
        pass

    elif 'Instant' in card_type:
        pass

    return entry


if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Download a local copy of the MTG database')
    parser.add_argument('set', metavar='SET_CODE', type=str,
                        help='The code of the set to download')
    parser.add_argument('--db-file', '-o', metavar='DATABASE_FILE', type=Path, default='card_db.yml',
                        help='The code of the set to download')
    args = parser.parse_args()

    with open(args.db_file) as in_file:
        card_db = safe_load(in_file)
        try:
            assert isinstance(card_db, dict)

        except AssertionError:
            card_db = {}

    with open(args.db_file, 'w') as out_file:
        mtg_set: str = args.set.upper()

        card_db.update({
            mtg_set: {
                'Cards': {int(card.number): generate_card_entry(card)
                          for card in Card.where(set=mtg_set, language='English').iter()},
            },
        })

        safe_dump(card_db, out_file, indent=2, width=120)
