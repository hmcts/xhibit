package uk.gov.courtservice.xhibit.client.im.entry;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.im.exception.IMTextException;
import uk.gov.courtservice.xhibit.client.im.util.AdHocMessageServices;
import uk.gov.courtservice.xhibit.client.im.util.IMResourceHelper;

/**
 * <p>
 * Title: IMTextArea
 * </p>
 * <p>
 * Description: Messaging text area - allows a limit of 120 characters, of the
 * GSM character set. Used for External (ad-hoc) messaging)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMTextArea extends JTextArea {

    private static final String WARNING_TITLE = "em.text.message.title";

    private static final String INVALID_CHAR_MSG = "em.text.message.invalid";

    private static final String TOO_LONG_MSG = "em.text.message.length";

    private static final int IM_MESSAGE_LEN = 120;

    private static Logger log = CSServices.getLogger(IMTextArea.class);

    /**
     * Creates an IMTextArea of size x by y
     * 
     * @param x
     *            int rows
     * @param y
     *            int columns
     */
    public IMTextArea(int x, int y) {
        super(x, y);
    }

    /**
     * Overrides JTextArea.replaceSection() Checks that the maximum length of
     * the text has not been exceeded and also that only valid GSM characters
     * are entered.
     * 
     * @param content
     *            String to insert into text area.
     */
    public void replaceSelection(String content) {
        try {
            checkMessageLength(content);
            validateContent(content);
            super.replaceSelection(content);
        } catch (IMTextException ex) {
            String message = ex.getMessage();

            JOptionPane.showMessageDialog(this,
                    // Strip out framework message code number
                    message.substring(message.indexOf("]") + 1), IMResourceHelper.getResourceString(WARNING_TITLE),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Utility method to check that only SMS (GSM) characters are input, will
     * handle the escaped characters as defined in GSM 03.38.
     * 
     * @param inContent
     *            the unicode characters to be checked.
     */
    private void validateContent(String inContent) throws IMTextException {
        log.debug("MESSAGING***: entering validateContent");
        int incorrectCharPos = AdHocMessageServices.validUnicodeForSMS(inContent);
        if (incorrectCharPos != -1) {
            throw new IMTextException("IMTextArea:validateContent", IMResourceHelper
                    .getResourceString(INVALID_CHAR_MSG));
        }
        log.debug("MESSAGING***: leaving validateContent");
    }

    /**
     * Based on JTextComponent.replaceSelection()
     * 
     * @param content
     *            String to check
     * @throws IMTextException
     */
    private void checkMessageLength(String content) throws IMTextException {
        log.debug("MESSAGING***: entering checkMessageLength");
        Document doc = getDocument();
        int docLength = doc.getLength();
        if (doc != null) {
            int p0 = Math.min(getCaret().getDot(), getCaret().getMark());
            int p1 = Math.max(getCaret().getDot(), getCaret().getMark());
            if (p0 != p1) {
                docLength -= (p1 - p0);
            }

            if (content != null && docLength + content.length() > IM_MESSAGE_LEN) {
                throw new IMTextException("IMTextArea:checkMessageLength", IMResourceHelper
                        .getResourceString(TOO_LONG_MSG));
            }
        }
        //
        log.debug("MESSAGING***: leaving checkMessageLength");
    }
}