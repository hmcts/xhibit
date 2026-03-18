package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DocumentDistributionBasicValue
 * </p>
 * <p>
 * Description: DocumentDistributionBasicValue value object.
 * DocumentDistribution entity holds the details of a list recipients subscribed
 * choices (e.g. Daily List, Warned List)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Brett Williams, Laurent Bossard
 * @version $Id: DocumentDistributionBasicValue.java,v 1.1 2004/03/30 10:12:44
 *          qzd3k3 Exp $
 */

public class DocumentDistributionBasicValue extends CSAbstractValue {
    private String distributionType;

    private String documentType;

    private String mimeType;

    private Integer courtId;

    private Integer recipientID;

    private Integer wllRecipientID;

    private Integer documentDistributionId;

    // added to support preferred distribution types
    private String usePrefDistType;
    
    private static final long serialVersionUID = 1252958686477161947L;

    /**
     * Default Constructors
     */
    public DocumentDistributionBasicValue() {
    }

    public DocumentDistributionBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * @return Integer
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * @return Integer
     */
    public Integer getDocumentDistributionId() {
        return documentDistributionId;
    }

    /**
     * @return String
     */
    public String getDistributionType() {
        return distributionType;
    }

    /**
     * @return String
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @return String
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @return Integer
     */
    public Integer getRecipientID() {
        return recipientID;
    }

    /**
     * @return Integer
     */
    public Integer getWllRecipientID() {
        return wllRecipientID;
    }

    /**
     * @return String
     */
    public String getUsePrefDistType() {
        return usePrefDistType;
    }

    /**
     * @param String
     *            distributionType
     */
    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    /**
     * @param String
     *            documentType
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @param String
     *            mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * @param Integer
     *            recipientID
     */
    public void setRecipientID(Integer recipientID) {
        this.recipientID = recipientID;
    }

    /**
     * @param Integer
     *            wllRecipientID
     */
    public void setWllRecipientID(Integer wllRecipientID) {
        this.wllRecipientID = wllRecipientID;
    }

    /**
     * @param Integer
     *            courtId
     */
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    /**
     * @return Integer
     */
    public void setDocumentDistributionId(Integer documentDistributionId) {
        this.documentDistributionId = documentDistributionId;
    }

    /**
     * @param String
     *            usePrefDistType
     */
    public void setUsePrefDistType(String usePrefDistType) {
        this.usePrefDistType = usePrefDistType;
    }
}