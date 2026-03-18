package uk.gov.courtservice.xhibit.business.vos.services.refsolicitorfirm;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseRefSolFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;

/**
 * <p>
 * Title: XhbDefOnCaseRefSolFirmBasicValue
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * updatable data from XHIBIT. The internal value obejcts is
 * DefOnCaseRefSolFirmBasicValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class RefSolicitorFirmValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private RefSolicitorFirmBasicValue refFBV;

	private Integer refSolicitorFirmId;
	private String solicitorFirmName;
	private Integer crestSofId;
	private Integer courtId;
	private String obsInd;
	private String shortName;
	private String dxRef;
	private String vatNo;
	private Date creationDate;
	private Date lastUpdateDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;
	private Integer addressId;
	private String laCode;

	public RefSolicitorFirmValue() {
	}

	public RefSolicitorFirmValue(Integer refSolicitorFirmId, String solicitorFirmName, Integer crestSofId,
			Integer courtId, String obsInd, String shortName, String dxRef, String vatNo, Date lastUpdateDate,
			Date creationDate, String createdBy, String lastUpdatedBy, Integer version, Integer addressId,
			String laCode) {

		setRefSolicitorFirmId(refSolicitorFirmId);
		setSolicitorFirmName(solicitorFirmName);
		setCrestSofId(crestSofId);
		setCourtId(courtId);
		setObsInd(obsInd);
		setShortName(shortName);
		setDxRef(dxRef);
		setVatNo(vatNo);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setAddressId(addressId);
		setLaCode(laCode);
	}

	public Integer getPrimaryKey() {
		return getRefSolicitorFirmId();
	}

	public Integer getRefSolicitorFirmId() {
		return refSolicitorFirmId;
	}

	public void setRefSolicitorFirmId(Integer refSolicitorFirmId) {
		this.refSolicitorFirmId = refSolicitorFirmId;
	}

	public String getSolicitorFirmName() {
		return solicitorFirmName;
	}

	public void setSolicitorFirmName(String name){
		solicitorFirmName = name;
	}

	public Integer getCrestSofId() {
		return crestSofId;
	}

	public void setCrestSofId(Integer crestSofId) {
		this.crestSofId = crestSofId;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDxRef() {
		return dxRef;
	}

	public void setDxRef(String dxRef) {
		this.dxRef = dxRef;
	}
	public String getVatNo() {
		return vatNo;
	}

	public void setVatNo(String vatNo) {
		this.vatNo = vatNo;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getLaCode() {
		return laCode;
	}

	public void setLaCode(String laCode) {
		this.laCode = laCode;
	}

	public RefSolicitorFirmBasicValue getRefSolicitorFirmBasicValue() {
		return refFBV;
	}

	public void setRefSolicitorFirmBasicValue(RefSolicitorFirmBasicValue refFBV) {
		this.refFBV = refFBV;
	}

}