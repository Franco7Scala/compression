package compression;

/**
 * @author francesco
 *
 */
public interface Compressor {
	public boolean compress(String fileName);
	public boolean decompress(String fileName, Object dictionary);
}
