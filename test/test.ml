(* let joindicts l1 l2 =
	List.fold_right (fun x acc -> match x with
								  (k,v) -> (let w = get1 v l2 in
										match w with
											None -> []
										  | Some val -> acc @ [(k, val)])) l1 []
 *)

type tree = Leaf | Node of int * tree list
 let rec incTree n t =
         match t with
           Leaf -> Leaf
         | Node(d,children) -> Node(d+n, List.map (fun x -> incTree n x) children)