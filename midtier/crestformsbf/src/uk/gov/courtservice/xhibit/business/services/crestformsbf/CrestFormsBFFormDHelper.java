package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFFormDXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * The Crest Form D Form Type Helper
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public class CrestFormsBFFormDHelper extends CrestFormsBFFormHelper {

    /**
     * Construct a helper for crest form D
     */
    public CrestFormsBFFormDHelper() {
        super("D");
    }

    /**
     * Return true if there are commitals for sentence for a defendant on a
     * case, this implements Business Rule 3.4.8 see Business Rules Catalog for
     * more info. Form D also available for Defendants that have unrelated
     * disposals
     * 
     * @param caze
     *            the case
     * @param defendant
     *            the defendant
     * @return true if this form type is valid for a defendant on that case
     */
    public boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return (CrestFormsBFDatabase.hasCommittalForSentenceOffences(caze, defendant) || (CrestFormsBFDatabase
                .hasUnrelatedDisposals(defendant, caze) && caze.getType().equals("S")));
    }

    /**
     * Get a form D XML helper
     * 
     * @return a form D XML helper
     */
    public CrestFormBFXmlHelper createXmlHelper() {
        return new CrestFormBFFormDXMLHelper(getName());
    }

}
