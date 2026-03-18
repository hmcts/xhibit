package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import static uk.gov.courtservice.framework.util.DateTimeUtilities.convertToCalendar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.listings.ListingDropdownPopulation;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XComboBox;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrBeforeTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationUtils;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Screen to enter Court Room Stats into the 
 * Judge Usage Table and Court Room Usage Table.
 * @author westalll
 *
 */
public class RecordCourtroomStatisticsPanel extends XPanel implements ValidationListener{

	private static final long serialVersionUID = 1L;
	
	private RecordCourtroomStatisticsModel model;
	private XDialog parent;
	
	private XDatePanelWithEvent sittingDatePanel;

	private XComboBox siteCombo;
	private XComboBox courtroomCombo;
	
	private JTable judgeUsageTable;
	private JTable courtRoomUsageTable;

	private JudgeUsageTableModel judgeUsageTableModel;
	private CourtRoomUsageTableModel courtRoomUsageTableModel;
	
	private JButton autoPopulateButton;
	private JButton addCourtRoomUsageButton;
	private JButton addJudgeUsageButton;
	
	private JLabel sittingDateError;
	
	private DateValidationController sittingDateValidator;
	protected List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
	
	public RecordCourtroomStatisticsPanel(final XDialog parent, final RecordCourtroomStatisticsModel model)
			throws CSRecoverableException {
		this.parent = parent;
		this.model = model;

		stepInitialise();
		jbInit();
		stepUpdateViewState();
	}

	@Override
	public void stepInitialise() throws CSRecoverableException {
		initSite();
		initAndRefreshRooms();
		
		// Need to set model sitting date today's date with no timestamp to return data for today when load screen
		model.setSittingDate(DateTimeUtilities.stripTimeToUtilDate(new Date()));
		
		// Set save to be disabled on start up.
		getButtonPanel().okButton.setEnabled(false);
	}

	private void initAndRefreshRooms() {
		XhbCourtRoomBasicValue courtRooms[] = getCourtRooms();
		courtroomCombo = new XComboBox(new DefaultComboBoxModel(courtRooms));
		courtroomCombo.setRenderer(new CourtroomCellRenderer());
	}
	
	private XhbCourtRoomBasicValue[] getCourtRooms() {
		CourtStructureValue courtStructure = XhibitSingleton.getInstance().getCourtStructureValue();
		CourtSiteBasicValue site = (CourtSiteBasicValue) siteCombo.getSelectedItem();
		return courtStructure.getCourtRoomsForSite(site.getId());
	}

	private void refreshJudgeUsageTableModelData() {
		// Clear the current table model data as reloading everything
		judgeUsageTableModel.setData((Object[]) null);
		if ( isSittingDateValid() ) {
			try {
				XhbCourtRoomBasicValue courtRooms[] = getCourtRooms();
	
				for (XhbCourtRoomBasicValue room : courtRooms) {
					@SuppressWarnings("unchecked")
					Collection<JudgeUsageComplexValue> values = XhibitDelegateHelper.getBizRefDelegate()
							.findJudgeUsageByDateAndRoom(model.getSittingDate(), room.getCourtRoomId());
	
					for (final JudgeUsageComplexValue value : values) {
						judgeUsageTableModel.addCase(createJudgeUsageRowFromComplexValue(value));
					}
				}
			} catch (SysRefControllerException e) {
				XHIBITConstant.handleError(e);
		}
		}
	}

	private JudgeUsageRow createJudgeUsageRowFromComplexValue(final JudgeUsageComplexValue value) {
		JudgeUsageRow row = new JudgeUsageRow();
		row.setJudgeUsageId(value.getJudgeUsageId());
		row.setJudgeId(value.getJudgeUsageId());
		row.setCourtChambersInd(value.getCourtChambersInd());
		row.setCourtRoomId(value.getCourtRoomId());
		row.setJudgeId(value.getRefJudgeId());
		row.setSittingDate(value.getSittingDate());
		row.setCourtRoomName(value.getCourtRoom().getDisplayName());
		setjudgeName(value, row);
		return row;
	}
	
	private void refreshCourtRoomUsageTableModelData() throws CSValidationException {
		// Clear the current table model data as reloading everything
		courtRoomUsageTableModel.setData((Object[]) null);
		if ( isSittingDateValid() ) {
			try {
				XhbCourtRoomBasicValue courtRooms[] = getCourtRooms();
				for (XhbCourtRoomBasicValue room : courtRooms) {
	
					@SuppressWarnings("unchecked")
					Collection<CourtRoomUsageComplexValue> values = XhibitDelegateHelper.getBizRefDelegate()
							.findCourtRoomUsageByDateAndRoom(model.getSittingDate(), room.getCourtRoomId());
	
					for (final CourtRoomUsageComplexValue value : values) {
						courtRoomUsageTableModel.addCase(createCourtRoomUsageRowFromComplexValue(value));
					}
				}
			} catch (SysRefControllerException e) {
				XHIBITConstant.handleError(e);
			}
		}
	}

	private CourtRoomUsageRow createCourtRoomUsageRowFromComplexValue(final CourtRoomUsageComplexValue value) {
		final CourtRoomUsageRow row = new CourtRoomUsageRow();

		row.setAmHours(value.getAmTimeHours());
		row.setAmMins(value.getAmTimeMins());
		row.setCourtRoomId(value.getCourtRoomId());
		row.setCourtRoomName(value.getCourtRoom().getDisplayName());
		row.setCourtRoomUsageId(value.getCourtRoomUsageId());
		row.setPmHours(value.getPmTimeHours());
		row.setPmMins(value.getPmTimeMins());
		row.setSittingDate(value.getSittingDate());
		return row;
	}

	private void setjudgeName(final JudgeUsageComplexValue value, JudgeUsageRow row) {
		final RefJudgeBasicValue judge = value.getJudge();
		if (judge != null) {
			row.setJudgeName(judge.getTitle() + " " + judge.getFirstName() + " " + judge.getSurname());
		}
	}

	private void initSite() {
		siteCombo = new XComboBox(new DefaultComboBoxModel(ListingDropdownPopulation.getCourtSites().toArray()));
		siteCombo.setRenderer(new SiteCellRenderer());
		siteCombo.addActionListener(new SiteComboBoxAction());
	}
	
	class SiteCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
	            boolean cellHasFocus) {
	        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, cellHasFocus);
	     	        
	        CourtSiteBasicValue courtSite = (CourtSiteBasicValue) value;
	        final String text = courtSite.getCourtSiteCode() + " " +courtSite.getCourtSiteName();
	        
	        label.setText(text);
	        return label;
	    }
	};
	
	class CourtroomCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
	            boolean cellHasFocus) {
	        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, selected, cellHasFocus);
	        
	        XhbCourtRoomBasicValue room = (XhbCourtRoomBasicValue) value;
	        final String text = room.getCourtRoomId() + " : " +room.getCourtRoomName();
	        
	        label.setText(text);
	        return label;
	    }
	};
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(950, 550));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		 		
		// Setup a main parent panel with vertical and horizontal scrollbars to prevent
		// resizing of components when the window size is reduced.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.add(scrollPane, gbc);
		
		mainPanel.add(getSittingDateAndCourtSitePanel(), gbc);
		
		//Court Room Usage Table.
		gbc.gridy++;
		JPanel courtRoomUsagePanel = initCourtRoomUsagePanel();
		mainPanel.add(courtRoomUsagePanel, gbc);
		
		//Judge Usage Table
		gbc.gridy++;
		JPanel judgeUsagePanel = initJudgeUsagePanel();
		mainPanel.add(judgeUsagePanel, gbc);	
		
		addDataChangedListeners();	
	}
	
	private JButton getSearchButton() {
    	if (autoPopulateButton == null) {
    		autoPopulateButton = new JButton(new OtherCasesSearchAction());
    	}
    	return autoPopulateButton;
    }
	
	private class OtherCasesSearchAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OtherCasesSearchAction() {
			populateFromBundle("RCSAutoFill");
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			
			final Integer courtId = XhibitSingleton.getInstance().getCourtId();
			
			Calendar cal = Calendar.getInstance();
	    	cal.setTime(model.getSittingDate());
	    	cal.add(Calendar.DAY_OF_MONTH, 2);
	    	
			XhibitDelegateHelper.getBizRefDelegate().generateRCSRecord(courtId, model.getSittingDate(), cal.getTime());
			stepUpdateViewState();
		}
	}
	
	private JButton getAddCourtRoomUsageButton() {
		if (addCourtRoomUsageButton == null) {
			addCourtRoomUsageButton = new JButton(new AddCourtRoomUsageAction());
    	}
    	return addCourtRoomUsageButton;
	}
	
	/**
	 * Class to handle the clicking of the Add Court Room Usage Button
	 * This should launch the Add popup for the user to enter details
	 * and refresh data upon closing.
	 */
	private class AddCourtRoomUsageAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddCourtRoomUsageAction() {
			populateFromBundle("RCSAddCourtRoomUsage");
		}
		
		private void handlePopup() throws CSRecoverableException {
			// Get the court site id and sitting date and pass to the model of the Add Court Room screen
			AddCourtRoomUsageModel acruModel = new AddCourtRoomUsageModel();
			acruModel.setSittingDate(model.getSittingDate());
			acruModel.setCourtSiteId( ((CourtSiteBasicValue) siteCombo.getSelectedItem()).getId() );
			AddCourtRoomUsageDialog acruDialog = new AddCourtRoomUsageDialog(parent, acruModel);
			acruDialog.setVisible(true);
			
			// On return, refresh screen
			refreshCourtRoomUsageTableModelData();
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			if (isDirty()) {
				boolean navigateAway = showCancelConfirmationMsg();
				if (navigateAway) {
					handlePopup();
				}
			}
			else {
				handlePopup();
			}
		}
	}
	
	private JButton getAddJudgeUsageButton() {
		if (addJudgeUsageButton == null) {
			addJudgeUsageButton = new JButton(new AddJudgeUsageAction());
    	}
    	return addJudgeUsageButton;
	}
	
	/**
	 * Class to handle the clicking of the Add Judge Usage Button
	 * This should launch the Add popup for the user to enter details
	 * and refresh data upon closing.
	 */
	private class AddJudgeUsageAction extends XAction {

		private static final long serialVersionUID = 1L;

		public AddJudgeUsageAction() {
			populateFromBundle("RCSAddJudgeUsage");
		}
		
		private void handlePopup() throws CSRecoverableException {
			// Get the court site id and sitting date and pass to the model of the Add Judge Usage screen
			AddJudgeUsageModel ajuModel = new AddJudgeUsageModel(model.getXac());
			ajuModel.setSittingDate(model.getSittingDate());
			ajuModel.setCourtSiteId( ((CourtSiteBasicValue) siteCombo.getSelectedItem()).getId() );
			AddJudgeUsageDialog ajuDialog = new AddJudgeUsageDialog(parent, ajuModel);
			ajuDialog.setVisible(true);
			
			// On return, refresh screen
			refreshJudgeUsageTableModelData();
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			if (isDirty()) {
				boolean navigateAway = showCancelConfirmationMsg();
				if (navigateAway) {
					handlePopup();
				}
			}
			else {
				handlePopup();
			}
		}
	}

	private void addDataChangedListeners() {
		judgeUsageTableModel.addTableModelListener(getTableChangeController());
		courtRoomUsageTableModel.addTableModelListener(getTableChangeController());
	}
	
	private JPanel initJudgeUsagePanel() {
		judgeUsageTableModel = new JudgeUsageTableModel();
		judgeUsageTable = XTableFactory.getInstance().createDefaultTable(judgeUsageTableModel);
		TableUtils.setupDefaultsOnJTable(judgeUsageTable);
		judgeUsageTable.setAutoCreateRowSorter(true);
		judgeUsageTable.getRowSorter().setSortKeys(
				Arrays.asList(new RowSorter.SortKey(JudgeUsageTableModel.COL_COURT_ROOM_NAME, SortOrder.ASCENDING)));

		final RecordCourtroomStatisticsPopupHelper helper = new RecordCourtroomStatisticsPopupHelper(parent);
		helper.addEntryAndSummaryPopupMenu(judgeUsageTable, judgeUsageTableModel);
		
		judgeUsageTableModel.setColumnWidths(judgeUsageTable);
		
		JScrollPane scrollPanel = new JScrollPane(judgeUsageTable);
		scrollPanel.setPreferredSize(judgeUsageTable.getPreferredSize());
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(900, 200));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  XHIBITConstant.nonContainerInsets, 0, 0);
		gbc.gridwidth = 2;
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,"RecordCourtroomStatistics.judgeUsage")));
		gbc.weighty = 0.95;
		panel.add(scrollPanel, gbc);
		
		gbc.weighty = 0.05;
		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.01;
		panel.add(getAddJudgeUsageButton(), gbc);
		gbc.gridx++;
		gbc.weightx = 0.99;
		panel.add(getSearchButton(), gbc);
		
		this.refreshJudgeUsageTableModelData();
		return panel;
	}
	
	
	private JPanel initCourtRoomUsagePanel() {
		courtRoomUsageTableModel = new CourtRoomUsageTableModel(parent);
		courtRoomUsageTable = new XTable(courtRoomUsageTableModel) {
		    /**
		     * Override prepareRenderer to set the tool tip text for individual cells in the 
		     * table (not the header)
		     */
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        Component c = super.prepareRenderer(renderer, row, column);
		        if (c instanceof JComponent) {
		            JComponent jc = (JComponent) c;
		            if ( column == CourtRoomUsageTableModel.COL_AM_TIME || column == CourtRoomUsageTableModel.COL_PM_TIME ) {
		            	// Only want tool tips on the AM Hours:Mins and PM Hours:Mins columns
		            	jc.setToolTipText(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.ampmcolumn.tooltip"));
		            }
		            else {
		            	jc.setToolTipText(null);
		            }	
		        }
		        return c;
		    }
		};
		courtRoomUsageTable.getTableHeader().setReorderingAllowed(false);
		TableUtils.setupDefaultsOnJTable(courtRoomUsageTable);
		
		courtRoomUsageTable.setAutoCreateRowSorter(true);
		courtRoomUsageTable.getRowSorter().setSortKeys(Arrays
				.asList(new RowSorter.SortKey(CourtRoomUsageTableModel.COL_COURT_ROOM_NAME, SortOrder.ASCENDING)));
		
		final RecordCourtroomStatisticsPopupHelper helper = new RecordCourtroomStatisticsPopupHelper(parent);
		helper.addEntryAndSummaryPopupMenu(courtRoomUsageTable, courtRoomUsageTableModel);
		
		JScrollPane scrollPanel = new JScrollPane(courtRoomUsageTable);
		scrollPanel.setPreferredSize(courtRoomUsageTable.getPreferredSize());
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(900, 200));
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,  XHIBITConstant.nonContainerInsets, 0, 0);
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,"RecordCourtroomStatistics.courtRoomUsage")));
		gbc.weighty = 0.95;
		panel.add(scrollPanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.05;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(getAddCourtRoomUsageButton(), gbc);
		
		return panel;
	}
	
	@Override
	public void stepActivate() throws CSRecoverableException {
		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		refreshJudgeUsageTableModelData();
		refreshCourtRoomUsageTableModelData();
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		
	}
	

	@Override
	public void stepDeactivate() throws CSRecoverableException {
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if (!update) {
			if (isDirty()) {
				showCancelConfirmationMsg();
			}
		} else {
			final String userName = XhibitSingleton.getInstance().getUserSession()
					.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

			if (getButtonPanel().okButton.equals(getDeinitialiseSource())) {
				
				updateJudgeUsage(userName);
				updateCourtRoomUsage(userName);
				displaySaveSuccess();
				refreshCourtRoomUsageTableModelData();
				refreshJudgeUsageTableModelData();
				getButtonPanel().okButton.setEnabled(false);
				
				throw new UserCancelException();	
			}
		}
	}
	
	private boolean isDirty() {
		
		//Check the Judge Usage Objects
		for (final Object obj : judgeUsageTableModel.getDataAsCollection()) {
			final JudgeUsageRow row = (JudgeUsageRow) obj;
			if (row.isDirty()) {
				return true;
			}
		}
		//Check the Court Room Usage Objects
		for (final Object obj : courtRoomUsageTableModel.getDataAsCollection()) {
			final CourtRoomUsageRow row = (CourtRoomUsageRow) obj;
			if (row.isDirty()) {
				return true;
			}
		}
		//No Changes found.
		return false;
	}

	private void updateJudgeUsage(final String userName) {
		for (final Object obj : this.judgeUsageTableModel.getDataAsCollection()) {
			final JudgeUsageRow row = (JudgeUsageRow) obj;
			if (row.isDirty()) {
				final JudgeUsageComplexValue complex = new JudgeUsageComplexValue();
				complex.setJudgeUsageId(row.getJudgeUsageId());
				complex.setCourtChambersInd(row.getCourtChambersInd());
				
				XhibitDelegateHelper.getBizRefDelegate().updateJudgeUsageCourtChambersInd(complex, userName);
			}
		}
	}

	private void updateCourtRoomUsage(final String userName) {
		for (final Object obj : courtRoomUsageTableModel.getDataAsCollection()) {
			final CourtRoomUsageRow row = (CourtRoomUsageRow) obj;
			if (row.isDirty()) {
				CourtRoomUsageComplexValue complex = new CourtRoomUsageComplexValue();
				complex.setAmTimeHours(row.getAmHours());
				complex.setAmTimeMins(row.getAmMins());
				complex.setPmTimeHours(row.getPmHours());
				complex.setPmTimeMins(row.getPmMins());
				complex.setCourtRoomUsageId(row.getCourtRoomUsageId());

				XhibitDelegateHelper.getBizRefDelegate().updateCourtRoomUsageTime(complex, userName);
			}
		}
	}
	
	private void displaySaveSuccess() {
		XMessageBox
				.alert(parent,
						XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
								"RecordCourtroomStatisticsSaveSuccessTitle"),
						true, XMessageBox.ICONINFORMATION,
						XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
								"RecordCourtroomStatisticsSaveSuccessMesssage"),
						XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	}
	
	private OkCancelPanel getButtonPanel() {
		return (OkCancelPanel) parent.getButtonPanel();
	}
	
	/**
	 * Display the Cancel Confirmation popup message when exit screen or change search fields
	 * 
	 * @throws CSRecoverableException
	 */
	private boolean showCancelConfirmationMsg() throws CSRecoverableException {
		boolean messageBoxReply = XMessageBox.alert(parent,
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
						"RecordCourtroomStatisticsCancelConfirmationTitle"),
				true, XMessageBox.ICONQUESTION,
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
						"RecordCourtroomStatisticsCancelConfirmationMessage"),
				XMessageBox.YESNO, XMessageBox.DEFAULTCANCEL);

		if (!messageBoxReply) {
			throw new UserCancelException();
		}
		return messageBoxReply;
	}
	
	/**
	 * Setup of the sitting date/court site panel
	 * @return JPanel
	 */
	private  JPanel getSittingDateAndCourtSitePanel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel Outerpanel = new JPanel();
		Outerpanel.setLayout(new GridBagLayout());
		
		// Add the error labels
		gbc.insets = XHIBITConstant.errorLabelInsets;
		gbc.gridwidth = 5;
		sittingDateError = new JLabel(" ");
		Outerpanel.add(sittingDateError, gbc);
		gbc.gridwidth = 1;
		gbc.gridy++;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		
		gbc.weightx = 0.05;
		Outerpanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.sittingdate.label")), gbc);
		
		gbc.weightx = 0.2;
		gbc.gridx++;
		sittingDatePanel = new XDatePanelWithEvent(Outerpanel, convertToCalendar(model.getSittingDate()), true) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void fireEvent() {
				if ( ValidationUtils.hasDate(this) ) {
					try {
						final Calendar cal = getDate();
						if (!cal.equals(model.getSittingDate())) {
							// Value has changed
							if (isDirty()) {
								boolean navigateAway = showCancelConfirmationMsg();
								
								if (navigateAway) {
									model.setSittingDate(cal.getTime());
									RecordCourtroomStatisticsPanel.this.stepUpdateViewState();
								}
							} else {
								model.setSittingDate(cal.getTime());
								RecordCourtroomStatisticsPanel.this.stepUpdateViewState();
							}
						}
					} catch (final CSRecoverableException csre) {
						XHIBITConstant.handleError(csre);
					}
				}
			}
		};
		
		sittingDateValidator = ValidationControllerFactory.createDateRequired(this,
				sittingDatePanel, sittingDateError,
				new SittingDateValidator());
		validationControllers.add(sittingDateValidator);
		
		Outerpanel.add(sittingDatePanel, gbc);
		
		gbc.weightx = 0.05;
		gbc.gridx++;
		Outerpanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RecordCourtroomStatistics.site.label")), gbc);
		
		gbc.weightx = 0.7;
		gbc.gridx++;
		Outerpanel.add(siteCombo, gbc);

		return Outerpanel;
	}
	
	private class SittingDateValidator extends DateEqualOrBeforeTodayValidator {

		@Override
		public void validate(XDatePanel target, List<String> errors) {
			// Invoke the DateEqualOrBeforeTodayValidator validation first
			super.validate(target, errors);
			
			if (errors.isEmpty()) {
				// If DateEqualOrBeforeTodayValidator passes then check if a valid sitting day
				if ( !isSittingDate(target) ){
    				errors.add(XHIBITConstant.getResource(XhibitBundles.ErrorText, "CourtroomStatistics.nonSittingDay"));
        		}
			}
		}
		
		/**
    	 * Checks the date entered the list of sitting dates to see if valid
    	 * @param date The date to check
    	 * @return true if a sitting date, else false
    	 */
    	private boolean isSittingDate(XDatePanel target) {
    		boolean isSittingDate = false;
    		try {
        		isSittingDate = XhibitDelegateHelper.getBizRefDelegate().isCourtAvailableOnDate(
        				XhibitSingleton.getInstance().getCourtId(), target.getDate().getTime());
    		} catch (CSRecoverableException e) {
    			
    		}
    		return isSittingDate;
    	}
	};
	
	private TableModelListener getTableChangeController() {
		return new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				if (isDirty()) {
					//If data changed, enable save.
					getButtonPanel().okButton.setEnabled(true);
				}
			}
		};
	}
	
	/**
	 * Action class for the dropdown field to refresh data on the screen
	 * when the court site is updated
	 */
	private class SiteComboBoxAction extends XAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void xActionPerformed(ActionEvent e) throws Exception {
			if (isDirty()) {
				boolean navigateAway = showCancelConfirmationMsg();
				if (navigateAway) {
					RecordCourtroomStatisticsPanel.this.stepUpdateViewState();
				}
			}
			else {
				RecordCourtroomStatisticsPanel.this.stepUpdateViewState();
			}
		}
	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		boolean valid = validateAll();
		
		// Set the enablement of the buttons on the screen based upon the validation checks
		autoPopulateButton.setEnabled(valid);
		addCourtRoomUsageButton.setEnabled(valid);
		addJudgeUsageButton.setEnabled(valid);
		getButtonPanel().okButton.setEnabled(valid);
	}
	
	/**
	 * Validates all components and returns true if all components are valid
	 * @return True if all components are valid, else false
	 */
	private boolean validateAll() {
		boolean valid = true;
		for (ValidationController<?> vc : validationControllers) {
			if (vc.hasErrors()) {
				valid = false;
				break;
			}
		}
		return valid;
	}
	
	private boolean isSittingDateValid() {
		final List<String> errors = new ArrayList<String>();
		final SittingDateValidator sdValidator = new SittingDateValidator();
		sdValidator.validate(sittingDatePanel, errors);
		return errors.isEmpty();
	}
	
}
