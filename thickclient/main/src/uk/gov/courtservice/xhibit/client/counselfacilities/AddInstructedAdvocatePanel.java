package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.BorderFactory;
//import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.comparator.RefSystemRefCodeOrderComparator;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;


/**
 * <p>
 * Title: Screen for choosing the legal representative
 * </p>
 * <p>
 * Description: Select the barrister.
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

public class AddInstructedAdvocatePanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private TitledBorder tb;

    private AddInstructedAdvocateModel model;
    
    private ResourceBundle resources;

    private JTextField firstNameText;

    private JTextField surnameText;
    
    private JTextField chambersText;
    
    private JLabel firstNameLabel;
    
    private JLabel surnameLabel;
    
    private JLabel chambersLabel;
    
    private JLabel defenceCategoryLabel;

    private JButton searchBtn;

    private JComboBox defenceCategoryCB;
    
    public JComboBox getDefenceCategoryCB() {
		return defenceCategoryCB;
	}

	public void setDefenceCategoryCB(JComboBox defenceCategoryCB) {
		this.defenceCategoryCB = defenceCategoryCB;
	}

	private JScrollPane resultsScrollPane;

    private XTable resultsTable;

    private Collection results = new Vector();
    
    private Vector<String> defenceCategoryComboxBoxData;
    
    public Vector<String> getDefenceCategoryComboxBoxData() {
		return defenceCategoryComboxBoxData;
	}

	public void setDefenceCategoryComboxBoxData(Vector<String> defenceCategoryComboxBoxData) {
		this.defenceCategoryComboxBoxData = defenceCategoryComboxBoxData;
	}

	private ArrayList defenceCategoryDisplayList;
    
    private XDialog parent;
    

    public AddInstructedAdvocatePanel(
            XDialog parent, 
            AddInstructedAdvocateModel model)
            throws CSRecoverableException {
        super();
        this.parent = parent;
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

        this.add(getFirstNameLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getFirstNameText(), new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSurnameText(), new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getChambersText(), new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getResultsScrollPane(), new GridBagConstraints(0, 3, 5, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getSearchBtn(), new GridBagConstraints(4, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getDefenceCategoryLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        this.add(getDefenceCategorySelector(), new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
    }

    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createDefaultTable(new AddInstructedAdvocateTableModel());
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
    
	private ArrayList getDefenceCategoryDisplayList() {
        if (defenceCategoryDisplayList != null) {
            return defenceCategoryDisplayList;
        }
        
        defenceCategoryDisplayList = getSortedDefenceCategories();
        
        return defenceCategoryDisplayList;
    }
    
    @SuppressWarnings("unchecked")
	private ArrayList getSortedDefenceCategories() {
    	ArrayList defCatCodes = new ArrayList();;
    	
    	try {
    		RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
	        criteria.setCodeType(RefSystemCodeCriteria.CodeType.ADVOCATE_TYPE);
	        criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
	    	
			defCatCodes = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
			Collections.sort(defCatCodes, new RefSystemRefCodeOrderComparator());
		} catch (BisRefControllerException e) {
			e.printStackTrace();
		}
        
        return defCatCodes;
    }
    
    /**
     * 
     * @param code
     */
    private String getItemText(String code) {
    	ArrayList items = getDefenceCategoryDisplayList();
    	String itemText = "";
    	
    	for (int i=0; i<items.size(); i++) {
   			String thisCode = ((RefSystemCodeBasicValue) (items.get(i))).getCode();
            if (code.equals(thisCode)) {
            	itemText = ((RefSystemCodeBasicValue) (items.get(i))).getDecode();
            }
    	}
    	
    	return itemText;
    }

    
    /**
     * 
     * @return
     */
    private Vector getComboCategoryData() {
    	// getting ref data for category combo box
    	Vector<String> comboCategoryData = new Vector<String>();
    	comboCategoryData.add("");
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.ADVOCATE_TYPE);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			
			@SuppressWarnings("unchecked")
			ArrayList<RefSystemCodeBasicValue> codeList = (ArrayList<RefSystemCodeBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria);
			Collections.sort(codeList, new RefSystemRefCodeOrderComparator());
			
			if (codeList.size() > 0) {
				for (int i = 0; i < codeList.size(); i++) {
					comboCategoryData.add(((RefSystemCodeBasicValue) (codeList.get(i))).getDecode());
				}
			}
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
		
		setDefenceCategoryComboxBoxData(comboCategoryData);
		
		return comboCategoryData;

    }

    private JComboBox getDefenceCategorySelector() {
        if (defenceCategoryCB == null) {
            defenceCategoryCB = new JComboBox(getComboCategoryData());//new JComboBox(getComboBoxModel());
        }

        defenceCategoryCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    stepUpdateViewState();
                } catch (CSRecoverableException csre) {
                    XHIBITConstant.handleError(csre);
                }
            }
        });
        
        return defenceCategoryCB;
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
    
    private JLabel getDefenceCategoryLabel() {
        if (defenceCategoryLabel == null) {
            defenceCategoryLabel = new JLabel();
            defenceCategoryLabel.setText(XHIBITConstant.getResource(resources, "lblDefenceCategory"));
        }

        return defenceCategoryLabel;
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
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
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
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return surnameText;
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
                    results = getAdvocateMatches();

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

    public JComponent getFirstEnterableComponent() {
        return getFirstNameText();
    }
    
    
    /**
     * Search for advocates with given user input.
     * 
     * @return Collection
     */
    private Collection getAdvocateMatches() throws CSRecoverableException {
        Collection<FindInstructedAdvocateTableRowModel> matches = 
            new Vector<FindInstructedAdvocateTableRowModel>();

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

                FindInstructedAdvocateTableRowModel trm = new FindInstructedAdvocateTableRowModel();
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
                trm.setLegalRepType(CounselFacilitiesHelper.BARRADIO);

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

        FindInstructedAdvocateTableRowModel item = null;

        int x = getResultsTable().getSelectedRow();

        if (x != -1) {
            XHIBITTableModelInterface xstModel = (XHIBITTableModelInterface) getResultsTable().getModel();
            item = (FindInstructedAdvocateTableRowModel) xstModel.getDataAt(x);
            
            String defenceCategoryDesc = (String)getDefenceCategoryCB().getSelectedItem();
            if (item != null && defenceCategoryDesc != null && !defenceCategoryDesc.equals("")) {
                Integer defenceCategoryId = getDefenceCategoryCB().getSelectedIndex();
            	item.setDefenceCategory(defenceCategoryId.toString()); 
            }
        }

        model.setFindInstructedAdvocateTableRowModel(item);
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepInitialise");
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
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepDeactivate");
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
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepValidate");
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
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepUpdateViewState");

        if (isSearchable(getFirstNameText().getText().trim())
                || isSearchable(getSurnameText().getText().trim())
                || isSearchable(getChambersText().getText().trim())) {
            getSearchBtn().setEnabled(true);
        } else {
            getSearchBtn().setEnabled(false);
        }
        
        if (parent.getButtonPanel() instanceof OkCancelPanel) {
            OkCancelPanel okCancelPanel = (OkCancelPanel)parent.getButtonPanel();
            String defenceCategory = (String)getDefenceCategoryCB().getSelectedItem();
            if (defenceCategory != null 
                    && !defenceCategory.equals("")
                    && getResultsTable().getSelectedRow() != -1) {
                okCancelPanel.okButton.setEnabled(true);
            } else {
                okCancelPanel.okButton.setEnabled(false);
            }
        }
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
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepDeinitialise");
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[AddInstructedAdvocatePanel] stepActivate");
        stepUpdateViewState();
    }

    private BisRefControllerBeanBusinessDelegate getBRCDelegate() {
        return XhibitDelegateHelper.getBizRefDelegate();
    }
    
    // Renderer for combobox
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            String crestCode = (String) value;
            setHorizontalAlignment(LEFT);

            if (list.getSelectedValue() != null) {
                setText(getItemText(crestCode));
            }
            return this;
        }
    }
}







