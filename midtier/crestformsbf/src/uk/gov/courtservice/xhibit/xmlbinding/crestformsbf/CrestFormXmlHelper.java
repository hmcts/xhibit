package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.exceptions.crestformsbf.CrestFormBFXMLException;

/**
 * <p>
 * Title: Abstract super class providing utility methods and method signatures
 * for a CrestFormXmlHelper.
 * </p>
 * <p>
 * Description: An CrestFormXmlHelper is a class that populates the initial XML
 * required by a CrestForm - in effect creating a snapshot of the Crest Form
 * data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */

public abstract class CrestFormXmlHelper {
    // hold type of Crest Form to be created
    protected final String typeCode;

    private static Logger log = CSServices.getLogger(CrestFormXmlHelper.class);

    /**
     * <p>
     * Constructor to create the helper, retrieve a copy of the blank schema for
     * a particular Crest Form Type
     * </p>
     * <p>
     * Constructor signature to be repeated amongst all the children classes to
     * allow the CrestFormBFXMLHelperFactory to create instances of the them
     * </p>
     * 
     * @param typeCode -
     *            the type code of the type of helper to create
     */
    public CrestFormXmlHelper(String typeCode) {
        this.typeCode = typeCode;
        log.debug("Super CrestFormXmlHelper created for type : " + typeCode);
    }

    /**
     * The XML returned will contain available data for the given Order
     * 
     * @param defendantOnCaseID -
     *            defendant on case identifier
     * @return string containing initial xml data for the Crest Form
     */
    public abstract String getCrestFormXml(Integer defendantOnCaseID, String courtClerkName, Integer scheduledHearingId)
            throws CrestFormBFXMLException;
}
