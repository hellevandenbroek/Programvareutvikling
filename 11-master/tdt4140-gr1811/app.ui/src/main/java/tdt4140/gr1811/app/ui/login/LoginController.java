package tdt4140.gr1811.app.ui.login;

import java.io.IOException;
import java.util.Base64;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.security.LoginCredentials;
import tdt4140.gr1811.app.security.LoginCredentialsFactory;
import tdt4140.gr1811.app.security.VerifyPassword;
import tdt4140.gr1811.app.ui.util.Alerter;

public class LoginController {
	
	@FXML VBox root;
	@FXML Button connect;
	@FXML TextField usernameField;
	@FXML PasswordField passwordField;
	@FXML CheckBox rememberCheckbox;
	@FXML Button loginButton;
	@FXML Label server_label, db_label;
	
	private boolean logged_in = false;
		
	public void initialize() {
		
		// Show popup if login with stored creds fail and if previous login information is stored use that
		if(LoginCredentialsFactory.getLoginCredentials() != null) {
			LoginCredentials logincreds = LoginCredentialsFactory.getLoginCredentials();
			String storedUsername = logincreds.getUsername();
			String storedPassword = new String(Base64.getDecoder().decode(logincreds.getPassword()));
			usernameField.setText(storedUsername);
			passwordField.setText(storedPassword);
			rememberCheckbox.setSelected(true);
			if(CredentialsFactory.get() == null) {
				Alerter.warning(null, "Fant ingen databaseinformasjon. Vennligst koble til database.");
			}
			else if(!VerifyPassword.verifyPassword(storedUsername, storedPassword)) {
				LoginCredentialsFactory.deleteFromDisk();
				Alerter.warning(null, "Kunne ikke logge inn med lagret informasjon.");
			} 
		}
		
		
		
		connect.setOnAction(e ->{
			try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("../connect/Connect.fxml"));
		        Parent root = loader.load();
		        Scene connectScene = new Scene(root);
		        Stage connectStage = new Stage();
		        connectStage.setScene(connectScene);
		        connectStage.setTitle("Koble til server...");
		        connectStage.initModality(Modality.APPLICATION_MODAL);
		        connectStage.showAndWait();
				verifyFields();
				updateDBInfo();
		    }catch (IllegalStateException | IOException el){
				Alerter.error(null, "Innlasting feilet.");
			}
		});
	
		
		loginButton.setOnAction(e -> {		
			login();
		});
		usernameField.setOnAction(e -> {
			login();
		});
		passwordField.setOnAction(e -> {
			login();
		});
		
		// verify button disable with listeners
		usernameField.textProperty().addListener((obs, oldv, newv) -> verifyFields());
		passwordField.textProperty().addListener((obs, oldv, newv) -> verifyFields());
		
		updateDBInfo();
		verifyFields();
		
		
	}
	
	private void login() {
		// If button is disabled do not login
		if(loginButton.isDisable()) {
			return;
		}
		
		try {
			// Read username / email and password from typed info
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			if(VerifyPassword.verifyPassword(username, password)) {
				logged_in = true;
				CredentialsFactory.setReady(true);
				// If checkbox is checked save login info
				if(rememberCheckbox.isSelected()) {
					LoginCredentials logincred = new LoginCredentials(username, Base64.getEncoder().encodeToString(password.getBytes()));
					LoginCredentialsFactory.saveLoginCredentialsLocalFile(logincred);
				}
				root.getScene().getWindow().hide();
			}
			else {
				CredentialsFactory.setReady(false);
				Alerter.error(null, "Feil brukernavn og/eller passord");
			}
		} catch (IOException e1) {
			CredentialsFactory.setReady(false);
			Alerter.warning(null, "Kunne ikke lagre login informasjon");
		}	
	}

	public boolean getLoggedIn() {
		return logged_in;
	}
	
	private void updateDBInfo() {
		// Fill in db-info if found
		if(CredentialsFactory.get() != null) {
			Credentials dbcreds = CredentialsFactory.get();
			server_label.setText(dbcreds.getServerName());
			db_label.setText(dbcreds.getDatabaseName());
		}
		else {

			server_label.setText("N / A");
			db_label.setText("N / A");
		}
	}
	
	private void verifyFields() {
		setDisabledFields(false);
		if(CredentialsFactory.get() == null) {
			setDisabledFields(true);
		} else {
			if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
				loginButton.setDisable(true);
			}	
		}
	}
	
	private void setDisabledFields(boolean disabled) {
		usernameField.setDisable(disabled);
		passwordField.setDisable(disabled);
		rememberCheckbox.setDisable(disabled);
		loginButton.setDisable(disabled);
	}

}
