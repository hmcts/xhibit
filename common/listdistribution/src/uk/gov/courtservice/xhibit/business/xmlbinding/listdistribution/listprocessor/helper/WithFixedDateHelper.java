package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FixtureSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDateSummary;

/**
 * <p>
 * Title: WithFixedDateHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating WithFixedDate and WithFixedDate
 * Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WithFixedDateHelper.java,v 1.3 2006/06/05 12:28:23 bzjrnl Exp $
 */
public class WithFixedDateHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WithFixedDateHelper.class);

    /**
     * Create a summary for the detailed withFixedDate for the given solicitor
     * 
     * @param withFixedDate
     *            the withFixedDate must not be null
     * @param fixture
     *            the fixture must not be null
     * @param caze
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @return the summary
     */
    public static WithFixedDateSummary createSummary(WithFixedDate withFixedDate, Fixture fixture, Case caze,
            Defendant defendant, Solicitor solicitor) {
        WithFixedDateSummary withFixedDateSummary = new WithFixedDateSummary();
        withFixedDateSummary.setId(withFixedDate.getId());
        withFixedDateSummary.setHearingType(withFixedDate.getHearingType());
        if (withFixedDate.hasCRESTListSequence()) {
            withFixedDateSummary.setCRESTListSequence(withFixedDate.getCRESTListSequence());
        }

        withFixedDateSummary.addFixtureSummary(FixtureHelper.createSummary(fixture, caze, defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(withFixedDateSummary) + ".");
        }

        return withFixedDateSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param withFixedDateSummary
     *            the summary
     * @param withFixedDate
     *            the details
     */
    public static boolean summaryOf(WithFixedDateSummary withFixedDateSummary, WithFixedDate withFixedDate) {
        return withFixedDateSummary.getId().equals(withFixedDate.getId());
    }

    /**
     * Get the fixture summary for the fixture or null if it does not exist
     * 
     * @param withFixedDateSummary
     *            the list to look for the summary in
     * @param withFixedDate
     *            the detailed Fixture for which we are looking for the summary
     */
    public static FixtureSummary getFixtureSummary(WithFixedDateSummary withFixedDateSummary, Fixture fixture) {
        for (int i = 0, c = withFixedDateSummary.getFixtureSummaryCount(); i < c; i++) {
            FixtureSummary fixtureSummary = withFixedDateSummary.getFixtureSummary(i);
            if (FixtureHelper.summaryOf(fixtureSummary, fixture)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + FixtureHelper.toDebug(fixtureSummary) + " in " + toDebug(withFixedDateSummary)
                            + ".");
                }

                return fixtureSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + FixtureHelper.toDebug(fixture) + " in "
                    + toDebug(withFixedDateSummary) + ".");
        }

        return null;
    }

    /**
     * Return the number of cases or 0 if null
     * 
     * @param withFixedDateSummary
     *            the summary to get the number of cases on
     * @return the number of cases
     */
    public static int getCaseCount(WithFixedDateSummary withFixedDateSummary) {
        int count = 0;
        if (withFixedDateSummary != null) {
            for (int i = 0, c = withFixedDateSummary.getFixtureSummaryCount(); i < c; i++) {
                count += FixtureHelper.getCaseCount(withFixedDateSummary.getFixtureSummary(i));
            }
        }
        return count;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param withFixedDate
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WithFixedDate withFixedDate) {
        return "WithFixedDate[id=" + withFixedDate.getId() + ", hearingType=" + withFixedDate.getHearingType()
                + ", sequence=" + withFixedDate.getCRESTListSequence() + ", valid=" + withFixedDate.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param withFixedDateSummary
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WithFixedDateSummary withFixedDateSummary) {
        return "WithFixedDateSummary[id=" + withFixedDateSummary.getId() + ", hearingType="
                + withFixedDateSummary.getHearingType() + ", sequence=" + withFixedDateSummary.getCRESTListSequence()
                + ", valid=" + withFixedDateSummary.isValid() + "]";
    }
}
