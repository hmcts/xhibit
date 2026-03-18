package uk.gov.courtservice.xhibit.client.results.RRCA;

import java.awt.event.ActionEvent;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayRRCAReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: RRCAReportPanel
 * </p>
 * <p>
 * Description: The panel which displays RRCA report
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
public class RRCAReportPanel extends MonthYearDatePeriodReportPanel {
	
	private static final long serialVersionUID = 1L;
	
    public RRCAReportPanel(XDialog parent, MonthYearDatePeriodReportModel model) throws CSRecoverableException{
    	super(parent, model);
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if(update){
			DisplayRRCAReportAction xaction = new DisplayRRCAReportAction(parent.getParentFrame(),model); 
			xaction.actionPerformed(new ActionEvent(model.getXac(),0,"call DisplayRRCAReportAction "));
			// Halt the exit process
			throw new UserCancelException();
		}
	}

	@Override
	protected String getSelectMonthLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"RRCAReport.panel_label_Select_Month_Label");
	}

	@Override
	protected String getPreviewLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "RRCAReport.panel_label_PreviewLabel");
	}


	@Override
	protected String[]getYears(){
		String[]years = new String[100];
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int thisMonth = cal.get(Calendar.MONTH);
		int thisYear = thisMonth==0?year-1:year;
		for (int i = 0;i < years.length;i++)
			years[i]=Integer.toString(thisYear -i);
		return years;
	}
}
		