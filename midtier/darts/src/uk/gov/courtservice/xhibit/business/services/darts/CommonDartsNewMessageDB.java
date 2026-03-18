package uk.gov.courtservice.xhibit.business.services.darts;


/** Common interface so that the 2 different message streams can be used 
 *  in the DartsSenderControllerBean and DartsPrioritySenderControllerBean 
 *  generically.
 *  
 * @author luisv
 *
 */

public interface CommonDartsNewMessageDB {

    /**
     *  Method to return a batch of messages from the relevant database, the subclasses
     *  explicitly call SQL packages that reference DAR_NEW_MESSAGES and
     *  DAR_PRIORITY_NEW_MESSAGES.
     */
    public abstract DartsMessageVO[] getMessages();
    /**
     *  Method to insert failed messages back into the relevant new message table for retry.
     *  Message is inserted in the same state as retrieved and the SF updates the
     *  relevant fields.
     */
    public abstract void reportMessageToBeRetried(DartsMessageVO message);// end of reportMessageToBeRetried

}