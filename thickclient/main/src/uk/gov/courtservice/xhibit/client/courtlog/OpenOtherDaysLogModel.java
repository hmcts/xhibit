package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */
public class OpenOtherDaysLogModel extends ApplicationCaseModel {

    public OpenOtherDaysLogModel() {
        super();
    }

    // Fields
    private Date selectedDate;
    
    private Date selectedDateFrom;
    
    private Date selectedDateTo;

    private ArrayList scheduledHearingDates;

    private boolean showAllDaysLogs;
    
    private boolean showRangeLogs;

    // Getters
    public Date getSelectedDate() {
        return selectedDate;
    }
    
    public Date getSelectedDateFrom() {
        return selectedDateFrom;
    }
    
    public Date getSelectedDateTo() {
        return selectedDateTo;
    }

    public ArrayList getScheduledHearingDates() {
        return scheduledHearingDates;
    }

    public boolean isShowAllDaysLogs() {
        return showAllDaysLogs;
    }
    
    public boolean isShowRangeLogs() {
        return showRangeLogs;
    }

    // Setters
    public void setSelectedDate(Date date) {
        selectedDate = date;
    }
    public void setSelectedDateFrom(Date date) {
        selectedDateFrom = date;
    }
    public void setSelectedDateTo(Date date) {
        selectedDateTo = date;
    }

    public void setScheduledHearingDates(ArrayList shDates) {
        scheduledHearingDates = shDates;
    }

    public void setShowAllDaysLogs(boolean param) {
        showAllDaysLogs = param;
    }
    
    public void setShowRangeLogs(boolean param) {
        showRangeLogs = param;
    }

    public void printModel() {
        super.printModel();
        XHIBITConstant.info("OpenOtherDaysLogModel");
        XHIBITConstant.info("---------------------");
        XHIBITConstant.info("Show All Days Logs     : " + isShowAllDaysLogs());
        XHIBITConstant.info("Selected Date          : " + getSelectedDate().toString());
        XHIBITConstant.info("Selected Date From     : " + getSelectedDateFrom().toString());
        XHIBITConstant.info("Selected Date To       : " + getSelectedDateTo().toString());
        XHIBITConstant.info("Scheduled Hearing Dates: ");
        printScheduledHearingDates();
    }

    private void printScheduledHearingDates() {
        Iterator iter = getScheduledHearingDates().iterator();
        while (iter.hasNext()) {
            Date item = (Date) iter.next();
            XHIBITConstant.info("-  " + item.toString());
        }
    }

    public void clearmodel() {
        super.clearmodel();
        setSelectedDate(null);
        setScheduledHearingDates(null);
        setShowAllDaysLogs(false);
    }
}
