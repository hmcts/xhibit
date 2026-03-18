package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: Enum that contains valid SCJSE Outbound Status Codes
 * </p>
 * <p>
 * Description: Valid Outbound Status Codes used to update the Outbound Messages
 * record in the GD Gate Database
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS,
 * @version $Id: OutboundStatusCodes.java,v 1.2 2006/10/03 11:37:48 qz4rwx Exp $
 */

/**
 * Initialise Enum with acceptable values and their associated codes.
 */
public enum OutboundStatusCodes {
    Create("NEW"), Success("SUCCESS"), Error("ERROR"), Fatal("FATAL");

    OutboundStatusCodes(String code) {
        this.code = code;
    }

    /**
     * Create a hashmap mapping the code values against the associated enum.
     */
    public static final Map<String, OutboundStatusCodes> outboundStatusCodesMap = new HashMap<String, OutboundStatusCodes>();
    static {
        for (OutboundStatusCodes outboundStatusCodes : OutboundStatusCodes.values()) {
            outboundStatusCodesMap.put(outboundStatusCodes.code, outboundStatusCodes);
        }
    }

    private final String code;

    public String getCode() {
        return code;
    }

    /**
     * Custom toString method return Enum name and value.
     */
    public String toString() {
        return this.name() + "(" + code + ")";
    }
}

