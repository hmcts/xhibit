package uk.gov.courtservice.xhibit.services.gdgateway.outbound.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import uk.gov.cjse.schemas.messages.receive.x200605.ReceiveRequest;

public class TestReceiveRequestHelper extends TestCase {

    private Map<String, String> propertiesAndValuesMap;
    private String payload;
    ReceiveRequestHelper receiveRequestHelper;
    
    //TODO Perhaps read resources in from test resource folder?  So different properties can be set up for tests.
    protected void setUp() throws Exception {
        super.setUp();
        //Create and populate Map
        propertiesAndValuesMap = new HashMap<String, String>();
        propertiesAndValuesMap.put("XHBMessageIdentifier","1000");
        propertiesAndValuesMap.put("XHBRequestingSystemName","XHIBIT");
        propertiesAndValuesMap.put("XHBRequestingSystemOrgUnitCode","448000");
        propertiesAndValuesMap.put("XHBRequestingSystemEnvironment","DEVELOPMENT");
        propertiesAndValuesMap.put("XHBMessageTypeType","MESSAGE");
        propertiesAndValuesMap.put("XHBMessageTypeVersion","1.2");
        propertiesAndValuesMap.put("XHBMessageSchemaNamespace","http://schemas.cjse.gov.uk/messages/receive/2006-05");
        propertiesAndValuesMap.put("XHBMessageSchemaVersion","1.23");
        propertiesAndValuesMap.put("XHBOriginatingSystemName","XHIBIT");
        propertiesAndValuesMap.put("XHBOriginatingSystemOrgUnitCode","448111");
        propertiesAndValuesMap.put("XHBOriginatingSystemEnvironment","DEVELOPMENT");
        propertiesAndValuesMap.put("XHBCreationDateTime","2001-12-17T09:40:47-05:00");
        propertiesAndValuesMap.put("XHBExpiryDateTime","2001-12-17T09:40:47-05:10");
        propertiesAndValuesMap.put("XHBBoolean","true");
        propertiesAndValuesMap.put("XHBDouble","3.142");
        
        //TODO Again perhaps worth reading from file
        //set payload
        payload = "Dummy payload string";
        receiveRequestHelper = new ReceiveRequestHelper();
    }

    public void testCreateReceiveRequest()throws ValidationException, MarshalException, MappingException, IOException {
        assertEquals(ReceiveRequest.class, receiveRequestHelper.createReceiveRequest(propertiesAndValuesMap, payload).getClass());
    }
    
    protected void tearDown() {
        propertiesAndValuesMap = null;
        payload = null;
    }    
}
