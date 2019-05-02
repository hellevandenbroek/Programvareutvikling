package tdt4140.gr1811.web.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.KeyAgreement;
import javax.xml.bind.DatatypeConverter;

import tdt4140.gr1811.app.security.AES;
import tdt4140.gr1811.app.security.AsymmetricKeyHolder;
import tdt4140.gr1811.app.security.DiffieHellman;;

public class TCPClient {
	private InetAddress hostname;
	private Integer port;
	
	public TCPClient(String hostname, int port) throws IOException {
		this.hostname = InetAddress.getByName(hostname);
		this.port = port;
		
		//TEST CONNECTION
		Socket test_socket = new Socket(hostname, port);
		test_socket.close();
				
	}
	
	public String sendMessage(String input) {
		
		try (Socket server = new Socket(hostname, port)){	
			
			if(!"".equals(input)) {
				PrintWriter out = new PrintWriter(server.getOutputStream(), true);	
				BufferedReader br = new BufferedReader(new InputStreamReader(server.getInputStream(), Charset.forName("UTF-8")));
				
				//DIFFIE HELLMAN KEY-EXCHANGE
				
				//Get public key and keyagreement
				DiffieHellman dh = new DiffieHellman();
				AsymmetricKeyHolder keyHolder = new AsymmetricKeyHolder();
				KeyAgreement keyagree = dh.generateKeyAgreement(keyHolder.getKeyPair());
				
				//encode public key, send to server and receive publickey from server
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
								
				//Encrypt the message with secret key
				AES aes = new AES(Arrays.copyOfRange(secretKey, 0, 48));
				byte[] encryted_bytes = aes.triple_AES_encrypt(input.getBytes());
				String encrypted_string = DatatypeConverter.printHexBinary(encryted_bytes);
					
				//Send encrypted message to server
				out.println(encrypted_string);
				out.flush();
				
				// Decrypt the response from the server
				String output = br.readLine();
				
				// Decrypt response from server
				byte[] decrypted_bytes = aes.triple_AES_decrypt(DatatypeConverter.parseHexBinary(output));
				output = new String(decrypted_bytes);
				
				return output;
			}		
		} catch(IOException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;		
	}
	
	public InetAddress getHostname() {
		return hostname;
	}

	public Integer getPort() {
		return port;
	}
	
}
