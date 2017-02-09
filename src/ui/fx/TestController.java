package ui.fx;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javax.swing.JComboBox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import engine.facade.SimulatorFacade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class TestController implements Initializable {
	
	private SimulatorFacade facade;
	private String[] methods;

	@FXML
    private Label compressionLabel;

    @FXML
    private Label dragNdropLabel;

    @FXML
    private JFXTextField clockTextField;

    @FXML
    private JFXTextField epiTextField;

    @FXML
    private JFXTextField emTextField;

    @FXML
    private JFXTextField elTextField;

    @FXML
    private JFXComboBox<String> methodsComboBox;

    @FXML
    private Label energyParametersLabel;

    @FXML
    private JFXButton provaButton;
    
    @FXML
    void print(ActionEvent event) {
    	System.out.println(clockTextField.getText());
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		methods = facade.getAllCompressionMethods();
		Arrays.asList(methods);
		methodsComboBox = new JFXComboBox();
		
	}

}
