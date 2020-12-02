% Puzzle X775RR from https://www.printable-puzzles.com

pieces([250, 500, 750, 1000, 1250, 1500, 1750]).
companies([astury, buralde, chaurd, denlend, furoth, rynir, sonaco]).
themes([autumn_leaves, city_skyline, coral_reef, football, outer_space, postage_stamp, rustic_village]).
years([1978, 1981, 1982, 1985, 1988, 1992, 1998]).

% Rule 1: The jigsaw puzzle made by Chaurd has
%   750 more pieces than the puzzle released in 1992.
%
% (Company1 = chaurd) ^ (Year2 = 1992) -> (Pieces1 = Pieces2 + 750)
% ¬[(Company1 = chaurd) ^ (Year2 = 1992)] V (Pieces1 = Pieces2 + 750)
% (Company1 != chaurd) V (Year2 != 1992)] V (Pieces1 = Pieces2 + 750)
rule1(Pieces1, Company1, Pieces2, Year2) :-
    not(Company1 = chaurd);
    not(Year2 = 1992);
    Pieces1 is Pieces2 + 750.

% Rule 2: The puzzle made by Astury doesn't have the football theme.
%
% (Company = Astury) -> (Theme != football)
% ¬(Company = Astury) V (Theme != football)
rule2(Company, Theme) :-
    not(Company = astury);
    not(Theme = football).

% Rule 3: The jigsaw puzzle made by Denlend has the autumn leaves theme.
%
% (Company = Denlend) -> (Theme = autumn_leaves)
% ¬(Company = Denlend) V (Theme = autumn_leaves)
rule3(Company, Theme) :-
    not(Company = denlend);
    Theme = autumn_leaves.

% Rule 4: The jigsaw puzzle released in 1992 has 750 fewer pieces
%   than the jigsaw puzzle with the outer space theme.
%
% (Year1 = 1992) ^ (Theme2 = outer_space) -> (Pieces1 = Pieces2 - 750)
% ¬[(Year1 = 1992) ^ (Theme2 = outer_space)] V (Pieces1 = Pieces2 - 750)
% (Year1 != 1992) V (Theme2 != outer_space) V (Pieces1 = Pieces2 - 750)
rule4(Pieces1, Year1, Pieces2, Theme2) :-
    not(Year1 = 1992);
    not(Theme2 = outer_space);
    Pieces1 is Pieces2 - 750.

% Rule 5: Of the jigsaw puzzle released in 1988 and the jigsaw puzzle with 250 pieces,
%   one has the rustic village theme and the other was made by Denlend.
%
% (Year1 = 1988) ^ (Pieces2 = 250) -> [(Theme1 = rustic_village) ^ (Company2 = Denlend)] V
%                                     [(Theme2 = rustic_village) ^ (Company1 = Denlend)]
% ¬[(Year1 = 1988) ^ (Pieces2 = 250)] V [(Theme1 = rustic_village) ^ (Company2 = Denlend)] V
%                                       [(Theme2 = rustic_village) ^ (Company1 = Denlend)]
% (Year1 != 1988) V (Pieces2 != 250) V [(Theme1 = rustic_village) ^ (Company2 = Denlend)] V
%                                      [(Theme2 = rustic_village) ^ (Company1 = Denlend)]
rule5(Company1, Theme1, Year1, Pieces2, Company2, Theme2) :-
    not(Year1 = 1988);
    not(Pieces2 = 250);
    (Theme1 = rustic_village, Company2 = denlend);
    (Theme2 = rustic_village, Company1 = denlend).

% Rule 6: The jigsaw puzzle made by Chaurd has 1250 pieces.
%
% (Company = Chaurd) -> (Pieces = 1250)
% ¬(Company = Chaurd) V (Pieces = 1250)
rule6(Pieces, Company) :-
    not(Company = chaurd);
    Pieces = 1250.

% Rule 7: The jigsaw puzzle with the autumn leaves theme is either
%   the puzzle with 1000 pieces or the jigsaw puzzle with 1250 pieces.
%
% (Theme = autumn_leaves) -> (Pieces = 1000) V (Pieces = 1250)
% ¬(Theme = autumn_leaves) V (Pieces = 1000) V (Pieces = 1250)
rule7(Pieces, Theme) :-
    not(Theme = autumn_leaves);
    Pieces = 1000;
    Pieces = 1250.

% Rule 8: The puzzle released in 1982 has 500 fewer pieces than the puzzle made by Furoth.
%
% (Year1 = 1982) ^ (Company2 = furoth) -> (Pieces1 = Pieces2 - 500)
% ¬[(Year1 = 1982) ^ (Company2 = furoth)] V (Pieces1 = Pieces2 - 500)
% (Year1 != 1982) V (Company2 != furoth) V (Pieces1 = Pieces2 - 500)
rule8(Pieces1, Year1, Pieces2, Company2) :-
    not(Year1 = 1982);
    not(Company2 = furoth);
    Pieces1 is Pieces2 - 500.

% Rule 9: The jigsaw puzzle with 500 pieces is either the jigsaw puzzle made by Astury
%   or the puzzle released in 1978.
%
% (Pieces = 500) -> (Company = astury) V (Year = 1978)
% ¬(Pieces = 500) V (Company = astury) V (Year = 1978)
rule9(Pieces, Company, Year) :-
    not(Pieces = 500);
    Company = astury;
    Year = 1978.

% Rule 10: Of the jigsaw puzzle made by Buralde and the jigsaw puzzle with 750 pieces,
%   one was released in 1985 and the other has the postage stamp theme.
%
% (Company1 = buralde) ^ (Pieces2 = 750) -> [(Year1 = 1985) ^ (Theme2 = stamp)] V [(Year2 = 1985) ^ (Theme1 = stamp)]
% ¬[(Company1 = buralde) ^ (Pieces2 = 750)] V [(Year1 = 1985) ^ (Theme2 = stamp)] V [(Year2 = 1985) ^ (Theme1 = stamp)]
% (Company1 != buralde) V (Pieces2 != 750) V [(Year1 = 1985) ^ (Theme2 = stamp)] V [(Year2 = 1985) ^ (Theme1 = stamp)]
rule10(Company1, Theme1, Year1, Pieces2, Theme2, Year2) :-
    not(Company1 = buralde);
    not(Pieces2 = 750);
    (Year1 = 1985, Theme2 = postage_stamp);
    (Year2 = 1985, Theme1 = postage_stamp).

% Rule 11: The jigsaw puzzle with the coral reef theme has somewhat more than the puzzle released in 1978.
%
% (Theme1 = coral_reef) ^ (Year2 = 1978) -> (Pieces1 > Pieces2)
% ¬[(Theme1 = coral_reef) ^ (Year2 = 1978)] V (Pieces1 > Pieces2)
% (Theme1 != coral_reef) V (Year2 != 1978) V (Pieces1 > Pieces2)
rule11(Pieces1, Theme1, Pieces2, Year2) :-
    not(Theme1 = coral_reef);
    not(Year2 = 1978);
    Pieces1 > Pieces2.

% Rule 12: The puzzle released in 1978 has somewhat fewer than the jigsaw puzzle made by Denlend.
%
% (Year1 = 1978) ^ (Company2 = Denlend) -> (Pieces1 < Pieces2)
% ¬[(Year1 = 1978) ^ (Company2 = Denlend)] V (Pieces1 < Pieces2)
% (Year1 != 1978) V (Company2 != Denlend) V (Pieces1 < Pieces2)
rule12(Pieces1, Year1, Pieces2, Company2) :-
    not(Year1 = 1978);
    not(Company2 = denlend);
    Pieces1 < Pieces2.

% Rule 13: The jigsaw puzzle released in 1985 doesn't have the coral reef theme.
%
% (Year = 1985) -> (Theme != coral_reef)
% ¬(Year = 1985) V (Theme != coral_reef)
rule13(Theme, Year) :-
    not(Year = 1985);
    not(Theme = coral_reef).

% Rule 14: The puzzle made by Rynir has 1750 pieces.
%
% (Company = rynir) -> (Pieces = 1750)
% ¬(Company = rynir) V (Pieces = 1750)
rule14(Pieces, Company) :-
    not(Company = rynir);
    Pieces = 1750.

% Rule 15: The puzzle with the outer space theme has somewhat fewer than the puzzle released in 1998.
%
% (Theme1 = outer_space) ^ (Year2 = 1998) -> (Pieces1 < Pieces2)
% ¬[(Theme1 = outer_space) ^ (Year2 = 1998)] V (Pieces1 < Pieces2)
% (Theme1 != outer_space) V (Year2 != 1998)] V (Pieces1 < Pieces2)
rule15(Pieces1, Theme1, Pieces2, Year2) :-
    not(Theme1 = outer_space);
    not(Year2 = 1998);
    Pieces1 < Pieces2.

rule1_compliant(X) :-
    forall((member([Pieces1, Company1, _, _], X),
            member([Pieces2, _, _, Year2], X)),
        rule1(Pieces1, Company1, Pieces2, Year2)).

rule2_compliant(X) :-
    forall(member([_, Company, Theme, _], X),
        rule2(Company, Theme)).

rule3_compliant(X) :-
    forall(member([_, Company, Theme, _], X),
        rule3(Company, Theme)).

rule4_compliant(X) :-
    forall((member([Pieces1, _, _, Year1], X),
            member([Pieces2, _, Theme2, _], X)),
        rule4(Pieces1, Year1, Pieces2, Theme2)).

rule5_compliant(X) :-
    forall((member([_, Company1, Theme1, Year1], X),
            member([Pieces2, Company2, Theme2, _], X)),
        rule5(Company1, Theme1, Year1, Pieces2, Company2, Theme2)).

rule6_compliant(X) :-
    forall(member([Pieces, Company, _, _], X),
        rule6(Pieces, Company)).

rule7_compliant(X) :-
    forall(member([Pieces, _, Theme, _], X),
        rule7(Pieces, Theme)).

rule8_compliant(X) :-
    forall((member([Pieces1, _, _, Year1], X),
            member([Pieces2, Company2, _, _], X)),
        rule8(Pieces1, Year1, Pieces2, Company2)).

rule9_compliant(X) :-
    forall(member([Pieces, Company, _, Year], X),
        rule9(Pieces, Company, Year)).

rule10_compliant(X) :-
    forall((member([_, Company1, Theme1, Year1], X),
            member([Pieces2, _, Theme2, Year2], X)),
        rule10(Company1, Theme1, Year1, Pieces2, Theme2, Year2)).

rule11_compliant(X) :-
    forall((member([Pieces1, _, Theme1, _], X),
            member([Pieces2, _, _, Year2], X)),
        rule11(Pieces1, Theme1, Pieces2, Year2)).

rule12_compliant(X) :-
    forall((member([Pieces1, _, _, Year1], X),
            member([Pieces2, Company2, _, _], X)),
        rule12(Pieces1, Year1, Pieces2, Company2)).

rule13_compliant(X) :-
    forall(member([_, _, Theme, Year], X),
        rule13(Theme, Year)).

rule14_compliant(X) :-
    forall(member([Pieces, Company, _, _], X),
        rule14(Pieces, Company)).

rule15_compliant(X) :-
    forall((member([Pieces1, _, Theme1, _], X),
            member([Pieces2, _, _, Year2], X)),
        rule15(Pieces1, Theme1, Pieces2, Year2)).

rule_compliant(X) :-
    % Sorted by single-row comparisons
    rule2_compliant(X),
    rule3_compliant(X),
    rule6_compliant(X),
    rule7_compliant(X),
    rule9_compliant(X),
    rule13_compliant(X),
    rule14_compliant(X),

    % Two-row comparisons
    rule1_compliant(X),
    rule4_compliant(X),
    rule5_compliant(X),
    rule8_compliant(X),
    rule10_compliant(X),
    rule11_compliant(X),
    rule12_compliant(X),
    rule15_compliant(X),

    % Print solution
    write(X),
    nl.

rule_compliant_threaded(X) :-
    % Sorted by single-row comparisons
    thread_create(rule2_compliant(X), ThreadId2),
    thread_create(rule3_compliant(X), ThreadId3),
    thread_create(rule6_compliant(X), ThreadId6),
    thread_create(rule7_compliant(X), ThreadId7),
    thread_create(rule9_compliant(X), ThreadId9),
    thread_create(rule13_compliant(X), ThreadId13),
    thread_create(rule14_compliant(X), ThreadId14),

    % Two-row comparisons
    thread_create(rule1_compliant(X), ThreadId1),
    thread_create(rule4_compliant(X), ThreadId4),
    thread_create(rule5_compliant(X), ThreadId5),
    thread_create(rule8_compliant(X), ThreadId8),
    thread_create(rule10_compliant(X), ThreadId10),
    thread_create(rule11_compliant(X), ThreadId11),
    thread_create(rule12_compliant(X), ThreadId12),
    thread_create(rule15_compliant(X), ThreadId15),

    thread_join(ThreadId1, ThreadStatus1),
    thread_join(ThreadId2, ThreadStatus2),
    thread_join(ThreadId3, ThreadStatus3),
    thread_join(ThreadId4, ThreadStatus4),
    thread_join(ThreadId5, ThreadStatus5),
    thread_join(ThreadId6, ThreadStatus6),
    thread_join(ThreadId7, ThreadStatus7),
    thread_join(ThreadId8, ThreadStatus8),
    thread_join(ThreadId9, ThreadStatus9),
    thread_join(ThreadId10, ThreadStatus10),
    thread_join(ThreadId11, ThreadStatus11),
    thread_join(ThreadId12, ThreadStatus12),
    thread_join(ThreadId13, ThreadStatus13),
    thread_join(ThreadId14, ThreadStatus14),
    thread_join(ThreadId15, ThreadStatus15),

    ThreadStatus1 = true,
    ThreadStatus2 = true,
    ThreadStatus3 = true,
    ThreadStatus4 = true,
    ThreadStatus5 = true,
    ThreadStatus6 = true,
    ThreadStatus7 = true,
    ThreadStatus8 = true,
    ThreadStatus9 = true,
    ThreadStatus10 = true,
    ThreadStatus11 = true,
    ThreadStatus12 = true,
    ThreadStatus13 = true,
    ThreadStatus14 = true,
    ThreadStatus15 = true,

    % Print solution
    write(X),
    nl.

candidate_solution(X) :-
    % Instantiate the lists of names, fruits, etc.
    pieces(Pieces),
    companies(Companies),
    themes(Themes),
    years(Years),

    % Generate all possible solutions (brute force)
    [P1, P2, P3, P4, P5, P6, P7] = Pieces,
    permutation([C1, C2, C3, C4, C5, C6, C7], Companies),
    permutation([T1, T2, T3, T4, T5, T6, T7], Themes),
    permutation([Y1, Y2, Y3, Y4, Y5, Y6, Y7], Years),

    % Format the variables as solutions table
    X = [[P1, C1, T1, Y1],
         [P2, C2, T2, Y2],
         [P3, C3, T3, Y3],
         [P4, C4, T4, Y4],
         [P5, C5, T5, Y5],
         [P6, C6, T6, Y6],
         [P7, C7, T7, Y7]].

solution(X) :-
    candidate_solution(X),
    rule_compliant(X).

solution_threaded(X) :-
    candidate_solution(X),
    thread_create(rule_compliant(X), _).

% :- forall(solution_threaded(X), forall(member(Element, X),
%            (write(Element), nl))),
%            nl, write("==== End of solutions ===="), nl, nl.

% :- rule_compliant([
%     [250, sonaco, rustic_village, 1982],
%     [500, astury, city_skyline, 1992],
%     [750, furoth, postage_stamp, 1978],
%     [1000, denlend, autumn_leaves, 1988],
%     [1250, chaurd, outer_space, 1981],
%     [1500, buralde, football, 1985],
%     [1750, rynir, coral_reef, 1998]]).
