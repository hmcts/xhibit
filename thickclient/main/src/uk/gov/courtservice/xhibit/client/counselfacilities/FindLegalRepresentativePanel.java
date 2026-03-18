package uk.gov.courtservice.xhibit.client.counselfacilities;

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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
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
 * Title: FindLegalRepresentativePanel
 * </p>
 * <p>
 * Description: Panel for finding legal representatives
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class FindLegalRepresentativePanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private static final String resources = XhibitBundles.CounselFacilities;

    private final TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlHighlight,
            SystemColor.controlShadow), XHIBITConstant.getResource(resources, "lblResults"));

    private JLabel repTypeLbl = null;

    private ButtonGroup legalRepRadioGroup = null;

    private JRadioButton barristerRadio = null;

    private JRadioButton solicitorRadio = null;

    private JRadioButton inPersonRadio = null;
    
    private JRadioButton nonAttendanceRadio = null;

    private JLabel fullNameLabel = null;

    private JLabel firstNameLabel = null;

    private JLabel surnameLabel = null;

    private JLabel chambersLabel = null;

    private JTextField fullNameText = null;

    private JTextField firstNameText = null;

    private JTextField surnameText = null;

    private JTextField chambersText = null;

    private JButton searchBtn = null;

    private JScrollPane resultsScrollPane = null;

    private XTable resultsTable = null;

    private JButton addBtn = null;

    private Collection<FindLegalRepresentativeTableRowModel> results = 
        new Vector<FindLegalRepresentativeTableRowModel>();

    // only to be set from the constructor...
    private final XDialog parent;

    private final FindLegalRepresentativeModel model;

    private final OkCancelPanel buttonPanel;

    public FindLegalRepresentativePanel(XDialog parent, FindLegalRepresentativeModel model)
            throws CSRecoverableException {
        super();

        this.parent = parent;
        this.model = model;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
        stepActivate();
    }

    void jbInit() {
        this.setLayout(new GridBagLayout());

        this.add(getRepTypeLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getBarristerRadio(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSolicitorRadio(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getInPersonRadio(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getNonAttendanceRadio(), new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSearchBtn(), new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFullNameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFullNameText(), new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFirstNameLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFirstNameText(), new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameText(), new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersText(), new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getResultsScrollPane(), new GridBagConstraints(1, 5, 4, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getAddBtn(), new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        getLegalRepRadioGroup().add(getBarristerRadio());
        getLegalRepRadioGroup().add(getSolicitorRadio());
        getLegalRepRadioGroup().add(getInPersonRadio());
        getLegalRepRadioGroup().add(getNonAttendanceRadio());
    }

    private JLabel getRepTypeLbl() {
        if (repTypeLbl == null) {
            repTypeLbl = new JLabel();
            repTypeLbl.setText(XHIBITConstant.getResource(resources, "lblRepType"));
        }

        return repTypeLbl;
    }

    private JRadioButton getBarristerRadio() {
        if (barristerRadio == null) {
            barristerRadio = new JRadioButton();
            barristerRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblBarrister"));
            barristerRadio.setActionCommand(CounselFacilitiesHelper.BARRADIO);
            barristerRadio.setText(XHIBITConstant.getResource(resources, "lblBarrister"));
            barristerRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmBarrister").charAt(0));
            barristerRadio.setSelected(false);
            barristerRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    legalRepRadioSelection_actionPerformed(e);
                }
            });
        }

        return barristerRadio;
    }

    private JRadioButton getSolicitorRadio() {
        if (solicitorRadio == null) {
            solicitorRadio = new JRadioButton();
            solicitorRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblSolicitor"));
            solicitorRadio.setActionCommand(CounselFacilitiesHelper.SOLRADIO);
            solicitorRadio.setText(XHIBITConstant.getResource(resources, "lblSolicitor"));
            solicitorRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmSolicitor").charAt(0));
            solicitorRadio.setSelected(false);
            solicitorRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    legalRepRadioSelection_actionPerformed(e);
                }
            });
        }

        return solicitorRadio;
    }

    private JRadioButton getInPersonRadio() {
        if (inPersonRadio == null) {
            inPersonRadio = new JRadioButton();
            inPersonRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblInPerson"));
            inPersonRadio.setActionCommand(CounselFacilitiesHelper.INPRADIO);
            inPersonRadio.setText(XHIBITConstant.getResource(resources, "lblInPerson"));
            inPersonRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmInPerson").charAt(0));
            inPersonRadio.setSelected(false);
            inPersonRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    legalRepRadioSelection_actionPerformed(e);
                }
            });
        }

        return inPersonRadio;
    }
    
    private JRadioButton getNonAttendanceRadio() {
        if (nonAttendanceRadio == null) {
            nonAttendanceRadio = new JRadioButton();
            nonAttendanceRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblNonAttendance"));
            nonAttendanceRadio.setActionCommand(CounselFacilitiesHelper.NONATTRADIO);
            nonAttendanceRadio.setText(XHIBITConstant.getResource(resources, "lblNonAttendance"));
            nonAttendanceRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmNonAttendance").charAt(0));
            nonAttendanceRadio.setSelected(false);
            nonAttendanceRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    legalRepRadioSelection_actionPerformed(e);
                }
            });
        }

        return nonAttendanceRadio;
    }

    private void legalRepRadioSelection_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        clearScreen();

        stepUpdateViewState();
    }

    private ButtonGroup getLegalRepRadioGroup() {
        if (legalRepRadioGroup == null) {
            legalRepRadioGroup = new ButtonGroup();
        }

        return legalRepRadioGroup;
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
                    if (getLegalRepRadioGroup().isSelected(getBarristerRadio().getModel())) {
                        results = getAdvocateMatches();
                    } else {
                        results = getSolicitorMatches();
                    }

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

    /**
     * Search for advocates with given user input.
     * 
     * @return Collection
     */
    private Collection<FindLegalRepresentativeTableRowModel> getAdvocateMatches() 
    throws CSRecoverableException {
        Collection<FindLegalRepresentativeTableRowModel> matches = 
            new Vector<FindLegalRepresentativeTableRowModel>();

        RefAdvocateCriteria criteria = new RefAdvocateCriteria();
        criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
        criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
        criteria.setFirstName(getFirstNameText().getText());
        criteria.setSurname(getSurnameText().getText());
        criteria.setChamberFirmName(getChambersText().getText());
        try {
            Iterator iter = getBRCDelegate().findAdvocates(criteria).iterator();

            while (iter.hasNext()) {
                // Only call accessor methods on the RefAdvocateComplexValue
                RefAdvocateComplexValue item = (RefAdvocateComplexValue) iter.next();

                FindLegalRepresentativeTableRowModel trm = new FindLegalRepresentativeTableRowModel();
                trm.setLegalRepId(item.getLegalRepId());
                trm.setTitle(item.getTitle());
                trm.setFirstName(item.getFirstName());
                trm.setSurname(item.getSurname());
                trm.setFullName(CounselFacilitiesHelper.getSurnameFirstName(item.getFirstName(), item.getSurname()));
                trm.setChambersName(item.getFirmName());
                trm.setAddressLine01(item.getAddress1());
                trm.setAddressLine02(item.getAddress2());
                trm.setTown(item.getTown());
                trm.setCounty(item.getCounty());
                trm.setPostCode(item.getPostcode());
                trm.setChambersId(item.getRefChamberId());
                trm.setLegalRepType(getLegalRepRadioGroup().getSelection().getActionCommand());

                matches.add(trm);
            }
        } catch (BisRefControllerException brce) {
            String msgStr = "The search for counsel failed";
            String msgKey = "gui.counselSignIn.search";
            CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, brce);
            throw (csre);
        }

        return matches;
    }

    /**
     * search for solicitors for given user input.
     * 
     * @return Collection
     */
    private Collection<FindLegalRepresentativeTableRowModel> getSolicitorMatches() 
    throws CSRecoverableException {
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
                trm.setLegalRepType(getLegalRepRadioGroup().getSelection().getActionCommand());

                matches.add(trm);
            }
        } catch (BisRefControllerException brce) {
            String msgStr = "The search for solicitor failed";
            String msgKey = "gui.counselSignIn.search";
            throw new CSRecoverableException(msgKey, msgStr, brce);
        }

        return matches;
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
                    stepUpdateViewState();
                }
            });
            enableTextField(fullNameText, false);
        }

        return fullNameText;
    }

    private JLabel getFirstNameLabel() {
        if (firstNameLabel == null) {
            firstNameLabel = new JLabel();
            firstNameLabel.setText(XHIBITConstant.getResource(resources, "lblFirstName"));
        }

        return firstNameLabel;
    }

    private JTextField getFirstNameText() {
        if (firstNameText == null) {
            firstNameText = new JTextField();
            firstNameText.setDocument(new LimitedTextValidatingDocumentDecorator(35));
            firstNameText.setToolTipText(XHIBITConstant.getResource(resources, "ttFirstName"));
            firstNameText.setColumns(20);
            firstNameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
            enableTextField(firstNameText, false);
        }

        return firstNameText;
    }

    private JLabel getSurnameLabel() {
        if (surnameLabel == null) {
            surnameLabel = new JLabel();
            surnameLabel.setText(XHIBITConstant.getResource(resources, "lblSurname"));
        }

        return surnameLabel;
    }

    private JTextField getSurnameText() {
        if (surnameText == null) {
            surnameText = new JTextField();
            surnameText.setDocument(new LimitedTextValidatingDocumentDecorator(35));
            surnameText.setToolTipText(XHIBITConstant.getResource(resources, "ttSurname"));
            surnameText.setColumns(20);
            surnameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
            enableTextField(surnameText, false);
        }

        return surnameText;
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
                    stepUpdateViewState();
                }
            });
            enableTextField(chambersText, false);
        }

        return chambersText;
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

                    stepUpdateViewState();
                }
            });
        }

        return resultsTable;
    }

    private JButton getAddBtn() {
        if (addBtn == null) {
            addBtn = new JButton();
            addBtn.setPreferredSize(getSearchBtn().getPreferredSize());
            addBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttAddLegalRep"));
            addBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmAdd").charAt(0));
            addBtn.setEnabled(false);
            addBtn.setActionCommand("ADD");
            addBtn.setText(XHIBITConstant.getResource(resources, "lblAdd"));
            addBtn.addActionListener(new XAction() {

                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    AddLegalRepresentativeModel alrModel = new AddLegalRepresentativeModel();
                    alrModel.setXac(model.getXac());
                    AddLegalRepresentativeDialog alrDialog = new AddLegalRepresentativeDialog(parent.getParentFrame(),
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
                        trm.setLegalRepType(getLegalRepRadioGroup().getSelection().getActionCommand());

                        results.add(trm);

                        CounselFacilitiesHelper.redisplayTable(getResultsTable(), (Vector) results);
                    }

                    stepUpdateViewState();
                }
            });
        }

        return addBtn;
    }

    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    public void stepActivate() throws CSRecoverableException {
        stepUpdateViewState();
    }

    public void stepUpdateViewState() {
        boolean barSelected = false;
        boolean solSelected = false;
        boolean inpSelected = false;
        boolean nonAttSelected = false;

        if (getLegalRepRadioGroup().getSelection() != null) {
            barSelected = getLegalRepRadioGroup().isSelected(getBarristerRadio().getModel());
            solSelected = getLegalRepRadioGroup().isSelected(getSolicitorRadio().getModel());
            inpSelected = getLegalRepRadioGroup().isSelected(getInPersonRadio().getModel());
            nonAttSelected = getLegalRepRadioGroup().isSelected(getNonAttendanceRadio().getModel());
        }

        getInPersonRadio().setEnabled(!model.isDisableInPerson());
        getNonAttendanceRadio().setEnabled(!model.isDisableNonAttendance());

        enableTextField(getFullNameText(), solSelected);
        enableTextField(getFirstNameText(), barSelected);
        enableTextField(getSurnameText(), barSelected);
        enableTextField(getChambersText(), barSelected || solSelected);

        getAddBtn().setEnabled(solSelected);

        getSearchBtn().setEnabled(isMandatoryFieldsCompleted());

        buttonPanel.okButton.setEnabled(resultsTable.getSelectedRowCount() > 0 || inpSelected || nonAttSelected);
    }

    /**
     * Check if the following mandatory fields have been entered: - Barristers:
     * one of the following has to be entered firstname, surname, chamber -
     * Solicitors: either name or firm needs to be entered.
     * 
     * @return boolean
     */
    private boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (getLegalRepRadioGroup().getSelection() == null) {
            result = false;
        } else {
            if (getLegalRepRadioGroup().getSelection().getActionCommand() == CounselFacilitiesHelper.BARRADIO) {
                if (getFirstNameText().getText().trim().length() == 0
                        && getSurnameText().getText().trim().length() == 0
                        && getChambersText().getText().trim().length() == 0) {
                    result = false;
                }
            } else {
                if (getFullNameText().getText().trim().length() == 0
                        && getChambersText().getText().trim().length() == 0) {
                    result = false;
                }
            }
        }

        if (isSearchable(getFullNameText().getText().trim()) || isSearchable(getFirstNameText().getText().trim())
                || isSearchable(getSurnameText().getText().trim()) || isSearchable(getChambersText().getText().trim())) {
            // NoAction
        } else {
            result = false;
        }

        return result;
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

    public void stepValidate() throws CSRecoverableException {
        // empty
    }

    public void stepDeactivate() throws CSRecoverableException {
        moveScreenToModel();

        model.printModel();
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // empty
    }

    private void moveScreenToModel() {
        final FindLegalRepresentativeTableRowModel item;

        if (getLegalRepRadioGroup().isSelected(getInPersonRadio().getModel())) {
            item = new FindLegalRepresentativeTableRowModel();
            item.setFullName(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, ("lblInPerson")));
            item.setLegalRepType(getLegalRepRadioGroup().getSelection().getActionCommand());
        } else if (getLegalRepRadioGroup().isSelected(getNonAttendanceRadio().getModel())) {
            item = new FindLegalRepresentativeTableRowModel();
            item.setFullName(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, ("lblNonAttendance")));
            item.setLegalRepType(getLegalRepRadioGroup().getSelection().getActionCommand());
        } else {
            int x = getResultsTable().getSelectedRow();
            XHIBITTableModelInterface xstModel = (XHIBITTableModelInterface) getResultsTable().getModel();
            item = (FindLegalRepresentativeTableRowModel) xstModel.getDataAt(x);
        }

        model.setRepTypeRadio(getLegalRepRadioGroup().getSelection().getActionCommand());
        model.setFindLegalRepresentativeTableRowModel(item);
    }

    private void clearScreen() {
        getFullNameText().setText("");
        getFirstNameText().setText("");
        getSurnameText().setText("");
        getChambersText().setText("");

        CounselFacilitiesHelper.redisplayTable(getResultsTable(), new Vector());
    }

    public JComponent getFirstEnterableComponent() {
        return getBarristerRadio();
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        // If field is disabled set the colour to the same as the panel
        // background
        // This is not the Windows default (white with grey text)
        // but is less confusing for the user.
        textField.setBackground((state ? Color.white : this.getBackground()));
    }

    private BisRefControllerBeanBusinessDelegate getBRCDelegate() {
        return XhibitDelegateHelper.getBizRefDelegate();
    }
}
