package uk.gov.courtservice.xhibit.business.services.caze;

import uk.gov.courtservice.xhibit.business.vos.services.caze.CaseType;

/**
 * <p>
 * Title: ChargeTypes
 * </p>
 * <p>
 * Description: Defines allowed charge types
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class CaseTypes {
    public static final CaseType MISC_APPEAL = new CaseType("Miscelleanous Appeal", "O");

    public static final CaseType CRIMINAL_APPEAL = new CaseType("Criminal Appeal", "CA");

    public static final CaseType CRIMINAL_APPEAL_1 = new CaseType("Criminal Appeal", "B");

    public static final CaseType CRIMINAL_APPEAL_2 = new CaseType("Criminal Appeal", "C");

    public static final CaseType CRIMINAL_APPEAL_3 = new CaseType("Criminal Appeal", "S");

    public static CaseType getCaseType(String casetype) {
        if (casetype == null) {
            return null;
        }
        String type = casetype.trim();
        if (type.equalsIgnoreCase("O"))
            return MISC_APPEAL;
        if (type.equalsIgnoreCase("B"))
            return CRIMINAL_APPEAL_1;
        if (type.equalsIgnoreCase("C"))
            return CRIMINAL_APPEAL_2;
        if (type.equalsIgnoreCase("S"))
            return CRIMINAL_APPEAL_3;
        return null;
    }
}