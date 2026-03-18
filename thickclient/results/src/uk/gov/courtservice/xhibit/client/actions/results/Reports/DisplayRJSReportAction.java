package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.framework.services.conversion.XDateFormat;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RJSReport;

public class DisplayRJSReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	private Frame parentFrame = null;
	private MonthYearDatePeriodReportModel model;
	
	 public DisplayRJSReportAction(Frame parentFrame, MonthYearDatePeriodReportModel model){
        populateFromBundle("btnOk");
        this.parentFrame = parentFrame;  
        this.model = model;
    }

	@Override
	public void xActionPerformed(ActionEvent e) throws Exception {
		final Date sittingDate = getMaximumDayDate();
		
		//Call Midtier to get result set
        RJSReport reportList = XhibitDelegateHelper.getResults2Delegate().getRJSReport(XhibitSingleton.getInstance().getCourtId(), sittingDate);
        reportList.setSittingDate(XDateFormat.format(sittingDate,XDateFormat.DATEFORMAT));
        if(!reportList.getRjsJudgeTypeValues().isEmpty())
        {         
        	DisplayActionReportHelper helper = new DisplayActionReportHelper();
 	        helper.setupReportDefaults(reportList);
    		
 	        PreviewReportAction previewReportAction = new PreviewReportAction(reportList,"config/xsl/results/reports/rjs/printRJSReport.xsl","RJSReportPreview");
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
	
	private Date getMaximumDayDate() throws Exception {
		final DateFormat df = DateFormat.getDateInstance();
		
		Date date = df.parse("01-" + model.getMonthPeriod() +"-" + model.getYearPeriod());
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String lastDay = String.valueOf(cal.getActualMaximum(Calendar.DATE)) + "-";
		
		return df.parse(lastDay + model.getMonthPeriod() + "-" + model.getYearPeriod());
	}
}
