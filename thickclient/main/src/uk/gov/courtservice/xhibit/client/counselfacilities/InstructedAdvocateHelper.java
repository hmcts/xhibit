package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Instructed Advocate Helper.
 * </p>
 * <p>
 * Description: Utilities pertaining to instructed/substitute advocates.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */
public class InstructedAdvocateHelper {

	public static final int NUMBER_OF_POSTS = 3;

	public static final String SUBSTITUE_ADVOCATE_FLAG = "S";

	public static final String INSTRUCTED_ADVOCATE_FLAG = "I";

	public static final String UNAVAILABLE_FLAG = "U";

	public static final String WITHDRAWN_FLAG = "W";

	public static Vector<FindInstructedAdvocateTableRowModel> getInstructedAdvocateMatches(Integer defendantId,
			Integer caseId) throws CSRecoverableException {
		Vector<FindInstructedAdvocateTableRowModel> matches = new Vector<FindInstructedAdvocateTableRowModel>();

		RefAdvocateCriteria criteria = new RefAdvocateCriteria();
		criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
		criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
		criteria.setDefendantId(defendantId.toString());
		criteria.setCaseId(caseId.toString());

		try {
			Iterator iter = XhibitDelegateHelper.getBizRefDelegate().findAdvocates(criteria).iterator();

			while (iter.hasNext()) {
				// Only call accessor methods on the RefAdvocateComplexValue
				RefAdvocateComplexValue item = (RefAdvocateComplexValue) iter.next();

				FindInstructedAdvocateTableRowModel trm = new FindInstructedAdvocateTableRowModel();
				trm.setLegalRepId(item.getLegalRepId());
				trm.setTitle(item.getTitle());
				trm.setFirstName(item.getFirstName());
				trm.setSurname(item.getSurname());
				trm.setFullName(CounselFacilitiesHelper.getSurnameFirstName(item.getFirstName(), item.getSurname()));
				trm.setChambersName(item.getFirmName());
				trm.setAddressLine01(item.getAddress1());
				trm.setAddressLine02(item.getAddress2());
				trm.setTown(item.getTown());
				trm.setCounty(item.getCounty());
				trm.setPostCode(item.getPostcode());
				trm.setChambersId(item.getRefChamberId());
				trm.setCrestPostNumber(item.getCrestPostNumber());
				trm.setAvailable(item.getAvailable());
				trm.setDefenceCategory(item.getCrestAdvCategory());
				trm.setLegalRepType(CounselFacilitiesHelper.BARRADIO);

				matches.add(trm);
			}
		} catch (BisRefControllerException brce) {
			String msgStr = "The search for counsel failed";
			String msgKey = "gui.counselSignIn.search";
			CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, brce);
			throw (csre);
		}

		return matches;
	}

	public static void addNewInstructedAdvocate(Integer defendantId, Integer caseId,
			FindInstructedAdvocateTableRowModel trm) throws CSRecoverableException {
		addNewInstructedAdvocate(defendantId, caseId, trm.getLegalRepId(), trm.getDefenceCategory(), trm.getAvailable(),
				trm.getCrestPostNumber());
	}

	public static void addNewInstructedAdvocate(Integer defendantId, Integer caseId, Integer legalRepId,
			String defenceCategory, String available, Integer crestPostNumber) throws CSRecoverableException {
		try {

			CounselFacilitiesControllerBeanBusinessDelegate delegate = XhibitDelegateHelper
					.getCounselFacilitiesDelegate();

			delegate.addInstructedAdvocate(defendantId, caseId, legalRepId, defenceCategory, available, crestPostNumber,
					XhibitSingleton.getInstance().getUserSession()
							.getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

		} catch (CounselFacilitiesControllerException ex) {
			String msgStr = "The add new instructed advocate failed";
			String msgKey = "gui.counselSignIn.addNewInstructedAdvocate";
			CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, ex);
			throw (csre);
		}
	}

	public static void updateInstructedAdvocate(Integer defendantId, Integer caseId,
			FindInstructedAdvocateTableRowModel trm, String userDisplayName) throws CSRecoverableException {
		try {

			CounselFacilitiesControllerBeanBusinessDelegate delegate = XhibitDelegateHelper
					.getCounselFacilitiesDelegate();

			delegate.updateInstructedAdvocate(defendantId, caseId, trm.getLegalRepId(), trm.getDefenceCategory(),
					trm.getAvailable(), trm.getCrestPostNumber(), userDisplayName);

		} catch (CounselFacilitiesControllerException ex) {
			String msgStr = "The update instructed advocate failed";
			String msgKey = "gui.counselSignIn.updateInstructedAdvocate";
			CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, ex);
			throw (csre);
		}
	}

	public static void removeInstructedAdvocate(Integer defendantId, Integer caseId,
			FindInstructedAdvocateTableRowModel trm) throws CSRecoverableException {
		try {

			CounselFacilitiesControllerBeanBusinessDelegate delegate = XhibitDelegateHelper
					.getCounselFacilitiesDelegate();

			delegate.deleteInstructedAdvocate(defendantId, caseId, trm.getLegalRepId(), trm.getCrestPostNumber(),
					XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

		} catch (CounselFacilitiesControllerException ex) {
			String msgStr = "The remove instructed advocate failed";
			String msgKey = "gui.counselSignIn.deleteInstructedAdvocate";
			CSRecoverableException csre = new CSRecoverableException(msgKey, msgStr, ex);
			throw (csre);
		}
	}

	public static Vector<FindInstructedAdvocateTableRowModel> populateDisplayAdvocates(
			Collection<FindInstructedAdvocateTableRowModel> editedAdvocatesVec) {

		Vector<FindInstructedAdvocateTableRowModel> retVal = new Vector<FindInstructedAdvocateTableRowModel>();

		for (int i = 1; i <= editedAdvocatesVec.size() + 1; i++) {

			boolean foundPost = false;

			for (FindInstructedAdvocateTableRowModel trm : editedAdvocatesVec) {

				if (trm.getCrestPostNumber() != null && trm.getCrestPostNumber().intValue() == i && trm.isAvailable()) {
					foundPost = true;
					retVal.add(trm);
					break;
				}
			}

			if (!foundPost) {
				FindInstructedAdvocateTableRowModel trm = new FindInstructedAdvocateTableRowModel();

				trm.setCrestPostNumber(new Integer(i));
				retVal.add(trm);
			}
		}

		return retVal;
	}

	public static void mergeChanges(Vector<FindInstructedAdvocateTableRowModel> previousState,
			Vector<FindInstructedAdvocateTableRowModel> newState, Integer postToBeMerged) {

		// Additions and updates
		for (FindInstructedAdvocateTableRowModel trm : newState) {
			boolean found = false;
			final int previousStateSize = previousState.size();

			if (!trm.getCrestPostNumber().equals(postToBeMerged)) {
				continue;
			}

			for (int i = 0; i < previousStateSize; ++i) {
				FindInstructedAdvocateTableRowModel trm2 = previousState.elementAt(i);

				if (equal(trm, trm2)) {
					// Replace the old record with a possibly edited new one
					previousState.setElementAt(trm, i);
					found = true;
					break;
				}
			}

			if (!found) {
				// A new advocate has been added by the user
				previousState.add(trm);
			}
		}

		// Deletions
		Iterator<FindInstructedAdvocateTableRowModel> iter = previousState.iterator();
		while (iter.hasNext()) {
			FindInstructedAdvocateTableRowModel trm = iter.next();
			boolean found = false;

			if (!trm.getCrestPostNumber().equals(postToBeMerged)) {
				continue;
			}

			for (FindInstructedAdvocateTableRowModel trm2 : newState) {
				if (equal(trm, trm2)) {
					found = true;
					break;
				}
			}

			if (!found) {
				// The advocate has been deleted by the user
				iter.remove();
			}
		}
	}

	private static boolean edited_wrt(FindInstructedAdvocateTableRowModel trm1,
			FindInstructedAdvocateTableRowModel trm2) {

		final boolean edited = (trm1.isAvailable() != trm2.isAvailable())
				|| (trm1.getDefenceCategory() != null && trm2.getDefenceCategory() != null
						&& !trm1.getDefenceCategory().equals(trm2.getDefenceCategory()))
				|| (trm1.getDefenceCategory() != null && trm2.getDefenceCategory() == null)
				|| (trm1.getDefenceCategory() == null && trm2.getDefenceCategory() != null);

		return edited;
	}

	public static void saveEdits(Vector<FindInstructedAdvocateTableRowModel> editedAdvocatesVec,
			Vector<FindInstructedAdvocateTableRowModel> crestAdvocatesVec, Integer defendantId, Integer caseId,
			String userDisplayName)
			throws CSRecoverableException {

		// Additions and updates
		for (FindInstructedAdvocateTableRowModel editedItem : editedAdvocatesVec) {

			boolean found = false;

			for (FindInstructedAdvocateTableRowModel crestItem : crestAdvocatesVec) {

				if (equal(crestItem, editedItem)) {

					if (edited_wrt(crestItem, editedItem)) {

						updateInstructedAdvocate(defendantId, caseId, editedItem,
								userDisplayName);
					}

					found = true;
					break;
				}
			}

			if (!found) {

				InstructedAdvocateHelper.addNewInstructedAdvocate(defendantId, caseId, editedItem);
			}
		}

		// Deletions
		for (FindInstructedAdvocateTableRowModel crestItem : crestAdvocatesVec) {

			boolean found = false;

			for (FindInstructedAdvocateTableRowModel editedItem : editedAdvocatesVec) {

				if (equal(crestItem, editedItem)) {
					found = true;
					break;
				}
			}

			if (!found) {

				InstructedAdvocateHelper.removeInstructedAdvocate(defendantId, caseId, crestItem);
			}
		}
	}

	private static boolean equal(FindInstructedAdvocateTableRowModel trm1, FindInstructedAdvocateTableRowModel trm2) {
		return trm1.getLegalRepId().equals(trm2.getLegalRepId())
				&& trm1.getCrestPostNumber().equals(trm2.getCrestPostNumber());
	}
}
