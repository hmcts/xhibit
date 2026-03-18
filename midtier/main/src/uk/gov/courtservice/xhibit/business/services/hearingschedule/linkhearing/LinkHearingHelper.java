package uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing;

// jdk

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.Calendar;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.linkedsh.LinkedSHMaintainer;
import uk.gov.courtservice.xhibit.business.entities.linkedsh.LinkedSh;
import uk.gov.courtservice.xhibit.business.entities.linkedsh.LinkedShHome;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingHome;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.entities.LinkedSHBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.LinkSuggestionValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version 1.0
 */

public class LinkHearingHelper {
    // Homes used in this helper
    CaseHome caseHome;

    LinkedShHome lshHome;

    ScheduledHearingHome schedHearingHome;

    // Maintainers used in this helper
    CaseMaintainer cMaintainer;

    LinkedSHMaintainer lshMaintainer;

    ScheduledHearingMaintainer shMaintainer;

    // Error keys
    private static final String SCHED_HEARING_NOT_FOUND = "linkhearing.schdhearingnotfound";

    private static final String NO_SCHED_HEARINGS_TO_LINK = "linkhearing.nothingtounlink";

    private static Logger log = CSServices.getLogger(LinkHearingHelper.class);

    public LinkHearingHelper() {
        log.debug("LinkHearingHelper constructor - start");

        // Homes
        caseHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);
        lshHome = (LinkedShHome) CSServices.getServiceLocator().getLocalHome(LinkedShHome.class);
        schedHearingHome = (ScheduledHearingHome) CSServices.getServiceLocator().getLocalHome(
                ScheduledHearingHome.class);

        // Maintainers
        cMaintainer = new CaseMaintainer();
        lshMaintainer = new LinkedSHMaintainer();
        shMaintainer = new ScheduledHearingMaintainer();

        log.debug("LinkHearingHelper constructor - end");
    }

    /**
     * Return all the scheduled hearings in the current sitting and also those
     * added via 'Add Hearing'.
     * @param schedHearing
     * @return Collection<ScheduledHearingBasicValue>
     */
    private Collection<ScheduledHearingBasicValue> getScheduledHearings(
            ScheduledHearing schedHearing) {
        
        Collection<ScheduledHearing> sittingHearings =
            schedHearing.getSitting().getScheduledHearings();
       
        
        Collection<ScheduledHearingBasicValue> sittingHearingsBasic = 
        	shMaintainer.getScheduledHearings(sittingHearings);
        // Start of today
        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Calendar.HOUR_OF_DAY, 0);
        fromDate.set(Calendar.MINUTE, 0);
        fromDate.set(Calendar.SECOND, 0);
        
        // End of today
        Calendar toDate = Calendar.getInstance();
        toDate.set(Calendar.HOUR_OF_DAY, 0);
        toDate.set(Calendar.MINUTE, 0);
        toDate.set(Calendar.SECOND, 0);
        toDate.add(Calendar.DATE, 1);
        
        try {
        	
            Collection<ScheduledHearing> manuallyAddedHearings = new ArrayList<ScheduledHearing>();
            //If the current one is added via add hearigns then we need to find the ones added via daily list
            if(schedHearing.getAddHearingUsed()!=null && schedHearing.getAddHearingUsed().equalsIgnoreCase("Y")) {
            	manuallyAddedHearings.addAll(
                        shMaintainer.findWhereAddHearingNotUsed(
                                fromDate.getTime(), 
                                toDate.getTime(), 
                                schedHearing.getHearing().getCourtId()));
                    
            }
            // Find a list of hearings added via the 'Add Hearing' menu option and
            // add them to those from the current sitting.  The hearing might already
            // be in the sitting for the case, depending on the value selected for the
            // judge in the Add Hearing Wizard.
        	manuallyAddedHearings.addAll(
                shMaintainer.findWhereAddHearingUsed(
                        fromDate.getTime(), 
                        toDate.getTime(), 
                        schedHearing.getHearing().getCourtId()));
            
            for (ScheduledHearing addedHearing : manuallyAddedHearings) {
                boolean alreadyInSitting = false;
                for (ScheduledHearing sittingHearing : sittingHearings) {
                    if (addedHearing.getScheduledHearingId().equals(sittingHearing.getScheduledHearingId())) {
                        alreadyInSitting = true;
                        break;
                    }
                }
                if (!alreadyInSitting) {
                    // Defect 6643 - SA 17/05/0212
                    // Change the filtered scheduled hearings to remove any references to hearings added manually
                    // to a different courtroom as these should not be shown.
                    // Only hearings added to the current courtroom are to be shown in addition to all sittings
                    // that were included in original schedule.
                    if (addedHearing.getSitting().getCourtRoomId().intValue() == schedHearing.getSitting().getCourtRoomId().intValue()) {
                        ScheduledHearingBasicValue addedHearingBv = shMaintainer.getScheduledHearingBasicValue(addedHearing);
                        sittingHearingsBasic.add(addedHearingBv);
                    }
                }
            }
        } catch (ObjectNotFoundException e) {
            log.debug("No hearings have been added via the Add Hearing option");
        }
        
        return sittingHearingsBasic;
    }
    
    /**
     * Provides suggestions for scheduled hearings which could be linked to the
     * scheduledHearingId provided. The sugesstions consist of all other
     * scheduled hearings in the same sitting, ordered by hearing progress from
     * 0 to 2 where 0 = To Be Heard, 1 = In Progress and 2 = Finished
     * 
     * @param scheduledHearingId
     *            The scheduled hearing id
     * @return LinkSuggestionValue
     * @throws HearingScheduleException
     */
    public LinkSuggestionValue suggestLinkCases(Integer scheduledHearingId) throws HearingScheduleException {
        log.debug("suggestLinkCases(Integer scheduledHearingId) - start with scheduledHearingId = "
                + scheduledHearingId);
        LinkSuggestionValue lsv = new LinkSuggestionValue();
        ScheduledHearing schedHearingBean;
        Integer linkedSHId;

        // Get the linkedSHId for this scheduled hearing
        try {
            schedHearingBean = shMaintainer.findByPK(scheduledHearingId);
            linkedSHId = schedHearingBean.getLinkedSHId();
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new HearingScheduleException(SCHED_HEARING_NOT_FOUND, "Couldn't find scheduledHearing for id = "
                    + scheduledHearingId, e);
        }

        // create a CaseSchedHearingValue for the current case
        CaseSchedHearingValue cshValue = createCaseSchedHearingValue(schedHearingBean);

        // set the current case
        lsv.setCurrentCase(cshValue);

        // get ScheduledHearingBasicValues for all the scheduled hearings in the
        // same sitting as the one specifed
        Collection<ScheduledHearingBasicValue> basicSchedHearings = 
            getScheduledHearings(schedHearingBean);
        
        // filter and order the scheduled hearings
        filterScheduledHearings(basicSchedHearings, scheduledHearingId, linkedSHId);
                
        ArrayList<ScheduledHearingBasicValue> allSuggestions = 
            new ArrayList<ScheduledHearingBasicValue>();
        allSuggestions.addAll(basicSchedHearings);
        orderScheduledHearings(allSuggestions);

        ArrayList<CaseSchedHearingValue> allCaseSuggestions = 
            new ArrayList<CaseSchedHearingValue>();

        // create a CaseSchedHearingValue for each scheduled hearing in the
        // list of suggestions
        int listSize = allSuggestions.size();

        for (int i = 0; i < listSize; i++) {
            try {
                CaseSchedHearingValue cshSuggestValue = new CaseSchedHearingValue();
                ScheduledHearingBasicValue shbv = allSuggestions.get(i);
                cshSuggestValue.setShbValue(shbv);
                schedHearingBean = shMaintainer.findByPK(shbv.getId());
                Integer caseId = schedHearingBean.getHearing().getCaseId();
                Case caze = cMaintainer.findByPrimaryKey(caseId);
                cshSuggestValue.setCaseId(caseId);
                cshSuggestValue.setCaseNumber(caze.getCaseNumber());
                cshSuggestValue.setCaseType(caze.getCaseType());
                cshSuggestValue.setHearingProgress(shbv.getHearingProgress());
                cshSuggestValue.setShbValue(shbv);
                allCaseSuggestions.add(cshSuggestValue);
            } catch (ObjectNotFoundException e) {
                // any objects not found here are due to inconsistencies in the
                // db
                // all these are being looked up from the schedhearing id which
                // has
                // already been found
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new EJBException(e);
            }
        }

        // set all suggestions
        lsv.setAllSuggestions(allCaseSuggestions);

        // not setting previously linked for this release, may be provided in
        // future releases, for now set empty ArrayList
        lsv.setPreviouslyLinked(new ArrayList());

        log.debug("suggestLinkCases(Integer scheduledHearingId) - end");

        return lsv;
    }

    /**
     * Links the scheduled hearings in the Collection with the link id of the
     * lead scheduled hearing, or a new link id if the lead scheduled hearing is
     * not already linked
     * 
     * @param scheduledHearingIds
     *            The ids of the scheduled hearings to link
     * @param leadScheduledHearingId
     *            The lead scheduled hearing
     * @throws HearingScheduleException
     */
    public void linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId, String userDisplayName)
            throws HearingScheduleException {
        log
                .debug("linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId) - start with leadScheduledHearingId = "
                        + leadScheduledHearingId);

        ScheduledHearing leadSchedHearingBean;
        Integer leadHearingId;
        Vector<Integer> linkHearingIds = new Vector<Integer>();

        // check if there is already a link group for this lead case
        try {
            leadSchedHearingBean = shMaintainer.findByPK(leadScheduledHearingId);
            // find the hearing id for the lead
            leadHearingId = leadSchedHearingBean.getHearingId();
            log.debug("linkCases() " + "leadHearingId = " + leadHearingId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new HearingScheduleException(SCHED_HEARING_NOT_FOUND, "Couldn't find scheduledHearing for id = "
                    + leadScheduledHearingId, e);
        }

        ScheduledHearingBasicValue leadShbv = shMaintainer.createBasicVO(leadSchedHearingBean);
        Integer linkedShId = leadShbv.getLinkedSHID();
        if (linkedShId == null) {
            // we need to create a new linkedShId by creating a new linkedSh
            // entity
            LinkedSHBasicValue lshBasicValue = new LinkedSHBasicValue();
            linkedShId = ((LinkedSh) lshMaintainer.create(lshBasicValue, userDisplayName)).getLinkedShId();

            // need to set the lead sched hearing with this link id
            ScheduledHearingBasicValue leadShBasicValue = shMaintainer.createBasicVO(leadSchedHearingBean);
            leadShBasicValue.setLinkedSHID(linkedShId);

            try {
                shMaintainer.update(leadShBasicValue, userDisplayName);
            } catch (ObjectNotFoundException e) {
                // if this object is not found then someone else has deleted it
                // since it was read
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new OptimisticLockException(e);
            }
        } else // we need to include all hearings from scheduled hearings
        // already linked to the lead
        {
            try {
                Collection schedHearings = shMaintainer.findByLinkedSchedHearingId(linkedShId);
                // add the hearingIds to the Vector of Hearing Ids
                Iterator it = schedHearings.iterator();
                while (it.hasNext()) {
                    // add the hearingId to the Vector of Hearing Ids
                    Integer newHearingId = ((ScheduledHearing) it.next()).getHearingId();
                    log.debug("linkCases() " + "newHearingId = " + newHearingId);

                    if (!newHearingId.equals(leadHearingId) && !linkHearingIds.contains(newHearingId))
                        linkHearingIds.add(newHearingId);
                }
            } catch (ObjectNotFoundException e) {
                // this should never be thrown from a finder which returns a
                // Collection!
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new EJBException(e);
            }
        }

        // now updated the linkedShId for each scheduled hearing in the
        // Collection
        int size = caseSchedHearingValues.length;

        for (int i = 0; i < size; i++) {
            ScheduledHearingBasicValue shBasicValue = caseSchedHearingValues[i].getShbValue();
            // add the hearingId to the Vector of Hearing Ids
            Integer hearingId = shBasicValue.getHearingID();
            log.debug("linkCases() " + "hearingId = " + hearingId);

            if (!hearingId.equals(leadHearingId) && !linkHearingIds.contains(hearingId))
                linkHearingIds.add(hearingId);

            // check if this scheduled hearing is already linked, if so we
            // need to
            // include all the scheduled hearings it is linked to in this
            // group
            Integer existingLinkId = shBasicValue.getLinkedSHID();
            if (existingLinkId != null && !existingLinkId.equals(linkedShId)) {
                try {
                    Collection schedHearings = shMaintainer.findByLinkedSchedHearingId(existingLinkId);
                    // add the hearingIds to the Vector of Hearing Ids
                    Iterator it = schedHearings.iterator();
                    while (it.hasNext()) {
                        // add the hearingId to the Vector of Hearing Ids
                        Integer newHearingId = ((ScheduledHearing) it.next()).getHearingId();
                        log.debug("linkCases() " + "newHearingId = " + newHearingId);

                        if (!newHearingId.equals(leadHearingId) && !linkHearingIds.contains(newHearingId))
                            linkHearingIds.add(newHearingId);
                    }

                    updateLinkIds(schedHearings, linkedShId, userDisplayName);
                } catch (ObjectNotFoundException e) {
                    // this should never be thrown from a finder which
                    // returns a Collection!
                    CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                    throw new EJBException(e);
                }

            }
            shBasicValue.setLinkedSHID(linkedShId);
            try {
                shMaintainer.update(shBasicValue, userDisplayName);
            } catch (ObjectNotFoundException e) {
                // if this object is not found then someone else has deleted it
                // since it was read
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new OptimisticLockException(e);
            }
        }

        // call the method to update the hearingIds
        if (log.isDebugEnabled()) {
            log.debug("linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId) - "
                    + "Hearing Ids to link: ");
            for (int j = 0; j < linkHearingIds.size(); j++) {
                log.debug("linkHearingId = " + linkHearingIds.elementAt(j));
            }
        }
        LinkHearingsHelper helper = new LinkHearingsHelper();
        helper.linkHearings(leadHearingId, linkHearingIds, userDisplayName);

        log.debug("linkCases(CaseSchedHearingValue[] caseSchedHearingValues, Integer leadScheduledHearingId) - end");
    }

    /**
     * Removes the scheduled hearing passed from any link group
     * 
     * @param scheduledHearingId
     * @throws HearingScheduleException
     */
    public void unLinkCase(ScheduledHearingBasicValue shBasicValue, String userDisplayName) throws HearingScheduleException {

        Vector<ScheduledHearing> schedHearingsToUpdate = new Vector<ScheduledHearing>();

        // find the scheduled hearing
        ScheduledHearing unlinkHearing = findSchedHearing(shBasicValue.getId());

        // get the linked scheduled hearing id
        Collection<ScheduledHearing> linkedSchedHearings = 
            this.findLinkedSchedHearings(unlinkHearing.getLinkedSHId());
        
        int size = linkedSchedHearings.size();

        log.debug("There are " + size + " sched hearings with linked schedhearing id " + unlinkHearing.getLinkedSHId());

        // if there is only one hearing - then just add this one to be updated.
        if (size == 1) {
            schedHearingsToUpdate.addElement(unlinkHearing);
        }
        // if there are two - then add the collection and update these two
        else if (size == 2) {
            schedHearingsToUpdate.addAll(linkedSchedHearings);
        }
        // if there are 3 or more linked hearings we don't want to break any
        // other
        // linkage so just add the one hearing.
        else if (size >= 3) {
            schedHearingsToUpdate.addElement(unlinkHearing);
        }

        // Update the hearing(s) with the "unlinkage" status.
        this.unlinkSchedHearings(schedHearingsToUpdate, userDisplayName);

        shBasicValue.setLinkedSHID(null);
        try {
            shMaintainer.update(shBasicValue, userDisplayName);
        } catch (ObjectNotFoundException e) {
            // if this object is not found then someone else has deleted it
            // since it was read
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new OptimisticLockException(e);
        }
    }

    /**
     * Finds all the scheduled hearings in a linked group for a given scheduled
     * hearing id.
     * 
     * @param linkedShId
     *            The linked scheduled hearing id
     * @return An array of CaseSchedHearingValue for the linked scheduled
     *         hearings
     * @throws HearingScheduleException
     */
    public CaseSchedHearingValue[] getLinkedSchedHearingsByLinkId(Integer linkedShId) throws HearingScheduleException {
        CaseSchedHearingValue[] cshValues;
        Collection schedHearings;
        try {
            schedHearings = shMaintainer.findByLinkedSchedHearingId(linkedShId);
        } catch (ObjectNotFoundException e) {
            // this should never be thrown from a finder which returns a
            // Collection!
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
        int size = schedHearings.size();
        cshValues = new CaseSchedHearingValue[size];
        Iterator it = schedHearings.iterator();
        for (int i = 0; i < size; i++) {
            ScheduledHearing schedHearing = (ScheduledHearing) it.next();
            cshValues[i] = createCaseSchedHearingValue(schedHearing);
        }

        return cshValues;
    }

    /**
     * Finds all the scheduled hearings in a linked group for a given scheduled
     * hearing id.
     * 
     * @param linkedSchedHearingId
     *            The scheduled hearing id
     * @return An array of CaseSchedHearingValue for the linked scheduled
     *         hearings
     * @throws HearingScheduleException
     */
    public CaseSchedHearingValue[] getLinkedSchedHearingsByShId(Integer schedHearingId) throws HearingScheduleException {
        CaseSchedHearingValue[] cshValues;
        ScheduledHearing schedHearing;
        try {
            schedHearing = shMaintainer.findByPK(schedHearingId);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new HearingScheduleException(SCHED_HEARING_NOT_FOUND, "Couldn't find scheduledHearing for id = "
                    + schedHearingId, e);
        }

        Integer linkedShId = schedHearing.getLinkedSHId();
        if (linkedShId == null) {
            cshValues = new CaseSchedHearingValue[0];
            return cshValues;
        } else {
            return getLinkedSchedHearingsByLinkId(linkedShId);
        }
    }

    // -----------------------------Private
    // Methods------------------------------//

    /**
     * Removes from the Collection the specifed scheduled hearing and any
     * hearings already linked to this scheduledHearing.
     * 
     * @param basicScheduledHearings
     * @param scheduledHearingId
     * @param linkedSHId
     */
    private void filterScheduledHearings(Collection basicScheduledHearings, Integer scheduledHearingId,
            Integer linkedSHId) {
        Iterator it = basicScheduledHearings.iterator();
        while (it.hasNext()) {
            ScheduledHearingBasicValue basicSchedHearing = (ScheduledHearingBasicValue) it.next();

            // remove the specifed scheduled hearing
            if (basicSchedHearing.getId().equals(scheduledHearingId)) {
                it.remove();
            }
            // remove any scheduled hearing already linked to the specifed
            // scheduled hearing
            else if (basicSchedHearing.getLinkedSHID() != null && basicSchedHearing.getLinkedSHID().equals(linkedSHId)) {
                it.remove();
            }
        }
    }

    /**
     * Orders the ScheduledHearings returned, by 'To Be Heard', 'In Progress'
     * and 'Finished'.
     * 
     * @param basicScheduledHearings
     */
    private void orderScheduledHearings(ArrayList allSuggestions) {
        // the attributes in the ScheduledHearingBasicValue by which to sort
        String[] keys = new String[] { "hearingProgress" };
        Sorter.sort(allSuggestions, keys);
    }

    /**
     * Updates the linkedShId for all scheduled hearing entities passed. All
     * scheduledHearings passed will have the same linkedSHId which is different
     * to the linkedShId to be set.
     * 
     * @param schedHearings
     *            Collection of ScheduledHearings
     * @param linkedShId
     *            The new linkedShId
     */
    private void updateLinkIds(Collection schedHearings, Integer linkedShId, String userDisplayName) {
        Iterator it = schedHearings.iterator();

        while (it.hasNext()) {
            ScheduledHearing schedHearing = (ScheduledHearing) it.next();
            ScheduledHearingBasicValue shBasicValue = shMaintainer.createBasicVO(schedHearing);
            shBasicValue.setLinkedSHID(linkedShId);
            try {
                shMaintainer.update(shBasicValue, userDisplayName);
            } catch (ObjectNotFoundException e) {
                // if this object is not found then someone else has deleted it
                // since it was read
                CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
                throw new OptimisticLockException(e);
            }
        }
    }

    /**
     * Creates a CaseSchedHearingValue from a ScheduledHearing entity
     * 
     * @param schedHearing
     *            The entity from which to creat the value object
     * @return CaseSchedHearingValue
     */
    private CaseSchedHearingValue createCaseSchedHearingValue(ScheduledHearing schedHearing) {
        Case caze;
        CaseSchedHearingValue cshValue = new CaseSchedHearingValue();
        ScheduledHearingBasicValue shbValue = shMaintainer.getScheduledHearingBasicValue(schedHearing);
        cshValue.setShbValue(shbValue);
        Integer caseId = schedHearing.getHearing().getCaseId();
        try {
            caze = cMaintainer.findByPrimaryKey(caseId);
        } catch (ObjectNotFoundException e) {
            // any objects not found here are due to inconsistencies in the
            // db
            // as this case id has just been found from the db
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
        cshValue.setCaseId(caseId);
        cshValue.setCaseNumber(caze.getCaseNumber());
        cshValue.setCaseType(caze.getCaseType());
        cshValue.setHearingProgress(shbValue.getHearingProgress());
        cshValue.setShbValue(shbValue);
        return cshValue;
    }

    /**
     * Uses the maintainer to find the ScheduledHearing local intf.
     * 
     * @param schedHearingID
     * @return ScheduledHearing entity
     * @throws HearingScheduleException
     */
    private ScheduledHearing findSchedHearing(Integer schedHearingId) throws HearingScheduleException {
        log.debug("findSchedHearing(Integer schedHearingID) called, schedHearingId = " + schedHearingId);
        ScheduledHearing schdHearing = null;
        try {
            schdHearing = shMaintainer.findByPK(schedHearingId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(SCHED_HEARING_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }
        log.debug("LinkHearingsHelper.findHearing(Integer hearingID) finished");
        return schdHearing;
    }

    /**
     * This wil find all the sched hearings with the same linked sched hearing
     * id.
     * 
     * @param linkedId
     * @return Collection of scheduled hearing local refs
     * @throws HearingScheduleException
     */
    private Collection<ScheduledHearing> findLinkedSchedHearings(Integer linkedId) throws HearingScheduleException {
        log.debug("LinkHearingHelper.findLinkedSchedHearings(Integer linkedId) called");
        Collection<ScheduledHearing> linkedSchedHearings = null;
        try {
            linkedSchedHearings = shMaintainer.findByLinkedSchedHearingId(linkedId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(SCHED_HEARING_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }
        log.debug("LinkHearingsHelper.findLinkedHearings(Integer linkedID) finished");
        return linkedSchedHearings;
    }

    /**
     * This method will loop through all sched hearings and set the new Lead
     * linked sched hearing id and update them.
     * 
     * @param leadSchedLinkedHearingID
     * @param Collection
     *            of schedHearings
     * @throws HearingScheduleException
     */
    private void unlinkSchedHearings(Collection schedHearings, String userDisplayName) throws HearingScheduleException {
        log.debug("LinkHearingHelper.unlinkSchedHearings(" + "Collection schedHearings) called");

        if (schedHearings.isEmpty()) {
            throw new HearingScheduleException(NO_SCHED_HEARINGS_TO_LINK,
                    "There are no scheduled hearings to link/unlink");
        }

        Iterator it = schedHearings.iterator();
        while (it.hasNext()) {
            // Get the hearing, convert to a basic value and set the new
            // linked id
            // before update.
            ScheduledHearing schedHearing = (ScheduledHearing) it.next();
            ScheduledHearingBasicValue value = shMaintainer.getScheduledHearingBasicValue(schedHearing);
            value.setLinkedSHID(null);
            try {
                log.debug("Try to update sched hearing : " + value.toString());
                shMaintainer.update(value, userDisplayName);
            } catch (ObjectNotFoundException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingScheduleException hex = new HearingScheduleException(SCHED_HEARING_NOT_FOUND, ex.getMessage(),
                        ex);
                throw hex;
            }
        }

        log.debug("LinkHearingHelper.unlinkSchedHearings(" + "Collection hearings) finished");
    }

}