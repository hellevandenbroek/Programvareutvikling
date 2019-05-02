package tdt4140.gr1811.web.server.tcp_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.KeyAgreement;
import javax.xml.bind.DatatypeConverter;

import tdt4140.gr1811.app.security.AES;
import tdt4140.gr1811.app.security.AsymmetricKeyHolder;
import tdt4140.gr1811.app.security.DiffieHellman;
import tdt4140.gr1811.web.server.protocol.Protocol;
import tdt4140.gr1811.web.server.protocol.ProtocolFactory;

public class TCPClient implements Runnable {

	private Socket socket;

	public TCPClient(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (Socket cs = socket;
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
				PrintStream out = new PrintStream(socket.getOutputStream(), true, "UTF-8")) {
			System.out.printf("%s connected\n", getReadableSocket(cs));
			String client_pubkey = inFromClient.readLine();
			byte[] secretkey = null;
			
			// DH KEY EXCHANGE
			if(client_pubkey != null) {				
				DiffieHellman dh = new DiffieHellman();
				AsymmetricKeyHolder keyHolder = new AsymmetricKeyHolder();
				byte[] server_publickey = keyHolder.getPublicKey();
				KeyAgreement serverKeyagree = dh.generateKeyAgreement(keyHolder.getKeyPair());

				PublicKey client_publickey = dh.getPublicKey(DatatypeConverter.parseHexBinary(client_pubkey));		
				out.println(DatatypeConverter.printHexBinary(server_publickey));
				
				serverKeyagree.doPhase(client_publickey, true);
				secretkey = serverKeyagree.generateSecret();
			} else {
				System.out.println("Warning client did not give public key. Disconnecting...");
				return;
			}
			
			String client_message = inFromClient.readLine();	
			
			if(client_message != null) {
				
				// DECRYPT MESSAGE FROM CLIENT USING SECRET KEY
				AES aes = new AES(Arrays.copyOfRange(secretkey, 0, 48));
				byte[] plaintext_byte = aes.triple_AES_decrypt(DatatypeConverter.parseHexBinary(client_message));
				String plaintext_msg = new String(plaintext_byte);
				
				
				Protocol protocol = new ProtocolFactory(plaintext_msg).get();
				String response = protocol.handleRequest();
				
				//ENCRYPT RESPONSE TO CLIENT
				byte[] encryted_bytes = aes.triple_AES_encrypt(response.getBytes());
				String encrypted_string = DatatypeConverter.printHexBinary(encryted_bytes);
				if (encrypted_string != null) {
					out.println(encrypted_string);
				}
			}
						
			System.out.printf("Disconnecting %s\n", getReadableSocket(cs));
		} catch (IOException e) {
			// TODO: Parse if connection was closed?
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Globals.CLIENT_THREADS.remove(Thread.currentThread());
		}
	}

	private String getReadableSocket(Socket s) {
		return String.format("%s (%s:%d)", s.getInetAddress().getHostName(), s.getInetAddress().getHostAddress(),
				s.getPort());
	}
}
