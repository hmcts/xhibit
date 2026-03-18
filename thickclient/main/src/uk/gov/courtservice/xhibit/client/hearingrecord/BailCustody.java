package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.DefHearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.caze.TimeEstimatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

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
public class BailCustody extends JPanel {

	private static final long serialVersionUID = 1L;

	// only to be set in the constructor
	private final HearingRecordModel model;

	private JRadioButton detBailRb;

	private JRadioButton detCustodyRb;

	private JRadioButton detInCareRb;

	private JRadioButton detNotApplicRb;

	private JComboBox hearingTypeCbx;

	private JCheckBox hraCb;

	private JCheckBox oralEvidCb;

	private JCheckBox trialInDefAbsence;

	private JCheckBox sentenceInDefAbsence;

	private XDatePanel applicDate;

	private XDatePanel newStatusDate;

	private JRadioButton grantedRb;

	private JRadioButton refusedRb;

	private JRadioButton resultNoneRb;

	private JRadioButton applicBailRb;

	private JRadioButton applicCustodyRb;

	private JRadioButton applicInCareRb;

	private JRadioButton applicNotApplicableRb;

	private JRadioButton applicNoneRb;

	TimeEstimatePanel tep;

	boolean tepChanged = false;

	/** The initial value of the trial time estimate */
	Float trialTimeEstimateStartState = null;

	Integer hearingTypeStartId = null;

	ComboBoxRow hearingTypeStartValue = null;

	private final Vector comboData = new Vector();

	private static final Logger log = CSServices.getLogger(BailCustody.class);

	private static final String PAD_CODE = "PAD";

	public BailCustody(HearingRecordModel model) throws CSRecoverableException {
		this.model = model;
		stepInitialise();
		jbInit();
	}

	/**
	 * Retrieve any non-volatile data, e.g. pull-down list details, and set
	 * initial states
	 * 
	 * @throws CSRecoverableException
	 */
	private void stepInitialise() throws CSRecoverableException {
		// Set initial values of time estimate and hearing type so that a change
		// can be detected
		trialTimeEstimateStartState = model.getHearingRecordUpdateVal().getDirectionsForCaseValue()
				.getDirectionsForCaseBasicValue().getTrialTimeEstimate();

		hearingTypeStartId = model.getHearingRecordUpdateVal().getDefHearingRecordValue().getRefDefHearingTypeID();

		// getting ref data for hearing type combo box.
		try {
			final RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
			criteria.setCodeType(RefSystemCodeCriteria.CodeType.DEFT_HRG_TYPE);
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());

			final Iterator it = XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria).iterator();

			while (it.hasNext()) {
				RefSystemCodeBasicValue bv = (RefSystemCodeBasicValue) it.next();
				comboData.add(new ComboBoxRow(bv.getId(), bv.getCode(), bv.getDecode()));

				// If the ID of the hearing type being processed matches the ID
				// of the initial hearing type for the defendant, save the
				// hearing type
				// so that a change of hearing type can be detected
				if (hearingTypeStartId != null && bv.getId().equals(hearingTypeStartId)) {
					hearingTypeStartValue = new ComboBoxRow(bv.getId(), bv.getCode(), bv.getDecode());
				}
			}
		} catch (Exception e) {
			XHIBITConstant.handleError(e);
		}
	}

	private void jbInit() throws CSRecoverableException {
		this.setLayout(new GridBagLayout());

		final JPanel detailsPanel = new JPanel(new GridBagLayout());
		detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bailCustodyDetails")));

		detBailRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bail"));
		detCustodyRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "custody"));
		detInCareRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "inCare"));
		detNotApplicRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "notApplicable"));

		ButtonGroup bg = new ButtonGroup();
		bg.add(detBailRb);
		bg.add(detCustodyRb);
		bg.add(detInCareRb);
		bg.add(detNotApplicRb);

		hearingTypeCbx = new JComboBox(comboData);

		hraCb = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "hraApplication"));
		// MH - Disable the HRA tick box since this will not be used until
		// future releases.
		this.hraCb.setEnabled(false);

		oralEvidCb = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "oralEvidence"));

		// Defendant in absence checkboxes
		trialInDefAbsence = new JCheckBox(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "trialInDefAbsence"));
		sentenceInDefAbsence = new JCheckBox(
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "sentenceInDefAbsence"));

		detailsPanel.add(
				new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bailCustodyStatusAtBeginning")),
				new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		detailsPanel.add(detBailRb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));
		detailsPanel.add(detCustodyRb, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));
		detailsPanel.add(detInCareRb, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));
		detailsPanel.add(detNotApplicRb, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));

		detailsPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "hearingType")),
				new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		detailsPanel.add(hearingTypeCbx, new GridBagConstraints(2, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		detailsPanel.add(trialInDefAbsence, new GridBagConstraints(2, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		detailsPanel.add(sentenceInDefAbsence, new GridBagConstraints(2, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		tep = new TimeEstimatePanel(model.getHearingRecordUpdateVal().getDirectionsForCaseValue(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "estimatedLengthOfTrial"));
		detailsPanel.add(tep, new GridBagConstraints(0, 5, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.add(hraCb, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(4, 4, 4, 4), 0, 0));
		panel1.add(XHIBITConstant.getSpacer(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		panel1.add(oralEvidCb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		detailsPanel.add(panel1, new GridBagConstraints(0, 6, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		this.add(detailsPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		final JPanel applicationsPanel = new JPanel(new GridBagLayout());
		applicationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bailCustodyApplications")));

		applicDate = new XDatePanel(applicationsPanel);
		newStatusDate = new XDatePanel(applicationsPanel);
		applicDate.setDateEditable(true);
		newStatusDate.setDateEditable(true);
		Date d = null;
		applicDate.setDate(d);
		newStatusDate.setDate(d);

		// Result radio buttons
		grantedRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "granted"));
		refusedRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "refused"));
		resultNoneRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "none"));
		ButtonGroup grbg = new ButtonGroup();
		grbg.add(grantedRb);
		grbg.add(refusedRb);
		grbg.add(resultNoneRb);

		JPanel resultPanel = new JPanel(new GridBagLayout());
		resultPanel.add(grantedRb, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));
		resultPanel.add(refusedRb, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));
		resultPanel.add(resultNoneRb, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 0, 4, 0), 0, 0));

		applicationsPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "result")),
				new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(4, 4, 4, 4), 0, 0));
		applicationsPanel.add(resultPanel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

		applicationsPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "ifMainHearing")),
				new GridBagConstraints(0, 2, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		// New Status radio buttons
		applicBailRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "bail"));
		applicCustodyRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "custody"));
		applicInCareRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "inCare"));
		applicNotApplicableRb = new JRadioButton(
				XHIBITConstant.getResource(XhibitBundles.HearingRecord, "notApplicable"));
		applicNoneRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "none"));

		ButtonGroup bccbg = new ButtonGroup();
		bccbg.add(applicBailRb);
		bccbg.add(applicCustodyRb);
		bccbg.add(applicInCareRb);
		bccbg.add(applicNotApplicableRb);
		bccbg.add(applicNoneRb);

		applicationsPanel.add(
				new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "ifPreliminaryHearing")),
				new GridBagConstraints(0, 0, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.WEST,
						GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
		applicationsPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "dateOfApplication")),
				new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		applicationsPanel.add(applicDate, new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		final JPanel newStatusPanel = new JPanel(new GridBagLayout());
		final GridBagConstraints newStatusPanelConstraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

		newStatusPanel.add(applicBailRb, newStatusPanelConstraints);
		newStatusPanel.add(applicCustodyRb, newStatusPanelConstraints);
		newStatusPanel.add(applicInCareRb, newStatusPanelConstraints);
		newStatusPanel.add(applicNotApplicableRb, newStatusPanelConstraints);
		newStatusPanel.add(applicNoneRb, newStatusPanelConstraints);

		applicationsPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "newStatus")),
				new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		applicationsPanel.add(newStatusPanel, new GridBagConstraints(1, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		applicationsPanel.add(
				new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "dateNewStatusStarted")),
				new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
						new Insets(4, 4, 4, 4), 0, 0));
		applicationsPanel.add(newStatusDate, new GridBagConstraints(1, 4, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

		this.add(applicationsPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
	}

	public void setUpdateData() {
		log.debug("setUpdateData() - Begin");

		if ((model.getHearingRecordUpdateVal() != null)
				&& (model.getHearingRecordUpdateVal().getDefHearingRecordValue() != null)) {
			// seperate out so as to prevent duplicate calls
			final DefHearingRecordValue defHearingRecordValue = model.getHearingRecordUpdateVal()
					.getDefHearingRecordValue();

			// this is set from the property change listener on the time
			// estimation panel
			// Also, only perform if p&d type selected
			if (this.tepChanged && isPleasAndDirectionsHearingTypeSelected()) {
				// then the time estimation panel changed value
				saveCaseTrialTime();
			}
			// bail status at beginning of hearing radio buttons
			if (this.detBailRb.isSelected()) {
				defHearingRecordValue.setStartBailStatus(HearingRecordConstants.BAIL_STATUS);
			} else if (this.detCustodyRb.isSelected()) {
				defHearingRecordValue.setStartBailStatus(HearingRecordConstants.CUSTODY_STATUS);
			} else if (this.detInCareRb.isSelected()) {
				defHearingRecordValue.setStartBailStatus(HearingRecordConstants.IN_CASE_STATUS);
			} else if (this.detNotApplicRb.isSelected()) {
				defHearingRecordValue.setStartBailStatus(HearingRecordConstants.NOT_APPLICABLE_STATUS);
			}

			// hearing type combo box
			if (this.hearingTypeCbx.getSelectedIndex() != -1) {
				ComboBoxRow item = ((ComboBoxRow) this.hearingTypeCbx.getSelectedItem());
				defHearingRecordValue.setRefDefHearingTypeID(item.getId());
			}

			// oral evidence check box
			if (this.oralEvidCb.isSelected()) {
				defHearingRecordValue.setOralEvidence(HearingRecordConstants.ORAL_EVIDENCE_YES);
			} else {
				defHearingRecordValue.setOralEvidence(HearingRecordConstants.ORAL_EVIDENCE_NO);
			}

			// DefInAbsence application Checkboxes
			if (this.trialInDefAbsence.isSelected()) {
				defHearingRecordValue.setTrialInDefAbsence("Y");
			} else {
				defHearingRecordValue.setTrialInDefAbsence(null);
			}
			if (this.sentenceInDefAbsence.isSelected()) {
				defHearingRecordValue.setSentenceInDefAbsence("Y");
			} else {
				defHearingRecordValue.setSentenceInDefAbsence(null);
			}

			try {
				if (this.resultNoneRb.isSelected()) {
					defHearingRecordValue.setDateBailApplication(null);
				} else if (grantedRb.isSelected() || refusedRb.isSelected()) {
					/*
					 * need to check something is selected as could be nothing
					 * selected in which case applicDate could be null
					 */
					if ( getApplicDate() != null ) {
						defHearingRecordValue.setDateBailApplication(getApplicDate());
					}
					else {
						defHearingRecordValue.setDateBailApplication(null);
					}
				}
			} catch (Exception ex) {
				log.fatal(ex, ex);
				throw new CSUnrecoverableException(ex);
			}

			try {
				if (applicNoneRb.isSelected()) {
					defHearingRecordValue.setStartDateNewBailStatus(null);
				} else if (applicBailRb.isSelected() || applicCustodyRb.isSelected() || applicInCareRb.isSelected()
						|| applicNotApplicableRb.isSelected()) {
					/*
					 * need to check something is selected as could be nothing
					 * selected in which case newStatusDate could be null
					 */
					if ( getNewBailStatusDate() != null ) {
						defHearingRecordValue.setStartDateNewBailStatus(getNewBailStatusDate());
					}
					else {
						defHearingRecordValue.setStartDateNewBailStatus(null);
					}
				}
			} catch (Exception ex) {
				log.fatal(ex, ex);
				throw new CSUnrecoverableException(ex);
			}

			// result of bail application radio buttons
			if (this.grantedRb.isSelected()) {
				defHearingRecordValue.setResultBailApplication(HearingRecordConstants.RESULT_BAIL_APP_GRANTED);
			} else if (this.refusedRb.isSelected()) {
				defHearingRecordValue.setResultBailApplication(HearingRecordConstants.RESULT_BAIL_APP_REFUSED);
			} else if (this.resultNoneRb.isSelected()) {
				defHearingRecordValue.setResultBailApplication(null);
			}

			// new bail status radio buttons
			if (this.applicBailRb.isSelected()) {
				defHearingRecordValue.setNewBailStatus(HearingRecordConstants.BAIL_STATUS);
			} else if (this.applicCustodyRb.isSelected()) {
				defHearingRecordValue.setNewBailStatus(HearingRecordConstants.CUSTODY_STATUS);
			} else if (this.applicInCareRb.isSelected()) {
				defHearingRecordValue.setNewBailStatus(HearingRecordConstants.IN_CASE_STATUS);
			} else if (this.applicNotApplicableRb.isSelected()) {
				defHearingRecordValue.setNewBailStatus(HearingRecordConstants.NOT_APPLICABLE_STATUS);
			} else if (this.applicNoneRb.isSelected()) {
				defHearingRecordValue.setNewBailStatus(null);
			}
		}
	}

	public void populateScreen() throws DefendantControllerException {
		XHIBITConstant.debug("in bail custody populate screen method");

		if ((model.getHearingRecordUpdateVal() != null)
				&& (model.getHearingRecordUpdateVal().getDefHearingRecordValue() != null)) {

			// bail status at beginning of hearing radio buttons

			if (HearingRecordConstants.BAIL_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getStartBailStatus())) {
				this.detBailRb.setSelected(true);
			} else if (HearingRecordConstants.CUSTODY_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getStartBailStatus())) {
				this.detCustodyRb.setSelected(true);
			} else if (HearingRecordConstants.IN_CASE_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getStartBailStatus())) {
				this.detInCareRb.setSelected(true);
			} else if (HearingRecordConstants.NOT_APPLICABLE_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getStartBailStatus())) {
				this.detNotApplicRb.setSelected(true);

			}

			// hearing type combo box
			this.hearingTypeCbx.setSelectedIndex(-1);
			final Integer defHearingId = model.getHearingRecordUpdateVal().getDefHearingRecordValue()
					.getRefDefHearingTypeID();

			for (int i = 0, n = this.comboData.size(); i < n; i++) {
				final Integer id = ((ComboBoxRow) comboData.get(i)).getId();

				if (id.equals(defHearingId)) {
					this.hearingTypeCbx.setSelectedIndex(i);
					break;
				}
			}

			// DefInAbsence checkboxes
			if ("Y".equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getTrialInDefAbsence())) {
				this.trialInDefAbsence.setSelected(true);
			}
			if ("Y".equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getSentenceInDefAbsence())) {
				this.sentenceInDefAbsence.setSelected(true);
			}

			// oral evidence check box
			if (HearingRecordConstants.ORAL_EVIDENCE_YES
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getOralEvidence())) {
				this.oralEvidCb.setSelected(true);
			}

			// date of application
			this.applicDate
					.setDate(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getDateBailApplication());

			// result radio buttons
			if (HearingRecordConstants.RESULT_BAIL_APP_GRANTED
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getResultBailApplication())) {
				this.grantedRb.setSelected(true);
			} else if (HearingRecordConstants.RESULT_BAIL_APP_REFUSED
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getResultBailApplication())) {
				this.refusedRb.setSelected(true);
			}

			// new bail status radio buttons
			if (HearingRecordConstants.BAIL_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getNewBailStatus())) {
				this.applicBailRb.setSelected(true);
			} else if (HearingRecordConstants.CUSTODY_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getNewBailStatus())) {
				this.applicCustodyRb.setSelected(true);
			} else if (HearingRecordConstants.IN_CASE_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getNewBailStatus())) {
				this.applicInCareRb.setSelected(true);
			} else if (HearingRecordConstants.NOT_APPLICABLE_STATUS
					.equals(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getNewBailStatus())) {
				this.applicNotApplicableRb.setSelected(true);
			}

			// date new status started
			this.newStatusDate
					.setDate(model.getHearingRecordUpdateVal().getDefHearingRecordValue().getStartDateNewBailStatus());
		}

		stepUpdateViewState();

		XHIBITConstant.debug("End of bail custody populate screen method.");
		if (model.getCaseId() != null && model.getDefendantId() != null) {
			DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
			ArrayList<DefendantOnCaseBasicValue> arr = (ArrayList<DefendantOnCaseBasicValue>) del
					.findByCaseId(model.getCaseId());
			if (!arr.isEmpty()) {
				for (DefendantOnCaseBasicValue def : arr) {
					if (model.getDefendantId().equals(def.getDefendantID())) {
						model.setDefOnCaseBasicValue(def);
					}
				}
			}
		}
	}

	// method used to set all screen components to read only if cfa already
	// exported.
	public void setScreenReadOnly() {
		this.detBailRb.setEnabled(false);
		this.detCustodyRb.setEnabled(false);
		this.detInCareRb.setEnabled(false);
		this.detNotApplicRb.setEnabled(false);
		this.hearingTypeCbx.setEditable(false);
		this.hearingTypeCbx.setEnabled(false);
		this.hraCb.setEnabled(false);
		this.trialInDefAbsence.setEnabled(false);
		this.sentenceInDefAbsence.setEnabled(false);
		this.oralEvidCb.setEnabled(false);

		this.applicDate.setDateEnabled(false);
		this.applicDate.setDateEditable(false);
		this.grantedRb.setEnabled(false);
		this.refusedRb.setEnabled(false);
		this.resultNoneRb.setEnabled(false);
		this.applicBailRb.setEnabled(false);
		this.applicCustodyRb.setEnabled(false);
		this.applicInCareRb.setEnabled(false);
		this.applicNotApplicableRb.setEnabled(false);
		this.applicNoneRb.setEnabled(false);
		this.tep.setEnabled(false);
		this.newStatusDate.setDateEnabled(false);
		this.newStatusDate.setDateEditable(false);
	}

	public void addListeners(final HearingRecordPanel.UpdateListener updateListener) {
		// adding listeners to updatable fields
		XHIBITConstant.debug("Adding listeners in bail custody");

		MyActionListener myActionListener = new MyActionListener();

		// get the update listener from the model for all components
		this.detBailRb.addActionListener(updateListener);
		this.detCustodyRb.addActionListener(updateListener);
		this.detInCareRb.addActionListener(updateListener);
		this.detNotApplicRb.addActionListener(updateListener);
		this.hearingTypeCbx.addActionListener(updateListener);
		this.hearingTypeCbx.addActionListener(myActionListener);

		this.trialInDefAbsence.addActionListener(updateListener);
		this.sentenceInDefAbsence.addActionListener(updateListener);
		this.oralEvidCb.addActionListener(updateListener);

		this.grantedRb.addActionListener(myActionListener);
		this.refusedRb.addActionListener(myActionListener);
		this.resultNoneRb.addActionListener(myActionListener);
		this.applicDate.getDateComponent().addMChangeListener(updateListener);

		this.applicBailRb.addActionListener(myActionListener);
		this.applicCustodyRb.addActionListener(myActionListener);
		this.applicInCareRb.addActionListener(myActionListener);
		this.applicNotApplicableRb.addActionListener(myActionListener);
		this.applicNoneRb.addActionListener(myActionListener);
		this.newStatusDate.getDateComponent().addMChangeListener(updateListener);

		tep.addPropertyChangeListener(XPanel.property_modified, new MyModifyPropertyListener());
	}

	/**
	 * Saves the Case Trial Time. This is only called if the time estiamte panel
	 * has changed and the selected hearing type is "Pleas and Directions"
	 */
	private void saveCaseTrialTime() {
		log.debug("saveCaseTrialTime() - Begin");

		final DirectionsForCaseValue directionsForCaseValue = model.getHearingRecordUpdateVal()
				.getDirectionsForCaseValue();

		final HashMap h = new HashMap();

		// ensure the model the time estimation panel points at is current
		tep.setModel(directionsForCaseValue);
		tep.moveScreenToModel();

		final CourtLogCRUDValue crud = new CourtLogCRUDValue();

		crud.setEntryFreeText("");
		crud.setEventType(PDHConstants.CASE_TRIALTIME);
		crud.setEntryDate(Calendar.getInstance().getTime());
		crud.setCaseId(model.getCaseId());
		crud.setInCourt(XhibitSingleton.getInstance().isUserInCourtroom()); // XI2B134
		crud.setProperty("Directions_By_Case_Options", h);

		tep.populateCRUD(h, null);

		CourtLogCRUDValue[] cruds;

		if (directionsForCaseValue.getCourtLogCRUDValues() == null
				|| directionsForCaseValue.getCourtLogCRUDValues().length == 0) {
			cruds = new CourtLogCRUDValue[] { crud };
			directionsForCaseValue.setCourtLogCRUDValues(cruds);
		} else {
			// Check if there is already a trial time estimate event listed
			// if so replace it, otherwise add it to the end
			cruds = directionsForCaseValue.getCourtLogCRUDValues();
			Collection c = new ArrayList();
			boolean replaced = false;
			if (cruds != null) {
				for (int i = 0; i < cruds.length; i++) {
					if (cruds[i].getEventType().equals(PDHConstants.CASE_TRIALTIME)) {
						cruds[i] = crud;
						replaced = true;
						break;
					} else {
						c.add(cruds[i]);
					}
				}
			}

			if (!replaced) {
				c.add(crud);
				cruds = new CourtLogCRUDValue[c.size()];
				c.toArray(cruds);
			}
		}

		directionsForCaseValue.setCourtLogCRUDValues(cruds);

		if (directionsForCaseValue.getDirectionsForCaseBasicValue().getCaseId() == null) {
			directionsForCaseValue.getDirectionsForCaseBasicValue().setCaseId(model.getCaseId());
		}

		log.debug("saveCaseTrialTime() - trial time estimate = "
				+ directionsForCaseValue.getDirectionsForCaseBasicValue().getTrialTimeEstimate());
		if (directionsForCaseValue.getDirectionsForCaseBasicValue().getTrialTimeEstimate() == null) {
			directionsForCaseValue.getDirectionsForCaseBasicValue().setTrialTimeEstimate(new Float(-1));
			directionsForCaseValue.getDirectionsForCaseBasicValue().setTrialTimeUnit(new Integer(2));
		}
	}

	public HearingRecordModel getModel() {
		return this.model;
	}

	public JRadioButton getGrantedRb() {
		return this.grantedRb;
	}

	public JRadioButton getRefusedRb() {
		return this.refusedRb;
	}

	public JRadioButton getResultNoneRb() {
		return this.resultNoneRb;
	}

	public Date getApplicDate() {
		try {
			return this.applicDate.getDate().getTime();
		}
		catch (Exception e) {
			return null;
		}
	}

	public Date getNewBailStatusDate() {
		try {
			return this.newStatusDate.getDate().getTime();
		}
		catch (Exception e) {
			return null;
		}
	}

	public JRadioButton getApplicBailRb() {
		return this.applicBailRb;
	}

	public JRadioButton getApplicCustodyRb() {
		return this.applicCustodyRb;
	}

	public JRadioButton getApplicInCareRb() {
		return this.applicInCareRb;
	}

	public JRadioButton getApplicNotApplicableRb() {
		return this.applicNotApplicableRb;
	}

	public JRadioButton getApplicNoneRb() {
		return this.applicNoneRb;
	}

	public void stepActivate() {
		log.debug("stepActivate - Begin");
		stepUpdateViewState();
	}

	public void stepValidate() throws CSValidationException {
		
		// bail status at beginning of hearing radio buttons is mandatory ctx-3404
		if (!this.detBailRb.isSelected() && !this.detCustodyRb.isSelected() && !this.detInCareRb.isSelected() && !this.detNotApplicRb.isSelected()) {
			throw new CSValidationException("gui.hearingrecord.update.hearingDetails.bailCustody.statusBeginningHearing",
					"Crest Form A.Bail Custody.New Status Date is required");
		}

		// hearing type combo box is mandatory ctx-3404
		if (this.hearingTypeCbx.getSelectedIndex() == -1) {
			throw new CSValidationException("gui.hearingrecord.update.hearingDetails.bailCustody.hearingType",
					"Crest Form A.Bail Custody.New Status Date is required");
		}
		
		if ((getGrantedRb().isSelected() || getRefusedRb().isSelected()) && (getApplicDate() == null)) {
			throw new CSValidationException(
					"gui.hearingrecord.update.hearingDetails.bailCustody.applicationDateRequired",
					"Crest Form A.Bail Custody.Application Date is required");
		}

		if ((getApplicBailRb().isSelected() || getApplicCustodyRb().isSelected() || getApplicInCareRb().isSelected()
				|| getApplicNotApplicableRb().isSelected()) && (getNewBailStatusDate() == null)) {
			throw new CSValidationException("gui.hearingrecord.update.hearingDetails.bailCustody.newStatusDateRequired",
					"Crest Form A.Bail Custody.New Status Date is required");
		}
	}

	public void maybeDisplayMessage() {
		if (this.hearingTypeCbx.getSelectedIndex() != -1) {
			ComboBoxRow item = ((ComboBoxRow) this.hearingTypeCbx.getSelectedItem());

			// If PAD was the original hearing type and it has been changed,
			// then inform the user that this will not remove the time
			// estimate
			if (hearingTypeStartValue != null && PAD_CODE.equalsIgnoreCase(hearingTypeStartValue.code)
					&& !PAD_CODE.equalsIgnoreCase(item.code)) {
				JOptionPane.showMessageDialog(this,
						XHIBITConstant.getResource(XhibitBundles.ErrorText,
								"gui.hearingrecord.hearingTypeChangedFromPAD"),
						XHIBITConstant.getResource(XhibitBundles.ErrorText,
								"gui.hearingrecord.hearingTypeChangedFromPAD.title"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void stepUpdateViewState() {
		if (this.resultNoneRb.isSelected()) {
			this.applicDate.setDate((Date) null);
		}

		if (this.applicNoneRb.isSelected()) {
			this.newStatusDate.setDate((Date) null);
		}

		this.applicDate.setDateEnabled(this.grantedRb.isSelected() || this.refusedRb.isSelected());
		
		this.newStatusDate.setDateEnabled(this.applicBailRb.isSelected() || this.applicCustodyRb.isSelected()
				|| this.applicInCareRb.isSelected() || this.applicNotApplicableRb.isSelected());

		// only enable the time estimate panel if for a pleas and directions
		// hearing
		if (isPleasAndDirectionsHearingTypeSelected()) {
			tep.setEnabled(true);
		} else {
			if (this.trialTimeEstimateStartState == null) {
				final DirectionsForCaseValue directionsForCaseValue = model.getHearingRecordUpdateVal()
						.getDirectionsForCaseValue();
				directionsForCaseValue.getDirectionsForCaseBasicValue().setTrialTimeEstimate(null);
				directionsForCaseValue.getDirectionsForCaseBasicValue().setTrialTimeUnit(new Integer(2));
				tep.setModel(directionsForCaseValue);
				tep.moveModelToScreen();
			}
			tep.setEnabled(false);
		}
	}

	private class MyModifyPropertyListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent e) {
			if (((Boolean) e.getNewValue()).booleanValue()) {
				String timeText = tep.getTimeFieldText().trim();
				if (trialTimeEstimateStartState == null && (timeText == null || timeText == "")) {
					// The time estimate field started as null, was changed
					// to a
					// value and then reset, therefore it has not changed.
					tepChanged = false;
				} else {
					// indicate that the time estimation panel has changed
					// value
					tepChanged = true;
					getModel().setUpdated(true);
				}
			}
		}
	}

	private class MyActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			getModel().setUpdated(true);
			stepUpdateViewState();
		}
	}

	/**
	 * Extracted method to indicate whether the pleas and directions type has
	 * been selected from the hearing type combo box
	 * 
	 * @return <i>true</i> if Pleas and Directions type has been selected,
	 *         <i>false</i> otherwise
	 */
	public boolean isPleasAndDirectionsHearingTypeSelected() {
		ComboBoxRow row = (ComboBoxRow) this.hearingTypeCbx.getSelectedItem();
		return ((row != null) ? row.isPleasAndDirections() : false);
	}

	/**
	 * Private class used to represent a row in the hearing type combo box.
	 */
	private class ComboBoxRow {
		private final Integer id;

		private final String code;

		private final String displayValue;

		/**
		 * Only constructor to create this row object.
		 * 
		 * @param id
		 *            An <code>Integer</code> representing the hearing type id
		 * @param code
		 *            A <code>String</code> containing the 3 digit code
		 *            reresenting this hearing type
		 * @param displayValue
		 *            A <code>String</code> of what is to be displayed in the
		 *            combo box for the user to see
		 */
		public ComboBoxRow(final Integer id, final String code, final String displayValue) {
			this.displayValue = displayValue;
			this.code = code;
			this.id = id;
		}

		/**
		 * Accessor method for the hearing type id
		 * 
		 * @return An <code>Integer</code>
		 */
		public Integer getId() {
			return this.id;
		}

		/**
		 * Indicate whether this hearing type is for pleas and directions
		 * 
		 * @return <i>true</i> if this is the Pleas and Directions type,
		 *         <i>false</i> otherwise
		 */
		public boolean isPleasAndDirections() {
			return (PAD_CODE).equals(this.code);
		}

		/**
		 * Overridden method that retuns the displayValue, as it is this method
		 * that is called to show in the combo box.
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return this.displayValue;
		}
	}

	public JComboBox getHearingTypeCbx() {
		return hearingTypeCbx;
	}
}
