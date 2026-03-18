package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//J2EE
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCourtHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.DefHearingRecordBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_legal_aid_order.XhbLegalAidOrderBeanNotFoundException;

/**
 * <p>
 * Title: HRDefendantValueHelper
 * </p>
 * <p>
 * Description: The value helper for defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Anthony Martin / Marie Holmberg
 * @version 1.0
 */
public class HRDefendantValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRDefendantValueHelper.class);

    private DefendantMaintainer defMaintainer = null;

    private DefendantOnCaseMaintainer defOnCaseMaintainer = null;

    private Integer m_defendantOnCaseId = null;

    /**
     * Default constructor that intantiate DefendantMaintainer and
     * DefendantOnCaseMaintainer
     */
    public HRDefendantValueHelper() {
        defMaintainer = new DefendantMaintainer();
        defOnCaseMaintainer = new DefendantOnCaseMaintainer();
    }

    /**
     * This will find a defendant and its values and then transform to a hearing
     * record Defendant value.
     * 
     * First, get Collection of DefendantReference objects Second, get
     * Collection of DefendantOnCase objects using caseID Use these to populate
     * the complex value then user complex value to get hrdefendantvalue
     * 
     * @param defendantID
     *            Integer
     * @param caseID
     *            Integer
     * @param hearingId
     *            Integer
     * @return HRDefendantValue
     * @throws HearingRecordException
     */
    public HRDefendantValue getDefendantHrValue(Integer defendantID, Integer caseID, Integer hearingId)
            throws HearingRecordException {
        HRDefendantValue returnValue = null;
        try {
            log.debug("HRDefendantValueHelper.getDefendant(Integer defendantID) called");
            // get a DefendantComplexValue from a defendantID using
            // maintainer
            Defendant defendant = defMaintainer.findByPrimaryKey(defendantID);
            DefendantComplexValue complexValue = defMaintainer.getDefendantComplexValue(defendant);
            // traverse the CMRs
            Collection defRefCollection = defendant.getDefendantReferences();
            Collection defOnCaseCollection = defendant.getDefendantOnCases();
            // use the maintainers to retrieve the basic values
            DefendantReferenceMaintainer defRefMaintainer = new DefendantReferenceMaintainer();
            DefendantOnCaseMaintainer docMaintainer = new DefendantOnCaseMaintainer();
            // now populate the null cmr fields in the complex value with
            // the basic value collections
            complexValue.setDefendantOnCases(docMaintainer.getDefendantOnCaseBasicValues(defOnCaseCollection));
            complexValue.setDefendantReferences(defRefMaintainer.getDefendantReferenceBasicValue(defRefCollection));
            returnValue = buildHRDefendantValue(complexValue, caseID, hearingId);
            log.debug("HRDefendantValueHelper.getDefendant(Integer defendantID) finished");
        } catch (ObjectNotFoundException onfe) {
            onfe.printStackTrace();
            throw new HearingRecordRetrievalException("", "onfe.getMessage()", onfe);
        }
        return returnValue;
    }

    /**
     * This will build the actual HRDefendantValue
     * 
     * Build HRDefendant from complex value, defendantReference value, and
     * defendantOnCase value First, iterate through the defendantreference
     * collection on the complex value to sort the elements into the correct
     * class attributes on the hrdefendantvalue object. Second, iterate through
     * the defendantoncase collection on the complex value to find the
     * corresponding defendantoncase basic value. Third, using the the
     * defendantoncase basic value, populate corresponding attributes on
     * hrdefendantvalue Fourth, using the complexvalue populate the basic
     * defendant attributes into the hrdefendantvalue.
     * 
     * @param complexValue
     *            DefendantComplexValue
     * @param caseID
     *            Integer
     * @param hearingId
     *            Integer
     * @return HRDefendantValue
     * @throws ObjectNotFoundException
     */
    private HRDefendantValue buildHRDefendantValue(DefendantComplexValue complexValue, Integer caseID, Integer hearingId)
            throws ObjectNotFoundException, HearingRecordException {
        log.debug("HRDefendantValueHelper.buildHRDefendantValue() called");
        HRDefendantValue hrValue = new HRDefendantValue(complexValue.getId());
        // Sort defendantreference Values
        populateWithDefendantReferences(hrValue, complexValue);
        // get defendantoncase which corresponds to the caseID
        populateWithDefendantOnCaseValue(hrValue, complexValue, caseID);
        // populate remaining attributes from the complex value
        hrValue.setFirstName(complexValue.getFirstName());
        hrValue.setMiddleName(complexValue.getMiddleName());
        hrValue.setSurname(complexValue.getSurname());
        hrValue.setInCustody(complexValue.getCurrentPrisonStatus());
        if (complexValue.getLastConvictionDate() != null) {
            hrValue.setLastConvictionDate((complexValue.getLastConvictionDate()).getTime());
        }
        populateWithDefHearingRecordID(hrValue, complexValue, hearingId, caseID);
        log.debug("HRDefendantValueHelper.getDefendant() finished");
        return hrValue;
    }

    /**
     * Helper method to set the DefHearingRecordID on the DefendantComplexValue
     * object
     * 
     * Find the appropriate DefendantOnCaseBasicValue extract the
     * defendantOnCaseID then use the DefHearingRecordMaintainer to find the
     * DefHearingRecordId
     * 
     * @param hrValue
     *            HRDefendantValue
     * @param complexValue
     *            DefendantComplexValue
     * @param hearingID
     *            Integer
     * @param caseID
     *            Integer
     * @throws ObjectNotFoundException
     */
    private void populateWithDefHearingRecordID(HRDefendantValue hrValue, DefendantComplexValue complexValue,
            Integer hearingID, Integer caseID) throws ObjectNotFoundException {
        Collection defendantOnCaseCollection = complexValue.getDefendantOnCases();
        Iterator iterator = defendantOnCaseCollection.iterator();
        while (iterator.hasNext()) {
            DefendantOnCaseBasicValue docBasic = (DefendantOnCaseBasicValue) iterator.next();
            if (docBasic.getCaseID().equals(caseID)) {
                // this is the defendantOnCaseBasicValue we want
                m_defendantOnCaseId = docBasic.getId();
            }
        }
        // get the DefHearingRecordMaintainer
        DefHearingRecordMaintainer defHearingRecordMaintainer = new DefHearingRecordMaintainer();
        DefHearingRecord defHearingRecord = defHearingRecordMaintainer.findByDefendantOnCaseIDAndHearingID(
                m_defendantOnCaseId, hearingID);

        DefHearingRecordBasicValue basicValue = defHearingRecordMaintainer
                .getDefHearingRecordBasicValue(defHearingRecord);
        hrValue.setDefHearingRecordID(basicValue.getId());
    }

    public Integer getDefendantOnCaseID(Integer defendantID, Integer caseID) throws HearingRecordRetrievalException {
        if (m_defendantOnCaseId == null) {
            throw new HearingRecordRetrievalException();
        }
        return m_defendantOnCaseId;
    }

    /**
     * Iterate through the defendantoncase collection on the complex value to
     * find the corresponding defendantoncase basic value.
     * 
     * @param hrValue
     *            HRDefendantValue
     * @param complexValue
     *            DefendantComplexValue
     * @param caseID
     *            Integer
     */
    private void populateWithDefendantOnCaseValue(HRDefendantValue hrValue, DefendantComplexValue complexValue,
            Integer caseID) throws HearingRecordException {
        Collection defendantOnCaseCollection = complexValue.getDefendantOnCases();
        Iterator iterator = defendantOnCaseCollection.iterator();
        while (iterator.hasNext()) {
            DefendantOnCaseBasicValue docBasic = (DefendantOnCaseBasicValue) iterator.next();
            if (docBasic.getCaseID().equals(caseID)) {
                hrValue.setNoOfTICs(docBasic.getNoOfTICs());
                hrValue.setFinalDrivingLicenseStatus(docBasic.getFinalDrivingLicenceStatus());
                hrValue.setCollectMagistrateCourtId(docBasic.getCollectMagistrateCourtId());
                if (docBasic.getCollectMagistrateCourtId() != null) {
                    hrValue.setCollectMagistrateCourtName(getCollectMagistrateCourtName(docBasic
                            .getCollectMagistrateCourtId()));
                }
                try {
                    log.debug("Lookup legallyAided using findByDefendantOnCaseIdValue(" 
                            + docBasic.getId() + ")");
                    XhbLegalAidOrderBeanHelper2.findByDefendantOnCaseIdValue(docBasic.getId());
                    log.debug("LegalAidOrder found");
                    hrValue.setLegallyAided(true);
                } catch (XhbLegalAidOrderBeanNotFoundException notFound) {
                    hrValue.setLegallyAided(false);
                }
            }
        }
    }

    /**
     * Iterate through the defendantreference collection on the complex value to
     * sort the elements into the correct class attributes on the
     * hrdefendantvalue object.
     * 
     * @param hrValue
     *            HRDefendantValue
     * @param complexValue
     *            DefendantComplexValue
     */
    private void populateWithDefendantReferences(HRDefendantValue hrValue, DefendantComplexValue complexValue) {
        // Sort defendantreference values
        Collection defendantReferenceValues = complexValue.getDefendantReferences();
        Iterator iterator = defendantReferenceValues.iterator();
        while (iterator.hasNext()) {
            DefendantReferenceBasicValue defRefBasic = (DefendantReferenceBasicValue) iterator.next();
            if (defRefBasic.getReferenceName().equals(
            /* DefendantReferenceProperties.DRIVER_NUMBER */"DRIVER_NO")) {
                hrValue.setDriverNumber(defRefBasic.getReferenceValue());
            }
            else if (defRefBasic.getReferenceName().equals(
            /* DefendantReferenceProperties.CRO_NUMBER */"CRO_NO")) {
                hrValue.setCroNumber(defRefBasic.getReferenceValue());
            }
            else if ( defRefBasic.getReferenceName().equals("LICENCE_TYPE")) {
            	hrValue.setLicenceType(defRefBasic.getReferenceValue());
            }
            else if ( defRefBasic.getReferenceName().equals("LICENCE_ISSUE_NUMBER")){
            	hrValue.setIssueNumber(defRefBasic.getReferenceValue());
            }
        }
    }

    /**
     * This will try to find a ref court via the BisRef
     * 
     * @param collectMagistrateCourtID
     *            Integer
     * @return String the description name of the ref court.
     * @throws HearingRecordException
     */
    private String getCollectMagistrateCourtName(Integer collectMagistrateCourtId) throws HearingRecordException {
        log.debug("HRDefendantValueHelper.getCollectMagistrateCourtName("
                + "Integer collectMagistrateCourtId called with id : " + collectMagistrateCourtId);
        try {
            RefCourtCriteria criteria = new RefCourtCriteria();
            criteria.setPrimaryKey(collectMagistrateCourtId);
            RefCourtHelper refCourtHelper = new RefCourtHelper();
            Collection courts = refCourtHelper.findCourts(criteria);
            // test collection has only one element
            if (courts.size() != 1) {
                // unexpected exception
                throw new CSUnrecoverableException("could not find the ref court");
            }
            java.util.Iterator iterator = courts.iterator();
            RefCourtBasicValue refCourtBasicValue = (RefCourtBasicValue) iterator.next();
            String courtName = refCourtBasicValue.getCourtFullName();

            log.debug("HRDefendantValueHelper.getCollectMagistrateCourtName("
                    + "Integer collectMagistrateCourtId finished");

            return courtName;
        } catch (BisRefControllerException e) {
            CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
            throw new HearingRecordException(e.getUserMessage(), "Failed to find name for CollectMagistrateCourt", e);
        }
    }

}