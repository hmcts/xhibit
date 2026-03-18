package uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.address.Address;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
abstract public class RefSolicitorFirmBean extends CSEntityBean {

    public Integer ejbCreate(Address address, Integer courtId, Integer crestSofId, String dxRef, String obsInd,
            String shortName, String laCode, String solicitorFirmName, String vatNo, String userDisplayName) throws CreateException {

        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setCrestSofId(crestSofId);
        setDxRef(dxRef);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setShortName(shortName);
        setLaCode(laCode);
        setSolicitorFirmName(solicitorFirmName);
        setVatNo(vatNo);
        return null;
    }

    public void ejbPostCreate(Address address, Integer courtId, Integer crestSofId, String dxRef, String obsInd,
            String shortName, String laCode, String solicitorFirmName, String vatNo, String userDisplayName) throws CreateException {
        setAddress(address);
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract Integer getAddressId();

    public abstract Integer getCourtId();

    public abstract Integer getCrestSofId();

    public abstract String getDxRef();

    public abstract String getObsInd();

    public abstract Integer getRefSolicitorFirmId();

    public abstract String getShortName();
    
    public abstract String getLaCode();

    public abstract String getSolicitorFirmName();

    public abstract String getVatNo();

    public abstract void setAddressId(Integer addressId);

    public abstract void setCourtId(Integer courtId);

    public abstract void setCrestSofId(Integer crestSofId);

    public abstract void setDxRef(String dxRef);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefSolicitorFirmId(Integer refSolicitorFirmId);

    public abstract void setShortName(String shortName);
    
    public abstract void setLaCode(String laCode);
    
    public abstract void setSolicitorFirmName(String solicitorFirmName);

    public abstract void setVatNo(String vatNo);

    // ------------------------------CMR
    // Fields------------------------------------
    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);

    public abstract java.util.Collection getSolicitors();

    public abstract void setSolicitors(java.util.Collection solicitors);
    
    public abstract java.util.Collection getProsecutorRefSolFirms(  ) ;
    
    public abstract void setProsecutorRefSolFirms(java.util.Collection prosecutorRefSolFirms) ;

}