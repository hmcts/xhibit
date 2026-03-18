package uk.gov.courtservice.xhibit.business.vos.services.dailylist;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class CourtHouseValue extends CSAbstractValue {

    private String courtHouseType;

    private String courtSiteCode;

    private String courtHouseName;

    private String crestCourtId;
    
    private static final long serialVersionUID =4034107496118709198L;

    // private AddressBasicValue addressBasicValue;
    private XhbAddressBasicValue xhbAddressBasicValue;

    public CourtHouseValue() {
    }

    // getters and setters.
    public XhbAddressBasicValue getAddress() {
        return xhbAddressBasicValue;
    }

    public String getCourtHouseName() {
        return courtHouseName;
    }

    public String getCourtSiteCode() {
        return courtSiteCode;
    }

    public void setCourtSiteCode(String courtSiteCode) {
        this.courtSiteCode = courtSiteCode;
    }

    public void setCourtHouseName(String courtHouseName) {
        this.courtHouseName = courtHouseName;
    }

    public void setAddress(XhbAddressBasicValue xhbAddressBasicValue) {
        this.xhbAddressBasicValue = xhbAddressBasicValue;
        System.out.println(xhbAddressBasicValue);
    }

    public String getCourtHouseType() {
        return courtHouseType;
    }

    public void setCourtHouseType(String courtHouseType) {
        this.courtHouseType = courtHouseType;
    }

    public Integer getCourtSiteId() {
        return getId();
    }

    public void setCourtSiteId(Integer courtSiteId) {
        setId(courtSiteId);
    }

    public String getCrestCourtId() {
        return crestCourtId;
    }

    public void setCrestCourtId(String crestCourtId) {
        this.crestCourtId = crestCourtId;
    }
}