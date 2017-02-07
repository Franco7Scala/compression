package engine.energy;


import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;


/**
 * @author francesco
 *
 */
public class EnergyProfilerParametric implements EnergyProfiler {
	private float clock;
	private float EPI;
	private float Em;
	private float El;
	private long startTime;
	private double memoryUsage;
	private double cpuUsageTime;


	public EnergyProfilerParametric(float clock, float EPI, float Em, float El) {
		this.clock = clock;
		this.EPI = EPI;
		this.Em = Em;
		this.El = El;
	}
	
	@Override
	public int energyResidue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void energyConsumptionStartMonitoring() {
		this.startTime = System.currentTimeMillis();
		this.memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		this.cpuUsageTime = getCPUUsageTime();
	}

	@Override
	public float energyConsumptionStopMonitoring() {
		float E_comp = (float) ( EPI * ( getCPUUsageTime() - cpuUsageTime ) * clock );
		float E_mem = (float) ( Em * ( System.currentTimeMillis() - startTime ) * memoryUsage * 1024 );
		float E_leak = (float) ( El * ( System.currentTimeMillis() - startTime ) * clock );
		return ( E_comp + E_mem + E_leak );
	}

	private double getCPUUsageTime() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for ( Method method : operatingSystemMXBean.getClass().getDeclaredMethods() ) {
			method.setAccessible(true);
			if ( method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers()) ) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} 
				catch (Exception e) {
					value = e;
				}
				if ( method.getName().equals("getProcessCpuTime") ) {
					return (double)value;
				}
			} 
		} 
		return 0;
	}

	
}
