package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Date;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.SearchCounselFacilitiesCriteria;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: FindCounselDefendantPanel
 * </p>
 * <p>
 * Description: The panel for finding a counsel or defendant
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @author Marie Holmberg
 * @version 1.0
 */

public class FindCounselDefendantPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private String resources = XhibitBundles.CounselFacilities;

    private TitledBorder tb = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlHighlight,
            SystemColor.controlShadow), XHIBITConstant.getResource(resources, "lblResults"));

    private GridBagLayout gridBagLayout = new GridBagLayout();

    private JLabel searchTypeLbl = null;

    private ButtonGroup searchTypeRadioGroup = null;

    private JRadioButton counselRadio = null;

    private JRadioButton defendantRadio = null;

    private JLabel firstNameLabel = null;

    private JTextField firstNameText = null;

    private JLabel surnameLabel = null;

    private JTextField surnameText = null;

    private JButton searchBtn = null;

    private JScrollPane resultsScrollPane = null;

    private XTable resultsTable = null;

    private Collection results = new Vector();

    private FindCounselDefendantModel model;

    private OkCancelPanel buttonPanel;

 

    public FindCounselDefendantPanel(XDialog parent, FindCounselDefendantModel model) throws CSRecoverableException {
        super();

        this.model = model;
        this.buttonPanel = (OkCancelPanel) parent.getButtonPanel();

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.add(getSearchTypeLbl(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getCounselRadio(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getDefendantRadio(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getSearchBtn(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getFirstNameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getFirstNameText(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getSurnameLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        searchPanel.add(getSurnameText(), new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new GridBagLayout());
        resultsPanel.add(getResultsScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        this.setLayout(gridBagLayout);
        this.add(searchPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.WEST, new Insets(2, 2, 2, 2), 0, 0));
        this.add(resultsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        getSearchTypeRadioGroup().add(getCounselRadio());
        getSearchTypeRadioGroup().add(getDefendantRadio());
    }

    private JLabel getSearchTypeLbl() {
        if (searchTypeLbl == null) {
            searchTypeLbl = new JLabel();
            searchTypeLbl.setText(XHIBITConstant.getResource(resources, "lblSearchType"));
        }

        return searchTypeLbl;
    }

    private JRadioButton getCounselRadio() {
        if (counselRadio == null) {
            counselRadio = new JRadioButton();
            counselRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblCounsel"));
            counselRadio.setActionCommand(CounselFacilitiesHelper.COURADIO);
            counselRadio.setText(XHIBITConstant.getResource(resources, "lblCounsel"));
            counselRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmCounsel").charAt(0));
            counselRadio.setSelected(true);
            counselRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    couDefRadio_actionPerformed(e);
                }
            });
        }

        return counselRadio;
    }

    private JRadioButton getDefendantRadio() {
        if (defendantRadio == null) {
            defendantRadio = new JRadioButton();
            defendantRadio.setToolTipText(XHIBITConstant.getResource(resources, "lblDefendant"));
            defendantRadio.setActionCommand(CounselFacilitiesHelper.DEFRADIO);
            defendantRadio.setText(XHIBITConstant.getResource(resources, "lblDefendant"));
            defendantRadio.setMnemonic(XHIBITConstant.getResource(resources, "mnmDefendant").charAt(0));
            defendantRadio.setSelected(false);
            defendantRadio.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    couDefRadio_actionPerformed(e);
                }
            });
        }

        return defendantRadio;
    }

    private void couDefRadio_actionPerformed(@SuppressWarnings("unused") ActionEvent e) {
        clearScreen();

        stepUpdateViewState();
    }

    private ButtonGroup getSearchTypeRadioGroup() {
        if (searchTypeRadioGroup == null) {
            searchTypeRadioGroup = new ButtonGroup();
        }

        return searchTypeRadioGroup;
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
                    doSearch();
                    stepUpdateViewState();
                    resizeColumns();
                }
            });
        }

        return searchBtn;
    }

    /**
     * Method to search for counsel or defendant depending on users selection.
     * 
     * @throws CSRecoverableException
     */
    private void doSearch() throws CSRecoverableException {
        // if the user has selected to search for counsels
        if (getSearchTypeRadioGroup().isSelected(getCounselRadio().getModel())) {
            results = getCounselMatches();
        }
        // else the user has selected to search for defendants
        else {
            results = getDefendantMatches();
        }

        // populating the model with the search result
        getResultsTable()
                .setModel(
                        new FindCounselDefendantTableModel(results, getSearchTypeRadioGroup().getSelection()
                                .getActionCommand()));

        // Set the counsel and defendant column to be multilined.
        setMultiLineColumn(FindCounselDefendantTableModel.COUNSEL);
        setMultiLineColumn(FindCounselDefendantTableModel.DEFENDANT);

        // Save the search results in the model
        model.setSearchResults(results);

        // If there is no search result a message will be displayed
        if (results.size() == 0) {
            JOptionPane.showMessageDialog(null, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                    "xs.gen.nomatchestext"), XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                    "xs.gen.nomatchestitle"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This will set the passed in column to be multilined
     * 
     * @param column
     *            the int of the column that should be set to be multilined.
     */
    private void setMultiLineColumn(int column) {
        getResultsTable().getColumnModel().getColumn(column).setCellRenderer(
                XTableFactory.getInstance().getMultiLineCellRenderer(getResultsTable()));
    }

    /**
     * This will resize all the columns to best use the space
     * 
     */
    private void resizeColumns() {
        TableColumn column = null;

        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.COURTROOM);
        column.setPreferredWidth(88);
        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.CASE_NUMBER);
        column.setPreferredWidth(88);
        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.TIME);
        column.setPreferredWidth(50);
        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.DEFENDANT);
        column.setPreferredWidth(198);
        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.LEGAL_ROLE);
        column.setPreferredWidth(78);
        column = getResultsTable().getColumnModel().getColumn(FindCounselDefendantTableModel.COUNSEL);
        column.setPreferredWidth(198);
    }

    /**
     * This will set the minimum criteria to be searched for. Can be used for
     * both counsel and defendant search.
     * 
     * @return SearchCounselFacilitiesCriteria - prepopulated
     */
    private SearchCounselFacilitiesCriteria prePopulateSearchCriteria() {
        SearchCounselFacilitiesCriteria criteria = new SearchCounselFacilitiesCriteria();

        criteria.setCourtId(XhibitSingleton.getInstance().getCourtId());
        criteria.setScheduleDate(new Date());
        criteria.setFirstName(getFirstNameText().getText().trim());
        criteria.setSurname(getSurnameText().getText().trim());

        return criteria;
    }

    /**
     * Search for the counsel with user entered criteria and sort the list by
     * courtroom and case
     * 
     * @return Collection of counsels
     * @throws CSRecoverableException
     */
    private Collection getCounselMatches() throws CSRecoverableException {
        // get the list of counsels and sort it.
        Collection counselPartyOnCaseList = getCFCDelegate().searchForCounsel(prePopulateSearchCriteria());
        CounselFacilitiesHelper.sortByCourtRoom(counselPartyOnCaseList);
        return counselPartyOnCaseList;
    }

    /**
     * Search for Defendants with given user criteria and sort the list by
     * courtroom and case
     * 
     * @return Collection of defendants
     * @throws CSRecoverableException
     */
    private Collection getDefendantMatches() throws CSRecoverableException {
        // get the list of defendants and sort it.
        Collection defendantPartyOnCaseList = getCFCDelegate().searchForDefendant(prePopulateSearchCriteria());
        CounselFacilitiesHelper.sortByCourtRoom(defendantPartyOnCaseList);
        return defendantPartyOnCaseList;
    }

    /**
     * This will return a label with the appropriate text.
     * 
     * @return
     */
    private JLabel getFirstNameLabel() {
        if (firstNameLabel == null) {
            firstNameLabel = new JLabel();
            firstNameLabel.setText(XHIBITConstant.getResource(resources, "lblFirstName"));
        }

        return firstNameLabel;
    }

    /**
     * This method will return a text field for the first name. It also adds a
     * key listener to the field.
     * 
     * @return JTextField
     */
    private JTextField getFirstNameText() {
        if (firstNameText == null) {
            firstNameText = new JTextField();
            firstNameText.setColumns(30);
            firstNameText.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            firstNameText.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            firstNameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return firstNameText;
    }

    /**
     * Method to set the appropriate text of the label.
     * 
     * @return JLabel
     */
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
            surnameText.setColumns(30);
            surnameText.setPreferredSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            surnameText.setMinimumSize(new Dimension(300, XHIBITConstant.getLineHeight()));
            surnameText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return surnameText;
    }

    /**
     * Get the JScrollPane. This will also set the size of the Pane.
     * 
     * @return JScrollPane
     */
    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            resultsScrollPane.setBorder(tb);
            resultsScrollPane.getViewport().add(getResultsTable(), null);
            resultsScrollPane.setPreferredSize(new Dimension(700, 434));
        }
        return resultsScrollPane;
    }

    /**
     * Returns the table that the result will be displayed in.
     * 
     * @return XTable
     */
    private XTable getResultsTable() {
        if (resultsTable == null) {
            FindCounselDefendantTableModel xsortmodel = new FindCounselDefendantTableModel(true);

            resultsTable = XTableFactory.getInstance().createMultiLineTable(xsortmodel);
            resultsTable.makeSortable(false);
            resultsTable.getTableHeader().setReorderingAllowed(false);
        }
        return resultsTable;
    }

    public void stepInitialise() throws CSRecoverableException {
        // empty
    }

    public void stepActivate() throws CSRecoverableException {
        stepUpdateViewState();
    }

    /**
     * This method is setting the tooptip text for defendant and counsels. It
     * also enables and disables any screen components.
     */
    public void stepUpdateViewState() {
        enableTextField(getFirstNameText(), true);
        enableTextField(getSurnameText(), true);

        if (getSearchTypeRadioGroup().isSelected(getDefendantRadio().getModel())) {
            firstNameText.setToolTipText(XHIBITConstant.getResource(resources, "ttDefendantFirstName"));
            getSurnameText().setToolTipText(XHIBITConstant.getResource(resources, "ttDefendantSurname"));
        } else {
            getFirstNameText().setToolTipText(XHIBITConstant.getResource(resources, "ttFirstName"));
            getSurnameText().setToolTipText(XHIBITConstant.getResource(resources, "ttSurname"));
        }

        getSearchBtn().setEnabled(isMandatoryFieldsCompleted());

        buttonPanel.okButton.setEnabled(true);
    }

    /**
     * Checks so that the mandatory fields have been completed, which are: -
     * selection of defence or counsel radio button - firstname, surname: One of
     * the two must be entered as a minimum.
     * 
     * @return boolean
     */
    private boolean isMandatoryFieldsCompleted() {
        boolean result = true;

        if (getSearchTypeRadioGroup().getSelection() == null) {
            result = false;
        } else {
            if (getFirstNameText().getText().trim().length() == 0 && getSurnameText().getText().trim().length() == 0) {
                result = false;
            }
        }
        return result;
    }

    public void stepValidate() throws CSRecoverableException {
        // empty
    }

    public void stepDeactivate() throws CSRecoverableException {
        // empty
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            // empty
        }
    }

    /**
     * Clear the follwoing fields on the screen: - firstname - surname - search
     * table
     */
    private void clearScreen() {
        getFirstNameText().setText("");
        getSurnameText().setText("");
        getResultsTable().setModel(new FindCounselDefendantTableModel(false));
    }

    public JComponent getFirstEnterableComponent() {
        return getCounselRadio();
    }

    private void enableTextField(JTextField textField, boolean state) {
        textField.setEnabled(state);
        textField.setEditable(state);
        textField.setBackground((state ? SystemColor.white : SystemColor.text));
    }

    /**
     * Get a delegate for the counsel facility
     * 
     * @return CounselFacilitiesBusinessDelegate
     */
    private CounselFacilitiesControllerBeanBusinessDelegate getCFCDelegate() {
        return XhibitDelegateHelper.getCounselFacilitiesDelegate();
    }

}
