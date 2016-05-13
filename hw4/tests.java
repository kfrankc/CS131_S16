import java.util.*;
import java.util.List;

class Person {

    private int rank;
    private String name;
    Person(int rank,String name) {this.rank = rank; this.name=name;}
    int getRank(){return rank;}
    public String toString() {return name;}
}

class JasonTests{
    public static void main (String [] args)
    {
         // (3*8)/6 -1 
       Exp exp1 = new BinOp(new BinOp(new BinOp(new Num(3), Op.TIMES, new Num(8)),Op.DIVIDE, new Num(6)), Op.MINUS, new Num(1));
     //(((69 + 1)/7) -8) * 2 )
       Exp exp2 = new BinOp(new BinOp(new BinOp(new BinOp(new Num(69), Op.PLUS,new Num(1)), Op.DIVIDE, new Num(7)), Op.MINUS, new Num(8)),Op.TIMES, new Num(2));
     // (5 * 6)
       Exp exp3 = new BinOp(new Num(5), Op.TIMES, new Num(6));
     // (2/10) * (10/2)
       Exp exp4 = new BinOp(new BinOp(new Num(2), Op.DIVIDE, new Num(10)), Op.TIMES, new BinOp(new Num(10), Op.DIVIDE, new Num(2)));
     // 5
       Exp exp5 = new Num(5);

       assert (exp1.eval() ==3);
       assert (exp2.eval()== 4);
       assert (exp3.eval() == 30);
       assert (exp4.eval() == 1);
       assert (exp5.eval() ==5);

        // // a test for Problem 1b
       List<Instr> is = new LinkedList<Instr>();
       is.add(new Push(1.0));
       is.add(new Push(2.0));
       is.add(new Calculate(Op.PLUS));
       is.add(new Push(3.0));
       is.add(new Calculate(Op.TIMES));
       Instrs instrs = new Instrs(is);
       assert(instrs.execute() == 9.0);

       List<Instr> is1 = new LinkedList<Instr>();
       is1.add(new Push(3));
       is1.add(new Push(8));
       is1.add(new Calculate(Op.TIMES));
       is1.add(new Push(6));
       is1.add(new Calculate(Op.DIVIDE));
       is1.add(new Push(1));
       is1.add(new Calculate(Op.MINUS));
       Instrs instrs1 = new Instrs(is1);
       assert(instrs1.execute() == 3 );

       List<Instr> is2 = new LinkedList<Instr>();
       is2.add(new Push(69));
       is2.add(new Push(1));
       is2.add(new Calculate(Op.PLUS));
       is2.add(new Push(7));
       is2.add(new Calculate(Op.DIVIDE));
       is2.add(new Push(8));
       is2.add(new Calculate(Op.MINUS));
       is2.add(new Push(2));
       is2.add(new Calculate(Op.TIMES));
       Instrs instrs2 = new Instrs(is2);
       assert(instrs2.execute() == 4);  

       List<Instr> is3 = new LinkedList<Instr>();
       is3.add(new Push(5));
       is3.add(new Push(6));
       is3.add(new Calculate(Op.TIMES));
       Instrs instrs3 = new Instrs(is3);
       assert (instrs3.execute() == 30);

     // (2/10) * (10/2)
       List<Instr> is4 = new LinkedList<Instr>();
       is4.add(new Push(2));
       is4.add(new Push(10));
       is4.add(new Calculate(Op.DIVIDE));
       is4.add(new Push(10));
       is4.add(new Push(2));
       is4.add(new Calculate(Op.DIVIDE));
       is4.add(new Calculate(Op.TIMES));
       Instrs instrs4 = new Instrs(is4);
       assert (instrs4.execute() == 1);

       List<Instr> is5 = new LinkedList<Instr>();
       is5.add(new Push(5));
       Instrs instrs5 = new Instrs(is5);
       assert (instrs5.execute() == 5);

        // // a test for Problem 1c
        /*List<Instr> test = exp.compile();
        for (Instr insn : test)
        System.out.println(insn.toString());*/
        //assert (exp.compile().equals(is));
        assert (exp1.compile().equals(is1));
        assert (exp2.compile().equals(is2));
        assert (exp3.compile().equals(is3));
        assert (exp4.compile().equals(is4));
        assert (exp5.compile().equals(is5));

        ListStringSet testS = new ListStringSet();
        assert (testS.size()==0);
        assert (testS.contains("hello")==false);
        testS.add("hello");
        assert(testS.contains("hello")==true);
        assert(testS.size()==1);
        testS.add("hello");
        assert(testS.size()==1);
        testS.add("merp");
        assert(testS.contains("merp")==true);
        assert(testS.contains("hello")==true);
        assert(testS.size()==2);
        testS.add("annuity");
        testS.add("zeta");
        assert(testS.contains("annuity")==testS.contains("zeta"));
        testS.add("zeta");
        assert(testS.contains("annuity")==true);
        assert(testS.size()==4);
        //System.out.println(testS.toString());

        ListSet<String> test = new ListSet<String>((s1,s2) -> s2.compareTo(s1));
        assert (test.size()==0);
        assert (test.contains("hello")==false);
        test.add("hello");
        assert(test.contains("hello")==true);
        assert(test.size()==1);
        test.add("hello");
        assert(test.size()==1);
        test.add("merp");
        assert(test.contains("merp")==true);
        assert(test.contains("hello")==true);
        assert(test.size()==2);
        test.add("annuity");
        test.add("zeta");
        assert(test.contains("annuity")==test.contains("zeta"));
        test.add("zeta");
        assert(test.contains("annuity")==true);
        assert(test.size()==4);
        //System.out.println(test.toString());

        ListSet<Integer> test1 = new ListSet<Integer>((i1, i2) -> i1-i2);
        assert(test1.size()==0);
        assert(!test1.contains(10));
        test1.add(10);
        test1.add(10);
        assert(test1.size()==1);
        test1.add(20);
        test1.add(5);
        assert(test1.contains(5)==test1.contains(20));
        assert(test1.size()==3);

        ListSet<Person> test2 = new ListSet<Person>((p1,p2) -> p1.getRank() - p2.getRank());
        assert(test2.size()==0);
        assert(!test2.contains(new Person(6,"derp")));
        Person jason = new Person(1,"JSON");
        Person naruto = new Person(3,"Naruto");
        Person badLuckBrian = new Person(1,"badLuckBrian");
        test2.add(jason);
        test2.add(naruto);
        test2.add(badLuckBrian);
        assert(test2.size()==2);
        assert(test2.contains(badLuckBrian));
        //System.out.println(test2.toString());
    }

}