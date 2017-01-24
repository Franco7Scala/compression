package utilities;


import utilities.Constants.Factor;


/**
 * @author francesco
 *
 */
public class EncoderParameters {
	public Factor[][][] generator;	// generator polynomials
	public int k; 					// quantity input bits to the coder 
	public int n; 					// quantity output bits by the coder
	public int m; 					// register size of minimum dimension
	public int M; 					// total registers size
}
