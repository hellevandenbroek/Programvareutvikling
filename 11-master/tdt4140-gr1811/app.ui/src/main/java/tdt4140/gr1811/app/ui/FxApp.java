package tdt4140.gr1811.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FxApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("D1Abytes");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("logo3.png")));
		stage.show();
	}


	public static void main(String[] args) {
		launch(args);
	}
}
