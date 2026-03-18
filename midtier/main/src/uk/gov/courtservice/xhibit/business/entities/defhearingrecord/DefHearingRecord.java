package uk.gov.courtservice.xhibit.business.entities.defhearingrecord;

import java.sql.Timestamp;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;

public interface DefHearingRecord extends CSEntityLocal {
	public void setRefAdjournmentId(Integer refAdjournmentId);

	public void setAdjournedDate(Timestamp adjournedDate);

	public void setIsAdjourned(String isAdjourned);

	public void setStartDateNewBailStatus(Timestamp startDateNewBailStatus);

	public void setNewBailStatus(String newBailStatus);

	public void setDateBailApplication(Timestamp dateBailApplication);

	public void setSubstBailApplication(String substBailApplication);

	public void setOralEvidence(String oralEvidence);

	public void setResultBailApplication(String resultBailApplication);

	public void setIsHraApplication(String isHraApplication);

	public void setRefDefHearingTypeId(Integer refDefHearingTypeId);

	public void setEndBailStatus(String endBailStatus);

	public void setStartBailStatus(String startBailStatus);

	public void setDefendantOnCaseId(Integer defendantOnCaseId);

	public void setHearingId(Integer hearingId);

	public void setHearing(Hearing hearing);

	public void setHearingDateFreeTxt1(String hearingDateFreeTxt1);

	public void setHearingDateFreeTxt2(String hearingDateFreeTxt2);

	public void setHearingDateFreeTxt3(String hearingDateFreeTxt3);

	public void setHearingStartDate(Timestamp hearingStartDate);

	public void setHearingEndDate(Timestamp hearingEndDate);

	public void setLastCalculatedDuration(Long lastCalculatedDuration);
	
	public void setS41Application(String s41Application);
	
	public void setS41Granted(String s41Granted);
	
	public void setS41ApplicationMade(String s41ApplicationMade);

	public Integer getHearingRecordId();

	public Integer getRefAdjournmentId();

	public Timestamp getAdjournedDate();

	public String getIsAdjourned();

	public Timestamp getStartDateNewBailStatus();

	public String getNewBailStatus();

	public Timestamp getDateBailApplication();

	public String getSubstBailApplication();

	public String getOralEvidence();

	public String getResultBailApplication();

	public String getIsHraApplication();

	public Integer getRefDefHearingTypeId();

	public String getEndBailStatus();

	public String getStartBailStatus();

	public Integer getDefendantOnCaseId();

	public Integer getHearingId();

	public Hearing getHearing();

	public String getHearingDateFreeTxt1();

	public String getHearingDateFreeTxt2();

	public String getHearingDateFreeTxt3();

	public Timestamp getHearingStartDate();

	public Timestamp getHearingEndDate();

	public Long getLastCalculatedDuration();
	
	public String getS41Application();
	
	public String getS41Granted();
	
	public String getS41ApplicationMade();		

	public String getMpHearingType();

	public void setMpHearingType(String mpHearingType);

	public String getTrialInDefAbsence();

	public void setTrialInDefAbsence(String trialInDefAbsence);

	public String getSentenceInDefAbsence();

	public void setSentenceInDefAbsence(String sentenceInDefAbsence);

	public void setFormAStatus(String formAStatus);

	public String getFormAStatus();

	public void setFormACourtClerk(String formACourtClerk);

	public String getFormACourtClerk();

}