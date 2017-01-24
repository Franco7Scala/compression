package encoding;


import utilities.EncoderParameters;


/**
 * @author francesco
 *
 */
public interface Encoder {
	public boolean encode(String fileName, EncoderParameters paramters);
	public boolean decode(String fileName, EncoderParameters paramters);
}
