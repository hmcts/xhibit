package uk.gov.courtservice.xhibit.business.vos.services.caze;

import java.util.Calendar;
import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;

/**
 * Helper class for Case information
 * 
 * @author rzvddy
 *
 */
public class CaseHelper {

    /**
     * Converts an XhbCaseBasicValue to a CaseBasicValue
     * 
     * This is a temporary method that will be removed when the old entity 
     * layer is removed and XhbCaseBasicValue totally replaces CaseBasicValue
     * 
     * @param oldCase and XhbCaseBasicValue
     * @return CaseBasicValue
     */
    public static CaseBasicValue convertToCaseBasicValue(XhbCaseBasicValue oldCase) {
        
        CaseBasicValue newCase = new CaseBasicValue();
        
        newCase.setBailMagCode(oldCase.getBailMagCode());
        newCase.setCaseClass(oldCase.getCaseClass());
        newCase.setCaseDescription(oldCase.getCaseDescription());
        newCase.setCaseNumber(oldCase.getCaseNumber());
        newCase.setCaseSubType(oldCase.getCaseSubType());
        newCase.setCaseTitle(oldCase.getCaseTitle());
        newCase.setCaseType(oldCase.getCaseType());
        newCase.setCccTransToRefCourtId(oldCase.getCccTransToRefCourtId());
        newCase.setChargeImportIndicator(oldCase.getChargeImportIndicator());
        newCase.setClassCode(oldCase.getClassCode());
        newCase.setCourtID(oldCase.getCourtId());
        newCase.setCpsCaseWorker(oldCase.getCpsCaseWorker());
        
        // CaseBasicValue has 2 fields for db column PROS_AGENCY_REFERENCE
        // but neither can update - see CaseMaintainer - 20july removed double ref
        newCase.setProsAgencyRef(oldCase.getProsAgencyReference());
        
        newCase.setCrestIndictResp(oldCase.getIndictResp());        
        newCase.setCrestSeveredInd(oldCase.getSeveredInd());
        
        Calendar calDateIndRec = null;
        if (oldCase.getDateIndRec() != null) {
            calDateIndRec = Calendar.getInstance();
            calDateIndRec.setTime(oldCase.getDateIndRec());
        }
        newCase.setDateIndRec(calDateIndRec);
        
        newCase.setEstPDHTrialLength(oldCase.getEstPdhTrialLength());
        newCase.setExportCharges(oldCase.getExportCharges());
        newCase.setId(oldCase.getCaseId());
        newCase.setIndChangeStatus(oldCase.getIndChangeStatus());
        newCase.setIndictmentInfo1(oldCase.getIndictmentInfo1());
        newCase.setIndictmentInfo2(oldCase.getIndictmentInfo2());
        newCase.setIndictmentInfo3(oldCase.getIndictmentInfo3());
        newCase.setIndictmentInfo4(oldCase.getIndictmentInfo4());
        newCase.setIndictmentInfo5(oldCase.getIndictmentInfo5());
        newCase.setIndictmentInfo6(oldCase.getIndictmentInfo6());
        newCase.setJudgeReasonForAppeal(oldCase.getJudgeReasonForAppeal());                
        newCase.setLengthTape(oldCase.getLengthTape());
        newCase.setLinkedCaseID(oldCase.getLinkedCaseId());
        
        Calendar calMagConvictionDate = null;
        if (oldCase.getMagConvictionDate() != null) {
            calMagConvictionDate = Calendar.getInstance();
            calMagConvictionDate.setTime(oldCase.getMagConvictionDate());
        }
        newCase.setMagConvictionDate(calMagConvictionDate);
        
        newCase.setNoPageProsEvidence(oldCase.getNoPageProsEvidence());
        newCase.setNoProsWitness(oldCase.getNoProsWitness());        
        newCase.setOffenceGroupCode(oldCase.getOffenceGroupUpdate());        
        newCase.setPoliceOfficerAttending(oldCase.getPoliceOfficerAttending());
        newCase.setReceiptType(oldCase.getReceiptType());
        newCase.setRefCourtID(oldCase.getRefCourtId());
        newCase.setResultsVerified(oldCase.getResultsVerified());
        newCase.setCivilUnrest(oldCase.getCivilUnrest());
        newCase.setVersion(oldCase.getVersion());
        
        //newCase.setDateTransFrom(oldCase.getDateTransFrom());
        //newCase.setDateTransTo(oldCase.getDateTransTo());
        
        return newCase;
    }
}
