package uk.gov.courtservice.xhibit.client.listdistribution;

// xhibit

import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: ManageLetterRecipientModel
 * </p>
 * <p>
 * Description: Data managed by the <code>ManageLEtterRecipientPanel</code>
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
 * @version $Id: ManageLetterRecipientModel.java,v 1.16 2004/03/30 14:18:06
 *          qzd3k3 Exp $
 * @editor Sarah Tong
 */
public class ManageLetterRecipientModel implements Cloneable {
    // start with blank VOs to avoid null pointer exception on the getters
    // and
    // in case we are creating a new distribution
    private WLLRecipientComplexValue complexWLLRecipient;

    private WLLRecipientBasicValue basicWLLRecipient;

    private DocumentDistributionBasicValue docDistribution;

    private String listType;

    private String callType;

    protected String panelTitle;

    private String transactionType = "";

    public static String POPULATECOMPLEX = "POPULATECOMPLEX";

    /**
     * Sets usePrefDistType to 'N'. usePrefDistType is a not null field, however
     * there is no concept of preferred distribution type in relation to WLL's
     * so it is set to 'N' here.
     */
    public ManageLetterRecipientModel() {
        complexWLLRecipient = new WLLRecipientComplexValue();
        basicWLLRecipient = complexWLLRecipient;
        docDistribution = new DocumentDistributionBasicValue();
        complexWLLRecipient.setDocumentDistribution(docDistribution);
        docDistribution.setUsePrefDistType("N");
    }

    // Getters - get from the WLLRecipientBasicValue or the
    // DocumentDistributionBasicValue value as relevant
    public Integer getCourtId() {
        return basicWLLRecipient.getCourtID();
    }

    public Integer getWLLRecipientId() {
        return basicWLLRecipient.getWllRecipientId();
    }

    public Integer getCrestSolicitorFirmId() {
        return basicWLLRecipient.getCrestSolicitorFirmID();
    }

    public String getWLLRecipientName() {
        return basicWLLRecipient.getSolicitorFirmName();
    }

    public String getSolicitorFirmAddress() {
        return basicWLLRecipient.getSolicitorFirmAddress();
    }

    public String getSolicitorFirmEmailAddress() {
        return basicWLLRecipient.getSolicitorFirmEmail();
    }

    public String getSolicitorFirmFaxNumber() {
        return basicWLLRecipient.getSolicitorFirmFax();
    }

    public Integer getDocumentDistributionId() {
        return this.docDistribution.getDocumentDistributionId();
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

    public WLLRecipientBasicValue getBasicWLLRecipient() {
        return basicWLLRecipient;
    }

    public WLLRecipientComplexValue getComplexWLLRecipient() {
        return complexWLLRecipient;
    }

    public DocumentDistributionBasicValue getDocDistribution() {
        return docDistribution;
    }

    // Setters - set on the WLLRecipientBasicValue and\or
    // WLLRecipientComplexValue
    // and\or DocumentDistributionBasicValue as relevant
    public void setCourtId(Integer cId) {
        basicWLLRecipient.setCourtID(cId);
        complexWLLRecipient.setCourtID(cId);
        docDistribution.setCourtId(cId);
    }

    public void setWLLRecipientId(Integer wllRecId) {
        basicWLLRecipient.setWllRecipientId(wllRecId);
        complexWLLRecipient.setWllRecipientId(wllRecId);
        docDistribution.setWllRecipientID(wllRecId);
    }

    public void setCrestSolicitorFirmId(Integer csfId) {
        basicWLLRecipient.setCrestSolicitorFirmID(csfId);
        complexWLLRecipient.setCrestSolicitorFirmID(csfId);
    }

    public void setSolicitorFirmEmailAddress(String csfEmail) {
        basicWLLRecipient.setSolicitorFirmEmail(csfEmail);
        complexWLLRecipient.setSolicitorFirmEmail(csfEmail);
    }

    public void setSolicitorFirmFaxNumber(String csfFax) {
        basicWLLRecipient.setSolicitorFirmFax(csfFax);
        complexWLLRecipient.setSolicitorFirmFax(csfFax);
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

    public void setComplexWLLRecipient(WLLRecipientComplexValue complexWLLRecipient) {
        this.complexWLLRecipient = complexWLLRecipient;
        setBasicWLLRecipient(complexWLLRecipient);
        if (complexWLLRecipient.getDocumentDistribution() == null) {
            setDocDistribution(new DocumentDistributionBasicValue());
            complexWLLRecipient.setDocumentDistribution(getDocDistribution());
        } else {
            setDocDistribution(complexWLLRecipient.getDocumentDistribution());
        }

        // when the complex value is set populate the basic also as this
        // is where the getters look for values
        // basicWLLRecipient.setCourtID(complexWLLRecipient.getCourtID());
        // basicWLLRecipient.setCrestSolicitorFirmID(complexWLLRecipient.getCrestSolicitorFirmID());
        // basicWLLRecipient.setSolicitorFirmAddress(complexWLLRecipient.getSolicitorFirmAddress());
        // basicWLLRecipient.setSolicitorFirmEmail(complexWLLRecipient.getSolicitorFirmEmail());
        // basicWLLRecipient.setSolicitorFirmFax(complexWLLRecipient.getSolicitorFirmFax());
        // basicWLLRecipient.setSolicitorFirmName(complexWLLRecipient.getSolicitorFirmName());
        // basicWLLRecipient.setWllRecipientId(complexWLLRecipient.getWllRecipientId());
    }

    private void setBasicWLLRecipient(WLLRecipientBasicValue basicWLLRecipient) {
        this.basicWLLRecipient = basicWLLRecipient;
    }

    public void setDocDistribution(DocumentDistributionBasicValue docDistribution) {
        this.docDistribution = docDistribution;
        docDistribution.setUsePrefDistType("N");
    }

    public void printModel() {
        XHIBITConstant.info("Letter Recipient Model");
        XHIBITConstant.info("-------------------");
        XHIBITConstant.info("Recipient Name  : " + getWLLRecipientName());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void clearmodel() {
        basicWLLRecipient = new WLLRecipientBasicValue();
        complexWLLRecipient = new WLLRecipientComplexValue();
        docDistribution = new DocumentDistributionBasicValue();

        setCourtId(null);
        setListType(null);
        setPanelTitle(null);
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}