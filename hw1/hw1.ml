(*
  Resources Used:
  https://ocaml.org/learn/tutorials/basics.html
  https://ocaml.org/learn/tutorials/common_error_messages.html
  http://stackoverflow.com/questions/33923/what-is-tail-recursion
  https://github.com/SaswatPadhi/S16_TA_CS131/blob/master/Notes/Sec01_Apr01/notes_01.md
  https://github.com/SaswatPadhi/S16_TA_CS131/blob/master/Notes/Sec01_Apr01/probs_01.ml
  Professor Millstein Week 1 Monday Lecture Notes
  Professor Millstein Week 1 Wednesday Lecture Notes
  Exchanged some test cases with student Ky-Cuong Huynh
  Used test cases posted on Piazza
*)

(* Problem 1 *)
            
let rec (member : 'a -> 'a list -> bool) =
  fun x s ->
    match s with
      []   -> false
    | h::t -> if h = x then true else member x t
;;

let rec (add : 'a -> 'a list -> 'a list) =
  fun x s ->
    match s with
      []   -> [x]
    | h::t -> if h = x then s else h::add x t
;;

let rec (union : 'a list -> 'a list -> 'a list) =
  fun s1 s2 ->
    match (s1, s2) with
      ([],[])          -> []
    | ([], _::_)       -> s2
    | (_::_, [])       -> s1
    | (h1::t1, h2::t2) -> union t1 (add h1 s2)
;;

let rec (fastUnion : 'a list -> 'a list -> 'a list) =
  fun s1 s2 ->
    match (s1, s2) with
      ([],[])          -> []
    | ([], _::_)       -> s2
    | (_::_, [])       -> s1
    | (h1::t1, h2::t2) -> if h1 = h2 then fastUnion t1 s2 
                          else if h1 < h2 then h1::fastUnion t1 s2
                          else h2::fastUnion s1 t2  
;;
                
let (intersection : 'a list -> 'a list -> 'a list) =
  fun s1 s2 -> 
    match (s1, s2) with
      ([], [])         -> []
    | ([], _::_)       -> []
    | (_::_, [])       -> []
    | (h1::t1, h2::t2) -> List.filter (fun (x) -> if member x s1 then true else false) s2
;;
         
let rec (setify : 'a list -> 'a list) =
  fun l ->
    match l with
      []   -> []
    | [h]  -> [h]
    | h::t -> if (member h t) then setify t else h::setify t
;;

let rec (powerset : 'a list -> 'a list list) =
  fun s ->
    match s with
      []   -> [[]]
    | h::t -> let s = powerset t in
                      s @ (List.map (fun x -> h :: x) s)
;;
       
(* Problem 2 *)
let rec (partition : ('a -> bool) -> 'a list -> 'a list * 'a list) =
  fun f l ->
    match l with
      []   -> ([], [])
    | [h]  -> if f h then ([h], []) else ([], [h])
    | hd::tl -> match partition f tl with
                  (l1, l2) -> if f hd then (hd::l1, l2) else (l1, hd::l2)
;;

let rec (whle : ('a -> bool) -> ('a -> 'a) -> 'a -> 'a) =
  fun p f x ->
    if p x then 
      let t = f x in 
        whle p f t 
    else x
;;

let rec (pow : int -> ('a -> 'a) -> ('a -> 'a)) =
  fun n f -> 
    match n with
      0 -> fun x -> x
    | k -> fun x -> f ((pow (k-1) f) x)
;;