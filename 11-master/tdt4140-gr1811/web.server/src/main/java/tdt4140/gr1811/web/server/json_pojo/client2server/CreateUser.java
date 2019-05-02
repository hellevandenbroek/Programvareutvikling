package tdt4140.gr1811.web.server.json_pojo.client2server;

import java.sql.Date;

import tdt4140.gr1811.app.pojo.Sex;

public class CreateUser {
	
	protected ClientMsgType msgType;
	private String ssn;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private Sex sex;
	private Double weight;
	private Double height;
	private String email;
	private String phoneNum;
	
	public CreateUser(String ssn, String firstName, String lastName, Date birthDate, Sex sex, Double weight, Double height, String email, String phoneNum) {
		this.msgType = ClientMsgType.CREATE_USER;
		this.ssn = ssn;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.weight = weight;
		this.height = height;
		this.email = email;
		this.phoneNum = phoneNum;
	}

	public final String getSsn() {
		return ssn;
	}

	public final void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public final String getLastName() {
		return lastName;
	}

	public final void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public final Date getBirthDate() {
		return birthDate;
	}

	public final void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public final Sex getSex() {
		return sex;
	}

	public final void setSex(Sex sex) {
		this.sex = sex;
	}

	public final Double getWeight() {
		return weight;
	}

	public final void setWeight(Double weight) {
		this.weight = weight;
	}

	public final Double getHeight() {
		return height;
	}

	public final void setHeight(Double height) {
		this.height = height;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}

	public final String getPhoneNum() {
		return phoneNum;
	}

	public final void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	 
}
