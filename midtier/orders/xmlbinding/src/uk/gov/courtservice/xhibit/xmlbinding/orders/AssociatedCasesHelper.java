package uk.gov.courtservice.xhibit.xmlbinding.orders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingHome;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCase;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCases;

/**
 * <p>
 * Title: Helper class that populates the Castor bound java-XML object for the
 * AssociatedCases
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Populates the AssociatedCases for all orders. Unlike other helpers which use
 * 'pass by reference', assuming that they will be passed an existing object,
 * this helper actually uses a return. If there are no associated cases, the
 * return will be null, thus supporting the schema design.
 * </p>
 * <p>
 * The code using this paricular method will look something like:
 * </p>
 * <p>
 * <code>
 *          remandOrder().setAssociatedCases(
 *                  AssociatedCasesHelper.populateAssociatedCases(
 *                      remandOrder().getAssociatedCases(), lcEntity));
 * </code>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 54697 14-10-2003 AW Daley populatesAssociatedCases method refactored.
 * Previous code was not iterating around all the ScheduledHearings within the
 * hearing.
 */

public class AssociatedCasesHelper {

    // set up logger
    private static final Logger log = CSServices.getLogger(AssociatedCasesHelper.class);

    private static CaseHome caseHome;

    private static HearingHome hearingHome;

    private static ScheduledHearingHome scheduledHearingHome;

    static {
        caseHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);

        hearingHome = (HearingHome) CSServices.getServiceLocator().getLocalHome(HearingHome.class);

        scheduledHearingHome = (ScheduledHearingHome) CSServices.getServiceLocator().getLocalHome(
                ScheduledHearingHome.class);
    }

    /**
     * 
     * @param acs
     *            The associated case structure to add associated cases to, can
     *            be null.
     * @param mainCaseEntity
     *            the Case entity bean to use for population.
     * @return an instance of AssociatedCases if there are any associated cases.
     * @throws OrderXMLException
     *             When there is a problem in population.
     */
    public static AssociatedCases populateAssociatedCases(AssociatedCases acs, Case mainCaseEntity)
            throws OrderXMLException {

        String methodName = "populateAssociatedCases";

        // get the case_id to be used for searching the hearing records
        Integer case_id = mainCaseEntity.getCaseId();

        if (case_id == null)
            return acs;

        if (log.isDebugEnabled())
            log.debug(methodName + " - Case to find linked cases for: " + case_id);

        Collection hrg; // used to hold hearing records

        // pick up all hearings records for the give case_id
        try {
            hrg = hearingHome.findByCaseId(case_id);
            if (hrg == null) {
                if (log.isDebugEnabled())
                    log.debug(methodName + " - No hearings for case id: " + case_id);
                return acs;
            }
        }

        catch (FinderException ex) {
            throw new OrderXMLException("order.validation.linked.hearing.missing", "Error finding linked hearings.", ex);
        }

        Collection schhrg = null; // holds scheduled hearings
        Collection associatedCases = null; // used to hold hearing records

        // get the linked_hearing_id for all the give hearings
        // For each hearing
        Iterator hearingIter = hrg.iterator();

        if (log.isDebugEnabled())
            log.debug(methodName + " - Number of hearings for case: " + hrg.size());

        while (hearingIter.hasNext()) {
            Hearing hearing = (Hearing) hearingIter.next();
            try {
                if (log.isDebugEnabled())
                    log.debug(methodName + " - Get scheduled hearing for hearing id: " + hearing.getHearingId());

                // Get the schedule Hearings for the hearing
                schhrg = scheduledHearingHome.findByHearingId(hearing.getHearingId());

                if (schhrg == null) {
                    if (log.isDebugEnabled())
                        log.debug(methodName + " - No scheduled hearings for hearing id: " + hearing.getHearingId());
                    continue;
                }

                if (log.isDebugEnabled())
                    log.debug(methodName + " - Number of Scheduled hearings for hearing: " + schhrg.size());

                // For each schedule hearing
                Iterator iter = schhrg.iterator();
                while (iter.hasNext()) {
                    ScheduledHearing scheduledHearing = (ScheduledHearing) iter.next();

                    // If link id is null then move onto next linked
                    // scheduled
                    // hearing
                    if (scheduledHearing.getLinkedSHId() == null) {
                        log.debug(methodName + " - No linked scheduled hearing id for scheduled hearing id: "
                                + scheduledHearing.getScheduledHearingId());
                        continue;
                    }

                    // Get associated cases for the schedule hearing
                    associatedCases = getAssociatedCasesFromLinkedScheduledHearings(scheduledHearing.getLinkedSHId(),
                            case_id);

                    // If not null then add to acs
                    if (!isEmpty(associatedCases)) {
                        if (acs == null)
                            acs = new AssociatedCases();

                        populateReturnObject(acs, associatedCases);
                    }

                }

            } catch (FinderException e) {
                e.printStackTrace();
                log.error(e);
                throw new OrderXMLException("order.validation.linked.hearing.missing",
                        "Error finding Hearings by LinkedHearingId", e);
            }
        }

        // Return the contents of the Associated Cases variable, may still be
        // null if there weren't any associated cases.
        if (acs != null && log.isDebugEnabled())
            log.debug("Number of associated cases  = " + acs.getAssociatedCaseCount());

        return acs;
    }

    /**
     * Get the case for which the schedule hearing applies
     * 
     * @param scheduledHearing
     * @param mainCaseId
     * @return
     */
    private static AssociatedCase getAssociatedCaseFromScheduledHearing(ScheduledHearing scheduledHearing,
            Integer mainCaseId) throws OrderXMLException {
        String methodName = "getAssociatedCaseFromScheduledHearing";

        Case caseEntity;

        Integer caseId = scheduledHearing.getHearing().getCaseId();

        try {
            caseEntity = caseHome.findByPrimaryKey(caseId);

            // check to ensure that not own case_id
            if (!caseEntity.getCaseId().equals(mainCaseId)) {
                // create a new AssociatedCase element
                AssociatedCase ac = new AssociatedCase();

                // set up the case type, number and pad correctly
                String caseType = caseEntity.getCaseType();
                Integer caseNumber = caseEntity.getCaseNumber();
                String trueCaseNumber = caseType + FormatHelper.EIGHT_DIGIT.format(caseNumber);

                // set the new padded casenumber and set the selected
                // attribute to true
                ac.setContent(trueCaseNumber);
                ac.setSelected(true);

                if (log.isDebugEnabled())
                    log.debug(methodName + " - Linked case number: " + trueCaseNumber);

                return ac;
            }

            return null;
        }

        catch (FinderException e) {
            e.printStackTrace();
            log.error(e);
            throw new OrderXMLException("order.validation.case.details.missing", "Error finding Case by CaseID", e);
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
        HashSet associatedCases = new HashSet();

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
            AssociatedCase ac = getAssociatedCaseFromScheduledHearing((ScheduledHearing) iter.next(), mainCaseId);

            // Add the AssociatedCase to AssociatedCases.
            if (ac != null) {
                if (log.isDebugEnabled())
                    log.debug(methodName + " - adding asscociated case");

                associatedCases.add(ac);
            }
        }

        return associatedCases;

    }

    /**
     * Method that updates the AssociatesCases object with new associated cases.
     * Duplicates are not allowed in the AssociatedCases object. A more elegant
     * approach would be to expose the 'contains' method on the AssociatedCases
     * class. Deterimned too risky to change since this class is auto generated
     * and would result in changing all generated classes.
     * 
     * @param acs
     * @param associatedCases
     */
    private static void populateReturnObject(AssociatedCases acs, Collection associatedCases) {
        String methodName = "populateReturnObject";

        if (associatedCases == null) {
            if (log.isDebugEnabled())
                log.debug(methodName + " cases to add to associated cases is null");
            return;
        }

        // If associated cases are null then create
        if (acs == null) {
            if (log.isDebugEnabled())
                log.debug(methodName + " acs is null");
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug(methodName + " existing acs size: " + acs.getAssociatedCase().length);
            log.debug(methodName + " new associatedCases size: " + associatedCases.size());
        }

        // Add each associated case to the associated cases object only if the
        // case
        // does not exist in the AssociatedCases object
        Iterator iter = associatedCases.iterator();
        boolean found;
        while (iter.hasNext()) {
            found = false;
            AssociatedCase ac = ((AssociatedCase) iter.next());
            AssociatedCase[] cases = acs.getAssociatedCase();

            // Determine if case already exists in case store
            for (int i = 0; i < cases.length; i++) {
                if (cases[i].getContent().equalsIgnoreCase(ac.getContent())) {
                    found = true;
                    break;
                }
            }

            // If not found than add to store
            if (!found) {
                if (log.isDebugEnabled()) {
                    log.debug(methodName + " adding asscociated case : " + ac.getContent());
                }
                acs.addAssociatedCase(ac);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug(methodName + " asscociated case : " + ac.getContent() + " already in collection");
                }
            }
        }

        return;
    }

    /**
     * Determine if the collection is empty or null
     * 
     * @param col
     *            collection of objects
     * @return true if empty
     */
    private static boolean isEmpty(Collection col) {
        if (col == null || col.isEmpty())
            return true;
        else
            return false;
    }

}