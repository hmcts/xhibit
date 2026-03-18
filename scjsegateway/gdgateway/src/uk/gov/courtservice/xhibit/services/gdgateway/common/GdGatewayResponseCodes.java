package uk.gov.courtservice.xhibit.services.gdgateway.common;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: Enum that contains valid SCJSE Response Codes
 * </p>
 * <p>
 * Description: Valid SCJSE codes that can be sent from the SCJSE into the
 * Gateway.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rob Sumner,
 * @version $Id: GdGatewayResponseCodes.java,v 1.4 2006/08/24 14:07:09 jzj6wd Exp $
 */

/**
 * Initialise Enum with acceptable values and their associated codes.
 */
public enum GdGatewayResponseCodes {
    Success(1), HasMore(102), Empty(103), Error(200), TemporaryProblem(201), ServerFailure(202), FatalError(300), WrongSource(
            304), WrongDestination(305), InvalidRequestId(306), ModeError(307), FutureMessage(308), RetrieveNotAvailable(
            309);

    GdGatewayResponseCodes(int code) {
        this.code = code;
    }

    /**
     * Create a hashmap mapping the code values against the associated enum.
     */
    public static final Map<Integer, GdGatewayResponseCodes> gdGatewayResponseCodesMap = new HashMap<Integer, GdGatewayResponseCodes>();
    static {
        for (GdGatewayResponseCodes gdGatewayResponseCodes : GdGatewayResponseCodes.values()) {
            gdGatewayResponseCodesMap.put(gdGatewayResponseCodes.code, gdGatewayResponseCodes);
        }
    }

    private final int code;

    public int getCode() {
        return code;
    }

    /**
     * Custom toString method return Enum name and value.
     */
    public String toString() {
        return this.name() + "(" + code + ")";
    }

    /**
     * Method to allow GdGatewayResponseCodes Enum usage from the value/code.
     * 
     * @param code
     * @return GdGatewayResponseCodes
     */
    public static GdGatewayResponseCodes valueOf(int code) {
        GdGatewayResponseCodes gdGatewayResponseCodes = gdGatewayResponseCodesMap.get(code);
        if (gdGatewayResponseCodes != null) {
            return gdGatewayResponseCodes;
        }
        throw new IllegalArgumentException("code: " + code);
    }
}
