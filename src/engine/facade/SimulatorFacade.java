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
import engine.utilities.Support;


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
		// resetting percentages
		delegate.notifyCompressionAdvancement(0);
		delegate.notifyEncodingAdvancement(0);
		delegate.notifyChannelAdvancement(0);
		// simulation
		delegate.notifyMessage("Starting simulation...");
		delegate.notifyMessage("-----------------------------------------");
		// compression
		delegate.notifyMessage("Compressing file with algorithm " + compressor.getCurrentCompressionMethod() + "...");
		energyProfiler.energyConsumptionStartMonitoring();
		String outputFileName = null;
		try {
			outputFileName = compressor.compress(fileName);
		} catch (Exception e) {
			delegate.notifyErrorMessage("Something went wrong during compression!");
			return;
		}
		float consumption = energyProfiler.energyConsumptionStopMonitoring();
		delegate.notifyCompressionAdvancement(1);
		delegate.notifyMessage("Compression completed!\nEnergy elapsed: " + consumption + " Joule");
		delegate.notifyMessage("Statisctics:");
		delegate.notifyMessage(compressor.getStatiscticsCompression());
		delegate.notifyMessage("----------------------------------------");
		// encoding
		delegate.notifyMessage("Encoding data...");
		byte [] encodedData = null;
		try {
			encodedData = encoder.encode(outputFileName, encoderParameters);
		} catch (Exception e) {
			delegate.notifyErrorMessage("Something went wrong during encoding!");
			return;
		}
		delegate.notifyEncodingAdvancement(1);
		delegate.notifyMessage("Encoding completed...");
		delegate.notifyMessage("----------------------------------------");
		// transmission
		delegate.notifyMessage("Transmitting data...");
		byte[] transmittedData = channel.simulateTransmission(encodedData);
		delegate.notifyMessage("End transmission data, error = " + String.format("%.7f", channel.getErrorPercentage()) + "%");
		delegate.notifyChannelAdvancement(1);
		delegate.notifyMessage("----------------------------------------");
		// resetting percentages
		delegate.notifyCompressionAdvancement(0);
		delegate.notifyEncodingAdvancement(0);
		// decoding
		delegate.notifyMessage("Decoding data...");
		encoder.decode(transmittedData, encoderParameters);
		delegate.notifyMessage("Decoding completed...");
		delegate.notifyMessage("Delay E2E: " + encoder.elapsedTime() + " ms");
		delegate.notifyEncodingAdvancement(1);
		delegate.notifyMessage("----------------------------------------");
		// decompression
		delegate.notifyMessage("Decompressing file...");
		energyProfiler.energyConsumptionStartMonitoring();
		compressor.decompress(encoderParameters.decodingOut);
		consumption = energyProfiler.energyConsumptionStopMonitoring();
		delegate.notifyMessage("Decompression completed!\nEnergy elapsed: " + consumption + " Joule");
		delegate.notifyImportantMessage("Simulation terminated!");
		delegate.notifyCompressionAdvancement(1);
		delegate.notifyMessage("----------------------------------------");
		String decompressedFileName;
		String container = fileName.substring(0, fileName.lastIndexOf('.'));
		decompressedFileName = container + " copy.";
		container = fileName.substring(fileName.lastIndexOf('.') + 1);
		decompressedFileName = decompressedFileName.concat(container);
		if ( Support.compareFiles(fileName, decompressedFileName) ) {
			delegate.notifyMessage("File rebuilded correctly!");
		}
		else {
			delegate.notifyMessage("File NOT rebuilded correctly!");
		}
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
