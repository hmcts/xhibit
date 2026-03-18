package uk.gov.courtservice.xhibit.client.results.NTRSF;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTextField;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * <p>
 * Title: NTRSFReportPanel
 * </p>
 * <p>
 * Description: The panel which displays CTLRP report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */
public class NTRSFReportPanel extends XPanel  implements ValidationListener  {
    private static final long serialVersionUID = 1L;
    
    private JLabel caseNumberLabel;
    private JLabel caseTitleLabel;
    private JLabel casePreviewLabel;
	private XTextField caseNumber;
	private XTextField caseTitle;
	private String caseNumberValue;
	private Integer caseId;
	private String caseTitleValue;
	
	public NTRSFReportPanel(RunNTRSFReportDialog runNTRSFReportDialog,Integer caseId,String caseNumberValue,String caseTitleValue) throws CSRecoverableException{
    	this.caseId = caseId;
    	this.caseNumberValue = caseNumberValue;
    	this.caseTitleValue = caseTitleValue;
		stepInitialise();
        jbInit();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(450,150);
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		
	}
	
	public void jbInit() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.REMAINDER, XHIBITConstant.nonContainerInsets, 0, 0);
		
		this.setLayout(new GridBagLayout());
			
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0.1;
		caseNumberLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NTRSFReport.dialog_label1"));
		this.add(caseNumberLabel, gbc);
			
		gbc.gridx++;
		gbc.weightx = 0.9;
		caseNumber = new XTextField();
		caseNumber.setColumns(6);
		caseNumber.setEnabled(false);
		caseNumber.setText(caseNumberValue.toString());
		this.add(caseNumber, gbc);
			
		gbc.gridx = 0; 
		gbc.gridy++;
		gbc.weightx = 0.1;
		caseTitleLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NTRSFReport.dialog_label2"));
		this.add(caseTitleLabel, gbc);
			
		gbc.gridx++;
		gbc.weightx = 0.9;
		caseTitle = new XTextField();
		caseTitle.setColumns(30);
		caseTitle.setEnabled(false);
		caseTitle.setText(caseTitleValue);
		this.add(caseTitle, gbc);
		
		gbc.gridy++;
		JPanel panel = initPreviewPanel();
	    this.add(panel, gbc);
	}
	
	private JPanel initPreviewPanel() {
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		casePreviewLabel = new JLabel(
		XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "NTRSFReport.dialog_label3"));
		panel.add(casePreviewLabel,gbc);
		
		return panel;
	}
	
	@Override
	public void stepUpdateViewState() throws CSRecoverableException{
		
    }

	@Override
	public void stepActivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		// TODO Auto-generated method stub
		
	}

	public String getCaseNumberValue() {
		return caseNumberValue;
	}

	public void setCaseNumberValue(String caseNumberValue) {
		this.caseNumberValue = caseNumberValue;
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseTitleValue() {
		return caseTitleValue;
	}

	public void setCaseTitleValue(String caseTitleValue) {
		this.caseTitleValue = caseTitleValue;
	}
}
