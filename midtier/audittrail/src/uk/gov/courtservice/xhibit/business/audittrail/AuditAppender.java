package uk.gov.courtservice.xhibit.business.audittrail;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: AuditAppender
 * </p>
 * <p>
 * Description: This is class is used to log a message to file.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class AuditAppender {
    private static Logger log = Logger.getLogger(AuditAppender.class);
    
    /**
     * Use log4j to write the message to the audit log file
     * @param message
     */
    public static void writeAuditMessage(String message){
        log.debug(message);        
    }
}
