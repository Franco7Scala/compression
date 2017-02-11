package engine.utilities;

/**
 * @author francesco
 *
 */
public interface SimulatorDelegate {
	public void notifyCompressionAdvancement(float percentage);
	public void notifyEncodingAdvancement(float percentage);
	public void notifyChannelAdvancement(float percentage);
	public void notifyMessage(String message);
	public void notifyImportantMessage(String message);
	public void notifyErrorMessage(String message);
}
