package engine.encoding;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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
				System.out.println("bZ: " + parameters.blockSize);
			}
		}
		Fragmenter fragmenter = new Fragmenter(fileName, parameters.blockSize);
		System.out.println("fZ: " + fragmenter.getFileSize());
		System.out.println("BZ: " + parameters.blockSize);
		parameters.fileSize = (int)fragmenter.getFileSize();
		if ( (fragmenter.getFileSize()%8) == 0 ) {
			parameters.blockQuantity = (int) fragmenter.getFileSize()/8;
		}
		else {
			parameters.blockQuantity = (int) (fragmenter.getFileSize()/8) + 1;
		}
		byte[] sequenceEncoded = new byte[ (int) ( (fragmenter.getFileSize() * 8) * (parameters.n) + 1 ) / 8 ];
		System.out.println("seq: " + sequenceEncoded.length);
		int indexSequence = 0;
		while ( fragmenter.hasMoreFragments() ) {
			if ( delegate != null ) {
				delegate.notifyAdvancementEncoding((float)fragmenter.getCurrentFragment()/(float)fragmenter.getFileSize());
			}
			BitSet input = BitSet.valueOf(fragmenter.nextFragment());
			int inputIndex = 0;
			for ( ; inputIndex < parameters.blockSize; inputIndex ++ ) {
				
				
				
				
				
				
				System.out.println("INPUT");
				for ( int ppp = inputIndex; ppp < (8 * parameters.k) ; ppp ++) {
					System.out.print( (input.get(ppp)? 1 : 0) +"");
				}
				System.out.print("\n");
				//System.out.println("read: " + input);
				parameters.coder.startEncoding();
				BitSet output = new BitSet( 8 * parameters.n );
				System.out.println("...k: " + parameters.k);
				int offset = 0;
				for ( int i = 0; i < input.size(); /*i += parameters.k*/ ) {
					//System.out.println("-----i= " + i);
					BitSet toEncode = new BitSet();
					//System.out.println("LLL");
					for ( int f = 0; f < parameters.k; f ++, i ++ ) {
						//System.out.print( (pene.get(ppp)? 1 : 0) +"");
						toEncode.set(f, input.get(i + (inputIndex * parameters.blockSize)));
					}
					//System.out.print("\n");
					//System.out.println("preso: " + (toEncode.get(0)? 1 : 0));
					BitSet tempFragment = parameters.coder.getNextSymbolEncoded(toEncode);
					//System.out.print("do:\n");
					for ( int j = 0; j < parameters.n; j ++ , offset ++ ) {
						output.set(offset, tempFragment.get(j));
						//System.out.print( (tempFragment.get(j)? 1 : 0) +"");
					}
				//	System.out.print("\n");
				}
				System.out.print("OUT\n");
				for ( int ppp = 0; ppp < (8 * parameters.n) ; ppp ++) {
					System.out.print( (output.get(ppp)? 1 : 0) +"");
				}
				System.out.print("\n");
				for ( int k = 0; k < (8 * parameters.n); ) {
					byte toPrint = 0;
					for ( int x = 0; x < 8; x ++, k ++ ) {
						toPrint = Support.addBitToPosition(toPrint, x, (output.get(k)? 1 : 0));
					}
					sequenceEncoded[indexSequence] = toPrint; //TODO fix NON multipli di 8
					System.out.println("--------WRITING IN: " + indexSequence);
					indexSequence ++;
				}
				/*for (byte b : output.toByteArray()) {
					sequenceEncoded[indexSequence] = b;
					//System.out.println("ENC: " + sequenceEncoded[indexSequence]);
					indexSequence ++;
				}*/
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
			System.out.println("--------INPUT-------");
			BitSet inputBitSet = BitSet.valueOf(input);
			for ( int ppp = 0; ppp < (((8 * parameters.n) * parameters.blockQuantity) * parameters.blockSize); ppp ++) {
				System.out.print( (inputBitSet.get(ppp)? 1 : 0) +"");
			}
			System.out.print("\n");
			
			//System.out.println("XXX: " + Arrays.toString(input));
			System.out.println("bSuze: " + parameters.blockSize);
			System.out.println("bQQQ: " + parameters.blockQuantity);
			System.out.println("n: " + parameters.n);
			for ( int i = 0; i < (((8 * parameters.n) * parameters.blockQuantity) * parameters.blockSize); ) {
				//System.out.println("i_" + i);
				if ( delegate != null ) {
					delegate.notifyAdvancementEncoding((float)i/(float)inputBitSet.size());
				}
				if ( ( i % (parameters.blockSize*2) ) == 0 ) {
					//System.out.println("asdsadasda");
					parameters.coder.stopDecoding();
					parameters.coder.startDecoding();
				}
				
				
				int indexReached = 0;
				byte toPrint = 0;
				while ( indexReached != 8 ) {
					
					BitSet toDecode = new BitSet();
					//System.out.println("+++LLL    " + parameters.n + " i=" + i);
					for ( int f = 0; f < parameters.n; f ++, i ++ ) {
						
						toDecode.set(f, inputBitSet.get(i));
						//System.out.print( (toDecode.get(f)? 1 : 0) +"");
					}
					//System.out.println("         mmm");
					
					BitSet fragment = parameters.coder.getNextSymbolDecoded(toDecode);
					
					for ( int x = 0; x < parameters.k; x ++, indexReached ++ ) {
						toPrint = Support.addBitToPosition(toPrint, indexReached, (fragment.get(x)? 1 : 0));
					}
					
				}
				
				
				
				
				
				
				
				
				//System.out.println("F: " + fragment.toString());
				//outputStream.write(fragment.toByteArray());
				/*
				System.out.print("\n");
				
				for ( int x = 0; x < 8; x ++) {
					toPrint = Support.addBitToPosition(toPrint, x, (fragment.get(x)? 1 : 0));
					System.out.print("" + Support.getBitValue(toPrint, x));
				}
				System.out.print("\n");*/
				outputStream.write(toPrint);
				
				//System.out.print("OUT\n");
				/*for ( int ppp = 0; ppp < parameters.k ; ppp ++) {
					System.out.print( (fragment.get(ppp)? 1 : 0) +"");
				}*/
				//System.out.print("\n");
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
				//throw new IOException();//TODO cacciare
			} catch (IOException e) {}
		}
	}

	@Override
	public long elapsedTime() {
		return time;
	}


}
