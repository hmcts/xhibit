package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CaseSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CaseSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.CaseSummaryType;

/**
 * <p>
 * Title: CaseHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Case and Case Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: CaseHelper.java,v 1.3 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class CaseHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(CaseHelper.class);

    /**
     * Create a new CaseSummary as specified by the arguments
     * 
     * @param caze
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @param return
     *            the new summary
     */
    public static CaseSummary createSummary(Case caze, Defendant defendant, Solicitor solicitor) {
        // If we have a defendant process that else assume prosecution
        if (defendant != null) {
            // If we have a solicitor assume we are represented else
            // unrepresented
            if (solicitor != null) {
                return createSummaryRepresentedDefendant(caze, solicitor);
            } else {
                return createSummaryUnrepresentedDefendant(caze, defendant);
            }
        } else {
            // If we have a solicitor assume we are represented else
            // unrepresented
            if (solicitor != null) {
                return createSummaryRepresentedProsecution(caze, solicitor);
            } else {
                return createSummaryUnrepresentedProsecution(caze);
            }
        }
    }

    /**
     * Create a case summary for a represented defendant
     * 
     * @param caze
     *            the case to make a summary of
     * @param solicitor
     *            the solicitor representing the defendant
     */
    public static CaseSummary createSummaryRepresentedDefendant(Case caze, Solicitor solicitor) {
        CaseSummary caseSummary = _createSummary(caze, CaseSummaryType.DEFENDANT);
        caseSummary.setDefendantSummaries(DefendantHelper.createSummaries(caze.getDefendants(), solicitor, true));

        Prosecution prosecution = caze.getProsecution(); // only 1
        // proecution so
        // can get from
        // case!
        if (prosecution != null) {
            caseSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }

        if (log.isDebugEnabled()) {
            log.debug("Created Represented Defendant " + toDebug(caseSummary) + ".");
        }

        return caseSummary;
    }

    /**
     * Create a case summary for a represented prosecution agency
     * 
     * @param caze
     *            the case to make a summary of
     */
    public static CaseSummary createSummaryRepresentedProsecution(Case caze, Solicitor solicitor) {
        CaseSummary caseSummary = _createSummary(caze, CaseSummaryType.PROSECUTION);
        caseSummary.setDefendantSummaries(DefendantHelper.createSummaries(caze.getDefendants(), solicitor, false));

        Prosecution prosecution = caze.getProsecution(); // only 1
        // proecution so
        // can get from
        // case!
        if (prosecution != null) {
            caseSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution, solicitor));
        }

        if (log.isDebugEnabled()) {
            log.debug("Created Represented Prosecution " + toDebug(caseSummary) + ".");
        }
        return caseSummary;
    }

    /**
     * Create a case summary for an unrepresented defendant
     * 
     * @param caze
     *            the case to make a summary of
     * @param defendant
     *            the defendant who the summary is for
     */
    public static CaseSummary createSummaryUnrepresentedDefendant(Case caze, Defendant defendant) {
        CaseSummary caseSummary = _createSummary(caze, CaseSummaryType.DEFENDANT);
        caseSummary.setDefendantSummaries(DefendantHelper.createSummaries(defendant));

        Prosecution prosecution = caze.getProsecution(); // only 1
        // proecution so
        // can get from
        // case!
        if (prosecution != null) {
            caseSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }

        if (log.isDebugEnabled()) {
            log.debug("Created Unrepresented Defendant " + toDebug(caseSummary) + ".");
        }
        return caseSummary;
    }

    /**
     * Create a case summary for an unrepresented prosecution agency
     * 
     * @param caze
     *            the case to make a summary of
     */
    public static CaseSummary createSummaryUnrepresentedProsecution(Case caze) {
        CaseSummary caseSummary = _createSummary(caze, CaseSummaryType.PROSECUTION);
        caseSummary.setDefendantSummaries(DefendantHelper.createSummaries(caze.getDefendants()));

        Prosecution prosecution = caze.getProsecution(); // only 1
        // proecution so
        // can get from
        // case!
        if (prosecution != null) {
            caseSummary.setProsecutionSummary(ProsecutionHelper.createSummary(prosecution));
        }

        if (log.isDebugEnabled()) {
            log.debug("Created Unrepresented Prosecution " + toDebug(caseSummary) + ".");
        }
        return caseSummary;
    }

    // Create a summary containing all details except DefendantSummaries
    private static CaseSummary _createSummary(Case caze, CaseSummaryType type) {
        CaseSummary caseSummary = new CaseSummary();
        caseSummary.setId(caze.getId());
        caseSummary.setType(type);
        caseSummary.setCaseNumber(caze.getCaseNumber());
        caseSummary.setHearingType(caze.getHearingType());
        caseSummary.setNoOfDefsOnCase(caze.getDefendants().getDefendantCount());
        return caseSummary;
    }

    /**
     * Return the number of cases in the summaries or 0 if none found
     * 
     * @param caseSummaries
     * @return Get the number of cases in the summaries
     */
    public static int getCaseCount(CaseSummaries caseSummaries) {
        return caseSummaries != null ? caseSummaries.getCaseSummaryCount() : 0;
    }

    /**
     * Return true if the summary is a summary of the details and of the correct
     * type
     * 
     * @param caseSummary
     *            the summary
     * @param case
     *            the details
     */
    public static boolean summaryOf(CaseSummary caseSummary, Case caze, CaseSummaryType type) {
        return caseSummary.getId().equals(caze.getId()) && caseSummary.getType().equals(type);
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param caze
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Case caze) {
        return "Case[id=" + caze.getId() + ", number=" + caze.getCaseNumber() + ", valid=" + caze.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param caseSummary
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(CaseSummary caseSummary) {
        return "CaseSummary[id=" + caseSummary.getId() + ", number=" + caseSummary.getCaseNumber() + ", valid="
                + caseSummary.isValid() + "]";
    }

}
