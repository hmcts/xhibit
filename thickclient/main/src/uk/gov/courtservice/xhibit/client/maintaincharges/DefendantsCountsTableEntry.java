package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.vos.services.charge.DefendantOnOffenceValue;

/**
 * <p>
 * Title: DefendantsCountsTableEntry
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
 * 54211 02-09-2003 AW Daley Modified to initialise commonRefNo to null
 */
abstract class DefendantsCountsTableEntry {
    // Boolean used so that table uses default renderer for Boolean object
    private Boolean selected;

    private String commonRefNo;

    private boolean commonRefNoEnabled;

    // Boolean used so that table uses default renderer for Boolean object
    private Boolean autoGenerate;

    private boolean autoGenerateEnabled;

    public DefendantsCountsTableEntry(boolean onCount, boolean autoGenerate) {
        this.setOnCount(new Boolean(onCount));
        this.setCommonRefNo(null);
        this.setAutoGenerate(new Boolean(autoGenerate));
        this.setAutoGenerateEnabled(false);
        this.setCommonRefNoEnabled(false);
    }

    public void setOnCount(Boolean onCount) {
        this.selected = onCount;
    }

    public void setCommonRefNo(String commonRefNo) {
        // If null set to null and exit
        if (commonRefNo == null) {
            this.commonRefNo = null;
            return;
        }

        // Convert CRN to upper case or set to null if blank
        if (commonRefNo.trim().equals(""))
            this.commonRefNo = null;
        else
            this.commonRefNo = commonRefNo.toUpperCase();

    }

    public void clearCommonRefNo() {
        setCommonRefNo(null);
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

    public Boolean isSelected() {
        return this.selected;
    }

    public String getCommonRefNo() {
        return this.commonRefNo;
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

    /**
     * To be implemented by the subclass. Returns the value to be displayed in
     * the first column of the table For an Offence the offence description
     * would be returned. For a Defendant the defendants full name would be
     * returned
     * 
     * @return
     */
    public abstract String getDisplayValue();

    /**
     * To be implemented by the subclass returns a DefendantOnOffenceValue for
     * the table entry.
     * 
     * @param id
     * @return
     */
    public abstract DefendantOnOffenceValue getDefendantOnOffenceValue(Integer id);

    /**
     * To be implemented by the subclass.Returns the DefendantValue or
     * OffenceValue held in the table entry.
     * 
     * @return
     */
    public abstract Object getValue();

}