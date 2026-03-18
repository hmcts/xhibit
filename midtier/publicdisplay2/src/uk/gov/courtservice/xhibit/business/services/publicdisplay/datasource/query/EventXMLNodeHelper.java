package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.BranchEventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.EventXMLNode;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes.LeafEventXMLNode;

/*TODO
 Rename VelocityXMLNodeBuilder
 Remove debug/replace with log4j statements
 Does file need to be read
 Determine which package this file is in PD XHIBIT
 */

public class EventXMLNodeHelper {
    /** Logger object */
    private final static Logger log = CSServices.getLogger(EventXMLNodeHelper.class);

    private EventXMLNodeHelper() {
        // Reduce Visibility
    }

    /**
     * Reads String passed in which contains path to xml file. A Document object
     * is created from the xml which is processed to create a VelocityXMLNode
     * object
     */
    public static EventXMLNode buildEventNode(String xml) {
        log.debug("START:");
        if (xml != null) {
            Document doc = CSServices.getXMLServices().createDocFromString(xml);
            addSeparator();

            log.debug("Get document element");

            // <event>
            Element element = doc.getDocumentElement();

            addSeparator();

            // Process Element Nodes
            return process(element);
        }
        return null;
    }
    
    /**
     * Takes a Node object and converts it into a Document object
     * from the xml which is processed to create a VelocityXMLNode
     * object
     */
    public static EventXMLNode buildEventNode(Node xmlNode) {
        log.debug("START:");
        if (xmlNode != null) {

            // Process Element Nodes
            return process(xmlNode);
        }
        return null;
    }

    /**
     * Reads String passed in which contains path to xml file. A Document object
     * is created from the xml which is processed to create a VelocityXMLNode
     * object
     */
    /*
     * Method not used from XHIBIT Midtier public EventXMLNode
     * buildEventNode(File file) { LOG.debug("START:"); try { //read file into
     * Inputsource InputSource is = readFile(file);
     * 
     * //retrieve a DOM document Document doc = retrieveDocument(is);
     * 
     * addSeparator();
     * 
     * LOG.debug("Get document element"); //<event> Element element =
     * doc.getDocumentElement();
     * 
     * addSeparator();
     * 
     * //Process Element Nodes return process(element); }
     * catch(FileNotFoundException fnfe) { LOG.debug("Error: " + fnfe); return
     * null; } catch (SAXException se) { LOG.debug("Error: " + se); return null; }
     * catch (IOException ioe) { LOG.debug("ERROR:" + ioe); return null; } }
     */

    private static EventXMLNode process(Node node) {
        if (node.hasChildNodes() && noTextNodeChildren(node)) {
            return processBranchNode(node);
        } else {
            return processLeafNode(node);
        }
    }

    private static boolean noTextNodeChildren(Node node) {
        log.debug("******** noTextNodeChildren ENTRY********");
        if (node.hasChildNodes()) {
            NodeList nodes = node.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                log.debug("Node name: " + node.getNodeName());
                log.debug("Node value: " + node.getNodeValue());
                log.debug("Child Node name: " + nodes.item(i).getNodeName());
                log.debug("Child Node value: " + nodes.item(i).getNodeValue());
                log.debug("Child Node type: " + nodes.item(i).getNodeType());

                if (nodes.item(i).getNodeType() != Node.TEXT_NODE) {
                    log.debug("******** nonTextNodeChildren EXIT: true********");
                    return true;
                }
            }
        }
        log.debug("******** nonTextNodeChildren EXIT: false********");
        return false;
    }

    private static LeafEventXMLNode processLeafNode(Node node) {
        addSeparator();
        log.debug("******** processLeafNode ENTRY********");
        addSeparator();

        String key = null;
        String value = null;

        // LEAFNODE
        // If childnodes are text nodes then concatenate and trim into single
        // node
        // If child node does not contain any text then value null
        key = node.getNodeName();

        if (node.hasChildNodes()) {
            log.debug("Has Child Nodes");
            NodeList nodes = node.getChildNodes();

            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < nodes.getLength(); i++) {
                if (nodes.item(i).getNodeType() == Node.TEXT_NODE)
                    ;
                {
                    log.debug("nodes.item(i).getNodeType(): " + nodes.item(i).getNodeType());
                    log.debug("Appending Leaf Value: " + nodes.item(i).getNodeValue());
                    buffer.append(nodes.item(i).getNodeValue());
                }
            }
            value = buffer.toString().trim();
            log.debug("Leaf Value: " + value);
        } else {
            // THIS SHOULD NEVER BE THE CASE
            if (node.getNodeType() == Node.TEXT_NODE) {
                value = node.getNodeValue();
            }
        }

        return new LeafEventXMLNode(key, value);
    }

    private static BranchEventXMLNode processBranchNode(Node nodeIn) {
        addSeparator();
        log.debug("******** processBranchNode ENTRY********");
        addSeparator();

        BranchEventXMLNode branchNode = new BranchEventXMLNode(nodeIn.getNodeName());
        NodeList list = nodeIn.getChildNodes();
        Node node = null;

        log.debug("NodeList length: " + list.getLength());

        for (int i = 0; i < list.getLength(); i++) {
            // for each node, check iof child nodes
            node = list.item(i);
            if (!checkForWhiteSpace(node)) {
                addSeparator();

                // Debug
                // LOG.debug("Node item: " + i);
                // LOG.debug("Node Type: " + node.getNodeType());
                printNodeType(node);
                // LOG.debug("Node Name: " + node.getNodeName());
                // LOG.debug("Node Value: " + node.getNodeValue());

                branchNode.add(process(node));

                addSeparator();
            }
        }

        addSeparator();
        log.debug("******** processBranchNode EXIT********");
        addSeparator();

        return branchNode;
    }

    private static void printEventXMLNode(EventXMLNode nodeIn) {

        addSeparator();
        log.debug("******** printEventXMLNode ENTRY********");
        addSeparator();

        Object key = null;
        Object value = null;
        Map map = null;

        if (nodeIn != null) {
            if (nodeIn instanceof BranchEventXMLNode) {
                map = ((BranchEventXMLNode) nodeIn).getMap();
                if (!map.isEmpty()) {
                    log.debug("Map size: " + map.size());

                    Iterator keySetIter = map.keySet().iterator();
                    while (keySetIter.hasNext()) {
                        key = keySetIter.next();
                        value = map.get(key);

                        if (value instanceof EventXMLNode) {
                            printEventXMLNode((EventXMLNode) value);
                        } else if (value instanceof List) {
                            printList((List) value);
                        } else {
                            log.debug("Value not an instanceof  EventXMLNode");
                            log.debug("Value: " + value);
                        }
                    }
                }
            } else if (nodeIn instanceof LeafEventXMLNode) {
                addSeparator();
                log.debug("LeafEventXMLNode  " + nodeIn);
                addSeparator();
            }
        }
        addSeparator();
        log.debug("******** printEventXMLNode EXIT********");
        addSeparator();
    }

    private static void printList(List listIn) {
        addSeparator();
        log.debug("******** printList ENTRY********");
        addSeparator();

        Object listValue = null;

        Iterator listIter = listIn.iterator();

        while (listIter.hasNext()) {
            listValue = listIter.next();
            if (listValue instanceof EventXMLNode) {

                printEventXMLNode((EventXMLNode) listValue);
            } else if (listValue instanceof List) {

                printList((List) listValue);
            } else {
                listIter.next();
            }
        }

        addSeparator();
        log.debug("******** printList EXIT********");
        addSeparator();
    }

    // UTILITY METHODS
    private static void printNodeType(Node node) {
        switch (node.getNodeType()) {
        case Node.ATTRIBUTE_NODE:
            log.debug("ATTRIBUTE_NODE");
            break;

        case Node.DOCUMENT_NODE:
            log.debug("DOCUMENT_NODE");
            break;

        case Node.DOCUMENT_TYPE_NODE:
            log.debug("DOCUMENT_TYPE_NODE");
            break;

        case Node.ELEMENT_NODE:
            log.debug("ELEMENT_NODE");
            break;

        case Node.ENTITY_NODE:
            log.debug("ENTITY_NODE");
            break;

        case Node.TEXT_NODE:
            log.debug("TEXT_NODE");
            break;
        }
    }

    private static boolean checkForWhiteSpace(Node node) {
        if (node.getNodeType() == (Node.TEXT_NODE) && node.getNodeValue().trim().equals("")) {
            // LOG.debug("White Space");
            return true;
        }
        return false;
    }

    private static void addSeparator() {
        log.debug("---------------------------------------------------------------");
    }
}