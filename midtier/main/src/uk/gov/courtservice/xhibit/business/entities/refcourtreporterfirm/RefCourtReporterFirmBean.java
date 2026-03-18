package uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefCourtReporterFirmBean extends CSEntityBean {

    public Integer ejbCreate(java.lang.String obsInd, java.lang.String displayFirst, java.lang.String dxRef,
            java.lang.String vatNo, java.lang.String firmName, Integer courtId, java.lang.Integer addressId,
            Integer crestCourtReporterFirmId, String userDisplayName) throws CreateException {
        setObsInd(obsInd);
        setDisplayFirst(displayFirst);
        setDxRef(dxRef);
        setVatNo(vatNo);
        setFirmName(firmName);
        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setAddressId(addressId);
        setCrestCourtReporterFirmId(crestCourtReporterFirmId);
        return null;
    }

    public void ejbPostCreate(java.lang.String obsInd, java.lang.String displayFirst, java.lang.String dxRef,
            java.lang.String vatNo, java.lang.String firmName, Integer courtId, java.lang.Integer addressId,
            Integer crestCourtReporterFirmId, String userDisplayName) throws CreateException {
    }

    public abstract void setRefCourtReporterFirmId(Integer refCourtReporterFirmId);

    public abstract void setObsInd(java.lang.String obsInd);

    public abstract void setDisplayFirst(java.lang.String displayFirst);

    public abstract void setDxRef(java.lang.String dxRef);

    public abstract void setVatNo(java.lang.String vatNo);

    public abstract void setFirmName(java.lang.String firmName);

    public abstract Integer getRefCourtReporterFirmId();

    public abstract java.lang.String getObsInd();

    public abstract java.lang.String getDisplayFirst();

    public abstract java.lang.String getDxRef();

    public abstract java.lang.String getVatNo();

    public abstract java.lang.String getFirmName();

    public abstract Integer getCourtId();

    public abstract java.lang.Integer getAddressId();

    public abstract void setCourtId(Integer courtId);

    public abstract void setAddressId(java.lang.Integer addressId);

    public abstract void setCrestCourtReporterFirmId(Integer crestCourtReporterFirmId);

    public abstract Integer getCrestCourtReporterFirmId();

    // cmr
    public abstract java.util.Collection getRefCourtReporters();

    public abstract void setRefCourtReporters(java.util.Collection refCourtReporters);

    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);

    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

}