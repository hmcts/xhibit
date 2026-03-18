package uk.gov.courtservice.xhibit.business.services.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import javax.activation.DataHandler;
import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_blob.XhbBlobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmailBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_email.XhbEmailBeanHelper2;
import uk.gov.courtservice.xhibit.business.exceptions.email.EmailException;
import uk.gov.courtservice.xhibit.business.vos.services.email.EmailValue;

/**
 * <p>
 * Title: E-Mail helper class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * The e-mail functionality at present is embodied within a database table that
 * is scanned by Mercator. There is no requirement to expose the e-mail
 * functionality directly to the client so we should have to requirement to
 * create a session bean (Controller). Any midtier functionality should just use
 * this helper class directly.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version $Id: EmailHelper.java,v 1.10 2015/04/23 00:07:39 atwells Exp $
 */
public class EmailHelper {
    // *************************************************************************
    // *************************************************************************

    // This class is not currently used in the application, however, there
    // may be plans in the future to implement it. I have commented out the
    // only method - and the associated test class will fail immediately as
    // this method will always throw an exception due to the mime type not
    // being set (database throws a not-null error).

    // *************************************************************************
    // *************************************************************************

    /*
     * Takes a populated email value object and prepares it for sending. @param
     * email The email to be sent. @throws EmailException when there is a
     * problem in email.
     */ 
     public static XhbEmailBasicValue sendEmail(EmailValue email) throws EmailException {
         XhbEmailBasicValue xhbEmail = new XhbEmailBasicValue();
     
         //Populate basic fields.
         xhbEmail.setRecipients(InternetAddress.toString(email.getRecipients()));
         xhbEmail.setBccrecipients(InternetAddress.toString(email.getBccrecipients()));
         xhbEmail.setCcrecipients(InternetAddress.toString(email.getCcrecipients()));
         xhbEmail.setCourtId(email.getCourtId());
         xhbEmail.setCreationTime(new Timestamp(System.currentTimeMillis()));
         xhbEmail.setMustreceive(email.getMustreceive()?"Y":"N");
         xhbEmail.setSender(email.getSender().toString());
         xhbEmail.setStatus("R"); //'R'eady
         xhbEmail.setSubject(email.getSubject());
         
         try { //Populate the Mime body 
             MimeMultipart mmp = new MimeMultipart();
             MimeBodyPart textBody = new MimeBodyPart();
             textBody.setText(email.getMessageBody());
             mmp.addBodyPart(textBody);
             
             PortableDataSource[] attachments = email.getAttachments();
             for(int i = 0; i < attachments.length; i++) {
                 MimeBodyPart attachment = new MimeBodyPart();
                 if (attachments[i] instanceof StringPortableDataSource) {
                    xhbEmail.setMimeType("HTM");
                 } else {
                     xhbEmail.setMimeType("PDF");
                 }
                 attachment.setDataHandler(new DataHandler(attachments[i]));
                 mmp.addBodyPart(attachment); 
             }
             
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             
             //write the content type at the start - includes the boundary..
             byte[] contentTypeBytes = mmp.getContentType().getBytes();
             baos.write(contentTypeBytes);
             
             //write out the message and attachments.
             mmp.writeTo(baos);
             
             // Store the blob first
             XhbBlobBasicValue xbbv = new XhbBlobBasicValue();
             xbbv.setBlobData(baos.toByteArray());
             Long blobId = XhbBlobBeanHelper2.create(xbbv).getBlobId();
             
             // Send the email
             xhbEmail.setMimeBodyBlobId(blobId);
             XhbEmailBasicValue xebv = XhbEmailBeanHelper2.create(xhbEmail);
             return xebv;
         } catch (IOException ex) {
             throw new EmailException(ex);
         } catch (MessagingException ex) {
             throw new EmailException(ex);
         } catch (EJBException ex) {
             throw new EmailException(ex);
         }
     }
     
}
