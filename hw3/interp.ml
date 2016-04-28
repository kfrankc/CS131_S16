(* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   Professor Millstein Week 3 Monday, Wednesday, Week 4 Monday, Wednesday notes
   TA Saswat's github notes: https://github.com/SaswatPadhi/S16_TA_CS131
   
*)

(* EXCEPTIONS *)

(* This is a marker for places in the code that you have to fill in.
   Your completed assignment should never raise this exception. *)
exception ImplementMe of string

(* This exception is thrown when a type error occurs during evaluation
   (e.g., attempting to invoke something that's not a function).
   You should provide a useful error message.
*)
exception DynamicTypeError of string

(* This exception is thrown when pattern matching fails during evaluation. *)  
exception MatchFailure  

(* EVALUATION *)

(* See if a value matches a given pattern.  If there is a match, return
   an environment for any name bindings in the pattern.  If there is not
   a match, raise the MatchFailure exception.
*)
let rec patMatch (pat:mopat) (value:movalue) : moenv =
  match (pat, value) with
      (* an integer pattern matches an integer only when they are the same constant;
	 no variables are declared in the pattern so the returned environment is empty *)
      (IntPat(i), IntVal(j)) when i=j -> Env.empty_env()
    | (BoolPat(i), BoolVal(j)) when i=j -> Env.empty_env()
    | (WildcardPat, _) -> Env.empty_env() (* TODO: check this *)
    | (VarPat(i), IntVal(j)) -> let env = Env.empty_env() in 
                                let v = IntVal(j) in
                                Env.add_binding i v env
    | (VarPat(i), BoolVal(j)) -> let env = Env.empty_env() in 
                                 let b = BoolVal(j) in
                                 Env.add_binding i b env
    | (VarPat(i), FunctionVal(None, p, e, env)) -> Env.empty_env()
    | (VarPat(i), FunctionVal(Some s, p, e, env)) when i=s -> let rf = FunctionVal(Some s, p, e, env) in Env.add_binding s rf env
    | (TuplePat(i), TupleVal(j)) -> let env = Env.empty_env() in List.fold_left2 (fun e x y-> Env.combine_envs e (patMatch x y)) env i j
    | (DataPat(s1, p), DataVal(s2, v)) -> raise (ImplementMe "got to data")
    | _ -> raise MatchFailure

(* Evaluate an expression in the given environment and return the
   associated value.  Raise a MatchFailure if pattern matching fails.
   Raise a DynamicTypeError if any other kind of error occurs (e.g.,
   trying to add a boolean to an integer) which prevents evaluation
   from continuing.
*)
let rec evalExpr (e:moexpr) (env:moenv) : movalue =
  match e with
      (* an integer constant evaluates to itself *)
    IntConst(i)            -> IntVal(i)
    | BoolConst(b)         -> BoolVal(b)
    | BinOp(l, o, r)       -> let lt = evalExpr l env in
                                let rt = evalExpr r env in
                                (match (lt, rt) with
                                  (IntVal lv, IntVal rv)   -> (match o with
                                                              Plus -> IntVal(lv + rv)
                                                            | Minus -> IntVal(lv - rv)
                                                            | Times -> IntVal(lv * rv)
                                                            | Eq -> if lv = rv then BoolVal(true) else BoolVal(false)
                                                            | Gt -> if lv > rv then BoolVal(true) else BoolVal(false))
                                | (_, _) -> raise (DynamicTypeError "binary operators can only be applied to integers"))
    | Negate(e)            -> let v = evalExpr e env in
                                (match v with 
                                  IntVal i -> IntVal (-i)
                                  | _      -> raise (DynamicTypeError "can only negate integers"))
    | If(e1, e2, e3)       -> let result = evalExpr e1 env in
                                (match result with
                                  BoolVal(b) -> if b then (evalExpr e2 env) else (evalExpr e3 env)
                                | _ -> raise (DynamicTypeError "expression must return bool"))
    | Function(p, e)       -> FunctionVal(None, p, e, env)
    | FunctionCall(e1, e2) -> (match (evalExpr e1 env) with
                                FunctionVal(None, p, e, env')     -> evalExpr e (Env.combine_envs env (patMatch p (evalExpr e2 env)))
                                | FunctionVal(Some s, p, e, env') -> evalExpr e (Env.combine_envs env (patMatch p (evalExpr e2 env)))
                                | _                               -> raise (DynamicTypeError "undeclared function variable"))
    | Match(e, pl)         -> let rec listMatch (e:moexpr) (pl:(mopat * moexpr) list) (env:moenv) : movalue =
                                (match pl with
                                  [] -> raise MatchFailure
                                | (pattern, e2)::t -> try
                                                        evalExpr e2 (patMatch pattern (evalExpr e env))
                                                      with 
                                                        MatchFailure -> listMatch e t env)
                              in listMatch e pl env
    | Tuple(el)            -> TupleVal(List.map(fun e -> evalExpr e env) el)
    | Data(s, e)           -> (match e with
                                None -> DataVal(s, None)
                              | Some ev -> let rv = Some (evalExpr ev env) in 
                                              DataVal(s, rv))
    | Var(s)               -> (try
                                Env.lookup s env
                              with
                                Not_found -> raise (DynamicTypeError "TEST3 undeclared variable")
                              | _ -> raise (DynamicTypeError "TEST2 undeclared variable"))
    | _                    -> raise (DynamicTypeError "TEST1 undeclared variable")

(* Evaluate a declaration in the given environment.  Evaluation
   returns the name of the variable declared (if any) by the
   declaration along with the value of the declaration's expression.
*)
let rec evalDecl (d:modecl) (env:moenv) : moresult =
  match d with
      (* a top-level expression has no name and is evaluated to a value *)
      Expr(e) -> (None, evalExpr e env)
    | Let(s, e) -> (Some s, evalExpr e (Env.add_binding s (evalExpr e env) env))
    | LetRec(s, e) -> (Some s, evalExpr e (Env.add_binding s (evalExpr e env) env))
    | _ -> raise (ImplementMe "let and let rec not implemented")



