package engine.facade;


import engine.compression.AbstractCompressor;
import engine.compression.ArithmeticCompression;
import engine.compression.CompressorDelegate;
import engine.compression.LempelZiv78;
import engine.compression.ScalaMadonna17;
import engine.utilities.Constants;


/**
 * @author francesco
 *
 */
public class CompressorFacade {
	private AbstractCompressor compressor;
	public CompressorDelegate delegate;
	
	private static CompressorFacade INSTANCE;
	
	
	public static CompressorFacade sharedInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new CompressorFacade();
		}
		return INSTANCE;
	}
	
	private CompressorFacade() {
		setCurrentCompressionMethod(Constants.SM17_COMPRESSION);
	}

	public String compress(String fileName) throws Exception {
		return compressor.compress(fileName);
	}
	
	public void decompress(String fileName) {
		Object dictionary = null;
		if ( compressor instanceof ArithmeticCompression ) {
			dictionary = ((ArithmeticCompression) compressor).getProbabilities();
		}
		compressor.decompress(fileName, dictionary);
	}
	
	public String[] getAllCompressionMethods() {
		return new String[] {Constants.SM17_COMPRESSION, Constants.LZ78_COMPRESSION, Constants.ARITHMETIC_COMPRESSION};
	}
	
	public String getCurrentCompressionMethod() {
		return compressor.name();
	}
	
	public String getStatiscticsCompression() {
		return "Algorithm: " + compressor.name() + "\n" +
	           "Compression factor: " + compressor.compressionFactor() + " %\n" + 
			   "Average Entropy: " + compressor.averageEntropy() + "\n" +
			   "Average Length: " + compressor.averageLength();
	}

	public void setCurrentCompressionMethod(String compressionMethod) {
		switch ( compressionMethod ) {
		case Constants.SM17_COMPRESSION:
			compressor = new ScalaMadonna17();
			break;	
		case Constants.LZ78_COMPRESSION:
			compressor = new LempelZiv78();
			break;
		default:
			compressor = new ArithmeticCompression();
			break;
		}
		compressor.delegate = delegate;
	}

	
}
