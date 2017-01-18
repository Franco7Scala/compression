package compression;


import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import utilities.Constants;
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
			fragmenter = new	Fragmenter(fileName, 1);
			File file = new File(fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION);
			if ( !file.createNewFile() ) {
				return false;
			}
			outputStream = new FileOutputStream(fileName + "." + Constants.ARITHMETIC_COMPRESSION_EXTENSION, true);
			int pendingBlocks = Constants.MAX_READABLE_BYTES;
			while ( fragmenter.hasMoreFragments() ) { 
				double lowerBound = 0;
				double upperBound = 1;
				while ( (pendingBlocks > 0) && fragmenter.hasMoreFragments() ) {
					pendingBlocks --;
					double intervalSize = upperBound - lowerBound;
					byte currentFragment = fragmenter.nextFragment()[0];
					for ( byte indexedFragment : probabilities.keySet() ) {
						if ( indexedFragment == currentFragment ) {
							upperBound += ( probabilities.get(currentFragment) * intervalSize );
							break;
						}
						else {
							lowerBound += ( probabilities.get(currentFragment) * intervalSize );
						}
					}
				}
				double tag = lowerBound + ((upperBound - lowerBound)/2);
				// converting TAG to byte[]
				byte[] compressionOutput = new byte[8];
				long lng = Double.doubleToLongBits(tag);
				for( int i = 0; i < 8; i ++ ) {
					compressionOutput[i] = (byte) ((lng >> ((7 - i) * 8)) & 0xff); 
				}
				// saving TAG to file
				outputStream.write(compressionOutput);
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
	public boolean decompress(String fileName) {
		//TODO to implement
		return false;
	}
	
	public HashMap<Byte, Double> getProbabilities() {
		return probabilities;
	}
	
	private void generateProbabilities (String fileName) throws Exception {
		double sum = 0;
		probabilities = new HashMap<>();
		Fragmenter fragmenter = new	Fragmenter(fileName, 1);
		while ( fragmenter.hasMoreFragments() ) {
			if ( probabilities.containsKey(fragmenter.nextFragment()[0]) ) {
				double value = probabilities.get(fragmenter.nextFragment()[0]);
				value ++;
				probabilities.put(fragmenter.nextFragment()[0], value);
			}
			else {
				probabilities.put(fragmenter.nextFragment()[0], (double)1);
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
