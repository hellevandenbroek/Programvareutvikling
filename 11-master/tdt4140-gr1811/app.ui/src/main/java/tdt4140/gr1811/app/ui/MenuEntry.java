package tdt4140.gr1811.app.ui;

import javafx.scene.Node;

public class MenuEntry {
	
	private final String entryName;
	private final String title;
	private final Node node;
	private final String description;
	
	public MenuEntry(String entryName, String title, String description, Node node) {
		this.entryName = entryName;
		this.title = title;
		this.description = description;
		this.node = node;
	}

	public String getEntryName() {
		return entryName;
	}

	public String getTitle() {
		return title;
	}

	public Node getNode() {
		return node;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return entryName;
	}

}
