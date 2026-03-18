package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria.DefaultDisposalCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria.DisposalCodeDisposalCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria.DisposalCodeTemplateVersionDisposalCriteria;
import uk.gov.courtservice.xhibit.client.results.disposals.disposalcriteria.LineAvailDisposalCriteria;

/**
 * <p>
 * Title: DisposalCriteriaFactory
 * </p>
 * <p>
 * Description: DisposalCriteriaFactory used to create disposal criteria.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.8 $
 */
public class DisposalCriteriaFactory {
    /**
     * Stop static factory from being created
     */
    private DisposalCriteriaFactory() {
        // Change permisions of default constructor
    }

    /**
     * Create a DisposalCriteria to match all disposals
     * 
     * @param DisposalCode
     *            the code to match
     * @return the new DisposalCriteria object
     */
    public static DisposalCriteria create() {
        return new DefaultDisposalCriteria();
    }

    /**
     * Create a DisposalCriteria to match the specified criteria
     * 
     * @param DisposalCode
     *            the code to match
     * @return the new DisposalCriteria object
     */
    public static DisposalCriteria create(String code) {
        return new DisposalCodeDisposalCriteria(code);
    }

    /**
     * Create a DisposalCriteria to match the specified criteria
     * 
     * @param disposalCode
     *            search criteria to match
     * @param templateVersion
     *            search criteria to match
     * @return the new DisposalCriteria object to match the specified criteria
     */
    public static DisposalCriteria create(String disposalCode, int templateVersion) {
        return new DisposalCodeTemplateVersionDisposalCriteria(disposalCode, templateVersion);
    }

    /**
     * Create a DisposalCriteria to match the specified criteria
     * 
     * @param lineAvail
     *            search criteria to match
     * @return the new DisposalCriteria object to match the specified criteria
     */
    public static DisposalCriteria createLineAvail(int lineAvail) {
        return new LineAvailDisposalCriteria(lineAvail);
    }

}