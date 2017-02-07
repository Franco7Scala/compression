package engine.facade;


import engine.compression.CompressorDelegate;
import engine.encoding.ConvolutionalEncoder;
import engine.encoding.Encoder;
import engine.encoding.EncoderParameters;
import engine.encoding.Polynomial;
import engine.energy.EnergyProfiler;
import engine.utilities.SimulatorDelegate;


/**
 * @author francesco
 *
 */
public class SimulatorFacade implements CompressorDelegate {
	// compression
	private CompressorFacade compressor;
	private EnergyProfiler energyProfiler;
	private String fileName;
	// encoding
	private Encoder encoder;
	private EncoderParameters encoderParameters;
	
	public SimulatorDelegate delegate;

	private static SimulatorFacade INSTANCE;
	
	
	public static SimulatorFacade sharedInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new SimulatorFacade();
		}
		return INSTANCE;
	}
	
	private SimulatorFacade() {
		compressor.delegate = this;
		encoder = new ConvolutionalEncoder();
	}

	// Simulation
	public void startSimulation() {
		delegate.notifyMessage("Starting simulation...");
		// compression
		delegate.notifyMessage("Compressing file with algorithm " + compressor.getCurrentCompressionMethod() + "...");
		energyProfiler.energyConsumptionStartMonitoring();
		String outputFileName = compressor.compress(fileName);
		float consumption = energyProfiler.energyConsumptionStopMonitoring();
		delegate.notifyMessage("Compression completed!\nEnergy elapsed: " + consumption);
		// encoding
		delegate.notifyMessage("Encoding data...");
		try {
			encoder.encode(outputFileName, encoderParameters);
		} catch (Exception e) {}
		delegate.notifyMessage("Encoding completed...");
		// transmission
		delegate.notifyMessage("Transmitting data...");
	}
	
	// Compression
	public String[] getAllCompressionMethods() {
		return compressor.getAllCompressionMethods();
	}
	
	public void setCurrentCompressionMethod(String compressionMethod) {
		compressor.setCurrentCompressionMethod(compressionMethod);
	}
	
	public void setEnergyProfiler(EnergyProfiler energyProfiler) {
		this.energyProfiler = energyProfiler;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	// Encoding
	public EncoderParameters[] getPolynomials() {
		return new EncoderParameters[] {Polynomial.get4StatesPolynomial(), Polynomial.get8StatesPolynomial(), Polynomial.get16StatesPolynomial()};
	}
	
	public void setEncoderParameters(EncoderParameters encoderParameters) {
		this.encoderParameters = encoderParameters;
	}
	
	// Transmission
	
	
	//TODO mettere delegati in transmission e encoding
	
	
	
	
	
	
	// chain delegation
	@Override
	public void notifyAdvancement(float percentage) {
		delegate.notifyCompressionAdvancement(percentage);
	}




	
	
}
