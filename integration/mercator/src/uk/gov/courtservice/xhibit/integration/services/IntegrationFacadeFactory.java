package uk.gov.courtservice.xhibit.integration.services;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: IntegrationFacadeFactory
 * </p>
 * <p>
 * Description: IntegrationFacadeFactory generates implementations for the
 * IntegrationFacade .
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */
public class IntegrationFacadeFactory {
    private static final String DISABLE_MERCATOR_USE = "disableMercatorUse";

    private static Logger log = CSServices.getLogger(IntegrationFacadeFactory.class);

    private static IntegrationFacadeFactory self = new IntegrationFacadeFactory();

    /**
     * @roseuid 3DDBB1460363
     */
    private IntegrationFacadeFactory() {

    }

    public static IntegrationFacadeFactory getInstance() {
        log.debug("IntegrationFacadeFactory getInstance() called.");

        return self;
    }

    /**
     * @return IntegrationFacade
     * @roseuid 3DDB955C006A
     */
    public IntegrationFacade getIntegrationFacade() {
        log.debug("IntegrationFacadeFactory getIntegrationFacade() called.");
        if (isDisableMercatorUse()) {
            log.debug("returning stubed Impl");
            return new ExceptionTracingFacadeImpl(new IntegrationStubImpl());
        } else {
            log.debug("returning mercator Impl");
            return new ExceptionTracingFacadeImpl(new IntegrationFacadeImpl());
        }
    }

    /**
     * Return true if the scheduler servlet has been disabled by system property
     */
    private boolean isDisableMercatorUse() {
        String property = System.getProperty(DISABLE_MERCATOR_USE);
        return property != null && property.equalsIgnoreCase("TRUE");
    }
}
