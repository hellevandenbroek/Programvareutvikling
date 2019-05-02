package tdt4140.gr1811.app.ui.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import tdt4140.gr1811.app.ui.util.BiMap;

public class VBoxCheckbox<T> extends VBox {
	
	/** max number of items that can be selected at a time */
	private final int MAX_SELECT_LIMIT = 5;
	private List<T> items = new ArrayList<T>();
	private ObservableList<T> selectedItems = FXCollections.observableArrayList();
	
	private Map<CheckBox, ChangeListener<Boolean>> checkboxToListener = new HashMap<>();
	private BiMap<T, CheckBox> bimap = new BiMap<>();
	
	/**
	 * Copies preferred size, max size, and min size from VBox
	 * 
	 * @param from vbox to copy from 
	 */
	public VBoxCheckbox(VBox from) {
		setPrefSize(from.getPrefWidth(), from.getPrefHeight());
		setMaxSize(from.getMaxWidth(), from.getMaxHeight());
		setMinSize(from.getMinWidth(), from.getMinHeight());
		setId(from.getId());
	}
	
	public void addCheckbox(T item, String label) {
		CheckBox chk = new CheckBox(label);
		items.add(item);
		ChangeListener<Boolean> listener = (observable, oldValue, newValue) -> {
			// If selected, check if limit is reached. If reached, deselect
			if (newValue) {
				if (selectedItems.size() == MAX_SELECT_LIMIT) {
					chk.setSelected(false);
					return;
				} else {
					T chkItem = bimap.getByV(chk);
					selectedItems.add(chkItem);
				}
			} else {
				T chkItem = bimap.getByV(chk);
				selectedItems.remove(chkItem);
			}
		};
		chk.selectedProperty().addListener(listener);
		
		getChildren().add(chk);
		checkboxToListener.put(chk, listener);
		bimap.put(item, chk);
	}
	
	public void removeCheckbox(T item) {
		CheckBox chk = bimap.getByK(item);
		if (chk == null) {
			return;
		}
		
		chk.setSelected(false);
		chk.selectedProperty().removeListener(checkboxToListener.get(chk));
		checkboxToListener.remove(chk);
		getChildren().remove(chk);
		bimap.removeByA(item);
		items.remove(item);
	}
	
	public void removeAll() {
		List<T> itemsCopy = new ArrayList<>(items);
		for (T item : itemsCopy) {
			removeCheckbox(item);
		}
	}
	
	public boolean isChecked(T item) {
		CheckBox chk = bimap.getByK(item);
		if (chk == null)
			return false;
		return chk.isSelected();
	}
	
	public void setChecked(T item, boolean value) {
		CheckBox chk = bimap.getByK(item);
		if (chk == null)
			return;
		chk.setSelected(value);
	}
	
	public void deselectAll() {
		for (T item : items) {
			CheckBox chk = bimap.getByK(item);
			if (chk != null) {
				chk.setSelected(false);
			}
		}
	}
	
	public ObservableList<T> getSelectectedItems() {
		return selectedItems;
	}
}
