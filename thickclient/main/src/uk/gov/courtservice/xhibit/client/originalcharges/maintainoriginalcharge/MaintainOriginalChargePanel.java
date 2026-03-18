package uk.gov.courtservice.xhibit.client.originalcharges.maintainoriginalcharge;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.originalcharges.OriginalChargesHelper;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;

public class MaintainOriginalChargePanel extends XPanel {
    
    // Parameters passed to the contructor
    private XDialog parent;
    private MaintainOriginalChargeModel model;
    
    // Screen widgets
    private JTextField defendantName = null;
    private JScrollPane originalChargeScrollPane = null;
    private JTextArea originalCharge = null;
    private JTextField sequenceNumber = null;
    private OkCancelPanel okCancelPanel;
    
    /**
     * Constructor
     * @param parent - the XDialog on which this panel sits
     * @param model - used to pass data to and from the invoking screen
     * @throws CSRecoverableException
     */
    public MaintainOriginalChargePanel(XDialog parent, MaintainOriginalChargeModel model)
    throws CSRecoverableException
    {
        super();
        this.model = model;
        this.parent = parent;
        this.okCancelPanel = (OkCancelPanel)parent.getButtonPanel();
        
        stepInitialise();
        init();
    }

    /**
     * Life-cycle method called in the constructor.
     */
    public void stepInitialise() throws CSRecoverableException {
        // NoAction
    }
        
    /**
     * Build the screen adding all widgets to the main panel 
     */
    private void init() {
        this.setLayout(new GridBagLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        
        // Defendant name
        mainPanel.add(
            new JLabel(OriginalChargesHelper.getResource("defendantPromptLbl")),
            new GridBagConstraints(
                0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        mainPanel.add(
            getDefendantName(), 
            new GridBagConstraints(
                1, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, 
                XHIBITConstant.nonContainerInsets, 0, 0));

        // Original charge
        mainPanel.add(
            new JLabel(OriginalChargesHelper.getResource("originalChargePromptLbl")),
            new GridBagConstraints(
                0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        mainPanel.add(
            getOriginalChargeScrollPane(), 
            new GridBagConstraints(
                1, 1, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        
        // Sequence number
        mainPanel.add(
            new JLabel(OriginalChargesHelper.getResource("seqNoPromptLbl")),
            new GridBagConstraints(
                0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));
        mainPanel.add(
            getSequenceNumber(), 
            new GridBagConstraints(
                1, 2, 1, 1, 1.0, 1.0, 
                GridBagConstraints.WEST,
                GridBagConstraints.NONE, 
                XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(
            mainPanel, 
            new GridBagConstraints(
                0, 0, 1, 1, 1.0, 1.0, 
                GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, 
                XHIBITConstant.nonContainerInsets, 0, 0));
    }

    /**
     * The name of the defendant for whom the original charge is to be added
     * @return JTextField
     */
    private JTextField getDefendantName() {
        if (defendantName == null) {
            defendantName = new JTextField();
            defendantName.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            defendantName.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            defendantName.setColumns(30);
            enableTextField(defendantName, false);
        }

        return defendantName;
    }

    /**
     * The scroll pane for the original charge text 
     * @return JScrollPane
     */
    private JScrollPane getOriginalChargeScrollPane() {
        if( originalChargeScrollPane == null ) {
            originalChargeScrollPane = new JScrollPane();
            originalChargeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            originalChargeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            originalChargeScrollPane.getViewport().add(getOriginalCharge(), null);
            originalChargeScrollPane.setPreferredSize(new Dimension(200, XHIBITConstant.getLineHeight() * 7));
        }

        return originalChargeScrollPane;
    }

    /**
     * The text of the original charge
     * @return JTextArea
     */
    private JTextArea getOriginalCharge() {
        if (originalCharge == null) {
            Document doc = DocumentFactory.newDocument(
                new Capability[] { Capability.utf8LimitedTextCapability(240) }
            );
            originalCharge = JTextAreaFactory.getTextArea(doc);
            originalCharge.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return originalCharge;
    }

    /**
     * The sequence number of the original charge
     * @return JTextField
     */
    private JTextField getSequenceNumber() {
        if (sequenceNumber == null) {
            Document doc = DocumentFactory.newDocument(
                new Capability[] { Capability.numeric(), Capability.limitedText(3) }
            );
            sequenceNumber = JTextFieldFactory.getTextField(doc);
            sequenceNumber.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            sequenceNumber.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            sequenceNumber.setColumns(5);
            sequenceNumber.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
            enableTextField(sequenceNumber, true);
        }

        return sequenceNumber;
    }
    
    /**
     * Life-cycle method.
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
        
        stepUpdateViewState();
        
        //initially OK button disabled until the user changes a field and all mandatory fields are filled
        this.okCancelPanel.okButton.setEnabled(false);
    }
    
    /**
     * Pseudo life-cycle method to move data from the model to the screen
     */
    private void moveModelToScreen() {
        getDefendantName().setText(model.getChargesTableRowModel().getDefendantOnCaseVO().getDisplayableName());

        getDefaultSequenceNumber();
        
        if( model.inEditMode() ) {
            getOriginalCharge().setText(model.getChargesTableRowModel().getChargeVO().getCrestOffenceFreetext());
            
            getSequenceNumber().setText(
                model.getChargesTableRowModel().getChargeVO().getSeqNo() == null ? 
               "" : 
                String.valueOf(model.getChargesTableRowModel().getChargeVO().getSeqNo())
            );
            
            
        }
    }

    /**
     * Sets the sequence number text field to the next available sequence number 
     */
    private void getDefaultSequenceNumber() {     
        SeqNoHelper.processSeqNoChange(this, getSequenceNumber(), model.getUsedSequenceNumbers(), null);
       
    }

    /**
     * Life-cycle method.
     */
    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();
    }
    
    /**
     * Pseudo life-cycle method to copy data from the screen to the model
     */
    private void moveScreenToModel() {
        model.getChargesTableRowModel().getChargeVO().setCrestOffenceFreetext(getOriginalCharge().getText());
        model.getChargesTableRowModel().getChargeVO().setSeqNo(new Integer(getSequenceNumber().getText()));
    }

    /**
     * Life-cycle method.
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // NoAction
    }

    /**
     * Life-cycle method to enable/disable controls depending on the state of the data
     */
    public void stepUpdateViewState() {
        okCancelPanel.okButton.setEnabled(isMandatoryFieldsComplete());
        okCancelPanel.cancelButton.setEnabled(true);
    }
    
    /**
     * Determines if mandatory fields have been complete
     * @return true - if mandatory fields have been completed otherwise returns false
     */
    private boolean isMandatoryFieldsComplete() {
        return getOriginalCharge().getText().trim().length() > 0
            && getSequenceNumber().getText().length()        > 0;
    }

    /**
     * Life-cycle method.
     * Ensures that the sequence number value is with acceptable range and that
     * it is NOT a value that has already been used for the defendant on case.
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException
    {
        // Ensure the sequence number is > 0
        if( new Integer(getSequenceNumber().getText()).intValue() == 0 ) {
            throw new CSValidationException(
               "validation.mininclusive",
                new Object[]{ OriginalChargesHelper.getResource("seqNoLbl"), "1" },
               "Sequence number must be greater than or equal to 1"
            );
        }
        
        
        //Always validate the sequence number unless we are in edit mode & the user has not changed the seqNo
        if( model.inEditMode()
                &&  model.getChargesTableRowModel().getChargeVO().getSeqNo().toString().equalsIgnoreCase(getSequenceNumber().getText()) ) {
                    // NoAction because user is editing record & has not changed seqNo
        }else{
            SeqNoHelper.validateSeqNo(this, getSequenceNumber(), model.getUsedSequenceNumbers(), null);
        }
    }
    
    /**
     * Convenience method to enable/disable text fields
     * @param textField - the JTextField to enable
     * @param state - true if the field is to be enabled
     */
    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? Color.white : this.getBackground()));
    }
}
