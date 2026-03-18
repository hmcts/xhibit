package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * Basic Value object to retrieve information from the 
 * XHB_CPP_STAGING_INBOUND table.
 */
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import java.util.Date;

public class D20OffenceLinkBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private Integer d20OffenceLinkId;
	private Integer defendantOnCaseId;
	private Integer seqNo;
	private String dvlaOffenceCode;
	private Integer refOffenceId;
	private Date convictionDate;
	private String intD20;
	private Date intD20Date;
	private String finalD20;
	private Date finalD20Date;
	private String obsInd;
	private String lastUpdatedBy;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String errorMessage;

	public D20OffenceLinkBasicValue() {
		// Empty
	}

	public D20OffenceLinkBasicValue(Integer id, Integer version) {
		super(id, version);
		this.d20OffenceLinkId = id;
	}
	
	public D20OffenceLinkBasicValue(Integer d20OffenceLinkId, Integer defendantOnCaseId, Integer seqNo, String dvlaOffenceCode, Integer refOffenceId,
			Date convictionDate, String intD20, Date intD20Date, String finalD20, Date finalD20Date) {
		this.d20OffenceLinkId = d20OffenceLinkId;
		this.defendantOnCaseId = defendantOnCaseId;
		this.seqNo=seqNo;
		this.dvlaOffenceCode = dvlaOffenceCode;
		this.refOffenceId=refOffenceId;
		this.convictionDate=convictionDate;
		this.intD20=intD20;
		this.intD20Date=intD20Date;
		this.finalD20=finalD20;
		this.finalD20Date=finalD20Date;
	}



	public Integer getD20OffenceLinkId() {
		return d20OffenceLinkId;
	}

	public void setD20OffenceLinkId(Integer d20OffenceLinkId) {
		this.d20OffenceLinkId = d20OffenceLinkId;
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getDvlaOffenceCode() {
		return dvlaOffenceCode;
	}

	public void setDvlaOffenceCode(String dvlaOffenceCode) {
		this.dvlaOffenceCode = dvlaOffenceCode;
	}

	public Integer getRefOffenceId() {
		return refOffenceId;
	}

	public void setRefOffenceId(Integer refOffenceId) {
		this.refOffenceId = refOffenceId;
	}

	public Date getConvictionDate() {
		return convictionDate;
	}

	public void setConvictionDate(Date convictionDate) {
		this.convictionDate = convictionDate;
	}

	public String getIntD20() {
		return intD20;
	}

	public void setIntD20(String intD20) {
		this.intD20 = intD20;
	}

	public Date getIntD20Date() {
		return intD20Date;
	}

	public void setIntD20Date(Date intD20Date) {
		this.intD20Date = intD20Date;
	}

	public String getFinalD20() {
		return finalD20;
	}

	public void setFinalD20(String finalD20) {
		this.finalD20 = finalD20;
	}

	public Date getFinalD20Date() {
		return finalD20Date;
	}

	public void setFinalD20Date(Date finalD20Date) {
		this.finalD20Date = finalD20Date;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}