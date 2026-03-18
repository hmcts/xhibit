package uk.gov.courtservice.xhibit.business.vos.services.refprosecutoragency;

import java.util.Date;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class RefProsecutorAgencyValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;
	private Integer refProsecutorAgencyId = 0;
	private String refProsecutorType = null;
	private Integer addressId = 0;
	private Integer courtId = 0;
	private String cpsCode = null;
	private String createdBy = null;
	private Date creationDate;
	private String crestOpposerId = null;
	private String dxRef = null;
	private String initials = null;
	private String lastUpdatedBy = null;
	private Date lastUpdateDate;
	private String obsInd = null;
	private String prosecutorName1 = null;
	private String prosecutorName2 = null;
	private String prosecutorName3 = null;
	private String title = null;
	private Integer version = 0;
	
	public RefProsecutorAgencyValue() {
	}
	
	public RefProsecutorAgencyValue(Integer refProsecutorAgencyId) {
		this.refProsecutorAgencyId = refProsecutorAgencyId;
	}

	public RefProsecutorAgencyValue(String refProsecutorType) {
		this.refProsecutorType = refProsecutorType;
	}

	public RefProsecutorAgencyValue(Integer refProsecutorAgencyId, String refProsecutorType) {
		this.refProsecutorAgencyId = refProsecutorAgencyId;
		this.refProsecutorType = refProsecutorType;
	}
	
	public Integer getRefProsecutoryAgencyId() {
		return refProsecutorAgencyId;
	}
	public void setRefProsecutorAgencyId(Integer refProsecutorAgencyId) {
		this.refProsecutorAgencyId = refProsecutorAgencyId;
	}

	public String getrefProsecutorType() {
		return refProsecutorType;
	}
	public void setRefProsecutorType(String refProsecutorType) {
		this.refProsecutorType = refProsecutorType;
	}

	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	
	public Integer getCourtId() {
		return courtId;
	}
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getCpsCode() {
		return cpsCode;
	}
	public void setCpsCode(String cpsCode) {
		this.cpsCode = cpsCode;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getCrestOpposerId() {
		return crestOpposerId;
	}
	public void setCrestOpposerId(String crestOpposerId) {
		this.crestOpposerId = crestOpposerId;
	}
	
	public String getDxRef() {
		return dxRef;
	}
	public void setDxRef(String dxRef) {
		this.dxRef = dxRef;
	}
	
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public String getObsInd() {
		return obsInd;
	}
	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	public String getProsecutorName1() {
		return prosecutorName1;
	}
	public void setProsecutorName1(String prosecutorName1) {
		this.prosecutorName1 = prosecutorName1;
	}
	
	public String getProsecutorName2() {
		return prosecutorName2;
	}
	public void setProsecutorName2(String prosecutorName2) {
		this.prosecutorName2 = prosecutorName2;
	}
	
	public String getProsecutorName3() {
		return prosecutorName3;
	}
	public void setProsecutorName3(String prosecutorName3) {
		this.prosecutorName3 = prosecutorName3;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}

}
