package uk.gov.courtservice.xhibit.web.framework.response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.PrimitiveUtil;
import uk.gov.courtservice.xhibit.web.framework.util.PropertyNotFoundException;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceNotFoundException;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;
import uk.gov.courtservice.xhibit.web.framework.util.TypedProperties;

/**
 * <p>
 * Title: ResponseFactory
 * </p>
 * <p>
 * Description: A factory for producing responses, this is configured by the
 * response properties resource file.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $
 * 
 * $Log: ResponseFactory.java,v $
 * Revision 1.5  2006/06/05 12:30:25  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:23:55 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.3 2004/12/09 15:05:47 sz0t7n Make thin client exceptions use standard
 * CSExceptions and report messages correctly
 * 
 * Revision 1.2 2003/06/26 11:15:47 cawleye Modified code to deal with common
 * properties files.
 * 
 * Revision 1.1 2003/03/21 11:48:24 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 20:13:32 fz0n8j Added better response functionality
 * 
 * Revision 1.1 2003/03/19 18:38:54 fz0n8j Framework changes.
 * 
 * Revision 1.1 2003/03/19 12:28:04 fz0n8j Framework update.
 * 
 */

public class ResponseFactory {
    /**
     * Response resource name
     */
    private static final String RESPONSE_PROPERTIES_RESOURCE_NAME = "Response.properties";

    /**
     * Responses key (list of responses speerated by ;)
     */
    private static final String RESPONSES_KEY = "responses";

    /**
     * Class sub key (The fully qualified name of the response class)
     */
    private static final String CLASS_SUB_KEY = "class";

    /**
     * map of responses against requests (this is the way responses are normally
     * looked up) not normally by name.
     */
    private final Map responseMap = new HashMap();

    /**
     * Construct a factory from the default resource name.
     * 
     * @throws FrameworkException
     *             if an error occures
     */
    public ResponseFactory() throws FrameworkException {
        this(RESPONSE_PROPERTIES_RESOURCE_NAME);
    }

    /**
     * Construct a factory from the given resource name.
     * 
     * @param the
     *            resource name to construct the factory from
     * @throws FrameworkException
     *             if an error occures
     */
    public ResponseFactory(String responseResourceName) throws FrameworkException {
        addPropertiesFile(responseResourceName);
    }

    /**
     * Construct a factory from the given resource names.
     * 
     * @param the
     *            resource names to construct the factory from
     * @throws FrameworkException
     *             if an error occures
     */
    public ResponseFactory(String[] responseResourceNames) throws FrameworkException {
        for (int i = 0; i < responseResourceNames.length; i++) {
            addPropertiesFile(responseResourceNames[i]);
        }
    }

    /**
     * Add properties file.
     * 
     * @param the
     *            resource name to add
     * @throws FrameworkException
     *             if an error occures
     */
    public void addPropertiesFile(String responseResourceName) throws FrameworkException {
        try {
            TypedProperties properties = ResourceUtil.getResourceAsTypedProperties(responseResourceName);

            Iterator names = properties.getIteratorProperty(RESPONSES_KEY);
            while (names.hasNext()) {
                String name = (String) names.next();
                if (!name.equals("")) {
                    TypedProperties subProperties = properties.getSubProperties(name);

                    Response response = createResponse(subProperties.getClassProperty(CLASS_SUB_KEY));
                    response.setName(name);

                    Iterator subNames = subProperties.getPropertyNames();
                    while (subNames.hasNext()) {
                        String subName = (String) subNames.next();
                        response.setParameter(subName, subProperties.getStringProperty(subName));
                    }
                    responseMap.put(name, response);
                }
            }
        } catch (IOException ioe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing responses.", ioe);
        } catch (ResourceNotFoundException rnfe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing responses.", rnfe);
        } catch (PropertyNotFoundException pnfe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing responses.", pnfe);
        }
    }

    /*
     * Gets the response for the given name (note this is not a fast lookup)
     * @param name the response name to lookup @throws ResponseNotFoundException
     * if the response is not found @throws IllegalStateException if the
     * response name is not available for an response
     */

    public Response getResponseByName(String name) throws ResponseNotFoundException, IllegalStateException {
        Response response = (Response) responseMap.get(name);
        if (response != null) {
            return response;
        } else {
            throw new ResponseNotFoundException(name);
        }
    }

    /**
     * Return a string repsentation of the object
     * 
     * @return a string containg the mappings
     */

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Responses:-");
        buffer.append(PrimitiveUtil.NL);
        buffer.append(PrimitiveUtil.NL);

        Iterator responses = responseMap.values().iterator();
        if (responses.hasNext()) {
            buffer.append(responses.next());
            buffer.append(PrimitiveUtil.NL);
            while (responses.hasNext()) {
                buffer.append(responses.next());
                buffer.append(PrimitiveUtil.NL);
            }
        } else {
            buffer.append("None.");
        }

        return buffer.toString();
    }

    /**
     * Create an response class from the given class object
     * 
     * @param responseClass
     *            the class to create an instance of
     */
    private static Response createResponse(Class responseClass) throws FrameworkException {
        try {
            return (Response) responseClass.newInstance();

        } catch (ClassCastException cce) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing response.", cce);
        } catch (IllegalAccessException iae) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing response.", iae);
        } catch (InstantiationException ie) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing response.", ie);
        }
    }

}
