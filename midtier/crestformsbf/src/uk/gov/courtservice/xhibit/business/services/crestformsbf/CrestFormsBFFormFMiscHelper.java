package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFFormFMiscXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * The Crest Form FMisc Form Type Helper
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

public class CrestFormsBFFormFMiscHelper extends CrestFormsBFFormHelper {

    /**
     * Construct a helper for crest form F
     */
    public CrestFormsBFFormFMiscHelper() {
        super("FMisc", "F");
    }

    /**
     * Return true if the case is a miscellaneous appeal and the defendant has
     * charges, this implements Business Rule 3.4.12 see Business Rules Catalog
     * for more info.
     * 
     * @param caze
     *            the case
     * @param defendant
     *            the defendant
     * @return true if this form type is valid for a defendant on that case
     */
    public boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return caze.isMiscAppeal();
    }

    /**
     * Get a form FMisc XML helper
     * 
     * @return a form FMisc XML helper
     */
    public CrestFormBFXmlHelper createXmlHelper() {
        return new CrestFormBFFormFMiscXMLHelper(getName());
    }

}
