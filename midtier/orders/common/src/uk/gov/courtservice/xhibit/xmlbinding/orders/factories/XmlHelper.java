package uk.gov.courtservice.xhibit.xmlbinding.orders.factories;

import java.util.ArrayList;
import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderValidationException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;

/**
 * <p>
 * Title: Abstract super class providing utility methods and method signatures
 * for an XmlHelper.
 * </p>
 * <p>
 * Description: An XmlHelper is a class that populates the initial XML required
 * by an order - in effect creating a snapshot of the order data. There should
 * be a lot of classes held in common between different order helpers, not least
 * among which are the generated classes for populating the XML through strongly
 * typed java methods. Additionally I would expect sub-helpers held in common,
 * used to populate common elements.
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
public abstract class XmlHelper {
    /**
     * Basic validation, will always occur, but this constant provided as a
     * convenience for when no other validation is required. The basic
     * validation encompasses only validation of the underlying xml against it's
     * schema.
     */
    public static final int BASIC_VALIDATION = 0;

    /**
     * Defines level of validation where the fields should be checked for
     * logical consistency. If this level of validation is used in isolation,
     * then the code should be forgiving of missing fields. If selected, logical
     * validation will raise errors instead of warnings.
     */
    public static final int LOGICAL_VALIDATION = 1;

    /**
     * Defines validation that mandatory fields are completed. If selected,
     * completeness validation will raise errors instead of warnings.
     */
    public static final int COMPLETENESS_VALIDATION = 2;

    /**
     * Defines validation that the XML content is checked against the
     * appropriate database fields. Should be forgiving of missing fields. If
     * selected, database validation will raise errors instead of warnings.
     */
    public static final int DATABASE_VALIDATION = 4;

    /**
     * The order type
     */
    protected final String typeCode;

    /**
     * <p>
     * This constructor creates the helper, retrieving a copy of the empty
     * schema for the particular order type.
     * </p>
     * <p>
     * The constructor signature should always be repeated in the constructors
     * of children to allow the XmlHelperFactoryImpl to successfully create
     * their instances.
     * </p>
     * 
     * @param typeCode
     *            the type code to create the helper for.
     */
    public XmlHelper(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * 
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public abstract String getOrderXML(Integer caseId, Integer xhibitCaseId, String caseTitle, Integer courtId, boolean isBCase) throws OrderXMLException;
    
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
    public abstract String getOrderXML(Integer defendantOnCaseID, String monetaryDisposalInfoForDisplayOnOrder, String monetaryTotalsForDisplayOrder) throws OrderXMLException;
    
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
    public abstract String getOrderXML(Integer defendantOnCaseID) throws OrderXMLException;
    
    /**
     * The xml returned here contains the empty schema for the order.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public abstract String getOrderXML() throws OrderXMLException;

    /**
     * The xml returned here contains the relevant and available data for the
     * order.
     * 
     * @param defendantOnCaseID
     *            the identifier for the defendant on the case.
     * @param originalXML
     *            the xml from an order that this new order is replacing
     * @param originalTypeId
     *            the type of the original order
     * @return A string containing the initial xml data for the order.
     * @throws OrderXMLException
     *             When there is a problem within the helper.
     */
    public abstract String getOrderXML(Integer defendantOnCaseID, String originalXML, Integer originalTypeId)
            throws OrderXMLException;

    /**
     * This method is used to validate the XML to various degrees of validation.
     * All levels of validation are always applied, but the validation level
     * determines whether they generate errors or only warnings.
     * <p>
     * To select multiple levels of validation, <b>add</b> the different
     * validation levels together for the call. For Example:
     * <p>
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
     * @return the validated xml
     * @throws OrderXMLValidationException
     *             When there is a problem in the validation.
     */
    public abstract String validateOrderXML(Integer defendantOnCaseID, String orderXML, int validationLevel)
            throws OrderValidationException, OrderXMLException;

}
