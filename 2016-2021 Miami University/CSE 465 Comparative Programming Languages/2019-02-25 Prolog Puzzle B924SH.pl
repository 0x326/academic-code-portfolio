% Puzzle B924SH from https://www.printable-puzzles.com

names([gary,
      juliana,
      keith,
      leslie,
      myles]).

fruits([cherries,
        cranberries,
        limes,
        pineapples,
        strawberries]).

wines([bordeaux,
       burgundy,
       chablis,
       chianti,
       shiraz]).

bills([930, 1020, 1400, 2000, 2290]).

% Rule 1: The wine enthusiast who specializes in chablis doesn't grow cherries.
%
% (Wine = chablis) -> (Fruit != cherries)
% ¬(Wine = chablis) V (Fruit != cherries)
% (Wine != chablis) V (Fruit != cherries)
% ¬[(Wine = chablis) ^ (Fruit = cherries)]
rule1(Fruit, Wine) :-
    not((Wine = chablis, Fruit = cherries)).

% Rule 2: The person who grows limes is Juliana
%
% (Fruit = limes) -> Name = juliana
% ¬(Fruit = limes) V Name = juliana
rule2(Name, Fruit) :-
    Name = juliana;
    not(Fruit = limes).

% Rule 3:
% The wine enthusiast who specializes in bordeaux
% owes more money than the person who grows strawberries
%
% (Wine1 = bordeaux ^ Fruit2 = strawberries) -> Bill1 > Bill2
% ¬(Wine1 = bordeaux ^ Fruit2 = strawberries) V Bill1 > Bill2
% Wine1 ≠ bordeaux V Fruit2 ≠ strawberries V Bill1 > Bill2
rule3(Bill1, Wine1, Bill2, Fruit2) :-
    not(Wine1 = bordeaux);
    not(Fruit2 = strawberries);
    Bill1 > Bill2.

% Rule 4:
% Of Leslie and the person who grows pineapples,
% one has the $2000 hospital bill
% and the other has the $1400 hospital bill.

% (Name1 = Leslie) ^ (Fruit2 = pineapples) ->
%       (Bill1 = 2000 ^ Bill2 = 1400) V (Bill1 = 1400 ^ Bill2 = 2000)
% ¬[(Name1 = Leslie) ^ (Fruit2 = pineapples)] V (Bill1 = 2000 ^ Bill2 = 1400) V (Bill1 = 1400 ^ Bill2 = 2000)
% (Name1 != Leslie) V (Fruit2 != pineapples) V (Bill1 = 2000 ^ Bill2 = 1400) V (Bill1 = 1400 ^ Bill2 = 2000)
rule4a(Bill1, Name1, Bill2, Fruit2) :-
    not(Name1 = leslie);
    not(Fruit2 = pineapples);
    (Bill1 = 2000, Bill2 = 1400);
    (Bill1 = 1400, Bill2 = 2000).

% (Name = leslie) -> (Fruit != pineapples)
% ¬(Name = leslie) V (Fruit != pineapples)
rule4b(Name, Fruit) :-
    not(Name = leslie);
    not(Fruit = pineapples).

% Rule 5:
% Either the wine enthusiast who specializes in chianti
% or the wine enthusiast who specializes in shiraz
% grows limes.
%
% (Fruit = limes) -> (Wine = chianti) V (Wine = shiraz)
% ¬(Fruit = limes) V (Wine = chianti) V (Wine = shiraz)
rule5(Fruit, Wine) :-
    not(Fruit = limes);
    Wine = chianti;
    Wine = shiraz.

% Rule 6:
% The patient with the $930 hospital bill doesn't grow strawberries.
%
% (Bill = 930) -> (Fruit != strawberries)
% ¬(Bill = 930) V (Fruit != strawberries)
% ¬[(Bill = 930) ^ (Fruit = strawberries)]
rule6(Bill, Fruit) :-
    not((Bill = 930, Fruit = strawberries)).

% Rule 7:
% The 5 people were
% the patient with the $1020 hospital bill,
% the person who grows strawberries,
% Myles,
% the wine enthusiast who specializes in chablis,
% and the patient with the $1400 hospital bill.
rule7(Bill, Name, Fruit, Wine) :-
    rule7a(Bill, Name, Fruit, Wine),
    rule7b(Bill, Name, Fruit, Wine),
    rule7c(Bill, Name, Fruit, Wine),
    rule7d(Bill, Name, Fruit, Wine),
    rule7e(Bill, Name, Fruit, Wine).

% (Bill = 1020) -> (Fruit != strawberries) ^ (Name != Myles) ^ (Wine != chablis)
% ¬(Bill = 1020) V [(Fruit != strawberries) ^ (Name != Myles) ^ (Wine != chablis)]
rule7a(Bill, Name, Fruit, Wine) :- not(Bill = 1020);
                                   (not(Fruit = strawberries), not(Name = myles), not(Wine = chablis)).

% (Fruit = strawberries) -> (Bill != 1020) ^ (Name != Myles) ^ (Wine != chablis) ^ (Bill != 1400)
% ¬(Fruit = strawberries) V [(Bill != 1020) ^ (Name != Myles) ^ (Wine != chablis) ^ (Bill != 1400)]
rule7b(Bill, Name, Fruit, Wine) :- not(Fruit = strawberries);
                                   (not(Bill = 1020), not(Name = myles), not(Wine = chablis), not(Bill = 1400)).

% (Name = Myles) -> (Bill != 1020) ^ (Fruit != strawberries) ^ (Wine != chablis) ^ (Bill != 1400)
% ¬(Name = Myles) V [(Bill != 1020) ^ (Fruit != strawberries) ^ (Wine != chablis) ^ (Bill != 1400)]
rule7c(Bill, Name, Fruit, Wine) :- not(Name = myles);
                                   (not(Bill = 1020), not(Fruit = strawberries), not(Wine = chablis), not(Bill = 1400)).

% (Wine = chablis) -> (Bill != 1020) ^ (Fruit != strawberries) ^ (Name != Myles) ^ (Bill != 1400)
% ¬(Wine = chablis) V [(Bill != 1020) ^ (Fruit != strawberries) ^ (Name != Myles) ^ (Bill != 1400)]
rule7d(Bill, Name, Fruit, Wine) :- not(Wine = chablis);
                                   (not(Bill = 1020), not(Fruit = strawberries), not(Name = myles), not(Bill = 1400)).

% (Bill = 1400) -> (Fruit != strawberries) ^ (Name != Myles) ^ (Wine != chablis)
% ¬(Bill = 1400) V [(Fruit != strawberries) ^ (Name != Myles) ^ (Wine != chablis)]
rule7e(Bill, Name, Fruit, Wine) :- not(Bill = 1400);
                                   (not(Fruit = strawberries), not(Name = myles), not(Wine = chablis)).

% Rule 8: Juliana owes less money than Myles.
%
% (Name1 = juliana) ^ (Name2 = myles) -> (Bill1 < Bill2)
% ¬[(Name1 = juliana) ^ (Name2 = myles)] V (Bill1 < Bill2)
% (Name1 != juliana) V (Name2 != myles) V (Bill1 < Bill2)
rule8(Bill1, Name1, Bill2, Name2) :-
    not(Name1 = juliana);
    not(Name2 = myles);
    Bill1 < Bill2.

% Rule 9: The patient with the $1400 hospital bill specializes in shiraz.
%
% (Bill = 1400) -> (Wine = shiraz)
% ¬(Bill = 1400) V (Wine = shiraz)
rule9(Bill, Wine) :-
    not(Bill = 1400);
    Wine = shiraz.

% Rule 10: The wine enthusiast who specializes in chianti owes more money than Keith.
%
% (Wine1 = chianti) ^ (Name2 = Keith) -> (Bill1 > Bill2)
% ¬[(Wine1 = chianti) ^ (Name2 = Keith)] V (Bill1 > Bill2)
% (Wine1 != chianti) V (Name2 != Keith) V (Bill1 > Bill2)
rule10a(Bill1, Wine1, Bill2, Name2) :-
    not(Wine1 = chianti);
    not(Name2 = keith);
    Bill1 > Bill2.

% (Name = Keith) -> (Wine != chianti)
% ¬(Name = Keith) V (Wine != chianti)
rule10b(Name, Wine) :-
    not(Name = keith);
    not(Wine = chianti).

rule1_compliant(X) :-
    forall(member([_, _, Fruit, Wine], X),
           rule1(Fruit, Wine)).

rule2_compliant(X) :-
    forall(member([_, Name, Fruit, _], X),
            rule2(Name, Fruit)).

rule3_compliant(X) :-
    forall((member([Bill1, _, _, Wine1], X),
                member([Bill2, _, Fruit2, _], X)),
            rule3(Bill1, Wine1, Bill2, Fruit2)).

rule4_compliant(X) :-
    forall((member([Bill1, Name1, _, _], X),
                member([Bill2, _, Fruit2, _], X)),
            rule4a(Bill1, Name1, Bill2, Fruit2)),
    forall(member([_, Name, Fruit, _], X),
            rule4b(Name, Fruit)).

rule5_compliant(X) :-
    forall(member([_, _, Fruit, Wine], X),
            rule5(Fruit, Wine)).

rule6_compliant(X) :-
    forall(member([Bill, _, Fruit, _], X),
            rule6(Bill, Fruit)).

rule7_compliant(X) :-
    forall(member([Bill, Name, Fruit, Wine], X),
            rule7(Bill, Name, Fruit, Wine)).

rule8_compliant(X) :-
    forall((member([Bill1, Name1, _, _], X),
                    member([Bill2, Name2, _, _], X)),
                rule8(Bill1, Name1, Bill2, Name2)).

rule9_compliant(X) :-
    forall(member([Bill, _, _, Wine], X),
            rule9(Bill, Wine)).

rule10_compliant(X) :-
    forall((member([Bill1, _, _, Wine1], X),
            member([Bill2, Name2, _, _], X)),
            rule10a(Bill1, Wine1, Bill2, Name2)),
    forall(member([_, Name, _, Wine], X),
            rule10b(Name, Wine)).

rule_compliant(X) :-
    rule1_compliant(X),
    rule2_compliant(X),
    rule3_compliant(X),
    rule4_compliant(X),
    rule5_compliant(X),
    rule6_compliant(X),
    rule7_compliant(X),
    rule8_compliant(X),
    rule9_compliant(X),
    rule10_compliant(X).

rule_compliant_threaded(X) :-
    thread_create(rule1_compliant(X), ThreadId1),
    thread_create(rule2_compliant(X), ThreadId2),
    thread_create(rule3_compliant(X), ThreadId3),
    thread_create(rule4_compliant(X), ThreadId4),
    thread_create(rule5_compliant(X), ThreadId5),
    thread_create(rule6_compliant(X), ThreadId6),
    thread_create(rule7_compliant(X), ThreadId7),
    thread_create(rule8_compliant(X), ThreadId8),
    thread_create(rule9_compliant(X), ThreadId9),
    thread_create(rule10_compliant(X), ThreadId10),

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

    ThreadStatus1 = true,
    ThreadStatus2 = true,
    ThreadStatus3 = true,
    ThreadStatus4 = true,
    ThreadStatus5 = true,
    ThreadStatus6 = true,
    ThreadStatus7 = true,
    ThreadStatus8 = true,
    ThreadStatus9 = true,
    ThreadStatus10 = true.

solution(X) :-
    % Instantiate the lists of names, fruits, etc.
    names(Names),
    fruits(Fruits),
    wines(Wines),
    bills(Bills),

    % Generate all possible solutions (brute force)
    permutation([N1, N2, N3, N4, N5], Names),
    permutation([F1, F2, F3, F4, F5], Fruits),
    permutation([W1, W2, W3, W4, W5], Wines),
    [B1, B2, B3, B4, B5] = Bills,

    % Format the variables as solutions table
    X = [[B1, N1, F1, W1],
         [B2, N2, F2, W2],
         [B3, N3, F3, W3],
         [B4, N4, F4, W4],
         [B5, N5, F5, W5]],

    rule_compliant(X).

% :- forall(solution(X), forall(member(Element, X),
%            (write(Element), nl))),
%            nl, write("==== End of solutions ===="), nl, nl.

% :- rule_compliant([
%     [930, keith, cranberries, chablis],
%     [1020, juliana, limes, chianti],
%     [1400, gary, pineapples, shiraz],
%     [2000, leslie, strawberries, burgundy],
%     [2290, myles, cherries, bordeaux]]).
