package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DartsMessageRowProcessor
 * </p>
 * <p>
 * Description: Class to process the DAR_NEW_MESSAGES.
 * </p>

 * <p>
 * Company: Logica
 * </p>
 *
 * @author Luis Valenzuela
 * @version v 1.0 20081202
 */
public class DartsMessageRowProcessor extends AbstractRowProcessor {

    private final List<DartsMessageVO> messages = new ArrayList<DartsMessageVO>();
    private static final Logger log = CSServices.getLogger(DartsMessageRowProcessor.class);
    
    public DartsMessageVO[] getMessages() {
        return messages.toArray(new DartsMessageVO[messages.size()]);
    }
    @Override
    public void processRow(Row row) {
        DartsMessageVO message = new DartsMessageVO();
        
        message.setId(row.getInt("MESSAGE_ID"));
        /*if(log.isDebugEnabled()){
            log.debug("DARTS Message ID =  " + message.getId());
        }*/
        
        message.set_xhibitMessageCode(row.getString("XHIBIT_MESSAGE_CODE"));
        //log.debug("XHIBIT_MESSAGE_CODE =  " + message.get_xhibitMessageCode());
        
        message.set_exissMessageCode(row.getString("EXISS_MESSAGE_CODE"));
        //log.debug("EXISS_MESSAGE_CODE =  " + message.get_exissMessageCode());
        
        message.set_payload(row.getClobAsString("PAYLOAD"));
        //log.debug("PAYLOAD =  " + message.get_payload());
        
        message.setRetryCount(row.getInt("RETRY_COUNT"));
        //log.debug("RETRY_COUNT =  " + message.getRetryCount());
        
        message.setNextRetryTime(row.getTimestamp("NEXT_RETRY_TIME"));
        //log.debug("NEXT_RETRY_TIME =  " + message.getNextRetryTime());
        
        message.setCreationDate(row.getTimestamp("CREATION_DATE"));
        //log.debug("CREATION_DATE =  " + message.getCreationDate());
        
        message.setLastUpdateDate(row.getTimestamp("LAST_UPDATE_DATE"));
       //log.debug("LAST_UPDATE_DATE =  " + message.getLastUpdateDate());
        
        messages.add(message);
    }
}
