package tdt4140.gr1811.app.ui.filter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.FilterAttributes;
import tdt4140.gr1811.app.pojo.Sex;
import tdt4140.gr1811.app.ui.util.Alerter;

public class FilterController {
	
	@FXML private VBox ageBox, heightBox, weightBox;
	@FXML private CheckBox ageCheck, heightCheck, weightCheck, sexCheck; 
	@FXML private Slider minAgeSlider, maxAgeSlider, minHeightSlider, maxHeightSlider, minWeightSlider, maxWeightSlider;
	@FXML private ComboBox<String> sexBox;
	@FXML private Button cancelButton, filterButton;
	
	private List<Integer> dataProviders;
	
	
	
	public void initialize() {
		ageCheck.selectedProperty().addListener((obs, newv, oldv) -> {
			if(newv != null) {
				ageBox.setVisible(!newv.booleanValue());
				ageBox.setManaged(!newv.booleanValue());
			}
		});
		
		heightCheck.selectedProperty().addListener((obs, newv, oldv) -> {
			if(newv != null) {
				heightBox.setVisible(!newv.booleanValue());
				heightBox.setManaged(!newv.booleanValue());
			}
		});
		
		weightCheck.selectedProperty().addListener((obs, newv, oldv) -> {
			if(newv != null) {
				weightBox.setVisible(!newv.booleanValue());
				weightBox.setManaged(!newv.booleanValue());
			}
		});
		
		sexCheck.selectedProperty().addListener((obs, newv, oldv) -> {
			if(newv != null) {
				sexBox.setVisible(!newv.booleanValue());
				sexBox.setManaged(!newv.booleanValue());
			}
		});
		
		ageBox.setVisible(false);
		ageBox.setManaged(false);
		heightBox.setVisible(false);
		heightBox.setManaged(false);
		weightBox.setVisible(false);
		weightBox.setManaged(false);
		sexBox.setVisible(false);
		sexBox.setManaged(false);
		
		
		sexBox.getItems().addAll("Mann", "Kvinne", "Annet");
		
		cancelButton.setOnAction(e -> {
			cancelButton.getScene().getWindow().hide();
		});
		
		filterButton.setOnAction(e -> {
			dataProviders = filterDataProviders();
			if(dataProviders != null) {
				filterButton.getScene().getWindow().hide();
			}
		});		
		
	}
	
	public List<Integer> filterDataProviders(){
		
		List<Integer> dataProviders = new ArrayList<>();
		
		Integer minAge = null;
		Integer maxAge = null;
		Integer minHeight = null;
		Integer maxHeight = null;
		Integer minWeight = null;
		Integer maxWeight = null;
		Sex sex = null;
				
		if(ageCheck.isSelected()) {
			minAge = (int) minAgeSlider.getValue();
			maxAge = (int) maxAgeSlider.getValue();
			if(minAge > maxAge) {
				Alerter.error("Ugyldig filtering", "Minimum alder (" + minAge + ") må være mindre enn maksimum alder (" + maxAge + ")");
				return null;
			}
		}
		
		if(heightCheck.isSelected()) {
			minHeight = (int) minHeightSlider.getValue();
			maxHeight = (int) maxHeightSlider.getValue();
			if(minHeight > maxHeight) {
				Alerter.error("Ugyldig filtering", "Minimum høyde (" + minHeight + ") må være mindre enn maksimum høyde (" + minHeight + ")");
				return null;
			}
		}
		
		if(weightCheck.isSelected()) {
			minWeight = (int) minWeightSlider.getValue();
			maxWeight = (int) maxWeightSlider.getValue();
			if(minWeight > maxWeight) {
				Alerter.error("Ugyldig filtering", "Minimum vekt (" + minWeight + ") + må være mindre enn maksimum vekt (" + minWeight + ")");
				return null;
			}
		}
		
		if(sexCheck.isSelected()) {
			String filteredSex = sexBox.getValue();
			if (filteredSex.equals("Mann")) {
				sex = Sex.M;
			} else if (filteredSex.equals("Kvinne")) {
				sex = Sex.F;
			} else if (filteredSex.equals("Annet")) {
				sex = Sex.O;
			} else {
				sex = null;
				System.err.println("Warning: Filtering by sex, but none chosen!");
			}
		}
			
		FilterAttributes fa = new FilterAttributes();
		fa.setMinHeight(minHeight);
		fa.setMaxHeight(maxHeight);
		fa.setMinWeight(minWeight);
		fa.setMaxWeight(maxWeight);
		fa.setMinAge(minAge);
		fa.setMaxAge(maxAge);
		fa.setSex(sex);
		
		try {
			DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
			DataProviderDao dao = new DataProviderDao(ds);
			dataProviders = dao.getIdsByFilter(fa);
		} catch (SQLException e) {
			e.printStackTrace();
			Alerter.exception(null, "En feil oppstod ved filtrering", e);
		}
		
		return dataProviders;
		
	}

	public List<Integer> getDataProviders(){
		return dataProviders;
	}
	
}
