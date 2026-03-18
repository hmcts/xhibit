package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.server;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.apache.log4j.Logger;

import weblogic.jws.*;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.CJSEServiceHelper;

/**
 * CJSEPortImpl class implements web service endpoint interface CJSEPort */

@WebService(
  serviceName="CJSEService",
  targetNamespace="http://schemas.cjse.gov.uk/endpoint/wsdl/",
  endpointInterface="uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.server.CJSEPort")
@WLHttpTransport(
  contextPath="delivery/services",
  serviceUri="Delivery",
  portName="Delivery")
@HandlerChain(file="CJSEServiceHandlerConfig.xml", name="CJSEServiceChain") 
public class CJSEPortImpl implements CJSEPort {

    private static final Logger log = CSServices.getLogger(CJSEPortImpl.class);
    
  public CJSEPortImpl() {

  }

  public uk.gov.cjse.schemas.endpoint.types.SubmitResponse submit(uk.gov.cjse.schemas.endpoint.types.SubmitRequest SubmitRequest)

  {
    log.info("submit: START");
    if (log.isDebugEnabled()) log.debug("\n\n  * * *   in submit   * * * \n\n");

    CJSEServiceHelper helper = new CJSEServiceHelper();
    log.info("submit: END");
     return helper.insertItemInbound(SubmitRequest);
  }

  public uk.gov.cjse.schemas.endpoint.types.RetrieveResponse retrieve(uk.gov.cjse.schemas.endpoint.types.RetrieveRequest RetrieveRequest)

  {
      log.error("retrieve called but has not been implemented!");
      return null;
  }
}