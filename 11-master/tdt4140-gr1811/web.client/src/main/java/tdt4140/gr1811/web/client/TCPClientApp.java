package tdt4140.gr1811.web.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TCPClientApp extends Application{
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("Server Client");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
