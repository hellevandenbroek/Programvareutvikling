package tdt4140.gr1811.web.server;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyAgreement;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.db.QueryImporter;
import tdt4140.gr1811.app.pojo.Status;
import tdt4140.gr1811.app.security.AES;
import tdt4140.gr1811.app.security.AsymmetricKeyHolder;
import tdt4140.gr1811.app.security.DiffieHellman;
import tdt4140.gr1811.web.server.App;
import tdt4140.gr1811.web.server.json_pojo.client2server.ChangeStatus;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteData;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteUser;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertBloodsugarData;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertPulseData;

public class InsertDeleteDataIT {

	static App a = null;
	private static DataSource ds;
	private static Credentials oldCredentials = null;
	private static ChangeStatus MainStatus;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		a = new App();
		a.initializeServer();
		Thread s = new Thread(new Runnable() {
			@Override
			public void run() {
				a.startServer();
			}
		});
		s.setDaemon(true);
		s.start();
		
		
		Credentials c = new Credentials("mysql-it", null, "IntegrationTest", "root", "abcd");
		ds = new DataSourceBuilder(c).allowMultiQueries().build();
		oldCredentials = CredentialsFactory.saveCredentials(c);

		try (Connection conn = ds.getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_schema.sql"));
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_example_values.sql"));
			}
		}
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (a != null) {
			a.disconnectServer();
		}
		
		try (Connection conn = ds.getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_schema_drop.sql"));
			}
		}
		CredentialsFactory.saveCredentials(oldCredentials);
		
	}

	@Test(timeout = 5000) 
	public void testInsertBloodsugarData() throws Exception {
		try (Socket soc = new Socket("localhost", a.getServerPort())) {
			Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Map<Timestamp, Double> data = new HashMap<>();
			InsertBloodsugarData bloodData = new InsertBloodsugarData(1, data);
			Date date = new Date();
			Timestamp t = new Timestamp(date.getTime());
			Double level = 12.0;
			data.put(t, level);

			String response = server_communication(g.toJson(bloodData), soc);
			assertTrue(response.contains("INSERT_ACK"));
		}
	}

	@Test(timeout = 5000) 
	public void testInsertPulseData() throws Exception {
		try (Socket soc = new Socket("localhost", a.getServerPort())) {
			Gson g = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			Map<Timestamp, Integer> data = new HashMap<>();
			InsertPulseData pulseData = new InsertPulseData(1, data);
			Date date = new Date();
			Timestamp t = new Timestamp(date.getTime());
			Integer BPM = 1250;
			data.put(t, BPM);
			String response = server_communication(g.toJson(pulseData), soc);
			assertTrue(response.contains("INSERT_ACK"));
		}
	}

	@Test(timeout = 5000) 
	public void testDelete() throws Exception {
		try (Socket soc = new Socket("localhost", a.getServerPort())) {

			Gson g = new Gson();
			DeleteData delete = new DeleteData(2);

			String response = server_communication(g.toJson(delete), soc);
			assertTrue(response.contains("DELETION_ACK"));
		}
	}
	
	@Test(timeout = 5000) 
	public void testDeleteUser() throws Exception {
		try (Socket soc = new Socket("localhost", a.getServerPort())) {
			Gson g = new Gson();
			DeleteUser deleteUser = new DeleteUser(6,false);
			String response = server_communication(g.toJson(deleteUser), soc);
			assertTrue(response.contains("USER_DELETION_ACK"));
		} 
	}

	////Tests the status and reactivation/deactivation of DataProviders
	@Test
	public void testSetInactive() throws IOException, GeneralSecurityException {
		try (Socket soc = new Socket("localhost", a.getServerPort()); Socket soc2 = new Socket("localhost", a.getServerPort())) {
			
			Gson g = new Gson();
			Gson gTest = new Gson();

			///Makes sure that the status is set to active before trying to make it inactive
			ChangeStatus prestatus = new ChangeStatus(1, Status.ACTIVE);
			String preVerify = server_communication(gTest.toJson(prestatus), soc);
			//////////////////
			
			ChangeStatus ChangeStatus = new ChangeStatus(1, Status.INACTIVE);
			String response = server_communication(g.toJson(ChangeStatus), soc2);
			System.out.println(response);
			assertTrue(response.contains("Success, user deactivated"));
			
		}
	}
	
	@Test
	public void testSetActive() throws IOException, GeneralSecurityException {
		try (Socket soc = new Socket("localhost", a.getServerPort()); Socket soc2 = new Socket("localhost", a.getServerPort())) {
			
			Gson g = new Gson();
			Gson gTest = new Gson();
			
			///Makes sure that the status is set to INACTIVE before trying to make it active
			ChangeStatus prestatus = new ChangeStatus(2, Status.INACTIVE);
			String preVerify = server_communication(gTest.toJson(prestatus), soc);
			//////////////////
			System.out.println(preVerify);
			ChangeStatus ChangeStatus = new ChangeStatus(2, Status.ACTIVE);
			String response = server_communication(g.toJson(ChangeStatus), soc2);
			System.out.println(response);
			assertTrue(response.contains("Success, activated user"));
		}
	}
	
	@Test
	public void testNoChange() throws UnsupportedEncodingException, IOException, GeneralSecurityException {
		try (Socket soc = new Socket("localhost", a.getServerPort()); Socket soc2 = new Socket("localhost", a.getServerPort());) {
			
			Gson g = new Gson();
			Gson gTest = new Gson();
			
			///Makes sure that the status is set to ACTIVE before trying to make it Activa
			ChangeStatus prestatus = new ChangeStatus(1, Status.ACTIVE);
			String preVerify = server_communication(gTest.toJson(prestatus), soc);
			//////////////////
			
			ChangeStatus ChangeStatus = new ChangeStatus(1, Status.ACTIVE);
			String response = server_communication(g.toJson(ChangeStatus), soc2);
			assertTrue(response.contains("No need for change"));
		}
	}
	
	//denne testen sjekker om brukeren blir aktivert igjen av å oppdatere data
	@Test
	public void testReactivated() throws UnsupportedEncodingException, IOException, GeneralSecurityException {
		try (Socket soc = new Socket("localhost", a.getServerPort()); Socket soc2 = new Socket("localhost", a.getServerPort());Socket soc3 = new Socket("localhost", a.getServerPort());) {
			
			Gson g = new Gson();
			Gson gTest = new Gson();
			
			///Second Gson for the insertion data
			Gson dataG = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			///////////
			
			//Sets the status to Inactive or makes sure it is currently inactive
			ChangeStatus ChangeStatus = new ChangeStatus(2, Status.INACTIVE);
			String preVerify = server_communication(gTest.toJson(ChangeStatus), soc);
			//Runs a test that will introduce new data thus reactivating the dataprovider
			
			Map<Timestamp, Integer> data = new HashMap<>();
			InsertPulseData pulseData = new InsertPulseData(2, data);
			Date date = new Date();
			Timestamp t = new Timestamp(date.getTime());
			Integer BPM = 1250;
			data.put(t, BPM);
			String neededTest = server_communication(dataG.toJson(pulseData), soc2);
			
			//////////////////////
			ChangeStatus VerifyStatus = new ChangeStatus(2, Status.ACTIVE);
			String VerifyString = server_communication(g.toJson(VerifyStatus), soc3);
			/////Verifies that The dataProvider has been set to Active, by checking if there is no Change needed

			assertTrue(VerifyString.contains("No need for change"));
		}
	}





	private String server_communication(String input, Socket socket)
			throws GeneralSecurityException, IOException {
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream out = new PrintStream(socket.getOutputStream(), true, "UTF-8")) {
			// DIFFIE HELLMAN KEY-EXCHANGE

			// Get public key and keyagreement
			DiffieHellman dh = new DiffieHellman();
			AsymmetricKeyHolder keyHolder = new AsymmetricKeyHolder();
			KeyAgreement keyagree = dh.generateKeyAgreement(keyHolder.getKeyPair());

			// encode public key, send to server and receive publickey from server
			byte[] client_publickey = keyHolder.getPublicKey();
			String client_pubkey_hex = DatatypeConverter.printHexBinary(client_publickey);
			out.println(client_pubkey_hex);

			// read the public key from the server
			String server_publickey_string = br.readLine();
			PublicKey server_publickey = dh.getPublicKey(DatatypeConverter.parseHexBinary(server_publickey_string));

			// get the secret key from the servers public key
			keyagree.doPhase(server_publickey, true);
			byte[] secretKey = keyagree.generateSecret();

			// ENCRYPT THE MESSAGE WITH AES AND THE SECRET KEY

			// Encrypt the message with secret key
			AES aes = new AES(Arrays.copyOfRange(secretKey, 0, 48));
			byte[] encryted_bytes = aes.triple_AES_encrypt(input.getBytes());
			String encrypted_string = DatatypeConverter.printHexBinary(encryted_bytes);

			// Send encrypted message to server
			out.println(encrypted_string);
			//out.flush();

			// Decrypt the response from the server
			String output = br.readLine();

			// Decrypt response from server
			byte[] decrypted_bytes = aes.triple_AES_decrypt(DatatypeConverter.parseHexBinary(output));
			output = new String(decrypted_bytes);
			
			return output;
		}
		
		
	}

}
