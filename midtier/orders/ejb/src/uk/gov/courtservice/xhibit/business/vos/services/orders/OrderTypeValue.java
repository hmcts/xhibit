package uk.gov.courtservice.xhibit.business.vos.services.orders;

/**
 * <p>
 * Title: Client-facing value object representing an order type.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class OrderTypeValue extends uk.gov.courtservice.framework.business.vos.CSAbstractValue {
    private java.lang.Integer orderTypeId;

    private java.lang.String code;

    private java.lang.Integer version;
    private static final long serialVersionUID =-9050566555662159787L;

    /**
     * Default no argument constructor, providing serialisation support.
     */
    public OrderTypeValue() {
    }

    /**
     * More complete constructor for convenience of creation.
     * 
     * @param code
     * @param orderTypeId
     * @param version
     */
    public OrderTypeValue(String code, Integer orderTypeId, Integer version) {
        this.code = code;
        this.orderTypeId = orderTypeId;
        this.version = version;
    }

    /**
     * The code returned is intended to be passed through an
     * internationalisation step before display.
     * 
     * @return The code for this order type.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The primary key identifier for this order type.
     */
    public Integer getOrderTypeId() {
        return orderTypeId;
    }

    /**
     * @return The version number for this particular order type, used for
     *         optimistic locking.
     */
    public Integer getVersion() {
        return version;
    }
}