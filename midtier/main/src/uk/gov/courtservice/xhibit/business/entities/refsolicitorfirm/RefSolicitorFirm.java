package uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.address.Address;

public interface RefSolicitorFirm extends CSEntityLocal {

//    public Integer getAddressId();

    public Integer getCourtId();

    public Integer getCrestSofId();

    public String getDxRef();

    public String getObsInd();

    public Integer getRefSolicitorFirmId();

    public String getShortName();
    
    public String getLaCode();
    
    public String getSolicitorFirmName();

    public String getVatNo();

    public void setCourtId(Integer courtId);

    public void setCrestSofId(Integer crestSofId);

    public void setDxRef(String dxRef);

    public void setObsInd(String obsInd);

    public void setShortName(String shortName);
    
    public void setLaCode(String laCode);    

    public void setSolicitorFirmName(String solicitorFirmName);

    public void setVatNo(String vatNo);

    public Address getAddress();

    public void setAddress(Address address);
    
    public Integer getAddressId();
    
    public void setAddressId(Integer addressId);

    public java.util.Collection getSolicitors();

    public void setSolicitors(java.util.Collection solicitors);
    
    public java.util.Collection getProsecutorRefSolFirms(  ) ;
    
    public void setProsecutorRefSolFirms(java.util.Collection prosecutorRefSolFirms) ;
    
    public String getLastUpdatedBy();
    
    public void setLastUpdatedBy(String userName);

}