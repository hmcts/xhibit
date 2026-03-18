package uk.gov.courtservice.xhibit.client.misc;

/**
 * <p>
 * Title: OrderStatus
 * </p>
 * <p>
 * Description: Order Status class
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */
public class OrderStatus extends AbstractOrderStatus {

    // int status with range 0-4.
    private int status;

    // true for newly created orders.
    private boolean isNew;

    // true for all saved orders.
    private boolean saved;

    // true for all printed orders.
    private boolean printed;

    // true for all signed orders.
    private boolean signed;
    
    // true for all sent orders.
    private boolean sent;

    /**
     * Status for a NEW order
     */
    public static final int NEW = 1;

    /**
     * Status for a SAVED order
     */
    public static final int SAVED = 2;

    /**
     * Status for a PRINTED order
     */
    public static final int PRINTED = 3;

    /**
     * Status for a SIGNED order
     */
    public static final int SIGNED = 4;

    /**
     * Status for a AMENDED order
     */
    public static final int AMENDED = 5;

    /**
     * Status for a SENT order
     */
    public static final int SENT = 6;
    
    /**
     * default constructor initialises status to 0 and isNew to true.
     */
    public OrderStatus() {
        this.isNew = true;
        this.status = 0;
    }

    /**
     * Sets integer status value. Value must satisfy the flow of an order
     * correctly.
     * 
     * @param i
     *            Integer.
     */
    public void setStatus(int i) {
        if (status != 4) {

            int detectChange = status;

            switch (i) {
            case 0:
            case 1:
                if (isNew) {
                    this.status = NEW;
                    saved = false;
                    printed = false;
                }
                break;
            case 2:
                this.status = SAVED;
                saved = true;
                printed = false;
                signed = false;
                break;
            case 3:
                if (saved) {
                    this.status = PRINTED;
                }
                saved = true;
                printed = true;
                signed = false;
                break;
            case 4:
                this.status = SIGNED;
                saved = true;
                printed = true;
                signed = true;
                break;
            case 5:
                this.status = AMENDED;
                saved = false;
                printed = false;
                signed = false;
                break;
            case 6:
                this.status = SENT;
                saved = true;
                printed = false;
                signed = false;
                sent = true;
                break;
            default:
                break;
            }

            if (detectChange != status) {
                changeSupport.firePropertyChange("status", detectChange, status);
            }

        }

    }

    /**
     * Return if the order is saved
     * 
     * @return true if SAVED
     */
    public boolean isSaved() {
        return this.saved;
    }

    /**
     * Return if the order is SIGNED
     * 
     * @return true if SIGNED
     */
    public boolean isSigned() {
        return this.signed;
    }

    /**
     * Return if Order is PRINTED
     * 
     * @return true if printed
     */
    public boolean isPrinted() {
        return this.printed;
    }
    
    /**
     * Return if Order is SENT
     * 
     * @return true if printed
     */
    public boolean isSent() {
        return this.sent;
    }

    /**
     * Retrun the current status object
     * 
     * @return The status
     */
    public int getStatus() {
        return this.status;
    }
}
