
// two kinds of polymorphism:
// parametric and subtype

// parametric: type variables

interface PolyList<T> {
	void add(T t);
	boolean contains(T t);
	T get(int i);
}

// subtype polymorphism:

interface Animal {
	void eat();
}

interface Cow extends Animal {
	void moo();
}

class Client {
	void doSomething(Animal a) {
		a.eat();
	}

}

// Why do we need parametric polymorhism?

interface ObjectList {
	void add(Object o);
	boolean contains(Object o);
	Object get(int i);
	// ...
}

// What is the difference between ObjectList
// and List<T> above?
/*
   1. ObjectList can contain any kind of object
      - a single list can contain Strings, Integers,
        Doubles, Foos, etc.
      List<String> ensures that we can only put in Strings

   2. Parametric polymorphism can correlate
      input and output types.  Subtyping can't.
      See the examples below.

*/

class Main {

	void m(PolyList<String> l1, ObjectList l2) {
		l1.add("hi");
		l1.add("bye");
//		l1.add(new Integer(43)); type error
		String s = l1.get(0);

		l2.add("hi");
		l2.add("bye");
		l2.add(new Integer(43));
		String s2 = (String) l2.get(0);


	}
}

// bounded parametric polymorphism
// combines subtyping and parametric polymorphism
class AnimalList<T extends Animal> 
	implements PolyList<T> {

	public void add(T t) {
		t.eat(); // allowed because T must be a subtype of Animal
	}

	public boolean contains(T t) { return false; }

	public T get(int i) { return null; }
}


// static overloading

class C {
	void m(String s) {}
	void m(Integer i) {}

	void n(Animal a) {} // m1
	void n(Cow c) {} // m2
}

class Main2 {

	void doit() {
		C c = new C();
		c.m("hi"); // method determined at compile time
		c.m(34);   // method determined at compile time
	}

	void doit2(Animal a, Cow cow) {
		C c = new C();
		c.n(a); // m1 method statically determined
		c.n(cow); // m2 method statically determined
		Animal a2 = cow;
		c.n(a2); // m1 method statically determined
				 // since it's based only on the static types
				//  of the arguments (in this case, C and Animal)
	}

}


class A {}
class B extends A {}

class X {
	void m(A a) { System.out.println("X,A"); }
}

class Y extends X {
	void m(A a) { System.out.println("Y,A"); }
	void m(B b) { System.out.println("Y,B"); }
}

class Stuff {
	public static void main(String[] args) {
		X x = new Y();
		x.m(new B());
	}
}

interface Greeter {
	void greet();
}
class Person implements Greeter {
	public void greet() { this.hello(new Integer(3)); }
	public void hello(Object o) { System.out.println("hello object"); }
}
class CSPerson extends Person {
	public void hello(Object o) { System.out.println("hello world!"); }
}
class FrenchPerson extends Person {
	public void hello(Object o) { System.out.println("bonjour object"); }
	public void hello(String s) { System.out.println("bonjour " + s); }
}
class Test {
	public static void main(String[] args) {
		Person p = new FrenchPerson(); 
		p.hello("joe");
	}
}




/* key point:
   if two methods have the same name but
   either different number of args or
   different parameters type, then they
   are treated as if they have different names.
*/

/*

two phases of method lookup:

static phase: based on the static types of the
parameters, we find the right "method family"
 - method name plus argument types

dynamic phase: dynamic dispatch on the receiver
object, over the methods from the statically
chosen method family.

*/

/* 

OO style: each object knows how to do certain
things.  specified by some kind of interface.

clients should be able to manipulate the object
just through that interface.

*/

