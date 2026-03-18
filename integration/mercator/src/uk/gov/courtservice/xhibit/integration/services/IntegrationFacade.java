package uk.gov.courtservice.xhibit.integration.services;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;

/**
 * This interface is managing the databases updates (CREST and XHBIT) for
 * pre-hearing related data.
 */
public interface IntegrationFacade {

    /**
     * This method is used to add an offence to the current charge.
     * 
     * @param offenceValue
     * @roseuid 3DAEB90C02E4
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method is used to add an offence to the joinder.
     * 
     * @param offenceValue
     * @roseuid 3DAEB90C02E4
     */
    public Integer addJoinderOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException;
    /**
     * This method deletes the offence corresponding to the offence ID passed as
     * parameter.
     * 
     * @param delOffenceVal
     * @roseuid 3DAEB95F001C
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method updates the breach passed as parameter.
     * 
     * @param breachValue
     * @roseuid 3DB00A230109
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method updates the case passed as parameter.
     * 
     * @param caseValue
     * @roseuid 3DB00A237809
     */
    public void updateCase(CaseUpdateValue caseValue) throws MercatorException, TransformationException,
            OutputTransformationException;
    
    /**
     * This method updates the offence passed as parameter.
     * 
     * @param offenceValue
     * @roseuid 3DAEB93D0186
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method is used to add a charge to the current case.
     * 
     * @param chargeValue
     * @roseuid 3DB8018A0173
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException,
            TransformationException, OutputTransformationException;

    /**
     * This method deletes the charge corresponding to the charge ID passed as
     * parameter.
     * 
     * @param delChargeVal
     * @roseuid 3DB812CE0133
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method will update the defendant details.
     * 
     * @param defendantValue
     * @roseuid 3DDB951503CF
     */
    public void updateDefendant(DefendantValue defendantValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * @param caseId
     * @throws uk.gov.courtservice.xhibit.integration.MercatorException
     * @roseuid 3DE34EA202A8
     */
    public void exportCharges(Integer caseId) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * @param signIndVal
     * @roseuid 3DE4D2220034
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * @param defendantCountList
     * @param newStatus
     * @roseuid 3DE4D2B4023D
     */
    public void updateDefendantOnCountStatus(Collection defendantCountList, String newStatus) throws MercatorException,
            TransformationException, OutputTransformationException;

    /**
     * @param linkVal
     * @roseuid 3DE4D456037E
     */
    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method creates the requested case from Crest. Bench Warrant can be
     * issued for cases that we not heard on that day.
     * 
     * @param vo
     * @throws MercatorException,
     *             TransformationException, OutputTransformationException
     */
    public AddCaseValue getCase(AddCaseValue addCaseValue) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method will import the global Offences for a specific court.
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF214602EF
     */
    public void importOffenceRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method will import all the global reference data except for
     * Offences. The global reference data that will be imported:
     * 
     * SYSTEM_REF_CODES DISPOSAL DISPOSAL_MENU HEARING_TYPE REF_COURTs
     * 
     * @param courtID -
     * 
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21C1022E
     */
    public void importGlobalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * this method will import all the local reference data, such as:
     * 
     * JUDGE JUSTICE COURT_REPORTER COURT_REPORTER_FIRM SOLICITOR_FIRM
     * PROSECUTOR_AGENCY
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21EE03AF
     */
    public void importLocalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method will import all the local reference data for Global data with
     * local override. This means that the data can be either local or global.
     * This method will only import the local entries. Local data to be
     * imported:
     * 
     * ADVOCATES CHAMBERS
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF21FA0077
     */
    public void importLocalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method will import all the global reference data for Global data
     * with local override. This means that the data can be either local or
     * global. This method will only import the global entries. Global data to
     * be imported:
     * 
     * ADVOCATES CHAMBERS
     * 
     * @param courtID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     * @roseuid 3DEF220C0290
     */
    public void importGlobalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method informs Mercator to export the hearing record(s) to CREST. It
     * references the PK on the ExportA table in the database. Mercator gets the
     * information from the database.
     * 
     * @param exportAID
     * @throws uk.gov.courtservice.xhibit.integration.services.MercatorException
     */
    public void exportHearingRecord(Integer exportAID) throws MercatorException, TransformationException,
            OutputTransformationException;

    /**
     * This method is used to create/update/delete results for a case. These
     * include Pleas, Verdicts, Disposals and Case results.
     * 
     * @param ResultsValue
     * 
     */
    public ResultsSaveValue setResults(ResultsSaveValue resultValue) throws MercatorException, TransformationException,
            OutputTransformationException;
    
    /**
     * This method is used to create/update/delete Original Charges. 
     * 
     * @param OriginalChargeValue 
     * 
     */
    public OriginalChargeVO[] maintainOriginalCharge(OriginalChargeVO[] originalChargeValues) 
             throws MercatorException, TransformationException, OutputTransformationException;
    
    /**
     * This method is used to update DefendantOnOffence. 
     * 
     * @param DefendantOnOffenceValue 
     * 
     */
    public void updateDefendantOnOffence(LinkCountDefValue linkCountDefValue) 
              throws MercatorException, TransformationException, OutputTransformationException;
    
    /**
     * This method is used to update Offence and DefendantOnOffence. 
     * 
     * @param OffenceValue 
     * 
     */
    public void updateOffenceAndDefOnOffence(OffenceValue offenceValue) 
              throws MercatorException, TransformationException, OutputTransformationException;
    
    /**
     * This method is used to update DefendantOnCase. 
     * 
     * @param DefendantOnCaseBasicValue  
     * 
     */
    public void updateDefendantOnCase(DefendantOnCaseValue defendantOnCaseValue) 
              throws MercatorException, TransformationException, OutputTransformationException;    

}