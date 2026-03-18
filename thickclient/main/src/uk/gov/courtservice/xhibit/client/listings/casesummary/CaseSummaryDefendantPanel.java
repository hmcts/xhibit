package uk.gov.courtservice.xhibit.client.listings.casesummary;

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
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.listings.list.common.TableUtils;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTextArea;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class CaseSummaryDefendantPanel extends CaseSummaryTab {

	private static final long serialVersionUID = 1L;
	private CaseSummaryModel model;
	private DisplayOnlyField caseTitleText;
	private JTable allDefendantsTable;
	private DisplayOnlyField parentGuardianText;
	private DisplayOnlyField solicitorText;
	private DisplayOnlyField custodyTimeLimitText;
	private DisplayOnlyField prisonerNumberText;
	private JTextArea addressText;
	private JCheckBox inCustodyCheckBox;
	private DisplayOnlyField prisonIdText;
	private DisplayOnlyField urnText;
	private DisplayOnlyField dateOfBirthText;
	private DisplayOnlyField authorisedText;
	private DisplayOnlyField firstAdvocateText;
	private DisplayOnlyField firstPhoneText;
	private DisplayOnlyField secondAdvocateText;
	private DisplayOnlyField secondPhoneText;
	private DisplayOnlyField putBackText;
	private DisplayOnlyField toText;
	private DisplayOnlyField bcStatusText;

	public CaseSummaryDefendantPanel(CaseSummaryModel model) {
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
		gbc.weighty = 0.8;
	    JPanel allDefendantsPanel = initAllDefendantsPanel();
	    this.add(allDefendantsPanel, gbc);
		
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
	
	private JPanel initAllDefendantsPanel() {
		CaseSummaryDefendantsTableModel tableModel = new CaseSummaryDefendantsTableModel(CaseSummaryDefendantsTableModel.TableId.DEFENDANT_PANEL);
		allDefendantsTable = new JTable(tableModel);
		JScrollPane allDefendantsTableScrollPanel = new JScrollPane(allDefendantsTable);
		allDefendantsTableScrollPanel.setPreferredSize(allDefendantsTableScrollPanel.getPreferredSize());
		TableUtils.setupDefaultsOnJTable(allDefendantsTable);
		setTableColumnWidths(allDefendantsTable, tableModel.getColumnWidths());
		allDefendantsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){				
					handleSelectionEvent(e);
				}
			}
		});
		
		JPanel allDefendantsPanel = new JPanel();
		allDefendantsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"allDefendantsPanelTitle")));
		allDefendantsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, GridBagConstraints.CENTER,GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0);
		allDefendantsPanel.add(allDefendantsTableScrollPanel, gbc);
		
		JPanel defendantDetailsPanel = initDefendantDetailsPanel();
		gbc.gridy++;
		gbc.weighty = 0.5;
		allDefendantsPanel.add(defendantDetailsPanel, gbc);
		
		return allDefendantsPanel;
	}

	private JPanel initDefendantDetailsPanel() {
		JPanel defendantDetailsPanel = new JPanel();
		defendantDetailsPanel.setBorder(BorderFactory.createTitledBorder(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantDetailsPanelTitle")));
		defendantDetailsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);
		
		JLabel parentGuardianLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantParentGuardian"));
		gbc.weightx = 0;
		defendantDetailsPanel.add(parentGuardianLabel, gbc);
		
		parentGuardianText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(parentGuardianText, gbc);
		
		//Solicitor
		JLabel solicitorLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantSolicitor"));
		Insets leftPaddedInsets = new Insets(4,20,4,4);
		gbc.insets = leftPaddedInsets;
		gbc.gridx++;
		gbc.weightx = 0;
		defendantDetailsPanel.add(solicitorLabel, gbc);
		
		solicitorText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(solicitorText, gbc);
		
		//Custody Time Limit
		JLabel custodyTimeLimitLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantCustodyTimeLimit"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		defendantDetailsPanel.add(custodyTimeLimitLabel, gbc);
		
		custodyTimeLimitText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(custodyTimeLimitText, gbc);
		
		
		//Prisoner Number
		JLabel prisonerNumberLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantPrisonerNumber"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		defendantDetailsPanel.add(prisonerNumberLabel, gbc);
		
		prisonerNumberText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(prisonerNumberText, gbc);
		
		//B/C Status
		JLabel bcStatusLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantBCStatusLimit"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		defendantDetailsPanel.add(bcStatusLabel, gbc);
		
		bcStatusText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(bcStatusText, gbc);

		//Prison Id
		JLabel prisonIdLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantPrisonId"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		defendantDetailsPanel.add(prisonIdLabel, gbc);
		
		prisonIdText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(prisonIdText, gbc);
		
		//URN
		JLabel URNLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantURN"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		defendantDetailsPanel.add(URNLabel, gbc);
		
		urnText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(urnText, gbc);

		//Date of Birth
		JLabel dateOfBirthLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantDateOfBirth"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		defendantDetailsPanel.add(dateOfBirthLabel, gbc);
		
		dateOfBirthText = new DisplayOnlyField();
		gbc.insets = XHIBITConstant.nonContainerInsets;
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(dateOfBirthText, gbc);
		
		//Results Authorised
		JLabel AuthorisedLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantResultsAuthorised"));
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 0;
		defendantDetailsPanel.add(AuthorisedLabel, gbc);
		
		authorisedText = new DisplayOnlyField();
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(authorisedText, gbc);

		//In Custody
		JLabel inCustodyLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantInCustody"));
		gbc.gridx++;
		gbc.insets = leftPaddedInsets;
		gbc.weightx = 0;
		defendantDetailsPanel.add(inCustodyLabel, gbc);
		
		inCustodyCheckBox = new JCheckBox(); 
		inCustodyCheckBox.setEnabled(false);
		gbc.gridx++;		
		gbc.weightx = 0.5;
		defendantDetailsPanel.add(inCustodyCheckBox, gbc);

		//Address
		JLabel addressLabel = new JLabel(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources, "defendantAddress"));
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		defendantDetailsPanel.add(addressLabel, gbc);
		gbc.anchor = GridBagConstraints.CENTER;
		
		addressText = new XTextArea();
		addressText.setRows(4);
		addressText.setLineWrap(true);
		addressText.setWrapStyleWord(true);
		addressText.setEditable(false);
		addressText.setFocusable(false);
		JScrollPane addressScrollpane = new JScrollPane(addressText);
		addressScrollpane.setPreferredSize(addressText.getPreferredSize());
		gbc.gridx++;		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		defendantDetailsPanel.add(addressScrollpane, gbc);
		
		//Section 28 Advocates
		gbc.gridy++;
		gbc.gridx = 0;
		JPanel section28AdvocatesPanel = initSection28AdvocatesPanel();
		defendantDetailsPanel.add(section28AdvocatesPanel, gbc);
		
		//Put Back
		gbc.gridy++;
		JPanel putPackPanel = initPutBackPanel();
		defendantDetailsPanel.add(putPackPanel, gbc);
		return defendantDetailsPanel;
		
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
	
	@Override
	protected void moveModelToScreen() {
		this.caseTitleText.setText(model.getCase().getCaseTitle());
		CaseSummaryDefendantsTableModel tableModel =  (CaseSummaryDefendantsTableModel) this.allDefendantsTable.getModel();
		tableModel.setTableSource((ArrayList<DefendantValue>)this.model.getCaseListingEntry().getDefendants());
	}
	
	@SuppressWarnings("unchecked")
	protected void handleSelectionEvent(ListSelectionEvent e) {
		CaseSummaryDefendantsTableModel tableModel = (CaseSummaryDefendantsTableModel)allDefendantsTable.getModel();
		DefendantValue selectedDefendant = null;
		for(int index = e.getFirstIndex(); index<=e.getLastIndex();index++)		{
			if(((ListSelectionModel) e.getSource()).isSelectedIndex(index)){
				selectedDefendant = tableModel.getDefendant(index);
				break;
			}
		}

		if (selectedDefendant != null) {
			try {
				DefendantValue fullSelectedDefendant = (DefendantValue) XhibitDelegateHelper.getDefendantDelegate().getFullDefendantDetails(selectedDefendant.getDefendantID(), model.getCaseId());
				
				this.inCustodyCheckBox.setSelected("Y".equals(fullSelectedDefendant.getCurrentPrisonStatus()));
				this.parentGuardianText.setText(fullSelectedDefendant.getParentGuardianName());
				
				if ( fullSelectedDefendant.getPrisonId() != null ) {
					String prisonName = "";
					RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
					criteria.setCodeType(RefSystemCodeCriteria.CodeType.PRISON_ID);
					criteria.setCode(fullSelectedDefendant.getPrisonId());
					criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
					ArrayList<RefSystemCodeBasicValue> prisonList = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
					if(prisonList != null && prisonList.size() > 0){
						prisonName = " - " + prisonList.get(0).getDecode();
					}
					this.prisonIdText.setText(fullSelectedDefendant.getPrisonId() + prisonName);
				}
				
				this.urnText.setText(fullSelectedDefendant.getPtiurn());
				Date dateOfBirth = fullSelectedDefendant.getDateOfBirth() != null ? fullSelectedDefendant.getDateOfBirth().getTime() : null;
				this.dateOfBirthText.setText(getFormattedDate(dateOfBirth));
				
				String resultsAuthorisedCode = "N";
				if ( fullSelectedDefendant.getDefOnCaseBasicValue().getResultsVerified() != null ) {
					resultsAuthorisedCode = fullSelectedDefendant.getDefOnCaseBasicValue().getResultsVerified();
				}
				this.authorisedText.setText( ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "results.authorise.status."+resultsAuthorisedCode) );
				
				this.addressText.setText(null);
				if(fullSelectedDefendant.getAddressValue() != null){
					this.addressText.setText(fullSelectedDefendant.getAddressValue().getHumanReadableAddressString());
					this.addressText.setCaretPosition(0);
				}
				
				if ( fullSelectedDefendant.getDefOnCaseBasicValue().getCurrentBcStatus() != null ) {
					RefSystemCodeCriteria bailTypeCriteria = new RefSystemCodeCriteria();
					bailTypeCriteria.setCodeType(RefSystemCodeCriteria.CodeType.BAIL_TYPE);
					bailTypeCriteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
					bailTypeCriteria.setCode(fullSelectedDefendant.getDefOnCaseBasicValue().getCurrentBcStatus());
					ArrayList<RefSystemCodeBasicValue> bailType = (ArrayList<RefSystemCodeBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(bailTypeCriteria);
					this.bcStatusText.setText(null);
					if(bailType != null && bailType.size() > 0){
						this.bcStatusText.setText(bailType.get(0).getDecode());
					}
				}
				
				this.prisonerNumberText.setText(null);
				if(fullSelectedDefendant.getPrisonerNo() != null){
					this.prisonerNumberText.setText(fullSelectedDefendant.getPrisonerNo().getReferenceValue());
				}
				
				if (fullSelectedDefendant.getDefOnCaseBasicValue().getCustodyTimeLimit() != null ) {
					String formattedCustodyDate = XDateFormat.format(fullSelectedDefendant.getDefOnCaseBasicValue().getCustodyTimeLimit(), XDateFormat.DATEFORMAT);
					this.custodyTimeLimitText.setText(formattedCustodyDate);			
				}	
				else{
					this.custodyTimeLimitText.setText(XHIBITConstant.getResource(XhibitBundles.CaseSummaryResources,"defendantNoCustodyTimeLimit"));
				}
				
				DefendantSolicitorFirm solicitorFirm = new DefendantSolicitorFirm(fullSelectedDefendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
				this.solicitorText.setText(solicitorFirm.getSolicitorText());
				
				this.firstAdvocateText.setText(fullSelectedDefendant.getDefOnCaseBasicValue().getSection28Name1());
				this.firstPhoneText.setText(fullSelectedDefendant.getDefOnCaseBasicValue().getSection28Phone1());
				this.secondAdvocateText.setText(fullSelectedDefendant.getDefOnCaseBasicValue().getSection28Name2());
				this.secondPhoneText.setText(fullSelectedDefendant.getDefOnCaseBasicValue().getSection28Phone2());
				
				PutBack putBack = new PutBack(fullSelectedDefendant.getDefOnCaseBasicValue().getDefendantOnCaseId());
				this.putBackText.setText(putBack.getPutBackText());
				this.toText.setText(putBack.getToText());
						
						
				this.repaint();
			} catch (DefendantControllerException ex) {
				Message newUserMessage = new Message("sysadmin.bisref.finderexception");
				throw new CSUnrecoverableException(newUserMessage, ex, "Error whilst retreiving Defendant details");
			} catch (FinderException e1) {
				Message newUserMessage = new Message("sysadmin.bisref.finderexception");
				throw new CSUnrecoverableException(newUserMessage, e1, "Error whilst retreiving Solicitor details");
			} catch (BisRefControllerException e1) {
				Message newUserMessage = new Message("sysadmin.bisref.finderexception");
				throw new CSUnrecoverableException(newUserMessage, e1, "Error whilst retreiving Solicitor details");
			}
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

