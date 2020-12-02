#program base.

player(us; them).
% They go after us
turn_order(us, them).
% We go after them
turn_order(them, us).

turn(1, us).

set("RNA").

card(red_land, "RNA", black).
card(black_land, "RNA", black).
land(red_land, red).
land(black_land, black).

card(creature_a, "RNA", black).
card_mana_cost(creature_a, red, 1).
creature(creature_a, 1, 1).

card(creature_b, "RNA", black).
card_mana_cost(creature_b, black, 1).
creature(creature_b, 2, 1).
creature_ability(creature_b, flying).

starting_deck(us, creature_a, 2).
starting_deck(us, creature_b, 2).
starting_deck(us, black_land, 2).
starting_deck(us, red_land, 2).

starting_deck(them, creature_a, 2).
starting_deck(them, creature_b, 2).
starting_deck(them, black_land, 2).
starting_deck(them, red_land, 2).
