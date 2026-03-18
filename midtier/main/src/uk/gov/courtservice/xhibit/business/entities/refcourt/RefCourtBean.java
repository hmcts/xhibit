package uk.gov.courtservice.xhibit.business.entities.refcourt;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
abstract public class RefCourtBean extends CSEntityBean {

    public Integer ejbCreate(String courtFullName, String courtShortName, String namePrefix, String courtType,
            String crestCode, String obsInd, String dxRef, String isPsd, String userDisplayName) throws CreateException {

        setCourtFullName(courtFullName);
        setCourtShortName(courtShortName);
        setNamePrefix(namePrefix);
        setCourtType(courtType);
        setCrestCode(crestCode);
        setObsInd(obsInd);
        setDxRef(dxRef);
        setIsPsd(isPsd);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String courtFullName, String courtShortName, String namePrefix, String courtType,
            String crestCode, String obsInd, String dxRef, String isPsd, String userDisplayName) throws CreateException {
    }

    public abstract Integer getRefCourtId();
    
    public abstract Integer getCourtId();

    public abstract String getCourtFullName();

    public abstract String getCourtShortName();

    public abstract String getNamePrefix();

    public abstract String getCourtType();

    public abstract String getCrestCode();

    public abstract String getObsInd();

    public abstract String getDxRef();

    public abstract String getIsPsd();

    public abstract Integer getAddressId();

    public abstract void setRefCourtId(Integer refCourtId);

    public abstract void setCourtFullName(String courtFullName);

    public abstract void setCourtShortName(String courtShortName);

    public abstract void setNamePrefix(String namePrefix);

    public abstract void setCourtType(String courtType);

    public abstract void setCrestCode(String crestCode);

    public abstract void setObsInd(String obsInd);

    public abstract void setDxRef(String dxRef);

    public abstract void setIsPsd(String isPsd);

    public abstract void setAddressId(Integer addressId);
    
    public abstract void setCourtId(Integer courtId);

    public abstract uk.gov.courtservice.xhibit.business.entities.court.Court getCourt();

    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

    public abstract void setCourt(uk.gov.courtservice.xhibit.business.entities.court.Court court);

    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);
}
