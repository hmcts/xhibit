package uk.gov.courtservice.xhibit.business.vos.services.prosecutorrefsolfirm;

import java.util.Date;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ProsecutorRefSolFirmBasicValue;

public class ProsecutorRefSolFirmValue extends CSAbstractValue {
	private static final long serialVersionUID = 1L;

	private ProsecutorRefSolFirmBasicValue prosRefSolFirmBV;

	private Integer prosecutorRefSolFirmId;
	private Integer refSolicitorFirmId;
	private Integer crestCpfId;
	private String repType;
	private Date repStDate;
	private Date repEndDate;
	private String createdBy;
	private Date creationDate;
	private Integer caseProsAgencyId;
	private String solicitorRef;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Integer version;
	private Integer posInTable;
	private String obsInd;
	private Integer legalAidOrderId;

	public ProsecutorRefSolFirmValue(Integer prosecutorRefSolFirmId, Integer refSolicitorFirmId, Integer crestCpfId,
			String repType, Date repStDate, Date repEndDate, Date lastUpdateDate, Date creationDate, String createdBy,
			String lastUpdatedBy, Integer version, Integer caseProsAgencyId, String solicitorRef, String obsInd, Integer legalAidOrderId) {

		setProsecutorRefSolFirmId(prosecutorRefSolFirmId);
		setRefSolicitorFirmId(refSolicitorFirmId);
		setCrestCpfId(crestCpfId);
		setRepType(repType);
		setRepStDate(repStDate);
		setRepEndDate(repEndDate);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		setCaseProsAgencyId(caseProsAgencyId);
		setSolicitorRef(solicitorRef);
		setObsInd(obsInd);
		setLegalAidOrderId(legalAidOrderId);
	}

	public ProsecutorRefSolFirmValue() {
	}

	public Integer getPrimaryKey() {
		return getProsecutorRefSolFirmId();
	}

	public Integer getProsecutorRefSolFirmId() {
		return prosecutorRefSolFirmId;
	}

	public void setProsecutorRefSolFirmId(Integer prosecutorRefSolFirmId) {
		this.prosecutorRefSolFirmId = prosecutorRefSolFirmId;
	}

	public Integer getRefSolicitorFirmId() {
		return refSolicitorFirmId;
	}

	public void setRefSolicitorFirmId(Integer refSolicitorFirmId) {
		this.refSolicitorFirmId = refSolicitorFirmId;
	}

	public Integer getCrestCpfId() {
		return crestCpfId;
	}

	public void setCrestCpfId(Integer crestCpfId) {
		this.crestCpfId = crestCpfId;
	}

	public String getRepType() {
		return repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
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

	public Integer getCaseProsAgencyId() {
		return caseProsAgencyId;
	}

	public void setCaseProsAgencyId(Integer caseProsAgencyId) {
		this.caseProsAgencyId = caseProsAgencyId;
	}

	public String getSolicitorRef() {
		return solicitorRef;
	}

	public void setSolicitorRef(String solicitorRef) {
		this.solicitorRef = solicitorRef;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getRepStDate() {
		return repStDate;
	}

	public void setRepStDate(Date repStDate) {
		this.repStDate = repStDate;
	}

	public Date getRepEndDate() {
		return repEndDate;
	}

	public void setRepEndDate(Date repEndDate) {
		this.repEndDate = repEndDate;
	}

	public ProsecutorRefSolFirmBasicValue getProsRefSolFirmBV() {
		return prosRefSolFirmBV;
	}

	public void setProsRefSolFirmBV(ProsecutorRefSolFirmBasicValue prosRefSolFirmBV) {
		this.prosRefSolFirmBV = prosRefSolFirmBV;
	}

	/**
	 * @return the posInTable
	 */
	public Integer getPosInTable() {
		return posInTable;
	}

	/**
	 * @param posInTable the posInTable to set
	 */
	public void setPosInTable(Integer posInTable) {
		this.posInTable = posInTable;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}
	
	/**
	 * @return the legalAidOrderId
	 */
	public Integer getLegalAidOrderId() {
		return legalAidOrderId;
	}

	/**
	 * @param legalAidOrderId the legalAidOrderId to set
	 */
	public void setLegalAidOrderId(Integer legalAidOrderId) {
		this.legalAidOrderId = legalAidOrderId;
	}

}
