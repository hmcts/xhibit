package uk.gov.courtservice.xhibit.business.services.version;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.exception.Message;
import uk.gov.courtservice.xhibit.business.vos.services.version.ComponentValue;

/**
 * <p>
 * Title: Version not found exception
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: VersionValueNotFoundException.java,v 1.1 2004/09/08 15:41:12
 *          sz0t7n Exp $
 */

public class VersionValueNotFoundException extends CSUnrecoverableException {
	
	static final long serialVersionUID = -920814352452430515L;

    /**
     * Basic Constructor.
     */
    public VersionValueNotFoundException() {
        super();
    }

    /**
     * Complex constructor.
     * 
     * @param message
     *            A message explaining the exception.
     * @param cause
     *            The root cause of the problem.
     */
    public VersionValueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Complex constructor.
     * 
     * @param courtRoomId
     *            Court room id.
     * @param cause
     *            The root cause of the problem.
     */
    public VersionValueNotFoundException(Message message, ComponentValue component) {
        super(message, "Component version not found for:" + component.getComponentName());
    }

}