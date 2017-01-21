package encoding;

/**
 * @author francesco
 *
 */
public interface Encoder {
	public boolean encode(String fileName);
	public boolean decode(String fileName);
}
