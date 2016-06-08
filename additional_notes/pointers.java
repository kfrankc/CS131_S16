

class Point {
	int x;
	int y;

	Point(int x, int y) { this.x = x; this.y = y; }

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

class Client {

	public static void main(String[] args) {
		Point p1 = new Point(3,4);
		Point p2 = p1;
		p2.x = 0; // p1 will also be changed!
		System.out.println(p1);
	}

	public void m(Point p) {
		p = new Point(5,6);
	}

	public void m2() {
		Point p0 = new Point(3,4);
		m(p0);
	}

}