package utilities;

/**
 * @author francesco
 *
 */
public interface Constants {
	
	// Compression
	public static final String DECOMPRESSION_EXTENSION = "decompressing";
	public static final byte EOF = -1;
	
	// Arithmetic compression
	public static final String ARITHMETIC_COMPRESSION_EXTENSION = "ac";
	public static final int MAX_READABLE_BYTES = 16;
	public static final double LOWER_BOUD = 0;
	public static final double UPPER_BOUND = 1000000000;
	
	// LempelZiv78
	public static final String LZ78_COMPRESSION_EXTENSION = "lz78";
	
	
	// Encoding
	public enum Factor {DIRECT, SINGLE_SHIFT, DOUBLE_SHIFT, TRIPLE_SHIFT, BLANK};
	public static int BLOCK_SIZE = 8;
	
	// Block size constants
	public static int SINGLE_BLOCK = 1;
	public static int SINGLE_BLOCK_ENCODED = SINGLE_BLOCK + 1;
	
	
	
}
