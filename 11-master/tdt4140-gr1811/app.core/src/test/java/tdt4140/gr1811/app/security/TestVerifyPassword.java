package tdt4140.gr1811.app.security;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestVerifyPassword {
	
	@Test
	public void testFailure() {
		assertFalse(VerifyPassword.verifyPassword("", ""));
	}

}
