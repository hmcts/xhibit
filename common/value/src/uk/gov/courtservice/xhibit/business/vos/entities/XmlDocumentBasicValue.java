package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: XmlDocumentBasicValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Laurent Bossard
 * @version 1.0
 */

public class XmlDocumentBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -7881079862319305300L;
    private Integer xmlDocumentId;

    private Date dateCreated;

    private Date creationDate;

    private String documentTitle;

    private String xmlDocument;

    private String status;

    private Date expiryDate;

    private String documentType;

    private Integer courtId;

    public XmlDocumentBasicValue() {
        super();
    }

    public XmlDocumentBasicValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * @return Integer
     */
    public Integer getXmlDocumentId() {
        return xmlDocumentId;
    }

    /**
     * @return Date
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @return String
     */
    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * @return String
     */
    public String getXmlDocument() {
        return xmlDocument;
    }

    /**
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return Date
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @return String
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @return Integer
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * @param Integer
     */
    public void setXmlDocumentId(Integer xmlDocumentId) {
        this.xmlDocumentId = xmlDocumentId;
    }

    /**
     * @param Date
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @param String
     */
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     * @param String
     */
    public void setXmlDocument(String xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    /**
     * @param String
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @param Date
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @param String
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * @param Integer
     */
    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}