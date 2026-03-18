package uk.gov.courtservice.xhibit.client.admin.querycompletedcase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.casehistory.CaseHistoryControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseHistoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.QACASValue;
import uk.gov.courtservice.xhibit.business.vos.services.casehistory.CaseHistoryValue;
import uk.gov.courtservice.xhibit.client.util.CustomButtonPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CompletedCaseSearchPanel extends XPanel {
	private static final long serialVersionUID = 1L;
	private final Logger log = CSServices.getLogger(getClass());
	private CompletedCaseSearchModel caseSearchModel = null;
	private XDialog parent;
	// --- Search Panel ---
	private JPanel searchPanel = null;
	private Dimension searchSize = new Dimension(400, 180);
	private Dimension caseSize = new Dimension(700, 400);
	private Dimension defSize = new Dimension(700, 400);
	private Dimension defSearchSize = new Dimension(700, 400);
	
	//------Initial search screen---------//
	private JLabel lblCaseNumber = new JLabel("Case Number");
	private JLabel lblICaseNumber = new JLabel(" ");
	private XTextField txtCaseNumber = new XTextField(9, "^[A-Za-z]{1}[0-9]{8}$", lblICaseNumber, false);
	private JLabel lblDefendantName = new JLabel("Defendant Name");
	private XTextField txtDefendantName = new XTextField(250);
	private DocumentListener searchPanelDocListener = null;
	
	//------QACAS/QADEF screen tables/models/pane---------//
	private DefaultTableModel caseTableModel = null;
	private DefaultTableModel defTableModel = null;
	private JTable caseTable = null;
	private JTable defTable = null;
	private JScrollPane caseScroll = null;
	private JScrollPane qadefScroll = null;

	//------Multiple defendants panel---------//
	private DefaultTableModel defSearchTableModel = null;
	private JTable defSearchTable = null;
	private JScrollPane defSearchScroll = null;
	private JPanel defPanelSearchResults = null;

	
	// --- Panels used  ---
	private enum ActivePanel {
		CASE_PANEL, SEARCH_PANEL, MULTIPLE_DEFS_PANEL,QADEF_PANEL
	}

	private ActivePanel currentPanel = null;
	//---Needed for knowing whether we went straight to qadef or via def search
	private ActivePanel previousPanel = null;
	private JButton btnBack = null;
	private JButton btnOk = null;
	private Integer courtId = null;
	
	//------Various delegates used to query data---------//
	private CaseHistoryControllerBeanBusinessDelegate caseHistoryDel = null;
	private BisRefControllerBeanBusinessDelegate bisRefDel = null;
	private DefendantControllerBeanBusinessDelegate defendantDel = null;
	

	
	private String caseType = null;
	private final String DATE_FORMAT = "dd-MMM-yyyy";
	private CustomButtonPanel buttonPanel;
	/**
	 * Case Search fields.
	 */
	private JLabel caseNoLbl = null;
	private XTextField caseNoTf = null;
	private XTextField caseTitleTf = null;
	private JLabel commSentLbl = null;
	private XDatePanel dtCommSent = null;
	private JLabel magCourtLbl = null;
	private XTextField magCourtTf = null;
	private JLabel dtCompletedLbl = null;
	private XDatePanel dtCompleted = null;
	private JLabel reasonLbl = null;
	private XTextField reasonTf = null;
	private JLabel defFoundlbl = null;
	
	/**
	 * QADEF fields.
	 */
	
	private JLabel surnameLbl = null;
	private XTextField surnameTf = null;
	private JLabel sexLbl = null;
	private XTextField sexTf = null;
	private JLabel firstNameLbl = null;
	private XTextField firstNameTf = null;
	private JLabel otherNamesLbl = null;
	private XTextField otherNamesTf = null;
	private JLabel dobLbl = null;
	private XDatePanel dtDob = null;
	private JLabel caseFoundlbl = null;
		
	
	/** 
	 * Constructor
	 * To call : parent - dialog parent class
	 * caseSearchModel - class model data
	 */
	public CompletedCaseSearchPanel(XDialog parent, CompletedCaseSearchModel caseSearchModel) throws CSRecoverableException {
		this.parent = parent;
		this.caseSearchModel = caseSearchModel;
		if(caseSearchModel.getCourtId()==null || caseSearchModel.getCourtId()==0) {
			caseSearchModel.setCourtId(XhibitSingleton.getInstance().getCourtId());
			moveModelToScreen();
		}
		stepInitialise();
		jbInit();
	}

	/**
	 * Add components to screen.
	 */
	private void jbInit() {
		parent.setPreferredSize(searchSize);
		currentPanel = ActivePanel.SEARCH_PANEL;
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill=GridBagConstraints.BOTH;
	
		// --- Add panels to dialog ---
		this.add(getSearchPanel(), gbc);
		this.add(getCasePanel(), gbc);
		this.add(getQadefPanel(), gbc);
		this.add(getDefSearchResults(), gbc);
		disableAllPanels();
		searchPanel.setVisible(true);


		buttonPanel = (CustomButtonPanel)parent.getButtonPanel();
		
		// Switch button panel settings to place WEST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = 0;
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.WEST;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL; 
		
		buttonPanel.add(getReasonPanel(), buttonPanel.getGridBagConstraints());
			
		// Switch button panel settings back to placing EAST pinned buttons
		buttonPanel.getGridBagConstraints().gridx = GridBagConstraints.RELATIVE;
		buttonPanel.getGridBagConstraints().fill = GridBagConstraints.HORIZONTAL; 
		buttonPanel.getGridBagConstraints().anchor = GridBagConstraints.EAST;
				
		btnBack = buttonPanel.addButton("", false, true);
		btnBack.setText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources, "caseSearch.btnBack.btnText"));
		btnBack.setToolTipText(ResourceBundleHelper.getResource(XhibitBundles.CaseMaintenanceResources,"caseSearch.btnBack.tooltipText"));
		btnBack.setVisible(false);
						
		btnOk = buttonPanel.addButton("btnOk", false, true);
		btnOk.setEnabled(false);

		buttonPanel.addButton("btnCancel", true, false);
		parent.getRootPane().setDefaultButton(btnOk);		
		
		
		// --- Add document listeners for each panel ---
		searchPanelDocListener = new SearchPanelDocListener();
		txtCaseNumber.getDocument().addDocumentListener(searchPanelDocListener);
		txtDefendantName.getDocument().addDocumentListener(searchPanelDocListener);
		
		// --- Misc setup ---
		lblICaseNumber.setForeground(Color.RED);
	}

	/**
	 * Get default set of gridbag constraints.
	 */
	private GridBagConstraints getDefaultGridBagConstraints() {
		GridBagConstraints gbc = null;
		gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				XHIBITConstant.nonContainerInsets, 0, 0);
		return gbc;
	}

	/**
	 * Get JPanel for case search.
	 */
	private JPanel getSearchPanel() {
		if (null == searchPanel) {
			searchPanel = new JPanel();
			searchPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			// --- Set initial positions ---
			
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;
			
			searchPanel.setBorder(new TitledBorder(null, XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "caseSearch.mainText"),
					TitledBorder.LEADING, TitledBorder.TOP, null, null));

			// --- Add labels to layout ---
			gbc.gridy++;
			searchPanel.add(lblCaseNumber, gbc);
			gbc.gridy++;
			searchPanel.add(lblDefendantName, gbc);
			
			// --- Add error labels to layout ---
			gbc.gridx++;
			gbc.gridy = 0;
			gbc.insets = XHIBITConstant.errorLabelInsets;
			gbc.weighty = 0.1;
			searchPanel.add(lblICaseNumber, gbc);
			
			// --- Set constraints for text fields ---
			gbc.gridy++;
			gbc.insets = XHIBITConstant.nonContainerInsets;
			gbc.weightx = 0.9;
			gbc.weighty = 0.2;
			
			// --- Add text fields ---
			txtCaseNumber.setColumns(10);
			txtCaseNumber.setUpperCase(true);
			txtCaseNumber.setMaxLength(9);
			txtCaseNumber.setGridBagLayout(true);
			txtCaseNumber.setMinimumSize(txtCaseNumber.getPreferredSize());
			txtCaseNumber.setInvalidText(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "caseSearch.errorText"));
			searchPanel.add(txtCaseNumber, gbc);
			
			gbc.gridy++;
			txtDefendantName.setColumns(10);
			txtDefendantName.setUpperCase(true);
			txtDefendantName.setMinimumSize(txtDefendantName.getPreferredSize());
			searchPanel.add(txtDefendantName, gbc);
		}
		return searchPanel;
	}

	/** 
	 * Get Panel for case table.
	 */
	private JScrollPane getCasePanel() {
		if (null == caseScroll) {
			JPanel casePanel = new JPanel();
			casePanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.weighty=0.25;
			casePanel.add(getTopCase(), gbc);
		
			gbc.gridy++;
			gbc.weighty=0.75;
			casePanel.add(getBottomCasePanel(), gbc);
			casePanel.setPreferredSize(new Dimension(casePanel.getPreferredSize().width, casePanel.getPreferredSize().height+100));
			caseScroll = new JScrollPane(casePanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}

		return caseScroll;
	}
	
	
	/**
	 * Get Panel for qadef page.
	 */
	private JScrollPane getQadefPanel() {
		if (null == qadefScroll) {
			JPanel qadefPanel = new JPanel();
			qadefPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = getDefaultGridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.weighty=0.1;
			qadefPanel.add(getTopQadef(), gbc);
		
			gbc.gridy++;
			gbc.weighty=0.9;
			qadefPanel.add(getBottomQadefPanel(), gbc);
			qadefPanel.setPreferredSize(new Dimension(qadefPanel.getPreferredSize().width, qadefPanel.getPreferredSize().height+100));
			qadefScroll = new JScrollPane(qadefPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}

		return qadefScroll;
	}
	
	/**
	 * Get Panel for the top half of the QACAS screen.
	 */
	private JPanel getTopCase() {
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.weightx=0.05;
		caseNoLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.caseNo"));
		topPane.add(caseNoLbl, gbc);
		
		gbc.gridx++;
		gbc.weightx=0.15;
		caseNoTf = new XTextField();
		caseNoTf.setDisabledTextColor(Color.black);
		caseNoTf.setEnabled(false);
		caseNoTf.setMinimumSize(caseNoTf.getPreferredSize());
		topPane.add(caseNoTf,gbc);
		
		gbc.weightx=0.80;
		gbc.gridx++;
		gbc.gridwidth=5;
		caseTitleTf = new XTextField();
		caseTitleTf.setEnabled(false);
		caseTitleTf.setDisabledTextColor(Color.black);
		topPane.add(caseTitleTf,gbc);
		
		
		gbc.gridy++;
		gbc.gridx=0;
		gbc.weightx=0.2;
		gbc.gridwidth=2;
		commSentLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.committal"));
		topPane.add(commSentLbl, gbc);
		
		//+2 as the above spans 2
		gbc.gridx=gbc.gridx+2;		
		gbc.gridwidth=1;
		gbc.weightx=0.2;
		dtCommSent = new XDatePanel(this);
		dtCommSent.setEnabled(false);
		dtCommSent.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
		topPane.add(dtCommSent, gbc);
		
		gbc.gridx++;
		gbc.weightx=0.16;
		magCourtLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.magCourt"));
		topPane.add(magCourtLbl, gbc);
		
		gbc.gridx++;
		magCourtTf = new XTextField();
		magCourtTf.setDisabledTextColor(Color.black);
		
		magCourtTf.setEnabled(false);
		topPane.add(magCourtTf, gbc);

		gbc.gridx++;
		dtCompletedLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.dtCompleted"));
		topPane.add(dtCompletedLbl, gbc);
		
		gbc.gridx++;
		dtCompleted = new XDatePanel(this);
		dtCompleted.setEnabled(false);
		dtCompleted.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
		topPane.add(dtCompleted, gbc);		
		
		return topPane;
	}
	

	/**
	 * Get Panel for the bottom half of the QACAS screen.
   */
	private JPanel getBottomCasePanel() {
		JPanel casePanelSearchResults = new JPanel();
		casePanelSearchResults.setLayout(new GridBagLayout());
		TitledBorder paneTitle = BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.defRecords"));
		paneTitle.setTitleJustification(TitledBorder.CENTER);
		casePanelSearchResults.setBorder(paneTitle);

		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor=GridBagConstraints.EAST;
		gbc.weighty=0.05;
		defFoundlbl = new JLabel();
		casePanelSearchResults.add(defFoundlbl,gbc);
		
		gbc.gridy++;
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.NORTHWEST;
		gbc.weighty=0.95;
		caseTable = new JTable();
		caseTableModel = new DefaultTableModel(new Object[][]{},new String[] {
			XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.no"), 
			XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.name"),
			XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.sex"),
			XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.dob")}){
			
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		//-----Saving this so its easier when double clicking on the table to go to the QADEF screen-----//
		caseTableModel.addColumn("defHistory");		
		
		caseTable.setModel(caseTableModel);
		
		caseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//-----Hide the defHistory column-----//
		caseTable.getColumnModel().getColumn(caseTableModel.getColumnCount() - 1).setMinWidth(0);
		caseTable.getColumnModel().getColumn(caseTableModel.getColumnCount() - 1).setMaxWidth(0);
		
		//-----If you double click the row it will call the QADEF panel passing in the defHistory object-----//
		caseTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					int selectedRow = caseTable.getSelectedRow();
					int selectedColumn = caseTable.getColumnModel().getColumnCount() - 1;
	
					if (selectedRow < 0) {
						return;
					}
					DefendantHistoryBasicValue bv = ((DefendantHistoryBasicValue) caseTable.getValueAt(selectedRow, selectedColumn));
					setActivePanel(ActivePanel.QADEF_PANEL);
					populateDefValues(bv);
				}
			}
			
		});
		
		// Set widths of table to properly pre-size it
		caseTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		caseTable.getColumnModel().getColumn(1).setPreferredWidth(400);
		caseTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		caseTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		
		
		caseTable.setPreferredScrollableViewportSize(caseTable.getPreferredSize());
		caseTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);	
		
		
		JScrollPane scrollPane = new JScrollPane(caseTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		casePanelSearchResults.add(scrollPane, gbc);
		return casePanelSearchResults;
	}
	
	/**
	 * Get Panel when multiple defendants of that name have been found.
   */
	private JPanel getDefSearchResults() {
		defPanelSearchResults = new JPanel();
		defPanelSearchResults.setLayout(new GridBagLayout());

		GridBagConstraints gbc = getDefaultGridBagConstraints();
		
		defPanelSearchResults.setBorder((BorderFactory
				.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.mainText"))));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 0.9;
		
		defSearchTable = new JTable();
		// --- Defendant table model ---
		defSearchTableModel = new DefaultTableModel(new Object[][] {}, new String[] { 
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.surname"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.firstName"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.sex"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.dob")}) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		//-----Will hold the def history which will be passed into the qadef panel.
		defSearchTableModel.addColumn("defendantHistory");
		
		defSearchTable.setModel(defSearchTableModel);
		//-----On double click open up qadef with select defendants-----//
		defSearchTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				btnOk.setEnabled(true);
				if(e.getClickCount()==2) {
					int selectedRow = defSearchTable.getSelectedRow();
					int selectedColumn = defSearchTable.getColumnModel().getColumnCount() - 1;
	
					if (selectedRow < 0) {
						return;
					}
					DefendantHistoryBasicValue bv = ((DefendantHistoryBasicValue) defSearchTable.getValueAt(selectedRow, selectedColumn));
					setActivePanel(ActivePanel.QADEF_PANEL);
					populateDefValues(bv);
				}
			}
			
		});

		defSearchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//-----Hide the defendantHistory column-----//
		defSearchTable.getColumnModel().getColumn(defSearchTableModel.getColumnCount() - 1).setMinWidth(0);
		defSearchTable.getColumnModel().getColumn(defSearchTableModel.getColumnCount() - 1).setMaxWidth(0);

		// --- Set column widths ---
		defSearchTable.getColumnModel().getColumn(0).setPreferredWidth(150); 
		defSearchTable.getColumnModel().getColumn(1).setPreferredWidth(150); 						
		defSearchTable.getColumnModel().getColumn(2).setPreferredWidth(150); 
		defSearchTable.getColumnModel().getColumn(3).setPreferredWidth(150); 
																				
		defSearchTable.setPreferredScrollableViewportSize(defSearchTable.getPreferredSize());
		defSearchScroll = new JScrollPane();
		defSearchScroll.setViewportView(defSearchTable);
		defPanelSearchResults.add(defSearchScroll, gbc);
		
		return defPanelSearchResults;
	}
	
	/**
	 * Get the top panel of the QADEF screen.
   */
	private JPanel getTopQadef() {
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.weightx=0.05;
		surnameLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.surname"));
		topPane.add(surnameLbl, gbc);
		
		gbc.gridx++;
		gbc.weightx=0.65;
		surnameTf=new XTextField();
		surnameTf.setEnabled(false);
		surnameTf.setDisabledTextColor(Color.BLACK);
		topPane.add(surnameTf,gbc);
		
		gbc.gridx++;
		gbc.weightx=0.05;
		sexLbl=new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.sex"));
		topPane.add(sexLbl,gbc);
		
		gbc.gridx++;
		gbc.weightx=0.25;
		sexTf = new XTextField();
		sexTf.setEnabled(false);
		sexTf.setDisabledTextColor(Color.BLACK);
		topPane.add(sexTf,gbc);
		
		gbc.gridy++;
		gbc.gridx=0;
		gbc.weightx=0.05;
		firstNameLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.firstName"));
		topPane.add(firstNameLbl,gbc);

		gbc.gridx++;
		gbc.weightx=0.65;
		firstNameTf = new XTextField();
		firstNameTf.setEnabled(false);
		firstNameTf.setDisabledTextColor(Color.BLACK);
		topPane.add(firstNameTf,gbc);

		gbc.gridy++;
		gbc.gridx=0;
		gbc.weightx=0.05;
		otherNamesLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.otherNames"));
		topPane.add(otherNamesLbl,gbc);
		
		gbc.gridx++;
		gbc.weightx=0.65;
		otherNamesTf = new XTextField();
		otherNamesTf.setEnabled(false);
		otherNamesTf.setDisabledTextColor(Color.BLACK);
		topPane.add(otherNamesTf,gbc);

		gbc.gridx++;
		gbc.weightx=0.05;
		dobLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.dob"));
		topPane.add(dobLbl,gbc);

		gbc.gridx++;
		gbc.weightx=0.25;
		dtDob = new XDatePanel(this);
		dtDob.setEnabled(false);
		dtDob.getEntryField().getDisplay().setDisabledTextColor(Color.BLACK);
		topPane.add(dtDob,gbc);

		return topPane;
	}
	
	/**
	 * Get the bottom panel of the QADEF screen.
	 */
	private JPanel getBottomQadefPanel() {
		JPanel topPane = new JPanel();
		topPane.setLayout(new GridBagLayout());		
		TitledBorder paneTitle = BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.caseRecords"));
		paneTitle.setTitleJustification(TitledBorder.CENTER);
		topPane.setBorder(paneTitle);
		
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor=GridBagConstraints.EAST;
		gbc.weighty=0.05;
		caseFoundlbl = new JLabel();
		topPane.add(caseFoundlbl,gbc);
		gbc.gridy++;
		
		gbc.fill=GridBagConstraints.BOTH;
		gbc.anchor=GridBagConstraints.NORTHWEST;

		gbc.weighty=0.95;		
		defTable = new JTable();
		defTableModel = new DefaultTableModel(new Object[][]{},new String[] {
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.caseNo"), 
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.title"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.magCourt"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.commSent"),
				XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.completed")})
				{
					private static final long serialVersionUID = 1L;

						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}
				};

		//-----Hold case history information so it's easier when going to the QACAS screen after double clicking entry-----//
		defTableModel.addColumn("caseHistory");
		defTable.setModel(defTableModel);
		defTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//-----Hide the caseHistory column-----//
		defTable.getColumnModel().getColumn(defTableModel.getColumnCount() - 1).setMinWidth(0);
		defTable.getColumnModel().getColumn(defTableModel.getColumnCount() - 1).setMaxWidth(0);
		
		defTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		defTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		defTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		defTable.getColumnModel().getColumn(3).setPreferredWidth(120);
		defTable.getColumnModel().getColumn(4).setPreferredWidth(120);

		defTable.setPreferredScrollableViewportSize(defTable.getPreferredSize());
		defTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		//-----On double click open up QACAS screen-----//
		defTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					int selectedRow = defTable.getSelectedRow();
					int selectedColumn = defTable.getColumnModel().getColumnCount() - 1;
	
					if (selectedRow < 0) {
						return;
					}
					CaseHistoryValue historyValue = ((CaseHistoryValue) defTable.getValueAt(selectedRow, selectedColumn));
					if(historyValue!=null &&historyValue.getCaseHistoryId()!=null) {
						populateCaseValues(historyValue);
						setActivePanel(ActivePanel.CASE_PANEL);
					}
				}
			}
			
		});
		
		JScrollPane tableScroll = new JScrollPane(defTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		topPane.add(tableScroll,gbc);
		topPane.setMinimumSize(defTable.getPreferredSize());
		
		return topPane;
	
	}
	
		
	/**
	 * Set up and return the reason panel (on the bottom of QACAS/QADEF screen).
	 */
	private JPanel getReasonPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = getDefaultGridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		
		gbc.weightx=0.05;
		reasonLbl= new JLabel(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.reason"));
		reasonLbl.setVisible(false);
		panel.add(reasonLbl,gbc);

		gbc.gridx++;
		gbc.weightx=0.95;
		reasonTf = new XTextField("");
		reasonTf.setEnabled(false);
		reasonTf.setVisible(false);
		reasonTf.setDisabledTextColor(Color.BLACK);
		panel.add(reasonTf,gbc);
		
		return panel;
	}
			
  /**
   * Update dialog with panel for specified type
	 */
	private void setActivePanel(ActivePanel activePanel) {
		if (parent.isVisible()) {
			switch (activePanel) {
			case CASE_PANEL:
				disableAllPanels();
				setUpDisplayScreens();
				parent.setSize(caseSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.title"));
				buttonPanel.getButtons().get(buttonPanel.getButtons().size()-1).setText("Close");
				caseScroll.setVisible(true);
				parent.validate();
				parent.repaint();
				break;
			case QADEF_PANEL:
				disableAllPanels();
				setUpDisplayScreens();
				parent.setSize(defSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryDefendants.mainTitle"));
				buttonPanel.getButtons().get(buttonPanel.getButtons().size()-1).setText("Close");
				qadefScroll.setVisible(true);
				parent.validate();
				parent.repaint();
				break;
			case SEARCH_PANEL:
				disableAllPanels();
				btnOk.setVisible(true);
				btnBack.setVisible(false);
				reasonLbl.setVisible(false);
				reasonTf.setVisible(false);
				parent.setSize(searchSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "caseSearch.title"));
				buttonPanel.getButtons().get(buttonPanel.getButtons().size()-1).setText("Cancel");
				searchPanel.setVisible(true);
				break;
			case MULTIPLE_DEFS_PANEL:
				disableAllPanels();
				
				btnBack.setVisible(true);
				btnOk.setVisible(true);
				btnOk.setEnabled(false);
				
				reasonLbl.setVisible(false);
				reasonTf.setVisible(false);
				
				parent.setSize(defSearchSize);
				parent.setLocationRelativeTo(parent.getParentFrame());
				
				parent.setTitle(XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "selectDefendant.title"));
				buttonPanel.getButtons().get(buttonPanel.getButtons().size()-1).setText("Cancel");
				parent.validate();
				parent.repaint();
				defPanelSearchResults.setVisible(true);
				
				break;
			}
			
			// --- Set current panel type and previousPanel(needed when you've gone from multiple defs to QADEF)  ---
			previousPanel = currentPanel;
			currentPanel = activePanel;
		}
	}

	/**
	 * Update local variables from screen.
	 */
	private void moveModelToScreen() {
		courtId = caseSearchModel.getCourtId();
	}
	
	/**
	 * stepInitialise which initialises all the delegates
	 */
	@Override
	public void stepInitialise() throws CSRecoverableException {
		caseHistoryDel = XhibitDelegateHelper.getCaseHistoryDelegate();
		bisRefDel = XhibitDelegateHelper.getBizRefDelegate();
		defendantDel = XhibitDelegateHelper.getDefendantDelegate();
	}

	/**
   * stepActivate which moves the model passed in to screen.
   */
	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}
	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}
	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	/** 
	 * Called when ok/back is clicked and responds accordingly.
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// === OK Button ===
		if (btnOk.equals(getDeinitialiseSource())) {
			switch (currentPanel) {
			case SEARCH_PANEL:
				//-----as per Fs if txt isn't empty then assume its a QACAS search-----//
				if (txtCaseNumber.getText().length() > 1) {
					if (!txtCaseNumber.hasError()) {
						caseType = txtCaseNumber.getText().substring(0, 1).toUpperCase();
						try {
							CaseHistoryValue historyValue = caseHistoryDel.findByCaseNumberCaseTypeAndCourtId(caseType, txtCaseNumber.getText().substring(1), courtId);
							if(historyValue!=null &&historyValue.getCaseHistoryId()!=null) {
								populateCaseValues(historyValue);
								setActivePanel(ActivePanel.CASE_PANEL);
							} else {
								JOptionPane.showMessageDialog(parent.getParentFrame(),
									XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationMessage"),
									XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
									JOptionPane.INFORMATION_MESSAGE);
							}
						} catch (FinderException e) {
							log.error("An error occurred when trying to find case history information");
							XHIBITConstant.handleError(e, this.getClass());	
						}
					}
				} else {
					ArrayList<DefendantHistoryBasicValue> defs =(ArrayList<DefendantHistoryBasicValue>) defendantDel.findDefendantHistoryBySurname(txtDefendantName.getText(), courtId);
					if(defs.size()>0) {
						if(defs.size()==1) {
							setActivePanel(ActivePanel.QADEF_PANEL);
							populateDefValues((DefendantHistoryBasicValue)defs.iterator().next());

						} else {
							
							//-----sort based on surname, firstname, dob
							Comparator<DefendantHistoryBasicValue> defendantComparator = new Comparator<DefendantHistoryBasicValue>() {
								@Override
								public int compare(DefendantHistoryBasicValue o1, DefendantHistoryBasicValue o2) {
									if (o1.getSurname().equals(o2.getSurname())) {
                    if(o1.getFirstName()!=null && o2.getFirstName()!=null){
                      if (o1.getFirstName().equals(o2.getFirstName())) {
                        if(o1.getDateOfBirth()!=null && o2.getDateOfBirth()!=null) {
                          return o1.getDateOfBirth().compareTo(o2.getDateOfBirth());
                        } else {
                          if(o1.getDateOfBirth()==null) {
                            return 1;
                          } else {
                            return -1;
                          }
                        }
                      } else {
                        if(!o1.getFirstName().equals("") && !o2.getFirstName().equals("")) {
                          return o1.getFirstName().compareTo(o2.getFirstName());
                        } else {
                          if(o1.getFirstName().equals("")) {
                            return 1;
                          } else {
                            return -1;
                          }
                        }
                      }
                    } else {
                      if(o1.getFirstName()==null) {
                        return 1;
                       } else {
                        return -1;
                        }
                    }
									} else {
										if(!o1.getSurname().equals("") && !o2.getSurname().equals("")) {
											return o1.getSurname().compareTo(o2.getSurname());
										} else {
											if(o1.getSurname().equals("")) {
												return 1;
											} else {
												return -1;
											}
										}
									}
								}
							};
							Collections.sort(defs, defendantComparator);
							setActivePanel(ActivePanel.MULTIPLE_DEFS_PANEL);
							populateDefSearchValues(defs);
							
						}
					}else {
							JOptionPane.showMessageDialog(parent.getParentFrame(),
								XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationMessage"),
								XHIBITConstant.getResource(XhibitBundles.Listings, "caseSearchOKConfirmationTitle"),
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
				break;
			case MULTIPLE_DEFS_PANEL:
				int selectedRow = defSearchTable.getSelectedRow();
				int selectedColumn = defSearchTable.getColumnModel().getColumnCount() - 1;

				if (selectedRow < 0) {
					return;
				}
				
				DefendantHistoryBasicValue bv = ((DefendantHistoryBasicValue) defSearchTable.getValueAt(selectedRow, selectedColumn));
				setActivePanel(ActivePanel.QADEF_PANEL);
				populateDefValues(bv);
				break;
			default:
				break;
			}
		}
		else if (btnBack.equals(getDeinitialiseSource())) {
			switch (currentPanel) {
				case SEARCH_PANEL:
					break;
				case CASE_PANEL:
					setActivePanel(ActivePanel.SEARCH_PANEL);
					break;
				case QADEF_PANEL:
					if(previousPanel == ActivePanel.MULTIPLE_DEFS_PANEL) {
						setActivePanel(ActivePanel.MULTIPLE_DEFS_PANEL);
						btnOk.setEnabled(true);
					} else {
						setActivePanel(ActivePanel.SEARCH_PANEL);					
					}
					break;
				case MULTIPLE_DEFS_PANEL:
					setActivePanel(ActivePanel.SEARCH_PANEL);
					break;
			}
		}
	}

	
	/** 
	 * Custom DocumentListener class to only enable OK button when data entered.
	 * @author waltersn
	 *
	 */
	private class SearchPanelDocListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantName.getText() != null && txtDefendantName.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantName.getText() != null && txtDefendantName.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if ((txtCaseNumber.getText() != null && txtCaseNumber.getText().length() > 0)
					|| (txtDefendantName.getText() != null && txtDefendantName.getText().length() > 0)) {
				btnOk.setEnabled(true);
			} else {
				btnOk.setEnabled(false);
			}
		}
	}
	
	/** 
	 * Called when you find a value for case search , populates the fields on the display screen.
	 * @param historyValue CaseHistoryValue object
	 */
	public void populateCaseValues(CaseHistoryValue historyValue) {
		//clear the table first 
		caseTableModel.setRowCount(0);
		caseNoTf.setText(historyValue.getCaseType()+""+historyValue.getCaseNumber());
		caseTitleTf.setText(historyValue.getCaseTitle());
		if(historyValue.getCommittalDate()!=null) {
			dtCommSent.setDate(historyValue.getCommittalDate());
		} else {
			dtCommSent.setDate(historyValue.getSentForTrial());
		}
		if(historyValue.getDateArchived()!=null) {
			dtCompleted.setDate(historyValue.getDateArchived());
		}
		String code = historyValue.getPsdCTCode();
		reasonTf.setText(historyValue.getReasonDeleted());
		
		try {
			if(code!=null && !code.equals("")) {
				String magName = bisRefDel.findByPSDCTCodeAndCourtId(code, courtId);
				if(magName!=null && !magName.equals("")) {
					magCourtTf.setText(magName);
					//needed otherwise the tf shrinks down to nothing on resizing
					magCourtTf.setMinimumSize(magCourtTf.getPreferredSize());
				} 
        
			}  else {
				magCourtTf.setText("");
			}
			ArrayList<QACASValue> defValues = (ArrayList<QACASValue>)defendantDel.findDefendantHistoryRecords(historyValue.getCaseHistoryId());
			Comparator<QACASValue> defendantComparator = new Comparator<QACASValue>() {
				@Override
				public int compare(QACASValue o1, QACASValue o2) {
					return o1.getDefendantOnCase().getDefendantNumber().compareTo(o2.getDefendantOnCase().getDefendantNumber());
				}
			};
			Collections.sort(defValues, defendantComparator);
			defFoundlbl.setText(defValues.size()+" "+XHIBITConstant.getResource(XhibitBundles.xhibitQACAS, "queryCase.defFound"));
			if(defValues.size()>0) {
				for(QACASValue qacasVal : defValues) {
					//no
					Integer defNo = qacasVal.getDefendantOnCase().getDefendantNumber();
					//name// : surname , first name , middlename
					String name = qacasVal.getDefendantHistory().getSurname();
					if(qacasVal.getDefendantHistory().getFirstName()!=null && !qacasVal.getDefendantHistory().getFirstName().equals("")) {
						name=name+", "+qacasVal.getDefendantHistory().getFirstName();
					}
					if(qacasVal.getDefendantHistory().getMiddleName()!=null && !qacasVal.getDefendantHistory().getMiddleName().equals("")) {
						name = name+", "+qacasVal.getDefendantHistory().getMiddleName();
					}
					//sex
					String sex = qacasVal.getGenderString();
					//dob
					String defDateOfBirth = "";
					if (null !=  qacasVal.getDefendantHistory().getDateOfBirth()) {
						SimpleDateFormat defDateOfBirthFormat = new SimpleDateFormat(DATE_FORMAT);
							defDateOfBirth = defDateOfBirthFormat
								.format(qacasVal.getDefendantHistory().getDateOfBirth().getTime());
					}
					caseTableModel.addRow(new Object[]{defNo, name, sex, defDateOfBirth, qacasVal.getDefendantHistory()});
				}
			} 
		} catch (ObjectNotFoundException e) {
			log.error("An issue has occurred while retrieving defendants for this case");
			XHIBITConstant.handleError(e, this.getClass());				
		}
	}
	
	/**
	 * Called when you find a value for def search, populates the fields on the display screen.
	 * @param defs DefendantHistoryBasicValue object.
	 */
	private void populateDefValues(DefendantHistoryBasicValue defs){
		//clearTable
		defTableModel.setRowCount(0);
		
		surnameTf.setText(defs.getSurname());
		firstNameTf.setText(defs.getFirstName());
		otherNamesTf.setText(defs.getMiddleName());
		sexTf.setText(defs.getGenderString());
		reasonTf.setText(defs.getReasonDeleted());
		if(defs.getDateOfBirth()!=null) {
			dtDob.setDate(defs.getDateOfBirth());
		} else {
			Date d = null;
			dtDob.setDate(d);
		}
		try {
			Collection<DefendantOnCaseHistoryBasicValue> defonCaseHistory = defendantDel.findDefendantOnCaseHistoryRecords(defs.getDefendantHistoryId());
			if(defonCaseHistory.size()>0) {
				caseFoundlbl.setText(defonCaseHistory.size()+" Cases Found");
				ArrayList<CaseHistoryValue> caseValues = new ArrayList<CaseHistoryValue>();
				for(DefendantOnCaseHistoryBasicValue defhistVal : defonCaseHistory) {
									
					CaseHistoryValue historyValue = caseHistoryDel.findByCaseHistoryId(defhistVal.getCaseHistoryId());
					caseValues.add(historyValue);
				}
				//Sort them by caseNumber and then case type
				Comparator<CaseHistoryValue> caseComparator = new Comparator<CaseHistoryValue>() {
					@Override
					public int compare(CaseHistoryValue o1, CaseHistoryValue o2) {
						int rc= 0;
						if(o1.getCaseNumber().equals(o2.getCaseNumber())) {
							rc = o1.getCaseType().compareTo(o2.getCaseType());
						} else {
							rc = o1.getCaseNumber().compareTo(o2.getCaseNumber());
						}
						return rc;
					}
				};
				Collections.sort(caseValues, caseComparator);
				for (CaseHistoryValue historyValue : caseValues) {
					String caseNo = historyValue.getCaseType()+""+historyValue.getCaseNumber();
					String title = historyValue.getCaseTitle();
					String magCourt = "";
							
					String code = historyValue.getPsdCTCode();
					if(code!=null && !code.equals("")) {
						String magName = bisRefDel.findByPSDCTCodeAndCourtId(code, courtId);
							if(magName!=null && !magName.equals("")) {
								magCourt=magName;
							}
					}
							
					String commSentDate = "";
					if (null !=  historyValue.getCommittalDate()) {
						SimpleDateFormat defDateOfCommFormat = new SimpleDateFormat(DATE_FORMAT);
						commSentDate = defDateOfCommFormat
								.format(historyValue.getCommittalDate().getTime());
					} else {
						SimpleDateFormat defDateOfTrialFormat = new SimpleDateFormat(DATE_FORMAT);
						commSentDate = defDateOfTrialFormat
								.format(historyValue.getSentForTrial().getTime());
					}
					String dateComplete = "";
					if(null!= historyValue.getDateArchived()){
						SimpleDateFormat defDateCompleteFormat = new SimpleDateFormat(DATE_FORMAT);
						dateComplete = defDateCompleteFormat
								.format(historyValue.getDateArchived().getTime());
					}
					defTableModel.addRow(new Object[]{caseNo, title, magCourt, commSentDate, dateComplete, historyValue});
				}
			} else {
				caseFoundlbl.setText("0 Cases Found");
			}
		}  	catch (FinderException e) {
				log.error("Unable to find case Id" );
				XHIBITConstant.handleError(e, this.getClass());				
		}
	}
	
	/**
	 * Populates the entries in the table for defendant search when there's multiple
	 * defendants with the same surname.
	 * @param defValues retrieved from db and passed to this method
	 */
	private void populateDefSearchValues(Collection<DefendantHistoryBasicValue> defValues) {
		defSearchTableModel.setRowCount(0);
		Iterator<DefendantHistoryBasicValue> it = defValues.iterator();
		while(it.hasNext()) {
			DefendantHistoryBasicValue def = (DefendantHistoryBasicValue) it.next();
			String surname = def.getSurname();
			String firstName = def.getFirstName();
			String sex = def.getGenderString();
			String dateOfBirth = "";
			if(null!= def.getDateOfBirth()){
				SimpleDateFormat defDateOfBirthFormat = new SimpleDateFormat(DATE_FORMAT);
				dateOfBirth = defDateOfBirthFormat
						.format(def.getDateOfBirth().getTime());
			}
			defSearchTableModel.addRow(new Object[]{surname, firstName, sex,dateOfBirth,def});
			
		}
	}
	/**
	 * Called from setActivePanel and sets all panels to not be visible.
	 */
	private void disableAllPanels() {
		caseScroll.setVisible(false);
		qadefScroll.setVisible(false);
		defPanelSearchResults.setVisible(false);
		searchPanel.setVisible(false);
	}
	
	/**
	 * Sets the visibility of various buttons/fields for the two display screens.
	 */
	private void setUpDisplayScreens() {
		btnBack.setVisible(true);
		btnOk.setVisible(false);
		reasonLbl.setVisible(true);
		reasonTf.setVisible(true);
	}

}
