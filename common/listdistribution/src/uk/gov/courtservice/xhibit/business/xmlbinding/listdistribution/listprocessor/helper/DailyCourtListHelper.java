package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.DailyCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: DailyCourtListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating DailyCourtList XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DailyCourtListHelper.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class DailyCourtListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(DailyCourtListHelper.class);

    /**
     * Create a summary of the DailyCourtList
     * 
     * @param courtList
     *            the court list must not be null
     * @param sitting
     *            the sitting must not be null
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summary
     */
    public static DailyCourtListSummary createSummary(DailyCourtList courtList, Sitting sitting, Hearing hearing,
            Defendant defendant, Solicitor solicitor) {
        DailyCourtListSummary courtListSummary = new DailyCourtListSummary();
        courtListSummary.setId(courtList.getId());
        courtListSummary.setCourtHouseSummary(CourtHouseHelper.createSummary(courtList.getCourtHouse()));
        courtListSummary.setSittingSummaries(SittingHelper.createSummaries(sitting, hearing, defendant, solicitor));
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(courtListSummary) + ".");
        }

        return courtListSummary;
    }

    /**
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param dailyCourtLists
     *            the dailyCourtLists to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(DailyCourtListSummaries dailyCourtListSummaries) {
        int count = 0;
        if (dailyCourtListSummaries != null) {
            for (int i = 0, c = dailyCourtListSummaries.getDailyCourtListSummaryCount(); i < c; i++) {
                count += getHearingCount(dailyCourtListSummaries.getDailyCourtListSummary(i));
            }
        }
        return count;
    }

    /**
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param dailyCourtListSummary
     *            the dailyCourtListSummary to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(DailyCourtListSummary dailyCourtListSummary) {
        return dailyCourtListSummary != null ? SittingHelper.getHearingCount(dailyCourtListSummary
                .getSittingSummaries()) : 0;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param courtListSummary
     *            the summary
     * @param courtList
     *            the details
     */
    public static boolean summaryOf(DailyCourtListSummary courtListSummary, DailyCourtList courtList) {
        return courtListSummary.getId().equals(courtList.getId());
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param courtListSummary
     *            the list to look for the summary in
     * @param courtList
     *            the detailed Sitting for which we are looking for the summary
     */
    public static SittingSummary getSittingSummary(DailyCourtListSummary courtListSummary, Sitting sitting) {
        SittingSummaries sittingSummaries = courtListSummary.getSittingSummaries();
        for (int i = 0, c = sittingSummaries.getSittingSummaryCount(); i < c; i++) {
            SittingSummary sittingSummary = sittingSummaries.getSittingSummary(i);
            if (SittingHelper.summaryOf(sittingSummary, sitting)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + SittingHelper.toDebug(sittingSummary) + " in " + toDebug(courtListSummary)
                            + ".");
                }

                return sittingSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + SittingHelper.toDebug(sitting) + " in "
                    + toDebug(courtListSummary) + ".");
        }

        return null;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(DailyCourtList courtList) {
        return "DailyCourtList[id=" + courtList.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtList.getCourtHouse()) + ", valid=" + courtList.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(DailyCourtListSummary courtListSumamary) {
        return "DailyCourtListSummary[id=" + courtListSumamary.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtListSumamary.getCourtHouseSummary()) + ", valid="
                + courtListSumamary.isValid() + "]";
    }
}
