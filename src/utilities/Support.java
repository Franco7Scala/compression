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
	
	public static byte[] longToByteArray(long value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putLong(value);
	    return bytes;
	}

	public static long byteArrayToLong(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getLong();
	}
	
	public static byte[] intToByteArray(int value) {
	    byte[] bytes = new byte[4];
	    ByteBuffer.wrap(bytes).putInt(value);
	    return bytes;
	}

	public static int byteArrayToInt(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getInt();
	}
	
	
}
