exception ImplementMe

(*
  Resources Used:
  http://caml.inria.fr/pub/docs/manual-ocaml/libref/Pervasives.html
  http://caml.inria.fr/pub/docs/manual-ocaml/libref/List.html
  http://caml.inria.fr/pub/docs/manual-ocaml/libref/index_exceptions.html
  http://rigaux.org/language-study/syntax-across-languages-per-language/OCaml.html
  https://github.com/SaswatPadhi/S16_TA_CS131
  Professor Millstein's class notes from Week 2 & Week 3 on Trees\
*)

(* Problem 1: Vectors and Matrices *)

(* type aliases for vectors and matrices *)            
type vector = float list                                 
type matrix = vector list

let (vplus : vector -> vector -> vector) =
  fun v1 v2 ->
    match (v1, v2) with
      ([], [])     -> []
    | (_::_, [])   -> v1
    | ([], _::_)   -> v2
    | (_::_, _::_) -> List.map2 (fun x y -> x +. y) v1 v2
;;

let (mplus : matrix -> matrix -> matrix) =
  fun m1 m2 ->
    match (m1, m2) with 
      ([], []) -> []
    | (_::_, []) -> m1
    | ([], _::_) -> m2
    | (_::_, _::_) -> List.map2 (fun x y -> vplus x y) m1 m2
;;

(* helper function that sums up a vector *)
let (sumVec : vector -> float) =
  fun v ->
    match v with
      [] -> 0.
    | _::_ -> List.fold_left (+.) 0. v
;;

let (dotprod : vector -> vector -> float) =
  fun v1 v2 ->
    match (v1, v2) with
      ([], [])     -> 0.
    | (_::_, [])   -> 0.
    | ([], _::_)   -> 0.
    | (_::_, _::_) -> let mat = List.map2 (fun x y -> x *. y) v1 v2
                      in sumVec mat
;;

(* transpose helper function to return an empty matrix *)
let (empty_matrix : vector -> 'a list list) =
  fun vec ->
    List.map (fun x -> []) vec
;;

let (transpose : matrix -> matrix) =
  fun m ->
    match m with
      []   -> []
    | _::_ -> List.fold_right (fun v acc1 -> List.map2 (fun e rl -> e::rl) v acc1) m (empty_matrix (List.hd m))
;;

(* mmult helper function that adds everything inside the vector *)
let (vadd : vector -> float) =
  fun v ->
    List.fold_left (+.) 0. v
;;

(* helper function that does mult *)
let (hmult : vector -> matrix -> vector) =
  fun v1 m ->
    List.map (fun v2 -> vadd (List.map2 (fun x y -> x *. y) v1 v2)) m
;;

let (mmult : matrix -> matrix -> matrix) =
  fun m1 m2 ->
    match (m1, m2) with
      ([], [])     -> []
    | (_::_, [])   -> []
    | ([], _::_)   -> []
    | (_::_, _::_) -> let mt = transpose m2
                      in List.map (fun x -> (hmult x mt)) m1
;;

(* Problem 2: Calculators *)           
           
(* a type for arithmetic expressions *)
type op = Plus | Minus | Times | Divide
type exp = Num of float | BinOp of exp * op * exp

let rec (evalExp : exp -> float) =
  fun e ->
    match e with
      Num(f)         -> f
    | BinOp(l, o, r) -> match o with
                          Plus   -> evalExp l +. evalExp r
                        | Minus  -> evalExp l -. evalExp r
                        | Times  -> evalExp l *. evalExp r
                        | Divide -> evalExp l /. evalExp r
;;

(* a type for stack instructions *)	  
type instr = Push of float | Swap | Calculate of op

(* execute implementation + helper functions *)
(* helper function to push onto stack *)
let rec (fpush : float -> float list -> float list) =
  fun f stack ->
    match stack with
      []   -> [f]
    | h::t -> f::h::t
;;

(* helper function to perform swapping *)
let rec (fswap : float list -> float list) =
  fun stack ->
    match stack with
      []        -> stack
    | [h]       -> stack
    | h1::h2::t -> h2::h1::t
;;

(* helper function to perform actual arithmetics *)
let (arith : float -> float -> op -> float) =
  fun x y o ->
    match o with
      Plus   -> y +. x
    | Minus  -> y -. x
    | Times  -> y *. x
    | Divide -> y /. x
;;

(* helper function to perform arithmetic calculation *)
let rec (fcalc : op -> float list -> float list) =
  fun o stack ->
    match stack with
      [] -> stack
    | [h] -> stack
    | h1::h2::t -> (arith h1 h2 o)::t
;;

(* helper function to perform execution *)
let rec (run : instr list -> float list -> float) =
  fun instrLst stack ->
    match instrLst with
      []   -> List.hd stack (*we are done, so return top of stack*)
    | h::t -> match h with
                Push(f)       -> run t (fpush f stack)
              | Swap          -> run t (fswap stack)
              | Calculate(op) -> run t (fcalc op stack)
;;

let (execute : instr list -> float) =
  fun instrLst ->
    run instrLst []
;;


(* compile implementation + helper functions *) 
let (op2instr : op -> instr) =
  fun o ->
    match o with
      Plus   -> Calculate Plus
    | Minus  -> Calculate Minus
    | Times  -> Calculate Times
    | Divide -> Calculate Divide
;;

let rec (compile : exp -> instr list) =
  fun e ->
    match e with
      Num(f)         -> [Push f]
    | BinOp(l, o, r) -> compile l@compile r@([op2instr o])
;;

(* decompile implementation + helper functions *)
(* helper function to push onto exp stack *)
let rec (dpush : float -> exp list -> exp list) =
  fun f stack ->
    match stack with
      []   -> [Num f]
    | h::t -> Num f::h::t
;;

(* helper function to perform exp swapping *)
let rec (dswap : exp list -> exp list) =
  fun stack ->
    match stack with
      []        -> stack
    | [h]       -> stack
    | h1::h2::t -> h2::h1::t
;;

(* helper function to create the BinOp tuple *)
let (create_exp : exp -> exp -> op -> exp) =
  fun x y o ->
    BinOp(y, o, x)
;;

(* helper function to perform arithmetic calculation *)
let rec (dcalc : op -> exp list -> exp list) =
  fun o stack ->
    match stack with
      [] -> stack
    | [h] -> stack
    | h1::h2::t -> (create_exp h1 h2 o)::t
;;

(* helper function to perform decompile *)
let rec (decomph : instr list -> exp list -> exp) =
  fun instrl stack ->
    match instrl with 
      []   -> List.hd stack (*we are done, so return top of stack*)
    | h::t -> match h with
                Push(f)       -> decomph t (dpush f stack)
              | Swap          -> decomph t (dswap stack)
              | Calculate(op) -> decomph t (dcalc op stack)
;;

let (decompile : instr list -> exp) =
  fun instrl ->
    decomph instrl []
;;

(*EXTRA CREDIT *) 

(* helper function to tell whether to go left or right *)
(* let rec (height : exp -> int) =
  fun e -> match e with
    Num(f) -> 1
  | BinOp(l, _, r) ->
      let hl = height l in
      let hr = height r in 
      if hl > hr
      then 1 + hl
      else 1 + hr
;; *)

(* let (compileOpt : exp -> (instr list * int)) =
  fun e -> 
    match e with
      Num(f)         -> ([Push f], 1)
    | BinOp(l, o, r) -> if height l > height r 
                        then (compile l@compile r@([op2instr o]), height l)
                        else if height l < height r
                          then match o with
                                Plus   -> (compile r@compile l@([op2instr o]), height r)
                              | Minus  -> (compile r@compile l@[Swap]@([op2instr o]), height r)
                              | Times  -> (compile r@compile l@([op2instr o]), height r)
                              | Divide -> (compile r@compile l@[Swap]@([op2instr o]), height r)
                        else (compile l@compile r@([op2instr o]), 1 + height l)
;; *)

let rec (compileOpt : exp -> (instr list * int)) =
  fun e -> 
    match e with
      Num(f)         -> ([Push f], 1)
    | BinOp(l, o, r) -> let (lhs, ls) = compileOpt l in
                        let (rhs, rs) = compileOpt r in 
                        if ls > rs 
                        then (lhs@rhs@([op2instr o]), ls)
                        else if ls < rs
                          then match o with
                                Plus   -> (rhs@lhs@([op2instr o]), rs)
                              | Minus  -> (rhs@lhs@[Swap]@([op2instr o]), rs)
                              | Times  -> (rhs@lhs@([op2instr o]), rs)
                              | Divide -> (rhs@lhs@[Swap]@([op2instr o]), rs)
                        else (lhs@rhs@([op2instr o]), 1 + ls)
;;
