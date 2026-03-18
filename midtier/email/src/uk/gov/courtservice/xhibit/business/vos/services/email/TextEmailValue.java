package uk.gov.courtservice.xhibit.business.vos.services.email;

/**
 * <p>
 * Title: Text Email Value Object, modified verion of email value for using Text
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
 * @author Bob Boothby
 * @version 1.0
 */
public class TextEmailValue implements java.io.Serializable {
    private javax.mail.internet.InternetAddress[] recipients;

    private java.lang.String subject;

    private java.lang.String messageBody;

    private java.lang.String textAttachment;

    private boolean mustreceive = false;

    private java.lang.Integer courtId;

    private javax.mail.internet.InternetAddress sender;

    /**
     * Complete constructor.
     * 
     * @param recipients
     *            The recipient (only one in this case).
     * @param subject
     *            The subject line of the e-mail.
     * @param sender
     *            The address of the sender.
     * @param messageBody
     *            Convenience attribute for a text body.
     * @param textAttachment
     *            the String for the text attachment.
     * @param mustreceive
     *            Whether to enforce strict checking of delivery.
     * @param courtId
     *            The court with which this e-mail is associated.
     */
    public TextEmailValue(javax.mail.internet.InternetAddress[] recipients, java.lang.String subject,
            javax.mail.internet.InternetAddress sender, java.lang.String messageBody, String textAttachment,
            boolean mustreceive, java.lang.Integer courtId) {
        setRecipients(recipients);
        setSubject(subject);
        setSender(sender);
        setMessageBody(messageBody);
        setTextAttachment(textAttachment);
        setMustreceive(mustreceive);
        setCourtId(courtId);
    }

    /**
     * The recipients.
     * 
     * @return The standard recipient.
     */
    public javax.mail.internet.InternetAddress[] getRecipients() {
        return this.recipients;
    }

    /**
     * The recipient.
     * 
     * @param recipient
     *            The recipient.
     */
    public void setRecipients(javax.mail.internet.InternetAddress[] recipients) {
        this.recipients = recipients;
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
     * the text attachment.
     * 
     * @return the text attachment.
     */
    public String getTextAttachment() {
        return textAttachment;
    }

    /**
     * text attachment.
     * 
     * @param attachments
     *            text attachment.
     */
    public void setTextAttachment(String textAttachment) {
        this.textAttachment = textAttachment;
    }

}