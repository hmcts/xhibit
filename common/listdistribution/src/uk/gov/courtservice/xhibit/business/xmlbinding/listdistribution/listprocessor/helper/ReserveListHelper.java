package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ReserveList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.ReserveListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.HearingSummaryType;

/**
 * <p>
 * Title: ReserveListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating ReserveList and ReserveList
 * Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ReserveListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class ReserveListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(ReserveListHelper.class);

    /**
     * Create a summary for the detailed reserveList, note the ReserveList
     * parameter is not currently used, it is retained so in case any data is
     * required for the summary.
     * 
     * @param reserveList
     *            the reserveList to create the summary for
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summary
     */
    public static ReserveListSummary createSummary(ReserveList reserveList, Hearing hearing, Defendant defendant,
            Solicitor solicitor) {
        if (reserveList == null) {
            throw new NullPointerException("reserveList");
        }

        ReserveListSummary reserveListSummary = new ReserveListSummary();

        reserveListSummary.addHearingSummary(HearingHelper.createSummary(hearing, defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(reserveListSummary) + ".");
        }

        return reserveListSummary;
    }

    /**
     * Get the hearing summary for the hearing or null if it does not exist
     * 
     * @param fixtureSummary
     *            the list to look for the summary in
     * @param fixture
     *            the detailed Hearing for which we are looking for the summary
     */
    public static HearingSummary getHearingSummary(ReserveListSummary reserveList, Hearing hearing,
            HearingSummaryType type) {
        for (int i = 0, c = reserveList.getHearingSummaryCount(); i < c; i++) {
            HearingSummary hearingSummary = reserveList.getHearingSummary(i);
            if (HearingHelper.summaryOf(hearingSummary, hearing, type)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + HearingHelper.toDebug(hearingSummary) + " in " + toDebug(reserveList) + ".");
                }

                return hearingSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + HearingHelper.toDebug(hearing) + " in " + toDebug(reserveList)
                    + ".");
        }

        return null;
    }

    /**
     * Get the number of hearings on the reserve list
     * 
     * @param reserveList
     * @return the number of hearings
     */
    public static int getHearingCount(ReserveListSummary reserveListSummary) {
        return reserveListSummary != null ? reserveListSummary.getHearingSummaryCount() : 0;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param reserveList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(ReserveList reserveList) {
        return "ReserveList[valid=" + reserveList.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param reserveList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(ReserveListSummary reserveListSummary) {
        return "ReserveListSummary[valid=" + reserveListSummary.isValid() + "]";
    }

}
