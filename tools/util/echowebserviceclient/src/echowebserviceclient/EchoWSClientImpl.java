package echowebserviceclient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.rmi.RemoteException;

import javax.xml.rpc.Stub;
import java.util.HashMap;
import java.util.Iterator;
import javax.jws.HandlerChain;

import weblogic.wsee.connection.transport.http.HttpTransportInfo;

/**
 * <p>
 * Title: Echo Web Service Client used to send an XHIBIT message to a remote Echo Web Service
 * and receieve a Response
 * 
 * </p>
 * <p>
 * Description: Creates a String message and sends it to a remote Echo Web Service
 * printing the original message, the response and any errors to file
 * 
 * The message can be sent via a Proxy or direct depending on the value of the configurable
 * property echoWebServiceProxyEnabled, which by default will be true
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
 * @version $Id: EchoWSClientImpl.java,v 1.4 2006/11/30 18:42:42 qz4rwx Exp $ Exp $
 */

@HandlerChain(file = "EchoClientHandlerConfig.xml", name = "EchoClientChain")
public class EchoWSClientImpl {
    private EchoConfigPropsStructure echoConfigPropsStructure = new EchoConfigPropsStructure();

    private EchoPort port;

    public void callSubmit(String serviceUrl, String message) throws RemoteException {
        String submitRequest = null;
        String submitResponse = null;

        try {
            System.out.println("[EchoWSClientImpl] callSubmit");

            submitRequest = setSubmitRequest(message);

            System.out.println("<< configurable property, echoWebServiceIPAddress:"
                    + echoConfigPropsStructure.getEchoWebServiceIPAddress());
            System.out.println("<< configurable property, echoWebServicePort:     "
                    + echoConfigPropsStructure.getEchoWebServicePort());
            System.out.println("<< configurable property, echoWebServiceName:     "
                    + echoConfigPropsStructure.getEchoWebServiceName());

            if (echoConfigPropsStructure.getEchoWebServiceIPAddress() != null
                    && echoConfigPropsStructure.getEchoWebServicePort() != null
                    && echoConfigPropsStructure.getEchoWebServiceName() != null) {
                serviceUrl = "http://" + echoConfigPropsStructure.getEchoWebServiceIPAddress() + ":"
                        + echoConfigPropsStructure.getEchoWebServicePort() + "/"
                        + echoConfigPropsStructure.getEchoWebServiceName();
            } else {
                System.out.println("[EchoWSClientImpl] ServiceUrl not defined in properties file so use default: "
                        + serviceUrl);
            }

            System.out.println("<< configurable property, isEchoWebServiceProxyEnabled:"
                    + echoConfigPropsStructure.isEchoWebServiceProxyEnabled());

            if (!echoConfigPropsStructure.isEchoWebServiceProxyEnabled()) {
                submitResponse = invokeWebServiceBypassingProxy(serviceUrl, submitRequest);
            } else {
                submitResponse = invokeWebServiceViaProxy(serviceUrl, submitRequest);
            }

            if (submitResponse != null) {
                System.out.println("[EchoWSClientImpl] Response = " + submitResponse);
            }

            System.out
                    .println("[EchoWSClientImpl] callSubmit: Completed invocation of the Echo Web Service and received a response");

        } catch (Exception e) {
            System.out.println("[EchoWSClientImpl] callSubmit:ERROR Exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Calls the remote Echo Web Service without going via the Proxy
     * 
     * @param String
     *            (serviceUrl)
     * @param String
     *            (submitRequest)
     * @return String (submitResponse)
     */
    private String invokeWebServiceBypassingProxy(String serviceUrl, String submitRequest) throws Exception {
        System.out
                .println("[EchoWSClientImpl] Call Echo Web Service (bypassing Proxy) using serviceUrl: " + serviceUrl);

        EchoService service = new EchoService_Impl();
        port = service.getEchoServicePort();

        Stub stub = (Stub) port;
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        return port.echo(submitRequest);
    }

    /**
     * Calls the remote Echo Web Service via the Proxy
     * 
     * @param String
     *            (serviceUrl)
     * @param String
     *            (submitRequest)
     * @return String (submitResponse)
     */
    private String invokeWebServiceViaProxy(String serviceUrl, String submitRequest) throws Exception {
        System.out.println("[EchoWSClientImpl] Call Echo Web Service (via Proxy) using serviceUrl: " + serviceUrl);

        if (echoConfigPropsStructure.getEchoWebServiceProxyPassword() == null
                || echoConfigPropsStructure.getEchoWebServiceProxyPassword().trim().equals("")) {
            throw new Exception("Proxy is enabled but Proxy Password not set");
        }

        EchoService service = new EchoService_Impl();
        port = service.getEchoServicePort();

        Stub stub = (Stub) port;
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);

        if (echoConfigPropsStructure.getConnectionTimeout() != null) {
            System.out.println("Set the Connection Timeout Property (weblogic.wsee.transport.connection.timeout) to:"
                    + echoConfigPropsStructure.getConnectionTimeout());
            stub._setProperty("weblogic.wsee.transport.connection.timeout", echoConfigPropsStructure
                    .getConnectionTimeout());
        }

        if (echoConfigPropsStructure.getReadTimeout() != null) {
            System.out.println("Set the Read Timeout Property (weblogic.wsee.transport.read.timeout) to:"
                    + echoConfigPropsStructure.getReadTimeout());
            stub._setProperty("weblogic.wsee.transport.read.timeout", echoConfigPropsStructure.getReadTimeout());
        }

        System.out.println("<< configurable property, echoWebServiceProxyHost:"
                + echoConfigPropsStructure.getEchoWebServiceProxyHost());
        System.out.println("<< configurable property, echoWebServiceProxyPort:"
                + echoConfigPropsStructure.getEchoWebServiceProxyPort());
        System.out.println("<< configurable property, echoWebServiceProxyUser:"
                + echoConfigPropsStructure.getEchoWebServiceProxyUser());

        Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(echoConfigPropsStructure
                .getEchoWebServiceProxyHost(), Integer.parseInt(echoConfigPropsStructure.getEchoWebServiceProxyPort())));
        HttpTransportInfo info = new HttpTransportInfo();
        info.setProxy(p);
        ((Stub) port)._setProperty("weblogic.wsee.connection.transportinfo", info);

        System.out.println("[EchoWSClientImpl] set proxy-authentication info ");

        ((Stub) port)._setProperty("weblogic.webservice.client.proxyusername", echoConfigPropsStructure
                .getEchoWebServiceProxyUser());
        ((Stub) port)._setProperty("weblogic.webservice.client.proxypassword", echoConfigPropsStructure
                .getEchoWebServiceProxyPassword());

        System.out.println("[EchoWSClientImpl] Execute remote web service thru firewall via proxy server");

        return port.echo(submitRequest);
    }

    /**
     * Builds a test echo request message using data retrieved from a file Only
     * the body is required (which will be a String). The URL properties of the
     * remote Web Service can also be specified in the file
     * 
     * @param String
     *            (message)
     * @return String (submitRequest)
     */
    private String setSubmitRequest(String message) throws Exception {
        String submitRequest = message;

        String propertyName = null;
        String propertyValue = null;

        HashMap echoWSClientPropertiesMap = EchoWSClientProperties.getEchoProperties();

        Iterator it = echoWSClientPropertiesMap.keySet().iterator();

        while (it.hasNext()) {
            propertyName = (String) it.next();
            System.out.println("Echo WS Client propertyName: " + propertyName);

            propertyValue = (String) echoWSClientPropertiesMap.get(propertyName);
            System.out.println("Echo WS Client propertyValue: " + propertyValue);

            if (propertyName.trim().equals("ProgressDocumentBody") && message == null) {
                System.out.println("***** Found the body for the Echo WS Client and no message specified:"
                        + propertyValue);
                submitRequest = propertyValue;
            } else if (propertyName.trim().equals("EchoWebServiceIPAddress")) {
                System.out.println("***** Found the EchoWebServiceIPAddress for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceIPAddress(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServicePort")) {
                System.out.println("***** Found the EchoWebServicePort for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServicePort(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceName")) {
                System.out.println("***** Found the EchoWebServiceName for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceName(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceProxyEnabled")) {
                System.out
                        .println("***** Found the EchoWebServiceProxyEnabled for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceProxyEnabled(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceProxyHost")) {
                System.out.println("***** Found the EchoWebServiceProxyHost for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceProxyHost(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceProxyPort")) {
                System.out.println("***** Found the EchoWebServiceProxyPort for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceProxyPort(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceProxyUser")) {
                System.out.println("***** Found the EchoWebServiceProxyUser for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setEchoWebServiceProxyUser(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceConnectionTimeout")) {
                System.out.println("***** Found the EchoWebServiceConnectionTimeout for the Echo WS Client:"
                        + propertyValue);
                echoConfigPropsStructure.setConnectionTimeout(propertyValue);
            } else if (propertyName.trim().equals("EchoWebServiceReadTimeout")) {
                System.out.println("***** Found the EchoWebServiceReadTimeout for the Echo WS Client:" + propertyValue);
                echoConfigPropsStructure.setReadTimeout(propertyValue);
            }
        }
        return submitRequest;
    }
}
