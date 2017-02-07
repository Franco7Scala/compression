package engine.utilities;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Scanner;


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

	public static byte[] shiftByteArray(byte[] array, int positions) throws Exception {
		if ( !(positions >= 0 && positions < 8) ) {
			throw new Exception("Too many shift!");
		}
		byte[] result = new byte[array.length + 1];
		byte container = 0;
		for ( int i = 0; i < array.length; i ++ ) {
			result[i] = (byte)(array[i] << positions);
			if ( i != 0 ) {
				container = array[i - 1];
				for ( int j = positions; j < 8; j ++ ) {
					result[i] = addBitToPosition(result[i], j-positions, getBitValue(container, j));
				}
			}
		}
		for ( int j = positions; j < 8; j ++ ) {
			container = array[result.length-1 - 1];
			result[result.length-1] = addBitToPosition(result[result.length-1], j-positions, getBitValue(container, j));
		}
		return result;
	}

	public static byte[] sumBytesArrayBitPerBit(byte[] a, byte[] b) throws Exception {
		if ( a.length != b.length ) {
			throw new Exception("Arrays have to be the same size!");
		}
		byte[] result = new byte[a.length];
		for ( int i = 0; i < a.length; i ++ ) {
			result[i] = sumBytesBitPerBit(a[i], b[i]);
		}
		return result;
	}

	private static byte sumBytesBitPerBit(byte a, byte b) throws Exception {
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

	public static byte addBitToPosition(byte result, int position, int value) throws Exception {
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

	public static int getBitValue(byte b, int position) {
		return (b >> position) & 1;
	}

	public static String executeCommand(String command) throws IOException {
		Process process = Runtime.getRuntime().exec(command);
		InputStream stream = process.getInputStream();
		Scanner scanner = new Scanner(stream);
		Scanner scannerWithDelimiter = scanner.useDelimiter("\\A");
		String val = "";
		if ( scannerWithDelimiter.hasNext() ) {
			val = scannerWithDelimiter.next();
		}
		else {
			val = "";
		}
		scannerWithDelimiter.close();
		scanner.close();
		stream.close();
		return val;
	}
}
