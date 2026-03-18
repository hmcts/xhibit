package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class UnrecognizedEventException extends PublicDisplayRuntimeException {
    /**
     * Creates a new UnrecognizedEventException object.
     * 
     * @param event
     *            TODO:
     */
    public UnrecognizedEventException(PublicDisplayEvent event) {
        super("Event not recognized: " + event.getClass());
    }
}
