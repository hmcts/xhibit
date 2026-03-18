package uk.gov.courtservice.xhibit.xmlbinding.crestformsbf;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;


/**
 * <p>
 * Title: Utility class for populating a Defendant in a Crest Form Schema.
 * </p>
 * <p>
 * Description: Defendant made up of : Defendant ID Defendant Nuumber Defendant
 * Name - Forename, Middle Name, Surname
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

public class CrestFormBFDefendantHelper {
    /**
     * Method populating an castor bound xml schema from an entity bean.
     * 
     * @param def
     *            The castor bound CFDefendant object to populate.
     * @param doc
     *            The Defendant On Case entity bean to use for population.
     */
    public static void populateDefendant(uk.gov.courtservice.xhibit.xmlbinding.generated.crestforms.Defendant def,
            XhbDefendantOnCase doc) {
        // get the defendant
        XhbDefendant defEntity = doc.getXhbDefendant();

        // get defendant info for the provided Defendant On Case
        int defID = defEntity.getCrestDefendantId().intValue();
        Integer defNo = doc.getDefendantNumber();
        String defForename = defEntity.getFirstName();
        String defMiddleName = defEntity.getMiddleName();
        String defSurname = defEntity.getSurname();

        // check not null before setting values
        def.setDefendantID(defID);

        // set as default 1
        def.setDefendantNumber(1);

        // set up default defendant surname
        def.getDefendantName().setSurname(" ");

        if (defNo != null) {
            def.setDefendantNumber(defNo.intValue());
        }

        if (defForename != null) {
            def.getDefendantName().setForename(defForename);
        }

        if (defMiddleName != null) {
            def.getDefendantName().setMiddleName(defMiddleName);
        }

        if (defSurname != null) {
            def.getDefendantName().setSurname(defSurname);
        }
    }
}
