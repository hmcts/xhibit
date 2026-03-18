package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenUpdateCasePropertiesAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
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
 * 
 *          <Change History/>
 * 
 *          <p>
 *          2003-06-23 MH - Bugfix 53587 - added validation for the hearing
 *          dates text fields.
 *          </p>
 *          <p>
 *          2003-09-05 ST - Bugfix 54286 - added None radio button
 *          </p>
 * 
 */
public class Other extends JPanel {
	
	private static final long serialVersionUID = 1L;

	private HearingRecordModel model;

	private JPanel adjournmentPanel;

	private JPanel otherPanel;

	private JRadioButton yesRb;

	private JRadioButton noRb;

	//private JRadioButton noneRb;

	private XDatePanel toDate;

	private JComboBox becauseCbx;

	private JCheckBox severedCb;

	private ArrayList codes;

	private JButton editBtn;

	public Other(HearingRecordModel model) {
		this.model = model;
		jbInit();
	}

	public void jbInit() {
		this.setLayout(new GridBagLayout());

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridBagLayout());

		adjournmentPanel = new JPanel();
		adjournmentPanel.setLayout(new GridBagLayout());
		adjournmentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "adjournment")));

		// getting ref data for because combo box.
		Vector<String> comboData = new Vector<String>();
		try {
			RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.PB_TYPE);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			this.codes = (ArrayList) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria);
			if (codes.size() > 0) {
				for (int i = 0; i < codes.size(); i++) {
					comboData.add(((RefSystemCodeBasicValue) (codes.get(i))).getDecode());
				}
			}
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}

		yesRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "yes"));
		noRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "no"));

		ButtonGroup bg = new ButtonGroup();
		bg.add(yesRb);
		bg.add(noRb);

		toDate = new XDatePanel(adjournmentPanel);
		toDate.setDateEditable(false);
		toDate.setDate((Date) null);
		becauseCbx = new JComboBox(comboData);

		adjournmentPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "wasCaseAdjourned")),
				new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(yesRb, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(noRb, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "toDate")),
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(toDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(XHIBITConstant.getSpacer(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "because")),
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		adjournmentPanel.add(becauseCbx, new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		containerPanel.add(adjournmentPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		otherPanel = new JPanel();
		otherPanel.setLayout(new GridBagLayout());
		otherPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "other")));

		severedCb = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "yes"));
		severedCb.setEnabled(false);

		otherPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "wasIndictmentSevered")),
				new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(0, 4, 0, 4), 0, 0));
		otherPanel.add(severedCb, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 4, 0, 4), 0, 0));

		containerPanel.add(otherPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		editBtn = new JButton(new OpenScheduleHearingsAction(this));
		editBtn.setText(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "edit"));

		containerPanel.add(editBtn, new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 4, 0, 4), 0, 0));

		this.add(containerPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

	}

	/**
	 * Action for what happens when Search button is clicked.
	 *
	 */
	private class OpenScheduleHearingsAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OpenScheduleHearingsAction(Other parent) {
			populateFromBundle("btnEdit");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			XHIBITConstant.debug("in scheduled hearings action");

			// PRE00090 - ensure we use the main frame

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
				try {
					model.getHearingRecordPanel().stepInitialise(); // BD call
																	// is in
					// stepInitialise
					// method.
					// indicate that we want to only refresh the read-only parts
					// of
					// the screen
					model.getHearingRecordPanel().populateScreens(false, true);
					// model.getHearingRecordPanel().stepActivate();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void setUpdateData() throws CSValidationException {
		if (this.yesRb.isSelected()) {
			model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.setIsAdjourned(HearingRecordConstants.ADJOURNED_YES);
		} else if (this.noRb.isSelected()) {
			model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.setIsAdjourned(HearingRecordConstants.ADJOURNED_NO);
		} 

		if (this.toDate.getText().length() == 0) {
			model.getHearingRecordUpdateVal().getDefHearingRecordValue().setAdjournedDate(null);
		} else {
			model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.setAdjournedDate(this.toDate.getDate().getTime());
		}

		if (this.becauseCbx.getSelectedIndex() != -1) {
			Integer i = ((RefSystemCodeBasicValue) (codes.get(this.becauseCbx.getSelectedIndex()))).getId();
			model.getHearingRecordUpdateVal().getDefHearingRecordValue().setRefAdjournmentID(i);
		} else {
			model.getHearingRecordUpdateVal().getDefHearingRecordValue().setRefAdjournmentID(null);
		}

	}

	/**
	 * Public method used to update only the read-only components on the screen
	 * from the latest value-objects
	 */
	public void populateReadOnlyComponents() {
		HRCaseValue hrCaseVal = model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCaseValue();

		if (hrCaseVal != null) {
			// severed indicator
			if (HearingRecordConstants.SEVERED_IND_YES.equals(hrCaseVal.getCrestSeveredInd())) {
				this.severedCb.setSelected(true);
			} else if (HearingRecordConstants.SEVERED_IND_NO.equals(hrCaseVal.getCrestSeveredInd())) {
				this.severedCb.setSelected(false);
			}

		}
	}

	public void populateScreen() {
		XHIBITConstant.debug("In other populate screen method");

		// first, populate all of the read-only components...
		populateReadOnlyComponents();

		// now, populate all of the updateable components...
		if (model.getHearingRecordUpdateVal().getDefHearingRecordValue() != null) {
			// was case adjourned radio buttons
			if (HearingRecordConstants.ADJOURNED_YES
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getIsAdjourned())) {
				this.yesRb.setSelected(true);
			} else if (HearingRecordConstants.ADJOURNED_NO
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getIsAdjourned())) {
				this.noRb.setSelected(true);
			}

			// adjourned to date
			this.toDate.setDate(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getAdjournedDate());

			// because dropdown ref data
			this.becauseCbx.setSelectedIndex(-1);
			for (int i = 0; i < codes.size(); i++) {
				if (((RefSystemCodeBasicValue) (codes.get(i))).getId()
						.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getRefAdjournmentID())) {
					this.becauseCbx.setSelectedIndex(i);
				}
			}

		}

		XHIBITConstant.debug("End of other populate screen method");
	}

	// method used to set all screen components to read only if cfa already
	// exported
	public void setScreenReadOnly() {
		this.yesRb.setEnabled(false);
		this.noRb.setEnabled(false);
		this.toDate.setDateEnabled(false);
		this.toDate.setDateEditable(false);
		this.becauseCbx.setEditable(false);
		this.becauseCbx.setEnabled(false);

		this.editBtn.setEnabled(false);
	}

	public void addListeners(final HearingRecordPanel.UpdateListener updateListener) {
		
		if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			// we only require one to be created
			final MyActionListener actionListener = new MyActionListener();
	
			// adding listeners to updatable fields.
			this.yesRb.addActionListener(actionListener);
			this.noRb.addActionListener(actionListener);
			this.becauseCbx.addActionListener(actionListener);
	
			// get the update listener from the model, can't use this listener
			// in the above parts as they require customer handling (an update to
			// see if certain components should be enabled or not)
			this.toDate.getDateComponent().addMChangeListener(updateListener);
		}
	}

	public void stepUpdateViewState() {
		if (model.getXac().getApplicationCaseModel().isInEditMode(FunctionList.EExportHearingRecord)) {
			this.yesRb.setEnabled(!model.isCaseAbandonedBeforeTrial());
			this.noRb.setEnabled(!model.isCaseAbandonedBeforeTrial());
	
			this.toDate.setDateEnabled(this.yesRb.isSelected());
			this.toDate.setDateEditable(this.yesRb.isSelected());
			this.becauseCbx.setEnabled(this.yesRb.isSelected());
	
			if (!this.yesRb.isSelected()) {
				this.toDate.setDate((Date) null);
				// PRE00049 - only want to change, and therefore fire the
				// actionPerformed event if the value will actually change
				if (this.becauseCbx.getSelectedIndex() != -1) {
					this.becauseCbx.setSelectedIndex(-1);
				}
			}
	
			if (model.isCaseAbandonedBeforeTrial() && !this.noRb.isSelected()) {
				this.noRb.setSelected(true);
				getModel().setUpdated(true);
			}
		}
	}

	public HearingRecordModel getModel() {
		return this.model;
	}

	public JRadioButton getYesRb() {
		return this.yesRb;
	}

	public JRadioButton getNoRb() {
		return this.noRb;
	}

	public JComboBox getBecauseCbx() {
		return this.becauseCbx;
	}

	private class MyActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			getModel().setUpdated(true);
			stepUpdateViewState();
		}
	}

	public void stepValidate() throws CSValidationException {
		// New if statement added to force selection of case adjourned radio
		// buttons - PR6717
		if (getYesRb().isSelected() == false && getNoRb().isSelected() == false ) {
			throw new CSValidationException("validation.mustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "caseAdjourned") },
					"In Crest Form A.Other.An Adjournement radio button must be selected");
		}
		if (getYesRb().isSelected() && getBecauseCbx().getSelectedIndex() < 0) {
			throw new CSValidationException("validation.mustselect",
					new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "because") },
					"If Crest Form A.Other.Yes radio button is set, a reason must be selected");
		}
		//ctx-2706 - if adjournment code=DS, an adjournment date is mandatory
		if (this.becauseCbx.getSelectedIndex() != -1) {
			String adjCode =  ((RefSystemCodeBasicValue) (codes.get(this.becauseCbx.getSelectedIndex()))).getCode();
			if(adjCode != null && adjCode.equals("DS")){
				// ensure the 'to date' (Adjournment date) has been set
				if (this.toDate == null || this.toDate.getText().length() == 0) {
					throw new CSValidationException("validation.mustselect",
							new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "toDate") },
							"If Crest Form A.Other.adjournment code is DS, a to date must be selected");
				}
			}
		}		
	}

}
