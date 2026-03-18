package nameserviceoverride;

import org.apache.log4j.Logger;


/*
 * Class used to register the custom DNS service.  The service is registered as "dns,LocalManagedDNS".
 * To select this service start the Java virtual machine with the following command line options.
 *
 *  -Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDNS
 *  -Dsun.net.spi.nameservice.provider.2=dns,sun
 *
 * The JVM will attempt to instantiate this service and if it is unable to do that will then try
 * to instantiate the default Sun implementation.
 */

public class LocalManagedDNSDescriptor implements sun.net.spi.nameservice.NameServiceDescriptor {

	private static final Logger log = Logger.getLogger(LocalManagedDNSDescriptor.class);

	public static final String PROVIDER_TYPE = "dns";
	public static final String PROVIDER_NAME = "LocalManagedDNS";

	private static sun.net.spi.nameservice.NameService nameService;

	static {
		nameService = new LocalManagedDNS();
	}

	public String getType() {
		log.debug("getType: " + PROVIDER_TYPE);
		return PROVIDER_TYPE;
	}

	public String getProviderName() {
		log.debug("getProviderName: " + PROVIDER_NAME);
		return PROVIDER_NAME;
	}

	public sun.net.spi.nameservice.NameService createNameService() {
		log.debug("createNameService");
		return nameService;
	}

}

