package uk.gov.courtservice.xhibit.client.results;

import java.util.Calendar;
import java.util.Date;

import javax.swing.Icon;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.results.appealresults.AppealResultsHelper;
import uk.gov.courtservice.xhibit.client.results.pleas.PleaHelper;
import uk.gov.courtservice.xhibit.client.results.util.table.AdditionalInfoTableCellComponent;
import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictHelper;
import uk.gov.courtservice.xhibit.client.util.IconFactory;
import uk.gov.courtservice.xhibit.client.util.PropertyChangeHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;

/**
 * <p>
 * Title: XHIBIT 2 Results
 * </p>
 * <p>
 * Description: A value object to represent a row in the table for results.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.87 $
 */
public class ResultsRowValue extends PropertyChangeHelper implements Cloneable {
    public static final int RESULT_UNCHANGED = 0;

    public static final int RESULT_DELETE = 1;

    public static final int RESULT_UPDATE = 2;

    public static final int RESULT_ADD = 3;

    public static final String DELETED = "DELETED: ";

    private static final String PLEA_CODE_GUILTY = "G";

    private static final String emptyStr = "";

    private static final String APPEAL_AGAINST_RESOURCE = "criminal.appealagainst.";

    private static final Logger log = CSServices.getLogger(ResultsRowValue.class);

    // the immutable fields, only to be set in the constructor...
    private final Integer caseId;

    private final Integer caseNumber;

    private final String caseType;

    private final String caseSubType;

    private final Integer scheduledHearingId;

    private final ScheduledHearingValue scheduledHearingValue;

    // Helper
    private final PleaHelper pleaHelper;

    private Integer defendantOnOffenceId;

    private Integer defendantOnChargeId;

    private Integer chargeSequenceNumber;

    private Integer offenceSequenceNumber;

    private Integer defendantOnCaseId;

    private DisposalSaveValue disposalSaveValue;

    private DisposalValue disposalValue;

    private DisposalValue variationDisposal;

    private DisposalReferenceValue disposalReferenceValue;

    private Integer psdDisId;

    private ChargeValue chargeValue;

    private OffenceValue offenceValue;

    private DefendantValue defendantValue;

    private PleaSaveValue pleaSaveValue;

    private PleaValue pleaValue;

    private PleaValue deletePleaValue;

    private VerdictSaveValue verdictSaveValue;

    private VerdictValue verdictValue;

    private VerdictValue deleteVerdictValue;

    private String chargeType;

    private DefendantOnOffenceComplexValue defendantOnOffenceValue;

    private String breachOffencesText = emptyStr;

    private boolean modified = false;

    private boolean breach = false;

    private int action = RESULT_UNCHANGED;

    private int preDeleteAction = RESULT_UNCHANGED;

    private int disposalCount = 0;

    private int variationDisposalCount = 0;

    private RefAppResultBasicValue rarbv = null;

    private XhibitApplicationController xac;

    private ApplicationCaseModel acm;

    private String receiptType = null;

    public ResultsRowValue(ApplicationCaseModel acm) {
        // this.verdictValue = verdictValue;
        this.acm = acm;
        this.caseType = acm.getCaseType();
        this.caseSubType = acm.getCaseSubType();
        this.caseNumber = acm.getCaseNumber();
        this.caseId = acm.getCaseId();
        this.scheduledHearingId = acm.getScheduledHearingId();
        this.scheduledHearingValue = acm.getScheduledHearingValue();
        this.xac = acm.getXhibitApplicationController();
        this.receiptType = acm.getScheduledHearingValue().getCaseBasicValue().getReceiptType();
        pleaHelper = new PleaHelper(acm);
    }

    // ******************************************************************************
    // Generic Row Data
    // ******************************************************************************

    public String getReceiptType() {
        return receiptType;
    }

    /**
     * Defendant on offence Id
     * 
     * @return id
     */
    public Integer getDefendantOnOffenceId() {
        return defendantOnOffenceId;
    }

    public void setDefendantOnOffenceId(Integer defendantOnOffenceId) {
        this.defendantOnOffenceId = defendantOnOffenceId;
    }

    /**
     * Defendant on charge Id Used for Miscellaneous Appeals
     * 
     * @return id
     */
    public Integer getDefendantOnChargeId() {
        return defendantOnChargeId;
    }

    public void setDefendantOnChargeId(Integer defendantOnChargeId) {
        this.defendantOnChargeId = defendantOnChargeId;
    }

    /**
     * Defendant on case Id Used for Unrelated Disposals
     * 
     * @return id
     */
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }

    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }

    /**
     * 
     * @param psdDisId
     */
    public void setPsdDisId(Integer psdDisId) {
        this.psdDisId = psdDisId;
    }

    /**
     * Set the modified flag<br>
     * To listen for this event add a PropertyChangeListener listening for value
     * 'modified'
     * 
     * @param isModified
     */
    public void setModified(boolean isModified) {
        boolean oldModified = modified;
        modified = isModified;
        firePropertyChange("modified", oldModified, isModified);
    }

    /**
     * Set the action flag<br>
     * To listen for this event add a PropertyChangeListener listening for value
     * 'action'
     * 
     * @param newAction
     */
    public void setAction(int newAction) {
        int oldAction = action;
        action = newAction;
        firePropertyChange("action", oldAction, action);
    }

    /**
     * @return The current state of the modified flag
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @return The current state of the preDeleteAction flag
     */
    public int getAction() {
        return action;
    }

    /**
     * The preDeleteAction is primarily required for the Disposal Screen
     * 
     * @param newAction
     */
    public void setPreDeleteAction(int newAction) {
        preDeleteAction = newAction;
    }

    public int getPreDeleteAction() {
        return preDeleteAction;
    }

    /**
     * Charge Sequence Number Can also be used for sorting
     * 
     * @return sequence number
     */
    public Integer getChargeSequenceNumber() {
        if (chargeSequenceNumber == null) {
            return new Integer(0);
        }

        return chargeSequenceNumber;
    }

    public void setChargeSequenceNumber(Integer chargeSequenceNumber) {
        this.chargeSequenceNumber = chargeSequenceNumber;
    }

    /**
     * Returns charge sequence no plus breach Ho description
     * 
     * @return String
     */
    public String getBreachChargeData() {
        return getChargeSequenceNumber() + ": " + getChargeValue().getBreachValue().getHoDescription();
    }

    /**
     * Offence Sequence Number Can also be used for sorting
     * 
     * @return sequence number
     */
    public Integer getOffenceSequenceNumber() {
        if (offenceSequenceNumber == null) {
            return new Integer(0);
        }

        return offenceSequenceNumber;
    }

    public void setOffenceSequenceNumber(Integer offenceSequenceNumber) {
        this.offenceSequenceNumber = offenceSequenceNumber;
    }

    /**
     * The disposal for the defendant Null if disposal not requested
     * 
     * @return value object
     */
    public DisposalValue getDisposalValue() {
        return disposalValue;
    }

    public void setDisposalValue(DisposalValue disposalValue) {
        this.disposalValue = disposalValue;
    }

    public DisposalValue getVariationDisposalValue() {
        return variationDisposal;
    }

    public void setVariationDisposalValue(DisposalValue variationDisposal) {
        this.variationDisposal = variationDisposal;
    }

    /**
     * The disposalReferenceValue for the defendant Null if disposal not
     * requested
     * 
     * @return value object
     */
    public DisposalReferenceValue getDisposalReferenceValue() {
        return disposalReferenceValue;
    }

    public void setDisposalReferenceValue(DisposalReferenceValue disposalReferenceValue) {
        this.disposalReferenceValue = disposalReferenceValue;
    }

    /**
     * Charge Value
     * 
     * @return value object
     */
    public ChargeValue getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(ChargeValue chargeValue) {
        this.chargeValue = chargeValue;
    }

    /**
     * Offence Value
     * 
     * @return value object
     */
    public OffenceValue getOffenceValue() {
        return offenceValue;
    }

    public void setOffenceValue(OffenceValue offenceValue) {
        this.offenceValue = offenceValue;
    }

    /**
     * Defendant Value
     * 
     * @return value object
     */
    public DefendantValue getDefendantValue() {
        return defendantValue;
    }

    public void setDefendantValue(DefendantValue defendantValue) {
        this.defendantValue = defendantValue;
    }

    /**
     * Defendant Surname
     * 
     * @return value object
     */
    public String getSurName() {
        return defendantValue.getSurName();
    }

    public void setSurName(String surName) {
        this.defendantValue.setSurName(surName);
    }

    /**
     * Defendant Surname
     * 
     * @return value object
     */
    public String getFirstName() {
        return defendantValue.getFirstName();
    }

    public void setFirstName(String firstName) {
        this.defendantValue.setFirstName(firstName);
    }

    /**
     * The plea for the defendant Null if plea not requested
     * 
     * @return value object
     */
    public PleaValue getPleaValue() {
        return pleaValue;
    }

    public void setPleaValue(PleaValue pleaValue) {
        this.pleaValue = pleaValue;
    }

    /**
     * The plea for the defendant to be deleted Null if plea not requested
     * 
     * @return value object
     */
    public PleaValue getDeletePleaValue() {
        return deletePleaValue;
    }

    public void setDeletePleaValue(PleaValue deletePleaValue) {
        this.deletePleaValue = deletePleaValue;
    }

    // ******************************************************************************
    // Table Model specific methods
    // ******************************************************************************

    /**
     * Return appropriate icon based on row altered status
     * 
     * @return Icon
     */
    public Icon getRowAltered() {
        if (getAction() != ResultsRowValue.RESULT_UNCHANGED) {
            return IconFactory.createTickIcon();
        }

        // return blank icon if not selected...
        return IconFactory.createBlankIcon();
    }

    /**
     * Checks if the given object is set
     * 
     * @param obj
     *            the Object to check.
     * @return true if the given object is not null. If the given object is a
     *         String it must contain a value other than "" or null.
     */
    private boolean isSet(Object obj) {
        // If null then return not set
        if (obj == null)
            return false;

        // If Not a String then return true. Method only detects if not null
        if (!(obj instanceof String))
            return true;

        // If String then check the String length
        if (((String) obj).length() == 0)
            return false;

        return true;
    }

    /**
     * constructs count Description from OffenceValue attributes
     * 
     * @return String
     */
    public String getCountDescription() {
        if (offenceValue != null) {
            Integer offenceSeqNo = getOffenceValue().getCrestOffenceSeqNo();
            String offenceDesc = getOffenceValue().getOffenceDescription();
            return "Count " + offenceSeqNo + " " + offenceDesc;
        }
        return emptyStr;
    }

    /**
     * constructs offeneDescription from OffenceValue attributes
     * 
     * @return String
     */
    public String getOffenceDescription() {
        if (offenceValue != null) {
            Integer offenceSeqNo = getOffenceValue().getCrestOffenceSeqNo();
            String offenceDesc = getOffenceValue().getOffenceDescription();
            return offenceSeqNo + ". " + offenceDesc;
        }
        return emptyStr;
    }

    // **************************************************************************
    // Plea Table Model specific methods
    // **************************************************************************

    /**
     * Calls getOperationalPlea(Calendar arraignmentDate) passing in null. This
     * is being for Section 41 Plea Screen.
     * 
     * @return
     */
    public void setOperationalPlea() {
        setOperationalPlea(null);
    }

    /**
     * Creates/Updates an operation Plea based on row status. Checks status of
     * pleaValue and sets appropriate Row status
     * 
     * @param arraignmentDate
     */
    public void setOperationalPlea(Calendar arraignmentDate) {
        if (pleaValue == null) {
            // Check if previous action was a Delete. If so we want to
            // restore previous pleaValue.
            if (getAction() == ResultsRowValue.RESULT_DELETE) {
                pleaValue = this.getDeletePleaValue();
                setAction(ResultsRowValue.RESULT_UPDATE);
            } else {
                // Adding new plea
                setAction(ResultsRowValue.RESULT_ADD);
                pleaValue = new PleaValue();
                pleaValue.setArraignmentDate(arraignmentDate);
                setPleaValue(pleaValue);
            }
        } else {
            // Check if row is being added but plea is changed before
            // saving.
            if (getAction() != ResultsRowValue.RESULT_ADD) {
                // Updating existing plea
                setAction(ResultsRowValue.RESULT_UPDATE);
            }
        }

        // set basic information (non plea specific)
        pleaValue.setDefendantOnOffenceId(getDefendantOnOffenceId());
    }

    /**
     * Creates/Updates an operation Plea based on row status for Breach. Checks
     * status of pleaValue and sets appropriate Row status
     * 
     * @param arraignmentDate
     */
    public void setOperationalBreachPlea() {
        if (pleaValue == null) {
            // Check if previous action was a Delete. If so we want to
            // restore previous pleaValue.
            if (getAction() == ResultsRowValue.RESULT_DELETE) {
                setAction(ResultsRowValue.RESULT_UPDATE);
            } else {
                // Adding new plea
                setAction(ResultsRowValue.RESULT_ADD);
                pleaValue = new PleaValue();
                if (getChargeValue() != null && getChargeValue().getBreachValue() != null
                        && getChargeValue().getBreachValue().getDatePut() != null) {
                    pleaValue.setDatePut(getChargeValue().getBreachValue().getDatePut().getTime());
                }
                pleaValue.setDefendantChargeId(getDefendantOnChargeId());
                setPleaValue(pleaValue);
            }
        } else {
            // Check if row is being added but plea is changed before
            // saving.
            if (getAction() != ResultsRowValue.RESULT_ADD) {
                // Updating existing plea
                setAction(ResultsRowValue.RESULT_UPDATE);
            }
        }
    }
    
    /**
     * Creates/Updates an operation Plea based on row status for BailAct. Checks
     * status of pleaValue and sets appropriate Row status
     * 
     * @param arraignmentDate
     */
    public void setOperationalBailActPlea() {
        if (pleaValue == null) {
            // Check if previous action was a Delete. If so we want to
            // restore previous pleaValue.
            if (getAction() == ResultsRowValue.RESULT_DELETE) {
                setAction(ResultsRowValue.RESULT_UPDATE);
            } else {
                // Adding new plea
                setAction(ResultsRowValue.RESULT_ADD);
                pleaValue = new PleaValue();
                if (getChargeValue() != null && getChargeValue().getBreachValue() != null
                        && getChargeValue().getBreachValue().getDatePut() != null) {
                    pleaValue.setDatePut(getChargeValue().getBreachValue().getDatePut().getTime());
                }
                pleaValue.setDefendantChargeId(getDefendantOnChargeId());
                setPleaValue(pleaValue);
            }
        } else {
            // Check if row is being added but plea is changed before
            // saving.
            if (getAction() != ResultsRowValue.RESULT_ADD) {
                // Updating existing plea
                setAction(ResultsRowValue.RESULT_UPDATE);
            }
        }
    } // end of setOperationalBailActPlea

    /**
     * Determines the row status and sets accordingly. calls processing for
     * cascade delete if necessary.
     */
    public void processDeletedPlea() {
        // If current action is new then reset action to UNCHANGED else set to
        // DELETE
        if (getAction() == ResultsRowValue.RESULT_ADD) {
            setAction(ResultsRowValue.RESULT_UNCHANGED);
        } else {
            try {
                // Does this row have verdicts/disposals
                pleaHelper.processRowDelete(this);
                setAction(ResultsRowValue.RESULT_DELETE);
                this.setDeletePleaValue(pleaValue);
            } catch (UserCancelException uce) {
                setAction(ResultsRowValue.RESULT_UNCHANGED);
                return;
            }
        }
        this.setPleaValue(null);
    }

    /**
     * Sets reference attributes to PleaValue searching by Plea code for
     * Indictment
     * 
     * @param code
     * @param arraignmentDate
     * @throws CSRecoverableException
     */
    public void setRefIndictmentPleaCode(String code, Calendar arraignmentDate) {
        RefSystemCodeBasicValue rscbv = PleaHelper.getRefSystemCodeBasicValue(PleaHelper.getPleaRefData(), code,
                PleaHelper.SEARCH_TYPE_PLEA_CODE);

        pleaValue.setRefPleaId(rscbv.getId());
        pleaValue.setRefPleaCode(code);
        pleaValue.setRefPleaDesc(rscbv.getDecode());
        checkPleaArraignmentDate(arraignmentDate);
        clearPleaAdditionalInfo();
    }

    /**
     * Sets reference attributes to PleaValue searching by Plea description
     * 
     * @param desc
     * @param arraignmentDate
     * @throws CSRecoverableException
     */
    public void setRefIndictmentPleaDesc(String desc, Calendar arraignmentDate) {
        RefSystemCodeBasicValue rscbv = PleaHelper.getRefSystemCodeBasicValue(PleaHelper.getPleaRefData(), desc,
                PleaHelper.SEARCH_TYPE_PLEA_DESCRIPTION);

        pleaValue.setRefPleaId(rscbv.getId());
        pleaValue.setRefPleaCode(rscbv.getCode());
        pleaValue.setRefPleaDesc(rscbv.getDecode());
        checkPleaArraignmentDate(arraignmentDate);
        clearPleaAdditionalInfo();
    }

    /**
     * Sets reference attributes to PleaValue searching by Plea code for Section
     * 41
     * 
     * @param code
     * @param arraignmentDate
     * @throws CSRecoverableException
     */
    public void setRefS41PleaCode(String code) {
        RefSystemCodeBasicValue rscbv = PleaHelper.getRefSystemCodeBasicValue(PleaHelper.getPleaS41RefData(), code,
                PleaHelper.SEARCH_TYPE_PLEA_CODE);

        pleaValue.setRefPleaId(rscbv.getId());
        pleaValue.setRefPleaCode(code);
        pleaValue.setRefPleaDesc(rscbv.getDecode());
    }

    /**
     * Sets reference attributes to PleaValue searching by Plea description
     * 
     * @param desc
     * @param arraignmentDate
     * @throws CSRecoverableException
     */
    public void setRefS41PleaDesc(String desc) {
        RefSystemCodeBasicValue rscbv = PleaHelper.getRefSystemCodeBasicValue(PleaHelper.getPleaS41RefData(), desc,
                PleaHelper.SEARCH_TYPE_PLEA_DESCRIPTION);

        pleaValue.setRefPleaId(rscbv.getId());
        pleaValue.setRefPleaCode(rscbv.getCode());
        pleaValue.setRefPleaDesc(rscbv.getDecode());
    }

    /**
     * Processes entry for Plea Code
     * 
     * @param obj
     * @param arraignmentDate
     */
    public void processRefIndictmentPleaCode(Object obj, Calendar arraignmentDate) {
        String code = ResultsHelper.checkNull((String) obj).toUpperCase();
        if (code.equals(emptyStr)) {
            processDeletedPlea();
            return;
        } else if (!pleaHelper.processGuiltyPlea(this, code)) {
            setAction(ResultsRowValue.RESULT_UNCHANGED);
            return;
        }
        setRefIndictmentPleaCode(code, arraignmentDate);
    }

    /**
     * Processes entry for Plea Description
     * 
     * @param obj
     * @param arraignmentDate
     */
    public void processRefIndictmentPleaDesc(Object obj, Calendar arraignmentDate) {
        String decode = ResultsHelper.checkNull((String) obj);
        if (decode.equals(emptyStr)) {
            processDeletedPlea();
            return;
        } else if (!pleaHelper.processGuiltyPlea(this, PleaHelper.getRefPleaCode(decode, PleaHelper.getPleaRefData()))) {
            setAction(ResultsRowValue.RESULT_UNCHANGED);
            return;
        }
        setRefIndictmentPleaDesc(decode, arraignmentDate);
    }

    /**
     * Processes entry for Plea Code
     * 
     * @param obj
     * @param arraignmentDate
     */
    public void processRefS41PleaCode(Object obj) {
        String code = ResultsHelper.checkNull((String) obj).toUpperCase();
        if (code.equals(emptyStr)) {
            processDeletedPlea();
            return;
        }
        setRefS41PleaCode(code);
    }

    /**
     * Processes entry for Plea Description
     * 
     * @param obj
     * @param arraignmentDate
     */
    public void processRefS41PleaDesc(Object obj) {
        String decode = ResultsHelper.checkNull((String) obj);
        if (decode.equals(emptyStr)) {
            processDeletedPlea();
            return;
        }
        setRefS41PleaDesc(decode);
    }

    /**
     * processBreachPlea
     * 
     * @param obj
     */
    public void processBreachPlea(Object obj) throws UserCancelException {
        String code = ResultsHelper.checkNull((String) obj);

        if (code.equals(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.true"))) {
            pleaValue.setBreachAdmitted(Boolean.TRUE);
        } else if (code.equals(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.false"))) {
            pleaValue.setBreachAdmitted(Boolean.FALSE);
        } else {
            // If current action is new then reset action to UNCHANGED else
            // set to DELETE
            if (getAction() == ResultsRowValue.RESULT_ADD) {
                pleaValue.setBreachAdmitted(null);
                pleaValue.setArraignmentDate(null);
                if (pleaValue.getDatePut() == null) {
                    pleaValue = null; // Needs to be null to ensure correct
                    // logic fires in
                    // setOperationalBreachPlea
                    setAction(ResultsRowValue.RESULT_UNCHANGED);
                }
            } else {
                try {
                    // Does this row have verdicts/disposals
                    pleaHelper.processRowDelete(this);
                    pleaValue.setBreachAdmitted(null);
                    pleaValue.setArraignmentDate(null);
                    if (pleaValue.getDatePut() == null) {
                        setAction(ResultsRowValue.RESULT_DELETE);
                    }
                } catch (UserCancelException uce) {
                    setAction(ResultsRowValue.RESULT_UNCHANGED);
                }
            }
        }
    } // end of processBreachPlea()

    /**
     * processBailActPlea
     * 
     * @param obj
     */
    public void processBailActPlea(Object obj) throws UserCancelException {
        String code = ResultsHelper.checkNull((String) obj);

        if (code.equals(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "bailAct.plea.admitted"))) {
            pleaValue.setBreachAdmitted(Boolean.TRUE);
        } else if (code.equals(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "bailAct.plea.notAdmitted"))) {
            pleaValue.setBreachAdmitted(Boolean.FALSE);
        } else {
            // If current action is new then reset action to UNCHANGED else
            // set to DELETE
            if (getAction() == ResultsRowValue.RESULT_ADD) {
                pleaValue.setBreachAdmitted(null);
                pleaValue.setArraignmentDate(null);
                if (pleaValue.getDatePut() == null) {
                    pleaValue = null; // Needs to be null to ensure correct
                    // logic fires in
                    // setOperationalBailActPlea
                    setAction(ResultsRowValue.RESULT_UNCHANGED);
                }
            } else {
                try {
                    // Does this row have verdicts/disposals
                    pleaHelper.processRowDelete(this);
                    pleaValue.setBreachAdmitted(null);
                    pleaValue.setArraignmentDate(null);
                    if (pleaValue.getDatePut() == null) {
                        setAction(ResultsRowValue.RESULT_DELETE);
                    }
                } catch (UserCancelException uce) {
                    setAction(ResultsRowValue.RESULT_UNCHANGED);
                }
            }
        }
    } // end of processBailActPlea()
    
    /**
     * Gets Plea Code
     * 
     * @return String
     */
    public String getPleaCode() {
        if (pleaValue != null) {
            return ResultsHelper.checkNull(pleaValue.getRefPleaCode());
        }
        return emptyStr;
    }

    /**
     * Gets Plea Description
     * 
     * @return String
     */
    public String getPleaDesc() {
        if (pleaValue != null) {
            return ResultsHelper.checkNull(pleaValue.getRefPleaDesc());
        }
        return emptyStr;
    }

    /**
     * Returns Arraignment Date if set.
     * 
     * @return Object
     */
    public Object getPleaArraignmentDate() {
        if (pleaValue != null) {
            if (pleaValue.getArraignmentDate() != null)
                return pleaValue.getArraignmentDate();
        }
        return emptyStr;
    }

    /**
     * Sets the arraignment Date on PleaValue if valid.
     * 
     * @param obj
     */
    public void setPleaArraignmentDate(Object obj) {
        Calendar gc = null;
        if (obj != null) {
            Date arraignmentDate = (Date) obj;
            gc = Calendar.getInstance();
            gc.setTime(arraignmentDate);
        }
        pleaValue.setArraignmentDate(gc);
    }

    /**
     * get breach DatePut
     * 
     * @return
     */
    public Object getBreachDatePut() {
        if (pleaValue != null) {
            return pleaValue.getDatePut();
        }
        return null;
    }

    /**
     * Sets the breach DatePut on PleaValue if valid.
     * 
     * @param obj
     */
    public void setBreachDatePut(Object obj) throws UserCancelException {
        if (obj == null) {
            pleaValue.setDatePut(null);
            if (pleaValue.getBreachAdmitted() == null) {
                if (getAction() == ResultsRowValue.RESULT_ADD) {
                    setAction(ResultsRowValue.RESULT_UNCHANGED);
                    pleaValue = null;
                } else {
                    setAction(ResultsRowValue.RESULT_DELETE);
                }
            }
            return;
        }

        if (obj instanceof Date) {
            pleaValue.setDatePut((Date) obj);
        }
    }
    
    /**
     * Sets the Bail Act DatePut on PleaValue if valid.  As BAO is modelled as Breach we 
     * can use existing functionality.
     * 
     * @param obj
     */
    public void setBailActDatePut(Object obj) throws UserCancelException {
        setBreachDatePut(obj);
    }

    /**
     * Set Arraignment Date on Plea if not null
     * 
     * @param arraignmentDate
     */
    private void checkPleaArraignmentDate(Calendar arraignmentDate) {
        if (pleaValue != null && pleaValue.getArraignmentDate() == null) {
            pleaValue.setArraignmentDate(arraignmentDate);
        }
    }

    /**
     * Reset Additional information on PleaValue depending on pleaCode
     */
    private void clearPleaAdditionalInfo() {
        String pleaCode = pleaValue.getRefPleaCode();
        if ((!pleaCode.equals("O")) && (!pleaCode.equals("GAO")) && (!pleaCode.equals("GLO"))) {
            pleaValue.setAltRefOffenceId(null);
            pleaValue.setAltRefOffenceCodeAndDesc(null, null);
            pleaValue.setOtherPleaText(null);
        }
    }

    /**
     * Determine whether additional information needs setting.
     * 
     * @param obj
     */
    /** @todo THIS NEEDS TO BE REFACTORED. REVIEW LOGIC */
    public void processPleaAdditionalInfo(Object obj) {
        String code = pleaValue.getRefPleaCode();
        if (code.equals("O")) {
            String otherText = (String) obj;
            pleaValue.setOtherPleaText(otherText);
            pleaValue.setAltRefOffenceId(null);
            pleaValue.setAltRefOffenceCodeAndDesc(null, null);
        } else {
            RefOffenceBasicValue refOffence = (RefOffenceBasicValue) obj;
            if (refOffence != null) {
                pleaValue.setAltRefOffenceId(refOffence.getId());
                pleaValue.setAltRefOffenceCodeAndDesc(refOffence.getOffenceCode(), refOffence.getOffenceDesc());
                pleaValue.setOtherPleaText(null);
            }
        }
    }

    /**
     * Returns appropriate additional info for on refPleaCode
     * 
     * @return
     */
    public AdditionalInfoTableCellComponent getPleaAdditionalInfo() {
        return AdditionalInfoTableCellComponent.getPleaComponent(((pleaValue != null) ? pleaValue.getRefPleaCode()
                : null), ((pleaValue != null) ? pleaValue.getAltRefOffenceDesc() : null),
                ((pleaValue != null) ? pleaValue.getOtherPleaText() : null), (pleaValue != null));
    }

    // ******************************************************************************
    // Verdict Table Model specific methods
    // ******************************************************************************

    /**
     * Gets Verdict Code
     * 
     * @return String
     */
    public String getVerdictCode() {
        if (verdictValue != null) {
            return ResultsHelper.checkNull(verdictValue.getRefVerdictCode());
        }
        return emptyStr;
    }

    /**
     * Gets Verdict Description
     * 
     * @return String
     */
    public String getVerdictDesc() {
        if (verdictValue != null) {
            return ResultsHelper.checkNull(verdictValue.getRefVerdictDesc());
        }
        return emptyStr;
    }

    /**
     * Jurors Assenting/Dissenting
     * 
     * @return AssentingDissenting
     */
    public AssentingDissenting getJurorsAssenting() {
        if (verdictValue != null) {
            return AssentingDissentingFactory.getInstance().getValue(verdictValue.getJurorsAssenting(),
                    verdictValue.getJurorsDissenting());
        }

        // return a null value by default...
        return AssentingDissentingFactory.getInstance().getNullValue();
    }

    /**
     * Verdict additional information
     * 
     * @return
     */
    public AdditionalInfoTableCellComponent getVerdictAdditionalInformation() {
        return AdditionalInfoTableCellComponent.getVerdictComponent(((verdictValue != null) ? verdictValue
                .getRefVerdictCode() : null), ((verdictValue != null) ? verdictValue.getAltRefOffenceDesc() : null),
                ((verdictValue != null) ? verdictValue.getOtherVerdictText() : null), (verdictValue != null));
    }

    /**
     * 
     * @return String
     */
    public Date getVerdictDate() {
        if (verdictValue != null) {
            return verdictValue.getVerdictDate();
        }
        return null;
    }

    /**
     * Creates/Updates an operation Verdict based on row status. Checks status
     * of pleaValue and sets appropriate Row status
     * 
     * @param arraignmentDate
     */
    public void setOperationalVerdict(Calendar verdictDate) {
        if (verdictValue == null) {
            if (getAction() == ResultsRowValue.RESULT_DELETE) {
                verdictValue = getDeleteVerdictValue();
                setAction(ResultsRowValue.RESULT_UPDATE);
            } else {
                // Adding new verdict
                setAction(ResultsRowValue.RESULT_ADD);
                verdictValue = new VerdictValue();
                setVerdictValue(verdictValue);
            }
        } else {
            // Check if row is being added but plea is changed before
            // saving.
            if (getAction() != ResultsRowValue.RESULT_ADD) {
                // Updating existing verdict
                setAction(ResultsRowValue.RESULT_UPDATE);
            }
        }
        verdictValue.setDefendantOnOffenceId(getDefendantOnOffenceId());
        verdictValue.setVerdictDate(verdictDate);
    }

    /**
     * Nulls the appropriate fields for the verdict code.
     * 
     * @param verdictValue
     *            the VerdictValue
     */
    private static void validateVerdict(VerdictValue verdictValue) {
        String verdictCode = verdictValue.getRefVerdictCode();

        if ((!VerdictHelper.hasOtherText(verdictCode)) && (!VerdictHelper.hasAlternateOffence(verdictCode))) {
            verdictValue.setAltRefOffenceId(null);
            verdictValue.setAltRefOffenceCodeAndDesc(null, null);
            verdictValue.setOtherVerdictText(null);
        }

        if (!VerdictHelper.hasJurorCounts(verdictCode) && !VerdictHelper.hasJurorCountsOptional(verdictCode)) {
            verdictValue.setJurorsAssenting(null);
            verdictValue.setJurorsDissenting(null);
        }
    }

    /**
     * Determines the row status and sets accordingly. calls processing for
     * cascade delete if necessary.
     */
    public void processDeletedVerdict() {
        // If current action is new then reset action to UNCHANGED else set to
        // DELETE
        if (getAction() == ResultsRowValue.RESULT_ADD) {
            setAction(ResultsRowValue.RESULT_UNCHANGED);
        } else {
            try {
                VerdictHelper.processRowDelete(this, xac);
                setAction(ResultsRowValue.RESULT_DELETE);
                setDeleteVerdictValue(verdictValue);
            } catch (UserCancelException uce) {
                setAction(ResultsRowValue.RESULT_UNCHANGED);
                return;
            }
        }
        setVerdictValue(null);
    }

    /**
     * Processes entry for Plea Code
     * 
     * @param obj
     * @param arraignmentDate
     */
    public void processVerdictCode(Object obj) {
        String code = ResultsHelper.checkNull((String) obj).toUpperCase();
        if (code.equals(emptyStr)) {
            processDeletedVerdict();
            return;
        }
        setVerdictCode(code);
    }

    /**
     * Sets reference attributes to VerdictValue searching by Veridct code
     * 
     * @param code
     * @throws CSRecoverableException
     */
    public void setVerdictCode(String code) {
        Integer id = null;
        // Verdict Code is already valid, just retrieving id
        id = VerdictHelper.getRefVerdictId(code, VerdictHelper.getVerdictRefData());

        verdictValue.setRefVerdictId(id);
        verdictValue.setRefVerdictCode(code);
        verdictValue.setRefVerdictDesc(VerdictHelper.getRefVerdictDescription(code, VerdictHelper.getVerdictRefData()));
        verdictValue.setRefVerdictType(VerdictHelper.getRefVerdictCodeType(code, VerdictHelper.getVerdictRefData()));
        validateVerdict(verdictValue);
    }

    /**
     * Process selection of Verdict description
     * 
     * @param obj
     * @return
     */
    public void processVerdictDesc(Object obj) {
        String decode = ResultsHelper.checkNull((String) obj);
        if (decode.equals(emptyStr)) {
            processDeletedVerdict();
            return;
        }

        setVerdictDesc(decode);
    }

    /*
     * Sets reference attributes to VerdictValue searching by Veridct code
     * @param code @param decode
     */
    public void setVerdictDesc(String decode) {
        RefSystemCodeBasicValue rscbv = VerdictHelper.getRefSystemCodeBasicValue(VerdictHelper.getVerdictRefData(),
                decode, VerdictHelper.SEARCH_TYPE_VERDICT_DESCRIPTION);
        if (rscbv != null) {
            verdictValue.setRefVerdictId(rscbv.getId());
            verdictValue.setRefVerdictCode(rscbv.getCode());
            verdictValue.setRefVerdictDesc(decode);
            verdictValue.setRefVerdictType(rscbv.getCodeType());
        }
        validateVerdict(verdictValue);
    }

    /**
     * Set verdict value with Jurrors Assenting and dessenting if any.
     * 
     * @param obj
     */
    public void setJurorsAssentingDessenting(Object obj) {
        AssentingDissenting assentingDissenting = (AssentingDissenting) obj;
        if (!assentingDissenting.isNull()) {
            verdictValue.setJurorsAssenting(new Integer(assentingDissenting.getAsscenting()));
            verdictValue.setJurorsDissenting(new Integer(assentingDissenting.getDissenting()));
        } else {
            verdictValue.setJurorsAssenting(null);
            verdictValue.setJurorsDissenting(null);
        }
    }

    /**
     * Determine whether additional information needs setting.
     * 
     * @param obj
     */
    public void processVerdictAdditionalInfo(Object obj) {
        String code = verdictValue.getRefVerdictCode();
        if (VerdictHelper.hasOtherText(code)) {
            String otherText = (String) obj;
            verdictValue.setOtherVerdictText(otherText);
            verdictValue.setAltRefOffenceId(null);
            verdictValue.setAltRefOffenceCodeAndDesc(null, null);
        } else {
            RefOffenceBasicValue refOffence = (RefOffenceBasicValue) obj;
            if (refOffence != null) {
                verdictValue.setAltRefOffenceId(refOffence.getId());
                verdictValue.setAltRefOffenceCodeAndDesc(refOffence.getOffenceCode(), refOffence.getOffenceDesc());
                verdictValue.setOtherVerdictText(null);
            }
        }
    }

    /**
     * 
     * @param obj
     */
    public void setVerdictDate(Object obj) {
        Calendar gc = null;
        if (obj == null)
            return;
        if (isSet(obj)) {
            Date verdictDate = (Date) obj;
            gc = Calendar.getInstance();
            gc.setTime(verdictDate);
        }
        verdictValue.setVerdictDate(gc);
    }

    /**
     * Determines whether verdict is required If the Plea Code is "G" ie Guilty,
     * a verdict is not required.
     * 
     * @return
     */
    public boolean isVerdictRequired() {
        if (pleaValue != null) {
            String pleaCode = pleaValue.getRefPleaCode();
            if (pleaCode != null) {
                if (pleaCode.equalsIgnoreCase(PLEA_CODE_GUILTY)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines whether Jurrors Assenting/Dissenting info for verdict should
     * be editable
     * 
     * @return
     */
    public boolean isJurorAssentingDissentingEditable() {
        if (verdictValue != null) {
            String code = verdictValue.getRefVerdictCode();
            if (code != null) {
                if (VerdictHelper.hasJurorCounts(code) || VerdictHelper.hasJurorCountsOptional(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check if plea code is guilty
     * 
     * @return
     */
    public boolean isGuiltyPlea() {
        return pleaValue != null
                && (pleaValue.isGuilty());
    }

    // ******************************************************************************

    public void setMagsRefAppResult() {
        rarbv = AppealResultsHelper.getRefAppResultBasicValue(this, AppealResultsHelper.getAppealGeneralMagsRefData());
    }

    // ******************************************************************************
    // Appeal Results Table Model specific methods
    // ******************************************************************************

    // getValueAt

    /**
     * get reference Appeal Result value
     */
    public void setRefAppResult() {
        rarbv = AppealResultsHelper.getRefAppResultBasicValue(this, AppealResultsHelper.getCrimOffenceRefData());
    }

    /**
     * Get Appeal againts type
     * 
     * @return
     */
    public String getAppealAgainst() {
        if (getDefendantOnOffenceValue().getAppealAgainstType() != null) {
            return ResourceBundleHelper.getResource(XhibitBundles.AppealResults, APPEAL_AGAINST_RESOURCE
                    + getDefendantOnOffenceValue().getAppealAgainstType());
        }
        
        return emptyStr;
    }

    /**
     * Use created RefAppResultBasicValue to get description.
     * 
     * @return
     */
    public String getAppealResultCode() {
        if (rarbv != null) {
            return rarbv.getCode();
        }
        return emptyStr;
    }

    /**
     * Use created RefAppResultBasicValue to get description.
     * 
     * @return the Appeal result description.
     */
    public String getAppealResultDescription() {
        if (rarbv != null) {
            return AppealResultsHelper.getAppealResultDescription(rarbv);
        }
        return emptyStr;
    }

    /**
     * Use RefAppResultBasicValue code to determine whether additional info is
     * required and return.
     */
    public AdditionalInfoTableCellComponent getAppealResultAdditionalInfo() {
        return AdditionalInfoTableCellComponent.getAppealResultComponent(((rarbv != null) ? rarbv.getCode() : null),
                ((verdictValue != null) ? verdictValue.getAppLesserOffence() : null), null,
                ((verdictValue != null) && (rarbv != null)));
    }

    // SetValueAt

    /**
     * Ensures an operation VerdictValue for an appeal result exists.
     */
    public void setOperationalAppealResult() {
        log.debug("setOperationalAppealResult - BEGIN");
        log.debug("setOperationalAppealResult - getAction() = " + getAction());

        log.debug("setOperationalAppealResult - verdictValue = " + verdictValue);
        if (verdictValue == null) {
            log.debug("setOperationalAppealResult - verdictValue == null");
            // Check if previous action was a Delete. If so we want to
            // restore
            // the previous Appeal Result.
            if (getAction() == ResultsRowValue.RESULT_DELETE && getDeleteVerdictValue() != null) {
                verdictValue = getDeleteVerdictValue();
                setVerdictValue(verdictValue);
                setAction(ResultsRowValue.RESULT_UPDATE);
                setDeleteVerdictValue(null);
            } else if (getAction() == ResultsRowValue.RESULT_DELETE && getDeleteVerdictValue() == null) {
                verdictValue = new VerdictValue();
                setVerdictValue(verdictValue);
                setAction(ResultsRowValue.RESULT_UPDATE);
                setDeleteVerdictValue(null);
            } else {
                log.debug("setOperationalAppealResult - Adding new Appeal Result");
                // Adding new Appeal Result
                setAction(ResultsRowValue.RESULT_ADD);
                verdictValue = new VerdictValue();
                setVerdictValue(verdictValue);
                setDeleteVerdictValue(null);
            }
        } else {
            // Check if row is being added but plea is changed before
            // saving.
            if (getAction() != ResultsRowValue.RESULT_ADD) {
                // Updating existing plea
                setAction(ResultsRowValue.RESULT_UPDATE);
                setDeleteVerdictValue(null);
            }
        }

        if (defendantOnOffenceId != null) {
            // Only set the defendantOnOffenceId if it exists as this also
            // sets
            // the defendant on charge or offence flag.
            verdictValue.setDefendantOnOffenceId(defendantOnOffenceId);
        }

        log.debug("setOperationalAppealResult - END");
    }

    /**
     * Removes result from Verdict value and reset status.
     */
    public void processDeletedAppealResult(boolean displayMessage) {
        log.debug("processDeletedAppealResult - BEGIN displayMessage = " + displayMessage);
        // If current action is new then reset action to UNCHANGED else set to
        // DELETE
        if (getAction() == ResultsRowValue.RESULT_ADD) {
            setAction(ResultsRowValue.RESULT_UNCHANGED);
        } else {
            try {
                if (displayMessage) {
                    // Does this row have disposals
                    AppealResultsHelper.processRowDelete(this, acm);
                }
                if (verdictValue != null) {
                    setAction(ResultsRowValue.RESULT_DELETE);
                    setDeleteVerdictValue(verdictValue);
                    verdictValue = null;
                }
            } catch (UserCancelException uce) {
                setAction(ResultsRowValue.RESULT_UNCHANGED);
                return;
            }
        }
        setVerdictValue(null);
    }

    /**
     * Sets appeal result to VerdictValue
     * 
     * @param rarbv
     */
    public void setRefAppealResult(RefAppResultBasicValue rarbv) {
        log.debug("setRefAppealResult - BEGIN");

        if (rarbv.getCode() != null) {
            log.debug("setRefAppealResult - in if");
            String code = rarbv.getCode();
            verdictValue.setRefAppealOffenceCode(code);
            verdictValue.setRefAppealOffenceDesc(AppealResultsHelper.getAppealResultDescription(rarbv));
            verdictValue.setRefAppResultId(rarbv.getRefAppResId());
            verdictValue.setVerdictDate(Calendar.getInstance());
        } else {
            log.debug("setRefAppealResult - in else");
            verdictValue.setRefAppealOffenceCode(null);
            verdictValue.setRefAppealOffenceDesc(null);
            verdictValue.setRefAppResultId(null);
            verdictValue.setVerdictDate((Date) null);
        }
        log.debug("setRefAppealResult - END");
    }

    public void linkResultToMagsGeneralDisposal(RefAppResultBasicValue rarbv) {
        log.debug("linkResultToMagsGeneralDisposal - BEGIN");

        if (verdictValue != null) {
            verdictValue.setDefendantOnCaseId(getDefendantOnCaseId());

            if (rarbv.getCode() != null) {
                // Add the disposalId of the Magistrates General Disposal to
                // this result
                verdictValue.setDisposal2Id(disposalValue.getDisposal2Id());
            }
        }
        log.debug("linkResultToMagsGeneralDisposal - END");
    }

    /**
     * Sets or removes appeal result depending on object selection
     * 
     * @param obj
     */
    public void processAppealResult(final RefAppResultBasicValue rarbv, final boolean showMessage) {
        log.debug("processAppealResult - BEGIN");

        clearAlternateOffence(verdictValue, rarbv);

        String code = ResultsHelper.checkNull(rarbv.getCode());
        log.debug("processAppealResult - code = [" + code + "]");
        if (code.equals(emptyStr)) {
            processDeletedAppealResult(true);
            return;
        }
        if (getAction() == RESULT_UPDATE) {
            log.debug("processAppealResult - getAction() == RESULT_UPDATE");
            // If the updated appeal result does not allow for variation
            // disposals, check if there are variation disposals and propmt
            // the
            // user to delete them.
            if (showMessage && !AppealResultsHelper.isAppealResultVariable(code)) {
                try {
                    AppealResultsHelper.processDeleteAppealResultVariationDisposals(this, acm);
                } catch (UserCancelException uce) {
                    setAction(RESULT_UNCHANGED);
                    return;
                }
            }
        }
        setRefAppealResult(rarbv);

        log.debug("processAppealResult - END");
    }

    /**
     * Clears the alternate offence details on the given verdict details if the
     * current appeal result is different to the given newly retrieved reference
     * appeal result.
     * 
     * @param verdictDetailValue
     *            the verdict details
     * @param rarbv
     *            the reference appeal result
     */
    private static void clearAlternateOffence(VerdictValue verdictValue, RefAppResultBasicValue rarbv) {
        if (verdictValue.getRefAppResultId() == null
                || !verdictValue.getRefAppResultId().equals(rarbv.getRefAppResId())) {
            verdictValue.setAppLesserOffence(null);
        }
    }

    /**
     * sets additional info if it exists
     * 
     * @param obj
     */
    public void processAppealAdditionalInfo(Object obj) {
        if (obj != null) {
            verdictValue.setAppLesserOffence((String) obj);
        }
    }

    // ******************************************************************************

    // *******************************************************************************
    // Disposals getValueAt

    /**
     * returns Charge sequence number concatenated with Breach description if
     * charge is of type breach
     * 
     * @param rrv
     * @return
     */
    public String getDisposalChargeText() {
        StringBuffer text = new StringBuffer();
        text.append(getChargeSequenceNumber().toString() == null ? emptyStr : getChargeSequenceNumber().toString());
        if (isBreach()) {
            text.append(": ");
            text.append(getChargeValue().getBreachValue().getHoDescription());
        }
        return text.toString();
    }

    /**
     * get Offence description consisting on Offence Sequence number and
     * description.
     * 
     * @return String
     */
    public String getDisposalOffenceText() {
        StringBuffer text = new StringBuffer();
        Integer offenceSequenceNr = getOffenceSequenceNumber();

        if (getOffenceValue() != null) {
            String offenceDescription = getOffenceValue().getOffenceDescription();
            text.append(offenceSequenceNr == null ? "" : offenceSequenceNr.toString());
            if (text.length() > 0) {
                text.append(": ");
            }
            text.append(offenceDescription == null ? "" : offenceDescription);
        }
        return text.toString();
    }

    /**
     * get Offence description consisting on Charge Sequence number and
     * description for Bail Act Offences.
     * 
     * @return String
     */
    public String getBailActDisposalOffenceText() {
        StringBuffer text = new StringBuffer();
        Integer chargeSequenceNr = getChargeSequenceNumber();

        if (getOffenceValue() != null) {
            String offenceDescription = getOffenceValue().getOffenceDescription();
            text.append(chargeSequenceNr == null ? "" : chargeSequenceNr.toString());
            if (text.length() > 0) {
                text.append(": ");
            }
            text.append(offenceDescription == null ? "" : offenceDescription);
        }
        return text.toString();
    }

    /**
     * 
     * @return String
     */
    public String getDisposalAppealAgainst() {
        //if (getDefendantOnOffenceValue().getAppealAgainstType() != null) {
        //    return ResourceBundleHelper.getResource(XhibitBundles.Disposals, APPEAL_AGAINST_RESOURCE
        //            + getDefendantOnOffenceValue().getAppealAgainstType());
        //}

    	// ctx-503
    	if(getOffenceValue().getAppealType() != null) {
    		return ResourceBundleHelper.getResource(XhibitBundles.Disposals, APPEAL_AGAINST_RESOURCE 
    				+ getOffenceValue().getAppealType());
    	}
    	
        return emptyStr;
    }

    /**
     * Returns disposal information with prefix pf DELETE if status is delete.
     * 
     * @return String
     */
    public String getDisposalDesc() {
        if (getDisposalValue() != null) {
            String shortDesc = getDisposalReferenceValue().getDisposalText(getDisposalValue());

            if (shortDesc == null) {
                return emptyStr;
            }

            return getAction() == ResultsRowValue.RESULT_DELETE
                    || (getAction() == ResultsRowValue.RESULT_UNCHANGED && getPreDeleteAction() == ResultsRowValue.RESULT_ADD) ? ResultsRowValue.DELETED
                    + shortDesc
                    : shortDesc;
        }

        // return an empty string if nothing selected...
        return emptyStr;
    }

    // *******************************************************************************
    /**
     * The PleaSaveValue for the Plea plus CourtLog information for Charge. This
     * is created from exisiting information held on the ResultsRowValue
     * 
     * @return value PleaSaveValue
     */
    public PleaSaveValue getPleaSaveValue(String operation) {
        if (offenceValue == null) {
            return getPleaSaveValueForCharge(operation);
        }

        return getPleaSaveValueForOffence(operation);
    }

    /**
     * The PleaSaveValue for the Plea plus CourtLog information for Charge. This
     * is created from exisiting information held on the ResultsRowValue
     * 
     * @return value PleaSaveValue
     */
    private PleaSaveValue getPleaSaveValueForCharge(String operation) {
        // create new instance of PleaSaveValue if null. Use info set on
        // ResultsRowValue
        if (pleaSaveValue == null) {
            PleaValue operationPleaValue = getOperationalPleaValue();

            pleaSaveValue = new PleaSaveValue(operationPleaValue, operation, caseId, caseNumber, caseType,
                    scheduledHearingId, defendantValue.getCrestDefendantID(), chargeValue.getChargeType(), chargeValue
                            .getCrestChargeID(), chargeValue.getCrestChargeSeqNo(), defendantOnCaseId,
                    ResultsRowValueHelper.getDefendantName(defendantValue, scheduledHearingValue), XhibitSingleton
                            .getInstance().isUserInCourtroom(), new Date());
        }

        return pleaSaveValue;
    }

    /**
     * The PleaSaveValue for the Plea plus CourtLog information for Offence.
     * This is created from exisiting information held on the ResultsRowValue
     * 
     * @return value PleaSaveValue
     */
    private PleaSaveValue getPleaSaveValueForOffence(String operation) {
        // create new instance of PleaSaveValue if null. Use info set on
        // ResultsRowValue
        if (pleaSaveValue == null) {
            PleaValue operationPleaValue = getOperationalPleaValue();
            
            pleaSaveValue = new PleaSaveValue(operationPleaValue, operation, caseId, caseNumber, caseType,
                    scheduledHearingId, offenceValue.getCrestOffenceID(), defendantValue.getCrestDefendantID(),
                    offenceValue.getCrestOffenceSeqNo(), chargeValue.getChargeType(), chargeValue.getCrestChargeID(),
                    chargeValue.getCrestChargeSeqNo(), defendantOnCaseId, ResultsRowValueHelper.getDefendantName(
                            defendantValue, scheduledHearingValue), offenceValue.getOffenceDescription(),
                    XhibitSingleton.getInstance().isUserInCourtroom(), new Date());
        }

        return pleaSaveValue;
    }

    /**
     * The verdict for the defendant Null if verdict not requested
     * 
     * @return value object
     */
    public VerdictValue getVerdictValue() {
        return verdictValue;
    }

    public void setVerdictValue(VerdictValue verdictValue) {
        this.verdictValue = verdictValue;
    }

    /**
     * The verdict for the defendant to be deleted Null if plea not requested
     * 
     * @return value object
     */
    public VerdictValue getDeleteVerdictValue() {
        return deleteVerdictValue;
    }

    public void setDeleteVerdictValue(VerdictValue deleteVerdictValue) {
        this.deleteVerdictValue = deleteVerdictValue;
    }

    /**
     * The VerdictSaveValue for the verdict plus CourtLog information for
     * Charge. This is created from exisiting information held on the
     * ResultsRowValue
     * 
     * @return the VerdictSaveValue
     */
    public VerdictSaveValue getVerdictSaveValue(String operation) {
        log.debug("getVerdictSaveValue - BEGIN; \n" + "offenceValue = " + offenceValue + "\n" + "operation    = "
                + operation + "\n");

        VerdictSaveValue vsv = null;

        VerdictValue operationVerdictValue = getOperationVerdictValue();

        if (offenceValue != null) {
            vsv = getVerdictSaveValueForOffence(operation);
        } else if (operationVerdictValue.getDefendantOnCaseId() != null
                && operationVerdictValue.getRefAppResultId() != null) {
            vsv = getVerdictSaveValueForDisposal(operation);
        } else if (operationVerdictValue.getRefVerdictId() != null) {
            vsv = getVerdictSaveValueForCase(operation);
        } else {
            log.error("getVerdictSaveValue - UNKNOWN verdict type!");
        }
        return vsv;
    }

    private PleaValue getOperationalPleaValue() {
    	PleaValue operationPleaValue;
    	if (pleaValue == null) {
            // plea deleted
            operationPleaValue = deletePleaValue;
        } else {
            // add or updated
            operationPleaValue = pleaValue;
        }	
    	
    	// Ensure the original verdictId is set for update to DARTS
        if (getVerdictValue() != null && operationPleaValue != null && operationPleaValue.getOriginalRefPleaId() == null) {
        	operationPleaValue.setOriginalRefPleaId(getVerdictValue().getRefVerdictId());
        }

    	return operationPleaValue;
    }
    
    private VerdictValue getOperationVerdictValue() {
    	VerdictValue operationVerdictValue;
        if (verdictValue == null) {
            // verdict deleted
            operationVerdictValue = deleteVerdictValue;
        } else {
            // add or updated
            operationVerdictValue = verdictValue;
        }
        
        // Ensure the original pleaId is set for update to DARTS
        if (getPleaValue() != null && operationVerdictValue != null && operationVerdictValue.getOriginalRefPleaId() == null) {
        	operationVerdictValue.setOriginalRefPleaId(getPleaValue().getRefPleaId());
        }
        
        return operationVerdictValue;
    }
    
    /**
     * The VerdictSaveValue for the verdict plus CourtLog information for
     * Charge. This is created from exisiting information held on the
     * ResultsRowValue
     * 
     * @return the VerdictSaveValue
     */
    private VerdictSaveValue getVerdictSaveValueForCase(String operation) {
        log.debug("getVerdictSaveValueForCase - BEGIN");
        if (verdictSaveValue == null) {
            VerdictValue operationVerdictValue = getOperationVerdictValue();
            
            verdictSaveValue = new VerdictSaveValue(operationVerdictValue, operation, caseId, caseNumber, caseType,
                    scheduledHearingId, caseSubType, defendantOnCaseId, ResultsRowValueHelper.getDefendantName(
                            defendantValue, scheduledHearingValue), XhibitSingleton.getInstance().isUserInCourtroom(),
                    new Date());
        }

        log.debug("getVerdictSaveValueForCase - ABOUT TO RETURN");
        return verdictSaveValue;
    }

    /**
     * The VerdictSaveValue for the verdict plus CourtLog information for
     * Offence. This is created from exisiting information held on the
     * ResultsRowValue
     * 
     * @return the VerdictSaveValue
     */
    private VerdictSaveValue getVerdictSaveValueForOffence(String operation) {
        if (verdictSaveValue == null) {
            VerdictValue operationVerdictValue = getOperationVerdictValue();

            verdictSaveValue = new VerdictSaveValue(operationVerdictValue, operation, caseId, caseNumber, caseType,
                    scheduledHearingId, caseSubType, offenceValue.getCrestOffenceID(), defendantValue
                            .getCrestDefendantID(), offenceValue.getCrestOffenceSeqNo(), chargeValue.getChargeType(),
                    chargeValue.getCrestChargeID(), chargeValue.getCrestChargeSeqNo(), defendantOnCaseId,
                    ResultsRowValueHelper.getDefendantName(defendantValue, scheduledHearingValue), offenceValue
                            .getOffenceDescription(), XhibitSingleton.getInstance().isUserInCourtroom(), new Date());
        }

        return verdictSaveValue;
    }

    private VerdictSaveValue getVerdictSaveValueForDisposal(String operation) {
        log.debug("getVerdictSaveValueForDisposal - BEGIN");
        if (verdictSaveValue == null) {
            VerdictValue operationVerdictValue = getOperationVerdictValue();
            
            verdictSaveValue = new VerdictSaveValue(operationVerdictValue, operation, caseId, caseNumber, caseType,
                    scheduledHearingId, caseSubType, defendantOnCaseId, ResultsRowValueHelper.getDefendantName(
                            defendantValue, scheduledHearingValue), XhibitSingleton.getInstance().isUserInCourtroom(),
                    new Date());
        }

        log.debug("getVerdictSaveValueForDisposal - ABOUT TO RETURN");
        return verdictSaveValue;
    }

    public DisposalSaveValue getDisposalSaveValue(String operation) {
        DisposalSaveValue dsv = null;
        if (offenceValue != null) {
            dsv = getDisposalSaveValueForOffence(operation);
        } else if (disposalValue.isMagistrateGeneralDisposal()) {
            dsv = getDisposalSaveValueForResult(operation);
        } else if (chargeValue == null) {
            dsv = getDisposalSaveValueForCase(operation);
        } else {
            log.error("getDisposalSaveValue - UNKNOWN disposal type!");
        }
        return dsv;
    }

    /**
     * The DisposalSaveValue for the Disposal plus CourtLog information for
     * Case. This is created from exisiting information held on the
     * ResultsRowValue
     * 
     * @return the DisposalSaveValue
     */
    private DisposalSaveValue getDisposalSaveValueForCase(String operation) {
        log.debug("getDisposalSaveValueForCase - BEGIN");
        if (disposalSaveValue == null) {
            disposalSaveValue = new DisposalSaveValue(disposalValue, disposalReferenceValue, operation, caseId,
                    caseNumber, caseType, caseSubType, defendantValue.getCrestDefendantID(), defendantOnCaseId,
                    ResultsRowValueHelper.getDefendantName(defendantValue, scheduledHearingValue), XhibitSingleton
                            .getInstance().isUserInCourtroom(), new Date(), scheduledHearingId, psdDisId);
        }

        return disposalSaveValue;
    }

    /**
     * Gets the DisposalSaveValue for a Magistrates Court General Disposal.
     * 
     * @param operation
     * @return DisposalSaveValue
     */
    private DisposalSaveValue getDisposalSaveValueForResult(String operation) {
        log.debug("getDisposalSaveValueForResult - BEGIN");
        return getDisposalSaveValueForCase(operation);
    }

    /**
     * The DisposalSaveValue for the Disposal plus CourtLog information for
     * Offence. This is created from exisiting information held on the
     * ResultsRowValue
     * 
     * @return value PleaSaveValue
     */
    private DisposalSaveValue getDisposalSaveValueForOffence(String operation) {
        if (disposalSaveValue == null) {
            disposalSaveValue = new DisposalSaveValue(
                    disposalValue,
                    disposalReferenceValue, // Need to implement
                    operation, caseId, caseNumber, caseType, caseSubType, offenceValue.getCrestOffenceID(),
                    defendantValue.getCrestDefendantID(), offenceValue.getCrestOffenceSeqNo(), chargeValue
                            .getCrestChargeID(), chargeValue.getCrestChargeSeqNo(), chargeValue.getChargeType(),
                    defendantOnCaseId, ResultsRowValueHelper.getDefendantName(defendantValue, scheduledHearingValue),
                    XhibitSingleton.getInstance().isUserInCourtroom(), new Date(), scheduledHearingId, psdDisId);
        }

        return disposalSaveValue;
    }

    /**
     * Charge Type
     * 
     * @return Charge Type
     */
    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
        if(chargeType.equals(ChargeTypes.BREACH.getChargeType()) 
                ||
                chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType()) ) {
            breach = true;
        }
        
    }

    public boolean isBreach() {
        return breach;
    }

    /**
     * Defendant On Offence
     * 
     * @return value object
     */
    public DefendantOnOffenceComplexValue getDefendantOnOffenceValue() {
        return defendantOnOffenceValue;
    }

    /**
     * 
     * @param defendantOnOffenceValue
     */
    public void setDefendantOnOffenceValue(DefendantOnOffenceComplexValue defendantOnOffenceValue) {
        this.defendantOnOffenceValue = defendantOnOffenceValue;
    }

    /**
     * 
     * @return
     */
    public Integer getCaseId() {
        return caseId;
    }

    /**
     * 
     * @return
     */
    public Integer getCaseNumber() {
        return caseNumber;
    }

    /**
     * 
     * @return
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * 
     * @return
     */
    public String getCaseSubType() {
        return caseSubType;
    }

    /**
     * Clone the object, as we know this class implements <code>Cloneable</code>,
     * the <code>CloneNotSupportException</code> is handled internally.
     * 
     * @return A clone of this object
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns breach offences text
     * 
     * @return String
     */
    public String getBreachOffencesText() {
        return breachOffencesText;
    }

    /**
     * Appends breach offences text.
     * 
     * @param String
     */
    public void addBreachOffenceText(String newBreachOffenceText) {
        breachOffencesText = breachOffencesText + newBreachOffenceText + "\n";
    }

    /**
     * Returns Breach Plea in text form using locale
     * 
     * @return String
     */
    public String getBreachPleaText() {
        if (isBreachPleaAdmitted()) {
            if (pleaValue.isBreachAdmitted()) {
                return ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.true");
            }

            return ResourceBundleHelper.getResource(XhibitBundles.Pleas, "breach.plea.false");
        }

        return emptyStr;
    }

    /**
     * Is BreachAdmitted set?
     * 
     * @return boolean
     */
    public boolean isBreachPleaAdmitted() {
        if (pleaValue != null) {
            return pleaValue.getBreachAdmitted() != null;
        }
        return false;
    }

    /**
     * Returns number of disposals set for row.
     * 
     * @return
     */
    public int getDisposalCount() {
        return disposalCount;
    }

    /**
     * Sets number of disposals for row.
     * 
     * @param disposalCount
     */
    public void setDisposalCount(int disposalCount) {
        this.disposalCount = disposalCount;
    }

    /**
     * Returns number of variation disposals set for row.
     * 
     * @return
     */
    public int getVariationDisposalCount() {
        return variationDisposalCount;
    }

    /**
     * Sets number of variation disposals for row.
     * 
     * @param disposalCount
     */
    public void setVariationDisposalCount(int variationDisposalCount) {
        this.variationDisposalCount = variationDisposalCount;
    }
}