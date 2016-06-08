class Person {
    public void greet() { this.hello(new Integer(3)); }
    public void hello(Object o) { 
    	System.out.println("hello object"); }
  }

  class CSPerson extends Person {
    public void hello(Object s) { 
    	System.out.println("hello world!"); }
  }

  // Client
  CSPerson p = new CSPerson();
  p.greet();