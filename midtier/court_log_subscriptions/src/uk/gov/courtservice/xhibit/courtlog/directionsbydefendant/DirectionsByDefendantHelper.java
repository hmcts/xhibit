package uk.gov.courtservice.xhibit.courtlog.directionsbydefendant;

import java.util.Properties;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBeanNotFoundException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Stephen Tully
 * @version $Revision: 1.5 $
 */
public class DirectionsByDefendantHelper {
    private static final String XPATH_CONFIG_FILE = "directions.xpaths";

    private static final Properties xpathConfig = CSServices.getConfigServices().getProperties(XPATH_CONFIG_FILE);

    private static final XMLServices xmlServices = CSServices.getXMLServices();

    private DirectionsByDefendantHelper() {
        // Prevent external instantiation
    }

    /**
     * Utility method used to find XhbDirectionsForDefendantBasicValue by
     * passing in a defendantOnCaseId.
     * 
     * @param defendantOnCaseId
     * @return XhbDirectionsForDefendantBasicValue
     */
    public static XhbDirectionsForDefendantBasicValue getDirForDefBV(Integer defendantOnCaseId) {
        XhbDefendantOnCase xdoc = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);
        XhbDirectionsForDefendantBasicValue[] xdfds = xdoc.getXhbDirectionsForDefendantsData();

        if (xdfds.length == 0) {
            throw new XhbDirectionsForDefendantBeanNotFoundException();
        }

        // we should only ever receive one, so return it...
        return xdfds[0];
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
        return xmlServices.getXpathValueFromXmlString(logEntry, getPropertyFromXml(property));
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
        return xpathConfig.getProperty(property);
    }
}