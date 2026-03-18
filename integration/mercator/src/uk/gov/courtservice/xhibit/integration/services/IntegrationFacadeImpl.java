package uk.gov.courtservice.xhibit.integration.services;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.defendant.DefendantUpdateIntController;
import uk.gov.courtservice.xhibit.integration.services.hearingrecord.HearingRecordUpdateIntController;
import uk.gov.courtservice.xhibit.integration.services.hearingschedule.HearingScheduleUpdateIntController;
import uk.gov.courtservice.xhibit.integration.services.prehearing.PreHearingUpdateIntController;
import uk.gov.courtservice.xhibit.integration.services.referencedata.ReferenceDataIntController;
import uk.gov.courtservice.xhibit.integration.services.results.ResultsUpdateIntController;

/**
 * <p>
 * Title: IntegrationFacadeImpl
 * </p>
 * <p>
 * Description: This implementation services all the method calls to Mercator
 * for the middle tier.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.1
 * 
 * @history Updated to include new UpdateCase
 */

public class IntegrationFacadeImpl implements IntegrationFacade {
    /**
     * @roseuid 3DDBB1D100E2
     */
    public IntegrationFacadeImpl() {
    }

    /**
     * @param offenceValue
     * @return uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue
     * @roseuid 3DDBB1D10178
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        return getPreHearingController().addOffence(offenceValue);
    }
    /**
     * @param offenceValue
     * @return uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue
     * @roseuid 3DDBB1D10178
     */
    public Integer addJoinderOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        return getPreHearingController().addJoinderOffence(offenceValue);
    }

    /**
     * @param offenceChargeID
     * @roseuid 3DDBB1D10197
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().deleteOffence(delOffenceVal);
    }

    /**
     * @param breachValue
     * @return uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue
     * @roseuid 3DDBB1D101C9
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().updateBreach(breachValue);
    }
    
    /**
     * @param caseValue
     * @roseuid 3DB00A237809
     */
    public void updateCase(CaseUpdateValue caseUpdateValue) throws MercatorException, TransformationException,
            OutputTransformationException{
        getPreHearingController().updateCase(caseUpdateValue);
    }
    

    /**
     * @param offenceValue
     * @roseuid 3DDBB1D101E7
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().updateOffence(offenceValue);
    }

    /**
     * @param chargeValue
     * @return uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue
     * @roseuid 3DDBB1D101FB
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        return getPreHearingController().addChargeToCase(chargeValue);
    }

    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException,
            TransformationException, OutputTransformationException {
        return getPreHearingController().addJoinderChargeToCase(chargeValue);
    }

    /**
     * @param chargeID
     * @roseuid 3DDBB1D10219
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().deleteCharge(delChargeVal);
    }

    /**
     * @param chargeID
     * @param numberOfDays
     * @roseuid 3DDBB1D101AB
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().signIndictment(signIndVal);
    }

    /**
     * @param defendantValue
     * @return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue
     * @roseuid 3DDBB1D1036D
     */
    public void updateDefendant(DefendantValue defendantValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        getDefendantController().updateDefendant(defendantValue);
    }

    public void exportCharges(Integer caseId) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().exportCharges(caseId);
    }

    public void updateDefendantOnCountStatus(Collection defendantCountList, String newStatus) throws MercatorException,
            TransformationException, OutputTransformationException {
        getPreHearingController().updateDefendantOnCountStatus(defendantCountList, newStatus);
    }

    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        getPreHearingController().linkCountsAndDefendants(linkVal);
    }

    /**
     * This method creates the requested case from Crest. Bench Warrant can be
     * issued for cases that we not heard on that day.
     * 
     * @param vo
     * @throws MercatorException,
     *             TransformationException, OutputTransformationException,
     *             TransformationException
     */
    public AddCaseValue getCase(AddCaseValue addCaseValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        return getHearingScheduleController().getCase(addCaseValue);
    }

    /**
     * These methods bring in Reference Data
     * 
     * @param courtID
     * @throws MercatorException,
     *             TransformationException, OutputTransformationException,
     *             TransformationException
     */
    public void importOffenceRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        getRefDataController().importOffenceRefData(courtID);
    }

    public void importGlobalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        getRefDataController().importGlobalRefData(courtID);
    }

    public void importLocalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException, TransformationException {
        getRefDataController().importLocalRefData(courtID);
    }

    public void importLocalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException, TransformationException {
        getRefDataController().importLocalOverrideRefData(courtID);
    }

    public void importGlobalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException, TransformationException {
        getRefDataController().importGlobalOverrideRefData(courtID);
    }

    /**
     * This method is used to create/update/delete results for a case. These
     * include Pleas, Verdicts, Disposals and Case results.
     * 
     * @param ResultsValue
     * 
     */
    public ResultsSaveValue setResults(ResultsSaveValue resultValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        return getResultsUpdateIntController().setResults(resultValue);
    }

    /**
     * This method informs Mercator to export the hearing record(s) to CREST. It
     * references the PK on the ExportA table in the database. Mercator gets the
     * information from the database.
     * 
     * @param exportAID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     */
    public void exportHearingRecord(Integer exportAID) throws MercatorException, TransformationException,
            OutputTransformationException, TransformationException {
        getHearingRecordController().exportHearingRecord(exportAID);
    }

    /**
     * This method is used to create/update/delete Original Charges. 
     * 
     * @param OriginalChargeValue 
     * 
     */
    public OriginalChargeVO[] maintainOriginalCharge(OriginalChargeVO[] originalChargeValues) 
             throws MercatorException, TransformationException, OutputTransformationException
    {
        Integer[] defOnOffenceIds = getPreHearingController().maintainOriginalCharge(originalChargeValues);
        
        if(defOnOffenceIds!=null && defOnOffenceIds.length>0 && 
           defOnOffenceIds.length==originalChargeValues.length)
        {
            for(int i=0;i<defOnOffenceIds.length;i++)
            {
                originalChargeValues[i].setDefendantOnOffenceId(defOnOffenceIds[i]);
            }
        }
        
        return originalChargeValues;
    }
    
    /**
     * This method is used to update DefendantOnOffence. 
     * 
     * @param DefendantOnOffenceValue 
     * 
     */
    public void updateDefendantOnOffence(LinkCountDefValue linkCountDefValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        getPreHearingController().updateDefendantOnOffence(linkCountDefValue);   
    }
    
    /**
     * This method is used to update Offence and DefendantOnOffence. 
     * 
     * @param OffenceValue 
     * 
     */
    public void updateOffenceAndDefOnOffence(OffenceValue offenceValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        getPreHearingController().updateOffenceAndDefOnOffence(offenceValue);   
    }
    
    /**
     * This method is used to update DefendantOnCase. 
     * 
     * @param DefendantOnCaseBasicValue  
     * 
     */
    public void updateDefendantOnCase(DefendantOnCaseValue defendantOnCaseValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        getDefendantController().updateDefendantOnCase(defendantOnCaseValue);  
    }   
    
    private PreHearingUpdateIntController getPreHearingController() throws MercatorException, TransformationException,
            OutputTransformationException, TransformationException {
        return new PreHearingUpdateIntController();
    }

    private DefendantUpdateIntController getDefendantController() throws MercatorExecutionException {
        return new DefendantUpdateIntController();
    }

    private HearingScheduleUpdateIntController getHearingScheduleController() throws MercatorExecutionException {
        return new HearingScheduleUpdateIntController();
    }

    private ReferenceDataIntController getRefDataController() throws MercatorExecutionException {
        return new ReferenceDataIntController();
    }

    private HearingRecordUpdateIntController getHearingRecordController() throws MercatorExecutionException {
        return new HearingRecordUpdateIntController();
    }

    private ResultsUpdateIntController getResultsUpdateIntController() throws MercatorExecutionException {
        return new ResultsUpdateIntController();
    }
}