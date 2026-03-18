package uk.gov.courtservice.xhibit.business.vos.gdgateway;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: Value Object used when updating the Outbound Status Code and
 * optionally the Outbound Failures records.
 * </p>
 * <p>
 * Description: Value Object for updating the Outbound Status Code on the
 * GDG_OUTBOUND_MESSAGES table and optionally creating a record on
 * the GDG_OUTBOUND_FAILURES table
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundUpdateStatusVO.java,v 1.2 2006/10/03 11:37:31 qz4rwx Exp $
 */
public class OutboundUpdateStatusVO implements Serializable {

    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The internal code of the outbound message status
     * This will be set to SUCCESS, ERROR or FATAL
     * depending on the outcome of attempting to send the message
     */
    private String outboundStatusCode;

    /**
     * The unique id of the outbound failure
     */
    private Long outboundFailureId;

    /**
     * The unique request id of the Outbound Message record
     * The Outbound Failure will reference this as a foreign key
     * when the outboundStatusId is ERROR or FATAL
     */
    private Long requestId;

    /**
     * The failure code eg one from the 200 or 300 series or a negative
     * code where an infrastructure exception has occurred
     */
    private String failureCode;

    /**
     * A description of the failure for the particular failure code
     */
    private String failureText;

    /**
     * The date/time of the failure.
     * This will be set to the current time on creation of the failure record
     */
    private Date failureTimestamp;


    /**
     * @return the outboundStatusCode
     */
    public String getOutboundStatusCode() {
        return outboundStatusCode;
    }

    /**
     * @param outboundStatusCode
     */
    public void setOutboundStatusCode(String outboundStatusCode) {
        this.outboundStatusCode = outboundStatusCode;
    }

    /**
     * @return the outboundFailureId
     */
    public Long getOutboundFailureId() {
        return this.outboundFailureId;
    }

    /**
     * @param outboundFailureId
     */
    public void setOutboundFailureId(Long outboundFailureId) {
        this.outboundFailureId = outboundFailureId;
    }

    /**
     * @return the requestId
     */
    public Long getRequestId() {
        return this.requestId;
    }

    /**
     * @param requestId
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the failureCode
     */
    public String getFailureCode() {
        return failureCode;
    }

    /**
     * @param failureCode
     */
    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    /**
     * @return the failureText
     */
    public String getFailureText() {
        return failureText;
    }

    /**
     * @param failureText
     */
    public void setFailureText(String failureText) {
        this.failureText = failureText;
    }

    /**
     * @return the failureTimestamp
     */
    public Date getFailureTimestamp() {
        return failureTimestamp;
    }

    /**
     * @param failureTimestamp
     */
    public void setFailureTimestamp(Date failureTimestamp) {
        this.failureTimestamp = failureTimestamp;
    }

    /**
     * @return a string representation of the object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OutboundMessage[requestId=");
        builder.append(requestId);
        builder.append(",outboundStatusCode=");
        builder.append(outboundStatusCode);
        builder.append("]");
        builder.append("OutboundFailure[outboundFailureId=");
        builder.append(outboundFailureId);
        builder.append(",requestId=");
        builder.append(requestId);
        builder.append(",failureCode=");
        builder.append(failureCode);
        builder.append(",failureText=");
        builder.append(failureText);
        builder.append(",failureTimestamp=");
        builder.append(failureTimestamp);
        builder.append("]");
        return builder.toString();
    }
}
