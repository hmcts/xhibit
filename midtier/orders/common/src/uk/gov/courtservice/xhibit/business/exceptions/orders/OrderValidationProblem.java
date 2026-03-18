package uk.gov.courtservice.xhibit.business.exceptions.orders;

/**
 * <p>
 * Title: A single instance of a problem in validating an order.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This class encapsulates a single problem encountered by an order, it is
 * intended to be used as part of OrderValidationException and in conjunction
 * with ResourceBundles.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class OrderValidationProblem implements java.io.Serializable {
	
	static final long serialVersionUID = -1550745260312629462L;
	
    private String messageCode;

    private java.io.Serializable[] messageFields;

    public OrderValidationProblem(String messageCode, java.io.Serializable[] messageFields) {
        this(messageCode);
        this.messageFields = messageFields;
    }

    public OrderValidationProblem(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public java.io.Serializable[] getMessageFields() {
        return messageFields;
    }
}