(* Practice Midterm Given in Class *)

type lazylist = Nil | Cons of int * (unit -> lazylist)

let head (Cons (x,f)) = x

let tail (Cons (x,f)) = f()

let rec intsFrom (n:int) : lazylist = 
	Cons(n, function() -> intsFrom (n+1))

let rec lazymap (func:int -> int) (l:lazylist) : lazylist =
	match l with
		Nil -> Nil
	  | Cons(x,f) -> Cons((func x), function() -> lazymap func (f()))
