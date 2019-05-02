package tdt4140.gr1811.app.security;

import static org.junit.Assert.*;

import java.security.*;
import java.util.Arrays;

import javax.crypto.*;

import org.junit.Test;

public class TestDH {
	
	
	
	
	// TEST A KEY EXCHANGE BETWEEN ALICE AND BOB
	@Test
	public void testDiffieHellman() throws Exception{
		
		//Create DH object
		DiffieHellman dh = new DiffieHellman();
		
		// Alice generates DH key pair
		KeyPair aliceKeyPair = dh.generateKeyPair();
		//Alice creates and initializes KeyAgreement
		KeyAgreement aliceKeyAgree = dh.generateKeyAgreement(aliceKeyPair);
		//Alice encodes her publickey and sends it to Bob
		byte[] alicePubKeyEncoded = aliceKeyPair.getPublic().getEncoded();
		
		//Bob instantiates a DH public key from the recived encoded public key
		PublicKey alicePubKey = dh.getPublicKey(alicePubKeyEncoded);
		//Bob creates his own DH key pair
		KeyPair bobKeyPair = dh.generateKeyPair();
		//Bob creates and initializes KeyAgreement
		KeyAgreement bobKeyAgree = dh.generateKeyAgreement(bobKeyPair);
		//Bob encodes his public key and sends it to Alice
		byte[] bobPubKeyEncoded = bobKeyPair.getPublic().getEncoded();
		
		//Alice instantiates a DH public key from the recived encoded public key
		PublicKey bobPublicKey = dh.getPublicKey(bobPubKeyEncoded);
		//Alice uses Bob public key to generate secretkey
		aliceKeyAgree.doPhase(bobPublicKey, true);
		byte[] aliceSecretKey = aliceKeyAgree.generateSecret();
		
		//Bob uses Alice's public key to genreate secretkey
		bobKeyAgree.doPhase(alicePubKey, true);
		byte[] bobSecretKey = bobKeyAgree.generateSecret();
		
		// ASSERT TRUE IF THE GENERATED SECRETS ARE THE SAME
		assertTrue("The two generated secrets should be the same", Arrays.equals(aliceSecretKey, bobSecretKey));
		
	}
	
	@Test
	public void testInstance() {
		DiffieHellman dh = new DiffieHellman();
		blackhole(dh);
	}

	private void blackhole(Object...objects) {
	}

}
