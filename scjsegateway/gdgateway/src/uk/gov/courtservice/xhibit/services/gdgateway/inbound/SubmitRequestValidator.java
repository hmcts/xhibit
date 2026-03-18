package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import uk.gov.cjse.schemas.endpoint.types.ExecMode;
import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertiesCache;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertyCodes;

import static uk.gov.courtservice.xhibit.services.gdgateway.inbound.MdiResponseCode.*;

/**
 * <p>
 * Title: SubmitRequestValidator
 * </p>
 * <p>
 * Description: Class to assist in validation of Submit Request
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author BSB
 * @version $Id: SubmitRequestValidator.java,v 1.3 2006/10/17 12:03:49 szfnvt Exp $
 */
public class SubmitRequestValidator {

    private static final ConfigPropertiesCache cache = ConfigPropertiesCache.getCache();

    private static final int REQUEST_ID_MAX_LENGTH = 128;

    private static final int SOURCE_ID_MAX_LENGTH = 255;

    public static MdiResponseCode validateSubmitRequest(SubmitRequest submitRequest) {
        
        final MdiResponseCode rcRequestId = validateRequestId(submitRequest.getRequestID());
        if (validationFailed(rcRequestId)) {
            return rcRequestId;
        }

        final MdiResponseCode rcSourceId = validateSourceId(submitRequest.getSourceID());
        if (validationFailed(rcSourceId)) {
            return rcSourceId;
        }

        final MdiResponseCode rcDestinationId = validateDestinationId(submitRequest.getDestinationID());
        if (validationFailed(rcDestinationId)) {
            return rcDestinationId;
        }

        final MdiResponseCode rcExecMode = validateExecMode(submitRequest.getExecMode());
        if (validationFailed(rcExecMode)) {
            return rcExecMode;
        }

        final MdiResponseCode rcMessage = validateMessage(submitRequest.getMessage());
        if (validationFailed(rcMessage)) {
            return rcMessage;
        }

        return SUCCESS;
    }

    /**
     * Checks if the given <code>MdiResponseCode</code> is a successful
     * response code
     * 
     * @param rcRequestId
     *            The <code>MdiResponseCode</code> to check
     * @return true if the given <code>MdiResponseCode</code> is not a
     *         successful response code.
     */
    private static boolean validationFailed(final MdiResponseCode rcRequestId) {
        return rcRequestId != SUCCESS;
    }

    private static MdiResponseCode validateRequestId(final String requestId) {
        if (emptyString(requestId)) {
            return REQUEST_ID_MISSING;
        } 
        if (requestId.trim().length() > REQUEST_ID_MAX_LENGTH) {
            return REQUEST_ID_INVALID;
        }
        return SUCCESS;
    }

    private static MdiResponseCode validateSourceId(final String sourceId) {
        //Check if missing Source Id
        if (emptyString(sourceId)) {
            return SOURCE_ID_MISSING;
        } 
        //Check if Source Id greater than Max allowed.
        if (sourceId.trim().length() > SOURCE_ID_MAX_LENGTH) {
            return SOURCE_ID_INVALID;
        } 
        
        //Check if Source Id equals cached XHB_LOCATION_ID
        if (sourceId.trim().equals(cache.get(ConfigPropertyCodes.XHB_LOCATION_ID))) {
            return SOURCE_ID_COURT_SERVICE_HUB;
        }
        //Check if Source Id equals cached SCJSE_LOCATION_ID
        if (!sourceId.trim().equals(cache.get(ConfigPropertyCodes.SCJSE_LOCATION_ID))) {
            return SOURCE_ID_INVALID;
        } 
        return SUCCESS;
    }

    private static MdiResponseCode validateDestinationId(final String[] destinationIds) {
        if (destinationIds == null || destinationIds.length == 0 || emptyString(destinationIds[0])) {
            return DESTINATION_ID_MISSING;
        }
        if (destinationIds.length > 1) {
            return DESTINATION_ID_MANY;
        }
        if (!destinationIds[0].equals(cache.get(ConfigPropertyCodes.XHB_LOCATION_ID))) {
            return DESTINATION_ID_INVALID;
        }
        return SUCCESS;
    }

    private static MdiResponseCode validateExecMode(final ExecMode execMode) {
        if (execMode == null) {
            return EXEC_MODE_ID_MISSING;
        } 
        if (execMode != ExecMode.asynch) {
            return EXEC_MODE_ID_INVALID;
        }
        return SUCCESS;
    }

    private static MdiResponseCode validateMessage(final String message) {
        return emptyString(message) ? MESSAGE_MISSING : SUCCESS;
    }

    /**
     * Tests if the given <code>String</code> is null or empty.
     * 
     * @param str
     *            the <code>String</code> to check.
     * @return true if the given <code>String</code> is null or empty.
     */
    private static boolean emptyString(final String str) {
        return str == null || str.trim().equals("");
    }

}
