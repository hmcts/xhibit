package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

public class GenerateAuthorisePreviewDialog extends XDialog {

	private static final long serialVersionUID = 1L;
	private final Integer selectedDefendantOnCaseId;
	private Timer previewTimer;
	private String caseType;
	private XDialog parent;

	public GenerateAuthorisePreviewDialog(XDialog authorisePanel, String string,  final Integer selectedDefendantOnCaseId, String caseType) throws CSRecoverableException {
		super(authorisePanel, string, true, XDialog.OKCANCEL, XDialog.DEFAULTCANCEL);
		this.selectedDefendantOnCaseId = selectedDefendantOnCaseId;
		this.caseType = caseType;
		this.parent = authorisePanel;
		this.setResizable(false);
		GenerateAuthorisePreviewPanel panel = new GenerateAuthorisePreviewPanel(); 
		prepareButtons();
        addBodyPanel(panel);
        pack();
				
		SynchGeneratePreview action = new SynchGeneratePreview();
        ActionEvent ae = new ActionEvent(this, 0, "OPEN");
        action.actionPerformed(ae);
	}
	
	private void prepareButtons(){
		this.getButtonPanel().okButton.setVisible(false);
		this.setCancelAction(new XAction("btnCancel"){ 
			private static final long serialVersionUID = 1L;
			public void xActionPerformed(ActionEvent e) {
				try {
					XhibitDelegateHelper.getResults2Delegate().cancelRecordSheetPreviewGeneration(selectedDefendantOnCaseId);
					cancelClicked(e);
				} catch (Exception e1) {
					 XHIBITErrorHandler.handleError(e1);
				}
            }
		});
	}

	private class SynchGeneratePreview extends SynchXAction
	{
		
		private static final long serialVersionUID = 1L;
		public static final String PREVIEW_GENERATION_REQUESTED = "R";
		public static final String PREVIEW_GENERATED = "S";
		public static final String PREVIEW_GENERATION_FAILED = "F";
		public static final String PREVIEW_GENERATION_CANCELLED = "C";
		
		@Override
		public void synchActionPerformed(ActionEvent e) throws Exception {
			//Send request
			XhibitDelegateHelper.getResults2Delegate().requestRecordSheetPreviewGeneration(selectedDefendantOnCaseId);
			
			previewTimer = new Timer();
			previewTimer.schedule(new GeneratePreviewTimer(), 1000, 1000);			
		}
		
	    /**
	     * Stops the synchronisation timer.
	     */
	    protected void stopSyncTimer() {
	        if (previewTimer != null) {
	        	previewTimer.cancel();
	        	previewTimer = null;
	        }
	    }
	    
	    private boolean checkPreviewGenerated() throws Exception {
	    		boolean previewGenerated = false;
				String status = XhibitDelegateHelper.getResults2Delegate().getRecordSheetPreviewGenerationStatus(selectedDefendantOnCaseId);
				if(PREVIEW_GENERATION_REQUESTED.equals(status))	{
					//do nothing - Keep checking
					previewGenerated = false;
				}else if (PREVIEW_GENERATED.equals(status))	{
					previewGenerated = true;
				} else if (PREVIEW_GENERATION_FAILED.equals(status)) {
					throw new Exception("Preview Generation Failed");
				}
			return previewGenerated;
		}
		
		private class GeneratePreviewTimer extends TimerTask {
	        public void run() {
	        	
	            try {
	                boolean generated = checkPreviewGenerated();
	                if(generated)
	                {
	                	//get and display xml
	                   	PreviewRecordSheetAction previewRecordSheetAction = new PreviewRecordSheetAction(selectedDefendantOnCaseId, caseType, parent);
	                   	previewRecordSheetAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	                	
	                	stopSyncTimer();
	                	closeDialog();
	                }
	            } catch (Exception ex) {
	                //display error dialog
	            	stopSyncTimer();
	            	String previewFailureTitle = XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.authoriseResults.previewGenerationErrorTitle"); 
	            	String previewFailureErrorMessage = XHIBITConstant.getResource(XhibitBundles.ErrorText, "gui.authoriseResults.previewGenerationError"); 
	            	XMessageBox.alert(getParentFrame(), previewFailureTitle, true, XMessageBox.ICONERROR, 
	            			previewFailureErrorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
	            	closeDialog();
	            }
	        }			
	    }
	}
}
