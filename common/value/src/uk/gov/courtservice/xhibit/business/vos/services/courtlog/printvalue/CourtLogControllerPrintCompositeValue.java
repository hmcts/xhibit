package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: CourtLogChargesPrintValue
 * </p>
 * <p>
 * Description: This creates a common base class for the objects which hold
 * court log view values for printing court log events.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell (xDevelopement 2003)
 * @version 1.0
 */
public abstract class CourtLogControllerPrintCompositeValue extends CSAbstractValue implements Comparable {

    /**
     * This is actually a collection of CourtLogViewValue objects but has had to
     * be given the attribute name chargeLogItems in order to re-use the XSL for
     * printing court log events
     */
    private Collection chargeLogItems;

    /**
     * This is used to determine in the xsl which type of composite value is
     * being used
     */
    private String type;

    /**
     * The date of this collection this is used for sorting
     */
    private Date date;
    
    private static final long serialVersionUID = 5843740714477002979L;

    /**
     * Construct a blank CourtLogChargePrintValue
     * 
     * @param the
     *            type
     */
    public CourtLogControllerPrintCompositeValue(String type, Date date) {
        setType(type);
        setDate(date);
    }

    /**
     * Add the charge log item (court log entry) to the collection of items
     * 
     * @param value
     *            the court log entry to add
     */
    public void addChargeLogItems(Collection chargeLogItemCollection) {
        if (chargeLogItems == null) {
            chargeLogItems = new ArrayList();
        }
        chargeLogItems.addAll(chargeLogItemCollection);
    }

    /**
     * Add the charge log item (court log entry) to the collection of items
     * 
     * @param value
     *            the court log entry to add
     */
    public void addChargeLogItem(CourtLogViewValue chargeLogItem) {
        if (chargeLogItems == null) {
            chargeLogItems = new ArrayList();
        }
        chargeLogItems.add(chargeLogItem);
    }

    /**
     * Get the charge log items (court log view)
     * 
     * @return the charge log items
     */
    public Collection getChargeLogItems() {
        return chargeLogItems;
    }

    /**
     * Get the value type (used by xsl to determine what to print)
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the value date (used by xsl to determine what to print)
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the charge log items (court log view)
     * 
     * @param the
     *            charge log items
     */
    public void setChargeLogItems(Collection param) {
        chargeLogItems = param;
    }

    /**
     * Set the type
     * 
     * @param type
     *            the new value type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the date
     * 
     * @param date
     *            the new value date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Compares this object with the specified object for order on date
     * returning 0 does not mean the objects are equal mearly that they can not
     * be sorted.
     * 
     * @param o
     *            the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * 
     * @throws ClassCastException
     *             if the specified object's type prevents it from being
     *             compared to this Object.
     */
    public int compareTo(Object object) {
        if (object == null) {
            return -1; // put null values at end
        } else {
            CourtLogControllerPrintCompositeValue value = (CourtLogControllerPrintCompositeValue) object;
            if (value.date == null) {
                if (date == null) {
                    return 0;
                } else {
                    return -1; // put values with null dates at end
                }
            } else {
                if (date == null) {
                    return 1; // put values with null dates at end
                } else {
                    return date.compareTo(value.date);
                }
            }
        }
    }

}
