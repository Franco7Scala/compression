package compression;


import java.util.HashMap;
import utilities.objects.dataManager.Fragmenter;


/**
 * @author francesco
 *
 */
public class ArithmeticCompression implements Compressor {
	private HashMap<Byte, Double> probabilities;
	
	
	@Override
	public boolean compress(String fileName) {
		try {
			generateProbabilitites(fileName);
			double lowerBound = 0;
			double upperBound = 1;
			Fragmenter fragmenter = new	Fragmenter(fileName, 1);
			while ( fragmenter.hasMoreFragments() ) {
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
	
	public HashMap<Byte, Double> getProbabilities() {
		return probabilities;
	}
	
	private void generateProbabilitites (String fileName) throws Exception {
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
