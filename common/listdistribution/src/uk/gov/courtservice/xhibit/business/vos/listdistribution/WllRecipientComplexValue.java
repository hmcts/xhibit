package uk.gov.courtservice.xhibit.business.vos.listdistribution;

import java.io.Serializable;

import uk.gov.courtservice.xhibit.business.entities.xhb_document_distribution.XhbDocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_recipient.XhbWllRecipientBasicValue;

/**
 * <p>
 * Title: WllRecipientComplexValue
 * </p>
 * <p>
 * Description: The recipient complex value.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: WllRecipientComplexValue.java,v 1.1 2005/02/23 14:55:34 bzjrnl
 *          Exp $
 */
public class WllRecipientComplexValue implements Serializable {
	
	static final long serialVersionUID = -5857316414569299114L;
	
    // Recipient Types
    public static final String SOLICITOR_RECIPIENT_TYPE = "S";

    public static final String OPPOSER_RECIPIENT_TYPE = "O";

    // Document Type
    public static final String DOCUMENT_TYPE = "WLL"; // Only value used for

    // letters

    // MIME Types
    public static final String PDF_MIME_TYPE = "PDF";

    public static final String HTML_MIME_TYPE = "HTM";

    // Use Prefered Dist Type
    public static final String USE_PREFERRED_DIST_TYPE = "N"; // Only value

    // used for
    // letters

    // Distribution Types
    public static final String EMAIL_DISTRIBUTION_TYPE = "EMAIL";

    public static final String POST_DISTRIBUTION_TYPE = "POST";

    public static final String FAX_DISTRIBUTION_TYPE = "FAX";

    // Recipient data
    private XhbWllRecipientBasicValue recipientBasicValue;

    // Distribution data
    private XhbDocumentDistributionBasicValue documentDistributionBasicValue;

    /**
     * Construct a new instance of the recipient complex value without
     * distribution data
     */
    public WllRecipientComplexValue(XhbWllRecipientBasicValue recipientBasicValue) {
        if (recipientBasicValue == null) {
            throw new IllegalArgumentException("recipientBasicValue: null");
        }
        this.recipientBasicValue = recipientBasicValue;
        this.documentDistributionBasicValue = null;
    }

    /**
     * Construct a new instance of the recipient complex value with distribution
     * data
     */
    public WllRecipientComplexValue(XhbWllRecipientBasicValue recipientBasicValue,
            XhbDocumentDistributionBasicValue documentDistributionBasicValue) {
        if (recipientBasicValue == null) {
            throw new IllegalArgumentException("recipientBasicValue: null");
        }
        if (documentDistributionBasicValue == null) {
            throw new IllegalArgumentException("documentDistributionBasicValue: null");
        }
        this.recipientBasicValue = recipientBasicValue;
        this.documentDistributionBasicValue = documentDistributionBasicValue;
    }

    /**
     * Create a string representation of the object
     */
    public String toString() {
        return getClass().getName() + "[recipientBasicValue=" + recipientBasicValue
                + ", documentDistributionBasicValue=" + documentDistributionBasicValue + "]";
    }

    // Business

    /**
     * Return true if the recipient is a solictitor
     */
    public boolean isSolicitor() {
        return SOLICITOR_RECIPIENT_TYPE.equals(getRecipientType());
    }

    /**
     * Return true if the recipient is an opposer
     */
    public boolean isOpposer() {
        return OPPOSER_RECIPIENT_TYPE.equals(getRecipientType());
    }

    /**
     * Return true if the recipient is subscribed
     */
    public boolean isDeliveryMethodSpecified() {
        return documentDistributionBasicValue == null;
    }

    /**
     * Get the recipients mime type (PDF is default)
     */
    public String getMimeType() {
        return documentDistributionBasicValue == null ? PDF_MIME_TYPE : documentDistributionBasicValue.getMimeType();
    }

    /**
     * Return true if the recipient mime type is PDF (PDF is default)
     */
    public boolean isMimePdf() {
        return PDF_MIME_TYPE.equals(getMimeType());
    }

    /**
     * Return true if the recipient mime type is HTM (PDF is default)
     */
    public boolean isMimeHtml() {
        return HTML_MIME_TYPE.equals(getMimeType());
    }

    /**
     * Get the distribution type (POST is default)
     */
    public String getDistributionType() {
        return documentDistributionBasicValue == null ? POST_DISTRIBUTION_TYPE : documentDistributionBasicValue
                .getDistributionType();
    }

    /**
     * Return true if the recipient distribution type is POST (POST is default)
     */
    public boolean isDistributionPost() {
        return POST_DISTRIBUTION_TYPE.equals(getDistributionType());
    }

    /**
     * Return true if the recipient distribution type is EMAIL (POST is default)
     */
    public boolean isDistributionEmail() {
        return EMAIL_DISTRIBUTION_TYPE.equals(getDistributionType());
    }

    /**
     * Return true if the recipient distribution type is FAX (POST is default)
     */
    public boolean isDistributionFax() {
        return FAX_DISTRIBUTION_TYPE.equals(getDistributionType());
    }

    /**
     * Get an id that uniquely identifies this object
     */
    public Integer getId() {
        // Use the id else the courtId and the crestId
        // cant use wll_recipient id as might not have one yet
        Integer courtId = recipientBasicValue.getCourtId();
        Integer crestId = recipientBasicValue.getCrestSolicitorFirmId();
        return new Integer((courtId.intValue() << 16) | (0x00000ffff & crestId.intValue()));
    }

    /**
     * Get the recipient's name
     */
    public String getName() {
        return recipientBasicValue.getSolicitorFirmName(); // Note
        // solicitor
        // firm used for
        // both
    }

    /**
     * Get the recipient's address
     */
    public String getAddress() {
        // Note spelling mistake in DB Schema
        return recipientBasicValue.getSolictiorFirmAddress(); // Note
        // solicitor
        // firm used for
        // both
    }

    /**
     * Get the recipient's email
     */
    public String getEmail() {
        return recipientBasicValue.getSolicitorFirmEmail(); // Note
        // solicitor
        // firm used for
        // both
    }

    /**
     * Get the recipient's fax
     */
    public String getFax() {
        return recipientBasicValue.getSolicitorFirmFax(); // Note
        // solicitor
        // firm used for
        // both
    }

    /**
     * Get the type of the recipient
     */
    public String getRecipientType() {
        return recipientBasicValue.getRecipientType();
    }

    /**
     * Set the recipient's fax (only editable field on recipient value)
     */
    public void setFax(String fax) {
        recipientBasicValue.setSolicitorFirmFax(fax); // Note solicitor firm
        // used for both
    }

    /**
     * Subscribe the user (if unsubscribed) and set the mime type to PDF
     */
    public void setMimePdf() {
        if (documentDistributionBasicValue == null) {
            documentDistributionBasicValue = createDocumentDistributionBasicValue();
        }
        documentDistributionBasicValue.setMimeType(PDF_MIME_TYPE);
    }

    /**
     * Subscribe the user (if unsubscribed) and set the mime type to HTML
     */
    public void setMimeHtml() {
        if (documentDistributionBasicValue == null) {
            documentDistributionBasicValue = createDocumentDistributionBasicValue();
        }
        documentDistributionBasicValue.setMimeType(HTML_MIME_TYPE);
    }

    /**
     * Subscribe the user (if unsubscribed) and set the distribution type to
     * POST
     */
    public void setDistributionPost() {
        if (documentDistributionBasicValue == null) {
            documentDistributionBasicValue = createDocumentDistributionBasicValue();
        }
        documentDistributionBasicValue.setDistributionType(POST_DISTRIBUTION_TYPE);
    }

    /**
     * Subscribe the user (if unsubscribed) and set the distribution type to
     * EMAIL
     */
    public void setDistributionEmail() {
        if (documentDistributionBasicValue == null) {
            documentDistributionBasicValue = createDocumentDistributionBasicValue();
        }
        documentDistributionBasicValue.setDistributionType(EMAIL_DISTRIBUTION_TYPE);
    }

    /**
     * Subscribe the user (if unsubscribed) and set the distribution type to
     * EMAIL
     */
    public void setDistributionFax() {
        if (documentDistributionBasicValue == null) {
            documentDistributionBasicValue = createDocumentDistributionBasicValue();
        }
        documentDistributionBasicValue.setDistributionType(FAX_DISTRIBUTION_TYPE);
    }

    // Accessors

    public XhbWllRecipientBasicValue getRecipientBasicValue() {
        return recipientBasicValue;
    }

    public void setRecipientBasicValue(XhbWllRecipientBasicValue recipientBasicValue) {
        this.recipientBasicValue = recipientBasicValue;
    }

    public XhbDocumentDistributionBasicValue getDocumentDistributionBasicValue() {
        return documentDistributionBasicValue;
    }

    public void setDocumentDistributionBasicValue(XhbDocumentDistributionBasicValue documentDistributionBasicValue) {
        this.documentDistributionBasicValue = documentDistributionBasicValue;
    }

    // Other

    /**
     * Create an identical (deep) copy of this object
     */
    public WllRecipientComplexValue copy() {
        XhbWllRecipientBasicValue newRecipientBasicValue = copy(recipientBasicValue);
        XhbDocumentDistributionBasicValue newDocumentDistributionBasicValue = copy(documentDistributionBasicValue);
        if (newDocumentDistributionBasicValue == null) {
            return new WllRecipientComplexValue(newRecipientBasicValue);
        } else {
            return new WllRecipientComplexValue(newRecipientBasicValue, newDocumentDistributionBasicValue);
        }
    }

    private static XhbWllRecipientBasicValue copy(XhbWllRecipientBasicValue value) {
        return value == null ? null : new XhbWllRecipientBasicValue(value);
    }

    private static XhbDocumentDistributionBasicValue copy(XhbDocumentDistributionBasicValue value) {
        return value == null ? null : new XhbDocumentDistributionBasicValue(value);
    }

    // Utilities

    /**
     * Create a new document distribution record for this letter recipient
     */
    private XhbDocumentDistributionBasicValue createDocumentDistributionBasicValue() {
        XhbDocumentDistributionBasicValue value = new XhbDocumentDistributionBasicValue();

        value.setDocumentType(DOCUMENT_TYPE); // Fixed for letters
        value.setUsePrefDistType(USE_PREFERRED_DIST_TYPE); // Fixed for
        // letters
        value.setCourtId(recipientBasicValue.getCourtId());
        value.setWllRecipientId(recipientBasicValue.getWllRecipientId());
        value.setDistributionType(POST_DISTRIBUTION_TYPE);
        value.setMimeType(PDF_MIME_TYPE);

        return value;
    }
}
