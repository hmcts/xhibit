package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Collection;

/**
 * <p>
 * Title: RecipientComplexValue
 * </p>
 * <p>
 * Description: Warned List Letter Recipient Value Object. Maps one to one with
 * Recipient entity.
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

public class RecipientComplexValue extends uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue {
    Collection documentDistribution;
    
    private static final long serialVersionUID =-2151464861176158283L;

    /**
     * Default Constructors
     */
    public RecipientComplexValue() {
    }

    public RecipientComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Returns a Collection of DocumentDistributionBasicValue
     * 
     * @return java.util.Collection
     */
    public Collection getDocumentDistribution() {
        return documentDistribution;
    }

    /**
     * takes a DocumentDistributionBasicValue value object as parameter
     * 
     * @param java.util.Collection
     *            documentDistribution
     */
    public void setDocumentDistribution(Collection documentDistribution) {
        this.documentDistribution = documentDistribution;
    }

}