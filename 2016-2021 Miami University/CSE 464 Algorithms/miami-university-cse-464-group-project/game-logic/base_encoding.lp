%%%% Input Predicates %%%%

% player(PlayerId).
% turn_order(PreviousPlayerId, NextPlayerId).
% set(SetId).
% card(CardId, SetId, Rarity).
% card_mana_cost(CardId, ManaColor, ManaCount).
% card_mana_cost_variable(CardId, ManaColor, VariableName).
% card_rule_exception(CardId, RuleNumber).

% land(CardId, Color).
% enchantment(CardId, TargetType).
% artifact(CardId).
% planeswalker(CardId, StartingLoyalty).
% planeswalker_action(CardId, LoyaltyEffect).
% creature(CardId, Power, Toughness).
% creature_ability(CardId, Ability).  % Such as flying, etc.
% sorcery(CardId).
% instant(CardId).

% card_archetype(CardId, Archetype).

% starting_deck(PlayerId, CardId, Quantity).

%%%% Game fact predicates %%%%

% mana_color(Color).
% rarity(Rarity).
% ability(Ability).
% archetype(Archetype).
% permanent(CardId).
% tappable(CardId).

%%%% Time tracking predicates %%%%

% turn(TurnNumber, ActivePlayerId).
% turn_phase(TurnNumber, TurnPhase).
% turn_phase_order(PreviousTurnPhase, NextTurnPhase).

%%%% Book keeping predicates (board state) %%%%

% life_count(TurnNumber, TurnPhase, PlayerId, LifeCount).
% planeswalker_loyalty(TurnNumber, TurnPhase, CardInstanceId, Loyalty).

% card_instance(CardInstanceId, CardId, OwnerPlayerId).
% card_present(TurnNumber, TurnPhase, CardInstanceId).
% card_controller(TurnNumber, TurnPhase, CardInstanceId, ControllerPlayerId).
% summoning_sickness(TurnNumber, TurnPhase, CardInstanceId).
% card_tapped(TurnNumber, TurnPhase, CardInstanceId).
% card_attacking(TurnNumber, TurnPhase, CardInstanceId).
% card_blocking(TurnNumber, TurnPhase, CardInstanceId).

%%%% Card pile predicates %%%%

% hand(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% deck(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% graveyard(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

%%%% Player action predicates %%%%

% untap_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% draw(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% mulligan(TurnNumber, TurnPhase, PlayerId).
% play_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% tap_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% declare_attacker(TurnNumber, TurnPhase, PlayerId, TargetId, CardInstanceId).
% declare_blocker(TurnNumber, TurnPhase, PlayerId, CardInstanceId).
% discard_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

%%%% Combat phase predicates %%%%

% combat_damage(TurnNumber, TurnPhase, CardInstanceId, Damage).
% card_dies(TurnNumber, TurnPhase, CardInstanceId).

%%%% Win condition predicates %%%%

% win(TurnNumber, TurnPhase, PlayerId).
% loss(TurnNumber, TurnPhase, PlayerId).

#program base.

%%%% Game facts %%%%

% The mana colors are as follows:
mana_color(any; white; blue; black; red; green; colorless; snow).

% The card rarities are as follows:
rarity(black; gray; yellow; red; green).

% Creature abilities are as follows:
ability(flying; reach; haste; vigilance).

% There are five different archetypes we can identify:
archetype(bomb; removal; advantage; evasion; win).

% Everything except a Sorcery and an instant is a permanent
permanent(CardId) :-
    card(CardId, _, _),
    land(CardId, _).

permanent(CardId) :-
    card(CardId, _, _),
    enchantment(CardId, _).

permanent(CardId) :-
    card(CardId, _, _),
    artifact(CardId).

permanent(CardId) :-
    card(CardId, _, _),
    planeswalker(CardId, _).

permanent(CardId) :-
    card(CardId, _, _),
    creature(CardId, _, _).

-permanent(CardId) :-
    card(CardId, _, _),
    sorcery(CardId).

-permanent(CardId) :-
    card(CardId, _, _),
    instant(CardId).

% Lands are tappable
tappable(CardId) :-
    land(CardId, _).

% Creatures are tappable
tappable(CardId) :-
    creature(CardId, _, _).

%%%% Time tracking %%%%

% A turn has five phases (beginning, pre_combat, combat, post_combat, ending)
turn_phase(TurnNumber, beginning; pre_combat; combat; post_combat; ending) :-
    turn(TurnNumber, _).

% Give order to turn phases (but not a total order)
turn_phase_order(beginning, pre_combat).
turn_phase_order(pre_combat, combat).
turn_phase_order(combat, post_combat).
turn_phase_order(post_combat, ending).

%%%% Book keeping: Beginning of turn %%%%

% Every player starts with 20 life
life_count(1, beginning, PlayerId, 20) :-
    turn(1, _),
    player(PlayerId).

% Every player's life count at the beginning of a turn is the same as it was at the end of the previous turn
% (Persistence property of life counts)
life_count(TurnNumber, beginning, PlayerId, LifeCount) :-
    turn(TurnNumber, _),
    life_count(TurnNumber - 1, ending, PlayerId, LifeCount).

% An untapped card from the end of the previous turn is still untapped at the start of the next turn
% (Persistence property of untapped cards)
-card_tapped(TurnNumber, beginning, CardInstanceId) :-
    turn(TurnNumber, _),
    turn_phase(TurnNumber, beginning),
    -card_tapped(TurnNumber - 1, ending, CardInstanceId).

% A tapped card from the end of the previous turn is still tapped at the start of the next turn,
% unless we have already derived it isn't
% (Persistence property of tapped cards)
card_tapped(TurnNumber, beginning, CardInstanceId) :-
    turn(TurnNumber, _),
    turn_phase(TurnNumber, beginning),
    card_tapped(TurnNumber - 1, ending, CardInstanceId),
    not -card_tapped(TurnNumber, beginning, CardInstanceId).

% Creatures recover from summoning sickness during the beginning phase of their controller's turn
-summoning_sickness(TurnNumber, beginning, CardInstanceId) :-
    card_present(TurnNumber, beginning, CardInstanceId),
    card_controller(TurnNumber, beginning, CardInstanceId, ControllerPlayerId),
    card_instance(CardInstanceId, CardId, _),
    creature(CardId, _, _),
    turn(TurnNumber, ActivePlayerId),
    ActivePlayerId = ControllerPlayerId.

%%%% Book keeping: Phase-to-phase %%%%

% Every player's life count is the same as the previous phase, unless derived to be otherwise
life_count(TurnNumber, NextTurnPhase, PlayerId, LifeCount) :-
    life_count(TurnNumber, PreviousTurnPhase, PlayerId, LifeCount),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    not life_count(TurnNumber, NextTurnPhase, PlayerId, _).

% Tapped cards from the previous phase are still tapped in the next phase
% Exception: combat phase
% TODO: Allow for exceptions
card_tapped(TurnNumber, NextTurnPhase, CardInstanceId) :-
    card_tapped(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    PreviousTurnPhase != combat.

% Tapped cards from the combat phase are still tapped in the next phase
% Exception: creatures with vigilance
card_tapped(TurnNumber, NextTurnPhase, CardInstanceId) :-
    card_tapped(TurnNumber, combat, CardInstanceId),
    turn_phase_order(combat, NextTurnPhase),
    card_instance(CardInstanceId, CardId, _),
    not creature_ability(CardId, vigilance).

% Untapped cards from the previous phase are still untapped in the next phase,
% unless derived otherwise
-card_tapped(TurnNumber, NextTurnPhase, CardInstanceId) :-
    -card_tapped(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    not card_tapped(TurnNumber, NextTurnPhase, CardInstanceId).

% Summoning sickness persists from the previous turn phase to the next
summoning_sickness(TurnNumber, NextTurnPhase, CardInstanceId) :-
    summoning_sickness(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase).

% Cards on the board during the previous turn phase stay on the board during the next,
% unless derived otherwise
card_present(TurnNumber, NextTurnPhase, CardInstanceId) :-
    card_present(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    not -card_present(TurnNumber, NextTurnPhase, CardInstanceId).

% Cards off the board during the previous turn phase stay off the board during the next,
% unless derived otherwise
-card_present(TurnNumber, NextTurnPhase, CardInstanceId) :-
    -card_present(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    not card_present(TurnNumber, NextTurnPhase, CardInstanceId).

% Cards in a player's hand stay in his hand from phase to phase,
% unless derived otherwise
hand(TurnNumber, NextTurnPhase, PlayerId, CardInstanceId) :-
    hand(TurnNumber, PreviousTurnNumber, PlayerId, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase),
    not -hand(TurnNumber, NextTurnPhase, PlayerId, CardInstanceId).

%%%% Book keeping: combat %%%%

% A card is tapped when it is declared as an attacker
card_tapped(TurnNumber, TurnPhase, CardInstanceId) :-
    declare_attacker(TurnNumber, TurnPhase, _, _, CardInstanceId).

% A blocking creature receives damage equal to the difference of
% his toughness to the attacker's power
combat_damage(TurnNumber, TurnPhase, CardInstanceId, Damage) :-
    declare_attacker(TurnNumber, TurnPhase, _, TargetId, AttackerCardInstanceId),
    declare_blocker(TurnNumber, TurnPhase, _, CardInstanceId),
    card_instance(AttackerCardInstanceId, AttackerCardId, _),
    card_instance(CardInstanceId, CardId, _),
    creature(AttackerCardId, Power, _),
    creature(CardId, _, Toughness),
    Damage = Toughness - Power.

% An attacking creature receives damage equal to the difference of
% his toughness to the blocker's power
combat_damage(TurnNumber, TurnPhase, CardInstanceId, Damage) :-
    declare_attacker(TurnNumber, TurnPhase, _, TargetId, CardInstanceId),
    declare_blocker(TurnNumber, TurnPhase, _, BlockerCardInstanceId),
    card_instance(CardInstanceId, CardId, _),
    card_instance(BlockerCardInstanceId, BlockerCardId, _),
    creature(BlockerCardId, Power, _),
    creature(CardId, _, Toughness),
    Damage = Toughness - Power.

% A creature dies when its combat damage is at least its toughness
card_dies(TurnNumber, TurnPhase, CardInstanceId) :-
    combat_damage(TurnNumber, TurnPhase, CardInstanceId, Damage),
    card_instance(CardInstanceId, CardId, _),
    creature(CardId, _, Toughness),
    Damage >= Toughness.

% When a card dies, it leaves the board in the next turn phase
-card_present(TurnNumber, NextTurnPhase, CardInstanceId) :-
    card_dies(TurnNumber, PreviousTurnPhase, CardInstanceId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase).

% When a card dies, it enters the graveyard in the next turn phase
graveyard(TurnNumber, NextTurnPhase, OwnerPlayerId, CardInstanceId) :-
    card_dies(TurnNumber, PreviousTurnPhase, CardInstanceId),
    card_instance(CardInstanceId, _, OwnerPlayerId),
    turn_phase_order(PreviousTurnPhase, NextTurnPhase).

%%%% Book keeping: player action %%%%

% Players' "draw" action adds the card to their hand
hand(TurnNumber, TurnPhase, PlayerId, CardInstanceId) :-
    draw(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

% Players' "draw" action removes the card from their deck
-deck(TurnNumber, TurnPhase, PlayerId, CardInstanceId) :-
    draw(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

% Players' "play_card" action removes the card from their hand
-hand(TurnNumber, TurnPhase, PlayerId, CardInstanceId) :-
    play_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

% Players' "play_card" action adds the card to the board, if it is a permanent
card_present(TurnNumber, TurnPhase, CardInstanceId) :-
    card_instance(CardInstanceId, CardId, _),
    permanent(CardId),
    play_card(TurnNumber, TurnPhase, _, CardInstanceId).

% Creatures have summoning sickness when they are played,
% unless they have haste
summoning_sickness(TurnNumber, TurnPhase, CardInstanceId) :-
    play_card(TurnNumber, TurnPhase, _, CardInstanceId),
    card_instance(CardInstanceId, CardId, _),
    creature(CardId, _, _),
    not creature_ability(CardId, haste).

% Players' "tap" action taps the card
card_tapped(TurnNumber, TurnPhase, CardInstanceId) :-
    tap_card(TurnNumber, TurnPhase, _, CardInstanceId).

% Players' "untap" action untaps the card
-card_tapped(TurnNumber, TurnPhase, CardInstanceId) :-
    untap_card(TurnNumber, TurnPhase, _, CardInstanceId).

% Players' "discard_card" action removes the card from his hand
-hand(TurnNumber, TurnPhase, PlayerId, CardInstanceId) :-
    discard_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

% Players' "discard_card" action adds the card to his graveyard
graveyard(TurnNumber, TurnPhase, PlayerId, CardInstanceId) :-
    discard_card(TurnNumber, TurnPhase, PlayerId, CardInstanceId).

%%%% Book keeping: next turn %%%%

% The next turn is played by the player who is after the previous player
turn(PreviousTurnNumber + 1, NextPlayerId) :-
    turn(PreviousTurnNumber, PreviousPlayerId),
    turn_order(PreviousPlayerId, NextPlayerId).

%%%% Player action %%%%

% Every player untaps all of his cards at the beginning of his turn
% This rule only untaps tapable cards (such as lands and creatures)
untap_card(TurnNumber, beginning, PlayerId, CardInstanceId) :-
    turn_phase(TurnNumber, beginning),
    turn(TurnNumber, ActivePlayerId),
    card_present(TurnNumber, beginning, CardInstanceId),
    card_controller(TurnNumber, beginning, CardInstanceId, ControllerPlayerId),
    card_instance(CardInstanceId, CardId, _),
    tappable(CardId),
    ActivePlayerId = ControllerPlayerId,
    PlayerId = ActivePlayerId.

% A player draws a card from this deck during the beginning phase of his turn
1 { draw(TurnNumber, beginning, ActivePlayerId, CardInstanceId) :
        deck(TurnNumber, beginning, ActivePlayerId, CardInstanceId) } 1 :-
    turn_phase(TurnNumber, beginning),
    turn(TurnNumber, ActivePlayerId).

% A player may play cards from his hand during the pre_combat phase of his turn
% or the post_combat phase of his turn
{ play_card(TurnNumber, TurnPhase, ActivePlayerId, CardInstanceId) :
        hand(TurnNumber, TurnPhase, ActivePlayerId, CardInstanceId) } :-
    turn_phase(TurnNumber, TurnPhase),
    TurnPhase = (pre_combat; post_combat),
    turn(TurnNumber, ActivePlayerId).

% TODO: A player may play an enchantment card from his hand during the pre_combat phase of his turn

% TODO: A player may utilize planswalkers under his control during the pre_combat phase of his turn

% A player selects creatures to attack another player during the combat phase of his turn
{ declare_attacker(TurnNumber, combat, ActivePlayerId, TargetId, CardInstanceId) :
        player(TargetId),
        TargetId != ActivePlayerId,
        card_present(TurnNumber, combat, CardInstanceId),
        card_controller(TurnNumber, combat, CardInstanceId, ActivePlayerId),
        card_instance(CardInstanceId, CardId, _),
        creature(CardId, _, _) } :-
    turn_phase(TurnNumber, combat),
    turn(TurnNumber, ActivePlayerId).

% A player can select creatures to block when attacked by another player during their turn
{ declare_blocker(TurnNumber, TurnPhase, BlockerPlayerId, CardInstanceId) :
        card_present(TurnNumber, TurnPhase, CardInstanceId),
        -card_tapped(TurnNumber, TurnPhase, CardInstanceId),
        card_controller(TurnNumber, TurnPhase, CardInstanceId, BlockerPlayerId),
        card_instance(CardInstanceId, CardId, _),
        creature(CardId, _, _) } :-
    declare_attacker(TurnNumber, TurnPhase, ActivePlayerId, _, _),
    BlockerPlayerId != ActivePlayerId.

% A player discards any excess of seven cards from his hand during the ending phase of his turn
ExcessCards { discard_card(TurnNumber, ending, PlayerId, CardInstanceId) :
        hand(TurnNumber, ending, PlayerId, CardInstanceId) } ExcessCards :-
    turn_phase(TurnNumber, ending),
    turn(TurnNumber, PlayerId),
    TotalCards = #count { 1, CardInstanceId :
        hand(TurnNumber, ending, PlayerId, CardInstanceId) },
    ExcessCards = TotalCards - 7,
    ExcessCards > 0.

%%%% Win conditions %%%%

% A player losses if he does not have any cards to draw at the beginning of his turn
loss(TurnNumber, beginning, ActivePlayerId) :-
    turn(TurnNumber, ActivePlayerId),
    not deck(TurnNumber, beginning, _, _).

%%%% Integrity constraints %%%%

% A card cannot be in both a player's hand and deck at the same time
:-
    hand(TurnNumber, TurnPhase, _, CardInstanceId),
    deck(TurnNumber, TurnPhase, _, CardInstanceId).

% A card cannot be in both a player's hand and graveyard at the same time
:-
    hand(TurnNumber, TurnPhase, _, CardInstanceId),
    graveyard(TurnNumber, TurnPhase, _, CardInstanceId).

% A card cannot be in both a player's deck and graveyard at the same time
:-
    deck(TurnNumber, TurnPhase, _, CardInstanceId),
    graveyard(TurnNumber, TurnPhase, _, CardInstanceId).
