package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import java.util.Iterator;

import javax.xml.namespace.QName;

import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;


/**
 * This class implements a handler in the handler chain, used to access the SOAP
 * request and response message from the Web Service Client.
 * <p>
 * This class extends the <code>javax.xml.rpc.handler.GenericHandler</code>
 * abstract classs and simply prints the SOAP request and response messages to
 * the server log file before the messages are processed by the backend
 * Java class that implements the Web Service itself.
 */
public class CJSEClientHandler extends GenericHandler
{
    private HandlerInfo handlerInfo;
    private ClientContentHandler contentHandler;
   
    private static final Logger log = CSServices.getLogger(CJSEClientHandler.class);

    /**
     *  Initializes the instance of the handler.  Creates a nonCatalogLogger to
     *  log messages to.
     */
    public void init(HandlerInfo hi)
    {
        log.debug("init");
        handlerInfo = hi;
    }

    /**
     * Specifies that the SOAP request message be logged to a log file before the
     * message is sent to the Java class that implements the Web Service.
     */
    public boolean handleRequest(MessageContext context)
    {
        if(SoapHandlerHelper.isWSClientFileLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEClientHandler handleRequest");
            log.debug("******************************************************");
            
            processMessage(context,"Inbound_Deliver_Submit_Request");
            
            log.debug("******************************************************");
            log.info("End CJSEClientHandler handleRequest");
            log.debug("******************************************************");
        }
        
        return true;
     }

    private ClientContentHandler getHandler() throws SAXException
    {
      log.debug("getContentHandler:START");
      if (contentHandler == null)
      {
         contentHandler = new ClientContentHandler();
      }
      log.debug("getContentHandler:END");
      return contentHandler;
    }

    /**
     * Specifies that the SOAP response message be logged to a log file before the
     * message is sent back to the client application that invoked the Web
     * service.
     */
    public boolean handleResponse(MessageContext context)
    {
        if(SoapHandlerHelper.isWSClientFileLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEClientHandler handleResponse");
            log.debug("******************************************************");

            processMessage(context,"Inbound_Deliver_Submit_Response");

            log.debug("******************************************************");
            log.info("End CJSEClientHandler handleResponse");
            log.debug("******************************************************");
        }

        return true;
    }

    /**
     * Specifies that a message be logged to the log file if a SOAP fault is
     * thrown by the Handler instance.
     */
    public boolean handleFault(MessageContext context)
    {
        if(SoapHandlerHelper.isWSClientFileErrorLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEClientHandler handleFault");
            log.debug("******************************************************");

            processMessage(context,"SCJSE_Submit_Error");

            log.debug("******************************************************");
            log.info("End CJSEClientHandler handleFault");
            log.debug("******************************************************");
        }

        return true;
    }

    /**
     * Prints the message to log
     */
    public void processMessage(MessageContext context, String messageType)
    {  
        try
        {
            if(context==null)
            {
                log.warn(messageType + " context was null");
                return;
            }
            
            SOAPMessageContext messageContext = (SOAPMessageContext) context;

            if(log.isDebugEnabled())
            {
                log.debug(messageType + ":" + messageContext.getMessage().toString());
            }

            SOAPMessage soapMessage =  messageContext.getMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
        
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            
            String xmlString ="";

            Iterator iter = soapBody.getChildElements();
            if(iter.hasNext())
            {
                Node node = (Node)iter.next();
                DOMSource domSource = new DOMSource(node);
                xmlString = DocumentHelper.getDocumentAsXml(domSource);
            }

            log.debug("xmlString soapPart = \n\n " + xmlString);

           
            String requestId = null;
            
            if (xmlString.toString().indexOf("<typ:RequestID>") >= 0 &&
                xmlString.toString().indexOf("</typ:RequestID>") >= 0    )
            {
                log.debug("xmlString contains a RequestID");
                
                int startRequestId = xmlString.indexOf("<typ:RequestID>") + 15;
                int endRequestId   = xmlString.indexOf("</typ:RequestID>");
                
                requestId = xmlString.substring(startRequestId, endRequestId);   
                
                log.debug("requestId:" + requestId);
            }

            if(SoapHandlerHelper.isForceWSClientFileLogging())
            {
                //Designed to force logging from the real WS Client (outbound) which you would not normally do
                //This class is deployed to the Steria Stub (which does log for inbound messages) as well as the real WS Client
                log.debug("Force outbound WS Client to log messages to file");
                if(messageType.indexOf("Request")!=-1)
                {
                    messageType="Outbound_Receive_Submit_Request";
                }
                else if(messageType.indexOf("Response")!=-1)
                {
                    messageType="Outbound_Receive_Submit_Response";
                }
                
                SoapHandlerHelper.createSoapFile(soapMessage,messageType,requestId);
                return;
            }            
            
            if ((xmlString.toString().indexOf("OriginalExceptionData") == -1 &&
                 xmlString.toString().indexOf("ReceiveRequest") >= 0) ||
                xmlString.toString().indexOf("<typ:ResponseText>Outbound") >= 0)
            {
                //This relies on the fact that in testing the ResponseText of the
                //outbound submit response is prefixed by Outbound
                //This means outbound web service client logging is always suppressed
                //so all logging will always be relative to the stub
                //We still want to print out inbound exceptions that resulted from
                //outbound messages which is why the OriginalExceptionData check is done
                //(this will be the original ReceiveRequest in an inbound exception)
                log.debug("Message was a ReceiveRequest submitRequest or submitResponse so don't log it");
                return;
            }
            
            SoapHandlerHelper.createSoapFile(soapMessage,messageType,requestId);
        }
        catch(TransformerConfigurationException tce)
        {
            log.warn(messageType + " TransformerConfigurationException " + tce);
        }
        catch(TransformerException te)
        {
            log.warn(messageType + " TransformerException " + te);
        }
        catch(SOAPException e)
        {
            log.warn(messageType + " SOAP error " + e);
        }
    }

    public QName[] getHeaders()
    {
        return handlerInfo.getHeaders();
    }
}
