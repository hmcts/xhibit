package uk.gov.courtservice.xhibit.client.maintaincharges.joinder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffenceController;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffenceModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.UncodedOffencePanel;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;

/**
 * <p>
 * Title: XHIBIT 2 - Merge Append Panel
 * </p>
 * <p>
 * Description: The panel responsible for merging and appending offences. If a
 * collision is found, i.e Two offences are found to be the same from the
 * joinder and selected indictment, then the merge append panel becomes
 * displayed. Otherwise a seamless append process for all offences execute, and
 * the panel displaying the newly formed joinder indictment is displayed (the
 * next panel after this in the wizard dialog).
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

public class MergeAppendPanel extends JoinderIndictmentPanel implements UncodedOffenceInterface {
    /**
     * Button for going back to the previous count.
     */
    private JButton previousCountButton = null;

    /**
     * Button for going to the next count.
     */
    private JButton nextCountButton = null;

    /**
     * The text area used to render the details of the current count.
     */
    private JTextArea countTextArea = null;

    /**
     * The panel used to hold the error message.
     */
    private JPanel countPanel = null;

    /**
     * This panel does not get displayed if there are no Counts that can be
     * merged.
     */
    private boolean displayThisPanel = true;

    /**
     * Flag determining if a collision was found during the append/merge
     * process. If this flag becomes set, then this panel becomes displayed on
     * the event of a prev action.
     */
    private boolean collisionFound = false;

    /**
     * The combo box which displays the Counts you can merge to.
     */
    private JComboBox mergeCb = null;

    /**
     * The combo box model which holds the data for the mergeCb.
     */
    private DefaultComboBoxModel countComboBoxModel = null;

    /**
     * Radio button the user selects if they wish to merge Counts.
     */
    private JRadioButton mergeRb = null;

    /**
     * Radio button the user selects if they wish to append Counts.
     */
    private JRadioButton appendRb = null;

    /**
     * A dummy radio button only used to de-select the merge and append radio
     * buttons. This is not displayed anywhere.
     */
    private JRadioButton dummyRb = null;

    /**
     * The title of this panel.
     */
    private String panelTitle = null;

    /**
     * The Count (offence) currently being processed on the selected indictment
     * that the user has the option to merge. i.e. it has the same ref offence
     * id as an offence on the Joinder but no defendants in common.
     */
    private OffenceValue selectedCollisionOffenceValue = null;

    /**
     * Contains clones of the JoinderIndictmentModel at each stage of the merge
     * append process for an indictment to allow the user to process the next or
     * previous Counts.
     */
    private final Stack models = new Stack();

    /**
     * Contains the current position (Count) in the selected indictment. Used to
     * allow the user to process the next or previous Counts.
     */
    private ListIterator selectedOffenceListIterator = null;

    /**
     * contains the crest offence sequence number of the offence that the user
     * currently has the option to merge or append.
     */
    private Integer currentSeqNo = null;

    /**
     * contains the crest offence sequence number of the offence that the user
     * previously had the option to merge or append.
     */
    private Integer previousSeqNo = null;

    /**
     * The titled border for current joinder merge append options panel.
     */
    private TitledBorder titledBorder = null;

    private UncodedOffenceController uoc = null;

    private UncodedOffenceModel uom = null;

    private UncodedOffencePanel uop = null;

    private UncodedOffenceModel emptyUncodedOffenceModel = null;

    /**
     * Prevents activate reentrancy issues due to setVisible changes in the
     * underlying JDK 1.5
     */
    private boolean subActivateReentrancy = false;

    /**
     * Default constructor.
     * 
     * @param wizardDialog
     *            the dialog responsible for hosting this dialog.
     * @param title
     *            the tiel for the wizard panel
     */
    public MergeAppendPanel(final XWizardDialog wizardDialog, final String title) {
        super(wizardDialog);
        this.panelTitle = title;

        try {
            stepInitialise();
        } catch (CSRecoverableException e) {
            log.debug("Error initialising merge append panel.");
            XHIBITErrorHandler.handleError(e);
        }
    }

    /**
     * Initialise the gui components.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        emptyUncodedOffenceModel = new UncodedOffenceModel();

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        PanelTitleLabel panelTitleLabel = new PanelTitleLabel(panelTitle);

        previousCountButton = new JButton(new PreviousAction());
        nextCountButton = new JButton(new NextAction());

        countPanel = new JPanel(gbLayout);

        gbConstraints = new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
        countPanel.add(getCountTextArea(), gbConstraints);

        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        buttonPanel.add(previousCountButton, gbConstraints);

        gbConstraints = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                XHIBITConstant.nonContainerInsets, 0, 0);
        buttonPanel.add(nextCountButton, gbConstraints);

        setLayout(new GridBagLayout());
        add(panelTitleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        add(countPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        add(getMergeAppendPanel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        add(buttonPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        // Default status of the panel.
        countPanel.setVisible(false);
    }

    private JPanel getMergeAppendPanel() {
        JPanel joinderTitledPanel = new JPanel(new GridBagLayout());

        joinderTitledPanel.setBorder(getTitledBorder());

        joinderTitledPanel.add(getAppendRb(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        joinderTitledPanel.add(getMergeRb(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        joinderTitledPanel.add(getMergeCombo(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        joinderTitledPanel.add(getUncodedOffencePanel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        ButtonGroup mergeAppendBtnGroup = new ButtonGroup();
        mergeAppendBtnGroup.add(mergeRb);
        mergeAppendBtnGroup.add(appendRb);
        dummyRb = new JRadioButton();
        mergeAppendBtnGroup.add(dummyRb);

        return joinderTitledPanel;
    }

    private UncodedOffencePanel getUncodedOffencePanel() {
        if (uop == null) {
            uoc = new UncodedOffenceController();
            uom = uoc.getModel();

            uop = new UncodedOffencePanel(uom);
            uop.stepActivate();
            uop.setEnabled(false);
        }
        return uop;
    }

    private TitledBorder getTitledBorder() {
        if (titledBorder == null) {
            Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
            titledBorder = BorderFactory.createTitledBorder(loweredetched, getResource(JoinderConstants.BORDER_TITLE));
            titledBorder.setTitleJustification(TitledBorder.LEFT);
        }
        return titledBorder;
    }

    private JComboBox getMergeCombo() {
        if (mergeCb == null) {
            countComboBoxModel = new DefaultComboBoxModel();
            mergeCb = new JComboBox(countComboBoxModel);
            mergeCb.setRenderer(new CountComboBoxRenderer());
            mergeCb.setToolTipText(getResource(JoinderConstants.MERGE_CB_TOOLTIP));
            mergeCb.setEnabled(false);
            mergeCb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    log.debug("mergeCombo actionPerformed");
                    stepUpdateViewState();
                }
            });
        }
        return mergeCb;
    }

    private JRadioButton getMergeRb() {
        if (mergeRb == null) {
            mergeRb = new JRadioButton(getResource(JoinderConstants.QUESTION_MERGE));
            mergeRb.setToolTipText(getResource(JoinderConstants.MERGE_RB_TOOLTIP));
            mergeRb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    log.debug("merge actionPerformed");
                    stepUpdateViewState();
                }
            });
        }
        return mergeRb;
    }

    private JRadioButton getAppendRb() {
        if (appendRb == null) {
            appendRb = new JRadioButton(getResource(JoinderConstants.QUESTION_APPEND));
            appendRb.setToolTipText(getResource(JoinderConstants.APPEND_RB_TOOLTIP));
            appendRb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    log.debug("append actionPerformed");
                    stepUpdateViewState();
                }
            });
        }
        return appendRb;
    }

    private JTextArea getCountTextArea() {
        if (countTextArea == null) {
            countTextArea = new JTextArea();
            countTextArea.setEditable(false);
            countTextArea.setBackground(SystemColor.control);
            countTextArea.setWrapStyleWord(true);
            countTextArea.setLineWrap(true);
            countTextArea.setFont(XHIBITConstant.getCurrentFont());
        }
        return countTextArea;
    }

    /**
     * Overrides the method in JoinderIndictmentPanel. If the latest event from
     * the wizard dialog was a back action, then this method delegates the call
     * to the previous panel loaded before this, and it is that one which
     * becomes displayed. Otherwise, this method instigates the append process,
     * and after completion displays the next panel loaded after this one in the
     * wizard dialog.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        super.stepActivate();
        // Refer to JoinderIndictmentPanel superclass for reentrancy details
        if (subActivateReentrancy) {
            log.debug("MergeAppendPanel - stepActivate reentrancy");
            return;
        } else {
            subActivateReentrancy = true;
        }
        log.debug("stepActivate Begin - event = " + wizardDialog.getLatestEvent());

        previousCountButton.setEnabled(false);

        // On the back event, reference the model to a clone of the cloned
        // model.
        // Also make a call to get the correct selected offence values.
        // If a collision is found, then go to the previous panel.
        // Otherwise just finish this method, and the error message will be
        // displayed.
        if (wizardDialog.getLatestEvent() == XWizardDialog.PREV_EVENT) {
            selectedCollisionOffenceValue = null;
            currentSeqNo = null;

            // make sure the merge and append radio buttons are de-selected.
            dummyRb.setSelected(true);

            try {
                log.debug("stepActivate cloning first model. size = " + models.size());
                // Get starting model.
                model = (JoinderIndictmentModel) ((JoinderIndictmentModel) models.get(0)).clone();

                models.clear();
                log.debug("stepActivate just cleared models. size = " + models.size());
                log.debug("stepActivate about to call saveTheLatestModel. size = " + models.size());
                saveTheLatestModel();
            } catch (CloneNotSupportedException e) {
                log.debug("stepActivate CloneNotSupportedException. size = " + models.size());
                log.warn(e);
            }

            log.debug("stepActivate - collisionFound = " + collisionFound);
            if (!collisionFound) {
                wizardDialog.prev();
                return;
            }
        } else {
            log.debug("stepActivate - in else. size = " + models.size());
            models.clear();
            log.debug("stepActivate about to call saveTheLatestModel. size = " + models.size());
            saveTheLatestModel();
        }

        collisionFound = false; // Reset this value.

        final Collection offences = model.getSelectedIndictment().getOffenceValues();
        selectedOffenceListIterator = ((List) offences).listIterator();

        displayThisPanel = true;
        if (appendSelectedOffences()) {
            log
                    .debug("appendSelectedOffences returns true so don't display this panel and enable Next before advancing to the next panel");
            displayThisPanel = false;
            wizardDialog.getButtonPanel().getNext().setEnabled(true);
            wizardDialog.next();
            return;
        }

        log.debug("Disable the next button - will never be used");
        wizardDialog.getButtonPanel().getNext().setEnabled(false);

        log.debug("stepActivate END");
    }

    public void stepUpdateViewState() {
        log.debug("stepUpdateViewState Begin - combo = " + getMergeCombo().getSelectedItem());

        // This method is called by the framework next() method in XWizardDialog
        // after it has called stepActivate. If there are no Counts to merge,
        // then this panel will not be displayed so we do not need to change the
        // view state of the components.
        if (!displayThisPanel)
            return;

        boolean isUncodedOffence = UNCODED_OFFENCE_REFERENCE_CODE
                .equals(selectedCollisionOffenceValue.getOffenceCode());

        getMergeCombo().setEnabled(getMergeRb().isSelected());
        nextCountButton.setEnabled(getAppendRb().isSelected()
                || (getMergeRb().isSelected() && getMergeCombo().getSelectedItem() != null));

        getUncodedOffencePanel().setEnabled(isUncodedOffence && getMergeRb().isSelected());
        getUncodedOffencePanel().setEditable(isUncodedOffence && getMergeRb().isSelected());

        // Merge the current collision offence on this call.
        if (isUncodedOffence && getMergeRb().isSelected()) {
            OffenceValue mergeOffenceValue = (OffenceValue) getMergeCombo().getSelectedItem();
            UncodedOffenceModel currentJoinderUOM = new UncodedOffenceModel(mergeOffenceValue);
            getUncodedOffencePanel().updateModel(currentJoinderUOM);
            getUncodedOffencePanel().stepActivate();
        }

        if (nextCountButton.isEnabled()) {
            getRootPane().setDefaultButton(nextCountButton);
        }
    }

    /**
     * No implementation needed for this particular panel. Next button should
     * never be enabled for this panel. Whenever the panel is displayed the user
     * needs to merge or append a count and will then use the next count button.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivateOnNext() throws CSRecoverableException {
    }

    /**
     * Sets reentrancy variable
     */
    public void stepDeactivateOnAll() {
        subActivateReentrancy = false;
    }

    /**
     * No implementation - no validation for this panel needed.
     */
    public void stepValidate() {
    }

    /**
     * Will traverse the list of selected offences. If a collision is found,
     * then this panel paints the details on the screen waiting for user to
     * either merge or append the count.
     * 
     * @return whether the process of appending all offences has been completed
     * @throws CSRecoverableException
     */
    private boolean appendSelectedOffences() throws CSRecoverableException {
        boolean isFinished = true;
        OffenceValue offenceValue = null;

        while (selectedOffenceListIterator.hasNext()) {
            // De-select the merge/append radio buttons.
            dummyRb.setSelected(true);

            // Enable the previous Count button if there was a collision.
            previousCountButton.setEnabled(collisionFound && (models.size() > 1));
            previousSeqNo = currentSeqNo;

            offenceValue = (OffenceValue) selectedOffenceListIterator.next();
            if (!processOffence(offenceValue, false)) {
                // Break then display merge append options.
                currentSeqNo = offenceValue.getCrestOffenceSeqNo();
                isFinished = false;
                collisionFound = true;
                log.debug(" - setting collisionFound to true");

                log.debug("appendSelectedOffences() about to call - saveTheLatestModel. size = " + models.size());
                saveTheLatestModel();

                getTitledBorder().setTitle(MergeAppendHelper.getJoinderTitle(model.getCasesAndIndictmentNos()));

                nextCountButton.setEnabled(false);
                countPanel.setVisible(true);
                break;
            }
            // If this is the last offence, go to the next panel in the
            // wizard.
            if (!selectedOffenceListIterator.hasNext()) {
                isFinished = true;
            }
        }
        return isFinished;
    }

    /**
     * Saves the JoinderIndictmentModel by cloning it and then pushing it onto
     * the stack of models.
     */
    private void saveTheLatestModel() {
        log.debug("saveTheLatestModel - Begin - size = " + models.size());
        try {
            models.push((JoinderIndictmentModel) model.clone());
        } catch (CloneNotSupportedException cnse) {
            log.debug("saveTheLatestModel - CloneNotSupportedException - size = " + models.size());
            log.warn(cnse);
        }
    }

    /**
     * Will append the given selected offence to the joinder indictment. If
     * there is the same offence in the joinder indictment then this method
     * returns false, and no action is taken. However, if appendIfCollision is
     * NOT set on, then the append is made whatever the circumstances.
     * 
     * @param selectedOffenceValue
     *            the selected offence value to be appended onto the joinder
     *            indictment.
     * @param appendIfCollision
     *            if set to true, then the offence will be appended regardless
     *            if there is a collision or not.
     * 
     * @return true if the append is successful, i.e there are no offences in
     *         the joinder indictment that are the same to the selected offence.
     */
    public boolean processOffence(final OffenceValue selectedOffenceValue, final boolean appendIfCollision) {
        log.debug("processOffence - Begin");

        if (!appendIfCollision) {
            Map offenceMap = MergeAppendHelper.createJoinderOffenceMap(model.getJoinderIndictmentValue()
                    .getNewChargeValue().getOffenceValues());

            // If any of the offences in the Joinder have the same
            // refOffenceID
            // as that of the selected offence then it is possible to merge.
            if (offenceMap.values().contains(selectedOffenceValue.getRefOffenceID())) {
                // re-build the merge combo box model
                Vector mergeOffences = MergeAppendHelper.createMergeComboData(model.getJoinderIndictmentValue()
                        .getNewChargeValue().getOffenceValues(), selectedOffenceValue, model.getDefendantMap());

                if (mergeOffences.size() > 0) {
                    selectedCollisionOffenceValue = selectedOffenceValue;

                    // build the offence details
                    String offenceDetails = MergeAppendHelper.getOffenceDetails(model, selectedOffenceValue);
                    countTextArea.setText(offenceDetails);

                    countComboBoxModel.removeAllElements();
                    for (Iterator i = mergeOffences.iterator(); i.hasNext();) {
                        countComboBoxModel.addElement(i.next());
                    }
                    getUncodedOffencePanel().updateModel(emptyUncodedOffenceModel);
                    getUncodedOffencePanel().stepActivate();

                    return false;
                }
            }
        }

        // The selected offence cannot be merged to another offence, so append
        // it the offences on the joinder indictment.
        appendOffence(selectedOffenceValue);

        // Re-enable the merge button so that it is available for use for
        // the next collision, if there is one.
        previousCountButton.setEnabled(true);
        return true;
    }

    /**
     * Convert the given selected offence value to a JoinderOffenceValue and
     * append this to the offence on joinder indictment.
     * 
     * @param selectedOffenceValue
     *            the offence value to append to the joinder indi
     */
    private void appendOffence(final OffenceValue selectedOffenceValue) {
        log.debug("MergeAppendPanel-2 - appendOffence - Begin");

        final Vector joinderOffences = new Vector(model.getJoinderIndictmentValue().getNewChargeValue()
                .getOffenceValues());

        final JoinderOffenceValue newOffenceValue = new JoinderOffenceValue(selectedOffenceValue.getOffenceID(),
                selectedOffenceValue.getChargeID(), selectedOffenceValue.getRefOffenceID(), new Vector(),// defendant
                                                                                                            // ids
                                                                                                            // -
                                                                                                            // empty
                                                                                                            // collection
                selectedOffenceValue.getCrestOffenceFreeText(), selectedOffenceValue.getCrestOffenceID(), new Integer(
                        ++model.offenceSeqNoCounter), selectedOffenceValue.getMultiple(), selectedOffenceValue
                        .getOffenceDescription());

        // Append the original case and offence if to the new offence.
        if (newOffenceValue.getOriginalCaseOffences() == null) {
            newOffenceValue.setOriginalCaseOffences(new Vector());
        }
        newOffenceValue.getOriginalCaseOffences().add(
                new Integer[] { model.getSelectedCaseId(), selectedOffenceValue.getOffenceID() });
        newOffenceValue.setDefendantIDs(new Vector());// empty collection

        // Rest of the offence setters.
        newOffenceValue.setOffenceCode(selectedOffenceValue.getOffenceCode());
        newOffenceValue.setRefOffenceID(selectedOffenceValue.getRefOffenceID());
        newOffenceValue.setRefSystemCodeID(selectedOffenceValue.getRefSystemCodeID());
        newOffenceValue.setCrestHOClass(selectedOffenceValue.getCrestHOClass());
        newOffenceValue.setCrestHOSubclass(selectedOffenceValue.getCrestHOSubclass());

        newOffenceValue.setOffenceStartDateTime(selectedOffenceValue.getOffenceStartDateTime());
        newOffenceValue.setOffenceEndDateTime(selectedOffenceValue.getOffenceEndDateTime());
        newOffenceValue.setForceLocationCode(selectedOffenceValue.getForceLocationCode());
        newOffenceValue.setAddressId(selectedOffenceValue.getAddressId());
        newOffenceValue.setAddressValue(selectedOffenceValue.getAddressValue());
        
        // Set the defendant's attributes onto the new offence value.
        DefendantValue selectedDefendantValue = null;
        Iterator defendantList = selectedOffenceValue.getDefendantValues().iterator();
        while (defendantList.hasNext()) {
            selectedDefendantValue = (DefendantValue) defendantList.next();
            setDefendantAttributesOnOffence(newOffenceValue, selectedDefendantValue);
        }

        // Add the new offences to the collection of offences in the joinder
        // indictment.
        joinderOffences.add(newOffenceValue);

        model.getJoinderIndictmentValue().getNewChargeValue().setOffenceValues(joinderOffences);

        log.debug("MergeAppendPanel-2 - appendOffence - End");
    }

    /**
     * Merge the collision selected and indictment values.
     * 
     * @param offenceId
     *            the id of the offence to merge.
     */
    public void mergeOffence(final Integer offenceId) {
        log.debug("MergeAppendPanel-2 - mergeOffence - Begin");

        JoinderOffenceValue joinderOffenceValue = MergeAppendHelper.getJoinderOffenceValue(model
                .getJoinderIndictmentValue().getNewChargeValue(), offenceId);

        // Append the caseid/offence id pair to the joinder offence.
        joinderOffenceValue.getOriginalCaseOffences().add(
                new Integer[] { model.getSelectedCaseId(), selectedCollisionOffenceValue.getOffenceID() });

        if (selectedCollisionOffenceValue.getOffenceCode().equals(
                UncodedOffenceInterface.UNCODED_OFFENCE_REFERENCE_CODE)) {
            // set the values for uncoded offences.
            joinderOffenceValue.setOffenceDescription(uom.getCrestDesc());
            joinderOffenceValue.setCrestOffenceFreeText(uom.getCrestDesc());
            joinderOffenceValue.setCrestHOClass(uom.getHoClass());
            joinderOffenceValue.setCrestHOSubclass(uom.getHoSubclass());
        } else {
            // set the values for coded offences.
            joinderOffenceValue.setOffenceDescription(selectedCollisionOffenceValue.getOffenceDescription());
        }

        // Now merge the selected defendants onto the joinder offence.
        DefendantValue defendantValue = null;
        Iterator selectedDefendants = selectedCollisionOffenceValue.getDefendantValues().iterator();
        while (selectedDefendants.hasNext()) {
            defendantValue = (DefendantValue) selectedDefendants.next();
            setDefendantAttributesOnOffence(joinderOffenceValue, defendantValue);
        }

        log.debug("MergeAppendPanel-2 - mergeOffence - End");
    }

    /**
     * Utility method for adding defendants onto an offence. This method will
     * add defendant's <b>original</b> id and value to the offence. The
     * defendant's normal Id is used for adding the DefendantOnOffenceBasicValue
     * to the offenceValue.
     * <P>
     * Note that the way this method is implemented assumes that no two
     * defendants the same can be associated to the same offence.
     * 
     * @param offenceValue
     *            the offence value whos defendant attributes will be appened.
     * @param defendantValue
     *            the value containing the defendant's attributes.
     */
    private void setDefendantAttributesOnOffence(JoinderOffenceValue offenceValue, DefendantValue defendantValue) {
        // If defendant values has not been set, then just create a vector as
        // the collection.
        if (offenceValue.getDefendantValues() == null) {
            offenceValue.setDefendantValues(new Vector());
        }

        // From the defendant map, get the original defendant id.
        DefendantMap defendantMap = model.getDefendantMap();
        Integer originalDefId = defendantMap.getOriginalId(defendantValue.getDefendantID());

        // The id may be null, as it may have never been mapped, so just
        // use the id from the given DefendantValue.
        if (originalDefId == null) {
            offenceValue.getDefendantIDs().add(defendantValue.getDefendantID());
            offenceValue.getDefendantValues().add(defendantValue);
        } else {
            offenceValue.getDefendantIDs().add(originalDefId);
            offenceValue.getDefendantValues().add(defendantMap.getOriginalValue(originalDefId));
        }

        offenceValue.addDefOnOffenceComplexValue(defendantValue.getDefendantID(), offenceValue
                .getDefendantOnOffence(defendantValue.getDefendantID()));
    }

    /**
     * <p>
     * Title: Merge Action
     * </p>
     * <p>
     * Description: This action holds functionality for instigating the merge
     * process of two offences.
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
    private class PreviousAction extends XAction {
        public PreviousAction() {
            String name = getResource(JoinderConstants.PREVIOUS_COUNT);
            setName(name);
            setShortDescription(name);
            setLongDescription(name);
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            log.debug("PreviousAction previousSeqNo = " + previousSeqNo);

            OffenceValue offenceValue = null;

            log.debug("PreviousAction about to pop. size = " + models.size());
            models.pop();
            log.debug("PreviousAction about to pop again. size = " + models.size());
            model = (JoinderIndictmentModel) models.pop();

            setEnabled(!(models.size() == 0));

            if (previousSeqNo == null) {
                // SG - Not sure this can happen?
                while (selectedOffenceListIterator.hasPrevious()) {
                    selectedOffenceListIterator.previous();
                }
            } else {
                while (selectedOffenceListIterator.hasPrevious()) {
                    offenceValue = (OffenceValue) selectedOffenceListIterator.previous();
                    if (offenceValue.getCrestOffenceSeqNo().equals(previousSeqNo)) {
                        break;
                    }
                }
            }

            if (appendSelectedOffences()) {
                wizardDialog.getButtonPanel().getNext().setEnabled(true);
                wizardDialog.next();
            }
        }
    }

    /**
     * <p>
     * Title: Append Action
     * </p>
     * <p>
     * Description: This action holds functionality for instigating the append
     * process of a selected offence onto a joinder indictment.
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
    private class NextAction extends XAction {
        OffenceValue mergeOffenceValue = null;

        public NextAction() {
            String name = getResource(JoinderConstants.NEXT_COUNT);
            setName(name);
            setShortDescription(name);
            setLongDescription(name);
        }

        public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
            log.debug("NextAction - xActionPerformed - Begin");

            if (appendRb.isSelected()) {
                if (processOffence(selectedCollisionOffenceValue, true) && appendSelectedOffences()) {
                    wizardDialog.getButtonPanel().getNext().setEnabled(true);
                    wizardDialog.next();
                }
            } else if (mergeRb.isSelected()) {
                // Merge the current collision offence on this call.
                mergeOffenceValue = (OffenceValue) getMergeCombo().getSelectedItem();
                if (mergeOffenceValue.getOffenceCode().equals(UNCODED_OFFENCE_REFERENCE_CODE)) {
                    uop.stepUpdateViewState();
                    uoc.validateModel("T", ChargeTypes.INDICTMENT.getChargeType());
                }
                mergeOffence(mergeOffenceValue.getOffenceID());

                // Re-start the append process for remaining offences in the
                // indictment.
                if (appendSelectedOffences()) {
                    wizardDialog.next();
                }
            } else {
                log.warn("NextAction: xActionPerformed: Unknown radio button pressed");
            }
        }
    }
}
