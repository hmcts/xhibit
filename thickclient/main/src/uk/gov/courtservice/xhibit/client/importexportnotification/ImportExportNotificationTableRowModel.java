package uk.gov.courtservice.xhibit.client.importexportnotification;

import java.util.Date;

import org.apache.log4j.Logger;

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
 * @author Stephen Tully
 * @version 1.0
 */
public class ImportExportNotificationTableRowModel {
    // log directly from class
    private static Logger log = Logger.getLogger(ImportExportNotificationTableRowModel.class.getName());

    // Data
    private String transaction;

    private String status;

    private String description;

    private String defendantName;

    private Date startDate;

    private Date endDate;

    // Getters
    public String getTransaction() {
        return transaction;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getDefendantName() {
        return defendantName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    // Setters
    public void setTransaction(String param) {
        transaction = param;
    }

    public void setStatus(String param) {
        status = param;
    }

    public void setDescription(String param) {
        description = param;
    }

    public void setDefendantName(String param) {
        defendantName = param;
    }

    public void setStartDate(Date param) {
        startDate = param;
    }

    public void setEndDate(Date param) {
        endDate = param;
    }

    // Utilities
    public void printModel() {
        log.debug("ImportExportNotificationTableRowModel");
        log.debug("-------------------------------------");
        log.debug("Transaction                : " + getTransaction());
        log.debug("Status                     : " + getStatus());
        log.debug("StartDate                  : " + getStartDate());
        log.debug("EndDate                    : " + getEndDate());
        log.debug("Description                : " + getDescription());
        log.debug("DefendantName              : " + getDefendantName());
    }
}
