package uk.gov.courtservice.xhibit.integration.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
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
import uk.gov.courtservice.xhibit.common.results.vos.ResultValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.stub.CaseStub;
import uk.gov.courtservice.xhibit.integration.services.stub.DefendantUpdateStub;
import uk.gov.courtservice.xhibit.integration.services.stub.HearingRecordUpdateStub;
import uk.gov.courtservice.xhibit.integration.services.stub.PreHearingAddStub;
import uk.gov.courtservice.xhibit.integration.services.stub.PreHearingUpdateStub;
import uk.gov.courtservice.xhibit.integration.services.stub.ResultsUpdateStub;

/**
 * <p>
 * Title: IntegrationStubImpl
 * </p>
 * <p>
 * Description: Provides implementations of the integration facade calls which
 * do not require mercator.
 * </p>
 * 
 * <p>
 * These methods mimic the behaviour of the mercator API however ony produce
 * results in Xhibit and not in Crest. This stub is intened for used in
 * development environments where connection with the mercator API is not
 * possible.
 * </p>
 * 
 * <p>
 * To use these stubbed methods set the system property 'disableMercatorUse' to
 * true. This will cause the <code>IntegrationFactoryFacade</code> to return
 * an instance of this Impl rather than <code>IntegrationFacadeImpl</code>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: IntegrationStubImpl.java,v 1.29 2010/02/01 09:58:00 pokalak Exp $
 * @author GJS (2006) Added functionality to test XML data
 */

public class IntegrationStubImpl implements IntegrationFacade {
    // the logger
    private static Logger log = CSServices.getLogger(IntegrationStubImpl.class);

    public IntegrationStubImpl() {
    }

    /**
     * Calls the method on the <code>PreHearingAddStub</code> which mimics the
     * same method on the <code>IntegrationFacadeImple</code>. Updates are
     * not made to CREST, inserts will be made to the xhb_offence, and
     * xhb_defendant_on_offence tables with values from the supplied VO.
     * 
     * @param offenceValue
     *            Contains the details to be added to xhibit
     * @return The offenceId of the new offence
     * @throws MercatorException
     *             never
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException {
        log.debug("***** STUB: addOffence *****");
        testXmlGeneration(offenceValue, "addOffence");
        PreHearingAddStub phAddStub = new PreHearingAddStub();
        return phAddStub.addOffence(offenceValue);
    }

    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. Updates
     * are no made to CREST, updates will be made to some or all of xhb_offence,
     * xhb_defendant_on_offence, xhb_charge, xhb_defendant_charge, xhb_breach
     * 
     * @param delOffenceVal
     *            Identifies which offence to delete
     * @throws MercatorException
     *             never
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException {
        log.debug("***** STUB: deleteOffence *****");
        testXmlGeneration(delOffenceVal, "deleteOffence");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.deleteOffence(delOffenceVal);
    }

    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. Updates
     * are not made to CREST, the xhb_breach table, xhb_charge and in some cases
     * xhb_plea tables are updated with vaules from the supplied VO
     * 
     * @param breachValue
     *            Contains the values with which to update the breach
     * @throws MercatorException
     *             never
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException {
        log.debug("***** STUB: updateBreach *****");
        testXmlGeneration(breachValue, "updateBreach");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.updateBreach(breachValue);
    }
    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. Updates
     * are not made to CREST, the xhb_case table are updated with vaules from the supplied VO
     * 
     * @param caseValue
     *            Contains the values with which to update the breach
     * @throws MercatorException
     *             never
     */
    public void updateCase(CaseUpdateValue caseValue) throws MercatorException {
        log.debug("***** STUB: updateCase *****");
        testXmlGeneration(caseValue, "updateCase");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.updateCase(caseValue);
    }

    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. Updates
     * are not made to CREST, the xhb_offence table is updated with vaules from
     * the supplied VO
     * 
     * @param offenceValue
     *            Contains the values with which to update the offence
     * @throws MercatorException
     *             never
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException {
        log.debug("***** STUB: offenceValue *****");
        testXmlGeneration(offenceValue, "updateOffence");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.updateOffence(offenceValue);
    }

    /**
     * Calls the method on the <code>PreHearingAddStub</code> which mimics the
     * same method on the <code>IntegrationFacadeImple</code>. Updates are
     * not made to CREST, inserts will be made to the xhb_charge, xhb_offence
     * and xhb_defendant_on_offence tables with values from the supplied VO. An
     * insert will be made to xhb_breach and xhb_defendant_on_charge tables if
     * the charge is a breach. The xhb_case table may be updated with dateIndRec
     * and indResp.
     * 
     * @param chargeValue
     *            Contains the details to be added to xhibit
     * @return The chargeId of the new charge
     * @throws MercatorException
     *             never
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException {
        log.debug("***** STUB: addChargeToCase *****");
        testXmlGeneration(chargeValue, "addChargeToCase");
        PreHearingAddStub phAddStub = new PreHearingAddStub();
        return phAddStub.addChargeToCase(chargeValue);
    }

    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException {
        log.debug("***** STUB: addJoinderChargeToCase (Charge Array) *****");
        testXmlGeneration(chargeValue, "addJoinderChargeToCase");
        PreHearingAddStub phAddStub = new PreHearingAddStub();
        return phAddStub.addJoinderChargeToCase(chargeValue);
    }

    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. Updates
     * are no made to CREST, updates will be made to some or all of xhb_offence,
     * xhb_defendant_on_offence, xhb_charge, xhb_defendant_charge, xhb_breach
     * 
     * @param delChargeVal
     *            Identifies which charge to delete
     * @throws MercatorException
     *             never
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException {
        log.debug("***** STUB: deleteCharge *****");
        testXmlGeneration(delChargeVal, "deleteCharge");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.deleteCharge(delChargeVal);
    }

    /**
     * Calls the method on the <code>DefendantUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. The
     * defendant is not updated in CREST, however the xhb_defendant, xhb_address
     * and xhb_defendant_on_case tables are updated in Xhibit
     * 
     * @param defendantValue
     *            Contains the values with which to update Xhibit
     * @throws MercatorException
     *             never
     */
    public void updateDefendant(DefendantValue defendantValue) throws MercatorException {
        log.debug("***** STUB: updateDefendant *****");
        testXmlGeneration(defendantValue, "updateDefendant");
        DefendantUpdateStub defUpdateStub = new DefendantUpdateStub();
        defUpdateStub.updateDefendant(defendantValue);
    }

    /**
     * Calls the method on the <code>PreHearingUpdateStub</code> which mimics
     * the same method on the <code>IntegrationFacadeImple</code>. The
     * Charges are not sent to CREST, however the status flag is set to
     * successful as it would be if this had occurred without error.
     * 
     * @param caseId
     *            The id of the case for which charges are exported.
     * @throws MercatorException
     */
    public void exportCharges(Integer caseId) throws MercatorException {
        log.debug("***** STUB: exportCharges (Integer) *****: " + caseId);
        testXmlGeneration(caseId, "exportCharges");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.exportCharges(caseId);
    }

    /**
     * Mimics the same method on the <code>IntegrationFacadeImple</code>.
     * Updates are not made to CREST, an update will be made to the xhb_charge
     * table with values from the supplied VO.
     * 
     * @param signIndVal
     *            Contains the details to be added to xhibit
     * @throws MercatorException
     *             never
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException {
        log.debug("***** STUB: signIndictment *****");
        testXmlGeneration(signIndVal, "signIndictment");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.signIndictment(signIndVal);
    }

    public void updateDefendantOnCountStatus(Collection defendantCountList, String newStatus) throws MercatorException {
        // stay defendant on count - this is not a mercator call, should not
        // be on the integration facace - it is never called
    }

    /**
     * Calls the method on the <code>PreHearingAddStub</code> which mimics the
     * same method on the <code>IntegrationFacadeImple</code>. Updates are
     * not made to CREST, inserts will be made to the xhb_defendant_on_offence
     * table with values from the supplied VO.
     * 
     * @param linkVal
     *            Contains the details to be added to xhibit
     * @throws MercatorException
     *             never
     */
    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException {
        log.debug("***** STUB: linkCountsAndDefendants *****");
        testXmlGeneration(linkVal, "linkCountsAndDefendants");
        PreHearingAddStub phAddStub = new PreHearingAddStub();
        phAddStub.linkCountsAndDefendants(linkVal);
    }

    /**
     * Calls a method on the <code>CaseStub</code> which mimics the
     * functionality of methods on the <code>IntegrationFacadeImpl</code>. If
     * will do one of two things:
     * <ul>
     * <li>if the createCaseOnCrest flag is true, a 'U' case is created on
     * XHIBIT
     * <li>if the createCaseOnCrest flag is false, return NULL, i.e. default
     * processing
     * </ul>
     */
    public AddCaseValue getCase(AddCaseValue addCaseValue) throws MercatorException {
        if (addCaseValue.isCreateCaseOnCrest()) {
            log.debug("***** STUB: getCase (AddCase) *****");
            testXmlGeneration(addCaseValue, "getCase");
            CaseStub caseStub = new CaseStub();
            return caseStub.addNewUCase(addCaseValue);
        }
        return null;
    }

    public void importOffenceRefData(Integer courtID) throws MercatorException {
        log.debug("***** STUB: importOffenceRefData (Integer) *****: " + courtID);
        testXmlGeneration(courtID, "importOffenceRefData");
    }

    public void importGlobalRefData(Integer courtID) throws MercatorException {
        log.debug("***** STUB: importGlobalRefData (Integer) *****: " + courtID);
        testXmlGeneration(courtID, "importGlobalRefData");
    }

    public void importLocalRefData(Integer courtID) throws MercatorException {
        log.debug("***** STUB: importLocalRefData (Integer) *****: " + courtID);
        testXmlGeneration(courtID, "importLocalRefData");
    }

    public void importLocalOverrideRefData(Integer courtID) throws MercatorException {
        log.debug("***** STUB: importLocalOverrideRefData (Integer) *****: " + courtID);
        testXmlGeneration(courtID, "importLocalOverrideRefData");
    }

    public void importGlobalOverrideRefData(Integer courtID) throws MercatorException {
        log.debug("***** STUB: importGlobalOverrideRefData (Integer) *****: " + courtID);
        testXmlGeneration(courtID, "importGlobalOverrideRefData");
    }

    /**
     * Calls the method on the <code>HearingRecordUpdateStub</code> which
     * mimics the same method on the <code>IntegrationFacadeImple</code>. The
     * Hearing Record is not sent to CREST, however the status flag is set to
     * successful as it would be if this had occurred without error.
     * 
     * @param exportAID
     *            The id of the exporta record for which to set the flag
     * @throws MercatorException
     *             never
     */
    public void exportHearingRecord(Integer exportAID) throws MercatorException {
        log.debug("***** STUB: exportHearingRecord (Integer) *****: " + exportAID);
        testXmlGeneration(exportAID, "exportHearingRecord");
        HearingRecordUpdateStub hrUpdateStub = new HearingRecordUpdateStub();
        hrUpdateStub.exportHearingRecord(exportAID);
    }

    public ResultsSaveValue setResults(ResultsSaveValue resultValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("***** STUB: setResults (ResultsSaveValue) *****");
        testXmlGeneration(resultValue, "setResults");
        ResultsUpdateStub resultsUpdateStub = new ResultsUpdateStub();
        return resultsUpdateStub.setResults(resultValue);
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
        log.debug("***** STUB: maintainOriginalCharge (OriginalChargeVO Array) *****");
        testXmlGeneration(originalChargeValues, "maintainOriginalCharge");
        PreHearingAddStub phAddStub = new PreHearingAddStub();
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        OriginalChargeVO originalChargeValue = null;
        
        for(int i=0;i<originalChargeValues.length;i++)
        {
            originalChargeValue = originalChargeValues[i];
            
            if(originalChargeValue.getTrxCode().equalsIgnoreCase("C"))
            {
                log.debug("***** Created Original Change, Case ID: " + originalChargeValue.getCaseId());  
                originalChargeValue.setDefendantOnOffenceId(phAddStub.addOriginalCharge(originalChargeValue));
                log.debug("***** Successfully Created Original Charge DOOID : " + originalChargeValue.getDefendantOnOffenceId());
            }
            else if(originalChargeValue.getTrxCode().equalsIgnoreCase("U"))
            {
                log.debug("***** Updated Original Change, DOO ID: " + originalChargeValue.getDefendantOnOffenceId());  
                phUpdateStub.updateOriginalCharge(originalChargeValue);
                log.debug("***** Successfully Updated Original Charge DOO ID : " + originalChargeValue.getDefendantOnOffenceId());
            }
            else if(originalChargeValue.getTrxCode().equalsIgnoreCase("D"))
            {
                log.debug("***** Deleted Original Change, Charge ID: " + originalChargeValue.getChargeId());  
                phUpdateStub.deleteOriginalCharge(originalChargeValue);
                log.debug("***** Successfully Updated Original Charge Charge ID : " + originalChargeValue.getChargeId());
            }
            else
            {
               log.error("**** Invalid TrxCode: " + originalChargeValue.getTrxCode());
               throw new RuntimeException();
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
    public void updateDefendantOnOffence(LinkCountDefValue linkDefendantOnOffenceValue) 
              throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("***** STUB: updateDefendantOnOffence (DefendantOnOffenceValue) *****");
        testXmlGeneration(linkDefendantOnOffenceValue, "updateDefendantOnOffence");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.updateDefendantOnOffence(linkDefendantOnOffenceValue);
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
        log.debug("***** STUB: updateOffenceAndDefOnOffence (OffenceValue) *****");
        testXmlGeneration(offenceValue, "updateOffenceAndDefOnOffence");
        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
        phUpdateStub.updateOffenceAndDefOnOffence(offenceValue);
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
        log.debug("***** STUB: updateDefendantOnCase (DefendantOnCaseValue) *****");
        testXmlGeneration(defendantOnCaseValue, "updateDefendantOnCase");
        DefendantUpdateStub defUpdateStub = new DefendantUpdateStub();
        defUpdateStub.updateDefendantOnCase(defendantOnCaseValue);
    }   
    
    /**
     * 
     * @param OffenceValue 
     */
    
    public Integer addJoinderOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
    OutputTransformationException{
        return null;
        }
      
    /**
     * Generic method to test the XML transform of the CSAbstractValue All
     * exceptions logged and suppressed
     * 
     * @param CSAbstractValue
     *            value: the CSValueObject
     * @param String
     *            operation: the name of the business operation being performed
     */
    private void testXmlGeneration(CSAbstractValue value, String operation) {
        try {
            executeXmlTransform(value, operation, getIntegrationFacadeImpl());
        } catch (Exception e) {
            log.debug("***** testXmlGeneration (CSAbstractValue) Exception: " + e.getMessage() + e);
            e.printStackTrace();
        }
    }

    /**
     * Generic method to test the XML transform of the CSAbstractValue arrays
     * All exceptions logged and suppressed
     * 
     * @param CSAbstractValue[]
     *            value: the CSValueObject array
     * @param String
     *            operation: the name of the business operation being performed
     */
    private void testXmlGeneration(CSAbstractValue[] value, String operation) {
        try {
            executeXmlTransform(value, operation, getIntegrationFacadeImpl());
        } catch (Exception e) {
            log.debug("***** testXmlGeneration (CSAbstractValue[]) Exception: " + e.getMessage() + e);
            e.printStackTrace();
        }
    }

    /**
     * Generic method to test the XML transform of an Integer All exceptions
     * logged and suppressed
     * 
     * @param Integer
     *            value:
     * @param String
     *            operation: the name of the business operation being performed
     */
    private void testXmlGeneration(Integer value, String operation) {
        try {
            executeXmlTransform(value, operation, getIntegrationFacadeImpl());
        } catch (Exception e) {
            log.debug("***** testXmlGeneration (Integer) Exception: " + e.getMessage() + e);
            e.printStackTrace();
        }
    }

    /**
     * Generic method to test the XML transform of the ResultValue All
     * exceptions logged and suppressed
     * 
     * @param ResultValue
     *            value: the ResultValue
     * @param String
     *            operation: the name of the business operation being performed
     */
    private void testXmlGeneration(ResultValue value, String operation) {
        try {
            executeXmlTransform(value, operation, getIntegrationFacadeImpl());
        } catch (Exception e) {
            log.debug("***** testXmlGeneration (ResultValue) Exception: " + e.getMessage() + e);
            e.printStackTrace();
        }
    }

    private IntegrationFacadeImpl getIntegrationFacadeImpl() {
        return new IntegrationFacadeImpl();
    }

    /**
     * Generic method to execute the XML Transform via reflection. The method
     * executed on IntegrationFacadeImpl should have the same name as on
     * IntegrationStubImpl
     * 
     * The system is designed so if disableMercatorUse is true the Mercator
     * Wrapper Type will always be Stub in IntegrationFacadeImpl so we can
     * execute these methods in a testing environment (ie one where there is no
     * mercator backend)
     * 
     * All exceptions logged and suppressed
     * 
     * @param Object
     *            value: the parameter
     * @param String
     *            methodName: the name of the method to execute on the
     *            IntegrationFacadeImpl via reflection
     * @param IntegrationFacadeImpl
     *            integrationFacadeImpl: does the Xml transform of the parameter
     */
    private void executeXmlTransform(Object value, String methodName, IntegrationFacadeImpl integrationFacadeImpl) {
        if (log.isDebugEnabled()) {
            log.debug("***** executeXmlTransform methodName: " + methodName);
            log.debug("***** executeXmlTransform value: " + value.getClass());
        }
        
       
        
        if(value instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue)
        {
            log.debug("***** class is an OffenceValue");
            uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue offenceValue = new uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue();
            Class[] methodParamTypes = { offenceValue.getClass() };
        
            Object[] parameters = { value };
            Method m = null;

            try {
                m = integrationFacadeImpl.getClass().getMethod(methodName, methodParamTypes);
            } catch (NoSuchMethodException nsme) {
                log.debug("***** executeXmlTransform NoSuchMethodException: " + nsme.getMessage() + nsme);
                nsme.printStackTrace();
            }

            try {
                m.invoke(integrationFacadeImpl, parameters);
            } catch (IllegalAccessException iae) {
                log.debug("***** executeXmlTransform IllegalAccessException: " + iae.getMessage() + iae);
                iae.printStackTrace();
            } catch (InvocationTargetException ite) {
                log.debug("***** executeXmlTransform InvocationTargetException: " + ite.getMessage() + ite);
                ite.printStackTrace();
            }        
        }
        else
        {
            log.debug("***** class is NOT an OffenceValue");
            
            Class[] methodParamTypes = { value.getClass() };
            
            Object[] parameters = { value };
            Method m = null;

            try {
                m = integrationFacadeImpl.getClass().getMethod(methodName, methodParamTypes);
            } catch (NoSuchMethodException nsme) {
                log.debug("***** executeXmlTransform NoSuchMethodException: " + nsme.getMessage() + nsme);
                nsme.printStackTrace();
            }

            try {
                m.invoke(integrationFacadeImpl, parameters);
            } catch (IllegalAccessException iae) {
                log.debug("***** executeXmlTransform IllegalAccessException: " + iae.getMessage() + iae);
                iae.printStackTrace();
            } catch (InvocationTargetException ite) {
                log.debug("***** executeXmlTransform InvocationTargetException: " + ite.getMessage() + ite);
                ite.printStackTrace();
            }
        }
    }
}