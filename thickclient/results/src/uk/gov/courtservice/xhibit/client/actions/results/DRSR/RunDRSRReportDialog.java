package uk.gov.courtservice.xhibit.client.actions.results.DRSR;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayCFIXReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayDRSRReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunDRSRReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the DRSR report
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


public class RunDRSRReportDialog extends XDialog {
	 private static final long serialVersionUID = 1L;

	    private static final String DRSR_REPORT = XhibitBundles.XhibitAdminResources;
	    
	    private DRSRReportPanel panel;
	    private MonthYearDatePeriodReportModel model;
	    
	    
	    public RunDRSRReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
	        super(xac,ResourceBundleHelper.getResource(DRSR_REPORT,"DRSRReport.dialog_title"),
	            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
	    
	        //Create new panel
	        model = new MonthYearDatePeriodReportModel(xac);
	        panel = new DRSRReportPanel(this,model);
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
					RunDRSRReportDialog.this.requestFocusInWindow();
				}
			});
		}
	    
	    private void prepareButtons(){
	        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
	      
	        
	        //Cancel Button
	        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
	    }
		
	}