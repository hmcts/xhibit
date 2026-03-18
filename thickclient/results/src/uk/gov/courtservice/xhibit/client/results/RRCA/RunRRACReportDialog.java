package uk.gov.courtservice.xhibit.client.results.RRCA;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: RunRRCAReportDialog
 * </p>
 * <p>
 * Description: The dialog which is displayed to run the RRCA report
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

public class RunRRACReportDialog extends XDialog {

	 private static final long serialVersionUID = 1L;

	    private static final String RRCA_REPORT = XhibitBundles.XhibitAdminResources;
	    
	    private RRCAReportPanel panel;
	    private MonthYearDatePeriodReportModel model;
	    
	    
	    public RunRRACReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
	        super(xac,ResourceBundleHelper.getResource(RRCA_REPORT,"RRCAReport.dialog_title"),
	            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
	    
	        //Create new panel
	        model = new MonthYearDatePeriodReportModel(xac);
	        panel = new RRCAReportPanel(this,model);
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
					RunRRACReportDialog.this.requestFocusInWindow();
				}
			});
		}
	    
	    private void prepareButtons(){
	        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
	      
	        
	        //Cancel Button
	        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
	    }
}
