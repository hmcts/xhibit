package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenUpdateCasePropertiesAction;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.client.counselfacilities.EditAdvocateWizard;
import uk.gov.courtservice.xhibit.client.counselfacilities.EditAdvocateWizardModel;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindInstructedAdvocateTableRowModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XDefaultComboBoxCellRenderer;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */

public class Representation extends JPanel {

	private static final long serialVersionUID = 1L;

	private HearingRecordModel model;

	private JPanel prosPanel;

	private JPanel defPanel;

	private JPanel defButtonPanel;

	private JPanel inPersonPanel;

	private JButton prosEditBtn;

	private JButton defEditBtn;

	private JButton defIOrSEditBtn;

	private XTable prosTable;

	private XTable defTable;

	private JComboBox categoryCb;

	private JLabel inPersonLbl;

	private JCheckBox inPersonCbx;

	private JLabel nonAttendanceLbl;

	private JCheckBox nonAttendanceCbx;

	private Vector<String> comboCategoryData;

	private DefenceRepTableModel defRepTableModel;

	private ListSelectionModel listSelectionModel;

	/**
	 * Creates a Representation panel.
	 * 
	 * @param model
	 *            the HearingRecordModel.
	 */
	public Representation(HearingRecordModel model) {
		this.model = model;
		stepInitialise();
		jbInit();
	}

	/**
	 * Gets data to initialise the screen.
	 */
	private void stepInitialise() {
		// getting ref data for category combo box
		comboCategoryData = new Vector<String>();
		comboCategoryData.add("");
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.ADVOCATE_TYPE);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			model.setDefCatCodes((ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria)));
			if (model.getDefCatCodes().size() > 0) {
				for (int i = 0; i < model.getDefCatCodes().size(); i++) {
					comboCategoryData.add(((RefSystemCodeBasicValue) (model.getDefCatCodes().get(i))).getDecode());
				}
			}
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}

		defRepTableModel = new DefenceRepTableModel(model);
	}

	/**
	 * Lay out the widgets on the screen.
	 */
	public void jbInit() {
		this.setLayout(new GridBagLayout());

		prosPanel = new JPanel();
		prosPanel.setLayout(new GridBagLayout());

		if (model.isAppealType()) {
			prosPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "respondentDetails")));
		} else {
			prosPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "prosecutorDetails")));
		}

		this.prosTable = XTableFactory.getInstance().createMultiLineTable(new ProsecutorTableModel(this.model));
		this.prosTable.setPreferredScrollableViewportSize(new Dimension(700, 100));
		this.prosTable.getColumnModel().getColumn(0).setMaxWidth(240);
		this.prosTable.getColumnModel().getColumn(1).setMaxWidth(240);
		this.prosTable.getColumnModel().getColumn(2).setMaxWidth(50);
		this.prosTable.getColumnModel().getColumn(3).setMaxWidth(170);

		JScrollPane scrollPane = new JScrollPane(this.prosTable);
		scrollPane.setMinimumSize(new Dimension(700, 100));
		prosPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		prosEditBtn = new JButton(new OpenScheduleHearingsAction(this));

		prosPanel.add(prosEditBtn, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(prosPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		this.categoryCb = new JComboBox(comboCategoryData);

		defPanel = new JPanel();
		defPanel.setLayout(new GridBagLayout());
		if (model.isAppealType()) {
			defPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appellantRepDetails")));
		} else {
			defPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
					XHIBITConstant.getResource(XhibitBundles.HearingRecord, "defendantRepDetails")));
		}

		this.inPersonPanel = new JPanel(new GridBagLayout());
		this.inPersonLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lblInPerson"));
		this.inPersonCbx = new JCheckBox();
		this.inPersonCbx.setEnabled(false);
		this.nonAttendanceLbl = new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lblNonAttendance"));
		this.nonAttendanceCbx = new JCheckBox();
		this.nonAttendanceCbx.setEnabled(false);
		this.inPersonCbx.setSelected(isDefendantRepresentedInPerson(this.model));
		this.nonAttendanceCbx.setSelected(isDefendantRepresentedNonAttendance(this.model));
		this.inPersonPanel.add(inPersonLbl, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.inPersonPanel.add(inPersonCbx, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.inPersonPanel.add(nonAttendanceLbl, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		this.inPersonPanel.add(nonAttendanceCbx, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.defTable = XTableFactory.getInstance().createMultiLineTable(defRepTableModel);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.CATEGORY)
				.setCellRenderer(new XDefaultComboBoxCellRenderer());
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.CATEGORY)
				.setCellEditor(new XDefaultComboBoxCellEditor(this.categoryCb));

		// setting column widths
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.NAME).setMaxWidth(100);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.ID_NUMBER).setMaxWidth(50);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.ADDRESS).setMaxWidth(170);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.ROLE).setMaxWidth(50);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.CATEGORY).setMaxWidth(160);
		this.defTable.getColumnModel().getColumn(DefenceRepTableModel.ATTENDANCE_DATES).setMaxWidth(170);

		if (model.getLegallyAided()) {
			this.defTable.getColumnModel().getColumn(DefenceRepTableModel.I_OR_S_FLAG).setMaxWidth(40);
		}

		this.defTable.setPreferredScrollableViewportSize(new Dimension(700, 150));
		JScrollPane scrollPane2 = new JScrollPane(this.defTable);
		scrollPane2.setMinimumSize(new Dimension(700, 120));
		defPanel.add(inPersonPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		defPanel.add(scrollPane2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		defEditBtn = new JButton(new OpenScheduleHearingsAction(this));

		defButtonPanel = new JPanel();

		if (model.getLegallyAided()) {
			defIOrSEditBtn = new JButton();
			defIOrSEditBtn.setAction(new XIOrSEditAction());
			defIOrSEditBtn.setText(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lblIOrSEdit"));
			defIOrSEditBtn.setToolTipText(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "ttIOrSEdit"));
			defIOrSEditBtn.setEnabled(false);
			defButtonPanel.add(defIOrSEditBtn);
		}

		defButtonPanel.add(defEditBtn);

		defPanel.add(defButtonPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(defPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		listSelectionModel = new DefaultListSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelectionModel.addListSelectionListener(new XDefenceRepresentationListSelectionListener());
		defTable.setSelectionModel(listSelectionModel);
	}

	/**
	 * Action for what happens when Search button is clicked.
	 *
	 */
	private class OpenScheduleHearingsAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OpenScheduleHearingsAction(Representation parent) {
			populateFromBundle("btnEdit");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			XHIBITConstant.debug("in scheduled hearings action");

			ScheduledHearingsDialog d = new ScheduledHearingsDialog(model.getXac(), model);
			d.setVisible(true);

			if ((d.isOkClicked()) && (model.getScheduledHearingId() != null)) {
				// open maintain hearing header
				model.setJusticeEdited(false);
				((OpenUpdateCasePropertiesAction) XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps))
						.setScheduledHearingID2BOpened(model.getScheduledHearingId());
				((OpenUpdateCasePropertiesAction) XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps))
						.setHearingRecordModel(model);
				XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps).xActionPerformed(ae);

				model.setScheduledHearingId(null); // setting scheduledHearingId
													// in
				// model to null again.

				// update crest form a screens by call to BD and populating
				// screens with new HearingRecordValue data.
				Boolean updated = true;
				if (model.isUpdated() == false) {
					updated = false;
				}
				try {
						model.getHearingRecordPanel().stepInitialise(); // BD
																		// call
																		// is in

						// stepInitialise
						// method.
						// indicate that we want to only refresh the read-only
						// parts
						// of
						// the screen
						model.getHearingRecordPanel().populateScreens(false, true);
						// model.getHearingRecordPanel().stepActivate();
						model.setUpdated(updated);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private class XDefenceRepresentationListSelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent lse) {
			stepUpdateViewState();
		}
	}

	private class XIOrSEditAction extends XAction {

		private static final long serialVersionUID = 1L;

		public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
			final int selectedRow = defTable.getSelectedRow();

			if (selectedRow < 0)
				// No rows selected
				return;

			Vector row = (Vector) defRepTableModel.getDataAt(selectedRow);

			final Integer legalRepId = (Integer) row.get(DefenceRepTableModel.LEGAL_REP_ID);
			final EditAdvocateWizardModel editAdvocateModel = new EditAdvocateWizardModel();
			editAdvocateModel.setXac(model.getXac());

			EditAdvocateWizard eaDialog = new EditAdvocateWizard(defRepTableModel, editAdvocateModel, legalRepId,
					model.getDefendantId());

			eaDialog.setVisible(true);

			if (eaDialog.getLatestEvent() == XWizardDialog.FINISH_EVENT) {
				eaDialog.stepValidate();

				FindInstructedAdvocateTableRowModel trm = editAdvocateModel
						.getSubstitutedInstructedAdvocateTableRowModel();

				synchronized (model) {

					if (trm != null) {
						HRCounselValue counsel = (HRCounselValue) row.get(DefenceRepTableModel.HR_COUNSEL_VAL);
						counsel.setInstructedAdvocateRefLegalRepId(trm.getLegalRepId());
					} else {
						HRCounselValue counsel = (HRCounselValue) row.get(DefenceRepTableModel.HR_COUNSEL_VAL);
						counsel.setInstructedAdvocateRefLegalRepId(counsel.getRefLegalRepID());
					}

				}

				defRepTableModel.updateIOrSValue(selectedRow);
			}
		}
	}

	public void addListeners(final HearingRecordPanel.UpdateListener updateListener) {
		// adding listeners to updatable fields
		XHIBITConstant.debug("Adding listeners in representation");

		defRepTableModel.addTableModelListener(updateListener);
	}

	public void setScreenReadOnly() {
		this.prosEditBtn.setEnabled(false);
		this.defEditBtn.setEnabled(false);
		this.defTable.setEnabled(false);
		this.prosTable.setEnabled(false);
	}

	/**
	 * Public method used to update only the read-only components on the screen
	 * from the latest value-objects
	 */
	public void populateReadOnlyComponents() {
		populateReadOnlyComponents(false);
	}

	/**
	 * As all components included in this panel are read only except for a small
	 * part of one component, this private method calls the new setData method
	 * using the passed in <code>boolean</code> parameter
	 * 
	 * @param fullRefresh
	 *            A <code>boolean</code> indicating if we want to do a full
	 *            refresh, or read parts from the previously displayed values
	 */
	private void populateReadOnlyComponents(boolean fullRefresh) {

		this.inPersonCbx.setSelected(this.isDefendantRepresentedInPerson(this.model));
		this.nonAttendanceCbx.setSelected(this.isDefendantRepresentedNonAttendance(this.model));

		((ProsecutorTableModel) this.prosTable.getModel()).setData(this.model);
		this.prosTable.tableChanged(new TableModelEvent(prosTable.getModel()));
		this.prosTable.revalidate();
		this.prosTable.repaint();

		// ((DefenceRepTableModel)this.defTable.getModel()).setData(this.model,
		// fullRefresh);
		defRepTableModel.setData(this.model, fullRefresh);
		this.defTable.tableChanged(new TableModelEvent(defTable.getModel()));
		this.defTable.revalidate();
		this.defTable.repaint();
	}

	public void populateScreen() {
		// indicate that we do want to do a full refresh
		populateReadOnlyComponents(true);
	}

	public void setUpdateData() {
		Vector legRepValues = (Vector) model.getHearingRecordVal().getHearingRecordUpdateValue().getHrSHLegRepValues();

		// loop through vector of defence rep advocate IDs
		for (int i = 0; i < model.getDefenceReps().size(); i++) {
			// find corresponding Ref Data ID for string value selected in
			// Category Combo box for that defence rep
			DefenceRepTableModel tableModel = (DefenceRepTableModel) this.defTable.getModel();
			String catString = (String) (tableModel.getValueAt(i, DefenceRepTableModel.CATEGORY));
			HRCounselValue counsel = (HRCounselValue) ((Vector) tableModel.getDataAt(i))
					.get(DefenceRepTableModel.HR_COUNSEL_VAL);

			Integer code = null;
			for (int x = 0; x < model.getDefCatCodes().size(); x++) {
				if (((RefSystemCodeBasicValue) (model.getDefCatCodes().get(x))).getDecode().equals(catString)) {
					code = ((RefSystemCodeBasicValue) (model.getDefCatCodes().get(x))).getId();
					break;
				}
			}

			// loop through collection of legRepValues
			for (int j = 0; j < legRepValues.size(); j++) {
				HRSHLegRepValue hrshLegRep = (HRSHLegRepValue) legRepValues.get(j);

				if (model.getDefenceReps().get(i).equals(hrshLegRep.getRefLegRepID())) {
					hrshLegRep.setRefDefenceCategoryID(code);

					if (counsel.getInstructedAdvocateRefLegalRepId() != null) {
						Integer instructedAdvocateRefLegalRepId = counsel.getInstructedAdvocateRefLegalRepId();

						if (counsel.getRefLegalRepID().equals(instructedAdvocateRefLegalRepId)) {
							hrshLegRep.setSubInst("I");
							hrshLegRep.setSubstitutedRefLegRepId(counsel.getRefLegalRepID());
						} else {
							hrshLegRep.setSubInst("S");
							hrshLegRep.setSubstitutedRefLegRepId(counsel.getInstructedAdvocateRefLegalRepId());
						}
					} else {
						// No change - use the old values
						hrshLegRep.setSubInst(counsel.getSubstituteOrInstructed());
						hrshLegRep.setSubstitutedRefLegRepId(counsel.getSubstitutedRefLegalRepId());
					}
				}
			}
		}
	}
	
	public void stepValidate() throws CSValidationException {
		//CTX-2649 DETAILS CATEGORY MANADATORY
		// loop through table to determine all categories are set
		for (int i = 0; i < model.getDefenceReps().size(); i++) {
			// Category Combo box for that defence rep
			DefenceRepTableModel tableModel = (DefenceRepTableModel) this.defTable.getModel();
			String catString = (String) (tableModel.getValueAt(i, DefenceRepTableModel.CATEGORY));
			if (catString == null || catString.equals("")){
				throw new CSValidationException("validation.mustselect",
						new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "repCategory") },
						"If Crest Form A.Representation.category mustbe set");
			}
		}			
	}

	private boolean isDefendantRepresentedInPerson(final HearingRecordModel hrModel) {
		boolean returnValue = false;
		final Vector counselVector = (Vector) hrModel.getHearingRecordVal().getHearingRecordDisplayValue()
				.getHrCounselValue();

		if (counselVector != null) {
			for (int x = 0; x < counselVector.size() && !returnValue; x++) {
				final HRCounselValue hrCounselValue = (HRCounselValue) counselVector.get(x);
				if (hrCounselValue.getSolFirmOrRefLegalRep() != null && hrCounselValue.getSolFirmOrRefLegalRep()
						.equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON)) {
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	private boolean isDefendantRepresentedNonAttendance(final HearingRecordModel hrModel) {
		boolean returnValue = false;
		final Vector counselVector = (Vector) hrModel.getHearingRecordVal().getHearingRecordDisplayValue()
				.getHrCounselValue();

		if (counselVector != null) {
			for (int x = 0; x < counselVector.size() && !returnValue; x++) {
				final HRCounselValue hrCounselValue = (HRCounselValue) counselVector.get(x);
				if (hrCounselValue.getSolFirmOrRefLegalRep() != null && hrCounselValue.getSolFirmOrRefLegalRep()
						.equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE)) {
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	public void stepUpdateViewState() {
		if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			if (model.getLegallyAided()) {
				final int selectedRow = defTable.getSelectedRow();
	
				defIOrSEditBtn.setEnabled(selectedRow >= 0 && defRepTableModel.hasEditableIOrSField(selectedRow));
			}
		}
	}
}