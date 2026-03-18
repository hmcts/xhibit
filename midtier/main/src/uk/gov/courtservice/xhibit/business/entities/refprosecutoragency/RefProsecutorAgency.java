package uk.gov.courtservice.xhibit.business.entities.refprosecutoragency;

import java.util.Date;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.address.Address;

public interface RefProsecutorAgency extends CSEntityLocal {
	
	public Integer getRefProsecutorAgencyId();
	public void setRefProsecutorAgencyId(Integer refProsecutorAgencyId);

	public String getTitle();
	public void setTitle(String title);

	public String getProsecutorName1();
	public void setProsecutorName1(String prosecutorName1);

	public String getProsecutorName2();
	public void setProsecutorName2(String prosecutorName2);

	public String getProsecutorName3();
	public void setProsecutorName3(String prosecutorName3);

	public String getInitials();
	public void setInitials(String initials);

	public Integer getAddressId();
	public String getCrestOpposerId();

	public void setCrestOpposerId(String crestOpposerId);
	public Integer getCourtId();

	public String getCpsCode();
	public void setCpsCode(String cpsCode);

	public String getDxRef();
	public void setDxRef(String dxRef);

	public String getObsInd();
	public void setObsInd(String obsInd);

	public Date getLastUpdateDate();
	public void setLastUpdateDate(Date lastUpdateDate);

	public Date getCreationDate();
	public void setCreationDate(Date creationDate);

	public String getCreatedBy();
	public void setCreatedBy(String createdBy);

	public String getLastUpdatedBy();
	public void setLastUpdatedBy(String lastUpdatedBy);

	public Integer getVersion();
	public void setVersion(Integer version);

	public Address getAddress();
    public void setAddress(Address address);
}
