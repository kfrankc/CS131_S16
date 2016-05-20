/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

// a marker for code that you need to implement
class ImplementMe extends RuntimeException {}

// an RGB triple
class RGB {
    public int R, G, B;

    RGB(int r, int g, int b) {
    	R = r;
		G = g;
		B = b;
    }

    public String toString() { return "(" + R + "," + G + "," + B + ")"; }

}


// an object representing a single PPM image
class PPMImage {
    protected int width, height, maxColorVal;
    protected RGB[] pixels;

    public PPMImage(int w, int h, int m, RGB[] p) {
		width = w;
		height = h;
		maxColorVal = m;
		pixels = p;
    }

    // parse a PPM image file named fname and produce a new PPMImage object
    public PPMImage(String fname) 
    	throws FileNotFoundException, IOException {
		FileInputStream is = new FileInputStream(fname);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		br.readLine(); // read the P6
		String[] dims = br.readLine().split(" "); // read width and height
		int width = Integer.parseInt(dims[0]);
		int height = Integer.parseInt(dims[1]);
		int max = Integer.parseInt(br.readLine()); // read max color value
		br.close();

		is = new FileInputStream(fname);
	    // skip the first three lines
		int newlines = 0;
		while (newlines < 3) {
	    	int b = is.read();
	    	if (b == 10)
				newlines++;
		}

		int MASK = 0xff;
		int numpixels = width * height;
		byte[] bytes = new byte[numpixels * 3];
        is.read(bytes);
		RGB[] pixels = new RGB[numpixels];
		for (int i = 0; i < numpixels; i++) {
	    	int offset = i * 3;
	    	pixels[i] = new RGB(bytes[offset] & MASK, 
	    						bytes[offset+1] & MASK, 
	    						bytes[offset+2] & MASK);
		}
		is.close();

		this.width = width;
		this.height = height;
		this.maxColorVal = max;
		this.pixels = pixels;
    }

	// write a PPMImage object to a file named fname
    public void toFile(String fname) throws IOException {
		FileOutputStream os = new FileOutputStream(fname);

		String header = "P6\n" + width + " " + height + "\n" 
						+ maxColorVal + "\n";
		os.write(header.getBytes());

		int numpixels = width * height;
		byte[] bytes = new byte[numpixels * 3];
		int i = 0;
		for (RGB rgb : pixels) {
	    	bytes[i] = (byte) rgb.R;
	    	bytes[i+1] = (byte) rgb.G;
	    	bytes[i+2] = (byte) rgb.B;
	    	i += 3;
		}
		os.write(bytes);
		os.close();
    }

    // helper function to perform swap on two RGB objects
    public void swap(RGB p1, RGB p2) {
		RGB tmp = new RGB(0, 0, 0);
		tmp.R = p1.R;
		tmp.G = p1.G;
		tmp.B = p1.B;
		p1.R = p2.R;
		p1.G = p2.G;
		p1.B = p2.B;
		p2.R = tmp.R;
		p2.G = tmp.G;
		p2.B = tmp.B;
	}

	// implement using Java 8 Streams
    public PPMImage negate() {
    	// set wt, ht, mt
    	int wt = this.width;
    	int ht = this.height;
    	int mt = this.maxColorVal;
    	// avoid pass by reference error - go through and copy RGB individually
    	RGB[] px = Arrays.stream(pixels)
    		.parallel()
    		.map(pi -> new RGB(pi.R, pi.G, pi.B))
    		.toArray(RGB[]::new);
    	// use .stream() on p, an RGB array
    	// use .parallel() to parallelize
    	// use .map to do computation
    	// use .toArray(RGB[]::new) to convert back to RGB array
    	RGB[] p = Arrays.stream(px)
    		.parallel()
    		.map((pi) -> {
    			pi.R = mt - pi.R;
    			pi.G = mt - pi.G;
    			pi.B = mt - pi.B;
    			return pi;
    		})
    		.toArray(RGB[]::new);
		return new PPMImage(wt, ht, mt, p);
    }

	// implement using Java 8 Streams
    public PPMImage greyscale() {
		// set wt, ht, mt
    	int wt = this.width;
    	int ht = this.height;
    	int mt = this.maxColorVal;
    	// avoid pass by reference error - go through and copy RGB individually
    	RGB[] px = Arrays.stream(pixels)
    		.parallel()
    		.map(pi -> new RGB(pi.R, pi.G, pi.B))
    		.toArray(RGB[]::new);
    	// use .stream() on p, an RGB array
    	// use .parallel() to parallelize
    	// use .map to do computation
    	// use .toArray(RGB[]::new) to convert back to RGB array
    	RGB[] p = Arrays.stream(px)
    		.parallel()
    		.map((pi) -> {
    			double tmp = .299 * pi.R + .587 * pi.G + .114 * pi.B;
    			int gray = (int)Math.round(tmp);
    			// int gray = Math.round(tmp1);
    			pi.R = gray;
    			pi.G = gray;
    			pi.B = gray;
    			return pi;
    		})
    		.toArray(RGB[]::new);
    	return new PPMImage(wt, ht, mt, p);
    }    
    
    class MirrorTask extends RecursiveTask <RGB[]> {
		private RGB[] px;
		private int low, high;
		private int width, height;

		// constants are final - assigned once
		// private final int SEQUENTIAL_CUTOFF = 800;

		public MirrorTask(RGB[] p, int low, int high, int width, int height) {
			this.px = p;
			this.low = low;
			this.high = high;
			this.width = width;
			this.height = height;
		}

		public RGB[] compute() {
			if (height != 1) {
				// int mid = (height / 2) * width;
				int mid = low + width;
				MirrorTask left = 
					new MirrorTask(px, low, mid, width, 1);
				MirrorTask right = 
					new MirrorTask(px, mid, high, width, height-1);
				left.fork(); // fork them both
				RGB[] l2 = right.compute(); // join is synchronization
				RGB[] l1 = left.join();
				RGB[] comb = 
					Stream.concat(Arrays.stream(l1), Arrays.stream(l2))
						.toArray(RGB[]::new);
                return comb;
			} else {
				for (int i = low; i < high; i++) {
					int row = i / width + 1;
					if (i < high - width/2)
						swap(px[i], px[high-(i-(row-1)*width)-1]);
				}
				return Arrays.copyOfRange(px, low, high);
			}
		}
	}

	// implement using Java's Fork/Join library
    public PPMImage mirrorImage() {
		// set wt, ht, mt
    	int wt = this.width;
    	int ht = this.height;
    	int mt = this.maxColorVal;
    	// avoid pass by reference error - go through and copy RGB individually
    	RGB[] px = Arrays.stream(pixels)
    		.parallel()
    		.map(pi -> new RGB(pi.R, pi.G, pi.B))
    		.toArray(RGB[]::new);
    	// use fork / join to create the new RGB pixels array p
    	RGB[] p = new MirrorTask(px, 0, wt*ht, wt, ht).compute();
    	return new PPMImage(wt, ht, mt, p);
    }

	// implement using Java 8 Streams
    public PPMImage mirrorImage2() {
		// set wt, ht, mt
    	int wt = this.width;
    	int ht = this.height;
    	int mt = this.maxColorVal;
    	// avoid pass by reference error - go through and copy RGB individually
    	RGB[] px = Arrays.stream(pixels)
    		.parallel()
    		.map(pi -> new RGB(pi.R, pi.G, pi.B))
    		.toArray(RGB[]::new);
    	// use .stream() on p, an RGB array
    	// use .parallel() to parallelize
    	// use .map to do computation
    	// use .toArray(RGB[]::new) to convert back to RGB array
    	int[] indexes = IntStream.range(0, wt*ht).toArray();
    	Arrays.stream(indexes)
    		.parallel()
    		.forEach((id) -> {
    			// extract current row number of id
    			int row = (id / wt) + 1;
    			// System.out.println("multiplier: " + (row*wt));
    			// only swap when id is the left side of current row
    			if (id < (row*wt - wt/2)) {
    				swap(px[id], px[row*wt-(id-(row-1)*wt)-1]);
    			}
    		});
    	return new PPMImage(wt, ht, mt, px);
    }

 //    class GaussianTask extends RecursiveTask <RGB[]> {
	// 	private RGB[] px;
	// 	private int low, high;
	// 	private int width, height;
	// 	private double[][] gfilter;

	// 	// constants are final - assigned once
	// 	// private final int SEQUENTIAL_CUTOFF = 800;

	// 	public GaussianTask(RGB[] p, int low, int high, int width, int height, double[][] gfilter) {
	// 		this.px = p;
	// 		this.low = low;
	// 		this.high = high;
	// 		this.width = width;
	// 		this.height = height;
	// 		this.gfilter = gfilter;
	// 	}

	// 	public RGB[] compute() {
	// 		if (height != 1) {
	// 			// int mid = (height / 2) * width;
	// 			int mid = low + width;
	// 			MirrorTask left = 
	// 				new GaussianTask(px, low, mid, width, 1);
	// 			MirrorTask right = 
	// 				new GaussianTask(px, mid, high, width, height-1);
	// 			left.fork(); // fork them both
	// 			RGB[] l2 = right.compute(); // join is synchronization
	// 			RGB[] l1 = left.join();
	// 			RGB[] comb = 
	// 				Stream.concat(Arrays.stream(l1), Arrays.stream(l2))
	// 					.toArray(RGB[]::new);
 //                return comb;
	// 		} else {
	// 			RGB[] row = Arrays.copyOfRange(px, low, high);
	// 			// for (int i = low; i < high; i++) {
	// 			// 	int row = i / width + 1;
	// 			// 	if (i < high - width/2)
	// 			// 		swap(px[i], px[high-(i-(row-1)*width)-1]);
	// 			// }
	// 			int index = low;
	// 			int filter_size = gfilter.length * gfilter[0].length;
	// 			for (int k = 0; k < width; k++) {
	// 				double tmp_R;
	// 				double tmp_G;
	// 				double tmp_B;
	// 				for (int i = 0; i < gfilter.length; i++) {
	// 					for (int j = 0; j < gfilter[0].length; j++) {
	// 						int clamping = (filter_size/2)+i+j;
	// 						// first row
	// 						if (high == width)
	// 							clamping = 0;
	// 						// last row
	// 						if (high == width*height)
	// 							clamp
	// 							clamping = width-1;
	// 						tmp_R += gfilter[i][j]*px[index-clamping].R;
	// 						tmp_G += gfilter[i][j]*px[index-clamping].G;
	// 						tmp_B += gfilter[i][j]*px[index-clamping].B;
	// 					}
	// 				}
	// 				int gauss_R = (int)Math.round(tmp_R);
	// 				int gauss_G = (int)Math.round(tmp_G);
	// 				int gauss_B = (int)Math.round(tmp_B);
	// 				row[k].R = gauss_R;
	// 				row[k].G = gauss_G;
	// 				row[k].B = gauss_B;
	// 				index++;
	// 			}
	// 			return row;
	// 		}
	// 	}
	// }

	// implement using Java's Fork/Join library
    // public PPMImage gaussianBlur(int radius, double sigma) {
		// // set wt, ht, mt
  //   	int wt = this.width;
  //   	int ht = this.height;
  //   	int mt = this.maxColorVal;
  //   	// avoid pass by reference error - go through and copy RGB individually
  //   	RGB[] px = Arrays.stream(pixels)
  //   		.parallel()
  //   		.map(pi -> new RGB(pi.R, pi.G, pi.B))
  //   		.toArray(RGB[]::new);
  //   	// use fork / join to create the new RGB pixels array p
  //   	double[][] gfilter = new Gaussian(radius, sigma).gaussianFilter;
  //   	RGB[] p = new GaussianTask(px, 0, wt*ht, wt, ht, gfilter).compute();
  //   	return new PPMImage(wt, ht, mt, p);
    // }

}

// code for creating a Gaussian filter
class Gaussian {

    protected static double gaussian(int x, int mu, double sigma) {
		return Math.exp( -(Math.pow((x-mu)/sigma,2.0))/2.0 );
    }

    public static double[][] gaussianFilter(int radius, double sigma) {
		int length = 2 * radius + 1;
		double[] hkernel = new double[length];
		for(int i=0; i < length; i++)
	    	hkernel[i] = gaussian(i, radius, sigma);
		double[][] kernel2d = new double[length][length];
		double kernelsum = 0.0;
		for(int i=0; i < length; i++) {
	    	for(int j=0; j < length; j++) {
				double elem = hkernel[i] * hkernel[j];
				kernelsum += elem;
				kernel2d[i][j] = elem;
	    	}
		}
		for(int i=0; i < length; i++) {
	    	for(int j=0; j < length; j++)
				kernel2d[i][j] /= kernelsum;
		}
		return kernel2d;
    }
}

class Test {
	public static void main (String [] args) {
		String s1 = "/Users/frankchen/Desktop/CS131_S16/hw5/florence.ppm";
		try {
			PPMImage img1 = new PPMImage(s1);
			PPMImage neg_img1 = img1.negate();
			PPMImage gray_img1 = img1.greyscale();
			PPMImage mirror_img1_1 = img1.mirrorImage();
			PPMImage mirror_img1_2 = img1.mirrorImage2();
			img1.toFile("original_1.ppm");
			neg_img1.toFile("neg_1.ppm");
			mirror_img1_1.toFile("mirror_1_1.ppm");
			mirror_img1_2.toFile("mirror_1_2.ppm");
		} catch(FileNotFoundException e1) {
			System.out.println("ERROR: can't find file.");
		} catch(IOException e2) {
			System.out.println("ERROR: can't find file.");
		}
    }
}
