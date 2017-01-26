package compression;


/**
 * @author francesco
 *
 */
public interface CompressorDelegate {
	public boolean notifyAdvancement(long percentage);
}
