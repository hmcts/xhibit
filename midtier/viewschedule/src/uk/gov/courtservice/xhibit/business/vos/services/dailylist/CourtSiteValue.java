package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import java.util.ArrayList;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: CourtSiteValue
 * </p>
 * <p>
 * Description: A court site value that contains a list of scheduled values. It
 * also contains specific data about the particular court site.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class CourtSiteValue extends CSAbstractValue {
    // list of sitting values for this particular court site.
    private ArrayList sittingValues;

    private CourtHouseValue courtHouseValue;
    
    private static final long serialVersionUID =6221505031334669803L;

    public CourtSiteValue() {
    }

    public ArrayList getSittingValues() {
        if (sittingValues == null) {
            sittingValues = new ArrayList();
        }
        return sittingValues;
    }

    public void setSittingValues(ArrayList sittingValues) {
        this.sittingValues = sittingValues;
    }

    public CourtHouseValue getCourtHouseValue() {
        return courtHouseValue;
    }

    public void setCourtHouseValue(CourtHouseValue courtHouseValue) {
        this.courtHouseValue = courtHouseValue;
    }

}