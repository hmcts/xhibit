package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: DocumentValue
 * </p>
 * <p>
 * Description: Value object to store document data about the daily list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class DocumentValue extends CSAbstractValue {

    private String documentName;

    private String uniqueID;

    private String documentType;
    private static final long serialVersionUID =-7818484618445954268L;

    public DocumentValue() {
    }

    public Integer getHearingListId() {
        return getId();
    }

    public void setHearingListId(Integer hearingListId) {
        setId(hearingListId);
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

}