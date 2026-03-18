package uk.gov.courtservice.xhibit.web.framework.action;

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
 * Title: ActionFactory
 * </p>
 * <p>
 * Description: A factory for producing actions, this is configured by the
 * action properties resource file.
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
 * $Log: ActionFactory.java,v $
 * Revision 1.5  2006/06/05 12:30:24  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:23:51 bzjrnl Change:
 * TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision 1.3
 * 2004/12/09 15:05:47 sz0t7n Make thin client exceptions use standard
 * CSExceptions and report messages correctly
 * 
 * Revision 1.2 2003/06/26 11:15:46 cawleye Modified code to deal with common
 * properties files.
 * 
 * Revision 1.1 2003/03/21 11:48:19 fz0n8j Revised thinclient framework!
 * 
 * Revision 1.2 2003/03/19 20:13:31 fz0n8j Added better response functionality
 * 
 * Revision 1.1 2003/03/19 18:38:53 fz0n8j Framework changes.
 * 
 * Revision 1.1 2003/03/19 12:28:04 fz0n8j Framework update.
 * 
 */

public class ActionFactory {
    /**
     * Action resource name
     */
    private static final String ACTION_PROPERTIES_RESOURCE_NAME = "Action.properties";

    /**
     * Actions key (list of actions speerated by ;)
     */
    private static final String ACTIONS_KEY = "actions";

    /**
     * Class sub key (The fully qualified name of the action class)
     */
    private static final String CLASS_SUB_KEY = "class";

    /**
     * Request sub key (The request to map the action against)
     */
    private static final String REQUEST_SUB_KEY = "request";

    /**
     * map of actions against requests (this is the way actions are normally
     * looked up) not normally by name.
     */
    private final Map actionMap = new HashMap();

    /**
     * Construct a factory from the default resource name.
     * 
     * @throws FrameworkException
     *             if an error occures
     */
    public ActionFactory() throws FrameworkException {
        this(ACTION_PROPERTIES_RESOURCE_NAME);
    }

    /**
     * Construct a factory from the given resource name.
     * 
     * @param the
     *            resource name to construct the factory from
     * @throws FrameworkException
     *             if an error occures
     */
    public ActionFactory(String actionResourceName) throws FrameworkException {
        addPropertiesFile(actionResourceName);
    }

    /**
     * Construct a factory from the given resource names.
     * 
     * @param the
     *            resource names to construct the factory from
     * @throws FrameworkException
     *             if an error occures
     */
    public ActionFactory(String[] actionResourceNames) throws FrameworkException {
        for (int i = 0; i < actionResourceNames.length; i++) {
            addPropertiesFile(actionResourceNames[i]);
        }
    }

    /**
     * Add the properties from a file to the factory.
     * 
     * @param the
     *            resource names to add to the action map
     * @throws FrameworkException
     *             if an error occures
     */
    public void addPropertiesFile(String actionResourceName) throws FrameworkException {
        try {
            TypedProperties properties = ResourceUtil.getResourceAsTypedProperties(actionResourceName);

            Iterator names = properties.getIteratorProperty(ACTIONS_KEY);
            while (names.hasNext()) {
                String name = (String) names.next();
                if (!name.equals("")) {
                    TypedProperties subProperties = properties.getSubProperties(name);

                    String request = subProperties.getStringProperty(REQUEST_SUB_KEY);
                    Action action = createAction(subProperties.getClassProperty(CLASS_SUB_KEY));
                    action.setName(name);

                    Iterator subNames = subProperties.getPropertyNames();
                    while (subNames.hasNext()) {
                        String subName = (String) subNames.next();
                        action.setParameter(subName, subProperties.getStringProperty(subName));
                    }
                    actionMap.put(request, action);
                }
            }
        } catch (IOException ioe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing actions.", ioe);
        } catch (ResourceNotFoundException rnfe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing actions.", rnfe);
        } catch (PropertyNotFoundException pnfe) {
            throw new FrameworkException("xhibit.error.unexpected", "Error parsing actions.", pnfe);
        }
    }

    /*
     * Gets the action for the given request. @param request, the request to
     * look for @throws ActionNotFoundException if the action is not found
     */
    public Action getActionByRequest(String request) throws ActionNotFoundException {
        Action action = (Action) actionMap.get(request);
        if (action != null) {
            return action;
        } else {
            throw new ActionNotFoundException(request);
        }
    }

    /*
     * Gets the action for the given name (note this is not a fast lookup)
     * @param name the action name to lookup @throws ActionNotFoundException if
     * the action is not found @throws IllegalStateException if the action name
     * is not available for an action
     */

    public Action getActionByName(String name) throws ActionNotFoundException, IllegalStateException {
        Iterator actions = actionMap.values().iterator();
        while (actions.hasNext()) {
            Action action = (Action) actions.next();
            if (action.getName().equals(name)) {
                return action;
            }
        }
        throw new ActionNotFoundException(name);
    }

    /**
     * Return a string repsentation of the object
     * 
     * @return a string containg the mappings
     */

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        Iterator requests = actionMap.keySet().iterator();

        buffer.append("Actions:-");
        buffer.append(PrimitiveUtil.NL);
        buffer.append(PrimitiveUtil.NL);

        while (requests.hasNext()) {
            String request = (String) requests.next();
            Action action = (Action) actionMap.get(request);
            buffer.append(action);
            buffer.append(PrimitiveUtil.NL);
        }
        return buffer.toString();
    }

    /**
     * Create an action class from the given class object
     * 
     * @param actionClass
     *            the class to create an instance of
     */
    private static Action createAction(Class actionClass) throws FrameworkException {
        try {
            return (Action) actionClass.newInstance();

        } catch (ClassCastException cce) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing action.", cce);
        } catch (IllegalAccessException iae) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing action.", iae);
        } catch (InstantiationException ie) {
            throw new FrameworkException("xhibit.error.unexpected", "Error createing action.", ie);
        }
    }

}
