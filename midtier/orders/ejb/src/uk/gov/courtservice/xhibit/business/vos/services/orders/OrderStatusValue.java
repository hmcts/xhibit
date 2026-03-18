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

public class OrderStatusValue extends uk.gov.courtservice.framework.business.vos.CSAbstractValue {
    private java.lang.Integer orderStatusId;

    private java.lang.String code;

    private java.lang.Integer version;
    
    private static final long serialVersionUID =4037407468821992211L;

    /**
     * Default no argument constructor, providing serialisation support.
     */
    public OrderStatusValue() {
    }

    /**
     * More complete constructor for convenience of creation.
     * 
     * @param code
     * @param orderStatusId
     * @param version
     */
    public OrderStatusValue(String code, Integer orderStatusId, Integer version) {
        this.code = code;
        this.orderStatusId = orderStatusId;
        this.version = version;
    }

    /**
     * The code returned is intended to be passed through an
     * internationalisation step before display.
     * 
     * @return The code for this order status.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The primary key identifier for this order status.
     */
    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    /**
     * @return The version number for this particular order status, used for
     *         optimistic locking.
     */
    public Integer getVersion() {
        return version;
    }
}