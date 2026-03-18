package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: WLLRecipientBasicValue
 * </p>
 * <p>
 * Description: Warned List Letter Recipient Value Object. Maps one to one with
 * WLLRecipient entity.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Brett Williams, Laurent Bossard
 * @version $Id: RecipientBasicValue.java,v 1.3 2006/06/05 12:29:15 bzjrnl Exp $
 */

public class RecipientBasicValue extends CSAbstractValue {
    private String recipientName;

    private String faxNumber;

    private String emailAddress;

    private Integer courtID;

    private Integer recipientId;

    // added to support preferred distribution types
    private String prefDistributionType;

    private String prefMimeType;
    private static final long serialVersionUID =6090262048517568152L;
    
    
    /**
     * Default Constructors
     */
    public RecipientBasicValue() {
    }

    public RecipientBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * @return Integer
     */
    public Integer getRecipientId() {
        return recipientId;
    }

    /**
     * @return String
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * @return String
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * @return String
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @return Integer
     */
    public Integer getCourtID() {
        return courtID;
    }

    /**
     * @return String
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    /**
     * @param String
     *            faxNumber
     */
    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * @param String
     *            emailAddress
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @param Integer
     *            courtID
     */
    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    /**
     * @param Integer
     */
    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getPrefDistributionType() {
        return prefDistributionType;
    }

    public String getPrefMimeType() {
        return prefMimeType;
    }

    public void setPrefDistributionType(String prefDistributionType) {
        this.prefDistributionType = prefDistributionType;
    }

    public void setPrefMimeType(String prefMimeType) {
        this.prefMimeType = prefMimeType;
    }

}