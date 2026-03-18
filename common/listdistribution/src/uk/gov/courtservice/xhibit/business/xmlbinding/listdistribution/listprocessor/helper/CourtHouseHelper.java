package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CourtHouse;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CourtHouseSummary;

/**
 * <p>
 * Title: CourtHouseHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating CourtHouse and CourtHouse Summary
 * XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: CourtHouseHelper.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class CourtHouseHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(CourtHouseHelper.class);

    /**
     * Create a summary for the detailed courtHouse
     * 
     * @param courtHouse
     *            the courtHouse to create the summary for
     * @return the summary
     */
    public static CourtHouseSummary createSummary(CourtHouse courtHouse) {
        CourtHouseSummary courtHouseSummary = new CourtHouseSummary();

        courtHouseSummary.setCourtHouseCode(courtHouse.getCourtHouseCode());
        courtHouseSummary.setCourtHouseName(courtHouse.getCourtHouseName());

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(courtHouseSummary) + ".");
        }

        return courtHouseSummary;
    }

    /**
     * Return a string containg the courtHouseCode
     * 
     * @param courtHouse
     *            the court house to retrieve the code from
     */
    public static final String getCourtHouseCode(CourtHouse courtHouse) {
        return courtHouse.getCourtHouseCode().getContent();
    }

    /**
     * Return a string containg the courtHouseCode
     * 
     * @param courtHouseSummary
     *            the court house to retrieve the code from
     */
    public static final String getCourtHouseCode(CourtHouseSummary courtHouseSummary) {
        return courtHouseSummary.getCourtHouseCode().getContent();
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtHouse
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(CourtHouse courtHouse) {
        return "CourtHouse[name=" + courtHouse.getCourtHouseName() + ", code=" + getCourtHouseCode(courtHouse)
                + ", valid=" + courtHouse.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtHouse
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(CourtHouseSummary courtHouseSummary) {
        return "CourtHouseSummary[name=" + courtHouseSummary.getCourtHouseName() + ", code="
                + getCourtHouseCode(courtHouseSummary) + ", valid=" + courtHouseSummary.isValid() + "]";
    }
}
