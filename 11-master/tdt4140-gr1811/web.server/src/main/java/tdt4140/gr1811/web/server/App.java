package tdt4140.gr1811.web.server;

import org.apache.commons.cli.*;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.web.server.scheduler.DeletedDataSchedule;
import tdt4140.gr1811.web.server.tcp_server.Globals;
import tdt4140.gr1811.web.server.tcp_server.TCPServer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

	public static final int DEFAULT_PORT = 64672;
	private TCPServer server = null;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


	public static void main(String[] args) {
		int port = -1;
		System.out.println("For usage information, run");
		System.out.println("\tjava -jar <jarfile> -h");

		// Arguments options
		Options options = new Options();

		options.addOption("h", "help", false, "print usage message");
		options.addOption("p", "port", true,
				"which port to run on, 0 for auto, leave out for default port " + DEFAULT_PORT);
		options.addOption("c", "credentials", true, "point to custom database credentials file");

		// Parse arguments
		CommandLineParser cmdParser = new DefaultParser();
		try {
			CommandLine cmd = cmdParser.parse(options, args);
			if (cmd.hasOption('h')) {
				printUsage(options);
				return;
			}

			if (cmd.hasOption('p')) {
				String userPort = cmd.getOptionValue('p');
				try {
					port = Integer.valueOf(userPort);
					System.out.printf("Using custom port %d...\n", port);
				} catch (NumberFormatException e) {
					System.err.println("Error, port must be an integer value");
					System.err.println("was " + userPort);
				}
			} else {
				port = DEFAULT_PORT;
			}

			if (cmd.hasOption('c')) {
				String path = cmd.getOptionValue('c');
				String errorMsg = validateCredentialsExists(path);
				if (errorMsg != null) {
					System.err.println("Credentials validation error:");
					System.err.println(errorMsg);
					return;
				}
				Path credentialsPath = getCredentialsPath(path);
				System.out.printf("Using custom database credentials file:\n\t%s\n", credentialsPath);
				CredentialsFactory.setCredentialsFile(credentialsPath.toFile());
			}
		} catch (ParseException e) {
			System.err.println("Invalid launch parameter:");
			System.err.println(e.getMessage());
			return;
		}

		App app = new App();
		app.startScheduler();
		app.initializeServer(port);
		app.startServer();

		// Shut down any client threads
		for (Iterator<Thread> iterator = Globals.CLIENT_THREADS.iterator(); iterator.hasNext();) {
			Thread thread = iterator.next();
			thread.interrupt();
		}
		long patienceInMs = 1000;
		long startWait = System.currentTimeMillis();
		try {
			for (Iterator<Thread> iterator = Globals.CLIENT_THREADS.iterator(); iterator.hasNext();) {
				Thread thread = iterator.next();
				long timeToWait = Math.max(1, startWait + patienceInMs - System.currentTimeMillis());
				thread.join(timeToWait);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initializeServer(int port) {
		server = new TCPServer(port);
	}

	public void initializeServer() {
		server = new TCPServer(DEFAULT_PORT);
	}

	public void startServer() {
		server.start();
	}

	public void disconnectServer() {
		server.disconnect();
	}

	public Integer getServerPort() {
		if (server == null) {
			return null;
		}
		return server.getAssingedPort();
	}

	private static void printUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar <jarfile> [OPTIONS]", options);
	}

	private static Path getCredentialsPath(String path) {
		Path pathPath = Paths.get(path);
		if (!pathPath.isAbsolute()) {
			Path userDir = Paths.get(System.getProperty("user.dir"));
			pathPath = userDir.resolve(path);
		}
		return pathPath;
	}

	private static String validateCredentialsExists(String path) {
		Path pathPath = getCredentialsPath(path);
		File credsFile = pathPath.toFile();
		if (!credsFile.exists()) {
			return "Credentials file does not exist";
		}
		if (credsFile.isDirectory()) {
			return "Credentials file is a directory, not a file";
		}
		return null;
	}

	public void startScheduler() {
		// scheduling    ( every 2 hours)
		scheduler.scheduleAtFixedRate(new DeletedDataSchedule(), 1, 2, TimeUnit.HOURS);
	}
}
