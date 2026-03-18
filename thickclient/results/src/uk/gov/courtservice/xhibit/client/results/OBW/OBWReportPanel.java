package uk.gov.courtservice.xhibit.client.results.OBW;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XDatePanelWithEvent;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.validation.DateEqualOrBeforeTodayValidator;
import uk.gov.courtservice.xhibit.client.util.validation.DateValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationController;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationControllerFactory;
import uk.gov.courtservice.xhibit.client.util.validation.ValidationListener;

/**
 * <p>
 * Title: OBWReportPanel
 * </p>
 * <p>
 * Description: The panel which displays OBW report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version 1.0
 */
public class OBWReportPanel extends XPanel implements ValidationListener {
    private static final long serialVersionUID = 1L;
    private RunOBWReportDialog parentDialog = null;
    private XDatePanelWithEvent priorToDateField;
    private JLabel warningLabel;
    private JLabel selectDateError = null;
    private List<ValidationController<?>> validationControllers = new ArrayList<ValidationController<?>>();
    private DateValidationController priorToDateValidation = null;
    private boolean invalidEntry = false;
    

	public OBWReportPanel(RunOBWReportDialog dialog) throws CSRecoverableException{
		stepInitialise();
		parentDialog = dialog;
	}
	
    private JLabel getSelectDateError() {
    	if (selectDateError == null) {
    		selectDateError = new JLabel(" ");
    	}
    	return selectDateError;
    }
      
	
	/**
	 * @return the priorToDateField
	 */
	public XDatePanelWithEvent getPriorToDateField() {
        if (priorToDateField == null) {	
        	priorToDateField = new XDatePanelWithEvent(this, null, false, warningLabel,"before") {
        		
        		private static final long serialVersionUID = 1L;
        	
        		@Override
        		protected void fireEvent() {
    				if (priorToDateField.getText().equals("")) {
    					validateOKButton();
    				}
    			}
    		};
        	priorToDateValidation = ValidationControllerFactory.createDateValid(this,
        			priorToDateField, getSelectDateError(),
            		new DateEqualOrBeforeTodayValidator(){
        		@Override
        		public void validate(XDatePanel target, List<String> errors) {
        			if (target.getText() != null && !target.getText().equals("")){
        					super.validate(target, errors);
        			}
        		}
        	});
        	
            validationControllers.add(priorToDateValidation);
        }
        //return selectDatePanel;
		
		return priorToDateField;
	}
	

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(535,80);
		
	}
	
	
	
	@Override
	public void stepInitialise() throws CSRecoverableException {
		this.setLayout(new GridLayout(2,2));
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		warningLabel = new JLabel();
		warningLabel.setForeground(Color.RED);	       
		
		this.add( getPriorToDateField());
		this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,"OBWReport.dialog_text")));
 		this.add(warningLabel);
 		JLabel label1 = new JLabel(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OBWReport.leave_blank"));
 		this.add(label1);
 				
	}
	
	@Override
	public void stepUpdateViewState() throws CSRecoverableException{
		if (parentDialog != null) {
			priorToDateField.validateDate();
            //boolean enable = !priorToDateField.hasError();
            //((OkCancelPanel) parentDialog.getButtonPanel()).okButton.setEnabled(enable);
            validateOKButton();
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

	@Override
	public void validationUpdatedView(ValidationController<?> validationController) {
		invalidEntry = (validationController.hasErrors()
				|| !ValidationControllerFactory.validateComponents(validationControllers));
		
	}
	
    private void validateOKButton() {
    	 boolean enable = !priorToDateField.hasError();
         ((OkCancelPanel) parentDialog.getButtonPanel()).okButton.setEnabled(enable);
    	
    }
}
