package uk.gov.courtservice.xhibit.business.vos.listdistribution;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.entities.xhb_wll_control.XhbWllControlBasicValue;

/**
 * <p>
 * Title: WllControlComplexValue
 * </p>
 * <p>
 * Description: The control complex value.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WllControlComplexValue.java,v 1.2 2005/12/01 15:19:55 bzjrnl
 *          Exp $
 */
public class WllControlComplexValue extends DistributionStatusComplexValue {
    private static final long serialVersionUID = 1L;

    // The list type
    public static final String DOCUMENT_TYPE_WARNED_LIST = "WLD";

    public static final String DOCUMENT_TYPE_FIRM_LIST = "FLD";

    public static final String DOCUMENT_TYPE_DAILY_LIST = "DLD";

    // The list status (xhb_wll_control.status)
    public static final String CONTROL_STATUS_NEW_DOCUMENT = "ND";

    public static final String CONTROL_STATUS_DOCUMENT_AUTHORISED = "DA";

    public static final String CONTROL_STATUS_PROCESSING_DOCUMENT = "PD";

    public static final String CONTROL_STATUS_PROCESSING_COMPLETE = "PC";

    public static final String CONTROL_STATUS_PROCESSING_ERROR = "PE";

    public static final String CONTROL_STATUS_SECOND_PROCESSING = "SD";

    public static final String CONTROL_STATUS_PROCESSING_FAILED = "PF";

    public static final String CONTROL_STATUS_DOCUMENT_DELETED = "XX";

    public static final String CONTROL_STATUS_DOCUMENT_ARCHIVED = "XA";

    // The letter status (xhb_xml_document_status)
    public static final String LETTER_STATUS_DOCUMENT_READY = "DR";

    public static final String LETTER_STATUS_DOCUMENT_EMAILED = "DE";

    public static final String LETTER_STATUS_DOCUMENT_FAXED = "DF";

    public static final String LETTER_STATUS_PRINTING_REQUIRED = "PR";

    public static final String LETTER_STATUS_DOCUMENT_PRINTED = "DP";

    public static final String LETTER_STATUS_PROCESSING_FAILED = "PF";

    public static final String LETTER_STATUS_DOCUMENT_DELETED = "XX";

    public static final String LETTER_STATUS_DOCUMENT_ARCHIVED = "DA";

    // List status/states (combined from control and list)
    public static final String LIST_STATUS_AUTHORIZATION_REQUIRED = "AR";

    public static final String LIST_STATUS_PROCESSING_LIST = "PL";

    public static final String LIST_STATUS_PRINT_REQUIRED = "PR";

    public static final String LIST_STATUS_PROCESING_COMPLETE = "PC";

    public static final String LIST_STATUS_LIST_ERROR = "LE";

    // Control data
    private XhbWllControlBasicValue basicValue;

    // List document data
    private final String documentType;

    private final String documentTitle;

    private final String language;

    private final String country;

    private final Integer majorSchemaVersion;

    private final Integer minorSchemaVersion;

    private final Date dateCreated;

    // Letter summary data
    private final Integer letterReadyCount;

    private final Integer letterFaxedCount;

    private final Integer letterEmailedCount;

    private Integer letterPrintRequiredCount;

    private Integer letterPrintedCount;

    private final Integer letterErrorCount;

    private final Integer letterDeletedCount;

    private final Integer letterArchivedCount;

    /**
     * Construct a new summary
     * 
     * @throws IllegalArgumentException
     *             if any parameter is null
     */
    public WllControlComplexValue(XhbWllControlBasicValue basicValue, String documentType, String documentTitle,
            String language, String country, Integer majorSchemaVersion, Integer minorSchemaVersion, Date dateCreated,
            Integer letterReadyCount, Integer letterFaxedCount, Integer letterEmailedCount,
            Integer letterPrintRequiredCount, Integer letterPrintedCount, Integer letterErrorCount,
            Integer letterDeletedCount, Integer letterArchivedCount) {
        if (basicValue == null) {
            throw new IllegalArgumentException("basicValue: null");
        }
        if (documentType == null) {
            throw new IllegalArgumentException("documentType: null");
        }
        if (documentTitle == null) {
            throw new IllegalArgumentException("documentTitle: null");
        }
        if (dateCreated == null) {
            throw new IllegalArgumentException("dateCreated: null");
        }
        if (letterReadyCount == null) {
            throw new IllegalArgumentException("letterReadyCount: null");
        }
        if (letterFaxedCount == null) {
            throw new IllegalArgumentException("letterFaxedCount: null");
        }
        if (letterReadyCount == null) {
            throw new IllegalArgumentException("letterReadyCount: null");
        }
        if (letterEmailedCount == null) {
            throw new IllegalArgumentException("letterEmailedCount: null");
        }
        if (letterPrintRequiredCount == null) {
            throw new IllegalArgumentException("letterPrintRequiredCount: null");
        }
        if (letterPrintedCount == null) {
            throw new IllegalArgumentException("letterPrintedCount: null");
        }
        if (letterErrorCount == null) {
            throw new IllegalArgumentException("letterErrorCount: null");
        }
        if (letterDeletedCount == null) {
            throw new IllegalArgumentException("letterDeletedCount: null");
        }
        if (letterArchivedCount == null) {
            throw new IllegalArgumentException("letterArchivedCount: null");
        }

        this.basicValue = basicValue;
        this.documentType = documentType;
        this.documentTitle = documentTitle;
        this.language = language;
        this.country = country;
        this.majorSchemaVersion = majorSchemaVersion;
        this.minorSchemaVersion = minorSchemaVersion;
        this.dateCreated = dateCreated;
        this.letterReadyCount = letterReadyCount;
        this.letterFaxedCount = letterFaxedCount;
        this.letterEmailedCount = letterEmailedCount;
        this.letterPrintRequiredCount = letterPrintRequiredCount;
        this.letterPrintedCount = letterPrintedCount;
        this.letterErrorCount = letterErrorCount;
        this.letterDeletedCount = letterDeletedCount;
        this.letterArchivedCount = letterArchivedCount;
    }

    /**
     * Get a string representation of the object
     */
    public String toString() {
        return getClass().getName() + "[basicValue=" + basicValue + ",documentType=" + documentType + ",documentTitle="
                + documentTitle + ",language=" + language + ",country=" + country + ",majorSchemaVersion="
                + majorSchemaVersion + ",minorSchemaVersion=" + minorSchemaVersion + ",dateCreated=" + dateCreated
                + ",letterReadyCount=" + letterReadyCount + ",letterFaxedCount=" + letterFaxedCount
                + ",letterEmailedCount=" + letterEmailedCount + ",letterPrintRequiredCount=" + letterPrintRequiredCount
                + ",letterPrintedCount=" + letterPrintedCount + ",letterErrorCount=" + letterErrorCount
                + ",letterDeletedCount=" + letterDeletedCount + ",letterArchivedCount=" + letterArchivedCount + "]";
    }

    /**
     * Return true if object is an instance of WllControlComplexValue and
     * represents the same record
     */
    public boolean equals(Object object) {
        return object instanceof WllControlComplexValue && equals((WllControlComplexValue) object);
    }

    /**
     * Return true if value represents the same record
     */
    public boolean equals(WllControlComplexValue value) {
        return value != null && basicValue.getWllControlId().equals(value.basicValue.getWllControlId());
    }

    /**
     * Return true if the list is a warned list
     */
    public boolean isWarnedList() {
        return DOCUMENT_TYPE_WARNED_LIST.equals(documentType);
    }

    /**
     * Return true if the list is a warned list
     */
    public boolean isFirmList() {
        return DOCUMENT_TYPE_FIRM_LIST.equals(documentType);
    }

    /**
     * Return true if the list is a warned list
     */
    public boolean isDailyList() {
        return DOCUMENT_TYPE_DAILY_LIST.equals(documentType);
    }

    /**
     * Return true if authorisation is required
     */
    public boolean isAuthorizationRequired() {
        return LIST_STATUS_AUTHORIZATION_REQUIRED.equals(getListStatus());
    }

    /**
     * Return true if authorisation is required
     */
    public boolean isPrintingRequired() {
        return LIST_STATUS_PRINT_REQUIRED.equals(getListStatus());
    }

    /**
     * Return true if the list can be authorized is required
     */
    public boolean canAuthorize() {
        return isAuthorizationRequired();
    }

    /**
     * Return true if there are letters which can be printed.
     */
    public boolean canPrint() {
        String status = getListStatus();
        return LIST_STATUS_PRINT_REQUIRED.equals(status) || LIST_STATUS_PROCESING_COMPLETE.equals(status);
    }

    /**
     * Return true if the list has letters which are posted
     */
    public boolean hasPost() {
        return getLetterReadyCount().intValue() == 0
                && (getLetterPrintRequiredCount().intValue() > 0 || getLetterPrintedCount().intValue() > 0);
    }

    /**
     * Return true if the list can be deleted
     */
    public boolean canDelete() {
        String status = getListStatus();
        return LIST_STATUS_AUTHORIZATION_REQUIRED.equals(status) || LIST_STATUS_PRINT_REQUIRED.equals(status)
                || LIST_STATUS_PROCESING_COMPLETE.equals(status) || LIST_STATUS_LIST_ERROR.equals(status);

    }

    public void authorize() {
        if (!canAuthorize()) {
            throw new IllegalStateException("listStatus: " + getListStatus());
        }
        basicValue.setStatus(CONTROL_STATUS_DOCUMENT_AUTHORISED);
    }

    public void delete() {
        if (!canDelete()) {
            throw new IllegalStateException("listStatus: " + getListStatus());
        }
        basicValue.setStatus(CONTROL_STATUS_DOCUMENT_DELETED);
    }

    public void printed() {
        if (!canPrint()) {
            throw new IllegalStateException("listStatus: " + getListStatus());
        }
        letterPrintedCount = letterPrintRequiredCount;
        letterPrintRequiredCount = new Integer(0);
    }

    /**
     * Get the id of the list
     */
    public Integer getListId() {
        return basicValue.getWllControlId();
    }

    /**
     * Get the status of the list. This represents the steps required to process
     * a list. A new list requires authorization. It is then processed into
     * letters, these letters are then distributed. Any that are mailed require
     * printing.
     */
    public String getListStatus() {
        String status = basicValue.getStatus();
        if (CONTROL_STATUS_NEW_DOCUMENT.equals(status)) {
            return LIST_STATUS_AUTHORIZATION_REQUIRED;
        } else if (CONTROL_STATUS_DOCUMENT_AUTHORISED.equals(status)
                || CONTROL_STATUS_PROCESSING_DOCUMENT.equals(status) || CONTROL_STATUS_PROCESSING_ERROR.equals(status)
                || CONTROL_STATUS_SECOND_PROCESSING.equals(status)) {
            // List is being processed (gets reprocesed if fails once!)
            return LIST_STATUS_PROCESSING_LIST;
        } else if (CONTROL_STATUS_PROCESSING_COMPLETE.equals(status)) {
            if (letterReadyCount.intValue() > 0) {
                // Letters are being processed
                return LIST_STATUS_PROCESSING_LIST;
            } else if (letterErrorCount.intValue() == 0) {
                // Processing complete some letters may require printing
                if (letterPrintRequiredCount.intValue() > 0) {
                    return LIST_STATUS_PRINT_REQUIRED;
                } else {
                    return LIST_STATUS_PROCESING_COMPLETE;
                }
            }
        }
        return LIST_STATUS_LIST_ERROR;
    }

    /**
     * Get the percentage of letters distributed by email
     */
    public Double getLetterEmailPercentage() {
        int count = _getLetterCount();
        return new Double(count == 0 ? 0.0 : letterEmailedCount.doubleValue() / count);
    }

    /**
     * Get the percentage of letters distributed by fax
     */
    public Double getLetterFaxPercentage() {
        int count = _getLetterCount();
        return new Double(count == 0 ? 0.0 : letterFaxedCount.doubleValue() / count);
    }

    /**
     * Get the percentage of letters distributed by post
     */
    public Double getLetterPostPercentage() {
        int count = _getLetterCount();
        return new Double(count == 0 ? 0.0
                : (letterPrintRequiredCount.doubleValue() + letterPrintedCount.doubleValue()) / count);
    }

    /**
     * Get the total number of letters
     */
    public Integer getLetterCount() {
        return new Integer(_getLetterCount());
    }

    private int _getLetterCount() {
        return letterReadyCount.intValue() + letterFaxedCount.intValue() + letterEmailedCount.intValue()
                + letterPrintRequiredCount.intValue() + letterPrintedCount.intValue() + letterErrorCount.intValue()
                + letterDeletedCount.intValue() + letterArchivedCount.intValue();
    }

    //
    // DistributionStatusComplexValue Implementation
    // 

    /**
     * DistributionStatusComplexValue Implementation
     */
    public Date getCreationDate() {
        return basicValue.getCreationDate();
    }

    /**
     * DistributionStatusComplexValue Implementation
     */
    public String getStatus() {
        return getListStatus();
    }

    /**
     * DistributionStatusComplexValue Implementation
     * 
     * Note this object is only used to represent posted records
     */
    public String getDistributionType() {
        return "POST";
    }

    /**
     * DistributionStatusComplexValue Implementation
     * 
     * Note this only includes the post (printed recipients)
     */
    public int getRecipientCount() {
        return letterPrintRequiredCount.intValue() + letterPrintedCount.intValue();
    }

    //
    // Accessors
    //

    public void setBasicValue(XhbWllControlBasicValue basicValue) {
        if (basicValue == null || !basicValue.getWllControlId().equals(this.basicValue.getWllControlId())) {
            throw new IllegalArgumentException("basicValue: " + basicValue);
        }
        this.basicValue = basicValue;
    }

    public XhbWllControlBasicValue getBasicValue() {
        return basicValue;
    }

    public Integer getWllControlId() {
        return basicValue.getWllControlId();
    }

    public Integer getVersion() {
        return basicValue.getVersion();
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * @return Returns the country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return Returns the language.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return Returns the majorSchemaVersion.
     */
    public Integer getMajorSchemaVersion() {
        return majorSchemaVersion;
    }

    /**
     * @return Returns the minorSchemaVersion.
     */
    public Integer getMinorSchemaVersion() {
        return minorSchemaVersion;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Integer getLetterReadyCount() {
        return letterReadyCount;
    }

    public Integer getLetterFaxedCount() {
        return letterFaxedCount;
    }

    public Integer getLetterEmailedCount() {
        return letterEmailedCount;
    }

    public Integer getLetterPrintRequiredCount() {
        return letterPrintRequiredCount;
    }

    public Integer getLetterPrintedCount() {
        return letterPrintedCount;
    }

    public Integer getLetterErrorCount() {
        return letterErrorCount;
    }

    public Integer getLetterArchivedCount() {
        return letterArchivedCount;
    }

}
