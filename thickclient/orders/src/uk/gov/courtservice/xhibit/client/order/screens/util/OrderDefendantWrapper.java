package uk.gov.courtservice.xhibit.client.order.screens.util;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: OrderDefendantWrapper
 * </p>
 * <p>
 * Description: Wraps a DefendantValue to allow use in ComboBoxes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderDefendantWrapper {

    private DefendantValue defendantValue;

    private DefendantBasicValue defendantBasicValue;

    // public OrderDefendantWrapper(DefendantValue def) {
    // defendantValue = def;
    // }

    public OrderDefendantWrapper(DefendantBasicValue def) {
        defendantBasicValue = def;
    }

    /**
     * Overrides toString to allow display of the name in a combo box
     * 
     * @return
     */
    public String toString() {
        String firstName = defendantBasicValue.getFirstName();
        String middleName = defendantBasicValue.getMiddleName();
        String initials = defendantBasicValue.getInitials();
        StringBuffer buf = new StringBuffer();
        buf.append(defendantBasicValue.getSurname());
        if (firstName != null && firstName.equals("") == false) {
            buf.append(", ");
            buf.append(firstName);
        }
        if (middleName != null && middleName.equals("") == false) {
            buf.append(" ");
            buf.append(middleName);
        }
        // Des Johnston - commented out to fix SCR 52565
        /*
         * if (initials != null && initials.equals("") == false) { buf.append("
         * "); buf.append(initials); }
         */
        return buf.toString();
    }

    /**
     * Returns the wrapped DefendantValue
     * 
     * @return DefendantBasicValue
     */
    public DefendantBasicValue getDefendant() {
        return defendantBasicValue;
    }
}