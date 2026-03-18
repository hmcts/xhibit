package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtList;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtListSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WarnedCourtListSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithFixedDateSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDate;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.WithoutFixedDateSummary;

/**
 * <p>
 * Title: WarnedCourtListHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating WarnedCourtList and
 * WarnedCourtList Summary XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WarnedCourtListHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class WarnedCourtListHelper {
    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(WarnedCourtListHelper.class);

    /**
     * Create a summary of the WarnedCourtList
     * 
     * @param courtList
     *            the court list must not be null
     * @param withFixedDate
     *            the withFixedDate, if null withoutFixedDate
     * @param withoutFixedDate
     *            the withoutFixedDate, if null withFixedDate
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
    public static WarnedCourtListSummary createSummary(WarnedCourtList courtList, WithFixedDate withFixedDate,
            WithoutFixedDate withoutFixedDate, Fixture fixture, Case caze, Defendant defendant, Solicitor solicitor) {
        WarnedCourtListSummary courtListSummary = new WarnedCourtListSummary();
        courtListSummary.setId(courtList.getId());
        courtListSummary.setCourtHouseSummary(CourtHouseHelper.createSummary(courtList.getCourtHouse()));
        courtListSummary.setWarnedForCourts(courtList.getWarnedForCourts());

        if (withFixedDate != null) {
            courtListSummary.addWithFixedDateSummary(WithFixedDateHelper.createSummary(withFixedDate, fixture, caze,
                    defendant, solicitor));
        } else {
            courtListSummary.addWithoutFixedDateSummary(WithoutFixedDateHelper.createSummary(withoutFixedDate, fixture,
                    caze, defendant, solicitor));
        }

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
    public static boolean summaryOf(WarnedCourtListSummary courtListSummary, WarnedCourtList courtList) {
        return courtListSummary.getId().equals(courtList.getId());
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param courtListSummary
     *            the list to look for the summary in
     * @param courtList
     *            the detailed WithFixedDate for which we are looking for the
     *            summary
     */
    public static WithFixedDateSummary getWithFixedDateSummary(WarnedCourtListSummary courtListSummary,
            WithFixedDate withFixedDate) {
        for (int i = 0, c = courtListSummary.getWithFixedDateSummaryCount(); i < c; i++) {
            WithFixedDateSummary withFixedDateSummary = courtListSummary.getWithFixedDateSummary(i);
            if (WithFixedDateHelper.summaryOf(withFixedDateSummary, withFixedDate)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + WithFixedDateHelper.toDebug(withFixedDateSummary) + " in "
                            + toDebug(courtListSummary) + ".");
                }

                return withFixedDateSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + WithFixedDateHelper.toDebug(withFixedDate) + " in "
                    + toDebug(courtListSummary) + ".");
        }

        return null;
    }

    /**
     * Get the court list summary for the court list or null if it does not
     * exist
     * 
     * @param courtListSummary
     *            the list to look for the summary in
     * @param courtList
     *            the detailed WithoutFixedDate for which we are looking for the
     *            summary
     */
    public static WithoutFixedDateSummary getWithoutFixedDateSummary(WarnedCourtListSummary courtListSummary,
            WithoutFixedDate withoutFixedDate) {
        for (int i = 0, c = courtListSummary.getWithoutFixedDateSummaryCount(); i < c; i++) {
            WithoutFixedDateSummary withoutFixedDateSummary = courtListSummary.getWithoutFixedDateSummary(i);
            if (WithoutFixedDateHelper.summaryOf(withoutFixedDateSummary, withoutFixedDate)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + WithoutFixedDateHelper.toDebug(withoutFixedDateSummary) + " in "
                            + toDebug(courtListSummary) + ".");
                }

                return withoutFixedDateSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Could not find summary for " + WithoutFixedDateHelper.toDebug(withoutFixedDate) + " in "
                    + toDebug(courtListSummary) + ".");
        }

        return null;
    }

    /**
     * Return the number of cases or 0 if null
     * 
     * @param warnedCourtListSummaries
     * 
     * @return the number of cases
     */
    public static int getCaseCount(WarnedCourtListSummaries warnedCourtListSummaries) {
        int count = 0;
        if (warnedCourtListSummaries != null) {
            for (int i = 0, c = warnedCourtListSummaries.getWarnedCourtListSummaryCount(); i < c; i++) {
                count += getCaseCount(warnedCourtListSummaries.getWarnedCourtListSummary(i));
            }
        }
        return count;
    }

    /**
     * Return the number of cases on the summary or 0 if null
     * 
     * @param warnedCourtListSummary
     * @return the number of cases
     */
    public static int getCaseCount(WarnedCourtListSummary warnedCourtListSummary) {
        int count = 0;
        if (warnedCourtListSummary != null) {
            for (int i = 0, c = warnedCourtListSummary.getWithFixedDateSummaryCount(); i < c; i++) {
                count += WithFixedDateHelper.getCaseCount(warnedCourtListSummary.getWithFixedDateSummary(i));
            }
            for (int i = 0, c = warnedCourtListSummary.getWithoutFixedDateSummaryCount(); i < c; i++) {
                count += WithoutFixedDateHelper.getCaseCount(warnedCourtListSummary.getWithoutFixedDateSummary(i));
            }
        }
        return count;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WarnedCourtList courtList) {
        return "WarnedCourtList[id=" + courtList.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtList.getCourtHouse()) + ", valid=" + courtList.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param courtList
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(WarnedCourtListSummary courtListSumamary) {
        return "WarnedCourtListSummary[id=" + courtListSumamary.getId() + ", courtHouse="
                + CourtHouseHelper.toDebug(courtListSumamary.getCourtHouseSummary()) + ", valid="
                + courtListSumamary.isValid() + "]";
    }
}
