package tdt4140.gr1811.web.server.tcp_server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Globals {

	public static final Collection<Thread> CLIENT_THREADS = Collections.synchronizedCollection(new ArrayList<Thread>());
	
}