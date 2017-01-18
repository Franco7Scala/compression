package compression;


import java.util.HashMap;
import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ArithmeticCompression implements Compressor {
	private HashMap<Byte, Float> probabilities;
	
	
	@Override
	public boolean compress(String fileName) {
		try {
			generateProbabilitites(fileName);
			float lowerBound = 0;
			float upperBound = 1;
			Fragmenter fragmenter = new	Fragmenter(fileName, 1);
			while ( fragmenter.hasMoreFragments() ) {
				float interval = upperBound - lowerBound;
				
				
				
				
				
			}
			float tag = lowerBound + ((upperBound - lowerBound)/2);
			//TODO to save lowerBound
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean decompress(String fileName) {
		//TODO to implement
		return false;
	}
	
	public HashMap<Byte, Float> getProbabilities() {
		return probabilities;
	}
	
	private void generateProbabilitites (String fileName) throws Exception {
		float sum = 0;
		probabilities = new HashMap<>();
		Fragmenter fragmenter = new	Fragmenter(fileName, 1);
		while ( fragmenter.hasMoreFragments() ) {
			if ( probabilities.containsKey(fragmenter.nextFragment()[0]) ) {
				float value = probabilities.get(fragmenter.nextFragment()[0]);
				value ++;
				probabilities.put(fragmenter.nextFragment()[0], value);
			}
			else {
				probabilities.put(fragmenter.nextFragment()[0], 1f);
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
