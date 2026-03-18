package uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This class is not completed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class CLDayValue extends CSAbstractValue {
    private String date;

    private String[] shorthandWriters;

    private String[] courtClerks;

    private String[] counsels;

    private String[] ushers;
    private static final long serialVersionUID = 730438610731505410L;

    public CLDayValue() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShorthandWriters(String[] shorthandWriters) {
        this.shorthandWriters = shorthandWriters;
    }

    public void setCourtClerks(String[] courtClerks) {
        this.courtClerks = courtClerks;
    }

    public void setCounsels(String[] counsels) {
        this.counsels = counsels;
    }

    public void setUshers(String[] ushers) {
        this.ushers = ushers;
    }

    public String getDate() {
        return date;
    }

    public String[] getShorthandWriters() {
        return shorthandWriters;
    }

    public String[] getCourtClerks() {
        return courtClerks;
    }

    public String[] getCounsels() {
        return counsels;
    }

    public String[] getUshers() {
        return ushers;
    }
}