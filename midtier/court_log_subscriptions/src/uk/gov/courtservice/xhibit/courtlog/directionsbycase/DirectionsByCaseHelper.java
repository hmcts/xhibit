package uk.gov.courtservice.xhibit.courtlog.directionsbycase;

import java.util.Properties;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBeanHelper2;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Helper class for the DirectionsByCase subscriber
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public class DirectionsByCaseHelper {
    private static final String XPATH_CONFIG_FILE = "directions.xpaths";

    private static final Properties _xpathConfig = CSServices.getConfigServices().getProperties(XPATH_CONFIG_FILE);

    private static final XMLServices _xmlServices = CSServices.getXMLServices();

    private DirectionsByCaseHelper() {
        // prevent external instantiation...
    }

    /**
     * Utility method used to find a value in a passed in xml string.
     * 
     * @param input
     *            String property
     * @param input
     *            String property
     * @return String
     */
    public static String getValueFromXml(String property, String logEntry) {
        return _xmlServices.getXpathValueFromXmlString(logEntry, getPropertyFromXml(property));
    }

    /**
     * Utility method used to find a String representing the xPath property from
     * a passed String.
     * 
     * @param input
     *            String
     * @return String
     */
    public static String getPropertyFromXml(String property) {
        return _xpathConfig.getProperty(property);
    }

    /**
     * Utility method used to find XhbDirectionsForCaseBasicValue by passing in
     * a CaseId.
     * 
     * @param caseId
     * @return XhbDirectionsForCaseBasicValue
     */
    public static XhbDirectionsForCaseBasicValue getDirForCaseBV(Integer caseId) {
        return XhbDirectionsForCaseBeanHelper2.findByCaseIDValue(caseId);
    }
}
