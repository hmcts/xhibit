package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseSummaryLinkingValue;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.listings.notes.CaseNotesDialog;
import uk.gov.courtservice.xhibit.client.listings.notes.CaseNotesModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.PopupListener;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseSummaryAssociatedCasesPanel extends CaseSummaryTab {

	private static final long serialVersionUID = 1811152823385018036L;
	private static final List<String> VALID_CASE_TYPES = CaseSummaryModel.ValidValues.CASE_TYPES;
	private CaseSummaryModel model;
	private JTable associatedCasesTable;
	private JPopupMenu associatedCasesPopupMenu;
	private DisplayOnlyField caseTitleText;
	private XAction reloadCaseSummaryAction;
	private XDialog parent;

	public CaseSummaryAssociatedCasesPanel(XDialog parent, CaseSummaryModel model, XAction reloadCaseSummaryAction) {
		super();
		this.parent = parent;
		this.model = model;
		this.reloadCaseSummaryAction = reloadCaseSummaryAction;
		jbInit();
	}
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,15,0,15), 0, 0);
		
		gbc.weighty = 0.1;
	    JPanel caseTitlePanel = initCaseTitlePanel();
	    this.add(caseTitlePanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.9;
	    JPanel listingHistoryPanel = initAssociatedCasesPanel();
	    this.add(listingHistoryPanel, gbc);

	}
	
	private JPanel initAssociatedCasesPanel() {
		CaseSummaryAssociatedCasesTableModel tableModel = new CaseSummaryAssociatedCasesTableModel();
		this.associatedCasesTable = XTableFactory.getInstance().createDefaultTable(tableModel);
		TableUtils.setupDefaultsOnJTable(associatedCasesTable);
		setTableColumnWidths(associatedCasesTable, tableModel.getColumnWidths());
		
		JPopupMenu popupMenu = getAssociatedCasesPopupMenu();
		associatedCasesTable.add(popupMenu);
		associatedCasesTable.addMouseListener(new PopupListener(popupMenu) {
			
			@Override
		    protected void maybeShowPopup(MouseEvent e) {
		    	// Select the row clicked
				JTable table = (JTable) e.getSource();
				CaseSummaryAssociatedCasesTableModel tableModel = (CaseSummaryAssociatedCasesTableModel) table.getModel();
		    	int row = table.rowAtPoint(e.getPoint());
		    	table.setRowSelectionInterval(row, row);
		    	CaseSummaryLinkingValue rowValue = tableModel.getRow(row);
		    	boolean showMenu = (rowValue != null && VALID_CASE_TYPES.contains(rowValue.getCaseType()));
		    	// Show the popup menu
		    	if (showMenu) {
		    		super.maybeShowPopup(e);
		    	}
		    }
		});
		
		JScrollPane scrollPanel = new JScrollPane(associatedCasesTable);
		scrollPanel.setPreferredSize(associatedCasesTable.getPreferredSize());
		
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  XHIBITConstant.nonContainerInsets, 0, 0);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"associatedCasesPanelTitle")));
		panel.add(scrollPanel, gbc);
		return panel;
	}

	private JPanel initCaseTitlePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel caseTitlePanel = new JPanel();
		caseTitlePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel caseTitleLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalCaseTitle"));
		caseTitlePanel.add(caseTitleLabel, gbc);
		
		gbc.gridx++;		
		gbc.weightx = 0.95;
		caseTitleText = new DisplayOnlyField();
		caseTitlePanel.add(caseTitleText, gbc);
	
		return caseTitlePanel;
	}

    private JPopupMenu getAssociatedCasesPopupMenu() {
        if (associatedCasesPopupMenu == null) {
        	associatedCasesPopupMenu = new JPopupMenu();
        	associatedCasesPopupMenu.add(new JMenuItem(reloadCaseSummaryAction));
        	associatedCasesPopupMenu.add(new JMenuItem(new CaseNotesMenuItemAction()));
        }
        return associatedCasesPopupMenu;
    }

	@SuppressWarnings("unchecked")
	@Override
	protected void moveModelToScreen() {
		this.caseTitleText.setText(model.getCase().getCaseTitle());
		Integer groupNumber = model.getCase().getCaseGroupNumber();
		ArrayList<CaseSummaryLinkingValue> associatedCases = new ArrayList<CaseSummaryLinkingValue>();
		 if (groupNumber != null) {
			associatedCases = (ArrayList<CaseSummaryLinkingValue>) XhibitDelegateHelper.getCaseDelegate().findCasesByGroupNumber(XhibitSingleton.getInstance().getCourtId(), groupNumber);
		}
		getTableModel().setTableSource(associatedCases);
	}
	
	private CaseSummaryAssociatedCasesTableModel getTableModel() {
		return (CaseSummaryAssociatedCasesTableModel) associatedCasesTable.getModel();
	}
	
	/**
	 * Private class to handle the popup menu item for Case Notes
	 */
	private class CaseNotesMenuItemAction extends XAction {

        private static final long serialVersionUID = 1L;

        public CaseNotesMenuItemAction() {
            populateFromBundle("CaseSummaryViewCaseNotes");
        }

        public void xActionPerformed(ActionEvent ae) throws Exception {
        	// Get the currently selected row
        	CaseSummaryLinkingValue valueObject = (CaseSummaryLinkingValue) (((CaseSummaryAssociatedCasesTableModel) associatedCasesTable.getModel())
        			.getRow(associatedCasesTable.getSelectedRow()));

        	// Display the notes screen
        	Timestamp dateTransTo = null;
        	if ( null != valueObject.getDateTransTo() ) {
        		dateTransTo = new Timestamp(valueObject.getDateTransTo().getTime());
        	}
        	
			CaseNotesModel notesModel = new CaseNotesModel(valueObject.getCaseId(), valueObject.getCaseType(),
					valueObject.getCaseNumber(), valueObject.getCaseListingEntryId(), dateTransTo);
			CaseNotesDialog notesDialog = new CaseNotesDialog(parent, notesModel);
			notesDialog.setVisible(true);
        }		
    }
}
