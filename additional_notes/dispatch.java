
/* every single method call is dynamically dispatched.
   invokes the "best" method implementation based on
   the class of the receiver object.
*/

class C {
	void n() { this.m(); }
	void m() { System.out.println("C"); }
}

class D extends C {
	void m() { System.out.println("D"); }
	void foo() { System.out.println("foo"); }
}

class Main {
	public static void main(String[] args) {
		C c = new C();
		D d = new D();
		C c2 = new D();
		c.n();
		d.n();
		c2.n();

	}
}


