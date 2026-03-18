package uk.gov.courtservice.framework.services.errorhandling;

/**
 * <p>
 * Title:
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
 * @author Pete Raymond
 * @version 1.0
 */

public class ErrorHandlerType {
    String type;

    ErrorHandlerType(String typeDescription) {
        type = typeDescription;
    }

    public final String toString() {
        return type;
    }
}