/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   http://www.gprolog.org/manual/gprolog.html#sec70
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

/* base case */
verbalarithmetic(Letters,[H1|T1],[H2|T2],[H3|T3]) :-
	fd_domain(H1, 1, 9), /* fd_domain constrains the first letter to be between 1 and 9 */
	fd_domain(H2, 1, 9),
	fd_domain(H3, 1, 9),
	fd_domain(T1, 0, 9), /* fd_domain constrains anything other than the first letter to be between 0 and 9 */
	fd_domain(T2, 0, 9),
	fd_domain(T2, 0, 9),
	fd_all_different(Letters), /* fd_all_different constrains Letters list to be unique */
