package uk.gov.courtservice.xhibit.business.vos.services.orders;

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

public class OrderDeliveryStatusValue extends uk.gov.courtservice.framework.business.vos.CSAbstractValue {
    private java.lang.Integer orderDeliveryStatusId;

    private java.lang.String code;

    private java.lang.Integer version;
    
    private static final long serialVersionUID =5015439468034134302L;

    /**
     * Default no argument constructor, providing serialisation support.
     */
    public OrderDeliveryStatusValue() {
    }

    /**
     * More complete constructor for convenience of creation.
     * 
     * @param code
     * @param orderDeliveryStatusId
     * @param version
     */
    public OrderDeliveryStatusValue(String code, Integer orderDeliveryStatusId, Integer version) {
        this.code = code;
        this.orderDeliveryStatusId = orderDeliveryStatusId;
        this.version = version;
    }

    /**
     * The code returned is intended to be passed through an
     * internationalisation step before display.
     * 
     * @return The code for this order delivery status.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The primary key identifier for this order delivery status.
     */
    public Integer getOrderDeliveryStatusId() {
        return orderDeliveryStatusId;
    }

    /**
     * @return The version number for this particular order delivery status,
     *         used for optimistic locking.
     */
    public Integer getVersion() {
        return version;
    }
}