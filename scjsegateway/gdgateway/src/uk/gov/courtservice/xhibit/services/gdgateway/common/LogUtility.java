package uk.gov.courtservice.xhibit.services.gdgateway.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequest;
import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequestStructureChoice;
import uk.gov.cjse.schemas.messages.exception.x200606.RelatesTo;
import uk.gov.cjse.schemas.messages.format.x200605.MessageFormat;
import uk.gov.cjse.schemas.messages.format.x200605.MessageSchema;
import uk.gov.cjse.schemas.messages.format.x200605.MessageType;
import uk.gov.cjse.schemas.messages.messaging.x200605.MessageSchemaStructureChoice;
import uk.gov.cjse.schemas.messages.messaging.x200605.MessageSchemaStructureChoiceSequence;
import uk.gov.cjse.schemas.messages.messaging.x200605.RequestingSystem;
import uk.gov.cjse.schemas.messages.messaging.x200605.SystemIdentifierStructure;
import uk.gov.cjse.schemas.messages.metadata.x200605.MessageMetadata;
import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequest;
import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequestStructureChoice;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: LogUtility
 * </p>
 * <p>
 * Description: Class to log Receive and Deliver objects to console.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author BSB
 * @version $Id: LogUtility.java,v 1.4 2006/10/17 12:02:06 szfnvt Exp $
 */
public class LogUtility {
    private static final String TAB = "   ";

    private static final Logger log = CSServices.getLogger(LogUtility.class);

    protected static void out(String in) {
        log.info(in + "\n\n");
    }

    /**
     * Prints out the component values of the ReceiveRequest structure This
     * calls further methods to print sub component information
     * 
     * @param ReceiveRequest
     */
    public static void logReceiveRequest(ReceiveRequest receiveRequest) {
        out("logReceiveRequest():START");
        if (receiveRequest != null) {
            // out("getSchemaVersion : "+ receiveRequest.getSchemaVersion());
            out("getMessageIdentifier(): " + receiveRequest.getMessageIdentifier());
            logRequestingSystem(receiveRequest.getRequestingSystem());
            out("getAckRequested(): " + receiveRequest.getAckRequested());
            logMessageMetadata(receiveRequest.getMessageMetadata());
            logMessageFormat(receiveRequest.getMessageFormat());
            // TODO Condition depending on whether Request or Response
            logReceiveRequestStructureChoice(receiveRequest.getReceiveRequestStructureChoice());
        } else {
            out("ReceiveRequest is null!");
        }
        out("logReceiveRequest():END");
    }

    /**
     * Prints out the component values of the ReceiveRequest structure This
     * calls further methods to print sub component information
     * 
     * @param ReceiveRequest
     */
    public static void logDeliverRequest(DeliverRequest deliverRequest) {
        out("logDeliverRequest():START");
        if (deliverRequest != null) {
            // out("getSchemaVersion : "+ receiveRequest.getSchemaVersion());
            out("getMessageIdentifier(): " + deliverRequest.getMessageIdentifier());
            logRequestingSystem(deliverRequest.getRequestingSystem());
            out("getAckRequested(): " + deliverRequest.getAckRequested());
            logMessageMetadata(deliverRequest.getMessageMetadata());
            logMessageFormat(deliverRequest.getMessageFormat());
            logDeliverRequestStructureChoice(deliverRequest.getDeliverRequestStructureChoice());
        } else {
            out("DeliverRequest is null!");
        }
        out("logDeliverRequest():END");
    }

    /**
     * Prints out the component values of the RequestingSystem structure
     * 
     * @param RequestingSystem
     */
    private static void logRequestingSystem(RequestingSystem rs) {
        out(addTab(1) + "logRequestingSystem:START");
        if (rs != null) {
            out(addTab(1) + "getName(): " + rs.getName());
            out(addTab(1) + "getOrgUnitCode: " + rs.getOrgUnitCode());
            out(addTab(1) + "getEnvironment: " + rs.getEnvironment());
        } else {
            out("RequestingSystem is NULL");
        }
        out("logRequestingSystem:END");
    }

    /**
     * Prints out the component values of the MessageFormat structure
     * 
     * @param MessageFormat
     */
    private static void logMessageFormat(MessageFormat mfs) {
        out(addTab(1) + "logMessageFormat:START");
        if (mfs != null) {
            out("getSchemaVersion(): " + mfs.getSchemaVersion());
            out("anyObject: " + mfs.getAnyObject());
            logMessageType(mfs.getMessageType());
            logMessageSchema(mfs.getMessageSchema());
        } else {
            out("MessageFormat is NULL");
        }
        out("logMessageFormat:END");
    }

    /**
     * Prints out the component values of the MessageType structure
     * 
     * @param MessageType
     */
    private static void logMessageType(MessageType mts) {
        if (mts != null) {
            out(addTab(2) + "logMesageType:START");
            out(addTab(2) + "getVersion(): " + mts.getVersion());
            out(addTab(2) + "getType(): " + mts.getType());
        } else {
            out("MessageType is NULL");
        }
        out("logMesageType:END");
    }

    /**
     * Prints out the component values of the MessageSchema structure
     * 
     * @param MessageSchema
     */
    private static void logMessageSchema(MessageSchema mss) {
        out(addTab(2) + "logMessageSchema():START");
        if (mss != null) {
            logMessageSchemaStructureChoice(mss.getMessageSchemaStructureChoice());
        } else {
            out("MessageSchema is NULL");
        }
        out(addTab(2) + "logMessageSchema():END");
    }

    /**
     * Prints out the component values of the MessageSchemaStructureChoice
     * structure
     * 
     * @param MessageSchemaStructureChoice
     */
    private static void logMessageSchemaStructureChoice(MessageSchemaStructureChoice msc) {
        if (msc != null) {
            out(addTab(1) + "getIdentifier(): " + msc.getIdentifier());
            logMessageSchemaStructureChoiceSequence(msc.getMessageSchemaStructureChoiceSequence());
        } else {
            out("MessageSchemaStructureChoiceSequence is NULL");
        }
    }

    /**
     * Prints out the component values of the
     * MessageSchemaStructureChoiceSequence structure
     * 
     * @param MessageSchemaStructureChoiceSequence
     */
    private static void logMessageSchemaStructureChoiceSequence(MessageSchemaStructureChoiceSequence mssc) {
        if (mssc != null) {
            out(addTab(1) + "getVersion(): " + mssc.getVersion());
            out(addTab(1) + "getNamespace():" + mssc.getNamespace());
        } else {
            out("MessageSchemaStructureChoiceSequence is NULL");
        }
    }

    /**
     * Prints out the component values of the MessageMetadata structure
     * 
     * @param MessageMetadata
     */
    private static void logMessageMetadata(MessageMetadata mmd) {
        out(addTab(1) + "logMessageMetadata:START");
        if (mmd != null) {
            out(addTab(1) + "getSchemaVersion(): " + mmd.getSchemaVersion());
            out(addTab(1) + "getCreationDateTime(): " + formatDate(mmd.getCreationDateTime()));
            out(addTab(1) + "getExpiryDateTime(): " + formatDate(mmd.getExpiryDateTime()));
            logOriginatingSystem(mmd.getOriginatingSystem());
        } else {
            out("MessageMetadata is NULL");
        }
        out("logMessageMetadata:END");
    }

    /**
     * Prints out the component values of the OriginatingSystem structure
     * 
     * @param SystemIdentifierStructure
     */
    private static void logOriginatingSystem(SystemIdentifierStructure sis) {
        out(addTab(2) + "logOriginatingSystem:START");
        if (sis != null) {
            out(addTab(2) + "getName(): " + sis.getName());
            out(addTab(2) + "getOrgUnitCode(): " + sis.getOrgUnitCode());
            out(addTab(2) + "getEnvironment(): " + sis.getEnvironment());
        } else {
            out("OriginatingSystem is NULL");
        }
        out("logOriginatingSystem:END");
    }

    /**
     * Prints out the component values of the ReceiveRequestStructureChoice
     * structure
     * 
     * @param ReceiveRequestStructureChoice
     */
    private static void logReceiveRequestStructureChoice(ReceiveRequestStructureChoice rrsc) {
        if (rrsc != null) {
            logReceiveMessage(rrsc.getMessage());
            logReceiveException(rrsc.getException());
        }
    }

    /**
     * Prints out the component values of the ReceiveRequestStructureChoice
     * structure
     * 
     * @param ReceiveRequestStructureChoice
     */
    private static void logDeliverRequestStructureChoice(DeliverRequestStructureChoice rrsc) {
        if (rrsc != null) {
            logDeliverMessage(rrsc.getMessage());
            logDeliverException(rrsc.getException());
        }
    }

    /**
     * Prints out the component values of the Receive Message structure
     * 
     * @param Message
     */
    private static void logReceiveMessage(uk.gov.cjse.schemas.messages.receive.x200605.Message m) {
        out(addTab(2) + "logMessage:START");
        if (m != null) {
            out(addTab(2) + "getAnyObject: " + m.getAnyObject());
        } else {
            out("Message is NULL");
        }
        out(addTab(2) + "logMessage:END");
    }

    /**
     * Prints out the component values of the Deliver Message structure
     * 
     * @param Message
     */
    private static void logDeliverMessage(uk.gov.cjse.schemas.messages.deliver.x200605.Message m) {
        out(addTab(2) + "logMessage:START");
        if (m != null) {
            out(addTab(2) + "getAnyObject: " + m.getAnyObject());
        } else {
            out("Message is NULL");
        }
        out(addTab(2) + "logMessage:END");
    }

    /**
     * Prints out the component values of the Receive Exception structure
     * 
     * @param Exception
     */
    public static void logReceiveException(uk.gov.cjse.schemas.messages.receive.x200605.Exception e) {
        out(addTab(2) + "logReceiveException:START");
        if (e != null) {
            out(addTab(2) + "getName: " + e.getName());
            out(addTab(2) + "getCode: " + e.getCode());
            out(addTab(2) + "getDetail: " + e.getDetail());
            out(addTab(2) + "getOriginalExceptionData: " + e.getOriginalExceptionData());
            logRelatesTo(e.getRelatesTo());
        } else {
            out("Exception is NULL");
        }
        out(addTab(2) + "logReceiveException:END");
    }

    /**
     * Prints out the component values of the Deliver Exception structure
     * 
     * @param Exception
     */
    public static void logDeliverException(uk.gov.cjse.schemas.messages.deliver.x200605.Exception e) {
        out(addTab(2) + "logDeliverException:START");
        if (e != null) {
            out(addTab(2) + "getName: " + e.getName());
            out(addTab(2) + "getCode: " + e.getCode());
            out(addTab(2) + "getDetail: " + e.getDetail());
            out(addTab(2) + "getOriginalExceptionData: " + e.getOriginalExceptionData());
            logRelatesTo(e.getRelatesTo());
        } else {
            out("Exception is NULL");
        }
        out(addTab(2) + "logDeliverException:END");
    }

    /**
     * Prints out the component values of the RelatesTo structure
     * 
     * @param RelatesTo
     */
    private static void logRelatesTo(RelatesTo rt) {
        out(addTab(2) + "logRelatesTo:START");
        if (rt != null) {
            out(addTab(2) + "getAnyObject: " + rt.getAnyObject(0));
        } else {
            out("RelatesTo is NULL");
        }
        out(addTab(2) + "logRelatesTo:END");
    }

    private static String formatDate(Date date) {
        if (date != null) {
            String result;
            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            result = formatter.format(date);
            return result;
        }
        return null;
    }

    /**
     * Adds one or more tabs for formatting.
     * 
     * @param int
     *            tabs
     */
    private static String addTab(int tabs) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tabs; i++) {
            buffer.append(TAB);
        }
        return buffer.toString();
    }

    // ****************************************************************************************************************
    // NOTE: Below code is commented as the optional DataController data will
    // NOT be set in the JMS Message(properties)
    // ****************************************************************************************************************
    /*
     * private static void logDataController(DataControllerLinkStructure[] dcls) {
     * out(addTab(2)+"logDataController:START"); if (dcls!=null) { for (int i=0;
     * i < dcls.length; i++) { out(addTab(2)+"getControlsEntireMessage():
     * "+dcls[i].getControlsEntireMessage());
     * logOrganisation(dcls[i].getOrganisation());
     * logReferenceElementURIs(dcls[i].getReferencedElementURI()); } } else {
     * out("DataControllerLinkStructure[] is NULL"); }
     * out("logDataController:END"); } private static void
     * logOrganisation(Organisation o) { out(addTab(3)+"logOrganisation:START");
     * if (o!=null) { out(addTab(3)+"get_value(): "+o.get_value());
     * out(addTab(3)+"getLiteralvalue(): "+o.getLiteralvalue()); } else {
     * out("Organisation is NULL"); } out(addTab(3)+"logOrganisation:END"); }
     * private static void logReferenceElementURIs(URI[] uris) {
     * out(addTab(3)+"logReferenceElementURIs:START"); if(uris != null) {
     * out(addTab(3)+"getReferencedElementURI().length: "+ uris.length); for
     * (int i=0; i < uris.length; i++) { out(addTab(3)+"uri["+i+"]:
     * "+uris[i].toString()); } } else { out("Organisation is NULL"); }
     * out(addTab(3)+"logReferenceElementURIs:END"); } //
     * out(addTab(3)+"ReferencedElementURIs:START"); // URI[] uris =
     * dcls.getReferencedElementURI(); // for (int i = 0; i < uris.length; i++) // { //
     * out(addTab(3)+"uri["+ i +"]: " + uris[i].toString()); // } //
     * out(addTab(3)+"ReferencedElementURIs:END");
     */
    // ****************************************************************************************************************
}