/*
 * Created on Jun 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package uk.gov.courtservice.xhibit.integration.services;

import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
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

/**
 * @author pznwc5
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 * @history 
 */
public class ExceptionTracingFacadeImpl implements IntegrationFacade {

    // Underlying facade
    private IntegrationFacade facade;

    // Logger
    private static final Logger LOG = Logger.getLogger(ExceptionTracingFacadeImpl.class);

    // Decoarator constructor
    public ExceptionTracingFacadeImpl(IntegrationFacade facade) {
        this.facade = facade;
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#addOffence(uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue)
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            return facade.addOffence(offenceValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
    }
    /**
     (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#addJoinderOffence(uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue)
    */
    
    public Integer addJoinderOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
    OutputTransformationException{
        try {
            return facade.addJoinderOffence(offenceValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
        }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#deleteOffence(uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue)
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.deleteOffence(delOffenceVal);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#updateBreach(uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue)
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.updateBreach(breachValue);
        } catch (Exception ex) {
            handleException(ex);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#updateCase(uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue)
     */
    public void updateCase(CaseUpdateValue caseUpdateValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.updateCase(caseUpdateValue);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#updateOffence(uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue)
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.updateOffence(offenceValue);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#addChargeToCase(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue)
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            return facade.addChargeToCase(chargeValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#addJoinderChargeToCase(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue[])
     */
    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException,
            TransformationException, OutputTransformationException {
        try {
            return facade.addJoinderChargeToCase(chargeValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#deleteCharge(uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue)
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.deleteCharge(delChargeVal);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#updateDefendant(uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue)
     */
    public void updateDefendant(DefendantValue defendantValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.updateDefendant(defendantValue);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#exportCharges(java.lang.Integer)
     */
    public void exportCharges(Integer caseId) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.exportCharges(caseId);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#signIndictment(uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue)
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.signIndictment(signIndVal);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#updateDefendantOnCountStatus(java.util.Collection,
     *      java.lang.String)
     */
    public void updateDefendantOnCountStatus(Collection defendantCountList, String newStatus) throws MercatorException,
            TransformationException, OutputTransformationException {
        try {
            facade.updateDefendantOnCountStatus(defendantCountList, newStatus);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#linkCountsAndDefendants(uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue)
     */
    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.linkCountsAndDefendants(linkVal);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#getCase(uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.AddCaseValue)
     */
    public AddCaseValue getCase(AddCaseValue addCaseValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            return facade.getCase(addCaseValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#importOffenceRefData(java.lang.Integer)
     */
    public void importOffenceRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.importOffenceRefData(courtID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#importGlobalRefData(java.lang.Integer)
     */
    public void importGlobalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.importGlobalRefData(courtID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#importLocalRefData(java.lang.Integer)
     */
    public void importLocalRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.importLocalRefData(courtID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#importLocalOverrideRefData(java.lang.Integer)
     */
    public void importLocalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.importLocalOverrideRefData(courtID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#importGlobalOverrideRefData(java.lang.Integer)
     */
    public void importGlobalOverrideRefData(Integer courtID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.importGlobalOverrideRefData(courtID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#exportHearingRecord(java.lang.Integer)
     */
    public void exportHearingRecord(Integer exportAID) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            facade.exportHearingRecord(exportAID);
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see uk.gov.courtservice.xhibit.integration.services.IntegrationFacade#setResults(uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue)
     */
    public ResultsSaveValue setResults(ResultsSaveValue resultValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        try {
            return facade.setResults(resultValue);
        } catch (Exception ex) {
            handleException(ex);
            return null;
        }
    }

    private void handleException(Exception ex) throws MercatorException, TransformationException,
            OutputTransformationException {
        LOG.fatal("**************************************************************");
        LOG.fatal("Exception thown from Integration: " + ex.getClass());
        LOG.fatal(ex.getMessage(), ex);
        if (ex instanceof MercatorException) {
            LOG.fatal("Return MVO: " + ((MercatorException) ex).getReturnValue());
            throw (MercatorException) ex;
        }
        // LOG.fatal("**************************************************************");
        else if (ex instanceof TransformationException)
            throw (TransformationException) ex;
        else if (ex instanceof OutputTransformationException)
            throw (OutputTransformationException) ex;
        else
            throw new CSUnrecoverableException(ex);
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
        try {
            return facade.maintainOriginalCharge(originalChargeValues); 
        } catch (Exception ex) {
            handleException(ex);
        } 
        return null;
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
        try {
            facade.updateDefendantOnOffence(linkCountDefValue); 
        } catch (Exception ex) {
            handleException(ex);
        }    
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
        try {
            facade.updateOffenceAndDefOnOffence(offenceValue); 
        } catch (Exception ex) {
            handleException(ex);
        }   
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
        try {
            facade.updateDefendantOnCase(defendantOnCaseValue); 
        } catch (Exception ex) {
            handleException(ex);
        }     
    }              
}
