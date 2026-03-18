package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHJudgeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordUpdateValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.OpenUpdateCasePropertiesAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.NumericValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @version $Revision: 1.39 $
 */
public class HearingDetails extends XPanel {
	// only to be set in the constructor
	private HearingRecordModel model;
	private final HearingDetails thisClass;
	private XhibitApplicationController xac;

	private JRadioButton mainHearingTypeRadioButton;

	private JRadioButton preliminaryHearingTypeRadioButton;

	private JTextField startDate;

	private JTextField endDate;

	private JTextField hoursText;

	private JTextField minsText;

	private JTextField judgeText;

	private JTextField txtJusticeOfPeace1;
	private JTextField txtJusticeOfPeace2;
	private JTextField txtJusticeOfPeace3;
	private JTextField txtJusticeOfPeace4;

	private JCheckBox deputyCb;

	private JButton recalculateBtn;

	private JButton editBtn;

	public HearingDetails(HearingRecordModel model, XhibitApplicationController xac) {
		super(new GridBagLayout());
		thisClass = this;
		this.model = model;
		this.xac = xac;
		jbInit();
	}

	private void jbInit() {
		// we also need to set up the hearing type button group
		final ButtonGroup hearingTypeGroup = new ButtonGroup();
		hearingTypeGroup.add(getMainHearingTypeRadioButton());
		hearingTypeGroup.add(getPreliminaryHearingTypeRadioButton());

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new GridBagLayout());

		final Insets fourByFourInsets = new Insets(4, 4, 4, 4);
		containerPanel.add(getDetailsPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, fourByFourInsets, 0, 0));
		containerPanel.add(getEditButton(), new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.NONE, new Insets(4, 4, 10, 4), 0, 0));
		this.add(containerPanel, new GridBagConstraints(0, 0, 0, 0, 0.1, 0.1, GridBagConstraints.NORTH,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
	}

	protected void setUpdateData() throws CSBusinessException {
		final HearingRecordUpdateValue hearingRecordUpdateVal = model.getHearingRecordUpdateVal();

		if (hearingRecordUpdateVal != null) {
			final HRSHJudgeValue hrShJudgeValue = hearingRecordUpdateVal.getHrSHJudgeValue();
			DefHearingRecordValue defHearingRecord = hearingRecordUpdateVal.getDefHearingRecordValue();

			if (defHearingRecord != null) {
				// hearing type radio buttons
				if (getMainHearingTypeRadioButton().isSelected()) {
					defHearingRecord.setMpHearingType(HearingRecordConstants.HEARING_TYPE_MAIN);
				} else if (getPreliminaryHearingTypeRadioButton().isSelected()) {
					defHearingRecord.setMpHearingType(HearingRecordConstants.HEARING_TYPE_PREL);
				}
				defHearingRecord.setLastCalculatedDuration(getDuration());
			}

			if (hrShJudgeValue != null) {
				// deputy HCJ checkbox
				if (getDeputyCb().isSelected()) {
					hrShJudgeValue.setDeputyHCJ(HearingRecordConstants.JUDGE_DEPUTY_HCJ_YES);
				} else {
					hrShJudgeValue.setDeputyHCJ(HearingRecordConstants.JUDGE_DEPUTY_HCJ_NO);
				}
			}
		}
	}

	/**
	 * Protected method (should only be called by classes in the same package -
	 * or subclasses) used to update only the read-only components on the screen
	 * from the latest value-objects
	 */
	protected void populateReadOnlyComponents() {
		final HearingRecordDisplayValue displayValue = model.getHearingRecordVal().getHearingRecordDisplayValue();
		final HearingRecordUpdateValue updateValue = model.getHearingRecordUpdateVal();

		if (displayValue != null) {
			final HRHearingDisplayValue hRDisplayValue = displayValue.getHrHearingDisplayValue();

			if (hRDisplayValue != null) {
				// getting the judge name as one string
				final HRJudgeValue judgeVal = hRDisplayValue.getHrJudgeValue();
				if (judgeVal != null) {
					final String listTitle = judgeVal.getJudgeFullListTitle1();
					final String surname = judgeVal.getJudgeSurname();
					String text = "";

					if ((listTitle != null) && (listTitle.trim().length() > 0)) {
						text = judgeVal.getJudgeFullListTitle1();
					} else if ((surname != null) && (surname.trim().length() > 0)) {
						text = judgeVal.getJudgeSurname();
					}

					getJudgeField().setText(text);
				}

				final DefHearingRecordValue defHearingRecordValue = updateValue.getDefHearingRecordValue();
				// justices listbox
				Collection justiceValues = hRDisplayValue.getHrJusticeValues();

				if (justiceValues != null) {
					ArrayList<SHJusticeBasicValue> jpsArr = new ArrayList<SHJusticeBasicValue>();
					Iterator it = justiceValues.iterator();

					while (it.hasNext()) {
						SHJusticeBasicValue justice = (SHJusticeBasicValue) it.next();
						if (justice != null) {
							if (justice.getHearingID() == null) {
								justice.setHearingID(defHearingRecordValue.getHearingID());
							}
							jpsArr.add(justice);
						} else {
							jpsArr.add(null);
						}
					}
					if (jpsArr.size() > 0) {
						if (jpsArr.get(0) != null && jpsArr.get(0).getJusticeName() != null) {
							txtJusticeOfPeace1.setText(jpsArr.get(0).getJusticeName().toString());
						}
					} else {
						jpsArr.add(null);
						txtJusticeOfPeace1.setText("");
					}

					if (jpsArr.size() > 1) {
						if (jpsArr.get(1) != null && jpsArr.get(1).getJusticeName() != null) {
							txtJusticeOfPeace2.setText(jpsArr.get(1).getJusticeName().toString());
						} 
					} else {
						jpsArr.add(null);
						txtJusticeOfPeace2.setText("");
					}
					if (jpsArr.size() > 2) {
						if (jpsArr.get(2) != null && jpsArr.get(2).getJusticeName() != null) {
							txtJusticeOfPeace3.setText(jpsArr.get(2).getJusticeName().toString());
						} 
					} else {
						jpsArr.add(null);
						txtJusticeOfPeace3.setText("");
					}
					if (jpsArr.size() > 3) {
						if (jpsArr.get(3) != null && jpsArr.get(3).getJusticeName() != null) {
							txtJusticeOfPeace4.setText(jpsArr.get(3).getJusticeName().toString());
						} 
					} else {
						jpsArr.add(null);
						txtJusticeOfPeace4.setText("");
					}
					justiceValues = jpsArr;
					model.getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue()
							.setHrJusticeValues(justiceValues);
				}

			}
		}

		if (updateValue != null) {
			// end hearing change - hearings are now defendant level
			final DefHearingRecordValue defHearingRecordValue = updateValue.getDefHearingRecordValue();

			if (defHearingRecordValue != null) {
				// start and end dates
				getStartDateField().setText(
						XDateFormat.format(defHearingRecordValue.getHearingStartDate(), XDateFormat.DATEFORMAT));
				getEndDateField()
						.setText(XDateFormat.format(defHearingRecordValue.getHearingEndDate(), XDateFormat.DATEFORMAT));
			}
		}

		// repopulate court reporters table
	}

	protected void populateScreen() throws CSBusinessException {
		// first, populate all of the read-only components...
		populateReadOnlyComponents();

		// now, populate all of the updateable components...
		final HearingRecordUpdateValue hearingRecordUpdateValue = model.getHearingRecordUpdateVal();

		if (hearingRecordUpdateValue != null) {
			final HearingBasicValue hearingBasicValue = hearingRecordUpdateValue.getHearingBasicValue();
			final DefHearingRecordValue defHearingRecordValue = hearingRecordUpdateValue.getDefHearingRecordValue();

			final HRSHJudgeValue hrShJudgeValue = hearingRecordUpdateValue.getHrSHJudgeValue();
			if (hrShJudgeValue != null) {
				final boolean selected = HearingRecordConstants.JUDGE_DEPUTY_HCJ_YES
						.equals(hrShJudgeValue.getDeputyHCJ());
				// deputy HCJ checkbox
				getDeputyCb().setSelected(selected);
			}

			boolean hearingTypeFound = false;
			// First try and find the 'defendant hearing type'
			if (defHearingRecordValue != null) {
				// hearing type radio buttons
				if (HearingRecordConstants.HEARING_TYPE_MAIN.equals(defHearingRecordValue.getMpHearingType())) {
					getMainHearingTypeRadioButton().setSelected(true);
					hearingTypeFound = true;
				} else if (HearingRecordConstants.HEARING_TYPE_PREL.equals(defHearingRecordValue.getMpHearingType())) {
					getPreliminaryHearingTypeRadioButton().setSelected(true);
					hearingTypeFound = true;
				}
				else{
					// default to Preliminary radio button. Have not set the hearingTypeFound boolean intentionally as if still false 
					// Hearing Type may be over written in next if clause at case level
					getPreliminaryHearingTypeRadioButton().setSelected(true);
				}
			}
			
			// If no defendant hearing type found then use the case level one
			if (!hearingTypeFound) {
				if (hearingBasicValue != null) {
					if (HearingRecordConstants.HEARING_TYPE_MAIN.equals(hearingBasicValue.getMpHearingType())) {
						getMainHearingTypeRadioButton().setSelected(true);
					} else if (HearingRecordConstants.HEARING_TYPE_PREL.equals(hearingBasicValue.getMpHearingType())) {
						getPreliminaryHearingTypeRadioButton().setSelected(true);
					}
				}
			}

			// hearing duration is now defendant level
			if (defHearingRecordValue != null) {
				// hearing duration is now defendant level...
				setDuration(defHearingRecordValue.getLastCalculatedDuration());
				model.setDuration(defHearingRecordValue.getLastCalculatedDuration());
				model.setDurationUpdated(false);
			}
		}
	}

	/**
	 * Method used to set all screen components to read only if cfa already
	 * exported.
	 */
	public void setScreenReadOnly() {
		// @todo - should recalculate button be disabled? - it wasn't
		// previously...
		getRecalculateButton().setEnabled(false);
		getEditButton().setEnabled(false);
		getMainHearingTypeRadioButton().setEnabled(false);
		getPreliminaryHearingTypeRadioButton().setEnabled(false);
		getHoursField().setEditable(false);
		getMinutesField().setEditable(false);
		getDeputyCb().setEnabled(false);
	}

	public HearingRecordModel getModel() {
		return this.model;
	}

	/**
	 * Helper method to return parts of the duration fields as long values
	 * 
	 * @param timeComponent
	 *            - JTextField screen widget
	 * @return long - the representation of this value as a long value
	 */
	private long convertTimeComponentToLong(JTextField timeComponent) {
		final String value = timeComponent.getText();
		return ((value.length() == 0) ? 0 : Long.parseLong(value));
	}

	public long getMinutes() {
		return convertTimeComponentToLong(getMinutesField());
	}

	public long getHours() {
		return convertTimeComponentToLong(getHoursField());
	}

	public Long getDuration() {
		long hours = getHours();
		long minutes = getMinutes();
		model.setDuration(new Long(((hours * 60) + minutes) * 60000));
		return new Long(((hours * 60) + minutes) * 60000);
	}

	public void setDuration(Long duration) {
		long hours = 0;
		long minutes = 0;

		if (duration != null) {
			// need to get the double value of the field so as to not lose
			// any
			// of the required precision with the implicit casts...
			final long totalMins = Math.round(duration.doubleValue() / 60000);
			hours = totalMins / 60;
			minutes = totalMins % 60;
		}

		getHoursField().setText(String.valueOf(hours));
		getMinutesField().setText(String.valueOf(minutes));
	}

	public void addListeners(final HearingRecordPanel.UpdateListener updateListener) {
		// adding listeners to updatable fields

		// we only require one of these to be created
		final MyDocumentListener listener = new MyDocumentListener();

		getHoursField().getDocument().addDocumentListener(listener);
		getMinutesField().getDocument().addDocumentListener(listener);

		getMainHearingTypeRadioButton().addActionListener(updateListener);
		getPreliminaryHearingTypeRadioButton().addActionListener(updateListener);
		// MH added listener for HCJ deputy flag
		getDeputyCb().addActionListener(updateListener);
	}

	private JTextField getStartDateField() {
		if (this.startDate == null) {
			this.startDate = new JTextField(15);
			this.startDate.setEditable(false);
		}

		return this.startDate;
	}

	private JTextField getEndDateField() {
		if (this.endDate == null) {
			this.endDate = new JTextField(15);
			this.endDate.setEditable(false);
		}

		return this.endDate;
	}

	private JTextField getHoursField() {
		if (this.hoursText == null) {
			this.hoursText = new JTextField("0", 5);
			LimitedTextValidatingDocumentDecorator limit = new LimitedTextValidatingDocumentDecorator(4);
			NumericValidatingDocumentDecorator numeric = new NumericValidatingDocumentDecorator(limit);
			this.hoursText.setDocument(numeric);
		}

		return this.hoursText;
	}

	private JTextField getMinutesField() {
		if (this.minsText == null) {
			this.minsText = new JTextField("0", 5);
			LimitedTextValidatingDocumentDecorator limit = new LimitedTextValidatingDocumentDecorator(2);
			NumericValidatingDocumentDecorator numeric = new NumericValidatingDocumentDecorator(limit);
			this.minsText.setDocument(numeric);
		}

		return this.minsText;
	}

	private JPanel getDurationPanel() {
		final JPanel durPanel = new JPanel();
		durPanel.add(createLabel("hours"));
		durPanel.add(getHoursField());
		durPanel.add(createLabel("minutes"));
		durPanel.add(getMinutesField());

		return durPanel;
	}

	private JButton getRecalculateButton() {
		if (this.recalculateBtn == null) {
			final XAction recalculateAction = XhibitActions.getAction(model.getXac(),
					XhibitActions.HearingDetailsRecalculate);
			recalculateAction.setCaller(this);
			this.recalculateBtn = new JButton(recalculateAction);
		}

		return this.recalculateBtn;
	}

	private JButton getEditButton() {
		if (this.editBtn == null) {
			this.editBtn = new JButton(new OpenScheduleHearingsAction(this));
		}

		return this.editBtn;
	}

	/**
	 * Action for what happens when Search button is clicked.
	 *
	 */
	private class OpenScheduleHearingsAction extends XAction {

		private static final long serialVersionUID = 1L;

		public OpenScheduleHearingsAction(HearingDetails parent) {
			populateFromBundle("btnEdit");
			setCaller(parent);
		}

		public void xActionPerformed(ActionEvent ae) throws Exception {
			XHIBITConstant.debug("in scheduled hearings action");

			ScheduledHearingsDialog d = new ScheduledHearingsDialog(model.getXac(), model);
			d.setVisible(true);

			if ((d.isOkClicked()) && (model.getScheduledHearingId() != null)) {
				// open maintain hearing header
				model.setJusticeEdited(true);
				((OpenUpdateCasePropertiesAction) XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps))
						.setScheduledHearingID2BOpened(model.getScheduledHearingId());
				((OpenUpdateCasePropertiesAction) XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps))
						.setHearingRecordModel(model);
				XhibitActions.getAction(model.getXac(), XhibitActions.CaseProps).xActionPerformed(ae);

				// model.setScheduledHearingId(null);
				// setting scheduledHearingId in
				// model to null again.

				// update crest form a screens by call to BD and populating
				// screens with new HearingRecordValue data.
				Boolean updated = true;
				if (model.isUpdated() == false) {
					updated = false;
				}
				try {
					if (model.isRepresentationUpdated()) {
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

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private JTextField getJudgeField() {
		if (this.judgeText == null) {
			this.judgeText = new JTextField(20);
			this.judgeText.setEditable(false);
		}

		return this.judgeText;
	}

	private JTextField getJusticeOfPeace1() {
		if (this.txtJusticeOfPeace1 == null) {
			this.txtJusticeOfPeace1 = new JTextField();
			this.txtJusticeOfPeace1.setColumns(15);
			this.txtJusticeOfPeace1.setEnabled(false);
			this.txtJusticeOfPeace1.setVisible(true);
		}
		return this.txtJusticeOfPeace1;
	}

	private JTextField getJusticeOfPeace2() {
		if (this.txtJusticeOfPeace2 == null) {
			this.txtJusticeOfPeace2 = new JTextField();
			this.txtJusticeOfPeace2.setColumns(15);
			this.txtJusticeOfPeace2.setEnabled(false);
			this.txtJusticeOfPeace2.setVisible(true);
		}
		return this.txtJusticeOfPeace2;
	}

	private JTextField getJusticeOfPeace3() {
		if (this.txtJusticeOfPeace3 == null) {
			this.txtJusticeOfPeace3 = new JTextField();
			this.txtJusticeOfPeace3.setColumns(15);
			this.txtJusticeOfPeace3.setEnabled(false);
			this.txtJusticeOfPeace3.setVisible(true);
		}
		return this.txtJusticeOfPeace3;
	}

	private JTextField getJusticeOfPeace4() {
		if (this.txtJusticeOfPeace4 == null) {
			this.txtJusticeOfPeace4 = new JTextField();
			this.txtJusticeOfPeace4.setColumns(15);
			this.txtJusticeOfPeace4.setEnabled(false);
			this.txtJusticeOfPeace4.setVisible(true);
		}
		return this.txtJusticeOfPeace4;
	}

	private JCheckBox getDeputyCb() {
		if (this.deputyCb == null) {
			this.deputyCb = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "sittingAsDepHCJ"));
		}

		return this.deputyCb;
	}

	private JPanel getDetailsPanel() {
		final JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridBagLayout());
		detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "hearingDetails")));

		final Insets fourByFourInsets = new Insets(4, 4, 4, 4);
		detailsPanel.add(createLabel("hearingType"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getMainHearingTypeRadioButton(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getPreliminaryHearingTypeRadioButton(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		detailsPanel.add(createLabel("startDate"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(createLabel("endDate"), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getStartDateField(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getEndDateField(), new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getRecalculateButton(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		detailsPanel.add(createLabel("duration"), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getDurationPanel(), new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		detailsPanel.add(createLabel("hearingJudge"), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(createLabel("jps"), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(createLabel("jps2"), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(createLabel("jps3"), new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(createLabel("jps4"), new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		detailsPanel.add(getJudgeField(), new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getDeputyCb(), new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		detailsPanel.add(getJusticeOfPeace1(), new GridBagConstraints(1, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getJusticeOfPeace2(), new GridBagConstraints(1, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getJusticeOfPeace3(), new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));
		detailsPanel.add(getJusticeOfPeace4(), new GridBagConstraints(1, 9, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, fourByFourInsets, 0, 0));

		return detailsPanel;
	}

	private JRadioButton getMainHearingTypeRadioButton() {
		if (this.mainHearingTypeRadioButton == null) {
			this.mainHearingTypeRadioButton = createRadioButton("main");
		}

		return this.mainHearingTypeRadioButton;
	}

	private JRadioButton getPreliminaryHearingTypeRadioButton() {
		if (this.preliminaryHearingTypeRadioButton == null) {
			this.preliminaryHearingTypeRadioButton = createRadioButton("preliminary");
		}

		return this.preliminaryHearingTypeRadioButton;
	}

	/**
	 * Private helper method for simple creation of a <code>JLabel</code> it
	 * uses the passed in resourceKey <code>String</code> to lookup the text to
	 * display in the relevant resource bundle. Note that there are no checks
	 * for the resourceKey being <i>null</i> as this is a private method, and we
	 * always know what is being passed to it
	 * 
	 * @param resourceKey
	 * @return A newly created <code>JLabel</code>
	 * @see uk.gov.courtservice.xhibit.client.util.XHIBITConstant#getResource(
	 *      java.lang.String, java.lang.String)
	 */
	private JLabel createLabel(String resourceKey) {
		return new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, resourceKey));
	}

	/**
	 * Private helper method for simple creation of a <code>JRadioButton</code>
	 * it uses the passed in resourceKey <code>String</code> to lookup the text
	 * to display in the relevant resource bundle. Note that there are no checks
	 * for the resourceKey being <i>null</i> as this is a private method, and we
	 * always know what is being passed to it
	 * 
	 * @param resourceKey
	 * @return A newly created <code>JRadioButton</code>
	 * @see uk.gov.courtservice.xhibit.client.util.XHIBITConstant#getResource(
	 *      java.lang.String, java.lang.String)
	 */
	private JRadioButton createRadioButton(String resourceKey) {
		return new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, resourceKey));
	}

	// not using the same one as everything else, as this updates the
	// durationUpdated part of the model, not the standard updated
	private class MyDocumentListener implements DocumentListener {
		public void changedUpdate(final DocumentEvent e) {
			getModel().setDurationUpdated(true);
		}

		public void removeUpdate(final DocumentEvent e) {
			getModel().setDurationUpdated(true);
		}

		public void insertUpdate(final DocumentEvent e) {
			getModel().setDurationUpdated(true);
		}
	}

	public void stepValidate() throws CSValidationException {
		if (model.isDurationUpdated()) {
			if (getHours() > 0 && getMinutes() > 59) {
				throw new CSValidationException("validation.maxexclusive",
						new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "minutes"), "60" },
						"Minutes must be < 60");
			}

			if (getDuration().intValue() == 0) {
				throw new CSValidationException("gui.hearingrecord.update.hearingDetails.other.durationIsZero",
						"The duration may not be set to zero");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
	 */
	@Override
	public void stepInitialise() throws CSRecoverableException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepActivate()
	 */
	@Override
	public void stepActivate() throws CSRecoverableException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
	 */
	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeactivate()
	 */
	@Override
	public void stepDeactivate() throws CSRecoverableException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise(boolean)
	 */
	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {

	}
}
