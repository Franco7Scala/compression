package compression;

/**
 * @author francesco
 *
 */
public interface Compressor {
	public void compress(String pathFile);
	public void decompress(String pathFile);
}
