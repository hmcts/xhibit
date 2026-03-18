package uk.gov.courtservice.xhibit.client.util.helpers;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: Helper class for Defendants
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: DefendantHelper.java,v 1.4 2006/06/05 12:30:42 bzjrnl Exp $
 */

public class DefendantHelper {

    /**
     * private constructor as this class only contains static helper methods.
     */
    private DefendantHelper() {
    }

    /**
     * Get the defendants name as a string based on the given
     * XhbDefendantBasicValue
     * 
     * @param defendantBasicValue
     *            the XhbDefendantBasicValue
     * @return the name of the defendant
     */
    public static String getDefendantFullName(XhbDefendantBasicValue defendantBasicValue) {
        StringBuffer buf = new StringBuffer();

        buf.append(checkNull(defendantBasicValue.getFirstName()));
        buf.append(defendantBasicValue.getFirstName() == null ? "" : " ");
        buf.append(checkNull(defendantBasicValue.getMiddleName()));
        buf.append(defendantBasicValue.getMiddleName() == null ? "" : " ");
        buf.append(checkNull(defendantBasicValue.getSurname()));

        return buf.toString();
    }

    /**
     * Get the defendants name as a string based on the given DefendantValue
     * 
     * @param defendantValue
     *            the DefendantValue
     * @return the name of the defendant
     */
    public static String getDefendantFullName(DefendantValue defendantValue) {
        StringBuffer buf = new StringBuffer();

        buf.append(checkNull(defendantValue.getFirstName()));
        buf.append(defendantValue.getFirstName() == null ? "" : " ");
        buf.append(checkNull(defendantValue.getMiddleName()));
        buf.append(defendantValue.getMiddleName() == null ? "" : " ");
        buf.append(checkNull(defendantValue.getSurName()));

        return buf.toString();
    }

    public static String checkNull(String checkString) {
        return (checkString == null ? "" : checkString);
    }
}