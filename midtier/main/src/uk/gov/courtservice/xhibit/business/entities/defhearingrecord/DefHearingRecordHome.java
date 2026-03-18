package uk.gov.courtservice.xhibit.business.entities.defhearingrecord;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface DefHearingRecordHome extends EJBLocalHome {
	public DefHearingRecord create(Integer refAdjournmentId, Timestamp adjournedDate, String isAdjourned,
			Timestamp startDateNewBailStatus, String newBailStatus, Timestamp dateBailApplication,
			String substBailApplication, String oralEvidence, String resultBailApplication, String isHraApplication,
			Integer refDefHearingTypeId, String endBailStatus, String startBailStatus, Integer defendantOnCaseId,
			Integer hearingId, String hearingDateFreeTxt1, String hearingDateFreeTxt2, String hearingDateFreeTxt3,
			Timestamp hearingStartDate, Timestamp hearingEndDate, Long lastCalculatedDuration, String mpHearingType,
			String userDisplayName, String trialInDefAbsence, String sentenceInDefAbsence, String formAStatus,
			String formACourtClerk, String s41Application, String s41Granted, String s41ApplicationMade)
			throws CreateException;

	public DefHearingRecord findByPrimaryKey(Integer hearingRecordId) throws FinderException;

	public DefHearingRecord findByKeyAndVersion(Integer key, Integer version) throws FinderException;

	public DefHearingRecord findByDefendantOnCaseIDAndHearingID(Integer docID, Integer hID) throws FinderException;

	public Collection findByDefendantOnCaseID(Integer docID) throws FinderException;
}