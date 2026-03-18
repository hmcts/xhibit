package uk.gov.courtservice.xhibit.client.results.authorise;

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

public class GenerateAuthorisePreviewPanel extends XPanel {
	
	private static final long serialVersionUID = 1L;

	public GenerateAuthorisePreviewPanel() throws CSRecoverableException{
		stepInitialise();
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		this.setSize(new Dimension(250,90));
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				XHIBITConstant.nonContainerInsets, 0, 0);
		JLabel lblInstruction = new JLabel(ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "results.authorise.generatepreviewdialog.text"));
 		this.add(lblInstruction, gbc);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(250,90);
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
