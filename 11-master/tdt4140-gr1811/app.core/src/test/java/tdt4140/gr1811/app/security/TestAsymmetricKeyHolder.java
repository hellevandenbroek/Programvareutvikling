package tdt4140.gr1811.app.security;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestAsymmetricKeyHolder {

	@Test
	public void testInstancing() throws Exception {
		AsymmetricKeyHolder holder = new AsymmetricKeyHolder();
		assertNotNull(holder);
	}

	@Test
	public void testGettingGeneratedPair() throws Exception {
		AsymmetricKeyHolder holder = new AsymmetricKeyHolder();

		assertNotNull(holder.getKeyPair());
		assertNotNull(holder.getPrivateKey());
		assertNotNull(holder.getPublicKey());
	}
}
