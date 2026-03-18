package uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefCourtReporterFirm extends CSEntityLocal {
    public Integer getRefCourtReporterFirmId();

    public void setObsInd(String obsInd);

    public String getObsInd();

    public void setDisplayFirst(String displayFirst);

    public String getDisplayFirst();

    public void setDxRef(String dxRef);

    public String getDxRef();

    public void setVatNo(String vatNo);

    public String getVatNo();

    public void setFirmName(String firmName);

    public String getFirmName();

    public Integer getCourtId();

    public void setCourtId(Integer courtId);

    public void setAddressId(Integer addressId);

    public Integer getAddressId();

    public void setCrestCourtReporterFirmId(Integer crestCourtReporterFirm);

    public Integer getCrestCourtReporterFirmId();

    // cmr
    public abstract Collection getRefCourtReporters();

    public abstract void setRefCourtReporters(Collection refCourtReporters);

    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);

    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

}