package channel;


import java.util.BitSet;
import java.util.Random;


/**
 * @author francesco
 *
 */
public class WiFiChannel implements Channel {

	
	@Override
	public byte[] simulateTransmission(byte[] input, float error) {
		BitSet output = BitSet.valueOf(input);
		Random random = new Random();
		for ( int i = 0; i < Math.round(input.length * 8 * error); i++ ) {
			output.flip(random.nextInt((input.length*8)));
		}	
		return output.toByteArray();
	}
	

}
