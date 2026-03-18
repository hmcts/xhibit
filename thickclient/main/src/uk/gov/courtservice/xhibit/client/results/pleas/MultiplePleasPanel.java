package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.SystemRefComboBoxRenderer;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Input Plea details panel for Count(s) on Indictments
 * </p>
 * <p>
 * Description: Used to input Plea details which will be applied to more than
 * one Count/Defendant pair.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Id: MultiplePleasPanel.java,v 1.22 2006/06/05 12:31:34 bzjrnl Exp $
 */
public class MultiplePleasPanel extends XPanel {
    private static final String OTHER_OFFENCE = "O";

    private static final String GUILTY_LESSER_OFFENCE = "GLO";

    private static final String GUILTY_ALTERNATE_OFFENCE = "GAO";

    private final String errorTitle;

    private final String errorMessage;

    private final ResourceBundle resources;

    private final PleaControllerModel model;

    private final XhibitApplicationController xac;

    private final Insets defaultInsets = new Insets(2, 2, 2, 2);

    private JLabel crestCodeLabel = null;

    private JTextField crestCodeText = null;

    private JLabel crestDescriptionLabel = null;

    private JComboBox pleaCb = null;

    private JLabel arraignmentDateLabel = null;

    private XDatePanel arraignmentDate = null;

    private JButton offenceBtn = null;

    private JLabel offenceLabel = null;

    private JLabel otherLabel = null;

    private JTextField otherText = null;

    private JTextArea offenceDescText = null;

    private JScrollPane scrollPane = null;

    private Integer offenceId = null;

    private String offenceCode = null;

    private RefOffenceBasicValue refOffence;

    private OkCancelPanel okCancelPanel;

    public MultiplePleasPanel(PleaControllerModel pcm) {
        super();
        this.model = pcm;
        this.xac = pcm.getACM().getXhibitApplicationController();
        this.resources = XHIBITConstant.getResourceBundle(XhibitBundles.Pleas);
        this.errorTitle = XHIBITConstant.getResource(resources, "plea.code.error.Title");
        this.errorMessage = XHIBITConstant.getResource(resources, "plea.code.error.Message");
        jbInit();
    }

    /**
     * Paints the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        this.add(getCrestCodeLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getCrestCodeText(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getCrestDescriptionLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getPleaCb(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getArraignmentDateLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getArraignmentDate(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getOtherLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getOtherText(), new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, defaultInsets, 0, 0));
        this.add(getOffenceLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getScrollPane(), new GridBagConstraints(1, 4, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(getSearchOffenceBtn(), new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.setMinimumSize(new Dimension(450, 230));
        this.setPreferredSize(new Dimension(450, 230));
    }

    private JLabel getCrestCodeLabel() {
        if (crestCodeLabel == null) {
            crestCodeLabel = new JLabel();
            crestCodeLabel.setText(XHIBITConstant.getResource(resources, "multiple.label.crestcode"));
        }
        return crestCodeLabel;
    }

    private JTextField getCrestCodeText() {
        if (crestCodeText == null) {
            crestCodeText = new JTextField();
            crestCodeText.setMinimumSize(new Dimension(50, 20));
            crestCodeText.setPreferredSize(new Dimension(50, 20));

            crestCodeText.setInputVerifier(new PleaCodeVerifier(this));
        }
        return crestCodeText;
    }

    /**
     * Checks that the code entered in the Crest code text field is a valid
     * Crest code.
     * 
     * @return true if a valid Plea Crest code has been entered.
     */
    private boolean isValidCrestCode() {
        boolean isValid = true;
        Collection col = model.getIndictmentPleaRefData();
        String code = getCrestCodeText().getText();
        code = code.trim().toUpperCase();
        RefSystemCodeBasicValue rscbv = PleaHelper.getRefSystemCodeBasicValue(col, code,
                PleaHelper.SEARCH_TYPE_PLEA_CODE);

        if (rscbv == null) {
            isValid = false;
        } else {
            getPleaCb().setSelectedItem(rscbv);
            getCrestCodeText().setText(code);
        }
        return isValid;
    }

    private JLabel getCrestDescriptionLabel() {
        if (crestDescriptionLabel == null) {
            crestDescriptionLabel = new JLabel();
            crestDescriptionLabel.setText(XHIBITConstant.getResource(resources, "multiple.label.crestdescription"));
        }
        return crestDescriptionLabel;
    }

    private JComboBox getPleaCb() {
        if (pleaCb == null) {
            Collection col = model.getIndictmentPleaRefData();
            pleaCb = new JComboBox(col.toArray());
            SystemRefComboBoxRenderer renderer = new SystemRefComboBoxRenderer(false);
            pleaCb.setRenderer(renderer);
            pleaCb.setSelectedIndex(0);
            pleaCb.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
                    RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) getPleaCb().getSelectedItem();
                    getCrestCodeText().setText(rscbv.getCode());
                    stepUpdateViewState();
                }
            });
        }
        return pleaCb;
    }

    private JLabel getArraignmentDateLabel() {
        if (arraignmentDateLabel == null) {
            arraignmentDateLabel = new JLabel();
            arraignmentDateLabel.setText(XHIBITConstant.getResource(resources, "multiple.label.arraignmentdate"));
        }
        return arraignmentDateLabel;
    }

    private XDatePanel getArraignmentDate() {
        if (arraignmentDate == null) {
            Calendar todaysDate = null;
            arraignmentDate = new XDatePanel(this, todaysDate);
        }
        return arraignmentDate;
    }

    protected JLabel getOffenceLabel() {
        if (offenceLabel == null) {
            offenceLabel = new JLabel();
            offenceLabel.setText(XHIBITConstant.getResource(resources, "multiple.label.alternate"));
            offenceLabel.setMinimumSize(new Dimension(100, 20));
            offenceLabel.setPreferredSize(new Dimension(100, 20));
        }
        return offenceLabel;
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane(getOffenceDescText());
            scrollPane.setPreferredSize(new Dimension(200, 65));
        }
        return scrollPane;
    }

    public JTextArea getOffenceDescText() {
        if (offenceDescText == null) {
            offenceDescText = new JTextArea();
            offenceDescText.setLineWrap(true);
            offenceDescText.setWrapStyleWord(true);
            offenceDescText.setMinimumSize(new Dimension(200, 65));
        }
        return offenceDescText;
    }

    private JButton getSearchOffenceBtn() {
        if (offenceBtn == null) {
            offenceBtn = new JButton();
            offenceBtn.setAction(new PleaSearchOffenceAction(this));
            offenceBtn.setMinimumSize(new Dimension(90, 25));
            offenceBtn.setPreferredSize(new Dimension(90, 25));
        }
        return offenceBtn;
    }

    private JLabel getOtherLabel() {
        if (otherLabel == null) {
            otherLabel = new JLabel();
            otherLabel.setText(XHIBITConstant.getResource(resources, "multiple.label.other"));
        }
        return otherLabel;
    }

    protected JTextField getOtherText() {
        if (otherText == null) {
            otherText = new JTextField();
            otherText.setMinimumSize(new Dimension(200, 20));
            otherText.setPreferredSize(new Dimension(200, 20));
        }
        return otherText;
    }

    private void moveModelToScreen() {
        XHIBITConstant.debug("Date = " + model.getArraignmentDate().getTime());
        getArraignmentDate().setDate(model.getArraignmentDate());
    }

    private void moveScreenToModel() throws CSValidationException {
        model.setPleaCode(getCrestCodeText().getText());

        RefSystemCodeBasicValue rscbv = (RefSystemCodeBasicValue) getPleaCb().getSelectedItem();
        model.setPleaDescription(rscbv.getDecode());
        model.setPleaId(rscbv.getId());
        model.setArraignmentDate(getArraignmentDate().getDate());

        model.setOtherPleaText(getOtherText().getText());
        model.setAltOffenceId(offenceId);
        model.setAltOffenceCode(offenceCode);
        model.setAltOffenceDesc(getOffenceDescText().getText());

        XHIBITConstant.debug("moveScreenToModel: Date = " + model.getArraignmentDate().getTime());
    }

    public void stepInitialise() throws CSRecoverableException {
    }

    public void stepDeactivate() throws CSRecoverableException {
        if (!PleaHelper.isMultipleArraignmentDateValid(arraignmentDate.getDate().getTime(), model)) {
            throw new UserCancelException();
        }
        moveScreenToModel();
    }

    public void stepValidate() throws CSRecoverableException, CSValidationException {
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        String code = getCrestCodeText().getText();
        Calendar date = getArraignmentDate().getDate();

        boolean codeEntered = (code != null) && (!code.trim().equals(""));
        boolean dateEntered = date != null;

        if (code.equals(OTHER_OFFENCE)) {
            getOffenceDescText().setText("");
            offenceId = null;
        } else if (code.equals(GUILTY_LESSER_OFFENCE) || code.equals(GUILTY_ALTERNATE_OFFENCE)) {
            getOtherText().setText("");
        } else {
            getOffenceDescText().setText("");
            offenceId = null;
            getOtherText().setText("");
        }

        getOtherLabel().setEnabled(code.equals(OTHER_OFFENCE));
        getOtherText().setEnabled(code.equals(OTHER_OFFENCE));

        getOffenceLabel().setEnabled(code.equals(GUILTY_LESSER_OFFENCE) || code.equals(GUILTY_ALTERNATE_OFFENCE));
        getOffenceDescText().setEnabled(false);
        getSearchOffenceBtn().setEnabled(code.equals(GUILTY_LESSER_OFFENCE) || code.equals(GUILTY_ALTERNATE_OFFENCE));

        okCancelPanel.okButton.setEnabled(codeEntered && dateEntered && isValidCrestCode());
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[MultiplePleasPanel] stepActivate");
        moveModelToScreen();
        getArraignmentDate().setRequired(true);

        XDialog xDialog = (XDialog) SwingUtilities.getWindowAncestor(this);
        okCancelPanel = (OkCancelPanel) xDialog.getButtonPanel();
        stepUpdateViewState();
    }

    public void setOffenceId(Integer offenceId) {
        this.offenceId = offenceId;
    }

    public void setOffenceCode(String offenceCode) {
        this.offenceCode = offenceCode;
    }

    public class PleaSearchOffenceAction extends XAction implements SearchProcessHandler {
        public PleaSearchOffenceAction(MultiplePleasPanel parent) {
            model.setMultiplePleasPanel(parent);

            setModel(model);
            setController(xac);
            setName(XHIBITConstant.getResource(resources, "multiple.button.search"));
            setShortDescription(XHIBITConstant.getResource(resources, "multiple.button.search"));
            setToolTipText(XHIBITConstant.getResource(resources, "multiple.button.search"));
        }

        public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
            AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                    (XhibitApplicationController) getController(), XhibitActions.OpenSearchObsoleteOffence);

            sa.setCaller(this);
            sa.xActionPerformed(e);
        }

        public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
            Collection col = searchAction.getResults();
            Iterator it = col.iterator();
            if (it.hasNext()) {
                refOffence = (RefOffenceBasicValue) it.next();
                offenceId = refOffence.getId();
                offenceCode = refOffence.getOffenceCode();
                getOffenceDescText().append(refOffence.getOffenceDesc());
            } else {
                throw new UserCancelException();
            }
        }
    }

    class PleaCodeVerifier extends InputVerifier {
        private Component component;

        public PleaCodeVerifier(Component component) {
            this.component = component;
        }

        public boolean verify(JComponent input) {
            boolean inRange = isValidCrestCode();
            return inRange;
        }

        public boolean shouldYieldFocus(JComponent input) {
            boolean valid = super.shouldYieldFocus(input);
            JTextField tf = (JTextField) input;

            if (!valid) {
                tf.requestFocus();
                JOptionPane.showMessageDialog(component, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
            }
            return valid;
        }
    }

}
