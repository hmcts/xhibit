package uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server;

import javax.jws.WebService;
import weblogic.jws.*;
import javax.jws.HandlerChain;

/**
 * EchoPortImpl class implements web service endpoint interface EchoPort */

@WebService(
  serviceName="EchoService",
  targetNamespace="http://schemas.cjse.gov.uk/echo/wsdl/2006-08",
  endpointInterface="uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server.EchoPort")
@WLHttpTransport(
  contextPath="services",
  serviceUri="echo",
  portName="EchoServicePort")
@HandlerChain(file="EchoHandlerConfig.xml", name="EchoChain")

public class EchoPortImpl implements EchoPort {

  public EchoPortImpl() {}

  public java.lang.String echo(java.lang.String echoin)
  {
      return "Hello " + echoin + ", this is XHIBIT";
  }
}