package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.util.Collection;
import java.util.Iterator;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Case Details Model
 * </p>
 * <p>
 * Description: Models the data on the case details tab of the import/export
 * notification screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class CaseDetailsModel implements Cloneable {
    private String caseNumber;

    private Collection caseDetails;

    private XhibitApplicationController xac;

    private Collection iENTableRowModels;

    public CaseDetailsModel() {
    }

    // Getters
    public String getCaseNumber() {
        return caseNumber;
    }

    public Collection getCaseDetails() {
        return caseDetails;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public Collection getIENTableRowModels() {
        return iENTableRowModels;
    }

    // Setters
    public void setCaseNumber(String param) {
        caseNumber = param;
    }

    public void setCaseDetails(Collection param) {
        caseDetails = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setIENTableRowModels(Collection param) {
        iENTableRowModels = param;
    }

    // Utilities
    public void printModel() {
        XHIBITConstant.info("CaseDetailsModel");
        XHIBITConstant.info("----------------");
        XHIBITConstant.info("CaseNumber          : " + getCaseNumber());
        XHIBITConstant.info("XAC                 : " + getXac());
    }

    public void printModel(boolean withDetails) {
        this.printModel();

        if (withDetails) {
            if (getIENTableRowModels() != null) {
                Iterator iter = getIENTableRowModels().iterator();
                while (iter.hasNext()) {
                    ImportExportNotificationTableRowModel item = (ImportExportNotificationTableRowModel) iter.next();

                    item.printModel();
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        setCaseNumber(null);
        setXac(null);
        setCaseDetails(null);
        setIENTableRowModels(null);
    }
}