package nameserviceoverride;

import java.lang.Exception;
import java.net.UnknownHostException;
import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.dns.DNSNameService;
import sun.net.spi.nameservice.NameServiceDescriptor;
import org.apache.log4j.Logger;



/*
 * Implement a NameService lookup facilty, which can lookup hostnames from IP addresses and
 * can look up IP addresses from hostnames.  
 *
 * Hostname mappings can be dynamically canged so to prevent caching of IP addresses it is best
 * to use the following definition on the JVM command line when starting your application:
 *
 *  -Dnetworkaddress.cache.ttl=0
 */

public class LocalManagedDNS implements NameService {
	private static final Logger log = Logger.getLogger(LocalManagedDNS.class);

	private sun.net.spi.nameservice.NameService fallbackDNSImplementation = null;

	
	LocalManagedDNS() {
		try {
			log.debug("Creating fallback name service");

			//TODO
			// Use the createNameService method to create a fallback NameService.
			// HOWEVER THE NAMESERVICE RETURNED ALWAYS RETURNS 
			//   javax.naming.ServiceUnavailableException
			//
			// NameServiceDescriptor sunNameServiceDescriptor =
			//	new sun.net.spi.nameservice.dns.DNSNameServiceDescriptor();
			//
			//fallbackDNSImplementation = sunNameServiceDescriptor.createNameService();
			//TODO

			//TODO
			// Directly create the fallback name service.  Unfortunately this does
			// not work either.  The object always returns
			//   javax.naming.ServiceUnavailableException
			//
			fallbackDNSImplementation = 
				new sun.net.spi.nameservice.dns.DNSNameService();
			//TODO

			log.debug("fallbackDNSImplementation = " + fallbackDNSImplementation);
		} catch (Exception e) {
			log.error("Failed to create fallback NameService: " + e.getStackTrace());
		}
	}


	/*
	 * Takes a 4 byte ip address and performs a reverse DNS lookup. e.g.
	 *
	 * getHostByAddr((byte[]){127,0,0,1}) = "localhost";
	 *
	 * The lookup is done firstly from a user supplied set of mappings.  If
	 * that lookup fails then the real DNS is used.
	 */
	public String getHostByAddr(byte[] ip) throws UnknownHostException {
		log.debug("getHostByAddr: " + Util.numericToTextFormat(ip));
		
		String ipAddress = Util.numericToTextFormat(ip);
		String hostname = NameStore.getInstance().getInverse(ipAddress);

		if (hostname == null || hostname.equals("")) {
			log.debug("Name override not found");

			if (fallbackDNSImplementation != null) {
				log.debug("using fallback");
				return fallbackDNSImplementation.getHostByAddr(ip);
			}

			throw new UnknownHostException("ipaddress: " + ipAddress);
		} else {
			log.debug("Name override found: " + hostname);
			return hostname;
		}
	}


	/*
	 * Takes a hostname string and performs a DNS lookup. e.g.
	 *
	 * lookupAllHostAddr("localhost") = (byte[][]){{127,0,0,1}}
	 *
	 * The lookup is done firstly from a user supplied set of mappings.  If
	 * that lookup fails then the real DNS is used.
	 */
	public byte[][] lookupAllHostAddr(String hostname) throws UnknownHostException {
		log.debug("lookupAllHostAddr: " + hostname);

		String ipAddress = NameStore.getInstance().get(hostname);
		if (ipAddress == null || ipAddress.equals("")) {
			log.debug("Name override not found");

			if (fallbackDNSImplementation != null) {
				log.debug("using fallback");
				return fallbackDNSImplementation.lookupAllHostAddr(hostname);
			}

			throw new UnknownHostException("hostname: " + hostname);
		} else {
			log.debug("Name override found: " + ipAddress);
			byte[] ip = Util.textToNumericFormat(ipAddress);
			return new byte[][]{ip};
		}
	}
}



