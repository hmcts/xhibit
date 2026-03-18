package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFFormEXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * The Crest Form E Form Type Helper
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

public class CrestFormsBFFormEHelper extends CrestFormsBFFormHelper {

    /**
     * Construct a helper for crest form E
     */
    public CrestFormsBFFormEHelper() {
        super("E");
    }

    /**
     * Return true if there are breach(es) for defendant(s) on a case, this
     * implements Business Rule 3.4.10 see Business Rules Catalog for more info.
     * 
     * @param caze
     *            the case
     * @param defendant
     *            the defendant
     * @return true if this form type is valid for a defendant on that case
     */
    public boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return CrestFormsBFDatabase.hasBreachOffences(caze, defendant);
    }

    /**
     * Get a form E XML helper
     * 
     * @return a form E XML helper
     */
    public CrestFormBFXmlHelper createXmlHelper() {
        return new CrestFormBFFormEXMLHelper(getName());
    }

}
