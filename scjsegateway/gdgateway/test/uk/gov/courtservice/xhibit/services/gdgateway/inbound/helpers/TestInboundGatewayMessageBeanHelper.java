package uk.gov.courtservice.xhibit.services.gdgateway.inbound.helpers;

import junit.framework.TestCase;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import uk.gov.cjse.schemas.messages.exception.x200606.Exception;
import uk.gov.cjse.schemas.messages.exception.x200606.InnerException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;


import uk.gov.cjse.schemas.messages.exception.x200606.Exception;
import uk.gov.cjse.schemas.messages.exception.x200606.InnerException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.framework.services.jms.TextMessageFactory;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.InboundGdgateDatabase;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.InboundGatewayMessageBean;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.TestInboundGatewayMessageBean;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.helpers.InboundGatewayMessageBeanHelper;

public class TestInboundGatewayMessageBeanHelper extends TestCase {
    private InboundGatewayMessageBean messageBean;
    private int counter = 0;
    
    private static long correlationId = System.currentTimeMillis();
    private static final String DEFAULT_PROVIDER_URL = "t3://" + getDefaultHostName() + ":9001," + getDefaultHostName()
    + ":9501";

    private static final String DEFAULT_CONTEXT_FACTORY_CLASS_NAME = "weblogic.jndi.WLInitialContextFactory";
    
    private static final String DEFAULT_CONNECTION_FACTORY_JNDI_NAME = "weblogic.jms.XAConnectionFactory";
    private InboundGatewayMessageBeanHelper messageBeanHelper;
    private static final Logger log = CSServices.getLogger(InboundGatewayMessageBean.class);
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    
    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }
    public static Test suite() {
        return new TestSuite(TestInboundGatewayMessageBean.class);
    }
    protected void setUp() {
        log.debug("###########setUp##############");

        String providerUrl = DEFAULT_PROVIDER_URL;
        String contextFactoryClassName = DEFAULT_CONTEXT_FACTORY_CLASS_NAME;
        String connectionFactoryJndiName = DEFAULT_CONNECTION_FACTORY_JNDI_NAME;

        try {
            InitialContext context = getContext(providerUrl, contextFactoryClassName);
            Connection connection = getConnection(context, connectionFactoryJndiName);

            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Message textMessage = getMessage(session, "");     
            InboundMessageVO vo = new InboundMessageVO();
            messageBeanHelper = new InboundGatewayMessageBeanHelper((TextMessage)textMessage, vo);
            log.debug("###########end setUp##############");
            
        } catch (NamingException e) {
            log.debug("###########setUp:NamingException##############");
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (JMSException e) {
            log.debug("###########setUp:JMSException##############");
            //e.printStackTrace();
        }

       
    }
    protected void tearDown() {
        //
    }
    private int printInnerException(InnerException innerException)
    {
        System.out.println("innerException.getAnyObject().toString() " + innerException.getAnyObject().toString());
        
        System.out.println("innerException.getCode() " + innerException.getCode());

        System.out.println("innerException.getDetail() " + innerException.getDetail());
        
        if(innerException.getInnerException()!= null)
        {   
            System.out.println("=========process Inner Exception================= " +  ++counter );
            printInnerException(innerException.getInnerException());
            //public uk.gov.cjse.schemas.messages.exception.x200606.InnerException getInnerException()
            System.out.println("=========end  Inner Exception==================== ");
        }

        System.out.println("innerException.getName() " + innerException.getName());
        System.out.println("innerException.getOriginalExceptionData() " + innerException.getOriginalExceptionData());        
        System.out.println("innerException.getRelatesTo()toString() " + innerException.getRelatesTo().getAnyObject(0).toString());//@ToDo - maybe should print out what's inside this.
        //public uk.gov.cjse.schemas.messages.exception.x200606.RelatesTo getRelatesTo()       
        System.out.println("innerException.getSchemaVersion() " + innerException.getSchemaVersion());
        return counter;
    }
//    public void testCreateInnerException(){
//        
//        InnerException innerException = messageBeanHelper.createInnerException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException())))));       
//        assertEquals(4, printInnerException(innerException));
//    }
    private void printException(Exception exception){
        System.out.println("exception.getAnyObject().toString() " + exception.getAnyObject().toString());       
        System.out.println("exception.getCode() " + exception.getCode());
        System.out.println("exception.getDetail() " + exception.getDetail());
        
        if(exception.getInnerException() != null)
        {   
            System.out.println("========== print out the inner Exceptions ================ " +  ++counter );
            int innerCounter = printInnerException(exception.getInnerException());           
            System.out.println(innerCounter + "   =============inner exceptions printed ================ ");
        }
        System.out.println("exception.getName() " + exception.getName());
        System.out.println("exception.getOriginalExceptionData() " + exception.getOriginalExceptionData());        
        System.out.println("exception.getRelatesTo()toString() " + exception.getRelatesTo().getAnyObject(0).toString());//@ToDo - maybe should print out what's inside this.
        //public uk.gov.cjse.schemas.messages.exception.x200606.RelatesTo getRelatesTo()       
        System.out.println("exception.getSchemaVersion() " + exception.getSchemaVersion());
    }
//    public void testCreateDeliverErrorException(){
//        Exception exception = messageBeanHelper.createDeliverErrorException(new CSUnrecoverableException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException(new CSRecoverableException()))))));
//        printException(exception);
//    }
    private static final Destination getDestination(InitialContext context, String destinationJndiName)
    throws NamingException {
    return (Destination) context.lookup(destinationJndiName);
    
    }
    
    private static final Connection getConnection(InitialContext context, String connectionFactoryJndiName)
        throws NamingException, JMSException {
    ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryJndiName);
    Connection connection = connectionFactory.createConnection();
    connection.start();
    return connection;
    }
    
    private static InitialContext getContext(String providerUrl, String contextFactoryClassName) throws NamingException {
    Hashtable<String, String> env = new Hashtable<String, String>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryClassName);
    env.put(Context.PROVIDER_URL, providerUrl);
    return new InitialContext(env);
    }
    
    private static void close(InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException ioe) {
            System.out.println("Warning: An error occured closing the input stream.");
            ioe.printStackTrace(System.out);
        }
    }
    
    private static String getDefaultHostName() {
        try {
            return InetAddress.getLocalHost().getHostName().toLowerCase();
        } catch (java.lang.Exception e) {
            System.err.println("Warning...");
            e.printStackTrace();
            return "localhost";
        }
    }
    private static final Message getMessage(Session session, String text) throws JMSException {
        Message message = session.createTextMessage(text);
        message.setJMSCorrelationID(String.valueOf(correlationId++));
        return message;
    }
    
    
//    public void testValidateXMLAgainstSchema(){
//        InboundGdgateDatabase database = new InboundGdgateDatabase();
//        InboundMessageVO value = database.getMessageByMessageId((Long)((long)1069));
//        log.debug("ValueObject = " + value);
//        log.debug("Item id = " + 1069);
//        
//        XMLServices xmlServices = XMLServicesImpl.getInstance();
//        String xmlString = xmlServices.decodeXML(value.getClobData());
//        xmlString = xmlServices.addXMLHeader(xmlString);
//        
//        try {          
//
//            messageBeanHelper.validateXMLAgainstSchema(xmlString);                    
//                 
//            //send successful 'message message' or 'exceiption message'
//                       
//        }catch (SAXException e){  //if failed Validate            
//           log.debug("SAXException");
//        }
//        catch (IOException e){  //if failed Validate           
//            log.debug("IOException");
//        }
//        catch (ClassNotFoundException e){  //if failed Validate           
//            log.debug("ClassNotFoundException");
//        }
//        catch (ParserConfigurationException e){  //if failed Validate            
//            log.debug("ClassNotFoundException");
//        }      
//        finally{
//            // Do nothing
//        }              
//    }
    
    
//      public void testValidateXMLAgainstSchema(){
// 
//      try {          
//              String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!--Sample XML file generated by XMLSPY v5 rel. 4 U (http://www.xmlspy.com)--><DeliverRequest xmlns=\"http://schemas.cjse.gov.uk/messages/deliver/2006-05\" xmlns:ex=\"http://schemas.cjse.gov.uk/messages/exception/2006-06\" xmlns:mf=\"http://schemas.cjse.gov.uk/messages/format/2006-05\" xmlns:mm=\"http://schemas.cjse.gov.uk/messages/metadata/2006-05\" xmlns:msg=\"http://schemas.cjse.gov.uk/messages/messaging/2006-05\" xmlns:xmime=\"http://www.w3.org/2005/05/xmlmime\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.cjse.gov.uk/messages/deliver/2006-05D:\\projects\\XHIBIT\\scjsegateway\\gdgatewayxmlbinding\\schemas\\DeliverService-v1-0.xsd\"><msg:MessageIdentifier>http://www.altova.com</msg:MessageIdentifier><msg:RequestingSystem><msg:Name>String</msg:Name><msg:OrgUnitCode>AAAAAA</msg:OrgUnitCode><msg:Environment>String</msg:Environment></msg:RequestingSystem><msg:AckRequested>1</msg:AckRequested><mm:MessageMetadata SchemaVersion=\"1.0\"><mm:OriginatingSystem><msg:Name>String</msg:Name><msg:OrgUnitCode>AAAAAA</msg:OrgUnitCode><msg:Environment>String</msg:Environment></mm:OriginatingSystem><mm:DataController><msg:Organisation>AAAAAA</msg:Organisation><msg:ReferencedElementURI>http://www.altova.com</msg:ReferencedElementURI><msg:ReferencedElementURI>http://www.altova.com</msg:ReferencedElementURI></mm:DataController><mm:DataController><msg:Organisation>AAAAAA</msg:Organisation><msg:ReferencedElementURI>http://www.altova.com</msg:ReferencedElementURI><msg:ReferencedElementURI>http://www.altova.com</msg:ReferencedElementURI></mm:DataController><mm:CreationDateTime>2001-12-17T09:30:47-05:00</mm:CreationDateTime><mm:ExpiryDateTime>2001-12-17T09:30:47-05:00</mm:ExpiryDateTime></mm:MessageMetadata><mf:MessageFormat SchemaVersion=\"1.0\"><mf:MessageType><msg:Type>String</msg:Type><msg:Version>String</msg:Version></mf:MessageType><mf:MessageSchema><msg:Namespace>http://www.altova.com</msg:Namespace><msg:Version>String</msg:Version></mf:MessageSchema></mf:MessageFormat><Exception SchemaVersion=\"1.0\"><ex:Name>String</ex:Name><ex:Code>String</ex:Code><ex:Detail>String</ex:Detail><ex:RelatesTo/><ex:OriginalExceptionData>String</ex:OriginalExceptionData><ex:InnerException SchemaVersion=\"1.0\"><ex:Name>String</ex:Name><ex:Code>String</ex:Code><ex:Detail>String</ex:Detail><ex:RelatesTo/><ex:OriginalExceptionData>String</ex:OriginalExceptionData></ex:InnerException></Exception></DeliverRequest>";
//          messageBeanHelper.validateXMLAgainstSchema(xmlString);                    
//               
//          //send successful 'message message' or 'exceiption message'
//                     
//      }catch (SAXException e){  //if failed Validate            
//         log.debug("SAXException");
//      }
//      catch (IOException e){  //if failed Validate           
//          log.debug("IOException");
//      }
//      catch (ClassNotFoundException e){  //if failed Validate           
//          log.debug("ClassNotFoundException");
//      }
//      catch (ParserConfigurationException e){  //if failed Validate            
//          log.debug("ClassNotFoundException");
//      }      
//      finally{
//          // Do nothing
//      }              
//    }
}
