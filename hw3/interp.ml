(* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   Professor Millstein Week 3 Monday, Wednesday, Week 4 Monday, Wednesday notes
   TA Saswat's github notes: https://github.com/SaswatPadhi/S16_TA_CS131
   http://caml.inria.fr/pub/docs/manual-ocaml/libref/List.html
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
      (IntPat(i), IntVal(j)) when i=j   -> Env.empty_env()
    | (BoolPat(i), BoolVal(j)) when i=j -> Env.empty_env()
    (* a wildcard matches to anything *)
    | (WildcardPat, _)                  -> Env.empty_env()
    (* a varpat matches to anything and binds a name whatever it matches to *)
    | (VarPat(i), mv)                   -> let env = Env.empty_env() in 
                                 Env.add_binding i mv env
    (* recursievly match each element in a tuple, since it is a list *)
    | (TuplePat(i), TupleVal(j))        -> let env = Env.empty_env() in
                                          let pat_len = List.length i in 
                                          let val_len = List.length j in
                                          if pat_len=val_len then
                                            List.fold_left2 (fun e x y-> Env.combine_envs e (patMatch x y)) env i j
                                          else raise MatchFailure
    (* two cases for DataPat: None, where we return empty env, and Some p, where we recursively match p and v *)
    | (DataPat(s1, None), DataVal(s2, _)) | (DataPat(s1, _), DataVal(s2, None)) when s1=s2 -> Env.empty_env()
    | (DataPat(s1, Some p), DataVal(s2, Some v)) when s1=s2 -> (patMatch p v)
    | (_,_) | _ -> raise MatchFailure

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
    (* for BinOp, similar to prev hw, eval left and right, and match on it *)
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
    (* match the expression from e and only negate it if it is an integer *)
    | Negate(e)            -> let v = evalExpr e env in
                                (match v with 
                                  IntVal i -> IntVal (-i)
                                | _      -> raise (DynamicTypeError "only integers can be negated"))
    (* evaluate e1 and match result of that to either true or false; true means evaluate e2, false means evaluate e3 *)
    | If(e1, e2, e3)       -> let result = evalExpr e1 env in
                                (match result with
                                  BoolVal(b) -> if b then (evalExpr e2 env) else (evalExpr e3 env)
                                | _ -> raise (DynamicTypeError "expression must return bool"))
    (* simply return the FunctionVal movalue *)
    | Function(p, e)       -> FunctionVal(None, p, e, env)
    (* f is the function def passed in, arg is the value that will be passed into f and be evaluated *)
    | FunctionCall(f, arg) -> (match (evalExpr f env) with
                                  (* if the movalue of f is a functionval without a name, then there's no recursion *)
                                  FunctionVal(None, p, e, e_env)   -> 
                                    (* we want to evaluate the function expression e with the combined environment of:
                                          1. e's own environment AND
                                          2. arg's environment, where arg is bounded to the pattern p *)
                                    let arg_env = patMatch p (evalExpr arg env) in
                                      evalExpr e (Env.combine_envs e_env arg_env)
                                (* if movalue of f is a functionval with name s, then there's recursion *)
                                | FunctionVal(Some s, p, e, e_env) -> 
                                    (* s_val: the functionval of the recursive function
                                       arg_env: the environment for the arg
                                       s_env: the environment of the recursive function (combined of e's own environment and arg's environment) *)
                                    let s_val = FunctionVal(Some s, p, e, e_env) in
                                    let arg_env = patMatch p (evalExpr arg env) in
                                    let s_env = Env.combine_envs e_env arg_env in
                                      (* bind the recursive function's name to s_env *)
                                      evalExpr e (Env.add_binding s s_val s_env)
                                | _ -> raise (DynamicTypeError "undeclared function variable"))
    (* defined a recursive function to help with Match *)
    | Match(e, pl)         -> let rec listMatch (e:moexpr) (pl:(mopat * moexpr) list) (env:moenv) : movalue =
                                (* listMatch takes in e, pl, env and pattern matches on pl *)
                                (match pl with
                                  [] -> raise MatchFailure
                                  (* use try to evaluate e2 with the combined environment of current env and the env from matching pattern to the evaluated e *)
                                | (pattern, e2)::t -> try
                                                        evalExpr e2 (Env.combine_envs env (patMatch pattern (evalExpr e env)))
                                                      with 
                                                        MatchFailure -> listMatch e t env)
                              in listMatch e pl env
    (* tuple evaluation requires a List.map to apply the function that evaluates e on el *)
    | Tuple(el)            -> TupleVal(List.map(fun e -> evalExpr e env) el)
    (* for data, we want to match e on None or Some ev; if it's Some ev, then we need to return a DataVal, where the movalue of that is from evaluating ev *)
    | Data(s, e)           -> (match e with
                                None -> DataVal(s, None)
                              | Some ev -> let rv = Some (evalExpr ev env) in 
                                              DataVal(s, rv))
    (* for a variable, we want to look it up in the environment; if it doesn't exist, we can simply raise error *)
    | Var(s)               -> (try
                                Env.lookup s env
                              with
                                _ -> raise (DynamicTypeError "undeclared variable"))
    | _                    -> raise (DynamicTypeError "no expression is matched")

(* Evaluate a declaration in the given environment.  Evaluation
   returns the name of the variable declared (if any) by the
   declaration along with the value of the declaration's expression.
*)
let rec evalDecl (d:modecl) (env:moenv) : moresult =
  match d with
      (* a top-level expression has no name and is evaluated to a value *)
      Expr(e) -> (None, evalExpr e env)
    | Let(s, e) -> (Some s, evalExpr e env)
    (* when we do let rec, we want to store the function variable s into the FunctionVal *)
    | LetRec(s, e) -> (Some s, (match (evalExpr e env) with
                                  FunctionVal(_, p, e', env') -> FunctionVal(Some s, p, e', env')))
    | _ -> raise (ImplementMe "let and let rec not implemented")



