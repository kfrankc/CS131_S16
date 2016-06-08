

/* Object Oriented Programming (In Java)

Java version 8

SEAS machines: /usr/local/cs/bin/{java,javac}

three key concepts:

subtyping
inheritance
dynamic dispatch

key idea:

everything is an object
objects only communicate by sending messages

separation of interface and implementation

*/

import java.util.*;

// a type for set of strings
interface Set {
	boolean contains(String s);
	void add(String s);
}

// an implementation of the Set type
class ListSet implements Set {
	protected List<String> elems = 
	new LinkedList<String>();

	public boolean contains(String s) {
		return elems.contains(s);
	}

	public void add(String s) {
		if(this.contains(s))
			return;
		else
			elems.add(s);
	}

}

/* 

Two forms of polymorphism:

- parametric polymorphism:
  some type is parameterized by one or more
  type variables

  client can choose how to instantiate type variables

  example: PolySet below.

*/

// T is a type variable, like 'a in OCaml
// clients can instantiate it, e.g. 
//    PolySet<String>
interface PolySet<T> {
	boolean contains(T t);
	void add(T t);
}  

/* 
   Java has a second kind of polymorphism:

   - subtype polymorphism

   there's a *subtype* relation on types

   if S is a subtype of T, then you can pass
   an object of type S where an object of type T
   is expected.

   example: RemovableSet is a subtype of Set.

*/

interface RemovableSet extends Set {
	void remove(String s);
}

/* Inheritance

   a mechanism for code reuse in implementations
*/

class RemovableListSet extends ListSet 
	implements RemovableSet {

	public void remove(String s) {
		elems.remove(s);
	}
}


class Client {

	void m(Set s) {
		if(s.contains("hi"))
			s.add("bye");
	}

	void n(RemovableSet rs) {
		this.m(rs);
	}

	void o(PolySet<String> s) {}


}


/* Inheritance versus Subtyping */


// // want subtyping without inheritance:

// class Rectangle {

// 	double width;
// 	double length;

// 	//...
// }

// class Square { 
// 	// ... 
// }

// // goal: Squares should be able to be passed wherever
// // Rectangles are expected, but don't want 
// // Square to inherit from Rectangle

// // one solution:

// interface Shape {

// 	double area();
// 	double perimeter();
// 	// ...

// }

// class Rectangle implements Shape { ... }

// class Square implements Shape { ... }

// class MyClient {
// 	void m(Shape s) { ... }
// }


// // want inheritance without subtyping

// // Suppose we have ListSet

// class ListSet {
// 	protected List<String> elems = 
// 	new LinkedList<String>();

// 	public boolean contains(String s) {
// 		return elems.contains(s);
// 	}

// 	public void add(String s) {
// 		if(this.contains(s))
// 			return;
// 		else
// 			elems.add(s);
// 	}
// }

//  we want to implement bags
//    - sets that can contain duplicates


// /* claim: we want ListBag to inherit code from 
//    ListSet, but ListBag should not be a subtype
//    of ListSet */

// // one option: Bag has a field of type Set
// // sort of works, but need a lot of 
// // boilerplate code to forward calls to the Set

// class ListBag {
// 	ListSet l;

// 	boolean contains(String s) {
// 		return l.contains(s);
// 	}

// }   

// // another option: shared superclass

// abstract class Collection {
// 	protected List<String> elems = 
// 	new LinkedList<String>();

// 	public boolean contains(String s) {
// 		return elems.contains(s);
// 	}
// 	// ... other method implementations

// 	abstract void add(String s);

// 	void addAll(String[] a) {
// 		for(String s : a)
// 			this.add(s);
// 	}
// }

// class ListSet extends Collection {
// 	void add(String s) { ... }

// }

// class ListBag extends Collection {
// 	void add(String s) { ... }
// }







