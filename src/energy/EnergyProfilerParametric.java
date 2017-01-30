package energy;

import java.lang.management.ManagementFactory;

/**
 * @author francesco
 *
 */
public class EnergyProfilerParametric implements EnergyProfiler {
	private float clock;
	private float EPI;
	private float Em;
	private float El;


	@Override
	public int energyResidue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void energyConsumptionStartMonitoring(float clock, float EPI, float Em, float El) {
		this.clock = clock;
		this.EPI = EPI;
		this.Em = Em;
		this.El = El;
		
	}

	@Override
	public float energyConsumptionStopMonitoring() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
