package uk.gov.courtservice.xhibit.client.schedule.addhearing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.helper.FormattedDisplayHelper;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchJudgeAction;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0 The second (of three) Add Bench Warrant Hearing screens. This
 *          screen allows the user to select which defendants the Hearing is
 *          for.
 */
public class AddHearingSelectDefendants extends XPanel {
    private static final Logger LOG = CSServices.getLogger(AddHearingSelectDefendants.class);

    private static final String CASE_TYPE_A = "A";

    private static final String CASE_TYPE_S = "S";

    private static final String CASE_TYPE_T = "T";

    // constants used to represent the field dimensions
    private final Dimension lblDim = new Dimension(75, 17);

    private final Dimension lbl2Dim = new Dimension(100, 17);

    private final Dimension mLblDim = new Dimension(260, 17);

    private final Dimension scrollBarDim = new Dimension(260, 80);

    // only to be set in the constructor
    private final AddHearingModel model;

    private final WizardButtonPanel buttonPanel;

    private final Vector pullDownList01 = new Vector();

    // all of the swing components
    private JLabel lblCourtNo;

    private JLabel lblTime;

    private JLabel lblCurrentCase;

    private JLabel lblHearingType;

    private JLabel lblJudge;

    private JLabel lblDefendants;

    private JLabel lblDefHelpText;

    private JLabel courtRoom;

    private JLabel timeValue;

    private JLabel currentCaseNumber;

    private JLabel hearingTypeValue;

    private JLabel caseTitleLbl;

    private JTextField caseTitleText;

    private JScrollPane scrollDefendants;

    private JList listDefendants;

    private JTextField judgeJTextField;

    private JButton judgeAddButton;

    private JButton judgeResetButton;
    
    private JScrollPane defendantsScrollPane;

    /**
     * Constructor that takes in the model for the dialog and a reference to the
     * button panel
     * 
     * @param model
     * @param buttonPanel
     * @throws CSRecoverableException
     */
    public AddHearingSelectDefendants(AddHearingModel model, WizardButtonPanel buttonPanel)
            throws CSRecoverableException {
        this.model = model;
        this.buttonPanel = buttonPanel;

        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - model: ");
            model.printModel();
        }

        stepInitialise();
        jbInit();
    }

    /**
     * Adds the screen widgets
     */
    private void jbInit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("  AddHearingSelectDefendants - jbInit ");
        }

        this.setLayout(new GridBagLayout());

        final JPanel confirmHearingPanel = new JPanel();

        confirmHearingPanel.setLayout(new GridBagLayout());

        // it is more efficient to re-use the same constraints object, and only
        // set the details that have changed between each use
        final GridBagConstraints constraints = new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

        // Add the Text labels to be shown on the left of the screen
        confirmHearingPanel.add(getLblCourtNo(), constraints);
        confirmHearingPanel.add(getLblTime(), constraints);
        confirmHearingPanel.add(getLblCurrentCase(), constraints);
        confirmHearingPanel.add(getLblHearingType(), constraints);
        confirmHearingPanel.add(getLblJudge(), constraints);
        confirmHearingPanel.add(getLblDefendants(), constraints);

        // Add the values to be shown on the right of the screen
        constraints.gridx = 1;
        confirmHearingPanel.add(getCourtRoom(), constraints);
        confirmHearingPanel.add(getTimeValue(), constraints);
        confirmHearingPanel.add(getCurrentCaseNumber(), constraints);

        constraints.gridwidth = 3;
        confirmHearingPanel.add(getHearingTypeValue(), constraints);
        constraints.gridwidth = 1;

        confirmHearingPanel.add(getJudgeValue(), constraints);

        // judge buttons
        constraints.gridx = 2;
        constraints.gridy = 4;
        confirmHearingPanel.add(getJudgeAddButton(), constraints);
        constraints.gridx = 3;
        constraints.gridy = 4;
        confirmHearingPanel.add(getJudgeResetButton(), constraints);

        // the defendants scroll box
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.insets = new Insets(4, 4, 4, 4);

        confirmHearingPanel.add(getScrollDefendants(), constraints);

        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridx = 0;
        constraints.gridy = 6;
        confirmHearingPanel.add(getCaseTitleLabel(), constraints);
        constraints.gridx = 1;
        constraints.gridy = 6;
        confirmHearingPanel.add(getCaseTitleText(), constraints);

        // need to make all of the changes required for laying out the panel
        // onto the main form
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 26, 4, 22);

        // Add the confirm hearing panel to this main panel
        this.add(confirmHearingPanel, constraints);
    }

    /**
     * Empty implementation of life-cycle method
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
    }

    /**
     * Life-cycle method called by the framework when thue user navigates off
     * the screen
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - stepDeActivate ");
        }
        moveScreenToModel();
    }

    /**
     * Empty implementation of life-cycle method
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    /**
     * Empty implementation of life-cycle method
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Life-cycle method called by the framework when the screen is made visible
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - stepActivate ");
        }
        scrollDefendants.getViewport().setView(getListDefendants());
        moveModelToScreen();
        stepUpdateViewState();
    }

    /**
     * Life-cycle method to set the enabled state of screen components
     */
    public void stepUpdateViewState() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - stepUpdateViewState ");
        }
        boolean caseType_A_S_T = isCaseTypeAST(model.getCaseType());

        enableTextField(getCaseTitleText(), model.isNewUCase());
        enableTextField(getJudgeValue(), false);

        getJudgeResetButton().setEnabled(getJudgeValue().getText().trim().length() > 0);

        getCaseTitleLabel().setVisible(!caseType_A_S_T);
        getCaseTitleText().setVisible(!caseType_A_S_T);
        getLblDefendants().setVisible(caseType_A_S_T);
        getScrollDefendants().setVisible(caseType_A_S_T);

        buttonPanel.getBack().setEnabled(true);
        buttonPanel.getNext().setEnabled(isMandatoryCompleted());
        buttonPanel.getFinish().setEnabled(false);
    }

    /**
     * Determines whether or not all mandatory fields have been completed. The
     * result of this is used to determine the enabled state of the 'Next'
     * button.
     * 
     * @return true if all mandatory fields are completed
     */
    private boolean isMandatoryCompleted() {
        boolean returnCode = true;

        if (LOG.isDebugEnabled()) {
            LOG.debug(" getCaseTitleText()   : " + getCaseTitleText().getText());
        }

        boolean caseTitleIsEmpty = getCaseTitleText().getText().trim().length() == 0;

        if (model.isNewUCase() && caseTitleIsEmpty) {
            returnCode = false;
        }

        return returnCode;
    }

    /**
     * Pseudo life-cycle method to move data from the model to the screen. This
     * is executed as a consequence of making the screen visible.
     */
    private void moveModelToScreen() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - moveModelToScreen ");
        }

        getCourtRoom().setText(model.getSelectedCourtRoomItem());

        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - moveModelToScreen: getCourtRoom() : "
                    + model.getSelectedCourtRoomItem());
            LOG.debug(" AddHearingSelectDefendants - moveModelToScreen: getTime() : " + model.getTime().toString());
        }

        getTimeValue().setText(XDateFormat.format(model.getTime(), XDateFormat.TIMEFORMAT));
        getJudgeValue().setText(model.getJudgeName());

        StringBuffer hearingType = new StringBuffer();
        hearingType.append(model.getRefHearingTypeBasicValue().getHearingTypeCode());
        hearingType.append(" (");
        hearingType.append(model.getRefHearingTypeBasicValue().getHearingTypeDesc());
        hearingType.append(")");
        getHearingTypeValue().setText(hearingType.toString());

        getCaseTitleText().setText(model.getCaseTitle());

        final String newCaseNumber = model.getCaseTypeAndNumber();

        if (!getCurrentCaseNumber().getText().equals(newCaseNumber)) {
            getCurrentCaseNumber().setText(newCaseNumber);

            // Populate pullDownList01 with the defendant names
            // need to ensure that the Vector is empty first
            pullDownList01.clear();

            final Iterator it = model.getDefendants().iterator();
            while (it.hasNext()) {
                DefendantValue defVO = (DefendantValue) it.next();
                StringBuffer buf = new StringBuffer();

                if (defVO.getFirstName() == null && defVO.getMiddleName() == null && defVO.getSurName() == null) {
                    buf.append("No name, id = " + defVO.getId());
                } else {
                    buf.append(checkNull(defVO.getSurName()));
                    buf.append(defVO.getSurName() == null ? "" : ", ");
                    buf.append(checkNull(defVO.getFirstName()));
                    buf.append(' ');
                    buf.append(checkNull(defVO.getMiddleName()));
                }
                PullDownListObject pdlo = new PullDownListObject(defVO.getDefendantID().intValue(), defVO
                        .getDefendantID().toString(), buf.toString(), defVO);
                pullDownList01.add(pdlo);
            }

            // Bugs X54784, X54785 and X54786 - refresh the list
            this.listDefendants.clearSelection();
            this.listDefendants.updateUI();
        }

        model.printModel();
    }

    private String checkNull(String checkString) {
        return ((checkString == null) ? "" : checkString);
    }

    /**
     * Pseudo life-cycle method that moves data from the screen to the model. It
     * is called as a consequence of the user navigating off the screen.
     */
    private void moveScreenToModel() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(" AddHearingSelectDefendants - moveScreenToModel ");
        }
        model.setCaseTitle(getCaseTitleText().getText());
        model.setJudgeName(getJudgeValue().getText());
        if (model.getJudgeName().trim().length() == 0) {
            model.setRefJudgeId(null);
        }

        // X54462 - also had problems with the defendant name changing...
        final Object[] selectedValues = getListDefendants().getSelectedValues();
        final int length = selectedValues.length;

        final Collection selectedDefendants = new Vector();

        for (int i = 0; i < length; i++) {
            final PullDownListObject plo = (PullDownListObject) selectedValues[i];
            selectedDefendants.add(plo.getDefendantValue());
            if (LOG.isDebugEnabled()) {
                LOG.debug(" selectedDefendant " + (i + 1) + " is: " + plo.getDesc());
            }
        }

        model.setSelectedBWHDefendants(selectedDefendants);
    }

    private JLabel getLblCourtNo() {
        if (lblCourtNo == null) {
            lblCourtNo = createLabel(lblDim, "AddHearingCourtNoLabel");
        }
        return lblCourtNo;
    }

    private JLabel getCourtRoom() {
        if (courtRoom == null) {
            courtRoom = createLabel(lbl2Dim);
        }
        return courtRoom;
    }

    private JLabel getLblTime() {
        if (lblTime == null) {
            lblTime = createLabel(lblDim, "AddHearingCourtTimeListedLabel");
        }
        return lblTime;
    }

    private JLabel getTimeValue() {
        if (timeValue == null) {
            timeValue = createLabel(lblDim);
        }
        return timeValue;
    }

    private JLabel getLblCurrentCase() {
        if (lblCurrentCase == null) {
            lblCurrentCase = createLabel(lblDim, "AddHearingCurrentCaseLabel");
        }
        return lblCurrentCase;
    }

    private JLabel getCurrentCaseNumber() {
        if (currentCaseNumber == null) {
            currentCaseNumber = createLabel(lblDim);
        }
        return currentCaseNumber;
    }

    private JLabel getLblHearingType() {
        if (lblHearingType == null) {
            lblHearingType = createLabel(lbl2Dim, "AddHearingHearingTypeLabel");
        }
        return lblHearingType;
    }

    private JLabel getHearingTypeValue() {
        if (hearingTypeValue == null) {
            hearingTypeValue = new JLabel();
            hearingTypeValue.setPreferredSize(new Dimension(425, XHIBITConstant.getLineHeight()));
        }
        return hearingTypeValue;
    }

    private JLabel getLblJudge() {
        if (lblJudge == null) {
            lblJudge = createLabel(lblDim, "AddHearingJudgeLabel");
        }
        return lblJudge;
    }

    private JLabel getCaseTitleLabel() {
        if (caseTitleLbl == null) {
            caseTitleLbl = createLabel(lblDim, "AddHearingCaseTitleLabel");
        }

        return caseTitleLbl;
    }

    private JTextField getCaseTitleText() {
        if (caseTitleText == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.utf8LimitedTextCapability(72) });
            caseTitleText = JTextFieldFactory.getTextField(doc);
            caseTitleText.setPreferredSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            caseTitleText.setMinimumSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            caseTitleText.setToolTipText(getBundleEntry("AddHearingCrestCaseNumberToolTip"));
            caseTitleText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return caseTitleText;
    }

    // SG - MH - added
    private JButton getJudgeAddButton() {
        if (judgeAddButton == null) {
            XhibitActions.getAction(this.model.getXAC(), XhibitActions.OpenSearchJudge).setCaller(this);
            judgeAddButton = new JButton(XhibitActions.getAction(this.model.getXAC(), XhibitActions.OpenSearchJudge));
        }
        return judgeAddButton;
    }

    /**
     * Lazy instantiation of a Reset button to clear the selected judge
     * 
     * @return
     */
    private JButton getJudgeResetButton() {
        if (judgeResetButton == null) {
            judgeResetButton = new JButton();
            judgeResetButton.setToolTipText(getBundleEntry("AddHearingResetToolTip"));
            judgeResetButton.setText(getBundleEntry("AddHearingReset"));
            judgeResetButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    judgeResetButton_actionPerformed(e);
                }
            });
        }

        return judgeResetButton;
    }

    /**
     * Initialises the judge name field
     * 
     * @param e
     */
    private void judgeResetButton_actionPerformed(ActionEvent e) {
        getJudgeValue().setText("");
        stepUpdateViewState();
    }

    private JTextField getJudgeValue() {
        // MH
        if (judgeJTextField == null) {
            judgeJTextField = JTextFieldFactory.getTextField();
            judgeJTextField.setPreferredSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            judgeJTextField.setMinimumSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            judgeJTextField.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return judgeJTextField;
    }

    /**
     * Processes the result of using the judge search framework. This iterates
     * through the collection of selected judges - although this will always
     * contain just one entry - and populates the judge name field.
     * 
     * @param action
     */
    public void processAddJudge(OpenSearchJudgeAction action) {
        Collection col = action.getResults();
        Iterator it = col.iterator();

        while (it.hasNext()) {
            final RefJudgeBasicValue judge = (RefJudgeBasicValue) it.next();

            // PRE00177 - changed to show the required display name
            final String title = FormattedDisplayHelper.getDisplayName(judge);
            getJudgeValue().setText(title);
            model.setJudgeName(title);
            model.setRefJudgeId(judge.getId());
        }

        stepUpdateViewState();
    }

    private JLabel getLblDefendants() {
        if (lblDefendants == null) {
            lblDefendants = createLabel(lblDim, "AddHearingDefendantsLabel");
        }
        return lblDefendants;
    }

    private JScrollPane getScrollDefendants() {
        if (scrollDefendants == null) {
            scrollDefendants = new JScrollPane();
            scrollDefendants.setPreferredSize(scrollBarDim);
            scrollDefendants.setMinimumSize(scrollBarDim);
            scrollDefendants.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return scrollDefendants;
    }

    private JList getListDefendants() {
        if (listDefendants == null) {
            listDefendants = new JList();
            listDefendants.setToolTipText(getBundleEntry("AddHearingDefendantsToolTip"));

            // Add the vector to the list
            listDefendants.setListData(pullDownList01);
            listDefendants.setVisibleRowCount(4);
            listDefendants.setValueIsAdjusting(false);
        }
        return listDefendants;
    }

    private JLabel createLabel(Dimension preferredSize, String resourceKey) {
        final JLabel label = new JLabel();
        label.setPreferredSize(preferredSize);

        if (resourceKey != null) {
            label.setText(getBundleEntry(resourceKey));
        }

        return label;
    }

    private JLabel createLabel(Dimension preferredSize) {
        return createLabel(preferredSize, null);
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? Color.white : SystemColor.text));
    }

    private boolean isCaseTypeAST(String caseType) {
        return (caseType != null && (caseType.equalsIgnoreCase(CASE_TYPE_A) || caseType.equalsIgnoreCase(CASE_TYPE_S) || caseType
                .equalsIgnoreCase(CASE_TYPE_T)));
    }

    /**
     * Immutable class used to represent the value of a row in a JList
     */
    private class PullDownListObject {
        private final int id;

        private final String code;

        private final String desc;

        private final DefendantValue defendantValue;

        public PullDownListObject(int id, String code, String desc, DefendantValue dv) {
            this.id = id;
            this.code = code;
            this.desc = desc;
            this.defendantValue = dv;
        }

        public int getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public DefendantValue getDefendantValue() {
            return defendantValue;
        }

        public String toString() {
            return desc;
        }
    }

    /**
     * Returns a value from the resource bundle associated with this task
     * 
     * @param param
     * @return
     */
    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.TodaysSchedule, param);
    }
}
