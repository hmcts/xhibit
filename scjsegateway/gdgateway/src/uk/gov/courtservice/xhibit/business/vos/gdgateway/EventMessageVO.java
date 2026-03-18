package uk.gov.courtservice.xhibit.business.vos.gdgateway;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: Value object to hold the Event Message information.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @version $Id: EventMessageVO.java,v 1.3 2006/10/17 12:00:02 szfnvt Exp $
 */
public class EventMessageVO implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Long messageId;
    private String messageType;
    private Date expiryTime;
    
    /**
     * Argument Constructor
     * @param messageId
     * @param messageType
     * @param expiryTime
     */
    public EventMessageVO(Long messageId, String messageType, Date expiryTime) {
        setMessageId(messageId);
        setMessageType(messageType);
        setExpiryTime(expiryTime);
    }
    
    /**
     * No Argument Constructor
     *
     */
    public EventMessageVO() {
    }
    
    /**
     * Retrieve MessageId
     * @return
     */
    public Long getMessageId() {
        return messageId;
    }
    /**
     * Set MessageId
     * @param messageId
     */
    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
    /**
     * Retrieve ExpiryTime
     * @return
     */
    public Date getExpiryTime() {
        return expiryTime;
    }
    /**
     * Set Expiry Time
     * @param expiryTime
     */
    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
    /**
     * Retrieve MessageType
     * @return
     */
    public String getMessageType() {
        return messageType;
    }
    /**
     * Set MessageType
     * @param messageType
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventMessageVO[messageId=");
        builder.append(getMessageId());
        builder.append(",messageType=");
        builder.append(getMessageType());
        builder.append(",expiryTime=");
        builder.append(getExpiryTime());
        builder.append("]");
        return builder.toString();
    }
}
