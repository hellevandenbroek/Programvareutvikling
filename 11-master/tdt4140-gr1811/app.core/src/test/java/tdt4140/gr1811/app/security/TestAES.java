package tdt4140.gr1811.app.security;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestAES {
	
	// TEST ENCRYPTING AND DECRYPTING USING TRIPLE AES
	@Test
	public void testAES() throws Exception {
		
		//Arrange
		String secretMessage = "TDT4140 GRUPPE11";
		//Convert string to bytes
		byte[] secretBytes = secretMessage.getBytes();	
		
		// Use DH to generate a simple key, and cut to size
		byte[] key = Arrays.copyOfRange(new DiffieHellman().generateKeyPair().getPublic().getEncoded(), 0, 48);
		
		//Initialize AES with key
		AES aes = new AES(key);
		
		//Encrypt the message
		byte[] ciphertext = aes.triple_AES_encrypt(secretBytes);
		
		//Decrypt the message
		byte[] plaintext = aes.triple_AES_decrypt(ciphertext);
		
		//Convert back to string
		String decrypted_msg = new String(plaintext);
		
		//Check that the messages are the same
		assertTrue("The two strings should be the same",secretMessage.equals(decrypted_msg));	
	}
	
	//Test use of wrong key size
	@Test
	public void testWrongKeySize() throws Exception{
		// Use DH to generate a simple key, and cut it too small
		byte[] key = Arrays.copyOfRange(new DiffieHellman().generateKeyPair().getPublic().getEncoded(), 0, 16);
		
		//Initialize AES with a small key
		boolean threwIllegalArgumentException = false;
		AES aes = null;
		try {
			aes = new AES(key);
		}catch (IllegalArgumentException e) {
			threwIllegalArgumentException = true;
		}
		
		assertNull(aes);
		assertTrue("Using a wrong key size should throw IllegalArgumentException",threwIllegalArgumentException);
	}
	
	@Test
	public void testInstance() throws Exception{
		AES aes = new AES(Arrays.copyOfRange(new DiffieHellman().generateKeyPair().getPublic().getEncoded(), 0, 48));
		blackhole(aes);
	}

	private void blackhole(Object...objects) {
	}
	
	
}
