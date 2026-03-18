package uk.gov.courtservice.xhibit.client.listings.list.additional;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.StringUtils;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.casemanagement.DropdownBoxCellRender;
import uk.gov.courtservice.xhibit.client.listings.ListTypeEnum;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.ListingUtils;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.MultiLineEditField;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractComboBoxValidator;
import uk.gov.courtservice.xhibit.client.util.validation.AbstractTextValidator;
import uk.gov.courtservice.xhibit.client.util.validation.ComboBoxValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.TextValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Class to display the additional details screen.
 * 
 * @author westalll
 *
 */
public class AdditionalDetailsPanel extends XPanel implements ValidationListener {

	private static final long serialVersionUID = 1L;
	
	private static final int DEFENDANT_COL_POSITION = 0;
	private static final int ATTENDING_COL_POSITION = 1;
	private static final int COURTROOM_COL_POSITION = 2;

	private static final boolean ALLOW_EDITABLE = true;
	private static final boolean NOT_EDITABLE = false;
	private static final boolean ALLOW_FOCUS = true;
	private static final boolean NO_FOCUS = false;

	private static final String YES = "Y";
	private static final String NO = "N";
	
	private static final String HRG_TYPE_TRN = "TRN";
	
	private static final int TIME_FIELD_SIZE = 2;
	
	private AdditionalDetailsModel model;
	private XDialog parent;
	
	private Collection<DefendantValue> defendantValues;
	private RefJudgeBasicValue refJudgeBasicValue;

	private DefendantTableModel defendantTableModel;
	private JTable defendantTable;
	private XTextField hearingType;
	private JButton verifyHearingTypeButton;
	private XComboBox hearingTypeCombo;
	private String regex = "[A-Z]{3}";

	private XComboBox preDefined;
	private MultiLineEditField freeText;
	private JLabel timeMarkingLabel;
	private JLabel timeListedLabel;
	private XComboBox timeMarkingComboBx = null;
	private XTextField timeListedHourText = null;
	private XTextField timeListedMinText = null;
	private ComboBoxValidationController timeMarkingVC;
	private TextValidationController timeListedHourVC;
	private TextValidationController timeListedMinVC;
	private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	private XTextField judgeText = null;
	private JCheckBox includeInCourtRoomList;
	private XTextField timeEstText = null;
	private boolean pageLoaded = false;

	public AdditionalDetailsPanel(final XDialog parent, final AdditionalDetailsModel model) throws CSRecoverableException {
		this.parent = parent;
		this.model = model;
		
		stepInitialise();
		jbInit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void stepInitialise() throws CSRecoverableException {
		// Get the defendants on the case
		final CaseOnListComplexValue caseOnList = model.getCaseOnList();
		defendantValues =  XhibitDelegateHelper.getListingsDelegate().getDefendantsByCaseId(caseOnList.getCaseId());

		// Get the required judge which is optional
		final CaseListingEntryBasicValue caseListingEntry = caseOnList.getCaseListingEntry();
		if (caseListingEntry != null && caseListingEntry.getJudgeId() != null) {
			refJudgeBasicValue = XhibitDelegateHelper.getListingsDelegate().getRefJudgeById(caseListingEntry.getJudgeId());
		}
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		moveModelToScreen();

		// Initialise display
		stepUpdateViewState();
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// Throw exception if validation failures to prevent saving
		if (!ValidationControllerFactory.validateComponents(validationControllers)) {
			throw new CSValidationException("validation.general", "Field validation failed");
		}		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		moveScreenToModel();
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
	}

	private void moveModelToScreen() {
		// Set time fields
		Integer timeMarkingId = model.getCaseOnList().getTimeMarkingId();
		if (timeMarkingComboBx != null && timeMarkingId != null) {
			timeMarkingComboBx.setSelectedItemById(timeMarkingId);
		}

		// Set hour and minute if time marking set
		if (timeMarkingId != null && timeMarkingId > 0) {
			Integer hour = model.getCaseOnList().getTimeListedHour();
			if (timeListedHourText != null && hour != null) {
				if (hour < 10) {
					timeListedHourText.setText("0" + hour.toString());
				} else {
					timeListedHourText.setText(hour.toString());
				}
			}

			Integer min = model.getCaseOnList().getTimeListedMinute();
			if (timeListedMinText != null && min != null) {
				if (min < 10) {
					timeListedMinText.setText("0" + min.toString());
				} else {
					timeListedMinText.setText(min.toString());
				}
			}
		}

		// Display time estimate which is optional
		timeEstText.setText(ListingUtils.getTimeEstimateText(model.getCaseOnList().getDirectionsForCase()));

		// Display required judge which is optional
		if (refJudgeBasicValue != null) {
			judgeText.setText(StringUtils.join(new String[] { refJudgeBasicValue.getJudgeType(), refJudgeBasicValue.getTitle(),
											refJudgeBasicValue.getFirstName(), refJudgeBasicValue.getSurname()}, ' '));
		}

		// Check court room list if flag set to Y
		String isCourtRoomListEntryCase = model.getCaseOnList().getIsCourtRoomListEntry();
		includeInCourtRoomList.setSelected(YES.equals(isCourtRoomListEntryCase));

		// Hearing could be null if this dialog is being displayed as part of
		// dropping an 'Other Case' if it has no default hearing type
		if (model.getCaseOnList().getHearingType() != null) {
			hearingType.setText(model.getCaseOnList().getHearingType().getHearingTypeCode());
			hearingTypeCombo.setSelectedItemById(model.getCaseOnList().getHearingType().getId());
		}

		// Display defendants not listed on another case on list and then select ones currently listed for this case on list
		for (final DefendantValue defendant : defendantValues) {
			final String defendantName = defendant.getFirstName() + " " + defendant.getSurName();
			final boolean attending = model.getListModel().isDefendantListed(model.getCaseOnList(),
					defendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
			
			Boolean display = !model.getListModel().isDefendantNotListable(model.getCaseOnList(), defendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
			
			// Determine value to display in the Defendant Court Room List checkbox
			boolean isCourtRoomListEntryDef = false;
			if ( attending ) {
				// If the defendant is attending, determine the value for the Court Room List
				String defOnCourtList = model.getListModel().isDefendantOnCourtRoomList(model.getCaseOnList(),
						defendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
				if ( null == defOnCourtList ) {
					// No previous value set, use same value as attending if Case Court Room List is set, else false
					isCourtRoomListEntryDef = YES.equals(isCourtRoomListEntryCase) ? attending : false;
				}
				else {
					// Use whatever value is stored in the database
					isCourtRoomListEntryDef = YES.equals(defOnCourtList);
				}
			}
			
			defendantTableModel.addRow(display, new Object[] { defendantName, attending, isCourtRoomListEntryDef });
		}
		
		// List note fields
		Integer preDefinedId = model.getCaseOnList().getListNotePredefinedId();
		if (preDefinedId != null) {
			preDefined.setSelectedItemById(preDefinedId);
		}
		freeText.setText(model.getCaseOnList().getListNoteText());

		setMandatoryLabels();
		
		// Ensure correct state of save button
		setSaveEnabled();
		
		pageLoaded = true;
	}

	private void moveScreenToModel() {
		// Update the model from the screen
 		// set the dirty flags as saving
 		model.getCaseOnList().setDirty(true);
 		model.setDirty(true);

 		// if time marking selected, store selection and set hour and minute
 		if (timeMarkingComboBx.getSelectedIndex() > 0){
 	 		// Set the time marking id on case on list and basic value for display on row
 			RefSystemCodeBasicValue timeMarking = (RefSystemCodeBasicValue) timeMarkingComboBx.getSelectedItem();
 			model.getCaseOnList().setTimeMarkingId(timeMarking.getId()); 			
 			model.getCaseOnList().setTimeMarking(timeMarking);

 			// Set the hour and minute components of time listed
 			Integer hourInteger =  new Integer(timeListedHourText.getText()); 
 			Integer minInteger =  new Integer(timeListedMinText.getText());
 			model.getCaseOnList().setTimeListedHour(hourInteger);
 			model.getCaseOnList().setTimeListedMinute(minInteger);
 		}
 		else {
 			// no time entered but we still have to clear time marking and time
 			model.getCaseOnList().setTimeMarkingId(null);
 			model.getCaseOnList().setTimeMarking(null);
 			model.getCaseOnList().clearTimeListedTime();
 		}
 		
		// Set court room list to Y if checked
 		model.getCaseOnList().setIsCourtRoomListEntry(includeInCourtRoomList.isSelected() ? YES : NO);

 		// Set the hearing type id on case on list and basic value for display on row
 		RefHearingTypeBasicValue hearingType = (RefHearingTypeBasicValue) hearingTypeCombo.getSelectedItem();
 		model.getCaseOnList().setHearingTypeId(hearingType.getId());
 		model.getCaseOnList().setHearingType(hearingType);

		// Add/remove defendants that are listed on the case
		for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
			// Retrieve the defendant details and whether they are now attending
			DefendantValue defendantValue = ((List<DefendantValue>)defendantValues).get(row);
			Integer defendantOnCaseId = defendantValue.getDefOnCaseBasicValue().getDefendantOnCaseId();
			boolean attending = (Boolean)defendantTableModel.getValueAt(row, ATTENDING_COL_POSITION);
			String courtRoomList = (Boolean)defendantTableModel.getValueAt(row, COURTROOM_COL_POSITION) ? YES : NO;

			boolean isDefListed = model.getListModel().isDefendantListed(model.getCaseOnList(), defendantOnCaseId);
			// If they are now attending but are not currently listed, add them to the case on list
			if (attending && !isDefListed) {
				model.getListModel().addDefendant(model.getCaseOnList(), defendantOnCaseId, courtRoomList);
			}
			// Else if they are not attending but are currently listed, remove them from the case on list
			else if (!attending && isDefListed) {
				model.getListModel().removeDefendant(model.getCaseOnList(), defendantOnCaseId);
			}
			// Else if they are attending and are listed, update the Defendant in case Court Room List has changed
			else if ( attending && isDefListed ) {
				model.getListModel().updateDefendant(model.getCaseOnList(), defendantOnCaseId, courtRoomList);
			}
		}
		
		// List note fields
		Integer preDefinedId = ((RefListingDataBasicValue) preDefined.getSelectedItem()).getId();
		model.getCaseOnList().setListNotePredefinedId(preDefinedId);
		model.getCaseOnList().setListNoteText(freeText.getText());
	}
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(750, 450));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		 		
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		mainPanel.setPreferredSize(new Dimension(700, 400));
		this.add(scrollPane, gbc);

		// Time Panel Element
		gbc.gridwidth = 2;
		JPanel timeMarkingAndListingPanel = initTimeMarkingAndListingPanel();
		mainPanel.add(timeMarkingAndListingPanel, gbc);

		// Judge Panel Element
		gbc.gridy++;
		JPanel judgeSearchPanel = initJudgeSearchPanel();
		mainPanel.add(judgeSearchPanel, gbc);
			
		// Content Panel Element
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.weightx = 0.20;
		JPanel hearingPanel = initHearingTypePanel();
		mainPanel.add(hearingPanel, gbc);

		// Panel Attending Defendant
		gbc.gridx++;
		gbc.weighty=30;
		gbc.weightx = 0.80;
		JPanel attendingDefendantsPanel = initAttendingDefendantsPanel();
		mainPanel.add(attendingDefendantsPanel, gbc);

		// Panel - List Note
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		JPanel defaultNotePanel = initListNotePanel();
		mainPanel.add(defaultNotePanel, gbc);
	}
	
	private JPanel initJudgeSearchPanel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel judgeSearchPanel = new JPanel();
		judgeSearchPanel.setLayout(new GridBagLayout());

		//judge label
		gbc.weightx = 0.10;		
		JLabel judgeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsJudge"));
		judgeSearchPanel.add(judgeLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.80;
		// Panel - Judge text box
		judgeText = setXFieldForDisplay(NOT_EDITABLE, NO_FOCUS );
		judgeSearchPanel.add(judgeText, gbc);		
		
		// court room list which is only available for daily lists
		gbc.gridx++;
		gbc.weightx = 0.10;
		includeInCourtRoomList = new JCheckBox();
		includeInCourtRoomList.setEnabled(ListTypeEnum.Daily.equals(model.getListModel().getListType()));
		includeInCourtRoomList.setText(XHIBITConstant.getResource(XhibitBundles.Listings,"listingAdditionalDetailsCourtRoomList"));
		includeInCourtRoomList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox jb = (JCheckBox)e.getSource();
				if ( jb.isSelected() ) {
					// Checkbox has been ticked - set the Court List checkboxes to be the same value
					// as the Attending checkbox
					for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
						if ( (Boolean)defendantTableModel.getValueAt(row, ATTENDING_COL_POSITION) ) {
							defendantTableModel.setValueAt( true, row, COURTROOM_COL_POSITION);
						}
					}
				}
				else {
					// Checkbox has been unticked - untick all the Attending Defendant Court List checkboxes
					for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
						defendantTableModel.setValueAt(false, row, COURTROOM_COL_POSITION);
					}
				}
				defendantTable.repaint();
			}
			
		} );
		judgeSearchPanel.add(includeInCourtRoomList, gbc);		

		return judgeSearchPanel;
	}
	
	private JPanel initListNotePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel listNotePanel = new JPanel();
		listNotePanel.setBorder(BorderFactory
				.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsNote")));
		listNotePanel.setLayout(new GridBagLayout());

		// Add the panel elements
		gbc.weightx = 0.05;
		JLabel preDefinedLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsPreDefined"));
		listNotePanel.add(preDefinedLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.95;
		preDefined = new XComboBox();
		setDropdownBoxArray(preDefined, ListingDropdownPopulation.getPreDefinedNotes().toArray());
		listNotePanel.add(preDefined, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		JLabel freeTextLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsFreeText"));
		listNotePanel.add(freeTextLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0.95;
		freeText = new MultiLineEditField(2,200);
		freeText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_ENTER){			
					e.consume();
			    }
			}
		});
		listNotePanel.add(freeText, gbc);

		return listNotePanel;
	}
	
	private JPanel initAttendingDefendantsPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel attendingDefendantsPanel = new JPanel();
		attendingDefendantsPanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsDefendants")));
		attendingDefendantsPanel.setLayout(new GridBagLayout());

		// Table
		String attendingResourceKey = model.isBOrUCase() ? "listingAdditionalDetailsAttending" : "listingAdditionalDetailsAttendingMandatory";
		String[] columnHeaders = new String[] {
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsDefendant"),
				XHIBITConstant.getResource(XhibitBundles.Listings, attendingResourceKey),
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsCourtRoomList"),};

		// Add the panel elements
		defendantTableModel = new DefendantTableModel(new Object[][] {}, columnHeaders);
		defendantTable = XTableFactory.getInstance().createDefaultTable(defendantTableModel);
		defendantTable.getColumnModel().getColumn(COURTROOM_COL_POSITION).setCellRenderer(new CheckBoxCellRenderer());
		defendantTable.setRowSorter(new FilteredTableRowSorter(defendantTableModel));
		
		TableUtils.setupDefaultsOnJTable(defendantTable);
		defendantTable.getCellSelectionEnabled();
		
		// Add SPACE Key Binding to the table to handle the ticking/unticking of the checkbox in the table
		String tabActionName = "Space";
		defendantTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), tabActionName);
		defendantTable.getActionMap().put(tabActionName, new AttendingDefendantsSpaceAction());
		
		// Add listener to enable/disable save button and handle Court Room List checkboxes
		defendantTable.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){
				
				if ( e.getColumn() == COURTROOM_COL_POSITION ) {
					// Value for Court Room List has changed
					boolean anyCourtRoomSelected = false;
					for (int row = 0; row < defendantTableModel.getRowCount(); row++) {
						if ( (Boolean)defendantTableModel.getValueAt(row, COURTROOM_COL_POSITION) ) {
							// Have found at least one Defendant with the Court Room List checkbox ticked
							anyCourtRoomSelected = true;
							break;
						}
					}
					
					if ( !anyCourtRoomSelected ) {
						// No defendants have the court room list checkbox ticked so untick the Case level Court List Entry Checkbox
						includeInCourtRoomList.setSelected(false);
					}
				}
				else if ( e.getColumn() == ATTENDING_COL_POSITION ) {
					// Value for Attending has changed
					if ( (Boolean)defendantTableModel.getValueAt(e.getFirstRow(), ATTENDING_COL_POSITION) ) {
						// Attending tickbox was ticked, set the corresponding Court List checkbox to
						// the same value as the Case Court Room List checkbox
						defendantTableModel.setValueAt(includeInCourtRoomList.isSelected(),
								e.getFirstRow(), COURTROOM_COL_POSITION);
					}
					else {
						// Attending tickbox was unticked, set the corresponding Court List checkbox to
						// be unticked as well
						defendantTableModel.setValueAt(false, e.getFirstRow(), COURTROOM_COL_POSITION);
					}
				}
				
				setSaveEnabled();
			}
		});

		// Add Table to Scroll Pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(gbc.gridwidth, gbc.gridheight));
		scrollPane.setViewportView(defendantTable);
		attendingDefendantsPanel.add(scrollPane, gbc);

		return attendingDefendantsPanel;
	}
	
	/**
	 * Private class to render a checkbox properly when enabled/disabled
	 */
	private class CheckBoxCellRenderer implements TableCellRenderer {

	    private final JCheckBox renderer;

	    public CheckBoxCellRenderer() {
	        renderer = new JCheckBox();
	        renderer.setHorizontalAlignment(SwingConstants.CENTER);
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        Color bg = isSelected ? table.getSelectionBackground() : table.getBackground();
	        renderer.setBackground(bg);
	        renderer.setEnabled(table.isCellEditable(row, column));
	        renderer.setSelected(value != null && (Boolean)value);
	        return renderer;
	    }
	}
	
	/**
	 * Class representing the Defendants table model
	 */
	private class DefendantTableModel extends FilteredTableModel {

		private static final long serialVersionUID = 1L;

		public DefendantTableModel(Object[][] objects, String[] columnHeaders) {
			super(objects, columnHeaders);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == DEFENDANT_COL_POSITION) {
				// Disable the Defendant Name column
				return false;
			}
			else if (col == COURTROOM_COL_POSITION && !includeInCourtRoomList.isSelected() ) {
				// Court Room List column should be disabled if the Case Court Room List checkbox is unticked
				return false;
			}
			else if (col == COURTROOM_COL_POSITION && includeInCourtRoomList.isSelected() ) {
				// If Case Court Room List is ticked, Defendant Court Room List will be enabled only
				// if the defendant is attending
				return (Boolean)this.getValueAt(row, ATTENDING_COL_POSITION);
			}
			return super.isCellEditable(row, col);
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch (columnIndex) {
			case 1:
			case 2:
				return Boolean.class;
			default:
				return super.getColumnClass(columnIndex);
			}
		}
	};
	
	/**
	 * Action class for clicking the Space Key on the Attending Defendants table
	 */
	private class AttendingDefendantsSpaceAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			// Retrieve the currently selected row and the current value of the checkbox in that row
			int currentRow = defendantTable.getSelectedRow();
			int currentColumn = defendantTable.getSelectedColumn();
			
			// Currently selected column is the Attending column so toggle that checkbox
			if ( currentColumn == ATTENDING_COL_POSITION ) {
				// Get current value
				String attending = ( (Boolean) defendantTableModel.getValueAt(currentRow, ATTENDING_COL_POSITION) ) ? YES : NO;
				
				if ( YES.equals(attending) ) {
					// Checkbox is currently ticked so untick it
					defendantTableModel.setValueAt(false, currentRow, ATTENDING_COL_POSITION);
				}
				else {
					// Checkbox is not currently ticked so tick it
					defendantTableModel.setValueAt(true, currentRow, ATTENDING_COL_POSITION);
				}
			}
			// Currently selected column is the Attending column so toggle that checkbox if possible
			else if ( currentColumn == COURTROOM_COL_POSITION &&
					  defendantTableModel.isCellEditable(currentRow, COURTROOM_COL_POSITION) ) {
				// Get current value
				String courtRoomList = ( (Boolean) defendantTableModel.getValueAt(currentRow, COURTROOM_COL_POSITION) ) ? YES : NO;
				
				if ( YES.equals(courtRoomList) ) {
					// Checkbox is currently ticked so untick it
					defendantTableModel.setValueAt(false, currentRow, COURTROOM_COL_POSITION);
				}
				else {
					// Checkbox is not currently ticked so tick it
					defendantTableModel.setValueAt(true, currentRow, COURTROOM_COL_POSITION);
				}
			}
		}
	}
	
	/**
	 * Determines whether or not at least one defendant is attending the fixture
	 * @return true if at least one defendant is attending the fixture, else false
	 */
	private boolean isDefendantAttending(){
		boolean attending = false ;
		int numberOfDefendants = defendantTableModel.getRowCount();
		
		//B and U cases have no defendants.
		if (numberOfDefendants == 0) {
			return true;
		}
		
		for (int row = 0; row < numberOfDefendants; row++) {
			if ( (Boolean) defendantTableModel.getValueAt(row, ATTENDING_COL_POSITION) ) {
				attending = true;
				break;
			}
		}
		return attending;
	}
		
	
	private JPanel initHearingTypePanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		

		JPanel hearingTypePanel = new JPanel();
		hearingTypePanel.setBorder(BorderFactory.createTitledBorder(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsHearingType")));
		hearingTypePanel.setLayout(new GridBagLayout());

		gbc.weightx = 0.60;
		hearingType = new XTextField();
		hearingType.setUpperCase(true);
		hearingType.setMaxLength(3);
		hearingTypePanel.add(hearingType, gbc);

		gbc.gridx++;
		gbc.weightx = 0.40;
		verifyHearingTypeButton = new JButton(new VerifyHearingTypeButtonAction());

		verifyHearingTypeButton.setEnabled(true);
		hearingType.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				if (hearingType.getText().matches(regex)) {
					verifyHearingTypeButton.setEnabled(true);
				} else {
					verifyHearingTypeButton.setEnabled(false);
				}
			}
		});

		hearingTypePanel.add(verifyHearingTypeButton, gbc);

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 1;
		gbc.gridwidth = 2;
		hearingTypeCombo = new XComboBox();

		setDropdownBoxArray(hearingTypeCombo, ListingDropdownPopulation.getHearingTypes().toArray());
		hearingTypePanel.add(hearingTypeCombo, gbc);
		hearingTypeCombo.addActionListener(new HearingTypeComboBoxAction());
		hearingTypeCombo.setPreferredSize(new Dimension(200, hearingTypeCombo.getPreferredSize().height));

		return hearingTypePanel;
	}
	
	
	/**
	 * Set the array and if the array is empty disable the dropdown box
	 */
	private void setDropdownBoxArray(XComboBox comboBox, final Object[] arrayItems) {
		comboBox.setModel(new DefaultComboBoxModel(arrayItems));
		if (comboBox.getSelectedItem() != null) {
			comboBox.setRenderer(new DropdownBoxCellRender());
			comboBox.enableAutoSelect();
		} else {
			comboBox.setEnabled(false);
		}
	}


	private class HearingTypeComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			RefHearingTypeBasicValue selectedHearingType = (RefHearingTypeBasicValue) hearingTypeCombo
					.getSelectedItem();
			String errorMsg = MessageFormat.format(XHIBITConstant.getResource(XhibitBundles.ErrorText, 
					"listings.validation.hearingType.notKnown"), new Object[] { hearingType.getText()});
			hearingType.setText(selectedHearingType != null ? selectedHearingType.getHearingTypeCode() : null);
			if (!ValidationUtils.hasText(hearingType)) {
				XMessageBox.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.XhibitConstant, "exception.validation.title"), true,
						XMessageBox.ICONERROR, errorMsg, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			}
			else if ( pageLoaded // Check page has loaded i.e. don't perform when screen opens
					&& null != selectedHearingType	// Hearing Type has been selected
					&& selectedHearingType.getHearingTypeCode().equals(HRG_TYPE_TRN) // New value is TRN
					) {
				XMessageBox.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.additionaldetails.trn.warning.title"), 
						true,
						XMessageBox.ICONWARNING, 
						XHIBITConstant.getResource(XhibitBundles.ErrorText, "listings.additionaldetails.trn.warning.message"), 
						XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
			}

			setSaveEnabled();
		}
		
	};
	
	private class VerifyHearingTypeButtonAction extends XAction {

		private static final long serialVersionUID = 1L;

		private Integer getComboBoxModelIndexForCode(ComboBoxModel model, String selectedCode) {
			if (model != null && selectedCode != null) {
				String elementCode = null;
				for (int i = 0; i < model.getSize(); i++) {
					elementCode = ((RefHearingTypeBasicValue) model.getElementAt(i)).getHearingTypeCode();
					if (elementCode != null && elementCode.equals(selectedCode)) {
						return i;
					}
				}
			}
			return null;
		}

		public VerifyHearingTypeButtonAction() {
			populateFromBundle("listingAdditionalDetailsHearingVerify");
		}

		@Override
		public void xActionPerformed(ActionEvent ae) throws Exception {
			Integer selectedIndex = getComboBoxModelIndexForCode(hearingTypeCombo.getModel(), hearingType.getText());
			hearingTypeCombo.setSelectedIndex(selectedIndex != null ? selectedIndex : 0);
		}
	};
	
	private JPanel initTimeMarkingAndListingPanel() {
		// Time marking and time listed disabled if Warned list
		boolean enableTime = !ListTypeEnum.Warned.equals(model.getListModel().getListType());
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel timeMarkingAndListingPanel = new JPanel();
		timeMarkingAndListingPanel.setLayout(new GridBagLayout());
		
		// validation labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		JLabel timeMarkingError = new JLabel(" ");
		timeMarkingAndListingPanel.add(timeMarkingError, gbc);
		
		gbc.gridx = 3;
		gbc.gridwidth = 5;
		JLabel timeError = new JLabel(" ");
		timeMarkingAndListingPanel.add(timeError, gbc);
		
		gbc.gridwidth = 1;
		gbc.insets = XHIBITConstant.nonContainerInsets;

		//time marking label
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.10;		
		timeMarkingAndListingPanel.add(getTimeMarkingLabel(), gbc);

		gbc.gridx++;
		gbc.weightx = 0.10;
		// Panel - time marking comboBox
		addTimeMarkingComboBox(gbc, timeMarkingAndListingPanel, enableTime);
		
		timeMarkingVC = ValidationControllerFactory.createComboBox(this, timeMarkingComboBx, timeMarkingError,
				new AbstractComboBoxValidator() {
					@Override
					public void validate(JComboBox target, List<String> errors) {
						setMandatoryLabels();
						if (!hasSelection(target) && eitherTimeComponentEntered()) {
							errors.add("Mandatory if time entered");
						} else if (hasSelection(target) && !isTimeEntered()) {
							errors.add("Clear selection if time not entered");
						}
					}
				});
		validationControllers.add(timeMarkingVC);
		
		// Panel - time text boxes
		gbc.gridx++;
		gbc.weightx = 0.10;
		timeMarkingAndListingPanel.add(getTimeListedLabel(), gbc);
		
		// HOUR text box
		gbc.gridx++;
		gbc.weightx = 0.08;
		timeListedHourText = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		timeListedHourText.setNumeric(true);
		timeListedHourText.setMaxLength(TIME_FIELD_SIZE);
		timeListedHourText.setColumns(TIME_FIELD_SIZE);
		timeListedHourText.setEnabled(enableTime);
		timeMarkingAndListingPanel.add(timeListedHourText, gbc);
		
		timeListedHourVC = ValidationControllerFactory.createText(this, timeListedHourText, timeError,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						setMandatoryLabels();
						Integer timeInteger = null;
						if (ValidationUtils.hasText(target)){
							timeInteger =  new Integer(target.getText());
							if (timeInteger < 10){
								target.setText("0" + timeInteger.toString());
							}
						}
						if(timeInteger != null &&  timeInteger > 23){
							errors.add("Invalid time");
						}
						else {	
							timeMarkingVC.validate();							
						}
					}
				});
		validationControllers.add(timeListedHourVC);
		
		//time divider label
		gbc.gridx++;
		gbc.weightx = 0.01;		
		JLabel timeDividerLabel = new JLabel(":");
		timeMarkingAndListingPanel.add(timeDividerLabel, gbc);
		
		// MINUTE text box
		gbc.gridx++;
		gbc.weightx = 0.08;
		timeListedMinText = setXFieldForDisplay(ALLOW_EDITABLE, ALLOW_FOCUS );
		timeListedMinText.setNumeric(true);
		timeListedMinText.setMaxLength(TIME_FIELD_SIZE);
		timeListedMinText.setColumns(TIME_FIELD_SIZE);
		timeListedMinText.setEnabled(enableTime);
		timeMarkingAndListingPanel.add(timeListedMinText, gbc);	
		
		timeListedMinVC = ValidationControllerFactory.createText(this, timeListedMinText, timeError,
				new AbstractTextValidator() {
					@Override
					public void validate(JTextComponent target, List<String> errors) {
						setMandatoryLabels();
						Integer timeInteger = null;
						if (ValidationUtils.hasText(target)){
							timeInteger =  new Integer(target.getText());
							if (timeInteger < 10){
								// set time to be 2 digits	
								target.setText("0" + timeInteger.toString());
							}
						}
						if(timeInteger != null &&  timeInteger > 59){									
							errors.add("Invalid time "); 
						}
						else {	
							timeMarkingVC.validate();					
						}
					}
				});
		validationControllers.add(timeListedMinVC);		
		
		gbc.gridx++;
		gbc.weightx = 0.10;
		JLabel timeEstLabel = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingAdditionalDetailsTimeEstimate"));
		timeMarkingAndListingPanel.add(timeEstLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.15;
		timeEstText  = setXFieldForDisplay(NOT_EDITABLE, NO_FOCUS );
		timeEstText.setMaxLength(20);
		timeEstText.setText("Test");
		
		timeMarkingAndListingPanel.add(timeEstText, gbc);
			
		// Panel - blank Panel to align fields
		gbc.gridx++;
		gbc.weightx = 0.53;
		gbc.anchor = GridBagConstraints.EAST;
		
		JPanel blank1Panel = new JPanel();
		timeMarkingAndListingPanel.add(blank1Panel, gbc);
		
		return timeMarkingAndListingPanel;
	}

	private JLabel getTimeMarkingLabel() {
		if (timeMarkingLabel == null) {
			setTimeMarkingLabel(false);
		}
		return timeMarkingLabel;
	}
	
	private JLabel getTimeListedLabel() {
		if (timeListedLabel == null) {
			setTimeListedLabel(false);
		}
		return timeListedLabel;
	}
	
	private String getResourceBundle(String resourceKey) {
		return XHIBITConstant.getResource(XhibitBundles.Listings, resourceKey);
	}
	
	private void setTimeMarkingLabel(boolean isMandatory) {
		String text = getResourceBundle(isMandatory ? "listingAdditionalDetailsTimeMarkingMandatory" : "listingAdditionalDetailsTimeMarking");
		if (timeMarkingLabel == null) {
			timeMarkingLabel = new JLabel(text);
		} else {
			timeMarkingLabel.setText(text);
		}
	}
	
	private void setTimeListedLabel(boolean isMandatory) {
		String text = getResourceBundle(isMandatory ? "listingAdditionalDetailsTimeListedMandatory" : "listingAdditionalDetailsTimeListed");
		if (timeListedLabel == null) {
			timeListedLabel = new JLabel(text);
		} else {
			timeListedLabel.setText(text);
		}
	}
	
	private void setMandatoryLabels() { 
		boolean isTimeMandatory = timeMarkingComboBx.getSelectedIndex() > 0 || eitherTimeComponentEntered();
		setTimeMarkingLabel(isTimeMandatory);
		setTimeListedLabel(isTimeMandatory);
	}
	
	private void addTimeMarkingComboBox(GridBagConstraints gbc, JPanel timeMarkingAndListingPanel, boolean enableTime) {
		ArrayList<RefSystemCodeBasicValue> timeMarkingArray = ListingDropdownPopulation.getTimeFormatTypes();
		timeMarkingComboBx = new XComboBox();
		timeMarkingComboBx.setEnabled(enableTime);
		setDropdownBoxArray(timeMarkingComboBx, timeMarkingArray.toArray());
		timeMarkingComboBx.addActionListener(new XAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void xActionPerformed(ActionEvent e) throws Exception {
				setMandatoryLabels();
			}
		});
		timeMarkingAndListingPanel.add(timeMarkingComboBx, gbc);
	}

	private XTextField setXFieldForDisplay(boolean editable, boolean focusable) {
		XTextField textField = new XTextField();
		textField.setEditable(editable);
		textField.setFocusable(focusable);
		return textField;
	}

	/**
	 * valid if both time field has a value
	 * 
	 * @return time entered
	 */
	private boolean isTimeEntered() {
		return ValidationUtils.hasText(timeListedHourText) && ValidationUtils.hasText(timeListedMinText);
	}

	/**
	 * valid if either time field has a value
	 * 
	 * @return either time entered
	 */
	private boolean eitherTimeComponentEntered() {
		return ValidationUtils.hasText(timeListedHourText) || ValidationUtils.hasText(timeListedMinText);
	}
	
	/**
	 * Enable save button if all fields are valid
	 */
	private void setSaveEnabled() {
		// Check if at least one defendant selected, time marking fields valid and hearing type selected
		boolean attending = isDefendantAttending();
		boolean validators = ValidationControllerFactory.validateComponents(validationControllers);
		boolean isValid = (attending && validators && hearingTypeCombo.getSelectedIndex() != 0);
	
		// Enable OK button if everything valid
		OkCancelPanel buttonPanel = (OkCancelPanel) parent.getButtonPanel();
		buttonPanel.okButton.setEnabled(isValid);			
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		// Only enable save if all fields are valid on dialog
		setSaveEnabled();
		
		// Time validation is special case as the two fields share the same error label,
		// so if no errors on the field just validated, ensure any error on other field
		// is still displayed to the user
		if (timeListedHourVC.equals(validationController) && !timeListedHourVC.hasErrors()) {
			timeListedMinVC.showErrors();
		} else if (timeListedMinVC.equals(validationController) && !timeListedMinVC.hasErrors()) {
			timeListedHourVC.showErrors();
		}
		
	}
	
	/**
	 * Filtered Table Model
	 */
	private class FilteredTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 1L;
		private List<Boolean> rowIncluded = new ArrayList<Boolean>();

		public FilteredTableModel(Object[][] objects, String[] columnHeaders) {
			super(objects, columnHeaders);
		}

		public void addRow(Boolean include, Object[] rowData) {
			rowIncluded.add(include);
			super.addRow(rowData);
		}
		
		@Override
		public void removeRow(int row) {
			rowIncluded.remove(row);
			super.removeRow(row);
		}
		
		public boolean isInclude(int rowNo) {
			return rowIncluded.get(rowNo);
		}
	}

	/**
	 * Filtered Table Row Sorter
	 */
	public class FilteredTableRowSorter extends TableRowSorter<FilteredTableModel> {
		public FilteredTableRowSorter(FilteredTableModel model) {
			super(model);
			setRowFilter(new FilteredTableRowFilter());
		}
	}

	/**
	 * Filtered Table Row Filter
	 */
	public class FilteredTableRowFilter extends RowFilter<FilteredTableModel, Integer> {
		@Override
		public boolean include(RowFilter.Entry<? extends FilteredTableModel, ? extends Integer> entry) {	
			return entry.getModel().isInclude(entry.getIdentifier());
		}
	}

}
