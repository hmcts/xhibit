package uk.gov.courtservice.xhibit.client.order.xml;

import java.util.Vector;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import uk.gov.courtservice.xhibit.client.order.OrderData;

public class MOClientXMLHelper {

	/**
     * A monetary orders hack/fix
     * @param xod
     * @return
     */
    public String getCollectionCentreNodeAsString(OrderData od) {
    	String retStr = "";
    	XMLOrderData xod = (XMLOrderData) od;
    	Document dom = xod.getDom();
    	//Element docEle = dom.getDocumentElement();
    	//new XMLOrderData(dom).listAllNodes(docEle.getChildNodes());
    	//Node n = (Node) xod.getValue("ord:CollectionCentreName");
    	
    	try {
    		Node node = XPathAPI.selectSingleNode(dom, "//ord:Order/ord:OrderData/ord:MonetaryOrder/ord:CollectionCentre/ord:CollectionCentreName");
    		if (node != null) {
    			System.out.println(node.getTextContent());
    			retStr = node.getTextContent();
    		}
    	} catch (TransformerException te) {
    		te.printStackTrace();
    	}
    	
    	return retStr;
    }
    
    /**
     * Used to ensure the combo box for collection centres is populated correctly for copied orders
     * 
     * @param od
     * @return
     */
    public String getDefaultCollectionCentreNameForComboBox(Vector v) {
    	String defaultCollectionCentre = "";
    	
    	if ((v != null) && (v.get(0) != null) && (v.get(0).toString().trim().length() > 0)) {
    		if (v.get(0).toString().trim().indexOf("\t") == -1) {
    			defaultCollectionCentre = v.get(0).toString();
    		} else {
	        	String s1 = v.get(0).toString().trim().substring(0, v.get(0).toString().trim().indexOf("\t"));
	        	if (s1.indexOf("\n") > 0)
	        		defaultCollectionCentre = s1.substring(0, s1.indexOf("\n"));
	        	if (s1.indexOf("\t") > 0)
	        		defaultCollectionCentre = s1.substring(0, s1.indexOf("\t"));
    		}
        }
    	
    	return defaultCollectionCentre;
    }
}
