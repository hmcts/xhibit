package uk.gov.courtservice.xhibit.business.entities.defhearingrecord;

import java.sql.Timestamp;

import javax.ejb.CreateException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;

public abstract class DefHearingRecordBean extends CSEntityBean {
	public Integer ejbCreate(Integer refAdjournmentId, Timestamp adjournedDate, String isAdjourned,
			Timestamp startDateNewBailStatus, String newBailStatus, Timestamp dateBailApplication,
			String substBailApplication, String oralEvidence, String resultBailApplication, String isHraApplication,
			Integer refDefHearingTypeId, String endBailStatus, String startBailStatus, Integer defendantOnCaseId,
			Integer hearingId, String hearingDateFreeTxt1, String hearingDateFreeTxt2, String hearingDateFreeTxt3,
			Timestamp hearingStartDate, Timestamp hearingEndDate, Long lastCalculatedDuration, String mpHearingType,
			String userDisplayName, String trialInDefAbsence, String sentenceInDefAbsence, String formAStatus,
			String formACourtClerk, String s41Application, String s41Granted, String s41ApplicationMade)
			throws CreateException {
		setRefAdjournmentId(refAdjournmentId);
		setAdjournedDate(adjournedDate);
		setIsAdjourned(isAdjourned);
		setStartDateNewBailStatus(startDateNewBailStatus);
		setNewBailStatus(newBailStatus);
		setDateBailApplication(dateBailApplication);
		setSubstBailApplication(substBailApplication);
		setOralEvidence(oralEvidence);
		setResultBailApplication(resultBailApplication);
		setIsHraApplication(isHraApplication);
		setRefDefHearingTypeId(refDefHearingTypeId);
		setEndBailStatus(endBailStatus);
		setStartBailStatus(startBailStatus);
		setDefendantOnCaseId(defendantOnCaseId);
		// setHearingId(hearingId);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setHearingDateFreeTxt1(hearingDateFreeTxt1);
		setHearingDateFreeTxt2(hearingDateFreeTxt2);
		setHearingDateFreeTxt3(hearingDateFreeTxt3);
		setHearingStartDate(hearingStartDate);
		setHearingEndDate(hearingEndDate);
		setLastCalculatedDuration(lastCalculatedDuration);
		setMpHearingType(mpHearingType);
		setTrialInDefAbsence(trialInDefAbsence);
		setSentenceInDefAbsence(sentenceInDefAbsence);
		setFormAStatus(formAStatus);
		setFormACourtClerk(formACourtClerk);
		setS41Application(s41Application);		
		setS41Granted(s41Granted);		
		setS41ApplicationMade(s41ApplicationMade);

		return null;
	}

	public void ejbPostCreate(Integer refAdjournmentId, Timestamp adjournedDate, String isAdjourned,
			Timestamp startDateNewBailStatus, String newBailStatus, Timestamp dateBailApplication,
			String substBailApplication, String oralEvidence, String resultBailApplication, String isHraApplication,
			Integer refDefHearingTypeId, String endBailStatus, String startBailStatus, Integer defendantOnCaseId,
			Integer hearingId, String hearingDateFreeTxt1, String hearingDateFreeTxt2, String hearingDateFreeTxt3,
			Timestamp hearingStartDate, Timestamp hearingEndDate, Long lastCalculatedDuration, String mpHearingType,
			String userDisplayName, String trialInDefAbsence, String sentenceInDefAbsence, String formAStatus,
			String formACourtClerk, String s41Application, String s41Granted, String s41ApplicationMade)
			throws CreateException {
		try {
			setHearing(HearingMaintainer.getInstance().findByPK(hearingId));
		} catch (ObjectNotFoundException e) {
			log.error("Could not find hearing: ", e);
			throw new CreateException(e.getMessage());
		}

	}

	public abstract void setHearingRecordId(Integer hearingRecordId);

	public abstract void setRefAdjournmentId(Integer refAdjournmentId);

	public abstract void setAdjournedDate(Timestamp adjournedDate);

	public abstract void setIsAdjourned(String isAdjourned);

	public abstract void setStartDateNewBailStatus(Timestamp startDateNewBailStatus);

	public abstract void setNewBailStatus(String newBailStatus);

	public abstract void setDateBailApplication(Timestamp dateBailApplication);

	public abstract void setSubstBailApplication(String substBailApplication);

	public abstract void setOralEvidence(String oralEvidence);

	public abstract void setResultBailApplication(String resultBailApplication);

	public abstract void setIsHraApplication(String isHraApplication);

	public abstract void setRefDefHearingTypeId(Integer refDefHearingTypeId);

	public abstract void setEndBailStatus(String endBailStatus);

	public abstract void setStartBailStatus(String startBailStatus);

	public abstract void setDefendantOnCaseId(Integer defendantOnCaseId);

	public abstract void setHearingId(Integer hearingId);

	public abstract void setHearingDateFreeTxt1(String hearingDateFreeTxt1);

	public abstract void setHearingDateFreeTxt2(String hearingDateFreeTxt2);

	public abstract void setHearingDateFreeTxt3(String hearingDateFreeTxt3);

	public abstract void setHearingStartDate(Timestamp hearingStartDate);

	public abstract void setHearingEndDate(Timestamp hearingEndDate);

	public abstract void setLastCalculatedDuration(Long lastCalculatedDuration);

	public abstract void setMpHearingType(String mpHearingType);

	public abstract void setTrialInDefAbsence(String trialInDefAbsence);

	public abstract void setSentenceInDefAbsence(String sentenceInDefAbsence);

	public abstract void setFormAStatus(String formAStatus);

	public abstract void setFormACourtClerk(String formACourtClerk);
	
	public abstract void setS41Application(String s41Application);
	
	public abstract void setS41Granted(String s41Granted);
	
	public abstract void setS41ApplicationMade(String s41ApplicationMade) ;

	// -----------------------------------------------------------------------------------------
	public abstract Integer getHearingRecordId();

	public abstract Integer getRefAdjournmentId();

	public abstract Timestamp getAdjournedDate();

	public abstract String getIsAdjourned();

	public abstract Timestamp getStartDateNewBailStatus();

	public abstract String getNewBailStatus();

	public abstract Timestamp getDateBailApplication();

	public abstract String getSubstBailApplication();

	public abstract String getOralEvidence();

	public abstract String getResultBailApplication();

	public abstract String getIsHraApplication();

	public abstract Integer getRefDefHearingTypeId();

	public abstract String getEndBailStatus();

	public abstract String getStartBailStatus();

	public abstract Integer getDefendantOnCaseId();

	public abstract Integer getHearingId();

	public abstract String getHearingDateFreeTxt1();

	public abstract String getHearingDateFreeTxt2();

	public abstract String getHearingDateFreeTxt3();

	public abstract Timestamp getHearingStartDate();

	public abstract Timestamp getHearingEndDate();

	public abstract Long getLastCalculatedDuration();

	public abstract String getMpHearingType();

	public abstract String getTrialInDefAbsence();

	public abstract String getSentenceInDefAbsence();

	public abstract String getFormAStatus();

	public abstract String getFormACourtClerk();
	
	public abstract String getS41Application();
	
	public abstract String getS41Granted();
	
	public abstract String getS41ApplicationMade();


	// CMR
	// ----------------------------------------------------------------------------------------

	public abstract void setHearing(Hearing hearing);

	public abstract Hearing getHearing();
}