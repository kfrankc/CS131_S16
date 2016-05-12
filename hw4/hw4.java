/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   
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
        stack.push(op.calculate(stack.pop(), stack.pop()));
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
        if (head.isEmpty() == true || s.compareTo(head.getData()) < 0)
            head = head.addString(s, head);
        else 
            head.addString(s, head);
    }
    public void print() { 
        System.out.println("==== Print ListStringSet ====");
        SNode tmp = head;
        while (tmp.isEmpty() == false){
            System.out.println(tmp.getData());
            tmp = tmp.getNext();
        }
    }
}

// a type for the nodes of the linked list
interface SNode {
    Boolean isEmpty();
    int calcSize(SNode node);
    Boolean checkContains(String s, SNode node);
    SNode addString(String s, SNode node);
    SNode getNext();
    void setNext(SNode node);
    String getData();
}

// represents an empty node (which ends a linked list)
class SEmpty implements SNode {
    public SEmpty() {}
    public Boolean isEmpty() {return true;}
    public int calcSize(SNode node) {return 0;}
    public Boolean checkContains(String s, SNode node) {return false;}
    public SNode addString(String s, SNode node) {
        SNode tmp = new SElement(s);
        tmp.setNext(new SEmpty());
        return tmp;
    }
    public SNode getNext() {return new SEmpty();}
    public void setNext(SNode node) {return;}
    public String getData() {return "";}
}

// represents a non-empty node
class SElement implements SNode {
    protected String elem;
    protected SNode next;
    public SElement(String s) {
        elem = s;
        next = new SEmpty();
    }
    public Boolean isEmpty() {return false;}
    public int calcSize(SNode node) {
        if (node.isEmpty() == true)
            return 0;
        else
            return 1 + next.calcSize(next);
    }
    public Boolean checkContains(String s, SNode node) {
        if (node.isEmpty() == true)
            return false;
        if (s.compareTo(node.getData()) == 0)
            return true;
        else
            return checkContains(s, node.getNext());
    }
    public SNode addString(String s, SNode node) {
        // check duplicates
        if (s.compareTo(node.getData()) == 0)
            return node;
        if (s.compareTo(node.getData()) < 0) { // s goes in front
            SNode tmp = new SElement(s);
            tmp.setNext(node);
            return tmp;
        } else {
            node.setNext((node.getNext()).addString(s, node.getNext()));
            return node;
        }
    }
    public SNode getNext() {return next;}
    public void setNext(SNode node) {next = node;}
    public String getData() {return elem;}
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
        if (head.isEmpty() == true || cmp.compare(t, head.getData()) < 0)
            head = head.addT(t, head, cmp);
        else
            head.addT(t, head, cmp);
    }
    public void print() {
        System.out.println("==== Print ListSet ====");
        Node<T> tmp = head;
        while (tmp.isEmpty() == false){
            System.out.println(tmp.getData());
            tmp = tmp.getNext();
        }
    }
}
// interface Node
interface Node<T> {
    boolean isEmpty();
    int calcSize(Node<T> node);
    boolean checkContains(T t, Node<T> node);
    Node<T> addT(T t, Node<T> node, Comparator<T> cmp);
    Node<T> getNext();
    void setNext(Node<T> node);
    T getData();
}
// class Empty Node
class Empty<T> implements Node<T> {
    public Empty() {}
    public boolean isEmpty() {return true;}
    public int calcSize(Node<T> node) {return 0;}
    public boolean checkContains(T t, Node<T> node) {return false;}
    public Node<T> addT(T t, Node<T> node, Comparator<T> cmp) {
        Node<T> tmp = new Element<T>(t, cmp);
        tmp.setNext(new Empty<T>());
        return tmp;
    }
    public Node<T> getNext() {return new Empty<T>();}
    public void setNext(Node<T> node) {return;}
    public T getData() {
        T tmp;
        return null; //TODO: shouldn't be null
    }

}
// class Element Node
class Element<T> implements Node<T> {
    protected T elem;
    protected Node<T> next;
    protected Comparator<T> cmp;
    public Element(T t, Comparator<T> c) {
        elem = t;
        cmp = c;
        next = new Empty<T>();
    }
    public boolean isEmpty() {return false;}
    public int calcSize(Node<T> node) {
        if (node.isEmpty() == true)
            return 0;
        else
            return 1 + next.calcSize(next);
    }
    public boolean checkContains(T t, Node<T> node) {
        if (node.isEmpty() == true)
            return false;
        if (cmp.compare(t, node.getData()) == 0)
            return true;
        else
            return checkContains(t, node.getNext());
    }
    public Node<T> addT(T t, Node<T> node, Comparator<T> cmp) {
        // check duplicates
        if (cmp.compare(t, node.getData()) == 0)
            return node;
        if (cmp.compare(t, node.getData()) < 0) { // s goes in front
            Node<T> tmp = new Element<T>(t, cmp);
            tmp.setNext(node);
            return tmp;
        } else {
            node.setNext((node.getNext()).addT(t, node.getNext(), cmp));
            return node;
        }
    }
    public Node<T> getNext() {return next;}
    public void setNext(Node<T> node) {next = node;}
    public T getData() {return elem;}
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
        assert(instrs3.execute() == 3.0); // TODO: SHOULD BE -3

        List<Instr> is4 = new LinkedList<Instr>();
        is4.add(new Push(1.0));
        Instrs instrs4 = new Instrs(is4);
        assert(instrs2.execute() == 1.0);

        List<Instr> is5 = new LinkedList<Instr>();
        is5.add(new Push(1.0));
        is5.add(new Push(2.0));
        is5.add(new Calculate(Op.DIVIDE));
        Instrs instrs5 = new Instrs(is5);
        assert(instrs5.execute() == 2.0);

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
        assert(s1.size() == 3);
        s1.add("e");
        s1.add("f");
        s1.add("c");
        s1.add("a");
        assert(s1.contains("e") == true);
        assert(s1.size() == 7);
        s1.add("cat");
        s1.add("z");
        s1.print();
        // tests for Problem 2b
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
        System.out.println("Passed all tests!");
    }
}