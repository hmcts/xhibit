package uk.gov.courtservice.xhibit.business.services.results.saver;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case_app_reason.XhbCaseAppReasonBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: CaseAppReasonResultsSaver
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
 * @version $Revision: 1.17 $
 */
public class CaseAppReasonResultsSaver extends AbstractResultsSaver {
    private static final Logger log = CSServices.getLogger(CaseAppReasonResultsSaver.class);

    public void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save null results.");
        }

        if (!(resultSaveValue instanceof CaseAppReasonSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save result of type " + resultSaveValue.getClass().getName()
                    + ".");
        }

        save((CaseAppReasonSaveValue) resultSaveValue);
    }

    public void saveCrestKeys(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException {
        if (resultsSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save crest keys for null results.");
        }

        ResultSaveValue resultSaveValue = resultsSaveValue.getResultSaveValue(index);

        if (resultSaveValue == null) {
            throw new ResultsControllerException("ResultsSaver.NullResult", "Can not save crest keys for null result.");
        }

        if (!(resultSaveValue instanceof CaseAppReasonSaveValue)) {
            throw new ResultsControllerException("ResultsSaver.InvalidResult", new Object[] { resultSaveValue
                    .getClass().getName() }, "Can not save crest keys for result of type "
                    + resultSaveValue.getClass().getName() + ".");
        }

        saveCrestKeys((CaseAppReasonSaveValue) resultSaveValue);
    }

    private void saveCrestKeys(CaseAppReasonSaveValue result) {
        log.debug("SaveCrestKeys(CaseAppReasonSaveValue result) : START");
        String operation = result.getOperation();
        if (operation.equals(ResultSaveValue.ADD)) {
            log.debug("SaveCrestKeys(CaseAppReasonSaveValue result) : createCaseAppReason");
            updateCaseAppReason(result); // Update: We do not want to
            // create a second row!
        }
    }

    private void save(CaseAppReasonSaveValue result) throws ResultsControllerException {
        log.debug("Save(CaseAppReasonSaveValue result) : START");
        String operation = result.getOperation();
        if (operation.equals(ResultSaveValue.ADD)) {
            log.debug("Save(CaseAppReasonSaveValue result) : createCaseAppReason");
            createCaseAppReason(result);
        } else if (operation.equals(ResultSaveValue.UPDATE)) {
            log.debug("Save(CaseAppReasonSaveValue result) : update");
            updateCaseAppReason(result);
        } else if (operation.equals(ResultSaveValue.DELETE)) {
            log.debug("Save(CaseAppReasonSaveValue result) : delete");
            deleteCaseAppReason(result);
        } else {
            throw new ResultsControllerException("AbstractResultsSaver.UnrecognisedOperation", new Object[] { result
                    .getOperation() }, "Unrecognised operation " + result.getOperation() + ".");
        }
    }

    private void createCaseAppReason(CaseAppReasonSaveValue result) {
        log.debug("createCaseAppReason(CaseAppReasonSaveValue result) : START");
        XhbCase caze = XhbCaseBeanHelper2.findByPrimaryKey(result.getCaseId());
        /**
         * @todo currently (18/05/04) carId is not nullable in the database so
         *       need to set this to -1, it gets updated in the saveCrestKeys
         *       method when after the record has been saved CREST
         */
        result.setCarId(new Integer(-1));
        result.setObsInd(false);
        log.debug("App Reason Before Insert: \"" + result.getAppReason() + "\"");
        result
                .setCaseAppReasonBasicValue(XhbCaseAppReasonBeanHelper2.create(result.getCaseAppReasonBasicValue(),
                        caze));
        log.debug("App Reason After Insert: \"" + result.getAppReason() + "\"");
        result.setCarId(result.getCaseAppReasonId());
    }

    private void updateCaseAppReason(CaseAppReasonSaveValue result) {
        log.debug("updateCaseAppReason(CaseAppReasonSaveValue result)- START");
        log.debug("updateCaseAppReason(CaseAppReasonSaveValue result)- VERSION BEFORE update: "
                + result.getCaseAppReasonBasicValue().getVersion());
        log.debug("Input PBV BEFORE UPDATE: " + result.getCaseAppReasonBasicValue());

        log.debug("App Reason Before Update: \"" + result.getAppReason() + "\"");
        XhbCaseAppReasonBeanHelper2.update(result.getCaseAppReasonBasicValue());
        log.debug("App Reason After Update: \"" + result.getAppReason() + "\"");

        log.debug("updateCaseAppReason(CaseAppReasonSaveValue result)- VERSION AFTER update: "
                + result.getCaseAppReasonBasicValue().getVersion());
        log.debug("updateCaseAppReason(CaseAppReasonSaveValue result)- END");
    }

    private void deleteCaseAppReason(CaseAppReasonSaveValue result) {
        log.debug("deleteCaseAppReason(CaseAppReasonSaveValue result)- START");
        if (!result.isObsolete()) {
            result.setObsInd(true);
            XhbCaseAppReasonBeanHelper2.update(result.getCaseAppReasonBasicValue());
        }
        log.debug("deleteCaseAppReason(CaseAppReasonSaveValue result)- END");
    }
}
