package uk.gov.courtservice.xhibit.business.services.email;

import java.sql.Timestamp;

import javax.ejb.EJBException;
import javax.mail.internet.InternetAddress;

import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmailBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.email.EmailException;
import uk.gov.courtservice.xhibit.business.vos.services.email.TextEmailValue;

/**
 * <p>
 * Title: Text E-Mail helper class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This is a modified version of the email helper class to deal with a Text
 * String as an attachment.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version $Id: TextEmailHelper.java,v 1.9 2006/06/05 12:29:10 bzjrnl Exp $
 */
public class TextEmailHelper {
    /**
     * Takes a text email value object and prepares it for sending.
     * 
     * @param email
     *            The email to be sent.
     * @throws EmailException
     *             when there is a problem in email.
     */
    public static void sendEmail(TextEmailValue email) throws EmailException {
        // Create The Blob
        XhbBlobBasicValue xhbBlob = new XhbBlobBasicValue();
        xhbBlob.setBlobData(email.getTextAttachment().getBytes());
        try {
            // Need to reasign blob to get new blob id!
            xhbBlob = XhbBlobBeanHelper2.create(xhbBlob);
        } catch (EJBException ex) {
            throw new EmailException(ex);
        }

        // Create The Email
        XhbEmailBasicValue xhbEmail = new XhbEmailBasicValue();

        // Populate basic fields.
        xhbEmail.setRecipients(InternetAddress.toString(email.getRecipients()));
        xhbEmail.setBccrecipients("");
        xhbEmail.setCcrecipients("");
        xhbEmail.setCourtId(email.getCourtId());
        xhbEmail.setCreationTime(new Timestamp(System.currentTimeMillis()));
        xhbEmail.setMustreceive(email.getMustreceive() ? "Y" : "N");
        xhbEmail.setSender(email.getSender().toString());
        xhbEmail.setStatus("R"); // 'R'eady
        xhbEmail.setSubject(email.getSubject());
        xhbEmail.setMimeBodyBlobId(xhbBlob.getBlobId());
        xhbEmail.setAttachment("N");
        xhbEmail.setMimeType("TXT");

        try {
            XhbEmailBeanHelper2.create(xhbEmail);
        } catch (EJBException ex) {
            throw new EmailException(ex);
        }
    }
}
