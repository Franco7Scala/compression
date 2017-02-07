package engine.energy;

/**
 * @author francesco
 *
 */
public interface EnergyProfiler {
	public int energyResidue();
	public void energyConsumptionStartMonitoring();
	public float energyConsumptionStopMonitoring();
}
