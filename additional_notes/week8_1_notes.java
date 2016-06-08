
// Parallel Computing

/*

This has become an important topic over the past
decade for two main reasons:

1. We can no longer use Moore's law to speed up
sequential programming.  Now we're using Moore's
law to double the number of processors in a 
machine roughly every two years.

2. Big Data.  With lots of data, we're now
programming across clusters of machines or
data centers.

*/

// The dream: automatic parallelization
/*
Today: exists for "embarrassingly parallel"
operations: maps, reduce, filter, sort

Big Data: Google's MapReduce, Hadoop, Spark,...

Java Streams provides similar capability for
a single machine.
*/

import java.util.Arrays;

class Sum {

	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		int[] arr = new int[size];
		for(int i = 0; i < size; i++)
			arr[i] = i;

		int sum = 
			Arrays.stream(arr)
			      .parallel()
		      	  .reduce(0, (i1, i2) -> i1+i2);
		System.out.println(sum);
	}

}

class Sort {

	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		double[] arr = new double[size];
		for(int i = 0; i < size; i++)
			arr[i] = Math.random() * size;

		double[] out = 
			Arrays.stream(arr)
			      .parallel()
			      .sorted()
			      .toArray();

		for(int i = 0; i < 10; i++)
			System.out.println(out[i]);
	}

}





