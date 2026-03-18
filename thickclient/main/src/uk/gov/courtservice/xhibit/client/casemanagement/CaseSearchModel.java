package uk.gov.courtservice.xhibit.client.casemanagement;

import java.util.Collection;
import java.util.List;

public class CaseSearchModel {
	private Integer caseId = 0;
	private String caseType = "";
	private Integer caseNumber = 0;
	private Integer courtId = 0;
	private boolean allowSearchByDefendant = true; // If false only enable
													// search by case number
	private boolean indictmentSearch = false; // If true search comes from
												// indictment
	private boolean transferCase = false; // If true search comes from transfer
											// case
	private boolean redel = false; // If true search comes from replace delete
									// defendant case
	private boolean deleteCase = false; // If true search comes from was
										// cancelled
	private boolean appealCourt = false;
	private boolean caseLinking = false;
	private boolean caseUnlinkNeeded = false;
	private boolean monetaryOrder = false;
	private List<String> validCaseTypes;
	private Collection monetaryOrdersCollection;

	public CaseSearchModel() {
	}

	public Integer getCaseId() {
		return caseId;
	}

	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public Integer getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Integer getCourtId() {
		return courtId;
	}

	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public boolean getAllowSearchByDefendant() {
		return allowSearchByDefendant;
	}

	public void setAllowSearchByDefendant(boolean allowSearchByDefendant) {
		this.allowSearchByDefendant = allowSearchByDefendant;
	}

	public boolean getIndictmentSearch() {
		return indictmentSearch;
	}

	public void setIndictmentSearch(boolean indictmentSearch) {
		this.indictmentSearch = indictmentSearch;
	}

	public boolean getREDEL() {
		return redel;
	}

	public void setREDEL(boolean redel) {
		this.redel = redel;
	}

	public boolean getTransferCase() {
		return transferCase;
	}

	public void setTransferCase(boolean transferCase) {
		this.transferCase = transferCase;
	}

	public boolean getAppealCourt() {
		return appealCourt;
	}

	public void setAppealCourt(boolean appealCourt) {
		this.appealCourt = appealCourt;
	}

	public boolean getDeleteCase() {
		return this.deleteCase;
	}

	public void setDeleteCase(boolean deleteCase) {
		this.deleteCase = deleteCase;
	}

	public boolean getCaseLinking() {
		return caseLinking;
	}

	public void setCaseLinking(boolean caseLinking) {
		this.caseLinking = caseLinking;
	}

	public List<String> getValidCaseTypes() {
		return validCaseTypes;
	}

	public void setValidCaseTypes(List<String> validCaseTypes) {
		this.validCaseTypes = validCaseTypes;
	}

	public void clearmodel() {
		this.setCaseId(0);
		this.setCaseType(null);
		this.setCaseNumber(null);
		this.setCourtId(null);
		this.setAllowSearchByDefendant(false);
		this.setIndictmentSearch(false);
		this.setREDEL(false);
		this.setTransferCase(false);
		this.setAppealCourt(false);
		this.setDeleteCase(false);
		this.setCaseLinking(false);
		this.setValidCaseTypes(null);
		this.setTransferCase(false);
		this.setCaseUnlinkNeeded(false);
		this.setMonetaryOrder(false);
		this.setMonetaryOrdersCollection(null);
	}

	public boolean isCaseUnlinkNeeded() {
		return caseUnlinkNeeded;
	}

	public void setCaseUnlinkNeeded(boolean caseUnlinkNeeded) {
		this.caseUnlinkNeeded = caseUnlinkNeeded;
	}

	public boolean isMonetaryOrder() {
		return monetaryOrder;
	}

	public void setMonetaryOrder(boolean monetaryOrder) {
		this.monetaryOrder = monetaryOrder;
	}

	public Collection getMonetaryOrdersCollection() {
		return monetaryOrdersCollection;
	}

	public void setMonetaryOrdersCollection(Collection monetaryOrdersCollection) {
		this.monetaryOrdersCollection = monetaryOrdersCollection;
	}
}
