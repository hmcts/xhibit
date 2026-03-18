package uk.gov.courtservice.xhibit.client.order.gui.general;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.swing.UIManager;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import antlr.collections.impl.Vector;
import sun.nio.cs.StandardCharsets;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.client.exceptions.OrdersInitialisationException;
import uk.gov.courtservice.xhibit.client.misc.OrderStatus;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.DataEntryReaderException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListFactory;
import uk.gov.courtservice.xhibit.client.order.io.DataEntryFormReader;
import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
import uk.gov.courtservice.xhibit.client.order.print.OrderDisplayHelper;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderGuiHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.util.D20OrderReportPopulator;
import uk.gov.courtservice.xhibit.client.order.util.Resource;
import uk.gov.courtservice.xhibit.client.order.xml.MOClientXMLHelper;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * OrderGUI forms the primary Panel for the Orders section of the Xhibit GUI It
 * is instantiated from three input streams. These input streams contain the
 * order data, the data entry template and the transform used to produce the
 * right-hand-side.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class OrderGUI extends XPanel {
    // Set default encoding to ensure that parser can handle characters
    // populated
    // by Castor
    
    private static final long serialVersionUID = 20100318L;
    
    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String ERROR_MSG = "orders.cannotCreateOrders";

    private static final Logger log = CSServices.getLogger(OrderGUI.class);

    private static StringBuffer buf = new StringBuffer();

    private OrderMainBar _mainBar;

    private OrderMainPane _orderMainPane;

    private DataEntryPanel _dataEntryPanel;

    private OrderStatus _status = new OrderStatus();
    
    private OrderInitialDataVO orderModel = new OrderInitialDataVO();

    // private OrdersExceptionDialog _val =
    // ValidatorFactory.getOrdersDialog();
    private OrderDisplayHelper _ordDisplayHelper;

    private XhbOrderValue _orderValue;

    private XhibitApplicationController _controller = null;

    private OrderGuiHelper _guiHelper;

    /**
     * Key for the Button Save label
     */
    public static final String BUTTON_SAVE_LBL = "order.button.save.label";
    
    /**
     * Key for the Buttonn Send label
     */
    public static final String BUTTON_SEND_LBL = "order.button.send.label";

    /**
     * Key for the Button Sign label
     */
    public static final String BUTTON_SIGN_LBL = "order.button.sign.label";

    /**
     * Key for the Button Print label
     */
    public static final String BUTTON_PRINT_LBL = "order.button.print.label";

    /**
     * Key for the Button Cancel Label
     */
    public static final String BUTTON_CANCEL_LBL = "order.button.cancel.label";

    /**
     * Key for the Bench Warrant code for identification
     */
    public static final String BENCH_WARRANT_CDE = "order.bench.warrant.code";

    /**
     * Keys for the Warrant After Failure codes for identification
     */
    public static final String WARRANT_AFTER_FAILURE_CDE1 = "order.warrant.after.failure.code1";
    public static final String WARRANT_AFTER_FAILURE_CDE2 = "order.warrant.after.failure.code2";
    
    /**
     * Key for the xpath reference for the signed date
     */
    public static final String XPATH_SIGN_DATE_REF = "order.xpath.signed.date.ref";

    /**
     * Key for the xpath reference for the signed information
     */
    public static final String XPATH_SIGN_REF = "order.xpath.signed.ref";

    /**
     * Key for the xpath reference for the judiciary reference
     */
    public static final String XPATH_SIGN_JUDICIARY_REF = "order.xpath.signed.judiciary.ref";

    /**
     * Key for the xpath reference for the clerk who signed the order reference
     */
    public static final String XPATH_SIGN_CLERK_REF = "order.xpath.signed.clerk.ref";
    
    /**
     * Key for the xpath reference for the clerk who sent the monetary order reference
     */
    public static final String XPATH_SENT_CLERK_REF = "order.xpath.sent.clerk.ref";

    /**
     * Key for the xpath reference for the judge
     */
    public static final String XPATH_SIGN_JUDGE_REF = "order.xpath.signed.judge.ref";

    /**
     * Key for the xpath reference for the signatory title
     */
    public static final String XPATH_SIGN_TITLE_REF = "order.xpath.signed.title.ref";

    /**
     * Key for the xpath reference for the signatory forename
     */
    public static final String XPATH_SIGN_FNAME_REF = "order.xpath.signed.forename.ref";

    /**
     * Key for the xpath reference for the signatoey surname
     */
    public static final String XPATH_SIGN_SNAME_REF = "order.xpath.signed.surname.ref";

    /**
     * Instantiates the OrderGUI from the three input streams.
     * 
     * @param orderInputStream
     *            is the order data
     * @param dataEntryFormInputStream
     *            is the data entry template
     * @param transformName
     *            is the transform for the preview.
     * @deprecated
     */
    public OrderGUI(InputStream orderInputStream, InputStream dataEntryFormInputStream, String transformName,
            InputStream narrativeInputStream) throws OrdersInitialisationException {
        setLookAndFeel();
        init(orderInputStream, dataEntryFormInputStream, transformName, narrativeInputStream, -1);

    }

    /**
     * Instantiates the OrderGUI given the OrderValue
     * 
     * @param value
     *            The XhbOrderValue containing the Orderdata
     * @param xac
     *            The controller
     * @param helper
     *            The Order Helper
     * @throws OrdersInitialisationException
     */
    public OrderGUI(XhbOrderValue value, XhibitApplicationController xac, OrderGuiHelper helper, OrderInitialDataVO model)
            throws OrdersInitialisationException {
    	this.orderModel = model;
        _guiHelper = helper;
        setLookAndFeel();
        _orderValue = value;
        _controller = xac;
        String dataXml = value.getDataXml();
        
        // When the xml gets parsed into a document any elements with no value in the format <element></element>
        // it becomes <element/>
        // This means that when XPathAPI.selectSingleNode gets called later on then it does not find it.
        // This breaks these elements and we must put a space character between them such as <element> </element>
        CharSequence s1 = "></";
        CharSequence s2 = "> </";
        String newDataXml = dataXml.replace(s1, s2);
        value.setDataXml(newDataXml);
        
        logDebug(new Object[] { "<<<<<<<<<>>>>>>>>> Order XML: ", value.getDataXml() });
        XhbOrderTemplateValue xhbOrderTemplate = value.getXhbOrderTemplate();
        String displayTransformName = xhbOrderTemplate.getDisplayTransformName();
        String narrativeTemplateName = xhbOrderTemplate.getNarrativeTemplateName();
        String editorTemplateName = xhbOrderTemplate.getEditorTemplateName();
        logDebug(new Object[] { "**** xhbOrderTemplate: ", xhbOrderTemplate });
        logDebug(new Object[] { "**** displayTransformName: ", displayTransformName });
        logDebug(new Object[] { "**** narrativeTemplateName: ", narrativeTemplateName });
        logDebug(new Object[] { "**** editorTemplateName: ", editorTemplateName });
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(newDataXml.getBytes(DEFAULT_ENCODING));
            
            // For debugging : uncomment
            //URL resource = this.getClass().getResource(editorTemplateName);
            //InputStream is1 = resource.openStream();
            //BufferedReader br = new BufferedReader(new InputStreamReader(is1));
            //String line;
            //while ((line = br.readLine()) != null) {
            //    System.out.println(line);
            //}
            //is1.close();
                        
            InputStream is1 = this.getClass().getResource(editorTemplateName).openStream();
            InputStream is2 = this.getClass().getResource(narrativeTemplateName).openStream();
            init( bais, is1, displayTransformName, is2, model.getMode());
            
            
        } catch (IOException ex) {

            throw new OrdersInitialisationException("ORDER_XXX", "Could not initialise Orders Transform resources.", ex);
        }

    }

    private void init(InputStream orderInputStream, InputStream editorTemplateInputStream, String transformName,
            InputStream narrativeInputStream, int orderMode) throws OrdersInitialisationException {
        // Boot up some reference data.
        CourtListFactory.createCourtList();
        _ordDisplayHelper = null;
        try {
            setUpGui(orderInputStream, editorTemplateInputStream, transformName, narrativeInputStream, orderMode);
        } catch (OrderReaderException ore) {
            // _val.setMessages(Resource.getOrdersErrorText(ERROR_MSG),
            // Resource.getOrdersErrorText(ERROR_TITLE));
            // _val.showOrdersExceptionDialog();
            throw new OrdersInitialisationException(Resource.getOrdersErrorText(ERROR_MSG), "Error with Order Reader.",
                    ore);
        } catch (DataEntryReaderException dre) {
            // _val.setMessages(Resource.getOrdersErrorText(ERROR_MSG),
            // Resource.getOrdersErrorText(ERROR_TITLE));
            // _val.showOrdersExceptionDialog();
            throw new OrdersInitialisationException(Resource.getOrdersErrorText(ERROR_MSG),
                    "Error with Order Data Entry.", dre);
        } catch (OrderTransformException ote) {
            // _val.setMessages(Resource.getOrdersErrorText(ERROR_MSG),
            // Resource.getOrdersErrorText(ERROR_TITLE));
            // _val.showOrdersExceptionDialog();
            throw new OrdersInitialisationException(Resource.getOrdersErrorText(ERROR_MSG),
                    "Error with Order Transform.", ote);
        }
    }
    
    private void loadDataIntoD20(InputStream orderInputStream, OrderData data) {
    	try {
    		log.debug("About to loadDataIntoD20");
    		D20OrderReportPopulator populator =  new D20OrderReportPopulator(_controller.getApplicationCaseModel().getCaseType(), data,orderModel,orderInputStream, isCreateOrder());
    		
    		populator.addConvictionDetails(orderModel);
    		populator.addOffenceDetails(orderModel.getd20Interim().equals("Y"));
    		log.debug("Data loaded into loadDataIntoD20");
    	} catch(Exception e) {
    		log.error("Error trying to load data into D20");
    		e.printStackTrace();
    		CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
    	}
    }

    private void setUpGui(InputStream orderInputStream, InputStream dataEntryFormInputStream, String transformName,
            InputStream narrativeInputStream, int orderMode) throws OrderReaderException, DataEntryReaderException,
            OrderTransformException {
        _dataEntryPanel = null;
        _orderMainPane = null;

        OrderReader reader = OrderFactory.getReader(orderInputStream);
        OrderData data = reader.read();
        
        data.mergeNarrative(((XMLOrderData) OrderFactory.getReader(narrativeInputStream).read()).getDom());
        
        // D20 specific call - for create orders only
        if (orderMode == OrderInitialDataVO.CREATE_MODE) {
	        if (this._orderValue.getXhbOrderTemplate().getEditorTemplateName().indexOf("D20") > 0) {
	        	loadDataIntoD20(orderInputStream, data);
	        }
        }
        
        // Fix for monetary orders collection centre - part 1 - 8.6.4 release
        // For view/amend orders, the Collection centre gets overwritten with the
        // default option from the combo box, so save this to put back in further down
        String ccString = "";
        if (this._orderValue.getXhbOrderTemplate().getEditorTemplateName().indexOf("MonetaryOrder") > 0) {
        	MOClientXMLHelper mx = new MOClientXMLHelper();
        	ccString = mx.getCollectionCentreNodeAsString(data);
        }
        
        
        DataEntryFormReader dataEntryFormReader = new DataEntryFormReader(data);
        _dataEntryPanel = dataEntryFormReader.read(dataEntryFormInputStream);
        
        // Fix for monetary orders collection centre - part 2 - put back the collection centre text to
        // what it should be for view/amend orders
        if ((this._orderValue.getXhbOrderTemplate().getEditorTemplateName().indexOf("MonetaryOrder") > 0)
        		&& ccString != null && ccString.trim().length() > 0) {
        	data.setValue("//ord:Order/ord:OrderData/ord:MonetaryOrder/ord:CollectionCentre/ord:CollectionCentreName", ccString);
        }
        
        

        // orderPreviewPane = new OrderPreviewPane(data, transformInputStream);
        _ordDisplayHelper = new OrderDisplayHelper(data, transformName, _status);
        _orderMainPane = new OrderMainPane(_dataEntryPanel, _ordDisplayHelper.getDisplayPanel(), _status);

        this.setLayout(new BorderLayout());
        _mainBar = new OrderMainBar(_status, _ordDisplayHelper.getScale(), data, _orderValue, _controller, this);

        // If the case has been opened in read-only mode or the user do not have
        // edit rights, display the order as though it has been signed
        // i.e. Full Screen - to prevent any changes from being made
        if (!_controller.getApplicationCaseModel().isInEditMode(FunctionList.ECreateOrder)) {
            _status.setStatus(OrderStatus.SIGNED);
        }

        add(_mainBar.getToolBar(), BorderLayout.SOUTH);
        add(_orderMainPane, BorderLayout.CENTER);
    }
    

    /**
     * Returns the Display Helper for this panel
     * 
     * @return The OrderDisplayHelper for the current preview
     */
    public OrderDisplayHelper getDisplayHelper() {
        return _ordDisplayHelper;
    }

    /**
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
           // does nothing
    }

    /**
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
            // does nothing
    }

    /**
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        // does nothing
    }

    /**
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // does nothing
    }

    /**
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
            // does nothing
        }

    /**
     * 
     * @param b
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean b) throws CSRecoverableException {
        // does nothing
        }

    /**
     * Sets the look and feel to match that of the main application
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            logLookAndFeelError(ex);
        }
    }
    
    /**
     * If this is a Create Order then the order value will be -1, otherwise it will be the value of
     * XHB_ORDER.ORDER_ID
     * 
     * @return
     */
    private boolean isCreateOrder() {
    	if (_orderValue.getOrderId() != null) {
    		return _orderValue.getOrderId() < 0;
    	} else {
    		return true;
    	}
    }

    private void logLookAndFeelError(Exception ex) {
        logError(new Object[] { "OrderGUI: setLookAndFeel: ", UIManager.getSystemLookAndFeelClassName(), " ",
                ex.getMessage() });
    }

    /**
     * Stops the redisplay of the preview if the thread is stopped
     */
    public void cleanup() {
        this.getDisplayHelper().stopRedisplay();
    }

    /**
     * Close the dialog
     */
    public void dispose() {
        if (_guiHelper != null) {
            _guiHelper.cleanup();
        }
    }

    private void logDebug(Object[] obj) {
        log.debug(getBuffer(obj));
        buf.delete(0, buf.length());
    }

    private void logError(Object[] obj) {
        log.error(getBuffer(obj));
        buf.delete(0, buf.length());
    }

    private StringBuffer getBuffer(Object[] obj) {
        for (int x = 0; x < obj.length; x++) {
            buf.append(obj[x]);
        }
        return buf;
    }
}
