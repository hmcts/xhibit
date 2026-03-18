package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.ChildOfXPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.util.text.ValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;

public class DefendantOffenceDetails extends JPanel implements ChildOfXPanel {

    private static final Logger log = CSServices.getLogger(DefendantOffenceDetails.class);

    protected static enum COMMITTED_MODE {
        YES, NO, NOT_KNOWN
    };

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel dateOfArrestLabel;

    private JLabel dateOfChargeLabel;

    private JLabel offenceCommittedOnBailLabel;

    private JLabel seqNoLabel;

    private JTextField seqNoText;

    private JTextField onBailText;

    private TitledBorder titledBorder;

    private XPanel parent;

    private XDatePanel dateOfArrestDate;

    private XDatePanel dateOfChargeDate;

    private Dimension medDim = new Dimension(100, 20);

    private Dimension dateDim = new Dimension(100, 25);

    private boolean isBreach;

    private ChargesControllerHelper.MODE mode;

    private DefendantOnOffenceComplexValue defOnOffComplexValue;

    private String caseType;

    private static String CASE_TYPE_TRIAL = "T";

    private Date currentDate = new Date();

    private Calendar blankDate = null;

    private List seqNosList;

    public DefendantOffenceDetails() {
        init();
    }

    public DefendantOffenceDetails(XPanel parent, boolean isBreach, ChargesControllerHelper.MODE mode, String caseType,
            DefendantOnOffenceComplexValue defOnOffComplexValue) {
        this(parent, isBreach, mode, caseType, defOnOffComplexValue, null);
    }

    public DefendantOffenceDetails(XPanel parent, boolean isBreach, ChargesControllerHelper.MODE mode, String caseType,
            DefendantOnOffenceComplexValue defOnOffComplexValue, List seqNosList) {
        if (parent == null || mode == null || caseType == null) {
            throw new IllegalArgumentException(
                    "DefendantOffenceDetails - parent, mode, caseType and defOnOffComplexValue parameters must contain values");
        }

        this.parent = parent;
        this.isBreach = isBreach;
        this.mode = mode;
        this.caseType = caseType;
        this.defOnOffComplexValue = defOnOffComplexValue;
        this.seqNosList = seqNosList;
        init();
        if (isBreach) {
            try {
                processItemChanged();
            } catch (CSRecoverableException csre) {
                XHIBITErrorHandler.handleError(csre);
            }
        }
    }

    private void init() {
        this.setLayout(gridBagLayout1);
        titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow), getString("defendantoffencedetails.border.title"));
        this.setBorder(titledBorder);

        // Add Labels
        this.add((getDateOfArrestLabel()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDateOfChargeLabel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getOffenceCommittedOnBailLabel(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
        if (isBreach) { // Sequence No field is on another panel for non-Breach
            // Offences
            this.add(getSeqNoLabel(), new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        }

        // Add Entry fields
        this.add(getDateOfArrestDate(), new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDateOfChargeDate(), new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getOnBailText(), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        
        if (isBreach) {// Sequence No field is on another panel for non-Breach
            // Offences
            this.add(getSeqNoText(), new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
        }
    }

    protected XDatePanel getDateOfArrestDate() {
        if (dateOfArrestDate == null) {
            dateOfArrestDate = new XDatePanel(this, blankDate);
            dateOfArrestDate.setMinimumSize(dateDim);
            dateOfArrestDate.setPreferredSize(dateDim);
            dateOfArrestDate.setToolTipText(getString("ttDateOfArrest"));
            // Date of arrest is optional
            dateOfArrestDate.setRequired(false);
        }
        return dateOfArrestDate;
    }

    protected void setDateOfArrestDate(XDatePanel dateOfArrestDate) {
        this.dateOfArrestDate = dateOfArrestDate;
    }

    private JLabel getDateOfArrestLabel() {
        if (dateOfArrestLabel == null) {
            dateOfArrestLabel = new JLabel(getString("dateOfArrestLabel"));
        }
        return dateOfArrestLabel;
    }

    private void setDateOfArrestLabel(JLabel dateOfArrestLabel) {
        this.dateOfArrestLabel = dateOfArrestLabel;
    }

    protected XDatePanel getDateOfChargeDate() {
        if (dateOfChargeDate == null) {
            dateOfChargeDate = new XDatePanel(this, blankDate);
            dateOfChargeDate.setToolTipText(getString("ttDateOfCharge"));
            dateOfChargeDate.setMinimumSize(dateDim);
            dateOfChargeDate.setPreferredSize(dateDim);
            // Date of charge is optional
            dateOfChargeDate.setRequired(false);
        }
        return dateOfChargeDate;
    }

    protected void setDateOfChargeDate(XDatePanel dateOfChargeDate) {
        this.dateOfChargeDate = dateOfChargeDate;
    }

    private JLabel getDateOfChargeLabel() {
        if (dateOfChargeLabel == null) {
            dateOfChargeLabel = new JLabel(getString("dateOfChargeLabel"));
        }
        return dateOfChargeLabel;
    }

    private void setDateOfChargeLabel(JLabel dateOfChargeLabel) {
        this.dateOfChargeLabel = dateOfChargeLabel;
    }

    private JLabel getOffenceCommittedOnBailLabel() {
        if (offenceCommittedOnBailLabel == null) {
            offenceCommittedOnBailLabel = new JLabel(getString("offenceCommittedOnBailLabel"));
        }
        return offenceCommittedOnBailLabel;
    }

    private void setOffenceCommittedOnBailLabel(JLabel offenceCommittedOnBailLabel) {
        this.offenceCommittedOnBailLabel = offenceCommittedOnBailLabel;
    }

    protected void setSeqNoText(JTextField seqNoText) {
        this.seqNoText = seqNoText;
    }

    private JLabel getSeqNoLabel() {
        if (seqNoLabel == null) {
            seqNoLabel = new JLabel(getString("seqNoLabel"));
        }
        return seqNoLabel;
    }

    public boolean enableOffenceCommittedOnBailPanel(boolean boo) {
        getOffenceCommittedOnBailLabel().setEnabled(boo);
        getOnBailText().setEnabled(boo);
        return boo;
    }
    

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }

    public JTextField getOnBailText() {
        if (onBailText == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.upperCase(),
                    Capability.limitedText(1), Capability.yesOrNo() });
            onBailText = JTextFieldFactory.getTextField(doc);
            onBailText.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            onBailText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            onBailText.setColumns(1);
            onBailText.setToolTipText(getString("ttOffenceCommittedOnBail"));
            onBailText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        parent.stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
        }
        return onBailText;
    }
    
    // ****************from SelectDefendantOnOffence
    public JTextField getSeqNoText() {
        if (seqNoText == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.numeric(),
                    Capability.limitedText(3) });
            seqNoText = JTextFieldFactory.getTextField(doc);
            seqNoText.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            seqNoText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            seqNoText.setColumns(5);
            seqNoText.setToolTipText(getString("ttSeqNo"));
            seqNoText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        parent.stepUpdateViewState();
                        // processItemChanged();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
        }
        return seqNoText;
    }

    private void processItemChanged() throws CSRecoverableException {
        SeqNoHelper.processSeqNoChange(this, getSeqNoText(), seqNosList, defOnOffComplexValue);
        parent.stepUpdateViewState();
    }

    /**
     * Validate dates in panel. If any of these are invalid a message box
     * detailing the problem is displayed and UserCancelException thrown.
     * 
     * @param offenceValue
     * @return
     * @throws CSValidationException
     * @throws UserCancelException
     */
    public void validateDates(OffenceValue offenceValue) throws CSValidationException, UserCancelException {
        if (offenceValue == null) {
            throw new IllegalArgumentException("validateDates - offenceValue parameter must contain value");
        }
        validateDates(offenceValue.getOffenceEndDateTime());
    }

    /**
     * Validate dates in panel. If any of these are invalid a message box
     * detailing the problem is displayed and UserCancelException thrown.
     * 
     * @param endDate
     * @return
     * @throws CSValidationExc
     * ption
     * @throws UserCancelException
     */
    public void validateDates(Calendar endDateIn) throws CSValidationException, UserCancelException {

        Calendar endDate = null;
        
        if (endDateIn != null) {
            log.debug("End date is now optional CCN0257");
            endDate = (Calendar)endDateIn.clone();
            endDate.set(Calendar.HOUR, 0);
            endDate.set(Calendar.MINUTE, 0);
            endDate.set(Calendar.SECOND, 0);
            endDate.set(Calendar.AM_PM, Calendar.AM);
        }

        if (getDateOfArrestDate().getDate() == null && getDateOfChargeDate().getDate() == null) {
            log.debug("Arrest Date and Charge Date are both null, which is OK");
            return;
        }

        // Dates syntactily valid
        if (getDateOfArrestDate().getDate() != null) {
            log.debug("Validate Arrest Date");
            getDateOfArrestDate().stepValidate();
        }
        if (getDateOfChargeDate().getDate() != null) {
            log.debug("Validate Charge Date");
            getDateOfChargeDate().stepValidate();
        }

        // Not in future checks
        if (getDateOfArrestDate().getDate() != null) {
            log.debug("Arrest Date Future Check");
            if (getDateOfArrestDate().getDate().getTime().after(currentDate)) {
                JOptionPane.showMessageDialog(this, getString("messageArrestDateInFuture"),
                        getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }
        if (getDateOfChargeDate().getDate() != null) {
            log.debug("Charge Date Future Check");
            if (getDateOfChargeDate().getDate().getTime().after(currentDate)) {
                JOptionPane.showMessageDialog(this, getString("messageChargeDateInFuture"),
                        getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }

        // Not before Offence End Date checks
        if (getDateOfArrestDate().getDate() != null) {
            log.debug("Arrest Date Not Before Offence End Date Check");
            if (endDate != null && endDate.after(getDateOfArrestDate().getDate())) {
                JOptionPane.showMessageDialog(this, getString("messageArrestDateBeforeOffenceEndDate"),
                        getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }
        if (getDateOfChargeDate().getDate() != null) {
            log.debug("Charge Date Not Before Offence End Date Check");
            if (endDate != null && endDate.after(getDateOfChargeDate().getDate())) {
                JOptionPane.showMessageDialog(this, getString("messageChargeDateBeforeOffenceEndDate"),
                        getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }

        // Ensure charge date is on or after arrest date
        if (getDateOfArrestDate().getDate() != null && getDateOfChargeDate().getDate() != null) {
            log.debug("Charge Date On or After Arrest Date Check");
            if (getDateOfArrestDate().getDate().after(getDateOfChargeDate().getDate())) {
                JOptionPane.showMessageDialog(this, getString("messageChargeDateBeforeArrestDate"),
                        getString("messageDateValidationDate"), JOptionPane.ERROR_MESSAGE);
                throw new UserCancelException();
            }
        }
    }

    /**
     * Validate Seq No on Panel.
     * 
     * @throws UserCancelException
     */
    public void validateSeqNo() throws UserCancelException {
        SeqNoHelper.validateSeqNo(this, getSeqNoText(), seqNosList, defOnOffComplexValue);
    }

    public void moveModelToScreen() {

        if (mode == ChargesControllerHelper.MODE.ADD) {
            if (enableOffenceCommittedOnBailPanel(CASE_TYPE_TRIAL.equals(caseType))) {
                getOnBailText().setText("");
            } else {
                log.debug("Is Committed On Bail disabled and null for non Trial cases (Add Count)");
                getOnBailText().setText("");
            }
        } else {
            if (defOnOffComplexValue.getArrestDate() != null)
                getDateOfArrestDate().setDate(defOnOffComplexValue.getArrestDate());

            if (defOnOffComplexValue.getChargeDate() != null)
                getDateOfChargeDate().setDate(defOnOffComplexValue.getChargeDate());

            // Enable Offence Committed radio panel only if Trial Case
            if (enableOffenceCommittedOnBailPanel(CASE_TYPE_TRIAL.equals(caseType))) {
                if (defOnOffComplexValue.getIsCommittedOnBail() == null) {
                    getOnBailText().setText("");
                } else if (defOnOffComplexValue.getIsCommittedOnBail().equals("Y")) {
                    getOnBailText().setText("Y");
                } else if (defOnOffComplexValue.getIsCommittedOnBail().equals("N")) {
                    getOnBailText().setText("N");
                }
            } else {
                log.debug("Is Committed On Bail disabled and null for non Trial cases");
                getOnBailText().setText("");
            }         

            // Seq No.
            if (defOnOffComplexValue.getSeqNo() != null)
                getSeqNoText().setText(defOnOffComplexValue.getSeqNo().toString());
        }
    }

    /**
     * Create DefendantOnOffenceComplexValue from screen fields
     * 
     * @return
     */
    protected DefendantOnOffenceComplexValue createDefendantOnOffenceComplexValue() {

        DefendantOnOffenceComplexValue docv = new DefendantOnOffenceComplexValue();

        return updateDefendantOnOffenceComplexValue(docv);
    }

    /**
     * Update DefendantOnOffenceComplexValue from screen fields
     * 
     * @return
     */
    protected DefendantOnOffenceComplexValue updateDefendantOnOffenceComplexValue(DefendantOnOffenceComplexValue docv) {

        try {
            if (getDateOfArrestDate().getDate() != null) {
                docv.setArrestDate(getDateOfArrestDate().getDate().getTime());
            } else {
                docv.setArrestDate(null);
            }
            if (getDateOfChargeDate().getDate() != null) {
                docv.setChargeDate(getDateOfChargeDate().getDate().getTime());
            } else {
                docv.setChargeDate(null);
            }
        } catch (CSValidationException csve) {
            XHIBITErrorHandler.handleError(csve);
        }

        // Determine IsCommittedOnBail Value

        if (getOnBailText() != null) {
            if (getOnBailText().getText().equals("Y")) {
                docv.setIsCommittedOnBail("Y");
            } else if (getOnBailText().getText().equals("N")) {
                docv.setIsCommittedOnBail("N");
            } else {
                docv.setIsCommittedOnBail(null);
            }
        } else {
            docv.setIsCommittedOnBail(null);
        }

        if (isBreach) {
            if (getSeqNoText().getText() != null && getSeqNoText().getText().length() > 0) {
                docv.setSeqNo(new Integer(getSeqNoText().getText()));
            }
        }
        return docv;
    }

    /**
     * Create DefendantOnOffenceComplexValue from screen fields
     * 
     * @return
     */
    protected DefendantOnOffenceValue createDefendantOnOffenceValue(Integer offenceID, Integer defendantID) {
        if (offenceID == null || defendantID == null) {
            throw new IllegalArgumentException(
                    "createDefendantOnOffenceValue - offenceID and defendantID parameters must contain values");
        }

        DefendantOnOffenceValue dov = new DefendantOnOffenceValue(offenceID, defendantID, null);
        return updateDefendantOnOffenceValue(dov);
    }

    /**
     * Create DefendantOnOffenceComplexValue from screen fields
     * 
     * @return
     */
    protected DefendantOnOffenceValue updateDefendantOnOffenceValue(DefendantOnOffenceValue dov) {
        if (dov == null) {
            throw new IllegalArgumentException("createDefendantOnOffenceValue - dov parameters must contain values");
        }

        try {
            // Defendant On Offence dates

            if (getDateOfArrestDate().getDate() != null) {
                dov.setDateOfArrest(getDateOfArrestDate().getDate());
            } else {
                dov.setDateOfArrest(null);
            }
            if (getDateOfChargeDate().getDate() != null) {
                dov.setDateOfCharge(getDateOfChargeDate().getDate());
            } else {
                dov.setDateOfCharge(null);
            }
        } catch (CSValidationException csve) {
            XHIBITErrorHandler.handleError(csve);
        }

        // Determine IsCommittedOnBail

        if (getOnBailText() != null) {
            if (getOnBailText().getText().equals("Y")) {
                dov.setIsCommittedOnBail("Y");
            } else if (getOnBailText().getText().equals("N")) {
                dov.setIsCommittedOnBail("N");
            } else {
                dov.setIsCommittedOnBail(null);
            }
        } else {
            dov.setIsCommittedOnBail(null);
        }

        if (isBreach && getSeqNoText().getText() != null)
            dov.setSequenceNo(new Integer(getSeqNoText().getText().toString()));

        return dov;
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        parent.stepUpdateViewState();
    }
}
