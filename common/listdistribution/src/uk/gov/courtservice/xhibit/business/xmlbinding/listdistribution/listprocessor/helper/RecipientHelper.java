package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.listprocessor.helper;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Defendant;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Prosecution;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Recipient;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.Solicitor;

/**
 * <p>
 * Title: RecipientHelper
 * </p>
 * <p>
 * Description: Helper class for manipulating Recipient XML bindings
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: RecipientHelper.java,v 1.3 2006/06/05 12:28:22 bzjrnl Exp $
 */
public class RecipientHelper {
    /**
     * If the recipient type is a solicitor
     */
    public static final String SOLICITOR_RECIPIENT_TYPE = "S";

    /**
     * If the recipient type is a prosecutor (opposer)
     */
    public static final String PROSECUTOR_RECIPIENT_TYPE = "O";

    /**
     * If the recipient type is a defendant (note defendants dont have a
     * recipient type)
     */
    public static final String DEFENDANT_RECIPIENT_TYPE = null;

    /**
     * The logger!
     */
    private static final Logger log = CSServices.getLogger(RecipientHelper.class);

    /**
     * Create a recipient for the specified representitive. If defendant is null
     * then prosecution, else defendant. If solicitor is null then
     * unrepresented, else represented. One of the arguments must not be null.
     * 
     * @param defendant
     *            the defendant if that is the reci pient else null
     * @param prosecution
     *            the prosecution if that is the recipient else null
     * @param solicitor
     *            the solicitor if that is the recipient else null
     * @return the new recipient
     */
    public static final Recipient create(Defendant defendant, Prosecution prosecution, Solicitor solicitor) {
        if (solicitor == null) {
            if (defendant == null) {
                return create(prosecution);
            } else {
                return create(defendant);
            }
        } else {
            return create(solicitor);
        }
    }

    /**
     * Create a Recipient from the Solicitor
     * 
     * @param solicitor
     *            the solicitor
     * @return the recipient
     * @throws NullPointerException
     *             if the solicitor is null;
     */
    public static Recipient create(Solicitor solicitor) {
        Recipient recipient = new Recipient();
        recipient.setSolicitor(solicitor);
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(recipient) + ".");

        }
        return recipient;
    }

    /**
     * Create a Recipient from the Defendant
     * 
     * @param defendant
     *            the defendant
     * @return the recipient
     * @throws NullPointerException
     *             if the defendant is null;
     */
    public static Recipient create(Defendant defendant) {
        Recipient recipient = new Recipient();
        recipient.setDefendant(defendant);
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(recipient) + ".");
        }
        return recipient;
    }

    /**
     * Create a Recipient from the Prosecution
     * 
     * @param prosecution
     *            the prosecution
     * @return the recipient
     * @throws NullPointerException
     *             if the prosecution is null;
     */
    public static Recipient create(Prosecution prosecution) {
        Recipient recipient = new Recipient();
        recipient.setProsecution(prosecution);
        if (log.isDebugEnabled()) {
            log.debug("Created " + toDebug(recipient) + ".");
        }
        return recipient;
    }

    /**
     * Return a String containing useful debug information
     * 
     * @param recipient
     *            the object to get the text for
     * @return the debug information
     */
    public static String toDebug(Recipient recipient) {
        Solicitor solicitor = recipient.getSolicitor();
        if (solicitor != null) {
            return "Recipient[recipientId=" + getRecipientId(recipient) + ", recipientType="
                    + getRecipientType(recipient) + ", solicitor=" + SolicitorHelper.toDebug(solicitor) + ", valid="
                    + recipient.isValid() + "]";
        }

        Defendant defendant = recipient.getDefendant();
        if (defendant != null) {
            return "Recipient[recipientId=" + getRecipientId(recipient) + ", recipientType="
                    + getRecipientType(recipient) + ", defendant=" + DefendantHelper.toDebug(defendant) + ", valid="
                    + recipient.isValid() + "]";
        }

        Prosecution prosecution = recipient.getProsecution();
        if (prosecution != null) {
            return "Recipient[recipientId=" + getRecipientId(recipient) + ", recipientType="
                    + getRecipientType(recipient) + ", prosecution=" + ProsecutionHelper.toDebug(prosecution)
                    + ", valid=" + recipient.isValid() + "]";
        }

        return "";

    }

    /**
     * Return true if this recipient has a recipient id, ie can be sent in an
     * automated way.
     */
    public static boolean hasRecipientId(Recipient recipient) {
        return recipient.getSolicitor() != null || recipient.getProsecution() != null;
    }

    /**
     * Get the recipients id.
     * 
     * @return the recipient type or null if no recipient id.
     */
    public static Integer getRecipientId(Recipient recipient) {
        Solicitor solicitor = recipient.getSolicitor();
        if (solicitor != null) {
            return SolicitorHelper.getRecipientId(solicitor);
        }

        Prosecution prosecution = recipient.getProsecution();
        if (prosecution != null) {
            return ProsecutionHelper.getRecipientId(prosecution);
        }

        return null;
    }

    /**
     * Get the recipients type this is used in conjunction with the id to
     * resolve the recipient. If the recipient has no id it has no type!
     * 
     * @return the recipient type or null if no recipient type.
     */
    public static String getRecipientType(Recipient recipient) {
        if (recipient.getSolicitor() != null) {
            return SOLICITOR_RECIPIENT_TYPE;
        }

        if (recipient.getProsecution() != null) {
            return PROSECUTOR_RECIPIENT_TYPE;
        }

        return DEFENDANT_RECIPIENT_TYPE;
    }
}
