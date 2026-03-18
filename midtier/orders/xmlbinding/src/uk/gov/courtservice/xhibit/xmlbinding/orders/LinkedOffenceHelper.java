package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingHome;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_charge.XhbCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_offence.XhbOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_offence.XhbRefOffence;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LinkedOffence;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.LinkedOffences;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * Linked Offences
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the Linked Offences for all orders. Unlike other helpers which use
 * 'pass by reference', assuming that they will be passed an existing object,
 * this helper actually uses a return. If there are no linked offences, the
 * return will be null, thus supporting the schema design.
 * </p>
 */
public class LinkedOffenceHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(LinkedOffenceHelper.class);

    private static CaseHome caseHome;

    private static HearingHome hearingHome;

    private static ScheduledHearingHome scheduledHearingHome;

    private static DefendantOnCaseHome defendantOnCaseHome;

    static {
        caseHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);
        hearingHome = (HearingHome) CSServices.getServiceLocator().getLocalHome(HearingHome.class);
        scheduledHearingHome = (ScheduledHearingHome) CSServices.getServiceLocator().getLocalHome(
                ScheduledHearingHome.class);
        defendantOnCaseHome = (DefendantOnCaseHome) CSServices.getServiceLocator().getLocalHome(
                DefendantOnCaseHome.class);
    }

    /**
     * 
     * @param linkedOffences
     *            The Linked Offences structure to add Linked offences to, can
     *            be null.
     * @param mainCaseEntity
     *            the Case entity bean to use for population.
     * @return an instance of Linked Offences if there are any associated cases.
     * @throws OrderXMLException
     */

    public static LinkedOffences populateLinkedOffences(LinkedOffences linkedOffences, Case mainCaseEntity,
            Integer defendantId, Integer courtId, String typeCode) throws OrderXMLException {

        // used to hold case ids for linked case
        ArrayList cases = new ArrayList();

        // get the case_id to be used for searching the hearing records
        Integer case_id = mainCaseEntity.getCaseId();

        if (case_id == null) {
            return linkedOffences;
        }

        Collection hrg; // used to hold hearing records

        // pick up all hearings records for the give case_id
        try {
            hrg = hearingHome.findByCaseId(case_id);
            if (hrg == null) {
                if (log.isDebugEnabled())
                    log.debug("LinkedOffenceHelper - No hearings for case id: " + case_id);
                return linkedOffences;
            }
        }

        catch (FinderException ex) {
            throw new OrderXMLException("order.validation.linked.hearing.missing", "Error finding linked hearings.", ex);
        }

        Collection schhrg = null; // holds scheduled hearings
        Collection linkedCases = null; // used to hold linked cases

        // get the linked_hearing_id for all the give hearings
        Iterator hearingIter = hrg.iterator();

        while (hearingIter.hasNext()) {
            Hearing hearing = (Hearing) hearingIter.next();
            try {
                if (log.isDebugEnabled())
                    log.debug("LinkedOffenceHelper - Get scheduled hearing for hearing id: " + hearing.getHearingId());

                // Get the schedule Hearings for the hearing
                schhrg = scheduledHearingHome.findByHearingId(hearing.getHearingId());

                if (schhrg == null) {
                    if (log.isDebugEnabled())
                        log.debug("LinkedOffenceHelper - No scheduled hearings for hearing id: "
                                + hearing.getHearingId());
                    continue;
                }

                if (log.isDebugEnabled())
                    log.debug("LinkedOffenceHelper  - Number of Scheduled hearings for hearing: " + schhrg.size());

                // For each schedule hearing
                Iterator iter = schhrg.iterator();
                while (iter.hasNext()) {
                    ScheduledHearing scheduledHearing = (ScheduledHearing) iter.next();

                    // If link id is null then move onto next linked
                    // scheduled hearing
                    if (scheduledHearing.getLinkedSHId() == null) {
                        log.debug("LinkedOffenceHelper - No linked scheduled hearing id for scheduled hearing id: "
                                + scheduledHearing.getScheduledHearingId());
                        continue;
                    }

                    // Get linked cases for the schedule hearing
                    linkedCases = getAssociatedCasesFromLinkedScheduledHearings(scheduledHearing.getLinkedSHId(),
                            case_id);

                    // check to see if any valid linked cases
                    if ((linkedCases.size() > 0)) {
                        // add the linked cases to the array
                        addCaseIdToList(linkedCases, cases);
                    }
                }
            } catch (FinderException e) {
                e.printStackTrace();
                log.error(e);
                throw new OrderXMLException("order.validation.linked.hearing.missing",
                        "Error finding Hearings by LinkedHearingId", e);
            }
        }

        if (cases.size() > 0) {
            log.debug("NEED TO CHECK CASES IDS FOR OFFENCES");
            Iterator linkedCaseIter = cases.iterator();
            while (linkedCaseIter.hasNext()) {
                Collection docs;
                Integer caseId = (Integer) linkedCaseIter.next();
                try {
                    docs = defendantOnCaseHome.findByCaseId(caseId);

                    log.debug("No of DOCS for case Id >> " + docs.size());
                    log.debug("Defendant id to check docs >> " + defendantId);

                    for (Iterator iter = docs.iterator(); iter.hasNext();) {
                        DefendantOnCase doc = (DefendantOnCase) iter.next();
                        if (doc.getDefendantId().equals(defendantId)) {
                            Collection relevantDOOS = XhbDefendantOnOffenceBeanHelper2
                                    .findByCourtIdOrderCodeDefendantOnCaseId(courtId, typeCode, doc
                                            .getDefendantOnCaseId());
                            log.debug("No of relevant DOOS >>  " + relevantDOOS.size());

                            // ensure that there is an object to populate
                            if (relevantDOOS.size() > 0 && linkedOffences == null) {
                                linkedOffences = new LinkedOffences();
                            }

                            Iterator i = relevantDOOS.iterator();
                            while (i.hasNext()) {
                                log.debug("Add Linked Offence");
                                XhbDefendantOnOffence doo = (XhbDefendantOnOffence) i.next();
                                XhbOffence offenceEntity = doo.getXhbOffence();
                                XhbCharge chargeEntity = offenceEntity.getXhbCharge();
                                XhbRefOffence refOffenceEntity = offenceEntity.getXhbRefOffence();
                                // set up the new linked xml binding object
                                LinkedOffence linkOff = new LinkedOffence();
                                // set the case number
                                linkOff.setLinkedCaseNumber(getCaseNumberFromCharge(chargeEntity));
                                // set the offence statement
                                linkOff.setLinkedOffenceStatement(refOffenceEntity.getOffenceDesc());
                                // add the linked offence to the list of
                                // offences
                                log.debug("Add Linked Offence with caseNumber = " + linkOff.getLinkedCaseNumber());
                                log.debug("Add Linked Offence with off statement = "
                                        + linkOff.getLinkedOffenceStatement());
                                linkedOffences.addLinkedOffence(linkOff);
                            }
                        }
                    }
                } catch (FinderException e) {
                    // no defs on case for caseId
                }
            }
        }

        return linkedOffences;
    }

    /**
     * Return the formatted Case Number from the Charge Entity
     * 
     * @param chargeEntity
     * @return the formatted case number
     */
    private static String getCaseNumberFromCharge(XhbCharge chargeEntity) {
        // Get case number.
        XhbCase caseEntity = chargeEntity.getXhbCase();
        return caseEntity.getCaseType() + FormatHelper.EIGHT_DIGIT.format(caseEntity.getCaseNumber());
    }

    /**
     * Add a case id to collection if not already exist
     * 
     * @param caseIds
     *            collection of caseId to check
     * @param cases
     *            collection containing valid caseIds
     */
    private static void addCaseIdToList(Collection caseIds, ArrayList cases) {
        // loop through the caseId's
        Iterator iter = caseIds.iterator();
        while (iter.hasNext()) {
            boolean found = false;
            // case id to check
            Integer caseId = (Integer) iter.next();
            for (int i = 0; i < cases.size(); i++) {
                if (cases.get(i).equals(caseId)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // add to the case id's arraylist
                cases.add(caseId);
            }
        }
    }

    /**
     * This method determines which scheduled hearings are linked to the
     * specified hearing. The method returns the cases for which the linked
     * scheduled hearings apply.
     * 
     * @param scheduleHearingLinkId
     * @return
     */
    private static Collection getAssociatedCasesFromLinkedScheduledHearings(Integer scheduleHearingLinkId,
            Integer mainCaseId) throws OrderXMLException {
        String methodName = "getAssociatedCasesFormLinkedScheduledHearings";

        if (log.isDebugEnabled())
            log.debug(methodName + " - get associated cases for schedule hearing link id: " + scheduleHearingLinkId);

        // Defined as a set so that duplicate cases are not processed
        ArrayList linkedCases = new ArrayList();
        Collection linkedScheduledHearings;
        try {
            // Find Linked Scheduled Hearings
            linkedScheduledHearings = scheduledHearingHome.findByLinkedSchedHearingId(scheduleHearingLinkId);

            // check if hearing records for the given hearing_id are
            // retrived
            if (linkedScheduledHearings == null) {
                if (log.isDebugEnabled())
                    log.debug(methodName + " - No linked scheduled hearings id for " + "link scheduled hearing id: "
                            + scheduleHearingLinkId);
                return null;
            }
        }

        catch (FinderException ex) {
            throw new OrderXMLException("order.validation.linked.hearing.missing", "Error finding linked hearings.", ex);
        }

        if (log.isDebugEnabled())
            log.debug(methodName + " - No of linked scheduled hearings: " + linkedScheduledHearings.size());

        // For each schedule hearing that is linked to the scheduled hearing
        // passed in as an argument
        Iterator iter = linkedScheduledHearings.iterator();
        while (iter.hasNext()) {
            // Get The Associated case from the scheduled hearing.
            Integer linkedCaseId = getAssociatedCaseFromScheduledHearing((ScheduledHearing) iter.next(), mainCaseId);

            // Add the AssociatedCase to AssociatedCases.
            if (linkedCaseId != null) {
                if (log.isDebugEnabled())
                    log.debug(methodName + " - adding asscociated case");

                linkedCases.add(linkedCaseId);
            }
        }
        return linkedCases;
    }

    /**
     * Get the case for which the schedule hearing applies
     * 
     * @param scheduledHearing
     * @param mainCaseId
     * @return
     */
    private static Integer getAssociatedCaseFromScheduledHearing(ScheduledHearing scheduledHearing, Integer mainCaseId)
            throws OrderXMLException {
        String methodName = "getAssociatedCaseFromScheduledHearing";

        Case caseEntity;

        Integer caseId = scheduledHearing.getHearing().getCaseId();

        try {
            caseEntity = caseHome.findByPrimaryKey(caseId);

            // check to ensure that not the original case_id
            if (!caseEntity.getCaseId().equals(mainCaseId)) {
                // return the caseId for scheduled hearing
                Integer linkedCaseId = caseEntity.getCaseId();
                if (log.isDebugEnabled())
                    log.debug(methodName + " - Linked case id: " + linkedCaseId);
                return linkedCaseId;
            }
            return null;
        } catch (FinderException e) {
            e.printStackTrace();
            log.error(e);
            throw new OrderXMLException("order.validation.case.details.missing", "Error finding Case by CaseID", e);
        }
    }
}
