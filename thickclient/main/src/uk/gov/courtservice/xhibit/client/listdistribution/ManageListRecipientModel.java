package uk.gov.courtservice.xhibit.client.listdistribution;

// third party

import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: ManageListRecipientModel
 * </p>
 * <p>
 * Description: Data managed by the ManageListRecipientPanel and
 * DeliveryMethodPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: G S Rajasekaran
 * </p>
 * 
 * @version $Id: ManageListRecipientModel.java,v 1.19 2004/03/30 14:18:06 qzd3k3
 *          Exp $
 * @editor Sarah Tong
 */
public class ManageListRecipientModel implements Cloneable {
    // start with blank VOs in case adding new recipient or distribution
    private RecipientBasicValue basicRecipient = new RecipientBasicValue();

    private RecipientComplexValue complexRecipient = new RecipientComplexValue();

    private DocumentDistributionBasicValue docDistribution = new DocumentDistributionBasicValue();

    private String listType;

    private String callType = "";

    // private boolean saveClicked;
    private boolean usePrefDistType = true; // init to true for new

    // subscriptions

    protected String panelTitle;

    /**
     * Empty
     */
    public ManageListRecipientModel() {
        // do nothing
    }

    // Getters - get from the RecipientBasicValue or the
    // DocumentDistributionBasicValue value as relevant
    public Integer getCourtId() {
        return basicRecipient.getCourtID();
    }

    public Integer getRecipientId() {
        return basicRecipient.getRecipientId();
    }

    public String getRecipientName() {
        return basicRecipient.getRecipientName();
    }

    public String getRecipientEmailAddress() {
        return basicRecipient.getEmailAddress();
    }

    public String getRecipientFaxNumber() {
        return basicRecipient.getFaxNumber();
    }

    public Integer getDocumentDistributionId() {
        return docDistribution.getDocumentDistributionId();
    }

    public String getDistributionType() {
        return docDistribution.getDistributionType();
    }

    public String getDocumentType() {
        return docDistribution.getDocumentType();
    }

    public String getListType() {
        return listType;
    }

    public String getMimeType() {
        return docDistribution.getMimeType();
    }

    public String getPanelTitle() {
        return panelTitle;
    }

    public String getCallType() {
        return callType;
    }

    public boolean isUsePrefDistType() {
        return usePrefDistType;
    }

    public String getPrefMimeType() {
        return basicRecipient.getPrefMimeType();
    }

    public String getPrefDistributionType() {
        return basicRecipient.getPrefDistributionType();
    }

    public RecipientBasicValue getBasicRecipient() {
        return basicRecipient;
    }

    public RecipientComplexValue getComplexRecipient() {
        return complexRecipient;
    }

    public DocumentDistributionBasicValue getDocDistribution() {
        return docDistribution;
    }

    // Setters - set on the RecipientBasicValue and\or RecipientComplexValue
    // and\or
    // DocumentDistributionBasicValue as relevant
    public void setCourtId(Integer cId) {
        basicRecipient.setCourtID(cId);
        complexRecipient.setCourtID(cId);
        docDistribution.setCourtId(cId);
    }

    public void setRecipientId(Integer recId) {
        basicRecipient.setRecipientId(recId);
        complexRecipient.setRecipientId(recId);
        docDistribution.setRecipientID(recId);
    }

    public void setRecipientName(String recName) {
        basicRecipient.setRecipientName(recName);
        complexRecipient.setRecipientName(recName);
    }

    public void setRecipientEmailAddress(String recEmail) {
        basicRecipient.setEmailAddress(recEmail);
        complexRecipient.setEmailAddress(recEmail);
    }

    public void setRecipientFaxNumber(String recFax) {
        basicRecipient.setFaxNumber(recFax);
        complexRecipient.setFaxNumber(recFax);
    }

    public void setDocumentDistributionId(Integer docDistId) {
        docDistribution.setDocumentDistributionId(docDistId);
    }

    public void setDistributionType(String distType) {
        docDistribution.setDistributionType(distType);
    }

    public void setDocumentType(String docType) {
        docDistribution.setDocumentType(docType);
    }

    public void setListType(String lType) {
        listType = lType;
    }

    public void setMimeType(String mType) {
        docDistribution.setMimeType(mType);
    }

    public void setPanelTitle(String s) {
        panelTitle = s;
    }

    public void setCallType(String s) {
        callType = s;
    }

    public void setUsePrefDistType(boolean usePrefDistType) {
        this.usePrefDistType = usePrefDistType;
        if (usePrefDistType) {
            docDistribution.setUsePrefDistType("Y");
        } else {
            docDistribution.setUsePrefDistType("N");
        }
    }

    public void setPrefMimeType(String prefMimeType) {
        basicRecipient.setPrefMimeType(prefMimeType);
        complexRecipient.setPrefMimeType(prefMimeType);
    }

    public void setPrefDistributionType(String prefDistributionType) {
        basicRecipient.setPrefDistributionType(prefDistributionType);
        complexRecipient.setPrefDistributionType(prefDistributionType);
    }

    public void setComplexRecipient(RecipientComplexValue complexRecipient) {
        this.complexRecipient = complexRecipient;
        // when the complex value is set populate the basic also as this
        // is where the getters look for values
        basicRecipient.setCourtID(complexRecipient.getCourtID());
        basicRecipient.setEmailAddress(complexRecipient.getEmailAddress());
        basicRecipient.setFaxNumber(complexRecipient.getFaxNumber());
        basicRecipient.setPrefDistributionType(complexRecipient.getPrefDistributionType());
        basicRecipient.setPrefMimeType(complexRecipient.getPrefMimeType());
        basicRecipient.setRecipientId(complexRecipient.getRecipientId());
        basicRecipient.setRecipientName(complexRecipient.getRecipientName());
        basicRecipient.setId(complexRecipient.getId());
    }

    public void setBasicRecipient(RecipientBasicValue basicRecipient) {
        this.basicRecipient = basicRecipient;
    }

    public void setDocDistribution(DocumentDistributionBasicValue docDistribution) {
        this.docDistribution = docDistribution;
        if (docDistribution.getUsePrefDistType() != null) {
            if (docDistribution.getUsePrefDistType().equals("Y"))
                setUsePrefDistType(true);
            else
                setUsePrefDistType(false);
        } else
            setUsePrefDistType(true);
    }

    public void printModel() {
        XHIBITConstant.info("List Recipient Model");
        XHIBITConstant.info("-------------------");
        XHIBITConstant.info("Recipient Name  : " + getRecipientName());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        basicRecipient = new RecipientBasicValue();
        complexRecipient = new RecipientComplexValue();
        docDistribution = new DocumentDistributionBasicValue();

        setUsePrefDistType(true);
        setListType(null);
        setPanelTitle(null);
        setCallType(null);
    }

}