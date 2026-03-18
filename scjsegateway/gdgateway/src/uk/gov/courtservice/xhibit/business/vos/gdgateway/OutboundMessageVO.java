package uk.gov.courtservice.xhibit.business.vos.gdgateway;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: Value Object for the outbound message records.
 * </p>
 * <p>
 * Description: Value Object for the outbound message records that get stored in
 * GDG_OUTBOUND_MESSAGES and GDG_OUTBOUND_CLOBS tables
 * (there is a 1:1 mandatory relationship between these two tables so
 * they can be represented by 1 VO)
 * FOR REVIEW: combine with the other VOS with the common props in a superclass?
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundMessageVO.java,v 1.1 2006/09/04 13:41:04 qz4rwx Exp $
 */
public class OutboundMessageVO implements Serializable {

    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The unique request id of the outbound message
     * This will be the same as the Item_Id on EXI_Item_Outbound table
     */
    private Long requestId;

    /**
     * Identifier of the outbound source system, this will be C00CourtServicesHub
     */
    private String sourceIdentifier;

    /**
     * Identifier of the outbound destination system, this will be Z00CJSE
     */
    private String destinationIdentifier;

    /**
     * The web service invocation mode - this will be ASYNC
     */
    private String execMode;

    /**
     * The date/time of the request.
     * This will be set to the current time on creation and will be updated
     * to the current time if the outboundStatusId is updated to ERROR
     */
    private Date requestTimestamp;

    /**
     * The current status of the outbound message
     * This will be set to the appropriate code for NEW on creation
     * and to the appropriate code for SUCCESS, ERROR or FATAL depending on the
     * outcome of attempting to send the message
     */
    private Long outboundStatusId;

    /**
     * The number of times an outbound message has been attempted to be sent
     * This will be set to 1 on creation
     * FOR REVIEW: Will this ever be required outside of a stored proc - probably not!!
     */
    private Long sendAttempts;

    /**
     * The text of the message.
     */
    private String clobData;

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
     * @return the destinationIdentifer
     */
    public String getDestinationIdentifier() {
        return destinationIdentifier;
    }

    /**
     * @param destinationIdentifer
     */
    public void setDestinationIdentifier(String destinationIdentifier) {
        this.destinationIdentifier = destinationIdentifier;
    }

    /**
     * @return the execMode
     */
    public String getExecMode() {
        return execMode;
    }

    /**
     * @param execMode
     */
    public void setExecMode(String execMode) {
        this.execMode = execMode;
    }

    /**
     * @return the sourceIdentifier
     */
    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    /**
     * @param sourceIdentifier
     */
    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    /**
     * @return the requestTimestamp
     */
    public Date getRequestTimestamp() {
        return requestTimestamp;
    }

    /**
     * @param requestTimestamp
     */
    public void setRequestTimestamp(Date requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    /**
     * @return the outboundStatusId
     */
    public Long getOutboundStatusId() {
        return outboundStatusId;
    }

    /**
     * @param outboundStatusId
     */
    public void setOutboundStatusId(Long outboundStatusId) {
        this.outboundStatusId = outboundStatusId;
    }

    /**
     * @return the sendAttempts
     */
    public Long getSendAttempts() {
        return sendAttempts;
    }

    /**
     * @param sendAttempts
     */
    public void setSendAttempts(Long sendAttempts) {
        this.sendAttempts = sendAttempts;
    }

    /**
     * @return the clobData
     */
    public String getClobData() {
        return clobData;
    }

    /**
     * @param clobData
     */
    public void setClobData(String clobData) {
        this.clobData = clobData;
    }

    /**
     * @return a string representation of the object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OutboundMessage[requestId=");
        builder.append(requestId);
        builder.append(",sourceIdentifier=");
        builder.append(sourceIdentifier);
        builder.append(",destinationIdentifier=");
        builder.append(destinationIdentifier);
        builder.append(",execMode=");
        builder.append(execMode);
        builder.append(",requestTimestamp=");
        builder.append(requestTimestamp);
        builder.append(",outboundStatusId=");
        builder.append(outboundStatusId);
        builder.append(",sendAttempts=");
        builder.append(sendAttempts);
        builder.append(",clobData=");
        builder.append(clobData);
        builder.append("]");
        return builder.toString();
    }
}
