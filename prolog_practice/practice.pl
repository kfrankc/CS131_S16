/* convert temperature */
temp(C, F) :- F is (1.8 * C + 32.0).

/* find length of list */
len([],0).
len([H|T],I) :- len(T,I2), I is 1 + I2.

/* N-Queens Problem */
attacks(queen(R,_), queen(R,_)).
attacks(queen(_,C), queen(_,C)).
attacks(queen(R1,C1), queen(R2, C2)) :-
	RDiff is R1-R2, CDiff is C1-C2, RDiff = CDiff.

noAttack(_,[]).
noAttack(Q,(Q1|QT)) :-
	noAttack(Q, QT), \+(attacks(Q, Q1)).

nqueens([]).
nqueens([Q|QT]) :-
	nqueens(QT),
	Q = queen(R,C),
	member(R, [1,2,3,4,5,6,7,8]),
	member(C, [1,2,3,4,5,6,7,8]),
	noAttack(Q,QT).

/* family ancestry */

married(alex,lina).
married(oscar,eva).
married(otto,peter).
married(john,ruth).

male(alex).
male(oscar).
male(otto).
male(peter).
male(john).

female(lina).
female(eva).
female(irma).
female(ruth).
female(kim).

parent(alex,ruth).
parent(lina,eva).
parent(otto,irma).
parent(peter,john).
parent(peter,oscar).
parent(ruth,kim).

/* brother and sister in law */
married_sym(A,B) :- married(A,B).
married_sym(A,B) :- married(B,A).
sibling(A,B) :- parent(P,A), parent(P,B), \+(A=B).
sibling(A,B) :- parent(P1,A), parent(P2, B), married_sym(P1,P2).

brotherinlaw(X,Z) :- male(X), sibling(X,Y), married_sym(Y,Z).
sisterinlaw(X,Z) :- female(X), sibling(X,Y), married_sym(Y,Z).

/* ancestor(X,Y); X is ancestor of Y */
any_parent(P,A) :- parent(P,A).
any_parent(P2,A) :- parent(P1,A), married_sym(P1,P2).

ancestor(X,Y) :- any_parent(X,Y).
ancestor(X,Y) :- any_parent(X,P), ancestor(P,Y).

/* merge */
merge([],[],[]).
merge(H,[],H).
merge([],H,H).
merge([H1|T1],[H2|T2],[H1,H2|T]) :- H1 < H2, merge(T1,T2,T).
merge([H1|T1],[H2|T2],[H2,H1|T]) :- H2 < H1, merge(T1,T2,T).

/* person, wolf, goat, cabbage */
opposite(west, east).
opposite(east, west).

move([P, W, G, C], person, [Q, W, G, C]) :-
	opposite(P, Q).
move([P, W, G, C], wolf, [Q, Q, G, C]) :-
	P=W, opposite(G, C), opposite(P, Q).
move([P, W, G, C], goat, [Q, W, Q, C]) :-
	P=G, opposite(P, Q).
move([P, W, G, C], cabbage, [Q, W, G, Q]) ;-
	P=C, opposite(W, G), opposite(P, Q).

puzzle(Goal, Goal, []).
puzzle(Start, Goal, .(Move, Moves)) :-
	move(Start, Move, S), puzzle(S, Goal, Moves).
