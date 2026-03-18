package uk.gov.courtservice.xhibit.database.messagegenerator;

import uk.gov.courtservice.framework.jdbc.core.AbstractExissDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.messagegenerator.MessageVO;
import uk.gov.courtservice.xhibit.database.messagegenerator.processor.MessageRowProcessor;

/**
 * A utility class used to extract all of the database actions performed by the
 * MessageBroker component in the Flow to ExISS sub-projectof RFC1492.
 *
 * @author szn20z
 */

public class MessageGeneratorExissDatabase extends AbstractExissDatabase {
    /**
     * Used for creating Debug text
     */
    private static final String NL = System.getProperty("line.separator", "\n");

    private static final String TAB = "    ";
        
    private static final String GET_JMS_MESSAGE = "{ ? = call exi_jms_message_pkg.get_jms_message_refcur() }";

    /**
     * Get the list of messages to be processed.
     *
     * @return A collection containing <code>SelectorVO</code> objects
     *         coresponding to all the selectors in the XHB rules data base
     */
    public MessageVO[] getMessages() {
        
        final MessageRowProcessor rp = new MessageRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_JMS_MESSAGE);
        sp.registerInTypes(new int[] {});
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {});
        
        MessageVO[] messages = rp.getMessages();
        
        if(log.isDebugEnabled()) {
            log.debug(toDebug(messages));
        } 
        
        return messages;
    }
    
    private String toDebug(MessageVO[] messages) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loaded ");
        builder.append(messages.length);
        builder.append(" messages:");
        for(MessageVO message : messages) {
            builder.append(NL);
            builder.append(TAB);
            builder.append(message);
        }
        return builder.toString();
    }
    
}
