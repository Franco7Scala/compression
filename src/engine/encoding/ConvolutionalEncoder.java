package engine.encoding;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;
import engine.utilities.Fragmenter;
import engine.utilities.Support;


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
		parameters.fileSize = (int)fragmenter.getFileSize();
		int sizeSequence = 0;
		if ( (fragmenter.getFileSize()%8) == 0 ) {
			parameters.blockQuantity = (int) fragmenter.getFileSize()/8;
			sizeSequence = (int) ( (fragmenter.getFileSize() * 8) * (parameters.n) + 1 ) / 8;
		}
		else {
			parameters.blockQuantity = (int) (fragmenter.getFileSize()/8) + 1;
			sizeSequence = (int) (((fragmenter.getFileSize()) * 8) * (parameters.n)) / 8;
		}
		byte[] sequenceEncoded = new byte[sizeSequence];
		int indexSequence = 0;
		while ( fragmenter.hasMoreFragments() ) {
			if ( delegate != null ) {
				delegate.notifyAdvancementEncoding((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
			}
			BitSet input = BitSet.valueOf(fragmenter.nextFragment());
			int inputIndex = 0;
			for ( ; inputIndex < parameters.blockSize && (indexSequence < sequenceEncoded.length); inputIndex ++ ) {
				parameters.coder.startEncoding();
				BitSet output = new BitSet( 8 * parameters.n );
				int offset = 0;
				for ( int i = 0; i < input.size(); ) {
					BitSet toEncode = new BitSet();
					for ( int f = 0; f < parameters.k; f ++, i ++ ) {
						toEncode.set(f, input.get(i + (inputIndex * parameters.blockSize)));
					}
					BitSet tempFragment = parameters.coder.getNextSymbolEncoded(toEncode);
					for ( int j = 0; j < parameters.n; j ++ , offset ++ ) {
						output.set(offset, tempFragment.get(j));
					}
				}
				for ( int k = 0; k < (8 * parameters.n) && (indexSequence < sequenceEncoded.length); ) {
					byte toPrint = 0;
					for ( int x = 0; x < 8; x ++, k ++ ) {
						toPrint = Support.addBitToPosition(toPrint, x, (output.get(k)? 1 : 0));
					}
					sequenceEncoded[indexSequence] = toPrint; 
					indexSequence ++;
				}
				parameters.coder.stopEncoding();				
			}	
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
			BitSet inputBitSet = BitSet.valueOf(input);
			for ( int i = 0; i < (input.length * 8); ) {
				if ( delegate != null ) {
					delegate.notifyAdvancementEncoding((float)i/(float)inputBitSet.size());
				}
				if ( ( i % (parameters.blockSize*2) ) == 0 ) {
					parameters.coder.stopDecoding();
					parameters.coder.startDecoding();
				}
				int indexReached = 0;
				byte toPrint = 0;
				while ( indexReached != 8 ) {
					BitSet toDecode = new BitSet();
					for ( int f = 0; f < parameters.n; f ++, i ++ ) {
						toDecode.set(f, inputBitSet.get(i));
					}
					BitSet fragment = parameters.coder.getNextSymbolDecoded(toDecode);
					for ( int x = 0; x < parameters.k; x ++, indexReached ++ ) {
						toPrint = Support.addBitToPosition(toPrint, indexReached, (fragment.get(x)? 1 : 0));
					}
					
				}
				outputStream.write(toPrint);
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
