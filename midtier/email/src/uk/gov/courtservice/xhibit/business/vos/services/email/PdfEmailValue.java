package uk.gov.courtservice.xhibit.business.vos.services.email;

/**
 * <p>
 * Title: PDF Email Value Object, modified verion of email value for using PDFs
 * without mime
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley
 * @version 1.0
 */
public class PdfEmailValue implements java.io.Serializable {
    private String recipient;

    private String recipientName;

    private java.lang.String subject;

    private java.lang.String messageBody;

    private byte[] pdfAttachment;

    private boolean mustreceive = false;

    private java.lang.Integer courtId;

    private javax.mail.internet.InternetAddress sender;

    /**
     * Complete constructor.
     * 
     * @param recipient
     *            The recipient (only one in this case).
     * @param recipientName
     *            The recipient Name for Fax cover details
     * @param subject
     *            The subject line of the e-mail.
     * @param sender
     *            The address of the sender.
     * @param messageBody
     *            Convenience attribute for a text body.
     * @param pdfAttachment
     *            the bytes for the pdf.
     * @param mustreceive
     *            Whether to enforce strict checking of delivery.
     * @param courtId
     *            The court with which this e-mail is associated.
     */
    public PdfEmailValue(String recipient, String recipientName, java.lang.String subject,
            javax.mail.internet.InternetAddress sender, java.lang.String messageBody, byte[] pdfAttachment,
            boolean mustreceive, java.lang.Integer courtId) {
        setRecipient(recipient);
        setRecipientName(recipientName);
        setSubject(subject);
        setSender(sender);
        setMessageBody(messageBody);
        setAttachment(pdfAttachment);
        setMustreceive(mustreceive);
        setCourtId(courtId);
    }

    /**
     * The recipient.
     * 
     * @return The standard recipient.
     */
    public String getRecipient() {
        return this.recipient;
    }

    /**
     * The recipient.
     * 
     * @param recipientName
     *            The recipient.
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    /**
     * The recipient.
     * 
     * @return The standard recipient.
     */
    public String getRecipientName() {
        return this.recipientName;
    }

    /**
     * The recipient.
     * 
     * @param recipient
     *            The recipient.
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * The subject line of the e-mail.
     * 
     * @return The subject line of the e-mail.
     */
    public java.lang.String getSubject() {
        return this.subject;
    }

    /**
     * The subject line of the e-mail.
     * 
     * @param subject
     *            The subject line of the e-mail.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }

    /**
     * The address of the sender.
     * 
     * @return The address of the sender.
     */
    public javax.mail.internet.InternetAddress getSender() {
        return this.sender;
    }

    /**
     * The address of the sender.
     * 
     * @param sender
     *            The address of the sender.
     */
    public void setSender(javax.mail.internet.InternetAddress sender) {
        this.sender = sender;
    }

    /**
     * Convenience attribute for a text body.
     * 
     * @return Convenience attribute for a text body.
     */
    public java.lang.String getMessageBody() {
        return this.messageBody;
    }

    /**
     * Convenience attribute for a text body.
     * 
     * @param messageBody
     *            Convenience attribute for a text body.
     */
    public void setMessageBody(java.lang.String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * Whether to enforce strict checking of delivery.
     * 
     * @return Whether to enforce strict checking of delivery.
     */
    public boolean getMustreceive() {
        return this.mustreceive;
    }

    /**
     * Whether to enforce strict checking of delivery.
     * 
     * @param mustreceive
     *            Whether to enforce strict checking of delivery.
     */
    public void setMustreceive(boolean mustreceive) {
        this.mustreceive = mustreceive;
    }

    /**
     * The court with which this e-mail is associated.
     * 
     * @return The court with which this e-mail is associated.
     */
    public java.lang.Integer getCourtId() {
        return this.courtId;
    }

    /**
     * The court with which this e-mail is associated.
     * 
     * @param courtId
     *            The court with which this e-mail is associated.
     */
    public void setCourtId(java.lang.Integer courtId) {
        this.courtId = courtId;
    }

    /**
     * the pdf attachment.
     * 
     * @return the pdf attachment.
     */
    public byte[] getAttachment() {
        return pdfAttachment;
    }

    /**
     * Any attachments.
     * 
     * @param pdfAttachment
     *            Any attachments.
     */
    public void setAttachment(byte[] pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
    }

}