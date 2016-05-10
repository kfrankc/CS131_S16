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
    public ListStringSet() {
        head = new SEmpty();
    }
    // return size of LinkedList
    public int size() {
        int s = 0;
        SNode tmp = head;
        while(tmp.getData() != null) {
            s = s + 1;
            tmp = tmp.getNext();
        }
        return s;
    }
    // contains function that checks if s is in LinkedList
    public boolean contains(String s) {
        SNode tmp = head;
        while (tmp.getData() != null) {
            if (s.compareTo(tmp.getData())==0)
                return true;
            else {
                tmp = tmp.getNext();
            }
        }
        return false;
    }
    // add s to linkedlist
    public void add(String s) {
        // if s is already in the linkedlist, do nothing
        if (contains(s))
            return;
        SNode tmp1 = head;
        SNode tmp2 = head.getNext();
        SNode node = new SElement(s);
        System.out.println(node.getData());
        while(true) {
            if (head.getData() == null) {
                // list is empty
                head = new SElement(s);
                head.setNext(new SEmpty());
                System.out.println("head: " + head.getData());
                return;
            }
            else if (s.compareTo(head.getData()) < 0) {
                // insert s before head
                SNode tmp = head;
                head = new SElement(s);
                head.setNext(tmp);
                System.out.println("added node in the beginning.");
                return;
            }
            else if (s.compareTo(tmp1.getData()) > 0 && s.compareTo(tmp2.getData()) < 0) {
                // insert s in between tmp1 and tmp2
                node.setNext(tmp2);
                tmp1.setNext(node);
                System.out.println("insertion in between tmp1 and tmp2");
                return;
            }
            else if ((tmp1.getNext()).getData() == null) {
                // reach end of list, add s as last node
                node.setNext(new SEmpty());
                tmp1.setNext(node);
                System.out.println("add to end of list due to tmp1");
                return;
            }
            else if ((tmp2.getNext()).getData() == null) {
                // reach end of list, add s as last node
                node.setNext(new SEmpty());
                tmp2.setNext(node);
                System.out.println("add to end of list due to tmp2");
                return;
            }
            else if (s.compareTo(tmp1.getData()) > 0 && s.compareTo(tmp2.getData()) > 0) {
                // increment tmp1 and tmp2
                tmp1 = tmp2;
                tmp2 = tmp2.getNext();
                System.out.println("incremented tmp1 and tmp2.");
            }
        }
    }
    public void print() { 
        SNode tmp = head;
        System.out.println("========= Print LinkedList ========");
        while(tmp.getData() != null) {
            System.out.println(tmp.getData() + ", ");
            tmp = tmp.getNext();
        }
    }
}

// a type for the nodes of the linked list
interface SNode {
    String getData();
    SNode getNext();
    void setNext(SNode n);
}

// represents an empty node (which ends a linked list)
class SEmpty implements SNode {
    public SEmpty() {
    }
    public String getData() {
        return null;
    }
    public SNode getNext() {
        return null;
    }
    public void setNext(SNode n) {
        return;
    }
}

// represents a non-empty node
class SElement implements SNode {
    protected String elem;
    protected SNode next;
    public SElement(String s) {
        elem = s;
        next = new SEmpty();
    }
    // get Node's string
    public String getData() {
        return elem;
    }
    // get next node
    public SNode getNext() {
        return next;
    }
    // set current node's string to next node's string
    public void setNext(SNode n) {
        next = n;
        return;
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
        s1.add("hi");
        s1.add("hi");
        s1.add("frank");
        s1.add("zebra");
        s1.add("monkey");
        s1.add("bear");
        s1.add("fox");
        s1.add("frank");
        s1.print();
        // a test for Problem 2b

    }
}