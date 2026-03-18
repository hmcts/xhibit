package uk.gov.courtservice.xhibit.client.results.pleas;

// Java
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: Assists in retrieving/setting information on Pleas
 * </p>
 * <p>
 * Description: Reference data and appropriate delegates can be accessed using
 * class methods
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.62 $
 */
public class PleaHelper {

    public static final String INDICTMENT_CHARGE_TYPE = ChargeTypes.INDICTMENT.getChargeType();
    public static final String SECTION41_CHARGE_TYPE = ChargeTypes.SECTION_41.getChargeType();
    public static final String FAIL2APPEAR_CHARGE_TYPE = ChargeTypes.FAIL2APPEAR.getChargeType();
    
    public static final int SEARCH_TYPE_PLEA_CODE = 1;
    public static final int SEARCH_TYPE_PLEA_DESCRIPTION = 2;

    public static String errorTitle = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "plea.code.error.Title");
    public static String errorMessage = ResourceBundleHelper.getResource(XhibitBundles.Pleas, "plea.code.error.Message");

    private ApplicationCaseModel acm = null;

    private static final Collection<RefSystemCodeBasicValue> pleaRefData = createPleaRefData();
    private static final Collection<RefSystemCodeBasicValue> pleaS41RefData = createPleaS41RefData();

    private static final String emptyStr = "";

    public PleaHelper() {
        // empty
    }

    public PleaHelper(ApplicationCaseModel acm) {
        this.acm = acm;
    }

    public ResultsHelper getResultsHelper() throws CSRecoverableException {
        return new ResultsHelper(acm);
    }

    private static Collection<RefSystemCodeBasicValue> createPleaRefData() {
        Collection<RefSystemCodeBasicValue> refData = null;
        try {
            RefSystemCodeCriteria rfsc = new RefSystemCodeCriteria();
            rfsc.setCodeType(RefSystemCodeCriteria.CodeType.PLEA);
            rfsc.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            refData = ResultsHelper.getBisRefDelegate().findSystemCodes(rfsc);
            Sorter.sort((List) refData, new String[] { "decode" });

        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }
        return refData;
    }

    private static Collection<RefSystemCodeBasicValue> createPleaS41RefData() {
        Collection<RefSystemCodeBasicValue> refData = null;
        try {
            RefSystemCodeCriteria rfsc = new RefSystemCodeCriteria();
            rfsc = new RefSystemCodeCriteria();
            rfsc.setCodeType("S41_PLEA");
            rfsc.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            refData = ResultsHelper.getBisRefDelegate().findSystemCodes(rfsc);
            Sorter.sort((List) refData, new String[] { "decode" });
        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }
        return refData;
    }

    public static Collection<RefSystemCodeBasicValue> getPleaRefData() {
        return pleaRefData;
    }

    public static Collection<RefSystemCodeBasicValue> getPleaS41RefData() {
        return pleaS41RefData;
    }

    public static RefSystemCodeBasicValue getRefSystemCodeBasicValue(Integer pleaId, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            Integer id = rscbv.getId();
            XHIBITConstant.debug("getRefSystemCodeBasicValue: pleaId = [" + pleaId + "] id = [" + id + "]");
            if (pleaId.equals(id)) {
                return rscbv;
            }
        }
        return null;
    }

    public static RefSystemCodeBasicValue getRefSystemCodeBasicValue(Collection refData, String searchString,
            int searchType) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();

            switch (searchType) {
            case SEARCH_TYPE_PLEA_CODE:
                code = rscbv.getCode();
                break;
            case SEARCH_TYPE_PLEA_DESCRIPTION:
                code = rscbv.getDecode();
                break;
            default:
                return null;
            }

            XHIBITConstant.debug("getRefSystemCodeBasicValue: searchString = [" + searchString + "] code = [" + code
                    + "]");
            if (searchString.equals(code)) {
                return rscbv;
            }
        }
        return null;
    }

    public static Integer getRefPleaId(String refPleaCode, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            code = rscbv.getCode();
            XHIBITConstant.debug("\nMethod: getRefPleaId: " + "\nrefPleaCode [" + refPleaCode + "]" + "\n       code ["
                    + code + "]");
            if (code.equals(refPleaCode)) {
                return rscbv.getId();
            }
        }
        return null;
    }

    public static String getRefPleaCode(String refPleaDescription, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        String desc;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            desc = rscbv.getDecode();
            XHIBITConstant.debug("\nMethod: getRefPleaCode: " + "\nrefPleaDescription [" + refPleaDescription + "]"
                    + "\n              desc [" + desc + "]");
            if (desc.equals(refPleaDescription)) {
                return rscbv.getCode();
            }
        }
        return "";
    }

    public static String getRefPleaDescription(String refPleaCode, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            code = rscbv.getCode();
            XHIBITConstant.debug("\nMethod: getRefPleaDescription: " + "\nrefPleaCode [" + refPleaCode + "]"
                    + "\n       code [" + code + "]");
            if (code.equals(refPleaCode)) {
                return rscbv.getDecode();
            }
        }
        return "";
    }

    protected boolean hasPlea(XHIBITTableModelInterface model, Integer defendantId) {
        ResultsRowValue rrv;
        DefendantValue dv;

        for (int i = 0; i < model.getRowCount(); i++) {
            rrv = (ResultsRowValue) model.getDataAt(i);
            dv = rrv.getDefendantValue();
            if (dv != null) {
                if (dv.getDefendantID() != null) {
                    if (defendantId.equals(dv.getDefendantID())) {
                        if (rrv.getPleaValue() != null) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines whether Date Put is allowed to be null by checking if HO
     * ProcCode is special
     */
    public static boolean isDatePutValid(Object obj, ResultsRowValue rrv) {
        // Determines whether Date Put is allowed to be null by checking if HO
        // ProcCode is special
        if ((obj == null || obj.equals(emptyStr))
                && 
                PleaSaveValue.isNotSpecialHoProcCode(rrv.getChargeValue().getBreachValue().getHoCode())
                && 
                (rrv.getReceiptType().equals("BB") 
                        || 
                        (!rrv.getReceiptType().equals("BB") && rrv.getChargeValue().getBreachValue().getBreachType().equals("B")
                        ||
                        rrv.getChargeValue().getBreachValue().getBreachType().equals("F")))) 
        {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.NoNullDatePut.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Determines whether a not null date put value should be allowed
        if ((obj != null && !obj.equals(emptyStr))
                && (!rrv.getReceiptType().equals("BB") && rrv.getChargeValue().getBreachValue().getBreachType().equals(
                        "C")))

        {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.NullDatePut.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Determines whether passed in code is a valid reference Code.
     * 
     * @param code
     * @param refData
     * @return boolean
     */
    public static boolean isPleaCodeValid(String code, Collection refData) {
        try {
            Integer id = PleaHelper.getRefPleaId(code, refData);
            if (id == null) {
                JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (ClassCastException exception) {
            return false;
        }

        return true;
    }

    /**
     * Determines whether passed in plea is a valid for Breach Type.
     * 
     * @param obj
     * @param rrv
     * @return boolean
     */
    public static boolean isBreachPleaValid(Object obj, ResultsRowValue rrv) {
        String code = ResultsHelper.checkNull((String) obj);
        java.util.Date date = null;
        if (rrv.getChargeValue().getBreachValue().getDatePut() != null) {
            date = rrv.getChargeValue().getBreachValue().getDatePut().getTime();
        }

        // This will check whether plea is mandatory
        boolean isMandatoryPleaSet = PleaSaveValue.isMandatoryPleaSet(rrv.getCaseType(), rrv.getChargeValue()
                .getBreachValue().getBreachType(), rrv.getReceiptType(), rrv.getChargeValue().getBreachValue()
                .getHoCode(), code, date);

        // This will check whether null plea is mandatory
        boolean isMandatoryNullPleaSet = PleaSaveValue.isMandatoryNullPleaSet(rrv.getCaseType(), rrv.getChargeValue()
                .getBreachValue().getBreachType(), rrv.getReceiptType(), rrv.getChargeValue().getBreachValue()
                .getHoCode(), code, date);

        if (!isMandatoryPleaSet) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.MandatoryPlea.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.MandatoryPlea.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isMandatoryNullPleaSet) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.MandatoryPleaNull.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.MandatoryPlea.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Determines whether passed in plea is a valid for Bail Act Type.
     * 
     * @param obj - object to set inot column value
     * @return boolean
     */
    public static boolean isBailActPleaValid(Object obj) {
        
        if (obj != null && !( obj.equals("Yes") || obj.equals("No")  ) ) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.BailActPleaSet.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.BailActPleaSet.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    
    
    protected void setAlteredFlags(XHIBITTableModelInterface tableModel, Collection<PleaValue> newAndChangedPleas) {
        ResultsRowValue rrv;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);
            if (rrv.isModified()) {
                newAndChangedPleas.add(rrv.getPleaValue());
                rrv.setModified(false);
            }
        }
    }

    protected void setAlteredFlags(XHIBITTableModelInterface tableModel,

    ResultsSaveValue resultsSaveValue) throws CSRecoverableException {
        ResultsRowValue rrv;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);

            PleaSaveValue pleaSaveValue;
            switch (rrv.getAction()) {
            case ResultsRowValue.RESULT_ADD:
                pleaSaveValue = rrv.getPleaSaveValue(ResultSaveValue.ADD);
                resultsSaveValue.addResultSaveValue(pleaSaveValue);
                break;
            case ResultsRowValue.RESULT_UPDATE:
                processChange(resultsSaveValue, rrv);
                // resultsSaveValue.addResultSaveValue(rrv.getPleaSaveValue(ResultSaveValue.UPDATE));

                break;
            case ResultsRowValue.RESULT_DELETE:
                // Cascade delete
                processDelete(resultsSaveValue, rrv);
                break;
            case ResultsRowValue.RESULT_UNCHANGED:
            default:
                break;
            }
        }
    }

    /**
     * Cascade Delete. Deletes all Verdicts/Disposals related to the plea.
     * 
     * @param deleteRRV
     */
    private void processDelete(ResultsSaveValue resultsSaveValue, ResultsRowValue deleteRRV)
            throws CSRecoverableException {
        // Delete Disposals
        if (deleteRRV.getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
            processBreachDisposalDelete(resultsSaveValue, deleteRRV);
        } else if (deleteRRV.getChargeType().equals(ChargeTypes.FAIL2APPEAR.getChargeType())) {
            processBailActDisposalDelete(resultsSaveValue, deleteRRV);
        }else {
            processDisposalDelete(resultsSaveValue, deleteRRV);
        }
        
        // Delete related Verdicts/Results if ChargeType is Indictment
        if (deleteRRV.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())
                && deleteRRV.getVerdictValue() != null) {
            ResultSaveValue resultSaveValue = deleteRRV.getVerdictSaveValue(ResultSaveValue.DELETE);
            resultSaveValue.setCourtLogged(false);
            resultsSaveValue.addResultSaveValue(resultSaveValue);
        }

        // Delete Plea
        resultsSaveValue.addResultSaveValue(deleteRRV.getPleaSaveValue(ResultSaveValue.DELETE));
    }

    /**
     * 
     * 
     */
    public void processChange(ResultsSaveValue resultsSaveValue, ResultsRowValue changedRRV) {

        // Delete related Indictment contains Verdict and a Guilty Plea
        if (changedRRV.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())
                && changedRRV.getVerdictValue() != null && changedRRV.isGuiltyPlea())

        {
            ResultSaveValue resultSaveValue = changedRRV.getVerdictSaveValue(ResultSaveValue.DELETE);
            resultSaveValue.setCourtLogged(false);
            resultsSaveValue.addResultSaveValue(resultSaveValue);
        }
        resultsSaveValue.addResultSaveValue(changedRRV.getPleaSaveValue(ResultSaveValue.UPDATE));
    }

    /**
     * Finds and deletes all disposals related to the plea
     * 
     * @param resultsSaveValue
     * @param deleteRRV
     */
    private void processDisposalDelete(ResultsSaveValue resultsSaveValue, ResultsRowValue deleteRRV)
            throws CSRecoverableException {
        // get list of disposal results using chargeType of deleteRRV
        java.util.List disposalList = getResultsHelper().getResultsForCharge(deleteRRV.getChargeType() + "D");

        // search for rrv matching the defendantOnOffenceId of the passed in RRV
        // and add to resultSaveValue as delete
        if (disposalList != null && disposalList.size() > 0) {
            ResultsRowValue listRRV = null;
            ResultSaveValue resultSaveValue = null;
            for (int i = 0; i < disposalList.size(); i++) {
                listRRV = (ResultsRowValue) disposalList.get(i);
                if (deleteRRV.getDefendantOnOffenceId().equals(listRRV.getDefendantOnOffenceId())
                        && listRRV.getDisposalValue() != null) {
                    resultSaveValue = listRRV.getDisposalSaveValue(ResultSaveValue.DELETE);
                    resultSaveValue.setCourtLogged(false);
                    resultsSaveValue.addResultSaveValue(resultSaveValue);
                }
            }
        }
    }

    /**
     * Finds and deletes all disposals related to the plea on Breach
     * 
     * @param resultsSaveValue
     * @param deleteRRV
     */
    private void processBreachDisposalDelete(ResultsSaveValue resultsSaveValue, ResultsRowValue deleteRRV)
            throws CSRecoverableException {

        // get list of disposal results using chargeType of deleteRRV
        java.util.List disposalList = getResultsHelper().getResultsForCharge(deleteRRV.getChargeType() + "D");

        // search for rrv matching the defendantOnOffenceId of the passed in RRV
        // and add to resultSaveValue as delete
        if (disposalList != null && disposalList.size() > 0) {
            ResultsRowValue listRRV = null;
            ResultSaveValue resultSaveValue = null;
            for (int i = 0; i < disposalList.size(); i++) {
                listRRV = (ResultsRowValue) disposalList.get(i);
                if (deleteRRV.getDefendantOnChargeId().equals(listRRV.getDefendantOnChargeId())
                        && listRRV.getDisposalValue() != null) {
                    resultSaveValue = listRRV.getDisposalSaveValue(ResultSaveValue.DELETE);
                    resultSaveValue.setCourtLogged(false);
                    resultsSaveValue.addResultSaveValue(resultSaveValue);
                }
            }
        }
    }// end of processBreachDisposalDelete

    /**
     * Finds and deletes all disposals related to the plea on Fail2 Appear charge (modelled as a Breach)
     * 
     * @param resultsSaveValue
     * @param deleteRRV
     */
    private void processBailActDisposalDelete(ResultsSaveValue resultsSaveValue, ResultsRowValue deleteRRV)
            throws CSRecoverableException {
        /* same functionality for Bail Act Offences as Breaches */
        processBreachDisposalDelete(resultsSaveValue,deleteRRV );
    }// end of processBailActDisposalDelete
    
    /**
     * Check if arraignmentDate entered on Multiple Plea Panel is before any
     * verdict Date that may be set for the selected pleas Call to show error
     * message if false
     * 
     * @param arraignmentDate
     * @param rrv
     * @return
     */
    public static boolean isMultipleArraignmentDateValid(java.util.Date arraignmentDate, PleaControllerModel model) {
        boolean earlierVerdict = false;
        ResultsRowValue rrv;
        int rows[] = model.getSelectedIndictments();
        PleaFilterModel indictmentFilterModel = model.getIndictmentFilterModel();

        for (int i = 0; i < rows.length; i++) {
            rrv = (ResultsRowValue) indictmentFilterModel.getDataAt(rows[i]);
            if (!PleaHelper.isArraignmentDateValid(arraignmentDate, rrv, false)) {
                // Set boolean if verdicts of an earlier date exist
                earlierVerdict = true;
            }
        }

        if (earlierVerdict) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.MultiPleaBeforeVerdictDate.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Check if arraignmentDate entered is before any verdict Date that may be
     * set Show Message
     * 
     * @param arraignmentDate
     * @param rrv
     * @return
     */
    public static boolean isArraignmentDateValid(java.util.Date arraignmentDate, ResultsRowValue rrv) {
        return isArraignmentDateValid(arraignmentDate, rrv, true);
    }

    /**
     * Check if arraignmentDate entered is before any verdict Date that may be
     * set
     * 
     * @param arraignmentDate
     * @param rrv
     * @return
     */
    public static boolean isArraignmentDateValid(java.util.Date arraignmentDate, ResultsRowValue rrv,
            boolean showMessage) {
        // Check arraignment date against verdict date
        if (rrv.getVerdictValue() != null && arraignmentDate.after(rrv.getVerdictDate())) {
            if (showMessage) {
                JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                        "Plea.BeforeVerdictDate.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                        "Plea.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }
        return true;
    }

    /**
     * checks if date passed in is in th future.
     * 
     * @param date
     * @return
     */
    public static boolean isFutureDate(java.util.Date date) {
        if (date.after(Calendar.getInstance().getTime())) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.DatePutFutureDate.Message"), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                    "Plea.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return true;
        }

        return false;
    }

    /**
     * 
     * @param ccv
     * @return
     */
    public Collection getAllIndictments(ChargeCompositeValue ccv) {
        Set<Integer> indictments = new TreeSet<Integer>();
        ChargeValue chargeValue;

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(INDICTMENT_CHARGE_TYPE)) {
                indictments.add(chargeValue.getCrestChargeSeqNo());
            }
        }
        return indictments;
    }

    /**
     * 
     * @param ccv
     * @param chargeType
     * @return
     */
    public Collection getAllCounts(ChargeCompositeValue ccv, String chargeType) {
        Set<Integer> indictmentsCounts = new TreeSet<Integer>();
        ChargeValue chargeValue;
        OffenceValue offenceValue;
        Collection offences;

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(chargeType)) {
                offences = chargeValue.getOffenceValues();
                Iterator offenceIterator = offences.iterator();
                while (offenceIterator.hasNext()) {
                    offenceValue = (OffenceValue) offenceIterator.next();
                    // offence must have a defendant to have a Plea
                    if (offenceValue.getDefendantValues() != null && offenceValue.getDefendantValues().size() > 0) {
                        indictmentsCounts.add(offenceValue.getCrestOffenceSeqNo());
                    }
                }
            }
        }
        return indictmentsCounts;
    }

    /**
     * 
     * @param ccv
     * @param chargeType
     * @return
     */
    public Collection getAllDefendants(ChargeCompositeValue ccv, String chargeType) {
        ChargeValue chargeValue;
        OffenceValue offenceValue;
        DefendantValue defendantValue;
        Collection offences;
        Collection defendants;
        Set<String> indictmentsDefendants = new TreeSet<String>();

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(chargeType)) {
                offences = chargeValue.getOffenceValues();
                Iterator offenceIterator = offences.iterator();
                while (offenceIterator.hasNext()) {
                    offenceValue = (OffenceValue) offenceIterator.next();
                    defendants = offenceValue.getDefendantValues();
                    Iterator defendantIterator = defendants.iterator();
                    while (defendantIterator.hasNext()) {
                        defendantValue = (DefendantValue) defendantIterator.next();
                        indictmentsDefendants.add(ResultsHelper.getName(defendantValue));
                    }
                }
            }
        }
        return indictmentsDefendants;
    }

    /**
     * 
     * @param chargeNo
     * @param ccv
     * @param chargeType
     * @return
     */
    public Collection getCountsOnIndictment(Integer chargeNo, ChargeCompositeValue ccv, String chargeType) {
        ChargeValue chargeValue;
        OffenceValue offenceValue;
        Collection offences;
        int seqNo = -1;

        Set<Integer> countsOnIndictment = new TreeSet<Integer>();

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(chargeType)) {
                if (chargeType.equalsIgnoreCase(INDICTMENT_CHARGE_TYPE)) {
                    seqNo = chargeValue.getCrestChargeSeqNo().intValue();
                } else if (chargeType.equalsIgnoreCase(SECTION41_CHARGE_TYPE)) {
                    seqNo = chargeValue.getChargeID().intValue();
                }

                if (seqNo == chargeNo.intValue()) {
                    offences = chargeValue.getOffenceValues();
                    Iterator offenceIterator = offences.iterator();
                    while (offenceIterator.hasNext()) {
                        offenceValue = (OffenceValue) offenceIterator.next();
                        // offence must have a defendant to have a Plea
                        if (offenceValue.getDefendantValues() != null && offenceValue.getDefendantValues().size() > 0) {
                            countsOnIndictment.add(offenceValue.getCrestOffenceSeqNo());
                        }
                    }
                }
            }
        }
        return countsOnIndictment;
    }

    /**
     * 
     * @param chargeNo
     * @param ccv
     * @param chargeType
     * @return
     */
    public Collection getDefendantsOnIndictment(Integer chargeNo, ChargeCompositeValue ccv, String chargeType) {
        ChargeValue chargeValue;
        OffenceValue offenceValue;
        DefendantValue defendantValue;
        Collection offences;
        Collection defendants;
        int seqNo = -1;

        Set<String> defendantsOnIndictment = new TreeSet<String>();

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(chargeType)) {
                if (chargeType.equalsIgnoreCase(INDICTMENT_CHARGE_TYPE)) {
                    seqNo = chargeValue.getCrestChargeSeqNo().intValue();
                } else if (chargeType.equalsIgnoreCase(SECTION41_CHARGE_TYPE)) {
                    seqNo = chargeValue.getChargeID().intValue();
                }

                if (seqNo == chargeNo.intValue()) {
                    offences = chargeValue.getOffenceValues();
                    Iterator offenceIterator = offences.iterator();
                    while (offenceIterator.hasNext()) {
                        offenceValue = (OffenceValue) offenceIterator.next();
                        defendants = offenceValue.getDefendantValues();
                        Iterator defendantIterator = defendants.iterator();
                        while (defendantIterator.hasNext()) {
                            defendantValue = (DefendantValue) defendantIterator.next();
                            defendantsOnIndictment.add(ResultsHelper.getName(defendantValue));
                        }
                    }
                }
            }
        }
        return defendantsOnIndictment;
    }

    /**
     * 
     * @param chargeNo
     * @param crestOffenceSeqNo
     * @param ccv
     * @param chargeType
     * @return
     */
    public Collection getDefendantsOnCount(Integer chargeNo, Integer crestOffenceSeqNo, ChargeCompositeValue ccv,
            String chargeType) {
        ChargeValue chargeValue;
        OffenceValue offenceValue;
        DefendantValue defendantValue;
        Collection offences;
        Collection defendants;
        int seqNo = -1;

        Set<String> defendantsOnCount = new TreeSet<String>();

        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(chargeType)) {
                if (chargeType.equalsIgnoreCase(INDICTMENT_CHARGE_TYPE)) {
                    seqNo = chargeValue.getCrestChargeSeqNo().intValue();
                } else if (chargeType.equalsIgnoreCase(SECTION41_CHARGE_TYPE)) {
                    seqNo = chargeValue.getChargeID().intValue();
                }

                if (seqNo == chargeNo.intValue()) {
                    offences = chargeValue.getOffenceValues();
                    Iterator offenceIterator = offences.iterator();
                    while (offenceIterator.hasNext()) {
                        offenceValue = (OffenceValue) offenceIterator.next();
                        if (offenceValue.getCrestOffenceSeqNo().intValue() == crestOffenceSeqNo.intValue()) {
                            defendants = offenceValue.getDefendantValues();
                            Iterator defendantIterator = defendants.iterator();
                            while (defendantIterator.hasNext()) {
                                defendantValue = (DefendantValue) defendantIterator.next();
                                defendantsOnCount.add(ResultsHelper.getName(defendantValue));
                            }
                        }
                    }
                }
            }
        }
        return defendantsOnCount;
    }

    /**
     * 
     * @param ccv
     * @return
     */
    public static Integer getSection41(ChargeCompositeValue ccv) {
        ChargeValue chargeValue;
        Collection charges = ccv.getCharges();
        Iterator chargesIterator = charges.iterator();
        while (chargesIterator.hasNext()) {
            chargeValue = (ChargeValue) chargesIterator.next();
            if (chargeValue.getChargeType().equals(SECTION41_CHARGE_TYPE)) {
                return chargeValue.getChargeID();
            }
        }
        return null;
    }

    /**
     * Determines by checking the number of verdicts/disposals, whether to warn
     * the user about the consequence of deleting the plea on the passed in row.
     * 
     * @param dataRow
     * @throws UserCancelException
     */
    public void processRowDelete(ResultsRowValue dataRow) throws UserCancelException {
        int rc = JOptionPane.DEFAULT_OPTION;
        int disposalCount = dataRow.getDisposalCount();
        boolean verdict = dataRow.getVerdictValue() != null;
        if (disposalCount > 0 || verdict) {
            String space = " ";
            rc = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(),
                    ResourceBundleHelper.getResource(XhibitBundles.Pleas, "Plea.DeletePlea.IntroMessage") + // Is
                                                                                                            // there
                                                                                                            // a
                                                                                                            // verdict?
                            (verdict ? space
                                    + ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                                            "Plea.DeletePlea.VerdictText") : "") + // Is
                            // there
                            // both
                            // a
                            // verdicts
                            // and
                            // Disposals?
                            (verdict && disposalCount > 0 ? space
                                    + ResourceBundleHelper.getResource(XhibitBundles.Pleas, "Plea.DeletePlea.And")
                                    : space) + // Are there disposals?
                            (disposalCount > 0 ? MessageFormat.format(ResourceBundleHelper.getResource(
                                    XhibitBundles.Pleas, "Plea.DeletePlea.DisposalText"), new Object[] { space
                                    + disposalCount }) : space) + // Question
                            ResourceBundleHelper.getResource(XhibitBundles.Pleas, "Plea.DeletePlea.Question"),
                    ResourceBundleHelper.getResource(XhibitBundles.Pleas, "Plea.DeletePlea.Title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                // carry on
            } else {
                throw new UserCancelException();
            }
        } else {
            rc = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(), ResourceBundleHelper.getResource(
                    XhibitBundles.Pleas, "Plea.DeletePlea.NoResults"), ResourceBundleHelper.getResource(
                    XhibitBundles.Pleas, "Plea.DeletePlea.Title"), JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (rc == JOptionPane.YES_OPTION) {
                // carry on
            } else {
                throw new UserCancelException();
            }
        }
    }

    /**
     * Determines by checking the selected plea is a guilty from a non-guilty
     * and if a verdict exists, whether to warn the user about the consequence
     * of changing the plea on the passed in row.
     * 
     * @param dataRow
     * @param decode
     */
    public boolean processGuiltyPlea(ResultsRowValue dataRow, String code) {
        boolean returnValue = true;
        if ((Arrays.binarySearch(VerdictHelper.NOT_DISPLAY_VERDICT_FOR_PLEA_CODES, code) >= 0)
                && Arrays.binarySearch(VerdictHelper.NOT_DISPLAY_VERDICT_FOR_PLEA_CODES, dataRow.getPleaCode()) < 0) {
            if (dataRow.getVerdictValue() != null) {
                returnValue = processGuiltyPleaPrompt();
            }
        }
        return returnValue;
    }

    private boolean processGuiltyPleaPrompt() {
        int rc = JOptionPane.DEFAULT_OPTION;
        rc = JOptionPane.showConfirmDialog(acm.getXhibitApplicationController(), ResourceBundleHelper.getResource(
                XhibitBundles.Pleas, "Plea.Update.GuiltyPlea.Message"), ResourceBundleHelper.getResource(
                XhibitBundles.Pleas, "Plea.Update.GuiltyPlea.Title"), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return rc == JOptionPane.YES_OPTION;
    }
}