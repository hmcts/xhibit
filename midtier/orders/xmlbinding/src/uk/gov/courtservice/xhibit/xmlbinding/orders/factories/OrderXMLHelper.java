package uk.gov.courtservice.xhibit.xmlbinding.orders.factories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.types.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationProblem;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryDisposals;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryOrder;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryOrderStructure;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.MonetaryTotals;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderData;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderHeader;
import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.types.OrderTypes;
import uk.gov.courtservice.xhibit.xmlbinding.orders.FormatHelper;
import uk.gov.courtservice.xhibit.xmlbinding.orders.OrderHeaderHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 02-09-2003 AW Daley ValidationHelper modified to hold the default order.
 */

public abstract class OrderXMLHelper extends XmlHelper {
    private static Logger log = CSServices.getLogger(OrderXMLHelper.class);

    public static final String BLANK_SCHEMA_PATH = "/blankschemas/";

    public static final String BLANK_SCHEMA_EXTENSION = ".xml";

    public static final int BUFFER_SIZE = 8192;

    // Assuming we can share instance of DefendantOnCaseHome across all
    // instances.
    private static final DefendantOnCaseHome docHome;

    private static final XhbOrderTypeHome xhbOrderTypeHome;

    /**
     * The original order type
     */
    protected String originalOrderTypeCode;

    // Statically initialise the required home interface.
    // Should be done for all home interfaces used.
    static {
        String fullClassName = DefendantOnCaseHome.class.getName();
        int lastDotIndex = fullClassName.lastIndexOf('.');

        String jndiName = fullClassName.substring(lastDotIndex + 1, fullClassName.length());

        System.err.println("**______________________________**");
        System.err.println(fullClassName);
        System.err.println(jndiName);
        System.err.println("**______________________________**");

        docHome = (DefendantOnCaseHome) CSServices.getServiceLocator().getLocalHome(DefendantOnCaseHome.class);
        xhbOrderTypeHome = (XhbOrderTypeHome) CSServices.getServiceLocator().getLocalHome(XhbOrderTypeHome.class);
    }

    private String blankSchema;

    /**
     * <p/> This constructor creates the helper, retrieving a copy of the empty
     * schema for the particular order type.
     * </p>
     * <p/> The constructor signature should always be repeated in the
     * constructors of children to allow the XmlHelperFactoryImpl to
     * successfully create their instances.
     * </p>
     * 
     * @param typeCode
     *            the type code to create the helper for.
     */
    public OrderXMLHelper(String typeCode) {
        super(typeCode);

        // Find the blank schema
        InputStream schemaInputStream = this.getClass().getResourceAsStream(
                BLANK_SCHEMA_PATH + typeCode + BLANK_SCHEMA_EXTENSION);

        if (schemaInputStream == null)
            throw new CSConfigurationException("Could not retrieve the blank schema for this type code.");

        // read the blank scheam
        BufferedReader schemaReader = new BufferedReader(new InputStreamReader(schemaInputStream));

        StringBuffer schemaBuildBuffer = new StringBuffer();
        char[] schemaReadBuffer = new char[BUFFER_SIZE];
        try {
            int readChars = schemaReader.read(schemaReadBuffer, 0, BUFFER_SIZE);
            while (readChars > -1) {
                schemaBuildBuffer.append(schemaReadBuffer, 0, readChars);
                readChars = schemaReader.read(schemaReadBuffer, 0, BUFFER_SIZE);
            }
        } catch (IOException ioe) {
            throw new CSConfigurationException("There was a problem retrieving the blank schema for this type code.",
                    ioe);
        }

        // Store internally.
        blankSchema = schemaBuildBuffer.toString();
        System.err.println(blankSchema);
    }

    /**
     * This method is used to validate the XML to various degrees of validation.
     * <p/> To select multiple levels of validation, <b>add</b> the different
     * validation levels together for the call. For Example: <p/>
     * 
     * <pre>
     * validateOrderXML(defendantOnCaseID, orderXML, XmlHelper.LOGICAL_VALIDATION + XmlHelper.COMPLETENESS_VALIDATION);
     * </pre>
     * 
     * @param defendantOnCaseID
     *            The defendant on the case for which to validate the XML.
     * @param orderXML
     *            The XML to be validated.
     * @param validation
     *            The validation to apply.
     * @throws OrderXMLValidationException
     *             When there is a problem in the validation.
     */
    public String validateOrderXML(Integer defendantOnCaseID, String orderXML, int validationLevel)
            throws OrderValidationException, OrderXMLException {
    	// Remove any chars that cause the unmarshal to crash (ie |). 
    	orderXML = MarshalSafeString.getSafeText(orderXML);
    	orderXML = MarshalSafeString.replaceSubstring(orderXML, "0000-00-00", ""); // Replace invalid dates that are auto-populated but do not validate if Date field is empty/unassigned
        try {
            // Set up the validation helper.
            // Cast now required as OrderType
            Order order = (Order) Order.unmarshal(new StringReader(orderXML));
            OrderData defaultOrder = this.getEmptySchema().getOrderData();
            ValidationHelper validationHelper = new ValidationHelper(order, defaultOrder, validationLevel);

            // perform the various validation checks.
            logicalValidation(defendantOnCaseID, validationHelper);
            completenessValidation(validationHelper);
            databaseValidation(defendantOnCaseID, validationHelper);

            // Check that the validation passed.
            validationHelper.checkValidationResult();
            if (log.isDebugEnabled()) {
                log.debug("$$$>>>>>> orderXML BEFORE = " + orderXML);
            }
            orderXML = validateOrderStructure(orderXML, validationHelper);
            if (log.isDebugEnabled()) {
                log.debug("$$$>>>>>> orderXML AFTER = " + orderXML);
            }
            orderXML = MarshalSafeString.replaceSubstring(orderXML, "0000-00-00", ""); // Replace invalid dates that are auto-populated but do not validate if Date field is empty/unassigned
            return orderXML;
        } catch (ValidationException ex) {
            log.error("Invalid schema:\n" + orderXML, ex);
            throw new OrderXMLException("order.validation.general.error", "Schema for this order type was invalid: "
                    + typeCode, ex);
        } catch (MarshalException ex) {
            log.error("Could not marshal schema:\n" + orderXML, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal populated schema for this order type: " + typeCode, ex);
        }
    }

    /**
     * This method is used to validate the XML so that dependent elements are
     * set correctly
     * 
     * @param defendantOnCaseID
     *            The defendant on the case for which to validate the XML.
     * @param orderXML
     *            The XML to be validated.
     * @param validationHelper
     *            The helper.
     * @return the validated xml
     * @throws OrderValidationException
     *             When there is a problem in the validation.
     * @throws OrderXMLException
     *             If the xml cannot be marshalled
     */
    public String validateOrderStructure(String orderXML, ValidationHelper validationHelper)
            throws OrderValidationException, OrderXMLException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("$$$>>>>> validateOrder initial XML :" + orderXML);
            }
            // Set up the validation helper.

            structuralValidation(validationHelper);

            // Check that the validation passed.
            validationHelper.checkValidationResult();
            return getXML(validationHelper.getOrderToBeValidated());
        } catch (ValidationException ex) {
            log.error("Invalid schema:\n" + orderXML.toString(), ex);
            throw new OrderXMLException("order.validation.general.error", "Schema for this order type was invalid: "
                    + typeCode, ex);
        } catch (MarshalException ex) {
            log.error("Could not marshal schema:\n" + orderXML.toString(), ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal populated schema for this order type: " + typeCode, ex);
        }
    }

    /**
     * Sub-classes should override this method to provide validation of the
     * internal logical consistency of information in the order xml, no
     * assumptions should be made about the order's completeness.
     * 
     * @param validationHelper
     *            Contains the XML and the accumulated errors/warnings.
     * @param defendantOnCaseID
     * 			  The defendsnt on this case ID
     */
    protected void logicalValidation(Integer defendantOnCaseID, ValidationHelper validationHelper) {
        // Intentionally implemented empty not abstract so that sub-class
        // implementation optional.
    }

    /**
     * Sub-classes should override this method to provide validation of the
     * internal structural consistency of information in the order xml, no
     * assumptions should be made about the order's completeness.
     * 
     * @param validationHelper
     *            Contains the XML and the accumulated errors/warnings.
     */
    protected String structuralValidation(ValidationHelper validationHelper) throws ValidationException,
            MarshalException, OrderXMLException {
        // Intentionally implemented empty not abstract so that sub-class
        // implementation optional.
        return OrderXMLHelper.getXML(validationHelper.getOrderToBeValidated());
    }

    /**
     * Sub-classes should override this method to provide validation of the
     * completeness of the order.
     * 
     * @param validationHelper
     *            Contains the XML and the accumulated errors/warnings.
     */
    protected void completenessValidation(ValidationHelper validationHelper) {
        // Intentionally implemented empty not abstract so that sub-class
        // implementation optional.
    }

    /**
     * Sub-classes should override this method to provide validation of the
     * consistency of information in the order xml with information in the
     * database, no assumptions should be made about the order's completeness.
     * 
     * @param validationHelper
     *            Contains the XML and the accumulated errors/warnings.
     */
    protected void databaseValidation(Integer defendantOnCaseID, ValidationHelper validationHelper) {
        // Intentionally implemented empty not abstract so that sub-class
        // implementation optional.
    }
    
    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * Used for B cases where we do not have a defendant on case id
     * 
     * @param caseId
     *            the identifier for the case.
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public String getOrderXML(Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId, boolean isBCase) throws OrderXMLException {
        try {
            // Retrieve entity for DefendantOnCase.
            
            DefendantOnCase doc = null;
            if (isBCase) {
                // Set dummy doc data for B cases as no defendant on case exists
                //doc = setDummyDOCData(caseId, caseTitle);
            }
            
            // Get instance of empty schema.
            Order order = getEmptySchema();

            // Allow the subclasses to populate the schema.
            populateSchema(order, caseId, xhibitCaseId, caseTitle, courtId);

            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(order, xmlOutput);
            return xmlOutput.toString();
        } catch (MarshalException me) {
            log.error("$$$ MarshalException: " + me, me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal generated schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException: " + ve, ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Schema generated for this order type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException: " + ex, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }

    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public String getOrderXML(Integer defendantOnCaseID) throws OrderXMLException {
        try {
            // Retrieve entity for DefendantOnCase.
            DefendantOnCase doc = docHome.findByPrimaryKey(defendantOnCaseID);

            // Get instance of empty schema.
            Order order = getEmptySchema();

            // Allow the subclasses to populate the schema.
            populateSchema(order, doc);

            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(order, xmlOutput);
            return xmlOutput.toString();
        } catch (FinderException fe) {
            log.error("$$$ FinderException" + fe, fe);
            throw new OrderXMLException("order.validation.general.error", "An invalid defendant on case was passed.",
                    fe);
        } catch (MarshalException me) {
            log.error("$$$ MarshalException: " + me, me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal generated schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException: " + ve, ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Schema generated for this order type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException: " + ex, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }
    
    /**
     * The xml returned here contains the blank schema data
     * 
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public String getOrderXML() throws OrderXMLException {
        try {
            // Get instance of empty schema.
            Order order = getEmptySchema();

            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(order, xmlOutput);
            return xmlOutput.toString();
        } catch (MarshalException me) {
            log.error("$$$ MarshalException: " + me, me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal generated schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException: " + ve, ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Schema generated for this order type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException: " + ex, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }
    
    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public String getOrderXML(Integer defendantOnCaseID, String monetaryDisposalInfoForDisplayOnOrder, String monetaryTotalsForDisplayOnOrder) throws OrderXMLException {
        try {
            // Retrieve entity for DefendantOnCase.
            DefendantOnCase doc = docHome.findByPrimaryKey(defendantOnCaseID);
            
            MonetaryDisposals md = new MonetaryDisposals();
            md.setMonetaryDisposalsList(monetaryDisposalInfoForDisplayOnOrder);
            MonetaryTotals mt = new MonetaryTotals();
            mt.setMonetaryTotalsList(monetaryTotalsForDisplayOnOrder);

            // Get instance of empty schema.
            Order order = getEmptySchema();
            // Deal with monetary orders
            if (order.getOrderData().getMonetaryOrder() != null) {
                MonetaryOrderStructure mos = order.getOrderData().getMonetaryOrder();
                mos.setMonetaryDisposals(md);
                mos.setMonetaryTotals(mt);
                order.getOrderData().setMonetaryOrder((MonetaryOrder)mos);
            }

            // Allow the subclasses to populate the schema.
            populateSchema(order, doc);

            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(order, xmlOutput);
            return xmlOutput.toString();
        } catch (FinderException fe) {
            log.error("$$$ FinderException" + fe, fe);
            throw new OrderXMLException("order.validation.general.error", "An invalid defendant on case was passed.",
                    fe);
        } catch (MarshalException me) {
            log.error("$$$ MarshalException: " + me, me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal generated schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException: " + ve, ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Schema generated for this order type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException: " + ex, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }
    
    /**
     * Used for B cases where no defendant on case exists but doc data is needed throughout
     * 
     * Need caseId, surname (which is the case title)
     * 
     * @return
     */
    private DefendantOnCase setDummyDOCData(Integer caseId, String surname ) {
        Defendant defendant = null;
        defendant.setSurname(surname);

        DefendantOnCase doc = null;
        doc.setCaseId(caseId);
        doc.setDefendant(defendant);
        
        return doc;
        
    }

    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public String getOrderXML(Integer defendantOnCaseID, String originalXML, Integer originalTypeId)
            throws OrderXMLException {
        try {
            // Retrieve entity for DefendantOnCase.
            DefendantOnCase doc = docHome.findByPrimaryKey(defendantOnCaseID);
            // Get instance of empty schema.
            Order newOrder = getEmptySchema();

            // Allow the subclasses to populate the schema.
            populateSchema(newOrder, doc);

            StringReader reader = new StringReader(originalXML);
            log.debug("<<<>>> ORIGINALXML: " + originalXML);
            Order originalOrder = getEmptySchema();

            getOrderHeadersForRevision(newOrder, unmarshalXML(originalOrder, reader), originalTypeId);

            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(newOrder, xmlOutput);

            log.debug("<<<>>> NEWXML: " + xmlOutput.toString());
            return xmlOutput.toString();
        } catch (FinderException fe) {
            log.error("$$$ FinderException" + fe, fe);
            throw new OrderXMLException("order.validation.general.error", "An invalid defendant on case was passed.",
                    fe);
        } catch (MarshalException me) {
            log.error("$$$ MarshalException: " + me, me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not marshal generated schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error("$$$ ValidationException: " + ve, ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Schema generated for this order type was invalid: " + typeCode, ve);
        } catch (IOException ex) {
            log.error("$$$ IOException: " + ex, ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: " + typeCode,
                    ex);
        }
    }

    public static String getXML(Order order) throws ValidationException, MarshalException, OrderXMLException {
        try {
            // return the generated XML
            StringWriter xmlOutput = new StringWriter();
            marshalXML(order, xmlOutput);
            return xmlOutput.toString();
        } catch (IOException ex) {
            log.error("$$$ IOException" + ex);
            throw new OrderXMLException("order.validation.general.error",
                    "Insane exception due to a string not being successfully wrapped in a StringWriter: ", ex);
        }

    }

    private static void marshalXML(Order order, StringWriter xmlOutput) throws IOException, ValidationException,
            MarshalException {
        Marshaller marshaller = null;
        marshaller = new Marshaller(xmlOutput);
        marshaller.setNamespaceMapping("apd", "http://www.govtalk.gov.uk/people/AddressAndPersonalDetails");
        marshaller.setNamespaceMapping("bs7666", "http://www.govtalk.gov.uk/people/bs7666");
        marshaller.setNamespaceMapping("cs", "http://www.courtservice.gov.uk/schemas/courtservice");
        marshaller.setNamespaceMapping("nar",
                "http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders/narrative");
        marshaller.setNamespaceMapping("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        marshaller.setNamespaceMapping("ord", "http://www.courtservice.gov.uk/schemas/courtservice/xhibit/orders");

        // order.marshal(xmlOutput);
        marshaller.marshal(order);
    }

    private Order unmarshalXML(Order originalOrder, StringReader xmlInput) throws IOException, ValidationException,
            MarshalException, OrderXMLException {

        log.debug("Start - unmarshalXML");
        Unmarshaller unmarshaller = new Unmarshaller(originalOrder);
        return (Order) unmarshaller.unmarshal(originalOrder.getClass(), xmlInput);
    }

    private void getOrderHeadersForRevision(Order newOrder, Order originalOrder, Integer originalTypeId)
            throws OrderXMLException {
        OrderHeader originalOrderHeader = null;
        OrderHeader newOrderHeader = null;

        try {
            Integer newOrderType = xhbOrderTypeHome.findByCodeUniquely(getTypeCode()).getOrderTypeId();
            originalOrderHeader = getOrderHeaderForType(originalTypeId, originalOrder);
            newOrderHeader = getOrderHeaderForType(newOrderType, newOrder);
        } catch (FinderException ex) {
            log.error("$$$ FinderException" + ex, ex);
            throw new OrderXMLException("order.validation.general.error", "An invalid order type was passed.", ex);
        }

        log.debug((null == originalOrderHeader) ? "originalOrderHeader is NULL" : originalOrderHeader.toString());
        log.debug((null == newOrderHeader) ? "newOrderHeader is NULL" : newOrderHeader.toString());
        setRevisionDetails(newOrderHeader, originalOrderHeader);
        log.debug("unmarshalXML revision no.: " + newOrderHeader.getRevisionDetails().getRevisionNo());
        log.debug("End - unmarshalXML");
    }

    /**
     * 
     * @param orderTypeId
     * @param order
     * @return
     */
    private static OrderHeader getOrderHeaderForType(Integer orderTypeId, Order order) {
        /* 
         * Order types held as enumeration on the OrderTypes.xsd schema, but these start from 0, so
         * decrement order type by 1 to check.
         */
        switch (orderTypeId.intValue() - 1) {
        case OrderTypes.BAILORDER_TYPE: {
            log.debug("<<<>>> BAILORDER_TYPE");
            return order.getOrderData().getBailOrder().getOrderHeader();
        }
        case OrderTypes.BAILORDERBCASE_TYPE: {
            log.debug("<<<>>> BAILORDERBCASE_TYPE");
            return order.getOrderData().getBailOrderBCase().getOrderHeader();
        }
        case OrderTypes.BENCHWARRANT_TYPE: {
            log.debug("<<<>>> BENCHWARRANT_TYPE");
            return order.getOrderData().getBailOrder().getOrderHeader();
        }
        case OrderTypes.MONETARYORDER_TYPE: {
            log.debug("<<<>>> MONETARYORDER_TYPE");
            return order.getOrderData().getMonetaryOrder().getOrderHeader();
        }
        case OrderTypes.D20_TYPE: {
            log.debug("<<<>>> D20_TYPE");
            return order.getOrderData().getD20().getOrderHeader();
        }
        case OrderTypes.CPORDER_TYPE: {
            log.debug("<<<>>> CPORDER_TYPE");
            return order.getOrderData().getCPOrder().getOrderHeader();
        }
        case OrderTypes.CPRORDER_TYPE: {
            log.debug("<<<>>> CPRORDER_TYPE");
            return order.getOrderData().getCPROrder().getOrderHeader();
        }
        case OrderTypes.YOIORDER_TYPE: {
            log.debug("<<<>>> YOIORDER_TYPE");
            return order.getOrderData().getYOIOrder().getOrderHeader();
        }
        case OrderTypes.CRORDER_TYPE: {
            log.debug("<<<>>> CRORDER_TYPE");
            return order.getOrderData().getCROrder().getOrderHeader();
        }
        case OrderTypes.IMPRISONMENTORDER_TYPE: {
            log.debug("<<<>>> IMPRISONMENTORDER_TYPE");
            return order.getOrderData().getImprisonmentOrder().getOrderHeader();
        }
        case OrderTypes.REMANDORDER_TYPE: {
            log.debug("<<<>>> REMANDORDER_TYPE");
            return order.getOrderData().getRemandOrder().getOrderHeader();
        }
        case OrderTypes.COORDER_TYPE: {
            log.debug("<<<>>> COORDER_TYPE");
            return order.getOrderData().getCOOrder().getOrderHeader();
        }
        case OrderTypes.SUSORDER_TYPE: {
            log.debug("<<<>>> SUSORDER_TYPE");
            return order.getOrderData().getSUSOrder().getOrderHeader();
        }
        case OrderTypes.IMPRISONMENTORDER5035C_TYPE: {
            log.debug("<<<>>> IMPRISONMENTORDER5035C_TYPE");
            return order.getOrderData().getImprisonmentOrder5035C().getOrderHeader();
        }
       
        case OrderTypes.YOUNGOFFENDERSORDER5044C_TYPE: {
            log.debug("<<<>>> YOUNGOFFENDERSORDER5044C_TYPE");
            return order.getOrderData().getYoungOffendersOrder5044().getOrderHeader();
        }
        case OrderTypes.YOUNGOFFENDERSORDER5044D_TYPE: {
            log.debug("<<<>>> YOUNGOFFENDERSORDER5044D_TYPE");
            return order.getOrderData().getYoungOffendersOrder5044().getOrderHeader();
        }
        case OrderTypes.BWAORDER_TYPE: {
            log.debug("<<<>>> BWAORDER_TYPE");
            return order.getOrderData().getBenchWarrant().getOrderHeader();
        }
        case OrderTypes.BWCORDER_TYPE: {
            log.debug("<<<>>> BWCORDER_TYPE");
            return order.getOrderData().getBenchWarrant().getOrderHeader();
        }
        case OrderTypes.COORDER_PRE_LASBO_TYPE: {
            log.debug("<<<>>> COORDER_PRE_LASBO_TYPE");
            return order.getOrderData().getCOOrder().getOrderHeader();
        }
        case OrderTypes.SUSORDER_PRE_LASBO_TYPE: {
            log.debug("<<<>>> SUSORDER_PRE_LASBO_TYPE");
            return order.getOrderData().getSUSOrder().getOrderHeader();
        }
        case OrderTypes.IMPRISONMENTORDER5035C_PRE_LASBO_TYPE: {
            log.debug("<<<>>> IMPRISONMENTORDER5035C_PRE_LASBO_TYPE");
            return order.getOrderData().getImprisonmentOrder5035C().getOrderHeader();
        }       
        case OrderTypes.YOUNGOFFENDERSORDER5044C_PRE_LASBO_TYPE: {
            log.debug("<<<>>> YOUNGOFFENDERSORDER5044C_PRE_LASBO_TYPE");
            return order.getOrderData().getYoungOffendersOrder5044().getOrderHeader();
        }
        case OrderTypes.YOUNGOFFENDERSORDER5044D_PRE_LASBO_TYPE: {
            log.debug("<<<>>> YOUNGOFFENDERSORDER5044D_PRE_LASBO_TYPE");
            return order.getOrderData().getYoungOffendersOrder5044().getOrderHeader();
        }

        case OrderTypes.NOTICEOFDEFERMENTSENTENCEORDER_TYPE: {
            log.debug("<<<>>> NOTICEOFDEFERMENTSENTENCEORDER_TYPE");
            return order.getOrderData().getNoticeOfDefermentSentenceOrder().getOrderHeader();
        }

        case OrderTypes.BREACHSUSPENDEDSENTENCEORDER_TYPE: {
            log.debug("<<<>>> BREACHSUSPENDEDSENTENCEORDER_TYPE");
            return order.getOrderData().getBreachSuspendedSentenceOrder().getOrderHeader();
        }

        case OrderTypes.NOTICEBREACHSUSPENDEDSENTENCE_TYPE: {
            log.debug("<<<>>> NOTICEBREACHSUSPENDEDSENTENCE_TYPE");
            return order.getOrderData().getNoticeBreachSuspendedSentence().getOrderHeader();
        }
 
        case OrderTypes.ACTIONCONDITIONALDISCHARGEORDER_TYPE: {
            log.debug("<<<>>> ACTIONCONDITIONALDISCHARGEORDER_TYPE");
            return order.getOrderData().getActionConditionalDischargeOrder().getOrderHeader();
        }
        case OrderTypes.BREACHCONDITIONALDISCHARGEORDER_TYPE: {
            log.debug("<<<>>> BREACHCONDITIONALDISCHARGEORDER_TYPE");
            return order.getOrderData().getBreachConditionalDischargeOrder().getOrderHeader();
        }
          default:

            return null;
        }
    }

    /**
     * Gets the empty Castor bound skeleton for this particular XMLHelper.
     * 
     * @return an instance of the empy schema
     * @throws OrderXMLException
     *             when there is a problem with the shema.
     */
    private Order getEmptySchema() throws OrderXMLException {
        // Get empty order.
        StringReader emptySchemaReader = new StringReader(blankSchema);

        try {
            // Cast now required
            return (Order) Order.unmarshal(emptySchemaReader);
        } catch (MarshalException me) {
            log.error(me);
            throw new OrderXMLException("order.validation.general.error",
                    "Could not unmarshal empty schema for this order type: " + typeCode, me);
        } catch (ValidationException ve) {
            log.error(ve);
            throw new OrderXMLException("order.validation.general.error",
                    "Empty schema for this order type was invalid: " + typeCode /* , ve */);
        }
    }

    /**
     * @return the type code for this xml helper.
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @return the type code for the original order.
     */
    public String getOriginalOrderTypeCode() {
        return originalOrderTypeCode;
    }

    /**
     * @return the type code for the original order.
     */
    public void setOriginalOrderTypeCode(String code) {
        this.originalOrderTypeCode = code;
    }

    /**
     * Subclasses should implement this to provide order specific population.
     * 
     * @param order
     *            the Castor-Bound order schema to be populated.
     * @param doc
     *            the DedendantOnCase entity bean for which to populate this
     *            order.
     * @throws OrderXMLException
     *             when there is a problem.
     */
    protected abstract void populateSchema(Order order, DefendantOnCase doc) throws OrderXMLException;
    
   
    /**
     * For B cases only
     * @param order
     * @param caseId
     * @param surname
     */
    protected void populateSchema(Order order, Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId) throws OrderXMLException {
        OrderHeader orderHeader = null;
        if (order.getOrderData().getBailOrder() != null) {
            orderHeader = order.getOrderData().getBailOrder().getOrderHeader();
        } else { // Could be a B case
            orderHeader = order.getOrderData().getBailOrderBCase().getOrderHeader();
        }
        orderHeader.setOrderType(this.getTypeCode());
        OrderHeaderHelper.populateHeader(orderHeader, courtId);

        // Populate case number.
        String caseType = "B";
        
        // Make sure that the number is correctly padded.
        String trueCaseNumber = caseType + FormatHelper.EIGHT_DIGIT.format(caseId);
        orderHeader.setCaseNumber(trueCaseNumber);

        // Set date to today's date. If not set to a date, then will
        // get nasty exception.
        orderHeader.setOrderDate(new Date(new java.util.Date()));
    }

    /**
     * Sets the revision details on the new order header based on the original
     * order
     * 
     * @param newOrderHeader
     *            the order header for the new order
     * @param originalOrderHeader
     *            the order header for the original order
     */
    private void setRevisionDetails(OrderHeader newOrderHeader, final OrderHeader originalOrderHeader) {
        log.debug("START - setRevisionDetails");
        log.debug("newOrderHeader case no"
                + ((null == newOrderHeader.getCaseNumber()) ? "NULL CASE NUMBER DETAILS" : newOrderHeader
                        .getCaseNumber().toString()));
        log.debug("newOrderHeader "
                + ((null == newOrderHeader.getRevisionDetails()) ? "NULL REVISION DETAILS" : newOrderHeader
                        .getRevisionDetails().toString()));
        int revision = (null == originalOrderHeader.getRevisionDetails()) ? 1 : originalOrderHeader
                .getRevisionDetails().getRevisionNo() + 1;
        newOrderHeader.getRevisionDetails().setRevisedOrder(true);
        newOrderHeader.getRevisionDetails().setPreviousOrderDate(originalOrderHeader.getSignedDate());
        newOrderHeader.getRevisionDetails().setRevisionNo(revision);
        setOriginalOrderTypeCode(originalOrderHeader.getOrderType());
        log.debug("NewOrder revision no.: " + revision);
        log.debug("END - setRevisionDetails");
    }

    /**
     * <p>
     * Title: Class that provides functionality to simplify order validation
     * behaviour.
     * </p>
     * <p>
     * Description:
     * </p>
     * <p/> This class encapsulates the behaviour required wrt order validation
     * behaviour.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Bob Boothby
     * @version 1.0
     */
    public final class ValidationHelper {
        private ArrayList errors = new ArrayList();

        private ArrayList warnings = new ArrayList();

        private int validationLevel = 0;

        private Order toBeValidated;

        private OrderData defaultOrder;

        /**
         * Private constructor so that can only be instantiated in appropriate
         * class.
         * 
         * @param toBeValidated
         *            The Order toBeValidated.
         * @param defaultOrder
         *            The deafult order.
         * @param validationLevel
         *            The validation level.
         */
        private ValidationHelper(Order toBeValidated, OrderData defaultOrder, int validationLevel) {
            this.toBeValidated = toBeValidated;
            this.defaultOrder = defaultOrder;
            this.validationLevel = validationLevel;
        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param logicalProblem
         *            The logical problem.
         */
        public void addLogicalProblem(OrderValidationProblem logicalProblem) {
            if ((validationLevel & LOGICAL_VALIDATION) == LOGICAL_VALIDATION)
                errors.add(logicalProblem);
            else
                warnings.add(logicalProblem);

        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param messageCode
         *            The message code for the problem.
         * @param messageFields
         *            The fields with which to populate the message.
         */
        public void addLogicalProblem(String messageCode, Serializable[] messageFields) {
            addLogicalProblem(new OrderValidationProblem(messageCode, messageFields));
        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param logicalProblem
         *            The logical problem.
         */
        public void addCompletenessProblem(OrderValidationProblem logicalProblem) {
            if ((validationLevel & COMPLETENESS_VALIDATION) == COMPLETENESS_VALIDATION)
                errors.add(logicalProblem);
            else
                warnings.add(logicalProblem);

        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param messageCode
         *            The message code for the problem.
         * @param messageFields
         *            The fields with which to populate the message.
         */
        public void addCompletenessProblem(String messageCode, Serializable[] messageFields) {
            addCompletenessProblem(new OrderValidationProblem(messageCode, messageFields));
        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param logicalProblem
         *            The logical problem.
         */
        public void addDatabaseProblem(OrderValidationProblem databaseProblem) {
            if ((validationLevel & DATABASE_VALIDATION) == DATABASE_VALIDATION)
                errors.add(databaseProblem);
            else
                warnings.add(databaseProblem);

        }

        /**
         * Add a problem in the logic of the order. Will populate errors or
         * warnings according to the established validation level.
         * 
         * @param messageCode
         *            The message code for the problem.
         * @param messageFields
         *            The fields with which to populate the message.
         */
        public void addDatabaseProblem(String messageCode, Serializable[] messageFields) {
            addDatabaseProblem(new OrderValidationProblem(messageCode, messageFields));
        }

        /**
         * Gets the Castor bound order XML to be validated.
         * 
         * @return The order as castor objects.
         */
        public Order getOrderToBeValidated() {
            return toBeValidated;
        }

        /**
         * Gets the default order.
         * 
         * @return
         */
        public OrderData getDefaultOrder() {
            return this.defaultOrder;
        }

        /**
         * This method checks whether there has been a problem with the
         * validation and if so throws a populated exception.
         * 
         * @throws OrderValidationException
         *             containing any problems in the validation.
         */
        private void checkValidationResult() throws OrderValidationException {
            if (errors.size() > 0 || warnings.size() > 0) {
                OrderValidationProblem[] errorArray = new OrderValidationProblem[errors.size()];
                errors.toArray(errorArray);
                OrderValidationProblem[] warningArray = new OrderValidationProblem[warnings.size()];
                warnings.toArray(warningArray);
                throw new OrderValidationException(errorArray, warningArray, "order.validation.general.error",
                        "There were problems in validation");
            }
        }
    }
    
    private static class MarshalSafeString {

    	private static final Character[] UNSAFE_CHARS = new Character[] {'|','Â'};
    	private static final Character[] SAFE_CHARS = new Character[]  {' ',' '};
    	
    	/**
    	 * Convert chars that cause the unmarshal to crash into safe text.
    	 */
		public static String getSafeText(String text) {
			String result = text;
			for (int chrNo = 0; chrNo < UNSAFE_CHARS.length; chrNo++) {
				result = replaceSubstring(result, Character.toString(UNSAFE_CHARS[chrNo]), Character.toString(SAFE_CHARS[chrNo]));
			}
			return result;
		}
		
   	
		/**
		 * Replace the occurrences of the passed in pattern with the selected string. 
		 */
		 private static String replaceSubstring(String str, String pattern, String replace) {
	        int slen = str.length();
	        int plen = pattern.length();
	        int s = 0, e = 0;

	        StringBuffer result = new StringBuffer(slen * 2);
	        char[] chars = new char[slen];

	        while ((e = str.indexOf(pattern, s)) >= 0) {
	            if (e == s) {
	            	result.append(replace);
	            } else {
		            str.getChars(s, e, chars, 0);
		            result.append(chars, 0, e - s).append(replace);
	            }
	            s = e + plen;
	        }

	        str.getChars(s, slen, chars, 0);
	        result.append(chars, 0, slen - s);
	        return result.toString();
	    }
    }

}
