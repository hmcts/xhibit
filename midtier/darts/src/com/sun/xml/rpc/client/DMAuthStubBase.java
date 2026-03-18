package com.sun.xml.rpc.client;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.handler.HandlerChain;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jvnet.fastinfoset.FastInfosetSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.rpc.encoding.SOAPSerializationContext;
import com.sun.xml.rpc.soap.message.SOAPBlockInfo;
import com.sun.xml.rpc.streaming.FastInfosetWriter;
import com.sun.xml.rpc.streaming.PrefixFactoryImpl;
import com.sun.xml.rpc.streaming.XMLWriter;
import com.sun.xml.rpc.streaming.XmlTreeWriter;
import com.sun.xml.rpc.util.xml.CDATA;

public class DMAuthStubBase extends StubBase {
	
    private static final String WSSE_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    private static final String WSU_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    private static final String EMC_RAD = "http://schemas.emc.com/documentum#ResourceAccessToken";
    
    protected String token;

	public DMAuthStubBase(HandlerChain handlerChain) {
		super(handlerChain);
	}

	
// CUSTOM
    public String addTokenToHeader(String token)
    {
        Element wsseSecurity = null;
        Element wsseToken;
        Document document = null;
        try
        {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            wsseSecurity = document.createElementNS(
                    WSSE_NAMESPACE, "wsse:Security"
            );
            wsseToken = (Element) wsseSecurity.appendChild(document.createElementNS(
                    WSSE_NAMESPACE, "wsse:BinarySecurityToken"
            ));
            wsseToken.setAttribute("QualificationValueType", EMC_RAD);
            wsseToken.setAttributeNS(WSU_NAMESPACE, "wsu:Id", "RAD");
            // Added to get over 1.4.6 issue
            wsseToken.appendChild(document.createTextNode(token));
            // wsseToken.setTextContent(token);
            document.appendChild(wsseSecurity);
            
            // System.out.println(xmlToString(document));
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        
        String xmlString = xmlToString(document);
        return xmlString;
    } 	
    
    public static String xmlToString(Node node) {
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            return stringWriter.getBuffer().toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }      
// CUSTOM
    
    
	@Override
	protected void _writeRequest(StreamingSenderState state) throws Exception {
        SOAPBlockInfo bodyInfo = state.getRequest().getBody();
        boolean pushedEncodingStyle = false;
        if(bodyInfo == null || bodyInfo.getSerializer() == null)
            throw new SenderException("sender.request.missingBodyInfo");
        XMLWriter writer = null;
        ByteArrayOutputStream bufferedStream = null;
        HandlerChain handlerChain = state.getHandlerChain();
        if(handlerChain == null || handlerChain.size() == 0)
        {
            bufferedStream = new ByteArrayOutputStream();
            writer = _getXMLWriterFactory().createXMLWriter(bufferedStream, _getPreferredCharacterEncoding());
        } else
        {
            writer = new XmlTreeWriter(state.getRequest().getMessage().getSOAPPart());
        }
        writer.setPrefixFactory(new PrefixFactoryImpl("ans"));
        SOAPSerializationContext serializationContext = new SOAPSerializationContext("ID");
        serializationContext.setMessage(state.getRequest().getMessage());
        writer.startElement("Envelope", "http://schemas.xmlsoap.org/soap/envelope/", "env");
        writer.writeNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
        writer.writeNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        writer.writeNamespaceDeclaration("enc", "http://schemas.xmlsoap.org/soap/encoding/");
        String namespaceDeclarations[] = _getNamespaceDeclarations();
        if(namespaceDeclarations != null)
        {
            for(int i = 0; i < namespaceDeclarations.length; i += 2)
                writer.writeNamespaceDeclaration(namespaceDeclarations[i], namespaceDeclarations[i + 1]);

        }
        if(_getDefaultEnvelopeEncodingStyle() != null)
            pushedEncodingStyle = serializationContext.pushEncodingStyle(_getDefaultEnvelopeEncodingStyle(), writer);
        else
        if(_getImplicitEnvelopeEncodingStyle() != null)
            pushedEncodingStyle = serializationContext.setImplicitEncodingStyle(_getImplicitEnvelopeEncodingStyle());
        
// CUSTOM: Add Documentum context registry service token to header         
        writer.startElement("Header", "http://schemas.xmlsoap.org/soap/envelope/");
        String header = addTokenToHeader(token);
        header = header.substring(header.indexOf(">") + 1);
        writer.writeChars(new CDATA(header));
        writer.endElement();
// CUSTOM END        
        
        writer.startElement("Body", "http://schemas.xmlsoap.org/soap/envelope/", "env");
        serializationContext.beginFragment();
        bodyInfo.getSerializer().serialize(bodyInfo.getValue(), bodyInfo.getName(), null, writer, serializationContext);
        serializationContext.serializeMultiRefObjects(writer);
        serializationContext.endFragment();
        writer.endElement();
        writer.endElement();
        if(pushedEncodingStyle)
            serializationContext.popEncodingStyle();
        writer.close();
        if(handlerChain == null || handlerChain.size() == 0)
        {
            byte data[] = bufferedStream.toByteArray();
            ByteInputStream bis = new ByteInputStream(data, data.length);
            state.getRequest().getMessage().getSOAPPart().setContent(((javax.xml.transform.Source) ((writer instanceof FastInfosetWriter) ? ((javax.xml.transform.Source) (new FastInfosetSource(bis))) : ((javax.xml.transform.Source) (new StreamSource(bis))))));
        }
	}
}
