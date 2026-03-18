package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: OffenceValueTableEntry
 * </p>
 * <p>
 * Description: A class private to the table model that represents an entry in
 * the table that contains an OffenceValue.
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
class OffenceValueTableEntry extends DefendantsCountsTableEntry {
    private OffenceValue offence;

    public OffenceValueTableEntry(OffenceValue offence, boolean onCount, boolean autoGenerate) {
        super(onCount, autoGenerate);
        this.offence = offence;
    }

    /**
     * Returns the Offence Description to be displayed in the first column of
     * the table
     * 
     * @return
     */
    public String getDisplayValue() {
        return (offence.getOffenceDescription());
    }

    /**
     * Returns a DefendantOnOffenceValue for the table entry
     * 
     * @param defendantId
     * @return
     */
    public DefendantOnOffenceValue getDefendantOnOffenceValue(Integer defendantId) {
        return new DefendantOnOffenceValue(this.getOffence().getOffenceID(), defendantId, this.getCommonRefNo());
    }

    /**
     * Returns an OffenceValue for the table entry
     * 
     * @return
     */
    public Object getValue() {
        return this.getOffence();
    }

    public OffenceValue getOffence() {
        return this.offence;
    }
}