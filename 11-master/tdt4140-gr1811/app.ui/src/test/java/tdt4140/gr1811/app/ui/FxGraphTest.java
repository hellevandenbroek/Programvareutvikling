package tdt4140.gr1811.app.ui;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.testfx_utils.ScaledApplicationTest;

public class FxGraphTest extends ScaledApplicationTest {

	private static boolean rdyBefore = true;
	
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
		Parent root = FXMLLoader.load(this.getClass().getResource("graph/bloodsugar_graph.fxml"));
		
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@Test
	public void enterDate() {
		clickOn("#startDate");
		
		write("2/19/2018").push(KeyCode.TAB);
		//write("00").push(KeyCode.TAB);
		//write("55").push(KeyCode.TAB);
		clickOn("#endDate");
		write("2/20/2018").push(KeyCode.TAB);
		//write("12").push(KeyCode.TAB);
		//write("32").push(KeyCode.TAB);
		
		//FxAssert.verifyThat("#startDate", DatePickerMatcher.hasText("2/19/2018"));
		//FxAssert.verifyThat("#startHour", NodeMatchers.hasText("00"));
		//FxAssert.verifyThat("#endHour", NodeMatchers.hasText("12"));
		//FxAssert.verifyThat("#endDate", DatePickerMatcher.hasText("2/20/2018"));
		//FxAssert.verifyThat("#startMinute", NodeMatchers.hasText("55"));
		//FxAssert.verifyThat("#endMinute", NodeMatchers.hasText("32"));
		
	}
	
	@Test
	@Ignore // TODO: Depends on database, maybe create mock checkboxes?
	public void correctNumberOfCheckboxes() {
		// TODO get number of checkboxes from VBox
		for (int i = 0; i < 6; i++) {
			FxAssert.verifyThat("#vboxListCheckBoxes" , NodeMatchers.hasChild("#checkboxID" + i));
		}
		
	}
	
	@Test
	@Ignore // TODO: Depends on database, maybe create mock checkboxes?
	public void pressCheckboxes135() {
		clickOn("#checkboxID1");
		clickOn("#checkboxID3");
		clickOn("#checkboxID5");
		
		FxAssert.verifyThat("#checkboxID1", NodeMatchers.isEnabled());
		FxAssert.verifyThat("#checkboxID3", NodeMatchers.isEnabled());
		FxAssert.verifyThat("#checkboxID5", NodeMatchers.isEnabled());

	}
	
	@Test
	public void checkActionUpdateButton() {
		
		// TODO check what button does
	}
	
	@Test
	@Ignore // TODO: Depends on database connection, maybe create mock checkboxes
	public void checkGraphNode() {
		clickOn("#checkboxID0");
		FxAssert.verifyThat("#0XYID19", NodeMatchers.isNotNull());
	}
	
	
}
