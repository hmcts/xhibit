package uk.gov.courtservice.xhibit.business.services.darts;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractDartsDatabase;
import uk.gov.courtservice.framework.services.CSServices;

public class DartsSwitchMessageDatabase extends AbstractDartsDatabase implements CommonDartsNewMessageDB{
	
    private static final Logger log = CSServices.getLogger(DartsSwitchMessageDatabase.class);
    private static CommonDartsNewMessageDB me;
    private CommonDartsNewMessageDB priority ;
    private DartsNewMessageDatabase data ;
    protected DartsConfiguration config;
    protected String priorityMessages;

    
    /**
     * 
     *  Singleton instance, only called from within the class.
     */
    private DartsSwitchMessageDatabase() {
        log.info("DartsSwitchMessageDatabase constructor called");
        try {
        	
			config = DartsConfiguration.getInstance();
	        log.debug("About to get priority Messages");
			priorityMessages = config.getProperty("darts.priorityMessages");
			log.debug("Successfully set priority messages as "+priorityMessages);
		} catch (DartsException e) {
			//if unable to get property out of db
			priorityMessages = "10100,10500,20913,30100,30300,30500,30600";
		}

    }

    
    /**
     * Singleton Accessor method.
     * 
     * @returns
     *      DartsSwitchMessageDatabase
     */
    public static CommonDartsNewMessageDB getInstance() {
        if (me == null) {
            me = new DartsSwitchMessageDatabase();
        }
        return me;
    }
    
    /**
     * 
     */
    public DartsMessageVO[] getMessages() {
    	log.debug("Getting New Messages from DB");
    	//try and get priority messages 
    	priority = DartsPriorityNewMessageDatabase.getInstance();
    	DartsMessageVO[] messages = priority.getMessages();
    	if(messages.length >0 ) {
    		return messages;
    	}
    	else {
    		log.debug("No priority messages therefore attempting normal");
    		data = DartsNewMessageDatabase.getInstance();
            messages = data.getMessages();
            return messages;
    		
    	}    	
    }

	@Override
	public void reportMessageToBeRetried(DartsMessageVO message) {
		if (priorityMessages.contains(message.get_xhibitMessageCode())) {
			priority.reportMessageToBeRetried(message);
		}
		else {
			data.reportMessageToBeRetried(message);

		}		
	}
    
}
