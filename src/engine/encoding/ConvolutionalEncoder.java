package engine.encoding;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;

import engine.utilities.Fragmenter;


/**
 * @author francesco
 *
 */
public class ConvolutionalEncoder implements Encoder {
	private long time;

	public EncoderDelegate delegate;
	
	
	@Override
	public byte[] encode(String fileName, EncoderParameters parameters) throws Exception {
		time = System.currentTimeMillis();
		parameters.decodingOut = fileName;
		for( int i = 1; i <= 8; i ++ ) {
			if( ( ( i * (parameters.k ) ) % 8 ) == 0 ) {
				parameters.blockSize = Math.abs( i * (parameters.k) );
			}
		}
		Fragmenter fragmenter = new Fragmenter(fileName, parameters.blockSize);
		byte[] sequenceEncoded = new byte[ (int) ( (fragmenter.getFileSize() * 8) * (parameters.n) + 1 ) / 8 ];
		int indexSequence = 0;
		while ( fragmenter.hasMoreFragments() ) {
			if ( delegate != null ) {
				delegate.notifyAdvancementEncoding((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
			}
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
			PrintWriter writer = new PrintWriter(parameters.decodingOut);
			writer.print("");
			writer.close();
			outputStream = new FileOutputStream(parameters.decodingOut, true);
			BitSet set = BitSet.valueOf(input);
			for ( int i = 0; i < set.size(); i += parameters.n ) {
				if ( delegate != null ) {
					delegate.notifyAdvancementEncoding((float)i/(float)set.size());
				}
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
				time = System.currentTimeMillis() - time;
			} catch (IOException e) {}
		}
	}

	@Override
	public long elapsedTime() {
		return time;
	}


}
