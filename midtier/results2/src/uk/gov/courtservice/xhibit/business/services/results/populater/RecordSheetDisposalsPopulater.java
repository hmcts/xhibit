package uk.gov.courtservice.xhibit.business.services.results.populater;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: RecordSheetDisposalsPopulater
 * </p>
 * <p>
 * Description: Populates ResultsCompositeValue with Disposals, Ensures
 * related(per offence) and unrelated Disposals are set
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.15 $
 */
public class RecordSheetDisposalsPopulater extends AbstractResultsPopulater {
    private static final Logger log = CSServices.getLogger(RecordSheetDisposalsPopulater.class);

    public void populate(ResultsCompositeValue rcv) throws ResultsControllerException {
        log.debug("populate() - START");
        populateDisposals(rcv);
        log.debug("populate() - END");
    }

    public void populateSaveValues(ResultsSaveValue results, MoveResultsValue oldMoveValue,
            MoveResultsValue newMoveValue) throws ResultsControllerException {
        log.debug("populateSaveValues() - START");

        // create disposal values for each defendant on offence
        DisposalValue[] disposalValues = createDisposalValues(XhbDisposal2BeanHelper2
                .findRSByDefendantOnOffenceIdValue(oldMoveValue.getDefendantOnOffenceId()), XhbDisposalLineBeanHelper2
                .findRSByDefendantOnOffenceIdValue(oldMoveValue.getDefendantOnOffenceId()));

        log.debug("olddefendantOnOffenceId= " + oldMoveValue.getDefendantOnOffenceId() + "No of disposals to copy "
                + disposalValues.length);
        DisposalSaveValue[] disposalSaveValues = new DisposalSaveValue[disposalValues.length];

        for (int i = 0, len = disposalValues.length; i < len; i++) {

            DisposalReferenceValue disposalReferenceValue = ResultsDatabase.getReferenceDisposal(new Integer(
                    disposalValues[i].getRefDisposalTypeId()));
            Integer psdDisId = null;
            if (disposalValues[i].getPsdDisposal2Id() != null) {
                psdDisId = (XhbDisposal2BeanHelper2.findByPrimaryKeyValue(disposalValues[i].getPsdDisposal2Id()))
                        .getDisId();
            }

            disposalSaveValues[i] = new DisposalSaveValue(disposalValues[i], disposalReferenceValue,
                    ResultSaveValue.DELETE, oldMoveValue.getCaseId(), oldMoveValue.getCaseNumber(), oldMoveValue
                            .getCaseType(), oldMoveValue.getCaseSubType(), oldMoveValue.getCrestOffenceId(),
                    oldMoveValue.getCrestDefendantId(), oldMoveValue.getCrestChargeId(), oldMoveValue.getChargeType(),
                    psdDisId);
            disposalSaveValues[i].setCourtLogged(false);
            results.addResultSaveValue(disposalSaveValues[i]);
            results.addResultSaveValue(getNewDisposal(disposalSaveValues[i], newMoveValue));

        }
        log.debug("populateSaveValues() - END");
    }

    // We use chargeId for related and caseId for unrelated as highest known
    // point in herarchy,
    // this minimises the number of calls to the Entity layer
    private void populateDisposals(ResultsCompositeValue rcv) throws ResultsControllerException {
        populateUnrelatedDisposals(rcv, rcv.getCaseId());

        Integer[] chargeIds = rcv.getChargeIds();
        for (int i = 0; i < chargeIds.length; i++) {
            populatePerOffenceDisposals(rcv, chargeIds[i]);
        }
    }

    private void populateUnrelatedDisposals(ResultsCompositeValue rcv, Integer caseId)
            throws ResultsControllerException {
        log.debug("populateUnrelatedDisposals - START");
        log.debug("populateUnrelatedDisposals - caseId: " + caseId);
        rcv.addUnrelatedDisposals(createDisposalValues(XhbDisposal2BeanHelper2.findRSByCaseIdValue(caseId),
                XhbDisposalLineBeanHelper2.findRSByCaseIdValue(caseId)));
        log.debug("populateUnrelatedDisposals - END");
    }

    private void populatePerOffenceDisposals(ResultsCompositeValue rcv, Integer chargeId)
            throws ResultsControllerException {
        rcv.addDisposals(createDisposalValues(XhbDisposal2BeanHelper2.findRSByChargeIdValue(chargeId),
                XhbDisposalLineBeanHelper2.findRSByChargeIdValue(chargeId)));
    }

    private DisposalValue[] createDisposalValues(XhbDisposal2BasicValue[] disposalBasicValues,
            XhbDisposalLineBasicValue[] disposalLineBasicValues) throws ResultsControllerException {
        log.debug("createDisposalValues - START");
        log.debug("createDisposalValues - disposalBasicValues.length: " + disposalBasicValues.length);
        DisposalValue[] disposalValues = new DisposalValue[disposalBasicValues.length];

        // Create the disposal values
        for (int i = 0; i < disposalValues.length; i++) {
            disposalValues[i] = new DisposalValue(disposalBasicValues[i]);
        }

        // Link the disposal value lines
        for (int i = 0; i < disposalLineBasicValues.length; i++) {
            findDisposalValue(disposalValues, disposalLineBasicValues[i].getDisposal2Id()).addLine(
                    disposalLineBasicValues[i]);
        }
        log.debug("createDisposalValues - END");
        return disposalValues;
    }

    // As disposalValues will remain small this is an acceptable way of
    // looking up, could consider
    // caching in a map if more than a certain number. This would require
    // extensive analyses to
    // ensure best performance.
    private DisposalValue findDisposalValue(DisposalValue[] disposalValues, Integer disposal2Id)
            throws ResultsControllerException {
        log.debug("findDisposalValue - START");
        log.debug("findDisposalValue - disposal2Id: " + disposal2Id);

        for (int i = 0; i < disposalValues.length; i++) {
            if (disposalValues[i].getDisposal2Id().equals(disposal2Id)) {
                return disposalValues[i];
            }
        }
        throw new ResultsControllerException("RecordSheetDisposalPopulater.DisposaNotFound",
                new Object[] { disposal2Id }, "Could not find disposalvalue for disposal2Id " + disposal2Id + ".");
    }

    private DisposalSaveValue getNewDisposal(DisposalSaveValue oldDisposal, MoveResultsValue newMoveValue) {
        // copy XhbDisposal2BasicValue
        XhbDisposal2BasicValue oldDisposalBV = oldDisposal.getDisposalBasicValue();
        XhbDisposal2BasicValue newDisposalBV = new XhbDisposal2BasicValue();

        newDisposalBV.setCourtType(oldDisposalBV.getCourtType());
        newDisposalBV.setDefendantOnOffenceId(newMoveValue.getDefendantOnOffenceId());
        newDisposalBV.setDisId(null);
        newDisposalBV.setDisposal2Id(null);
        newDisposalBV.setPsdDisposal2Id(oldDisposalBV.getPsdDisposal2Id());
        newDisposalBV.setRefDisposalTypeId(oldDisposalBV.getRefDisposalTypeId());

        // create DisposalValue
        DisposalValue oldDisposalValue = oldDisposal.getDisposalValue();
        DisposalValue newDisposalValue = new DisposalValue(newDisposalBV);

        // copy XhbDisposalLineBasicValues
        XhbDisposalLineBasicValue[] oldDisposalLineBVs = oldDisposal.getDisposalLineValues();
        XhbDisposalLineBasicValue[] newDisposalLineBVs = new XhbDisposalLineBasicValue[oldDisposalLineBVs.length];
        for (int i = 0, len = oldDisposalLineBVs.length; i < len; i++) {
            newDisposalLineBVs[i] = new XhbDisposalLineBasicValue();
            newDisposalLineBVs[i].setDelG1(oldDisposalLineBVs[i].getDelG1());
            newDisposalLineBVs[i].setDelG2(oldDisposalLineBVs[i].getDelG2());
            newDisposalLineBVs[i].setDelLineData(oldDisposalLineBVs[i].getDelLineData());
            newDisposalLineBVs[i].setDisposal2Id(null);
            newDisposalLineBVs[i].setDisposalLineId(null);
            newDisposalLineBVs[i].setLineData(oldDisposalLineBVs[i].getLineData());
            newDisposalLineBVs[i].setLineNumber(oldDisposalLineBVs[i].getLineNumber());
            newDisposalLineBVs[i].setRefDisposalLineId(oldDisposalLineBVs[i].getRefDisposalLineId());
            newDisposalLineBVs[i].setObsInd("N");
            newDisposalValue.addLine(newDisposalLineBVs[i]);
        }

        // create new DisposalSaveValue
        DisposalSaveValue newDisposal = new DisposalSaveValue(
                newDisposalValue,
                oldDisposal.getDisposalReferenceValue(), // ok to point
                // reference as
                // nothing needs
                // changing (I
                // think)
                ResultSaveValue.ADD, newMoveValue.getCaseId(), newMoveValue.getCaseNumber(),
                newMoveValue.getCaseType(), newMoveValue.getCaseSubType(), newMoveValue.getCrestOffenceId(),
                newMoveValue.getCrestDefendantId(), newMoveValue.getCrestChargeId(), newMoveValue.getChargeType(), null); // psdId
                                                                                                                            // null
                                                                                                                            // because
                                                                                                                            // it
                                                                                                                            // needs
                                                                                                                            // to
                                                                                                                            // be
                                                                                                                            // set
                                                                                                                            // by
                                                                                                                            // mercator
        newDisposal.setCourtLogged(false);
        newDisposal.setObsInd(false);
        return newDisposal;

    }
}