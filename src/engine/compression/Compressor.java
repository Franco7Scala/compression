package engine.compression;


/**
 * @author francesco
 *
 */
public interface Compressor {
	public String compress(String fileName) throws Exception;
	public boolean decompress(String fileName, Object dictionary);
	public float compressionFactor();
	public float averageEntropy();
	public float averageLength();
	public String name();
}
