package uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit;

/**
 * <p>
 * Title: ListLetter
 * </p>
 * <p>
 * Description: Implemented by all list letters.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListLetter.java,v 1.4 2006/06/05 12:28:52 bzjrnl Exp $
 */
public interface ListLetter {
    /**
     * Get the letters recipient, all letters have a recipient.
     */
    public Recipient getRecipient();
}
