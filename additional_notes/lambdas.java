import java.util.*;


// Java has first-class functions!

class Sorting {

	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
		for(String s : args)
			l.add(s);
		Collections.sort(
			l, 
			// this is a first-class function
			(s1, s2) -> s1.length() - s2.length());
		System.out.println(l);
	}

}
