package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Court Details Model
 * </p>
 * <p>
 * Description: Models the data on the court details tab of the import/export
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
public class CourtDetailsModel implements Cloneable {
    // log directly from this class rather than XHIBITConstants
    private static Logger log = Logger.getLogger(CourtDetailsModel.class.getName());

    private Collection courtDetails;

    private Collection iENTableRowModels;

    private XhibitApplicationController xac;

    public CourtDetailsModel() {
    }

    // Getters
    public Collection getCourtDetails() {
        return courtDetails;
    }

    public XhibitApplicationController getXac() {
        return xac;
    }

    public Collection getIENTableRowModels() {
        return iENTableRowModels;
    }

    // Setters
    public void setCourtDetails(Collection param) {
        courtDetails = param;
    }

    public void setXac(XhibitApplicationController param) {
        xac = param;
    }

    public void setIENTableRowModels(Collection param) {
        iENTableRowModels = param;
    }

    // Utilities
    public void printModel() {
        log.debug("CourtDetailsModel");
        log.debug("-----------------");
        log.debug("XAC                 : " + getXac());
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
        setXac(null);
        setCourtDetails(null);
        setIENTableRowModels(null);
    }
}