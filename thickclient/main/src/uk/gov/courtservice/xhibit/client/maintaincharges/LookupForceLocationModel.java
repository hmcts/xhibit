package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;


/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class LookupForceLocationModel {
    
    private XhibitApplicationController xac;
    
    private List<RefSystemCodeBasicValue> locationCodes;

    String forceLocationCode;
    
    
    public LookupForceLocationModel(
            XhibitApplicationController xac,
            List<RefSystemCodeBasicValue> locationCodes) {
        
        this.xac = xac;
        this.locationCodes = locationCodes;
    }
    
    public XhibitApplicationController getXac() {
        return xac;
    }
    
    public List<RefSystemCodeBasicValue> getLocationCodes() {
        return locationCodes;
    }
    
    public String getForceLocationCode() {
        return forceLocationCode;
    }
    
    public void setForceLocationCode(String forceLocationCode) {
        this.forceLocationCode = forceLocationCode;
    }
}


