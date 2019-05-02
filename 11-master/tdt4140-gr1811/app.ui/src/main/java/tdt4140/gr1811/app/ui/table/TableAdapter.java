package tdt4140.gr1811.app.ui.table;

import java.text.SimpleDateFormat;

import tdt4140.gr1811.app.pojo.DataProvider;

public class TableAdapter {
	public static DataProvider_TableRow convert(DataProvider a) {
		DataProvider_TableRow b = new DataProvider_TableRow();
		b.setFname(a.getFirstName());
		b.setEname(a.getLastName());
		b.setDate(new SimpleDateFormat("yyyy-MM-dd").format(a.getBirthday()));
		b.setPnr(a.getSsn());
		b.setSex(a.getSex().toString());
		b.setWeight(a.getWeight());
		b.setHeight(a.getHeight());
		switch(a.getStatus()) {
		case ACTIVE:
			b.setStatus("Aktiv");
			break;
		case INACTIVE:
			b.setStatus("Inaktiv");
			break;
		case DEACTIVATED:
			b.setStatus("Deaktivert");
			break;
		default:
			b.setStatus(null);
			break;
		}
		return b;
	}
}
