package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderIndictmentValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2 - Joinder Indictment Model
 * </p>
 * <p>
 * Description: This class holds all the necessary data to be submitted to the
 * server for merging/appending joinder indictments.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Joseph Antoniou
 * @version 1.0
 */

public class JoinderIndictmentModel extends Object implements Serializable {
    /** The selected case number. */
    private Integer selectedCaseNumber = null;

    /** The selected case type. */
    private String selectedCaseType = null;

    /** The selected case type and number. */
    private String selectedCaseTypeNumber = null;

    /** The number of the case to which the join operation is performed on. */
    private Integer joinderCaseNumber = null;

    /** The charges from the selected case. */
    private ChargeCompositeValue chargeCompValue = null;

    /** The indictment selected in the wizard. */
    private ChargeValue selectedIndictment = null;

    private JoinderIndictmentValue joinderIndictmentValue = null;

    private String joinderCaseType = null;

    private Integer joinderCourtId = null;

    private Integer joinderCaseId = null;

    private Integer selectedCaseId = null;

    private DefendantMap defendantMap = new DefendantMap();

    public int offenceSeqNoCounter = 0;

    private transient ArrayList wizardPanels = null;

    private ArrayList joinedCaseIds = null;

    private Collection joinedChargeIds = null;

    /**
     * Stores the case basic value for each case that has an indictment joined.
     * Must be stored in the order that they are joined as this is used to
     * populate the Crest Indictment Log.
     */
    private List<XhbCaseBasicValue> caseBasicValues = null;

    /**
     * Stores the ChargeCompositeValue for each case that has an indictment
     * joined. Key is the case number (String) e.g. T20028811
     */
    private HashMap caseCharges = null;

    private String judgeName = null;

    private Integer refJudgeId = null;

    /**
     * The indictment to which the join operation will be performed on. It is
     * this instance that will stored in the database.
     */
    private ChargeValue joinderIndictment = null;

    /**
     * Contains arrays String[2] where position 0 is the case type & number and
     * position 1 is the indictments crest offence sequence number
     */
    private Collection casesAndIndictmentNos = null;

    /**
     * Default Constructor.
     */
    public JoinderIndictmentModel() {
        super();
        this.joinedCaseIds = new ArrayList();
        this.joinedChargeIds = new HashSet();
        this.caseBasicValues = new ArrayList<XhbCaseBasicValue>();
        this.caseCharges = new HashMap();
        this.casesAndIndictmentNos = new ArrayList();
        this.judgeName = "";
    }

    /**
     * Set the selected case number in the wizard.
     */
    public void setSelectedCaseNumber(Integer caseNumber) {
        this.selectedCaseNumber = caseNumber;
    }

    /**
     * @return the number of the selected case in the wizard.
     */
    public Integer getSelectedCaseNumber() {
        return this.selectedCaseNumber;
    }

    public void setSelectedCaseType(String type) {
        this.selectedCaseType = type;
    }

    public String getSelectedCaseType() {
        return this.selectedCaseType;
    }

    public void setSelectedCaseTypeNumber(String caseTypeNumber) {
        this.selectedCaseTypeNumber = caseTypeNumber;
    }

    public String getSelectedCaseTypeNumber() {
        return selectedCaseTypeNumber;
    }

    /**
     * Set the joinder case number in the wizard.
     */
    public void setJoinderCaseNumber(Integer caseNumber) {
        this.joinderCaseNumber = caseNumber;
    }

    /**
     * @return the number of the selected case in the wizard.
     */
    public Integer getJoinderCaseNumber() {
        return this.joinderCaseNumber;
    }

    /**
     * Set the charges of the selected case.
     */
    public void setSelectedChargeCompValue(ChargeCompositeValue value) {
        this.chargeCompValue = value;
    }

    /**
     * @return the charge composite value related to the selected case.
     */
    public ChargeCompositeValue getSelectedChargeCompValue() {
        return this.chargeCompValue;
    }

    /**
     * Sets the selected indictment the user selected. Will also sort all of its
     * offences.
     *
     * @param selectedIndictment
     *            user selected indictment
     */
    public void setSelectedIndictment(ChargeValue selectedIndictment) {
        this.selectedIndictment = selectedIndictment;
        this.sortOffences(this.selectedIndictment.getOffenceValues());
    }

    /**
     * @return the selected indictment in the wizard.
     */
    public ChargeValue getSelectedIndictment() {
        return this.selectedIndictment;
    }

    public void setJoinderCourtId(Integer courtId) {
        this.joinderCourtId = courtId;
    }

    public Integer getJoinderCourtId() {
        return this.joinderCourtId;
    }

    public void setJoinderCaseType(String type) {
        this.joinderCaseType = type;
    }

    public String getJoinderCaseType() {
        return this.joinderCaseType;
    }

    public Integer getSelectedCaseId() {
        return this.selectedCaseId;
    }

    public void setSelectedCaseId(Integer id) {
        this.selectedCaseId = id;
    }

    public Integer getJoinderCaseId() {
        return this.joinderCaseId;
    }

    public void setJoinderCaseId(Integer id) {
        this.joinderCaseId = id;
    }

    public Collection getCasesAndIndictmentNos() {
        return casesAndIndictmentNos;
    }

    public JoinderIndictmentValue createJoinderIndictmentValue(ChargeValue chargeValue) {
        // First create the corersponding charge value for joinder indictments.
        JoinderIndictmentValue indictmentValue = new JoinderIndictmentValue();
        chargeValue.setCourtID(XhibitSingleton.getInstance().getCourtId());
        indictmentValue.setNewChargeValue(chargeValue);

        // Sort the offence values.
        this.sortOffences(chargeValue.getOffenceValues());

        // Change the offence values to JoinderOffenceValue references.
        ArrayList joinderOffenceValues = new ArrayList();
        Iterator offenceValues = indictmentValue.getNewChargeValue().getOffenceValues().iterator();
        OffenceValue offenceValue = null;
        JoinderOffenceValue newOffenceValue = null;
        while (offenceValues.hasNext()) {
            offenceValue = (OffenceValue) offenceValues.next();
            newOffenceValue = new JoinderOffenceValue(offenceValue.getOffenceID(), offenceValue.getChargeID(),
                    offenceValue.getRefOffenceID(), offenceValue.getDefendantIDs(), offenceValue
                            .getCrestOffenceFreeText(), offenceValue.getCrestOffenceID(), new Integer(
                            ++this.offenceSeqNoCounter), // crest offence
                    // seqno
                    offenceValue.getMultiple(), offenceValue.getOffenceDescription());

            newOffenceValue.setRefSystemCodeID(offenceValue.getRefSystemCodeID());
            newOffenceValue.setCourtID(offenceValue.getCourtID());
            newOffenceValue.setCaseID(offenceValue.getCaseID());
            newOffenceValue.setOffenceCode(offenceValue.getOffenceCode());
            newOffenceValue.setActSection(offenceValue.getActSection());
            newOffenceValue.setStatute(offenceValue.getStatute());
            newOffenceValue.setPlea(offenceValue.getPlea());
            newOffenceValue.setCrestHOClass(offenceValue.getCrestHOClass());
            newOffenceValue.setCrestHOSubclass(offenceValue.getCrestHOSubclass());
            
            newOffenceValue.setForceLocationCode(offenceValue.getForceLocationCode());
            newOffenceValue.setAddressId(offenceValue.getAddressId());
            newOffenceValue.setAddressValue(offenceValue.getAddressValue());
            newOffenceValue.setOffenceStartDateTime(offenceValue.getOffenceStartDateTime());
            newOffenceValue.setOffenceEndDateTime(offenceValue.getOffenceEndDateTime());

            newOffenceValue.setDefendantValues(offenceValue.getDefendantValues());
            Vector originalCaseOffences = newOffenceValue.getOriginalCaseOffences();

            // Needed, as it is not set at constructor time of
            // JoinderOffenceValue
            if (originalCaseOffences == null) {
                originalCaseOffences = new Vector();
                newOffenceValue.setOriginalCaseOffences(originalCaseOffences);
            }
            originalCaseOffences.add(new Integer[] { this.getJoinderCaseId(), offenceValue.getOffenceID() });
            joinderOffenceValues.add(newOffenceValue);
        }

        // Rest of the offence setters.
        newOffenceValue.setRefOffenceID(offenceValue.getRefOffenceID());

        indictmentValue.getNewChargeValue().setOffenceValues(joinderOffenceValues);

        indictmentValue.setRefJudgeId(getRefJudgeId());

        return indictmentValue;
    }

    public void setJoinderIndictmentValue(JoinderIndictmentValue indictmentValue) {
        this.joinderIndictmentValue = indictmentValue;
    }

    public JoinderIndictmentValue getJoinderIndictmentValue() {
        return this.joinderIndictmentValue;
    }

    /**
     * Will retrieve a unique list of defendants specific to the Joinder
     * Indictment.
     *
     * @return unique collection of defendants.
     */
    public Collection getJoinderDefendants() {
        ArrayList defendants = new ArrayList();
        Iterator offenceValues = this.joinderIndictmentValue.getNewChargeValue().getOffenceValues().iterator();
        Iterator defendantValues = null;
        OffenceValue offenceValue = null;
        DefendantValue defendantValue = null;
        while (offenceValues != null && offenceValues.hasNext()) {
            offenceValue = (OffenceValue) offenceValues.next();
            if ((offenceValue == null) || (offenceValue.getDefendantValues() == null)) {
                continue;
            }
            defendantValues = offenceValue.getDefendantValues().iterator();
            while (defendantValues.hasNext()) {
                defendantValue = (DefendantValue) defendantValues.next();
                if (isDefendantIdUnique(defendantValue.getDefendantID(), defendants)) {
                    defendants.add(defendantValue);
                }
            }
        }
        return defendants;
    }

    /**
     * Will retrieve a unique list of defendants specific to the Selected
     * Indictment.
     *
     * @return the unique collection defendants.
     */
    public Collection getSelectedDefendantsFromIndictment() {
        ArrayList defendants = new ArrayList();
        Iterator offenceValues = this.selectedIndictment.getOffenceValues().iterator();
        Iterator defendantValues = null;
        OffenceValue offenceValue = null;
        DefendantValue defendantValue = null;
        while (offenceValues != null && offenceValues.hasNext()) {
            offenceValue = (OffenceValue) offenceValues.next();
            defendantValues = offenceValue.getDefendantValues().iterator();
            while (defendantValues.hasNext()) {
                defendantValue = (DefendantValue) defendantValues.next();
                if (isDefendantIdUnique(defendantValue.getDefendantID(), defendants)) {
                    defendants.add(defendantValue);
                }
            }
        }
        return defendants;
    }

    private boolean isDefendantIdUnique(Integer defId, Collection defendantValues) {
        Iterator defendantValueIterator = defendantValues.iterator();
        DefendantValue defendantValue = null;
        while (defendantValueIterator.hasNext()) {
            defendantValue = (DefendantValue) defendantValueIterator.next();
            if (defendantValue.getDefendantID().equals(defId)) {
                return false;
            }
        }
        return true;
    }

    public void setWizardPanels(ArrayList wizardPanels) {
        this.wizardPanels = wizardPanels;
    }

    /**
     * This method will reset the joinder indictment panels in the wizard, and
     * clear the selected references of this model.
     */
    public void reset() {
        if (this.wizardPanels != null) {
            JoinderIndictmentPanel wizardPanel = null;
            for (int i = 0; i < this.wizardPanels.size(); i++) {
                wizardPanel = (JoinderIndictmentPanel) this.wizardPanels.get(i);
                wizardPanel.reset();
            }
        }
        // Now reset the components of this model.
        this.selectedCaseId = null;
        this.selectedCaseNumber = null;
        this.selectedIndictment = null;
        this.setSelectedChargeCompValue(null);
        // this.defendantMap.reset();
    }

    public DefendantMap getDefendantMap() {
        return this.defendantMap;
    }

    public void sortOffences(Collection offenceValues) {
        if (offenceValues instanceof List) {
            Sorter.sort((List) offenceValues, new String[] { "crestOffenceSeqNo" });
        }
    }

    public void addCaseId(Integer caseId) {
        this.joinedCaseIds.add(caseId);
    }

    public Integer[] getJoinderCaseIds() {
        Integer[] caseIds = new Integer[this.joinedCaseIds.size()];
        joinedCaseIds.toArray(caseIds);
        return caseIds;
    }

    /**
     * Add the given charge id to the list of charge ids that have been joined
     * in the current wizard process.
     *
     * @param chargeId
     */
    public void addChargeId(Integer chargeId) {
        this.joinedChargeIds.add(chargeId);
    }

    /**
     * Get the charge ids that have been joined in the current wizard process.
     *
     * @return an array of charge ids that have been joined.
     */
    public Integer[] getJoinderChargeIds() {
        Integer[] chargeIds = new Integer[this.joinedChargeIds.size()];
        joinedChargeIds.toArray(chargeIds);
        return chargeIds;
    }

    /**
     * Check if the given charge id has already been joined in the current
     * wizard process.
     *
     * @param chargeId
     *            the charge id
     * @return true if the given charge has already been joined in the current
     *         wizard process.
     */
    public boolean isChargeJoined(Integer chargeId) {
        return joinedChargeIds.contains(chargeId);
    }

    public void addJoinderCase(XhbCaseBasicValue caseValue) {
        boolean caseAlreadyExists = false;
        Iterator i = caseBasicValues.iterator();
        while (i.hasNext()) {
            XhbCaseBasicValue caseBasicValue = (XhbCaseBasicValue) i.next();
            if (caseBasicValue.getCaseId().equals(caseValue.getCaseId())) {
                caseAlreadyExists = true;
                break;
            }
        }

        if (!caseAlreadyExists) {
            caseBasicValues.add(caseValue);
        }
    }

    public Collection<XhbCaseBasicValue> getJoinderCases() {
        return this.caseBasicValues;
    }

    /**
     * Adds the given ChargeCompositeValue to the caseCharges map using the
     * given case number as the key.
     *
     * @param caseNumber
     *            the case number e.g. T20028811
     * @param ccv
     *            the ChargeCompositeValue
     */
    public void addChargeCompositeValue(String caseTypeNumber, ChargeCompositeValue ccv) {
        caseCharges.put(caseTypeNumber, ccv);
    }

    /**
     * Gets the ChargeCompositeValue for the given case.
     *
     * @param caseNumber
     *            the case number e.g. T20028811
     * @return the ChargeCompositeValue for the case if it exists, otherwise
     *         null
     */
    public ChargeCompositeValue getChargeCompositeValue(String caseTypeNumber) {
        return (ChargeCompositeValue) caseCharges.get(caseTypeNumber);
    }

    /**
     * Checks if the given case number e.g. T20028811 has been joined.
     *
     * @param caseNumber
     *            the case number e.g. T20028811
     * @return true if the given case is already involved in the join.
     */
    public boolean isCaseJoined(String caseTypeNumber) {
        return caseCharges.containsKey(caseTypeNumber);
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public String getJudgeName() {
        return this.judgeName;
    }

    public void setRefJudgeId(Integer refJudgeId) {
        this.refJudgeId = refJudgeId;
    }

    public Integer getRefJudgeId() {
        return this.refJudgeId;
    }

    protected Object clone(Object object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
        } catch (StreamCorruptedException e) {
            XHIBITErrorHandler.handleError(e);
        } catch (OptionalDataException e) {
            XHIBITErrorHandler.handleError(e);
        } catch (IOException e) {
            XHIBITErrorHandler.handleError(e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            XHIBITErrorHandler.handleError(e);
        }
        return null;
    }

    /**
     * Overrides the clone method in the Object class so as to provide a deep
     * copy of the joinder indictment model instance.
     *
     * @return a new joinder indictment model.
     */
    public Object clone() throws CloneNotSupportedException {
        JoinderIndictmentModel joinderModel = (JoinderIndictmentModel) this.clone(this);
        joinderModel.setWizardPanels(this.wizardPanels);
        return joinderModel;
    }
}