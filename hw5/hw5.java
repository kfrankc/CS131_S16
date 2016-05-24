/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
   http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
   http://homes.cs.washington.edu/~djg/teachingMaterials/grossmanSPAC_forkJoinFramework.html
   https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html
   Professor Millstein Lecture Notes Week 7 Monday, Wednesday; Week 8 Monday
   Sample Piazza test cases posted by students
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
		private final int SEQUENTIAL_CUTOFF;

		public MirrorTask(RGB[] p, int low, int high, int width, int height) {
			this.px = p;
			this.low = low;
			this.high = high;
			this.width = width;
			this.height = height;
			this.SEQUENTIAL_CUTOFF = width;
		}

		public RGB[] compute() {
			if (high - low > SEQUENTIAL_CUTOFF) {
				// int mid = (height / 2) * width;
				int mid = low + width;
				MirrorTask left = 
					new MirrorTask(px, low, mid, width, height);
				MirrorTask right = 
					new MirrorTask(px, mid, high, width, height);
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

    class GaussianTask extends RecursiveTask <RGB[]> {
		private RGB[] px;
		private int low, high;
		private int width, height;
		private double[][] gfilter;
		private int radius;

		// constants are final - assigned once
		private final int SEQUENTIAL_CUTOFF;

		public GaussianTask(RGB[] p, int low, int high, int width, int height, double[][] gfilter, int radius) {
			this.px = p;
			this.low = low;
			this.high = high;
			this.width = width;
			this.height = height;
			this.gfilter = gfilter;
			this.radius = radius;
			this.SEQUENTIAL_CUTOFF = width;
		}

		public RGB[] compute() {
			if (high - low > SEQUENTIAL_CUTOFF) {
				// int mid = (height / 2) * width;
				int mid = low + width;
				GaussianTask left = 
					new GaussianTask(px, low, mid, width, height, gfilter, radius);
				GaussianTask right = 
					new GaussianTask(px, mid, high, width, height, gfilter, radius);
				left.fork(); // fork them both
				RGB[] l2 = right.compute(); // join is synchronization
				RGB[] l1 = left.join();
				RGB[] comb = 
					Stream.concat(Arrays.stream(l1), Arrays.stream(l2))
						.toArray(RGB[]::new);
                return comb;
			} else {
				RGB[] row = Arrays.copyOfRange(px, low, high);
				// indexes (i,j) for the image
				int j = 0;
				int i = low / width;
				for (int k = 0; k < width; k++) {
					double tmp_R = 0;
					double tmp_G = 0;
					double tmp_B = 0;
					int ri = -radius;
					for (int fi = 0; fi < gfilter[0].length; fi++) {
						int rj = -radius;
						for (int fj = 0; fj < gfilter.length; fj++) {
							int index = (i+ri)*width + (j+rj);
							// check index for out of bounds
							if ((i+ri < 0 || i+ri >= height) && j+rj >= 0 && j+rj < width)
								index = i*width + (j+rj);
							else if ((j+rj < 0 || j+rj >= width) && i+ri >= 0 && i+ri < height)
								index = (i+ri)*width + j;
							// diagonals:
							else if (((i+ri < 0 || i+ri >= height) && (j+rj < 0 || j+rj >= width)) && (ri == rj || ri == -rj))
								index = i*width + j;
							// if ((i+ri < 0 && j+rj < 0) || (i+ri >= height && j+rj >= width))
							// 	index = i*width + j;
							// two checks for top & bottom; left & right
							else if (((i+ri < 0 || i+ri >= height) && (j+rj < 0 || j+rj >= width)) && (ri != rj))
								index = i*width + j;
							tmp_R += gfilter[fi][fj]*px[index].R;
							tmp_G += gfilter[fi][fj]*px[index].G;
							tmp_B += gfilter[fi][fj]*px[index].B;
							// System.out.println("index for i=" + i + "; k=" + k + " (" + fi + "," + fj + "): " + index);
							// System.out.println("tmp_R for " + "(" + fi + "," + fj + "): " + tmp_R);
							rj++;
						}
						ri++;
					}
					// System.out.println("tmpR: " + tmp_R);
					int gauss_R = (int)Math.round(tmp_R);
					int gauss_G = (int)Math.round(tmp_G);
					int gauss_B = (int)Math.round(tmp_B);
					row[k].R = gauss_R;
					row[k].G = gauss_G;
					row[k].B = gauss_B;
					j++;
				}
				return row;
			}
		}
	}

	//implement using Java's Fork/Join library
    public PPMImage gaussianBlur(int radius, double sigma) {
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
    	double[][] gfilter = new Gaussian().gaussianFilter(radius, sigma);
    	// printGaussian(gfilter);
    	RGB[] p = new GaussianTask(px, 0, wt*ht, wt, ht, gfilter, radius).compute();
    	return new PPMImage(wt, ht, mt, p);
    }
  //   // TODO: take out
  //   public void printGaussian(double[][] gfilter) {
  //   	System.out.println("Gaussian Filter");
  //   	for (int i = 0; i < gfilter[0].length; i++) {
  //   		System.out.print("(");
  //   		for (int j = 0; j < gfilter.length; j++) {
  //   			System.out.print(gfilter[i][j] + ",");
  //   		}
  //   		System.out.println(")");
  //   	}
  //   }
  //   // TODO: take out
  //   public void print() {
  //   	int iter = 0;
  //   	for (int j = 0; j < height; j++) {
		// 	for (int i = 0; i < width; i++) {
		// 		System.out.print("(" + pixels[iter].R + "," + pixels[iter].G + "," + pixels[iter].B + ")" + ";");
		// 		iter++;
		// 	}
		// 	System.out.println("");
		// }
  //   }

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
			// sample image testing
			PPMImage img1 = new PPMImage(s1);
			// PPMImage neg_img1 = img1.negate();
			// PPMImage grey_img1 = img1.greyscale();
			// PPMImage mirror_img1_1 = img1.mirrorImage();
			// PPMImage mirror_img1_2 = img1.mirrorImage2();
			// PPMImage gaussian1 = img1.gaussianBlur(60, 3.0);
			// img1.toFile("original_1.ppm");
			// neg_img1.toFile("neg_1.ppm");
			// grey_img1.toFile("grey_1.ppm");
			// mirror_img1_1.toFile("mirror_1_1.ppm");
			// mirror_img1_2.toFile("mirror_1_2.ppm");
			// gaussian1.toFile("gaussian_12.ppm");

			// small image testing 
			// RGB p1 = new RGB(0,0,0);
			// RGB p2 = new RGB(1,2,3);
			// RGB p3 = new RGB(4,5,6);
			// RGB p4 = new RGB(7,7,7);
			// basic testing
			// RGB p1 = new RGB(0,0,0);
			// RGB p2 = new RGB(0,0,0);
			// RGB p3 = new RGB(1,0,0);
			// RGB p4 = new RGB(0,0,0);
			// RGB[] px = {p1, p2, p3, p4};
			// PPMImage img2 = new PPMImage(2, 2, 7, px);
			// PPMImage neg_img2 = img2.negate();
			// PPMImage grey_img2 = img2.greyscale();
			// PPMImage mirror_img2_1 = img2.mirrorImage();
			// PPMImage mirror_img2_2 = img2.mirrorImage2();
			// PPMImage gaussian2 = img2.gaussianBlur(1, 2.0);
			// System.out.println("original");
			// img2.print();
			// System.out.println("negate");
			// neg_img2.print();
			// System.out.println("greyscale");
			// grey_img2.print();
			// System.out.println("mirror1");
			// mirror_img2_1.print();
			// System.out.println("mirror2");
			// mirror_img2_2.print();
			// System.out.println("gaussian");
			// gaussian2.print();

		} catch(FileNotFoundException e1) {
			System.out.println("ERROR: can't find file.");
		} catch(IOException e2) {
			System.out.println("ERROR: can't find file.");
		}
    }
}

class Main {
    public static void main(String[] args)
    {
        try
        {
            PPMImage tester = new PPMImage("florence.ppm");
            PPMImage testerNegated = tester.negate();
            try
            {
                testerNegated.toFile("florenceNegated.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the negated image!");
            }

            PPMImage testerNegatedTwice = testerNegated.negate();
            try
            {
                testerNegatedTwice.toFile("florenceNegatedTwice.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the doubly negated image!");
            }

            PPMImage testerGreyscaled = tester.greyscale();
            try
            {
                testerGreyscaled.toFile("florenceGreyscaled.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the greyscaled image!");
            }

            PPMImage testerGreyscaledTwice = testerGreyscaled.greyscale();
            try
            {
                testerGreyscaledTwice.toFile("florenceGreyscaledTwice.ppm");
            }
            catch (Exception e)
            {
                System.out.println("There was an error in generating the doubly greyscaled image!");
            }

            PPMImage testerMirrored = tester.mirrorImage();
            try
            {
                testerMirrored.toFile("florenceMirrored.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the mirrored image!");
            }
            
            PPMImage testerMirroredTwice = testerMirrored.mirrorImage();
            try
            {
                testerMirroredTwice.toFile("florenceMirroredTwice.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the doubly mirrored image!");
            }

            PPMImage testerMirrored2 = tester.mirrorImage2();
            try
            {
                testerMirrored2.toFile("florenceMirrored2.ppm");
            }
            catch(Exception e)
            {
                System.out.println("There was an error in generating the mirrored image using the stream method!");
            }

            PPMImage testerMirrored2Twice = testerMirrored2.mirrorImage2();
            try 
            {
                testerMirrored2Twice.toFile("florenceMirrored2Twice.ppm");
            }
            catch (Exception e)
            {
                System.out.println("There was an error in generating the doubly mirrored image using the stream method!");
            }         
        }
        catch(Exception e)
        {
            System.out.println("Oh no! An exception has occurred.");
        }


    }
}
