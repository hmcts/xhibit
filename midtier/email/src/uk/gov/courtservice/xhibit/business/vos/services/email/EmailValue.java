package uk.gov.courtservice.xhibit.business.vos.services.email;

import uk.gov.courtservice.xhibit.business.services.email.PortableDataSource;

/**
 * <p>
 * Title: Email Value Object
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
 * @author Bob Boothby
 * @version 1.0
 */
public class EmailValue implements java.io.Serializable {
    private javax.mail.internet.InternetAddress[] recipients;

    private javax.mail.internet.InternetAddress[] ccrecipients;

    private javax.mail.internet.InternetAddress[] bccrecipients;

    private java.lang.String subject;

    private java.lang.String messageBody;

    private PortableDataSource[] attachments;

    private boolean mustreceive = false;

    private java.lang.Integer courtId;

    private javax.mail.internet.InternetAddress sender;

    /**
     * Complete constructor.
     * 
     * @param recipients
     *            The standard recipients.
     * @param ccrecipients
     *            The CC recipients.
     * @param bccrecipients
     *            The BCC recipients.
     * @param subject
     *            The subject line of the e-mail.
     * @param sender
     *            The address of the sender.
     * @param messageBody
     *            Convenience attribute for a text body.
     * @param attachments
     *            Any attachments.
     * @param mustreceive
     *            Whether to enforce strict checking of delivery.
     * @param courtId
     *            The court with which this e-mail is associated.
     */
    public EmailValue(javax.mail.internet.InternetAddress[] recipients,
            javax.mail.internet.InternetAddress[] ccrecipients, javax.mail.internet.InternetAddress[] bccrecipients,
            java.lang.String subject, javax.mail.internet.InternetAddress sender, java.lang.String messageBody,
            PortableDataSource[] attachments, boolean mustreceive, java.lang.Integer courtId) {
        setRecipients(recipients);
        setCcrecipients(ccrecipients);
        setBccrecipients(bccrecipients);
        setSubject(subject);
        setSender(sender);
        setMessageBody(messageBody);
        setAttachments(attachments);
        setMustreceive(mustreceive);
        setCourtId(courtId);
    }

    /**
     * The standard recipients.
     * 
     * @return The standard recipients.
     */
    public javax.mail.internet.InternetAddress[] getRecipients() {
        return this.recipients;
    }

    /**
     * The standard recipients.
     * 
     * @param recipients
     *            The standard recipients.
     */
    public void setRecipients(javax.mail.internet.InternetAddress[] recipients) {
        this.recipients = recipients;
    }

    /**
     * The CC recipients.
     * 
     * @return The CC recipients.
     */
    public javax.mail.internet.InternetAddress[] getCcrecipients() {
        return this.ccrecipients;
    }

    /**
     * The CC recipients.
     * 
     * @param ccrecipients
     *            The CC recipients.
     */
    public void setCcrecipients(javax.mail.internet.InternetAddress[] ccrecipients) {
        this.ccrecipients = ccrecipients;
    }

    /**
     * The BCC recipients.
     * 
     * @return The BCC recipients.
     */
    public javax.mail.internet.InternetAddress[] getBccrecipients() {
        return this.bccrecipients;
    }

    /**
     * The BCC recipients.
     * 
     * @param bccrecipients
     *            The BCC recipients.
     */
    public void setBccrecipients(javax.mail.internet.InternetAddress[] bccrecipients) {
        this.bccrecipients = bccrecipients;
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
     * Any attachments.
     * 
     * @return Any attachments.
     */
    public PortableDataSource[] getAttachments() {
        return attachments;
    }

    /**
     * Any attachments.
     * 
     * @param attachments
     *            Any attachments.
     */
    public void setAttachments(PortableDataSource[] attachments) {
        this.attachments = attachments;
    }

}