package tdt4140.gr1811.app.ui.graph;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tdt4140.gr1811.app.csv.CsvTable;
import tdt4140.gr1811.app.dao.DataProvider_PulseDao;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.CredentialsListener;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.ui.filter.FilterController;
import tdt4140.gr1811.app.ui.util.Alerter;

public class FxPulseGraphController implements CredentialsListener {

	@FXML private LineChart<Number, Number> lineChart;
	@FXML private NumberAxis xAxis;
	@FXML private NumberAxis yAxis;		
	@FXML private DatePicker startdate, enddate;
	@FXML private Label displayID, displayBPM, displayTime;	
	@FXML private VBox vboxDummyReplaced;
	@FXML private Button filterButton;
	@FXML private Slider startSlider, endSlider;
	@FXML private Button buttonSave;
	private VBoxCheckbox<DataProvider> vboxCheckboxes;

	private long start = 0;
	private long end = 0;

	private DecimalFormat numberformat = new DecimalFormat("#.#");


	public void initialize() {
		// place our VBoxCheckbox inside the vbox
		vboxCheckboxes = new VBoxCheckbox<>(vboxDummyReplaced);
		vboxCheckboxes.setPadding(new Insets(5));
		vboxCheckboxes.setSpacing(5);
		vboxDummyReplaced.getChildren().add(vboxCheckboxes);
		
		// Updated when changed date
		startdate.valueProperty().addListener((obs, oldv, newv) -> {
			if(!date_update()) {
				startdate.setValue(oldv);
			}
		});
		enddate.valueProperty().addListener((obs, oldv, newv) -> {
			if(!date_update()) {
				enddate.setValue(oldv);
			}
		});
		startSlider.valueProperty().addListener((obs, oldv, newv) -> {
			if(!date_update()) {
				startSlider.setValue(oldv.doubleValue() - 1);
			}
		});
		endSlider.valueProperty().addListener((obs, oldv, newv) -> {
			if(!date_update()) {
				endSlider.setValue(oldv.doubleValue() + 1);
			}
		});
		
		// Listen to credentials
		CredentialsFactory.addListener(this);
		
		// make graph update if checkbox selection changes
		vboxCheckboxes.getSelectectedItems().addListener((ListChangeListener<DataProvider>) c -> {
			while (c.next()) {
				if (c.wasAdded() || c.wasRemoved()) {
					for (DataProvider removedItem : c.getRemoved()) {
						removeFromGraph(removedItem);
					}
					for (DataProvider addedItem : c.getAddedSubList()) {
						addToGraph(addedItem);
					}
				}
			}
			buttonSave.setDisable(c.getList().size() == 0);
		});
		buttonSave.setDisable(true);
		
		filterButton.setOnAction(ae -> {
			displayFilter();
		});
		
		buttonSave.setOnAction(ae -> {
			// find which dataprovider to save
			List<DataProvider> selected = vboxCheckboxes.getSelectectedItems();
			if (selected.size() == 0) {
				return;
			}
			DataProvider toExport = selected.get(0);
			if (selected.size() > 1) {
				List<String> names = new ArrayList<>();
				for (DataProvider d : selected) {
					names.add(d.getFirstName());
				}
				ChoiceDialog<String> dialog = new ChoiceDialog<>(names.get(0), names);
				dialog.setTitle("Velg datagiver");
				dialog.setHeaderText("Velg hvilken datagiver du vil eksportere");
				dialog.setContentText("Navn:");
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					String chosen = result.get();
					for (DataProvider d : selected) {
						if (d.getFirstName().equals(chosen)) {
							toExport = d;
						}
					}
				} else {
					return;
				}
			}
			// with dataprovider, export data
			FileChooser fileChooser = new FileChooser();
			File saveTo = fileChooser.showSaveDialog(buttonSave.getScene().getWindow());
			if (saveTo == null) {
				return;
			}
			
			CsvTable cvsTable = new CsvTable("Tidspunkt", "Verdi");
			for (Timestamp time : toExport.getPulse_data().keySet()) {
				String timeEntry = time.toString();
				String value = toExport.getPulse_data().get(time).toString();
				cvsTable.addRow(timeEntry, value);
			}
			try {
				cvsTable.save(saveTo);
			} catch (IOException e) {
				e.printStackTrace();
				Alerter.exception(null, "Et problem oppstod under lagring av data", e);
			}
		});
	}
	
	private void displayFilter() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../filter/Filter.fxml"));
			Parent root = loader.load();
			Scene connectScene = new Scene(root);
			Stage connectStage = new Stage();
			connectStage.setScene(connectScene);
			connectStage.setTitle("Filtrer datagivere");
			connectStage.initModality(Modality.APPLICATION_MODAL);	
			connectStage.showAndWait();
			
			Object controllerObj = loader.getController();
			
			if (controllerObj instanceof FilterController) {
				FilterController fc = (FilterController) controllerObj;
				List<Integer> dp_id = fc.getDataProviders();
				if(dp_id != null) {
					updateCheckboxes(dp_id);
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateCheckboxes() {
		vboxCheckboxes.removeAll();
		if (CredentialsFactory.isReady() && CredentialsFactory.get() != null) {
			try {
				DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
				DataProvider_PulseDao dao = new DataProvider_PulseDao(ds);
				List<DataProvider> dataproviders = dao.getAllDataProviders_Pulse();
				
				for (DataProvider dp : dataproviders) {
					vboxCheckboxes.addCheckbox(dp, dp.toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Alerter.exception(null, "Noe galt skjedde under oppkobling mot databasen.", e);
			}
		}
	}
	
	private void updateCheckboxes(List<Integer> id_list) {
		vboxCheckboxes.removeAll();
		if (CredentialsFactory.isReady() && CredentialsFactory.get() != null) {
			try {
				List<DataProvider> dataproviders = new ArrayList<>();
				DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
				DataProvider_PulseDao dao = new DataProvider_PulseDao(ds);
				for(Integer id : id_list) {
					DataProvider dp = dao.getDataProvider_Pulse(id);
					if(dp != null) {
						dataproviders.add(dp);
					}				
				}				
				
				for (DataProvider dp : dataproviders) {
					vboxCheckboxes.addCheckbox(dp, dp.toString());
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Alerter.exception(null, "Noe galt skjedde under oppkobling mot databasen.", e);
			}
		}
	}

	private void addToGraph(DataProvider addedItem) {
		XYChart.Series<Number, Number> series = new XYChart.Series<>();	

		//Add data to series
		for(Map.Entry<Timestamp, Integer> entry : addedItem.getPulse_data().entrySet()) {
			XYChart.Data<Number, Number> node = new XYChart.Data<>((double)(entry.getKey().getTime()-start)/60000, entry.getValue());
			series.getData().add(node);
		}

		//Set start and end for x-axis
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(0);
		double upperbound = ((end - start) / 60000)-60;
		xAxis.setUpperBound(upperbound);
		xAxis.setTickUnit(get_tickCount(upperbound));

		//Add graph to chart
		series.setName(addedItem.toString());
		lineChart.getData().add(series);
		add_hover_node(lineChart);

	}

	private void removeFromGraph(DataProvider removedItem) {
		// Update the series values
		for (XYChart.Series<Number, Number> serie : lineChart.getData()) {
			// Looping through changing each node
			if(serie.getName().equals(removedItem.toString())) {
				lineChart.getData().remove(serie);
				break;
			}
		}
	}

	/**
	 * Called by "Tilbakestill" button
	 */
	public void reset() {
		lineChart.getData().clear();
		end = 0;
		start = 0;
		displayID.setText("");
		displayBPM.setText("");
		displayTime.setText("");
		//Reset checkboxes
		vboxCheckboxes.deselectAll();
	}

	/**
	 * Update the time of the graph, return true if success
	 */
	public boolean date_update() {
		
		//Validate datepicker
		if(!validateDatePicker()) {
			Alerter.error("Ugyldig dato", "Startdato må være før sluttdato.");
			return false;
		}

		long previousstart = start;

		long[] start_end = get_start_end();
		
		start = start_end[0];
		end = start_end[1];

		// Set start and end for x-axis
		xAxis.setAutoRanging(false);
		xAxis.setLowerBound(0);
		double upperbound = Math.ceil((end - start) / 3600000.0) - 1;
		xAxis.setUpperBound(upperbound);
		xAxis.setTickUnit(get_tickCount(upperbound));

		// Update the series values
		for (XYChart.Series<Number, Number> serie : lineChart.getData()) {
			// Looping through changing each node
			for (XYChart.Data<Number, Number> node : serie.getData()) {
				Number previousvalue = node.getXValue();
				node.setXValue(((double) previousvalue + (double) (previousstart - start) / 3600000.0));
			}
		}
		return true;
	}

	private void add_hover_node(LineChart<Number, Number> lineChart) {
		// Loop through all the series
		for (XYChart.Series<Number, Number> series : lineChart.getData()) {
			// Loop through each node in the series
			for (XYChart.Data<Number, Number> node : series.getData()) {

				// Add eventlisteners
				node.getNode().setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						node.getNode().setCursor(Cursor.HAND);
						displayID.setText(series.getName());
						displayBPM.setText(numberformat.format(node.getYValue()));
						
						long x_value = (long) (node.getXValue().doubleValue()*60000);	
						
						Date date = new Date(start + x_value);
						displayTime.setText(new SimpleDateFormat("dd.MM.yyyy - HH:mm").format(date));
					}
				});
				node.getNode().setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						node.getNode().setCursor(Cursor.DEFAULT);
					}
				});
			}
		}
	}

	private long[] get_start_end() {

		long[] start_end = new long[2];

		// Get starttime and endtime in millis
		start = end = System.currentTimeMillis();
		start -= 10800000;

		// Add clocktime
		double starthour = startSlider.getValue();
		double endhour = endSlider.getValue();

		//Begrense muligheter for klokkeslett
		if(starthour < 0 || starthour > 24 ||  endhour < 0 || endhour > 24) {
			starthour = 0;
			endhour = 24;
			Alerter.error("Ugyldig klokkeslett", "Valgt klokkeslett er ugyldig. Standard (00:00 - 24:00) benyttes.");
		}

		// If user has entered date -> change
		if (startdate.getValue() != null) {
			start = startdate.getValue().toEpochDay() * 86400000;
			start -= 3600000;
			start += (starthour * 60 * 60 * 1000);
		}
		if (enddate.getValue() != null) {
			end = enddate.getValue().toEpochDay() * 86400000;
			end += (endhour * 60 * 60 * 1000);
		}

		start_end[0] = start;
		start_end[1] = end;
		return start_end;
	}
	
	private boolean validateDatePicker() {
		
		// If either is null return true
		if(startdate.getValue() == null || enddate.getValue() == null) {
			return true;
		}
		
		return !enddate.getValue().isBefore(startdate.getValue());
	}

	@Override
	public void credentialsChanged(Credentials oldCredentials, Credentials newCredentials) {
		updateCheckboxes();
	}

	private int get_tickCount(double upperbound) {
		if(upperbound <= 25) {
			return 1;
		}
		if(upperbound <= 50) {
			return 2;
		}
		if(upperbound <= 100) {
			return 4;
		}
		if(upperbound <= 200) {
			return 6;
		}
		if(upperbound <= 300) {
			return 8;
		}
		if(upperbound <= 400) {
			return 10;
		}

		return 0;
	}
}
