package engine.energy;

/**
 * @author francesco
 *
 */
public interface EnergyProfiler {
	public int energyResidue();
	public void energyConsumptionStartMonitoring(float clock, float EPI, float Em, float El);
	public float energyConsumptionStopMonitoring();
}
