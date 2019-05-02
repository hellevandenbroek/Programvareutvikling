package tdt4140.gr1811.web.client;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tdt4140.gr1811.app.ui.util.Alerter;

public class ServerConnectController {
	
	@FXML public VBox port_vbox;
	@FXML public TextField server_field, port_field;
	@FXML public CheckBox standard_port;
	@FXML public Button close_button, connect_button;

	private TCPClient tcp_client;
	
	
	public void initialize() {
		port_vbox.setVisible(false);
		port_vbox.setManaged(false);
		standard_port.selectedProperty().addListener((obs, oldv, newv) -> {
			if (newv != null) {
				port_vbox.setVisible(!newv.booleanValue());
				port_vbox.setManaged(!newv.booleanValue());
			}
		});
		
		connect_button.setOnAction(ae -> {
			String server = server_field.getText();
			int port = 64672;
			if(!standard_port.isSelected() && !port_field.getText().isEmpty()) {
				port = Integer.parseInt(port_field.getText());
			}
			try {
				tcp_client = new TCPClient(server, port);
			} catch (IOException e) {
				Alerter.error(null, "Failed to connect to server");
			}
			connect_button.getScene().getWindow().hide();			
		});
		close_button.setOnAction(e -> {
			close_button.getScene().getWindow().hide();			
		});
	}
	
	public TCPClient getTCPClient() {
		return tcp_client;
	}
	
	
	

}
