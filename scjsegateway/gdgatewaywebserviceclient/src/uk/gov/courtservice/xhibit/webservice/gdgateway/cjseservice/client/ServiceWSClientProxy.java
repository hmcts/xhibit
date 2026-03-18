package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import uk.gov.cjse.schemas.endpoint.types.SubmitRequest;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;

import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEPort;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEService;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.CJSEService_Impl;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;

import java.net.Proxy;
import java.net.InetSocketAddress;
import weblogic.wsee.connection.transport.http.HttpTransportInfo;

import java.util.Map;

/**
 * <p>
 * Title: Web Service Client used to send Outbound messages to a remote Web Service
 * via a firewall (ie using a server proxy)
 * 
 * </p>
 * <p>
 * Description: Creates a SubmitRequest using the properties and String passed in
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: ServiceWSClientProxy.java,v 1.3 2006/12/07 17:29:26 qz4rwx Exp $ Exp $
 */
public class ServiceWSClientProxy extends ServiceWSClient {
    private CJSEPort port;

    private String proxyHost = null;

    private String proxyPort = null;

    private String proxyUser = null;

    private String proxyPassword = null;

    /**
     * Calls a Web Service via a firewall (ie using a server proxy)
     * passing the message and properties in a SubmitRequest
     * and returning a SubmitResponse from the remote Web Service
     * @param String message
     * @param Map properties
     * @return SubmitResponse
     * @throws RemoteException, ServiceException
     */
    public SubmitResponse callSubmitRequest(String message, Map properties) throws RemoteException, ServiceException {
        log.debug("[ServiceWSClientProxy] callSubmit");
        return invokeWebService(setSubmitRequest(message, properties));
    }

    /**
     * Invokes a Web Service passing the message and properties in a SubmitRequest
     * @param  SubmitRequest
     * @param  String representing the serviceUrl 
     * @return SubmitResponse
     * @throws RemoteException, ServiceException    
     */
    protected SubmitResponse invokeWebService(SubmitRequest submitRequest) throws RemoteException, ServiceException {
        log.info("[ServiceWSClientProxy] invokeWebService");

        SubmitResponse submitResponse = new SubmitResponse();

        CJSEService service = new CJSEService_Impl();
        port = service.getDelivery();

        Stub stub = (Stub) port;

        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, getServiceUrl());

        if (getConnectionTimeout() != null) {
            log.debug("Set the Connection Timeout Property (weblogic.wsee.transport.connection.timeout) to:"
                    + getConnectionTimeout());
            stub._setProperty("weblogic.wsee.transport.connection.timeout", getConnectionTimeout());
        }

        if (getReadTimeout() != null) {
            log.debug("Set the Read Timeout Property (weblogic.wsee.transport.read.timeout) to:" + getReadTimeout());
            stub._setProperty("weblogic.wsee.transport.read.timeout", getReadTimeout());
        }

        log.debug("[ServiceWSClientProxy] Set proxy server info");

        Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), Integer.parseInt(getProxyPort())));
        HttpTransportInfo info = new HttpTransportInfo();
        info.setProxy(p);
        ((Stub) port)._setProperty("weblogic.wsee.connection.transportinfo", info);

        log.debug("[ServiceWSClientProxy] set proxy-authentication info ");

        ((Stub) port)._setProperty("weblogic.webservice.client.proxyusername", getProxyUser());
        ((Stub) port)._setProperty("weblogic.webservice.client.proxypassword", getProxyPassword());

        log.debug("[ServiceWSClientProxy] Execute remote web service thru firewall via proxy server");

        submitResponse = port.submit(submitRequest);

        if (log.isDebugEnabled() && submitResponse != null) {
            log.debug("[ServiceWSClient] Request Id = " + submitResponse.getRequestID());
            log.debug("[ServiceWSClient] Response Code = " + submitResponse.getResponseCode());
            log.debug("[ServiceWSClient] Response Text = " + submitResponse.getResponseText());
        }

        log
                .info("[ServiceWSClientProxy] callSubmit: Completed invocation of the CJSE Web Service thru a firewall via a proxy server and received a response");

        return submitResponse;
    }

    /**
     * Sets additional request properties
     * Does nothing by default but can be overridden in subclasses          
     * @param SubmitRequest
     * @param String propertyName
     * @param String propertyValue     
     */
    protected void setAdditionalProperties(SubmitRequest submitRequest, String propertyName, String propertyValue) {
        if (propertyName.trim().equals(OutboundConstants.PROXY_HOST)) {
            log.debug("***** Found the Proxy Host:" + propertyValue);
            setProxyHost(propertyValue);
        } else if (propertyName.trim().equals(OutboundConstants.PROXY_PORT)) {
            log.debug("***** Found the Proxy Port:" + propertyValue);
            setProxyPort(propertyValue);
        } else if (propertyName.trim().equals(OutboundConstants.PROXY_USER)) {
            log.debug("***** Found the Proxy User:" + propertyValue);
            setProxyUser(propertyValue);
        } else if (propertyName.trim().equals(OutboundConstants.PROXY_PASSWORD)) {
            log.debug("***** Found the Proxy Password(not displayed)");
            setProxyPassword(propertyValue);
        } else {
            log.debug("***** Unknown property found propertyName:" + propertyName + " propertyValue:" + propertyValue);
        }
    }

    /**
     * Returns the Proxy Host          
     * @return String proxyHost     
     */
    private String getProxyHost() {
        log.debug("[ServiceWSClientProxy] getProxyHost: " + proxyHost);
        return proxyHost;
    }

    /**
     * Sets the Proxy Host
     * @param String proxyHost 
     */
    private void setProxyHost(String proxyHost) {
        log.debug("[ServiceWSClientProxy] SetProxyHost: " + proxyHost);
        this.proxyHost = proxyHost;
    }

    /**
     * Returns the Proxy Port          
     * @return String proxyPort     
     */
    private String getProxyPort() {
        log.debug("[ServiceWSClientProxy] getProxyPort: " + proxyPort);
        return proxyPort;
    }

    /**
     * Sets the Proxy Port
     * @param String proxyPort 
     */
    private void setProxyPort(String proxyPort) {
        log.debug("[ServiceWSClientProxy] SetProxyPort: " + proxyPort);
        this.proxyPort = proxyPort;
    }

    /**
     * Returns the Proxy User          
     * @return String proxyUser     
     */
    private String getProxyUser() {
        log.debug("[ServiceWSClientProxy] getProxyUser: " + proxyUser);
        return proxyUser;
    }

    /**
     * Sets the Proxy User
     * @param String proxyUser 
     */
    private void setProxyUser(String proxyUser) {
        log.debug("[ServiceWSClientProxy] SetProxyUser: " + proxyUser);
        this.proxyUser = proxyUser;
    }

    /**
     * Returns the Proxy Password          
     * @return String proxyPassword     
     */
    private String getProxyPassword() {
        log.debug("[ServiceWSClientProxy] getProxyPassword(not displayed)");
        return proxyPassword;
    }

    /**
     * Sets the Proxy Password
     * @param String proxyPassword 
     */
    private void setProxyPassword(String proxyPassword) {
        log.debug("[ServiceWSClientProxy] SetProxyPassword(not displayed)");
        this.proxyPassword = proxyPassword;
    }
}
