package uk.gov.courtservice.xhibit.client.actions.results.CFIX;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayCFIXReportAction;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunCFIXReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the CFIX report
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */


public class RunCFIXReportDialog extends XDialog {
	 private static final long serialVersionUID = 1L;

	    private static final String CFIX_REPORT = XhibitBundles.XhibitAdminResources;
	    
	    private CFIXReportPanel panel;
	    
	    public RunCFIXReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
	        super(xac,ResourceBundleHelper.getResource(CFIX_REPORT,"CFIXReport.dialog_title"),
	            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
	    
	        //Create new panel
	        panel = new CFIXReportPanel(this);
	        prepareButtons();
	        addBodyPanel(panel);
	        makeFocusable();
	    
	        pack();
	        
	        //disable OK button after pack() so it stays disabled
	        getButtonPanel().okButton.setEnabled(false);
	    }

		private void makeFocusable(){
			this.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e)
				{
					RunCFIXReportDialog.this.requestFocusInWindow();
				}
			});
		}
	    
	    private void prepareButtons(){
	        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
	        //OK Button 
	        buttonPanel.okButton.setAction(new DisplayCFIXReportAction(panel.getStartDateField(),panel.getEndDateField(), getParentFrame()));
	        //Cancel Button
	        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
	    }
		
	}


