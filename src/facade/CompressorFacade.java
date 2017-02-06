package facade;


import compression.ArithmeticCompression;
import compression.AbstractCompressor;
import compression.CompressorDelegate;
import compression.LempelZiv78;
import compression.ScalaMadonna17;
import utilities.Constants;


/**
 * @author francesco
 *
 */
public class CompressorFacade {
	private AbstractCompressor compressor;
	private CompressorDelegate delegate;

	
	public CompressorFacade(CompressorDelegate delegate) {
		this.delegate = delegate;
		setCurrentCompressionMethod(Constants.SM17_COMPRESSION);
	}

	public void compress(String fileName) {
		compressor.compress(fileName);
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
	           "Compression factor: " + compressor.compressionFactor() + "\n" + 
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
