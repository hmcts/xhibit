package uk.gov.courtservice.xhibit.client.order.delegate;

import uk.gov.courtservice.framework.client.delegate.CSBusinessDelegate;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;

/**
 * <p>
 * Title: OrdersControllerBusinessDelegate
 * </p>
 * <p>
 * Description: All Orders client requests should be issued via this interface
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public interface OrdersControllerBusinessDelegate extends CSBusinessDelegate {
    /**
     * Creates an instance of an order (including the order XML population from
     * the database), however it does not save this instance to the database -
     * the order created in transitory unless saved.
     * 
     * @param defendantOnCaseId
     *            Key identifying the defendant on a case for which this order
     *            is to be created
     * @param orderType
     * @param disposals
     * @param forceCreate
     * @return Newly created order value object - not saved to system yet.
     * @throws OrderXMLException
     * @throws OrderNotSupportedException
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue createOrder(Integer defendantOnCaseId, Integer orderType, Integer[] disposals,
            boolean forceCreate) throws OrderXMLException, OrderNotSupportedException, OrderException,
            CSUnrecoverableException;

    /**
     * Replaces an existing order with a new one.Creates an instance of an order
     * (including the order XML population from the database), however it does
     * not save this instance to the database - the order created in transitory
     * unless saved.
     * 
     * @param defendantOnCaseId
     *            Key identifying the defendant on a case for which this order
     *            is to be created
     * @param orderType
     * @param disposals
     * @param forceCreate
     * @param originalOrderId
     * @return Newly created order value object - not saved to system yet.
     * @throws OrderXMLException
     * @throws OrderNotSupportedException
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue replaceOrder(Integer defendantOnCaseId, Integer orderType, Integer[] disposals,
            Integer originalOrderId) throws OrderXMLException, OrderNotSupportedException, OrderException,
            CSUnrecoverableException;

    /**
     * Returns a single order identified by the primary key parameter.
     * 
     * @param orderId
     *            The primary key identifier for the order.
     * @return The details of the requested order.
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue getOrder(Integer orderId) throws OrderException, CSUnrecoverableException;

    /**
     * This method returns the appropriate order types for a defendant on a case
     * given their current disposals.
     * 
     * @param defendantOnCaseId
     * @return
     * @throws CSUnrecoverableException
     */
    public XhbOrderTemplateValue[] getOrderTemplates(Integer defendantOnCaseId) throws CSUnrecoverableException;

    /**
     * This methods returns a complete list of the types of orders. If templated
     * types is true, then only the order types for which a template exists is
     * returned, if templated types is false, then all types recorded in the
     * system are returned.
     * 
     * @param templatedTypes
     * @return
     * @throws CSUnrecoverableException
     */
    public XhbOrderTypeValue[] getOrderTypes() throws CSUnrecoverableException;

    /**
     * This method returns an array of all existing orders for the given
     * defendant on case id.
     * 
     * @param defendantOnCase
     * @return
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue[] getReplaceableOrdersForDefendantOnCaseAndOrderType(Integer defendantOnCaseId,
            Integer orderType) throws CSUnrecoverableException;

    /**
     * This method returns an array of all existing orders for the given
     * defendant on case id.
     * 
     * @param defendantOnCase
     * @param status
     * @return
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue[] getReplaceableOrdersForDefendantOnCaseAndOrderTypeAndStatus(Integer defendantOnCaseId,
            Integer orderType, Integer status) throws CSUnrecoverableException;

    /**
     * This method returns an array of all existing orders for the given
     * defendant on case id.
     * 
     * @param defendantOnCase
     * @return
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue[] getOrdersForDefendantOnCase(Integer defendantOnCase) throws CSUnrecoverableException;

    /**
     * Retrieves an array of template definitions, containing only current,
     * non-obsolete templates.
     * 
     * @return An array of type OrderTemplateValue
     * @throws CSUnrecoverableException
     */
    public XhbOrderTemplateValue[] getValidTemplates() throws CSUnrecoverableException;

    /**
     * Saves the order with an order status of 'PRINTED'.
     * 
     * @param order
     *            The order to be saved as printed.
     * @return A 'new' order value object reflecting the new status. throws
     *         OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue printOrder(XhbOrderValue order) throws OrderException, OrderXMLException,
            CSUnrecoverableException;

    /**
     * Saves the order with an order status of 'SAVED'.
     * 
     * @param order
     *            The order to be saved.
     * @return A 'new' order value object reflecting the new status.
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue saveOrder(XhbOrderValue order) throws OrderException, OrderXMLException,
            CSUnrecoverableException;

    /**
     * Saves the order with an order status of 'SIGNED', while checking that the
     * order is currently at 'PRINTED' status and that signing information is
     * present. At the same time it marks the order ready for delivery by
     * mercator.
     * 
     * @param order
     *            The order to be saved as signed.
     * @return A 'new' order value object reflecting the new status.
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue signOrder(XhbOrderValue order) throws OrderException, OrderXMLException,
            CSUnrecoverableException;
    
    /**
     * For orders which are to be sent, currently only monetary orders.
     *  
     * @param order
     *            The order to be saved as sent.
     * @return A 'new' order value object reflecting the new status.
     * @throws OrderException
     * @throws CSUnrecoverableException
     */
    public XhbOrderValue sendOrder(XhbOrderValue order) throws OrderException, OrderXMLException,
            CSUnrecoverableException;

}