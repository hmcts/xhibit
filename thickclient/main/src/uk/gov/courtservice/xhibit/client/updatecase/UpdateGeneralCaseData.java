package uk.gov.courtservice.xhibit.client.updatecase;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.services.shjustice.SHJusticeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.helper.FormattedDisplayHelper;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRHearingDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordDisplayValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HearingRecordValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.AttendeeValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.schedule.HearingProgressValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.OpenSearchJudgeAction;
import uk.gov.courtservice.xhibit.client.hearingrecord.HearingRecordModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2 - Implementation of LP80_04_01_X2GUIDesign CaseProperties.doc
 * </p>
 * <p>
 * Description: This Panel is instantiated with a Header Value Object and will
 * display the relevant 'general' hearing information such as time listed, case
 * number, hearing type and judges and justices as appropriate. (there are more
 * details on this panel, documented in LP80_04_01_X2GUIDesign
 * CaseProperties.doc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 */
public class UpdateGeneralCaseData extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Logger log = CSServices.getLogger(UpdateGeneralCaseData.class);

	public static final String mustSelectError = "validation.mustselect";

	public static final String prosEvidencePagesMandatoryError = "gui.updateGeneralCaseData.invalid.ProsEvidencePages.TCase.mandatory";

	private static final int JP_MAX_FIELD_SIZE = 50;

	// only to be set in the constructor...
	private final UpdateCasePanel uc;

	private CaseBasicValue aCase = null;

	private Vector<String> classCodeVector = new Vector<String>();

	private Vector<String> offenceGroupCodeVector = new Vector<String>();

	private Vector<String> caseProgVector = new Vector<String>();

	private Hashtable<String, String> caseProgressIndicators = new Hashtable<String, String>();

	private Hashtable<String, String> caseProgressMappings = new Hashtable<String, String>();

	private boolean valuesUpdated = false;

	private boolean judgeUpdated = false;

	private XHearingProgressModel xHearingProgressModel;

	private JLabel appealAgainstType;

	private JTextField judgeText;

	private JLabel hearingTypeText;

	private JCheckBox hideCaseInPublicDisplay;

	private JComboBox caseProgressComboBox;

	private JPanel indictmentSeveredPanel;

	private JRadioButton indictmentSeveredYesRb;

	private JRadioButton indictmentSeveredNoRb;

	private JRadioButton indictmentSeveredNARb;

	private JButton judgeAddButton;

	private XTextField txtJusticeOfPeace1;
	private XTextField txtJusticeOfPeace2;
	private XTextField txtJusticeOfPeace3;
	private XTextField txtJusticeOfPeace4;

	private JComboBox offenceGroupCodeComboBox;

	private JComboBox classCodeComboBox;

	// declare all other instance variables...
	private final UpdateGeneralCaseDataListener changeListener = new UpdateGeneralCaseDataListener();

	private CSRecoverableException jbInitException;

	private PersonValue judge = null;

	private boolean isAppealCase = false;

	private boolean isCombinedCase = false;

	private boolean isTrialCase = false;

	private JPanel containerPanel;
	private JPanel mainPanel;
	
	//section 28
	private static final String S28_ELIGIBLE_YES = "Y";
	private static final String S28_ELIGIBLE_NO = "N";
	private static final String S28_ORDER_MADE_YES = "Y";
	private static final String S28_ORDER_MADE_NO = "N";
	
	private JRadioButton s28EligibleYesRb;
	private JRadioButton s28EligibleNoRb;
	private JRadioButton s28EligibleNARb;
	private JRadioButton s28OrderMadeYesRb;	
	private JRadioButton s28OrderMadeNoRb;
	private JRadioButton s28OrderMadeNARb;


	public UpdateGeneralCaseData(UpdateCasePanel uc) throws CSRecoverableException {
		this.uc = uc;
		try {
			stepInitialise();

			jbInit();
			if (jbInitException != null)
				throw this.jbInitException;

			stepActivate();
		} catch (final Exception e) {
			log.error(e);
			// String errorMessage = "UpdateGeneralCaseData(UpdateCase uc) threw
			// exception";
			// throw new
			// CSRecoverableException("gui.updateGeneralCaseData.ConstructorFailed",
			// errorMessage, e);
			XHIBITErrorHandler.handleError(e);
		}
	}

	/**
	 * layout the controls on the screen.
	 */
	private void jbInit() {
		setToolTipText(getResource("updateGeneralCasePropertiesToolTip"));

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(550, 550));
		GridBagConstraints gbc = getGridBagLayout();
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		JScrollPane scrollPane = new JScrollPane(mainPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.setPreferredSize(new Dimension(400, 400));
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		this.add(scrollPane, gbc);

		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weighty = 0.5;
		gbc.gridwidth = 4;
		mainPanel.add(getContainerPanel(), gbc);
		gbc.gridy++;
		gbc.gridx = 0;
		mainPanel.add(getSection28Panel(), gbc);
	}

	private JPanel getContainerPanel() {
		if (containerPanel == null) {
			containerPanel = new JPanel();
			GridBagConstraints gbc = getGridBagLayout();
			gbc.anchor = GridBagConstraints.WEST;
			containerPanel.setLayout(new GridBagLayout());

			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			// For combined courts case, the maintain hearing header/case
			// properties
			// component only displays the general tab with minimal case
			// informtion.
			// Other case types may show significantly more information. (see
			// the
			// else clause)

			// PRE00144 - only combined cases should be handled differently
			if (isCombinedCase) {
				// create all of the labels...
				final JLabel caseTypeLabel = createJLabel("lblCaseType");
				final JLabel caseNumberLabel = createJLabel("lblCaseNumber");
				final JLabel caseTitleLabel = createJLabel("lblCaseTitle");

				// create all of the text fields...
				final JLabel caseTypeText = new JLabel(aCase.getCaseType() == null ? "n/a" : aCase.getCaseType());
				final JLabel caseNumberText = new JLabel(
						aCase.getCaseNumber() == null ? "n/a" : aCase.getCaseNumber().toString());
				final JLabel caseTitleText = new JLabel(clearNull(aCase.getCaseTitle()));

				// now add everything to the container panel...
				gbc.gridx = 0;
				containerPanel.add(caseTypeLabel, gbc);
				containerPanel.add(caseNumberLabel, gbc);
				containerPanel.add(caseTitleLabel, gbc);

				gbc.gridx = 1;
				containerPanel.add(caseTypeText, gbc);
				containerPanel.add(caseNumberText, gbc);
				containerPanel.add(caseTitleText, gbc);
			} else {
				final JLabel caseNumberLabel = createJLabel("lblCaseNumber");
				final JLabel timeListedLabel = createJLabel("lblTimeListed");
				final JLabel hearingTypeLabel = createJLabel("lblHearingType");
				final JLabel appealAgainstLabel = createJLabel("lblAppealAgainst");

				final JLabel caseProgressLabel = createJLabel("lblCaseProgress");
				final JLabel judgeLabel = createJLabel("lblJudge");

				// ---------------------- CASE NUMBER ----------------------
				final JLabel caseNumberText = new JLabel();
				String caseTypeString = clearNull(aCase.getCaseType());
				Integer caseNumber = aCase.getCaseNumber() == null ? new Integer(0) : aCase.getCaseNumber();
				String displayCaseNumber = caseTypeString.concat(caseNumber.toString());
				caseNumberText.setText(displayCaseNumber);

				// ---------------------- CASE TIME LISTED
				// ----------------------
				final JLabel timeListedText = new JLabel();
				try {
					timeListedText.setText(XDateFormat.format(uc.hhv.getTimeListed(), XDateFormat.TIMEFORMAT));
				} catch (final Exception e1) {
					log.error("Exception occured whilst getting the time listed from the CourtLogHeaderValue");
					log.error(e1);
					String msgKey = "gui.updategeneralcasedata.getTimeListed";
					addException(new CSRecoverableException(msgKey, msgKey, e1));
				}

				caseProgressComboBox = new JComboBox();

				// now add the labels to the first column of the screen...
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.insets = XHIBITConstant.nonContainerInsets;
				containerPanel.add(caseNumberLabel, gbc);
				gbc.gridy++;
				containerPanel.add(timeListedLabel, gbc);
				gbc.gridy++;
				containerPanel.add(hearingTypeLabel, gbc);
				gbc.gridy++;
				if (uc.isCriminalAppealHearing()) {
					containerPanel.add(appealAgainstLabel, gbc);
					gbc.gridy++;
				}
				
				// X55142 - only display class offence and group code fields for
				// trial case
				if (isTrialCase) {
					final JLabel classCodeLabel = createJLabel("lblOffenceClass");
					final JLabel offGroupCodeLabel = createJLabel("lblOffenceGroup");
					final JLabel indicSeveredLabel = createJLabel("lblIndicmentServerd");
					containerPanel.add(classCodeLabel, gbc);
					gbc.gridy++;
					containerPanel.add(offGroupCodeLabel, gbc);
					gbc.gridy++;
					containerPanel.add(indicSeveredLabel, gbc);
					gbc.gridy++;
				}
				containerPanel.add(caseProgressLabel, gbc);
				gbc.gridy++;
				containerPanel.add(judgeLabel, gbc);
				gbc.gridy++;

				// now add the rest of the columns...
				gbc.gridx = 1;
				gbc.gridy = 0;
				gbc.weightx = 0.9;

				containerPanel.add(caseNumberText, gbc);
				gbc.gridy++;
				containerPanel.add(timeListedText, gbc);
				gbc.gridy++;
				containerPanel.add(getHearingTypeText(), gbc);
				gbc.gridy++;
				if (uc.isCriminalAppealHearing()) {
					containerPanel.add(getAppealAgainst(), gbc);
					gbc.gridy++;
				}

				if (isTrialCase) {
					classCodeComboBox = new JComboBox(classCodeVector);
					offenceGroupCodeComboBox = new JComboBox(offenceGroupCodeVector);

					// ensure that both boxes are the same size
					final Dimension dim = new Dimension(40, XHIBITConstant.getLineHeight());
					classCodeComboBox.setPreferredSize(dim);
					offenceGroupCodeComboBox.setPreferredSize(dim);
					containerPanel.add(classCodeComboBox, gbc);
					gbc.gridy++;
					containerPanel.add(offenceGroupCodeComboBox, gbc);
					gbc.gridy++;
					containerPanel.add(getIndictmentSeveredPanel(), gbc);
					gbc.gridy++;
				}
				containerPanel.add(caseProgressComboBox, gbc);
				gbc.gridy++;

				containerPanel.add(getJudgeJTextField(), gbc);
				gbc.gridx++;

				gbc.weightx = 0.2;
				XAction openSearchJudgeAction = XhibitActions.getAction(uc.ucd.xac, XhibitActions.OpenSearchJudge);
				openSearchJudgeAction.setCaller(this);
				judgeAddButton = new JButton(openSearchJudgeAction);
				containerPanel.add(judgeAddButton, gbc);
				gbc.gridx--;
				gbc.gridy++;

				gbc.gridx--;
				gbc.weightx = 0.9;
				if (isAppealCase || (uc.getHearingRecordModel()!=null && uc.getHearingRecordModel().getJusticeEdited())) {		
					// modification for ctx-3296
					if(uc.getHearingRecordModel() != null && !uc.getHearingRecordModel().getCaseId().equals(aCase.getCaseId())) {
						uc.setHearingRecordModel(null);
					}
					
					if(uc.getHearingRecordModel() == null) {
						try {
							HearingRecordModel hrModel = new HearingRecordModel();
							hrModel.setCaseId(aCase.getCaseId());
							hrModel.setHearingId(uc.getHearingScheduleBD().getHearingIdFromScheduleHearing(uc.getScheduledHearingId()));
							
							
							SHJusticeControllerBeanBusinessDelegate shDel = XhibitDelegateHelper.getSHJusticeDelegate();
							ArrayList<SHJusticeBasicValue> results = (ArrayList<SHJusticeBasicValue>)shDel.findByHearingId(hrModel.getHearingId());
								
							HearingRecordValue hrValue = new HearingRecordValue();
							HearingRecordDisplayValue hrDispValue = new HearingRecordDisplayValue();
							HRHearingDisplayValue hrHearDispValue = new HRHearingDisplayValue();
							hrHearDispValue.setHrJusticeValues(results);
							
							hrDispValue.setHrHearingDisplayValue(hrHearDispValue);
							hrValue.setHearingRecordDisplayValue(hrDispValue);
							hrModel.setHearingRecordVal(hrValue);
							uc.setHearingRecordModel(hrModel);
							
						} catch (ObjectNotFoundException e) {
							XHIBITConstant.handleError(e, this.getClass());
						} catch (FinderException e) {
							XHIBITConstant.handleError(e, this.getClass());
						}
					}
					
					JLabel appealJusticeLabel = new JLabel("Justices :");
					gbc.anchor = GridBagConstraints.WEST;
					gbc.fill = GridBagConstraints.NONE;
					containerPanel.add(appealJusticeLabel, gbc);

					gbc.anchor = GridBagConstraints.NORTHEAST;
					JLabel appealJusticeLabel1 = new JLabel("1.");
					containerPanel.add(appealJusticeLabel1, gbc);
					gbc.gridy++;

					JLabel appealJusticeLabel2 = createJLabel("jps2");
					containerPanel.add(appealJusticeLabel2, gbc);
					gbc.gridy++;

					JLabel appealJusticeLabel3 = createJLabel("jps3");
					containerPanel.add(appealJusticeLabel3, gbc);
					gbc.gridy++;

					JLabel appealJusticeLabel4 = createJLabel("jps4");
					containerPanel.add(appealJusticeLabel4, gbc);
					gbc.gridy++;

					gbc.anchor = GridBagConstraints.WEST;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.gridx++;
					gbc.gridy -= 4;
					containerPanel.add(getJusticeOfPeace1(), gbc);
					gbc.gridy++;

					containerPanel.add(getJusticeOfPeace2(), gbc);
					gbc.gridy++;

					containerPanel.add(getJusticeOfPeace3(), gbc);
					gbc.gridy++;

					containerPanel.add(getJusticeOfPeace4(), gbc);
					gbc.gridy++;
					gbc.gridx--;
          if(uc.getHearingRecordModel()!=null) {
            populateJustices();
          }
				}
				JLabel hideCaseInPublicDisplayLabel = createJLabel("lblHideCaseInPublicDisplay");
				containerPanel.add(hideCaseInPublicDisplayLabel, gbc);
				gbc.gridx++;

				containerPanel.add(getHideCaseInPublicDisplayCheckBox(), gbc);

			}

		}
		return containerPanel;
	}

	/**
	 * Default gridbag that's used throughout the panels.
	 * 
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

	protected void populateJustices() {
		final HearingRecordDisplayValue displayValue = uc.getHearingRecordModel().getHearingRecordVal()
				.getHearingRecordDisplayValue();
		final HRHearingDisplayValue hRDisplayValue = displayValue.getHrHearingDisplayValue();

		Collection justiceValues = hRDisplayValue.getHrJusticeValues();

		if (justiceValues != null) {
			ArrayList<SHJusticeBasicValue> jpsArr = new ArrayList<SHJusticeBasicValue>();
			Iterator it = justiceValues.iterator();

			while (it.hasNext()) {
				SHJusticeBasicValue justice = (SHJusticeBasicValue) it.next();
				if (justice != null) {
					if (justice.getHearingID() == null) {
						justice.setHearingID(hRDisplayValue.getHearingID());
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
			}

			if (jpsArr.size() > 1) {
				if (jpsArr.get(1) != null && jpsArr.get(1).getJusticeName() != null) {
					txtJusticeOfPeace2.setText(jpsArr.get(1).getJusticeName().toString());
				}
			} else {
				jpsArr.add(null);
			}
			if (jpsArr.size() > 2) {
				if (jpsArr.get(2) != null && jpsArr.get(2).getJusticeName() != null) {
					txtJusticeOfPeace3.setText(jpsArr.get(2).getJusticeName().toString());
				}
			} else {
				jpsArr.add(null);
			}
			if (jpsArr.size() > 3) {
				if (jpsArr.get(3) != null && jpsArr.get(3).getJusticeName() != null) {
					txtJusticeOfPeace4.setText(jpsArr.get(3).getJusticeName().toString());
				}
			} else {
				jpsArr.add(null);
			}
			justiceValues = jpsArr;
			uc.getHearingRecordModel().getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue()
					.setHrJusticeValues(justiceValues);
		}

	}

	private JCheckBox getHideCaseInPublicDisplayCheckBox() {
		if (hideCaseInPublicDisplay == null) {
			hideCaseInPublicDisplay = new JCheckBox();
			hideCaseInPublicDisplay.setSelected(aCase.isHideCaseInPublicDisplay());
		}
		return hideCaseInPublicDisplay;
	}

	private XTextField getJusticeOfPeace1() {
		if (this.txtJusticeOfPeace1 == null) {
			this.txtJusticeOfPeace1 = new XTextField();
			this.txtJusticeOfPeace1.setUpperCase(true);
			this.txtJusticeOfPeace1.setColumns(15);
			this.txtJusticeOfPeace1.setMaxLength(JP_MAX_FIELD_SIZE);
			this.txtJusticeOfPeace1.setEnabled(uc.ucd.openingAction.isEditable());
			this.txtJusticeOfPeace1.setVisible(true);
		}
		return this.txtJusticeOfPeace1;
	}

	private XTextField getJusticeOfPeace2() {
		if (this.txtJusticeOfPeace2 == null) {
			this.txtJusticeOfPeace2 = new XTextField();
			this.txtJusticeOfPeace2.setUpperCase(true);
			this.txtJusticeOfPeace2.setColumns(15);
			this.txtJusticeOfPeace2.setMaxLength(JP_MAX_FIELD_SIZE);
			this.txtJusticeOfPeace2.setEnabled(uc.ucd.openingAction.isEditable());
			this.txtJusticeOfPeace2.setVisible(true);
		}
		return this.txtJusticeOfPeace2;
	}

	private XTextField getJusticeOfPeace3() {
		if (this.txtJusticeOfPeace3 == null) {
			this.txtJusticeOfPeace3 = new XTextField();
			this.txtJusticeOfPeace3.setUpperCase(true);
			this.txtJusticeOfPeace3.setColumns(15);
			this.txtJusticeOfPeace3.setMaxLength(JP_MAX_FIELD_SIZE);
			this.txtJusticeOfPeace3.setEnabled(uc.ucd.openingAction.isEditable());
			this.txtJusticeOfPeace3.setVisible(true);
		}
		return this.txtJusticeOfPeace3;
	}

	private XTextField getJusticeOfPeace4() {
		if (this.txtJusticeOfPeace4 == null) {
			this.txtJusticeOfPeace4 = new XTextField();
			this.txtJusticeOfPeace4.setUpperCase(true);
			this.txtJusticeOfPeace4.setColumns(15);
			this.txtJusticeOfPeace4.setMaxLength(JP_MAX_FIELD_SIZE);
			this.txtJusticeOfPeace4.setEnabled(uc.ucd.openingAction.isEditable());
			this.txtJusticeOfPeace4.setVisible(true);
		}
		return this.txtJusticeOfPeace4;
	}

	/**
	 * Gets the resources for the case progress options.
	 */
	private void getCaseProgressResources() {
		// need to ensure that each Vector contains an empty first element
		classCodeVector.add("");
		offenceGroupCodeVector.add("");

		try {
			ResourceBundle caseProgRsc = ResourceBundleHelper
					.getResourceBundle(XhibitBundles.XhibitClientDefaultResources);
			final Enumeration caseProgEnum = caseProgRsc.getKeys();

			while (caseProgEnum.hasMoreElements()) {
				String caseProgKey = (String) caseProgEnum.nextElement();

				if (caseProgKey.startsWith(UpdateCaseDialog.HEARINGPROGRESS_RSC_PREFIX)) {
					caseProgVector.add(caseProgRsc.getString(caseProgKey));
					caseProgressIndicators.put(caseProgKey, caseProgRsc.getString(caseProgKey));
				} else if (caseProgKey.startsWith(UpdateCaseDialog.HEARINGPROGRESS_TO_STATIC_RSC_PREFIX)) {
					log.debug("String aKeyWithoutPrefix = caseProgKey.substring("
							+ UpdateCaseDialog.HEARINGPROGRESS_TO_STATIC_RSC_PREFIX.length() + ", "
							+ caseProgKey.length() + ");");
					String aKeyWithoutPrefix = caseProgKey.substring(
							UpdateCaseDialog.HEARINGPROGRESS_TO_STATIC_RSC_PREFIX.length(), caseProgKey.length());
					log.debug("aKeyWithoutPrefix is  " + aKeyWithoutPrefix);

					caseProgressMappings.put(aKeyWithoutPrefix, caseProgRsc.getString(caseProgKey));
				}
				// X55161 - moved definition of drop down values to properties
				// file
				else if (caseProgKey.startsWith(UpdateCaseDialog.HEARINGPROGRESS_CLASS_CODE_PREFIX)) {
					classCodeVector.add(caseProgRsc.getString(caseProgKey));
				} else if (caseProgKey.startsWith(UpdateCaseDialog.HEARINGPROGRESS_OFFENCE_GROUP_CODE_PREFIX)) {
					offenceGroupCodeVector.add(caseProgRsc.getString(caseProgKey));
				}
			}

			// need to sort our combo boxes, to a logical order (i.e. 1 - 9,
			// A - Z)
			Collections.sort(classCodeVector);
			Collections.sort(offenceGroupCodeVector);
		} catch (final Exception e) {
			log.error("Exception occured whilst getting the Case Progress from the CourtLogHeaderValue");
			log.error(e);
			e.printStackTrace();
			String msgKey = "gui.updategeneralcasedata.getCaseProgress";
			addException(new CSRecoverableException(msgKey, msgKey, e));
		}
	}

	/**
	 * Gets the Hearing Progress
	 */
	private String getHearingProgress() {
		String realKey = null;

		if (uc.hhv.getHearingProgress() != null) {
			// find the right key inthe caseprogmapping vector.
			// and make the right selection in the model
			if (uc.ucd.internalDebug)
				log.debug("uc.hhv.getHearingProgress() == " + uc.hhv.getHearingProgress());

			if (uc.hhv.getHearingProgress().equals(HearingProgressValue.ADJOURNED)) {
				String mapKey = caseProgressMappings.get("ADJOURNED");
				realKey = caseProgressIndicators.get(mapKey);
			} else if (uc.hhv.getHearingProgress().equals(HearingProgressValue.FINISHED)) {
				String mapKey = caseProgressMappings.get("FINISHED");
				realKey = caseProgressIndicators.get(mapKey);
			} else if (uc.hhv.getHearingProgress().equals(HearingProgressValue.IN_PROGRESS)) {
				String mapKey = caseProgressMappings.get("IN_PROGRESS");
				realKey = caseProgressIndicators.get(mapKey);
			} else if (uc.hhv.getHearingProgress().equals(HearingProgressValue.TO_BE_HEARD)) {
				String mapKey = caseProgressMappings.get("TO_BE_HEARD");
				realKey = caseProgressIndicators.get(mapKey);
			} else {
				log.debug("no matching hearingProgessValue static to match up with");
				// leave realKey as null and return null
			}
		} else {
			log.error("CaseProgress is null (uc.hhv.getHearingProgress() == null)");
		}
		return realKey;
	}

	/**
	 * Used to intialise or retrieve any data required by this panel.
	 * 
	 * @throws CSRecoverableException
	 */
	private void stepInitialise() throws CSRecoverableException {
		aCase = uc.hhv.getHhCase();
		if (aCase == null) {
			String errorMessage = "UpdateGeneralCaseData(UpdateCase uc) retrieved null on uc.hhv.getHhCase()";
			throw new CSRecoverableException("gui.updateGeneralCaseData.CaseDetailsNotFound", errorMessage, null);
		}

		isAppealCase = uc.isCriminalAppealHearing() || uc.isMiscAppealCase();
		isCombinedCase = uc.isCombinedCase();
		isTrialCase = uc.isTrialCase();

		getCaseProgressResources();
	}

	/**
	 * Moves the data from the model to the screen controls.
	 */
	private void moveModelToScreen() {
		if (!isCombinedCase) {
			if (isTrialCase) {
				classCodeComboBox
						.setSelectedItem((aCase.getClassCode() == null) ? null : aCase.getClassCode().toString());
				offenceGroupCodeComboBox.setSelectedItem(aCase.getOffenceGroupCode());
				populateSeveredIndictment(aCase.getCrestSeveredInd());
			}
			
			populateS28RadioButtons(aCase.getS28Eligible(), aCase.getS28OrderMade());

			xHearingProgressModel = new XHearingProgressModel(caseProgVector);
			caseProgressComboBox.setModel(xHearingProgressModel);

			String hearingProgress = getHearingProgress();
			if (hearingProgress != null) {
				xHearingProgressModel.setSelectedItem(hearingProgress);
			}
		}
	}

	/**
	 * Adds the listeners for the screen controls.
	 */
	private void addListeners() {
		if (!isCombinedCase) {
			if (isTrialCase) {
				classCodeComboBox.getModel().addListDataListener(changeListener);
				offenceGroupCodeComboBox.getModel().addListDataListener(changeListener);
			}
			if ((uc.getHearingRecordModel()!=null && uc.getHearingRecordModel().getJusticeEdited()) || isAppealCase) {
				getJusticeOfPeace1().getDocument().addDocumentListener(changeListener);
				getJusticeOfPeace2().getDocument().addDocumentListener(changeListener);
				getJusticeOfPeace3().getDocument().addDocumentListener(changeListener);
				getJusticeOfPeace4().getDocument().addDocumentListener(changeListener);
			}
			xHearingProgressModel.addListDataListener(changeListener);
			this.getHideCaseInPublicDisplayCheckBox().addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent changeEvent) {
					setChanged();
				}
			});
		}
	}

	/**
	 * Called when the screen is loaded to set the initial state of the screen.
	 */
	private void stepActivate() {
		log.debug("stepActivate() - Start");

		moveModelToScreen();
		stepUpdateViewState();
		addListeners();
	}

	private void stepUpdateViewState() {
		// PRE00144 - undefined cases should be handled as normal on this tab
		if (!isCombinedCase) {
			final boolean enabled = uc.ucd.openingAction.isEditable();
			final boolean notExported = !uc.isExported().booleanValue();
			
			if (isTrialCase) {
				classCodeComboBox.setEnabled(enabled);
				offenceGroupCodeComboBox.setEnabled(enabled);
				enableSeveredIndictmentPanel(enabled);
			}
			
			String eligibleValue = aCase.getS28Eligible();
			boolean eligibleEnabled = enabled;
			boolean orderMadeEnabled = false;
			if (enabled && eligibleValue != null && eligibleValue.equals(S28_ELIGIBLE_YES)){
				orderMadeEnabled = true;
			}
			enableS28Panel(eligibleEnabled, orderMadeEnabled);

			caseProgressComboBox.setEnabled(enabled);
			judgeText.setEnabled(enabled && notExported);
			judgeAddButton.setEnabled(enabled && notExported);
			getHideCaseInPublicDisplayCheckBox().setEnabled(enabled);
		}
	}

	private void addException(CSRecoverableException e) {
		if (jbInitException == null) {
			jbInitException = e;
		} else {
			try {
				this.jbInitException = new CSRecoverableException(e.getUserMessageAsMessage().getKey(), e.getMessage(),
						this.jbInitException.getCause());
			} catch (final Exception ex) {
				log.debug("start report trouble adding an internal exception");
				ex.printStackTrace();
				log.debug("end report trouble adding an internal exception");
			}
		}
	}

	public void processAddJudge(OpenSearchJudgeAction action) {
		log.debug("processAddJudge(OpenSearchJudgeAction " + action + ")");
		Collection col = action.getResults();
		log.debug("the results collection has " + col.size() + " objects in it.");

		Iterator it = col.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			try {
				log.debug("Found object " + o + " in the OpenSearchJudgeAction's results.");

				RefJudgeBasicValue aNewJudge = (RefJudgeBasicValue) o;

				PersonValue theNewJudge = new PersonValue(aNewJudge.getId(), new Integer(0));

				// PRE00177 - changed to show the required display name
				final String title = FormattedDisplayHelper.getDisplayName(aNewJudge);
				this.judgeText.setText(title);
				this.judge = theNewJudge;

				// indicate that we have changed the details
				setChanged();
				judgeUpdated = true;
			} catch (final Exception e) {
				e.printStackTrace();
				log.error("Exception thrown in processAddJudge whilst casting results objects.");
				log.error(e);
			}
		}
	}

	/**
	 * XPanel life-cycle imitation method which validates the data on this 'tab'
	 * 
	 * @throws CSValidationException
	 */
	public void stepValidate() throws CSValidationException {
		// if T case then ensure class code drop down has been set.
		if (isTrialCase && (classCodeComboBox.getSelectedItem() == null
				|| ((String) classCodeComboBox.getSelectedItem()).trim().length() == 0)) {
			String[] message = new String[] { getResource("lblOffenceClass") };
			throw new CSValidationException(mustSelectError, message, "Class code must be selected.");
		}
	}

	/**
	 * XPanel life cycle imitation method which saves the data on this 'tab' of
	 * the case properties/hearing header. - Case/Hearing Progress - Judge -
	 * Justices (only if appeal hearing)
	 * 
	 * @throws HearingScheduleException
	 * @throws CSRecoverableException
	 */
	public void stepDeactivate() throws HearingScheduleException, CSRecoverableException {
		// only attempt to save the details on this tab if something
		// has actually changed
		if (this.valuesUpdated && this.uc.getModified()) {
			final CaseBasicValue caseValue = this.uc.hhv.getHhCase();
			
			//section 28
			caseValue.setS28Eligible(getS28EligibleScreenValue());
			caseValue.setS28OrderMade(getS28OrderMadeScreenValue());
			
			// the indictment severed flag
			if (isTrialCase) {
				caseValue.setCrestSeveredInd(getSeveredIndictmentOption());

				// class code
				String selectedItem = (String) classCodeComboBox.getSelectedItem();
				caseValue.setClassCode(
						((selectedItem == null) || (selectedItem.length() == 0)) ? null : new Integer(selectedItem));

				// offence group
				selectedItem = (String) offenceGroupCodeComboBox.getSelectedItem();
				caseValue.setOffenceGroupCode(
						((selectedItem == null) || (selectedItem.length() == 0)) ? null : selectedItem);
			} else {
				caseValue.setCrestSeveredInd(null);
			}

			caseValue.setHideCaseInPublicDisplay(this.getHideCaseInPublicDisplayCheckBox().isSelected());
			

			Integer oo = null;
			try {
				final String o = (String) xHearingProgressModel.getSelectedItem();
				oo = xHearingProgressModel.getObjectForHearingProgressStatusLabel(o);
				uc.hhv.setHearingProgress(oo);
			} catch (final Exception e) {
				String msgStr = "Exception during uc.getHearingScheduleBD().updateHHMain(uc.hhv.getHhCase(), "
						+ uc.getScheduledHearingId() + ", " + oo + ")";
				String msgKey = "gui.updategeneralcasedata.updateHHMain";
				log.error(msgStr);
				throw new CSRecoverableException(msgKey, msgStr, e);
			}

			try {
				log.debug("To be saved uc.hhv.getHhCase() " + caseValue.toString());
				log.debug("Using ScheduledHearingId : " + uc.getScheduledHearingId().toString());
				log.debug("Using Hearing Progress : " + uc.hhv.getHearingProgress().toString());
				uc.getHearingScheduleBD().updateHHMain(caseValue, uc.getScheduledHearingId(),
						uc.hhv.getHearingProgress(), XhibitSingleton.getInstance().getUserSession()
								.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
			} catch (HearingScheduleException hse) {
				throw hse;
			} catch (final Exception e) {
				String msgStr = "Exception during uc.getHearingScheduleBD().updateHHMain(uc.hhv.getHhCase(),"
						+ caseValue.getId() + ", " + caseValue.getVersion() + ")";
				String msgKey = "gui.updategeneralcasedata.updateHHMain";
				log.error(msgStr);
				throw new CSRecoverableException(msgKey, msgStr, e);
			}

			// JUDGE SAVING
			if (judgeUpdated) {
				if (uc.ucd.internalDebug)
					log.debug("judge changed. saving now.");
				try {
					int judgeForAllCases = JOptionPane.showOptionDialog(this,
							getResource("onJudgeChanged.changeOnCaseLevelQuestion"),
							getResource("onJudgeChanged.changeOnCaseLevelTitle"), JOptionPane.OK_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null,
							new String[] { "Current Hearing", "Current and Future" }, "Current Hearing");
					final AttendeeValue attendee = new AttendeeValue();

					switch (judgeForAllCases) {
					case JOptionPane.CLOSED_OPTION:
						uc.cancelledPrompt = true;
						throw new UserCancelException();
					case 0:
						attendee.setIsAttendingSubsequentSH(new Boolean(false));
						break;
					case 1:
						attendee.setIsAttendingSubsequentSH(new Boolean(true));
						break;
					}

					if (uc.ucd.internalDebug)
						log.debug("the judge to add: " + judge.toString());
					PersonValue pv = new PersonValue(judge.getId(), new Integer(1));
					pv.setPersonType(PersonValue.JUDGE);
					attendee.setPerson(pv);
					Collection<AttendeeValue> toAdd = new Vector<AttendeeValue>();
					toAdd.add(attendee);
					uc.getHearingScheduleBD().addAttendees(uc.getScheduledHearingId(), toAdd, XhibitSingleton
							.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
				} catch (UserCancelException uce) {
					throw uce;
				} catch (final Exception e) {
					String msgStr = "HearingScheduleException during uc.getHearingScheduleBD().addAttendees(uc.scheduledHearingId, toAdd)";
					String msgKey = "gui.updategeneralcasedata.addAttendees.Judge";
					log.error(msgStr);
					throw new CSRecoverableException(msgKey, msgStr, e);
				}
			}
			if (isAppealCase || (uc.getHearingRecordModel()!=null && uc.getHearingRecordModel().getJusticeEdited())) {
				if (uc.getHearingRecordModel()!=null && uc.getHearingRecordModel().getHearingRecordVal().getHearingRecordDisplayValue() != null) {
					ArrayList<SHJusticeBasicValue> arr = (ArrayList<SHJusticeBasicValue>) uc.getHearingRecordModel()
							.getHearingRecordVal().getHearingRecordDisplayValue().getHrHearingDisplayValue()
							.getHrJusticeValues();
				

					if (arr.get(0) != null) {
						arr.get(0).setJusticeName(txtJusticeOfPeace1.getText());
					} else if (txtJusticeOfPeace1.getText().length() > 0) {
						SHJusticeBasicValue justice = new SHJusticeBasicValue();
						justice.setJusticeName(txtJusticeOfPeace1.getText());
						justice.setHearingID(uc.getHearingRecordModel().getHearingRecordVal()
								.getHearingRecordDisplayValue().getHrHearingDisplayValue().getHearingID());
						if(justice.getHearingID() == null) {
							justice.setHearingID(uc.getHearingRecordModel().getHearingId());
						}
						arr.add(0, justice);
					} else {
						arr.add(null);
					}

					if (arr.get(1) != null) {
						arr.get(1).setJusticeName(txtJusticeOfPeace2.getText());
					} else if (txtJusticeOfPeace2.getText().length() > 0) {
						SHJusticeBasicValue justice = new SHJusticeBasicValue();
						justice.setHearingID(uc.getHearingRecordModel().getHearingRecordVal()
								.getHearingRecordDisplayValue().getHrHearingDisplayValue().getHearingID());
						justice.setJusticeName(txtJusticeOfPeace2.getText());
						if(justice.getHearingID() == null) {
							justice.setHearingID(uc.getHearingRecordModel().getHearingId());
						}
						arr.add(1, justice);
					} else {
						arr.add(null);
					}

					if (arr.get(2) != null) {
						arr.get(2).setJusticeName(txtJusticeOfPeace3.getText());
					} else if (txtJusticeOfPeace3.getText().length() > 0) {
						SHJusticeBasicValue justice = new SHJusticeBasicValue();
						justice.setHearingID(uc.getHearingRecordModel().getHearingRecordVal()
								.getHearingRecordDisplayValue().getHrHearingDisplayValue().getHearingID());
						justice.setJusticeName(txtJusticeOfPeace3.getText());
						if(justice.getHearingID() == null) {
							justice.setHearingID(uc.getHearingRecordModel().getHearingId());
						}
						arr.add(2, justice);
					} else {
						arr.add(null);
					}

					if (arr.get(3) != null) {
						arr.get(3).setJusticeName(txtJusticeOfPeace4.getText());
					} else if (txtJusticeOfPeace4.getText().length() > 0) {
						SHJusticeBasicValue justice = new SHJusticeBasicValue();
						justice.setHearingID(uc.getHearingRecordModel().getHearingRecordVal()
								.getHearingRecordDisplayValue().getHrHearingDisplayValue().getHearingID());
						justice.setJusticeName(txtJusticeOfPeace4.getText());
						if(justice.getHearingID() == null) {
							justice.setHearingID(uc.getHearingRecordModel().getHearingId());
						}
						arr.add(3, justice);
					} else {
						arr.add(null);
					}
					arr.subList(4, arr.size()).clear();
					Collection<SHJusticeBasicValue> JPCol = arr;
					uc.getHearingRecordModel().getHearingRecordVal().getHearingRecordDisplayValue()
							.getHrHearingDisplayValue().setHrJusticeValues(JPCol);
					
					// Make sure to refresh data from db to maintain version integrity
					try {
						XhibitDelegateHelper.getSHJusticeDelegate().update(uc.getHearingRecordModel().getHearingRecordVal(),
								XhibitSingleton.getInstance().getUserSession()
										.getSessionProperty(UserTerminalProperties.DISPLAY_NAME), uc.getScheduledHearingId());
						
						SHJusticeControllerBeanBusinessDelegate shDel = XhibitDelegateHelper.getSHJusticeDelegate();
						ArrayList<SHJusticeBasicValue> results = (ArrayList<SHJusticeBasicValue>)shDel.findByHearingId(uc.getHearingRecordModel().getHearingId());
						
						uc.getHearingRecordModel().getHearingRecordVal().getHearingRecordDisplayValue()
							.getHrHearingDisplayValue().setHrJusticeValues(results);
					} catch (FinderException e) {
						XHIBITErrorHandler.handleError(e);
					} catch (SQLException e) {
						XHIBITErrorHandler.handleError(e);
					}
				}
			
			}
			if(uc.getHearingRecordModel()!=null) {
				uc.getHearingRecordModel().setRepresentationUpdated(true);
			}
		}
	}

	private JTextField getJudgeJTextField() {
		if (this.judgeText == null) {
			String judgeName = "n/a";
			try {
				Collection judgesCol = this.uc.hhvh.getStaffByType(PersonValue.JUDGE);

				// in iteration 2, xhibit cases/schedhearing 1 judge only
				Iterator judgeIt = judgesCol.iterator();
				if (judgeIt.hasNext()) {
					try {
						this.judge = (PersonValue) judgeIt.next();
						judgeName = "" + judge.getFullName();
					} catch (final Exception e) {
						XHIBITConstant.handleError(e, this.getClass());
					}
				}

				if (judge == null) {
					log.debug("No Judge set on HearingHeaderValue.");
				}
			} catch (final Exception e) {
				log.error("Exception occured when trying getting the judge from courtlogheadervalue");
				log.error(e);
				String msgKey = "gui.updategeneralcasedata.getJugdge";
				addException(new CSRecoverableException(msgKey, msgKey, e));
			}

			this.judgeText = new JTextField(judgeName);
			judgeText.setColumns(26);
			// PRE00156, the judge field should always be read-only
			judgeText.setEditable(false);
		}

		return this.judgeText;
	}

	private JLabel getHearingTypeText() {
		if (this.hearingTypeText == null) {
			String hearingTypeString;
			try {
				hearingTypeString = uc.hhv.getHearingType();
			} catch (final Exception e) {
				log.error("Exception occured whilst getting the hearing type from the CourtLogHeaderValue");
				log.error(e);
				String msgKey = "gui.updategeneralcasedata.getHearingType";
				addException(new CSRecoverableException(msgKey, msgKey, e));
				hearingTypeString = "not set";
			}

			this.hearingTypeText = new JLabel(hearingTypeString);
			this.hearingTypeText.setPreferredSize(new Dimension(375, XHIBITConstant.getLineHeight()));
		}

		return this.hearingTypeText;
	}

	private JLabel getAppealAgainst() {
		if (appealAgainstType == null) {
			appealAgainstType = new JLabel(
					getResource("criminal.appealAgainst." + uc.hhv.getHhCase().getCaseSubType()));
		}
		return appealAgainstType;
	}

	/**
	 * Get the severed indictment panel
	 * 
	 * @return the severed indictment panel
	 */
	private JPanel getIndictmentSeveredPanel() {
		if (indictmentSeveredPanel == null) {
			indictmentSeveredPanel = new JPanel();
			indictmentSeveredPanel.setLayout(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0);

			indictmentSeveredYesRb = new JRadioButton(getResource("lblIndicmentServerd.Yes"));
			indictmentSeveredNoRb = new JRadioButton(getResource("lblIndicmentServerd.No"));
			indictmentSeveredNARb = new JRadioButton(getResource("lblIndicmentServerd.NA"));

			indictmentSeveredYesRb.addActionListener(changeListener);
			indictmentSeveredNoRb.addActionListener(changeListener);
			indictmentSeveredNARb.addActionListener(changeListener);

			ButtonGroup indictmentServeredGroup = new ButtonGroup();
			indictmentServeredGroup.add(indictmentSeveredYesRb);
			indictmentServeredGroup.add(indictmentSeveredNoRb);
			indictmentServeredGroup.add(indictmentSeveredNARb);

			indictmentSeveredPanel.add(indictmentSeveredYesRb, gbc);
			gbc.gridx = 1;
			gbc.insets = new Insets(4, 0, 4, 4);
			indictmentSeveredPanel.add(indictmentSeveredNoRb, gbc);
			gbc.gridx = 2;
			indictmentSeveredPanel.add(indictmentSeveredNARb, gbc);

		}
		return indictmentSeveredPanel;
	}

	/**
	 * Populates the appropriate severed indictment radio button.
	 * 
	 * @param severedInd
	 *            the severed indictment indicator.
	 */
	private void populateSeveredIndictment(String severedInd) {
		if (severedInd == null) {
			indictmentSeveredNARb.setSelected(true);
		} else if (severedInd.equalsIgnoreCase(HearingRecordConstants.SEVERED_IND_YES)) {
			indictmentSeveredYesRb.setSelected(true);
		} else if (severedInd.equalsIgnoreCase(HearingRecordConstants.SEVERED_IND_NO)) {
			indictmentSeveredNoRb.setSelected(true);
		}
	}
	
	private void populateS28RadioButtons(String s28Eligible, String s28OrderMade) {	
		if (s28Eligible == null) {
			s28EligibleNARb.setSelected(true);
		} else if (s28Eligible.equals(S28_ELIGIBLE_YES)) {
			s28EligibleYesRb.setSelected(true);
		} else if (s28Eligible.equals(S28_ELIGIBLE_NO)) {
			s28EligibleNoRb.setSelected(true);
		}
		
		if (s28OrderMade == null) {
			s28OrderMadeNARb.setSelected(true);
		} else if (s28OrderMade.equals(S28_ORDER_MADE_YES)) {
			s28OrderMadeYesRb.setSelected(true);
		} else if (s28OrderMade.equals(S28_ORDER_MADE_NO)) {
			s28OrderMadeNoRb.setSelected(true);
		}
	}
	
	private String getS28OrderMadeScreenValue() {
		String orderMade = null;
		if (s28OrderMadeYesRb.isSelected()) {
			orderMade = S28_ORDER_MADE_YES;
		} else if (s28OrderMadeNoRb.isSelected()) {
			orderMade = S28_ORDER_MADE_NO;
		}
		return orderMade;
	}
	
	private String getS28EligibleScreenValue() {
		String eligible = null;
		if (s28EligibleYesRb.isSelected()) {
			eligible = S28_ELIGIBLE_YES;
		} else if (s28EligibleNoRb.isSelected()) {
			eligible = S28_ELIGIBLE_NO;
		}
		return eligible;
	}

	/**
	 * Gets the severed indictment indicator.
	 * 
	 * @return the severed indictment indicator.
	 */
	private String getSeveredIndictmentOption() {
		String selectedOption;
		if (indictmentSeveredYesRb.isSelected()) {
			selectedOption = HearingRecordConstants.SEVERED_IND_YES;
		} else if (indictmentSeveredNoRb.isSelected()) {
			selectedOption = HearingRecordConstants.SEVERED_IND_NO;
		} else {
			selectedOption = null;
		}
		return selectedOption;
	}
	
	private void enableS28Panel(boolean s28EligibleEnabled, boolean s28OrderMadeEnabled) {
		
		s28EligibleNARb.setEnabled(s28EligibleEnabled);		
		s28EligibleYesRb.setEnabled(s28EligibleEnabled);		
		s28EligibleNoRb.setEnabled(s28EligibleEnabled);

		s28OrderMadeNARb.setEnabled(s28OrderMadeEnabled);		
		s28OrderMadeYesRb.setEnabled(s28OrderMadeEnabled);		
		s28OrderMadeNoRb.setEnabled(s28OrderMadeEnabled);
		
		if(!s28EligibleYesRb.isSelected()){
			s28OrderMadeNARb.setSelected(true);
			s28OrderMadeYesRb.setSelected(false);
			s28OrderMadeNoRb.setSelected(false);
		}
	}
	
private JPanel getSection28Panel() {
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		GridBagConstraints gbcEligible = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		GridBagConstraints gbcOrderMade = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		// empty used for alignment
		JLabel empty = new JLabel("");
		
		// create section 28 panel
		final JPanel section28Panel = new JPanel();
		section28Panel.setLayout(new GridBagLayout());		
		
		//set up buttons and button groups
		s28EligibleYesRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28EligibleYes"));
		s28EligibleNoRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28EligibleNo"));
		s28EligibleNARb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28EligibleNA"));
		
		s28OrderMadeYesRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28OrderMadeYes"));
		s28OrderMadeNoRb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28OrderMadeNo"));
		s28OrderMadeNARb = new JRadioButton(XHIBITConstant.getResource(XhibitBundles.MaintainHearingHeader, "lblS28OrderMadeNA"));
		
		final Section28ActionListener actionListener = new Section28ActionListener();
		s28EligibleYesRb.addActionListener(actionListener);
		s28EligibleNoRb.addActionListener(actionListener);
		s28EligibleNARb.addActionListener(actionListener);
				
		s28OrderMadeYesRb.addActionListener(actionListener);
		s28OrderMadeNoRb.addActionListener(actionListener);
		s28OrderMadeNARb.addActionListener(actionListener);
		
				
		ButtonGroup s28EligibleBG = new ButtonGroup();
		s28EligibleBG.add(s28EligibleYesRb);
		s28EligibleBG.add(s28EligibleNoRb);
		s28EligibleBG.add(s28EligibleNARb);
		
		ButtonGroup s28OrderMadeBG = new ButtonGroup();
		s28OrderMadeBG.add(s28OrderMadeYesRb);
		s28OrderMadeBG.add(s28OrderMadeNoRb);
		s28OrderMadeBG.add(s28OrderMadeNARb);
		
		// add items to the panel
		section28Panel.add(createJLabel("section28Eligble"), gbc);		
		
		final JPanel section28EligiblePanel = new JPanel();
		section28EligiblePanel.add(s28EligibleYesRb, gbcEligible);
		gbcEligible.gridx++;
		section28EligiblePanel.add(s28EligibleNoRb, gbcEligible);
		gbcEligible.gridx++;
		section28EligiblePanel.add(s28EligibleNARb, gbcEligible);
		
		gbc.gridx++;
		gbc.gridx++;
		section28Panel.add(section28EligiblePanel, gbc);
		// add empty value for alignment
		gbc.gridx++;
		section28Panel.add(empty, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		section28Panel.add(createJLabel("section28OrderMade"), gbc);		
		final JPanel section28OrderMadePanel = new JPanel();
		section28OrderMadePanel.add(s28OrderMadeYesRb, gbcOrderMade);
		gbcOrderMade.gridx++;
		section28OrderMadePanel.add(s28OrderMadeNoRb, gbcOrderMade);
		gbcOrderMade.gridx++;
		section28OrderMadePanel.add(s28OrderMadeNARb, gbcOrderMade);
		
		gbc.gridx++;
		gbc.gridx++;
		section28Panel.add(section28OrderMadePanel, gbc);
		// add empty value for alignment
		gbc.gridx++;
		section28Panel.add(empty, gbc);	
		
		
		return section28Panel;
	}

	private class Section28ActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			setChanged();		
			updateSection28State();
		}
	}
	
	public void updateSection28State() {
		boolean orderMadeEnabled = false;
		if(this.s28EligibleYesRb.isSelected()){
			orderMadeEnabled = true;
		}
		enableS28Panel(true, orderMadeEnabled);		
	}

	/**
	 * enable or disable the severed indictment panel (radio butttons) depending
	 * on the the given param
	 * 
	 * @param enabled
	 *            the enabled state to set severed indictment panel (radio
	 *            butttons).
	 */
	private void enableSeveredIndictmentPanel(boolean enabled) {
		indictmentSeveredYesRb.setEnabled(enabled);
		indictmentSeveredNoRb.setEnabled(enabled);
		indictmentSeveredNARb.setEnabled(enabled);
	}

	/**
	 * Private helper method used to acquire the text of a text field as an
	 * <code>Integer</code>
	 * 
	 * @param textField
	 *            a <code>JTextField</code> which cannot be <i>null</i>!
	 * @return An <code>Integer</code> representation of the text of the passed
	 *         in text field, or null if the text field is empty
	 */
	private Integer getIntegerValueOfJTextField(JTextField textField) {
		try {
			return ((textField.getText().length() > 0) ? new Integer(textField.getText()) : null);
		} catch (final NumberFormatException e) {
			// this should never happen as we have input decorators!
			return null;
		}
	}

	private JLabel createJLabel(String labelText) {
		return new JLabel(ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader, labelText));
	}

	private String getResource(String labelText) {
		return ResourceBundleHelper.getResource(XhibitBundles.MaintainHearingHeader, labelText);
	}

	/**
	 * Private method used to remove duplicate code from several methods
	 * 
	 * @param value
	 *            The <code>Integer</code> value that the text field should
	 *            display as its text
	 * @param size
	 *            The maximum number of characters that the text field should
	 *            allow.
	 * @return The newly create <code>JTextField</code> with decorators and
	 *         listeners added
	 * @see UpdateGeneralCaseData#getNoOfProsWitTF(Integer)
	 * @see UpdateGeneralCaseData#getNoOfPagesTF(Integer)
	 * @see UpdateGeneralCaseData#getTapeLength(Integer)
	 */
	private JTextField createNumericJTextField(Integer value, int size) {
		// create the required capabilities (document decorators/validators)
		Capability numeric = Capability.numeric();
		Capability limitedText = Capability.limitedText(size);

		// Create the text document with the required capabilities.
		Document doc = DocumentFactory.newDocument(new Capability[] { numeric, limitedText });

		// create the text field to return
		final JTextField textField = JTextFieldFactory.getTextField(doc);
		textField.setText(clearNull(value));
		textField.setColumns(10);

		// Add the listener
		// textField.getDocument().addDocumentListener(this.changeListener);

		return textField;
	}

	/**
	 * Private helper method (will be in-lined by the compiler) to prevent
	 * <i>null</i> value being passed around as <code>String</code>'s
	 * 
	 * @param obj
	 * @return A guaranteed non-null <code>String</code>, if the passed in
	 *         <code>Object</code> is <i>null</i> then a new, 0-length
	 *         <code>String</code> will be returned
	 */
	private String clearNull(Object obj) {
		return ((obj == null) ? "" : obj.toString());
	}

	/**
	 * Method used to indicate that this tab, and therefore the main
	 * <code>UpdateCasePanel</code> have been updated
	 */
	protected void setChanged() {
		this.valuesUpdated = true;
		this.uc.setModified(true);
	}

	/**
	 * Private class used to implement all of the listeners required by the
	 * general panel. All required methods from the interfaces make a call to
	 * the <code>setChanged</code> method of the
	 * <code>UpdateGeneralCaseData</code> to indicate that data has changed. The
	 * only exceptions to this are <code>valueChanged</code> from the
	 * <code>ListSelectionListener</code> interface which simply calls the
	 * <code>stepUpdateViewState()</code> method from the containing class, and
	 * <code>actionPerformed()</code> which will behave as most of the other
	 * methods under normal conditions, or, if it was the remove justice button
	 * presses, it will call processRemoveJustice() instead.
	 */
	private class UpdateGeneralCaseDataListener
			implements DocumentListener, ListDataListener, ListSelectionListener, ActionListener {
		// required methods from the DocumentListener interface
		public void insertUpdate(DocumentEvent e) {
			setChanged();
		}

		public void removeUpdate(DocumentEvent e) {
			setChanged();
		}

		public void changedUpdate(DocumentEvent e) {
			setChanged();
		}

		// required methods from the ListDataListener interface
		public void contentsChanged(ListDataEvent e) {
			setChanged();
		}

		public void intervalAdded(ListDataEvent e) {
			setChanged();
		}

		public void intervalRemoved(ListDataEvent e) {
			setChanged();
		}

		// required methods from the ListSelectionListener interface
		public void valueChanged(ListSelectionEvent lse) {
			stepUpdateViewState();
		}

		// required methods from the ActionListener interface
		public void actionPerformed(ActionEvent e) {
			// if it was the remove justice button pressed, process
			// seperatly
			setChanged();
		}
	}

	private class XHearingProgressModel extends DefaultComboBoxModel {

		private static final long serialVersionUID = 1L;

		private final Hashtable<String, String> caseProgressIndicators = new Hashtable<String, String>();

		public XHearingProgressModel(Vector v) {
			super(v);
			initModel();
		}

		public void initModel() {
			ResourceBundle caseProgRsc = ResourceBundleHelper
					.getResourceBundle(XhibitBundles.XhibitClientDefaultResources);
			Enumeration caseProgEnum = caseProgRsc.getKeys();

			while (caseProgEnum.hasMoreElements()) {
				String caseProgKey = (String) caseProgEnum.nextElement();

				if (caseProgKey.startsWith(UpdateCaseDialog.HEARINGPROGRESS_RSC_PREFIX)) {
					caseProgressIndicators.put(caseProgKey, caseProgRsc.getString(caseProgKey));
				}
			}
		}

		public Integer getObjectForHearingProgressStatusLabel(String statusLabel) {
			Integer returnObject = null;
			final Enumeration enumeration = caseProgressIndicators.keys();
			while (enumeration.hasMoreElements()) {
				String progressLabelKey = (String) enumeration.nextElement();
				String progressLabel = caseProgressIndicators.get(progressLabelKey);
				if (progressLabel.equals(statusLabel)) {
					if (progressLabelKey.equals("hp_adjourned"))
						returnObject = HearingProgressValue.ADJOURNED;
					else if (progressLabelKey.equals("hp_finished"))
						returnObject = HearingProgressValue.FINISHED;
					else if (progressLabelKey.equals("hp_inProgress"))
						returnObject = HearingProgressValue.IN_PROGRESS;
					else if (progressLabelKey.equals("hp_toBeHeard"))
						returnObject = HearingProgressValue.TO_BE_HEARD;

					break;
				}
			}

			return returnObject;
		}
	}
}