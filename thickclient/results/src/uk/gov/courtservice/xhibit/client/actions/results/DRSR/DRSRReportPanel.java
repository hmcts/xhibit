package uk.gov.courtservice.xhibit.client.actions.results.DRSR;


import java.awt.event.ActionEvent;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.results.Reports.DisplayDRSRReportAction;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportModel;
import uk.gov.courtservice.xhibit.client.actions.results.common.MonthYearDatePeriodReportPanel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: DRSRReportPanel
 * </p>
 * <p>
 * Description: The panel which displays DRSR report
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

public class DRSRReportPanel extends MonthYearDatePeriodReportPanel {
	
	private static final long serialVersionUID = 1L;
	
    public DRSRReportPanel(XDialog parent, MonthYearDatePeriodReportModel model) throws CSRecoverableException{
    	super(parent, model);
	}

	@Override
	public void stepDeinitialise(boolean update) throws CSRecoverableException {
		if(update){
			DisplayDRSRReportAction xaction = new DisplayDRSRReportAction(model,parent.getParentFrame()); 
			xaction.actionPerformed(new ActionEvent(model.getXac(),0,"call DisplayDRSRReportAction "));
			// Halt the exit process
			throw new UserCancelException();
		}
	}


	@Override
	protected String getSelectMonthLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
				"DRSRReport.panel_label_Select_Month_Label");
	}

	@Override
	protected String getPreviewLabel() {
		return XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_label_PreviewLabel");
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

