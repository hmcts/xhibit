package uk.gov.courtservice.xhibit.business.services.results.saver;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreach;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_breach.XhbBreachBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantCharge;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_charge.XhbDefendantChargeBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearing;
import uk.gov.courtservice.xhibit.business.entities.xhb_hearing.XhbHearingBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPleaBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: PleaResultsSaver
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
 * @version $Revision: 1.25 $
 */
public class PleaResultsSaver extends AbstractResultsSaver {
    private static final Logger log = CSServices.getLogger(PleaResultsSaver.class);

    public void preprocess(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not preprocess null result.");
        }

        if (!(resultSaveValue instanceof PleaSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not preprocess result of type "
                    + resultSaveValue.getClass().getName() + ".");
        }

        preprocess(resultsSaveValue, index, (PleaSaveValue) resultSaveValue);
    }

    public void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        if (!(resultSaveValue instanceof PleaSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save result of type " + resultSaveValue.getClass().getName()
                    + ".");
        }

        save((PleaSaveValue) resultSaveValue);
    }

    public void log(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not log null results.");
        }

        if (!(resultSaveValue instanceof PleaSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not log result of type " + resultSaveValue.getClass().getName() + ".");
        }

        PleaCourtLogHelper.getInstance().log((PleaSaveValue) resultSaveValue);
        Collection cases = XhbCaseBeanHelper2.findByJoinderDefendantOnOffenceId(((PleaSaveValue) resultSaveValue).getDefendantOnOffenceId());
        if(cases!=null && cases.size()>0) {
	        // get hold of all available linked cases to this case
	        Collection<XhbHearing> linkedScheduledHearings =
	        		XhbHearingBeanHelper2.findLinkedHearingsFromSchedId(((PleaSaveValue)resultSaveValue).getScheduledHearingId());
	
	        if (linkedScheduledHearings != null) {
	        	  final Iterator<XhbHearing> it = linkedScheduledHearings.iterator();
	              while (it.hasNext()) {
	                resultSaveValue.setCourtLogCaseId(it.next().getCaseId());
	                PleaCourtLogHelper.getInstance().log((PleaSaveValue) resultSaveValue);
	            }
	        }
        }
        
    }

    private void preprocess(ResultsSaveValue resultsSaveValue, int index, PleaSaveValue pleaSaveValue)
            throws ResultsControllerException {
        if (pleaSaveValue.isOnOffence()) {
            VCOCalculator.calculate(resultsSaveValue, index, pleaSaveValue);
            setJoinderAttributes(pleaSaveValue, pleaSaveValue.getDefendantOnOffenceId());
        }
    }

    private void save(PleaSaveValue result) throws ResultsControllerException {
        String operation = result.getOperation();
        if (operation.equals(ResultSaveValue.ADD)) {
        	if(result.getDefendantOnOffenceId()!=null){
     //need to do one more check in case it's been saved in the meantime		
        		if(XhbPleaBeanHelper2.findByDefendantOnOffenceId(result.getDefendantOnOffenceId()).isEmpty()) {
                    createPlea(result);
        		} else {
        			
   	        	 String errMessage = "OptimisticLock exception";
   	             OptimisticLockException opEx = new OptimisticLockException(errMessage);
   	             CSServices.getDefaultErrorHandler().handleError(opEx, PleaResultsSaver.class);
   	             throw opEx;
        		}
        	}else {
                createPlea(result);
        	}
        	
        } else if (operation.equals(ResultSaveValue.UPDATE)) {
            updatePlea(result);
        } else if (operation.equals(ResultSaveValue.DELETE)) {
            deletePlea(result);
        } else {
            throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation", new Object[] { result
                    .getOperation() }, "Unrecognised operation " + result.getOperation() + ".");
        }
    }

    private void createPlea(PleaSaveValue result) {
        result.setPleaBasicValue(XhbPleaBeanHelper2.create(result.getPleaBasicValue()));
        updateVco(result);
        updateBreach(result);
    }

    private void updatePlea(PleaSaveValue result) {
        XhbPleaBeanHelper2.update(result.getPleaBasicValue());
        updateVco(result);
        updateBreach(result);
    }

    private void deletePlea(PleaSaveValue result) {
        if (!result.getObsInd()) {
            result.setObsInd(true);
            XhbPleaBeanHelper2.update(result.getPleaBasicValue());
            updateVco(result);
            updateBreach(result);
        }
    }

    /**
     * Update the vco date on the defendant on offence object
     */
    private void updateVco(PleaSaveValue result) {
        if (result.isOnOffence()) {
            updateVco(result.getDefendantOnOffenceId(), result.getVcoDate(), result.getVcoFlag());
        }
    }

    private void updateBreach(PleaSaveValue result) {
        if (ChargeTypes.isBreachChargeType(result.getChargeType())) {
            log.debug("updateBreach - Started");
            log.debug("updateBreach - DefendantChargeId =" + result.getDefendantChargeId());
            XhbDefendantCharge defendantCharge = XhbDefendantChargeBeanHelper2.findByPrimaryKey(result
                    .getDefendantChargeId());
            XhbBreach breach = defendantCharge.getXhbCharge().getXhbBreach();
            XhbBreachBasicValue breachBV = breach.getData();
            log.debug("updateBreach - datePut =" + result.getDatePut());
            breachBV.setDatePut(result.getDatePut());
            XhbBreachBeanHelper2.update(breachBV);
        }
        log.debug("updateBreach - Exited");
    }

}