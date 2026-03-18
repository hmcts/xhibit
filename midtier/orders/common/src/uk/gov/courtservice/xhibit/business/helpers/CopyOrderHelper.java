package uk.gov.courtservice.xhibit.business.helpers;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CopyOrderHelper {
	/**
     * Go through the order and ensure that the data from the original order is populated into the new schema
     * @param originalOrder
     * @param newBlankOrder
     */
    public String copyDataIntoNewSchema(String originalOrder, String newBlankOrder) {
    	String output = "";
    	
    	try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	
	    	Document orig = null;
	    	if (originalOrder.indexOf("UTF-8") > 0) {
	    		orig = builder.parse(new ByteArrayInputStream(originalOrder.getBytes("UTF-8")));
	    	} else { // For some monetary orders
	    		orig = builder.parse(new ByteArrayInputStream(originalOrder.getBytes("ISO-8859-1")));
	    	}
	    	Document blank = builder.parse(new ByteArrayInputStream(newBlankOrder.getBytes("UTF-8")));
	    	
	    	Document mergedDoc = compareDocs(orig.getDocumentElement(), blank, 0);
	    	
	    	mergedDoc = replaceOrderHeader(mergedDoc, orig);
	    	
	    	TransformerFactory tf = TransformerFactory.newInstance();
	    	Transformer transformer = tf.newTransformer();
	    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	    	StringWriter writer = new StringWriter();
	    	transformer.transform(new DOMSource(mergedDoc), new StreamResult(writer));
	    	output = writer.getBuffer().toString();
	    	if (output.startsWith("<ord:")) {
	    		output = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+output;
	    	}
	    	System.out.println("FINAL OUTPUT::" +output);

    	} catch (Exception e) {
    		e.printStackTrace();
    		return originalOrder; // Worst case at least retain original
    	}

    	return output;
    }
    
    /**
     * Iterate through all nodes on the original order.
     * We're only interested in copying data into the new order so therefore are only interested in
     * finding nodes that are populated in old order and then determining if they exist in the new order.
     *  
     * For each node:
     * 		- Is there a corresponding node on the new order
     * 			- If so then copy the data into it
     * 			- If not then carry on but flag this in the logs
     * 
     * If there are nodes on the new blank order not on the original order then thats ok
     *  
     * 
     * @param node1
     * @param node2
     * @return
     */
    private Document compareDocs(Node node1, Document blank, int duplicateNo) {
    	
    	NodeList nl = node1.getChildNodes();
    	for (int i = 0; i<nl.getLength(); i++) {
    		Node currNode = nl.item(i);
    		    		
    		// Find the path to the current node - we need full path in case of duplicate individual node names which are likely
			Node pathNode = nl.item(i);
    		String path = getPath(pathNode);
			
			// We are only interested in nodes that have values so only process nodes where there is a TEXT_NODE at the end of the tree
    		// OR where there are attributes on the node, as either way these values that may need to be copied into the new order
			// OR if its an order header element - in which case we want to also copy across any missing elements from the order being copied
			HashMap hm = new HashMap();
    		if ((currNode.getNodeType() == Node.TEXT_NODE) && (currNode.getNodeValue() != null)) // A text node with a value in it
    		{    			
				// Pass in the parent node (as it was a text node - so parent should be an element)
				//System.out.println("Checking...."+path);
				hm = findAndInsertNodeData(currNode.getParentNode(), duplicateNo, path, blank, "Value");
				duplicateNo = setDuplicateNo(currNode.getParentNode(), path, duplicateNo);
    			//System.out.println("Duplicate no = "+duplicateNo+": text value="+currNode.getTextContent());
    		} else if ((currNode.getAttributes() != null) && (currNode.getAttributes().getLength() > 0)) { // a node with attributes
    			
    			hm = findAndInsertNodeData(currNode, duplicateNo, path, blank, "Attr");
    			duplicateNo = setDuplicateNo(currNode, path, duplicateNo);
    			//System.out.println("Duplicate no = "+duplicateNo);
    		} else {
    			
    			duplicateNo = setDuplicateNo(currNode, path, duplicateNo);
    			//System.out.println("Skipping..."+path);
			}
    		
    		// Check if data has been populated/copied into new order from original order 
    		if (hm.get("nodeExists") != null && hm.get("document") != null) {
				blank = (Document) hm.get("document");
			}
    		
    		// Carry on through the document
			compareDocs(currNode, blank, duplicateNo);
    	}
    	
    	return blank;
    }
    
    /**
     * There are often multiple elements with the exact same path that must be accounted for
     * when copying across;
     * e.g. <Order><Address><AddressLine/><AddressLine/></Address></Order>
     * 
     * @param n
     * @param path
     * @param currDuplicateNo
     * @return
     */
    private int setDuplicateNo(Node n, String path, int currDuplicateNo) {
    	// Need a tracker of the current full node path
		// If its the same as the previous path then increment "duplicateNo" to ensure that element is inserted correctly
		if (n!= null && n.getPreviousSibling() != null && (path.equals(getPath(n.getPreviousSibling())))) {
			currDuplicateNo++;
		} else {
			currDuplicateNo=0;
		}
		
		return currDuplicateNo;
    }
    

    /**
     * Determine whether the node exists at the path given for the document given.
     * If it exists then copy in any content (text or attributes) from the order being copied.
     * If it does not exist, then ignore UNLESS....
     * ....it is part of the order header, in which case it also needs to be created in the new order
     * from a copy of the original order
     * 
     * @param node
     * @param path
     * @param newDoc
     * @return
     */
    private HashMap findAndInsertNodeData(Node node, int duplicateNo, String path, Document newDoc, String datatype) {
    	HashMap retMap = new HashMap();
    	boolean nodeExists = false;
    	
    	retMap.put("nodeExists", nodeExists);
    	
    	try {
    		// Get all nodes in document to iterate over
    		NodeList nodeList = newDoc.getElementsByTagName("*");
    		
    		for (int i=0; i<nodeList.getLength(); i++) {
    			Node thisTempNode = nodeList.item(i);

    			if (thisTempNode.getNodeType() == Node.ELEMENT_NODE) {
    				
    				if (thisTempNode.getNodeName().equals(node.getNodeName())) {
    					//System.out.println("Node::"+node.getNodeName()+"; Found a matching node.");
    					
    					// Only if at least 1 matching node is found now check for duplicates of this
    					if (duplicateNo > 0) {
    						thisTempNode = nodeList.item(i+duplicateNo);
    					}
    					
    					String thisTempPath = getPath(thisTempNode);
    					//System.out.println("Comparing : "+thisTempPath+" :: "+path);
    					
    					if (thisTempPath.equals(path)) {
    						//System.out.println("Node::"+node.getNodeName()+"; Found a matching path.");
    						
    						// Replace the data here based on the "datatype" input: value or attributes
    						if (datatype.equals("Value")) {
    							thisTempNode.setTextContent(node.getTextContent());
    						} else if (datatype.equals("Attr")) {
    							// Get the attributes from the existing node
    							NamedNodeMap nnm = node.getAttributes();
    							for (int j=0; j<nnm.getLength(); j++) {
    			    				((Element) thisTempNode).setAttribute(nnm.item(j).getNodeName(), nnm.item(j).getNodeValue());
    			    			}
    						}
    						
    						retMap.put("nodeExists", nodeExists);
    						retMap.put("document", newDoc);
    						return retMap;
    					}
    				}
    			}
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	//System.out.println("No matching path for....."+path);
    	
    	return retMap;
    }
    
    /** For a given Order Header element it needs to be copied along with any children
	  * As this is painfully hard with existing w3c methods the quick and dirty way is to convert the new and old orders
	  * into strings and do a string replace of the <ord:OrderHeader></ord:OrderHeader> sections and then
	  * convert back into a Document
	  * 
	  * @param d1 - is the Document being created
	  * @param d2 - is the original Document
	  * @return - an updated Document with the OrderHeader copied from the original 
	  */
    private Document replaceOrderHeader(Document d1, Document d2) throws Exception {
    	TransformerFactory tf = TransformerFactory.newInstance();
    	Transformer transformer = tf.newTransformer();
    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	StringWriter w1 = new StringWriter();
    	StringWriter w2 = new StringWriter();
    	
    	transformer.transform(new DOMSource(d1), new StreamResult(w1));
    	StringBuffer o1 = w1.getBuffer();
    	
    	transformer.transform(new DOMSource(d2), new StreamResult(w2));
    	StringBuffer o2 = w2.getBuffer();
    	
    	// Get the section to replace in the Document that is being created
    	int ixd1_1 = o1.indexOf("<ord:OrderHeader>");
    	int ixd1_2 = o1.indexOf("</ord:OrderHeader>");
    	
    	// Get the section to replace in the new Document
    	int ixd2_1 = o2.indexOf("<ord:OrderHeader>");
    	int ixd2_2 = o2.indexOf("</ord:OrderHeader>");
    	
    	// Grab the OrderHeader from original doc (d2) and insert into the new doc (d1)
    	String o3 = o2.substring(ixd2_1, ixd2_2);
    	o1.replace(ixd1_1, ixd1_2, o3);
    	
    	// Convert o1 back into a document
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	
    	Document doc = builder.parse(new InputSource(new StringReader(o1.toString())));
    	
    	return doc;
    }
    
    
    /**
     * For a given node get the full path
     * 
     * @param pathNode
     * @return
     */
    private String getPath(Node pathNode) {
    	String path = "";
		while (pathNode != null) {
			if (pathNode.getNodeType() == Node.ELEMENT_NODE) {
				if (path.length() > 0) 
					path = pathNode.getNodeName() + '/' + path;
				else
					path = pathNode.getNodeName();
			}
			pathNode = pathNode.getParentNode();
		}
		path = "/"+path;
		// SO THE FULL PATH IS...
		//System.out.println("PATH = "+path);
		
		return path;
    }
}
