package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FirmCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: FirmCourtListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating FirmCourtList XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: FirmCourtListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class FirmCourtListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(FirmCourtListHelper.class);

    /**
     * Create a summary of the FirmCourtList
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
    public static FirmCourtListSummary createSummary(FirmCourtList courtList, Sitting sitting, Hearing hearing,
            Defendant defendant, Solicitor solicitor) {
        FirmCourtListSummary courtListSummary = new FirmCourtListSummary();
        courtListSummary.setId(courtList.getId());
        courtListSummary.setCourtHouseSummary(CourtHouseHelper.createSummary(courtList.getCourtHouse()));
        courtListSummary.setSittingDate(courtList.getSittingDate());
        courtListSummary.setSittingSummaries(SittingHelper.createSummaries(sitting, hearing, defendant, solicitor));
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(courtListSummary) + ".");
        }

        return courtListSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param courtListSummary
     *            the summary
     * @param courtList
     *            the details
     */
    public static boolean summaryOf(FirmCourtListSummary courtListSummary, FirmCourtList courtList) {
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
    public static SittingSummary getSittingSummary(FirmCourtListSummary courtListSummary, Sitting sitting) {
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
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param firmCourtLists
     *            the firmCourtLists to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(FirmCourtListSummaries firmCourtListSummaries) {
        int count = 0;
        if (firmCourtListSummaries != null) {
            for (int i = 0, c = firmCourtListSummaries.getFirmCourtListSummaryCount(); i < c; i++) {
                count += getHearingCount(firmCourtListSummaries.getFirmCourtListSummary(i));
            }
        }
        return count;
    }

    /**
     * Count the number of hearings on the court lists and the reserve list
     * 
     * @param firmCourtListSummary
     *            the firmCourtListSummary to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(FirmCourtListSummary firmCourtListSummary) {
        return firmCourtListSummary != null ? SittingHelper.getHearingCount(firmCourtListSummary.getSittingSummaries())
                : 0;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(FirmCourtList courtList) {
        return "FirmCourtList[id=" + courtList.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtList.getCourtHouse()) + ", valid=" + courtList.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(FirmCourtListSummary courtListSumamary) {
        return "FirmCourtListSummary[id=" + courtListSumamary.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtListSumamary.getCourtHouseSummary()) + ", valid="
                + courtListSumamary.isValid() + "]";
    }
}
