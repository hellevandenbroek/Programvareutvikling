package tdt4140.gr1811.web.server.tcp_server;

import tdt4140.gr1811.web.server.App;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPServer {

	private int configPort;
	private Integer assignedPort = null;
	private AtomicBoolean disconnected = new AtomicBoolean(false);


	/**
	 * This is the TCP server that will take in messages from the client(from the
	 * app) Messages will go through this server, as is the norm.
	 * 
	 * @param port
	 */
	public TCPServer(int port) {
		this.configPort = port;
	}

	public void start() {
		System.out.println("Starting server at port " + configPort);
		try (ServerSocket ss = new ServerSocket(configPort)) {
			ss.setSoTimeout(400);
			assignedPort = ss.getLocalPort();
			System.out.println("Server is listening for incoming connections at port " + assignedPort);
			System.out.println("Waiting for connection...");
			int count = 0;
			while (!disconnected.get() || !Thread.interrupted()) {
				// Connection socket
				try {
					Socket cs = ss.accept();
					System.out.println("Got a connection with a client.");
					TCPClient client = new TCPClient(cs);
					Thread clientThread = new Thread(client);
					clientThread.setName("client #" + count);
					Globals.CLIENT_THREADS.add(clientThread);
					clientThread.start();
					count++;
				} catch (SocketTimeoutException e) {
					// ignore, check thread interruption status in while-condition
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server closed.");
	}
	
	public void disconnect() {
		this.disconnected.set(true);
	}
	
	public Integer getAssingedPort() {
		return this.assignedPort;
	}


}
