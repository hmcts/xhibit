package uk.gov.courtservice.xhibit.business.vos.gdgateway;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: Value Object for the inbound message.
 * </p>
 * <p>
 * Description: Value Object for the inbound messages that get stored in
 * GDG_INBOUND_MESSAGES.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Id: InboundMessageVO.java,v 1.2 2006/08/24 08:29:00 rzvddy Exp $
 */
public class InboundMessageVO implements Serializable {

    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The unique id of the inbound message.
     */
    private Long inboundMessageId;

    /**
     * The unique request id for the associated source system.
     */
    private String requestIdentifier;

    /**
     * Identifier of the source system
     */
    private String sourceIdentifier;

    /**
     * Identifier of the destination systems
     */
    private String destinationIdentifer;

    /**
     * The web service invocation mode - synchronous, asynchronous and routing
     * only.
     */
    private String execMode;

    /**
     * The date/time of the request.
     */
    private Date requestTimeStamp;

    /**
     * The text of the message.
     */
    private String clobData;

    /**
     * @return the inboundMessageId
     */
    public Long getInboundMessageId() {
        return inboundMessageId;
    }

    /**
     * @param inboundMessageId
     *            the inboundMessageId to set
     */
    public void setInboundMessageId(Long inboundMessageId) {
        this.inboundMessageId = inboundMessageId;
    }

    /**
     * @return the destinationIdentifer
     */
    public String getDestinationIdentifer() {
        return destinationIdentifer;
    }

    /**
     * @param destinationIdentifer
     *            the destinationIdentifer to set
     */
    public void setDestinationIdentifer(String destinationIdentifer) {
        this.destinationIdentifer = destinationIdentifer;
    }

    /**
     * @return the execMode
     */
    public String getExecMode() {
        return execMode;
    }

    /**
     * @param execMode
     *            the execMode to set
     */
    public void setExecMode(String execMode) {
        this.execMode = execMode;
    }

    /**
     * @return the requestIdentifier
     */
    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    /**
     * @param requestIdentifier
     *            the requestIdentifier to set
     */
    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    /**
     * @return the sourceIdentifier
     */
    public String getSourceIdentifier() {
        return sourceIdentifier;
    }

    /**
     * @param sourceIdentifier
     *            the sourceIdentifier to set
     */
    public void setSourceIdentifier(String sourceIdentifier) {
        this.sourceIdentifier = sourceIdentifier;
    }

    /**
     * @return the requestTimeStamp
     */
    public Date getRequestTimeStamp() {
        return requestTimeStamp;
    }

    /**
     * @param requestTimeStamp
     *            the requestTimeStamp to set
     */
    public void setRequestTimeStamp(Date requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    /**
     * @return the clobData
     */
    public String getClobData() {
        return clobData;
    }

    /**
     * @param clobData
     *            the clobData to set
     */
    public void setClobData(String clobData) {
        this.clobData = clobData;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InboundMessage[inboundMessageId=");
        builder.append(inboundMessageId);
        builder.append(",requestIdentifier=");
        builder.append(requestIdentifier);
        builder.append(",sourceIdentifier=");
        builder.append(sourceIdentifier);
        builder.append(",destinationIdentifer=");
        builder.append(destinationIdentifer);
        builder.append(",execMode=");
        builder.append(execMode);
        builder.append(",requestTimeStamp=");
        builder.append(requestTimeStamp);
        builder.append(",clobData=");
        builder.append(clobData);
        builder.append("]");
        return builder.toString();
    }
}
