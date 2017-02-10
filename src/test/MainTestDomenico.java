package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.MainSimulator;

public class MainTestDomenico extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/test/Test.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(MainSimulator.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
		scene.getStylesheets().add(MainSimulator.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
		stage.setMinWidth(920);
		stage.setMinHeight(450);
		stage.setScene(scene);
		stage.show();
	}
}
