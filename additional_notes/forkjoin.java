
import java.util.concurrent.*;

/* class RecursiveTask<V> {

	void fork() {
		// create a new thread;
		// invoke this.compute() in the new
		// thread.
	}	

	V join() {
		// wait for the new thread to finish
		// return its result.
	}

}
*/

class SumTask extends RecursiveTask<Long> {

    private final int SEQUENTIAL_CUTOFF = 10000;

	private int[] arr;
	private int low, hi;

	public SumTask(int[] a, int low, int hi) {
		this.arr = a;
		this.low = low;
		this.hi = hi;
	}

	public Long compute() {
		if(hi - low > SEQUENTIAL_CUTOFF) {
			int mid = (low + hi) / 2;
			SumTask left =
				new SumTask(arr, low, mid);
			SumTask right = 
			    new SumTask(arr, mid, hi);
			left.fork();
			long l2 = right.compute();
			long l1 = left.join();			
			return l1+l2;
		} else {
			long sum = 0;
			for(int i = low; i < hi; i++)
				sum += arr[i];
			return sum;
		}

	}

}

class SumFJ {

	public static void main(String[] args) {
		int size = Integer.parseInt(args[0]);
		int[] arr = new int[size];
		for(int i = 0; i < size; i++)
			arr[i] = i;

		long sum = 
		  new SumTask(arr, 0, size).compute();
		System.out.println(sum);
	}

}

