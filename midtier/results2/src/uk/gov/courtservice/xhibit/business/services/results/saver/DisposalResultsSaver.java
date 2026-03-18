package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailService;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLine;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.services.darts.DartsHelper;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This interface defines what the ResultsSaver can do.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Revision: 1.22 $
 */
public class DisposalResultsSaver extends AbstractResultsSaver {
    private static final Logger log = CSServices.getLogger(DisposalResultsSaver.class);

    private DartsHelper dartsHelper;
    
    public void preprocess(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null result.");
        }

        if (!(resultSaveValue instanceof DisposalSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not preprocess result of type "
                    + resultSaveValue.getClass().getName() + ".");
        }
        preprocess((DisposalSaveValue) resultSaveValue);
    }

    public void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null result.");
        }

        if (!(resultSaveValue instanceof DisposalSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save result of type " + resultSaveValue.getClass().getName()
                    + ".");
        }

        save((DisposalSaveValue) resultSaveValue);
    }

    public void saveCrestKeys(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save crest keys for null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save crest keys for null result.");
        }

        if (!(resultSaveValue instanceof DisposalSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save crest keys for  result of type "
                    + resultSaveValue.getClass().getName() + ".");
        }

        saveCrestKeys((DisposalSaveValue) resultSaveValue);
    }

    public void log(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null result.");
        }

        if (!(resultSaveValue instanceof DisposalSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not log result of type " + resultSaveValue.getClass().getName() + ".");
        }

        DisposalCourtLogHelper.getInstance().log((DisposalSaveValue) resultSaveValue);
    }

    private void save(DisposalSaveValue result) throws ResultsControllerException {
        String operation = result.getOperation();
        if (operation.equals(ResultSaveValue.ADD)) {
            createDisposal(result);
            saveDeportationAndHateCrimeDetails(result);
        } else if (operation.equals(ResultSaveValue.UPDATE)) {
            //Got to do the audit here becaause UpdateDisposal is used elsewhere
            Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
            hashtable.put(XhbDisposal2BasicValue.class.getName(), result.getDisposalBasicValue());
            AuditTrailService auditService = CSServices.getAuditTrailService();
            AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
            event.setSuccess(true);
            auditService.createAuditRecord(event);
            updateDisposal(result, false);
            saveDeportationAndHateCrimeDetails(result);
        } else if (operation.equals(ResultSaveValue.DELETE)) {
            deleteDisposal(result);
        } else {
            throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation", new Object[] { result
                    .getOperation() }, "Unrecognised operation " + result.getOperation() + ".");
        }
    }
    
    private boolean isDefined(String value) {
        return value != null && !value.equals("");
    }
    
    /**
     * Description: Retrieves the defendant on case value and updates with the deportation reason.
     * @param results
     * @param index
     * @throws ResultsControllerException
     */
    private void saveDeportationAndHateCrimeDetails(DisposalSaveValue result) throws ResultsControllerException {
        log.debug("saveDeportationAndHateCrimeDetails(DisposalSaveValue result) : START");
        
        try {
            DefendantHelper temp = new DefendantHelper();
            
            ////Retreive defendant details
            DefendantOnCase doc = temp.getDefendantOnCaseDetails(result.getDefendantOnCaseId());
            DefendantValue defV = temp.getDefendantDetailsWithAddress(doc.getDefendantId(), doc.getCaseId());
            
            //update defendant details
            if (isDefined(result.getDisposalReferenceValue().getCustodial())) {
                defV.getDefOnCaseBasicValue().setCustodial(result.getDisposalReferenceValue().getCustodial());
            }
            else{
                defV.getDefOnCaseBasicValue().setCustodial("");
            }
             
            if (isDefined(result.getDisposalReferenceValue().getSuspended())) {
                defV.getDefOnCaseBasicValue().setSuspended(result.getDisposalReferenceValue().getSuspended());
            }else{
                defV.getDefOnCaseBasicValue().setSuspended("");
            }
            
            if (isDefined(result.getDisposalReferenceValue().getSeriousDrugOffence())) {
                defV.getDefOnCaseBasicValue().setSeriousDrugOffence(result.getDisposalReferenceValue().getSeriousDrugOffence());
            }else{
                defV.getDefOnCaseBasicValue().setSeriousDrugOffence("");
            }
            
            if (isDefined(result.getDisposalReferenceValue().getRecommendedDeportation())) {
                defV.getDefOnCaseBasicValue().setRecommendedDeportation(result.getDisposalReferenceValue().getRecommendedDeportation());
            }else{
                defV.getDefOnCaseBasicValue().setRecommendedDeportation("");
            }
            
            // General Disability - Ref = 1
            if (result.getDisposalReferenceValue().isGeneralDisability()) {
                defV.getDefOnCaseBasicValue().setGeneralDisability(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setGeneralDisability(false);
            }
            
            // Victim Disability - Ref = 2
            if (result.getDisposalReferenceValue().isVictimDisability()) {
                defV.getDefOnCaseBasicValue().setVictimDisability(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setVictimDisability(false);
            }
            
            // Racial Aggravation - Ref = 3
            if (result.getDisposalReferenceValue().isRacialAggravated()) {
                defV.getDefOnCaseBasicValue().setRacialAggravated(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setRacialAggravated(false);
            }
            
            // Race and Religious Aggravation - Ref = 4
            if (result.getDisposalReferenceValue().isRaceAndReligionAggravated()) {
                defV.getDefOnCaseBasicValue().setRaceAndReligionAggravated(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setRaceAndReligionAggravated(false);
            }
            
            // Religious Aggravation - Ref = 5
            if (result.getDisposalReferenceValue().isReligionAggravated()) {
                defV.getDefOnCaseBasicValue().setReligionAggravated(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setReligionAggravated(false);
            }
            
            // General Sexual - Ref = 6
            if (result.getDisposalReferenceValue().isGeneralSexual()) {
                defV.getDefOnCaseBasicValue().setGeneralSexual(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setGeneralSexual(false);
            }
            
            // Victim Sexual - Ref = 7
            if (result.getDisposalReferenceValue().isVictimSexual()) {
                defV.getDefOnCaseBasicValue().setVictimSexual(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setVictimSexual(false);
            }
            
            // General Transgender - Ref = 8
            if (result.getDisposalReferenceValue().isGeneralTransgender()) {
                defV.getDefOnCaseBasicValue().setGeneralTransgender(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setGeneralTransgender(false);
            }
            
            // Victim Transgender - Ref = 9
            if (result.getDisposalReferenceValue().isVictimTransgender()) {
                defV.getDefOnCaseBasicValue().setVictimTransgender(true);
            } else { // Its now been de-selected, so check whether it needs to be deleted
                defV.getDefOnCaseBasicValue().setVictimTransgender(false);
            }
            
            defV.getDefOnCaseBasicValue().setHateSentencingUpdate(true);

            // ctx-417 Remove call to mercator api
            //IntegrationFacade intFacade = IntegrationFacadeFactory.getInstance().getIntegrationFacade();
            //try {
            //    intFacade.updateDefendant(defV);
            //} catch (MercatorException e) {
            //    CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
            //    throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
            //} catch (TransformationException e) {
            //    CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
            //    throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
            //} catch (OutputTransformationException e) {
            //    CSServices.getDefaultErrorHandler().handleError(e, DefendantControllerBean.class);
            //    throw new DefendantControllerException(e.getUserMessageAsMessage().getKey(), e.getMessage(), e);
            //}
            saveAggravatingReasons(result, defV);         
        } catch (DefendantControllerException e) {
            throw new ResultsControllerException(
                    "ResultsSaver.UnableToSaveDeportationAndHateCrimeDetails",
                    "Error in saveDeportationAndHateCrimeDetails.",
                    e);
        }

        log.debug("saveDeportationAndHateCrimeDetails(DisposalSaveValue result) : END");
    }
    
    private void saveAggravatingReasons(DisposalSaveValue result, DefendantValue defV) {
    	log.debug("saveAggravatingReasons() - Start");
    	defV.getDefOnCaseBasicValue().setAggravatingAssaultOnWorkers(result.getDisposalReferenceValue().isAggravatingAssaultOnWorkers());
    	defV.getDefOnCaseBasicValue().setAggravatingTerroristConnection(result.getDisposalReferenceValue().isAggravatingTerroristConnection());
    	defV.getDefOnCaseBasicValue().setAggravatingEmergencyWorkers(result.getDisposalReferenceValue().isAggravatingEmergencyWorkers());
    	defV.getDefOnCaseBasicValue().setAggravatingHostility(result.getDisposalReferenceValue().isAggravatingHostility());
    	defV.getDefOnCaseBasicValue().setAggravatingSexualOrientation(result.getDisposalReferenceValue().isAggravatingSexualOrientation());
    	defV.getDefOnCaseBasicValue().setAggravatingSexualOrientationOfVictim(result.getDisposalReferenceValue().isAggravatingSexualOrientationOfVictim());
    	defV.getDefOnCaseBasicValue().setAggravatingTransgender(result.getDisposalReferenceValue().isAggravatingTransgender());
    	defV.getDefOnCaseBasicValue().setAggravatingTransgenderOfVictim(result.getDisposalReferenceValue().isAggravatingTransgenderOfVictim());
    	log.debug("saveAggravatingReasons() - End");
    }
      
    private void saveCrestKeys(DisposalSaveValue result) throws ResultsControllerException {
        log.debug("SaveCrestKeys(DisposalSaveValue result) : START");
       if (ResultSaveValue.ADD.equals(result.getOperation())) {
            log.debug("SaveCrestKeys(DisposalSaveValue result)");
            updateDisposal(result, true); // Update: We do not want to create a
            // second row!
        } 
    }

    private void createDisposal(DisposalSaveValue result) throws ResultsControllerException {
        log.debug("createDisposal(DisposalSaveValue result) : START");
        // disId is not nullable in database so need to set this to -1,
        // it gets updated in the database by the insert trigger
        result.setDisId(new Integer(-1));
        result.setObsInd(false);

        result.setDisposalBasicValue(XhbDisposal2BeanHelper2.create(result.getDisposalBasicValue()));

        createDisposalLines(result);
        
        try {
			getDARTSHelper().createDisposal(
					result.getEntityCaseId(),
					result.getDisposal2Id(), result.getDisposalCode(), 
					result.getDefendantOnCaseId(), result.getDisposalValue().getDisposal().getDefendantOnOffenceId(),
					result.getDisposalLineValues(),
					result.getDisposalBasicValue().getLastUpdatedBy());
        } catch (FinderException ex) {
            throw new ResultsControllerException(
                    "ResultsSaver.UnableToSaveDeportationAndHateCrimeDetails",
                    "Error creating DARTS records in createDisposal.",
                    ex);
		}

        log.debug("createDisposal(DisposalSaveValue result) : END");

    }

    private void updateDisposal(DisposalSaveValue result, boolean saveCrestKeys) throws ResultsControllerException {
        log.debug("updateDisposal(DisposalSaveValue result)- START");
        log.debug("updateDisposal(DisposalSaveValue result)- VERSION BEFORE update: "
                + result.getDisposalBasicValue().getVersion());
        log.debug("Input VBV BEFORE UPDATE: " + result.getDisposalBasicValue());

        XhbDisposal2BeanHelper2.update(result.getDisposalBasicValue());

        //  update disposal lines
        updateDisposalLines(result);
        
        if (!saveCrestKeys) {
        	try {
		        getDARTSHelper().updateDisposal(
		        		result.getEntityCaseId(),
		        		result.getDisposal2Id(), result.getDisposalCode(), 
		    			result.getDefendantOnCaseId(), result.getDisposalValue().getDisposal().getDefendantOnOffenceId(),
		    			result.getDisposalLineValues(),
		        		result.getDisposalBasicValue().getLastUpdatedBy());
            } catch (FinderException ex) {
                throw new ResultsControllerException(
                        "ResultsSaver.UnableToSaveDeportationAndHateCrimeDetails",
                        "Error creating DARTS records in updateDisposal.",
                        ex);
            }
        }
        
        log.debug("updateDisposal(DisposalSaveValue result)- VERSION AFTER update: "
                + result.getDisposalBasicValue().getVersion());
        log.debug("updateDisposal(DisposalSaveValue result)- END");
    }
    
    private void updateDisposalLines(DisposalSaveValue result) {
    	XhbDisposalLineBasicValue[] arr = result.getDisposalLineValues();
    	
    	for (int i = 0; i < arr.length; i++) {
    		if (arr[i].getDisposalLineId() != null) {
    			// standard update
    			XhbDisposalLineBeanHelper2.update(arr[i]);
    		} else {
    			// need to create the entry as edit disposal occurred adding a need for new disp line
    			arr[i] = XhbDisposalLineBeanHelper2.create(arr[i]);
    		}
    	}
    	
    	result.setDisposalLineValues(arr);
    }
    	
    private void deleteDisposal(DisposalSaveValue result) {
        log.debug("deleteDisposal - START");

        if (!result.getObsInd()) {
            log.debug("deleteDisposal - set Obsolete flag");
            result.setObsInd(true);
            deleteDisposalBasicValue(result.getDisposal2Id());
            
            //Create audit event
            Hashtable<String, Object> hashtable = new Hashtable<String, Object>();
            hashtable.put(XhbDisposal2BasicValue.class.getName(), result.getDisposalBasicValue());
            AuditTrailService auditService = CSServices.getAuditTrailService();
            AuditTrailEvent event = auditService.getAuditTrailEvent(hashtable);
            event.setSuccess(true);
            auditService.createAuditRecord(event);
            
            // delete disposal lines
            deleteDisposalLines(result);

            deleteMagGeneralDisposalAppResult(result);
            
            getDARTSHelper().deleteDisposal(result.getEntityCaseId(), result.getDisposal2Id(), result.getDisposalBasicValue().getLastUpdatedBy());
        }

        log.debug("deleteDisposal - END");
    }

    /**
     * Delete a Magistrate Court General Disposal Appeal Result if it exists.
     * These only exist on Criminal Appeal cases.
     * 
     * @param result
     *            DisposalSaveValue
     */
    private void deleteMagGeneralDisposalAppResult(DisposalSaveValue result) {
        if (result.isCriminalAppeal() && result.isUnrelatedDisposal() && result.getCourtType() != null
                && result.getCourtType().equals("M")) {
            try {
                final XhbVerdict verdict = XhbVerdictBeanHelper2.findByDisposal2Id(result.getDisposal2Id());
                verdict.setObsInd("Y");
            } catch (XhbVerdictBeanNotFoundException vbnfe) {
                // No appeal result for this Magistrate General Disposal
                // so continue.
            }
        }
    }

    private void createDisposalLines(DisposalSaveValue result) {
        log.debug("createDisposalLines - START");

        XhbDisposalLineBasicValue[] xhbDisposalLineBasicValues = result.getDisposalLineValues();

        Integer disposalId = result.getDisposalBasicValue().getPrimaryKey();

        for (int i = 0, len = xhbDisposalLineBasicValues.length; i < len; i++) {
            xhbDisposalLineBasicValues[i].setDisposal2Id(disposalId);
            xhbDisposalLineBasicValues[i] = XhbDisposalLineBeanHelper2.create(xhbDisposalLineBasicValues[i]);
        }
        result.setDisposalLineValues(xhbDisposalLineBasicValues);

        log.debug("createDisposalLines - END");
    }

    private void deleteDisposalBasicValue(Integer disposal2Id) {
    	log.debug("deleteDisposal - START - disposal2Id = " + disposal2Id);
    	try {
	    	XhbDisposal2BasicValue disposalBasicValue = XhbDisposal2BeanHelper2.findByPrimaryKeyValue(disposal2Id);
	    	disposalBasicValue.setObsInd("Y");
	    	XhbDisposal2BeanHelper2.update(disposalBasicValue);
    	} catch (XhbDisposal2BeanNotFoundException ex) {
    		// Disposal2 no longer exists
    	}
    	log.debug("deleteDisposal - END");
    }
    
    /**
     * Private utility method used to set obsolete all of the disposal lines
     * that are currently in the system (that are not already obsolete) for the
     * disposal set in the passed in <code>DisposalSaveValue</code>
     * 
     * @param result
     *            The <code>DisposalSaveValue</code> that contains the
     *            disposal2Id for use in the lookup.
     */
    private void deleteDisposalLines(DisposalSaveValue result) {
        log.debug("deleteDisposalLines - START - disposal2Id = " + result.getDisposal2Id());

        final Collection disposalLines = XhbDisposalLineBeanHelper2.findByDisposal2Id(result.getDisposal2Id());
        final Iterator it = disposalLines.iterator();

        log.debug("deleteDisposalLines - About to make obsolete " + disposalLines.size() + " disposal lines");

        while (it.hasNext()) {
            final XhbDisposalLine line = (XhbDisposalLine) it.next();
            log.debug("deleteDisposalLines - Setting line with id " + line.getDisposalLineId() + " to obsolete");
            line.setObsInd("Y");
        }

        log.debug("deleteDisposalLines - END");
    }

    private void preprocess(DisposalSaveValue disposalSaveValue)
            throws ResultsControllerException {
        if (disposalSaveValue.isRelatedDisposal()) {
            setJoinderAttributes(disposalSaveValue, disposalSaveValue.getDefendantOnOffenceId());
        }
    }
    
    private DartsHelper getDARTSHelper() {
    	if (dartsHelper == null) {
    		dartsHelper = new DartsHelper();
    	}
    	return dartsHelper;
    }
}
