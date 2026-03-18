package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CommittingCourt;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Hearing;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.HearingSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.HearingSummaryType;

/**
 * <p>
 * Title: HearingHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Hearing and Hearing Summary XML
 * bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: HearingHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class HearingHelper {
    /**
     * The format for fixed dates, note its use should be synchronized on it
     * self!
     */
    private static final SimpleDateFormat HEARING_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(HearingHelper.class);

    /**
     * Create a hearing summaries object containing a sumamry for the hearing
     * 
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     */
    public static HearingSummaries createSummaries(Hearing hearing, Defendant defendant, Solicitor solicitor) {
        HearingSummaries hearingSummaries = new HearingSummaries();

        hearingSummaries.addHearingSummary(HearingHelper.createSummary(hearing, defendant, solicitor));

        return hearingSummaries;
    }

    /**
     * Create a summary for the detailed hearing for the given solicitor
     * 
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @param return
     *            the new summary
     */
    public static final HearingSummary createSummary(Hearing hearing, Defendant defendant, Solicitor solicitor) {
        if (defendant != null) {
            if (solicitor != null) {
                return createSummaryRepresentedDefendant(hearing, solicitor);
            } else {
                return createSummaryUnrepresentedDefendant(hearing, defendant);
            }
        } else {
            if (solicitor != null) {
                return createSummaryRepresentedProsecution(hearing, solicitor);
            } else {
                return createSummaryUnrepresentedProsecution(hearing);
            }
        }
    }

    /**
     * Create a summary for the detailed hearing for an unreprsented defendant
     * 
     * @param hearing
     *            the hearing must not be null
     * @param defendant
     *            the case must not be null
     * @param return
     *            the new summary
     */
    public static final HearingSummary createSummaryUnrepresentedDefendant(Hearing hearing, Defendant defendant) {
        HearingSummary hearingSummary = _createSummary(hearing);
        hearingSummary.setType(HearingSummaryType.DEFENDANT);
        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            hearingSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }
        hearingSummary.setDefendantSummaries(DefendantHelper.createSummaries(defendant));

        if (log.isDebugEnabled()) {
            log.debug("Created Unrepresented Defendant " + toDebug(hearingSummary) + ".");
        }

        return hearingSummary;
    }

    /**
     * Create a summary for the detailed hearing for a reprsented defendant
     * 
     * @param hearing
     *            the hearing must not be null
     * @param solicitor
     *            the case must not be null
     * @param return
     *            the new summary
     */
    public static final HearingSummary createSummaryRepresentedDefendant(Hearing hearing, Solicitor solicitor) {
        HearingSummary hearingSummary = _createSummary(hearing);
        hearingSummary.setType(HearingSummaryType.DEFENDANT);
        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            hearingSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }
        hearingSummary.setDefendantSummaries(DefendantHelper.createSummaries(hearing.getDefendants(), solicitor, true));

        if (log.isDebugEnabled()) {
            log.debug("Created Represented Defendant " + toDebug(hearingSummary) + ".");
        }

        return hearingSummary;
    }

    /**
     * Create a summary for the detailed hearing for an unreprsented prosecution
     * 
     * @param hearing
     *            the hearing must not be null
     * @param return
     *            the new summary
     */
    public static final HearingSummary createSummaryUnrepresentedProsecution(Hearing hearing) {
        HearingSummary hearingSummary = _createSummary(hearing);
        hearingSummary.setType(HearingSummaryType.PROSECUTION);
        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            hearingSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }
        hearingSummary.setDefendantSummaries(DefendantHelper.createSummaries(hearing.getDefendants()));

        if (log.isDebugEnabled()) {
            log.debug("Created Unrepresented Prosecution " + toDebug(hearingSummary) + ".");
        }

        return hearingSummary;
    }

    /**
     * Create a summary for the detailed hearing for a reprsented prosecution
     * 
     * @param hearing
     *            the hearing must not be null
     * @param solicitor
     *            the case must not be null
     * @param return
     *            the new summary
     */
    public static final HearingSummary createSummaryRepresentedProsecution(Hearing hearing, Solicitor solicitor) {
        HearingSummary hearingSummary = _createSummary(hearing);
        hearingSummary.setType(HearingSummaryType.PROSECUTION);
        Prosecution prosecution = hearing.getProsecution();
        if (prosecution != null) {
            hearingSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution, solicitor));
        }
        hearingSummary
                .setDefendantSummaries(DefendantHelper.createSummaries(hearing.getDefendants(), solicitor, false));

        if (log.isDebugEnabled()) {
            log.debug("Created Represented Prosecution " + toDebug(hearingSummary) + ".");
        }

        return hearingSummary;
    }

    // Initialise the common summary information
    private static final HearingSummary _createSummary(Hearing hearing) {
        HearingSummary hearingSummary = new HearingSummary();
        hearingSummary.setId(hearing.getId());

        hearingSummary.setHearingSequenceNumber(hearing.getHearingSequenceNumber());
        hearingSummary.setHearingDetails(hearing.getHearingDetails());
        if (hearing.hasCRESThearingID()) {
            hearingSummary.setCRESThearingID(hearing.getCRESThearingID());
        }
        hearingSummary.setTimeMarkingNote(hearing.getTimeMarkingNote());
        hearingSummary.setCaseNumber(hearing.getCaseNumber());
        CommittingCourt committingCourt = hearing.getCommittingCourt();
        if (committingCourt != null) {
            hearingSummary.setCommittingCourtSummary(CommittingCourtHelper.createSummary(committingCourt));
        }
        hearingSummary.setListNote(hearing.getListNote());
        if (hearing.hasNumberOfDefendants()) {
            hearingSummary.setNumberOfDefendants(hearing.getNumberOfDefendants());
        } else {
            hearingSummary.setNumberOfDefendants(hearing.getDefendants().getDefendantCount());
        }
        return hearingSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param hearingSummary
     *            the summary
     * @param hearing
     *            the details
     */
    public static boolean summaryOf(HearingSummary hearingSummary, Hearing hearing, HearingSummaryType type) {
        return hearingSummary.getId().equals(hearing.getId()) && hearingSummary.getType().equals(type);
    }

    /**
     * Return the number of hearings or 0 if null
     * 
     * @param hearings
     *            the hearings to count
     * @return the number of hearings
     */
    public static int getHearingCount(HearingSummaries hearingSummaries) {
        return hearingSummaries != null ? hearingSummaries.getHearingSummaryCount() : 0;
    }

    /**
     * Return the formated fixed date
     * 
     * @param hearing
     *            the hearing to get the formated fixed date from
     * @return the formated fixed date or null if not fixed
     */
    public static String getFormatedHearingDate(Hearing hearing) {
        Date date = hearing.getHearingDetails().getHearingDate();
        if (date == null) {
            return null;
        } else {
            synchronized (HEARING_DATE_FORMAT) {
                return HEARING_DATE_FORMAT.format(date.toDate());
            }
        }
    }

    /**
     * Return the formated fixed date
     * 
     * @param hearingSummary
     *            the hearing to get the formated fixed date from
     * @return the formated fixed date or null if not fixed
     */
    public static String getFormatedHearingDate(HearingSummary hearingSummary) {
        Date date = hearingSummary.getHearingDetails().getHearingDate();
        if (date == null) {
            return null;
        } else {
            synchronized (HEARING_DATE_FORMAT) {
                return HEARING_DATE_FORMAT.format(date.toDate());
            }
        }
    }

    /**
     * Return the formated CREST hearing id.
     * 
     * @param hearing
     *            the hearing to get the formated CREST hearing id. from
     * @return the formated CREST hearing id. or null if not CREST
     */
    public static String getFormatedCRESTHearingId(Hearing hearing) {
        return hearing.hasCRESThearingID() ? String.valueOf(hearing.hasCRESThearingID()) : "null";
    }

    /**
     * Return the formated CREST hearing id.
     * 
     * @param hearingSummary
     *            the hearing to get the formated CREST hearing id. from
     * @return the formated CREST hearing id. or null if not CREST
     */
    public static String getFormatedCRESTHearingId(HearingSummary hearingSummary) {
        return hearingSummary.hasCRESThearingID() ? String.valueOf(hearingSummary.hasCRESThearingID()) : "null";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param hearing
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Hearing hearing) {
        return "Hearing[id=" + getFormatedCRESTHearingId(hearing) + ", caseNumber=" + hearing.getCaseNumber()
                + ", fixedDate=" + getFormatedHearingDate(hearing) + ", valid=" + hearing.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param hearing
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(HearingSummary hearingSummary) {
        return "HearingSummary[id=" + getFormatedCRESTHearingId(hearingSummary) + ", caseNumber="
                + hearingSummary.getCaseNumber() + ", fixedDate=" + getFormatedHearingDate(hearingSummary) + ", valid="
                + hearingSummary.isValid() + "]";
    }

}
