package encoding;


import java.io.IOException;

import utilities.Constants;
import utilities.EncoderParameters;
import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ConvolutionalEncoder implements Encoder {


	@Override
	public boolean encode(String fileName, EncoderParameters paramters) {
		Fragmenter fragmenter = null; 
		try {
			fragmenter = new Fragmenter(fileName, Constants.BLOCK_SIZE);
			while ( fragmenter.hasMoreFragments() ) {
				byte[] fragment = fragmenter.nextFragment();
				
			}
			
			
			
			
			
		} 
		catch (Exception e) {
			return false;
		}
		finally {
			try {
				fragmenter.close();
			} 
			catch (IOException e) {}
		}
		return true;
	}

	@Override
	public boolean decode(String fileName, EncoderParameters paramters) {
		// TODO Auto-generated method stub
		return false;
	}


}
