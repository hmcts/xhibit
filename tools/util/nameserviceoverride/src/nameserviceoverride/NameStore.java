package nameserviceoverride;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import org.apache.log4j.Logger;


/*
 * A class which holds the mappings between hostnames and ipAddresses.
 * The class contains two mappings:
 *    hostname -> ipAddress
 *    ipAddress -> hostname
 *
 * The mappings from ipAddress to hostname are considered inverse lookups.
 * The mappings can be defined either for the current thread only, or
 * globally across all threads.
 */

public class NameStore {
	private static final Logger log = Logger.getLogger(NameStore.class);
	
	protected static final String DEFAULT_HOSTS_FILE_NAME = "jhosts.txt";
	protected static NameStore singleton;

	protected Map<String,String> globalNames;
	protected Map<String,String> globalNamesInverse;

	protected ThreadLocal localNames;
	protected ThreadLocal localNamesInverse;


	protected NameStore() {
		globalNames = Collections.synchronizedMap(new HashMap<String,String>());
		globalNamesInverse = Collections.synchronizedMap(new HashMap<String,String>());
		localNames = new ThreadLocal();
		localNamesInverse = new ThreadLocal();
		readJHostsFile();
	}


	/*
	 * Populate the name store with hostname mappings read from a file.
	 */
	protected void readJHostsFile() {
		String filename = getJHostsFilename();
		FileInputStream fileInputStream;

		try {
			fileInputStream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			log.error("Unable to find file: " + filename);
			return;
		}

		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
		parseHostFile(bufferedReader);
	}


	/* 
	 * Return the filename of the file containing the host name mappings.
	 * The value is looked up from a system property, and if that is not
	 * present a default filename in the users home directory is assumed.
	 */
	protected String getJHostsFilename() {
		String filename = System.getProperty("nameserviceoverride.hosts.filename");

		if (filename == null || filename.equals("")) {
			String homeDirectory = System.getProperty("user.home");
			filename = homeDirectory + "/" + DEFAULT_HOSTS_FILE_NAME;
		}

		return filename;
	}


	/*
	 * Read the hosts file a line at a time and store any hostname mappings.
	 */
	protected void parseHostFile(BufferedReader reader) {
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				parseHostFileLine(line);
			}
		} catch (IOException e) {
			log.error("IOException reading file " + getJHostsFilename() + ":\n" + e.getStackTrace());
		}
	}


	/*
	 * Take a hosts file line such as
	 *
	 *   10.63.127.71	xdevxpa xhibitdisplay
	 *
	 * and split it into tokens.  Then for each hostname associated with the
	 * IP address add a mapping from the hostname to the IP address.
	 */
	protected void parseHostFileLine(String line) {
		// Replace any sequences of whitespace with a single space character.
		// e.g. <tab><tab><space> will be replaced with a single space character.
		String str = line.trim().replaceAll("\\s+", " ");

		int commentChar = str.indexOf('#');
		if (commentChar > -1) {
			// remove all characters after the comment char
			str = str.substring(0, commentChar);
		}

		if (str.length() == 0) {
			// blank line
			return;
		}

		String[] tokens = str.split(" ");
		if (tokens.length < 2) {
			log.error("Invalid line '" + line + "' in file " + getJHostsFilename());
			return;
		}

		for (int i = 1; i < tokens.length; ++i) {
			String ipAddress = tokens[0];
			String hostname = tokens[i];
			put(hostname, ipAddress);
		}
	}


	/*
	 * The constructor is protected to ensure there is only one instance of
	 * the NameStore.  getInstance() returns that unique instance.
	 */
	public static NameStore getInstance() {
		if (singleton == null) {
			synchronized (NameStore.class) {
				if (singleton == null) {
					singleton = new NameStore();
				}
			}
		}
		return singleton;
	}


	/*
	 * This method is for use on the inverse map which maps from ipAddress to hostname.
	 * It removes all key-value pairs in the map that have a value mathching hostname. 
	 */	
	protected void removeValue(Map<String,String> inverseMap, String hostname) {
		Collection<String> collection = inverseMap.values();
		Iterator<String> itr = collection.iterator();
		while (itr.hasNext()) {
			String str = itr.next();
			if (str != null && str.equals(hostname)) {
				// Removing the value from the collection also
				// removes it from the map the collection was
				// created from. 
				itr.remove();
			}
		}
	}


	/*
	 * Return the hostname map for the current thread.
	 */
	protected Map<String,String> getMap(ThreadLocal threadLocal) {
		Map<String,String> map = (Map) threadLocal.get();
		if (map == null) {
			map = Collections.synchronizedMap(new HashMap<String,String>());
			threadLocal.set(map);
		}
		return map;
	}


	/*
	 * Add a mapping from hostname to ipAddress in the global mappings.
	 */
	public synchronized void put(String hostname, String ipAddress) {
		log.debug("global put hostname=" + hostname + " ipAddress=" + ipAddress);
		globalNames.put(hostname.toUpperCase(), ipAddress);
		globalNamesInverse.put(ipAddress, hostname.toUpperCase());
	}


	/*
	 * Remove all mappings for hostname from the global mappings.
	 */
	public synchronized void remove(String hostname) {
		log.debug("global remove hostname=" + hostname);
		globalNames.remove(hostname.toUpperCase());
		removeValue(globalNamesInverse, hostname.toUpperCase());
	}


	/*
	 * Add a mapping from hostname to ipAddress in the current thread's mapping.
	 */
	public synchronized void putLocal(String hostname, String ipAddress) {
		log.debug("local put hostname=" + hostname + " ipAddress=" + ipAddress);
		Map<String,String> localThreadNames = getMap(localNames);
		localThreadNames.put(hostname.toUpperCase(), ipAddress);
		Map<String,String> localThreadNamesInverse = getMap(localNamesInverse);
		localThreadNamesInverse.put(ipAddress, hostname.toUpperCase());
	}


	/*
	 * Remove all mappings for hostname from the current thread's mapping.
	 */
	public synchronized void removeLocal(String hostname) {
		log.debug("local remove hostname=" + hostname);
		Map<String,String> localThreadNames = getMap(localNames);
		localThreadNames.remove(hostname.toUpperCase());
		Map<String,String> localThreadNamesInverse = getMap(localNamesInverse);
		removeValue(localThreadNamesInverse, hostname.toUpperCase());
	}


	/*
	 * Return the ipAddress (in String form) associated with hostname.
	 * Look first in the current thread's mappings and if no match is
	 * found then look in the global mappings.  Return null if no match
	 * is found in either mapping.
	 */
	public String get(String hostname) {
		String ipAddress;
		Map<String,String> localThreadNames = getMap(localNames);
		ipAddress = localThreadNames.get(hostname.toUpperCase());
		if (ipAddress == null || ipAddress.equals("")) {
			return globalNames.get(hostname.toUpperCase());
		}
		return ipAddress;
	}


	/*
	 * Return the hostname associated with ipAddress.
	 * Look first in the current thread's mappings and if no match is
	 * found then look in the global mappings.  Return null if no match
	 * is found in either mapping.
	 */
	public String getInverse(String ipAddress) {
		String hostname;
		Map<String,String> localThreadNamesInverse = getMap(localNamesInverse);
		hostname = localThreadNamesInverse.get(ipAddress);
		if (hostname == null || hostname.equals("")) {
			return globalNamesInverse.get(ipAddress);
		}
		return hostname;
	}
}


		
		