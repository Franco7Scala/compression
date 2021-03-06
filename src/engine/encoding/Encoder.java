package engine.encoding;

/**
 * @author francesco
 *
 */
public interface Encoder {
	public byte[] encode(String fileName, EncoderParameters paramters) throws Exception;
	public boolean decode(byte[] input, EncoderParameters paramters);
	public long elapsedTime();
}
