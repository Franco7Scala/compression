package compression;


/**
 * @author francesco
 *
 */
public interface Compressor {
	public boolean compress(String fileName);
	public boolean decompress(String fileName, Object dictionary);
	public float compressionFactor();
	public float averageEntropy();
	public float averageLength();
	public String name();
}
