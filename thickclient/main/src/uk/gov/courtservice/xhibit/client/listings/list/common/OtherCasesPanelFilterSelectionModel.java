package uk.gov.courtservice.xhibit.client.listings.list.common;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CaseFilterResultComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class OtherCasesPanelFilterSelectionModel {
	private String caseType;
	private String caseClass;
	private String bcStatus;
	private RefHearingTypeBasicValue defaultHearingType;
	private Integer timeFrom ;
	private Integer timeTo ;
	private Integer timeUnits;
	private Integer timeEstWeeks;
	private RefSystemCodeBasicValue requiredJudgeType;
	private String secureCourtroom;
	private String juvenileOnly;
	private String priority ;
	private String restricted;
	private String standard;
	private String sortBy;
	
	private List<CaseFilterResultComplexValue> caseFilterResults;
	
	private static final String YES = "Y";
	private static final String NO = "N";
	private XhibitApplicationController  xac;

	public OtherCasesPanelFilterSelectionModel(XhibitApplicationController xac) {
		this.xac = xac;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseClass() {
		return caseClass;
	}
	public void setCaseClass(String caseClass) {
		this.caseClass = caseClass;
	}
	public String getBcStatus() {
		return bcStatus;
	}
	public void setBcStatus(String bcStatus) {
		this.bcStatus = bcStatus;
	}
	public RefHearingTypeBasicValue getDefaultHearingType() {
		return defaultHearingType;
	}
	public void setDefaultHearingType(RefHearingTypeBasicValue defaultHearingType) {
		this.defaultHearingType = defaultHearingType;
	}
	public Integer getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(Integer timeFrom) {
		this.timeFrom = timeFrom;
	}
	public void setTimeFrom(String timeFromStr) {
		Integer intValue = timeFromStr != null && !timeFromStr.isEmpty() ? Integer.valueOf(timeFromStr) : null;
		setTimeFrom(intValue);
	}	
	public Integer getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(Integer timeTo) {
		this.timeTo = timeTo;
	}	
	public void setTimeTo(String timeToStr) {
		Integer intValue = timeToStr != null && !timeToStr.isEmpty() ? Integer.valueOf(timeToStr) : null;
		setTimeTo(intValue);
	}		
	public Integer getTimeUnits() {
		return timeUnits;
	}
	public void setTimeUnits(Integer timeUnits) {
		this.timeUnits = timeUnits;
	}
	public Integer getTimeEstWeeks() {
		return timeEstWeeks;
	}
	public void setTimeEstWeeks(Integer timeEstWeeks) {
		this.timeEstWeeks = timeEstWeeks;
	}
	public void setTimeEstWeeks(String timeEstWeeksStr) {
		Integer intValue = timeEstWeeksStr != null && !timeEstWeeksStr.isEmpty() ? Integer.valueOf(timeEstWeeksStr) : null;
		setTimeEstWeeks(intValue);
	}		
	public RefSystemCodeBasicValue getRequiredJudgeType() {
		return requiredJudgeType;
	}
	public void setRequiredJudgeType(RefSystemCodeBasicValue requiredJudgeType) {
		this.requiredJudgeType = requiredJudgeType;
	}
	public boolean getBooleanFromString(String val) {
		return val != null && val.equals(YES);
	} 
    public String getStringFromBoolean(boolean val) {
    	return val ? YES : NO;
    }  
	public Boolean getSecureCourtroomAsBoolean() {
		return getBooleanFromString (getSecureCourtroom());
	}	
	 public String getSecureCourtroom(){
		 return secureCourtroom;
	 }
	public void setSecureCourtroomAsBoolean(boolean secureCourtroom) {
		setSecureCourtroom(getStringFromBoolean(secureCourtroom));
	}	
	public void setSecureCourtroom(String secureCourtroom ){
		this.secureCourtroom = secureCourtroom;
	}
	public Boolean getJuvenileOnlyAsBoolean() {
		return getBooleanFromString (getJuvenileOnly());
	}	
	public   String getJuvenileOnly() {
		return juvenileOnly;
		}	
	public void setJuvenileOnlyAsBoolean(boolean juvenileOnly) {
		setJuvenileOnly(getStringFromBoolean(juvenileOnly));
	}
	public void setJuvenileOnly(String juvenileOnly) {
		this.juvenileOnly = juvenileOnly;
	}
	public Boolean getPriorityAsBoolean() {
		return getBooleanFromString( getPriority());
	}	
	public String getPriority() {
		return priority;
	}	
	public void setPriorityAsBooean(boolean priority) {
		setPriority(getStringFromBoolean(priority));
	}	
	public void setPriority(String priority) {
		this.priority = priority;
	}
		
	public Boolean getRestrictedAsBoolean() {
		return getBooleanFromString(getRestricted());
	}
	public String getRestricted() {
		return restricted;
	}
	public void setRestrictedAsBoolean(boolean restricted) {
		setRestricted(getStringFromBoolean(restricted));
	}	
	public void setRestricted(String restricted) {
		this.restricted = restricted;
	}
	public Boolean getStandardAsBoolean() {
		return getBooleanFromString(getStandard());
	}
	public String getStandard() {
		return standard;
	}
	public void setStandardAsBoolean(boolean standard) {
		setStandard(getStringFromBoolean(standard));
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public XhibitApplicationController getXac() {
		return xac;
	}
	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}
	
	/**
	 * @return the caseFilterResults
	 */
	public List<CaseFilterResultComplexValue> getCaseFilterResults() {
		return caseFilterResults;
	}
	
	/**
	 * @param caseFilterResults the caseFilterResults to set
	 */
	public void setCaseFilterResults(List<CaseFilterResultComplexValue> caseFilterResults) {
		this.caseFilterResults = caseFilterResults;
	}
}