package uk.gov.courtservice.xhibit.client.courtlog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
public class PrintCourtLogModel extends ApplicationCaseModel {

    // selectedOption constants
    public static final String SELECTEDDAY = "SELECTEDDAY";
    
    public static final String SELECTEDFROM = "SELECTEDFROM";
    
    public static final String SELECTEDTO = "SELECTEDTO";

    public static final String ALL = "ALL";
    
    public boolean printAllCourtLog() {
        return ALL.equals(selectedOption);
    }
    
    public boolean printCourtLogRange() {
        return SELECTEDFROM.equals(selectedOption);
    }
    
    public PrintCourtLogModel() {
        super();
    }

    public PrintCourtLogModel(XhibitApplicationController xac) {
        setXhibitApplicationController(xac);
    }

    // Fields
    private Date selectedDate;
    
    private Date selectedDateFrom;
    
    private Date selectedDateTo;

    private ArrayList scheduledHearingDates;

    private String selectedOption;

    // private ArrayList logRequestedForDates;

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

    public String getSelectedOption() {
        return selectedOption;
    }

    // public ArrayList getlogRequestedForDates( ) { return
    // logRequestedForDates; }

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

    public void setSelectedOption(String param) {
        selectedOption = param;
    }

    // public void setlogRequestedForDates( ArrayList param ) {
    // logRequestedForDates = param; }

    public void printModel() {
        super.printModel();
        XHIBITConstant.info("PrintCourtLogModel");
        XHIBITConstant.info("------------------");
        XHIBITConstant.info("Selected Option        : " + getSelectedOption());
        if (getSelectedDate() != null)
        	XHIBITConstant.info("Selected Date          : " + getSelectedDate().toString());
        if (getSelectedDateFrom() != null)
        	XHIBITConstant.info("Selected Date From     : " + getSelectedDateFrom().toString());
        if (getSelectedDateTo() != null)
        	XHIBITConstant.info("Selected Date To       : " + getSelectedDateTo().toString());
        XHIBITConstant.info("Scheduled Hearing Dates: ");
        printDates(getScheduledHearingDates());
        /*
         * XHIBITConstant.info( "Log Requested For Dates: " ); if
         * (getlogRequestedForDates() != null) { printDates(
         * getlogRequestedForDates( ) ); }
         */
    }

    private void printDates(ArrayList param) {
        Iterator iter = param.iterator();
        while (iter.hasNext()) {
            Date item = (Date) iter.next();
            XHIBITConstant.info("-  " + item.toString());
        }
    }

    public void clearmodel() {
        super.clearmodel();
        setSelectedDate(null);
        setSelectedDateFrom(null);
        setSelectedDateTo(null);
        setSelectedOption(null);
        setScheduledHearingDates(null);
        // setlogRequestedForDates( null );
    }
}
