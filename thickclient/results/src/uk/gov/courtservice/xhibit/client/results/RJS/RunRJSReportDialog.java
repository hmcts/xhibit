package uk.gov.courtservice.xhibit.client.results.RJS;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.RJS.RJSReportPanel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearSingleComboReportPanel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class RunRJSReportDialog extends XDialog {

    private static final long serialVersionUID = 1L;
    
    private MonthYearSingleComboReportPanel panel;
    private MonthYearDatePeriodReportModel model;
    
    protected final static String DIALOG_TITLE = "RJSReport.dialog_title";
    
    
    public RunRJSReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
        super(xac,ResourceBundleHelper.getResource(XhibitBundles.XhibitAdminResources,DIALOG_TITLE),
            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
    
        //Create new panel
        model = new MonthYearDatePeriodReportModel(xac);
        panel = new RJSReportPanel(this, model);
        prepareButtons();
        addBodyPanel(panel);
        makeFocusable();
        pack();
    }
    
    
	private void makeFocusable(){
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				RunRJSReportDialog.this.requestFocusInWindow();
			}
		});
	}
	
    private void prepareButtons() throws CSRecoverableException{
    	  OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
    	  
	        //Cancel Button
	        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
    }
    
} 

