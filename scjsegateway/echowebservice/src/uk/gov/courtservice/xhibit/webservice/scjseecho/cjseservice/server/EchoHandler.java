package uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server;


import java.util.Iterator;
import javax.xml.namespace.QName;

import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class implements a handler in the handler chain, used to access the SOAP
 * request and response message.
 * <p>
 * This class extends the <code>javax.xml.rpc.handler.GenericHandler</code>
 * abstract classs and simply prints the SOAP request and response messages to
 * the server log file before the messages are processed by the backend
 * Java class that implements the Web Service itself.
 */
public class EchoHandler extends GenericHandler
{
    private HandlerInfo handlerInfo;
    private ServerContentHandler contentHandler;

    /**
     *  Initializes the instance of the handler.  Creates a nonCatalogLogger to
     *  log messages to.
     */
    public void init(HandlerInfo hi)
    {
        out("init");
        handlerInfo = hi;
    }

    /**
     * Specifies that the SOAP request message be logged to a log file before the
     * message is sent to the Java class that implements the Web Service.
     */
    public boolean handleRequest(MessageContext context)
    {
        out("******************************************************");
        out("EchoHandler handleRequest START:");
        out("******************************************************");

        SOAPMessageContext messageContext = (SOAPMessageContext) context;
        out("handleRequest " + messageContext.getMessage().toString());

        SOAPMessage soapMessage =  messageContext.getMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        try
        {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPHeader soapHeader = soapEnvelope.getHeader();
            String xmlString ="";

            Iterator iter = soapBody.getChildElements();
            if(iter.hasNext())
            {
                Node node = (Node)iter.next();
                DOMSource domSource = new DOMSource(node);
                xmlString = DocumentHelper.getDocumentAsXml(domSource);
            }

            out("xmlString soapPart = \n\n " + xmlString);

            SoapHandlerHelper.createSoapFile(soapMessage,"Echo_Request");

            out("******************************************************");
            out("EchoHandler handleRequest END:");
            out("******************************************************");
        }
        catch(TransformerConfigurationException tce)
        {
            out("handleRequest TransformerConfigurationException " + tce);
            //throw new SOAPFaultException();
        }
        catch(TransformerException te)
        {
            out("handleRequest TransformerException " + te);
            //throw new SOAPFaultException();
        }
        catch(SOAPException e)
        {
            out("handleRequest SOAP error " + e);
            //throw new SOAPFaultException();

        }

        return true;
     }

    private ServerContentHandler getHandler() throws SAXException
    {
      out("getContentHandler:START");
      if (contentHandler == null)
      {
         contentHandler = new ServerContentHandler();
      }
      out("getContentHandler:END");
      return contentHandler;
    }

    /**
     * Specifies that the SOAP response message be logged to a log file before the
     * message is sent back to the client application that invoked the Web
     * service.
     */
    public boolean handleResponse(MessageContext context)
    {
        SOAPMessageContext messageContext = (SOAPMessageContext) context;
        out("handleResponse " + messageContext.getMessage().toString());
        SOAPMessage soapMessage =  messageContext.getMessage();

        SoapHandlerHelper.createSoapFile(soapMessage,"Echo_Response");

        return true;
    }

    /**
     * Specifies that a message be logged to the log file if a SOAP fault is
     * thrown by the Handler instance.
     */
    public boolean handleFault(MessageContext context)
    {
        SOAPMessageContext messageContext = (SOAPMessageContext) context;
        //System.out.println("** Fault: "+messageContext.getMessage().toString());
        out("handleFault " + messageContext.getMessage().toString());
        SOAPMessage soapMessage =  messageContext.getMessage();

        SoapHandlerHelper.createSoapFile(soapMessage,"Echo_Error");

        return true;
    }


    public QName[] getHeaders()
    {
        return handlerInfo.getHeaders();
    }

    private static void out(String in)
    {
        System.out.println(in+"\n\n");
    }
}
