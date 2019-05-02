package tdt4140.gr1811.app.ui.table;

import javafx.scene.control.TableCell;

public class TableCellDouble extends TableCell<DataProvider_TableRow, Number> {
	@Override
	protected void updateItem(Number item, boolean empty) {
		super.updateItem(item, empty);
		
		if (item == null || empty) {
			setText(null);
		} else {
			setText(String.format("%.2f", item));
		}
	}
}
