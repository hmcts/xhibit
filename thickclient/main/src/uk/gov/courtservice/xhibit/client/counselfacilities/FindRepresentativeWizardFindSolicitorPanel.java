package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Screen for choosing the legal representative
 * </p>
 * <p>
 * Description: Select the solicitor.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class FindRepresentativeWizardFindSolicitorPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private TitledBorder tb = null;

    private FindLegalRepresentativeModel model = null;

    private ResourceBundle resources = null;

    private FindRepresentativeWizardController controller = null;

    private XWizardDialog parent = null;

    private JLabel fullNameLabel = null;

    private JLabel chambersLabel = null;

    private JTextField fullNameText = null;

    private JTextField chambersText = null;

    private JButton searchBtn = null;

    private JScrollPane resultsScrollPane = null;

    private XTable resultsTable = null;

    private JButton addBtn = null;

    private Collection<FindLegalRepresentativeTableRowModel> results = 
        new Vector<FindLegalRepresentativeTableRowModel>();

    public FindRepresentativeWizardFindSolicitorPanel(XWizardDialog parent,
            FindRepresentativeWizardController controller, FindLegalRepresentativeModel model)
            throws CSRecoverableException {
        super();
        this.parent = parent;
        this.controller = controller;
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Paints the controls on the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());

        this.add(getFullNameLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFullNameText(), new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersText(), new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getResultsScrollPane(), new GridBagConstraints(0, 2, 5, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSearchBtn(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddBtn(), new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JButton getAddBtn() {
        if (addBtn == null) {
            addBtn = new JButton();
            addBtn.setPreferredSize(getSearchBtn().getPreferredSize());
            addBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttAddLegalRep"));
            addBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmAdd").charAt(0));
            addBtn.setEnabled(true);
            addBtn.setActionCommand("ADD");
            addBtn.setText(XHIBITConstant.getResource(resources, "lblAdd"));
            addBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    getResultsTable().clearSelection();
                    
                    AddLegalRepresentativeModel alrModel = new AddLegalRepresentativeModel();
                    alrModel.setXac(controller.getXac());
                    
                    // Using the Xac as the frame removes the "parentFrame not instance"
                    // debug message written by XDialog, but has the side effect that 
                    // status message is blanked on the main screen.
                    // java.awt.Frame frame = model.getXac()
                    java.awt.Frame frame = parent.getParentFrame();
                    AddLegalRepresentativeDialog alrDialog = new AddLegalRepresentativeDialog(frame,
                            alrModel);

                    alrDialog.setVisible(true);

                    if (alrDialog.isOkClicked()) {
                        FindLegalRepresentativeTableRowModel trm = new FindLegalRepresentativeTableRowModel();
                        trm.setLegalRepId(alrModel.getLegalRepId());
                        trm.setFirstName(alrModel.getFirstName());
                        trm.setSurname(alrModel.getSurname());
                        trm.setFullName(alrModel.getFirstName() + " " + alrModel.getSurname());
                        trm.setChambersName(alrModel.getChambers());
                        trm.setAddressLine01(alrModel.getAddressLine01());
                        trm.setAddressLine02(alrModel.getAddressLine02());
                        trm.setTown(alrModel.getTown());
                        trm.setCounty(alrModel.getCounty());
                        trm.setPostCode(alrModel.getPostCode());
                        trm.setChambersId(alrModel.getChambersId());
                        trm.setLegalRepType(CounselFacilitiesHelper.SOLRADIO);

                        results.add(trm);

                        CounselFacilitiesHelper.redisplayTable(getResultsTable(), (Vector) results);
                    }

                    stepUpdateViewState();
                }
            });
        }

        return addBtn;
    }

    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createDefaultTable(new FindLegalRepresentativeTableModel());
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resultsTable.makeSortable();

            ListSelectionModel rowSM = resultsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    try {
                        moveScreenToModel();
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return resultsTable;
    }

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            resultsScrollPane.setBorder(tb);
            resultsScrollPane.getViewport().add(getResultsTable(), null);
            resultsScrollPane.setPreferredSize(new Dimension(500, 312));
        }

        return resultsScrollPane;
    }

    private JLabel getChambersLabel() {
        if (chambersLabel == null) {
            chambersLabel = new JLabel();
            chambersLabel.setText(XHIBITConstant.getResource(resources, "lblChambers"));
        }

        return chambersLabel;
    }

    private JTextField getChambersText() {
        if (chambersText == null) {
            chambersText = new JTextField();
            chambersText.setDocument(new LimitedTextValidatingDocumentDecorator(35));
            chambersText.setToolTipText(XHIBITConstant.getResource(resources, "ttChambers"));
            chambersText.setColumns(20);
            chambersText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return chambersText;
    }

    private JLabel getFullNameLabel() {
        if (fullNameLabel == null) {
            fullNameLabel = new JLabel();
            fullNameLabel.setText(XHIBITConstant.getResource(resources, "lblName"));
        }

        return fullNameLabel;
    }

    private JTextField getFullNameText() {
        if (fullNameText == null) {
            fullNameText = new JTextField();
            fullNameText.setDocument(new LimitedTextValidatingDocumentDecorator(40));
            fullNameText.setToolTipText(XHIBITConstant.getResource(resources, "ttName"));
            fullNameText.setColumns(20);
            fullNameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return fullNameText;
    }

    private JButton getSearchBtn() {
        if (searchBtn == null) {
            searchBtn = new JButton();
            searchBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttSearchLegalRep"));
            searchBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmSearch").charAt(0));
            searchBtn.setEnabled(false);
            searchBtn.setActionCommand("SEARCH");
            searchBtn.setText(XHIBITConstant.getResource(resources, "lblSearch"));
            searchBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    getResultsTable().clearSelection();
                    results = getSolicitorMatches();

                    if (results.size() == 0) {
                        JOptionPane.showMessageDialog(null, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                                "xs.gen.nomatchestext"), XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                                "xs.gen.nomatchestitle"), JOptionPane.INFORMATION_MESSAGE);
                    }
                    CounselFacilitiesHelper.redisplayTable(getResultsTable(), (Vector) results);

                    stepUpdateViewState();
                }
            });
        }

        return searchBtn;
    }

    private Collection<FindLegalRepresentativeTableRowModel> getSolicitorMatches() throws CSRecoverableException {
        Collection<FindLegalRepresentativeTableRowModel> matches = 
            new Vector<FindLegalRepresentativeTableRowModel>();

        SolicitorCriteria criteria = new SolicitorCriteria();
        criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
        criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
        criteria.setCrestSolicitorName(getFullNameText().getText());
        criteria.setSolicitorFirmName(getChambersText().getText());

        try {
            Iterator iter = getBRCDelegate().findSolicitors(criteria).iterator();

            while (iter.hasNext()) {
                SolicitorComplexValue item = (SolicitorComplexValue) iter.next();
                RefSolicitorFirmComplexValue firm = item.getFirm();

                FindLegalRepresentativeTableRowModel trm = new FindLegalRepresentativeTableRowModel();
                trm.setLegalRepId(item.getLegalRepId());
                trm.setFullName(item.getCrestSolicitorName());
                trm.setChambersName(firm.getSolicitorFirmName());
                trm.setAddressLine01(firm.getAddress1());
                trm.setAddressLine02(firm.getAddress2());
                trm.setTown(firm.getTown());
                trm.setCounty(firm.getCounty());
                trm.setPostCode(firm.getPostcode());
                trm.setChambersId(item.getFirmId());
                trm.setLegalRepType(CounselFacilitiesHelper.SOLRADIO);

                matches.add(trm);
            }
        } catch (BisRefControllerException brce) {
            String msgStr = "The search for solicitor failed";
            String msgKey = "gui.counselSignIn.search";
            throw new CSRecoverableException(msgKey, msgStr, brce);
        }

        return matches;
    }

    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return getResultsTable().getSelectedRow() != -1;
    }

    public boolean isSearchable(String data) {
        return (countSearchableCharacters(data) >= CounselFacilitiesHelper.MINIMUM_SEARCHABLE_CHARACTERS ? true : false);
    }

    private int countSearchableCharacters(String data) {
        int total = 0;

        for (int x = 0; x < data.length(); x++) {
            if (data.charAt(x) != '%') {
                total++;
            }
        }

        return total;
    }

    /**
     * Saves the data on the screen to the FindRepresentationWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() throws CSRecoverableException {
        XHIBITConstant.debug("moveScreenToModel");

        FindLegalRepresentativeTableRowModel item = null;

        int x = getResultsTable().getSelectedRow();

        if (x != -1) {
            XHIBITTableModelInterface xstModel = (XHIBITTableModelInterface) getResultsTable().getModel();
            item = (FindLegalRepresentativeTableRowModel) xstModel.getDataAt(x);
        }

        model.setInstructedAdvocateTableRowModel(null);
        model.setFindLegalRepresentativeTableRowModel(item);
    }

    public void clearSelection() throws CSRecoverableException {
        getResultsTable().clearSelection();
        moveScreenToModel();
        stepUpdateViewState();
    }
    
    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
        tb = new TitledBorder(
                BorderFactory.createEtchedBorder(SystemColor.controlHighlight, SystemColor.controlShadow),
                XHIBITConstant.getResource(resources, "lblResults"));
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepDeactivate");
    }

    /**
     * XPanel implementation of life cycle method, called when leaving this
     * screen to validate the data.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     *             if an invalid date is entered.
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepValidate");
        // Nothing to validate
        moveScreenToModel();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepUpdateViewState");

        if (isSearchable(getFullNameText().getText().trim()) || isSearchable(getChambersText().getText().trim())) {
            getSearchBtn().setEnabled(true);
        } else {
            getSearchBtn().setEnabled(false);
        }

        controller.stepUpdateViewState();
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * closed. Note - This is only called if the user has been to the other
     * screens in the wizard, populated the necessary data and then come back to
     * this screen.
     * 
     * @param update
     *            true if the data on the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepDeinitialise");

        if (update) {
            controller.stepDeinitialise();
        }
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[FindRepresentativeWizardFindSolicitorPanel] stepActivate");
        stepUpdateViewState();
    }

    private BisRefControllerBeanBusinessDelegate getBRCDelegate() {
        return XhibitDelegateHelper.getBizRefDelegate();
    }
}