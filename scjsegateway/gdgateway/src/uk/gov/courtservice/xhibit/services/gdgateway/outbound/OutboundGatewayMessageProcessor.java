package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundUpdateStatusVO;
import uk.gov.courtservice.xhibit.database.gdgateway.OutboundGdGateDatabase;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientFactory;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertiesCache;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertyCodes;
import uk.gov.courtservice.xhibit.services.gdgateway.common.GdGatewayResponseCodes;

import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;

import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;

import java.util.Map;
import java.util.HashMap;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import java.text.ParseException;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title: Processes an Outbound Message record retrieved using the
 * RequestId passed from the OutboundGatewayMessageBean
 * </p>
 * <p>
 * Description: Retrieves the Outbound Message and CLOB from the database
 * using the requestId in the JMS message and then passes it on to the XHIBIT Web Service Client
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundGatewayMessageProcessor.java,v 1.11 2006/12/07 17:27:04 qz4rwx Exp $ Exp $
 */

public class OutboundGatewayMessageProcessor {

    private static final Logger log = CSServices.getLogger(OutboundGatewayMessageProcessor.class);

    private OutboundGdGateDatabase outboundDatabase = new OutboundGdGateDatabase();

    private ConfigPropertiesCache cache = ConfigPropertiesCache.getCache();

    private String connectionTimeout = null;

    private String readTimeout = null;

    /**
     * Method called to retrieve and process the Outbound Message record.
     */
    public void processMessage(Long requestId) throws Exception {
        log.info("Start processMessage, requestId:" + requestId);

        int responseCode = 0;

        try {
            if (requestId == null) {
                log.error("processMessage: requestId passed in was null");
                throw new CSUnrecoverableException("processMessage: requestId passed in was null");
            }

            if (log.isDebugEnabled()) {
                log.debug("processMessage requestId:" + requestId.toString());
            }

            OutboundMessageVO value = getOutboundMessage(requestId);

            if (log.isDebugEnabled() && value != null) {
                log.debug("OutboundMessageVO properties:" + value.toString());
            }

            String webServiceUrl = getWebServiceUrl();

            log.debug("serviceUrl:" + webServiceUrl);

            String payload = null;

            if (value != null) {
                payload = value.getClobData();
            } else {
                log.error("OutboundMessageVO representing Outbound Messages Record was null, requestId: " + requestId);
                throw new CSUnrecoverableException("OutboundMessageVO representing Outbound Messages Record was null");
            }

            Map<String, String> properties = setProperties(value);

            properties.put(OutboundConstants.SERVICE_URL, webServiceUrl);

            log.debug("Set the timeout properties");
            setTimeoutProperties(properties);

            log.debug("Set the Proxy properties");
            setProxyProperties(properties);

            responseCode = processSubmitResponse(executWebServiceClient(payload, properties), value.getRequestId());
        } catch (RemoteException re) {
            //Something wrong with the connection to the remote web service (eg an IOException).
            //Set record status as Error so it will be retried by the Message Generator
            try {
                log.warn("RemoteException caught from Web Service invocation and will be retried, requestId: "
                        + requestId + " exception: " + re);

                updateOutboundStatus(requestId, OutboundStatusCodes.Error, GdGatewayResponseCodes.Error,
                        "RemoteException");

                responseCode = GdGatewayResponseCodes.Error.getCode();
            } catch (Exception e) {
                //Unexpected exception - the transaction will rollback and will be retried
                log
                        .fatal("Exception caught from Web Service invocation following a RemoteException - throw as CSUnrecoverableException  : "
                                + requestId + " exception: " + e);
                responseCode = GdGatewayResponseCodes.FatalError.getCode();

                handleError(e);
                throw new CSUnrecoverableException(
                        "Unexpected exception in OutboundGatewayMessageBean following a RemoteException, requestId: "
                                + requestId, e);
            }
        } catch (ServiceException se) {
            //Something wrong with the WSDL so set record as Error and end processing            
            try {
                log
                        .error("ServiceException caught from Web Service invocation and is likely to be an issue with the WSDL, requestId: "
                                + requestId + " exception: " + se);
                updateOutboundStatus(requestId, OutboundStatusCodes.Fatal, GdGatewayResponseCodes.FatalError,
                        "ServiceException");

                responseCode = GdGatewayResponseCodes.FatalError.getCode();
            } catch (Exception e) {
                //Unexpected exception - the transaction will rollback and will be retried
                log
                        .fatal("Exception caught from Web Service invocation following a ServiceException - throw as CSUnrecoverableException  : "
                                + requestId + " exception: " + e);
                responseCode = GdGatewayResponseCodes.FatalError.getCode();

                handleError(e);
                throw new CSUnrecoverableException(
                        "Unexpected exception in OutboundGatewayMessageBean following a RemoteException, requestId: "
                                + requestId, e);
            }
        } catch (DataAccessException dae) {
            //Database failures - the transaction will rollback and will be retried
            log.fatal("DataAccessException caught from Web Service invocation throw as CSUnrecoverableException  : "
                    + requestId + " exception: " + dae);
            responseCode = GdGatewayResponseCodes.FatalError.getCode();

            handleError(dae);
            throw new CSUnrecoverableException("DataAccessException in OutboundGatewayMessageBean requestId: "
                    + requestId, dae);
        } catch (CSUnrecoverableException csue) {
            //Failure to retrieve mandatory properties like the Service URL or date formatting error
            log.fatal("CSUnrecoverableException caught from Web Service invocation rethrow  : " + requestId
                    + " exception: " + csue);
            responseCode = GdGatewayResponseCodes.FatalError.getCode();

            handleError(csue);
            throw csue;
        } catch (Exception e) {
            //Unexpected exception - the transaction will rollback and will be retried
            log.fatal("Exception caught from Web Service invocation throw as CSUnrecoverableException  : " + requestId
                    + " exception: " + e);
            responseCode = GdGatewayResponseCodes.FatalError.getCode();

            handleError(e);
            throw new CSUnrecoverableException("Unexpected exception in OutboundGatewayMessageBean requestId: "
                    + requestId, e);
        } finally {
            try {
                updateOutboundTrigger(requestId, responseCode);
            } catch (Exception e) {
                //Unexpected exception - not rethrown as if the original transaction succeeded we don't want to retry and if it failed the appropriate exception will be thrown anyway
                log
                        .warn("Exception caught when updating the Outbound Trigger following a Web Service invocation, Request Id:"
                                + requestId + ", Response Code:" + responseCode + " exception: " + e);
            }

            log.info("End processMessage");
        }
    }

    /**
     * Delegates to the CSServices.getDefaultErrorHandler to handle the error
     * @param Exception e
     */
    protected void handleError(Exception e) {
        CSServices.getDefaultErrorHandler().handleError(e, OutboundGatewayMessageBean.class);
    }

    /**
     * Method to retrieve the service Url from the database
     * Expected format is http://${wls.hostname}:${wls.port}/${ws_context_name}/${ws_name}
     * The ws_context_name may have several entries separated by /
     * @return String
     */
    protected String getWebServiceUrl() {
        String ScjseWsUrl = null;

        String ScjseWsIpAddr = getConfigProperty(ConfigPropertyCodes.SCJSE_WEB_IP_ADDR);
        String ScjseWsPort = getConfigProperty(ConfigPropertyCodes.SCJSE_WEB_PORT);
        String ScjseWsName = getConfigProperty(ConfigPropertyCodes.SCJSE_WEB_NAME);

        if (isStaticPropertiesFile()) {
            if (ScjseWsIpAddr == null || ScjseWsIpAddr.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.SCJSE_WEB_IP_ADDR
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                ScjseWsIpAddr = CSServices.getConfigServices().getProperty(
                        ConfigPropertyCodes.SCJSE_WEB_IP_ADDR.toString(), null);
            }

            if (ScjseWsPort == null || ScjseWsPort.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.SCJSE_WEB_PORT
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                ScjseWsPort = CSServices.getConfigServices().getProperty(ConfigPropertyCodes.SCJSE_WEB_PORT.toString(),
                        null);
            }

            if (ScjseWsName == null || ScjseWsName.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.SCJSE_WEB_NAME
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                ScjseWsName = CSServices.getConfigServices().getProperty(ConfigPropertyCodes.SCJSE_WEB_NAME.toString(),
                        null);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("***** ScjseWsIpAddr:" + ScjseWsIpAddr);
            log.debug("***** ScjseWsPort:" + ScjseWsPort);
            log.debug("***** ScjseWsName:" + ScjseWsName);
        }

        if (ScjseWsIpAddr == null || ScjseWsPort == null || ScjseWsName == null || ScjseWsIpAddr.trim().equals("")
                || ScjseWsPort.trim().equals("") || ScjseWsName.trim().equals("")) {
            log
                    .error("The URL properties for the remote Web Service (IP Address, Port and Name) have not been fully defined");
            throw new CSUnrecoverableException(
                    "The URL properties for the remote Web Service (IP Address, Port and Name) have not been fully defined");
        }

        ScjseWsUrl = "http://" + ScjseWsIpAddr.trim() + ":" + ScjseWsPort.trim() + "/" + ScjseWsName.trim();

        log.info("***** ScjseWsUrl:" + ScjseWsUrl);

        return ScjseWsUrl;
    }

    /**
     * Method to retrieve properties from the OutboundMessageVO
     * and add them to a hashmap     
     *
     * @param OutboundMessageVO value
     * @return Map
     */
    protected Map<String, String> setProperties(OutboundMessageVO value) {
        Map<String, String> properties = new HashMap<String, String>();

        if (value == null || value.getRequestId() == null) {
            log.error("OutboundMessageVO or RequestId on Outbound Messages Record was null");
            throw new CSUnrecoverableException("OutboundMessageVO or RequestId on Outbound Messages Record was null");
        }

        properties.put(OutboundConstants.REQUEST_ID, value.getRequestId().toString());
        properties.put(OutboundConstants.SOURCE_IDENTIFIER, value.getSourceIdentifier());
        properties.put(OutboundConstants.DESTINATION_IDENTIFIER, value.getDestinationIdentifier());
        properties.put(OutboundConstants.EXEC_MODE, value.getExecMode());

        try {
            properties.put(OutboundConstants.REQUEST_TIMESTAMP, DateTimeUtilities.convertOracleDate(value
                    .getRequestTimestamp()));
        } catch (ParseException pe) {
            log.error("ParseException for date: " + value.getRequestTimestamp()
                    + " in OutboundGatewayMessageBean for message with requestId: " + value.getRequestId() + pe);
            throw new CSUnrecoverableException("ParseException for date: " + value.getRequestTimestamp()
                    + " in OutboundGatewayMessageBean for message with requestId: " + value.getRequestId(), pe);
        }
        return properties;
    }

    /**
     * Method to set the ConnectionTimeout and ReadTimeout
     * @param Map 
     */
    protected void setTimeoutProperties(Map<String, String> properties) {

        setConnectionTimeout(getConfigProperty(ConfigPropertyCodes.WEB_SERVICE_CONNECTION_TIMEOUT));
        setReadTimeout(getConfigProperty(ConfigPropertyCodes.WEB_SERVICE_READ_TIMEOUT));

        if (isStaticPropertiesFile()) {
            if (getConnectionTimeout() == null || getConnectionTimeout().trim().equals("")
                    || getConnectionTimeout().equals("0")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.WEB_SERVICE_CONNECTION_TIMEOUT
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                setConnectionTimeout(CSServices.getConfigServices().getProperty(
                        ConfigPropertyCodes.WEB_SERVICE_CONNECTION_TIMEOUT.toString(), "0"));
            }

            if (getReadTimeout() == null || getReadTimeout().trim().equals("") || getReadTimeout().trim().equals("0")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.WEB_SERVICE_READ_TIMEOUT
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                setReadTimeout(CSServices.getConfigServices().getProperty(
                        ConfigPropertyCodes.WEB_SERVICE_READ_TIMEOUT.toString(), "0"));
            }
        }

        if (getConnectionTimeout() != null) {
            log.debug("Add the Connection Timeout property:" + getConnectionTimeout());
            properties.put(OutboundConstants.CONNECTION_TIMEOUT, getConnectionTimeout().trim());
        }

        if (getReadTimeout() != null) {
            log.debug("Add the Read Timeout property:" + getReadTimeout());
            properties.put(OutboundConstants.READ_TIMEOUT, getReadTimeout().trim());
        }
    }

    /**
     * Method to retrieve and process proxy variables.
     * Three of them (host name, port and user) are retrieved from the GDG_CONFIG_PROPERTIES
     * The fourth (password) will be retrieved from the internal_proxy_user in Weblogic     
     *
     * @param Map
     */
    protected void setProxyProperties(Map<String, String> properties) {
        String proxyHost = getConfigProperty(ConfigPropertyCodes.PROXY_HOST);
        String proxyPort = getConfigProperty(ConfigPropertyCodes.PROXY_PORT);
        String proxyUser = getConfigProperty(ConfigPropertyCodes.PROXY_USER);
        String proxyPassword = getProxyPassword();

        if (isStaticPropertiesFile()) {
            if (proxyHost == null || proxyHost.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.PROXY_HOST
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                proxyHost = CSServices.getConfigServices().getProperty(ConfigPropertyCodes.PROXY_HOST.toString(), null);
            }
            if (proxyPort == null || proxyPort.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.PROXY_PORT
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                proxyPort = CSServices.getConfigServices().getProperty(ConfigPropertyCodes.PROXY_PORT.toString(), null);
            }
            if (proxyUser == null || proxyUser.trim().equals("")) {
                if (log.isDebugEnabled()) {
                    log.debug("***** Config Property:" + ConfigPropertyCodes.PROXY_USER
                            + " not in GDG_CONFIG_PROPERTIES on the Database so try CSServices.getConfigServices");
                }
                proxyUser = CSServices.getConfigServices().getProperty(ConfigPropertyCodes.PROXY_USER.toString(), null);
            }
        }

        if (proxyHost == null || proxyPort == null || proxyHost.trim().equals("") || proxyPort.trim().equals("")) {
            log
                    .info("The mandatory proxy properties for the remote Web Service (Hostname and Port) have not been fully defined. This may be OK if it is intended not to go via a Proxy to the SCJSE region");
        }

        if (proxyHost != null && !proxyHost.trim().equals("")) {
            properties.put(OutboundConstants.PROXY_HOST, proxyHost.trim());
        }

        if (proxyPort != null && !proxyPort.trim().equals("")) {
            properties.put(OutboundConstants.PROXY_PORT, proxyPort.trim());
        }

        if (proxyUser != null) {
            properties.put(OutboundConstants.PROXY_USER, proxyUser.trim());
        } else if (proxyHost != null && !proxyHost.trim().equals("")) {
            log.info("Proxy Host specified with no user - add an empty string for the proxy user");
            properties.put(OutboundConstants.PROXY_USER, "");
        }

        if (proxyPassword != null) {
            properties.put(OutboundConstants.PROXY_PASSWORD, proxyPassword.trim());
        } else if (proxyHost != null && !proxyHost.trim().equals("")) {
            log.info("Proxy Host specified with no password - add an empty string for the proxy password");
            properties.put(OutboundConstants.PROXY_PASSWORD, "");
        }
    }

    /**
     * Sets the Proxy Password based on the value retrieved from the Weblogic startup proeprties
     * Unlike other properties this must NOT be retrieved from the database
     *
     * @return String
     */
    protected String getProxyPassword() {
        return CSServices.getConfigServices().getProperty(OutboundConstants.PROXY_PASSWORD_LOOKUP);
    }

    /**
     * Method to process the submitResponse.
     *
     * @param  SubmitResponse
     * @param  Long (requestId from the original submitRequest)
     * @return int (the submitResponseCode)
     */
    protected int processSubmitResponse(SubmitResponse submitResponse, Long requestId) {
        if (submitResponse == null) {
            log.error("FATAL Error in submitResponse returned but was null, requestId:" + requestId);
            updateOutboundStatus(requestId, OutboundStatusCodes.Fatal, GdGatewayResponseCodes.FatalError,
                    "submitResponse null");

            return GdGatewayResponseCodes.FatalError.getCode();
        }

        if (requestId == null) {
            log.error("FATAL Error in submitResponse returned but original requestId was null");
            updateOutboundStatus(requestId, OutboundStatusCodes.Fatal, GdGatewayResponseCodes.FatalError,
                    "original requestId null");

            return GdGatewayResponseCodes.FatalError.getCode();
        }

        if (log.isDebugEnabled()) {
            log.debug("[processSubmitResponse] Request Id    = " + submitResponse.getRequestID());
            log.debug("[processSubmitResponse] Response Code = " + submitResponse.getResponseCode());
            log.debug("[processSubmitResponse] Response Text = " + submitResponse.getResponseText());
        }

        Long submitResponseRequestId = new Long(submitResponse.getRequestID());
        int submitResponseCode = submitResponse.getResponseCode();
        String submitResponseText = submitResponse.getResponseText();

        if (submitResponseRequestId.longValue() != requestId.longValue()) {
            log.error("FATAL Error in submitResponse returned but requestId on response: " + submitResponseRequestId
                    + " did not match that on the submitRequest:" + requestId);
            updateOutboundStatus(requestId, OutboundStatusCodes.Fatal, GdGatewayResponseCodes.FatalError,
                    "submitResponseRequestId not the same as submitRequestId");

            return GdGatewayResponseCodes.FatalError.getCode();
        }

        if (submitResponseCode >= GdGatewayResponseCodes.Success.getCode()
                && submitResponseCode < GdGatewayResponseCodes.Error.getCode()) {
            log.debug("submitResponse returned with a successful response code");
            updateOutboundStatus(submitResponseRequestId, OutboundStatusCodes.Success, null, null);
        } else if (submitResponseCode >= GdGatewayResponseCodes.Error.getCode()
                && submitResponseCode < GdGatewayResponseCodes.FatalError.getCode()) {
            log.warn("submitResponse returned with a retriable response code");
            updateOutboundStatus(submitResponseRequestId, OutboundStatusCodes.Error, GdGatewayResponseCodes
                    .valueOf(submitResponseCode), submitResponseText);
        } else if (submitResponseCode >= GdGatewayResponseCodes.FatalError.getCode()) {
            log.error("submitResponse returned with a fatal response code, requestId:" + requestId);
            updateOutboundStatus(submitResponseRequestId, OutboundStatusCodes.Fatal, GdGatewayResponseCodes
                    .valueOf(submitResponseCode), submitResponseText);
        }

        log.debug("submitResponse code returned: " + submitResponseCode);
        return submitResponseCode;
    }

    /**
     * Reads the Outbound Messages record specified by the RequestId passed in
     * from the GD Gateway Database
     *
     * @param Long     
     * @return OutboundMessageVO
     */
    protected OutboundMessageVO getOutboundMessage(Long requestId) {
        return outboundDatabase.getMessageByRequestId(requestId);
    }

    /**
     * Executes the Web Service Client passing the payload string and properties map
     *
     * @param String payload, 
     * @param Map properties     
     * @return SubmitResponse
     */
    protected SubmitResponse executWebServiceClient(String payload, Map properties) throws RemoteException,
            ServiceException {
        ServiceWSClient serviceWSClient = ServiceWSClientFactory.getInstance().getServiceWSClient();
        return serviceWSClient.callSubmitRequest(payload, properties);
    }

    /**
     * Updates the Outbound Status Id of the Outbound Messages record specified by the RequestId passed in
     * on the GD Gateway Database
     * 
     *
     * @param Long
     * @param OutboundStatusCodes
     * @param GdGatewayResponseCodes
     * @param String
     */
    protected void updateOutboundStatus(Long requestId, OutboundStatusCodes outboundStatusCode,
            GdGatewayResponseCodes failureCode, String failureText) {
        OutboundUpdateStatusVO value = new OutboundUpdateStatusVO();

        value.setRequestId(requestId);
        if (outboundStatusCode != null) {
            value.setOutboundStatusCode(outboundStatusCode.getCode());
        }
        if (failureCode != null) {
            value.setFailureCode(new Integer(failureCode.getCode()).toString());
        }
        if (failureText != null && !failureText.trim().equals("")) {
            value.setFailureText(failureText);
        }

        updateOutboundMessageStatus(value);
    }

    /**
     * Updates the Outbound Message Status
     *    
     * @param OutboundUpdateStatusVO
     * @return void
     */
    protected void updateOutboundMessageStatus(OutboundUpdateStatusVO value) {
        outboundDatabase.updateOutboundMessageStatus(value);
    }

    /**
     * Gets the specified Config Property from the cache
     *    
     * @return String
     */
    protected String getConfigProperty(ConfigPropertyCodes configPropertyCode) {
        return cache.get(configPropertyCode);
    }

    /**
     * Determines the Outbound Trigger ItemTrackingInternalCode message
     *
     * @param Long
     * @param int
     */
    protected void updateOutboundTrigger(Long requestId, int responseCode) {
        log.debug("updateOutboundTrigger,  requestId: " + requestId + ", responseCode: " + responseCode);

        if (responseCode >= GdGatewayResponseCodes.Success.getCode()
                && responseCode < GdGatewayResponseCodes.Error.getCode()) {
            log.debug("responseCode was a successful response code");
            sendOutboundTriggerMessage(requestId, ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_OK);
        } else if (responseCode >= GdGatewayResponseCodes.Error.getCode()
                && responseCode < GdGatewayResponseCodes.FatalError.getCode()) {
            log.debug("responseCode was a retriable response code");
            sendOutboundTriggerMessage(requestId, ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_ERROR);
        } else if (responseCode >= GdGatewayResponseCodes.FatalError.getCode()) {
            log.debug("responseCode was a fatal response code");
            sendOutboundTriggerMessage(requestId, ItemTrackingInternalCode.SCJSE_MESSAGE_SENT_FATAL);
        }
    }

    /**
     * Sends a message to the ITEM TRACKING QUEUE
     *
     * @param requestId the request to update.
     * @param trackingCode the code to set for the request.
     */
    protected void sendOutboundTriggerMessage(Long requestId, ItemTrackingInternalCode trackingCode) {
        if (log.isDebugEnabled()) {
            log.debug("sendOutboundTriggerMessage, requestId:     " + requestId);
            log.debug("sendOutboundTriggerMessage, tracking code: " + trackingCode.toString());
        }

        CSServices.getJMSServices().send(
                new ItemTrackingMessageFactory(OutboundConstants.ITEM_TRACKING_QUEUE, requestId.longValue(),
                        trackingCode));
    }

    /**
     * Return false if the system should be totally reliant
     * on the database for static properties or whether they can
     * be retrieved from a properties file
     * It is useful to get properties from a file during testing
     * however it is expected that
     * properties be returned from the database in production so the
     * default will be false
     *     
     * @return boolean
     */
    protected boolean isStaticPropertiesFile() {
        String property = CSServices.getConfigServices().getProperty(
                OutboundConstants.SCJSE_SERVER_START_ARGUMENTS_ENABLED);
        if (property != null && property.equalsIgnoreCase("TRUE")) {
            log
                    .debug("Properties file (ie properties in the Weblogic startup script or on the JAVA_OPTIONS command line) is available");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the Connection Timeout
     * @return String connectionTimeout     
     */
    protected String getConnectionTimeout() {
        log.debug("getConnectionTimeout: " + connectionTimeout);
        return connectionTimeout;
    }

    /**
     * Sets the connectionTimeout
     * @param String connectionTimeout 
     */
    protected void setConnectionTimeout(String connectionTimeout) {
        log.debug("Set connectionTimeout: " + connectionTimeout);
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * Returns the readTimeout          
     * @return String readTimeout     
     */
    protected String getReadTimeout() {
        log.debug("get readTimeout: " + readTimeout);
        return readTimeout;
    }

    /**
     * Sets the readTimeout
     * @param String readTimeout 
     */
    protected void setReadTimeout(String readTimeout) {
        log.debug("Set readTimeout: " + readTimeout);
        this.readTimeout = readTimeout;
    }
}
