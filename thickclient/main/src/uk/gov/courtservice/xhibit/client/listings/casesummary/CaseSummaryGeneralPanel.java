package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.WordUtils;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.vos.services.listing.CaseSummaryOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class CaseSummaryGeneralPanel extends CaseSummaryTab {
	
	private static final long serialVersionUID = 1L;
	private static final String LINEFEED = "\n";
	private static final String EMPTY_STRING = "";
	private DisplayOnlyField caseTitleText;
	private DisplayOnlyField sentCommittalDateText;
	private DisplayOnlyField courtSiteCodeText;
	private DisplayOnlyField defaultHearingTypeText;
	private DisplayOnlyField liveStatusText;
	private DisplayOnlyField magistratesCourtText;
	private DisplayOnlyTextArea hightlightNoteText;
	private DisplayOnlyField interpreterNoteText;
	private DisplayOnlyField requiredJudgeText;
	private DisplayOnlyField cpsCodeText;
	private DisplayOnlyField nameText;
	private DisplayOnlyField solicitorFirmText;
	private DisplayOnlyField firstProsecutionAdvocateText;
	private DisplayOnlyField firstPhoneText;
	private DisplayOnlyField secondProsecutionAdvocateText;
	private DisplayOnlyField secondPhoneText;
	private DisplayOnlyCheckBox secureCourtCheckBox;
	private DisplayOnlyCheckBox videoLinkReqdCheckBox;
	private DisplayOnlyField dartsRetentionPolicy;
	private DisplayOnlyField dartsRetentionDuration;
	private JTable chargesOffencesTable;
	private CaseSummaryModel model;
	
	public CaseSummaryGeneralPanel(CaseSummaryModel model)
	{
		super();
		this.model = model;
		jbInit();
		moveModelToScreen();
		this.dataLoaded = true;
	}
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,15,0,15), 0, 0);
		
		gbc.weighty = 0.05;
	    JPanel caseTitlePanel = initCaseTitlePanel();
	    this.add(caseTitlePanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.2;
	    JPanel hearingInformationPanel = initHearingInformationPanel();
	    this.add(hearingInformationPanel, gbc);
	    
	    gbc.gridy++;
	    gbc.weighty = 0.2;
	    JPanel prosecutionRespondentPanel = initProsecutionRespondentPanel();
	    this.add(prosecutionRespondentPanel, gbc);
	    
	    gbc.gridy++;
	    gbc.weighty = 0.15;
	    JPanel section28ProsecutionAdvocatesPanel = initSection28ProsecutionAdvocatesPanel();
	    this.add(section28ProsecutionAdvocatesPanel, gbc);
	    
	    gbc.gridy++;
	    gbc.weighty = 0.4;
	    JScrollPane chargesOffencesPanel = initChargesOffencesPanel();
	    this.add(chargesOffencesPanel, gbc);
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
	
	private JPanel initHearingInformationPanel() {
		
		JPanel hearingInformationPanel = new JPanel();
		hearingInformationPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//Sent Committal Date
		String committalAppealLabelText;
		switch (model.getCaseStatus().getCaseType())
		{
			case APPEAL:
			case MISC:
				committalAppealLabelText = XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalAppealLodgedDate");
				break;	
			case SENTENCE:
			case TRIAL:
			default:
				committalAppealLabelText = XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalSentCommittalDate");	
				break;
		}
		
		JLabel sentCommittalDate = new JLabel(committalAppealLabelText);
		gbc.weightx = 0.05;
		hearingInformationPanel.add(sentCommittalDate, gbc);
		
		sentCommittalDateText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.2;
		hearingInformationPanel.add(sentCommittalDateText, gbc);
		
		JLabel courtSiteCodeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalCourtSiteCode"));
		gbc.weightx = 0.05;
		gbc.gridx++;
		hearingInformationPanel.add(courtSiteCodeLabel, gbc);
		
		courtSiteCodeText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.2;
		hearingInformationPanel.add(courtSiteCodeText, gbc);
		
		//Default Hearing Type
		JLabel hearingTypelabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalDefaultHearingType"));
		gbc.gridx++;
		gbc.weightx = 0.05;
		hearingInformationPanel.add(hearingTypelabel, gbc);
		
		defaultHearingTypeText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.45;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		hearingInformationPanel.add(defaultHearingTypeText, gbc);
		
		//Live Status
		JLabel liveStatusLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalLiveStatus"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		hearingInformationPanel.add(liveStatusLabel, gbc);
		
		liveStatusText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.3;
		hearingInformationPanel.add(liveStatusText, gbc);
	
		//Magistrates Court
		JLabel magistratesCourtLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalMagistratesCourt"));
		gbc.gridx++;
		gbc.weightx = 0.05;
		hearingInformationPanel.add(magistratesCourtLabel, gbc);
		
		magistratesCourtText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.6;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		hearingInformationPanel.add(magistratesCourtText, gbc);
		
		
		//Highlight Notes
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		JLabel highlightNoteLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalHighlightNote"));
		hearingInformationPanel.add(highlightNoteLabel, gbc);
		
		hightlightNoteText = new DisplayOnlyTextArea();
		hightlightNoteText.setRows(3);
		hightlightNoteText.setLineWrap(true);
		hightlightNoteText.setWrapStyleWord(true);
		JScrollPane hightlightNoteScrollpane = new JScrollPane(hightlightNoteText);
		hightlightNoteScrollpane.setPreferredSize(hightlightNoteText.getPreferredSize());
		gbc.gridx++;	
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		hearingInformationPanel.add(hightlightNoteScrollpane, gbc);
		
		//Interpreter Note
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		JLabel interpreterNoteLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalInterpreter"));
		hearingInformationPanel.add(interpreterNoteLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		interpreterNoteText = new DisplayOnlyField();
		hearingInformationPanel.add(interpreterNoteText, gbc);
	
		//Required Judge
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		JLabel requiredJudgeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalRequiredJudge"));
		hearingInformationPanel.add(requiredJudgeLabel, gbc);
		
		gbc.gridx++;	
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		requiredJudgeText = new DisplayOnlyField();
		hearingInformationPanel.add(requiredJudgeText, gbc);
		
		// Secure Court
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		secureCourtCheckBox = new DisplayOnlyCheckBox(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalSecureCourt"));
		hearingInformationPanel.add(secureCourtCheckBox, gbc);

		// Video Link Required
		gbc.gridx++;
		gbc.weightx = 0.5;
		gbc.gridwidth = 1;
		videoLinkReqdCheckBox = new DisplayOnlyCheckBox(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalVideoLinkRequired"));		
		hearingInformationPanel.add(videoLinkReqdCheckBox, gbc);

		// DARTS retention period 
		gbc.gridx++;
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 2;
		hearingInformationPanel.add(initDARTSRetentionPanel(),gbc);
		
		return hearingInformationPanel;
	}
	
	private JPanel initDARTSRetentionPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalDARTSRetentionPanelTitle")));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		JLabel dartsRetentionPeriodLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalDARTSRetentionPolicy"));
		panel.add(dartsRetentionPeriodLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		dartsRetentionPolicy = new DisplayOnlyField();		
		panel.add(dartsRetentionPolicy, gbc);
		
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		gbc.gridwidth = 1;
		JLabel dartsRetentionDurationLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalDARTSRetentionDuration"));
		panel.add(dartsRetentionDurationLabel, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0.95;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		dartsRetentionDuration = new DisplayOnlyField();	
		panel.add(dartsRetentionDuration, gbc);
		
		return panel;
	}
	
	private JPanel initProsecutionRespondentPanel() {
		JPanel prosecutionRespondentPanel = new JPanel();
		prosecutionRespondentPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalProsecutionRespondentPanelTitle")));
		prosecutionRespondentPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//CPS Code
		JLabel cpsCodeLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalCPSCode"));
		gbc.weightx = 0.05;
		prosecutionRespondentPanel.add(cpsCodeLabel, gbc);
		
		cpsCodeText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.3;
		prosecutionRespondentPanel.add(cpsCodeText, gbc);
		
		//Name
		JLabel nameLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalName"));
		Insets leftPaddedInsets = new Insets(4,20,4,4);
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0.05;
		prosecutionRespondentPanel.add(nameLabel, gbc);
		
		nameText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.6;
		prosecutionRespondentPanel.add(nameText, gbc);
		
		//Solicitor Firm
		JLabel solicitorFirmLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalSolicitorFirm"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0.05;
		prosecutionRespondentPanel.add(solicitorFirmLabel, gbc);
		
		solicitorFirmText = new DisplayOnlyField();
		gbc.gridx++;	
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.95;
		prosecutionRespondentPanel.add(solicitorFirmText, gbc);
		
		return prosecutionRespondentPanel;
	}
	
	private JPanel initSection28ProsecutionAdvocatesPanel() {
		JPanel section28ProsecutionAdvocatesPanel = new JPanel();
		section28ProsecutionAdvocatesPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "generalSection28PanelTitle")));
		section28ProsecutionAdvocatesPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//1
		JLabel firstProsecutionAdvocateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Name1"));
		gbc.weightx = 0.05;
		section28ProsecutionAdvocatesPanel.add(firstProsecutionAdvocateLabel, gbc);
		
		firstProsecutionAdvocateText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		section28ProsecutionAdvocatesPanel.add(firstProsecutionAdvocateText, gbc);
		
		//1 Phone
		JLabel firstPhoneLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Phone"));
		Insets leftPaddedInsets = new Insets(4,20,4,4);
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0.05;
		section28ProsecutionAdvocatesPanel.add(firstPhoneLabel, gbc);
		
		firstPhoneText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.3;
		section28ProsecutionAdvocatesPanel.add(firstPhoneText, gbc);
		
		//2
		JLabel secondProsecutionAdvocateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Name2"));
		gbc.gridy++;
		gbc.gridx=0;
		gbc.weightx = 0.05;
		section28ProsecutionAdvocatesPanel.add(secondProsecutionAdvocateLabel, gbc);
		
		secondProsecutionAdvocateText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		section28ProsecutionAdvocatesPanel.add(secondProsecutionAdvocateText, gbc);
		
		//2 Phone
		JLabel secondPhoneLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Phone"));
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0.05;
		section28ProsecutionAdvocatesPanel.add(secondPhoneLabel, gbc);
		
		secondPhoneText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.3;
		section28ProsecutionAdvocatesPanel.add(secondPhoneText, gbc);
		
		return section28ProsecutionAdvocatesPanel;
	}
	
	private JScrollPane initChargesOffencesPanel() {
		chargesOffencesTable = new JTable();
		JScrollPane chargesOffencesPanel = new JScrollPane(chargesOffencesTable);
		chargesOffencesPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalChargesOffencesPanelTitle")));
		chargesOffencesTable.setPreferredScrollableViewportSize(chargesOffencesTable.getPreferredSize());
		chargesOffencesTable.setTableHeader(null);
		return chargesOffencesPanel;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void moveModelToScreen() {
		this.caseTitleText.setText(model.getCase().getCaseTitle());		
		switch (model.getCaseStatus().getCaseType())
		{
			case APPEAL:
			case MISC:
				this.sentCommittalDateText.setText(XDateFormat.format(model.getCase().getAppealLodgedDate(), XDateFormat.DATEFORMAT));
				break;	
			case SENTENCE:
			case TRIAL:
			default:				 
				if (model.getCase().getCommittalDate() != null) {
					this.sentCommittalDateText.setText(XDateFormat.format(model.getCase().getCommittalDate(), XDateFormat.DATEFORMAT));
				} else {
					this.sentCommittalDateText.setText(XDateFormat.format(model.getCase().getSentForTrialDate(), XDateFormat.DATEFORMAT));
				}
				break;
		}
		
		this.courtSiteCodeText.setText(model.getCourtSite() != null ? model.getCourtSite().getCourtSiteCode() : EMPTY_STRING);
		this.defaultHearingTypeText.setText(EMPTY_STRING);
		if(model.getCase().getDefaultHearingType() != null)	{
	        RefHearingTypeBasicValue defaultHearingType = null;
			try {
				defaultHearingType = (RefHearingTypeBasicValue) XhibitDelegateHelper.getBizRefDelegate().findHearingTypeById(model.getCase().getDefaultHearingType());
			} catch (BisRefControllerException e) {
				Message userMessage = new Message("gui.updategeneralcasedata.getHearingtype");
				throw new CSUnrecoverableException(userMessage, e, "Error whilst retreiving hearing details");
			}
			
	        if(defaultHearingType != null) {
	        	this.defaultHearingTypeText.setText(String.format("%s - %s", defaultHearingType.getHearingTypeCode(), defaultHearingType.getHearingTypeDesc()));
	        }
		}
				
		this.liveStatusText.setText(model.getLiveStatus());
		
		if ( model.getCase().getRefCourtID() != null ) {
			RefCourtCriteria criteria = new RefCourtCriteria();
			criteria.setPrimaryKey(model.getCase().getRefCourtID());
			ArrayList<RefCourtBasicValue> courtList;
			try {
				courtList = (ArrayList<RefCourtBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findCourts(criteria));
			} catch (BisRefControllerException e) {
				Message userMessage = new Message("gui.user.sysref.findCourtText");
				throw new CSUnrecoverableException(userMessage, e, "Error whilst retreiving court details");
			}
			if(courtList.size() == 1)
			{
				 RefCourtBasicValue court = courtList.get(0);
				 this.magistratesCourtText.setText(String.format("%s - %s", court.getCourtShortName(), court.getCourtFullName()));
			} else {
				 this.magistratesCourtText.setText(EMPTY_STRING);
			}
		}
		else {
			this.magistratesCourtText.setText(EMPTY_STRING);
		}
		
		this.firstProsecutionAdvocateText.setText(model.getCase().getSection28Name1());
		this.firstPhoneText.setText(model.getCase().getSection28Phone1());
		this.secondProsecutionAdvocateText.setText(model.getCase().getSection28Name2());
		this.secondPhoneText.setText(model.getCase().getSection28Phone2());
		
		CaseListingEntryComplexValue caseListingEntry = model.getCaseListingEntry();
		this.hightlightNoteText.setText(caseListingEntry.getHighlightDiaryNoteEntry() != null ? caseListingEntry.getHighlightDiaryNoteEntry().getDiaryNoteText() : null);
		this.interpreterNoteText.setText(caseListingEntry.getInterpreterDiaryNoteEntry() != null ? caseListingEntry.getInterpreterDiaryNoteEntry().getDiaryNoteText() : null);
		if(caseListingEntry.getRefJudge() != null)
		{
			this.requiredJudgeText.setText(caseListingEntry.getRefJudge().getFirstName() + " " + caseListingEntry.getRefJudge().getSurname());
		} else {
			this.requiredJudgeText.setText(EMPTY_STRING);
		}
	
		this.secureCourtCheckBox.setSelected(getBooleanFromString(caseListingEntry.getCaseBasicValue().getSecureCourt()));
		this.videoLinkReqdCheckBox.setSelected(getBooleanFromString(caseListingEntry.getCaseBasicValue().getVideoLinkRequired()));
		
		// Add the retention policy text
		this.dartsRetentionPolicy.setText(getResourceBundle("generalDARTSRetentionPolicy.noData"));
		this.dartsRetentionDuration.setText(getResourceBundle("generalDARTSRetentionDuration.noData"));
		if (model.getDartsRetentionPolicy() != null && 
				model.getDartsRetentionPolicy().getRefDarRetentionPoliciesBasicValue() != null &&
				model.getDartsRetentionPolicy().getRefDarRetentionPoliciesBasicValue().getPolicyNo() > 0) {
			this.dartsRetentionPolicy.setText(getResourceBundle("generalDARTSRetentionPolicy.text",new Object[] {
					model.getDartsRetentionPolicy().getRefDarRetentionPoliciesBasicValue().getPolicyNo(),
					model.getDartsRetentionPolicy().getRefDarRetentionPoliciesBasicValue().getPolicyDescription()}));
			this.dartsRetentionDuration.setText(getDARTSRetentionDurationText());
		}
		
		// Add the legacy stamp to retention policy text (if required)
		boolean isLegacy = isLegacy(model.getCase().getCreationDate(), model.getDvrReleaseDate());
		this.dartsRetentionDuration.setText(this.dartsRetentionDuration.getText() 
				+ (isLegacy ? " "+getResourceBundle("generalDARTSRetentionDuration.legacy") : ""));
		
		populateProsecutorDetails();	
	
		populateChargesLog();
	}

	private String getDARTSRetentionDurationText() {
		String yearsText = getResourceBundle(Integer.valueOf(1).equals(model.getDartsRetentionPolicy().getDurationYears()) ?
				"generalDARTSRetentionDuration.text.year" : "generalDARTSRetentionDuration.text.years");
		String monthsText = getResourceBundle(Integer.valueOf(1).equals(model.getDartsRetentionPolicy().getDurationMonths()) ?
				"generalDARTSRetentionDuration.text.month" : "generalDARTSRetentionDuration.text.months");
		String daysText = getResourceBundle(Integer.valueOf(1).equals(model.getDartsRetentionPolicy().getDurationDays()) ?
				"generalDARTSRetentionDuration.text.day" : "generalDARTSRetentionDuration.text.days");
		
		return getResourceBundle("generalDARTSRetentionDuration.text",new Object[] {
				model.getDartsRetentionPolicyDurationYears(),
				yearsText,
				model.getDartsRetentionPolicyDurationMonths(),
				monthsText,
				model.getDartsRetentionPolicyDurationDays(),
				daysText});
	}
	
	private boolean isLegacy(Date caseCreationDate, Date dvrReleaseDate) {
		if (caseCreationDate != null && dvrReleaseDate != null) {
			return caseCreationDate.before(dvrReleaseDate);
		}
		return false;
	}
	
	public boolean getBooleanFromString(String val) {
		return val != null && val.equals(YES);
	}
	
	private void populateChargesLog() {
		// Get Charges
		Integer caseId = model.getCaseId();
		String chargesLog = XhibitDelegateHelper.getCaseDelegate().getChargesLog(caseId);
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {"ChargesLogColumn"});
		chargesOffencesTable.setModel(model);
		for (String line : wordWrap(chargesLog, 80)) {
			model.addRow(new String[]{ line });
		}
		
		// Get Offences
		if ( displayOffences() ) {
			ArrayList<String> offences = getCaseOffences(caseId);
			for (String offence : offences) {
				model.addRow( new String[]{offence});
			}
		}
	}
	
	private List<String> wordWrap(String text, int lineLength) {
		List<String> result = new ArrayList<String>();
		if (text != null && !EMPTY_STRING.equals(text)) {
			String[] splitLines = text.split(LINEFEED);
			for (String splitLine : splitLines) {
				String[] lines = WordUtils.wrap(splitLine, lineLength).split(LINEFEED);
				for (String line : lines) {
					result.add(line);
				}
			}
		}
		return result;
	}

	private void populateProsecutorDetails() {
		this.cpsCodeText.setText(EMPTY_STRING);
		this.nameText.setText(EMPTY_STRING);
		this.solicitorFirmText.setText(EMPTY_STRING);
		try {
			for(CaseProsecutorAgencyValue caseProsecutor : model.getCaseProsecutors())
			{
				if(caseProsecutor != null && (!"O".equals(caseProsecutor.getRespondentStatus()))){				
					ProsecutorAgency prosecutorAgency = new ProsecutorAgency(caseProsecutor.getRefProsecutorAgencyID());
					this.cpsCodeText.setText(prosecutorAgency.getCpsCode());
					this.nameText.setText(prosecutorAgency.getProsecutorName());

					ProsecutorSolicitorFirm solicitorFirm = new ProsecutorSolicitorFirm(caseProsecutor.getCaseProsAgencyID());
					this.solicitorFirmText.setText(solicitorFirm.getSolicitorText());
				}
			}	
		} catch (FinderException e) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e, "Error whilst retreiving Ref Prosecutor Agency details");
		} catch (CSBusinessException e) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e, "Error whilst retreiving Prosecutor Firm Name");
		}
	}
	
	private class DisplayOnlyCheckBox extends JCheckBox {

		private static final long serialVersionUID = 1L;
		
		public DisplayOnlyCheckBox(String text) {
			super(text);
			setFocusable(false);
		}

		@Override
		protected void processMouseEvent(MouseEvent e) {
			// Stop selection via mouse click
		}
	}
	
	/**
	 * Determines whether or not the case should display Offence information
	 * @return true if the case should display Offence details else false
	 */
	private boolean displayOffences() {
		boolean displayOffences = false;
		String caseType = model.getCase().getCaseType();
		String caseSubType = model.getCase().getCaseSubType();
		String receiptType = model.getCase().getReceiptType();
		
		if ( "S".equals(caseType) ) {
			// Sentence Case
			if ( "CS".equals(receiptType) || "CB".equals(receiptType) || "BB".equals(receiptType) ) {
				displayOffences = true;
			}
		}
		else if ( "A".equals(caseType) ) {
			// Appeal Case
			if ( "C".equals(caseSubType) || "S".equals(caseSubType) || "B".equals(caseSubType) ) {
				displayOffences = true;
			}
		}
		
		return displayOffences;
	}
	
	/**
	 * Retrieves all offences on the case and returns an ordered list of the offence descriptions with duplicates 
	 * removed.
	 * @param caseId Case Id to retrieve offences for
	 * @return List of offence descriptions
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getCaseOffences(Integer caseId) {
		// Get the list of all Offences in the correct sort order
		ArrayList<CaseSummaryOffenceValue> offences = (ArrayList<CaseSummaryOffenceValue>) XhibitDelegateHelper.getCaseDelegate().getCaseOffences(caseId);
		
		// Remove duplicates
		ArrayList<String> processedOffences = new ArrayList<String>();
		for (CaseSummaryOffenceValue offence : offences) {
			if ( !processedOffences.contains(offence.getOffenceDesc()) ) {
				processedOffences.add(offence.getOffenceDesc());
			}
		}
		return processedOffences;
	}
	
}