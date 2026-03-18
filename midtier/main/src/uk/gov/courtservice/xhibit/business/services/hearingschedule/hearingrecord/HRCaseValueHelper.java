package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

//J2EE
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCaseValue;

/**
 * <p>
 * Title: HRCaseValueHelper
 * </p>
 * <p>
 * Description: This will get the case details required for hearing record
 * display
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
 */
public class HRCaseValueHelper implements HRBasicValueHelper {

    // logger
    private static Logger log = CSServices.getLogger(HRCaseValueHelper.class);

    private CaseMaintainer maintainer = null;

    /**
     * Default constructor that intantiate the CaseMaintainer
     */
    public HRCaseValueHelper() {
        maintainer = new CaseMaintainer();
    }

    /**
     * This will find a case and convert it to a Hearing record value.
     * 
     * @param caseID
     *            Integer
     * @return HRCaseValue
     * @throws HearingRecordException
     */
    public HRCaseValue getHRCaseValue(Integer caseID) throws HearingRecordException {
        log.debug("HRCaseValueHelper.getHRCaseValue(Integer caseID) called");
        HRCaseValue hrValue = null;
        // get the basic value
        CaseBasicValue basicValue = this.getBasicCaseValue(caseID);
        // build a hearing record value from a basic value
        hrValue = this.buildHRCaseValue(basicValue);
        log.debug("HRCaseValueHelper.getHRCaseValue(Integer caseID) finished");
        return hrValue;
    }

    /**
     * This will build a Hearing Record case value from a basic value.
     * 
     * @param caseBasicValue
     *            CaseBasicValue
     * @return HRCaseValue
     */
    private HRCaseValue buildHRCaseValue(CaseBasicValue caseBasicValue) {
        log.debug("HRCaseValueHelper.buildHRCaseValue(CaseBasicValue caseBasicValue) called");
        HRCaseValue hrCaseValue = new HRCaseValue(caseBasicValue.getId());
        hrCaseValue.setCaseType(caseBasicValue.getCaseType());
        hrCaseValue.setCaseSubType(caseBasicValue.getCaseSubType());
        hrCaseValue.setCaseNumber(caseBasicValue.getCaseNumber());
        hrCaseValue.setEstPDHTrialLength(caseBasicValue.getEstPDHTrialLength());
        hrCaseValue.setCrestSeveredInd(caseBasicValue.getCrestSeveredInd());
        hrCaseValue.setNoProsWitness(caseBasicValue.getNoProsWitness());
        hrCaseValue.setNoPageProsEvidence(caseBasicValue.getNoPageProsEvidence());
        hrCaseValue.setLengthTape(caseBasicValue.getLengthTape());
        log.debug("HRCaseValueHelper.buildHRCaseValue(CaseBasicValue caseBasicValue) finished");
        return hrCaseValue;
    }

    /**
     * This will find the case from the container via the maintainer.
     * 
     * @param caseID
     *            Integer
     * @return Case entity
     * @throws HearingRecordException
     */
    private Case findCase(Integer caseID) throws HearingRecordException {
        try {
            log.debug("HRCaseValueHelper.findCase(Integer caseID) called");
            CaseBasicValue caseValue = null;
            Case caze = maintainer.findByPrimaryKey(caseID);
            log.debug("HRCaseValueHelper.getCaseBasicValue(Integer caseID) finsihed");
            return caze;
        } catch (ObjectNotFoundException onfe) {
            onfe.printStackTrace();
            throw new HearingRecordException("ObjectNotFoundException", onfe.getMessage(), onfe);
        }
    }

    /**
     * This will find a case and convert it to a CaseBasicValue
     * 
     * @param caseID
     *            Integer
     * @return CaseBasicValue
     * @throws HearingRecordException
     */
    private CaseBasicValue getBasicCaseValue(Integer caseID) throws HearingRecordException {
        log.debug("HRCaseValueHelper.findCase(Integer caseID) called");
        CaseBasicValue basicValue = null;
        Case caze = this.findCase(caseID);
        basicValue = this.maintainer.getCaseBasicValue(caze);
        log.debug("HRCaseValueHelper.findCase(Integer caseID) finished");
        return basicValue;
    }

}