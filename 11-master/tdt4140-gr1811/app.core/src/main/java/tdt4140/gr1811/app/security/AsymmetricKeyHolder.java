package tdt4140.gr1811.app.security;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class AsymmetricKeyHolder {
	
	private KeyPair keyPair;
	
	public AsymmetricKeyHolder() throws NoSuchAlgorithmException {
		this.keyPair = new DiffieHellman().generateKeyPair();
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public byte[] getPublicKey() {
		return getKeyPair().getPublic().getEncoded();
	}

	public byte[] getPrivateKey() {
		return getKeyPair().getPrivate().getEncoded();
	}
	
	
	
}
