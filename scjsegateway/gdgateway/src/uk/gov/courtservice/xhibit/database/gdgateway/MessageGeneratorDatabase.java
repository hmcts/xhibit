package uk.gov.courtservice.xhibit.database.gdgateway;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractGdGateDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.EventMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.processor.MessageGeneratorRowProcessor;


/**
 * <p>
 * Title: Database utility class for Event Message components.
 * </p>
 * <p>
 * Description: A utility class used to extract all of the database actions
 * performed by the Message Genertation for the Outbound and Inbound in the GDGate domain.
 * This class is thread-safe as it holds no state.
 * Note - the reads for config data and org unit Id are expected to be in a common
 * database class and the message generator database access routines will also have
 * their own class
 * </p>
 *
 * @version $Id: MessageGeneratorDatabase.java,v 1.2 2006/10/17 12:02:38 szfnvt Exp $
 */
public class MessageGeneratorDatabase extends AbstractGdGateDatabase
{
    private static final String NL = System.getProperty("line.separator", "\n");
    private static final String TAB = "    ";
    private static final String GET_JMS_MESSAGE = "{ ? = call gdg_scjse_gateway_jms_pkg.get_jms_message_refcur(?) }";
    
    /**
     * The MessageType is used to return messages matching the type
     * @param messageType
     * @return
     */
    public EventMessageVO[] getMessages(String messageType) {    
        final MessageGeneratorRowProcessor rp = new MessageGeneratorRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_JMS_MESSAGE);
        sp.registerInTypes(new int[] { Types.VARCHAR });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { messageType });
        
        EventMessageVO[] messages = rp.getMessages();
        
        if(log.isDebugEnabled()) {
            log.debug(toDebug(messages));
        } 
        
        return messages;
    }
    
    private String toDebug(EventMessageVO[] messages) {
        StringBuilder builder = new StringBuilder();
        builder.append("Loaded ");
        builder.append(messages.length);
        builder.append(" messages:");
        for(EventMessageVO message : messages) {
            builder.append(NL);
            builder.append(TAB);
            builder.append(message);
        }
        return builder.toString();
    }
}
