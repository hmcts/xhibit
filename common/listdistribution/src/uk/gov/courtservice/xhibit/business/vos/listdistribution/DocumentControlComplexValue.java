package uk.gov.courtservice.xhibit.business.vos.listdistribution;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.entities.xhb_document_control.XhbDocumentControlBasicValue;

/**
 * <p>
 * Title: DocumentControlComplexValue
 * </p>
 * <p>
 * Description: The documentControl complex value.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DocumentControlComplexValue.java,v 1.2 2006/02/09 11:43:03
 *          xztnfq Exp $
 */
public class DocumentControlComplexValue extends DistributionStatusComplexValue {
	
	static final long serialVersionUID = 1508358019944612228L;
	
    // Status
    public static final String NEW_DOCUMENT_STATUS = "ND";

    public static final String FORMATTING_DOCUMENT_STATUS = "FD";

    public static final String DOCUMENT_READY_STATUS = "DR";

    public static final String FORMATTING_ERROR_STATUS = "FE";

    public static final String FORMATTING_FAILURE_STATUS = "FF";

    public static final String DOCUMENT_ACCEPTED_STATUS = "DA";

    public static final String DOCUMENT_REJECTED_STATUS = "RJ";

    public static final String FOR_TRANSMISSION_STATUS = "FT";

    public static final String SUCCESSFUL_EMAIL_STATUS = "SE";

    public static final String SUCCESSFUL_FAX_STATUS = "SX";

    public static final String SUCCESSFUL_FTP_STATUS = "SF";

    public static final String DELIVERY_FAILED_EMAIL_STATUS = "FE";

    public static final String DELIVERY_FAILED_FAX_STATUS = "FF";

    public static final String ARCHIVE_STATUS = "XA";

    public static final String DELETE_STATUS = "XX";

    // Control Data
    private final XhbDocumentControlBasicValue basicValue;

    // Recipient Names
    private final List recipientNames = new ArrayList();

    /**
     * Construct a new instance of the document control complex value
     */
    public DocumentControlComplexValue(XhbDocumentControlBasicValue basicValue) {
        if (basicValue == null) {
            throw new IllegalArgumentException("basicValue: null");
        }
        this.basicValue = basicValue;
    }

    /**
     * Return true if object is an instance of DocumentControlComplexValue and
     * represents the same record
     */
    public boolean equals(Object object) {
        return object instanceof DocumentControlComplexValue && equals((DocumentControlComplexValue) object);
    }

    /**
     * Return true if value represents the same record
     */
    public boolean equals(DocumentControlComplexValue value) {
        return value != null && basicValue.getDocControlId().equals(value.basicValue.getDocControlId());
    }

    /**
     * Create a string representation of the object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());

        // Append Basic Value
        buffer.append("[basicValue=");
        buffer.append(basicValue);

        // Append Recipient Names
        int s = recipientNames.size();
        if (s < 0) {
            buffer.append(", recipientNames={");
            buffer.append(recipientNames.get(0));
            for (int i = 1; i < s; i++) {
                buffer.append(", ");
                buffer.append(recipientNames.get(i));
            }
            buffer.append("}");
        } else {
            buffer.append(", recipientNames={}");
        }

        buffer.append("]");
        return buffer.toString();
    }

    // Business

    /**
     * Add the name of a recipient
     */
    public void addRecipientName(String recipientName) {
        if (recipientName == null) {
            throw new IllegalArgumentException("recipientName: null");
        }
        recipientNames.add(recipientName);
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public String getDocumentType() {
        return basicValue.getDocumentType();
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public Date getCreationDate() {
        return basicValue.getCreationDate();
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public Date getDistributionDate() {
        return basicValue.getDistributedDate();
    }

    /**
     * DistributionStatusComplexValue Implementation
     */
    public String getDistributionType() {
        return basicValue.getDistributionType();
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public String getStatus() {
        return basicValue.getStatus();
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public int getRecipientCount() {
        return recipientNames.size();
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public boolean hasRecipientNames() {
        return true;
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public String getRecipientName(int index) {
        return (String) recipientNames.get(index);
    }

    /**
     * ControlStatusComplexValue Implementaion
     * 
     * Return true if this can be deleted, it is dangerous to delete the
     * document control record if it is currently being processed.
     */
    public boolean canDelete() {
        String status = basicValue.getStatus();
        return FORMATTING_FAILURE_STATUS.equals(status) || SUCCESSFUL_EMAIL_STATUS.equals(status)
                || SUCCESSFUL_FAX_STATUS.equals(status) || DELIVERY_FAILED_EMAIL_STATUS.equals(status)
                || DELIVERY_FAILED_FAX_STATUS.equals(status) || ARCHIVE_STATUS.equals(status);
    }

    /**
     * ControlStatusComplexValue Implementaion
     */
    public void delete() {
        basicValue.setStatus(DELETE_STATUS);
    }

    // Accessors

    public XhbDocumentControlBasicValue getDocumentControlBasicValue() {
        return basicValue;
    }

}
