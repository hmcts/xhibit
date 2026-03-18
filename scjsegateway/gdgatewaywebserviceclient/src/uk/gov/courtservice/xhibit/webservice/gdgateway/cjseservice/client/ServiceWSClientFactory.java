package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: ServiceWSClientFactory
 * </p>
 * <p>
 * Description: ServiceWSClientFactory generates implementations for the
 * ServiceWSClient .
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version 1.0
 */
public class ServiceWSClientFactory {
    private static ServiceWSClientFactory serviceWSClientFactory = new ServiceWSClientFactory();

    private static Logger log = CSServices.getLogger(ServiceWSClientFactory.class);

    /**
     * Zero parm constructor for Factory
     */
    protected ServiceWSClientFactory() {}

    /**
     * Returns the singleton factory object.
     *
     * @return ServiceWSClientFactory
     */
    public static ServiceWSClientFactory getInstance() {
        if (serviceWSClientFactory == null) {
            createServiceWSClientFactory();
        }
        return serviceWSClientFactory;
    }

    /**
     * Creates the singleton factory object. This method is synchronized as it
     * can be accessed from multiple threads but we only ever want to create one
     * factory (it is called from a SLSB so there may be more than one active at
     * the same time)
     *
     * @return void
     */
    protected synchronized static void createServiceWSClientFactory() {
        serviceWSClientFactory = new ServiceWSClientFactory();
    }

    /**
     * Returns a ServiceWSClient object
     *
     *
     * @return ServiceWSClient
     */
    public ServiceWSClient getServiceWSClient() {
        return createServiceWSClient();
    }

    /**
     * Creates an ServiceWSClient object.
     *
     * In future the production system will always use ServiceWSClientImpl
     *
     * @return ServiceWSClient
     */
    protected ServiceWSClient createServiceWSClient() {
        try {
            
            return (ServiceWSClient) CSServices.getDiscoveryServices().createInstance(ServiceWSClient.class);
      
        } catch (Throwable ex) {
            log.debug("createServiceWSClient failed returning ServiceWSClientImpl. Error was: " + ex);
            return new ServiceWSClientImpl();
        }
    }
}
