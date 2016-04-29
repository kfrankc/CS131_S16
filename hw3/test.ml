
(* A simple test harness for the MOCaml interpreter. *)

(* put your tests here:
   each test is a pair of a MOCaml declaration and the expected
   result:
     - the MOCaml declaration is a string of exactly what you would type into the interpreter prompt,
       without the trailing ";;"
     - the expected result is a string of exactly what you expect the interpreter to print as a result
   use the string "dynamic type error" as the result if a DynamicTypeError is expected to be raised.
   use the string "match failure" as the result if a MatchFailure is expected to be raised.
   use the string "implement me" as the result if an ImplementMe exception is expected to be raised

   call the function runtests() to run these tests
*)
let tests = [
    (* YOU NEED TO ADD A LOT MORE TESTS! *)
		("3", "3"); 
		("false", "false");
    ("let x = 5", "val x = 5");
    ("let rec f x = if x = 0 then 0 else f(x-1)", "val f = <fun>"); 
    ("f 10", "0");
		("let x = 34", "val x = 34");
		("y", "dynamic type error");
    ("x + 4", "38");
    ("let double = function x -> x * 2", "val double = <fun>");
    ("double 6", "12");
    ("let two = 2", "val two = 2");
    ("let addTwo = function x -> x + two", "val addTwo = <fun>");
    ("addTwo 5", "7");
    ("let two = 3", "val two = 3");
    ("addTwo 5", "7");
    ("let add = function a -> function b -> a + b", "val add = <fun>");
    ("add 10 (-3)", "7");
    ("let p = (1, 2)", "val p = (1, 2)");
    ("let leaf = Leaf", "val leaf = Leaf");
    ("let node = Node(Leaf, 1, Leaf)", "val node = Node (Leaf, 1, Leaf)");
    ("match x with 34 -> true | _ -> false", "true");
    ("match x with 35 -> true | _ -> false", "false");
    ("if true then 1 else 0", "1");
    ("match p with (a, b) -> a + b", "3");
    ("match p with (a, b, c) -> a + b + c", "match failure");
    ("match node with Node(l, v, r) -> (l, r)", "(Leaf, Leaf)");
    ("let iffPositive = function x -> if x > 0 then x else false", "val iffPositive = <fun>");
    ("iffPositive 3", "3");
    ("iffPositive (-3)", "false");
    ("let rec sumTree n = match n with Leaf -> 0 | Node(l, v, r) -> v + (sumTree l) + (sumTree r)", "val sumTree = <fun>");
    ("let a = Node(Leaf, 2, Leaf)", "val a = Node (Leaf, 2, Leaf)");
    ("let b = Node(Leaf, 3, Leaf)", "val b = Node (Leaf, 3, Leaf)");
    ("let c = Node(a, 11, b)", "val c = Node (Node (Leaf, 2, Leaf), 11, Node (Leaf, 3, Leaf))");
    ("let d = Node(Leaf, 5, Leaf)", "val d = Node (Leaf, 5, Leaf)");
    ("let root = Node(c, 100, d)", "val root = Node (Node (Node (Leaf, 2, Leaf), 11, Node (Leaf, 3, Leaf)), 100, Node (Leaf, 5, Leaf))");
    ("sumTree root", "121");
    ("let rec fib x = match x with 0 -> 0 | 1 -> 1 | n -> (fib (n-1)) + (fib (n-2))", "val fib = <fun>");
    ("fib 19", "4181");
    ("let rec fibIt a = function b -> function n -> if n > 0 then (fibIt b (a+b) (n-1)) else a", "val fibIt = <fun>");
    ("fibIt 0 1 19", "4181");

		("x + 4", "38");
    ("let y = 6", "val y = 6");
    ("x + y", "40");
    (* if statement checks *)
    ("if 1=1 then 2 else 3","2");
    ("if 1=2 then 2 else 3","3");
    ("if 1+2 then 2 else 3","dynamic type error");
    (* tuple checks *)
    ("(1+1, 2+2)","(2, 4)");
    ("(1,2)","(1, 2)");
    ("(1=1, 2+2)","(true, 4)");
    (* data checks *)
    ("Leaf","Leaf");
    ("Node 1","Node 1");
    ("Node (true, 1, Leaf)","Node (true, 1, Leaf)");
    (* function checks *)
    ("function x -> x", "<fun>");
    ("let add2 = function x -> x + 2", "val add2 = <fun>");
    ("add2 4", "6");
    ("let add = function (x,y) -> x + y", "val add = <fun>");
    ("add (1,2)","3");
    (* more function checks *)
    ("(function x -> (function y -> x*y) x) 5","25");
    ("let y = 100","val y = 100");
    ("let f = function x -> x + y","val f = <fun>");
    ("f 10","110");

    (* ryan's test case *)
    ("3", "3");
    ("false", "false");
    ("let x = 34", "val x = 34");
    ("x + 4", "38");
    ("1>3", "false");
    ("3>1", "true");
    ("3=3", "true");
    ("3=4", "false");
    ("true=true", "dynamic type error");
    ("-3", "-3");
    ("-x", "-34");
    ("if true then 3 else 4", "3");
    ("if false then 60 else x", "34");
    ("(1+3,4+7)","(4, 11)");
    ("match x with 34 -> true | _ -> false","true");
    ("Some 3","Some 3");
    ("None", "None");
    ("let inc = function x -> x + 1", "val inc = <fun>");
    ("inc 1", "2");
    ("inc 4", "5"); 

    (* Alex's *)
    ("let y = 7", "val y = 7");
    ("y", "7");
    ("x + 4", "38");
    ("-(1-2)", "1");
    ("let f = function x -> x", "val f = <fun>");
    ("let f2 = function y -> x", "val f2 = <fun>");
    ("f 100", "100");
    ("f2 100", "34");
    ("let inc = function x -> x+1", "val inc = <fun>");
    ("inc 2", "3");
    ("let tupAdd = function (x,y) -> x+y","val tupAdd = <fun>");
    ("tupAdd (1,2)","3");
    ("let conjFunct  = function ((x,y),b) -> if ((x+y)=b) then true else false","val conjFunct = <fun>");
    ("conjFunct ((1,1),2)","true");
    ("conjFunct ((1,1),1)","false"); 
    ("let addTwoArgs = (function x -> (function y -> x+y));;", "val addTwoArgs = <fun>");
    ("addTwoArgs 124 412", "536");
    ("addTwoArgs false 412", "dynamic type error");
    ("let matchThis = 7","val matchThis = 7");
    ("match matchThis with 4 -> false | 3 -> 3","match failure");
    ("match matchThis with 4 -> false | 3 -> 3 | _ -> true","true");
    ("let matchers = function x -> (function y -> match x with true -> y*y | 2 -> y*2 | -1 -> y-1 | _ -> y)","val matchers = <fun>");
    ("matchers true 7","49");
    ("let rec double i = i*2","val double = <fun>");
    ("let rec twice f = function x -> f(f(x))","val twice = <fun>");
    ("(twice double) 10","40");
    ("let rec fact x = match x with 0 -> 1 | x -> x * (fact (x-1))","val fact = <fun>");
    ("fact 10","3628800");
    ("let tree = function ojb -> match ojb with Leaf -> 0 | Node(x) -> x | _ -> -1","val tree = <fun>");
    ("tree Leaf","0");
    ("tree (Node(10))","10");
    ("let tupAdd = function tup -> match tup with (x,y) -> x+y | _ -> -1","val tupAdd = <fun>");
    ("tupAdd (5,12)","17");
    ("let rec sumtree n = match n with Leaf -> 0 | Node(left,val,right) -> (sumtree left) + val + (sumtree right) | _ -> false","val sumtree = <fun>");
    ("let tree = Node(Node(Leaf,7,Leaf),1,Leaf)","val tree = Node (Node (Leaf, 7, Leaf), 1, Leaf)");
    ("sumtree tree","8");

    ("match (3, 3) with (x,y) -> x + y","6");
    ("match (3, 3) with (x,y) -> x + y","6");
    ("match true with x -> if x then 6 else 3","6");
    ("match (3, 3) with (x,y) -> x + y","6");
    ("match false with _ -> 123", "123");
    ("match None with None -> 0 | Some(x) -> x","0");
    ("match Some(12) with None -> 0 | Some(x) -> x","12");
    ("match None with 3 -> 0 | 123 -> 14","match failure"); 
    ("1 + 1","2");
    ("2 * 2","4");
    ("1 - 1","0");
    ("true","true");
    ("let x = 12", "val x = 12");
    ("x","12");
    ("-1","-1");
    ("if 1>3 then 123 else 321","321");
    ("let three = (function a -> (function b -> (function c -> (a + b + c))));;","val three = <fun>");
    ("three 1 2 3", "6");
    ("three true 2 3", "dynamic type error");
    ("three 3 2 false", "dynamic type error");
    ("if (3 + 2) then true else false", "dynamic type error");
    ("-true", "dynamic type error");
    ("2 + true", "dynamic type error");
    ("notbound", "dynamic type error");
    ("true=true", "dynamic type error");
    ("let f = function (x,y) -> x + y", "val f = <fun>");
    ("f (1,1)", "2");
    ("f (true,1)", "dynamic type error")
		]

(* The Test Harness
   You don't need to understand the code below.
*)
  
let testOne test env =
  let decl = main token (Lexing.from_string (test^";;")) in
  let res = evalDecl decl env in
  let str = print_result res in
  match res with
      (None,v) -> (str,env)
    | (Some x,v) -> (str, Env.add_binding x v env)
      
let test tests =
  let (results, finalEnv) =
    List.fold_left
      (fun (resultStrings, env) (test,expected) ->
	let (res,newenv) =
	  try testOne test env with
	      Parsing.Parse_error -> ("parse error",env)
	    | DynamicTypeError _ -> ("dynamic type error",env)
	    | MatchFailure -> ("match failure",env)
	    | ImplementMe s -> ("implement me",env) in
	(resultStrings@[res], newenv)
      )
      ([], Env.empty_env()) tests
  in
  List.iter2
    (fun (t,er) r ->
      let out = if er=r then "ok" else "expected " ^ er ^ " but got " ^ r in
      print_endline
	(t ^ "....................." ^ out))
      tests results

(* CALL THIS FUNCTION TO RUN THE TESTS *)
let runtests() = test tests
  
