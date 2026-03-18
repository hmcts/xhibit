package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FixtureSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDateSummary;

/**
 * <p>
 * Title: WithoutFixedDateHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating WithoutFixedDate and
 * WithoutFixedDate Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WithoutFixedDateHelper.java,v 1.1 2005/02/23 14:55:47 bzjrnl
 *          Exp $
 */
public class WithoutFixedDateHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WithoutFixedDateHelper.class);

    /**
     * Create a summary for the detailed withoutFixedDate for the given
     * solicitor
     * 
     * @param withoutFixedDate
     *            the withoutFixedDate to create the summary for
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
    public static WithoutFixedDateSummary createSummary(WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze,
            Defendant defendant, Solicitor solicitor) {
        WithoutFixedDateSummary withoutFixedDateSummary = new WithoutFixedDateSummary();
        withoutFixedDateSummary.setId(withoutFixedDate.getId());
        withoutFixedDateSummary.setHearingType(withoutFixedDate.getHearingType());
        if (withoutFixedDate.hasCRESTListSequence()) {
            withoutFixedDateSummary.setCRESTListSequence(withoutFixedDate.getCRESTListSequence());
        }

        withoutFixedDateSummary.addFixtureSummary(FixtureHelper.createSummary(fixture, caze, defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(withoutFixedDateSummary) + ".");
        }

        return withoutFixedDateSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param withoutFixedDateSummary
     *            the summary
     * @param withoutFixedDate
     *            the details
     */
    public static boolean summaryOf(WithoutFixedDateSummary withoutFixedDateSummary, WithoutFixedDate withoutFixedDate) {
        return withoutFixedDateSummary.getId().equals(withoutFixedDate.getId());
    }

    /**
     * Get the fixture summary for the fixture or null if it does not exist
     * 
     * @param withoutFixedDateSummary
     *            the list to look for the summary in
     * @param withoutFixedDate
     *            the detailed Fixture for which we are looking for the summary
     */
    public static FixtureSummary getFixtureSummary(WithoutFixedDateSummary withoutFixedDateSummary, Fixture fixture) {
        for (int i = 0, c = withoutFixedDateSummary.getFixtureSummaryCount(); i < c; i++) {
            FixtureSummary fixtureSummary = withoutFixedDateSummary.getFixtureSummary(i);
            if (FixtureHelper.summaryOf(fixtureSummary, fixture)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + FixtureHelper.toDebug(fixtureSummary) + " in "
                            + toDebug(withoutFixedDateSummary) + ".");
                }

                return fixtureSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + FixtureHelper.toDebug(fixture) + " in "
                    + toDebug(withoutFixedDateSummary) + ".");
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
    public static int getCaseCount(WithoutFixedDateSummary withoutFixedDateSummary) {
        int count = 0;
        if (withoutFixedDateSummary != null) {
            for (int i = 0, c = withoutFixedDateSummary.getFixtureSummaryCount(); i < c; i++) {
                count += FixtureHelper.getCaseCount(withoutFixedDateSummary.getFixtureSummary(i));
            }
        }
        return count;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param withoutFixedDate
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WithoutFixedDate withoutFixedDate) {
        return "WithoutFixedDate[id=" + withoutFixedDate.getId() + ", hearingType=" + withoutFixedDate.getHearingType()
                + ", sequence=" + withoutFixedDate.getCRESTListSequence() + ", valid=" + withoutFixedDate.isValid()
                + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param withoutFixedDateSummary
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WithoutFixedDateSummary withoutFixedDateSummary) {
        return "WithoutFixedDateSummary[id=" + withoutFixedDateSummary.getId() + ", hearingType="
                + withoutFixedDateSummary.getHearingType() + ", sequence="
                + withoutFixedDateSummary.getCRESTListSequence() + ", valid=" + withoutFixedDateSummary.isValid() + "]";
    }

}
