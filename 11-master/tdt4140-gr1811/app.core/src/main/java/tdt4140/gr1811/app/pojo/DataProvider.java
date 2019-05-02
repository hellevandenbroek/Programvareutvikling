package tdt4140.gr1811.app.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;


public class DataProvider {

	private Integer id;
	private String ssn, firstName, lastName;
	private Sex sex;
	private Date birthday;
	private Integer weight, height;
	private String email;
	private String phoneNum;
	private Status status;
	private Date startDate;
	private HashMap<Timestamp, Double> blood_data = new HashMap<>();
	private HashMap<Timestamp, Integer> pulse_data = new HashMap<>();

	@Override
	public String toString() {
		return id + " - " + firstName + " " + lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public HashMap<Timestamp, Double> getBlood_data() {
		return blood_data;
	}

	public HashMap<Timestamp, Integer> getPulse_data() {
		return pulse_data;
	}

}
