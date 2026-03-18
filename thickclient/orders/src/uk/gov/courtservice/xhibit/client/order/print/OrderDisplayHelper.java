package uk.gov.courtservice.xhibit.client.order.print;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.TooManyListenersException;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.log4j.Logger;
import org.apache.xalan.trace.PrintTraceListener;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.gui.general.TimerThread;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.print.helper.AbstractPrintHelper;

/**
 * <p>
 * Title: Order Display Helper
 * </p>
 * <p>
 * Description: Generates the Orders Right Hand Panel as a JPanel. This performs
 * a FOP transform on a DOM, using a streamed XSLT ffile
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003</p
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 01-10-2003 AW Daley Now uses the synchronised setUpdated method on the timer
 * class
 */
public class OrderDisplayHelper extends AbstractPrintHelper implements PropertyChangeListener {
	private static final Logger log = CSServices.getLogger(OrderDisplayHelper.class);
	
    public static final String DISPLAY_MODE = "display";

    private static final String BASE_IMAGE_URL = "/resource.txt";

    private static final Logger _log = CSServices.getLogger(OrderDisplayHelper.class);

    private static OrderPreviewPanel _frame;

    private Document _dom;
    
    private Document _narrativeDOM;

    private OrderData _data;

    private TimerThread _timer = new TimerThread("timer", this);

    private OrderStatus _status;

    private String _transformName;

    private HashMap map;
    
    
    // Debug stuff
    PrintWriter pw = new PrintWriter(System.out);
    PrintTraceListener pt1 = new PrintTraceListener(pw);
    

    /**
     * Constructor using an OrderData object and streamed XSL
     * 
     * @param order
     *            OrderData as generated in the order gui
     * @param is
     *            InputStream - streamed XSL file
     * @param status
     *            OrderStatus - status of current order
     * @throws OrderTransformException
     */
    public OrderDisplayHelper(OrderData order, String is, OrderStatus status) throws OrderTransformException {
        super();
        _status = status;
        _transformName = is;
        setupDOM(order);
        map = new HashMap();
        _frame = createPreviewDialog();

        try {
            loadTransform(_dom);
        } catch (FOPException fe) {
            throw new OrderTransformException(fe);
        }
        // Only start refresh timer when everything else is ready.
        _timer.start();
    }

    /**
     * Set the DOM to an updated version
     * 
     * @param order
     */
    public void setupDOM(OrderData order) {
        XMLOrderData xmlOrderData = (XMLOrderData) order;
        xmlOrderData.addPropertyChangeListener(this);
        xmlOrderData = amendOrderDataToFixEncoding(xmlOrderData);
        _dom = xmlOrderData.getDom();
        if (xmlOrderData.getNarrativeDOM() != null) {
        	_narrativeDOM = xmlOrderData.getNarrativeDOM();
        }
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
     * Constructor using a Document object and streamed XSL
     * 
     * @param order
     *            Document
     * @param is
     *            InputStream - streamed XSL file
     * @throws OrderTransformException
     */
    public OrderDisplayHelper(Document order, InputStream is) throws OrderTransformException {
        super();
        _dom = order;
        try {
            transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(is));
        } catch (TransformerException te) {
            throw new OrderTransformException(te);
        }

        try {
            loadTransform(_dom);
        } catch (FOPException fe) {
            throw new OrderTransformException(fe);
        }
        // Only start refresh timer when everything else is ready.
        _timer.start();
    }

    /**
     * Creates an OrderPreviewPanel containing the AWT rendering of the FOP
     * document
     * 
     * @return OrderPreviewPanel
     */
    protected OrderPreviewPanel createPreviewDialog() {
        OrderPreviewPanel frame = new OrderPreviewPanel(this);
        frame.validate();

        // center window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Transforms the DOM using FOP
     * 
     * @param dom
     *            the dom
     * @throws FOPException
     */
    public void loadTransform(Document dom) throws FOPException {
        _dom = dom;
        resetRenderer();
        setScale();
        resetFop();
        setFOPBaseDir();
        map.put("mode", DISPLAY_MODE);

        try {
            setTransformer(XSLServices.getInstance().getTransformer(this._transformName, Locale.getDefault(), map));
            
            if (transformer instanceof TransformerImpl) {
                TransformerImpl transformerImpl = (TransformerImpl) transformer;
                
                TraceManager trMgr = transformerImpl.getTraceManager();
                trMgr.addTraceListener(pt1);
            }
            transformer.transform(new DOMSource(dom), new SAXResult(getFop().getDefaultHandler()));
            
            if (log.isDebugEnabled()) {
            	Transformer transformer2 = XSLServices.getInstance().getTransformer(this._transformName, Locale.getDefault(), map);
	            StringWriter writer = new StringWriter();
	            transformer2.transform(new DOMSource(dom), new StreamResult(writer));
	            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
	            log.debug("xmlDomSource = "  + output);
            }
        } catch (TransformerException te) {
            throw new FOPException("Transformer Exception", te);
        } catch (TooManyListenersException tmle) {
            tmle.printStackTrace();
        }

        _frame.showPage();
        map.clear();
    }

    /**
     * Property change listener added to the DOM. Updates the display whenever
     * DOM is updated
     * 
     * @param pce
     *            PropertyChangeEvent
     */
    public void propertyChange(PropertyChangeEvent pce) {
        _data = (OrderData) pce.getSource();
        _timer.setUpdated(true);
        // If the DOM has changed, set the status as NEW to disable
        // the Print and Signed buttons
        if (_status.getStatus() != OrderStatus.NEW // Status NOT NEW
                && // AND
                (_status.getStatus() != OrderStatus.SIGNED || _status.getStatus() != OrderStatus.SENT)) // Status NOT
        // SIGNED
        {
            _status.setStatus(OrderStatus.AMENDED);
        }
    }

    /**
     * Produces an error if the TimerThread invoked loadTransform generates an
     * exception
     * 
     * @param e
     *            Exception
     */
    public void displayCriticicalFailure(Exception e) {
        _log.error("Critical Transform Error" + e.getMessage());
        e.printStackTrace();
        throw new CSUnrecoverableException("Critical Failure with OrderDisplayHelper", e);
    }

    /**
     * Returns the scale ComboBox from the panel
     * 
     * @return the scale combo box
     */
    public JComboBox getScale() {
        return _frame.getScale();
    }

    private void setScale() {
        getRenderer().setScaleFactor(Double.parseDouble((String) getScale().getSelectedItem()) / 100);
    }

    /**
     * Returns the Document object to refresh the display
     * 
     * @return Document
     */
    public Document getDOM() {
        return _dom;
    }
    
    public Document getNarrativeDOM() {
        return _narrativeDOM;
    }

    /**
     * Returns the OrderPreviewPanel as a JPanel
     * 
     * @return JPanel
     */
    public JPanel getDisplayPanel() {
        return (JPanel) _frame;
    }

    /**
     * Stop refresh of preview pane, normally used while
     * saving/printing/signing. While saving we need to remove the narrative
     * node, therefore we cannot redisplay.
     */
    public void suspendRedisplay() {
        _timer.suspendThread();
    }

    /**
     * Resume refresh of preview pane, normally used while
     * saving/printing/signing. While saving we need to remove the narrative
     * node, therefore we cannot redisplay until narrative node returned..
     */
    public void resumeRedisplay() {
        _timer.resumeThread();
    }
    
    public boolean isRedisplaying() {
    	return _timer.getUpdated();
    }

    /**
     * Finish redisplay once and for all.
     */
    public void stopRedisplay() {
        _timer.stopThread();
    }

    /**
     * FOP requires a url to lookup an image. The root url is passed to the
     * transform to retrieve the Court Service logo
     */
    private void setFOPBaseDir() {
        URL url = this.getClass().getResource(BASE_IMAGE_URL);

        if (url != null) {
            _log.debug("$$$ BASE URL " + url.toString() + " $$$");
            String baseDir = url.toString();
            int endPoint = baseDir.lastIndexOf("/");
            baseDir = baseDir.substring(0, endPoint);
            _log.debug("$$$ Reduced BASEDIR " + baseDir + " $$$");
            map.put("basedir", baseDir);
        }
    }
    
    protected AWTRenderer getRenderer() {
    	return super.getRenderer();
    }
}
