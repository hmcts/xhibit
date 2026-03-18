package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title: DefendantValueTableEntry
 * </p>
 * <p>
 * Description: A class private to the table model that represents an entry in
 * the table that contains a DefendantValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 04-08-2003 AW Daley Initial Version
 */

class DefendantValueTableEntry extends DefendantsCountsTableEntry {
    private DefendantValue defendant;

    public DefendantValueTableEntry(DefendantValue defendant, boolean onCount, boolean autoGenerate) {
        super(onCount, autoGenerate);
        this.defendant = defendant;
    }

    /**
     * * Returns the full name of the defendant from the table entry to be
     * displayed in the first column of the table
     * 
     * @return
     */
    public String getDisplayValue() {
        return (getDefendantsFullName(defendant));
    }

    /**
     * Builds the Defenfants full name.This method is a candidate for
     * refactoring
     * 
     * @param defendant
     * @return
     */
    private String getDefendantsFullName(DefendantValue defendant) {
        String firstName = defendant.getFirstName();
        if (firstName == null)
            firstName = "";

        String middleName = defendant.getMiddleName();
        if (middleName == null)
            middleName = "";

        String surname = defendant.getSurName();
        if (surname == null)
            surname = "";

        String defendantName = firstName + " " + middleName + " " + surname;

        return defendantName;

    }

    /**
     * Returns a DefendantOnOffenceValue for the table entry
     * 
     * @param offenceId
     * @return
     */
    public DefendantOnOffenceValue getDefendantOnOffenceValue(Integer offenceId) {
        return new DefendantOnOffenceValue(offenceId, getDefendant().getDefendantID(), this.getCommonRefNo());
    }

    /**
     * Returns a DefendantValue for the table entry
     * 
     * @return
     */
    public Object getValue() {
        return this.getDefendant();
    }

    public DefendantValue getDefendant() {
        return this.defendant;
    }
}