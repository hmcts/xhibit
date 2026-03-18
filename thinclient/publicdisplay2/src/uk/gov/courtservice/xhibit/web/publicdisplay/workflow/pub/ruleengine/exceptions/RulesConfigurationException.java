package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.exceptions;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;

/**
 * <p>
 * Title: RulesConfigurationException
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class RulesConfigurationException extends PublicDisplayRuntimeException {

    public RulesConfigurationException() {
        super("An error occurred loading the rules configuration");
    }

    public RulesConfigurationException(String message) {
        super(message);
    }

    public RulesConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RulesConfigurationException(Throwable cause) {
        super(cause);
    }
}