package encoding;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.BitSet;

import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ConvolutionalEncoder implements Encoder {


	@Override
	public byte[] encode(String fileName, EncoderParameters parameters) throws Exception {
		for( int i = 1; i <= 8; i ++ ) {
			if( ( ( i * (parameters.k ) ) % 8 ) == 0 ) {
				parameters.blockSize = Math.abs( i * (parameters.k) );
			}
		}
		Fragmenter fragmenter = new Fragmenter(fileName, parameters.blockSize);
		byte[] sequenceEncoded = new byte[ (int) ( (fragmenter.getFileSize() * 8) * (parameters.n) + 1 ) / 8 ];
		int indexSequence = 0;
		while ( fragmenter.hasMoreFragments() ) {
			BitSet set = BitSet.valueOf(fragmenter.nextFragment());
			parameters.coder.startEncoding();
			BitSet fragment = new BitSet( 8 * parameters.k );
			for ( int i = 0; i < set.size(); i += parameters.k ) {
				BitSet tempFragment = parameters.coder.getNextSymbolEncoded(set.get(i, i + parameters.k));
				for (int j = 0; j < parameters.k; j++) {
					fragment.set(j, tempFragment.get(j));
				}
			}
			for (byte b : fragment.toByteArray()) {
				sequenceEncoded[indexSequence] = b;
				indexSequence ++;
			}
			parameters.coder.stopEncoding();
		}
		return sequenceEncoded;
	}

	@Override
	public boolean decode(byte[] input, EncoderParameters parameters) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(parameters.decodingOut, true);
			BitSet set = BitSet.valueOf(input);
			for ( int i = 0; i < set.size(); i += parameters.n ) {
				if ( i % parameters.blockSize == 0 ) {
					parameters.coder.stopDecoding();
					parameters.coder.startDecoding();
				}
				BitSet fragment = parameters.coder.getNextSymbolDecoded(set.get(i, i+parameters.n));
				outputStream.write(fragment.toByteArray());
			}
			parameters.coder.stopDecoding();
			return true;
		} 
		catch (Exception e) {
			return false;
		}
		finally {
			try {
				outputStream.close();
			} catch (IOException e) {}
		}
	}


}
