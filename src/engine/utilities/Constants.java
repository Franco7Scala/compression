package engine.utilities;

/**
 * @author francesco
 *
 */
public interface Constants {
	
	// COMPRESSION
	public static final byte EOF = -1;
	
	// Arithmetic compression
	public static final String ARITHMETIC_COMPRESSION = "Arithmetic Compression";
	public static final String ARITHMETIC_COMPRESSION_EXTENSION = "ac";
	public static final int MAX_READABLE_BYTES = 16;
	public static final double LOWER_BOUD = 0;
	public static final double UPPER_BOUND = 1000000000;
	
	// LempelZiv78
	public static final String LZ78_COMPRESSION = "LZ78";
	public static final String LZ78_COMPRESSION_EXTENSION = "lz78";
	
	// ScalaMadonna17
	public static final String SM17_COMPRESSION = "SM17";
	public static final String SM17_COMPRESSION_EXTENSION = "sm17";
	
	
	// ENCODING
	public enum Factor {DIRECT, SINGLE_SHIFT, DOUBLE_SHIFT, TRIPLE_SHIFT, BLANK};
	public static int BLOCK_SIZE = 8;
	public static int SINGLE_BLOCK = 1;
	public static int SINGLE_BLOCK_ENCODED = SINGLE_BLOCK + 1;
	
	
	// TRANSMISSION
	public static float DEFAULT_ERROR_WIFI = (float)0.00001;
	public static final int NICE_STATE = 1;
	public static final int BAD_STATE = 2;
	
}
