package uk.gov.courtservice.xhibit.client.actions.admin.referencedata.search;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import uk.gov.courtservice.framework.business.vos.CSValueObject;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseDiaryFixtureComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SittingOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.search.XHIBITSearchDetails;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Ref SolicitorFirm - Update screen.
 * 
 * @author grewalg
 *
 */
public class JudgeSearchUpdatePanel extends RefSearchUpdatePanel {

	private static final long serialVersionUID = -5219710902763805593L;

	private static final Insets ERROR_INSETS = new Insets(0, 4, 0, 0);
	private static final double WEIGHT_LEFT = 0.32;
	private static final double WEIGHT_RIGHT = 0.32;
	private static final String DEFAULT_JUDGE_TYPE = " ";
	private static final String ASSISTANT_RECORDER = "AR";

	private static final Integer COURT_ID = XhibitSingleton.getInstance().getCourtId();
	private static final BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
	private static final String DISPLAY_NAME = XhibitSingleton.getInstance().getUserSession()
			.getSessionProperty(UserTerminalProperties.DISPLAY_NAME);

	private JPanel mainPanel, ticketTypePanel, buttonPanel;

	private JButton deleteButton;

	private JLabel surnameLbl, surnameErrLbl, initialsLbl, initialsErrLbl, prefixLbl, prefixErrLbl, firstNameLbl,
			firstNameErrLbl, otherNamesLbl, otherNamesErrLbl, suffixLbl, suffixErrLbl, judgeTypeLbl, judgeTypeErrLbl,
			statsCodeLbl, statsCodeErrLbl, title1Lbl, title1ErrLbl, title2Lbl, title2ErrLbl, title3Lbl, title3ErrLbl,
			ticketTypeLbl, ticketTypeErrLbl;

	private XTextField surnameTxt, initialsTxt, prefixTxt, firstNameTxt, otherNamesTxt, suffixTxt, statsCodeTxt,
			title1Txt, title2Txt, title3Txt;

	private JComboBox judgeTypeCombo;
	
	private JCheckBox chkBxMurderTicketType = null;
	private JCheckBox chkBxAttemptedMurderTicketType = null;
	private JCheckBox chkBxRapeTicketType = null;
	private JCheckBox chkBxFraudTicketType = null;

	private RefSystemCodeBasicValue[] ticketTypes;

	private RefSystemCodeBasicValue[] judgeTypes;

	private RefJudgeComplexValue refJudge;

	/**
	 * Flag to indicate when values have finished loaded into controls.
	 */
	private boolean loadedValues;

	/*
	 * Constructor/s.
	 */
	public JudgeSearchUpdatePanel(XHIBITSearchDetails xsDetails, JudgeSearch xsSearch) {
		super(xsDetails, xsSearch);
	}

	/*
	 * Getters and Setters
	 */

	private JComboBox getJudgeTypeCombo() {
		if (judgeTypeCombo == null) {
			judgeTypeCombo = new JComboBox(getJudgeTypeCodes(null));
			judgeTypeCombo.setRenderer(new JudgeRenderer());
			judgeTypeCombo.setPreferredSize(new Dimension(170, 20));

			judgeTypeCombo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					validateJudgeType(true);
					if (getJudgeType() != null && !getJudgeType().equals(refJudge.getJudgeType()) && loadedValues) {
						setModified(true);
					}
					enableSaveButton();
				}
			});

			judgeTypeCombo.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					validateJudgeType(true);
				}
			});
			componentList.add(judgeTypeCombo);
		}
		return judgeTypeCombo;
	}

	private String getSurname() {
		return surnameTxt.getText();
	}

	private String getInitials() {
		return initialsTxt.getText();
	}

	private String getPrefix() {
		return prefixTxt.getText();
	}

	private String getFirstName() {
		return firstNameTxt.getText();
	}

	private String getOtherNames() {
		return otherNamesTxt.getText();
	}

	private String getSuffix() {
		return suffixTxt.getText();
	}

	private String getJudgeType() {
		return ((RefSystemCodeBasicValue) judgeTypeCombo.getSelectedItem()).getCode();
	}

	private String getStatsCode() {
		return statsCodeTxt.getText();
	}

	private String getTitle1() {
		return title1Txt.getText();
	}

	private String getTitle2() {
		return title2Txt.getText();
	}

	private String getTitle3() {
		return title3Txt.getText();
	}

	/*
	 * Overridden methods.
	 */
			
	@Override
	protected void save() {
		try {
			if(title1Txt.getText().length()+
					title2Txt.getText().length()+
					title3Txt.getText().length() > 70) {
				JOptionPane.showMessageDialog(this,
						XHIBITConstant.getResource(resources, "judge.ref.maxtitle.message"),
						XHIBITConstant.getResource(resources, "judge.ref.maxtitle.warning"),
						JOptionPane.WARNING_MESSAGE);
			}
			if (saveRefJudge()) {
				setModified(false);

				JOptionPane.showMessageDialog(null,
						XHIBITConstant.getResource(resources, "judge.ref.update.success.message"),
						XHIBITConstant.getResource(resources, "judge.ref.update.success.heading"),
						JOptionPane.INFORMATION_MESSAGE);
				// Set Judge
				BisRefControllerBeanBusinessDelegate bizRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
				try {
					if (getRefSearchController().getTheResults() != null
							&& getRefSearchController().getTheResults().size() > 1 && refJudge.getId() != null) {
						// If Update was performed after selecting from Results
						// Page, then need to update Results collection
						refJudge = bizRefDelegate.findJudgeById(refJudge.getId());
						Collection<RefJudgeBasicValue> results = updateResultsAfterUpdate();
						getRefSearchController().setTheResults(results);
						getRefSearchController().showXSResultsPanel(results);
					}
					getParentContainer().dispose();
				} catch (Exception ex) {
					XHIBITConstant.handleError(ex);
				}				
			}
		} catch (Exception er) {
			log.error("Error occurred while saving Judge: " + er);
			JOptionPane.showMessageDialog((Component) null,
					XHIBITConstant.getResource(resources, "xs.gen.update.failure.message"),
					XHIBITConstant.getResource(resources, "xs.gen.update.failure.heading"), JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	protected void delete() throws CSRecoverableException {
		int confirmed = deleteJudgeWarningMessage();
		if (confirmed == 0) {
			if (deleteJudge()) {
				JOptionPane.showMessageDialog(null,
						XHIBITConstant.getResource(resources, "judge.ref.details.delete.success"),
						XHIBITConstant.getResource(resources, "judge.ref.details.delete.title"),
						JOptionPane.INFORMATION_MESSAGE);

				Collection<RefJudgeBasicValue> results = updateResultsAfterDelete();
				getRefSearchController().setTheResults(results);
				if (results.size() > 1) {
					getRefSearchController().showXSResultsPanel(results);
					getParentContainer().dispose();
				} else {
					getRefSearchController().showXSResultsPanelAgain();
				}
			}
		}
	}

	@Override
	public void jbInit(XHIBITSearchDetails ixsDetails) {
		this.xsDetails = ixsDetails;
	}

	@Override
	protected void createPanelControls(XDialog parent) {
		setParentContainer(parent);
		componentList = new ArrayList<Component>();
		textFields = new ArrayList<XTextField>();

		this.setLayout(new GridBagLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();

		gbc.gridwidth = 1;
		gbc.gridheight = 1;

		// Surname
		addComponent(getPlaceholderPanel(), 0, 0, 0, ERROR_INSETS, GridBagConstraints.WEST);

		surnameLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.surName"));
		addComponent(surnameLbl, 0, 1, 0, getNewInsets());

		surnameErrLbl = getErrorLbl();
		addComponent(surnameErrLbl, 1, 0, 0, ERROR_INSETS);

		surnameTxt = new XTextField(35, "^.{1,35}$", surnameErrLbl, true);
		surnameTxt.setMaxLength(35);
		addComponent(surnameTxt, 1, 1, WEIGHT_LEFT, getNewInsets());
		mandatoryFields.add(surnameTxt);

		// Initials
		addComponent(getPlaceholderPanel(), 0, 2, 0, ERROR_INSETS, GridBagConstraints.WEST);

		initialsLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.initials"));
		addComponent(initialsLbl, 0, 3, 0, getNewInsets());

		initialsErrLbl = getErrorLbl();
		addComponent(initialsErrLbl, 1, 2, 0, ERROR_INSETS);

		initialsTxt = new XTextField(4, "^.{1,4}$", initialsErrLbl, false);
		initialsTxt.setMaxLength(4);
		addComponent(initialsTxt, 1, 3, WEIGHT_LEFT, getNewInsets());

		// Judge Name Prefix
		addComponent(getPlaceholderPanel(), 0, 4, 0, ERROR_INSETS, GridBagConstraints.WEST);

		prefixLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.judgeNamePrefix"));
		addComponent(prefixLbl, 0, 5, 0, getNewInsets());

		prefixErrLbl = getErrorLbl();
		addComponent(prefixErrLbl, 1, 4, 0, ERROR_INSETS);

		prefixTxt = new XTextField(25, "^.{1,25}$", prefixErrLbl, false);
		prefixTxt.setMaxLength(25);
		addComponent(prefixTxt, 1, 5, WEIGHT_LEFT, getNewInsets());

		// First Name
		addComponent(getPlaceholderPanel(), 0, 6, 0, ERROR_INSETS, GridBagConstraints.WEST);

		firstNameLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.firstName"));
		addComponent(firstNameLbl, 0, 7, 0, getNewInsets());

		firstNameErrLbl = getErrorLbl();
		addComponent(firstNameErrLbl, 1, 6, 0, ERROR_INSETS);

		firstNameTxt = new XTextField(35, "^.{1,35}$", firstNameErrLbl, false);
		firstNameTxt.setMaxLength(35);
		addComponent(firstNameTxt, 1, 7, WEIGHT_LEFT, getNewInsets());

		// Other Names
		addComponent(getPlaceholderPanel(), 0, 8, 0, ERROR_INSETS, GridBagConstraints.WEST);

		otherNamesLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.otherNames"));
		addComponent(otherNamesLbl, 0, 9, 0, getNewInsets());

		otherNamesErrLbl = getErrorLbl();
		addComponent(otherNamesErrLbl, 1, 8, 0, ERROR_INSETS);

		otherNamesTxt = new XTextField(35, "^.{1,35}$", otherNamesErrLbl, false);
		otherNamesTxt.setMaxLength(35);
		addComponent(otherNamesTxt, 1, 9, WEIGHT_LEFT, getNewInsets());

		// Judge Name Suffix
		addComponent(getPlaceholderPanel(), 0, 10, 0, ERROR_INSETS, GridBagConstraints.WEST);

		suffixLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.judgeNameSuffix"));
		addComponent(suffixLbl, 0, 11, 0, getNewInsets());

		suffixErrLbl = getErrorLbl();
		addComponent(suffixErrLbl, 1, 10, 0, ERROR_INSETS);

		suffixTxt = new XTextField(35, "^.{1,35}$", suffixErrLbl, false);
		suffixTxt.setMaxLength(35);
		addComponent(suffixTxt, 1, 11, WEIGHT_LEFT, getNewInsets());

		// Judge Type
		addComponent(getPlaceholderPanel(), 0, 12, 0, ERROR_INSETS, GridBagConstraints.WEST);

		judgeTypeLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.typeOfJudge"));
		addComponent(judgeTypeLbl, 0, 13, 0, getNewInsets());

		judgeTypeErrLbl = getErrorLbl("Mandatory Field");

		addComponent(judgeTypeErrLbl, 1, 12, 0, ERROR_INSETS);

		addComponent(getJudgeTypeCombo(), 1, 13, WEIGHT_LEFT, getNewInsets());
		mandatoryFields.add(getJudgeTypeCombo());

		// Statistical Code
		addComponent(getPlaceholderPanel(), 0, 14, 0, ERROR_INSETS, GridBagConstraints.WEST);

		statsCodeLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.statsCode"));
		addComponent(statsCodeLbl, 0, 15, 0, getNewInsets());

		statsCodeErrLbl = getErrorLbl();
		addComponent(statsCodeErrLbl, 1, 14, 0, ERROR_INSETS);

		statsCodeTxt = new XTextField(5, "^[0-9]{1,6}$", statsCodeErrLbl, false);
		statsCodeTxt.setMaxLength(5);
		addComponent(statsCodeTxt, 1, 15, WEIGHT_LEFT, getNewInsets());

		// Full List Title 1

		title1Lbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.fullListTitle1"));
		addComponent(title1Lbl, 2, 1, 0, getNewInsets());

		title1ErrLbl = getErrorLbl();
		addComponent(title1ErrLbl, 3, 0, 0, ERROR_INSETS);
		
		title1Txt = new XTextField(80, "^.{1,80}$", title1ErrLbl, true);
		title1Txt.setMaxLength(80);
		addComponent(title1Txt, 3, 1, WEIGHT_RIGHT, getNewInsets());
		mandatoryFields.add(title1Txt);

		// Full List Title 2
		addComponent(getPlaceholderPanel(), 2, 2, 0, ERROR_INSETS, GridBagConstraints.WEST);

		title2Lbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.fullListTitle2"));
		addComponent(title2Lbl, 2, 3, 0, getNewInsets());

		title2ErrLbl = getErrorLbl();
		addComponent(title2ErrLbl, 3, 2, 0, ERROR_INSETS);

		title2Txt = new XTextField(80, "^.{1,80}$", title2ErrLbl, false);
		title2Txt.setMaxLength(80);
		addComponent(title2Txt, 3, 3, WEIGHT_RIGHT, getNewInsets());

		// Full List Title 3
		addComponent(getPlaceholderPanel(), 2, 4, 0, ERROR_INSETS, GridBagConstraints.WEST);

		title3Lbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.fullListTitle3"));
		addComponent(title3Lbl, 2, 5, 0, getNewInsets());

		title3ErrLbl = getErrorLbl();
		addComponent(title3ErrLbl, 3, 4, 0, ERROR_INSETS);

		title3Txt = new XTextField(80, "^.{1,80}$", title3ErrLbl, false);
		title3Txt.setMaxLength(80);
		addComponent(title3Txt, 3, 5, WEIGHT_RIGHT, getNewInsets());

		// Ticket Type
		ticketTypeErrLbl = getErrorLbl(XHIBITConstant.getResource(resources, "judge.ref.details.ticket.error.label"), new Dimension(195, 14));
		JPanel errorHolderPanel = getErrorHolderPanel(ticketTypeErrLbl);
		addComponent(errorHolderPanel, 2, 6, 1, ERROR_INSETS, GridBagConstraints.WEST, 2, 1);

		ticketTypeLbl = new JLabel(XHIBITConstant.getResource(resources, "judge.ref.details.ticketType"));
		addComponent(ticketTypeLbl, 2, 7, 0, getNewInsets());
		
		addComponent(getTicketTypePanel(), 3, 7, WEIGHT_RIGHT, getNewInsets(), GridBagConstraints.NORTHWEST, 1, 7);

		// Add Main Panel
		gbc = new GridBagConstraints(0, 0, 4, 16, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH,
				new Insets(4, 4, 4, 4), 0, 0);
		doAdd(mainPanel, gbc);

		// Button Panel to contain the Delete and Save buttons
		buttonPanel = new JPanel();
		this.deleteButton = new JButton();
		deleteButton.setAction(new DeleteAction(this));
		deleteButton.setMnemonic(((XAction) deleteButton.getAction()).getMnemonicKey().intValue());
		enableDeleteButton(false);

		buttonPanel.add(deleteButton);
		componentList.add(deleteButton);

		// Save button
		this.saveButton = new JButton();
		saveButton.setEnabled(false);
		saveButton.setAction(new SaveAction(this));
		buttonPanel.add(saveButton);
		componentList.add(saveButton);

		gbc = new GridBagConstraints(2, 16, 1, 1, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(4, 0, 4, 8), 0, 0);
		doAdd(buttonPanel, gbc);

		// Spacer
		JSeparator separator = new JSeparator();
		gbc = new GridBagConstraints(0, 17, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
		doAdd(separator, gbc);

		cancelButton = new JButton();
		cancelButton.setAction(new CancelAction(this));

		gbc = new GridBagConstraints(2, 18, 1, 1, 0.5, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(4, 0, 4, 12), 0, 0);
		doAdd(cancelButton, gbc);
		componentList.add(cancelButton);
	}
	
	@Override
	protected void createUpdatePanel(XDialog parent, boolean isUpdate) {
		super.createUpdatePanel(parent, isUpdate);
		deleteButton.setEnabled(false);
	}
	
	/**
	 * Returns a panel which contains the ticket type panel
	 * @return
	 */
	public JPanel getTicketTypePanel() {
		GridBagConstraints gbc = getGridBagLayout();
		if (ticketTypePanel == null) {
			ticketTypePanel = new JPanel();
			ticketTypePanel.setLayout(new GridBagLayout());
			gbc.anchor = GridBagConstraints.WEST;

			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0.1;
			gbc.weighty = 0.2;

			/* Top of Column */
			gbc.gridy = 1;
			gbc.gridx++;

			ticketTypePanel.add(getMurderTicketType(), gbc);
			componentList.add(getMurderTicketType());
			gbc.gridy += 2;
			
			ticketTypePanel.add(getAttemptedMurderTicketType(), gbc);
			componentList.add(getAttemptedMurderTicketType());
			gbc.gridy += 2;
			
			ticketTypePanel.add(getRapeTicketType(), gbc);
			componentList.add(getRapeTicketType());
			gbc.gridy += 2;
			
			ticketTypePanel.add(getFraudTicketType(), gbc);
			componentList.add(getFraudTicketType());

		}

		return ticketTypePanel;
	}
	
	public JCheckBox getMurderTicketType() {
		if (chkBxMurderTicketType == null) {
			chkBxMurderTicketType = new JCheckBox(
					XHIBITConstant.getResource(resources, "judge.ref.details.ticket.murder"));
			chkBxMurderTicketType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setModified(true);
					enableSaveButton();
				}

			});
		}
		return chkBxMurderTicketType;
	}

	public JCheckBox getAttemptedMurderTicketType() {
		if (chkBxAttemptedMurderTicketType == null) {
			chkBxAttemptedMurderTicketType = new JCheckBox(
					XHIBITConstant.getResource(resources, "judge.ref.details.ticket.attemptedMurder"));
			chkBxAttemptedMurderTicketType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setModified(true);
					enableSaveButton();
				}

			});
		}
		return chkBxAttemptedMurderTicketType;
	}

	public JCheckBox getRapeTicketType() {
		if (chkBxRapeTicketType == null) {
			chkBxRapeTicketType = new JCheckBox(XHIBITConstant.getResource(resources, "judge.ref.details.ticket.rape"));
			chkBxRapeTicketType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setModified(true);
					enableSaveButton();
				}

			});
		}
		return chkBxRapeTicketType;
	}

	public JCheckBox getFraudTicketType() {
		if (chkBxFraudTicketType == null) {
			chkBxFraudTicketType = new JCheckBox(
					XHIBITConstant.getResource(resources, "judge.ref.details.ticket.fraud"));
			chkBxFraudTicketType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setModified(true);
					enableSaveButton();
				}

			});
		}
		return chkBxFraudTicketType;
	}
	
	private Boolean getChkBxMurderTicketType() {
		return chkBxMurderTicketType.isSelected();
	}
	
	private void setChkBxMurderTicketType(Boolean checked) {
		this.chkBxMurderTicketType.setSelected(checked);
	}
	
	private Boolean getChkBxAttemptedMurderTicketType() {
		return chkBxAttemptedMurderTicketType.isSelected();
	}
	
	private void setChkBxAttemptedMurderTicketType(Boolean checked) {
		this.chkBxAttemptedMurderTicketType.setSelected(checked);
	}
	
	private Boolean getChkBxRapeTicketType() {
		return chkBxRapeTicketType.isSelected();
	}
	
	private void setChkBxRapeTicketType(Boolean checked) {
		this.chkBxRapeTicketType.setSelected(checked);
	}
	
	private Boolean getChkBxFraudTicketType() {
		return chkBxFraudTicketType.isSelected();
	}
	
	private void setChkBxFraudTicketType(Boolean checked) {
		this.chkBxFraudTicketType.setSelected(checked);
	}
	
	/**
	 * Sets the selection on the Judge Ticket List box.
	 * 
	 * @param judgeTickets
	 */
	private void setTicketTypeSelection(Collection<RefJudgeTicketBasicValue> judgeTickets) {
		//Clear all checkbox selections first
		setChkBxMurderTicketType(false);
		setChkBxAttemptedMurderTicketType(false);
		setChkBxRapeTicketType(false);
		setChkBxFraudTicketType(false);
		
		if (judgeTickets != null && judgeTickets.size() > 0) {
			for (RefJudgeTicketBasicValue judgeTicket : judgeTickets) {
				if(judgeTicket.getTicketType().equals("MUR")) {
					setChkBxMurderTicketType(true);
				}
				if(judgeTicket.getTicketType().equals("ATT")) {
					setChkBxAttemptedMurderTicketType(true);
				}
				if(judgeTicket.getTicketType().equals("RAP")) {
					setChkBxRapeTicketType(true);
				}
				if(judgeTicket.getTicketType().equals("FRA")) {
					setChkBxFraudTicketType(true);
				}
			}			
		}
	}

	/**
	 * Get custom insets depending on controls position.
	 * 
	 * @return Insets
	 */
	private Insets getNewInsets() {
		int top = 2, left = 4, bottom = 4, right = 4;

		if (gbc.gridx == 0) {
			left = 10;
		}
		if (gbc.gridx == 2) {
			left = 5;
		}
		return new Insets(top, left, bottom, right);
	}

	/**
	 * Adds a component to the main panel.
	 * 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param insets
	 */
	private void addComponent(Component comp, int gridx, int gridy, double weightx, Insets insets) {
		int defaultAnchor = GridBagConstraints.LINE_START;
		int defaultGridWidth = 1;
		int defaultGridHeight = 1;
		addComponent(comp, gridx, gridy, weightx, insets, defaultAnchor, defaultGridWidth, defaultGridHeight);
	}

	/**
	 * Adds a component to the main panel.
	 * 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param insets
	 * @param anchor
	 */
	private void addComponent(Component comp, int gridx, int gridy, double weightx, Insets insets, int anchor) {
		int defaultGridWidth = 1;
		int defaultGridHeight = 1;
		addComponent(comp, gridx, gridy, weightx, insets, anchor, defaultGridWidth, defaultGridHeight);
	}

	/**
	 * Adds a component to the main panel. Overloaded with custom anchor
	 * property specified.
	 * 
	 * @param comp
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param insets
	 * @param anchor
	 * @param gridwidth
	 * @param gridheight
	 */
	private void addComponent(Component comp, int gridx, int gridy, double weightx, Insets insets, int anchor,
			int gridwidth, int gridheight) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.insets = insets;
		gbc.anchor = anchor;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;

		if (comp instanceof XTextField) {
			((XTextField) comp).setColumns(15);
			((XTextField) comp).setPreferredSize(new Dimension(30, 20));
			((XTextField) comp).setUpperCase(true);
			comp.addFocusListener(new InvalidMandatoryDataFocusListener());
			comp.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					setModified(true);
				}
			});
			componentList.add(comp);
			textFields.add((XTextField) comp);
		}
		mainPanel.add(comp, gbc);
	}

	@Override
	public void setValueObject(CSValueObject valueObject) {
		this.loadedValues = false;
		this.refJudge = (RefJudgeComplexValue) valueObject;

		if (valueObject != null && isUpdate()) {
			surnameTxt.setText(this.refJudge.getSurname());
			initialsTxt.setText(this.refJudge.getInitials());
			prefixTxt.setText(this.refJudge.getTitle());
			firstNameTxt.setText(this.refJudge.getFirstName());
			otherNamesTxt.setText(this.refJudge.getMiddleName());
			suffixTxt.setText(this.refJudge.getHonours());
			setJudgeTypeSelection(this.refJudge.getJudgeType());
			statsCodeTxt.setText(this.refJudge.getStatsCode());
			title1Txt.setText(this.refJudge.getFullListTitle1());
			title2Txt.setText(this.refJudge.getFullListTitle2());
			title3Txt.setText(this.refJudge.getFullListTitle3());
			setTicketTypeSelection(this.refJudge.getRefJudgeTickets());

			enableDeleteButton(true);
		} else {
			surnameTxt.setText(null);
			initialsTxt.setText(null);
			prefixTxt.setText(null);
			firstNameTxt.setText(null);
			otherNamesTxt.setText(null);
			suffixTxt.setText(null);
			setJudgeTypeSelection(DEFAULT_JUDGE_TYPE);
			statsCodeTxt.setText(null);
			title1Txt.setText(null);
			title2Txt.setText(null);
			title3Txt.setText(null);
			setChkBxMurderTicketType(false);
			setChkBxAttemptedMurderTicketType(false);
			setChkBxRapeTicketType(false);
			setChkBxFraudTicketType(false);
		}
		this.loadedValues = true;
	}

	/**
	 * Getter for ValueObject.
	 * 
	 * @return
	 */
	public CSValueObject getValueObject() {
		return refJudge;
	}

	/**
	 * Sets the selections on the Judge Type combo box.
	 * 
	 * @param value
	 */
	private void setJudgeTypeSelection(String value) {
		// Get the array of excluded codes
		String[] excludedCodes = getExcludedJudgeTypeCodes(value);
		// Get the valid list of judge types
		RefSystemCodeBasicValue[] judgeTypesArray = getJudgeTypeCodes(excludedCodes);
		// Reset the combo to include all the correct values 
		setJudgeTypeComboValues(judgeTypesArray);
		
		int index = 0;
		if (value != null && !value.equals("")) {
			for (RefSystemCodeBasicValue code : judgeTypesArray) {
				if (value.equals(code.getCode())) {
					getJudgeTypeCombo().setSelectedIndex(index);
					break;
				}
				index++;
			}
		}
		setModified(false);
	}

	private String[] getExcludedJudgeTypeCodes(String currentCode) {
		if (currentCode == null || !ASSISTANT_RECORDER.equals(currentCode)) {
			return new String[] {ASSISTANT_RECORDER};
		}
		return null;
	}

	/**
	 * Update ValueObject from controls.
	 * 
	 */
	private void updateValueObject() {
		refJudge.setSurname(getSurname());
		refJudge.setInitials(getInitials());
		refJudge.setTitle(getPrefix());
		refJudge.setFirstName(getFirstName());
		refJudge.setMiddleName(getOtherNames());
		refJudge.setHonours(getSuffix());
		refJudge.setJudgeType(getJudgeType());
		refJudge.setStatsCode(getStatsCode());
		refJudge.setFullListTitle1(getTitle1());
		refJudge.setFullListTitle2(getTitle2());
		refJudge.setFullListTitle3(getTitle3());
		refJudge.setLastUpdatedBy(
				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
	}

	/**
	 * 
	 * Get a list of Judge Types.
	 * 
	 * @return the JudgeTypes
	 */
	private RefSystemCodeBasicValue[] getJudgeTypeCodes(String[] excludedCodes) {
		RefSystemCodeBasicValue[] results;
		if (judgeTypes == null) {
			try {
				// Load all the available judge types
				RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
				criteria.setCourtId(COURT_ID.toString());
				criteria.setCodeType(RefSystemCodeCriteria.CodeType.JUDGE_TYPE);

				@SuppressWarnings("unchecked")
				List<RefSystemCodeBasicValue> judgeTypesList = (List<RefSystemCodeBasicValue>) bizRefDelegate
						.findSystemCodes(criteria);

				RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
				defValue.setCode(DEFAULT_JUDGE_TYPE);
				judgeTypesList.add(0, defValue);

				judgeTypes = judgeTypesList.toArray(new RefSystemCodeBasicValue[judgeTypesList.size()]);
			} catch (Exception ex) {
				log.error("Error occurred while searching for Judge Types: " + ex);
				XHIBITConstant.handleError(ex);
			}
		}
		// Exclude certain codes (if any have been passed)
		if (excludedCodes != null && excludedCodes.length > 0) {
			List<RefSystemCodeBasicValue> judgeTypesList = new ArrayList<RefSystemCodeBasicValue>();
			List<String> excludedCodesList = Arrays.asList(excludedCodes);
			for (RefSystemCodeBasicValue judge : judgeTypes) {
				if (!excludedCodesList.contains(judge.getCode())) {
					judgeTypesList.add(judge);
				}
			}
			results = judgeTypesList.toArray(new RefSystemCodeBasicValue[judgeTypesList.size()]);
		} else {
			results = judgeTypes;
		}
		return results;
	}

	private void setJudgeTypeComboValues(RefSystemCodeBasicValue[] judgeTypesArray) {
		judgeTypeCombo.setModel(new DefaultComboBoxModel(judgeTypesArray));
	}

	/**
	 * @return the ticketTypes
	 */
	private RefSystemCodeBasicValue[] getTicketTypeCodes() {
		if (ticketTypes == null) {
			try {
				RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
				criteria.setCourtId(COURT_ID.toString());
				criteria.setCodeType(RefSystemCodeCriteria.CodeType.TICKET_TYPE);

				@SuppressWarnings("unchecked")
				List<RefSystemCodeBasicValue> ticketTypesList = (List<RefSystemCodeBasicValue>) bizRefDelegate
						.findSystemCodes(criteria);
				ticketTypes = ticketTypesList.toArray(new RefSystemCodeBasicValue[ticketTypesList.size()]);
			} catch (Exception ex) {
				log.error("Error occurred while searching for Ticket Types: " + ex);
				XHIBITConstant.handleError(ex);
			}
		}
		return ticketTypes;
	}

	/**
	 * Validates the mandatory Judge Type comboBox selection.
	 * 
	 * @return
	 */
	private boolean validateJudgeType(boolean setErrorLbl) {
		boolean isValid = true;
		String judgeType = getJudgeType();
		if (judgeType.equals(" ")) {
			isValid = false;
		}
		if (setErrorLbl) {
			if (loadedValues) {
				if (!isValid) {
					judgeTypeErrLbl.setVisible(true);
				} else {
					judgeTypeErrLbl.setVisible(false);
				}
			}
		}
		return isValid;
	}

	/**
	 * Enables the Save button if changes are detected and all validations pass.
	 */
	private void enableSaveButton() {
		saveButton.setEnabled(validateJudgeType(false) && validateFields()
				&& RefSearchUpdatePanelUtil.hasUnsavedData(componentList, isUpdate(), getModified()));
	}

	/**
	 * Saves the RefJudge and associated Judge Tickets.
	 */
	private boolean saveRefJudge() throws CSRecoverableException {
		boolean hasSaved = false;
		// Add new Judge
		if (checkStatsCode()) {
			if (!isUpdate()) {
				if (insertJudge()) {
					hasSaved = true;
				}
			} else {
				// Existing Judge record
				updateValueObject();
				try {
					bizRefDelegate.updateRefJudge(refJudge.getId(), refJudge, getTicketsToAdd(), getTicketsToRemove(),
							DISPLAY_NAME);
					hasSaved = true;
				} catch (Exception ex) {
					XHIBITConstant.handleError(ex);
				}
			}
		}
		return hasSaved;
	}

	/**
	 * Checks if a record exists in the database with the same Stats Code for
	 * another Judge Type.
	 */
	private boolean checkStatsCode() {
		boolean isValid = true;
		try {
			if (getStatsCode() != null && !getStatsCode().equals("")) {
				if (StatsCodeValidator.isStatsCodeExist(getStatsCode(), refJudge.getId())) {
					JOptionPane.showMessageDialog((Component) null,
							XHIBITConstant.getResource(resources, "judge.ref.update.statsCode.exists.message"),
							XHIBITConstant.getResource(resources, "judge.ref.update.statsCode.exists.heading"),
							JOptionPane.WARNING_MESSAGE);
					isValid = false;
				}
				if (isValid) {
					if (!StatsCodeValidator.validateStatsCode(getJudgeType(), getStatsCode())) {
						JOptionPane.showMessageDialog((Component) null,
								XHIBITConstant.getResource(resources, "judge.ref.update.statsCode.invalid.message"),
								XHIBITConstant.getResource(resources, "judge.ref.update.statsCode.invalid.heading"),
								JOptionPane.WARNING_MESSAGE);
						isValid = false;
					}
				}
			}
		} catch (BisRefControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
		return isValid;
	}

	/**
	 * Deletes a RefJudge and associated Judge Tickets by marking them as
	 * obsolete. A Judge can only be deleted if it doesn't exist in a Case
	 * Listings Entry.
	 */
	private boolean deleteJudge() throws SysRefControllerException {
		Boolean hasDeleted = false;

		if (hasCaseListingEntry() || hasSittingOnList() || hasScheduleToday()) {
			JOptionPane.showMessageDialog((Component) null,
					XHIBITConstant.getResource(resources, "judge.ref.details.delete.failure"),
					XHIBITConstant.getResource(resources, "judge.ref.details.delete.title"),
					JOptionPane.WARNING_MESSAGE);
		} else {
			try {
				bizRefDelegate.deleteRefJudge(refJudge,
						DISPLAY_NAME);
				hasDeleted = true;
			} catch (Exception ex) {
				XHIBITConstant.handleError(ex);
			}
		}
		return hasDeleted;
	}

	/**
	 * Check if Judge has any CaseListing entries for current or future dates.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean hasCaseListingEntry() throws SysRefControllerException {
		boolean exists = false;
		// 1st check for Case Listing Entries matching Judge id
		try {
			Collection<CaseListingEntryBasicValue> col = bizRefDelegate
					.findCaseListingEntriesByJudgeId(refJudge.getId());
			if (col != null && col.size() > 0) {
				for (CaseListingEntryBasicValue listingBV : col) {
					// 2nd check for any fixtures for case listing entry
					Collection<CaseDiaryFixtureComplexValue> results = bizRefDelegate
							.findCaseDiaryFixtureByListingId(listingBV.getCaseListingEntryId());
					Date currentDate = new Date();
					for (CaseDiaryFixtureComplexValue cv : results) {
						if (currentDate.before(cv.getListingDate())) {
							exists = true;
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			XHIBITConstant.handleError(ex);
		}

		return exists;
	}

	/**
	 * Check if Judge has any sittings for current or future dates.
	 * 
	 * @return
	 * @throws SysRefControllerException
	 */
	private boolean hasSittingOnList() throws SysRefControllerException {
		boolean exists = false;
		try {
			Date currentDate = DateTimeUtilities.stripTimeToUtilDate(new Date());
			@SuppressWarnings("unchecked")
			Collection<SittingOnListBasicValue> results = bizRefDelegate.findSittingByJudgeIdAndDate(refJudge.getId(),
					currentDate);

			if (results != null && results.size() > 0) {
				exists = true;
			}
		} catch (Exception ex) {
			XHIBITConstant.handleError(ex);
		}
		return exists;
	}

	private boolean hasScheduleToday() {
		Date currentDate = DateTimeUtilities.stripTimeToUtilDate(new Date());
		Boolean exists = XhibitDelegateHelper.getHearingDelegate().isJudgeSittingOnDate(refJudge.getId(), currentDate);
		return exists;
	}

	/**
	 * Inserts a new RefJudge and associated Judge Tickets.
	 * 
	 * @param refJudgeCV
	 */
	private Boolean insertJudge() throws SysRefControllerException {
		Boolean hasSaved = false;
		// Create new RefJudgeComplexValue
		RefJudgeComplexValue refJudgeCV = new RefJudgeComplexValue();
		refJudgeCV.setCourtId(COURT_ID);
		refJudgeCV.setJudgeType(getJudgeType());
		refJudgeCV.setFirstName(getFirstName());
		refJudgeCV.setMiddleName(getOtherNames());
		refJudgeCV.setSurname(getSurname());
		refJudgeCV.setFullListTitle1(getTitle1());
		refJudgeCV.setFullListTitle2(getTitle2());
		refJudgeCV.setFullListTitle3(getTitle3());
		refJudgeCV.setStatsCode(getStatsCode());
		refJudgeCV.setInitials(getInitials());
		refJudgeCV.setTitle(getPrefix());
		refJudgeCV.setHonours(getSuffix());
		refJudgeCV.setObsInd("N");

		try {
			bizRefDelegate.insertRefJudge(refJudgeCV, getTicketsForNewJudge(), DISPLAY_NAME);
			hasSaved = true;
		} catch (CSRecoverableException e) {
			XHIBITConstant.handleError(e);
		}
		
		return hasSaved;
	}
	
	/**
	 * Populates a list of RefJudgeTickets to remove.
	 * 
	 */
	private List<RefJudgeTicketBasicValue> getTicketsToRemove() {
		List<RefJudgeTicketBasicValue> deleteList = new ArrayList<RefJudgeTicketBasicValue>();
		Collection<RefJudgeTicketBasicValue> refJudgeTicketList = refJudge.getRefJudgeTickets();
		if (refJudgeTicketList != null && refJudgeTicketList.size() > 0) {
			for (RefSystemCodeBasicValue rscbv : getJudgeTicketNonSelection()) {
				for (RefJudgeTicketBasicValue rjtbv : refJudgeTicketList) {
					if (rscbv.getCode().equals(rjtbv.getTicketType())) {
						// Find existing RefJudgeTicket entity and remove from
						// DB
						deleteList.add(rjtbv);
						break;
					}
				}
			}	
		}
		return deleteList;
	}
	
	private List<RefJudgeTicketBasicValue> getTicketsToAdd() {
		RefSystemCodeBasicValue[] selectedValues = getJudgeTicketSelection();
		List<RefJudgeTicketBasicValue> ticketList = new ArrayList<RefJudgeTicketBasicValue>();
		if (selectedValues.length > 0) {
			// Get Selected values
			for (RefSystemCodeBasicValue rscbv : selectedValues) {
				boolean exists = false;
				for (RefJudgeTicketBasicValue rjtbv : refJudge.getRefJudgeTickets()) {
					if (rscbv.getCode().equals(rjtbv.getTicketType())) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					// Create new XhbRefJudgeTicket entity and save to DB
					RefJudgeTicketBasicValue newVal = new RefJudgeTicketBasicValue();
					newVal.setCourtId(COURT_ID);
					newVal.setJudgeId(refJudge.getId());
					newVal.setTicketType(rscbv.getCode());
					ticketList.add(newVal);
				}
			}
		}
		return ticketList;
	}
	
	private List<RefJudgeTicketBasicValue> getTicketsForNewJudge() {
		RefSystemCodeBasicValue[] selectedValues = getJudgeTicketSelection();
		List<RefJudgeTicketBasicValue> ticketList = new ArrayList<RefJudgeTicketBasicValue>();
		if (selectedValues.length > 0) {
			// Get Selected values
			for (RefSystemCodeBasicValue rscbv : selectedValues) {
				RefJudgeTicketBasicValue newVal = new RefJudgeTicketBasicValue();
				newVal.setCourtId(COURT_ID);
				newVal.setJudgeId(refJudge.getId());
				newVal.setTicketType(rscbv.getCode());
				ticketList.add(newVal);
			}
		}
		return ticketList;
	}
	
	/**
	 * Converts ArrayList to Array and returns an array of selected Judge Tickets.
	 * 
	 * @return
	 */
	private RefSystemCodeBasicValue[] getJudgeTicketSelection() {
		ArrayList<RefSystemCodeBasicValue> selectedValues = getSelectedValues();
		RefSystemCodeBasicValue[] selection = new RefSystemCodeBasicValue[selectedValues.size()];
		int i = 0;
		for (Object o : selectedValues) {
			selection[i] = (RefSystemCodeBasicValue) o;
			i++;
		}
		return selection;
	}
	
	/**
	 * Returns an ArrayList of selected Judge Tickets.
	 * 
	 * @return
	 */
	private ArrayList<RefSystemCodeBasicValue> getSelectedValues() {
		ArrayList<RefSystemCodeBasicValue> selectedValues = new ArrayList<RefSystemCodeBasicValue>();

		RefSystemCodeBasicValue[] tickets = getTicketTypeCodes();

		for (RefSystemCodeBasicValue ticket : tickets) {
			if (getChkBxMurderTicketType()
					&& getMurderTicketType().getText().substring(0, 3).equals(ticket.getCode())) {
				selectedValues.add(ticket);
			}
			if (getChkBxAttemptedMurderTicketType()
					&& getAttemptedMurderTicketType().getText().substring(0, 3).equals(ticket.getCode())) {
				selectedValues.add(ticket);
			}
			if (getChkBxRapeTicketType() && getRapeTicketType().getText().substring(0, 3).equals(ticket.getCode())) {
				selectedValues.add(ticket);
			}
			if (getChkBxFraudTicketType() && getFraudTicketType().getText().substring(0, 3).equals(ticket.getCode())) {
				selectedValues.add(ticket);
			}
		}

		return selectedValues;
	}
	
	/**
	 * Returns an array of Judge Ticket codes which are not selected.
	 * 
	 * @return
	 */
	private RefSystemCodeBasicValue[] getJudgeTicketNonSelection() {
		List<RefSystemCodeBasicValue> nonSelectionValues = new ArrayList<RefSystemCodeBasicValue>();
		for (RefSystemCodeBasicValue code : getTicketTypeCodes()) {
			boolean isSelected = false;
			for (RefSystemCodeBasicValue selectedCodes : getJudgeTicketSelection()) {
				if (code.getCode().equals(selectedCodes.getCode())) {
					isSelected = true;
					break;
				}
			}
			if (!isSelected) {
				nonSelectionValues.add(code);
			}
		}
		return nonSelectionValues.toArray(new RefSystemCodeBasicValue[nonSelectionValues.size()]);
	}

	private void enableDeleteButton(boolean enable) {
		deleteButton.setEnabled(enable);
	}

	/**
	 * Updates the Results collection after performing a Delete.
	 * 
	 * @return
	 */
	private Collection<RefJudgeBasicValue> updateResultsAfterDelete() {
		@SuppressWarnings("unchecked")
		ArrayList<RefJudgeBasicValue> col = (ArrayList<RefJudgeBasicValue>) getRefSearchController().getTheResults();

		for (RefJudgeBasicValue val : col) {
			if (refJudge.getId().intValue() == val.getId().intValue()) {
				col.remove(val);
				break;
			}
		}
		return col;
	}

	/**
	 * Updates the Results collection after performing an Update.
	 * 
	 * @return
	 */
	private Collection<RefJudgeBasicValue> updateResultsAfterUpdate() {
		@SuppressWarnings("unchecked")
		ArrayList<RefJudgeBasicValue> col = (ArrayList<RefJudgeBasicValue>) getRefSearchController().getTheResults();

		for (RefJudgeBasicValue val : col) {
			if (refJudge.getId().intValue() == val.getId().intValue()) {
				col.remove(val);
				col.add((RefJudgeBasicValue) refJudge);
				break;
			}
		}
		return col;
	}

	/**
	 * Warning message to confirm if user wants to delete the Judge.
	 * 
	 * @return
	 */
	private int deleteJudgeWarningMessage() {
		return JOptionPane.showConfirmDialog(null,
				XHIBITConstant.getResource(resources, "judge.ref.details.delete.message"),
				XHIBITConstant.getResource(resources, "judge.ref.details.delete.title"), JOptionPane.YES_NO_OPTION);
	}

	/**
	 * Event handler on tabbing out of a field to check validation.
	 * 
	 * @author grewalg
	 *
	 */
	class InvalidMandatoryDataFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			enableSaveButton();
		}
	}

	/**
	 * Nested class to validate the Stat Code against the permitted range for
	 * each Judge Type. Also checks to see if Stat Code is in use for another
	 * Judge record.
	 * 
	 * @author grewalg
	 *
	 */
	static class StatsCodeValidator {
		/**
		 * Validates Stat Code against permitted range of values for Judge Type
		 * 
		 * @param judgeType
		 * @param statCode
		 * @return
		 */
		static boolean validateStatsCode(String judgeType, String statCode) {
			boolean isValid = false;
			for (StatsCodeRange scr : StatsCodeRange.values()) {
				if (scr.getJudgeType().equals(judgeType)) {
					if (Integer.valueOf(statCode) >= scr.getRangeStart()
							&& Integer.valueOf(statCode) <= scr.getRangeEnd()) {
						isValid = true;
					}
					break;
				}
			}
			return isValid;
		}

		/**
		 * Returns whether a Stat Code already exists for another Judge record.
		 * 
		 * @param statCode
		 * @param judgeId
		 * @return
		 * @throws BisRefControllerException
		 */
		static boolean isStatsCodeExist(String statCode, Integer judgeId) throws BisRefControllerException {
			// Check if Stats Code entered doesn't exist for another Judge id
			boolean exists = true;
			try {
				@SuppressWarnings("unchecked")
				Collection<RefJudgeBasicValue> judgesList = bizRefDelegate.findJudgesByCourtIdAndStatsCode(COURT_ID,
						statCode);

				// existing Judge record
				if (judgeId != null) {
					if (judgesList.size() == 0) {
						exists = false;
					} else {
						for (RefJudgeBasicValue val : judgesList) {
							// If StatCode is used for current Judge record only
							// then ignore
							if (val.getId().intValue() == judgeId.intValue() && judgesList.size() == 1) {
								exists = false;
								break;
							}
						}
					}
				} else // new Judge record not yet persisted, just check if a
						// record
						// exists
				if (judgesList.size() == 0) {
					exists = false;
				}
			} catch (Exception ex) {
				XHIBITConstant.handleError(ex);
			}
			return exists;
		}
	}
	
	/**
	 * Default gridbag that's used throughout the panels.
	 * @return gridbagconstraints
	 */
	private GridBagConstraints getGridBagLayout() {
		return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
	}

}
