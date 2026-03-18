package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;

/**
 * <p>
 * Title: CourtData
 * </p>
 * <p>
 * Description: Data about a court. To not be confused with court site. One
 * court can have many sites.
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

public class CourtValue extends CSAbstractValue {
    private String courtHouseType;

    private String courtHouseCode;

    private String courtHouseName;

    private XhbAddressBasicValue xhbAddressBasicValue;
    
    private static final long serialVersionUID =-4247662062677841892L;

    public CourtValue() {
    }

    public XhbAddressBasicValue getAddress() {
        return xhbAddressBasicValue;
    }

    public String getCourtHouseCode() {
        return courtHouseCode;
    }

    public String getCourtHouseType() {
        return courtHouseType;
    }

    public void setAddress(XhbAddressBasicValue xhbAddressBasicValue) {
        this.xhbAddressBasicValue = xhbAddressBasicValue;
    }

    public void setCourtHouseCode(String courtHouseCode) {
        this.courtHouseCode = courtHouseCode;
    }

    public void setCourtHouseName(String courtHouseName) {
        this.courtHouseName = courtHouseName;
    }

    public void setCourtHouseType(String courtHouseType) {
        this.courtHouseType = courtHouseType;
    }

    public String getCourtHouseName() {
        return courtHouseName;
    }

    public Integer getCourtId() {
        return getId();
    }

    public void setCourtId(Integer courtId) {
        setId(courtId);
    }

}