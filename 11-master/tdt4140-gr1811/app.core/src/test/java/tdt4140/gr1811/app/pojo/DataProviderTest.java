package tdt4140.gr1811.app.pojo;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;

import org.junit.Test;

public class DataProviderTest {

	@Test
	public void test_setters_getters() {
		// Arrange
		Integer id = 123;
		Integer weight = 256;
		Integer height = 27;
		String ssn = "123456";
		String firstName = "Troll";
		String lastName = "Hunter";
		Sex sex = Sex.M;
		Date birthday = new Date(12345);
		HashMap<Timestamp, Double> blood = new HashMap<>();
		blood.put(new Timestamp(5), new Double(0));
		HashMap<Timestamp, Integer> pulse = new HashMap<>();
		pulse.put(new Timestamp(3), 5);
		String email = "test@example.com";
		String phonenr = "12345678";
		Status status = Status.ACTIVE;
		Date startDate = new Date(123456);
		
		DataProvider dp = new DataProvider();

		// Act
		dp.setId(id);
		dp.setFirstName(firstName);
		dp.setLastName(lastName);
		dp.setSsn(ssn);
		dp.setHeight(height);
		dp.setWeight(weight);
		dp.setSex(sex);
		dp.setBirthday(birthday);
		dp.getBlood_data().putAll(blood);
		dp.getPulse_data().putAll(pulse);
		dp.setEmail(email);
		dp.setPhoneNum(phonenr);
		dp.setStatus(status);
		dp.setStartDate(startDate);

		// Assert
		assertEquals(dp.getId(), id);
		assertEquals(dp.getFirstName(), firstName);
		assertEquals(dp.getLastName(), lastName);
		assertEquals(dp.getSsn(), ssn);
		assertEquals(dp.getHeight(), height);
		assertEquals(dp.getWeight(), weight);
		assertEquals(dp.getSex(), sex);
		assertEquals(dp.getBirthday(), birthday);
		assertEquals(blood, dp.getBlood_data());
		assertEquals(pulse, dp.getPulse_data());
		assertEquals(email, dp.getEmail());
		assertEquals(phonenr, dp.getPhoneNum());
		assertEquals(status, dp.getStatus());
		assertEquals(startDate, dp.getStartDate());
	}

	@Test
	public void test_toString() {
		String fName = "Per";
		String lName = "Hanssen";
		Integer id = 5;
		
		DataProvider dp1 = new DataProvider();
		DataProvider dp2 = new DataProvider();
		dp1.setId(id);
		dp1.setFirstName(fName);
		dp1.setLastName(lName);
		dp2.setId(id);
		dp2.setFirstName(fName);
		dp2.setLastName(lName);
		
		assertEquals(dp1.toString(), dp2.toString());
	}
	
	@Test
	public void testGetSetPulse_data() {
		HashMap<Timestamp, Integer> map = new HashMap<>();
		map.put(new Timestamp(5), 5);
		map.put(new Timestamp(20), 2);

		DataProvider dpp = new DataProvider();
		dpp.getPulse_data().putAll(map);

		assertEquals(map, dpp.getPulse_data());
	}
	
	@Test
	public void testGetSetBlood_data() {
		HashMap<Timestamp, Double> map = new HashMap<>();
		map.put(new Timestamp(5), new Double(2.2f));
		map.put(new Timestamp(20), new Double(1.2f));
		
		DataProvider dpb = new DataProvider();
		dpb.getBlood_data().putAll(map);
		
		assertEquals(map, dpb.getBlood_data());
	}
}
