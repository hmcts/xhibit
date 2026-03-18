package uk.gov.courtservice.xhibit.client.results.verdicts;

// Java
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.results.ResultsHelper;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

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
 * @author Simon Gilmore
 * @HITSORY kelvin davies - 04062009 - ccn1260 - updated to include new verdict codes
 */
public class VerdictHelper {
    private static final Logger log = CSServices.getLogger(VerdictHelper.class);

    public static final String OTHER_TEXT = "O";

    public static final String LESSER_OFFENCE_NOT_NAMELY_CHARGED = "GL";

    public static final String LESSER_OFFENCE_JUDGE_DIRECTION = "GLJ";

    public static final String ALTERNATE_OFFENCE_NOT_NAMELY_CHARGED = "GA";

    public static final String ALTERNATE_OFFENCE_JUDGE_DIRECTION = "GAJ";

    public static final String GUILTY = "G";

    public static final String GUILTY_BY_OTHER_JURY = "RTG";

    public static final String GUILTY_BY_JURY_JUDGE = "GJJ";
    
    public static final String GUILTY_BY_JUDGE_ALONE_DVC_VA = "GJ";
    
    public static final String ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA ="GAOJ"; 
    
    public static final String LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA = "GLOJ";

    public static final String DEFENDANT_FOUND_UNDER_DISABILITY = "DUD";

    public static final int SEARCH_TYPE_VERDICT_CODE = 1;

    public static final int SEARCH_TYPE_VERDICT_DESCRIPTION = 2;

    // If a count/defendant has any of these Plea Codes it will be displayed
    // on
    // the verdict screen. Note - codes must be in alphabetical order!
    // private static final String[] DISPLAY_VERDICT_FOR_PLEA_CODES =
    // new String[] { "AA", "AC", "CPGJ", "CPNG", "NG", "NPT", "P", "O"};
    public static final String[] NOT_DISPLAY_VERDICT_FOR_PLEA_CODES = new String[] { "CPG", "G", "GAO", "GLO" };
    
    public static String errorTitle = ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "Verdict.error.Title");
    public static String errorMessage = ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "Verdict.error.Message");


    private static final Collection verdictRefData = createVerdictRefData();

    private static VerdictRestrictionHelper verdictRestriction = VerdictRestrictionHelper
            .getInstance(getVerdictRefData());

    private VerdictHelper() {
    }

    private static Collection createVerdictRefData() {
        Collection refData = null;
        try {
            RefSystemCodeCriteria rfsc = new RefSystemCodeCriteria();
            rfsc.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            rfsc.setCodeType(RefSystemCodeCriteria.CodeType.VERDICT);
            refData = ResultsHelper.getBisRefDelegate().findSystemCodes(rfsc);
            Sorter.sort((List) refData, new String[] { "decode" });
        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }
        return refData;
    }

    /**
     * return Verdict Reference data
     * 
     * @return
     */
    public static Collection getVerdictRefData() {
        return verdictRefData;
    }

    /**
     * Return RefSystemCodeBasicValue for using verdictId and collection of
     * refdata passed in.
     * 
     * @param verdictId
     * @param refData
     * @return
     */
    public static RefSystemCodeBasicValue getRefSystemCodeBasicValue(Integer verdictId, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            Integer id = rscbv.getId();
            log.debug("getRefSystemCodeBasicValue: verdictId = [" + verdictId + "] id = [" + id + "]");
            if (verdictId.equals(id)) {
                return rscbv;
            }
        }
        return null;
    }

    /**
     * Loops through verdictRefData and depnding on the searchType compares the
     * appropriate attribute in the RefSystemCodeBasicValue with the
     * searchString.
     * 
     * @param verdictRefData
     * @param searchString
     * @param searchType
     * @return
     */
    public static RefSystemCodeBasicValue getRefSystemCodeBasicValue(Collection refData, String searchString,
            int searchType) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();

            switch (searchType) {
            case SEARCH_TYPE_VERDICT_CODE:
                code = rscbv.getCode();
                break;
            case SEARCH_TYPE_VERDICT_DESCRIPTION:
                code = rscbv.getDecode();
                break;
            default:
                return null;
            }

            log.debug("getRefSystemCodeBasicValue: searchString = [" + searchString + "] code = [" + code + "]");
            if (searchString.equalsIgnoreCase(code)) {
                return rscbv;
            }
        }
        return null;
    }

    /**
     * Determines whether verdictCode is requires an alternate offence
     * 
     * @param verdictCode
     * @return
     */
    public static boolean hasAlternateOffence(String verdictCode) {
        return (verdictCode.equals(ALTERNATE_OFFENCE_JUDGE_DIRECTION)
                || verdictCode.equals(ALTERNATE_OFFENCE_NOT_NAMELY_CHARGED)
                || verdictCode.equals(LESSER_OFFENCE_JUDGE_DIRECTION) || verdictCode
                .equals(LESSER_OFFENCE_NOT_NAMELY_CHARGED) || verdictCode.equals(ALTERNATIVE_OFFENCE_NOT_CHARGED_NAMELY_DVC_VA)
                || verdictCode.equals(LESSER_OFFENCE_NOT_NAMELY_CHARGED_DVC_VA));
    }

    /**
     * Determines whether the verdictCode requires other text
     * 
     * @param verdictCode
     * @return
     */
    public static boolean hasOtherText(String verdictCode) {
        return (verdictCode.equals(OTHER_TEXT));
    }

    /**
     * Determines whether the verdictCode requires juror counts
     * 
     * @param verdictCode
     * @return
     */
    public static boolean hasJurorCounts(String verdictCode) {
        return GUILTY.equals(verdictCode) || ALTERNATE_OFFENCE_NOT_NAMELY_CHARGED.equals(verdictCode)
                || LESSER_OFFENCE_NOT_NAMELY_CHARGED.equals(verdictCode) || GUILTY_BY_OTHER_JURY.equals(verdictCode);
    }

    /**
     * Optionally has juror counts
     */
    public static boolean hasJurorCountsOptional(String verdictCode) {
        return DEFENDANT_FOUND_UNDER_DISABILITY.equals(verdictCode)
                || ALTERNATE_OFFENCE_JUDGE_DIRECTION.equals(verdictCode)
                || LESSER_OFFENCE_JUDGE_DIRECTION.equals(verdictCode) || OTHER_TEXT.equals(verdictCode);
    }

    /**
     * Loops thorugh refData and finds the refVerdictId for the passed in
     * refVerdictCode
     * 
     * @param refVerdictCode
     * @param verdictRefData
     * @return
     */
    public static Integer getRefVerdictId(String refVerdictCode, Collection refData) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = refData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            code = rscbv.getCode();
            log.debug("\nMethod: getRefVerdictId: " + "\nrefVerdictCode [" + refVerdictCode + "]" + "\n       code ["
                    + code + "]");
            if (code.equals(refVerdictCode)) {
                return rscbv.getId();
            }
        }
        return null;
    }

    /**
     * Loops thorugh verdictRefData and finds the refVerdictDescription for the
     * passed in refVerdictCode
     * 
     * @param refVerdictCode
     * @param verdictRefData
     * @return
     */
    public static String getRefVerdictDescription(String refVerdictCode, Collection verdictRefData) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = verdictRefData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            code = rscbv.getCode();
            log.debug("\nMethod: getRefVerdictDescription: " + "\nrefVerdictCode [" + refVerdictCode + "]"
                    + "\n       code [" + code + "]");
            if (code.equals(refVerdictCode)) {
                return rscbv.getDecode();
            }
        }
        return "";
    }

    /**
     * Loops thorugh verdictRefData and finds the refVerdictType for the passed
     * in refVerdictCode
     * 
     * @param refVerdictCode
     * @param verdictRefData
     * @return
     */
    public static String getRefVerdictCodeType(String refVerdictCode, Collection verdictRefData) {
        RefSystemCodeBasicValue rscbv;
        String code;
        Iterator i = verdictRefData.iterator();
        while (i.hasNext()) {
            rscbv = (RefSystemCodeBasicValue) i.next();
            code = rscbv.getCode();
            log.debug("\nMethod: getRefVerdictCodeType: " + "\nrefVerdictCode [" + refVerdictCode + "]"
                    + "\n       code [" + code + "]");
            if (code.equals(refVerdictCode)) {
                return rscbv.getCodeType();
            }
        }
        return "";
    }

    /**
     * Check if entered verdict code id valid. The code is checked against any
     * restrictions that may be set on the plea
     * 
     * @param code
     * @param pleaValue
     * @return
     */
    public static boolean isVerdictCodeValid(String code, PleaValue pleaValue) {
        Integer id = null;
        // If the current plea for this row has restrictions on which verdict
        // can be entered
        // then validate against list from model
        if (verdictRestriction.hasVerdictRestrictions(pleaValue.getRefPleaCode())) {
            String[] validCodes = verdictRestriction.getRestrictedVerdictCodes(pleaValue.getRefPleaCode());
            boolean found = false;
            for (int i = 0; i < validCodes.length; i++) {
                if (code.equalsIgnoreCase(validCodes[i])) {
                    found = true;
                    break;
                }
            }
            if (found)
                id = VerdictHelper.getRefVerdictId(code, VerdictHelper.getVerdictRefData());
        } else {
            id = VerdictHelper.getRefVerdictId(code, VerdictHelper.getVerdictRefData());
        }

        if (id == null) {
            JOptionPane.showMessageDialog(null, VerdictHelper.errorMessage, VerdictHelper.errorTitle,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Gets a populates ResultsSaveValue with verdict values to be added,
     * updated and deleted. Performs appropriate validation during this process
     * which thorows a CSValidationException on failure Resets action on
     * ResultsRowValue
     * 
     * @param tableModel
     *            XHIBITTableModelInterface.
     */
    protected static void setAlteredFlags(XHIBITTableModelInterface tableModel, ResultsSaveValue resultsSaveValue)
            throws CSValidationException {
        ResultsRowValue rrv;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rrv = (ResultsRowValue) tableModel.getDataAt(i);

            VerdictSaveValue verdictSaveValue;
            VerdictValue verdictValue;

            switch (rrv.getAction()) {
            case ResultsRowValue.RESULT_ADD:
                verdictValue = rrv.getVerdictValue();
                verdictSaveValue = rrv.getVerdictSaveValue(ResultSaveValue.ADD);
                resultsSaveValue.addResultSaveValue(verdictSaveValue);
                break;
            case ResultsRowValue.RESULT_UPDATE:
                verdictValue = rrv.getVerdictValue();
                validateVerdictCode(verdictValue);
                resultsSaveValue.addResultSaveValue(rrv.getVerdictSaveValue(ResultSaveValue.UPDATE));
                break;
            case ResultsRowValue.RESULT_DELETE:
                resultsSaveValue.addResultSaveValue(rrv.getVerdictSaveValue(ResultSaveValue.DELETE));
                break;
            case ResultsRowValue.RESULT_UNCHANGED:
            default:
                break;
            }
        }
    }

    /**
     * Check if the verdict id is zero. This occurs when the user edits the
     * verdict code by and and deletes the entry and then saves.
     * 
     * @param values
     *            of VerdictValue
     * @throws CSValidationException
     */
    public static void validateVerdictCode(VerdictValue value) throws CSValidationException {
        if (value.getXhbVerdictBasicValue().getRefVerdictId().equals(new Integer(0))) {
            throw new CSValidationException("gui.user.VerdictHelper.verdictcode", "Invalid verdict code");
        }
    }

    public static boolean isVerdictDateValid(java.util.Date verdictDate, ResultsRowValue rrv) // throws
                                                                                                // UserCancelException
    {
        // Check verdict date against current date
        if (verdictDate.after(Calendar.getInstance().getTime())) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.BeforeCurrentDate.Message"), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check verdct date against arraignment date
        if (verdictDate.before((java.util.Date) rrv.getPleaArraignmentDate())) {
            JOptionPane.showMessageDialog(null, ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.AfterArraignmentDate.Message"), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                    "Verdict.InvalidDate.Title"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Takes a collection of ResultsRowValue and removes any whose Plea Code is
     * not in the array of Plea Codes or a Plea does not exist.
     * 
     * @param results
     *            collection of ResultsRowValue that populate the verdict
     *            indictment table model.
     */
    public static void removeGuiltyPleaAndNoPleaIndictmentResults(Collection results) {
        if (results != null) {
            ResultsRowValue rrv = null;
            Iterator i = results.iterator();
            while (i.hasNext()) {
                rrv = (ResultsRowValue) i.next();
                if (rrv.getPleaValue() != null) {
                    String pleaCode = rrv.getPleaValue().getRefPleaCode();

                    // If a Plea has been entered and it is not in the
                    // array, then
                    // it is not to be displayed on the verdict screen, so
                    // remove it.
                    if (pleaCode != null && Arrays.binarySearch(NOT_DISPLAY_VERDICT_FOR_PLEA_CODES, pleaCode) >= 0) {
                        i.remove();
                    }
                } else {
                    // Need to have pleas before verdicts can be added.
                    i.remove();
                }
            }
        }
    }

    /**
     * Determines by checking the number of verdicts/disposals, whether to warn
     * the user about the consequence of deleting the verdict on the passed in
     * row.
     * 
     * @param dataRow
     * @throws UserCancelException
     */
    public static void processRowDelete(ResultsRowValue dataRow, java.awt.Frame parent) throws UserCancelException {
        int rc = JOptionPane.DEFAULT_OPTION;

        rc = JOptionPane.showConfirmDialog(parent, ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                "Verdict.DeleteVerdict.NoResults"), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                "Verdict.DeleteVerdict.Title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (rc == JOptionPane.YES_OPTION) {
            // carry on
        } else {
            throw new UserCancelException();
        }
    }
}
