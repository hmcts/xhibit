package uk.gov.courtservice.framework.services.audittrail;

import uk.gov.courtservice.framework.services.jms.TextMessageFactory;

/**
 * <p>
 * Title: AuditTrailMessageFactory
 * </p>
 * <p>
 * Description: This is the abstract class which defines an AuditTrailMessageFactory.
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

public abstract class AuditTrailMessageFactory extends TextMessageFactory {

    public AuditTrailMessageFactory(String destination) {
        super(destination);        
    }

}
