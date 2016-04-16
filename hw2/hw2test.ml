#use "hw2.ml";;

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

(* vplus test cases *)
let result = vplus [1.;2.;3.] [4.;5.;6.] in
  let vout = (result = [5.;7.;9.]) in 
    test (vout = true) "Test failed: result should be [5;7;9].";;

let passed = print_string "Test cases for 'vplus' passed!";;

(* mplus test cases *)
let result = mplus [[1.;2.;3.];[1.;2.;3.]] [[1.;2.;3.];[1.;2.;3.]] in
  let vout = (result = [[2.;4.;6.];[2.;4.;6.]]) in 
    test (vout = true) "Test failed: result should be [2.;4.;6.].";;

let passed = print_string "Test cases for 'mplus' passed!";;

(* dotprod test cases *)
let result = dotprod [1.;2.;3.] [1.;2.;3.] in
  let vout = (result = 14.) in 
    test (vout = true) "Test failed: result should be 14.";;

let passed = print_string "Test cases for 'dotprod' passed!";;

(* transpose test cases *)
let result = transpose [[1.;2.;3.]] in
  let vout = (result = [[1.];[2.];[3.]]) in 
    test (vout = true) "Test failed: result should be [[1.];[2.];[3.]]";;

let passed = print_string "Test cases for 'transpose' passed!";;

(* mmult test cases *)
let result = mmult [[1.;2.];[3.;4.]] [[5.;6.];[7.;8.]] in
  let vout = (result = [[19.;22.];[43.;50.]]) in 
    test (vout = true) "Test failed: result should be [[19.;22.];[43.;50.]]";;

let passed = print_string "Test cases for 'mmult' passed!";;

(* evalExp test cases *)
let expr1_1 = BinOp(BinOp(Num 1., Plus, Num 2.), Times, Num 3.);;
let expr1_2 = [Push 1.; Push 2.; Calculate Plus; Push 3.; Calculate Times];;
let expr2_1 = BinOp(Num 1.0, Minus, BinOp(Num 2.0, Plus, Num 3.0));;
let expr2_2 = [Push 2.; Push 3.; Calculate Plus; Push 1.; Swap; Calculate Minus];;

let result = evalExp expr1_1 in
  let vout = (result = 9.) in 
    test (vout = true) "Test failed: result should be 9.";;

let passed = print_string "Test cases for 'evalExp' passed!";;

(* execute test cases *)
let result = execute expr1_2 in
  let vout = (result = 9.0) in 
    test (vout = true) "Test failed: result should be 9.0";;

let passed = print_string "Test cases for 'execute' passed!";;

(* compile test cases *)
let result = compile expr1_1 in
  let vout = (result = [Push 1.; Push 2.; Calculate Plus; Push 3.; Calculate Times]) in 
    test (vout = true) "Test failed: result should be [Push 1.; Push 2.; Calculate Plus; Push 3.; Calculate Times]";;

let passed = print_string "Test cases for 'compile' passed!";;

(* decompile test cases *)
let result = decompile expr1_2 in
  let vout = (result = BinOp (Num 3., Times, BinOp (Num 2., Plus, Num 1.))) in 
    test (vout = true) "Test failed: result should be BinOp (Num 3., Times, BinOp (Num 2., Plus, Num 1.))";;

let passed = print_string "Test cases for 'decompile' passed!";;

(* compileOpt test cases *)
let result = compileOpt expr2_1 in
  let vout = (result = ([Push 2.; Push 3.; Calculate Plus; Push 1.; Swap; Calculate Minus], 2)) in 
    test (vout = true) "Test failed: result should be ([Push 2.; Push 3.; Calculate Plus; Push 1.; Swap; Calculate Minus], 2))";;

let passed = print_string "Test cases for 'compileOpt' passed!";;