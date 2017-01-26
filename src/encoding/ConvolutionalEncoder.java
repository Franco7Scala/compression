package encoding;


import java.io.IOException;
import java.util.Arrays;

import javax.naming.spi.DirStateFactory.Result;

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
	public byte[] encode(String fileName, EncoderParameters parameters) throws Exception {
		Fragmenter fragmenter = null;
		fragmenter = new Fragmenter(fileName, Constants.BLOCK_SIZE);
		byte[] result = new byte[(int) (fragmenter.getFileSize() + ( ( fragmenter.getFileSize()/Constants.BLOCK_SIZE ) * ( Constants.SINGLE_BLOCK_ENCODED - Constants.SINGLE_BLOCK ) ))];
		while ( fragmenter.hasMoreFragments() ) {
			byte[] fragment = fragmenter.nextFragment();
			byte[][] dataToCoded = new byte [parameters.generator.length][Constants.SINGLE_BLOCK_ENCODED];
			byte[][] codedData = new byte [parameters.generator[0].length][Constants.SINGLE_BLOCK_ENCODED];
			byte[] codedDataSum = null;
			byte[] sum = null;
			byte[][] applyPolynomialAlongLine = null;
			int position = 0;
			for (int i = 0; i < parameters.generator[0].length; i++) {
				for (int j = 0; j < parameters.generator.length; j++) {
					Factor[] factors = parameters.generator[j][i];
					applyPolynomialAlongLine = applyPolynomial(factors, Arrays.copyOfRange(fragment, j, j + Constants.SINGLE_BLOCK));
					sum = applyPolynomialAlongLine[0];
					for (int k = 1; k < applyPolynomialAlongLine.length ; k++) {
						sum = Support.sumBytesArrayBitPerBit(sum, applyPolynomialAlongLine[k]);
					}
					dataToCoded[j] = Arrays.copyOf(sum, sum.length);
				}
				codedDataSum = dataToCoded[0];
				for (int k = 1; k < dataToCoded.length ; k++) {
					codedDataSum = Support.sumBytesArrayBitPerBit(codedDataSum, dataToCoded[k]);
				}
				// codedDataSum rappresenta c1, c2 e così via.
				codedData[i] = Arrays.copyOf(codedDataSum, codedDataSum.length);
			}
			for (int i = 0; i < codedData[0].length; i++) {
				position++;
				int positionBit = 0;
				for (int k = 0; k < 8; k++) {
					for ( int j = 0; j < codedData.length; j ++ ) {
						result[position] = Support.addBitToPosition(result[position], positionBit, Support.getBitValue(codedData[j][i], k));
						positionBit++;
					}
				}
			}
		}
		fragmenter.close();
		return result;
	}

	private byte[][] applyPolynomial(Factor[] factors, byte[] fragment) throws Exception {
		byte[][] dataToCoded = new byte [factors.length][Constants.SINGLE_BLOCK_ENCODED];
		for (int i = 0; i < factors.length; i++) {
			if ( (factors[i] != Constants.Factor.DIRECT) && (factors[i] != Constants.Factor.BLANK) ) {
				dataToCoded[i] = Support.shiftByteArray(fragment, factors[i].ordinal());
			}
		}
		return dataToCoded;
	}

	@Override
	public boolean decode(byte[] input, EncoderParameters paramters) {
		// TODO Auto-generated method stub
		return false;
	}


}
