package tdt4140.gr1811.app.ui.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Utility methods for showing a simple dialog
 * <p>
 * Can be called from any thread, will be shown on the JavaFX application thread
 */
public class Alerter {
	
	/**
	 * Show an error dialog with an exception stack trace as expandable content
	 * 
	 * @param header header text, {@code null} for no header
	 * @param message main content text
	 * @param e the exception whose track trace is shown to the user
	 */
	public static void exception(String header, String message, Exception e) {
		Runnable r = () -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Exception Dialog");
			alert.setHeaderText(header);
			alert.setContentText(message);
			
			// print exception to stream
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			String stacktrace = sw.toString();
			pw.close();
			
			// Show stack trace in expandable GUI element
			Label label = new Label("The exception stack trace was:");
			
			TextArea ta = new TextArea(stacktrace);
			ta.setEditable(false);
			ta.setWrapText(true);
			
			ta.setPrefWidth(600);
			ta.setMaxWidth(Double.MAX_VALUE);
			ta.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(ta, Priority.ALWAYS);
			GridPane.setHgrow(ta, Priority.ALWAYS);
			
			GridPane gridpane = new GridPane();
			gridpane.setMaxWidth(Double.MAX_VALUE);
			gridpane.add(label, 0, 0);
			gridpane.add(ta, 0, 1);
			alert.getDialogPane().setExpandableContent(gridpane);
			
			alert.showAndWait();
		};
		run(r);
	}
	
	/**
	 * Show a simple information dialog
	 * 
	 * @param header {@code null} for no header
	 * @param message
	 */
	public static void info(String header, String message) {
		Runnable r = () -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(header);
			alert.setContentText(message);
			alert.showAndWait();
		};
		run(r);
	}
	
	/**
	 * Show a simple warning dialog
	 * 
	 * @param header {@code null} for no header
	 * @param message
	 */
	public static void warning(String header, String message) {
		Runnable r = () -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Warning Dialog");
			alert.setHeaderText(header);
			alert.setContentText(message);
			alert.showAndWait();
		};
		run(r);
	}
	
	/**
	 * Show a simple error dialog
	 * 
	 * @param header {@code null} for no header
	 * @param message
	 */
	public static void error(String header, String message) {
		Runnable r = () -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(header);
			alert.setContentText(message);
			alert.showAndWait();
		};
		run(r);
	}
	
	/**
	 * Runs a {@link Runnable} on the platform application thread
	 * 
	 * @param r
	 */
	private static void run(Runnable r) {
		if (Platform.isFxApplicationThread()) {
			r.run();
		} else {
			Platform.runLater(r);
		}
	}

}
