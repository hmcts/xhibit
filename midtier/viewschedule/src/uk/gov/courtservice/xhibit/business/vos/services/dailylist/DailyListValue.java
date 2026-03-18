package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DailyListValue
 * </p>
 * <p>
 * Description: Value for the Daily list. This is the base for the daily list,
 * it is the top level value for the daily list component.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class DailyListValue extends CSAbstractValue {
    /**
     * This is a collection of CourtValues. There is a value per court site.
     * Each court site contains their own set of ScheduledValues.
     */
    private ArrayList courtSiteValues;

    // The list value - data related for the entire list
    private ListValue listValue;

    // The list value - data related for the entire list
    private DocumentValue documentValue;

    // The court the list is for. The court may or may not have court sites.
    private CourtValue courtValue;
    
    private static final long serialVersionUID =-8984054669804730121L;

    public DailyListValue() {
    }

    public DocumentValue getDocumentValue() {
        return documentValue;
    }

    public void setDocumentValue(DocumentValue documentValue) {
        this.documentValue = documentValue;
    }

    public CourtValue getCourtValue() {
        return courtValue;
    }

    public ListValue getListValue() {
        return listValue;
    }

    public void setCourtValue(CourtValue courtValue) {
        this.courtValue = courtValue;
    }

    public void setListValue(ListValue listValue) {
        this.listValue = listValue;
    }

    public ArrayList getCourtSiteValues() {
        if (courtSiteValues == null) {
            courtSiteValues = new ArrayList();
        }
        return courtSiteValues;
    }

    public void setCourtSiteValues(ArrayList courtSiteValues) {
        this.courtSiteValues = courtSiteValues;
    }
}