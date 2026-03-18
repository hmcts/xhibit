package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingheader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanNotFoundException;
import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.leg_rep.HearingLegalRepQuery;
import uk.gov.courtservice.xhibit.business.entities.ccinfo.CCInfoMaintainer;
import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfo;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepHome;
import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing_leg_rep.XhbHearingLegRepBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.LegalRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefLegalRepresentativeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;

/**
 * <p>
 * Title: Hearing Header Legal Represenaive Helper
 * </p>
 * <p>
 * Description: Methods to add, remove and retrieve legal reps from a schedule
 * hearing
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: HHLegalRepHelper.java,v 1.53 2015/12/01 21:56:37 atwells Exp $
 */
public class HHLegalRepHelper {
    // variables holding the maintainers and bisref controller references
    private BisRefControllerLocal bisRefDelegate;

    private ShLegRepMaintainer shLegRepMaintainer;

    private SchedHearingDefendantMaintainer shDefendantMaintainer;

    private static final Logger log = CSServices.getLogger(HHLegalRepHelper.class);

    /**
     * Constructor
     * 
     * @param bisRefDelegate -
     *            BisRefController
     */
    public HHLegalRepHelper(BisRefControllerLocal bisRefDelegate) {
        // set up the maintainers and bisref controller
        this.bisRefDelegate = bisRefDelegate;
        shLegRepMaintainer = new ShLegRepMaintainer();
        shDefendantMaintainer = new SchedHearingDefendantMaintainer();
    }

    /**
     * Get the details of the legal representatives for this schedule hearing
     * 
     * @param scheduledHearingId -
     *            the schedule hearing id
     * @return Collection - collection of LegalRepValue objects
     * @throws HearingScheduleException
     */
    public Collection getLegalReps(Integer scheduledHearingId, Integer caseId) throws HearingScheduleException {
        String methodName = "getLegalReps() - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        try {
            List legalRepDetails = new ArrayList();
            ShLegRepHome shLegRepHome = (ShLegRepHome) CSServices.getServiceLocator().getLocalHome(ShLegRepHome.class);

            // get the legal reps for this scheduled hearing
            Collection shLegReps = shLegRepHome.findByScheduledHearingId(scheduledHearingId);

            debug("*** The number of legal reps for this schedule hearing = " + shLegReps.size() + " ***");

            // iterate through the legal reps and get the details for each
            // one
            Iterator it = shLegReps.iterator();
            while (it.hasNext()) {
                // add legal rep details to the collection
                LegalRepValue legalRepValue = getShLegRepDetails((ShLegRep) it.next());
                
                //PR5673
                if (legalRepValue != null) {
                    legalRepDetails.add(legalRepValue);
                }
            }

            // add unrepresented defendants
            legalRepDetails.addAll(getUnrepresentedDefendants(scheduledHearingId, caseId));
            
            debug(methodName + "Exited OK");

            return legalRepDetails;

        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e.toString(), e);
        }
    }

    /**
     * Retrieves the details for a legal rep, creates a LegalRepValue object
     * 
     * @param shLegRep -
     *            the shLegRep VO
     * @return LegalRepValue
     */
    private LegalRepValue getShLegRepDetails(ShLegRep shLegRep) {
        String methodName = "getShLegRepDetails() - ";
        debug(methodName + "called with : shLegRepId =  " + shLegRep.getShLegRepId());

        String legalRole = shLegRep.getLegalRole();
        LegalRepValue legalRep = null;
        PersonValue defPerson = null;
        legalRep = new LegalRepValue(shLegRep.getShLegRepId(), shLegRep.getVersion());
        // get the Basiv value object from the maintainer and set the SH leg rep
        legalRep.setSHLegRep(shLegRepMaintainer.createBasicVOFromEntity(shLegRep));

        if (legalRole.equals(PersonValue.DEFENCE) || legalRole.equals(PersonValue.PROSECUTION)
                || legalRole.equals(PersonValue.RESPONDENT) || legalRole.equals(PersonValue.OBJECTOR)
                || legalRole.equals(PersonValue.THIRD_PARTY)) {
            // if the defendant is representing themselves then don't set a
            // LegalRepValue
            if (!shLegRep.getSolFirmOrRefLegalRep().equals(PersonValue.IN_PERSON) && !shLegRep.getSolFirmOrRefLegalRep().equals(PersonValue.NON_ATTENDANCE)) {
                // get the PersonValue (i.e name etc) and set the legal rep
                legalRep.setLegalRep(getLegalPerson(shLegRep));
            }

            // may still be CCInfo if in person
            CcInfo tmpCcInfo = shLegRep.getCcInfo();
            String tmpCcInfoStr = null;
            if (tmpCcInfo != null) {
                tmpCcInfoStr = tmpCcInfo.getCcInfoText();
            }
            /** @todo Should this be set to null or empty string? */
            legalRep.setCcInfo(tmpCcInfoStr);
        }

        // if the legal role is for the defence then get the defendant
        // if the defendant is representing themselves the set the legal rep to
        // the defendant
        if (legalRole.equals(PersonValue.DEFENCE)) {
            // get the defendant that this leg rep is representing
            defPerson = getDefendant(shLegRep.getSchedHearingDefendant());
            
            //PR5673
            if (defPerson != null) {
                legalRep.setDefendant(defPerson);
            } else {
                return null;
            }
            
            try {
                XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseIdValue(
                        shLegRep.getSchedHearingDefendant().getDefOnCaseID());
                legalRep.setLegallyAidedDefendant(true);
            } catch (XhbLegalAidOrderBeanNotFoundException notFound) {
                legalRep.setLegallyAidedDefendant(false);
            }
        }

        log.debug(methodName + "Exited OK");
        return legalRep;
    }

    /**
     * Get the defendant details
     * 
     * @param schedHearingDefendant -
     *            the scheduled hearing defendant id
     * @return PersonValue
     */
    private PersonValue getDefendant(SchedHearingDefendant schedHearingDefendant) {
        String methodName = "getDefendant() - ";
        debug(methodName + "called with : SchedHearDefId = " + schedHearingDefendant.getSchedHearDefId());

        // get the defendant
        XhbDefendantOnCase defOnCase = 
            XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(schedHearingDefendant.getDefOnCaseID());
        XhbDefendant defendant = defOnCase.getXhbDefendant();
        
        // PR5673
        try {
            // findByDefendantAndCase only returns values that are not obsolete.
            // Use this to check that the defendant has not had obs_ind set to "Y".
            XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(
                    defOnCase.getDefendantId(), defOnCase.getCaseId());
        } catch (XhbDefendantOnCaseBeanNotFoundException notFound) {
            return null;
        }

        // get the name of the defendant
        StringBuffer fullName = new StringBuffer("");
        fullName.append(substitueForNullValue(defendant.getFirstName(), "", " "));
        fullName.append(substitueForNullValue(defendant.getMiddleName(), "", " "));
        if (fullName.length() < 1) {
            fullName.append(substitueForNullValue(defendant.getInitials(), "", " "));
        }
        fullName.append(substitueForNullValue(defendant.getSurname(), "", ""));

        // create a person value representing this defendant
        PersonValue defPerson = new PersonValue(schedHearingDefendant.getSchedHearDefId(), schedHearingDefendant
                .getVersion(), defendant.getDefendantId(), defendant.getVersion(), fullName.toString(),
                PersonValue.DEFENDANT);
        defPerson.setFirstName(defendant.getFirstName());
        defPerson.setMiddleName(defendant.getMiddleName());
        defPerson.setSurname(defendant.getSurname());

        debug(methodName + "Exited OK");

        // return the PersonValue
        return defPerson;
    }

    /**
     * Gets the legal person details
     * 
     * @param shLegRep -
     *            the shLegRep Value object
     * @return PersonValue
     */
    private PersonValue getLegalPerson(ShLegRep shLegRep) {
        String methodName = "getLegalPerson - ";
        debug(methodName + "called with : shLegRepId =  " + shLegRep.getShLegRepId());
        PersonValue person = null;

        try {
            // Use the bisRefController to find the legalRep
            RefLegalRepresentativeCriteria refLegalRepCriteria = new RefLegalRepresentativeCriteria();
            refLegalRepCriteria.setPrimaryKey(shLegRep.getRefLegalRepId());
            Collection legalReps = bisRefDelegate.findLegalRepresentatives(refLegalRepCriteria);

            // if the ref legal rep has been found
            if (!legalReps.isEmpty()) {
                Iterator it = legalReps.iterator();
                // only expecting one
                RefLegalRepresentativeBasicValue refLegalRepresentativeValue = (RefLegalRepresentativeBasicValue) it
                        .next();

                // set up the legal reps name
                StringBuffer fullName = null;

                // check that the solicitor can be found in the
                // xhb_ref_solicitor table, if not then it must be
                // in the xhb_ref_advocate table

                SolicitorBasicValue solBVO = null;

                if (refLegalRepresentativeValue.getLegalRepType().equals(PersonValue.SOLICITOR)) {
                    solBVO = findSolicitorRecord(refLegalRepresentativeValue);
                }

                if (solBVO != null) {
                    fullName = getSolicitorName(solBVO);
                } else {
                    fullName = new StringBuffer("");
                    fullName.append(substitueForNullValue(refLegalRepresentativeValue.getFirstName(), "", " "));
                    fullName.append(substitueForNullValue(refLegalRepresentativeValue.getMiddleName(), "", " "));
                    if (fullName.length() < 1) {
                        fullName.append(substitueForNullValue(refLegalRepresentativeValue.getInitials(), "", " "));
                    }
                    fullName.append(substitueForNullValue(refLegalRepresentativeValue.getSurname(), "", ""));
                }

                // create the PersonValue object
                person = new PersonValue(shLegRep.getShLegRepId(), shLegRep.getVersion(), refLegalRepresentativeValue
                        .getId(), refLegalRepresentativeValue.getVersion(), fullName.toString(), shLegRep
                        .getLegalRole());
            } else {
                debug(methodName + "Did not find legal rep  with legal rep Id = " + shLegRep.getShLegRepId());
                throw new CSConfigurationException("Did not find legal rep Id = " + shLegRep.getShLegRepId());
            }
        } catch (BisRefControllerException e) {
            debug("*** BisRefController Exception has been caught ***");
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new CSConfigurationException(e.toString(), e);
        }

        debug(methodName + "Exited OK");
        // return the PersonValue for this refLegalRep
        return person;

    }

    /**
     * Utility method to add a collection of legal reps. Underlying call to
     * addLegalRep
     * 
     * @param scheduledHearingId -
     *            the schedule hearing id
     * @param shLegRepBasicValues -
     *            Collection of SHLegRepBasicValue
     * @throws HearingScheduleException
     */
    public void addLegalReps(Integer scheduledHearingId, Collection shLegRepBasicValues, String userDisplayName)
            throws HearingScheduleException {
        String methodName = "addLegalReps - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        // iterate through the collection and add each legal rep
        Iterator it = shLegRepBasicValues.iterator();
        while (it.hasNext()) {
            SHLegRepBasicValue legRep = (SHLegRepBasicValue) it.next();
            // add the legal rep
            addLegalRep(legRep, userDisplayName);
        }

        debug(methodName + "Exited OK");
    }

    /**
     * Utility method to remove a collection of legal reps. Underlying call to
     * removeLegalRep
     * 
     * @param shLegRepBasicValues
     *            Collection of SHLegRepBasicValue
     * @throws HearingScheduleException
     */
    public void removeLegalReps(Collection shLegRepBasicValues) throws HearingScheduleException {
        String methodName = "removeLegalReps - ";
        debug(methodName + "called");

        // iterate through the collection and remove each legal rep
        Iterator it = shLegRepBasicValues.iterator();
        while (it.hasNext()) {
            SHLegRepBasicValue legRep = (SHLegRepBasicValue) it.next();
            // remove the legal rep
            removeLegalRep(legRep);
        }

        debug(methodName + "Exited OK");
    }

    /**
     * Utility method to update a collection of legal reps. Underlying call to
     * updateLegalRep.
     * 
     * @param shLegRepBasicValues
     *            Collection of SHLegRepBasicValue
     * @throws HearingScheduleException
     */
    public void updateLegalReps(Collection shLegRepBasicValues, String userDisplayName) throws HearingScheduleException {
        String methodName = "updateLegalReps - ";
        debug(methodName + "called");

        // iterate through the collection and remove each legal rep
        Iterator it = shLegRepBasicValues.iterator();
        while (it.hasNext()) {
            SHLegRepBasicValue legRep = (SHLegRepBasicValue) it.next();
            // update the legal rep
            updateLegalRep(legRep, userDisplayName);
        }

        debug(methodName + "Exited OK");
    }
    
    /**
     * BUG-FIX : 52764 & 52761
     * 
     * This method is for when adding legal representatives to a defendant, when
     * the scheduledhearing defendant id is NOT know. The defendantID is
     * TEMPORARY populated as the schedHearDefendantID (from the GUI or
     * elsewhere). This method will look up the right hearingScheduleDefID and
     * set it. This method will take the defendantID (that is know and temporary
     * set as the schedHearDefID) and the hearingScheduleID (that is also known)
     * and search for the scheduledHearingDefendant. When we have this value we
     * will set the rigth id and call addLegalRep(SHLegRepBasicValue legRep).
     * 
     * @param scheduledHearingId -
     *            schedulehearingid
     * @param shLegRepBasicValues -
     *            Collection of shLegRepBasicValues
     * @param caseID
     *            the case id
     */
    public void addLegalRepValues(Integer scheduledHearingId, Integer caseID, Collection shLegRepBasicValues, String userDisplayName) {
        debug("Method addLegalRepValues(" + scheduledHearingId + ", " + caseID + ", " + shLegRepBasicValues.size()
                + ") called");

        if (scheduledHearingId != null && caseID != null && shLegRepBasicValues != null) {

            Iterator it = shLegRepBasicValues.iterator();

            while (it.hasNext()) {
                SHLegRepBasicValue legRep = (SHLegRepBasicValue) it.next();
                debug("LegRep before changing it : " + legRep.toString());

                // the set scheduledhearingDefID from the gui is not the
                // right id, it is the temporary defendantID.
                Integer defendantID = legRep.getSchedHearDefID();

                Integer defOnCaseID = this.getDefendantOnCaseID(defendantID, caseID);

                // find the right scheduledHearingDefendant with given
                // defendantOnCase
                // and scheudledHearingID
                SchedHearingDefendant schedHearingDef = null;
                try {
                    schedHearingDef = shDefendantMaintainer.findBySchedHearingIdAndDefOnCaseId(scheduledHearingId,
                            defOnCaseID);
                    debug("Found a SchedHearingDefendant with id : " + schedHearingDef.getSchedHearDefId());
                } catch (ObjectNotFoundException onfe) {
                    // could not find the scheduledhearing defendant.
                    CSServices.getDefaultErrorHandler().handleError(onfe, this.getClass());
                    throw new EJBException(onfe);
                }

                if (schedHearingDef != null) {
                    // get the right scheduledhearingdefendantid and
                    // "re-set" the legRep
                    // with the right schedHearingDefID
                    legRep.setSchedHearDefID(schedHearingDef.getSchedHearDefId());
                    debug("The legRep after changed the id : " + legRep.toString());

                    // Finally - call the addLegalRep that will create an
                    // entry in the database.
                    addLegalRep(legRep, userDisplayName);

                    debug("Added the legRep successfully");
                }
            }
            debug("Method addLegalRepValues() finished");
        }
    }

    /**
     * This will search for a defendantOnCaseID by defendantID and caseID
     * 
     * @param caseID -
     *            the case id
     * @param defendantID -
     *            the defedant id
     * @return Integer - the defendantOnCaseID
     */
    private Integer getDefendantOnCaseID(Integer defendantID, Integer caseID) {
        return XhbDefendantOnCaseBeanHelper2.findByDefendantAndCase(defendantID, caseID).getDefendantOnCaseId();
    }

    /**
     * Adds the legal rep to the schedule hearing.
     * 
     * @param legRep -
     *            a SHLegRepBasicValue object
     */
    public void addLegalRep(SHLegRepBasicValue legRep, String userDisplayName) {
        String methodName = "addLegalRep - ";
        debug(methodName + "called with : shLegRepID =  " + legRep.getId());
        debug(methodName + "legRep:" + legRep.toString());
        debug(methodName + "SH ID:" + legRep.getScheduledHearingID());
        debug(methodName + "RefLegalRepID:" + legRep.getRefLegalRepID());

        /**
         * @todo Do we need to use XHB entity layer here for SH Legal Rep code
         *       below?
         */
        try {
            // create Scheduled Hearing Legal Rep
            ScheduledHearingMaintainer chm = new ScheduledHearingMaintainer();
            ScheduledHearing schedH = chm.findByPK(legRep.getScheduledHearingID());
            
            try {
                Collection existingLegalReps = 
                    this.shLegRepMaintainer.findByScheduledHearingID(legRep.getScheduledHearingID());
                
                Iterator iter = existingLegalReps.iterator();
                while (iter.hasNext()) {
                    ShLegRep item = (ShLegRep)iter.next();
                    
                    Integer itemSchedHearDefId = item.getSchedHearDefId();
                    Integer legRepSchedHearDefID = legRep.getSchedHearDefID();
                    
                    if (item.getRefLegalRepId() != null 
                            && item.getRefLegalRepId().equals(legRep.getRefLegalRepID())
                            && itemSchedHearDefId != null
                            && itemSchedHearDefId.equals(legRepSchedHearDefID)) {
                        debug("shLegRepID is already signed in the scheduled hearing");
                        // It is debatable whether we should throw an exception here.
                        // If we throw an exception then we treat this as an error
                        // and need to report it to the user in both the thin client
                        // and thick client.  However it is fairly harmless action
                        // doing a double sign-in. 
                        return;
                    }
                }
            } catch (ObjectNotFoundException notFound) {
                // Ok to find nothing
            }
            
            ShLegRep shl = (ShLegRep) this.shLegRepMaintainer.create(legRep, userDisplayName);
            // set ccinfo
            Integer ccInfoId = legRep.getCcInfoID();
            if (ccInfoId != null) {
                CCInfoMaintainer ccM = new CCInfoMaintainer();
                CcInfo ccInfo = ccM.findByPrimaryKey(legRep.getCcInfoID());
                shl.setCcInfo(ccInfo);
            }

            // set scheduledHearing
            shl.setScheduledHearing(schedH);

            // setscheduledHearingDefedant
            Integer shdId = legRep.getSchedHearDefID();
            if (shdId != null) {
                SchedHearingDefendant shd = shDefendantMaintainer.findByPrimaryKey(shdId);
                shl.setSchedHearingDefendant(shd);
            }

            // HearingLegalRep only required if refLegalRepId exists i.e. we
            // have a solicitor or a barrister.
            // The refLegalRepId will be null for In Person
            if (legRep.getRefLegalRepID() != null) {
                maintainHearingLegRep(legRep, schedH);
            }
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }
        debug(methodName + "Exited OK");
    }

    /**
     * maintain the Hearing Legal Representative
     * 
     * @param legRep
     *            a SHLegRepBasicValue
     * @param schedH
     *            a ScheduledHearing
     */
    private void maintainHearingLegRep(SHLegRepBasicValue legRep, ScheduledHearing schedH) {
        String methodName = "maintainHearinglegRep - ";
        debug(methodName + "called with : shLegRepID =  " + legRep.getId());

        // initialise Hearing Leg specific variables
        Date notBeforeDate = resetTime(schedH.getNotBeforeTime());
        Date notBeforeTime = schedH.getNotBeforeTime();
        Date dayBeforeNotBeforeDate = calculateDayBefore(notBeforeDate);
        Date dayAfterNotBeforeDate = calculateDayAfter(notBeforeDate);
        XhbHearingLegRepBasicValue legRepBV;

        // Set Hearing Legal Rep details
        HearingLegalRepQuery hlrQuery = new HearingLegalRepQuery();
        XhbHearingLegRepBasicValue[] legRepsBVs = hlrQuery.getHearingLegRepEntries(schedH.getHearingId(), legRep
                .getRefLegalRepID(), notBeforeDate);

        if (legRepsBVs != null && legRepsBVs.length > 0) {
            legRepBV = legRepsBVs[0];
            Date loopStartDate = resetTime(legRepBV.getStartDate());
            Date loopEndDate = resetTime(legRepBV.getEndDate());

            // If the record has an end date equivalent to yesterday,
            // then reset the end date to todays date
            // Otherwise if the record has a start date equivalent to
            // tomorrow,
            // then reset the start date to todays date.
            // If neither of these conditions apply, then the current
            // notBeforeDate
            // lies within the record's start and end date so do nothing.
            if (loopEndDate.compareTo(dayBeforeNotBeforeDate) == 0) {
                updateHearingLegRepEndDate(legRepBV, notBeforeTime);
            } else if (loopStartDate.compareTo(dayAfterNotBeforeDate) == 0) {
                updateHearingLegRepStartDate(legRepBV, notBeforeTime);
            }
        } else {
            // create altogether new XhbHearingLegRepBasicValue based input
            // to method
            legRepBV = new XhbHearingLegRepBasicValue();
            legRepBV.setHearingId(schedH.getHearingId());
            legRepBV.setRefLegalRepId(legRep.getRefLegalRepID());
            createHearingLegRep(legRepBV, notBeforeTime, notBeforeTime);
        }
        debug(methodName + "Exited OK");
    }

    /**
     * Updates the subInst and substitutedRefLegalRepId fields of a legalRep
     *
     * @param legRep -
     *            a SHLegRepBasicValue object
     */
    public void updateLegalRep(SHLegRepBasicValue legRep, String userDisplayName) {
        String methodName = "updateLegalRep - ";
        debug(methodName + "called with : shLegRepID =  " + legRep.getId());
        
        try {
            shLegRepMaintainer.update(legRep, userDisplayName);
        } catch (ObjectNotFoundException e) {
            // if this object is not found it means someone else had deleted
            // it since
            // it was read
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new OptimisticLockException(e);
        }
        debug(methodName + "Exited OK");
    }
    
    
    /**
     * Removes the legal rep from the schedule hearing.
     * 
     * @param legRep -
     *            a SHLegRepBasicValue object
     */
    public void removeLegalRep(SHLegRepBasicValue legRep) {
        String methodName = "removeLegalRep - ";
        debug(methodName + "called with : shLegRepID =  " + legRep.getId());

        try {
            shLegRepMaintainer.delete(legRep.getId(), legRep.getVersion());

            debug(methodName + "shLegRepID DELETED");

            // Check if there is a legal representative associated with this
            // record
            if (legRep.getRefLegalRepID() == null) {
                // When a defendant represents themselves "In Person", there
                // will not
                // be a legal representative associated with the record.
                log.debug("There is no legal representative associated with the record");
            } else {
                // Delete Hearing Legal Rep details
                ScheduledHearingMaintainer chm = new ScheduledHearingMaintainer();
                ScheduledHearing schedH = chm.findByPK(legRep.getScheduledHearingID());

                Date notBeforeTime = schedH.getNotBeforeTime();
                Date notBeforeDate = resetTime(schedH.getNotBeforeTime());

                // Check if Hearing Rep is on another Scheduled Hearing on the
                // same day
                // Use RefLegRepID and notBeforeDate
                Collection hearingRepUseCol = shLegRepMaintainer.findByRefLegRepIDForDateHearingID(legRep
                        .getRefLegalRepID(), new Timestamp(notBeforeDate.getTime()), new Timestamp(calculateEndOfDay(
                        notBeforeDate).getTime()), schedH.getHearingId());

                if (hearingRepUseCol == null || hearingRepUseCol.size() == 0) {
                    debug(methodName + " hearingRepUseCol size == 0 so OK to DELETE!!");
                    // Hearing Rep is not on another Scheduled Hearing on
                    // the same day
                    HearingLegalRepQuery hlrQuery = new HearingLegalRepQuery();
                    XhbHearingLegRepBasicValue[] legRepsBVs = hlrQuery.getHearingLegRepEntriesForRemoval(schedH
                            .getHearingId(), legRep.getRefLegalRepID(), notBeforeDate);

                    if (legRepsBVs.length == 0) {
                        // Minor error - don't do anything and don't throw an
                        // exception
                    } else if (legRepsBVs.length == 1) {
                        debug(methodName + "legRepsCol NOT NULL: " + legRepsBVs.length);

                        // Variables specifc to Hearing Leg Rep Removal
                        Date dayBeforeNotBeforeDate = calculateDayBefore(notBeforeTime);
                        Date dayAfterNotBeforeDate = calculateDayAfter(notBeforeTime);
                        Date originalEndDate;

                        XhbHearingLegRepBasicValue legRepBV = legRepsBVs[0];
                        Date loopStartDate = resetTime(legRepBV.getStartDate());
                        Date loopEndDate = resetTime(legRepBV.getEndDate());

                        // If the record's startDate and endDate match,
                        // then it is safe to delete the record
                        // Otherwise if the record's startDate matches the
                        // notBeforeDate,
                        // then update the record's startDate to "tomorrow's"
                        // date
                        // Otherwise if the record's endDate matches the
                        // notBeforeDate,
                        // then update the record's endDate to "yesterday's"
                        // date
                        // Otherwise update the record's endDate to
                        // "yesterday's" date
                        // and create a new record with the start and end dates
                        // set to
                        // "tomorrow" and the original record's endDate
                        // respectively
                        if (loopStartDate.compareTo(loopEndDate) == 0) {
                            XhbHearingLegRepBeanHelper2.remove(legRepBV);
                        } else if (loopStartDate.compareTo(notBeforeDate) == 0) {
                            updateHearingLegRepStartDate(legRepBV, dayAfterNotBeforeDate);
                        } else if (loopEndDate.compareTo(notBeforeDate) == 0) {
                            updateHearingLegRepEndDate(legRepBV, dayBeforeNotBeforeDate);
                        } else {
                            originalEndDate = legRepBV.getEndDate();
                            updateHearingLegRepEndDate(legRepBV, dayBeforeNotBeforeDate);
                            createHearingLegRep(legRepBV, dayAfterNotBeforeDate, originalEndDate);
                        }
                    } else {
                        // This is unexpected and implies that a data corruption
                        // exists
                        // for the hearingID/legRepID/notBeforeDate combination
                        throw new CSUnrecoverableException(
                                new Message("counselSignIn.data.corruption", new Object[] {}),
                                "Too many rows were returned for the following hearingID/legRepID/notBeforeDate:"
                                        + schedH.getHearingId() + "/" + legRep.getRefLegalRepID() + "/" + notBeforeDate);

                    }
                } else {
                    // do not deleted Hearing Rep record
                    debug(methodName + " hearingRepUseCol size == " + hearingRepUseCol.size() + " so NOT DELETING!!");
                }
            }
        } catch (ObjectNotFoundException e) {
            // if this object is not found it means someone else had deleted
            // it since
            // it was read
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new OptimisticLockException(e);
        }
        debug(methodName + "Exited OK");
    }

    /**
     * Gets defendants that are not represented.
     * 
     * @param scheduledHearingId -
     *            the scheduled hearing id
     * @return - Collection of LegRepValues
     */
    private Collection getUnrepresentedDefendants(Integer scheduledHearingId, Integer caseId) {

        String methodName = "getUnrepresentedDefendants - ";
        debug(methodName + "called with : scheduledHearingId =  " + scheduledHearingId);

        List unrepresentedDefendants = new ArrayList();
        Collection schedHearingDefendants;

        try {
            schedHearingDefendants = shDefendantMaintainer.findUnrepresentedDefendants(scheduledHearingId);
        } catch (ObjectNotFoundException e) {
            // if nothing was found an empty collection would be returned,
            // this must
            // be an unexpected exception
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new EJBException(e);
        }

        // iterate over the collection and create a legRepValue for each
        // unrepresented defendant
        // only setting the defendant details.
        Iterator it = schedHearingDefendants.iterator();
        while (it.hasNext()) {
            // get the ScheduleHearingDefendant
            SchedHearingDefendant shDefendant = (SchedHearingDefendant) it.next();
            // c=Create a LegRepvalue
            LegalRepValue legRepVal = new LegalRepValue();
            // set the defendant details
            PersonValue defendant = getDefendant(shDefendant);
            
            //PR5673
            if (defendant != null) {
                legRepVal.setDefendant(defendant);
            } else {
                debug(methodName + " defendant null");
                continue;
            }
            
            // add LegrepValue to the collection
            
            try {
                XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseIdValue(shDefendant.getDefOnCaseID());
                legRepVal.setLegallyAidedDefendant(true);
            } catch (XhbLegalAidOrderBeanNotFoundException notFound) {
                legRepVal.setLegallyAidedDefendant(false);
            }
            
            unrepresentedDefendants.add(legRepVal);
        }
        
        // For B cases this will return null as there is no sched hearing defendants setup
        if (schedHearingDefendants.size() == 0) {
            XhbCase cs = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
            if (cs.getCaseType().equals("B")) {
                LegalRepValue legRepVal = new LegalRepValue();
                PersonValue bCaseDef = new PersonValue();
                // Is there a defendant id on this case (if there had been a saved B order then there may well be)
                Collection allDefsOnCase = XhbDefendantOnCaseBeanHelper2.findByCaseId(caseId);
                Iterator i = allDefsOnCase.iterator();
                boolean defExists = false;
                while (i.hasNext()) {
                    XhbDefendantOnCase defOnCase = (XhbDefendantOnCase) i.next();
                    XhbDefendant thisDef = defOnCase.getXhbDefendant();
                    if (thisDef != null) {
                        if (thisDef.getDefendantId() != null) {
                            defExists = true;
                            
                            StringBuffer fullName = new StringBuffer("");
    
                            fullName.append(substitueForNullValue(thisDef.getFirstName(), "", " "));
                            fullName.append(substitueForNullValue(thisDef.getMiddleName(), "", " "));
                            if (fullName.length() < 1) {
                                fullName.append(substitueForNullValue(thisDef.getInitials(), "", " "));
                            }
                            fullName.append(substitueForNullValue(thisDef.getSurname(), "", ""));
                            bCaseDef = new PersonValue(null, null, thisDef.getDefendantId(), thisDef.getVersion(), fullName.toString(), PersonValue.DEFENDANT);
                            bCaseDef.setFirstName(thisDef.getFirstName());
                            bCaseDef.setMiddleName(thisDef.getMiddleName());
                            bCaseDef.setSurname(thisDef.getSurname());
                        }
                    }
                }
                
                if (!defExists) {
                    bCaseDef = new PersonValue(null, null, null, null, cs.getCaseTitle(), PersonValue.DEFENDANT);
                }
                legRepVal.setDefendant(bCaseDef);
                unrepresentedDefendants.add(legRepVal);
            }
        }
        

        debug(methodName + "Exited OK " + unrepresentedDefendants.size());
        return unrepresentedDefendants;
    }

    /**
     * Retreives the Solicitors name.
     * 
     * @param solValue -
     *            a SolicitorBasicValue
     * @return StringBuffer - the solicitors name
     */
    private StringBuffer getSolicitorName(SolicitorBasicValue solValue) {
        String methodName = "getSolicitorName - ";
        debug(methodName + "called with : refLegalRepresentativeValue =  " + solValue);

        StringBuffer fullName = new StringBuffer("");

        fullName.append(substitueForNullValue(solValue.getFirstName(), "", " "));
        fullName.append(substitueForNullValue(solValue.getMiddleName(), "", " "));
        if (fullName.length() < 1) {
            fullName.append(substitueForNullValue(solValue.getInitials(), "", " "));
        }
        fullName.append(substitueForNullValue(solValue.getSurname(), "", ""));

        if (fullName.length() < 1) {
            fullName.append(solValue.getCrestSolicitorName());
        }
        debug(methodName + "Exited OK");
        return fullName;

    }

    /**
     * Method to find out whether the solicitor exists in the xhb_ref_solicitor
     * table.
     * 
     * @param refLegalRepresentativeValue -
     *            a RefLegalRepresentativeBasicValue
     * @return - If found returns the SolicitorBasicValue else it returns null
     */
    private SolicitorBasicValue findSolicitorRecord(RefLegalRepresentativeBasicValue refLegalRepresentativeValue) {
        String methodName = "findSolicitorRecord - ";
        debug(methodName + "called with : refLegalRepresentativeValue =  " + refLegalRepresentativeValue);

        SolicitorBasicValue solValue = null;
        try {
            SolicitorCriteria solCriteria = new SolicitorCriteria();
            // we have to add "" to the ID as the method takes a string
            // rather than an Integer
            solCriteria.setRefLegalRepId(refLegalRepresentativeValue.getId() + "");

            // find the solicitor
            Collection sols = bisRefDelegate.findSolicitors(solCriteria);

            if (!sols.isEmpty()) {
                Iterator it = sols.iterator();
                // only expecting one back
                solValue = (SolicitorBasicValue) it.next();
            } else {
                debug("*** Have not been able to find solicitor in xhb_solicitor table ***");
                debug("*** Returning empty SolicitorBasicValue object, and try and get from advocate ***");
                solValue = null;
            }
        } catch (BisRefControllerException e) {
            debug("*** BisRefController Exception has been caught ***");
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new CSConfigurationException(e.toString(), e);
        }

        debug(methodName + "Exited OK");
        return solValue;
    }

    /**
     * Private method for debug. This method checks that debugging is on, If so
     * then the message passed in is sent to the Logger.debug method.
     * 
     * @param mes
     *            String
     */
    private void debug(String mes) {
        if (log.isDebugEnabled()) {
            log.debug(mes);
        }
    }

    /**
     * Private method that will check if the value is null, if so it will return
     * the substitition value passed in, else it will return the origianl value
     * 
     * @param value -
     *            value to test for null
     * @param sub -
     *            the substiution value is value is null
     * @param extraPostChars -
     *            any extra characters to append when returning value only
     * @return - value if value is not null and sub if value is null
     */
    private String substitueForNullValue(String value, String sub, String extraPostChars) {
        return value == null ? sub : value + extraPostChars;
    }

    /**
     * create new Hearing rep with start date and end date equal to parameters
     * passed in. Note: the XhbHearingLegRepBasicValue passed in will be
     * modified although this is discarded after helper create() method is
     * called.
     * 
     * @param legRepBV
     * @param startDate
     * @param endDateTime
     * @return
     */
    private XhbHearingLegRepBasicValue createHearingLegRep(XhbHearingLegRepBasicValue legRepBV, Date startDate,
            Date endDate) {
        debug("createHearingLegRep. StartDate: " + startDate + ", EndDate: " + endDate);
        legRepBV.setStartDate(startDate);
        legRepBV.setEndDate(endDate);
        return XhbHearingLegRepBeanHelper2.create(legRepBV);
    }

    /**
     * update Hearing rep with end date equal to passed in Date Note: the
     * XhbHearingLegRepBasicValue passed in will be modified although this is
     * discarded after helper update() method is called.
     * 
     * @param legRepBV
     * @param notBeforeTime
     * @return
     */
    private XhbHearingLegRepBasicValue updateHearingLegRepEndDate(XhbHearingLegRepBasicValue legRepBV, Date date) {
        debug("updateHearingLegRepEndDate : " + date);
        legRepBV.setEndDate(date);
        return XhbHearingLegRepBeanHelper2.update(legRepBV);
    }

    /**
     * update Hearing rep with start date equal to passed in Date Note: the
     * XhbHearingLegRepBasicValue passed in will be modified although this is
     * discarded after helper update() method is called.
     * 
     * @param legRepBV
     * @param notBeforeTime
     * @return
     * @throws ObjectNotFoundException
     */
    private XhbHearingLegRepBasicValue updateHearingLegRepStartDate(XhbHearingLegRepBasicValue legRepBV, Date date) {
        debug("updateHearingLegRepStartDate : " + date);
        legRepBV.setStartDate(date);
        return XhbHearingLegRepBeanHelper2.update(legRepBV);
    }

    /**
     * Private class that contains a method to compare the startDate of 2
     * XhbHearingLegRepBasicValue passed in
     */
    static final Comparator EVENT_DATE_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            XhbHearingLegRepBasicValue entry1 = (XhbHearingLegRepBasicValue) o1;
            XhbHearingLegRepBasicValue entry2 = (XhbHearingLegRepBasicValue) o2;

            // Timestamp implements Comparable so fine to do this, we want
            // the results in reverse chronological order so the latest
            // event
            // is first
            return entry2.getStartDate().compareTo(entry1.getStartDate());
        }
    };

    /**
     * Private method that will use the date passed in to calculate a date for
     * the day before.
     * 
     * @param date -
     *            Date
     * @return - Date
     */
    private Date calculateDayBefore(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return resetTime(cal);
    }

    /**
     * Private method that will use the date passed in to calculate a date for
     * the day after.
     * 
     * @param date -
     *            Date
     * @return - Date
     */
    private Date calculateDayAfter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, +1);
        return resetTime(cal);
    }

    /**
     * Private method that will set the date of the argument passed in to the
     * start of the day (hour, minutes, seconds, milliseconds)
     * 
     * @param inDate -
     *            Date
     * @return - Date
     */
    private Date resetTime(Date inDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        return resetTime(cal);
    }

    /**
     * Private method that will set the date of the argument passed in to the
     * start of the day (hour, minutes, seconds, milliseconds)
     * 
     * @param inCal -
     *            Date
     * @return - Date
     */
    private Date resetTime(Calendar inCal) {
        inCal.set(Calendar.HOUR_OF_DAY, 0);
        inCal.set(Calendar.MINUTE, 0);
        inCal.set(Calendar.SECOND, 0);
        inCal.set(Calendar.MILLISECOND, 0);
        return inCal.getTime();
    }

    /**
     * Private method that will set the date of the argument passed in to the
     * end of the day (hour, minutes, seconds, milliseconds)
     * 
     * @param inDate -
     *            Date
     * @return - Date
     */
    private Date calculateEndOfDay(Date inDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(inDate);
        return calculateEndOfDay(cal);
    }

    /**
     * Private method that will set the date of the argument passed in to the
     * end of the day (hour, minutes, seconds, milliseconds)
     * 
     * @param inCal -
     *            Calendar
     * @return - Date
     */
    private Date calculateEndOfDay(Calendar inCal) {
        inCal.set(Calendar.HOUR_OF_DAY, 23);
        inCal.set(Calendar.MINUTE, 59);
        inCal.set(Calendar.SECOND, 59);
        inCal.set(Calendar.MILLISECOND, 999);
        return inCal.getTime();
    }
}