
// Exceptions in Java

interface List {
	void add(String s);
	String get(int i)
		throws OutOfBoundsAccess, Exn2;
}

class OutOfBoundsAccess extends Exception {}

class Exn2 extends Exception {}

class MyList implements List {

	public void add(String s) {}

	public String get(int i) 
		throws OutOfBoundsAccess {
		if (i < 0)
			throw new OutOfBoundsAccess();
//		else if (i > 20)
//			throw new Exn2();
		return "hi";
	}

	public String callsGet(int i) { 
			try {
				return this.get(i);
//			} catch(Exn2 e) {
//				return "exn2 error";
			} catch(OutOfBoundsAccess e) {
				return "negative number";
			}
	}

}

class Client {

	void m() throws OutOfBoundsAccess, Exn2 {

		int[] arr = new int[3];
		arr[-1] = 0;
		List l = new MyList();
		String s = l.get(-1);
	}

	public static void main(String[] args) 
		throws OutOfBoundsAccess {
		try {
			new Client().m();
		} catch(Exn2 e) {}
	}
}

/* Problem:

	I call a function get(...);
	it throws an exception.

	How do I get back to a safe state of memory,
	in order to continue executing?

	"exception safety"

*/

/*

class X {}
class Y {}

class XException extends Exception {}
class YException extends Exception {}

class Example {
	X x;
	Y y;

	void updateX() throws XException {
		// ... do some computation that may
		// throw an XException
		// update the value this.x
	}
	void updateY() throws YException {
		// ... do some computation that may
		// throw a YException
		// update the value this.y
	}

	// intended invariant:
	// either both x and y are updated,
	// or neither is
	void updateBoth() 
		throws XException, YException{
		X oldx = this.x;
		updateX();
	    try {
			updateY();
		} catch(YException ye) {
			this.x = oldx;
			throw ye;
		}
	}

	// intended invariant:
	// the file should always be closed
	// at the end of the function.
	void readFileAndCompute(File f) {
		String s = f.read();
		try {
			compute(s);
		} catch(Exn2 e) {
			// ...
		} finally {
			f.close();
		}
	}

}

*/
