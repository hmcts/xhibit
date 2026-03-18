package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;


import uk.gov.courtservice.framework.services.CSServices;

/**
 * Interceptor file to add the token to the header of a soap message being sent to
 * addDocument in DARTS.
 *
 */
public class SOAPMessageHandler implements SOAPHandler<SOAPMessageContext> {
    protected static final Logger log = CSServices.getLogger(SOAPMessageHandler.class);

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outBoundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		String token =  null;
		
		if (outBoundProperty) {
			SOAPMessage soapMsg = context.getMessage();
			log.debug("Outbound SOAP message:");
			
			//If outbound addDocument message add token to header
			try {
				boolean check = checkForAddDoc(soapMsg);
				if(check)
					token = (String) context.get("token");
					if (token != null) {
						log.debug("Token context value: " + token);
						addTokenToHeader(soapMsg, token);
					} else {
						log.debug("Token null");
					}
			} catch (SOAPException e) {
				e.printStackTrace();
			}
			
			String soap = formatSOAPMessageOut(soapMsg);
			log.debug(soap);
		} else if (!outBoundProperty){
			SOAPMessage soapMsg = context.getMessage();
			log.debug("Inbound SOAP message:");
			String soap = formatSOAPMessageOut(soapMsg);
			log.debug(soap);
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	/**
	 * Check the incoming/outgoing message for 'addDocument' to see if
	 * token needs to be added to the header.
	 * @param message SOAPMessage
	 * @return true if addDocument is present
	 * @throws SOAPException
	 */
	private boolean checkForAddDoc(SOAPMessage message) throws SOAPException {
		log.debug("checkForAddDoc");
		boolean outboundIsAddDoc = false;
		
		SOAPMessage soapMsg = message;
		SOAPPart soapPart = soapMsg.getSOAPPart();
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		SOAPBody soapBody = soapEnvelope.getBody();
		Iterator soapElement = soapBody.getChildElements();
		
		while(soapElement.hasNext()) {
			Object element = soapElement.next();
			log.debug(element);
			if (element.toString().contains("addDocument")) {
				log.debug("addDocument exists");
				outboundIsAddDoc = true;
				return outboundIsAddDoc;
			} else {
				log.debug("addDocument not present");
			}
		}
		return outboundIsAddDoc;
	}
	
	/**
	 * Add the security token required for authentication
	 * into the SOAP header being passed to DARTS.
	 * @param message SoapMessage needing the token added
	 * @param token security token required for authentication 
	 */
	private void addTokenToHeader(SOAPMessage message, String token) {
		log.debug("addTokenToHeader method");
		
		final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	    final String WSU_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	    final String EMC_RAD = "http://schemas.emc.com/documentum#ResourceAccessToken";
		SOAPMessage soapMsg = message;
		SOAPEnvelope soapEnv;
		SOAPHeader soapHeader = null;
		
		try {
			soapEnv = soapMsg.getSOAPPart().getEnvelope();
			soapHeader = soapEnv.getHeader();
			
			if (soapHeader == null) {
				log.debug("Adding SOAP header");
				soapHeader = soapEnv.addHeader();
			}
		
			
			QName secQName = new QName(WSSE_NAMESPACE, "Security");
	        SOAPHeaderElement security = soapHeader.addHeaderElement(secQName);
	        
	        QName authQName = new QName(WSSE_NAMESPACE, "BinarySecurityToken");
	        SOAPHeaderElement auth = soapHeader.addHeaderElement(authQName);
	        
	        auth.setAttribute("QualificationValueType", EMC_RAD);
            auth.setAttributeNS(WSU_NAMESPACE, "wsu:Id", "RAD");
            auth.addTextNode(token);
            
	        security.addChildElement(auth);
			
			soapMsg.saveChanges();
			
		} catch (SOAPException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Method to format the SoapMessage being sent.
	 * @param soapMsg the Message to format
	 * @return the formatted message
	 */
	private String formatSOAPMessageOut(SOAPMessage soapMsg) {
		String formattedXML = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			Source src = soapMsg.getSOAPPart().getContent();
			StreamResult result = new StreamResult(new StringWriter());
			
			transformer.transform(src, result);
			String xmlResult = result.getWriter().toString();
			formattedXML = xmlResult;
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return formattedXML;
	}
}
