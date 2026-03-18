package uk.gov.courtservice.xhibit.client.actions.results.RJS;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayRJSReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearSingleComboReportPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class RJSReportPanel extends MonthYearSingleComboReportPanel {
	
	private static final long serialVersionUID = 1L;
	
    public RJSReportPanel(XDialog parent, MonthYearDatePeriodReportModel model) throws CSRecoverableException{
    	super(parent, model);
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if(update){
			DisplayRJSReportAction xaction = new DisplayRJSReportAction(parent.getParentFrame(),model); 
			xaction.actionPerformed(new ActionEvent(model.getXac(),0,"call DisplayRJSReportAction "));
			// Halt the exit process
			throw new UserCancelException();
		}
	}

	@Override
	protected String getSelectMonthLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"RJSReport.panel_label_Select_Month_Label");
	}

	@Override
	protected String getPreviewLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RJSReport.panel_label_PreviewLabel");
	}

	@Override
	public int getNumberOfPreviousMonthsToShowInCalendar() {
		return 12;
	}
	
	@Override
	protected String[] getStringDates() {
		final DateFormat df = new SimpleDateFormat("MMM-yyyy");

		List<String> dateStrings = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.MONTH, -1); // start from last month
		dateStrings.add(df.format(cal.getTime()));
		for (int i = 0; i < getNumberOfPreviousMonthsToShowInCalendar(); i++) {
			cal.add(Calendar.MONTH, -1);
			dateStrings.add(df.format(cal.getTime()));
		}
		return dateStrings.toArray(new String[0]);
	}
}

