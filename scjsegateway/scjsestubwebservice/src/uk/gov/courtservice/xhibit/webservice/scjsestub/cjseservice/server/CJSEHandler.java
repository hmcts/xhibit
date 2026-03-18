package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import java.util.Calendar;
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
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;

import org.apache.log4j.Logger;

/**
 * This class implements a handler in the handler chain, used to access the SOAP
 * request and response message.
 * <p>
 * This class extends the <code>javax.xml.rpc.handler.GenericHandler</code>
 * abstract classs and simply prints the SOAP request and response messages to
 * the server log file before the messages are processed by the backend
 * Java class that implements the Web Service itself.
 */
public class CJSEHandler extends GenericHandler
{
    private HandlerInfo handlerInfo;
    private ServerContentHandler contentHandler;
    
    private static final Logger log = CSServices.getLogger(CJSEHandler.class);

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
        if(SoapHandlerHelper.isWSFileLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEHandler handleRequest");
            log.debug("******************************************************");

            processMessage(context,"Outbound_Receive_Submit_Request");

            log.debug("******************************************************");
            log.info("End CJSEHandler handleRequest");
            log.debug("******************************************************");
        }

        return true;
     }

    private ServerContentHandler getHandler() throws SAXException
    {
      log.debug("getContentHandler:START");
      if (contentHandler == null)
      {
         contentHandler = new ServerContentHandler();
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
        if(SoapHandlerHelper.isWSFileLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEHandler handleResponse");
            log.debug("******************************************************");

            processMessage(context,"Outbound_Receive_Submit_Response");

            log.debug("******************************************************");
            log.info("End CJSEHandler handleResponse");
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
        if(SoapHandlerHelper.isWSFileErrorLogging())
        {
            log.debug("******************************************************");
            log.info("Start CJSEHandler handleFault");
            log.debug("******************************************************");

            processMessage(context,"Outbound_Receive_Submit_Error");

            log.debug("******************************************************");
            log.info("End CJSEHandler handleFault");
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
                xmlString.toString().indexOf("</typ:RequestID>") >= 0)
            {
                log.debug("xmlString contains a RequestID");
                
                int startRequestId = xmlString.indexOf("<typ:RequestID>") + 15;
                int endRequestId   = xmlString.indexOf("</typ:RequestID>");
                
                requestId = xmlString.substring(startRequestId, endRequestId);   
                
                log.debug("requestId:" + requestId);
            }
            
            SoapHandlerHelper.createSoapFile(soapMessage,messageType,requestId);

            XMLServices xmlServices = XMLServicesImpl.getInstance();
            xmlString = xmlServices.decodeXML(xmlString);
            
            String directoryName = SoapHandlerHelper.determineDirectoryName(messageType);

            String receiveRequestMessageName = null;
            
            if(messageType.equals("Outbound_Receive_Submit_Error"))
            {
                receiveRequestMessageName = "Outbound_Receive_Request_Error";
            }
            else
            {
                receiveRequestMessageName = "Outbound_Receive_Request_Message";
            }
  
            String receiveRequestMessage = null;

            if (xmlString.toString().indexOf("<typ:Message>") >= 0 &&
                xmlString.toString().indexOf("</typ:Message>") >= 0)
            {
                log.debug("xmlString contains a receiveRequestMessage");
                  
                int startMessage = xmlString.indexOf("<typ:Message>") + 13;
                int endMessage   = xmlString.indexOf("</typ:Message>");
                    
                receiveRequestMessage = xmlString.substring(startMessage, endMessage); 
            }
            
            if(receiveRequestMessage!=null)
            {
                log.debug("receiveRequestMessage not null so write it to file");
                FileHelper.logDataToXmlFile(receiveRequestMessage,
                    receiveRequestMessageName,
                    requestId,
                    null,
                    directoryName);
            }
            
            String businessMessage = null;

            String businessMessageName = null;
            
            if(messageType.equals("Outbound_Receive_Submit_Error"))
            {
                businessMessageName = "Outbound_Receive_Business_Error";
            }
            else
            {
                businessMessageName = "Outbound_Receive_Business_Request";
            }
            
            if (xmlString.toString().indexOf("<Message>") >= 0 &&
                xmlString.toString().indexOf("</Message>") >= 0)
            {
                log.debug("xmlString contains a Business Message");
                  
                int startBusinessMessage = xmlString.indexOf("<Message>") + 9;
                int endBusinessMessage   = xmlString.indexOf("</Message>");
                    
                businessMessage = xmlString.substring(startBusinessMessage, endBusinessMessage); 
            }
            
            if(businessMessage!=null)
            {
                log.debug("businessMessage not null so write it to file");
                FileHelper.logDataToXmlFile(businessMessage,
                    businessMessageName,
                    requestId,
                    null,
                    directoryName);
            }
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
