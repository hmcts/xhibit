package uk.gov.courtservice.xhibit.business.services.crestformsbf;

import uk.gov.courtservice.xhibit.business.database.crestformsbf.CrestFormsBFDatabase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFFormFXMLHelper;
import uk.gov.courtservice.xhibit.xmlbinding.crestformsbf.CrestFormBFXmlHelper;

/**
 * <p>
 * The Crest Form F Form Type Helper
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

public class CrestFormsBFFormFHelper extends CrestFormsBFFormHelper {

    /**
     * Construct a helper for crest form F
     */
    public CrestFormsBFFormFHelper() {
        super("F");
    }

    /**
     * Return true if the case is a criminal appeal and has offences, this
     * implements Business Rule 3.4.12 see Business Rules Catalog for more info.
     * Form F also available for Defendants that have unrelated disposals
     * 
     * @param caze
     *            the case
     * @param defendant
     *            the defendant
     * @return true if this form type is valid for a defendant on that case
     */
    public boolean isValidForm(CrestFormsBFCase caze, CrestFormsBFDefendant defendant) {
        return ((caze.isCriminalAppeal() && CrestFormsBFDatabase.hasCriminalAppealOffences(caze, defendant)) || (CrestFormsBFDatabase
                .hasUnrelatedDisposals(defendant, caze) && caze.getType().equals("A")));
    }

    /**
     * Get a form F XML helper
     * 
     * @return a form F XML helper
     */
    public CrestFormBFXmlHelper createXmlHelper() {
        return new CrestFormBFFormFXMLHelper(getName());
    }

}
