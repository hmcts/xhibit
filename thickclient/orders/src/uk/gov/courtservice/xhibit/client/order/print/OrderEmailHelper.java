package uk.gov.courtservice.xhibit.client.order.print;

import java.io.ByteArrayOutputStream;

import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;

/**
 * <p>
 * Title: Order Email Helper class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class provides the methods to create the PDF to be sent for monetary orders
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */

public class OrderEmailHelper extends OrderPrintHelper {

    private ByteArrayOutputStream out;
    
    /**
     * Constructor using an OrderData object and streamed XSL
     * 
     * @param is
     *            InputStream - streamed XSL file
     * @throws OrderTransformException
     */
    public OrderEmailHelper(String is) throws OrderTransformException {
        super(is);
    }
    
    @Override
    protected synchronized void resetFop() throws FOPException {
    	// Remove any screen renderer overrides
    	getFOUserAgent().setRendererOverride(null);
    	// Reset the FOP to the output file
    	super.resetFop(out); 
    }
    
    /**
     * Checking monetary orders for dodgy characters
     * 
     * @param inData
     * @return
     */
    private XMLOrderData amendOrderDataToFixEncoding(XMLOrderData inData) {
    	try {
    		Node node = XPathAPI.selectSingleNode(inData.getDom(), "//ord:MonetaryOrder/ord:MonetaryDisposals/ord:MonetaryDisposalsList");
    		if (node != null) {
    			String nv = node.getTextContent();
    			if (nv != null) {
    				nv = removeUnwantedChars(nv);
    				node.setTextContent(nv);
    			}
    		}
    		node = XPathAPI.selectSingleNode(inData.getDom(), "//ord:MonetaryOrder/ord:MonetaryTotals/ord:MonetaryTotalsList");
    		if (node != null) {
    			String nv = node.getTextContent();
    			if (nv != null) {
    				nv = removeUnwantedChars(nv);
    				node.setTextContent(nv);
    			}
    		}
    		node = XPathAPI.selectSingleNode(inData.getDom(), "//ord:MonetaryOrder/ord:PaymentRateValue");
    		if (node != null) {
    			String nv = node.getTextContent();
    			if (nv != null && nv.length() > 0) {
    				nv = removeUnwantedChars(nv);
    				node.setTextContent(nv);
    			}
    		}
    		node = XPathAPI.selectSingleNode(inData.getDom(), "//ord:MonetaryOrder/ord:ParentGuardianName");
    		if (node != null) {
    			String nv = node.getTextContent();
    			if (nv != null && nv.length() > 0) {
    				nv = removeUnwantedChars(nv);
    				node.setTextContent(nv);
    			}
    		}
    		node = XPathAPI.selectSingleNode(inData.getDom(), "//ord:MonetaryOrder/ord:AdditionalDetails");
    		if (node != null) {
    			String nv = node.getTextContent();
    			if (nv != null && nv.length() > 0) {
    				nv = removeUnwantedChars(nv);
    				node.setTextContent(nv);
    			}
    		}
    	} catch (TransformerException te) {
    		te.printStackTrace();
    	}
    	return inData;
    }
    
    private String removeUnwantedChars(String inStr) {
    	StringBuffer outStringB = new StringBuffer(inStr);
    	int startChar;
        do {
        	startChar = outStringB.indexOf("Â");
        	if (startChar >= 0) {
        		outStringB = outStringB.replace(startChar, startChar+1, "");
        	}
        } while (outStringB.indexOf("Â") > 0);
    	
    	return outStringB.toString();
    }

    /**
     * Print the transformed data.
     * 
     * @param order
     *            the current/latest version of the OrderData
     * @throws OrderTransformException
     * @throws CSRecoverableException
     */
    public byte[] createPDF(OrderData order) throws OrderTransformException, CSRecoverableException {
    	map.clear();
		out = new ByteArrayOutputStream();
    	XMLOrderData xmlOrderData = (XMLOrderData) order;
        xmlOrderData = amendOrderDataToFixEncoding(xmlOrderData);
        Document dom = xmlOrderData.getDom();
        try {
            loadTransform(dom);
        } catch (FOPException fe) {
            throw new OrderTransformException(fe);
        }
        //super.print();
        byte[] pdfBytes = out.toByteArray();
        return pdfBytes;
    }
}