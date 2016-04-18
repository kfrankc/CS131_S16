#use "hw2.ml";;

(* Helper test functions *)

let almost_equal x y =
  abs_float (x -. y) < 1e-10;;

let rec vec_almost_equal x y =
  match x,y with
    [],[] -> true
  | [],_ -> failwith "unequal size"
  | _,[] -> failwith "unequal size"
  | h1::t1,h2::t2 -> if not (almost_equal h1 h2) then false else
                     vec_almost_equal t1 t2;;

let rec matrix_almost_equal x y =
  match x,y with
    [],[] -> true
  | [],_ -> failwith "unequal size"
  | _,[] -> failwith "unequal size"
  | h1::t1,h2::t2 -> if not (vec_almost_equal h1 h2) then false else
                     matrix_almost_equal t1 t2;;

(* Problem 1a - adding vectors *)

let v1 = [1.1;2.2;3.3];;
let v2 = [2.2;4.4;6.6];;
let v3 = [];;

assert (vec_almost_equal (vplus v1 v1) v2);;				(* v1 + v1 = v2, non-empty vector *)
assert (vec_almost_equal (vplus v3 v3) v3);;				(* empty vector case *)
print_string "1a Tests Succeeded!\n";;

(* Problem 1b - adding matrices *)

let m1 = [[1.;2.;3.];[4.;5.;6.];[7.;8.;9.]];;
let m2 = [[2.;4.;6.];[8.;10.;12.];[14.;16.;18.]];;
let m3 = [[1.;2.];[3.;4.];[5.;6.]];;
let m4 = [[2.;4.];[6.;8.];[10.;12.]];;
let m5 = [];;

assert (matrix_almost_equal (mplus m1 m1) m2);;			(* m1 + m1 = m2, square matrix *)
assert (matrix_almost_equal (mplus m3 m3) m4);;			(* m3 + m3 = m4, rectangluar matrix *)
assert (matrix_almost_equal (mplus m5 m5) m5);;			(* empty matrix case *)
print_string "1b Tests Succeeded!\n";;

(* Problem 1c - dot product of vectors *)

let v1 = [1.1;2.2;3.3];;
let v2 = [];;

assert (almost_equal (dotprod v1 v1) 16.94);;				(* non-empty vector case *)
assert (almost_equal (dotprod v2 v2) 0.0);;					(* empty vector case *)
print_string "1c Tests Succeeded!\n";;

(* Problem 1d - transpose *)

let m1 = [[1.;2.];[3.;4.]];;
let m2 = [[1.;3.];[2.;4.]];;
let m3 = [[1.;2.;3.];[4.;5.;6.]];;
let m4 = [[1.;4.];[2.;5.];[3.;6.]];;
let m5 = [];;

assert (matrix_almost_equal (transpose m1) m2);;		(* m2 = m1', square matrix case *)
assert (matrix_almost_equal (transpose m3) m4);;		(* m4 = m3', rectangular matrix case *)
assert (matrix_almost_equal (transpose m4) m3);;		(* m3 = m4', inverted rec. matrix case *)
(* assert (matrix_almost_equal (transpose m5) m5);;	(* May vary based on implementation *) *)
print_string "1d Tests Succeeded!\n";;

(* Problem 1e - matrix multiplication *)

let m1 = [[1.;2.];[3.;4.]];;
let m2 = [[5.;6.];[7.;8.]];;
let m3 = [[19.;22.];[43.;50.]];;
let m4 = [[1.;2.];[3.;4.];[5.;6.]];;
let m5 = [[1.;2.;3.;4.];[5.;6.;7.;8.]];;
let m6 = [[11.;14.;17.;20.];[23.;30.;37.;44.];[35.;46.;57.;68.]];;
let m7 = [];;
let m8 = [[1.;2.;3.];[4.;5.;6.]];;
let m9 = [[7.;8.];[9.;10.];[11.;12.]];;
let m10 = [[58.;64.];[139.;154.]];;
    
assert (matrix_almost_equal (mmult m1 m2) m3);;		(* m1 * m2 = m3, 2x2 square * 2x2 square matrix case *)
assert (matrix_almost_equal (mmult m4 m5) m6);;		(* m4 * m5 = m6, 3x2 square * 2x4 square matrix case *)
assert (matrix_almost_equal (mmult m7 m7) m7);;		(* [] * [] = [], empty matrix case *)
assert (matrix_almost_equal (mmult m8 m9) m10);;
print_string "1e Tests Succeeded!\n";;

(* Problem 2a - arithmetic calculator *)

let exp1 = Num 3.0;;
let exp2 = BinOp(Num 1.0, Plus, Num 2.0);;
let exp3 = BinOp(exp2, Times, exp1);;
let exp4 = BinOp(exp3, Divide, exp2);;
let exp5 = BinOp(exp1, Minus, exp4);;

assert (almost_equal (evalExp exp1) 3.0);;
assert (almost_equal (evalExp exp2) 3.0);;
assert (almost_equal (evalExp exp3) 9.0);;
assert (almost_equal (evalExp exp4) 3.0);;
assert (almost_equal (evalExp exp5) 0.0);;
print_string "2a Tests Succeeded!\n";;

(* Problem 2b - stack machine *)

let instr1 = [Push 1.0; Push 2.0; Calculate Plus; Push 3.0; Calculate Times];;
let instr2 = [Push 1.0; Push 2.0; Push 3.0; Calculate Plus; Calculate Minus];;
let instr3 = [Push 2.0; Push 3.0; Calculate Plus; Push 1.0; Swap; Calculate Minus];;

assert (almost_equal (execute instr1) 9.0);;
assert (almost_equal (execute instr2) (-4.0));;
assert (almost_equal (execute instr3) (-4.0));;
print_string "2b Tests Succeeded!\n";;

(* Problem 2c - compilation *)

let exp1 = Num 3.0;;
let exp2 = BinOp(Num 1.0, Plus, Num 2.0);;
let exp3 = BinOp(exp2, Times, exp1);;
let lst1 = [Push 3.0];;
let lst2 = [Push 1.0; Push 2.0; Calculate Plus];;
let lst3 = [Push 1.0; Push 2.0; Calculate Plus; Push 3.0; Calculate Times];;

assert (compile exp1 = lst1);;
assert (compile exp2 = lst2);;
assert (compile exp3 = lst3);;
print_string "2c Tests Succeeded!\n";;

(* Problem 2d - decompile *)

let lst1 = [Push 3.0];;
let lst2 = [Push 1.0; Push 2.0; Calculate Plus];;
let lst3 = [Push 1.0; Push 2.0; Calculate Plus; Push 3.0; Calculate Times];;
let exp1 = Num 3.0;;
let exp2 = BinOp(Num 1.0, Plus, Num 2.0);;
let exp3 = BinOp(exp2, Times, exp1);;

assert (decompile lst1 = exp1);;
assert (decompile lst2 = exp2);;
assert (decompile lst3 = exp3);;
print_string "2d Tests Succeeded!\n";;

(* Problem 2e - optimized compilation *)

let exp1 = BinOp(BinOp(Num 1.0, Plus, Num 2.0), Times, Num 3.0);;
let exp2 = BinOp(BinOp(Num 1.0, Plus, Num 2.0), Times, BinOp(Num 3.0, Plus, BinOp(Num 4.0, Minus, Num 5.0)));;
let exp3 = BinOp(BinOp(Num 1.0, Plus, Num 2.0), Times, BinOp(Num 3.0, Plus, Num 4.0));;
let exp4 = BinOp(exp2, Times, BinOp(exp2, Plus, exp3));;
let exp5 = BinOp(BinOp(Num 1.,Minus,BinOp(Num 2.,Plus,Num 3.)),Times,BinOp(Num 1.,Minus,BinOp(Num 3.,Plus,Num 4.)));;

let lst1,len1 = ([Push 1.; Push 2.; Calculate Plus; Push 3.; Calculate Times], 2);;
let ccopt2,len2 = compileOpt exp2;; 
let ccopt3,len3 = compileOpt exp3;;
let ccopt4,len4 = compileOpt exp4;;
let lst5, len5 = ([Push 2.; Push 3.; Calculate Plus; Push 1.; Swap; Calculate Minus; Push 3.;
  Push 4.; Calculate Plus; Push 1.; Swap; Calculate Minus; Calculate Times],
 3);;

assert (compileOpt exp1 = (lst1, len1));;
assert (len2 = 3);; assert (len3 = 3);; assert (len4 = 4);;
assert (almost_equal (execute ccopt4) 162.);;
(*assert (compileOpt exp5 = (lst5, len5));; (* Answer may not match, but this is the ideal result. *)*)
print_string "2e Tests Succeeded!\n";;