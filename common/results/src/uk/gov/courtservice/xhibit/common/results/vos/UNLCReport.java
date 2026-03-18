package uk.gov.courtservice.xhibit.common.results.vos;


import java.util.ArrayList;
import java.util.Vector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.common.results.vos.common.ReportAbsttractValue;

public class UNLCReport extends ReportAbsttractValue {
	private static final long serialVersionUID = 1L;
	
	private String timeofReport;
	private String dateofReport;
	private String sortBy;
	private String notesRequested;
	private String priorityNotes;
	private String restrictedNotes;
	private String standardNotes;
	private String caseType;
	private String caseClass;
	private String bcStatus;
    private String hearingType;
    private String judgeDescription;
	private String requiredJudgeType;
	private Vector<UNLCReportValue> unlcReportValues ;
	//private ArrayList outstandingCasesValues;
	private ArrayList unlistedCasesValues;
	
	public UNLCReport(){
		unlcReportValues = new Vector<UNLCReportValue>();
	}
		
	public String getDateofReport() {
		return dateofReport;
	}

	public void setDateofReport(String dateofReport) {
		this.dateofReport = dateofReport;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getBcStatus() {
		return bcStatus;
	}

	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}


	public String getRequiredJudgeType() {
		return requiredJudgeType;
	}

	public void setRequiredJudgeType(String requiredJudgeType) {
		this.requiredJudgeType = requiredJudgeType;
	}

	public Vector<UNLCReportValue> getUnlcReportValues() {
		return unlcReportValues;
	}

	public void setUnlcReportValues(Vector<UNLCReportValue> unlcReportValues) {
		this.unlcReportValues = unlcReportValues;
	}

	public String getTimeofReport() {
		return timeofReport;
	}

	public void setTimeofReport(String timeofReport) {
		this.timeofReport = timeofReport;
	}

	public String getHearingType() {
		return hearingType;
	}

	public void setHearingType(String hearingType) {
		this.hearingType = hearingType;
	}

	public String getJudgeDescription() {
		return judgeDescription;
	}

	public void setJudgeDescription(String judgeDescription) {
		this.judgeDescription = judgeDescription;
	}

	public String getNotesRequested() {
		return notesRequested;
	}

	public void setNotesRequested(String notesRequested) {
		this.notesRequested = notesRequested;
	}

	public String getPriorityNotes() {
		return priorityNotes;
	}

	public void setPriorityNotes(String priorityNotes) {
		this.priorityNotes = priorityNotes;
	}

	public String getRestrictedNotes() {
		return restrictedNotes;
	}

	public void setRestrictedNotes(String restrictedNotes) {
		this.restrictedNotes = restrictedNotes;
	}

	public String getStandardNotes() {
		return standardNotes;
	}

	public void setStandardNotes(String standardNotes) {
		this.standardNotes = standardNotes;
	}

	public String getCaseClass() {
		return caseClass;
	}

	public void setCaseClass(String caseClass) {
		this.caseClass = caseClass;
	}

	@Override
	public String getShortReportCode() {
		return "UNLC";
	}

	public ArrayList getUnlistedCasesValues() {
		return unlistedCasesValues;
	}

	public void setUnlistedCasesValues(ArrayList unlistedCasesValues) {
		this.unlistedCasesValues = unlistedCasesValues;
	}


	
}