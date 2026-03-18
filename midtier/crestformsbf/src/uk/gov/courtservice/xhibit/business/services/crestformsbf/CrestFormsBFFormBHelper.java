package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFFormBXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * The Crest Form B Form Type Helper
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

public class CrestFormsBFFormBHelper extends CrestFormsBFFormHelper {

    /**
     * Construct a helper for crest form B
     */
    public CrestFormsBFFormBHelper() {
        super("B");
    }

    /**
     * Return true if the defendant has indictment(s) with count(s) on the given
     * case, this implements Business Rule 3.4.4 see Business Rules Catalog for
     * more info. Form B also available for Defendants that have unrelated
     * disposals
     * 
     * @param caze
     *            the case
     * @param defendant
     *            the defendant
     * @return true if this form type is valid for a defendant on that case
     */
    public boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return (CrestFormsBFDatabase.hasIndictmentCounts(caze, defendant) || (CrestFormsBFDatabase
                .hasUnrelatedDisposals(defendant, caze) && caze.getType().equals("T")));
    }

    /**
     * Get a form B XML helper
     * 
     * @return a form B XML helper
     */
    public CrestFormBFXmlHelper createXmlHelper() {
        return new CrestFormBFFormBXMLHelper(getName());
    }

}
