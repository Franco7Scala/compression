package test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JComboBox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;

import engine.facade.SimulatorFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ui.compressor.utilities.UISupport;

public class TestController implements Initializable {
	
	private SimulatorFacade facade;
	private String[] methods;
	
	@FXML
	private AnchorPane pane;

	@FXML
    private Label compressionLabel;

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
    private Label dragNdropLabel;
    
    @FXML
    private JFXProgressBar progressCompression;
    
    @FXML
    void print(ActionEvent event) {
    	System.out.println("PWWWWWNWWNNNENENENENE");
    	System.out.println(clockTextField.getText());
    	System.out.println(methodsComboBox.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    void captureFileName(DragEvent event) {
    	System.out.println("FUNGEEEEEE");
      Dragboard board = event.getDragboard();
      if (board.hasFiles()) {
        List<File> files = board.getFiles();
        System.out.println(files.get(files.size()-1).getAbsolutePath());
      }
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		facade = SimulatorFacade.sharedInstance();
		methods = facade.getAllCompressionMethods();
		methodsComboBox.getItems().clear();
		methodsComboBox.setItems(FXCollections.observableList(Arrays.asList(methods)));
		methodsComboBox.getSelectionModel().selectFirst();

		
		 

	}


}
