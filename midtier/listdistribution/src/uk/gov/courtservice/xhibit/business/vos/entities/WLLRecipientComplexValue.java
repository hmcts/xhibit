package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * <p>
 * Title: WLLRecipientComplexValue
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
 * @version 1.0
 */

public class WLLRecipientComplexValue extends uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue {
    uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue wllRecipientBasicValue;

    DocumentDistributionBasicValue documentDistribution;
    private static final long serialVersionUID =-4669502781750082611L;

    /**
     * Default Constructors
     */
    public WLLRecipientComplexValue() {
    }

    public WLLRecipientComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Returns a DocumentDistributionBasicValue object
     * 
     * @return DocumentDistributionBasicValue
     */
    public DocumentDistributionBasicValue getDocumentDistribution() {
        return documentDistribution;
    }

    /**
     * takes a DocumentDistributionBasicValue object as parameter
     * 
     * @param DocumentDistributionBasicValue
     *            documentDistribution
     */
    public void setDocumentDistribution(DocumentDistributionBasicValue documentDistribution) {
        this.documentDistribution = documentDistribution;
    }

}