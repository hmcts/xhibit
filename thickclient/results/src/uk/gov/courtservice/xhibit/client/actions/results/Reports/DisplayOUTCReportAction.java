package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.results.OUTC.OUTCReportModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.OUTCReport;

public class DisplayOUTCReportAction extends XAction implements Action {
	 private static final long serialVersionUID = 1L;
	    

    private Frame parentFrame = null;
    private OUTCReportModel model;
        
    public DisplayOUTCReportAction(OUTCReportModel model, Frame parentFrame){
        this.parentFrame = parentFrame;
        this.model = model;  
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		String caseType = model.getCaseType() != null ? model.getCaseType(): null;
		String caseClass = model.getCaseClass() != null ? model.getCaseClass():null;
		String bcStatus = model.getBcStatus() != null ? model.getBcStatus() :null;
		String hearingTypeCode = model.getDefaultHearingType() != null ? model.getDefaultHearingType().getHearingTypeCode() : null;
		Integer timeEstFrom = model.getTimeFrom() !=null ? model.getTimeFrom() : null ;
		Integer timeEstTo = model.getTimeTo() !=null ? model.getTimeTo() : null;
		Integer units = model.getTimeUnits() !=null ? model.getTimeUnits() : null;
		Integer refJudgeTypeId = model.getRequiredJudgeType() != null ? model.getRequiredJudgeType().getId() : null;
		String refJudgeTypeDesc = model.getRequiredJudgeType() != null ? model.getRequiredJudgeType().getDecode() : null;
		Integer unitsWeeks = model.getTimeEstWeeks() != null ? model.getTimeEstWeeks() : null;
		String SecureCourtRoom = model.getSecureCourtroom() != null ? model.getSecureCourtroom() : null ;
		String juvenileOnly = model.getJuvenileOnly() != null ? model.getJuvenileOnly() : null ;
		String priorityNotes = model.getPriority() != null ? model.getPriority() : null ;
		String RestrictedNotes = model.getRestricted() != null ? model.getRestricted() : null ;
		String standardNotes = model.getStandard() != null ? model.getStandard() : null ;
		String sortBy = model.getSortBy() != null ? model.getSortBy() : null ;
		
		//Call Midtier to get result set
        OUTCReport outcReport = 
             XhibitDelegateHelper.getResults2Delegate().getOUTCReport( XhibitSingleton.getInstance().getCourtId() 
            		 , caseType
            		 , caseClass
            		 , bcStatus
            		 , hearingTypeCode
            		 , timeEstFrom
            		 , timeEstTo
            		 , units
            		 , refJudgeTypeId
            		 , refJudgeTypeDesc
            		 , unitsWeeks
            		 , SecureCourtRoom
            		 , juvenileOnly
            		 , priorityNotes
            		 , RestrictedNotes
            		 , standardNotes
            		 , sortBy);

            		        
        //Set the court name here as we don't retrieve it from the database
        outcReport.setCourtName(XhibitSingleton.getInstance().getCourtBasicValue().getCourtName());
		
        if(outcReport.getOutstandingCasesValues().size() > 0)
        {
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(outcReport);

        	PreviewReportAction previewReportAction = new PreviewReportAction(outcReport,"config/xsl/results/reports/outc/printOUTCReport.xsl","OUTCReportPreview");
        	previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
         
        }
        else
        {
        	String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
        	String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message");
        	XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
        					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
        }
    }       
}
