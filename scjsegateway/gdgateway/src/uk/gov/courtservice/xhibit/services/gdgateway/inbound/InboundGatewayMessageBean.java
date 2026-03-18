package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import java.io.IOException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.InboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.InboundGdgateDatabase;
import uk.gov.courtservice.xhibit.services.gdgateway.inbound.helpers.InboundGatewayMessageBeanHelper;
import uk.gov.courtservice.xhibit.services.scjsegateway.inbound.ScjseInboundGateway;
import uk.gov.courtservice.xhibit.services.scjsegateway.inbound.ScjseInboundGatewayFactory;

/**
 * <p>
 * Title: Message Driven Bean that takes events from the InboundGatewayQueue in
 * the GDGateway
 * </p>
 * <p>
 * Description: MDB takes messages from the InboundGatewayQueue, processes the
 * message and payload and writes and places the processed message onto the
 * ScjseInboundQueue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @ejb.bean acknowledge-mode="Auto-acknowledge"
 *           destination-type="javax.jms.Queue"
 *           subscription-durability="NonDurable"
 * @ejb.transaction type="Required"
 * @ejb.security-identity run-as="XHBInternal"
 * @weblogic.message-driven destination-jndi-name="scjsegateway/jms/GdInboundGatewayQueue"
 * @weblogic.dispatch-policy inbound.gateway.message.execute.queue
 *
 * @author Rob Sumner, Will Fardell
 * @version $Id: InboundGatewayMessageBean.java,v 1.1 2006/08/25 10:52:52 jzj6wd
 *          Exp $ Exp $
 */

public class InboundGatewayMessageBean extends CSTextMessageBean implements MessageDrivenBean, MessageListener {

    private static final long serialVersionUID = 1L;

    public static final String GDGITEMID = "GdgItemId";

    private InboundGdgateDatabase database;

    /**
     * Create any resources used by the bean
     *
     * @throws CreateException
     *             when there is a problem in configuration.
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
        database = new InboundGdgateDatabase();
    }


    /**
     * Method called when a JMS message is received by this MDB.
     * The incoming message contains the ID of a record on the InboundMessages/Clob table.
     * This record is retrieved from the D/B.  the payload is re-constituted as an XML document
     * validated against the DeliverService schema.
     * If the record passes schema validation, the details are sent to the ScjseInboundQueue.
     * If the fails schema validation, an Exception structure is created and <i>that</i> is passed
     * to the ScjseInboundQueue.
     *
     * @param msg
     *            an object message
     * @ejb.interface-method view-type="local"
     * @ejb.permission role-name="XHBInternal"
     */
    public void onTextMessage(TextMessage message) throws java.lang.Exception {
        log.info("onTextMessage: START");
        Date messageTimeStamp = new Date(message.getJMSTimestamp());

        log.debug("Message Time Stamp = " + messageTimeStamp);
        long itemId = message.getLongProperty(GDGITEMID);
        InboundMessageVO value = database.getMessageByMessageId(new Long(itemId));
        log.debug("ValueObject = " + value);
        log.debug("Item id = " + itemId);

        XMLServices xmlServices = XMLServicesImpl.getInstance();
        String xmlString = xmlServices.decodeXML(value.getClobData());
        xmlString = xmlServices.addXMLHeader(xmlString);
        log.debug("\n\n" + xmlString + "\n\n");

        ScjseInboundGateway gateway = ScjseInboundGatewayFactory.getInstance().getScjseInboundGateway();
        InboundGatewayMessageBeanHelper helper = new InboundGatewayMessageBeanHelper(message, value);

        try {
            log.debug("about to call validateXMLAgainstSchema()");
            helper.validateXMLAgainstSchema(xmlString);

            //send successful 'message message' or 'exceiption message'
            log.debug("about to call sendMessageToGateway()");
            helper.sendMessageToGateway(xmlString, gateway);
        }
        catch (SAXException e){  //if failed Validate
            log.error("SAXException on inbound message, will attempt to create a DELIVERERROR message",e);
            helper.createAndSendExceptionMessage(gateway, e, database);
        }
        catch (IOException e){  //if failed Validate
            log.error("IOException on inbound message, will attempt to create a DELIVERERROR message",e);
            helper.createAndSendExceptionMessage(gateway, e, database);
        }
        catch (ClassNotFoundException e){  //if failed Validate
            log.error("ClassNotFoundException on inbound message, will attempt to create a DELIVERERROR message",e);
            helper.createAndSendExceptionMessage(gateway, e, database);
        }
        catch (ParserConfigurationException e){  //if failed Validate
            log.error("ParserConfigurationException on inbound message, will attempt to create a DELIVERERROR message",e);
            helper.createAndSendExceptionMessage(gateway, e, database);
        }
        finally{
            log.info("onTextMessage: END");
        }
    }
}
