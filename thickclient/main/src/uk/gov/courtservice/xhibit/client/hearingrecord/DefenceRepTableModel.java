package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord.HearingRecordConstants;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRCounselValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRSHLegRepValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRStartEndDates;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.client.counselfacilities.InstructedAdvocateHelper;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */
public class DefenceRepTableModel extends XHIBITTableModel {

    private static final long serialVersionUID = 1L;

    public static final int NAME = 0;

    public static final int ID_NUMBER = 1;

    public static final int ADDRESS = 2;

    public static final int ROLE = 3;

    public static final int CATEGORY = 4;

    public static final int ATTENDANCE_DATES = 5;

    public static final int I_OR_S_FLAG = 6;
    
    /** int value to represent the column index of the legal rep id column */
    public static final int LEGAL_REP_ID = 7;
    
    public static final int HR_COUNSEL_VAL = 8;



    //private Vector data = new Vector();
    
    private boolean legallyAided;

    public DefenceRepTableModel(final HearingRecordModel model) {
        super();
        
        String[] columnNames;
        
        if (model.getLegallyAided()) {
            columnNames =
                new String[] { 
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "name"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "idNumber"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "address"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "role"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "category"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "attendanceDates"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "iorsflag") 
            };
        } else {
            columnNames =
                new String[] { 
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "name"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "idNumber"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "address"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "role"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "category"),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "attendanceDates"),
                    };
        }
        
                
        setColumnNames(columnNames);
        setLongValues(columnNames);
        
        this.legallyAided = model.getLegallyAided();
        this.setData(model);
    }

    /**
     * Private helper method, used to convert an array of String parameters into
     * one long String, with a space between each one, unless the parameter is
     * null or zero length
     * 
     * @param params
     *            A <code>String[]</code> of all the parameters we wish to
     *            append together
     * @return A <code>String</code> of the appended input parameters, or a
     *         zero-length <code>String</code> if the passed in parameter is
     *         empty
     * @TODO Move to a helper class, would then require validation for null
     *       input
     */
    private static String arrayToString(final String[] params) {
        // give a default size of 10 for each parameter
        final StringBuffer returnBuffer = new StringBuffer(params.length * 10);

        for (int i = 0; i < params.length; i++) {
            if ((params[i] != null) && (params[i].length() > 0)) {
                // although we should see if we need to append the space,
                // but the previous version always appended the last space!
                returnBuffer.append(params[i]).append(' ');
            }
        }

        return returnBuffer.toString();
    }

    /**
     * Private helper method to get the counsels full name from the passed in
     * counsel value object. This method delegates to the arrayToString method
     * passing in an array of the counsels first name, middle name and surname
     * 
     * @param hrCounselVal
     *            A <code>HRCounselValue</code>
     * @return The counsels full name as a <code>String</code> concatenated
     *         together into one value
     */
    private static String getCounselFullName(final HRCounselValue hrCounselVal) {
        return arrayToString(new String[] { hrCounselVal.getFirstName(), hrCounselVal.getMiddleName(),
                hrCounselVal.getSurname() });
    }

    /**
     * Private helper method to get the full address in one String of the passed
     * in address value object. This method delegates to the arrayToString
     * method passing in an array of all of the required address lines
     * 
     * @param addressVal
     *            A <code>AddressBasicValue</code>
     * @return The full address as a <code>String</code> concatenated together
     *         into one value
     */
    private static String getFullAddress(final AddressBasicValue addressVal) {
        if (addressVal != null) {
            return arrayToString(new String[] { addressVal.getAddress1(), addressVal.getAddress2(),
                    addressVal.getAddress3(), addressVal.getAddress4(), addressVal.getTown(), addressVal.getCounty(),
                    addressVal.getPostcode(), addressVal.getCountry() });
        } else {
            return "";
        }
    }

    /**
     * Returns a String that shows all the dates( from and to ) on which the
     * legal representative attended hearings
     * 
     * @param startEndDates
     * @return String
     */
    private static String buildStartEndDates(final HRStartEndDates[] startEndDates) {
        StringBuffer allDates = new StringBuffer();
        boolean first = true;

        for (int x = 0; x < startEndDates.length; x++) {
            if (first) {
                first = false;
            } else {
                allDates.append("\n");
            }
            allDates.append(XDateFormat.format(startEndDates[x].getStartDate(), XDateFormat.DATEFORMAT));
            allDates.append(" - ");
            allDates.append(XDateFormat.format(startEndDates[x].getEndDate(), XDateFormat.DATEFORMAT));
        }

        return allDates.toString();
    }

    /**
     * Acquire the category for the legal representative. This will attempt to
     * read it from the cache if the passed in cache is not null, otherwise it
     * will attempt toread it from value objects associated with the model.
     * 
     * @param model
     *            The <code>HearingRecordModel</code>
     * @param cache
     *            A <code>HashMap</code> containing the cached data (if
     *            applicable)
     * @param refLegalRepId
     *            An <code>Integer</code> representing the reps id
     * @return A <code>String</code> of the category
     */
    private static String getCategory(final HearingRecordModel model, final HashMap cache, final Integer refLegalRepId) {
        String category = null;

        if ((cache != null) && (cache.containsKey(refLegalRepId))) {
            // read the category from the cache
            category = (String) cache.get(refLegalRepId);
        } else {
            // do not read the category from the cache
            final Vector reps = (Vector) model.getHearingRecordVal().getHearingRecordUpdateValue()
                    .getHrSHLegRepValues();

            for (int j = 0; j < reps.size(); j++) {
                final HRSHLegRepValue legRep = (HRSHLegRepValue) reps.get(j);

                // Ignore any 'In Person' sign-in records.
                // These are characterised by having a null refLegalRepID.
                if (legRep.getRefLegRepID() != null) {
                    if (legRep.getRefLegRepID().equals(refLegalRepId)) {
                        category = legRep.getRefDefenceCategoryDesc();
                        break;
                    }
                }
            }
        }

        // if the category is null, return a new, empty String
        return ((category == null) ? "" : category);
    }

    /**
     * Private helper method to construct a <code>HashMap</code> containing
     * all of the previously displayed categories (based on the rep id). The
     * <code>HashMap</code> is only constructed if the allow cache has been
     * set to <i>true</i> and the previousData <code>Vector</code> is not
     * <i>null</i>, and has some elements inside it.
     * 
     * @param allowCache
     *            A <code>boolean</code> of <i>true</i> if we are to create
     *            the cache
     * @param previousData
     *            A <code>Vector</code> containing the <code>Vector</code>s
     *            of previous results
     * @return A <code>HashMap</code> if one is created, or <i>null</i>
     */
    private static HashMap getPreviousCategories(final boolean allowCache, final Vector previousData) {
        HashMap returnMap = null;

        if (allowCache && (previousData != null) && (previousData.size() > 0)) {
            returnMap = new HashMap();

            for (int i = 0; i < previousData.size(); i++) {
                final Vector item = (Vector) previousData.get(i);

                // previousCategories.put(repId, category);
                returnMap.put(item.get(LEGAL_REP_ID), item.get(CATEGORY));
            }
        }

        return returnMap;
    }

    /**
     * Overloaded setData method, delegates to the method
     * setData(HearingRecordModel, boolean), passing a <i>true</i> value for
     * the booelan
     * 
     * @param model
     *            The <code>HearingRecordModel</code>
     */
    public void setData(final HearingRecordModel model) {
        setData(model, true);
    }

    /**
     * Overloaded setData method, used to populate all of the rows for the table
     * 
     * @param model
     *            The <code>HearingRecordModel</code>
     * @param fullRefresh
     *            A <code>boolean</code> value to indicate if we should do a
     *            full refresh (<i>true</i>) of the data, or if the category
     *            details should attempt to be read from the previous entry on
     *            the screen (<i>false</i>)
     */
    public void setData(HearingRecordModel model, boolean fullRefresh) {
        // construct the hash map of previous categories if required
        final HashMap previousCategories = getPreviousCategories(!fullRefresh, this.data);

        // Remove all elements the data vector had before,then add the new set
        // of data.
        this.data.removeAllElements();

        final Vector defenceRepIds = new Vector();
        final Vector counselVec = (Vector) model.getHearingRecordVal().getHearingRecordDisplayValue()
                .getHrCounselValue();

        if (counselVec != null) {
            for (int i = 0; i < counselVec.size(); i++) {
                final HRCounselValue hrCounselVal = (HRCounselValue) counselVec.get(i);

                if (hrCounselVal.getLegalRole().equals(HearingRecordConstants.LEGAL_ROLE_DEFENCE)) {
                    // Handle 'In-Person' sign-ins.
                    if (hrCounselVal.getSolFirmOrRefLegalRep() != null
                            && (hrCounselVal.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_IN_PERSON) ||
                            	hrCounselVal.getSolFirmOrRefLegalRep().equalsIgnoreCase(CounselFacilitiesHelper.LEGAL_REP_TYPE_NON_ATTENDANCE))) {
                        // These must not be displayed in the defence
                        // representation table
                        // so just ignore the record
                    } else {
                        // This is a real defence advocate so process it.

                        // cache the legal rep id, as used in a couple of places
                        final Integer refLegalRepId = hrCounselVal.getRefLegalRepID();
                        final Vector tableRow = new Vector();

                        // setting vector of leg rep IDs
                        defenceRepIds.add(refLegalRepId);

                        // now populate the table row
                        tableRow.add(getCounselFullName(hrCounselVal));
                        tableRow.add(hrCounselVal.getBarNumber());
                        tableRow.add(getFullAddress(hrCounselVal.getAddressBasicValue()));
                        tableRow.add(hrCounselVal.getLegalRepType());
                        tableRow.add(getCategory(model, previousCategories, refLegalRepId));
                        tableRow.add(buildStartEndDates(hrCounselVal.getStartEndDatesArray()));
                        
                        if (legallyAided) {
                            if (!hrCounselVal.getSolFirmOrRefLegalRep().equals("L")) {
                                // solicitors dont have I/S flag
                                tableRow.add("---");
                            } else {
                                tableRow.add(getIOrSFlag(hrCounselVal));
                            }
                        } else {
                            tableRow.add("---");
                        }

                        // adding the legal rep id here, so that we can refresh
                        // the other
                        // parts, without refreshing this (read from the cache)
                        tableRow.add(hrCounselVal.getRefLegalRepID());
                        
                        tableRow.add(hrCounselVal);


                        data.add(tableRow);
                    }
                }
            }
        }

        model.setDefenceReps(defenceRepIds);
        this.setData(data);
    }
    
    private String getIOrSFlag(HRCounselValue counsel) {
        if (counsel.getInstructedAdvocateRefLegalRepId() != null) {
            // Use the new value - not yet saved to the DB
            if (counsel.getRefLegalRepID().equals(counsel.getInstructedAdvocateRefLegalRepId())) {
                return "I";
            } else {
                return "S";
            }
        } else {
            // Use the old value - read from the DB
            String subInst = counsel.getSubstituteOrInstructed();
            if (subInst == null) {
                return "?";
            } else {
                return subInst;
            }
        }
    }
    
    public void updateIOrSValue(int row) {
        if (row < 0 || row >= data.size()) {
            return;
        }
        
        Vector rowData = (Vector)data.get(row);
        HRCounselValue hrCounselVal = (HRCounselValue)rowData.get(HR_COUNSEL_VAL);
        
        setValueAt(getIOrSFlag(hrCounselVal), row, I_OR_S_FLAG);
    }
    
    public boolean hasEditableIOrSField(int row) {
        if (!legallyAided || row >= data.size()) {
            return false;
        }
        
        Vector rowData = (Vector)data.get(row);
        HRCounselValue hrCounselVal = (HRCounselValue)rowData.get(HR_COUNSEL_VAL);
        
        return 
            hrCounselVal.getSolFirmOrRefLegalRep().equals("L")
            && hrCounselVal.getSubstituteOrInstructed() == null
            && hrCounselVal.getInstructedAdvocateRefLegalRepId() == null;
    }

    private Integer getSubstitutedAdvocateOnNewRec(Vector row) {
        // Record not yet stored in the DB
        
        HRCounselValue counsel = (HRCounselValue)row.get(HR_COUNSEL_VAL);
        if (counsel.getInstructedAdvocateRefLegalRepId() != null) {
            if (!counsel.getRefLegalRepID().equals(counsel.getInstructedAdvocateRefLegalRepId())) {
                return counsel.getInstructedAdvocateRefLegalRepId();
            }
        }

        return null;
    }
    
    private Integer getSubstitutedAdvocateOnOldRec(Vector row) {
        // A record read from the DB
        
        HRCounselValue counsel = (HRCounselValue)row.get(HR_COUNSEL_VAL);
        if (counsel.getInstructedAdvocateRefLegalRepId() == null) {
            String subInst = counsel.getSubstituteOrInstructed();
            if (subInst != null && subInst.equals(InstructedAdvocateHelper.SUBSTITUE_ADVOCATE_FLAG)) {
                return counsel.getSubstitutedRefLegalRepId();
            }
        }

        return null;
    }
    
    private Integer getInstructedAdvocateOnNewRec(Vector row) {
        // Record not yet stored in the DB
        
        HRCounselValue counsel = (HRCounselValue)row.get(HR_COUNSEL_VAL);
        if (counsel.getInstructedAdvocateRefLegalRepId() != null) {
            if (counsel.getRefLegalRepID().equals(counsel.getInstructedAdvocateRefLegalRepId())) {
                return counsel.getInstructedAdvocateRefLegalRepId();
            }
        }

        return null;
    }
    
    private Integer getInstructedAdvocateOnOldRec(Vector row) {
        // A record read from the DB
        
        HRCounselValue counsel = (HRCounselValue)row.get(HR_COUNSEL_VAL);
        if (counsel.getInstructedAdvocateRefLegalRepId() == null) {
            String subInst = counsel.getSubstituteOrInstructed();
            if (subInst != null && subInst.equals(InstructedAdvocateHelper.INSTRUCTED_ADVOCATE_FLAG)) {
                return counsel.getRefLegalRepID();
            }
        }

        return null;
    }
    
    public boolean hasSubstitutedAdvocate(
            Integer subsitutedAdvocateLegalRepID) {
        
        Iterator iter = this.getData().iterator();
        while (iter.hasNext()) {
            Vector row = (Vector)iter.next();

            Integer legalRepId = getSubstitutedAdvocateOnOldRec(row);
            if (legalRepId != null
                    && legalRepId.equals(subsitutedAdvocateLegalRepID)) {
                return true;
            }

            legalRepId = getSubstitutedAdvocateOnNewRec(row);
            if (legalRepId != null
                    && legalRepId.equals(subsitutedAdvocateLegalRepID)) {
                return true;
            }

        }

        return false;
    }
    
    public boolean hasInstructedAdvocate(
            Integer instructedAdvocateLegalRepID) {
        
        Iterator iter = this.getData().iterator();
        while (iter.hasNext()) {
            Vector row = (Vector)iter.next();

            Integer legalRepId = getInstructedAdvocateOnOldRec(row);
            if (legalRepId != null
                    && legalRepId.equals(instructedAdvocateLegalRepID)) {
                return true;
            }

            legalRepId = getInstructedAdvocateOnNewRec(row);
            if (legalRepId != null
                    && legalRepId.equals(instructedAdvocateLegalRepID)) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isCellEditable(int row, int col) {
        if (legallyAided) {
            return (col == CATEGORY || col == I_OR_S_FLAG);
        } else {
            return col == CATEGORY;
        }
    }

    public void setValueAt(Object value, int rowIdx, int colIdx) {
        if ((colIdx < 0) || (colIdx >= this.getColumnCount())) {
            return;
        }

        // If the value has not changed then do nothing.
        if (value != null && value.equals(getValueAt(rowIdx, colIdx))) {
            return;
        }

        final Vector row = (Vector) this.data.get(rowIdx);
        row.setElementAt(value, colIdx);

        fireTableDataChanged();
    }

    public Object getValueAt(int row, int col) {
        if ((row < 0) || row >= (this.data.size()))
            return "";

        final Vector rowData = (Vector) this.data.get(row);
        final Object value = rowData.elementAt(col);

        return ((value == null) ? "" : value.toString());
    }
}