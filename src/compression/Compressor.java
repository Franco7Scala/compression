package compression;

/**
 * @author francesco
 *
 */
public interface Compressor {
	public boolean compress(String pathFile);
	public boolean decompress(String pathFile);
}
