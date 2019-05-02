package tdt4140.gr1811.app.pojo;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilterAttributesTest {

	@Test
	public void testSettersGetters() {
		FilterAttributes fa = new FilterAttributes();
		
		Integer maxAge = 5;
		Integer minAge = 2;
		Integer maxHeight = 5;
		Integer minHeight = 1;
		Integer maxWeight = 20;
		Integer minWeight = 5;
		Sex sex = Sex.M;
		
		fa.setMaxAge(maxAge);
		fa.setMinAge(minAge);
		fa.setMaxHeight(maxHeight);
		fa.setMinHeight(minHeight);
		fa.setMaxWeight(maxWeight);
		fa.setMinWeight(minWeight);
		fa.setSex(sex);
		
		assertEquals(maxAge, fa.getMaxAge());
		assertEquals(minAge, fa.getMinAge());
		assertEquals(maxHeight, fa.getMaxHeight());
		assertEquals(minHeight, fa.getMinHeight());
		assertEquals(maxWeight, fa.getMaxWeight());
		assertEquals(minWeight, fa.getMinWeight());
		assertEquals(sex, fa.getSex());
	}

}
