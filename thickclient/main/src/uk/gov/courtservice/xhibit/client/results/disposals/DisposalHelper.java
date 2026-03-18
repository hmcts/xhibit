package uk.gov.courtservice.xhibit.client.results.disposals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DisposalHelper.java,v 1.9 2006/06/05 12:31:33 bzjrnl Exp $
 */

public class DisposalHelper {
    private static final Logger log = CSServices.getLogger(DisposalHelper.class);

    private DisposalHelper() {
    }

    /**
     * Add a new results row value (unrelated disposal) to a defendant
     * 
     * @param defendantId
     * @param list
     * @param rrv
     */
    public static void addToEndOfUnrelatedList(Integer defendantOnCaseId, List list, ResultsRowValue rrv) {
        ResultsRowValue foundRRV = null;
        // locate last instance of defendant (assume already sorted)
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            ResultsRowValue item = (ResultsRowValue) iter.next();
            if (defendantOnCaseId == item.getDefendantOnCaseId()) {
                foundRRV = item;
            } else {
                // if we have already found a Row Value, then we have now
                // gone past the end of the defendants list so last row
                // has been found.
                if (foundRRV != null)
                    break;
            }
        }
        if (foundRRV == null) {
            /**
             * @todo Need a defendant not exist run time exception (unexpected
             *       condition)
             */
            throw new CSUnrecoverableException("Defendant Not found. This is not expected");
        } else {
            int index = list.indexOf(foundRRV);
            addDisposalToRRV(rrv, list, index);
        }
    }

    /**
     * Add a new results row value to a defendant on offence
     * 
     * @param defendantOnOffenceId
     * @param list
     * @param rrv
     */
    public static void addToEndOfList(Integer defendantOnOffenceId, List list, ResultsRowValue rrv) {
        ResultsRowValue foundRRV = null;
        // locate last instance of defendant (assume already sorted)
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            ResultsRowValue item = (ResultsRowValue) iter.next();
            if (defendantOnOffenceId == item.getDefendantOnOffenceId()) {
                foundRRV = item;
            } else {
                // if we have already found a Row Value, then we have now
                // gone past the end of the defendants list so last row
                // has been found.
                if (foundRRV != null)
                    break;
            }
        }
        if (foundRRV == null) {
            /**
             * @todo Need a defendant not exist run time exception (unexpected
             *       condition)
             */
            throw new CSUnrecoverableException("Defendant Not found. This is not expected");
        } else {
            int index = list.indexOf(foundRRV);
            addDisposalToRRV(rrv, list, index);
        }
    }

    public static int getSelectedRow(OffencePanelModel model) {
        return model.getDisposalController().getSelectedList().indexOf(model.getResultRowValue());
    }

    /**
     * Takes a collection of ResultsRowValue and removes any who i. does not
     * have a ChargeValue ii. ChargeValue does not have a BreachValue iii.
     * ChargeValue does not have any OffenceValues
     * 
     * @param results
     *            collection of ResultsRowValue that populate the disposal
     *            screen OffenceTableModel.
     */
    public static void removeBreachesWithNoOffences(Collection results) {
        if (results != null) {
            ResultsRowValue rrv = null;
            for (Iterator i = results.iterator(); i.hasNext();) {
                rrv = (ResultsRowValue) i.next();
                if (rrv.getChargeValue() != null && rrv.getChargeValue().getBreachValue() != null) {
                    ChargeValue chargeValue = rrv.getChargeValue();
                    if (chargeValue.getOffenceValues() == null || chargeValue.getOffenceValues().size() == 0) {
                        // There are no offences, so remove.
                        i.remove();
                    }
                } else {
                    // Need to have a charge and a breach before offences
                    // exist,
                    // so remove this row.
                    i.remove();
                }
            }
        }
    }

    private static void addDisposalToRRV(ResultsRowValue rrv, List list, int index) {
        if (((ResultsRowValue) list.get(index)).getDisposalValue() == null) {
            ResultsRowValue thisRRV = (ResultsRowValue) list.get(index);
            thisRRV.setDisposalValue(rrv.getDisposalValue());
            thisRRV.setDisposalReferenceValue(rrv.getDisposalReferenceValue());
            thisRRV.setAction(ResultsRowValue.RESULT_ADD);
            thisRRV.setModified(true);
        } else {
            list.add(index + 1, rrv);
        }
    }

    /**
     * Sets enable property on List of disposal actions to value passed in
     * 
     * @param enabled
     * @param xac
     */
    public static void disableDisposalActions(XhibitApplicationController xac) {
        XhibitActions.getAction(xac, XhibitActions.AddDisposal).setEnabled(false);
        XhibitActions.getAction(xac, XhibitActions.EditDisposal).setEnabled(false);
        XhibitActions.getAction(xac, XhibitActions.DeleteDisposal).setEnabled(false);
        XhibitActions.getAction(xac, XhibitActions.UndeleteDisposal).setEnabled(false);
        XhibitActions.getAction(xac, XhibitActions.CopyDisposal).setEnabled(false);
        XhibitActions.getAction(xac, XhibitActions.CopyUnrelatedDisposal).setEnabled(false);
    }

    /**
     * Takes a collection of ResultsRowValue and removes any does NOT contain a
     * plea.
     * 
     * @param results
     *            collection of ResultsRowValue that populate the verdict
     *            indictment table model.
     */
    public static void removedNoPleaResults(Collection results) {
        if (results != null) {
            ResultsRowValue rrv = null;
            Iterator i = results.iterator();
            while (i.hasNext()) {
                rrv = (ResultsRowValue) i.next();
                if (rrv.getPleaValue() == null) {
                    if (rrv.getChargeValue().getBreachValue() != null) {
                        if (!rrv.getChargeValue().getBreachValue().getBreachType().equalsIgnoreCase("C")) {
                            // Need to have pleas before disposals if not
                            // breach not commited
                            i.remove();
                        }
                    } else // Charge not a breach
                    {
                        // Need to have pleas before disposals
                        i.remove();
                    }
                }
            }
        }
    }

    /**
     * Populates ResultsSaveValue with disposals to be added, updated and
     * deleted.
     * 
     * @param tableModel
     *            XHIBITTableModelInterface.
     * @param resultsSaveValue
     *            ResultsSaveValue.
     */
    public static void setAlteredFlags(XHIBITTableModelInterface tableModel, ResultsSaveValue resultsSaveValue) {
        ResultsRowValue rrv;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);

            DisposalSaveValue disposalSaveValue;

            switch (rrv.getAction()) {
            case ResultsRowValue.RESULT_ADD:
                disposalSaveValue = rrv.getDisposalSaveValue(ResultSaveValue.ADD);
                resultsSaveValue.addResultSaveValue(disposalSaveValue);
                break;
            case ResultsRowValue.RESULT_UPDATE:
                resultsSaveValue.addResultSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.UPDATE));
                break;
            case ResultsRowValue.RESULT_DELETE:
                resultsSaveValue.addResultSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.DELETE));
                break;
            case ResultsRowValue.RESULT_UNCHANGED:
            default:
                break;
            }
        }
    }

    /**
     * Populates ResultsSaveValue with disposals to be added, updated and
     * deleted.
     * 
     * @param tableModel
     *            XHIBITTableModelInterface.
     * @param resultsSaveValue
     *            ResultsSaveValue.
     */
    public static void setOrderedAlteredFlags(XHIBITTableModelInterface tableModel, ResultsSaveValue resultsSaveValue) {
        log.debug("setOrderedAlteredFlags - BEGIN");
        ResultsRowValue rrv;
        ArrayList childList = new ArrayList();
        ArrayList parentList = new ArrayList();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);

            DisposalSaveValue disposalSaveValue;

            switch (rrv.getAction()) {
            case ResultsRowValue.RESULT_ADD:
                disposalSaveValue = rrv.getDisposalSaveValue(ResultSaveValue.ADD);
                resultsSaveValue.addResultSaveValue(disposalSaveValue);
                break;
            case ResultsRowValue.RESULT_UPDATE:
                resultsSaveValue.addResultSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.UPDATE));
                break;
            case ResultsRowValue.RESULT_DELETE:
                processCriminalParentChildren(rrv, childList, parentList);
                break;
            case ResultsRowValue.RESULT_UNCHANGED:
            default:
                break;
            }
        }

        // Below ensures variation disposals are deleted first.
        if (!childList.isEmpty()) {
            for (int i = 0; i < childList.size(); i++) {
                resultsSaveValue.addResultSaveValue(((ResultsRowValue) childList.get(i))
                        .getDisposalSaveValue(ResultSaveValue.DELETE));
            }
        }

        if (!parentList.isEmpty()) {
            DisposalSaveValue disposalSaveValue;
            VerdictSaveValue verdictSaveValue;
            for (int i = 0; i < parentList.size(); i++) {
                rrv = (ResultsRowValue) parentList.get(i);
                disposalSaveValue = rrv.getDisposalSaveValue(ResultSaveValue.DELETE);
                if (disposalSaveValue.isMagistrateGeneralDisposal()) {
                    // Get Appeal Result if it exists and delete it */
                    if (rrv.getVerdictValue() != null) {
                        verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.DELETE);
                        verdictSaveValue.setDisposalSaveValue(rrv.getDisposalSaveValue(ResultSaveValue.DELETE));
                        resultsSaveValue.addResultSaveValue(verdictSaveValue);
                    }
                }
                resultsSaveValue.addResultSaveValue(((ResultsRowValue) parentList.get(i))
                        .getDisposalSaveValue(ResultSaveValue.DELETE));
            }
        }
    }

    /**
     * Separate variation disposals from magistrates disposals
     * 
     * @param deleteRRV
     * @param childList
     * @param parentList
     */
    private static void processCriminalParentChildren(ResultsRowValue deleteRRV, ArrayList childList,
            ArrayList parentList) {
        if (deleteRRV.getDisposalValue().getPsdDisposal2Id() != null) {
            childList.add(deleteRRV);
        } else {
            parentList.add(deleteRRV);
        }
    }

    public static List sortCriminalAppealUnrelatedDisposals(List results) {
        DisposalComparator disposalComparator = new DisposalComparator();
        List sortedDisposals = new ArrayList();
        List disposals = new ArrayList();
        List variationDisposals = new ArrayList();
        ResultsRowValue rrv;

        for (Iterator i = results.iterator(); i.hasNext();) {
            rrv = (ResultsRowValue) i.next();
            if (rrv.getDisposalValue() != null && rrv.getDisposalValue().isVariationDisposal()) {
                variationDisposals.add(rrv);
            } else {
                disposals.add(rrv);
            }
        }

        if (!disposals.isEmpty()) {
            Collections.sort(disposals, disposalComparator);
        }
        if (!variationDisposals.isEmpty()) {
            Collections.sort(variationDisposals, disposalComparator);
        }

        ResultsRowValue variationRRV;
        for (Iterator i = disposals.iterator(); i.hasNext();) {
            rrv = (ResultsRowValue) i.next();
            if (rrv.getDisposalValue() != null && rrv.getDisposalValue().isMagistrateDisposal()
                    && !variationDisposals.isEmpty()) {
                sortedDisposals.add(rrv);
                for (Iterator j = variationDisposals.iterator(); j.hasNext();) {
                    variationRRV = (ResultsRowValue) j.next();
                    if (rrv.getDisposalValue().getDisposal2Id().equals(
                            variationRRV.getDisposalValue().getPsdDisposal2Id())) {
                        sortedDisposals.add(variationRRV);
                        j.remove();
                    }
                }
            } else {
                sortedDisposals.add(rrv);
            }
        }
        return sortedDisposals;
    }

    private static class DisposalComparator implements Comparator {
        public int compare(Object o1, Object o2) {
        	DisposalValue d1 = ((ResultsRowValue) o1).getDisposalValue();
        	DisposalValue d2 = ((ResultsRowValue) o2).getDisposalValue();
        	// Cater for nullpointer comparisons
        	Date d1Date = d1 != null ? d1.getCreationDate() : new Date();
        	Date d2Date = d2 != null ? d2.getCreationDate() : new Date();
            return d1Date.compareTo(d2Date);
        }
    }
}
