package uk.gov.courtservice.xhibit.services.scjsestub.outbound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.cjse.schemas.messages.deliver.x200605.DeliverRequest;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;
import uk.gov.courtservice.xhibit.services.gdgateway.common.LogUtility;
import uk.gov.courtservice.xhibit.services.gdgateway.common.XMLMarshaller;
import uk.gov.courtservice.xhibit.services.scjsegateway.outbound.ScjseOutboundProcessor;
import uk.gov.courtservice.xhibit.services.scjsestub.outbound.helpers.DeliverRequestHelper;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.OutboundConstants;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClient;
import uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client.ServiceWSClientFactory;
import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;

/**
 * <p>
 * Title: SCJSE Stub implementation of messaging used by the SCJSE
 * Gateway Services.
 * </p>
 * <p>
 * Description:
 * This is responsible for creating and sending a Deliver Request XML to the ServiceWSClient Stub.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Bal Bhamra
 * @version $Id: ScjseOutboundProcessorStub.java
 */
public class ScjseOutboundProcessorStub implements ScjseOutboundProcessor {
    private static final Logger log = CSServices.getLogger(ScjseOutboundProcessorStub.class);

    /**
     * Takes the data from the JMS queue and create a DeliverRequest except where XHBMessageTypeType=INVALID
     * Where XHBMessageTypeType=RECEIVEERROR the DeliverRequest will be populated with an Exception otherwise it will be a Message
     * The RequestId and DeliverRequest XML (or when invalid just the original JMS body) 
     * will be passed to the callSubmitRequest method in the ServiceWSClientStub 
     * (which is retrieved using the ServiceWSClientFactory)
     *
     * @param Map
     *            propertiesMap
     * @param String
     *            payload
     */
    public void processRequest(Map propertiesAndValuesMap, String payload) throws Exception {
        log.info("processRequest: START ");
       
        String messageIdentifier = (String) propertiesAndValuesMap.get("XHBMessageIdentifier");

        String messageType = (String) propertiesAndValuesMap.get("XHBMessageTypeType");
        try {
            // Build a ReceiveRequest Structure
            log.debug("Instantiate DeliverRequestHelper");
            DeliverRequestHelper deliverRequestHelper = new DeliverRequestHelper();

            log.debug("Call DeliverRequestHelper.createReceiveRequest");
            DeliverRequest deliverRequest = deliverRequestHelper.createDeliverRequest(propertiesAndValuesMap, payload);

            log.debug("deliverRequest built");
            LogUtility.logDeliverRequest(deliverRequest);

            String deliverRequestXML = XMLMarshaller.marshallDeliverObjectToXml(deliverRequest);
            log.debug("deliverRequestXML: " + deliverRequestXML);
            
            /**
             * NOTE: This is work around code for Castor Limitations. 
             * This below is calling a method to replace the String temporarily added in the anyObject value.
             * As this is on the marshalled XML, it is a simple find and replace with the payload string.
             * See called XMLMarshaller.replaceAnyObjectStrings for further details.
             */
            String replacedXML = XMLMarshaller.replaceAnyObjectStrings(deliverRequestXML, payload, messageType);
            
            log.debug("replacedXML:START");
            log.debug(replacedXML);
            log.debug("replacedXML:END");
            
            log.debug("replaced XML Version Tag :START");
            String replaceTagXml = replacedXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","").trim();
             
            log.debug("replaceTagXml: " + replaceTagXml);
            log.debug("replaced XML Version Tag :END");
            
            Map<String, String> properties = new HashMap<String, String>();
            properties.put(OutboundConstants.REQUEST_ID, messageIdentifier);
           
            ServiceWSClient wsClientStub = ServiceWSClientFactory.getInstance().getServiceWSClient();
            SubmitResponse submitResponse = wsClientStub.callSubmitRequest(replaceTagXml, properties);
            
            log.debug("******************************************************************\n\n");
            log.debug("*****  ScjseOutboundProcessorStub SubmitResponse Request Id: " + submitResponse.getRequestID()+ " *****\n\n");
            log.debug("*****  ScjseOutboundProcessorStub SubmitResponse Response Code: " + submitResponse.getResponseCode() + " *****\n\n");
            log.debug("*****  ScjseOutboundProcessorStub SubmitResponse Response Desc: " + submitResponse.getResponseText() + " *****\n\n");                        
            log.debug("******************************************************************\n\n");
            
        } catch (MappingException mape) {
            log.error("A Mapping exception has occured during when setting a mapping file to the marshaller: " + mape);
            throw mape;
        } catch (MarshalException mare) {
            log.error("A Marshal Exception has occurred during marshalling/unmarshalling: " + mare);
            throw mare;
        } catch (ValidationException ve) {
            log.error("A Validation Exception has occurred during Castor processing: " + ve);
            throw ve;
        } catch (DataAccessException dae) {
            log.error("A DataAccessException has occurred during Guaranteed Messaging processing: " + dae);
            throw dae;
        } catch (NumberFormatException nfe) {
            log.error("An NumberFormatException Exception has occurred during Guaranteed Messaging processing: " + nfe);
            throw nfe;
        } catch (IllegalArgumentException iae) {
            log.error("A IllegalArgumentException has occurred during Guaranteed Messaging processing: " + iae);
            throw iae;
        } catch (IOException ioe) {
            log.error("An IOException has occurred during Guaranteed Messaging processing: " + ioe);
            throw ioe;
        } catch (Exception e) {
            log.error("An Exception has occurred during Guaranteed Messaging processing: " + e);
            throw e;
        }
    }
 }