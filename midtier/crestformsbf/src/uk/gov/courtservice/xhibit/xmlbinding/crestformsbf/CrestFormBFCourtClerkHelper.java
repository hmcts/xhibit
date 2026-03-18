package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

/**
 * <p>
 * Title: Utility class for populating Court Clerk's name in a Crest Form
 * Schema.
 * </p>
 * <p>
 * Description: Court Clerks names
 * </p>
 * <p>
 * This class populates castor bound xml objects from entity beans.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Surtar Bachra
 * @version 1.0
 */

public class CrestFormBFCourtClerkHelper {
    /**
     * Util method to populate the Court Clerk
     */
    public static void populateCourtClerk(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.CourtClerk cc,
            String courtClerkName) {
        if (courtClerkName != null) {
            cc.setClerkName(courtClerkName);
        }
    }
}
