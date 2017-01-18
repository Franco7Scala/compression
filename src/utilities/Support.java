package utilities;


import java.nio.ByteBuffer;


/**
 * @author francesco
 *
 */
public class Support {
	
	
	public static byte[] doubleToByteArray(double value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble(value);
	    return bytes;
	}

	public static double byteArrayToDouble(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getDouble();
	}
	
	
}
