package uk.gov.courtservice.xhibit.client.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.CaseHelper;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
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

public class ApplicationCaseModel {
    private Integer scheduledHearingId;

    private Integer caseId;

    private Integer caseNumber;

    private String caseType;

    private String caseSubType;
    
    private String caseTitle; // used for B cases

    private Timestamp scheduledHearingDateFrom;

    private Timestamp scheduledHearingDateTo;

    private boolean inEditMode;

    private ScheduledHearingValue scheduledHearingValue;

    private XhibitApplicationController xhibitApplicationController;

    private Collection listShv = null;

    private boolean isLinked;
    
    private boolean isMigrated;

    private Vector linkedGroup;

    private boolean forAllDaysLogs;
    
    private boolean forRangeLogs;
    
    //This hashmap holds lists of sequence numbers for each defendant on the case. It was moved from chargesControllerModel
    private HashMap<Integer,List> defOnCaseSeqNosMap;

    public ApplicationCaseModel() {
    }

    // Getters
    public Integer getScheduledHearingId() {
        return scheduledHearingId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public Timestamp getScheduledHearingDateFrom() {
        return scheduledHearingDateFrom;
    }

    public Timestamp getScheduledHearingDateTo() {
        return scheduledHearingDateTo;
    }

    public boolean isInEditMode() {
        return inEditMode;
    }

    /**
     * Overriden method that checks if the case was open in update mode and if
     * the user has access to the functional area. This is required when the
     * functional area is accessed independantly of an action.
     * 
     * @param function
     * @return
     */
    public boolean isInEditMode(FunctionList function) {
        return inEditMode && (FunctionList.hasAccess(function));
    }

    public ScheduledHearingValue getScheduledHearingValue() {
        return scheduledHearingValue;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public String getCaseType() {
        return caseType;
    }

    public String getCaseSubType() {
        return caseSubType;
    }

    /**
     * This returns the case type + case number
     * 
     * @return
     */
    public String getDisplayCaseNumber() {
        StringBuffer caseNumber = new StringBuffer();
        caseNumber.append(getCaseType());
        caseNumber.append(getCaseNumber());
        return caseNumber.toString();
    }

    public boolean isLinked() {
        return isLinked;
    }

    public boolean isFloating() {
        if (getScheduledHearingValue() != null) {
            if (getScheduledHearingValue().getIsFloating() != null) {
                return getScheduledHearingValue().getIsFloating().booleanValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public XhibitApplicationController getXhibitApplicationController() {
        return xhibitApplicationController;
    }

    /**
     * Returns a list of all the scheduled hearing values for the case.
     * 
     * @return
     * @throws CSRecoverableException
     */
    public Collection getAllScheduledHearingForCase(boolean useListShvInCache) throws CSRecoverableException {
    	if (useListShvInCache) {
	        if (listShv == null || listShv.size() == 0) {
	            if (getCaseId() != null) {
	                CaseHelper ch = new CaseHelper();
	                listShv = ch.getScheduledHearings(getCaseId());
	            } else {
	                return null;
	            }
	        }
    	} else { // Get all hearings again from scratch
    		if (getCaseId() != null) {
                CaseHelper ch = new CaseHelper();
                listShv = ch.getScheduledHearings(getCaseId());
            } else {
                return null;
            }
    	}
        return listShv;
    }
    
    /**
     * Returns a list of all the scheduled hearing values for the case for the specified range of dates.
     * 
     * @return
     * @throws CSRecoverableException
     */
    public Collection getScheduledHearingRangeForCase(Date fromDate, Date toDate) throws CSRecoverableException {
        if (getCaseId() != null) {
            CaseHelper ch = new CaseHelper();
            listShv = ch.getScheduledHearings(getCaseId(), fromDate, toDate);
        } else {
            return null;
        }
        return listShv;
    }

    /**
     * Return a distinct collection of Hearing Dates
     * 
     * @return
     * @throws CSRecoverableException
     */
    public Collection getAllScheduledHearingDatesForCase(boolean useListShvInCache) throws CSRecoverableException {
        CaseHelper ch = new CaseHelper();
        ArrayList distinctHearingDates = new ArrayList();
        Collection hearingDates = ch.getScheduledHearingDates(getAllScheduledHearingForCase(useListShvInCache));
        Iterator iter = hearingDates.iterator();
        while (iter.hasNext()) {
            Date item = (Date) iter.next();
            if (!distinctHearingDates.contains(item)) {
                distinctHearingDates.add(item);
            }
        }
        // sort the ArrayList
        Collections.sort(distinctHearingDates);
        return distinctHearingDates; // return the sorted collection of Hearing Dates
    }

    /**
     * Return the first Schedule Hearing for the given date
     * 
     * @param selectedDate
     * @return Scheduled Hearing Value
     * @throws CSRecoverableException
     */
    public ScheduledHearingValue getShvForDate(Date selectedDate) throws CSRecoverableException {
        String formatDate = XDateFormat.format(selectedDate, XDateFormat.DATEFORMAT);
        Iterator iter = getAllScheduledHearingForCase(true).iterator();
        while (iter.hasNext()) {
            uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue item = (uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue) iter
                    .next();
            if (XDateFormat.format(item.getScheduledHearingDate(), XDateFormat.DATEFORMAT).equals(formatDate)) {
                ScheduledHearingValue[] ts_Shv = getDelegate().getScheduledHearings(
                        new Integer[] { item.getScheduledHearingID() });
                if (ts_Shv != null && ts_Shv.length > 0) {
                    return ts_Shv[0];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private HearingScheduleControllerBeanBusinessDelegate getDelegate() {
        return XhibitDelegateHelper.getHearingDelegate();
    }

    public boolean isForAllDaysLogs() {
        return forAllDaysLogs;
    }
    
    public boolean isForRangeLogs() {
        return forRangeLogs;
    }

    // Setters
    public void setScheduledHearingId(Integer param) {
        this.scheduledHearingId = param;
    }

    public void setCaseId(Integer param) {
        this.caseId = param;
    }

    public void setScheduledHearingDateFrom(Date param) {
        if (param == null) {
            this.scheduledHearingDateFrom = null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(param);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            this.scheduledHearingDateFrom = new Timestamp(cal.getTime().getTime());
        }
    }

    public void setScheduledHearingDateTo(Date param) {
        if (param == null) {
            this.scheduledHearingDateFrom = null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(param);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);

            this.scheduledHearingDateTo = new Timestamp(cal.getTime().getTime());
        }
    }

    public void setScheduledHearingDateFrom(Calendar param) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(param.getTime());
        if (param == null) {
            this.scheduledHearingDateFrom = null;
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);

            this.scheduledHearingDateFrom = new Timestamp(cal.getTime().getTime());
        }
    }

    public void setScheduledHearingDateTo(Calendar param) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(param.getTime());
        if (param == null) {
            this.scheduledHearingDateFrom = null;
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);

            this.scheduledHearingDateTo = new Timestamp(cal.getTime().getTime());
        }
    }

    public void setInEditMode(boolean param) {
        this.inEditMode = param;
    }

    public void setScheduledHearingValue(
            uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue param) {
        clearmodel();
        this.scheduledHearingValue = param;
        setCaseId(getScheduledHearingValue().getCaseBasicValue().getId());
        setCaseType(getScheduledHearingValue().getCaseBasicValue().getCaseType());
        setCaseSubType(getScheduledHearingValue().getCaseBasicValue().getCaseSubType());
        setCaseNumber(getScheduledHearingValue().getCaseBasicValue().getCaseNumber());
        setCaseTitle(getScheduledHearingValue().getCaseBasicValue().getCaseTitle());
        // setScheduledHearingDateFrom( getScheduledHearingValue(
        // ).getNotBeforeTime( ) );
        // setScheduledHearingDateTo( getScheduledHearingValue(
        // ).getNotBeforeTime( ) );
        // Changed to use hearing list date
        setScheduledHearingDateFrom(getScheduledHearingValue().getHearingListStartDate());
        setScheduledHearingDateTo(getScheduledHearingValue().getHearingListStartDate());
        setScheduledHearingId(getScheduledHearingValue().getScheduledHearingId());
        setIsLinked(param.getScheduledHearingBasicValue().getLinkedSHID() == null
                || param.getScheduledHearingBasicValue().getLinkedSHID().intValue() <= 0 ? false : true);
    }

    public void setCaseNumber(Integer param) {
        this.caseNumber = param;
    }

    public void setCaseType(String param) {
        this.caseType = param;
    }

    public void setCaseSubType(String param) {
        this.caseSubType = param;
    }

    public void setIsLinked(boolean newValue) {
        isLinked = newValue;
    }

    public void setXhibitApplicationController(XhibitApplicationController param) {
        this.xhibitApplicationController = param;
    }

    public void setForAllDaysLogs(boolean param) {
        forAllDaysLogs = param;
    }
    
    public void setForRangeLogs(boolean param) {
        forRangeLogs = param;
    }
    
    public void setDefOnCaseSeqNosMap(HashMap<Integer,List> defOnCaseSeqNosMap){
        this.defOnCaseSeqNosMap = defOnCaseSeqNosMap;
    }
    
    public HashMap<Integer,List> getDefOnCaseSeqNosMap(){
        return defOnCaseSeqNosMap;
    }    

    // Utility methods
    public void printModel() {
        XHIBITConstant.info("ApplicationCaseModel");
        XHIBITConstant.info("--------------------");
        XHIBITConstant.info("In Edit Mode?           : " + isInEditMode());
        XHIBITConstant.info("ScheduledHearingID      : " + getScheduledHearingId());
        XHIBITConstant.info("ScheduledHearingDateFrom: " + getScheduledHearingDateFrom());
        XHIBITConstant.info("ScheduledHearingDateTo  : " + getScheduledHearingDateTo());
        XHIBITConstant.info("CaseID                  : " + getCaseId());
        XHIBITConstant.info("CaseType                : " + getCaseType());
        XHIBITConstant.info("CaseSubType             : " + getCaseSubType());
        XHIBITConstant.info("CaseNumber              : " + getCaseNumber());
        XHIBITConstant.info("Linked?                 : " + isLinked());
        XHIBITConstant.info("For All Days Logs?      : " + isForAllDaysLogs());
        XHIBITConstant.info("For Range Logs?         : " + isForRangeLogs());
    }

    /**
     * Call this method if you want the scheduled hearing value refreshed from
     * the database.
     */
    public void refresh() throws HearingScheduleException {
        boolean editMode = isInEditMode();
        ScheduledHearingValue shv = XhibitDelegateHelper.getHearingDelegate().getScheduledHearings(
                new Integer[] { this.getScheduledHearingId() })[0];
        this.setScheduledHearingValue(shv);
        setInEditMode(editMode);
    }

    public Object clone() {
        ApplicationCaseModel copy = new ApplicationCaseModel();

        copy.setCaseId(this.getCaseId());
        copy.setInEditMode(this.isInEditMode());
        copy.setScheduledHearingDateFrom(this.getScheduledHearingDateFrom());
        copy.setScheduledHearingDateTo(this.getScheduledHearingDateTo());
        copy.setScheduledHearingId(this.getScheduledHearingId());
        copy.setCaseNumber(this.getCaseNumber());
        copy.setCaseType(this.getCaseType());
        copy.setCaseSubType(this.getCaseSubType());
        copy.setCaseTitle(this.getCaseTitle());
        copy.setScheduledHearingValue(this.getScheduledHearingValue());
        copy.setXhibitApplicationController(this.getXhibitApplicationController());
        copy.setForAllDaysLogs(this.isForAllDaysLogs());
        copy.setForRangeLogs(this.isForRangeLogs());
        copy.setDefOnCaseSeqNosMap(this.getDefOnCaseSeqNosMap());
        
        return copy;
    }

    public void clearmodel() {
        setCaseId(null);
        setInEditMode(false);
        scheduledHearingDateFrom = null;
        scheduledHearingDateTo = null;
        // setScheduledHearingDateFrom( null );
        // setScheduledHearingDateTo( null );
        setScheduledHearingId(null);
        setCaseNumber(null);
        setCaseType(null);
        setCaseTitle(null);
        setCaseSubType(null);
        scheduledHearingValue = null;
        // setScheduledHearingValue( null );
        // setXhibitApplicationController( null );
        listShv = null;
        setForAllDaysLogs(false);
        setForRangeLogs(false);
        defOnCaseSeqNosMap = null;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }
}
