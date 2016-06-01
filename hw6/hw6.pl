/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   http://www.gprolog.org/manual/gprolog.html#sec70
   http://www.gprolog.org/manual/html_node/gprolog044.html
   http://www.gprolog.org/manual/html_node/gprolog062.html
   http://www.gprolog.org/manual/html_node/gprolog063.html
   http://www.gprolog.org/manual/html_node/gprolog057.html
   http://pauillac.inria.fr/~haemmerl/gprolog-rh/doc/manual063.html
   Professor Millstein's notes from Wednesday Week 8, Monday and Wednesday Week 9
   TA Notes from week 8 and week 9 on Prolog, fd_domain, fd_labeling, fd_all_different
*/

/* base case, empty list is a duplist of empty list */
duplist([],[]).
/* recursive case: given that T2 is a duplist of T1, a list containing H, H, and tail list T2 will be a duplist of a list
   containing head H and tail list T1 */
duplist([H|T1],[H,H|T2]) :- duplist(T1,T2).

/* base case, empty list is subseq of empty list */
subseq([],[]).
/* recursive case: if T1 is a subseq of T2, then adding H to both lists will still satisfy the constraint
   recursive case: if H1 is a subseq of T2, then adding H2 to T2 will still satisfy */
subseq([H|T1], [H|T2]) :- subseq(T1,T2).
subseq(H1, [H2|T2]) :- subseq(H1, T2).

/* Verbal Arithmetic */

/** add helper predicate 
 * @param A word1's number
 * @param B word2's number
 * @param CarryOver carry over from addition
 * @param arg4 either a 1 or 0 based on carry over
 * @param LeftOver remaining number after carry over computed
*/
add(A, B, CarryOver, 0, LeftOver) :-
	(A + B + CarryOver) =< 9, LeftOver is (A + B + CarryOver).
add(A, B, CarryOver, 1, LeftOver) :-
	(A + B + CarryOver) > 9, LeftOver is ((A + B + CarryOver) mod 10).

/* compute helper predicate 
 * @param arg1 word1
 * @param arg2 word2 
 * @param arg3 word3
 * @param arg4 carry over from addition
*/
compute([], [], [], CarryOver) :- 
	CarryOver =:= 0. /* base case: carryover is 0, and nothing is added */
compute([], [], [H], CarryOver) :- 
	CarryOver =:= H.
compute([H1|T1], [], [H3|T3], CarryOver) :- 
	add(H1, 0, CarryOver, Tens, Ones),
	Ones =:= H3, /* Ones is definitely H3, since that's head of the list, which means it is the ones of the result */
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
	fd_all_different(Letters), /* fd_all_different constrains Letters list to be unique */
	fd_domain([H1, H2, H3], 1, 9), /* fd_domain constrains the first letter to be between 1 and 9 */
	fd_domain(T1, 0, 9), /* fd_domain constrains anything other than the first letter to be between 0 and 9 */
	fd_domain(T2, 0, 9),
	fd_domain(T3, 0, 9),
	fd_labeling(Letters), /* label Letters for finite domain solving */
	reverse([H1|T1], R1), /* easier to extract head from reversed list to perform arithmetic */
	reverse([H2|T2], R2),
	reverse([H3|T3], R3),
	compute(R1, R2, R3, 0). /* run compute on the three lists with initial carryover to be 0 */

/** move helper predicate
* @param current world
* @param action (pickup | putdown)
* @param next world
* @return nothing, just sets next world
*/
move(world([H1|S1], S2, S3, none), pickup(H1, stack1), world(S1, S2, S3, H1)).
move(world(S1, S2, S3, H1), putdown(H1, stack1), world([H1|S1], S2, S3, none)).
move(world(S1, [H2|S2], S3, none), pickup(H2, stack2), world(S1, S2, S3, H2)).
move(world(S1, S2, S3, H2), putdown(H2, stack2), world(S1, [H2|S2], S3, none)).
move(world(S1, S2, [H3|S3], none), pickup(H3, stack3), world(S1, S2, S3, H3)).
move(world(S1, S2, S3, H3), putdown(H3, stack3), world(S1, S2, [H3|S3], none)).

/** blocksworld
 * @param arg1 current state (could be start or in the middle of the objective)
 * @param arg2 list of actions
 * @param arg3 goal state
*/
blocksworld(GoalState, [], GoalState). /* base case: we are at the goal state */
blocksworld(StartState, [Action|ActionList], GoalState) :- 
	move(StartState, Action, NextState), /* move with head Action so we get to next state */
	blocksworld(NextState, ActionList, GoalState). /* use next state and tail ActionList to get to goal state  */

