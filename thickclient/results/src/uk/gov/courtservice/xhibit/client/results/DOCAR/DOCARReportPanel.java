package uk.gov.courtservice.xhibit.client.results.DOCAR;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: DOCARReportPanel
 * </p>
 * <p>
 * Description: The panel which displays DOCAR report
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
public class DOCARReportPanel extends XPanel {
    private static final long serialVersionUID = 1L;
    private RunDOCARReportDialog parentDialog = null;
    private XDatePanel priorToDateField = null;
    

	public DOCARReportPanel(RunDOCARReportDialog runDOCARReportDialog) throws CSRecoverableException{
		stepInitialise();
		parentDialog = runDOCARReportDialog;
	}
	
	/**
	 * @return the priorToDateField
	 */
	public XDatePanel getPriorToDateField() {
		return priorToDateField;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(500,80);
		
	}
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		this.setLayout(new GridLayout(2,2));
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		JLabel warningLabel = new JLabel();
		warningLabel.setForeground(Color.RED);
		
		priorToDateField = new XDatePanel(this, null, true, warningLabel,"before");
                 
 		this.add( priorToDateField);
 	
 		this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"RunDOCARReport.dialog_text")));
 		
 		this.add(warningLabel);
 		
 		this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"RunDOCARReport.dialog_message")));
	}
	
	
	@Override
	public void stepUpdateViewState() throws CSRecoverableException{
		if (parentDialog != null) {
			priorToDateField.validateDate();
            boolean enable = !priorToDateField.hasError();
            ((OkCancelPanel) parentDialog.getButtonPanel()).okButton.setEnabled(enable);
        }
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
}
