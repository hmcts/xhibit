package echowebserviceclient;


/**
 * <p>
 * Title: Echo Web Service Client used to send an XHIBIT message to a remote Echo Web Service
 * and receieve a Response
 * 
 * </p>
 * <p>
 * Description: Structure containing the configurable properties
 * required for the echo test. These properties are retrieved from
 * EchoWebServiceClientProperties.txt at runtime, except the proxy password which must be
 * passed in on the command line
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
 * @version $Id: EchoConfigPropsStructure.java,v 1.3 2006/11/30 18:42:06 qz4rwx Exp $ Exp $
 */

public class EchoConfigPropsStructure implements java.io.Serializable {

    private String echoWebServiceIPAddress = null;

    private String echoWebServicePort = null;

    private String echoWebServiceName = null;

    private boolean echoWebServiceProxyEnabled = true;

    private String echoWebServiceProxyHost = null;

    private String echoWebServiceProxyPort = null;

    private String echoWebServiceProxyUser = null;

    private String echoWebServiceProxyPassword = null;

    private Integer connectionTimeout = null;

    private Integer readTimeout = null;

    public String getEchoWebServiceIPAddress() {
        return this.echoWebServiceIPAddress;
    }

    public void setEchoWebServiceIPAddress(String newEchoWebServiceIPAddress) {
        this.echoWebServiceIPAddress = newEchoWebServiceIPAddress;
    }

    public String getEchoWebServicePort() {
        return echoWebServicePort;
    }

    public void setEchoWebServicePort(String newEchoWebServicePort) {
        this.echoWebServicePort = newEchoWebServicePort;
    }

    public String getEchoWebServiceName() {
        return echoWebServiceName;
    }

    public void setEchoWebServiceName(String newEchoWebServiceName) {
        this.echoWebServiceName = newEchoWebServiceName;
    }

    public boolean isEchoWebServiceProxyEnabled() {
        return echoWebServiceProxyEnabled;
    }

    public void setEchoWebServiceProxyEnabled(String newEchoWebServiceProxyEnabled) {
        try {
            this.echoWebServiceProxyEnabled = new Boolean(newEchoWebServiceProxyEnabled.trim().toLowerCase())
                    .booleanValue();
        } catch (Exception e) {
            System.out
                    .println("WARNING echoWebServiceProxyEnabled defaulted to true as it was set to an invalid value (must be true or false):"
                            + newEchoWebServiceProxyEnabled);
            this.echoWebServiceProxyEnabled = false;
        }
    }

    public String getEchoWebServiceProxyHost() {
        return echoWebServiceProxyHost;
    }

    public void setEchoWebServiceProxyHost(String newEchoWebServiceProxyHost) {
        this.echoWebServiceProxyHost = newEchoWebServiceProxyHost;
    }

    public String getEchoWebServiceProxyPort() {
        return echoWebServiceProxyPort;
    }

    public void setEchoWebServiceProxyPort(String newEchoWebServiceProxyPort) {
        this.echoWebServiceProxyPort = newEchoWebServiceProxyPort;
    }

    public String getEchoWebServiceProxyUser() {
        return echoWebServiceProxyUser;
    }

    public void setEchoWebServiceProxyUser(String newEchoWebServiceProxyUser) {
        this.echoWebServiceProxyUser = newEchoWebServiceProxyUser;
    }

    public String getEchoWebServiceProxyPassword() {
        if (System.getProperty("echo.wsclient.proxy.password") != null
                && !System.getProperty("echo.wsclient.proxy.password").trim().equals("")) {
            setEchoWebServiceProxyPassword(System.getProperty("echo.wsclient.proxy.password").trim());
        } else {
            System.out
                    .println("WARNING Password has not been set and so will be null. This is OK if the proxy is not enabled, echoWebServiceProxyEnabled is:"
                            + isEchoWebServiceProxyEnabled());
        }

        return echoWebServiceProxyPassword;
    }

    public void setEchoWebServiceProxyPassword(String newEchoWebServiceProxyPassword) {
        this.echoWebServiceProxyPassword = newEchoWebServiceProxyPassword;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(String newConnectionTimeout) {
        try {
            this.connectionTimeout = new Integer(newConnectionTimeout.trim());
        } catch (Exception e) {
            System.out
                    .println("WARNING connectionTimeout defaulted to 0 as it was set to an invalid value (must be a numeric):"
                            + newConnectionTimeout);
            this.connectionTimeout = new Integer(0);
        }
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(String newReadTimeout) {
        try {
            this.readTimeout = new Integer(newReadTimeout.trim());
        } catch (Exception e) {
            System.out
                    .println("WARNING readTimeout defaulted to 0 as it was set to an invalid value (must be a numeric):"
                            + newReadTimeout);
            this.readTimeout = new Integer(0);
        }
    }
}
