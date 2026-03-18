package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * Orders< /p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the Common elements for Orders.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class GenericOrderHelper {
    private static final Logger log = CSServices.getLogger(GenericOrderHelper.class);

    /**
     * Get the conviction date, the latest guilty verdict or plea
     * 
     * @param courtId
     *            The court id
     * @param disposalCode
     *            The order disposal code used to determine the set of offences
     * @param defendantOnCaseId
     *            the defendant on case id to use
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    public static Date getConvictionDate(Integer courtId, String disposalCode, Integer defendantOnCaseId) {
        // Look first at verdict dates.
        Date latestDate = GenericOrderHelper.getVerdictDate(courtId, disposalCode, defendantOnCaseId);
        if (latestDate != null) {
            return latestDate;
        }

        // If those don't exist look for plea dates (these should be the dates
        // of arraignment).
        latestDate = GenericOrderHelper.getPleaDate(courtId, disposalCode, defendantOnCaseId);
        if (latestDate != null) {
            return latestDate;
        }

        // If we still don't have a date go with today.
        return new Date();
    }

    /**
     * Get the conviction date, the latest guilty verdict
     * 
     * @param courtId
     *            The court id
     * @param disposalCode
     *            The order disposal code used to determine the set of offences
     * @param defendantOnCaseId
     *            the defendant on case id to use
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    public static java.util.Date getVerdictDate(Integer courtId, String disposalCode, Integer defendantOnCaseId) {
        Collection verdictCollection = XhbVerdictBeanHelper2.findByCourtIdOrderCodeDefendantOnCaseId(courtId,
                disposalCode, defendantOnCaseId);
        Date latestDate = null;

        Iterator verdicts = verdictCollection.iterator();
        while (verdicts.hasNext()) {
            Date currentDate = ((XhbVerdict) verdicts.next()).getVerdictDate();
            if (latestDate == null || (currentDate != null && currentDate.compareTo(latestDate) > 0)) {
                latestDate = currentDate;
            }
        }

        return latestDate;
    }

    /**
     * Get the conviction date, the latest guilty plea
     * 
     * @param courtId
     *            The court id
     * @param disposalCode
     *            The order disposal code used to determine the set of offences
     * @param defendantOnCaseId
     *            the defendant on case id to use
     * @throws OrderXMLException
     *             when there is a problem in population.
     */
    public static java.util.Date getPleaDate(Integer courtId, String disposalCode, Integer defendantOnCaseId) {
        Collection pleaCollection = XhbPleaBeanHelper2.findByCourtIdOrderCodeDefendantOnCaseId(courtId, disposalCode,
                defendantOnCaseId);
        Date latestDate = null;

        Iterator pleas = pleaCollection.iterator();
        while (pleas.hasNext()) {
            Date currentDate = ((XhbPlea) pleas.next()).getArraignmentDate();
            if (latestDate == null || (currentDate != null && currentDate.compareTo(latestDate) > 0)) {
                latestDate = currentDate;
            }
        }

        return latestDate;
    }

}