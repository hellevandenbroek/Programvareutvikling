package tdt4140.gr1811.app.db;

import static org.junit.Assert.*;

import org.junit.Test;

import tdt4140.gr1811.app.pojo.FilterAttributes;
import tdt4140.gr1811.app.pojo.Sex;

public class FilterQueryBuilderTest {

	@Test
	public void testAllNulls() {
		FilterAttributes fa = new FilterAttributes();
		FilterQueryBuilder builder = new FilterQueryBuilder();
		String query = builder.queryBuilder(fa);

		assertNotNull(query);
		assertTrue(query.toLowerCase().contains("select"));
		assertTrue(query.toLowerCase().contains("from"));
	}

	@Test
	public void testAllFiltersAvailable() {
		FilterAttributes fa = new FilterAttributes();
		fa.setMaxAge(100);
		fa.setMinAge(18);
		fa.setMaxHeight(200);
		fa.setMinHeight(120);
		fa.setMaxWeight(120);
		fa.setMinWeight(10);
		fa.setSex(Sex.M);
		FilterQueryBuilder builder = new FilterQueryBuilder();
		String query = builder.queryBuilder(fa);
		String queryWithNull = builder.queryBuilder(new FilterAttributes());

		assertNotNull(query);
		assertTrue(query.toLowerCase().contains("select"));
		assertTrue(query.toLowerCase().contains("from"));
		assertNotNull(queryWithNull);

		assertTrue("The filtered request should be larger than the unfiltered one",
				query.length() > queryWithNull.length());
	}

	@Test
	public void testSexMatters() {
		FilterAttributes male = new FilterAttributes();
		male.setSex(Sex.M);
		FilterAttributes female = new FilterAttributes();
		female.setSex(Sex.F);
		FilterAttributes other = new FilterAttributes();
		other.setSex(Sex.O);
		FilterQueryBuilder builder = new FilterQueryBuilder();
		String findMales = builder.queryBuilder(male);
		String findFemales = builder.queryBuilder(female);
		String findOthers = builder.queryBuilder(other);

		assertNotNull(findMales);
		assertNotNull(findFemales);
		assertNotNull(findOthers);
		assertNotEquals(findFemales, findMales);
		assertNotEquals(findFemales, findOthers);
		assertNotEquals(findOthers, findMales);
	}

}
