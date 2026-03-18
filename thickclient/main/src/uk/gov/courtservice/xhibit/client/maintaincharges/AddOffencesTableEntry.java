package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.HashMap;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

/**
 * <p>
 * Title: AddOffencesTableEntry
 * </p>
 * <p>
 * Description: A class private to the table model that represents an entry in
 * the table. This is an abstract class the must be extend by objects that are
 * to be displayed in the table
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
 * 31-07-2003 AW Daley Initial Version
 *
 * 02-09-2003 AW Daley Modified so that the model can handle null CRNs
 */
class AddOffencesTableEntry {
    private boolean commonRefNoEnabled;

    private OffenceValue offence;

    // Boolean used so that table uses default renderer for Boolean object
    private Boolean autoGenerate;

    private boolean autoGenerateEnabled;

    public AddOffencesTableEntry(OffenceValue offence, boolean autoGenerate) {
        this.offence = offence;
        this.setAutoGenerate(new Boolean(autoGenerate));
        this.setAutoGenerateEnabled(true);
        this.setCommonRefNoEnabled(false);
    }

    public void setCommonRefNo(String commonRefNo, Integer defendantId) {
        // Create defendant on offence to hold CRN
        XhbDefendantOnOffenceBasicValue defOnOffence = new XhbDefendantOnOffenceBasicValue();

        // Convert CRN to upper case
        if (commonRefNo != null) {
            if (commonRefNo.trim().equals(""))
                defOnOffence.setCrnId(null);
            else
                defOnOffence.setCrnId(commonRefNo.toUpperCase());
        } else
            defOnOffence.setCrnId(null);

        // Add DefendantOnOffence to Offence
        HashMap map = new HashMap();
        map.put(defendantId, defOnOffence);
        getOffence().setDefOnOffenceBasicValues(map);
    }

    public void setCommonRefNoEnabled(boolean commonRefNoEnabled) {
        this.commonRefNoEnabled = commonRefNoEnabled;
    }

    public void setAutoGenerate(Boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public void setAutoGenerateEnabled(boolean autoGenerateEnabled) {
        this.autoGenerateEnabled = autoGenerateEnabled;
    }

    public String getCommonRefNo(Integer defendantId) {
        HashMap map;
        map = getOffence().getDefOnOffenceBasicValues();

        if (map == null || map.isEmpty())
            return "";

        XhbDefendantOnOffenceBasicValue defOnOffence = (XhbDefendantOnOffenceBasicValue) map.get(defendantId);

        String crn = defOnOffence.getCrnId();

        return crn;
    }

    public Boolean isAutoGenerate() {
        return this.autoGenerate;
    }

    public boolean isAutoGenerateEnabled() {
        return this.autoGenerateEnabled;
    }

    public boolean isCommonRefNoEnabled() {
        return this.commonRefNoEnabled;

    }

    public String getOffenceDescription() {
        return (offence.getOffenceDescription());
    }

    public String getOffenceCode() {
        return (offence.getOffenceCode());
    }

    public OffenceValue getOffence() {
        return this.offence;
    }
}