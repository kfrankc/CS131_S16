
class Exn1 extends Exception{}
class Exn2 extends Exception{}

class Client {

	void n() throws Exn2 {
		throw new Exn2();
	}

	void m(int i) throws Exn1 {
		if (i < 0)
			throw new Exn1();
		else if (i % 2 == 1) {
			try {
				n();
			} catch(Exn2 e) {
				// log the error...
			}
		}
	}
}