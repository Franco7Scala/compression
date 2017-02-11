package engine.facade;


import engine.channel.ChannelDelegate;
import engine.channel.WiFiChannel;
import engine.compression.CompressorDelegate;
import engine.encoding.ConvolutionalEncoder;
import engine.encoding.EncoderDelegate;
import engine.encoding.EncoderParameters;
import engine.encoding.Polynomial;
import engine.energy.EnergyProfiler;
import engine.utilities.SimulatorDelegate;


/**
 * @author francesco
 *
 */
public class SimulatorFacade implements CompressorDelegate, ChannelDelegate, EncoderDelegate {
	// compression
	private CompressorFacade compressor;
	private EnergyProfiler energyProfiler;
	private String fileName;
	// encoding
	private ConvolutionalEncoder encoder;
	private EncoderParameters encoderParameters;
	// transmission 
	private WiFiChannel channel;
	
	public SimulatorDelegate delegate;

	private static SimulatorFacade INSTANCE;
	
	
	public static SimulatorFacade sharedInstance() {
		if ( INSTANCE == null ) {
			INSTANCE = new SimulatorFacade();
		}
		return INSTANCE;
	}
	
	private SimulatorFacade() {
		compressor = CompressorFacade.sharedInstance();
		compressor.delegate = this;
		encoder = new ConvolutionalEncoder();
		encoder.delegate = this;
		channel = new WiFiChannel();
		channel.delegate = this;
	}

	// Simulation
	public void startSimulation() {
		delegate.notifyMessage("Starting simulation...");
		// compression
		delegate.notifyMessage("Compressing file with algorithm " + compressor.getCurrentCompressionMethod() + "...");
		energyProfiler.energyConsumptionStartMonitoring();
		String outputFileName = null;
		try {
			outputFileName = compressor.compress(fileName);
		} catch (Exception e) {
			delegate.notifyImportantMessage("Something went wrong during compression!");
			return;
		}
		float consumption = energyProfiler.energyConsumptionStopMonitoring();
		delegate.notifyMessage("Compression completed!\nEnergy elapsed: " + consumption);
		// encoding
		delegate.notifyMessage("Encoding data...");
		byte [] encodedData = null;
		try {
			encodedData = encoder.encode(outputFileName, encoderParameters);
		} catch (Exception e) {
			delegate.notifyImportantMessage("Something went wrong during encoding!");
			return;
		}
		delegate.notifyMessage("Encoding completed...");
		delegate.notifyMessage("Statisctics:");
		delegate.notifyMessage(compressor.getStatiscticsCompression());
		// transmission
		delegate.notifyMessage("Transmitting data...");
		byte[] transmittedData = channel.simulateTransmission(encodedData);
		delegate.notifyMessage("End transmission data...");
		// resetting percentages
		delegate.notifyCompressionAdvancement(0);
		delegate.notifyChannelAdvancement(0);
		delegate.notifyEncodingAdvancement(0);
		// decoding
		delegate.notifyMessage("Decoding data...");
		encoder.decode(transmittedData, encoderParameters);
		delegate.notifyMessage("Decoding completed...");
		// decompression
		delegate.notifyMessage("Decompressing file...");
		energyProfiler.energyConsumptionStartMonitoring();
		compressor.decompress(encoderParameters.decodingOut);
		consumption = energyProfiler.energyConsumptionStopMonitoring();
		delegate.notifyMessage("Decompression completed!\nEnergy elapsed: " + consumption);
		delegate.notifyImportantMessage("Simulation terminated!");
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
	public void setChannelMatrix(float[][] matrix) {
		channel = new WiFiChannel(matrix);
		channel.delegate = this;
	}
	
	// chain delegation
	@Override
	public void notifyAdvancementCompression(float percentage) {
		delegate.notifyCompressionAdvancement(percentage);
	}

	@Override
	public void notifyAdvancementTransmission(float percentage) {
		delegate.notifyChannelAdvancement(percentage);
	}

	@Override
	public void notifyAdvancementEncoding(float percentage) {
		delegate.notifyEncodingAdvancement(percentage);
	}
	
	
}
