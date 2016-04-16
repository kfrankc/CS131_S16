#use "hw1.ml";;

(* member *)
assert( member 1 [] = false);;
assert( member 2 [1; 2] = true);;
(* add *)
assert( add 1 [] = [1]);;
assert( add 2 [1; 2; 3;] = [1; 2; 3]);;
assert( add 5 [1; 2] = [1; 2; 5]);;
(* union *)
assert( union [] [] = []);;
assert( union [1] [1; 2] = [1; 2]);;
assert( union [1; 2] [3; 4] = [3; 4; 1; 2]);;
assert( union [] [1; 2] = [1; 2]);;
assert( union [1] [1; 2; 3] = [1; 2; 3]);;
assert( union [5; 3; 2] [3; 7; 2] = [3; 7; 2; 5]);;
(* fastUnion *)
assert( fastUnion [] [] = []);;
assert( fastUnion [] [1; 2] = [1; 2]);;
assert( fastUnion [5; 6] [] = [5; 6]);;
assert( fastUnion [1] [1] = [1]);;
assert( fastUnion [1; 3; 4] [2; 3] = [1; 2; 3; 4]);;
assert( fastUnion [1; 3; 4] [2; 3; 6; 8] = [1; 2; 3; 4; 6; 8]);;
(* intersection *)
assert( intersection [] [] = []);;
assert( intersection [1] [] = []);;
assert( intersection [1] [1] = [1]);;
assert( intersection [1] [1; 2] = [1]);;
assert( intersection [1; 2; 3] [1; 3; 2] = [1; 3; 2]);;
assert( intersection [1; 3; 5; 7] [2; 4; 6; 8] = []);;
(* setify *)
assert( setify [] = []);;
assert( setify [1; 2] = [1; 2]);;
assert( setify [1; 2; 1] = [2; 1]);;
assert( setify [1; 2; 1; 4; 1] = [2; 4; 1]);;
assert( setify [1; 2; 1; 4; 2; 5; 4] = [1; 2; 5; 4]);;
(* powerset *)
assert( powerset [] = [[]]);;
assert( powerset [1;2] = [[]; [2]; [1]; [1;2]]);;
assert( powerset [1;2;3] = [[]; [3]; [2]; [2;3]; [1]; [1;3]; [1;2]; [1;2;3]]);;
(* partition *)
assert( partition (function x -> x > 3) [1;5;4;3;2;6] = ([5;4;6], [1;3;2]));;
assert( partition (function x -> x > 3) [] = ([], []));;
assert( partition (function x -> x < 5) [1;5;4;3;2;6] = ([1;4;3;2], [5;6]));;
assert( partition (function x -> x > 0) [1;5;4;3;2;6] = ([1;5;4;3;2;6], []));;
assert( partition (function x -> x > 7) [1;5;4;3;2;6] = ([], [1;5;4;3;2;6]));;
(* whle *)
assert( whle (function x -> x < 10) (function x -> x * 2) 1 = 16);;
assert( whle (function x -> x < 10) (function x -> x + 1) 0 = 10);;
assert( whle (function x -> x > 5) (function x -> x / 5) 50 = 2);;
(* pow *)
assert( pow 0 (function x -> x + 1) 2 = 2);;
assert( pow 1 (function x -> x + 1) 2 = 3);;
assert( pow 5 (function x -> x + 1) 2 = 7);;
assert( pow 1 (function x -> x*2) 3 = 6);;
assert( pow 2 (function x -> x*x) 2 = 16);;
