package uk.gov.courtservice.xhibit.business.services.publicnotice;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DefinitivePublicNoticeStatusValue;

/**
 * <p>
 * Title: Reads in the XML file and creates a Map which is used in
 * PublicNoticeManipulator
 * </p>
 * <p>
 * Description: see title
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Pat Fox, Bob Boles
 * @created 24 February 2003
 */
public class PublicNoticeXmlHelper implements PublicNoticeConstants {

    private HashMap m_manipulatorMap = new HashMap();

    private static Logger log = CSServices.getLogger(PublicNoticeXmlHelper.class);

    private static PublicNoticeXmlHelper m_publicNoticeXmlHelper = new PublicNoticeXmlHelper();

    private Properties m_configProperties = null;

    /**
     * Gets the instance attribute of the PublicNoticeXmlHelper class
     * 
     * @return The instance value
     */
    public static PublicNoticeXmlHelper getInstance() {
        return m_publicNoticeXmlHelper;
    }

    /**
     * Constructor for the PublicNoticeXmlHelper object
     */
    private PublicNoticeXmlHelper() {
        // load propertiesfile
        getPublicNoticeProperties();

        String xmlFileName = m_configProperties.getProperty(XML_CONFIG_FILE);

        if (log.isDebugEnabled()) {
            log.debug("Loading xml file : " + xmlFileName);
        }
        // load the xmlfile to the Map
        loadXMLToMap(xmlFileName);

    }

    /**
     * Gets the manipulatorMap attribute of the PublicNoticeXmlHelper object
     * 
     * @return The manipulatorMap value
     */
    public HashMap getManipulatorMap() {
        return m_manipulatorMap;
    }

    /**
     * Description of the Method
     * 
     * @param xmlSourceFile
     *            Description of the Parameter
     */
    private void loadXMLToMap(String xmlSourceFile) {
        // Get a DocumentBuilderFactory

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        String xmlBasePath = m_configProperties.getProperty(XML_BASE_PATH);
        URL myUrl = null;

        if (log.isDebugEnabled()) {
            log.debug("Loading file from :" + xmlBasePath + xmlSourceFile);
        }

        myUrl = this.getClass().getResource(xmlBasePath + xmlSourceFile);

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource theStream = new InputSource(myUrl.openStream());

            // Parse our xml file
            doc = db.parse(theStream);
        } catch (IOException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
        } catch (ParserConfigurationException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
        } catch (SAXException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            throw new EJBException(ex);
        }

        // Get the root node.
        Element elem = doc.getDocumentElement();

        // Traverse the tree.
        traverse(elem);

    }

    /**
     * Traverses the XML tree, fore each courtlogEvet tag it builds an arrayList
     * of objects that are keyed on the EventId each element in the array list
     * contains an {definitivePNId & isActive/status}
     * 
     * @param n
     *            Description of the Parameter
     */
    private void traverse(Node n) {

        if (log.isDebugEnabled()) {
            log.debug(" Entering the traverse ");
        }

        // if it's an element node
        if (Node.ELEMENT_NODE == n.getNodeType()) {
            // if it's a <courtlogEvent> tag then we create an
            // array list and add to Map
            if ("CourtLogEvent".equals(n.getNodeName())) {
                NamedNodeMap theAttributes = n.getAttributes();

                String theEventType = theAttributes.item(0).getNodeValue();

                if (log.isDebugEnabled()) {
                    log.debug("The event type is " + theEventType);
                }

                ArrayList theList = createListOfDefPN(n);

                if (log.isDebugEnabled()) {
                    log.debug(" Size of Map currently is " + m_manipulatorMap.size());
                    log.debug(" Adding component with Key " + Integer.valueOf(theEventType));
                }

                m_manipulatorMap.put(Integer.valueOf(theEventType), theList);

                if (log.isDebugEnabled()) {
                    log.debug(" Size of Map after addition is :" + m_manipulatorMap.size());
                }

                return;
            }
            // if it's not a <courtlogEvent> tag,
            // we want to check all of it's children
            else {
                NodeList nl = n.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    traverse(nl.item(i));
                }
                return;
            }
        } else {
            // if it's not an element, just return

            log.debug("Exiting traverse() ");
            return;
        }
    }

    // end traverse

    /**
     * Description of the Method
     * 
     * @param n
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    private ArrayList createListOfDefPN(Node n) {
        ArrayList theList = new ArrayList();

        if (log.isDebugEnabled()) {
            log.debug(" Entering createListOfDefPN() ");
        }

        NodeList nl = n.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node defnotice = nl.item(i);
            log.debug(" Node name = " + defnotice.getNodeName());
            if ("DefinitivePublicNotice".equals(defnotice.getNodeName())) {

                if (log.isDebugEnabled()) {
                    log.debug(" Getting the attributes");
                }
                NamedNodeMap theAttributes = defnotice.getAttributes();

                // get the length of attributes
                if (log.isDebugEnabled()) {
                    log.debug(" length of Attributes " + theAttributes.getLength());
                }
                // print out the attributes

                Node defPNid = theAttributes.item(0);
                if (log.isDebugEnabled()) {
                    log.debug(" Attribute name is " + defPNid.getNodeName());
                    log.debug(" Attribute value is " + defPNid.getNodeValue());
                }
                Node defPNStatus = theAttributes.item(1);
                if (log.isDebugEnabled()) {
                    log.debug(" Attribute name is " + defPNStatus.getNodeName());
                    log.debug(" Attribute value is " + defPNStatus.getNodeValue());
                }
                theList.add(new DefinitivePublicNoticeStatusValue(Integer.valueOf(defPNid.getNodeValue()), Boolean
                        .valueOf(defPNStatus.getNodeValue()).booleanValue()));

            }
        }

        if (log.isDebugEnabled()) {
            log.debug(" Exiting createListOfDefPN() ");
        }
        return theList;
    }

    /**
     * Gets the publicNoticeProperties attribute of the PublicNoticeXmlHelper
     * object
     */
    private void getPublicNoticeProperties() {

        if (log.isDebugEnabled()) {
            log.debug("Entering getPublicNoticeProperties() ");
        }

        try {
            m_configProperties = CSServices.getConfigServices().getProperties(PROPERTIESFILENAME);
        } catch (CSConfigurationException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            log.error("Cannot find properties file: " + PROPERTIESFILENAME, e);
            throw new EJBException("Xhibit_Messaging.Unable_To_Locate_Properties" + e.getMessage(), e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Exiting getPublicNoticeProperties() ");
        }
    }

}
