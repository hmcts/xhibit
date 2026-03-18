package uk.gov.courtservice.xhibit.web.framework.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.gov.courtservice.xhibit.web.framework.action.Action;
import uk.gov.courtservice.xhibit.web.framework.util.PrimitiveUtil;
import uk.gov.courtservice.xhibit.web.framework.util.SafeIterator;

/**
 * <p>
 * Title: Request Mapping
 * </p>
 * <p>
 * Description: This holds information about a request mapping
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         RequestMapping.java,v $ Revision 1.3 2003/03/21 11:48:26 fz0n8j
 *         Revised thinclient framework!
 * 
 * Revision 1.3 2003/03/17 11:32:05 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.2 2003/03/11 16:31:44 fz0n8j Added CVS log comments - ecawley
 * 
 */

public class RequestMapping {
    /**
     * The name of the request
     */
    private final String name;

    /**
     * The request we are mapping too
     */
    private final String request;

    /**
     * The action used to handle the request
     */
    private final Action action;

    /**
     * The map of reponses
     */

    private final Map responseMap = new HashMap();

    /**
     * Construct a request mapping with the given name, request and mapping
     * 
     * @param newName
     *            name of the request
     * @param newRequest
     *            request we are mapping too
     * @param newAction
     *            action used to handle the request
     * @throws IllegalArgumentException
     *             if any of the arguments are null
     */
    public RequestMapping(String newName, String newRequest, Action newAction) throws IllegalArgumentException {
        if (newName == null) {
            throw new IllegalArgumentException("newName");
        }
        if (newRequest == null) {
            throw new IllegalArgumentException("newRequest");
        }
        if (newAction == null) {
            throw new IllegalArgumentException("newAction");
        }
        name = newName;
        request = newRequest;
        action = newAction;
    }

    /**
     * Returns the name of the request mapping
     * 
     * @return the name of the request mapping
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the request of the request mapping
     * 
     * @return the request of the request mapping
     */
    public String getRequest() {
        return request;
    }

    /**
     * Returns the action of the action mapping
     * 
     * @return the action of the action mapping
     */
    public Action getAction() {
        return action;
    }

    /**
     * Returns an Iterator over the reponse Names
     * 
     * @return an Iterator over the reponse Names
     */
    public Iterator getResponseMappingNames() {
        return new SafeIterator(responseMap.keySet().iterator());
    }

    /**
     * Add a response to the request mapping
     * 
     * @param response
     * @throws IllegalArgumentException
     *             if the response is null
     */
    public void addResponse(RequestMappingResponse response) throws IllegalArgumentException {
        if (response == null) {
            throw new IllegalArgumentException("response");
        }
        responseMap.put(response.getName(), response);
    }

    /**
     * Get a request mapping response
     * 
     * @param name
     *            the name of the response
     * @return the named RequestMappingResponse object
     * @throws ResponseNotFoundException
     *             if the response can not be found
     */
    public RequestMappingResponse getResponse(String name) throws ResponseNotFoundException {
        if (name == null) {
            throw new ResponseNotFoundException("null");
        }
        RequestMappingResponse response = (RequestMappingResponse) responseMap.get(name);
        if (response == null) {
            throw new ResponseNotFoundException(name);
        }
        return response;
    }

    /**
     * Returns a string representation of the object. In general, the toString
     * method returns a string that "textually represents" this object. The
     * result should be a concise but informative representation that is easy
     * for a person to read.
     * 
     * @return a string representation of the object.
     */
    public String toString() {
        return getName() + ": " + getRequest() + " - " + getAction();
    }

    /**
     * Get debug information about the object
     * 
     * @param indent
     *            the amount to indent the output
     * @return a String repsentation of the object useful for debuging
     */
    public String toDebug(String indent) {
        return indent + toString() + " - responses:-" + responseMaptoDebug(indent + PrimitiveUtil.TAB);
    }

    /**
     * Get debug information about the response map
     * 
     * @param indent
     *            the amount to indent the output
     * @return a String repsentation of the response map useful for debuging
     */
    private String responseMaptoDebug(String indent) {
        StringBuffer buffer = new StringBuffer();
        Iterator iterator = responseMap.values().iterator();
        if (iterator.hasNext()) {
            RequestMappingResponse current = (RequestMappingResponse) iterator.next();
            buffer.append(PrimitiveUtil.NL);
            buffer.append(indent);
            buffer.append(current.toDebug(indent));
            while (iterator.hasNext()) {
                current = (RequestMappingResponse) iterator.next();
                buffer.append(PrimitiveUtil.NL);
                buffer.append(indent);
                buffer.append(current.toDebug(indent));
            }
        } else {
            buffer.append(PrimitiveUtil.NL);
            buffer.append(indent);
            buffer.append("None");
        }
        return buffer.toString();
    }
}
