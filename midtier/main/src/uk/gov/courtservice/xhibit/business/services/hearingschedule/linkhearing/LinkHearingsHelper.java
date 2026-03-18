package uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
import uk.gov.courtservice.xhibit.business.entities.linkedhearing.LinkedHearing;
import uk.gov.courtservice.xhibit.business.entities.linkedhearing.LinkedHearingMaintainer;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerException;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocal;
import uk.gov.courtservice.xhibit.business.services.caze.CaseControllerLocalHome;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordExportALockedException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordExportInProgressException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordHearingAlreadyExportedException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordStatusHelper;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocal;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.LinkedHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.HearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefHearingTypeCriteria;

/**
 * <p>
 * Title: LinkHearingsHelper
 * </p>
 * <p>
 * Description: This class is for link and un-link hearings. There will be a
 * lead hearing that all the other ones should be linked with. They will all get
 * the same linkedHearingID.
 * 
 * The class also contains a supporting method to search for a case and return
 * the case with all the listed hearings except from the ones that have already
 * been linked.
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 * 
 * <p>
 * 20/06/03 - MH - Added check to make sure a hearing is not linked to itself.
 * </p>
 */
public class LinkHearingsHelper {

    // Strings to be used when throwing exceptions
    static final String HEARING_NOT_FOUND = "hearingschedule.linkhearing.hearing_not_found";

    static final String NO_HEARINGS_TO_LINK = "hearingschedule.linkhearing.no_hearings_to_link_or_unlink";

    static final String LINKED_HEARING_NOT_CREATED = "hearingschedule.linkhearing.linked_hearing_not_created";

    static final String CASE_NOT_FOUND = "hearingschedule.linkhearing.case_not_found";

    static final String INVALID_CASE = "hearingschedule.linkhearing.invalid_case";

    static final String HEARING_TYPE_NOT_FOUND = "hearingschedule.linkhearing.hearing_type_no_found";

    static final String HEARING_CANNOT_LINK_TO_ITSELF = "hearingschedule.linkhearing.cannot_link_to_itself";

    private static Logger log = CSServices.getLogger(LinkHearingsHelper.class);

    private HearingMaintainer hearingMaintainer;

    /* CaseController to be used when searching for cases */
    private CaseControllerLocal caseController;

    /* BisRefController to be used to search for reference data */
    private BisRefControllerLocal bisRefController;

    /* CaseMaintainer to be used to search for cases */
    private CaseMaintainer caseMaintainer;

    /* To get ExportAValue */
    private HearingRecordStatusHelper hearingRecordStatusHelper;

    /**
     * Default constructor that intantiate the HearingMaintainer
     */
    public LinkHearingsHelper() {
        hearingMaintainer = new HearingMaintainer();
    }

    /**
     * This method will link the hearings passed in. They will all get the same
     * linked hearing id as the lead hearing. If the lead hearing already has a
     * linked id then the others will get the same and also the ones they are
     * linked with.
     * 
     * @param leadHearingID
     * @param hearingIDs
     * @throws HearingScheduleException
     */
    public void linkHearings(Integer leadHearingID, Collection hearingIDs, String userDisplayName) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.linkHearings(Integer leadHearingID, " + "Collection hearingIDs) called");

        if (hearingIDs.isEmpty()) {
            throw new HearingScheduleException(NO_HEARINGS_TO_LINK,
                    "There are no hearings to link with the leadhearing");
        }
        log.debug("The lead hearing id is: " + leadHearingID);

        // Vector to be used with hearings that should be updated.
        Vector hearingsToUpdate = new Vector();

        // get the lead hearing entity and the linked hearing id
        Hearing leadHearing = this.findHearing(leadHearingID);

        // Get the basic value
        HearingBasicValue leadBasicValue = hearingMaintainer.getHearingBasicValue(leadHearing);

        // check if has been exported already or in progress.
        this.checkIfExported(leadHearing);

        /*
         * ExportAValue exportAValue = null; try { exportAValue =
         * this.getHearingRecordStatusHelper().getExportValue(leadBasicValue); }
         * catch(HearingRecordException ex) {
         * CSServices.getDefaultErrorHandler().handleError(ex, getClass(),
         * ex.toString()); throw new
         * HearingScheduleException(ex.getUserMessageAsMessage().getKey(),ex.getMessage(),ex); }
         * this.checkExportState(exportAValue);
         */

        Integer leadLinkedHearingID = leadHearing.getLinkedHearingId();

        // if the lead linked hasn't been linked previously we need to create a
        // new linked id.
        if (leadLinkedHearingID == null) {
            // Since the link id is null we need to create a new linked
            // hearing id
            leadLinkedHearingID = this.createNewLinkedHearingID(userDisplayName);
            hearingsToUpdate.addElement(leadHearing);
        }

        // this will be used to make sure we only add hearings with the same
        // linkage
        // once. It will store the unique linkids that are different to the
        // lead's
        HashSet set = new HashSet();

        // loop through all hearings and check if they have been linked
        // previously
        // if so get the previous linkage and link them as well.
        Iterator it = hearingIDs.iterator();
        while (it.hasNext()) {
            Integer hearingId = (Integer) it.next();
            log.debug("Will process hearing with id: " + hearingId);

            // If the hearing (to be linked) passed in is the same as the
            // lead hearing's
            // id the fail the linkage since a hearing cannot be linked to
            // itself
            if (leadBasicValue.getId().intValue() == hearingId.intValue()) {
                log.debug("The lead hearing id is : " + leadBasicValue.getId() + " and passed in hearingid is : "
                        + hearingId + " The Ids cannot be the same so throw an exception");
                throw new HearingScheduleException(HEARING_CANNOT_LINK_TO_ITSELF, "Hearing cannot be linked to itself");
            }

            Hearing hearing = this.findHearing(hearingId);

            // Get the basic value
            HearingBasicValue basicValue = hearingMaintainer.getHearingBasicValue(hearing);

            // check if has been exported already or in progress.
            // this.checkIfExported(leadHearing);
            this.checkIfExported(hearing);

            Integer prevLinkedID = hearing.getLinkedHearingId();

            // Check if the hearing has been linked before and if so - is it
            // different
            // from the lead?
            if ((prevLinkedID != null) && (!prevLinkedID.equals(leadLinkedHearingID))) {
                // check if the linked hearing id has already been evaluated -
                // if not
                // find all linked hearings and add them to the Collection. Also
                // add the
                // prevLinkedID to the Set so that the hearings are only added
                // ones to
                // the collection to be updated.
                if (!set.contains(prevLinkedID)) {
                    set.add(prevLinkedID);
                    Collection prevLinkedHearings = this.findLinkedHearings(prevLinkedID);
                    hearingsToUpdate.addAll(prevLinkedHearings);
                }
            }
            // We don't need to add the ones that already have the same
            // linked id but we need to add the ones that have never been
            // linked before
            else if (prevLinkedID == null) {
                hearingsToUpdate.addElement(hearing);
            }
        }

        // finally update all hearings with the new linked hearing id if there
        // are any hearings to update.
        if (!hearingsToUpdate.isEmpty()) {
            this.updateHearings(leadLinkedHearingID, hearingsToUpdate, userDisplayName);
        }

        log.debug("LinkHearingsHelper.linkHearings(Integer leadHearingID, " + "Collection hearingIDs) finished");
    }

    /**
     * This will unlink this one hearing that is passed in. If there is only one
     * more hearing with the same id - then this will be unlinked as well. This
     * because there shouldn't be just one hearing for one linkedhearing id. If
     * the hearing is linked with more than one other hearings then only this
     * hearing will be unlinked.
     * 
     * @param hearingID
     * @throws HearingScheduleException
     */
    public void unlinkHearing(Integer hearingID, String userDisplayName) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.unlinkHearing(Integer hearingID) called");

        Vector hearingsToUpdate = new Vector();

        // find the hearing
        Hearing unlinkHearing = this.findHearing(hearingID);

        // get the linked hearingid
        Collection linkedHearings = this.findLinkedHearings(unlinkHearing.getLinkedHearingId());
        int size = linkedHearings.size();

        log.debug("There are " + size + " hearings with linked hearing id " + unlinkHearing.getLinkedHearingId());

        // if there is only one hearing - then just add this one to be updated.
        if (size == 1) {
            hearingsToUpdate.addElement(unlinkHearing);
        }
        // if there are only two - then add the collection and update these two
        else if (size == 2) {
            hearingsToUpdate.addAll(linkedHearings);
        }
        // if there are 3 or more linked hearins we don't want to break any
        // other
        // linkage so just add the one hearing.
        else if (size >= 3) {
            hearingsToUpdate.addElement(unlinkHearing);
        }

        // Update the hearing(s) with the "unlinkage" status.
        this.updateHearings(null, hearingsToUpdate, userDisplayName);

        log.debug("LinkHearingsHelper.unlinkHearing(Integer hearingID) finished");
    }

    /**
     * This will call the listCasesWithHearings(String caseNumberTypeCriterium,
     * Integer CourtID, Integer leadHearingID). Therefore this method could be
     * used for other purposes as well. The linkedHearingID will be set to null.
     * 
     * @param caseNumberTypeCriterium
     * @param CourtID
     * @return CaseHearingValue
     * @throws HearingScheduleException
     */
    public CaseHearingValue listCaseWithHearings(String caseNumberTypeCriterium, Integer courtID)
            throws HearingScheduleException {
        log
                .debug("LinkHearingsHelper.listCaseWithHearings(String caseNumberTypeCriterium,"
                        + "Integer courtID) called");

        CaseHearingValue value = this.listCaseWithHearings(caseNumberTypeCriterium, courtID, null);

        log.debug("LinkHearingsHelper.listCaseWithHearings(String caseNumberTypeCriterium,"
                + "Integer courtID) finished");
        return value;
    }

    /**
     * This will search for the case passed in and return the case and all
     * related hearings except from the ones that have the same linkedHearingID
     * as the one passed in. The case passed in needs massaging since the
     * argument will be 1 string containing the case type and number - e.g.
     * T20030932
     * 
     * @param caseNumberTypeCriterium
     * @param CourtID
     * @param leadHearingID
     * @return CaseHearingValue
     * @throws HearingScheduleException
     */
    public CaseHearingValue listCaseWithHearings(String caseTypeAndNumber, Integer courtID, Integer leadHearingID)
            throws HearingScheduleException {
        log.debug("LinkHearingsHelper.listCaseWithHearings(String caseTypeAndNumber " + caseTypeAndNumber
                + ", Integer courtID " + courtID.toString() + ", Integer leadHearingID " + leadHearingID.toString()
                + ") called");

        CaseHearingValue caseHearingValue = null;
        Collection hearingValues = null;
        Integer caseID = null;
        String caseType = "";
        Integer caseNumber = null;

        // Get the first character since this is always a letter and
        // the rest is always numbers.
        caseType = this.getCaseType(caseTypeAndNumber);
        log.debug("CaseType : " + caseType);

        // Get the case number out of the String
        caseNumber = this.getCaseNumber(caseTypeAndNumber);
        log.debug("caseNumber : " + caseNumber);

        // find the caseid
        caseID = this.findCaseID(caseType, caseNumber, courtID);
        log.debug("caseID : " + caseID);

        // find the actual case
        CaseBasicValue caseBasicValue = this.findCaseByID(caseID);

        Hearing hearing = this.findHearing(leadHearingID);

        // find the hearings for that hearing
        if (caseBasicValue != null) {
            // This will find the hearings and return a collection of
            // HearingValues.
            hearingValues = this.findHearingByCaseID(caseID, hearing.getLinkedHearingId());
        }

        // Finally set the caseHearingValue
        caseHearingValue = new CaseHearingValue(caseBasicValue.getId(), caseBasicValue.getCaseNumber(), caseBasicValue
                .getCaseType(), caseBasicValue.getCaseSubType(), caseBasicValue.getCourtID(), hearingValues);

        log.debug("LinkHearingsHelper.listCaseWithHearings(String caseTypeAndNumber,"
                + "Integer courtID, Integer leadHearingID) finished");

        return caseHearingValue;
    }

    /**
     * This will search for a case by the primary key.
     * 
     * @param caseID
     * @return CaseBasicValue
     * @throws HearingScheduleException
     * 
     * @todo - do not use the maintainer.
     */
    private CaseBasicValue findCaseByID(Integer caseID) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.findCaseByID(Integer caseID) called");
        Case caze = null;
        CaseBasicValue basicValue = null;
        try {
            caze = getCaseMaintainer().findByPrimaryKey(caseID);
            basicValue = getCaseMaintainer().getCaseBasicValue(caze);
        } catch (ObjectNotFoundException ex) {
            // the case could not be found
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(CASE_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }
        log.debug("LinkHearingsHelper.findCaseByID(Integer caseID) finished");
        return basicValue;
    }

    /**
     * This will create a CaseMaintainer if it doesn't exist and return it.
     * 
     * @return CaseMaintainer
     * 
     * @todo - This maintainer is not in the scheudled hearing subsystem and
     *       should therefore not be used directly. We will therefore have to
     *       add this method on the facade.
     */
    private CaseMaintainer getCaseMaintainer() {
        log.debug("LinkHearingsHelper.getCaseMaintainer() called");
        if (caseMaintainer == null) {
            caseMaintainer = new CaseMaintainer();
        }
        log.debug("LinkHearingsHelper.getCaseMaintainer() finished");
        return caseMaintainer;
    }

    /**
     * This method will find the hearings related to a case. The hearings will
     * be populated and added to the collection if the linked hearing id is
     * different from the one passsed in.
     * 
     * @param caseID
     * @param linkedHearingID
     * @return Collection of Hearing Values.
     * @throws HearingScheduleException
     */
    @SuppressWarnings("unchecked")
	private Collection findHearingByCaseID(Integer caseID, Integer linkedHearingID) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.findHearings(Integer caseID, Integer linkedHearingID) called");

        Vector hearings = null;
        Collection<HearingBasicValue> basicValues = null;

        try {
            // try to find all the hearings
            basicValues = hearingMaintainer.findByCaseId(caseID);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(HEARING_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }

        // loop through the hearings and set the refhearingtype and the hearing
        // values
        // for the hearings that are not already linked.
        if (basicValues != null && !basicValues.isEmpty()) {
            hearings = new Vector();

            for (HearingBasicValue basicValue : basicValues) {

                // the linked hearingids need to be different if we should
                // populate
                // the collection or it has to be null so that it can be used
                // when we don't
                // have a linked id.
                if (linkedHearingID == null || !linkedHearingID.equals(basicValue.getLinkedHearingID())) {
                    // build a Hearing Value with only the hearing values
                    HearingValue value = this.buildHearingValueFromBasicValue(basicValue);

                    // Get the hearing type from BisRef.
                    RefHearingTypeBasicValue refHrgTypeBasicValue = this.getRefHearingTypeBasicValue(value
                            .getRefHearingTypeID());

                    // continue build with ref hearing types.
                    value.setHearingTypeCode(refHrgTypeBasicValue.getHearingTypeCode());
                    value.setHearingTypeDesc(refHrgTypeBasicValue.getHearingTypeDesc());

                    // add to the vector.
                    hearings.addElement(value);
                }
            }
        }
        log.debug("LinkHearingsHelper.findHearings(Integer caseID, Integer linkedHearingID) finished");
        return hearings;
    }

    /**
     * This will search for the Refhearing type by the primary key via the
     * Bisref controller.
     * 
     * @param refHrgTypeID
     * @return RefHearingTypeBasicValue
     * @throws HearingScheduleException
     */
    private RefHearingTypeBasicValue getRefHearingTypeBasicValue(Integer refHrgTypeID) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.getRefHearingTypeBasicValue(Integer refHrgTypeID) called");

        RefHearingTypeBasicValue basicValue = null;
        Collection refHrgTypes = null;

        // set the criteria
        RefHearingTypeCriteria criteria = new RefHearingTypeCriteria();
        criteria.setPrimaryKey(refHrgTypeID);
        log.debug(">>>>> Search for RefHrgType with id : " + refHrgTypeID);

        try {
            refHrgTypes = this.getBisRefController().findHearingTypes(criteria);
        } catch (BisRefControllerException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(HEARING_TYPE_NOT_FOUND, ex.getMessage(), ex);
        }

        // There will only be one element in the collection since we search for
        // by primary key.
        if (refHrgTypes != null) {
            Iterator it = refHrgTypes.iterator();
            basicValue = (RefHearingTypeBasicValue) it.next();
        }
        log.debug("LinkHearingsHelper.getRefHearingTypeBasicValue(Integer refHrgTypeID) finished");
        return basicValue;
    }

    /**
     * This will get a BisRefController
     * 
     * @return BisRefController
     */
    private BisRefControllerLocal getBisRefController() throws HearingScheduleException {
        log.debug("LinkHearingsHelper.getBisRefController() called");
        if (bisRefController == null) {
            this.bisRefController = ((BisRefControllerLocal) CSServices.getEJBServices().createLocalSession(
                    BisRefControllerLocalHome.class));
        }
        log.debug("LinkHearingsHelper.getBisRefController() finished");
        return bisRefController;
    }

    /**
     * This will set create a HearingValue and set the hearing values but not
     * the refhearingtype values.
     * 
     * @param basicValue
     * @return HearingValue
     */
    private HearingValue buildHearingValueFromBasicValue(HearingBasicValue basicValue) {
        log.debug("LinkHearingsHelper.buildHearingValueFromBasicValue(" + "HearingBasicValue basicValue) called");

        HearingValue value = new HearingValue(basicValue.getId(), basicValue.getHearingStartDate(), basicValue
                .getHearingEndDate(), basicValue.getRefHearingTypeID());

        log.debug("LinkHearingsHelper.buildHearingValueFromBasicValueller(" + "HearingBasicValue basicValue) finished");
        return value;
    }

    /**
     * This will take out the case number of a string and create an integer of
     * it. It will throw an exception if the supposed casenumber is not a
     * number.
     * 
     * @param caseTypeAndNo
     * @return Integer - case number
     * @throws HearingScheduleException
     */
    private Integer getCaseNumber(String caseTypeAndNo) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.getCaseNumber(String caseTypeAndNo) called");
        String caseString = null;
        String tmpCaseNumber = null;
        Integer caseNumber = null;

        caseString = caseTypeAndNo.trim();

        // the first character is the casetype and the rest is the case number.
        tmpCaseNumber = caseString.substring(1);

        try {
            // try to create an Integer from the String.
            caseNumber = new Integer(tmpCaseNumber);
        } catch (NumberFormatException ex) {
            throw new HearingScheduleException(INVALID_CASE, "The given case is not valid.");
        }
        log.debug("LinkHearingsHelper.getCaseNumber(String caseTypeAndNo) finished");
        return caseNumber;
    }

    /**
     * This will take out the case type of the string passed in. The case type
     * is always the first charater and it has to be a letter.
     * 
     * @param caseTypeAndNo
     * @return String - the CaseType.
     * @throws HearingScheduleException
     */
    private String getCaseType(String caseTypeAndNo) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.getCaseType(String caseTypeAndNo) called");

        String caseString = null;
        Character charCaseType = null;
        String caseType = null;

        // make sure it doesn't contain any spaces before processing starts
        caseString = caseTypeAndNo.trim();

        // get the case type - it should always be the first charchter and it
        // should
        // be a letter
        charCaseType = new Character(caseString.charAt(0));

        if (!Character.isLetter(caseString.charAt(0))) {
            throw new HearingScheduleException(INVALID_CASE, "The given case is not valid.");
        }

        caseType = charCaseType.toString();
        log.debug("LinkHearingsHelper.getCaseType(String caseTypeAndNo) finished");
        return caseType;
    }

    /**
     * This will create a CaseController if it hasn't already been intantiated
     * and then return it.
     * 
     * @return CaseController
     */
    private CaseControllerLocal getCaseController() {
        log.debug("LinkHearingsHelper.getCaseController() called");
        if (caseController == null) {
            caseController = (CaseControllerLocal) CSServices.getEJBServices().createLocalSession(
                    CaseControllerLocalHome.class);
        }
        log.debug("LinkHearingsHelper.getCaseController() finished");
        return caseController;
    }

    /**
     * This will make a call to the CaseController to try to find a case with
     * the given criterias.
     * 
     * @param caseType
     * @param caseNumber
     * @param courtID
     * @return Integer
     * @throws HearingScheduleException
     */
    private Integer findCaseID(String caseType, Integer caseNumber, Integer courtID) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.findCaseID(String caseType, Integer caseNumber," + " Integer courtID) called");

        Integer caseID = null;
        try {
            // find the case
            caseID = this.getCaseController().findCaseId(caseType, caseNumber, courtID);
        } catch (CaseControllerException ex) {
            // case not found
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(CASE_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }
        log.debug("LinkHearingsHelper.findCaseID(String caseType, Integer caseNumber," + " Integer courtID) finished");
        return caseID;
    }

    /**
     * This method will create a new Linked hearing id.
     * 
     * @return Integer - the newly created linked hearing id.
     * @throws HearingScheduleException
     */
    private Integer createNewLinkedHearingID(String userDisplayName) {
        log.debug("LinkHearingsHelper.createNewLinkedHearingID() called");

        Integer newLinkedHearingID = null;
        LinkedHearing linkedHearing = null;

        LinkedHearingMaintainer lhMaintainer = new LinkedHearingMaintainer();
        LinkedHearingBasicValue basicValue = new LinkedHearingBasicValue();
        log.debug("Will try to create a new linked hearing id");
        linkedHearing = (LinkedHearing) lhMaintainer.create(basicValue, userDisplayName);
        if (linkedHearing != null)
            log.debug("New linked hearing Id created : " + linkedHearing.getLinkedHearingId().toString());
        else
            log.debug("The create failed and the linkedHearing is null....");

        newLinkedHearingID = linkedHearing.getLinkedHearingId();

        log.debug("LinkHearingsHelper.createNewLinkedHearingID() finished");
        return newLinkedHearingID;
    }

    /**
     * Uses the maintainer to find the local Hearing.
     * 
     * @param hearingID
     * @return Hearing entity
     * @throws HearingScheduleException
     */
    private Hearing findHearing(Integer hearingID) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.findHearing(Integer hearingID) called");
        Hearing hearing = null;
        try {
            hearing = hearingMaintainer.findByPK(hearingID);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            HearingScheduleException hex = new HearingScheduleException(HEARING_NOT_FOUND, ex.getMessage(), ex);
            throw hex;
        }
        log.debug("LinkHearingsHelper.findHearing(Integer hearingID) finished");
        return hearing;
    }

    /**
     * This wil find all the hearings with the same linked hearing id.
     * 
     * @param linkedID
     * @return Collection of hearings
     * @throws HearingScheduleException
     */
    private Collection findLinkedHearings(Integer linkedID) {
        log.debug("LinkHearingsHelper.findLinkedHearings(Integer linkedID) called");
        Collection linkedHearings = hearingMaintainer.findByLinkedHearingId(linkedID);
        log.debug("LinkHearingsHelper.findLinkedHearings(Integer linkedID) finished");
        return linkedHearings;
    }

    /**
     * This method will loop through all hearings and set the new Lead linked
     * hearing id and update them.
     * 
     * @param leadLinkedHearingID
     * @param Collection
     *            of hearings
     * @throws HearingScheduleException
     */
    private void updateHearings(Integer leadLinkedHearingID, Collection hearings, String userDisplayName) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.updateHearings(Integer leadLinkedHearingID, " + "Collection hearings) called");

        if (hearings.isEmpty()) {
            throw new HearingScheduleException(NO_HEARINGS_TO_LINK, "There are no hearings to link/unlink");
        }

        Iterator it = hearings.iterator();
        while (it.hasNext()) {
            // Get the hearing, convert to a basic value and set the new
            // linked id
            // before update.
            Hearing hearing = (Hearing) it.next();
            HearingBasicValue value = hearingMaintainer.getHearingBasicValue(hearing);
            value.setLinkedHearingID(leadLinkedHearingID);
            try {
                log.debug("Try to update hearing : " + value.toString());
                hearingMaintainer.update(value, userDisplayName);
            } catch (ObjectNotFoundException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                HearingScheduleException hex = new HearingScheduleException(HEARING_NOT_FOUND, ex.getMessage(), ex);
                throw hex;
            }
        }

        log.debug("LinkHearingsHelper.updateHearings(Integer leadLinkedHearingID, " + "Collection hearings) finished");
    }

    /**
     * Check if the hearing has already been exported or export is in progress
     * or exportA record is locked else throw a specific exception
     * 
     * @param exportAValue
     * @return boolean
     * @throws HearingRecordExportInProgressException
     * @throws HearingRecordHearingAlreadyExportedException
     * @throws HearingRecordExportALockedException
     */
    private boolean checkExportState(ExportAValue exportAValue) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.checkExportState(ExportAValue exportAValue) called");

        boolean isUpdateOk = false;

        if (exportAValue != null) {
            log.debug("exportAValue is not null!");

            try {
                // the export is in progress
                if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.IN_PROGRESS)) {
                    throw new HearingRecordExportInProgressException(HearingRecordConstants.EXPORT_IN_PROGRESS_EXC,
                            "Export is in progress");
                }
                // the export has already been done and was successful
                else if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.EXPORT_SUCCESS)) {
                    throw new HearingRecordHearingAlreadyExportedException(
                            HearingRecordConstants.HEARING_ALREADY_EXPORTED_EXC, "The hearing has already been changed");
                }
                // the export record has been locked
                else if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.EXPORT_LOCKED)) {
                    throw new HearingRecordExportALockedException(HearingRecordConstants.EXPORT_A_LOCKED_EXC,
                            "The record is locked and can not be changed");
                }
                // the export record has been marked to be exported already but
                // the export has not yet started
                else if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.READY_FOR_EXPORT)) {
                    throw new HearingRecordExportInProgressException(HearingRecordConstants.READY_FOR_EXPORT_EXC,
                            "The hearing record has already been marked to be exported");
                }
                // the export exists and has previously failed then allow the
                // user to export again.
                else if (exportAValue.getStatusFlag().equalsIgnoreCase(HearingRecordConstants.EXPORT_FAILED)) {
                    isUpdateOk = true;
                }
            } catch (HearingRecordExportInProgressException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
            } catch (HearingRecordHearingAlreadyExportedException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
            } catch (HearingRecordExportALockedException ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
                throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
            }
        }// end of first if

        // the export A record does not yet exist!
        else {
            log.debug("exportAValue is null");
            isUpdateOk = true;
        }

        log.debug("LinkHearingsHelper.checkExportState(ExportAValue exportAValue) exit");
        return isUpdateOk;
    }

    /**
     * Get a HearingRecordStatusHelper
     * 
     * @return HearingRecordStatusHelper
     */
    private HearingRecordStatusHelper getHearingRecordStatusHelper() {
        if (hearingRecordStatusHelper == null) {
            hearingRecordStatusHelper = new HearingRecordStatusHelper();
        }
        return hearingRecordStatusHelper;
    }

    /**
     * Checks if a hearing has been exported, export is in progress or is
     * locked.
     * 
     * @param hearing
     * @throws HearingScheduleException
     */
    private void checkIfExported(Hearing hearing) throws HearingScheduleException {
        log.debug("LinkHearingsHelper.checkIfExported(Hearing hearing) called");

        // Get the basic value
        HearingBasicValue leadBasicValue = hearingMaintainer.getHearingBasicValue(hearing);

        ExportAValue exportAValue = null;
        try {
            // get the exportA value
            exportAValue = this.getHearingRecordStatusHelper().getExportValue(leadBasicValue);
        } catch (HearingRecordException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new HearingScheduleException(ex.getUserMessageAsMessage().getKey(), ex.getMessage(), ex);
        }

        // check if has been exported already or in progress.
        this.checkExportState(exportAValue);

        log.debug("LinkHearingsHelper.checkIfExported(ExportAValue exportAValue) finished");
    }

}