package uk.gov.courtservice.xhibit.business.vos.services.orders;

import uk.gov.courtservice.xhibit.business.vos.services.email.EmailValue;

/** @todo need to sort out defendant on case in the value object - hinges on the
 finalisation of the databse design.*/

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
public class OrderValue extends uk.gov.courtservice.framework.business.vos.CSAbstractValue {
    private java.lang.Integer orderId;

    private java.lang.String dataXml;

    private java.util.Date signingDate;

    private java.lang.String signedBy;
    
    private java.util.Date deliveryDate;

    private java.lang.Integer version;

    private OrderStatusValue orderStatus;

    private OrderDeliveryStatusValue orderDeliveryStatus;

    private OrderTemplateValue orderTemplate;
    
    private EmailValue email;
    
    private static final long serialVersionUID =-944652933502796868L;

    public EmailValue getEmail() {
        return email;
    }

    public void setEmail(EmailValue email) {
        this.email = email;
    }

    /**
     * Default no argument constructor, providing serialisation support.
     */
    public OrderValue() {
    }

    /**
     * @param orderId
     *            The id of the order.
     * @param dataXml
     *            The data xml behind the order.
     * @param signingDate
     *            The date that the order was signed.
     * @param signedBy
     *            Who signed the order.
     * @param deliveryDate
     *            The date that the order was successfully delivered to the
     *            document repository
     * @param version
     *            The version number for this particular order type, used for
     *            optimistic locking.
     * @param obsInd
     *            Obsolescence indicator.
     * @param orderStatus
     *            The status of the order from the point of view of the end
     *            user.
     * @param orderDeliveryStatus
     *            The delivery status of the order from the point of view of
     *            Mercator.
     * @param email
     *            Email details for monetary orders (not sent to Mercator)
     */
    public OrderValue(java.lang.Integer orderId, java.lang.String dataXml, java.util.Date signingDate,
            java.lang.String signedBy, java.util.Date deliveryDate, java.lang.Integer version,
            OrderStatusValue orderStatus, OrderDeliveryStatusValue orderDeliveryStatus, OrderTemplateValue orderTemplate, EmailValue email) {
        this.orderId = orderId;
        this.dataXml = dataXml;
        this.signingDate = signingDate;
        this.signedBy = signedBy;
        this.deliveryDate = deliveryDate;
        this.version = version;
        this.orderStatus = orderStatus;
        this.orderDeliveryStatus = orderDeliveryStatus;
        this.email = email;
    }

    /**
     * @return The XML data representing the order.
     */
    public String getDataXml() {
        return dataXml;
    }

    /**
     * @return The date that the order was 'delivered' to the document store.
     */
    public java.util.Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @return The delivery status of the order.
     */
    public OrderDeliveryStatusValue getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    /**
     * @return The primary key identifying the order
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @return The status of the order from the point of view of a user.
     */
    public OrderStatusValue getOrderStatus() {
        return orderStatus;
    }

    /**
     * @return The template for this particular order.
     */
    public OrderTemplateValue getOrderTemplate() {
        return orderTemplate;
    }

    /**
     * @return Whoever signed the order.
     */
    public String getSignedBy() {
        return signedBy;
    }

    /**
     * @return When the order was signed.
     */
    public java.util.Date getSigningDate() {
        return signingDate;
    }

    /**
     * @return The version number for this particular order type, used for
     *         optimistic locking.
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param dataXml
     *            The XML data representing the order.
     */
    public void setDataXml(String dataXml) {
        this.dataXml = dataXml;
    }

    /**
     * @param orderStatus
     *            The status of the order from the point of view of a user.
     */
    public void setOrderStatus(OrderStatusValue orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @param signedBy
     *            Whoever signed the order.
     */
    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    /**
     * @param signingDate
     *            When the order was signed.
     */
    public void setSigningDate(java.util.Date signingDate) {
        this.signingDate = signingDate;
    }

}