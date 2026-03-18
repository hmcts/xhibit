package uk.gov.courtservice.xhibit.services.gdgateway.common;

/**
 * <p>
 * Title: Enum that contains the valid Property Codes on the database table
 * GDG_CONFIG_PROPERTIES
 * </p>
 * <p>
 * Description: Valid Property Codes on GDG_CONFIG_PROPERTIES that can used to
 * retrieve the associated Property Value once the properties have been
 * retrieved from the database.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Simon Gilmore,
 * @version $Id: ConfigPropertyCodes.java,v 1.6 2007/03/06 16:00:22 szn20z Exp $
 */
public enum ConfigPropertyCodes {
    CONFIG_PROPERTIES_REFRESH_TIME,
    MAX_LOCK_ATTEMPTS,
    MAX_MSG_ATTEMPTS,
    MAX_MSG_CONSEC_FAIL_COUNT,
    MSG_CONSEC_FAIL_COUNT,
    MSG_MALFORMED_XML_FAIL_COUNT,
    MSG_MALFORMED_XML_FAIL_LIMIT,
    PROXY_HOST,
    PROXY_PORT,
    PROXY_USER,
    SCJSE_LOCATION_ID,
    SCJSE_MSG_IMPL_CLASS,
    SCJSE_WEB_IP_ADDR,
    SCJSE_WEB_NAME,
    SCJSE_WEB_PORT,
    SERV_AVAIL_BATCH_SIZE,
    SERV_AVAIL_MSG_RETRY_FREQ,
    SERV_AVAIL_MSG_SEND_FREQ,
    SERV_CURRENT_DELAY,
    SERV_UNAVAIL_BATCH_SIZE,
    SERV_UNAVAIL_MSG_RETRY_FREQ,
    SERV_UNAVAIL_MSG_SEND_FREQ,
    SUCCESSFUL_STORAGE_TIME,
    UNSUCCESSFUL_STORAGE_TIME,
    WEB_SERVICE_CONNECTION_TIMEOUT,
    WEB_SERVICE_READ_TIMEOUT,
    XHB_LOCATION_ID;
}
