package tdt4140.gr1811.app.testfx_utils;

import static org.testfx.matcher.base.GeneralMatchers.baseMatcher;

import java.time.LocalDate;
import java.util.Objects;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;

public class DatePickerMatcher {

	/**
     * Creates a matcher that matches all {@link javafx.scene.control.DatePicker} objects
     * that have the given {@code string}.
     */
	@Factory
	public static Matcher<Node> hasText(String string) {
		String descriptionText = "Node has text \"" + string + "\"";
		return baseMatcher(descriptionText, node -> {
			if (node instanceof DatePicker) {
				DatePicker dp = ((DatePicker) node);
				LocalDate ld = dp.getValue();
				return Objects.equals(string, dp.getConverter().toString(ld));
			}
			return false;
		});
	}

}
