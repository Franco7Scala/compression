package engine.compression;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import engine.utilities.Constants;
import engine.utilities.Fragmenter;
import engine.utilities.Support;


/**
 * @author francesco
 *
 */
public class ArithmeticCompression extends AbstractCompressor {

	
	@Override
	public String compress(String fileName) throws Exception {
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		int outputSize = 0;
		String outputFileName = null;
		try {
			generateProbabilities(fileName);
			fragmenter = new Fragmenter(fileName, 1);
			outputFileName = fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION;
			File file = new File(outputFileName);
			if ( !file.createNewFile() ) {
				throw new Exception("Error creating output file!");
			}
			outputStream = new FileOutputStream(fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION, true);
			outputStream.write( Support.longToByteArray(fragmenter.getFileSize()) );
			int pendingBlocks = Constants.MAX_READABLE_BYTES;
			while ( fragmenter.hasMoreFragments() ) { 
				double lowerBound = Constants.LOWER_BOUD;
				double upperBound = Constants.UPPER_BOUND;
				while ( (pendingBlocks > 0) && fragmenter.hasMoreFragments() ) {
					if ( delegate != null ) {
						delegate.notifyAdvancement(fragmenter.getCurrentFragment()/fragmenter.getFileSize());
					}
					pendingBlocks --;
					double intervalSize = upperBound - lowerBound;
					byte currentFragment = fragmenter.nextFragment()[0];
					for ( byte indexedFragment : probabilities.keySet() ) {
						if ( indexedFragment == currentFragment ) {
							upperBound = lowerBound + ( probabilities.get(indexedFragment) * intervalSize );							
							break;
						}
						else {
							lowerBound += ( probabilities.get(indexedFragment) * intervalSize );
						}
					}
				}
				double tag = lowerBound + ((upperBound - lowerBound)/2);
				// saving TAG to file
				outputSize += 8;
				outputStream.write(Support.doubleToByteArray(tag));
				pendingBlocks = Constants.MAX_READABLE_BYTES;
			}
		} catch (Exception e) {
			throw e;
		}
		finally {
			try {
				compressionFactor = 1 - ( outputSize / fragmenter.getFileSize() );
				outputStream.close();
				fragmenter.close();
			} catch (Exception e) {}
		}
		return outputFileName;
	}

	@Override
	public boolean decompress(String fileName, Object dictionary) {
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		try {
			if ( !(dictionary instanceof HashMap) ) {
				return false;
			}
			probabilities = (HashMap<Byte, Double>) dictionary;
			fragmenter = new Fragmenter(fileName, 8);
			long fileSize = Support.byteArrayToLong(fragmenter.nextFragment());
			String decompressedFileName = fileName.substring(0, fileName.lastIndexOf('.'));
			File file = new File(decompressedFileName);
			if ( !file.createNewFile() ) {
				String container = fileName.substring(0, fileName.lastIndexOf('.'));
				container = container.substring(0, container.lastIndexOf('.'));
				decompressedFileName = container + " copy.";
				container = fileName.substring(0, fileName.lastIndexOf('.'));
				container = container.substring(container.lastIndexOf('.') + 1);
				decompressedFileName = decompressedFileName.concat(container);
				file = new File(decompressedFileName);
				if ( !file.createNewFile() ) {
					return false;
				}
			}
			outputStream = new FileOutputStream(decompressedFileName, true);
			long read = 0;
			while ( fragmenter.hasMoreFragments() && (read < fileSize) ) { 
				double lowerBound = Constants.LOWER_BOUD;
				double upperBound = Constants.UPPER_BOUND;
				double tag = Support.byteArrayToDouble(fragmenter.nextFragment());				
				byte[] decompressionOutput = new byte[Constants.MAX_READABLE_BYTES];
				for ( int i = 0; i < Constants.MAX_READABLE_BYTES && (read < fileSize) ; i ++ ) {
					read ++;
					double intervalSize = upperBound - lowerBound;	
					// finding right interval
					for ( byte indexedFragment : probabilities.keySet() ) {
						upperBound = lowerBound + ( probabilities.get(indexedFragment) * intervalSize );
						if ( tag >= lowerBound && tag < upperBound ) {
							decompressionOutput[i] = indexedFragment;
							break;
						}
						else {
							lowerBound = upperBound;
						}
					}
				}
				// saving decompression to file
				if ( fileSize < Constants.MAX_READABLE_BYTES ) {
					byte[] truncatedDecompressionOutput = new byte[(int)fileSize];
					for ( int i = 0; i < truncatedDecompressionOutput.length; i ++ ) {
						truncatedDecompressionOutput[i] = decompressionOutput[i];
					}
					outputStream.write(truncatedDecompressionOutput);
				}
				else if ( read >= fileSize ) {
					byte[] truncatedDecompressionOutput = new byte[Constants.MAX_READABLE_BYTES - (int)(fileSize%Constants.MAX_READABLE_BYTES)];
					for ( int i = 0; i < truncatedDecompressionOutput.length; i ++ ) {
						truncatedDecompressionOutput[i] = decompressionOutput[i];
					}
					outputStream.write(truncatedDecompressionOutput);
				}
				else {
					outputStream.write(decompressionOutput);
				}
			}
		} catch (Exception e) {
			return false;
		}
		finally {
			try {
				outputStream.close();
				fragmenter.close();
			} catch (Exception e) {}
		}
		return true;
	}

	public HashMap<Byte, Double> getProbabilities() {
		return probabilities;
	}
	
	@Override
	public String name() {
		return Constants.ARITHMETIC_COMPRESSION;
	}


}
