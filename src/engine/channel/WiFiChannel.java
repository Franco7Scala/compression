package engine.channel;


import java.util.BitSet;
import java.util.Random;
import engine.utilities.Constants;


/**
 * @author francesco
 *
 */
public class WiFiChannel implements Channel {
	private float[][] chainMatrix = { {1, 0}, {1, 0} };
	private float errorPercentage;
	
	public ChannelDelegate delegate;
	

	public WiFiChannel() {
		// use the default matrix
	}
	
	public WiFiChannel(float[][] chainMatrix) {
		this.chainMatrix = chainMatrix;
	}
	
	@Override
	public byte[] simulateTransmission(byte[] input) {
		BitSet output = BitSet.valueOf(input);
		Random random = new Random();
		int lengthChain = output.size(); 
		int previousState = (random.nextInt(2) + 1);
		errorPercentage = 0;
		for ( int i = 0; i < lengthChain; i ++ ) {
			if ( delegate != null ) {
				delegate.notifyAdvancementTransmission((float)i/(float)lengthChain);
			}
			float event = Math.abs(random.nextFloat()); 
			if ( previousState == Constants.NICE_STATE ) {
				if ( event <= getTransitionProbability(0, 1) ) {
					previousState = Constants.BAD_STATE;
					output.flip(i);
					errorPercentage ++;
				}
				else {
					// do nothing
				}
			}
			else { 
				if ( event <= getTransitionProbability(1, 0) ) { 
					previousState = Constants.NICE_STATE;
				}
				else {
					output.flip(i);
					errorPercentage ++;
				}
			}
		}
		errorPercentage /= input.length;
		return output.toByteArray();
	}
	
	public float getErrorPercentage() {  
		return errorPercentage;
	}

	private float getTransitionProbability(int i, int j) {
		return chainMatrix[i][j];
	}
	

}
