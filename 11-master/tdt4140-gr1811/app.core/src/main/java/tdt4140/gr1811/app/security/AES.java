package tdt4140.gr1811.app.security;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	private byte[] key_1;
	private byte[] key_2;
	private byte[] key_3;
	
	public AES(byte[] key) {
		if(key.length != 48) {
			throw new IllegalArgumentException("Key length must be 48 bytes for triple 16 bytes AES");
		}
		this.key_1 = Arrays.copyOfRange(key, 0, 16);
		this.key_2 = Arrays.copyOfRange(key, 16, 32);
		this.key_3 = Arrays.copyOfRange(key, 32, 48);
	}
	
	public byte[] triple_AES_encrypt(byte[] plaintext) {
		byte[] ciphertext = encrypt(plaintext, key_1);
		ciphertext = encrypt(ciphertext, key_2);
		ciphertext = encrypt(ciphertext, key_3);
		return ciphertext;
	}
	
	public byte[] triple_AES_decrypt(byte[] ciphertext) {
		byte[] plaintext = decrypt(ciphertext, key_3);
		plaintext = decrypt(plaintext, key_2);
		plaintext = decrypt(plaintext, key_1);
		return plaintext;
	}
		
	private byte[] encrypt(byte[] plaintext, byte[] key) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(plaintext);
		}catch (GeneralSecurityException e) {
			e.printStackTrace();
		} 

		return null;
	}
	
	private byte[] decrypt(byte[] ciphertext, byte[] key) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(ciphertext);
		}catch (GeneralSecurityException e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		return null;
		
	}
}
