package tdt4140.gr1811.app.ui.table;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataProvider_TableRow {
	private StringProperty fname = new SimpleStringProperty();
	private StringProperty ename = new SimpleStringProperty();
	private StringProperty pnr = new SimpleStringProperty();
	private StringProperty date = new SimpleStringProperty();
	private StringProperty sex = new SimpleStringProperty();
	private StringProperty status = new SimpleStringProperty();
	private IntegerProperty height = new SimpleIntegerProperty();
	private IntegerProperty weight = new SimpleIntegerProperty();

	// fornavn
	public final StringProperty FnameProperty() {
		return this.fname;
	}

	public final String getFname() {
		return this.FnameProperty().get();
	}

	public final void setFname(final String fname) {
		this.FnameProperty().set(fname);
	}

	// etternavn
	public final StringProperty EnameProperty() {
		return this.ename;
	}

	public final String getEname() {
		return this.EnameProperty().get();
	}

	public final void setEname(final String fname) {
		this.EnameProperty().set(fname);
	}

	// Personnummer
	public final StringProperty PnrProperty() {
		return this.pnr;
	}

	public final String getPnr() {
		return this.PnrProperty().get();
	}

	public final void setPnr(final String pnr) {
		this.PnrProperty().set(pnr);
	}

	// Fødselsdato
	public final StringProperty DateProperty() {
		return this.date;
	}

	public final String getDate() {
		return this.DateProperty().get();
	}

	public final void setDate(final String timestamp) {
		this.DateProperty().set(timestamp);
	}

	// Kjønn
	public final StringProperty SexProperty() {
		return this.sex;
	}

	public final String getSex() {
		return this.SexProperty().get();
	}

	public final void setSex(final String sex) {
		if (sex.equals("M")) {
			this.SexProperty().set("Mann");
		} else if (sex.equals("F")) {
			this.SexProperty().set("Kvinne");
		} else {
			this.SexProperty().set("Annet");
		}
	}

	// Høyde
	public final IntegerProperty HeightProperty() {
		return this.height;
	}

	public final Integer getHeight() {
		return this.HeightProperty().get();
	}

	public final void setHeight(final Integer height) {
		this.HeightProperty().set(height);
	}

	// Vekt
	public final IntegerProperty WeightProperty() {
		return this.weight;
	}

	public final Integer getWeight() {
		return this.WeightProperty().get();
	}

	public final void setWeight(final Integer weight) {
		this.WeightProperty().set(weight);
	}

	// Status
	public final StringProperty StatusProperty() {
		return this.status;
	}

	public final String getStatus() {
		return this.StatusProperty().get();
	}

	public final void setStatus(final String status) {
		this.StatusProperty().set(status);
	}

}
