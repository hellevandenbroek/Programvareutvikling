package tdt4140.gr1811.app.ui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.security.LoginCredentials;
import tdt4140.gr1811.app.security.LoginCredentialsFactory;
import tdt4140.gr1811.app.security.VerifyPassword;
import tdt4140.gr1811.app.ui.login.LoginController;
import tdt4140.gr1811.app.ui.util.Alerter;


public class FxAppController {

	// FXML elements

    @FXML private MenuItem menuitemConnect;
    @FXML private MenuItem menuitemClose;
    @FXML private MenuItem menuitemAbout;
	@FXML private ListView<MenuEntry> listviewMenu;
	@FXML private Label labelTitle;
	@FXML private VBox vboxContainer;
	@FXML private Button buttonCollapse;
	@FXML private Label labelMenu;
	@FXML private DatePicker datetime;
	@FXML private WebView logo;
	@FXML private Separator menuSep;
	@FXML private Label description;


	private boolean collapsed = false;
	
	private boolean checkedCredentialsStartup = false;

	public void initialize() {
			
		// Finally, changing the selection should update the vbox contents and title
		listviewMenu.getSelectionModel().selectedItemProperty().addListener((obs, oldval, newval) -> {
			if (newval != null) {
				vboxContainer.getChildren().setAll(newval.getNode());
				VBox.setVgrow(newval.getNode(), Priority.ALWAYS);
				labelTitle.setText(newval.getTitle());
				description.setText(newval.getDescription());
			}
		});
		listviewMenu.getSelectionModel().clearAndSelect(0);

		// collapse menu
		buttonCollapse.setOnAction(ae -> {
			buttonCollapse.setText(collapsed ? "« Skjul meny" : "»");
			for (Node n : Arrays.asList(labelMenu, listviewMenu, menuSep)) {
				n.setVisible(collapsed);
				n.setManaged(collapsed);

			}
			collapsed = collapsed == false;
		});
		
		menuitemConnect.setOnAction(ae -> {
			showConnectWindow();
		});
		
		menuitemAbout.setOnAction(ae -> {
			System.out.println("About menu clicked");
			String info = "D1Abytes er et program for en tjenesteyter "
					+ "som består av et forskerteam som samler "
					+ "blodsukker- og pulsnivå fra datagivere med "
					+ "diabetes type 1. Programmet tar inn data"
					+ " fra en database, som får data fra en ekstern "
					+ "app på datagivers side, og dataen blir illustrert"
					+ " i en graf. Datagiveren har mulighet til å redigere"
					+ " på data, inkludert endring og sletting av deres bruker.";
			Alerter.info("D1Abytes", info);
			// TODO: Create about
		});
		
		menuitemClose.setOnAction(ae -> {
			Platform.exit();
		});
		
		// Goal: Show popup when scene is loaded and stage is showing,
		// if login credentials are not set load popup
		if(LoginCredentialsFactory.getLoginCredentials() == null) {
			sceneListener();
		}
		// check if stored username and password are correct
		else {
			LoginCredentials logincreds = LoginCredentialsFactory.getLoginCredentials();
			String storedUsername = logincreds.getUsername();
			String storedPassword = new String(Base64.getDecoder().decode(logincreds.getPassword()));
			if(!VerifyPassword.verifyPassword(storedUsername, storedPassword)) {
				sceneListener();
			}
			else {
				loadMenu();
			}
		}	
	}
	
	private void showConnectWhenWindowShowing() {
		if (checkedCredentialsStartup) {
			return;
		}
		checkedCredentialsStartup = true;
		// wait for stage to show
		Window w = listviewMenu.getScene().getWindow();
		if (w.isShowing()) {
			showConnectWindow();
		} else {
			w.showingProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue != null && newValue.booleanValue()) {
						w.showingProperty().removeListener(this);
						showConnectWindow();
					}
				}
			});
		}
	}
	
	private void sceneListener() {
		// Wait for scene to finish rendering, so listen for scene change...
		System.out.println("No login credentials found, loading login popup");
		listviewMenu.sceneProperty().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				if (newValue != null) {
					listviewMenu.sceneProperty().removeListener(this);
					// Scene is assigned, now get stage
					if (newValue.getWindow() != null) {
						showConnectWhenWindowShowing();
					} else {
						// Stage still null, wait for stage to be assigned
						newValue.windowProperty().addListener(new ChangeListener<Window>() {
							@Override
							public void changed(ObservableValue<? extends Window> observable, Window oldValue,
									Window newW) {
								if (newW != null) {
									newValue.windowProperty().removeListener(this);
									showConnectWhenWindowShowing();
								}
							}
						});
					}
				}
			}
		});
	}
	
	private void showConnectWindow() {
		CredentialsFactory.setReady(false);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login/Login.fxml"));
			Parent root = loader.load();
			Scene connectScene = new Scene(root);
			Stage connectStage = new Stage();
			connectStage.setScene(connectScene);
			ContextMenu cntx = menuitemConnect.getParentPopup();
			connectStage.setTitle("Logg inn på D1Abytes");
			connectStage.setResizable(false);
			connectStage.getIcons().add(new Image(getClass().getResourceAsStream("logo3.png")));
			connectStage.initOwner(cntx.getScene().getWindow());
			connectStage.initModality(Modality.APPLICATION_MODAL);
			connectStage.showAndWait();
			Object o = loader.getController();
			if(o instanceof LoginController) {
				LoginController controller = (LoginController) o;
				if(controller.getLoggedIn()) {
					loadMenu();
				}
				
			}
			
		} catch (IOException e) {
			Alerter.exception(null, "An exception occured while attempting to load the connect menu", e);
			e.printStackTrace();
		}
	}
	
	private void loadMenu() {
		// Clear list view
		listviewMenu.getItems().clear();
		// Add blood sugar graph to menu
		try {
			Parent graphRoot_bloodsugar = (new FXMLLoader(getClass().getResource("graph/bloodsugar_graph.fxml"))).load();
			listviewMenu.getItems().add(new MenuEntry("Graf - Blodsukker", "Blodsukkergraf", "Graf for blodsukker", graphRoot_bloodsugar));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Add pulse graph to menu
		try {
			Parent graphRoot_pulse = (new FXMLLoader(getClass().getResource("graph/pulse_graph.fxml"))).load();
			listviewMenu.getItems().add(new MenuEntry("Graf - Puls", "Pulsgraf","Graf for puls", graphRoot_pulse));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Add tabell to menu
		try {
			Parent tabellRoot = (new FXMLLoader(getClass().getResource("table/Table.fxml"))).load();
			listviewMenu.getItems().add(new MenuEntry("Personinfo", "Personinformasjon", "Her får du informasjon om personer",tabellRoot));
		} catch (IOException e) {
			e.printStackTrace();
		}
		listviewMenu.getSelectionModel().select(0);
		listviewMenu.getFocusModel().focus(0);
	}
}