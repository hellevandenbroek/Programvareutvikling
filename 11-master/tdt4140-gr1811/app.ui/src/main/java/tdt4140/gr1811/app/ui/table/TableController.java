package tdt4140.gr1811.app.ui.table;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import tdt4140.gr1811.app.csv.CsvTable;
import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.CredentialsListener;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.ui.util.Alerter;

public class TableController implements CredentialsListener {

	// FXML elements
	@FXML
	private TableView<DataProvider_TableRow> table;
	@FXML
	private Label labelTitle;

	@FXML
	private TableColumn<DataProvider_TableRow, String> fnavn;
	@FXML
	private TableColumn<DataProvider_TableRow, String> enavn;
	@FXML
	private TableColumn<DataProvider_TableRow, String> pnr;
	@FXML
	private TableColumn<DataProvider_TableRow, String> dato;
	@FXML
	private TableColumn<DataProvider_TableRow, String> k;
	@FXML
	private TableColumn<DataProvider_TableRow, Number> h;
	@FXML
	private TableColumn<DataProvider_TableRow, Number> v;
	@FXML
	private TableColumn<DataProvider_TableRow, String> status;
	@FXML
	private Button buttonSave;
	
	
	public void initialize() throws IOException {
		
		// Listen to credentials
		CredentialsFactory.addListener(this);
		
		// Cell value factories
		fnavn.setCellValueFactory(param -> param.getValue().FnameProperty());
		enavn.setCellValueFactory(param -> param.getValue().EnameProperty());
		pnr.setCellValueFactory(param -> param.getValue().PnrProperty());
		dato.setCellValueFactory(param -> param.getValue().DateProperty());
		k.setCellValueFactory(param -> param.getValue().SexProperty());
		h.setCellValueFactory(param -> param.getValue().HeightProperty());
		v.setCellValueFactory(param -> param.getValue().WeightProperty());	
		status.setCellValueFactory(param -> param.getValue().StatusProperty());
		
		updateTable();
		
		buttonSave.setOnAction(ae -> {
			FileChooser fileChooser = new FileChooser();
			File saveTo = fileChooser.showSaveDialog(buttonSave.getScene().getWindow());
			if (saveTo == null) {
				return;
			}

			List<TableColumn<DataProvider_TableRow, ? extends Object>> tableCols = new ArrayList<>();
			tableCols.addAll(Arrays.asList(fnavn, enavn, pnr, dato, k, h, v, status));

			// headers
			List<String> headers = tableCols.stream().map(t -> t.getText()).collect(Collectors.toList());
			CsvTable csvTable = new CsvTable(headers);
			System.out.println(headers);

			// rows
			for (DataProvider_TableRow provider : table.getItems()) {
				List<String> row = tableCols.stream().map(t -> t.getCellData(provider).toString())
						.collect(Collectors.toList());
				csvTable.addRow(row);
			}

			try {
				csvTable.save(saveTo);
			} catch (IOException e) {
				e.printStackTrace();
				Alerter.exception(null, "Det oppstod et problem under lagring av data.", e);
			}
		});
	}
	
	private void updateTable() {
		if (CredentialsFactory.isReady() && CredentialsFactory.get() != null) {
			try {
				table.getItems().clear();
				DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
				DataProviderDao dao = new DataProviderDao(ds);
				List<DataProvider> test = dao.getAllDatagivers_tableData();
				for (DataProvider d : test) {
					table.getItems().add(TableAdapter.convert(d));
				};
			} catch (SQLException e) {
				e.printStackTrace();
				Alerter.exception(null, "Noe galt skjedde under oppkobling mot databasen.", e);
			}
		}
	}

	@Override
	public void credentialsChanged(Credentials oldCredentials, Credentials newCredentials) {
		if (newCredentials == null) {
			table.getItems().clear();
		} else {
			updateTable();
		}
	}
}
