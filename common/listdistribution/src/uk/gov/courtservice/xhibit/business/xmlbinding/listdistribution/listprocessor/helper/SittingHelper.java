package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Sitting;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.SittingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.HearingSummaryType;

/**
 * <p>
 * Title: SittingHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Sitting and Sitting Summary XML
 * bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: SittingHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class SittingHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(SittingHelper.class);

    /**
     * Create a sitting summaries object containing the specified sitting
     * 
     * @param sitting
     *            the sitting must not be null
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summaries object
     */
    public static SittingSummaries createSummaries(Sitting sitting, Hearing hearing, Defendant defendant,
            Solicitor solicitor) {
        SittingSummaries sittingSummaries = new SittingSummaries();

        sittingSummaries.addSittingSummary(createSummary(sitting, hearing, defendant, solicitor));
        return sittingSummaries;
    }

    /**
     * Create a summary for the detailed sitting
     * 
     * @param sitting
     *            the sitting to create the summary for
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summary
     */
    public static SittingSummary createSummary(Sitting sitting, Hearing hearing, Defendant defendant,
            Solicitor solicitor) {
        SittingSummary sittingSummary = new SittingSummary();
        sittingSummary.setId(sitting.getId());
        sittingSummary.setCourtRoomNumber(sitting.getCourtRoomNumber());
        sittingSummary.setSittingSequenceNo(sitting.getSittingSequenceNo()); // Mandatory
        sittingSummary.setSittingAt(sitting.getSittingAt());
        sittingSummary.setSittingPriority(sitting.getSittingPriority());
        sittingSummary.setSittingNote(sitting.getSittingNote());
        sittingSummary.setJudiciary(sitting.getJudiciary());
        sittingSummary.setHearingSummaries(HearingHelper.createSummaries(hearing, defendant, solicitor));
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(sittingSummary) + ".");
        }

        return sittingSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param sittingSummary
     *            the summary
     * @param sitting
     *            the details
     * @return true if the summary is a summary of the details, else false
     */
    public static boolean summaryOf(SittingSummary sittingSummary, Sitting sitting) {
        return sittingSummary.getId().equals(sitting.getId());
    }

    /**
     * Get the hearing summary for the hearing or null if it does not exist
     * 
     * @param fixtureSummary
     *            the list to look for the summary in
     * @param fixture
     *            the detailed Hearing for which we are looking for the summary
     */
    public static HearingSummary getHearingSummary(SittingSummary sitting, Hearing hearing, HearingSummaryType type) {
        HearingSummaries hearingSummaries = sitting.getHearingSummaries();
        for (int i = 0, c = hearingSummaries.getHearingSummaryCount(); i < c; i++) {
            HearingSummary hearingSummary = hearingSummaries.getHearingSummary(i);
            if (HearingHelper.summaryOf(hearingSummary, hearing, type)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + HearingHelper.toDebug(hearingSummary) + " in " + toDebug(sitting) + ".");
                }

                return hearingSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + HearingHelper.toDebug(hearing) + " in " + toDebug(sitting) + ".");
        }

        return null;
    }

    /**
     * Count the number of hearings in the sittings
     * 
     * @param sittingSummaries
     *            the sittings to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(SittingSummaries sittingSummaries) {
        int count = 0;
        if (sittingSummaries != null) {
            for (int i = 0, c = sittingSummaries.getSittingSummaryCount(); i < c; i++) {
                count += getHearingCount(sittingSummaries.getSittingSummary(i));
            }
        }
        return count;
    }

    /**
     * Count the number of hearings in the sitting
     * 
     * @param sittingSummary
     *            the sitting to get the number of hearings on
     * @return the number of hearings
     */
    public static int getHearingCount(SittingSummary sittingSummary) {
        return sittingSummary != null ? HearingHelper.getHearingCount(sittingSummary.getHearingSummaries()) : 0;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param sitting
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Sitting sitting) {
        return "Sitting[id=" + sitting.getId() + ", courtRoomNumber=" + sitting.getCourtRoomNumber()
                + ", sittingSequenceNo=" + sitting.getSittingSequenceNo() + ", sittingAt=" + sitting.getSittingAt()
                + ", sittingPriority=" + sitting.getSittingPriority() + ", sittingNote=" + sitting.getSittingNote()
                + ", valid=" + sitting.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param sitting
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(SittingSummary sittingSummary) {
        return "SittingSummary[id=" + sittingSummary.getId() + ", courtRoomNumber="
                + sittingSummary.getCourtRoomNumber() + ", sittingSequenceNo=" + sittingSummary.getSittingSequenceNo()
                + ", sittingAt=" + sittingSummary.getSittingAt() + ", sittingPriority="
                + sittingSummary.getSittingPriority() + ", sittingNote=" + sittingSummary.getSittingNote() + ", valid="
                + sittingSummary.isValid() + "]";
    }

}
