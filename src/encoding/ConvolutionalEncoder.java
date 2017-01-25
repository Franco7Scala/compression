package encoding;


import java.io.IOException;

import utilities.Constants;
import utilities.Constants.Factor;
import utilities.EncoderParameters;
import utilities.Support;
import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ConvolutionalEncoder implements Encoder {


	@Override
	public boolean encode(String fileName, EncoderParameters parameters) {
		Fragmenter fragmenter = null;
		try {
			fragmenter = new Fragmenter(fileName, Constants.BLOCK_SIZE);
			while ( fragmenter.hasMoreFragments() ) {
				
				byte[] fragment = fragmenter.nextFragment();
				byte[][] dataToCoded = new byte [parameters.generator.length][Constants.BLOCK_SIZE];
				byte[][] codedData = new byte [parameters.generator[0].length][Constants.BLOCK_SIZE];
				byte[] codedDataSum = null;
				byte[] sum = null;
				byte[][] applyPolynomialAlongLine = null;
				
				for (int i = 0; i < parameters.generator[0].length; i++) {
					for (int j = 0; j < parameters.generator.length; j++) {
						Factor[] factors = parameters.generator[j][i];
						applyPolynomialAlongLine = applyPolynomial(factors, fragment, Constants.BLOCK_SIZE);
						sum = applyPolynomialAlongLine[0];
						for (int k = 1; k < applyPolynomialAlongLine.length ; k++) {
							sum = Support.sumBytesArrayBitPerBit(sum, applyPolynomialAlongLine[k]);
						}
						dataToCoded[j] = sum;
					}
					codedDataSum = dataToCoded[0];
					for (int k = 1; k < dataToCoded.length ; k++) {
						codedDataSum = Support.sumBytesArrayBitPerBit(codedDataSum, dataToCoded[k]);
					}
					// codedDataSum rappresenta c1, c2 e così via.
					codedData[i] = codedDataSum;
				}
				// costruzione C
				for (int i = 0; i < codedData[0].length; i++) {
					for (int j = 0; j < codedData.length; j++) {
						// concatenare per colonna
					}
				}
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

	private byte[][] applyPolynomial(Factor[] factors, byte[] fragment, int blockSize) throws Exception {
		byte[][] dataToCoded = new byte [factors.length][blockSize];
		for (int i = 0; i < factors.length; i++) {
			dataToCoded[i] = Support.shiftByteArray(fragment, factors[i].ordinal());
			
		}
		return dataToCoded;
	}

	@Override
	public boolean decode(String fileName, EncoderParameters paramters) {
		// TODO Auto-generated method stub
		return false;
	}


}
