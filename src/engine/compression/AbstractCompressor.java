package engine.compression;


import java.util.HashMap;

import engine.utilities.Fragmenter;


/**
 * @author francesco
 *
 */
public abstract class AbstractCompressor implements Compressor {
	protected HashMap<Byte, Double> probabilities;
	protected float compressionFactor;
	protected boolean probabilitiesGenerated;
	protected String fileName;

	public CompressorDelegate delegate;

	
	@Override
	public float compressionFactor() {
		return compressionFactor;
	}

	@Override
	public float averageEntropy() {
		if ( !probabilitiesGenerated ) {
			try {
				generateProbabilities(fileName);
			} 
			catch (Exception e) {}
		}
		float entropy = 0;
		for ( double probability : probabilities.values() ) {
			entropy += ( probability * ( Math.log(1/probability) / Math.log(2) ) );
		}
		return entropy;
	}
	
	@Override
	public float averageLength() {
		if ( !probabilitiesGenerated ) {
			try {
				generateProbabilities(fileName);
			} 
			catch (Exception e) {}
		}
		float length = 0;
		for ( double probability : probabilities.values() ) {
			length += ( probability * 8 );
		}
		return length;
	}
	
	protected void generateProbabilities(String fileName) throws Exception {
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
		probabilitiesGenerated = true;
		fragmenter.close();
	}
	

}
