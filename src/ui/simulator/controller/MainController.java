package ui.simulator.controller;


import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import engine.encoding.EncoderParameters;
import engine.energy.EnergyProfilerParametric;
import engine.facade.SimulatorFacade;
import engine.utilities.SimulatorDelegate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;


public class MainController implements Initializable, SimulatorDelegate {
	private SimulatorFacade facade = SimulatorFacade.sharedInstance();
	private EncoderParameters parameters;
	private String compressionMethod;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		facade.delegate = this;
		// 2st panel
		//TODO file
		methodsComboBox.getItems().clear();
		methodsComboBox.setItems(FXCollections.observableList(Arrays.asList(facade.getAllCompressionMethods())));
		methodsComboBox.getSelectionModel().selectFirst();
		changeCompressionMethod(null);
		// 2nd panel
		polynomialsComboBox.getItems().clear();
		polynomialsComboBox.setItems(FXCollections.observableList(Arrays.asList(facade.getPolynomials())));
		polynomialsComboBox.getSelectionModel().selectFirst();
		changePolynomial(null);
		// 4th panel
		logOutputPanel.setWrapText(true);
		ScrollBar scrollBar = (ScrollBar)logOutputPanel.lookup(".scroll-bar:vertical");
		if ( scrollBar != null ) {
			scrollBar.setStyle("-fx-opacity: 0;");
		}
	}
	
	@FXML
	void cleanTextField(KeyEvent event) {
		if ( ! event.getText().matches("[0-9]") ) {
			if ( event.getSource() == a11 ) {
				a11.setText(a11.getText().replaceAll("[^\\d.]", ""));
				a11.positionCaret(a11.getText().length());
			}
			if ( event.getSource() == a12 ) {
				a12.setText(a12.getText().replaceAll("[^\\d.]", ""));
				a12.positionCaret(a12.getText().length());
			}
			if ( event.getSource() == a21 ) {
				a21.setText(a21.getText().replaceAll("[^\\d.]", ""));
				a21.positionCaret(a21.getText().length());
			}
			if ( event.getSource() == a22 ) {
				a22.setText(a22.getText().replaceAll("[^\\d.]", ""));
				a22.positionCaret(a22.getText().length());
			}	
		}
	}
	
	// 1st panel
	//TODO file
	
    @FXML
    private JFXComboBox<String> methodsComboBox;
	
	@FXML
    private JFXTextField clockTextField;

    @FXML
    private JFXTextField epiTextField;

    @FXML
    private JFXTextField emTextField;

    @FXML
    private JFXTextField elTextField;
    
    @FXML
	private JFXProgressBar progressCompression;
	
	// 2nd panel
	@FXML
	private JFXComboBox<EncoderParameters> polynomialsComboBox;
	
	@FXML
	private Label polynomialDescription;
	
	@FXML
	private JFXProgressBar progressEncoding;
	
	// 3rd panel
	@FXML
	private JFXTextField a11;

	@FXML
	private JFXTextField a12;

	@FXML
	private JFXTextField a21;

	@FXML
	private JFXTextField a22;
	
	@FXML
	private JFXProgressBar progressChannel;
	
	// 4th panel
	@FXML
	private JFXButton loadSimulationButton;
	
	@FXML
	private TextArea logOutputPanel;

	// 1st panel
	@FXML
	void changeCompressionMethod(ActionEvent event) {
		compressionMethod = methodsComboBox.getValue();
	}
	//TODO file
	
	// 2nd panel
	@FXML
	void changePolynomial(ActionEvent event) {
		parameters = polynomialsComboBox.getValue();
		polynomialDescription.setText(parameters.coder.getDescription());
	}
	
	// 4th panel
	@FXML
	void loadSimulation(ActionEvent event) {
		// configuring compression
		//TODO file
		facade.setCurrentCompressionMethod(compressionMethod);
		facade.setEnergyProfiler(new EnergyProfilerParametric(Float.parseFloat(clockTextField.getText()), 
															  Float.parseFloat(epiTextField.getText()), 
															  Float.parseFloat(emTextField.getText()), 
															  Float.parseFloat(elTextField.getText())));
		// configuring encoding
		facade.setEncoderParameters(parameters);
		// configuring channel
		facade.setChannelMatrix(new float[][] { {Float.parseFloat(a11.getText()), Float.parseFloat(a12.getText())},
			 								    {Float.parseFloat(a21.getText()), Float.parseFloat(a21.getText())} });		
		facade.startSimulation();
	}

	// Delegated methods
	@Override
	public void notifyCompressionAdvancement(float percentage) {
		progressCompression.setProgress(percentage);
	}

	@Override
	public void notifyEncodingAdvancement(float percentage) {
		progressEncoding.setProgress(percentage);
	}

	@Override
	public void notifyChannelAdvancement(float percentage) {
		progressChannel.setProgress(percentage);
	}

	@Override
	public void notifyMessage(String messsage) {
		logOutputPanel.setText( logOutputPanel.getText() + "\n" + messsage);
		ScrollBar scrollBar = (ScrollBar)logOutputPanel.lookup(".scroll-bar:vertical");
		if ( scrollBar != null ) {
			scrollBar.setStyle("-fx-opacity: 0;");
		}
	}
	
	
}
