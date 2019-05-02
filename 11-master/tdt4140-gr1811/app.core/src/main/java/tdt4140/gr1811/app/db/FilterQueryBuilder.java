package tdt4140.gr1811.app.db;

import java.time.Year;
import java.util.regex.Pattern;

import tdt4140.gr1811.app.pojo.FilterAttributes;
import tdt4140.gr1811.app.pojo.Sex;

public class FilterQueryBuilder {

	public String queryBuilder(FilterAttributes fa) {

		Integer minHeight = fa.getMinHeight();
		Integer maxHeight = fa.getMaxHeight();
		Integer minWeight = fa.getMinWeight();
		Integer maxWeight = fa.getMaxWeight();
		Integer minAge = fa.getMinAge();
		Integer maxAge = fa.getMaxAge();
		Sex sex = fa.getSex();

		///// Builds the Query
		String query = SelectBuilder() + " " + FromBuilder() + " "
				+ WhereBuilder(minHeight, maxHeight, minWeight, maxWeight, minAge, maxAge, sex) + " "
				+ OrderByBuilder();
		return query;

	}

	///// Creates The SELECT statement
	private String SelectBuilder() {
		String SelectString = "SELECT DataproviderID";
		return SelectString;
	}

	///// Creates the FROM statement
	private String FromBuilder() {
		String FromString = "FROM DataProvider";
		return FromString;
	}

	///// Creates the WHERE statements
	private String WhereBuilder(Integer minHøyde, Integer maxHøyde, Integer minVekt, Integer maxVekt, Integer minAlder,
			Integer maxAlder, Sex kjønn) {

		/// Starts the STatement
		String WhereStatements = "WHERE";

		/// Keeps a counter in case of need to edit excess AND statments
		int counter = 0;

		if (minHøyde != null) {
			WhereStatements = WhereStatements + " AND Height >= " + minHøyde;
			counter++;
		}

		if (maxHøyde != null) {
			WhereStatements = WhereStatements + " AND Height <= " + maxHøyde;
			counter++;
		}

		if (minVekt != null) {
			WhereStatements = WhereStatements + " AND Weight >= " + minVekt;
			counter++;
		}

		if (maxVekt != null) {
			WhereStatements = WhereStatements + " AND Weight <= " + maxVekt;
			counter++;
		}

		if (minAlder != null) {
			int year = Year.now().getValue();
			Integer ageString = year - minAlder;
			String SQL_Age = ageString.toString() + "-01-01";

			WhereStatements = WhereStatements + " AND BirthDate >= " + SQL_Age;
			counter++;
		}

		if (maxAlder != null) {
			int year = Year.now().getValue();
			Integer ageString = year - maxAlder;
			String SQL_Age = ageString.toString() + "-01-01";

			WhereStatements = WhereStatements + " AND BirthDate >= " + SQL_Age;
			counter++;
		}

		/// Checks the gender
		if (kjønn != null) {
			if (kjønn == Sex.M) {
				WhereStatements = WhereStatements + " AND Sex = 'M'";
				counter++;
			}
			if (kjønn == Sex.F) {
				WhereStatements = WhereStatements + " AND Sex = 'F'";
				counter++;
			}
			if (kjønn == Sex.O) {
				WhereStatements = WhereStatements + " AND Sex = 'O'";
				counter++;
			}
			counter++;
		}

		/// Checks if there is a need to edit the results for an unneccasary AND
		/// statment
		if (counter == 0) {
			return "";
		} else {
			String result = WhereStatements.replaceFirst(Pattern.quote("AND"), "");
			return result;
		}
	}

	private String OrderByBuilder() {
		String OrderByString = "ORDER BY DataproviderID";
		return OrderByString;
	}
}
