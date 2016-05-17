/* Name: Kang Chen

   UID: 204256656

   Others With Whom I Discussed Things:

   Other Resources I Consulted:
   
*/

import java.io.*;
import java.util.*;

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
    
	// implement using Java's Fork/Join library
    public PPMImage mirrorImage() {
		throw new ImplementMe();
    }

	// implement using Java 8 Streams
    public PPMImage mirrorImage2() {
		throw new ImplementMe();
    }

	// implement using Java's Fork/Join library
    public PPMImage gaussianBlur(int radius, double sigma) {
		throw new ImplementMe();
    }

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
			img1.toFile("original.ppm");
			neg_img1.toFile("neg.ppm");
			gray_img1.toFile("gray.ppm");
		} catch(FileNotFoundException e1) {
			System.out.println("ERROR: can't find file.");
		} catch(IOException e2) {
			System.out.println("ERROR: can't find file.");
		}
    }
}
