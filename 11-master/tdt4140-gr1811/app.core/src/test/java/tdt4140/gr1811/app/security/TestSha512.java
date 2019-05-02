package tdt4140.gr1811.app.security;

import static org.junit.Assert.*;

import java.util.Random;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import tdt4140.gr1811.app.security.SHA_512;

public class TestSha512 {

	@Test
	public void testSHA_512_SecurePassword() throws Exception {
		// Arrange
		String password_plain = "hunter12";
		
		// Act
		String password_hash_salt = SHA_512.SHA_512_SecurePassword(password_plain);
		
		// Assert
		assertNotNull("The hashed password should not be null", password_hash_salt);
		
		String hash = null;
		String salt = null;
		char delimiter = ':';
		int delimiter_index = password_hash_salt.indexOf(delimiter);
		assertFalse("Couldn't find delimiter", delimiter_index == -1);
		hash = password_hash_salt.substring(0, delimiter_index);
		salt = password_hash_salt.substring(delimiter_index + 1);
		delimiter_index = password_hash_salt.indexOf(delimiter, delimiter_index + 1);
		assertTrue("Multiple delimeters found", delimiter_index == -1);
		
		assertTrue("SHA512 hash in hex string must be 128 chars = 64 bytes = 512 bits", hash.length() == 128);
		assertTrue("The salt must be 32 chars = 16 bytes = 128 bits", salt.length() == 32);
	}

	@Test
	public void testSHA_512_SecurePassword_null() throws Exception {
		// Arrange
		String password_plain = null;
		
		// Act
		boolean threwNullPointerException = false;
		String password_hash_salt = null;
		try {
			password_hash_salt = SHA_512.SHA_512_SecurePassword(password_plain);
		} catch (NullPointerException e) {
			threwNullPointerException = true;
		}
		
		// Assert
		assertNull(password_hash_salt);
		assertTrue("Input of null should throw an exception", threwNullPointerException);
	}

	@Test
	public void testVerifyPassword() throws Exception {
		// Arrange
		String password_plain = "hunter12";
		
		// Act
		String password_hash_salt = SHA_512.SHA_512_SecurePassword(password_plain);
		
		// Assert
		assertNotNull(password_hash_salt);
		assertTrue(SHA_512.verifyPassword(password_plain, password_hash_salt));
	}

	@Test
	public void testVerifyPassword_salt() throws Exception {
		// Arrange
		String password_plain_1 = "hunter12";
		String password_plain_2 = "hunter12";
		
		// Act
		String password_hash_salt_1 = SHA_512.SHA_512_SecurePassword(password_plain_1);
		String password_hash_salt_2 = SHA_512.SHA_512_SecurePassword(password_plain_2);
		
		// Assert
		assertNotNull(password_hash_salt_1);
		assertNotNull(password_hash_salt_2);
		assertNotEquals(password_hash_salt_1, password_hash_salt_2);

		assertTrue(SHA_512.verifyPassword(password_plain_1, password_hash_salt_1));
		assertTrue(SHA_512.verifyPassword(password_plain_2, password_hash_salt_2));
	}

	@Test
	public void testVerifyPassword_huge() throws Exception {
		// Arrange
		Random r = new Random();
		byte[] hugePasswordBytes = new byte[1024 * 256];
		r.nextBytes(hugePasswordBytes);
		String hugePasswordPlain = DatatypeConverter.printHexBinary(hugePasswordBytes);
		
		// Act
		String password_hash_salt = SHA_512.SHA_512_SecurePassword(hugePasswordPlain);
		
		// Assert
		assertNotNull(password_hash_salt);
		assertTrue(SHA_512.verifyPassword(hugePasswordPlain, password_hash_salt));
	}
	
	@Test
	public void testInstance() {
		SHA_512 sha512 = new SHA_512();
		blackhole(sha512);
	}

	private void blackhole(Object...objects) {
	}
}
