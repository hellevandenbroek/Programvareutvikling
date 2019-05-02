package tdt4140.gr1811.app.ui.connect;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.ui.util.Alerter;

public class ConnectController {

	@FXML private TextField textfieldServer,textfieldPort, textfieldDatabaseName, textfieldUsername;

	@FXML private CheckBox checkboxUseDefaultPort, checkboxRemember;

	@FXML private VBox vboxPort;
	
	@FXML private PasswordField textfieldPassword;

	@FXML private Button buttonConnect, buttonCancel;
	
	public void initialize() {
		vboxPort.setVisible(false);
		vboxPort.setManaged(false);
		checkboxUseDefaultPort.selectedProperty().addListener((obs, oldv, newv) -> {
			if (newv != null) {
				vboxPort.setVisible(!newv.booleanValue());
				vboxPort.setManaged(!newv.booleanValue());
			}
		});
		
		// if previous credentials exists, use them
		if (CredentialsFactory.get() != null) {
			Credentials prevCreds = CredentialsFactory.get();
			textfieldServer.setText(prevCreds.getServerName());
			if (prevCreds.getPort() != null) {
				textfieldPort.setText(prevCreds.getPort().toString());
				checkboxUseDefaultPort.setSelected(false);
			}
			textfieldDatabaseName.setText(prevCreds.getDatabaseName());
			textfieldUsername.setText(prevCreds.getUsername());
			textfieldPassword.setText(prevCreds.getPassword());
		}
		
		// If credentials file exists, the credentials have been saved before
		checkboxRemember.setSelected(CredentialsFactory.fileExists());
		
		// verify button disable with listeners
		textfieldServer.textProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		checkboxUseDefaultPort.selectedProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		textfieldPort.textProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		textfieldDatabaseName.textProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		textfieldUsername.textProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		textfieldPassword.textProperty().addListener((obs, oldv, newv) -> verifyConfirmButton());
		
		verifyConfirmButton();
		
		buttonCancel.setOnAction(ae -> {
			buttonCancel.getScene().getWindow().hide();
		});
		
		buttonConnect.setOnAction(ae -> {
			// Create new credentials
			String serverName = textfieldServer.getText();
			Integer port = null;
			if (!checkboxUseDefaultPort.isSelected()) {
				try {
					port = Integer.valueOf(textfieldPort.getText());
				} catch (NumberFormatException e) {
					Alerter.error(null, "Porten må være et heltall. Var " + textfieldPort.getText());
					e.printStackTrace(); 
					return;
				}
			}
			String databaseName = textfieldDatabaseName.getText();
			String username = textfieldUsername.getText();
			String password = textfieldPassword.getText();
			Credentials c = new Credentials(serverName, port, databaseName, username, password);
			if (checkboxRemember.isSelected()) {
				// save to disk
				try {
					CredentialsFactory.saveCredentialsLocalFile(c);
				} catch (IOException e) {
					Alerter.exception("Feil ved lagring av databasedetaljer!", "Could not save the credentials."
							+ " See stack trace for details.", e);
					e.printStackTrace();
					return;
				}
			} else {
				// delete from disk
				CredentialsFactory.deleteFromDisk();
				CredentialsFactory.saveCredentials(c);
			}
			buttonConnect.getScene().getWindow().hide();
		});
	}
	
	private void verifyConfirmButton() {
		boolean disabled = false;
		if (textfieldServer.getText().isEmpty())
			disabled = true;
		if (!checkboxUseDefaultPort.isSelected() && textfieldPort.getText().isEmpty())
			disabled = true;
		if (textfieldDatabaseName.getText().isEmpty())
			disabled = true;
		if (textfieldUsername.getText().isEmpty())
			disabled = true;
		if (textfieldPassword.getText().isEmpty())
			disabled = true;
		buttonConnect.setDisable(disabled);
	}

}
