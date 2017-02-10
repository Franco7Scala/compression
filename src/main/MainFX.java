package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

	
	@Override
	public void start(Stage stage) throws Exception {
		/*Parent root = FXMLLoader.load(getClass().getResource("/ui/fx/Test.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		*/
		
		Parent root = FXMLLoader.load(getClass().getResource("/ui/fx/MainPanel.fxml"));
		Scene scene = new Scene(root);
		//scene2.getStylesheets().add(MainFX.class.getResource("/ui/fx/resources/css/design.css").toExternalForm());
		//scene2.getStylesheets().add(MainFX.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
		scene.getStylesheets().add(MainFX.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
		stage.setMinWidth(920);
		stage.setMinHeight(450);
		stage.setScene(scene);
		stage.show();
	}
	

}
