package uk.gov.courtservice.xhibit.client.importexportnotification;

//java

//Xhibit

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UpperCaseTransformingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: ImportExportNotificationPanel
 * </p>
 * <p>
 * Description: The Panel class for the notification of the import and export
 * statuses.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class ImportExportNotificationPanel extends XPanel {
    // initialise in the constructor
    private XDialog _parent;

    private ImportExportNotificationModel _model;

    // For the tabbs.
    private JTabbedPane _tabbedPane;

    // models for court and case
    private CourtDetailsModel _courtModel;

    private CaseDetailsModel _caseModel;

    // panels for court and case
    private CourtDetailsPanel _courtPanel;

    private CaseDetailsPanel _casePanel;

    private JButton searchBtn;

    private JTextField caseNumberText;

    private JLabel caseNumberLbl;

    // The resouce files that contains title information.
    private String _resources = XhibitBundles.ImportExportNotification;

    private ImportExportNotificationHelper helper = new ImportExportNotificationHelper();

    // For case number validation
    public static final int LENGTH_OF_CASE_NUMBER_TEXT = 9;

    /**
     * Constructor that takes
     * 
     * @param parent
     * @param model
     * @throws CSRecoverableException
     */
    public ImportExportNotificationPanel(XDialog parent, ImportExportNotificationModel model)
            throws CSRecoverableException {
        super();

        _parent = parent;
        _model = model;

        stepInitialise();
        jbInit();
    }

    /**
     * Empty method.
     * 
     * @param update
     *            boolean
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Empty method
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * Empty method
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    /**
     * Set the search button enabled if a case number has been entered.
     */
    public void stepUpdateViewState() {
        getSearchBtn().setEnabled(getCaseNumberText().getText().trim().length() > 0);
    }

    /**
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        stepUpdateViewState();
    }

    /**
     * This will get the court id and case id (if any) from the session and call
     * the delegate to get the import export statuses. It will also populate the
     * case and court panel.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        Integer courtId = null;
        Integer caseId = null;
        ImportExportStatusValue value = null;

        // get the courtid from the session.
        courtId = XhibitSingleton.getInstance().getCourtId();

        // get the caseid from the application controller. Test if we have one
        // first.
        if (_model.getXac() != null && _model.getXac().getApplicationCaseModel() != null
                && _model.getXac().getApplicationCaseModel().getCaseId() != null) {
            caseId = _model.getXac().getApplicationCaseModel().getCaseId();
        }

        // call the delegate to get the statuses.

        value = XhibitDelegateHelper.getImportExportDelegate().getImportExportStatuses(courtId, caseId);

        // set the courtPanel
        setCourtDetailsPanel(value.getCourtStatuses());

        // set the casePanel
        setCaseDetailsPanel(value);
    }

    /**
     * Method to set and populate the case detail panel with data.
     * 
     * @param caseStatuses
     *            ArrayList
     * @throws CSRecoverableException
     */
    private void setCaseDetailsPanel(ImportExportStatusValue value) throws CSRecoverableException {
        // set the case panel
        if (_caseModel == null) {
            _caseModel = new CaseDetailsModel();
            _caseModel.setXac(this._model.getXac());
        }
        _caseModel.setCaseDetails(value.getCaseStatuses());
        _caseModel.setCaseNumber(helper.tidyUp(value.getcaseType()) + helper.tidyUp(value.getcaseNumber()));

        if (_casePanel == null) {
            _casePanel = new CaseDetailsPanel(_parent, _caseModel);
        }
        // the casepanel exist so we just need to refresh it.
        else {
            _casePanel.refresh(_caseModel);
        }
    }

    /**
     * Method to set and populate the court detail panel with data.
     * 
     * @param courtStatuses
     *            ArrayList
     * @throws CSRecoverableException
     */
    private void setCourtDetailsPanel(ArrayList courtStatuses) throws CSRecoverableException {
        // set the courtPanel
        if (_courtModel == null) {
            _courtModel = new CourtDetailsModel();
            _courtModel.setXac(_model.getXac());
        }
        _courtModel.setCourtDetails(courtStatuses);

        if (_courtPanel == null) {
            _courtPanel = new CourtDetailsPanel(_parent, _courtModel);
        }
        // the court panel exists so just refresh with the new data.
        else {
            _courtPanel.refresh(_courtModel);
        }
    }

    /**
     * Build and create the TabbedPane and add the court panel and the case
     * panel to it.
     * 
     * @return JTabbedPane populated with court and case.
     */
    private JTabbedPane getTabbedPane() {
        if (_tabbedPane == null) {
            _tabbedPane = new JTabbedPane();

            // add the court
            _tabbedPane.addTab(XHIBITConstant.getResource(_resources, "lblCourt"), _courtPanel);

            // add the case
            _tabbedPane.addTab(XHIBITConstant.getResource(_resources, "lblCase"), _casePanel);
        }
        return _tabbedPane;
    }

    /**
     * Init method to set up the screen.
     */
    void jbInit() {
        this.setLayout(new GridBagLayout());

        JPanel caseNumberPanel = new JPanel(new GridBagLayout());
        caseNumberPanel.add(getCaseNumberLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        caseNumberPanel.add(getCaseNumberText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        caseNumberPanel.add(getSearchBtn(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        this.add(caseNumberPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getTabbedPane(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
    }

    /**
     * Method to get the case number label
     * 
     * @return JLabel the label to be displayed
     */
    private JLabel getCaseNumberLbl() {
        if (caseNumberLbl == null) {
            caseNumberLbl = new JLabel();
            caseNumberLbl.setText(XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "lblCaseNumber"));
        }
        return caseNumberLbl;
    }

    /**
     * Get the case number and type. This uses the UpperCaseDecorating pattern
     * so all letters will be be upper cases.
     * 
     * @return JTextField
     */
    private JTextField getCaseNumberText() {
        if (caseNumberText == null) {
            UpperCaseTransformingDocumentDecorator uCase = new UpperCaseTransformingDocumentDecorator();
            LimitedTextValidatingDocumentDecorator limit = new LimitedTextValidatingDocumentDecorator(uCase,
                    ImportExportNotificationPanel.LENGTH_OF_CASE_NUMBER_TEXT);
            caseNumberText = JTextFieldFactory.getTextField(limit);
            caseNumberText.setColumns(10);
            caseNumberText.setEnabled(true);
            caseNumberText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent ke) {
                    stepUpdateViewState();
                }
            });
        }
        return caseNumberText;
    }

    /**
     * This will get the search button. If search criteria has been entered and
     * search button pressed then call the delegate to start the search.
     * 
     * @return
     */
    private JButton getSearchBtn() {
        if (searchBtn == null) {
            searchBtn = new JButton();
            searchBtn.setText(XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "lblSearch"));
            searchBtn.setMnemonic(XHIBITConstant.getResource(XhibitBundles.ImportExportNotification, "mnmSearch")
                    .charAt(0));
            searchBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    getTabbedPane().setSelectedIndex(ImportExportNotificationHelper.CASE_TAB);

                    validateCaseNumber();

                    ImportExportStatusValue value = XhibitDelegateHelper.getImportExportDelegate()
                            .getImportExportStatuses(XhibitSingleton.getInstance().getCourtId(),
                                    getCaseNumberText().getText());

                    setCourtDetailsPanel(value.getCourtStatuses());
                    setCaseDetailsPanel(value);

                    if (value.getCaseStatuses().size() == 0) {
                        JOptionPane.showMessageDialog(null, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                                "xs.gen.nomatchestext"), XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                                "xs.gen.nomatchestitle"), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return searchBtn;
    }

    /**
     * Checks that the case number is valid
     * 
     * @throws CSValidationException
     */
    private void validateCaseNumber() throws CSValidationException {
        Object[] params = new Object[] { new Integer(LENGTH_OF_CASE_NUMBER_TEXT - 1) };

        // Case text field must be 9 characters long
        if (getCaseNumberText().getText().length() < LENGTH_OF_CASE_NUMBER_TEXT) {
            getCaseNumberText().requestFocus();
            throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                    "Too few characters entered");
        }

        try {
            // The first character must be alphabetic
            char caseType = getCaseNumberText().getText().charAt(0);
            String caseNumber = getCaseNumberText().getText().substring(1);

            if (!Character.isLetter(caseType)) {
                getCaseNumberText().requestFocus();
                throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                        "Invalid case type");
            }

            // The remaining characters must be numeric
            Long.parseLong(caseNumber);
        } catch (NumberFormatException nfe) {
            getCaseNumberText().requestFocus();
            throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                    "Invalid case number", nfe);
        }
    }
}
