package ui.simulator.controller;


import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import engine.facade.SimulatorFacade;
import engine.utilities.SimulatorDelegate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;


public class MainController implements Initializable, SimulatorDelegate {
	private SimulatorFacade facade = SimulatorFacade.sharedInstance();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		facade.delegate = this;
		
		//JFXButton doneChannelButton = new JFXButton("JFoenix Button");
		 
		 //doneChannelButton = new JFXButton("Raised Button".toUpperCase());
		 //doneChannelButton.getStyleClass().add("button-raised");
		 
		 //a11 = new JFXTextField();                    
		 //a11.setPromptText("A");
	}
	
	@FXML
	void cleanTextField(KeyEvent event) {
		if ( ! event.getText().matches("[0-1]") ) {
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
	
	// 2nd panel
	
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
	private JFXButton doneChannelButton;
	
	@FXML
	private JFXProgressBar progressChannel;
	
	// 1st panel
	
	// 2nd panel
	
	// 4th panel
	@FXML
	private JFXButton loadSimulationButton;
	
	@FXML
	private Label logOutputPanel;

	
	// 3rd panel
	@FXML
	void doneChannel(ActionEvent event) {
		System.out.println("called");
		System.out.println(a11.getText());
	}
	
	// 4th panel
	@FXML
	void loadSimulation(ActionEvent event) {
		facade.startSimulation();
	}

	// Delegated methods
	@Override
	public void notifyCompressionAdvancement(float percentage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyEncodingAdvancement(float percentage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyChannelAdvancement(float percentage) {
		progressChannel.setProgress(percentage);
	}

	@Override
	public void notifyMessage(String messsage) {
		logOutputPanel.setText( logOutputPanel.getText() + "\n" + messsage);
	}
	
	
}
