package nameserviceoverride;

import java.security.Security;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;


public class LocalManagedDNSTest {
	private static final Logger log = Logger.getLogger(LocalManagedDNSTest.class);


	public static void main(String[] args) throws UnknownHostException {
		BasicConfigurator.configure();

		// Set the system properties.  These values could be provided on the
		// command line as:
		//
		//  -Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDNS
		//  -Dsun.net.spi.nameservice.provider.2=dns,sun
		//
		System.setProperty(
			"sun.net.spi.nameservice.provider.1", 
			LocalManagedDNSDescriptor.PROVIDER_TYPE
			+ ","
			+ LocalManagedDNSDescriptor.PROVIDER_NAME);

		System.setProperty(
			"sun.net.spi.nameservice.provider.2", "dns,sun");


		// The following two security properties take priority over the following
		// command line settings:
		// 
		//  -Dsun.net.inetaddr.ttl=0
		//  -Dsun.net.inetaddr.negative.ttl=0
		//
		Security.setProperty(
			"networkaddress.cache.ttl", "0");

		Security.setProperty(
			"networkaddress.cache.negative.ttl", "0");


		LocalManagedDNSTest tester = new LocalManagedDNSTest();
		tester.test();
	}


	public void test() throws UnknownHostException {
		log.debug("local host lookup:-");
		InetAddress localHost = InetAddress.getLocalHost();
		log.debug("local host name=" + localHost.getHostName());
		log.debug("local host address=" + localHost.getHostAddress());

		boolean doRemove = false;

		String hostname = "xdevxpf";
		sleep();
		performHostnameLookup(hostname);

		NameStore.getInstance().put(hostname, "127.0.0.1");
		sleep();
		performHostnameLookup(hostname);

		if (doRemove) {
			NameStore.getInstance().remove(hostname);
			sleep();
			performHostnameLookup(hostname);
		}
		
		NameStore.getInstance().putLocal(hostname, "127.0.0.2");
		sleep();
		performHostnameLookup(hostname);

		if (doRemove) {
			NameStore.getInstance().removeLocal(hostname);
			sleep();
			performHostnameLookup(hostname);
		}
	}

	
	public void sleep() {
		try {
			int secondsToSleep = 1;
			int milliSecondsToSleep = secondsToSleep  * 1000;
			Thread.currentThread().sleep(milliSecondsToSleep);
		} catch (java.lang.InterruptedException e) {
			log.debug("sleep interupted");
		}
	}

	
	public void performHostnameLookup(String hostname) throws UnknownHostException {
		log.debug("looking up hostname=" + hostname);
		InetAddress[] addresses = InetAddress.getAllByName(hostname);
		for (InetAddress address : addresses) {
			log.debug("host address=" + address.getHostAddress());
			// Perform reverse lookup
			InetAddress reverseAddress = 
				InetAddress.getByAddress(address.getAddress());
			log.debug("reverse lookup hostname=" + reverseAddress.getHostName());
		}
	}
}

