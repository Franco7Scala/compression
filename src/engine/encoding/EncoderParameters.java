package engine.encoding;


import engine.utilities.Constants.Factor;


/**
 * @author francesco
 *
 */
public class EncoderParameters {
	public GraphCoder coder;
	public int blockSize;
	public String decodingOut;
	public Factor[][][] generator;	// generator polynomials
	public int k; 					// quantity input bits to the coder 
	public int n; 					// quantity output bits by the coder
	public int m; 					// register size of minimum dimension
	public int M; 					// total registers size
	
	
	@Override
	public String toString() {
		return ((int)(Math.pow(2, M))) + " states polynomial";
	}
	
	
}
