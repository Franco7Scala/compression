package engine.energy;


import java.util.StringTokenizer;

import engine.utilities.Support;


/**
 * @author francesco
 *
 */
public class EnergyProfilerMacOS extends EnergyProfilerParametric {

	
	
	public EnergyProfilerMacOS() {
		super(0, 0, 0, 0);
	}

	@Override
	public int energyResidue() {
		try {
			String output = Support.executeCommand("system_profiler SPPowerDataType");
			StringTokenizer tokenizer = new StringTokenizer(output, " \n");
			while ( tokenizer.hasMoreTokens() ) {
				if ( tokenizer.nextToken().equals("Charge") && tokenizer.nextToken().equals("Remaining") && tokenizer.nextToken().equals("(mAh):") ) {
					return Integer.parseInt(tokenizer.nextToken());
				}
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}
	

}
