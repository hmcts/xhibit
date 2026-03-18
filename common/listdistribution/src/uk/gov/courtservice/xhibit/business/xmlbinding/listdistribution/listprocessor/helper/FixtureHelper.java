package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Case;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CaseSummaries;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.CaseSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Fixture;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.FixtureSummary;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.types.CaseSummaryType;

/**
 * <p>
 * Title: FixtureHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Fixture and Fixture Summary XML
 * bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: FixtureHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class FixtureHelper {
    /**
     * The format for fixed dates
     */
    private static final SimpleDateFormat FIXED_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(FixtureHelper.class);

    /**
     * Create a summary for the detailed fixture for the given solicitor
     * 
     * @param fixture
     *            the fixture must not be null
     * @param caze
     *            the case must not be null
     * @param defendant
     *            the case, if null prosecution summary
     * @param solicitor
     *            the solicitor, if null unrepresented summary
     * @param return
     *            the new summary
     */
    public static final FixtureSummary createSummary(Fixture fixture, Case caze, Defendant defendant,
            Solicitor solicitor) {
        FixtureSummary fixtureSummary = new FixtureSummary();
        fixtureSummary.setId(fixture.getId());
        fixtureSummary.setFixedDate(fixture.getFixedDate());
        fixtureSummary.setNotes(fixture.getNotes());
        fixtureSummary.setCaseSummaries(new CaseSummaries());

        fixtureSummary.getCaseSummaries().addCaseSummary(CaseHelper.createSummary(caze, defendant, solicitor));

        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(fixtureSummary) + ".");
        }

        return fixtureSummary;
    }

    /**
     * Return true if the summary is a summary of the details
     * 
     * @param fixtureSummary
     *            the summary
     * @param fixture
     *            the details
     */
    public static boolean summaryOf(FixtureSummary fixtureSummary, Fixture fixture) {
        return fixtureSummary.getId().equals(fixture.getId());
    }

    /**
     * Get the case summary for the case or null if it does not exist
     * 
     * @param fixtureSummary
     *            the list to look for the summary in
     * @param fixture
     *            the detailed Case for which we are looking for the summary
     */
    public static CaseSummary getCaseSummary(FixtureSummary fixtureSummary, Case caze, CaseSummaryType type) {
        CaseSummaries caseSummaries = fixtureSummary.getCaseSummaries();
        for (int i = 0, c = caseSummaries.getCaseSummaryCount(); i < c; i++) {
            CaseSummary caseSummary = caseSummaries.getCaseSummary(i);
            if (CaseHelper.summaryOf(caseSummary, caze, type)) {
                if (log.isDebugEnabled()) {
                    log.debug("Found " + CaseHelper.toDebug(caze) + " in " + toDebug(fixtureSummary) + ".");
                }

                return caseSummary;
            }
        }

        if (log.isDebugEnabled()) {
            log
                    .debug("Could not find summary for " + CaseHelper.toDebug(caze) + " in " + toDebug(fixtureSummary)
                            + ".");
        }

        return null;
    }

    /**
     * Return the formated fixed date
     * 
     * @param fixture
     *            the fixture to get the formated fixed date from
     * @return the formated fixed date or null if not fixed
     */
    public static String getFormatedFixedDate(Fixture fixture) {
        Date date = fixture.getFixedDate();
        if (date == null) {
            return null;

        } else {
            return FIXED_DATE_FORMAT.format(date.toDate());
        }
    }

    /**
     * Return the formated fixed date
     * 
     * @param fixtureSummary
     *            the fixture to get the formated fixed date from
     * @return the formated fixed date or null if not fixed
     */
    public static String getFormatedFixedDate(FixtureSummary fixtureSummary) {
        Date date = fixtureSummary.getFixedDate();
        if (date == null) {
            return null;

        } else {
            return FIXED_DATE_FORMAT.format(date.toDate());
        }
    }

    /**
     * Return the number of cases on the fixture or 0 if null
     * 
     * @param fixtureSummary
     *            the fixtureSummary to count the cases on
     * @return the number of cases
     */
    public static int getCaseCount(FixtureSummary fixtureSummary) {
        return CaseHelper.getCaseCount(fixtureSummary.getCaseSummaries());
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param fixture
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Fixture fixture) {
        return "Fixture[id=" + fixture.getId() + ", fixedDate=" + getFormatedFixedDate(fixture) + ", valid="
                + fixture.isValid() + "]";
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param fixture
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(FixtureSummary fixtureSummary) {
        return "FixtureSummary[id=" + fixtureSummary.getId() + ", fixedDate=" + getFormatedFixedDate(fixtureSummary)
                + ", valid=" + fixtureSummary.isValid() + "]";
    }

}
