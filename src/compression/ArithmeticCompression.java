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
			int pendingBlocks = Constants.MAX_READABLE_BYTES;
			while ( fragmenter.hasMoreFragments() ) { 
				double lowerBound = 0;
				double upperBound = 1;
				System.out.println("INIT LB: " + lowerBound + "\tUB: " + upperBound  );
				while ( (pendingBlocks > 0) && fragmenter.hasMoreFragments() ) {
					pendingBlocks --;
					double intervalSize = upperBound - lowerBound;
					byte currentFragment = fragmenter.nextFragment()[0];
					for ( byte indexedFragment : probabilities.keySet() ) {
						if ( indexedFragment == currentFragment ) {
							upperBound = lowerBound + ( probabilities.get(indexedFragment) * intervalSize );
							System.out.println("COMP: " + indexedFragment);
							break;
						}
						else {
							lowerBound += ( probabilities.get(indexedFragment) * intervalSize );
						}
					}
				}
				double tag = lowerBound + ((upperBound - lowerBound)/2);
				// saving TAG to file
				System.out.println("SAVING: " + tag);
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
			while ( fragmenter.hasMoreFragments() ) { 
				double lowerBound = 0;
				double upperBound = 1;
				double tag = Support.byteArrayToDouble(fragmenter.nextFragment());
				System.out.println("READING: " + tag);
				byte[] decompressionOutput = new byte[Constants.MAX_READABLE_BYTES];
				for ( int i = 0; i < Constants.MAX_READABLE_BYTES; i ++ ) {
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
				outputStream.write(decompressionOutput);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
