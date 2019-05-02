package tdt4140.gr1811.app.ui;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.testfx_utils.ScaledApplicationTest;
import tdt4140.gr1811.app.ui.filter.FilterController;

public class FxFilterTest extends ScaledApplicationTest{
	
private static boolean rdyBefore = true;

	FilterController controller;
	
	@BeforeClass
	public static void beforeClass() {
		Headless.headless();
		
		// disable database
		rdyBefore = CredentialsFactory.setReady(false);
	}
	
	@AfterClass
	public static void restoreDatabase() {
		CredentialsFactory.setReady(rdyBefore);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("filter/Filter.fxml"));
		Parent root = loader.load();		
		Scene scene = new Scene(root);
		controller = loader.getController();
		stage.setScene(scene);
		stage.show();
	}
	
	//Check that boxes are visible after checkbox click
	@Test
	public void checkboxTest() {
		clickOn("#ageCheck");
		clickOn("#heightCheck");
		clickOn("#weightCheck");
		clickOn("#sexCheck");
		FxAssert.verifyThat("#ageBox", NodeMatchers.isVisible());	
		FxAssert.verifyThat("#heightBox", NodeMatchers.isVisible());
		FxAssert.verifyThat("#weightBox", NodeMatchers.isVisible());
		FxAssert.verifyThat("#sexBox", NodeMatchers.isVisible());
	}

}
