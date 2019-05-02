package tdt4140.gr1811.app.security;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;


public class DiffieHellman {
	
	
	//Create DH key pair with 2048-bit key size
	public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keypairgen = KeyPairGenerator.getInstance("DH"); 
		keypairgen.initialize(512);
		KeyPair keypair = keypairgen.generateKeyPair();
		return keypair; 		
	}
	
	// Create and initialize DH KeyAgreement object
	public KeyAgreement generateKeyAgreement(KeyPair keypair) {
		KeyAgreement keyagree = null;
		try {
			keyagree = KeyAgreement.getInstance("DH");
			keyagree.init(keypair.getPrivate());	
			return keyagree;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keyagree;
				
	}
	
	//Instantiate a DH public key from the received encoded key material
	public PublicKey getPublicKey(byte[] receivedKey) {	
		try {
			KeyFactory keyfac = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(receivedKey);
			PublicKey publickey = keyfac.generatePublic(x509KeySpec); 
			return publickey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}
}
