package uk.gov.courtservice.xhibit.client.order.print;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.transform.Transformer;

import org.apache.fop.apps.FOPException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XSLServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.print.helper.AbstractPrintHelper;

/**
 * <p>
 * Title: Order Print Helper class.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class provides the basic print services to the Orders GUI.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby / Neil Entwistle.
 * @version 1.0
 */

public class OrderPrintHelper extends AbstractPrintHelper {
    private static final Logger log = CSServices.getLogger(OrderPrintHelper.class);

    private static final String BASE_IMAGE_URL = "/resource.txt";

    public static final String PRINT_MODE = "print";

    protected HashMap map;

    private String transformerName;

    /**
     * Constructor using an OrderData object and streamed XSL
     * 
     * @param order
     *            OrderData as generated in the order gui
     * @param is
     *            InputStream - streamed XSL file
     * @throws OrderTransformException
     */
    public OrderPrintHelper(String is) throws OrderTransformException {
        super();
        transformerName = is;
        map = new HashMap();
    }

    /**
     * Transforms the DOM using FOP
     * 
     * @throws FOPException
     */
    public void loadTransform(Document dom) throws FOPException {
    	resetRenderer();
    	getRenderer().setScaleFactor(1);
        resetFop();
        setFOPBaseDir();
        map.put("mode", PRINT_MODE);
        super.loadTransform(dom);
    }
    
    @Override
    protected void setTransformer(Transformer transformer) {
    	 super.setTransformer(XSLServices.getInstance().getTransformer(this.transformerName,
                 Locale.getDefault(), map));
    }
    
    /**
     * Print the transformed data.
     * 
     * @param order
     *            the current/latest version of the OrderData
     * @throws OrderTransformException
     * @throws CSRecoverableException
     */
    public void printTransform(OrderData order) throws OrderTransformException, CSRecoverableException {
    	map.clear();
    	XMLOrderData xmlOrderData = (XMLOrderData) order;
        Document dom = xmlOrderData.getDom();
        try {
            loadTransform(dom);
        } catch (FOPException fe) {
            throw new OrderTransformException(fe);
        }
        super.print();
    }

    /**
     * FOP requires a url to lookup an image. The root url is passed to the
     * transform to retrieve the Court Service logo
     */
    private void setFOPBaseDir() {
        URL url = this.getClass().getResource(BASE_IMAGE_URL);

        if (url != null) {
            log.debug("$$$ BASE URL " + url.toString() + " $$$");
            String baseDir = url.toString();
            int endPoint = baseDir.lastIndexOf("/");
            baseDir = baseDir.substring(0, endPoint);
            log.debug("$$$ Reduced BASEDIR " + baseDir + " $$$");
            log.debug("Map value setFOPBaseDir>>>>>>>>>>>>>>>>>>>>>>>>>>" + map);
            map.put("basedir", baseDir);
        }
    }
}