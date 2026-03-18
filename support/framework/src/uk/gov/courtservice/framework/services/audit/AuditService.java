package uk.gov.courtservice.framework.services.audit;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: AuditService
 * </p>
 * <p>
 * Description: The service class for all audit logging within the CSHub
 * framework
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class AuditService {
    private static Logger log = CSServices.getLogger(AuditService.class);

    private static AuditService auditor = null;

    private AuditService() {
        super();
    }

    /**
     * 
     * @return an instace of AuditService
     */
    public static AuditService getInstance() {
        if (auditor == null) {
            auditor = new AuditService();
        }
        return auditor;
    }

    /**
     * Log an auditable event
     * 
     * @param id
     *            userID
     * @param event
     *            the type of auditable event being logged
     */
    public void audit(int id, EventType event) {
        String message = "ID: " + id + " " + event;

        if (!log.isEnabledFor(AuditLevel.AUDIT)) {
            log.warn("Audit logging is disabled. Set Logger " + AuditService.class.getName()
                    + " to AUDIT#classname to enable this facility ");
        }
        System.out.println("message = " + message);
        log.log(AuditLevel.AUDIT, message);
    }

}