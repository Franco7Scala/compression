package compression;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import utilities.Constants;
import utilities.Support;
import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ArithmeticCompression implements Compressor {
	private HashMap<Byte, Double> probabilities;


	@Override
	public boolean compress(String fileName) {
		FileOutputStream outputStream = null;
		Fragmenter fragmenter = null;
		try {
			generateProbabilities(fileName);
			fragmenter = new Fragmenter(fileName, 1);
			File file = new File(fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION);
			if ( !file.createNewFile() ) {
				return false;
			}
			outputStream = new FileOutputStream(fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION, true);
			outputStream.write( Support.longToByteArray(fragmenter.getFileSize()) );
			int pendingBlocks = Constants.MAX_READABLE_BYTES;
			while ( fragmenter.hasMoreFragments() ) { 
				double lowerBound = Constants.LOWER_BOUD;
				double upperBound = Constants.UPPER_BOUND;
				while ( (pendingBlocks > 0) && fragmenter.hasMoreFragments() ) {
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
				outputStream.write(Support.doubleToByteArray(tag));
				pendingBlocks = Constants.MAX_READABLE_BYTES;
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

	private void generateProbabilities (String fileName) throws Exception {
		double sum = 0;
		probabilities = new HashMap<>();
		Fragmenter fragmenter = new	Fragmenter(fileName, 1);
		while ( fragmenter.hasMoreFragments() ) {
			byte nextFragment = fragmenter.nextFragment()[0];
			if ( probabilities.containsKey(nextFragment) ) {
				double value = probabilities.get(nextFragment);
				value ++;
				probabilities.put(nextFragment, value);
			}
			else {
				probabilities.put(nextFragment, (double)1);
			}
			sum ++;
		}
		//normalizing probabilities
		for ( byte currentFragment : probabilities.keySet() ) {
			probabilities.put(currentFragment, (probabilities.get(currentFragment)/sum) );			
		}
		fragmenter.close();
	}


}
