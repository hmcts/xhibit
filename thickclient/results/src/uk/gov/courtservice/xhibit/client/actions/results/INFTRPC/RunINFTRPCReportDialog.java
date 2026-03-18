package uk.gov.courtservice.xhibit.client.actions.results.INFTRPC;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;



	public class RunINFTRPCReportDialog extends XDialog {
		 private static final long serialVersionUID = 1L;

		    private static final String INFTRPC_REPORT = XhibitBundles.XhibitAdminResources;
		    
		    private INFTRPCReportPanel panel;
		    private INFTRPCModel model;
		    
		    public RunINFTRPCReportDialog(XhibitApplicationController xac) throws CSRecoverableException{
		        super(xac,ResourceBundleHelper.getResource(INFTRPC_REPORT,"INFTRPCReport.dialog_title"),
		            true,XDialog.OKCANCEL,XDialog.DEFAULTCANCEL);
		    
		        //Create new panel
		        model = new INFTRPCModel(xac);
		        panel = new INFTRPCReportPanel(this,model);
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
						RunINFTRPCReportDialog.this.requestFocusInWindow();
					}
				});
			}
		    
		    private void prepareButtons(){
		        OkCancelPanel buttonPanel = (OkCancelPanel) getButtonPanel(); 
		      
		        
		        //Cancel Button
		        buttonPanel.getCancelAction().populateFromBundle("btnCancel");
		    }
			
		}