package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CommittingCourt;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CommittingCourtSummary;

/**
 * <p>
 * Title: CommittingCourtHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating CommittingCourt and
 * CommittingCourt Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: CommittingCourtHelper.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class CommittingCourtHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(CommittingCourtHelper.class);

    /**
     * Create a summary for the detailed committingCourt
     * 
     * @param committingCourt
     *            the committingCourt to create the summary for
     * @return the summary
     */
    public static CommittingCourtSummary createSummary(CommittingCourt committingCourt) {
        CommittingCourtSummary committingCourtSummary = new CommittingCourtSummary();

        committingCourtSummary.setCourtHouseCode(committingCourt.getCourtHouseCode());
        committingCourtSummary.setCourtHouseName(committingCourt.getCourtHouseName());

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(committingCourtSummary) + ".");
        }

        return committingCourtSummary;
    }

    /**
     * Return a string containg the committingCourtCode
     * 
     * @param committingCourt
     *            the court house to retrieve the code from
     */
    public static final String getCourtHouseCode(CommittingCourt committingCourt) {
        return committingCourt.getCourtHouseCode().getContent();
    }

    /**
     * Return a string containg the committingCourtCode
     * 
     * @param committingCourtSummary
     *            the court house to retrieve the code from
     */
    public static final String getCourtHouseCode(CommittingCourtSummary committingCourtSummary) {
        return committingCourtSummary.getCourtHouseCode().getContent();
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param committingCourt
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(CommittingCourt committingCourt) {
        return "CommittingCourt[name=" + committingCourt.getCourtHouseName() + ", code="
                + getCourtHouseCode(committingCourt) + ", valid=" + committingCourt.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param committingCourt
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(CommittingCourtSummary committingCourtSummary) {
        return "CommittingCourtSummary[name=" + committingCourtSummary.getCourtHouseName() + ", code="
                + getCourtHouseCode(committingCourtSummary) + ", valid=" + committingCourtSummary.isValid() + "]";
    }
}
