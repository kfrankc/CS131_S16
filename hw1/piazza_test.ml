(* Test 1A *)
assert ( member 4 [1;2;3;4] = true );;
assert ( member 0 [1;2;3;4] = false );;
assert ( member 3 [] = false );;

print_string "1A Tests Succeeded!\n";;

(* Test 1B *)
assert ( add 3 [1;2;3] = [1;2;3] );;
assert ( add 3 [1;2] = [1;2;3] );;
assert ( add 2 [1;3] = [1;3;2] );
assert ( add 2 [1;2] = [1;2] );;

print_string "1B Tests Succeeded!\n";;

(* Test 1C *)
assert ( union [1;2] [3;4] = [3;4;1;2] );;
assert ( union [1;2] [2;3] = [2;3;1] );;
assert ( union [] [1] = [1] );;
assert ( union [2;3] [] = [2;3] );;
assert ( union [5;4] [3;2;1] = [3;2;1;5;4] );;
print_string "1C Tests Succeeded!\n";;

(* Test 1D *)
assert ( fastUnion [1;2] [3;4] = [1;2;3;4] );;
assert ( fastUnion [1;2;3] [1;2;3] = [1;2;3] );;
assert ( fastUnion [1;2;3;4;5;6] [1;2] = [1;2;3;4;5;6] );;
assert ( fastUnion [] [] = [] );;
assert ( fastUnion [] [1;2;3] = [1;2;3] );;
assert ( fastUnion [1;2;3] [] = [1;2;3] );;
print_string "1D Tests Succeeded!\n";;

(* Test 1E *)
assert ( intersection  [1;2] [2;3] = [2] );;
assert ( intersection  [1;2] [3;4] = []);;
assert ( intersection  [] [1;2;3;4;] = [] );;
print_string "1E Tests Succeeded!\n";;

(* Test 1F *)
assert ( setify [1;2;3;2;1] = [3;2;1] );;
assert ( setify [1;2] = [1;2] );;
assert ( setify [] = [] );;
print_string "1F Tests Succeeded!\n";;

(* Test 1G *)
assert ( powerset [1;2] = [[]; [2]; [1]; [1; 2]] );;
print_string "1G Tests Succeeded!\n";;

(* Test 2A *)
assert ( partition (function x -> x>3)  [6;4;1;3;2;5] = ([6;4;5], [1;3;2]));;
assert ( partition (function x -> x>3) [7;6;5] = ([7;6;5], []));;
assert ( partition (function x -> x<3) [7;6;5] = ([], [7;6;5]));;
print_string "2A Tests Succeeded!\n";;

(* Test 2B *)
assert ( whle (function x -> x < 10) (function x -> x * 2) 1 =  16 );;
assert ( whle (function x -> x < 5) (function x -> x + 2) 1 = 5; );;
print_string "2B Tests Succeeded!\n";;

(* Test 2C *)
let twice = fun x -> x * 2 ;;
assert ( pow 2 twice 3 = 12 ) ;;
print_string "2C Tests Succeeded!\n";;
