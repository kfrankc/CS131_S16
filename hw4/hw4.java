/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   http://www.tutorialspoint.com/java/java_string_compareto.htm
   https://docs.oracle.com/javase/7/docs/api/java/util/Comparator.html
   http://stackoverflow.com/questions/10179449/what-is-parametric-polymorphism-in-java-with-example
   Professor Millstein Lecture Notes Week 6 Wednesday, Week 7 Monday, & Week 7 Wednesday
   
*/

// import lists and other data structures from the Java standard library
import java.util.*;
// PROBLEM 1

// a type for arithmetic expressions
interface Exp {
    double eval(); 	                       // Problem 1a
    List<Instr> compile(); 	               // Problem 1c
}

class Num implements Exp {
    protected double val;
    // constructor that sets val to input
    public Num(double input) {
        val = input;
    }
    // function that returns val, since no computation work is needed
    public double eval() {
        return val;
    }

    // Problem 1c
    public List<Instr> compile() {
        List<Instr> result = new ArrayList<Instr>();
        Instr tmp = new Push(val);
        result.add(tmp);
        return result;
    }

    public boolean equals(Object o) { return (o instanceof Num) && ((Num)o).val == this.val; }

    public String toString() { return "" + val; }
}

class BinOp implements Exp {
    protected Exp left, right;
    protected Op op;
    // constructor
    public BinOp(Exp l, Op o, Exp r) {
        left = l;
        right = r;
        op = o;
    }
    // function that uses the op.calculate method
    public double eval() {
        return op.calculate(left.eval(), right.eval());
    }

    // Problem 1C
    public List<Instr> compile() {
        // result is variable where we'll return after computation
        List<Instr> result = new ArrayList<Instr>();
        List<Instr> l = left.compile();
        List<Instr> r = right.compile();
        // loop through both left and right expressions
        for (Instr instr : l)
            result.add(instr);
        for (Instr instr : r)
            result.add(instr);
        result.add(new Calculate(op));
        return result;
    }

    public boolean equals(Object o) {
    	if(!(o instanceof BinOp))
    		return false;
    	BinOp b = (BinOp) o;
    	return this.left.equals(b.left) && this.op.equals(b.op) &&
		    	this.right.equals(b.right);
    }

    public String toString() {
		return "BinOp(" + left + ", " + op + ", " + right + ")";
    }
}

// a representation of four arithmetic operators
enum Op {
    PLUS { public double calculate(double a1, double a2) { return a1 + a2; } },
    MINUS { public double calculate(double a1, double a2) { return a1 - a2; } },
    TIMES { public double calculate(double a1, double a2) { return a1 * a2; } },
    DIVIDE { public double calculate(double a1, double a2) { return a1 / a2; } };

    abstract double calculate(double a1, double a2);
}

// a type for arithmetic instructions
interface Instr {
    // we need an abstract class for runInstr
    void runInstr(Stack<Double> stack);
    String toString();
}

class Push implements Instr {
    protected double val;
    // constructor
    public Push(double v) {
        val = v;
    }
    // uses the stack to push values on
    public void runInstr(Stack<Double> stack) {
        stack.push(val);
    }

	public boolean equals(Object o) { return (o instanceof Push) && ((Push)o).val == this.val; }

    public String toString() {
		return "Push " + val;
    }

}

class Calculate implements Instr {
    protected Op op;
    // constructor
    public Calculate(Op o) {
        op = o;
    }

    public void runInstr(Stack<Double> stack) {
        double exp1 = stack.pop();
        double exp2 = stack.pop();
        stack.push(op.calculate(exp2, exp1));
    }

    public boolean equals(Object o) { return (o instanceof Calculate) && 
    						  ((Calculate)o).op.equals(this.op); }

    public String toString() {
		return "Calculate " + op;
    }
}

class Instrs {
    protected List<Instr> instrs;

    public Instrs(List<Instr> instrs) { this.instrs = instrs; }

    // execute function calls runInstr, which in turn calls Push or Calculate
    public double execute() {
        Stack<Double> nums = new Stack<Double>();
        for (Instr instr : instrs) {
            instr.runInstr(nums);
        }
        return nums.pop();
    }  // Problem 1b
}

// PROBLEM 2

// the type for a set of strings
interface StringSet {
    int size();
    boolean contains(String s);
    void add(String s);
    void print();
}

// an implementation of StringSet using a linked list
class ListStringSet implements StringSet {
    protected SNode head;
    public ListStringSet() {head = new SEmpty();}
    // return size of LinkedList
    public int size() {return head.calcSize(head);}
    // contains function that checks if s is in LinkedList
    public boolean contains(String s) {return head.checkContains(s, head);}
    // add s to linkedlist
    public void add(String s) {
        head = head.addString(s, head);
    }
    public void print() {
        System.out.println("==== Print ListStringSet ====");
        head.print(head);
    }
}

// a type for the nodes of the linked list
interface SNode {
    int calcSize(SNode node);
    Boolean checkContains(String s, SNode node);
    SNode addString(String s, SNode node);
    void print(SNode node);
}

// represents an empty node (which ends a linked list)
class SEmpty implements SNode {
    public SEmpty() {}
    public int calcSize(SNode node) {return 0;}
    public Boolean checkContains(String s, SNode node) {return false;}
    public SNode addString(String s, SNode node) {
        SNode next = new SEmpty();
        SNode tmp = new SElement(s, next);
        return tmp;
    }
    public void print(SNode node) {return;}
}

// represents a non-empty node
class SElement implements SNode {
    protected String elem;
    protected SNode next;
    public SElement(String s, SNode node) {
        elem = s;
        next = node;
    }
    public int calcSize(SNode node) {
        return 1 + next.calcSize(next);
    }
    public Boolean checkContains(String s, SNode node) {
        if (s.compareTo(elem) == 0)
            return true;
        else
            return next.checkContains(s, next);
    }
    public SNode addString(String s, SNode node) {
        // check duplicates
        if (s.compareTo(elem) == 0)
            return node;
        if (s.compareTo(elem) < 0) { // s goes in front
            SNode tmp = new SElement(s, node);
            return tmp;
        } else {
            next = next.addString(s, next);
            return node;
            // node.setNext((node.getNext()).addString(s, node.getNext()));
            // return node;
        }
    }
    public void print(SNode node) {
        System.out.println(elem);
        next.print(next);
    }
}

// Part 2b
// the type for a set
interface Set<T> {
    int size();
    boolean contains(T t);
    void add(T t);
    void print();
}
// class ListSet
class ListSet<T> implements Set<T> {
    protected Node<T> head;
    protected Comparator<T> cmp;
    public ListSet(Comparator<T> c) {
        head = new Empty<T>();
        cmp = c;
    }
    public int size() {return head.calcSize(head);}
    public boolean contains(T t) {return head.checkContains(t, head);}
    public void add(T t) {
        head = head.addT(t, head, cmp);
    }
    public void print() {
        System.out.println("==== Print ListSet ====");
        head.print(head);
    }
}
// interface Node
interface Node<T> {
    int calcSize(Node<T> node);
    Boolean checkContains(T t, Node<T> node);
    Node<T> addT(T t, Node<T> node, Comparator<T> cmp);
    void print(Node<T> node);
}
// class Empty Node
class Empty<T> implements Node<T> {
    public int calcSize(Node<T> node) {return 0;}
    public Boolean checkContains(T t, Node<T> node) {return false;}
    public Node<T> addT(T t, Node<T> node, Comparator<T> cmp) {
        Node<T> next = new Empty<T>();
        Node<T> tmp = new Element<T>(t, next, cmp);
        return tmp;
    }
    public void print(Node<T> node) {return;}
}
// class Element Node
class Element<T> implements Node<T> {
    protected T elem;
    protected Node<T> next;
    protected Comparator<T> cmp;
    public Element(T t, Node<T> node, Comparator<T> c) {
        elem = t;
        cmp = c;
        next = node;
    }
    public int calcSize(Node<T> node) {
        return 1 + next.calcSize(next);
    }
    public Boolean checkContains(T t, Node<T> node) {
        if (cmp.compare(t, elem) == 0)
            return true;
        else
            return next.checkContains(t, next);
    }
    public Node<T> addT(T t, Node<T> node, Comparator<T> cmp) {
        // check duplicates
        if (cmp.compare(t, elem) == 0)
            return node;
        if (cmp.compare(t, elem) < 0) { // s goes in front
            Node<T> tmp = new Element<T>(t, node, cmp);
            return tmp;
        } else {
            next = next.addT(t, next, cmp);
            return node;
        }
    }
    public void print(Node<T> node) {
        System.out.println(elem);
        next.print(next);
    }
}

class CalcTest {
    public static void main(String[] args) {
        // tests for Problem 1a
        Exp exp  = new BinOp(new BinOp(new Num(1.0), Op.PLUS, new Num(2.0)), Op.TIMES, new Num(3.0));
        Exp exp2 = new BinOp(new BinOp(new Num(1.0), Op.PLUS, new Num(2.0)), Op.DIVIDE, new Num(3.0));
        Exp exp3 = new BinOp(new BinOp(new Num(1.0), Op.MINUS, new Num(2.0)), Op.TIMES, new Num(3.0));
        Exp exp4 = new Num(1.0);
        Exp exp5 = new BinOp(new Num(1.0), Op.DIVIDE, new Num(2.0));
        assert(exp.eval() == 9.0);
        assert(exp2.eval() == 1.0);
        assert(exp3.eval() == -3.0);
        assert(exp4.eval() == 1.0);
        assert(exp5.eval() == 0.5);

        // tests for Problem 1b
        List<Instr> is = new LinkedList<Instr>();
        is.add(new Push(1.0));
        is.add(new Push(2.0));
        is.add(new Calculate(Op.PLUS));
        is.add(new Push(3.0));
        is.add(new Calculate(Op.TIMES));
        Instrs instrs = new Instrs(is);
        assert(instrs.execute() == 9.0);

        List<Instr> is2 = new LinkedList<Instr>();
        is2.add(new Push(1.0));
        is2.add(new Push(2.0));
        is2.add(new Calculate(Op.PLUS));
        is2.add(new Push(3.0));
        is2.add(new Calculate(Op.DIVIDE));
        Instrs instrs2 = new Instrs(is2);
        assert(instrs2.execute() == 1.0);

        List<Instr> is3 = new LinkedList<Instr>();
        is3.add(new Push(1.0));
        is3.add(new Push(2.0));
        is3.add(new Calculate(Op.MINUS));
        is3.add(new Push(3.0));
        is3.add(new Calculate(Op.TIMES));
        Instrs instrs3 = new Instrs(is3);
        assert(instrs3.execute() == -3.0);

        List<Instr> is4 = new LinkedList<Instr>();
        is4.add(new Push(1.0));
        Instrs instrs4 = new Instrs(is4);
        assert(instrs2.execute() == 1.0);

        List<Instr> is5 = new LinkedList<Instr>();
        is5.add(new Push(1.0));
        is5.add(new Push(2.0));
        is5.add(new Calculate(Op.DIVIDE));
        Instrs instrs5 = new Instrs(is5);
        assert(instrs5.execute() == 0.5);

        // a test for Problem 1c
        assert(exp.compile().equals(is));
        assert(exp2.compile().equals(is2));
        assert(exp3.compile().equals(is3));
        assert(exp4.compile().equals(is4));
        assert(exp5.compile().equals(is5));

        // a test for Problem 2a
        StringSet s1 = new ListStringSet();
        assert(s1.size() == 0);
        assert(s1.contains("a") == false);
        s1.add("ant");
        s1.add("b");
        s1.add("d");
        s1.add("y");
        assert(s1.size() == 4);
        s1.add("e");
        s1.add("f");
        s1.add("c");
        s1.add("a");
        assert(s1.contains("e") == true);
        assert(s1.size() == 8);
        s1.add("cat");
        s1.add("z");
        s1.print();
        // // tests for Problem 2b
        ListSet<Integer> sList = new ListSet<Integer>((Integer i1, Integer i2) -> i2 - i1);
        sList.add(1);
        sList.add(3);
        sList.add(-6);
        assert(sList.contains(100) == false);
        sList.add(100);
        sList.add(1);
        sList.add(100);
        assert(sList.contains(1) == true);
        assert(sList.size() == 4);
        sList.print();
        ListSet<String> sList2 = new ListSet<String>((String i1, String i2) -> i2.length() - i1.length());
        sList2.add("Frank");
        sList2.add("Dog");
        sList2.add("Yo");
        assert(sList2.contains("Frank") == true);
        sList2.add("Francisco");
        sList2.add("Bank");
        sList2.add("O");
        assert(sList2.contains("o") == true);
        assert(sList2.size() == 6);
        sList2.print();
        ListSet<String> sList3 = new ListSet<String>((String i1, String i2) -> i1.compareTo(i2));
        assert(sList3.size() == 0);
        assert(sList3.contains("a") == false);
        sList3.add("ant");
        sList3.add("b");
        sList3.add("d");
        sList3.add("y");
        assert(sList3.size() == 4);
        sList3.add("e");
        sList3.add("f");
        sList3.add("c");
        sList3.add("a");
        sList3.add("a");
        assert(sList3.contains("e") == true);
        assert(sList3.size() == 8);
        sList3.add("cat");
        sList3.add("zebra");
        sList3.print();
        System.out.println("Passed all tests!");
    }
}
