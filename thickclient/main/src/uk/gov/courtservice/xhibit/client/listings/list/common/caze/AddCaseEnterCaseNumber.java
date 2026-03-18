package uk.gov.courtservice.xhibit.client.listings.list.common.caze;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.client.util.WizardButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * This class produced the screen to add a case.
 * Based on AddHearingEnterCaseNumber.
 * 
 * @author westalll
 *
 */
public class AddCaseEnterCaseNumber extends XPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = CSServices.getLogger(AddCaseEnterCaseNumber.class);
    private static final int ROWS_TO_SHOW = 4;
    private final Dimension lbl2Dim = new Dimension(100, 17);
    private final Dimension listDim = new Dimension(350, XHIBITConstant.getLineHeight() * ROWS_TO_SHOW);

    // only to be set in the constructor...
    private final AddCaseDataModel model;
    private final WizardButtonPanel buttonPanel;
    private final List<PullDownListObject> allRefHearingTypes = new ArrayList<PullDownListObject>();
    private final Vector top5RefHearingTypes = new Vector();
    private final Vector distinctRefHearingTypes = new Vector();
    private HashMap refHearingTypesHashMap;
    private JLabel caseTitleLbl;
    private JTextField hearingTypeDescTxt;
    private JTextField hearingTypeCodeTxt;    
    private JTextField caseTitleText;
    private JPanel hearingTypesPanel;
    private JPanel hearingTypeCodePanel;
    private JPanel hearingTypeFilterPanel;
    private JScrollPane hearingTypesScrollPane;
    private JList hearingTypesList;
    private JButton verifyCodeBtn;
    private JToggleButton allTypesBtn;
    private JToggleButton top5TypesBtn;
    private ButtonGroup hearingTypesBtnGrp;


    /**
     * Constructor that takes in the model for this dialog and a reference to
     * the button panel
     * 
     * @param model
     * @param buttonPanel
     * @param caseType 
     * @throws CSRecoverableException
     */
    public AddCaseEnterCaseNumber(AddCaseDataModel model, WizardButtonPanel buttonPanel, final String caseType)
            throws CSRecoverableException {
        this.model = model;
        model.setCaseType(caseType);
        this.buttonPanel = buttonPanel;

        stepInitialise();
        jbInit();
        stepActivate();
    }
    


    /**
     * Add widgets to the screen
     */
    private void jbInit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - jbInit ");
        }

        this.setLayout(new GridBagLayout());

        hearingTypesPanel = new JPanel(new GridBagLayout());
        hearingTypeCodePanel = new JPanel(new GridBagLayout());
        hearingTypeFilterPanel = new JPanel(new GridBagLayout());

        // add the items to the panel
        final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);


        // Hearing type code
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(createLabel("AddHearingHearingTypeCodeLabel", lbl2Dim), gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        hearingTypeCodePanel.add(getHearingTypeCodeTxt(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        hearingTypeCodePanel.add(getVerifyCodeBtn(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        this.add(hearingTypeCodePanel, gbc);

        // Hearing types description and list
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(4, 4, 4, 4);
        this.add(createLabel("AddHearingHearingTypeDescLabel", lbl2Dim), gbc);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        hearingTypesPanel.add(getHearingTypeDescTxt(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        hearingTypesPanel.add(getHearingTypesScrollPane(), gbc);
        
        //Case Text
        gbc.gridx=0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        //gbc.insets = new Insets(4, 4, 4, 4);
        gbc.gridx = 0;
        gbc.gridy = 7;
        this.add(getCaseTitleLabel(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 7;
        this.add(getCaseTitleText(), gbc);

        // Hearing type toggle button controls
        gbc.gridx = 0;
        gbc.gridy = 0;
        hearingTypeFilterPanel.add(getTop5TypesBtn(), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        hearingTypeFilterPanel.add(getAllTypesBtn(), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        hearingTypesPanel.add(hearingTypeFilterPanel, gbc);
        
        
        

        // Add the hearing types panel to the main panel
        gbc.gridx = 1;
        gbc.gridy = 6;
        this.add(hearingTypesPanel, gbc);


        getHearingTypesBtnGrp().add(getAllTypesBtn());
        getHearingTypesBtnGrp().add(getTop5TypesBtn());
    }
    
    private JLabel getCaseTitleLabel() {
        if (caseTitleLbl == null) {
            caseTitleLbl = createLabel("AddHearingCaseTitleLabel", lbl2Dim);
        }

        return caseTitleLbl;
    }

    /**
     * Life-cycle method to retrieve non-volatile data, e.g. entries for pull
     * down lists. In this case, the list of court rooms and hearing types for
     * the current court.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepInitialise:START");
        }

        model.setCourtId(XhibitSingleton.getInstance().getCourtId());
        model.setCourtSiteId(XhibitSingleton.getInstance().getCourtSiteId());
        

        // Build a HashSet of the top 5 codes
        Set<String> top5Codes = new HashSet<String>();
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code01"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code02"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code03"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code04"));
        top5Codes.add(getBundleEntry("AddHearingTop5HearingTypes.code05"));

        // Obtain the hearing types and save them all in a HashMap to be used
        // for validation
        refHearingTypesHashMap = new HashMap();

        Collection refHearingTypesCollection = XhibitDelegateHelper.getBizRefDelegate().findHearingTypesByCourtId(XhibitSingleton.getInstance().getCourtId());

        // Sort all the hearing types by the description
        Sorter.sort((List) refHearingTypesCollection, new String[] { "hearingTypeDesc" });

        // Iterate through the hearing types saving them in various Vector
        // objects for use when the filtering command buttons are clicked
        Iterator iter = refHearingTypesCollection.iterator();
        while (iter.hasNext()) {
            RefHearingTypeBasicValue rhtBV = (RefHearingTypeBasicValue) iter.next();
            PullDownListObject pdlo = new PullDownListObject(rhtBV);

            // Save the item in a full list of hearing types
            allRefHearingTypes.add(pdlo);

            if (!refHearingTypesHashMap.containsKey(rhtBV.getHearingTypeCode())) {
                // Save the item in a unique collection of codes
                refHearingTypesHashMap.put(rhtBV.getHearingTypeCode(), rhtBV);
                distinctRefHearingTypes.add(pdlo);

                if (top5Codes.contains(rhtBV.getHearingTypeCode())) {
                    // Save the item in the collection of top 5 codes
                    top5RefHearingTypes.add(pdlo);
                }
            }
        }

        stepUpdateViewState();

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepInitialise:END");
        }
    }

    /**
     * Life-cycle method to vaidate the data on the screen. This method is
     * called by the framework when the user navigates off this screen.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepValidate:START");
        }


        if (getHearingTypesList().getSelectedIndex() < 0) {
            validateHearingTypeCode();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepValidate:END");
        }
    }


    /**
     * Empty implementation of the life-cycle method.
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepDeInitialise ");
        }
    }

    /**
     * Life-cycle method that is executed as a consequence of making the screen
     * visible. In 'Wizard' dialogs, the method is called explicitly for the
     * first screen in the sequence but is called by the framework for all
     * subsequent screens.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepActivate:START");
        }

        stepUpdateViewState();

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepActivate:END");
        }
    }

    /**
     * Life-cycle methos to manage the enabled state of screen components.
     */
    public void stepUpdateViewState() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepUpdateViewState:START");
        }

  
        getVerifyCodeBtn().setEnabled(getHearingTypeCodeTxt().getText().trim().length() > 0);
        getAllTypesBtn().setEnabled(true);
        getTop5TypesBtn().setEnabled(true);
        buttonPanel.getFinish().setEnabled(isMandatoryCompleted());

        if (LOG.isDebugEnabled()) {
            LOG.debug("AddHearingEnterCaseNumber - stepUpdateViewState:END");
        }
    }


    /**
     * Determines whether or not all mandatory fields have been completed. The
     * result of this is used to determine the enabled state of the 'Next'
     * button.
     * 
     * @return true if all mandatory fields are completed
     */
    private boolean isMandatoryCompleted() {
        return validHearingTypeCode() && caseTitleContainsText();
    }


	private boolean validHearingTypeCode() {
		return refHearingTypesHashMap.containsKey(getHearingTypeCodeTxt().getText());
	}

	private boolean caseTitleContainsText() {
		return getCaseTitleText().getText().trim().length() > 1;
	}


    /**
     * Create a JLabel using the passed in parameters
     * 
     * @param resourceKey
     *            A <code>String</code> value of the resource key used to get
     *            the text to be display
     * @param preferredSize
     *            Preferred size of the created <code>JLabel</code>
     * @return
     */
    private JLabel createLabel(String resourceKey, Dimension preferredSize) {
        final JLabel label = new JLabel();

        if (resourceKey != null) {
            label.setText(getBundleEntry(resourceKey));
        }

        return label;
    }

    /**
     * Immutable object used to represent the value of a row in a JList
     */
    private class PullDownListObject {

        final private RefHearingTypeBasicValue basicValue;

        public PullDownListObject(RefHearingTypeBasicValue basicValue) {
            this.basicValue = basicValue;
        }

        public String getDesc() {
            return getBasicValue().getHearingTypeDesc();
        }

        public RefHearingTypeBasicValue getBasicValue() {
            return basicValue;
        }

        public String toString() {
            return getDesc();
        }
    }

    private JTextField getHearingTypeCodeTxt() {
        if (hearingTypeCodeTxt == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.upperCase(),
                    Capability.limitedText(3) });
            hearingTypeCodeTxt = JTextFieldFactory.getTextField(doc);

            hearingTypeCodeTxt.setPreferredSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeCodeTxt.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeCodeTxt.setEnabled(true);
            hearingTypeCodeTxt.setToolTipText(getBundleEntry("AddHearingHearingTypeCodeToolTip"));
            
            
            hearingTypeCodeTxt.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    getHearingTypesList().clearSelection();

                    stepUpdateViewState();
                }
            });
        }
        return hearingTypeCodeTxt;
    }

    private JButton getVerifyCodeBtn() {
        if (verifyCodeBtn == null) {
            verifyCodeBtn = new JButton(getBundleEntry("AddHearingHearingTypeVerifyBtnLabel"));

            verifyCodeBtn.setToolTipText(getBundleEntry("AddHearingHearingTypeVerifyBtnToolTip"));
            verifyCodeBtn.setEnabled(false);
            verifyCodeBtn.setMnemonic(getBundleEntry("AddHearingHearingTypeVerifyBtnMnemonic").charAt(0));
            verifyCodeBtn.addActionListener(new XAction() {
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    validateHearingTypeCode();

                    RefHearingTypeBasicValue temp = getSelectedHearingTypeFromCode();
                    getHearingTypeDescTxt().setText(temp.getHearingTypeDesc());

                    stepUpdateViewState();
                }
            });
        }
        return verifyCodeBtn;
    }

    private JTextField getHearingTypeDescTxt() {
        if (hearingTypeDescTxt == null) {
            hearingTypeDescTxt = JTextFieldFactory.getTextField();
            hearingTypeDescTxt.setPreferredSize(new Dimension(350, XHIBITConstant.getLineHeight()));
            hearingTypeDescTxt.setMinimumSize(new Dimension(350, XHIBITConstant.getLineHeight()));
            hearingTypeDescTxt.setEnabled(true);
            hearingTypeDescTxt.setToolTipText(getBundleEntry("AddHearingHearingTypeDescToolTip"));
            hearingTypeDescTxt.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    getHearingTypeCodeTxt().setText("");
                    getHearingTypesList().clearSelection();
                    filterHearingTypeList();
                    stepUpdateViewState();
                }
            });
        }
        return hearingTypeDescTxt;
    }
    
    private JTextField getCaseTitleText() {
        if (caseTitleText == null) {
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability.utf8LimitedTextCapability(72) });
            caseTitleText = JTextFieldFactory.getTextField(doc);
            caseTitleText.setPreferredSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            caseTitleText.setMinimumSize(new Dimension(260, XHIBITConstant.getLineHeight()));
            caseTitleText.setToolTipText(getBundleEntry("AddCaseTitleToolTip"));
            caseTitleText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return caseTitleText;
    }

    /**
     * Filters the list of hearing types based on the data entered into the
     * hearing description field.
     */
    private void filterHearingTypeList() {
        String searchText = getHearingTypeDescTxt().getText().trim();
        Vector filteredList = new Vector();
        for (int x = 0; x < distinctRefHearingTypes.size(); x++) {
            PullDownListObject item = (PullDownListObject) distinctRefHearingTypes.elementAt(x);
            if (item.getDesc().regionMatches(true, 0, searchText, 0, searchText.length())) {
                filteredList.add(item);
            }
        }
        getHearingTypesList().setListData(filteredList);
    }

    private JScrollPane getHearingTypesScrollPane() {
        if (hearingTypesScrollPane == null) {
            hearingTypesScrollPane = new JScrollPane(getHearingTypesList());
            hearingTypesScrollPane.setMinimumSize(listDim);
            hearingTypesScrollPane.setPreferredSize(listDim);
        }
        return hearingTypesScrollPane;
    }

    private JList getHearingTypesList() {
        if (hearingTypesList == null) {
            hearingTypesList = new JList(top5RefHearingTypes);
            hearingTypesList.setVisibleRowCount(ROWS_TO_SHOW);
            hearingTypesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            hearingTypesList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (getHearingTypesList().getSelectedIndex() >= 0) {
                    	RefHearingTypeBasicValue selectedHearingType = getSelectedHearingTypeFromList();
                        getHearingTypeDescTxt().setText(selectedHearingType.getHearingTypeDesc());
                        getHearingTypeCodeTxt().setText(selectedHearingType.getHearingTypeCode());
                    }

                    stepUpdateViewState();
                }
            });
        }
        return hearingTypesList;
    }

    /**
     * Determines whether or not the user entered hearing type code is valid.
     * 
     * @throws CSValidationException
     */
    private void validateHearingTypeCode() throws CSValidationException {
        if (!refHearingTypesHashMap.containsKey(getHearingTypeCodeTxt().getText())) {
            getHearingTypeCodeTxt().requestFocus();
            throw new CSValidationException("gui.addHearing.refHearingTypeCodeNotKnown",
                    new String[] { getHearingTypeCodeTxt().getText() }, "Hearing type code not known");
        }
    }

    /**
     * Returns the requested value from the resource bundle for this task.
     * 
     * @param param
     * @return
     */
    private String getBundleEntry(String param) {
        return ResourceBundleHelper.getResource(XhibitBundles.Listings, param);
    }


    private JToggleButton getAllTypesBtn() {
        if (allTypesBtn == null) {
            allTypesBtn = new JToggleButton();
            allTypesBtn.setToolTipText(getBundleEntry("AddHearingAllTypesBtnToolTip"));
            allTypesBtn.setActionCommand(getBundleEntry("AddHearingAllTypesAction"));
            allTypesBtn.setText(getBundleEntry("AddHearingAllTypesBtn"));
            allTypesBtn.setMnemonic(getBundleEntry("AddHearingAllTypesBtnMnemonic").charAt(0));
            allTypesBtn.setPreferredSize(getTop5TypesBtn().getPreferredSize());
            allTypesBtn.setSelected(false);
            allTypesBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rebuildHearingTypes_actionPerformed(e);
                }
            });
        }
        return allTypesBtn;
    }

    private JToggleButton getTop5TypesBtn() {
        if (top5TypesBtn == null) {
            top5TypesBtn = new JToggleButton();
            top5TypesBtn.setToolTipText(getBundleEntry("AddHearingTop5TypesBtnToolTip"));
            top5TypesBtn.setActionCommand(getBundleEntry("AddHearingTop5TypesAction"));
            top5TypesBtn.setText(getBundleEntry("AddHearingTop5TypesBtn"));
            top5TypesBtn.setMnemonic(getBundleEntry("AddHearingTop5TypesBtnMnemonic").charAt(0));
            top5TypesBtn.setPreferredSize(getTop5TypesBtn().getPreferredSize());
            top5TypesBtn.setSelected(true);
            top5TypesBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rebuildHearingTypes_actionPerformed(e);
                }
            });
        }
        return top5TypesBtn;
    }


    private ButtonGroup getHearingTypesBtnGrp() {
        if (hearingTypesBtnGrp == null) {
            hearingTypesBtnGrp = new ButtonGroup();
        }

        return hearingTypesBtnGrp;
    }

    /**
     * Manages the rebuildiong of the hearing type list.
     * 
     * @param e
     */
    private void rebuildHearingTypes_actionPerformed(ActionEvent e) {
        rebuildHearingTypeList(e.getActionCommand());
    }

    /**
     * Rebuilds the hearing type list depending on the 'filter' button that was
     * pressed
     * 
     * @param actionCommand
     */
    private void rebuildHearingTypeList(String actionCommand) {
        if (actionCommand.equalsIgnoreCase(getBundleEntry("AddHearingTop5TypesAction"))) {
            getHearingTypesList().setListData(top5RefHearingTypes);
        } else if (actionCommand.equalsIgnoreCase(getBundleEntry("AddHearingByCaseTypeAction"))) {
            List<PullDownListObject> temp = new ArrayList<PullDownListObject>();
            
            for (PullDownListObject pdlo :  allRefHearingTypes) {
                RefHearingTypeBasicValue rhtBV = pdlo.getBasicValue();
                if (isNewUCaseSelected() && rhtBV.getCategory().equalsIgnoreCase(model.getCaseType())) {
                    temp.add(pdlo);
                }
            }
            getHearingTypesList().setListData(temp.toArray());
        } else {
            getHearingTypesList().setListData(distinctRefHearingTypes);
        }
    }


    /**
     * Helper method to determine if the new case radio button is selected
     * 
     * @return true - if the new case radio button is selected
     */
    private boolean isNewUCaseSelected() {
        return true;
    }

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveScreenToModel();
	}
	
    /**
     * Move the screen variables to the model
     */
	private void moveScreenToModel() {
		RefHearingTypeBasicValue selectedHearingType = getSelectedHearingTypeFromCode();
		model.setCaseTitle(getCaseTitleText().getText().trim());
		model.setHearingTypeDescription(selectedHearingType.getHearingTypeDesc());
		model.setHearingTypeId(selectedHearingType.getId());
		model.setHearingTypeCode(selectedHearingType.getHearingTypeCode());
	}
	
	/**
     * Get the selected hearing type from the code
     */
    private RefHearingTypeBasicValue getSelectedHearingTypeFromCode() {
    	return (RefHearingTypeBasicValue) refHearingTypesHashMap.get(getHearingTypeCodeTxt().getText());
    }
    
    /**
     * Get the selected hearing type from the selected value in the list
     */
    private RefHearingTypeBasicValue getSelectedHearingTypeFromList() {
    	PullDownListObject pdlo = (PullDownListObject) getHearingTypesList().getSelectedValue();
    	return pdlo.getBasicValue();
    }
}
