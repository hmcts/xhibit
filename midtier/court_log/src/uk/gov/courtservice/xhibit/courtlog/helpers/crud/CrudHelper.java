package uk.gov.courtservice.xhibit.courtlog.helpers.crud;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.courtlog.helpers.xml.CourtLogXmlHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author pznwc5
 * @version $Revision: 1.9 $
 */
public class CrudHelper {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(CrudHelper.class);

    /**
     * Generates the court log XML
     * 
     * @param crudVal
     *            The value sent by the client
     * @return Generated XML
     */
    public static String validateEntry(CourtLogCRUDValue crudVal) {
        LOG.debug("validateEntry() - entry");

        String logEntry = CourtLogXmlHelper.getXML(crudVal);
        CourtLogXmlHelper.validateXML(logEntry, crudVal.getEventType());

        return logEntry;
    }
}
