package uk.gov.courtservice.framework.services.audit;

/**
 * <p>
 * Title: EventTypes
 * </p>
 * <p>
 * Description: Interface containing all valid auditable event types
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

public interface EventTypes {
    public static final EventType DEFAULT = new EventType("Default Event Message");

    // add new event types here
}