package uk.gov.courtservice.xhibit.client.listings.casesummary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.FinderException;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import uk.gov.courtservice.framework.exception.CSBusinessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseSummaryAppellantPanel extends CaseSummaryTab {

	private static final long serialVersionUID = 1L;
	private CaseSummaryModel model;
	private Insets leftPaddedInsets = new Insets(4,20,4,4);
	private static final String EMPTY_STRING = "";
	
	private DisplayOnlyField caseTitleText;
	private JTable objectorsTable;
	private DisplayOnlyField nameText;
	private DisplayOnlyField genderText;
	private DisplayOnlyField parentGuardianText;
	private DisplayOnlyField solicitorText;
	private DisplayOnlyField prisonerNumberText;
	private DisplayOnlyField bcStatusText;
	private DisplayOnlyField prisonIdText;
	private JCheckBox inCustodyCheckBox;
	private XTextArea addressText;
	private JCheckBox isJuvenileCheckBox;
	private DisplayOnlyField dateOfBirthText;
	private DisplayOnlyField authorisedText;
	private DisplayOnlyField firstAdvocateText;
	private DisplayOnlyField firstPhoneText;
	private DisplayOnlyField secondAdvocateText;
	private DisplayOnlyField secondPhoneText;
	private DisplayOnlyField putBackText;
	private DisplayOnlyField toText;
	private DisplayOnlyField urnText;

	public CaseSummaryAppellantPanel(CaseSummaryModel model) {
		super();
		this.model = model;
		jbInit();
	}
	
	private void jbInit() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,15,0,15), 0, 0);
		
		gbc.weighty = 0.2;
	    JPanel caseTitlePanel = initCaseTitlePanel();
	    this.add(caseTitlePanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0.4;
	    JPanel objectorsPanel = initObjectorsPanel();
	    this.add(objectorsPanel, gbc);
	    
		gbc.gridy++;
		gbc.weighty = 0.4;
	    JPanel appellantDetailsPanel = initAppellantDetailsPanel();
	    this.add(appellantDetailsPanel, gbc);
		
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
	
	private JPanel initObjectorsPanel() {
		
		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {}, new String[]{"Name", "Solicitor Firm or Representation"}) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		objectorsTable = XTableFactory.getInstance().createDefaultTable(tableModel);
		JScrollPane objectorsTableScrollPanel = new JScrollPane(objectorsTable);
		objectorsTableScrollPanel.setPreferredSize(new Dimension(200, 80));
		objectorsTable.setPreferredScrollableViewportSize(objectorsTableScrollPanel.getPreferredSize());
		
		JPanel objectorsPanel = new JPanel();
		objectorsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"appellantObjectorsPanelTitle")));
		objectorsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.7, GridBagConstraints.CENTER,GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		objectorsPanel.add(objectorsTableScrollPanel, gbc);
		return objectorsPanel;
	}
	
	private JPanel initAppellantDetailsPanel() {
		JPanel appellantDetailsPanel = new JPanel();
		appellantDetailsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"appellantDetailsPanelTitle")));
		appellantDetailsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JLabel nameLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "appellantName"));
		appellantDetailsPanel.add(nameLabel, gbc);
		
		gbc.gridx++;		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = XHIBITConstant.containerInsets;
		appellantDetailsPanel.add(initNamePanel(), gbc);
		
		JLabel parentGuardianLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantParentGuardian"));
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.insets = XHIBITConstant.nonContainerInsets;
		appellantDetailsPanel.add(parentGuardianLabel, gbc);
		
		parentGuardianText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		appellantDetailsPanel.add(parentGuardianText, gbc);
		
		//Solicitor
		JLabel solicitorLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantSolicitor"));
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		appellantDetailsPanel.add(solicitorLabel, gbc);
		
		solicitorText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.4;
		appellantDetailsPanel.add(solicitorText, gbc);
		
		//Prison Id
		JLabel prisonIdLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantPrisonId"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		appellantDetailsPanel.add(prisonIdLabel, gbc);
		
		prisonIdText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		appellantDetailsPanel.add(prisonIdText, gbc);
		
		//Prisoner Number
		JLabel prisonerNumberLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantPrisonerNumber"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		appellantDetailsPanel.add(prisonerNumberLabel, gbc);
		
		prisonerNumberText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.4;
		appellantDetailsPanel.add(prisonerNumberText, gbc);
		
		//B/C Status
		JLabel bcStatusLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantBCStatusLimit"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		appellantDetailsPanel.add(bcStatusLabel, gbc);
		
		bcStatusText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		appellantDetailsPanel.add(bcStatusText, gbc);
		
		// Date of Birth
		JLabel dateOfBirthLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "appellantDateOfBirth"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		appellantDetailsPanel.add(dateOfBirthLabel, gbc);
		
		dateOfBirthText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.4;
		appellantDetailsPanel.add(dateOfBirthText, gbc);
		
		//Results Authorised
		JLabel AuthorisedLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantResultsAuthorised"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		appellantDetailsPanel.add(AuthorisedLabel, gbc);
				
		authorisedText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.6;
		appellantDetailsPanel.add(authorisedText, gbc);
		
		//URN
		JLabel URNLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantURN"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		appellantDetailsPanel.add(URNLabel, gbc);
		
		urnText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.4;
		appellantDetailsPanel.add(urnText, gbc);
		
		//In Custody
		JLabel inCustodyLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantInCustody"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		appellantDetailsPanel.add(inCustodyLabel, gbc);
		
		inCustodyCheckBox = new JCheckBox(); 
		inCustodyCheckBox.setEnabled(false);
		gbc.gridx++;		
		gbc.weightx = 0.6;
		appellantDetailsPanel.add(inCustodyCheckBox, gbc);
		
		//Is Juvenile
		JLabel isJuvenileLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "appellantIsJuvenile"));
		gbc.gridx++;
		gbc.weightx = 0;
		gbc.insets = leftPaddedInsets;
		appellantDetailsPanel.add(isJuvenileLabel, gbc);
		
		isJuvenileCheckBox = new JCheckBox(); 
		isJuvenileCheckBox.setEnabled(false);
		gbc.gridx++;		
		gbc.weightx = 0.4;
		gbc.insets =  XHIBITConstant.nonContainerInsets;
		appellantDetailsPanel.add(isJuvenileCheckBox, gbc);
	
		//Address
		JLabel addressLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantAddress"));
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth=1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		appellantDetailsPanel.add(addressLabel, gbc);

		addressText = new XTextArea();
		addressText.setRows(3);
		addressText.setLineWrap(true);
		addressText.setWrapStyleWord(true);
		addressText.setEditable(false);
		addressText.setFocusable(false);
		gbc.gridx++;		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		JScrollPane addressScrollpane = new JScrollPane(addressText);
		addressScrollpane.setPreferredSize(addressText.getPreferredSize());
		appellantDetailsPanel.add(addressScrollpane, gbc);
		
		//Section 28 Advocates
		gbc.gridy++;
		gbc.gridx = 0;
		JPanel section28AdvocatesPanel = initSection28AdvocatesPanel();
		appellantDetailsPanel.add(section28AdvocatesPanel, gbc);
		
		//Put Back
		gbc.gridy++;
		JPanel putPackPanel = initPutBackPanel();
		appellantDetailsPanel.add(putPackPanel, gbc);
		
		return appellantDetailsPanel;
	}
	
	private JPanel initNamePanel()
	{
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0.8, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//Name
		nameText = new DisplayOnlyField();
		namePanel.add(nameText, gbc);
		
		//Gender
		JLabel genderLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "appellantGender"));
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		namePanel.add(genderLabel, gbc);
		
		genderText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.2;
		namePanel.add(genderText, gbc);
		return namePanel;
	}
	
	private JPanel initSection28AdvocatesPanel() {
		JPanel section28AdvocatesPanel = new JPanel();
		section28AdvocatesPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantSection28AdvocatesPanelTitle")));
		section28AdvocatesPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//1
		JLabel firstProsecutionAdvocateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Name1"));
		gbc.weightx = 0;
		section28AdvocatesPanel.add(firstProsecutionAdvocateLabel, gbc);
		
		firstAdvocateText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.7;
		section28AdvocatesPanel.add(firstAdvocateText, gbc);
		
		//1 Phone
		JLabel firstPhoneLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Phone"));
		Insets leftPaddedInsets = new Insets(4,20,4,4);
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		section28AdvocatesPanel.add(firstPhoneLabel, gbc);
		
		firstPhoneText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.3;
		section28AdvocatesPanel.add(firstPhoneText, gbc);
		
		//2
		JLabel secondProsecutionAdvocateLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Name2"));
		gbc.gridy++;
		gbc.gridx=0;
		gbc.weightx = 0;
		section28AdvocatesPanel.add(secondProsecutionAdvocateLabel, gbc);
		
		secondAdvocateText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.7;
		section28AdvocatesPanel.add(secondAdvocateText, gbc);
		
		//2 Phone
		JLabel secondPhoneLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"generalSection28Phone"));
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		section28AdvocatesPanel.add(secondPhoneLabel, gbc);
		
		secondPhoneText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.3;
		section28AdvocatesPanel.add(secondPhoneText, gbc);
		
		return section28AdvocatesPanel;
	}
	
	private JPanel initPutBackPanel() {
		JPanel putBackPanel = new JPanel();
		putBackPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantPutBackPanelPanelTitle")));
		putBackPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		//Put back
		putBackText = new DisplayOnlyField();	
		gbc.weightx = 0.7;
		putBackPanel.add(putBackText, gbc);
		
		//To
		JLabel toLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantPutBackTo"));
		Insets leftPaddedInsets = new Insets(4,20,4,4);
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		putBackPanel.add(toLabel, gbc);
		
		toText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.3;
		putBackPanel.add(toText, gbc);
		
		return putBackPanel;		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void moveModelToScreen() {
		try {
			this.caseTitleText.setText(model.getCaseListingEntry().getCaseBasicValue().getCaseTitle());
			
			DefaultTableModel tableModel = (DefaultTableModel) this.objectorsTable.getModel();
			for(CaseProsecutorAgencyValue caseProsecutor : model.getCaseProsecutors())
			{
				if(caseProsecutor != null && "O".equals(caseProsecutor.getRespondentStatus())){				
					ProsecutorAgency prosecutor = new ProsecutorAgency(caseProsecutor.getRefProsecutorAgencyID());
					ProsecutorSolicitorFirm solicitorFirm = new ProsecutorSolicitorFirm(caseProsecutor.getCaseProsAgencyID());
					tableModel.addRow(new String[]{prosecutor.getProsecutorFullName(), solicitorFirm.getSolicitorText()});
				}
			}
			
			if(model.getCaseListingEntry().getDefendants().size() > 0){
				DefendantValue appellant = (DefendantValue) model.getCaseListingEntry().getDefendants().toArray()[0];
				DefendantValue fullAppellant = (DefendantValue) XhibitDelegateHelper.getDefendantDelegate().getFullDefendantDetails(appellant.getDefendantID(), model.getCaseId());
				this.nameText.setText(fullAppellant.getFirstName() + " ") ;
				if(fullAppellant.getMiddleName()!=null) {
					this.nameText.setText(this.nameText.getText()+fullAppellant.getMiddleName()+" ");
				}
				this.nameText.setText(this.nameText.getText()+fullAppellant.getSurName());
				this.genderText.setText(fullAppellant.getGenderString());
				this.isJuvenileCheckBox.setSelected( DefendantValue.IS_JUVENILE_TRUE.equals(fullAppellant.getIsJuvenile()));
				this.inCustodyCheckBox.setSelected("Y".equals(fullAppellant.getCurrentPrisonStatus()));
				this.parentGuardianText.setText(fullAppellant.getParentGuardianName());
				this.urnText.setText(fullAppellant.getPtiurn());
				
				if ( fullAppellant.getPrisonId() != null ) {
					String prisonName = "";
					RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
					criteria.setCodeType(RefSystemCodeCriteria.CodeType.PRISON_ID);
					criteria.setCode(fullAppellant.getPrisonId());
					criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
					ArrayList<RefSystemCodeBasicValue> prisonList = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
					if(prisonList != null && prisonList.size() > 0){
						prisonName = " - " + prisonList.get(0).getDecode();
					}
					this.prisonIdText.setText(fullAppellant.getPrisonId() + prisonName);
				}
				
				String resultsAuthorisedCode = "N";
				if ( fullAppellant.getDefOnCaseBasicValue().getResultsVerified() != null ) {
					resultsAuthorisedCode = fullAppellant.getDefOnCaseBasicValue().getResultsVerified();
				}
				this.authorisedText.setText( ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "results.authorise.status."+resultsAuthorisedCode) );
				
				if(fullAppellant.getAddressValue() != null){
					this.addressText.setText(fullAppellant.getAddressValue().getHumanReadableAddressString().trim());
					this.addressText.setCaretPosition(0);
				} else {
					this.addressText.setText(EMPTY_STRING);
				}
				this.firstAdvocateText.setText(fullAppellant.getDefOnCaseBasicValue().getSection28Name1());
				this.firstPhoneText.setText(fullAppellant.getDefOnCaseBasicValue().getSection28Phone1());
				this.secondAdvocateText.setText(fullAppellant.getDefOnCaseBasicValue().getSection28Name2());
				this.secondPhoneText.setText(fullAppellant.getDefOnCaseBasicValue().getSection28Phone2());
				
				Date dateOfBirth = fullAppellant.getDateOfBirth() != null ? fullAppellant.getDateOfBirth().getTime() : null;
				this.dateOfBirthText.setText(getFormattedDate(dateOfBirth));
				
				this.bcStatusText.setText(EMPTY_STRING);
				if ( fullAppellant.getDefOnCaseBasicValue().getCurrentBcStatus() != null ) {
					RefSystemCodeCriteria bailTypeCriteria = new RefSystemCodeCriteria();
					bailTypeCriteria.setCodeType(RefSystemCodeCriteria.CodeType.BAIL_TYPE);
					bailTypeCriteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
					bailTypeCriteria.setCode(fullAppellant.getDefOnCaseBasicValue().getCurrentBcStatus());
					ArrayList<RefSystemCodeBasicValue> bailType = (ArrayList<RefSystemCodeBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(bailTypeCriteria);
					if(bailType != null && bailType.size() > 0){
						this.bcStatusText.setText(bailType.get(0).getDecode());
					}
				}
				
				if(fullAppellant.getPrisonerNo() != null){
					this.prisonerNumberText.setText(fullAppellant.getPrisonerNo().getReferenceValue());
				}
				
				DefendantSolicitorFirm defSolicitorFirm = new DefendantSolicitorFirm(fullAppellant.getDefOnCaseBasicValue().getDefendantOnCaseId());
				this.solicitorText.setText(defSolicitorFirm.getSolicitorText());
				
				PutBack putBack = new PutBack(fullAppellant.getDefOnCaseBasicValue().getDefendantOnCaseId());
				this.putBackText.setText(putBack.getPutBackText());
				this.toText.setText(putBack.getToText());
	
			}
			this.repaint();
		} catch (CSBusinessException e) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e, "Error whilst retreiving Prosecutor Firm Name");
		} catch (FinderException e1) {
			Message newUserMessage = new Message("sysadmin.bisref.finderexception");
			throw new CSUnrecoverableException(newUserMessage, e1, "Error whilst retreiving Solicitor details");
		} 
	}
	
	private String getFormattedDate(Date date) {
		String result = null;
		if (date != null) {
			try {
				result = DateTimeUtilities.convertDate(date, new SimpleDateFormat("dd-MMM-yyyy"));
			} catch (ParseException ex) {
				XHIBITConstant.handleError(ex);
			}
		}
		return result;
	}
}
