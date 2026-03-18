/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 9:58:08 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.xml;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;



import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.MalformedOrderDataException;

/**
 * <p>
 * Title: XMLOrderData
 * </p>
 * <p>
 * Description: This class provides an implementation of the OrderData class to
 * handle order data. It internally stores the data as an XML Document and
 * provides the relevant accessor methods. It also provides a hashCode
 * implementation to allow simple comparisons.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class XMLOrderData extends OrderData {

    private static final Logger log = CSServices.getLogger(XMLOrderData.class);
    
    private Document dom;

    private Node narrativeNode;

    private Document narrativeDOM;

    /**
     * Returns the dom
     *
     * @return the dom
     */
    public Document getDom() {
        return dom;
    }

    /**
     * Instantiates the class from an org.w3c.dom.Document.
     *
     * @param theDom
     *            the XML document from which the class is instatiated.
     */
    public XMLOrderData(Document theDom) {
        dom = theDom;
    }

    /**
     * Sets a value in the value model, please check the documentation for
     * OrderData for the specification of the name parameter.
     *
     * @param name
     *            a name for the value string to be requested.
     * @param value
     *            the value to set
     * @see OrderData
     */
    public void setValue(String name, String value) {
        Node node = findNodeValue(name);
        if (node == null) {
            MalformedOrderDataException e;
            e = new MalformedOrderDataException("There is no such node: " + name + " so it's value cannot change to: "
                    + value);
            if (log.isDebugEnabled()){
                log.debug("Order XML = " + toString());
            }
            e.printStackTrace();
            return;
        }
        String nodeValue = node.getNodeValue();
        node.setNodeValue(value);
        changeSupport.firePropertyChange(name, nodeValue, value);
    }

    /**
     * Return the value retrieved from the dom givennthe appropriate key.
     *
     * @param name
     *            The key to the value to be retrieved
     * @return An object containing the retrieved value
     */
    public Object getValue(String name) {
        Node node = findNodeValue(name);

        if (node == null) {
            return null;
        }
        return node.getNodeValue();
    }

    /**
     * Return all child references for this component
     *
     * @param name
     *            the name of the node to find child references for
     * @return a vector containing the child references
     */
    public Vector getChildReferences(String name) {
        NodeList node = findNodeListValue(name);

        Vector cases = new Vector();

        for (int i = 0; i < node.getLength(); i++) {
            if (node.item(i).getFirstChild().getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) node.item(i).getFirstChild();
                cases.add(text.getData());
            }
        }

        if (node == null) {
            throw new MalformedOrderDataException("There is no such node: " + name);
        }
        return cases;
    }

    /**
     * Return the node given the key
     *
     * @param name
     *            the key to find the name
     * @return the node
     */
    private Node findNodeValue(String name) {
        try {
            if (name == null) {
                throw new NullPointerException("Name for findNodeValue was null.");
            }
            
            // Print out the nodes
            NodeList nl = XPathAPI.selectNodeList(dom, "/");
            
            Node node = XPathAPI.selectSingleNode(dom, name);

            if (node == null) {
                log.error("Node could not be found: " + name  );
                return null;
            }
            return node.getFirstChild();
        } catch (TransformerException e) {
            throw new MalformedOrderDataException("No value was given for the element " + name + ".", e);
        }
    }
    
    public void listAllNodes(NodeList nl) {
        if (nl.getLength() == 0) {
            return;
        } else {
            for (int i=0; i<nl.getLength(); i++) {
                Node n = nl.item(i);
                log.debug("--------------");
                if (n.getParentNode() != null) {
                    log.debug("Node name ="+n.getNodeName()+" with parent "+n.getParentNode().getNodeName());
                } else {
                    log.debug("Node name ="+n.getNodeName()+" and is root.");
                }
                log.debug("Node value ="+n.getNodeValue());
                log.debug("Node type ="+n.getNodeType());
                listAllNodes(n.getChildNodes());
            }
        }
    }
    
    /*public Node findNode(NodeList nl, String name) {
        boolean found = false;
        if (nl.getLength() == 0) {
            return null;
        } else {
            for (int i=0; i<nl.getLength(); i++) {
                Node n = nl.item(i);
                String s = n.getNodeName();
                if (name.endsWith(n.getNodeName())) {
                    // I think this could be a valid node that wasnt found using selectSingleNode
                    found = true;
                    return n;
                    
                }
                Node node = findNode(n.getChildNodes(), name);
                if (node != null) {
                    return n;
                }
            }
        }
        return null;
    }*/

    /**
     * Return the node list value
     *
     * @param name
     *            the key to find the node list
     * @return the node list
     */
    private NodeList findNodeListValue(String name) {
        try {

            if (name == null) {
                throw new NullPointerException("Name for findNodeValue was null.");
            }

            NodeList node = XPathAPI.selectNodeList(dom, name);

            if (node == null) {
                return null;
            }
            return node;
        } catch (TransformerException e) {
            throw new MalformedOrderDataException("No value was given for the element " + name + ".", e);
        }
    }

    /**
     * Return the hashcode for the dom
     *
     * @return the hashcode
     */
    public int hashCode() {
        int code = 0;
        if (dom == null)
            return 0;
        NodeList elements = dom.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            code += elements.item(i).getNodeName().hashCode();
        }
        return code;
    }

    /**
     * Merge the narrative with the main order element. This has to be done
     * after a save has taken place
     *
     * @param narrative
     *            the document to merge
     */
    public void mergeNarrative(Document narrative) {
        this.narrativeDOM = narrative;
        
        // get root elements of two doms.
        Element orderRoot = this.dom.getDocumentElement();
        Element narrativeRoot = narrative.getDocumentElement();
        
        // get Node we need to add to the order.
        this.narrativeNode = this.dom.importNode(narrativeRoot, true);

        // add narrative.
        orderRoot.appendChild(this.narrativeNode);
    }

    /**
     * Remove the narrative from the main order element prior to saving
     */
    public void removeNarrativeForSave() {
        Element orderRoot = this.dom.getDocumentElement();
        orderRoot.removeChild(this.narrativeNode);
    }

    /**
     * Replace the narrative after a save
     */
    public void reinstateNarrativeAfterSave() {
        Element orderRoot = this.dom.getDocumentElement();
        // add narrative.
        orderRoot.appendChild(this.narrativeNode);
    }

    /**
     * Return the narrative DOM for when we need to reset the DOM
     *
     * @return the narrative DOM
     */
    public Document getNarrativeDOM() {
        return this.narrativeDOM;
    }
    
    public void setNarrativeDOM(Document narrativeDOM) {
        this.narrativeDOM = narrativeDOM;
    }
    
    /**
     * Helper toString method
     * 
     * @param node
     * @return
     */
    private String toString(Node node) {
    	
    	try {
    		StringWriter writer = new StringWriter();
	        StreamResult result = new StreamResult(writer);
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.transform(new DOMSource(node), result);
	        return writer.toString();
	    } catch (TransformerConfigurationException e) {
	        log.error("Could not convert Document to String : TransformerConfigurationException" + e.getMessage());
	        return null;
	    } catch (TransformerFactoryConfigurationError e) {
	        log.error("Could not convert Document to String : TransformerFactoryConfigurationError" + e.getMessage());
	        return null;
	    } catch (TransformerException e) {
	        log.error("Could not convert Document to String : TransformerException" + e.getMessage());
	        return null;
	    }
    }
    
    /**
     * Helper toString method
     * @param doc
     * @return
     */
    private String toString(Document doc) {
    	try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
         } catch (TransformerConfigurationException e) {
             log.error("Could not convert Document to String : TransformerConfigurationException" + e.getMessage());
             return null;
         } catch (TransformerFactoryConfigurationError e) {
             log.error("Could not convert Document to String : TransformerFactoryConfigurationError" + e.getMessage());
             return null;
         } catch (TransformerException e) {
             log.error("Could not convert Document to String : TransformerException" + e.getMessage());
             return null;
         }
    }
    
    
    /**
     * Helper toString method
     * @param element
     * @return
     */
    private String toString(Element element) {
    	Document doc = element.getOwnerDocument();
    	DOMImplementationLS dil = (DOMImplementationLS) doc.getImplementation();
    	LSSerializer serializer = dil.createLSSerializer();
    	String str = serializer.writeToString(element);
    	return str;
    }
    

    /**
     * Standard toString object method override
     */
    public String toString() {
      
       try {
           DOMSource domSource = new DOMSource(dom);
           StringWriter writer = new StringWriter();
           StreamResult result = new StreamResult(writer);
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.transform(domSource, result);
           return writer.toString();
        } catch (TransformerConfigurationException e) {
            log.error("Could not convert Document to String : TransformerConfigurationException" + e.getMessage());
            return null;
        } catch (TransformerFactoryConfigurationError e) {
            log.error("Could not convert Document to String : TransformerFactoryConfigurationError" + e.getMessage());
            return null;
        } catch (TransformerException e) {
            log.error("Could not convert Document to String : TransformerException" + e.getMessage());
            return null;
        }
         
    } 
}