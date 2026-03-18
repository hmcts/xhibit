package uk.gov.courtservice.xhibit.client.actions.casemanagement;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.casemanagement.AddIndictmentCase;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * 
 * @author C.Kudzin
 * @version 1.0
 * 
 *          Change Log
 * @version 1.0 C.Kudzin
 */

public class AddIndictmentCaseAction extends XAction {
	public AddIndictmentCaseAction() {
		populateFromBundle("AddIndictmentCase");
	}

	private XhibitApplicationController xac;

	public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
		xac = (XhibitApplicationController) getController();
		AddIndictmentCase addIndictment = new AddIndictmentCase(xac);
		if (addIndictment.getCaseId() != null) {
			if (addIndictment.getCaseId() > 0) { // returns 0 by default so just null check wasn't enough

				CaseBasicValue basic = XhibitDelegateHelper.getCaseDelegate().getCase(addIndictment.getCaseId());

				DefendantControllerBeanBusinessDelegate del = XhibitDelegateHelper.getDefendantDelegate();
				ArrayList<DefendantOnCaseBasicValue> arr = new ArrayList<DefendantOnCaseBasicValue>();
				arr = (ArrayList<DefendantOnCaseBasicValue>) del.findByCaseId(addIndictment.getCaseId());

				ApplicationCaseModel acm = populateApplicationCaseModel(true, basic, arr);
				acm.setXhibitApplicationController(xac);

				ChargesController cc = new ChargesController(acm, true);
				cc.setAccessedFromAddIndictmentCase(true);
				xac.setApplicationCaseModel(acm);
				xac.open(cc);
				xac.getCaseStatus().setCaseCreateInProgressFlag(true);
				xac.setCaseChargesDisposalsOpened(true);
			}
		}

	}

	private ApplicationCaseModel populateApplicationCaseModel(boolean overload, CaseBasicValue oldCBV,
			ArrayList<DefendantOnCaseBasicValue> colDefToAddToCase) {
		ApplicationCaseModel populatedACM = new ApplicationCaseModel();
		populatedACM.setScheduledHearingValue(populateScheduledHearingValue(true, oldCBV, colDefToAddToCase)); // calls other method
		populatedACM.setCaseId(oldCBV.getCaseId());
		populatedACM.setCaseNumber(oldCBV.getCaseNumber());
		populatedACM.setCaseSubType(oldCBV.getCaseSubType());
		populatedACM.setCaseType(oldCBV.getCaseType());
		populatedACM.setCaseTitle(oldCBV.getCaseTitle());
		populatedACM.setForAllDaysLogs(false);
		populatedACM.setForRangeLogs(false);
		populatedACM.setInEditMode(true);
		populatedACM.setIsLinked(false);
		populatedACM.setScheduledHearingDateFrom(Calendar.getInstance());
		populatedACM.setScheduledHearingDateFrom(Calendar.getInstance().getTime());
		populatedACM.setScheduledHearingDateTo(Calendar.getInstance());
		populatedACM.setScheduledHearingId(0); // never gets written to DB, so not necessary to be correct val
		populatedACM.setScheduledHearingDateTo(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		populatedACM.setXhibitApplicationController(xac);

		return populatedACM;
	}

	private ScheduledHearingValue populateScheduledHearingValue(boolean overload, CaseBasicValue oldCBV,
			ArrayList<DefendantOnCaseBasicValue> colDefToAddToCase) {
		ScheduledHearingValue populatedSHV = new ScheduledHearingValue();

		populatedSHV.setCaseBasicValue(oldCBV);
		populatedSHV.setCourtRoomValue(populateCourtRoomBasicValue(true));
		populatedSHV.setCourtSiteShortName("");
		populatedSHV.setCrestCourtId("");
		populatedSHV.setCurrentStatus("");
		populatedSHV.setCurrentStatusTime(Calendar.getInstance());
		populatedSHV.setDefendantOnCaseBasicValues(colDefToAddToCase);
		// populatedSHV.setDefendantsOnCase(colDefToAddToCase); // is this
		// necessary? would need iterator
		populatedSHV.setHearingListStartDate(Calendar.getInstance());
		populatedSHV.setId(0); // this is never written to DB at this stage, so
								// no need to worry about val (primary key)
		populatedSHV.setIsFloating(false);
		populatedSHV.setJudge("");
		// populatedSHV.setRefHearingTypeBasicValue(val); // is this necessary?
		populatedSHV.setScheduledHearingBasicValue(populateScheduledHearingBasicValue(true));
		populatedSHV.setSittingSequenceNo(0);
		populatedSHV.setVersion(0);

		return populatedSHV;
	}

	private ScheduledHearingBasicValue populateScheduledHearingBasicValue(boolean overload) {
		ScheduledHearingBasicValue populatedSHBV = new ScheduledHearingBasicValue();

		populatedSHBV.setAddHearingUsed("");
		populatedSHBV.setDateOfHearing(Calendar.getInstance().getTime());
		populatedSHBV.setEndTime(Calendar.getInstance().getTime());
		populatedSHBV.setHearingID(0);
		populatedSHBV.setHearingProgress(0);
		populatedSHBV.setId(0); // again, not written to db, no need for this (primary key)
		populatedSHBV.setIsCaseActive(true);
		populatedSHBV.setLinkedSHID(0);
		populatedSHBV.setListingNote("");
		populatedSHBV.setMovedFrom("");
		populatedSHBV.setMovedFromCourtRoomId(0);
		populatedSHBV.setNotBeforeTime(Calendar.getInstance().getTime());
		populatedSHBV.setOriginalTime(Calendar.getInstance().getTime());
		populatedSHBV.setSequenceNo(0);
		populatedSHBV.setSittingID(0);
		populatedSHBV.setStartTime(Calendar.getInstance().getTime());
		populatedSHBV.setVersion(0);

		return populatedSHBV;
	}

	private CourtRoomBasicValue populateCourtRoomBasicValue(boolean overload) {
		CourtRoomBasicValue populatedCRBV = new CourtRoomBasicValue();

		populatedCRBV.setCourtRoomName("Court N/A");
		populatedCRBV.setCourtSiteId(0);
		populatedCRBV.setCrestCourtRoomNo(0);
		populatedCRBV.setDescription("");
		populatedCRBV.setDisplayName("");
		populatedCRBV.setId(0); // not needed for db
		populatedCRBV.setLocation("");
		populatedCRBV.setVersion(0);

		return populatedCRBV;
	}
}
