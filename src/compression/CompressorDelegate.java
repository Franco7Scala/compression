package compression;


/**
 * @author francesco
 *
 */
public interface CompressorDelegate {
	public void notifyAdvancement(long percentage);
}
