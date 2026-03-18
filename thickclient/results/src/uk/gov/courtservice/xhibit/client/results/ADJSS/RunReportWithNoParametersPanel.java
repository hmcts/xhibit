package uk.gov.courtservice.xhibit.client.results.ADJSS;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: RunReportWithNoParametersPanel
 * </p>
 * <p>
 * Description: The panel which can be used to run a report that requires no input parameters
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author David Burden
 * @version 1.0
 */
public class RunReportWithNoParametersPanel extends XPanel {
    private static final long serialVersionUID = 1L;
    

	public RunReportWithNoParametersPanel() throws CSRecoverableException{
		stepInitialise();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(350,60);
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		this.setLayout(new GridBagLayout());

 		JLabel lblInstruction = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"NoParameterReport.dialog_text"));
 		this.add(lblInstruction, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
	}

	@Override
	public void stepActivate() throws CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepUpdateViewState() throws CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepValidate() throws CSValidationException, CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepDeactivate() throws CSRecoverableException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		// TODO Auto-generated method stub
		
	}
}
