package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: BwHistoryBasicValue
 * </p>
 * <p>
 * Description: Contains the object retrieved from XHB_BW_HISTORY table.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Matt Newman
 * @version 1.0
 */

public class BwHistoryBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;
	
	private Integer defendantOnCaseId;
	private Date bwIssueDate;
	private Date bwEndDate;
	private String bcStatusBwIssued;
	private String bcStatusBwEnded;
	private String withdrawn;
	private String absconding;
	private String obsInd;
	private Date creationDate;
	
	public BwHistoryBasicValue() {
	}
	
	public BwHistoryBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public BwHistoryBasicValue(Integer defendantOnCaseId, Date bwIssueDate, Date bwEndDate, 
							   String bcStatusBwIssued, String bcStatusBwEnded, String withdrawn, String absconding,
							   String obsInd, Date creationDate) {	
		setDefendantOnCaseId(defendantOnCaseId);
		setBwIssueDate(bwIssueDate);
		setBwEndDate(bwEndDate);
		setBcStatusBwIssued(bcStatusBwIssued);
		setBcStatusBwEnded(bcStatusBwEnded);
		setWithdrawn(withdrawn);
		setAbsconding(absconding);
		setObsInd(obsInd);
		setCreationDate(creationDate);
	}

	public Integer getDefendantOnCaseId() {
		return defendantOnCaseId;
	}

	public void setDefendantOnCaseId(Integer defendantOnCaseId) {
		this.defendantOnCaseId = defendantOnCaseId;
	}

	public Date getBwIssueDate() {
		return bwIssueDate;
	}

	public void setBwIssueDate(Date bwIssueDate) {
		this.bwIssueDate = bwIssueDate;
	}

	public Date getBwEndDate() {
		return bwEndDate;
	}

	public void setBwEndDate(Date bwEndDate) {
		this.bwEndDate = bwEndDate;
	}

	public String getBcStatusBwIssued() {
		return bcStatusBwIssued;
	}

	public void setBcStatusBwIssued(String bcStatusBwIssued) {
		this.bcStatusBwIssued = bcStatusBwIssued;
	}

	public String getBcStatusBwEnded() {
		return bcStatusBwEnded;
	}

	public void setBcStatusBwEnded(String bcStatusBwEnded) {
		this.bcStatusBwEnded = bcStatusBwEnded;
	}

	public String getWithdrawn() {
		return withdrawn;
	}

	public void setWithdrawn(String withdrawn) {
		this.withdrawn = withdrawn;
	}

	public String getAbsconding() {
		return absconding;
	}

	public void setAbsconding(String absconding) {
		this.absconding = absconding;
	}

	public String getObsInd() {
		return obsInd;
	}

	public void setObsInd(String obsInd) {
		this.obsInd = obsInd;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}