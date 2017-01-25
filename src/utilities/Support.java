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
	
	public static byte[] shiftByteArray(byte array[], int positions) throws Exception {
		byte[] result = new byte[array.length + 1];
		for (  ) {
			
		}
		
		
		
		
		return null;
	}
	
	public static byte[] sumBytesArrayBitPerBit(byte[] a, byte[] b) throws Exception {
		return null;
	}
	
	
	public static byte sumBytesBitPerBit(byte a, byte b) throws Exception {
		byte result = 0;
		for ( int i = 0; i < 8; i ++ ) {
			if ( getBitValue(a, i) == 1 && getBitValue(b, i) == 0 ) {
				result = addBitToPosition(result, i, 1);
			}
			else if ( getBitValue(a, i) == 0 && getBitValue(b, i) == 1 ) {
				result = addBitToPosition(result, i, 1);
			}
		}
		return result;
	}
	
	private static byte addBitToPosition(byte result, int position, int value) throws Exception {
		if ( !(position >= 0 && position < 8) ) {
			throw new Exception("Wrong bit position!");
		}
		if ( value == 1 ) {
			return (byte) (result | (1 << position));	
		}
		else if ( value == 0 ) {
			return (byte) (result & ~(1 << position));
		}
		else {
			throw new Exception("Wrong bit value!");
		}
	}
	
	private static int getBitValue(byte b, int position) {
	   return (b >> position) & 1;
	}
	
	
}
