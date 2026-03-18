package uk.gov.courtservice.framework.services.audittrail;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: FileAuditProvider
 * </p>
 * <p>
 * Description: This File AuditProvider is only intended for development. It 
 * simply logs the message to a file
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

public class FileAuditProvider implements AuditProvider {
    private static Logger log = Logger.getLogger(FileAuditProvider.class);
    
    /**
     * Log the message to file using Log4J Logger class
     */
    public void sendMessage(String message) {
        // Append message to file
        log.debug(message);
    }

}
