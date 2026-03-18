package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

import uk.gov.cjse.schemas.endpoint.types.SubmitResponse;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import java.util.Map;

/**
 * <p>
 * Title: Web Service Client used to send Outbound messages to a remote Web Service
 * </p>
 * <p>
 * Description: Creates a SubmitRequest using the properties and String passed in
 * 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: ServiceWSClientImpl.java,v 1.1 2006/09/22 14:03:25 rzvddy Exp $ Exp $
 */
public class ServiceWSClientImpl extends ServiceWSClient
{
    /**
     * Calls a Web Service passing the message and properties in a SubmitRequest
     * and returning a SubmitResponse from the remote Web Service
     * @param String message
     * @param Map properties
     * @return SubmitResponse
     * @throws RemoteException, ServiceException    
     */    
    public SubmitResponse callSubmitRequest(String message, Map properties) throws RemoteException, ServiceException
    {
        log.debug("[ServiceWSClientImpl] callSubmit");
        return invokeWebService(setSubmitRequest(message, properties));
    }
}
