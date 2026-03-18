package uk.gov.courtservice.xhibit.client.actions.results.Reports;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;

import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.results.DOCAR.PreviewReportAction;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.RRCAReport;

public class DisplayRRCAReportAction extends XAction implements Action {
	private static final long serialVersionUID = 1L;
	    
    private Frame parentFrame = null;
    private MonthYearDatePeriodReportModel model;
    
    public DisplayRRCAReportAction(Frame parentFrame,MonthYearDatePeriodReportModel model){
        populateFromBundle("btnOk");
        this.parentFrame = parentFrame;
        this.model = model;
                       
    }
    
	@Override
    public void xActionPerformed(ActionEvent e) throws Exception {
		final DateFormat df = DateFormat.getDateInstance();
		//Parse the date period. Just use the 1st of the month.
		final Date monthlyDate = df.parse("01-" + model.getMonthPeriod() +"-" + model.getYearPeriod());
		
		RRCAReport rracReport1 = 
				XhibitDelegateHelper.getResults2Delegate().getRRCASummaryReport(XhibitSingleton.getInstance().getCourtId(),monthlyDate);
		
		RRCAReport rracReport2 = 
				XhibitDelegateHelper.getResults2Delegate().getRRCADetailReport(rracReport1,XhibitSingleton.getInstance().getCourtId(),monthlyDate);

		if (rracReport2.getRrcaSummaryDetail() != null &&  rracReport2.getRrcaSummaryDetail().size() > 0) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(monthlyDate);
			int lastDate = cal.getActualMaximum(Calendar.DATE);
			
			DisplayActionReportHelper helper = new DisplayActionReportHelper();
   	        helper.setupReportDefaults(rracReport2);
			
			String weekmonthdate = lastDate + " " + model.getMonthPeriod() + " " + model.getYearPeriod();
			
			//Set ending date
			rracReport2.setWeekmonthdate(weekmonthdate);
			
			PreviewReportAction previewReportAction = new PreviewReportAction(rracReport2,"config/xsl/results/reports/rrca/printRRCAReport.xsl","RRCAReportPreview");
			previewReportAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));

		} else {
			String messageBoxTitle = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_title");
			String errorMessage = XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RunReport.error_message" );
			XMessageBox.alert(parentFrame, messageBoxTitle, true, XMessageBox.ICONERROR, 
					errorMessage, XMessageBox.OK_ONLY, XMessageBox.DEFAULTOK);
		}
	}
}
