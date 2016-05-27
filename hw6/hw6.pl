/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   http://www.gprolog.org/manual/gprolog.html#sec70
   http://www.gprolog.org/manual/html_node/gprolog044.html
   http://pauillac.inria.fr/~haemmerl/gprolog-rh/doc/manual063.html
   Professor Millstein's notes from Wednesday Week 8, Monday and Wednesday Week 9
*/

/* base case, empty list and empty list */
duplist([],[]).
/* recursive case: given that Y is a duplist of X, a list containing N and N or Y will be a duplist of a list
   containing N or X */
duplist([N|X],[N,N|Y]) :- duplist(X,Y).

/* base case, empty list is subseq of any list*/
subseq([],_).
/* recursive case: if B is a subseq of C, then adding A to both sides will work
   recursive case: if A or B is a subseq of C or D, then adding anything in C or D will still satisfy */
subseq([A|B], [A|C]) :- subseq(B,C).
subseq([A|B], [_,C|D]) :- subseq([A|B], [C|D]).

/* Verbal Arithmetic */

/** add helper predicate 
 * @param A word1's number
 * @param B word2's number
 * @param CarryOver carry over from addition
 * @param arg4 either a 1 or 0 based on carry over
 * @param LeftOver remaining number after carry over computed
*/
add(A, B, CarryOver, 1, LeftOver) :-
	(A + B + CarryOver) > 9, LeftOver is ((A + B + CarryOver) mod 10).
add(A, B, CarryOver, 0, LeftOver) :-
	(A + B + CarryOver) =< 9, LeftOver is (A + B + CarryOver).

/* compute helper predicate 
 * @param arg1 word1
 * @param arg2 word2 
 * @param arg3 word3
 * @param arg4 carry over from addition
*/
compute([], [], [H], CarryOver) :- CarryOver =:= H.
compute([H1|T1], [], [H3|T3], CarryOver) :- 
	add(H1, 0, CarryOver, Tens, Ones),
	Ones =:= H3,
	compute(T1, [], T3, Tens).
compute([], [H2|T2], [H3|T3], CarryOver) :-
	add(0, H2, CarryOver, Tens, Ones),
	Ones =:= H3,
	compute([], T2, T3, Tens).
compute([H1|T1], [H2|T2], [H3|T3], CarryOver) :-
	add(H1, H2, CarryOver, Tens, Ones),
	Ones =:= H3,
	compute(T1, T2, T3, Tens).

/** verbalarthmetic predicate
 * @param Letters list of possible letters
 * @param [H1|T1] word1
 * @param [H2|T2] word2
 * @param [H3|T3] word3
 * @return number mapping for each letter such that word1 + word2 = word3
*/
verbalarithmetic(Letters,[H1|T1],[H2|T2],[H3|T3]) :-
	fd_domain(H1, 1, 9), /* fd_domain constrains the first letter to be between 1 and 9 */
	fd_domain(H2, 1, 9),
	fd_domain(H3, 1, 9),
	fd_domain(T1, 0, 9), /* fd_domain constrains anything other than the first letter to be between 0 and 9 */
	fd_domain(T2, 0, 9),
	fd_domain(T3, 0, 9),
	fd_all_different(Letters), /* fd_all_different constrains Letters list to be unique */
	fd_labeling(Letters), /* label Letters for finite domain solving */
	reverse([H1|T1], R1), /* easier to extract head from reversed list to perform arithmetic */
	reverse([H2|T2], R2),
	reverse([H3|T3], R3),
	compute(R1, R2, R3, 0).

/** move helper predicate
* @param current state
* @param action (pickup | place)
* @param next state
* @return nothing, just sets next state
*/
move(world([H1|S1], S2, S3, none), pickup(stack1), world(S1, S2, S3, H1)).
move(world(S1, S2, S3, H1), place(stack1), world([H1|S1], S2, S3, none)).
move(world(S1, [H2|S2], S3, none), pickup(stack2), world(S1, S2, S3, H2)).
move(world(S1, S2, S3, H2), place(stack2), world(S1, [H2|S2], S3, none)).
move(world(S1, S2, [H3|S3], none), pickup(stack3), world(S1, S2, S3, H3)).
move(world(S1, S2, S3, H3), place(stack3), world(S1, S2, [H3|S3], none)).

/** blocksworld
 * @param arg1 current state (could be start or in the middle of the objective)
 * @param arg2 list of actions
 * @param arg3 goal state
*/
blocksworld(Goal, [], Goal). /* base case */
blocksworld(Start, [Action|Actions], Goal) :- 
	move(Start, Action, NextState), blocksworld(NextState, Actions, Goal).

