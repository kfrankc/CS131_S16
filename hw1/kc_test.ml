#use "hw1.ml";;

(* If the boolean 'expr' evaluates to false, then print the error 'message'
 *)
let test = 
  fun expr message -> 
    if expr = false then
      print_string message; print_newline ();;

(* TODO: Rewrite this with map *)
let rec print_set =   
  fun lst -> 
    match lst with 
    | [] -> print_newline ()
    | h::t -> print_string h; print_string " "; print_set t;;


(* Original debug message forms *)
(* if isIn <> true then error 1 "\"5\" should be in the list!" *)
(* if isIn <> true then print_string "Test 1 failed: \"5\" should be in the list!"; print_newline ();; *)

  (* Run test cases for 'member' *)
  (* Takes an item and a list, not a pair! *)

(* Empty list *)
let isIn = member "4" [] in 
  test (isIn = false) "Test failed: \"4\" should NOT be in the EMPTY list!";;

(* Basic set presence *)
let isIn = member "5" ["1"; "2"; "5"; "6"; "7"] in 
  test (isIn = true) "Test failed: \"5\" should be in the list!";;

(* Basic set non-presence *)
let isIn = member "4" ["1"; "2"; "5"; "6"; "7"] in 
  test (isIn = false) "Test failed: \"4\" should NOT be in the list!";;

(* Using ints *)
let isIn = member (-7) [1; 2; 5; 6; -7] in 
  test (isIn = true) "Test failed: -7 should be in the list!";;

let isIn = member (-0) [1; 2; 5; 6; -7] in 
  test (isIn = false) "Test failed: -0 should NOT be in the list!";;

(* Using floats *)
let isIn = member (-7.04) [1.67; 2.34235; -3.35; 6.8; -7.04] in 
  test (isIn = true) "Test failed: -7.04 should be in the list!";;

let isIn = member (0.01) [1.67; 2.34235; -3.35; 6.8; -7.04] in 
  test (isIn = false) "Test failed: 0.01 should NOT be in the list!";;

let passed = print_string "Test cases for 'member' passed!";;

  (* Test cases for 'add' *)

(* Add to empty set *)
let result = add "5" [] in 
  let added = (result = ["5"]) in 
    test (added = true) "Test failed: \"5\" should be added to the EMPTY set.";;

(* No duplicates allowed with single-item set *)
let result = add "5" ["5"] in 
  let added = (result = ["5"]) in 
    test (added = true) "Test failed: \"5\" should NOT be added as it is duplicate.";;

(* 'add' succeeds when no duplicates *)
let result = add "5" ["4"] in 
  let added = (member "5" result) in 
    (* print_set result; *)
    test (added = true) "Test failed: \"5\" should have been added to short list as it is NOT a duplicate.";;

(* 'add' succeeds when no duplicates *)
let result = add "5" ["4"; "7"; "9"; "10"] in 
  let added = (member "5" result) in 
    (* print_set result; *)
    test (added = true) "Test failed: \"5\" should have been added to long list as it is NOT a duplicate.";;

let passed = print_string "Test cases for 'add' passed!";;

    (* Test cases for 'union' *)

(* Union of empty with empty *)
let result = union [] [] in 
  let unified = (result = []) in 
    (* print_set result; *)
    test (unified = true) "Test failed: union of [] with [] should produce [].";;

(* Other set does have to be a set, i.e., not just an atomic item *)
let result = union ["1"] [] in 
  let unified = (result = ["1"]) in 
    (* print_set result; *)
    test (unified = true) "Test failed: union of [\"1\"] with [] should produce [\"1\"].";;

let result = union [] ["1"] in 
  let unified = (result = ["1"]) in 
    (* print_set result; *)
    test (unified = true) "Test failed: union of [] with [\"1\"] should produce [\"1\"].";;

(* '&&' is logical AND in OCaml *)
let result = union ["1"; "2"] ["3"] in 
  let unified = (member "1" result && member "2" result && member "3" result) in 
    (* print_set result; *)
    test (unified = true) "Test failed: union of [\"1\"; \"2\"] with [\"3\"] failed to produce [\"1\"; \"2\"; \"3\"].";;

let result = union ["1"; "2"] ["1"; "2"] in 
  let unified = (result = ["1"; "2"]) in
    (* print_set result; *)
    test (unified) "Test failed: union of [\"1\"; \"2\"] with [\"1\"; \"2\"] should produce [\"1\"; \"2\"].";;

(* TODO: Update this to use membership testing instead *)
let result = union ["1"; "2"; "3"] ["4"; "5"; "6"] in 
  let unified = (result = ["4"; "5"; "6"; "1"; "2"; "3";]) in 
     (* print_set result; *)
    test (unified) "Test failed: union of [\"1\"; \"2\"; \"3\"] with [\"4\"; \"5\"; \"6\"] should produce [\"1\"; \"2\"; \"3\"; \"4\"; \"5\"; \"6\"].";;

let passed = print_string "Test cases for 'union' passed!";;
    
    (* Test cases for 'fastUnion' *)

(* Union of empty with empty *)
let result = fastUnion [] [] in 
  let unified = (result = []) in 
    (* print_set result; *)
    test (unified = true) "Test failed: fastUnion of [] with [] should produce [].";;

(* Other set does have to be a set, i.e., not just an atomic item *)
let result = fastUnion ["1"] [] in 
  let unified = (result = ["1"]) in 
    (* print_set result; *)
    test (unified = true) "Test failed: fastUnion of [\"1\"] with [] should produce [\"1\"].";;

let result = fastUnion [] ["1"] in 
  let unified = (result = ["1"]) in 
    (* print_set result; *)
    test (unified = true) "Test failed: fastUnion of [] with [\"1\"] should produce [\"1\"].";;

(* '&&' is logical AND in OCaml *)
let result = fastUnion ["1"; "2"] ["3"] in 
  let unified = (result = ["1"; "2"; "3"]) in 
    (* print_set result; *)
    test (unified = true) "Test failed: fastUnion of [\"1\"; \"2\"] with [\"3\"] failed to produce [\"1\"; \"2\"; \"3\"].";;

let result = fastUnion ["1"; "2"] ["1"; "2"] in 
  let unified = (result = ["1"; "2"]) in
    (* print_set result; *)
    test (unified) "Test failed: fastUnion of [\"1\"; \"2\"] with [\"1\"; \"2\"] should produce [\"1\"; \"2\"].";;

(* TODO: Update this to use membership testing instead *)
let result = fastUnion ["1"; "2"; "3"] ["4"; "5"; "6"] in 
  let unified = (result = ["1"; "2"; "3"; "4"; "5"; "6"]) in 
     (* print_set result;   *)
    test (unified) "Test failed: fastUnion of [\"1\"; \"2\"; \"3\"] with [\"4\"; \"5\"; \"6\"] should produce [\"1\"; \"2\"; \"3\"; \"4\"; \"5\"; \"6\"].";;

let result = fastUnion ["1"; "4"; "6"] ["2"; "3"; "5"] in 
  let unified = (result = ["1"; "2"; "3"; "4"; "5"; "6"]) in 
     (* print_set result; *)
    test (unified) "Test failed: fastUnion of [\"1\"; \"4\"; \"6\"] with [\"2\"; \"3\"; \"5\"] should produce [\"1\"; \"2\"; \"3\"; \"4\"; \"5\"; \"6\"].";;

    (* Test cases for 'intersection' *)

(* TODO: Do not copy-paste test cases! *)
let result = intersection [] [] in 
  let correct = (result = []) in 
    test (correct) "Test failed: intersection of [] with [] should produce [].";;

    (* TODO: Can I eliminate 'correct=true'? *)

let result = intersection ["1"] [] in 
  let correct = (result = []) in 
    test (correct) "Test failed: intersection of [\"1\"] with [] should produce [].";;

let result = intersection [] ["1"] in 
  let correct = (result = []) in 
    test (correct) "Test failed: intersection of [] with [\"1\"] should produce [].";;

let result = intersection [1] [1] in 
  let correct = (result = [1]) in 
    test (correct) "Test failed: intersection of [1] with [1] should produce [1].";;

let result = intersection [1; 2; 3] [1] in 
  let correct = (result = [1]) in 
    test (correct) "Test failed: intersection of [1; 2; 3] with [1] should produce [1].";;

let result = intersection [1] [1; 2; 3] in 
  let correct = (result = [1]) in 
    test (correct) "Test failed: intersection of [1] with [1; 2; 3] should produce [1].";;

let result = intersection [1; 2; 3] [1; 3] in 
  let correct = (result = [1; 3]) in 
    test (correct) "Test failed: intersection of [1; 2; 3] with [1; 3] should produce [1].";;

let passed = print_string "Test cases for 'intersection' passed!";;

  (* Test cases for 'setify' *)

let result = setify [] in 
  let correct = (result = []) in 
    test (correct) "Test failed: setify of [] should produce [].";; 

let result = setify [1] in 
  let correct = (result = [1]) in 
    test (correct) "Test failed: setify of [1] should produce [1].";;

let result = setify [1; 1] in 
  let correct = (result = [1]) in 
    test (correct) "Test failed: setify of [1; 1] should produce [1].";;

let result = setify [1; 2; 3] in
  let correct = (result = [1; 2; 3]) in 
    test (correct) "Test failed: setify of [1; 2; 3] should produce [1; 2; 3].";;

let result = setify [2; 1; 2; 3; 1; 3; 5; 6] in 
  let correct = (result = [2; 1; 3; 5; 6]) in 
    test (correct) "Test failed: setify of [2; 1; 2; 3; 1; 3; 5; 6] should produce [1; 2; 3; 5; 6].";;

let passed = print_string "Test cases for 'setify' passed!"; print_newline ();

  (* Test cases for 'whle' *)

(* Pathological case: the predicate always returns false, so never execute function.
 * But if the predicate function always returned true, then we would loop infinitely.  *)
let result = whle (fun x -> false) (fun x -> x + 1) 1 in 
  let correct = (result = 1) in 
    test (correct) "Test failed: whle (x -> false) (x + 1) 1 should leave x = 1.";;

let result = whle (fun x -> x < 42) (fun x -> x + 5) 0 in 
  let correct = (result = 45) in 
    test (correct) "Test failed: whle (x < 42) (x + 5) 0 should produce x = 45.";;

let result = whle (fun x -> x < 10) (fun x -> x * 2) 1 in 
  let correct = (result = 16) in 
    test (correct) "Test failed: whle (x < 10) (x * 2) should produce x = 16.";;

let result = whle (fun x -> x < 10) (fun x -> x * x) 2 in 
  let correct = (result = 16) in 
    test (correct) "Test failed: whle (x < 10) (x * x) should produce x = 16.";;

let passed = print_string "Test cases for 'whle' passed!"; print_newline (); 

  (* Test cases for 'pow' *)







