package ui.simulator.controller;


import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import engine.encoding.EncoderParameters;
import engine.energy.EnergyProfilerParametric;
import engine.facade.SimulatorFacade;
import engine.utilities.SimulatorDelegate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class MainController implements Initializable, SimulatorDelegate {
	private SimulatorFacade facade = SimulatorFacade.sharedInstance();
	private EncoderParameters parameters;
	private String compressionMethod;
	private String fileName;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		facade.delegate = this;
		// 1st panel
		methodsComboBox.getItems().clear();
		methodsComboBox.setItems(FXCollections.observableList(Arrays.asList(facade.getAllCompressionMethods())));
		methodsComboBox.getSelectionModel().selectFirst();
		changeCompressionMethod(null);
		// 2nd panel
		polynomialsComboBox.getItems().clear();
		polynomialsComboBox.setItems(FXCollections.observableList(Arrays.asList(facade.getPolynomials())));
		polynomialsComboBox.getSelectionModel().selectFirst();
		changePolynomial(null);
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
	
	@FXML
    private StackPane container;
	
	// 1st panel
	@FXML
    private Label dragNdropLabel;
	
	@FXML
    private Label fileNameLabel;
	
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
    
    @FXML
	private JFXButton infoEnergyButton;
	
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
	private Label logOutputPanel;

	// 1st panel
	@FXML
	void changeCompressionMethod(ActionEvent event) {
		compressionMethod = methodsComboBox.getValue();
	}
	
	@FXML
	public void showEnergyInformations() {
		String message = ("These values are necessaries to determinate the energy consumption:\n\n"
					    + "Clock: The CPU's clock (2.8);\n"
				        + "EPI (Energy per instruction): Average of energy consumed for each instruction (6.86);\n"
				        + "Em (Energy per memory access): Average of energy consumed for each memory access (12.23);\n"
				        + "El: Hardware constant to have a more accureted calcolous, if available (1).\n\n"
				        + "(The default values are for a CPU Intel® Haswell i7)\n");
		showDialog("Information", message, Color.BLUE, true);
	}
	
	@FXML
    void captureFileName(DragEvent event) {
      Dragboard board = event.getDragboard();
      if (board.hasFiles()) {
        List<File> files = board.getFiles();
        fileName = files.get(files.size()-1).getAbsolutePath();
        fileNameLabel.setText("File loaded: " + fileName.substring(fileName.lastIndexOf('/')+1, fileName.length()));
      }
    }
	
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
		if ( fileName == null ) {
			String message = "You have to drop a file inside the arrow before start the simulation!";
			showDialog("Oops...", message, Color.RED, true);
			return;
		}
		logOutputPanel.setText("");
		facade.setFileName(fileName);
		facade.setCurrentCompressionMethod(compressionMethod);
		try {
			facade.setEnergyProfiler(new EnergyProfilerParametric(Float.parseFloat(clockTextField.getText()), 
															  	  Float.parseFloat(epiTextField.getText()), 
															      Float.parseFloat(emTextField.getText()), 
															      Float.parseFloat(elTextField.getText())));
		} catch (Exception e) {			
			String message = "For this simulaton will be used the default values for energy consumption!";
			showDialog("Information", message, Color.BLUE, true);
			facade.setEnergyProfiler(new EnergyProfilerParametric((float)2.8, (float)6.86, (float)12.23, (float)1));
		}
		// configuring encoding
		facade.setEncoderParameters(parameters);
		// configuring channel
		facade.setChannelMatrix(new float[][] { {Float.parseFloat(a11.getText()), Float.parseFloat(a12.getText())},
			 								    {Float.parseFloat(a21.getText()), Float.parseFloat(a21.getText())} });	
		new Thread () {
			public void run() {
				facade.startSimulation();
			}
		}.start();
	}

	// Delegated methods
	@Override
	public void notifyCompressionAdvancement(float percentage) {
		try {
			progressCompression.setProgress(percentage);
		}catch (Exception e) {}
	}

	@Override
	public void notifyEncodingAdvancement(float percentage) {
		try {
			progressEncoding.setProgress(percentage);
		} catch (Exception e) {}
	}

	@Override
	public void notifyChannelAdvancement(float percentage) {
		try {
			progressChannel.setProgress(percentage);
		} catch (Exception e) {}
	}

	@Override
	public void notifyMessage(String message) {
		try {
			Platform.runLater(new Runnable() {
				public void run() {
					logOutputPanel.setText( logOutputPanel.getText() + "\n" + message );
				}
			});
		} catch (Exception e) {}
	}

	@Override
	public void notifyImportantMessage(String message) {
		showDialog("Message", message, Color.BLACK, false);
	}

	@Override
	public void notifyErrorMessage(String message) {
		showDialog("Oops...", message, Color.RED, false);
	}
	
	public void notifyInfoMessage(String message) {
		showDialog("Information", message, Color.BLUE, false);
	}

	private void showDialog(String title, String message, Color color, boolean main) {
		try {
			Runnable dialogThread = new Runnable() {
				public void run() {
					JFXDialogLayout content = new JFXDialogLayout();
					Text textTitle = new Text(title);
					textTitle.setFill(color);
					textTitle.setFont(Font.font("Roboto", FontWeight.BOLD, 25));
					content.setHeading(textTitle);
					content.setBody(new Text(message));
					JFXDialog dialog = new JFXDialog(container, content, JFXDialog.DialogTransition.CENTER);
					JFXButton button = new JFXButton("Close");
					button.setStyle("-fx-text-fill: #817A78;");
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							dialog.close();
						}
					});
					content.setActions(button);
					dialog.show();
				}
			};
			if ( main ) {
				dialogThread.run();
			}
			else {
				Platform.runLater(dialogThread);
			}
		} catch (Exception e) {}
	}


}
