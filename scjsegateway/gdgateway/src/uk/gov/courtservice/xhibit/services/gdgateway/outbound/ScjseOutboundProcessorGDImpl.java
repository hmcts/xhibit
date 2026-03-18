package uk.gov.courtservice.xhibit.services.gdgateway.outbound;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequest;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingInternalCode;
import uk.gov.courtservice.xhibit.business.services.exiss.itemtracking.ItemTrackingMessageFactory;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.OutboundMessageVO;
import uk.gov.courtservice.xhibit.database.gdgateway.OutboundGdGateDatabase;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertiesCache;
import uk.gov.courtservice.xhibit.services.gdgateway.common.ConfigPropertyCodes;
import uk.gov.courtservice.xhibit.services.gdgateway.common.LogUtility;
import uk.gov.courtservice.xhibit.services.gdgateway.common.XMLMarshaller;
import uk.gov.courtservice.xhibit.services.gdgateway.outbound.helpers.ReceiveRequestHelper;
import uk.gov.courtservice.xhibit.services.scjsegateway.outbound.ScjseOutboundProcessor;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;

/**
 * <p>
 * Title: Guaranteed Messaging implementation of messaging used by the SCJSE
 * Gateway. Services.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: RequestProcessorGDImpl.java
 */
public class ScjseOutboundProcessorGDImpl implements ScjseOutboundProcessor{
    private static final Logger log = CSServices.getLogger(ScjseOutboundProcessorGDImpl.class);
    
    private ConfigPropertiesCache cache = ConfigPropertiesCache.getCache(); 
    
    private static final Long DEFAULT_SEND_ATTEMPTS = new Long(1);

    /**
     * processRequest: Consructs a ReceiveRequestStructure from the passed in
     * Map and String. This is then serialised into and XML string and then
     * encoded. The encoded string and the value in the MessageIdentifier
     * property is used to add this to eht Gateway DB.
     * 
     * Unrecoverable non infrastructure errors will have an error message sent 
     * to item tracking.
     * 
     * Recoverable infrastructure error will be re-thrown and error message 
     * will not be sent to ItemTracking as the transaction will be rolled back. 
     * 
     * @param Map
     *            propertiesMap
     * @param String
     *            payload
     */
    public void processRequest(Map propertiesAndValuesMap, String payload) throws Exception {
        log.info("processRequest: START ");
        boolean isError = true;
        
        String messageIdentifier = (String) propertiesAndValuesMap.get("XHBMessageIdentifier");
        String messageType = (String) propertiesAndValuesMap.get("XHBMessageTypeType"); 
        
        try {
            // Build a ReceiveRequest Structure
            log.debug("Instantiate ReceiveRequestHelper");
            ReceiveRequestHelper receiveRequestHelper = new ReceiveRequestHelper();

            log.debug("Call ReceiveRequestHelper.createReceiveRequest");
            ReceiveRequest receiveRequest = receiveRequestHelper.createReceiveRequest(propertiesAndValuesMap, payload);

            log.debug("receiveRequest built");
            LogUtility.logReceiveRequest(receiveRequest);

            String receiveRequestXML = XMLMarshaller.marshallReceiveObjectToXml(receiveRequest);
            log.debug("receiveRequestXML: " + receiveRequestXML);
            
            /**
            * NOTE: This is work around code for Castor Limitations. 
            * This below is calling a method to replace the String temporarily added in the anyObject value.
            * As this is on the marshalled XML, it is a simple find and replace with the payload string.
            * See called XMLMarshaller.replaceAnyObjectStrings for further details.
            */
            String replacedXML = XMLMarshaller.replaceAnyObjectStrings(receiveRequestXML, payload, messageType);

            //Strip XML Header information
            String strippedXML = replacedXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","").trim();
            log.debug("strippedXML: " + strippedXML);
            
            //Update GDGate database with OutboundMessage Row
            log.debug("Inserting Outbound Message into database.");
            OutboundGdGateDatabase outboundGDDB = new OutboundGdGateDatabase();
            outboundGDDB.insertOutboundMessage(getOutboundMessageVO(messageIdentifier, strippedXML));
            isError = false;
            log.debug(" OutboundGdGateDatabase: insertOutboundMessage complete");
            // Update ItemTracking Status 
        } catch (MappingException mape) {
            log.error("A Mapping exception has occured during when setting a mapping file to the marshaller: " + mape, mape);
        } catch (MarshalException mare) {
            log.error("A Marshal Exception has occurred during marshalling/unmarshalling: " + mare, mare);
        } catch (ValidationException ve) {
            log.error("A Validation Exception has occurred during Castor processing: " + ve, ve);
        } catch (NumberFormatException nfe) {
            log.error("An NumberFormatException Exception has occurred during Guaranteed Messaging processing: " + nfe, nfe);
        } catch (IllegalArgumentException iae) {
            log.error("A IllegalArgumentException has occurred during Guaranteed Messaging processing: " + iae, iae);
        } catch (DataAccessException dae) {
            log.fatal("A DataAccessException has occurred during Guaranteed Messaging processing: " + dae, dae);
            throw dae;
        } catch (IOException ioe) {
            log.fatal("An IOException has occurred during Guaranteed Messaging processing: " + ioe, ioe);
            throw ioe;
        } catch (Exception e){
            log.fatal("An Exception has occurred during Guaranteed Messaging processing: " + e, e);
            throw e;
        } finally {
            sendItemTrackingStatus(messageIdentifier, isError);
            log.info("processRequest: END");
        }
    }

    private void sendItemTrackingStatus(String messageIdentifier, boolean isError){
        log.debug("sendItemTrackingStatus:  Start processing");
                
        try {
            if (isError) {
                CSServices.getJMSServices().send(
                        new ItemTrackingMessageFactory(OutboundConstants.ITEM_TRACKING_QUEUE, Long
                                .parseLong(messageIdentifier), ItemTrackingInternalCode.SCJSE_MESSAGE_GDDB_ERROR));
                log.debug("sendItemTrackingStatus: ItemTracking Status updated: "+ ItemTrackingInternalCode.SCJSE_MESSAGE_GDDB_ERROR.toString());
            } else {
                CSServices.getJMSServices().send(
                        new ItemTrackingMessageFactory(OutboundConstants.ITEM_TRACKING_QUEUE, Long
                                .parseLong(messageIdentifier), ItemTrackingInternalCode.SCJSE_MESSAGE_GDDB_OK));
                log.debug("sendItemTrackingStatus: ItemTracking Status updated: "+ ItemTrackingInternalCode.SCJSE_MESSAGE_GDDB_OK.toString());
            }                
        } catch (NumberFormatException nfe) {
            log.error("An NumberFormatException has occurred during sendItemTrackingStatus. Status update not sent: " + nfe);
        } finally {
            log.info("sendItemTrackingStatus: END ");
        }
    }   

    private OutboundMessageVO getOutboundMessageVO(String requestId, String clobData) {
        if (requestId != null && clobData != null) {
            OutboundMessageVO outboundMessageVO = new OutboundMessageVO();
            outboundMessageVO.setRequestId(Long.parseLong(requestId));
            outboundMessageVO.setSourceIdentifier(cache.get(ConfigPropertyCodes.XHB_LOCATION_ID));
            outboundMessageVO.setDestinationIdentifier(cache.get(ConfigPropertyCodes.SCJSE_LOCATION_ID));
            outboundMessageVO.setSendAttempts(DEFAULT_SEND_ATTEMPTS);
            outboundMessageVO.setClobData(clobData);
            return outboundMessageVO;
        } else {
            throw new IllegalArgumentException("Either RequestId or clobData is NULL");
        }
    }

}