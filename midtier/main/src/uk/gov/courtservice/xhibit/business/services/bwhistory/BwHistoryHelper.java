package uk.gov.courtservice.xhibit.business.services.bwhistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.bwhistory.BwHistory;
import uk.gov.courtservice.xhibit.business.entities.bwhistory.BwHistoryHome;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecord;
import uk.gov.courtservice.xhibit.business.entities.defhearingrecord.DefHearingRecordMaintainer;
import uk.gov.courtservice.xhibit.business.vos.services.bwhistory.BwHistoryValue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;

public class BwHistoryHelper {
	private static final Logger LOG = CSServices.getLogger(BwHistoryHelper.class);

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public BwHistoryHelper() {

	}

	@SuppressWarnings("unchecked")
	public Collection findByDefendantOnCaseId(Integer defOnCaseId)
			throws FinderException {
		BwHistoryHome bwH = (BwHistoryHome) CSServices.getServiceLocator()
				.getLocalHome(BwHistoryHome.class);
		Collection bwCol = null;
		ArrayList returnBwHistoryValues = new ArrayList();
		
		try {
			bwCol = bwH.findByDefendantOnCaseId(defOnCaseId);
		} catch (FinderException e) {
			throw new FinderException("BwHistory: " + defOnCaseId + " not found");
		}
		
		Iterator bwColIterator = bwCol.iterator();
        while (bwColIterator.hasNext()) {	
			BwHistoryValue bV = new BwHistoryValue();
			BwHistory bw = (BwHistory) bwColIterator.next();
			
			if (bw != null) {
				if(bw.getBwHistoryId() != null) {
					bV.setBwHistoryId(bw.getBwHistoryId());
				}
				if(bw.getAbsconding() != null) {
					bV.setAbsconding(bw.getAbsconding());
				}
				if(bw.getBcStatusBwEnded() != null) {
					bV.setBcStatusBwEnded(bw.getBcStatusBwEnded());
				}
				if(bw.getBcStatusBwIssued() != null) {
					bV.setBcStatusBwIssued(bw.getBcStatusBwIssued());
				}
				if(bw.getBwEndDate() != null) {
					bV.setBwEndDate(bw.getBwEndDate());
				}
				if(bw.getBwIssueDate() != null) {
					bV.setBwIssueDate(bw.getBwIssueDate());
				}
				if(bw.getDefendantOnCaseId() != null) {
					bV.setDefendantOnCaseId(bw.getDefendantOnCaseId());
				}
				if(bw.getObsInd() != null) {
					bV.setObsInd(bw.getObsInd());
				}
				if(bw.getVersion() != null) {
					bV.setVersion(bw.getVersion());
				}
				if(bw.getWithdrawn() != null) {
					bV.setWithdrawn(bw.getWithdrawn());
				}
				
				returnBwHistoryValues.add(bV);
			}
		}
		return returnBwHistoryValues;
	}
	
	@SuppressWarnings("unchecked")
	public boolean updateBwHistory(BwHistoryValue bwValue, String userDisplayName) throws BwHistoryControllerException {
		try {
			BwHistoryHome bwHome = (BwHistoryHome) CSServices.getServiceLocator().getLocalHome(BwHistoryHome.class);
			BwHistory bw = bwHome.findByPrimaryKey(bwValue.getBwHistoryId());
			if(!bw.getVersion().equals(bwValue.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				/*
				 * ctx-1388 kudzinc find xhb_def_heraing, use end_bail_status if
				 * not null to update bc_status_bw_ended
				 */
				DefHearingRecordMaintainer defMaintainer = new DefHearingRecordMaintainer();
				ArrayList<DefHearingRecord> arr = (ArrayList<DefHearingRecord>) defMaintainer
						.findByDefendantOnCaseID(bw.getDefendantOnCaseId());
				if (arr != null) {
					for (int i = 0; i < arr.size(); i++) {
						bwValue.setBcStatusBwEnded(arr.get(i).getEndBailStatus());
					}
				}
				
				if(bwValue.getAbsconding() != null) {
					bw.setAbsconding(bwValue.getAbsconding());
				}
				if(bwValue.getBcStatusBwEnded() != null) {
					bw.setBcStatusBwEnded(bwValue.getBcStatusBwEnded());
				}
				if(bwValue.getBcStatusBwIssued() != null) {
					bw.setBcStatusBwIssued(bwValue.getBcStatusBwIssued());
				}
				if(bwValue.getBwEndDate() != null) {
					bw.setBwEndDate(bwValue.getBwEndDate());
				}
				if(bwValue.getBwIssueDate() != null) {
					bw.setBwIssueDate(bwValue.getBwIssueDate());
				}				
				if(bwValue.getDefendantOnCaseId() != null) {
					final DefendantOnCase def = DefendantOnCaseMaintainer.getInstance()
							.findByPrimaryKey(bwValue.getDefendantOnCaseId());
					bw.setDefendantOnCase(def);
				}
				if(bwValue.getObsInd() != null) {
					bw.setObsInd(bwValue.getObsInd());
				}
				if(bwValue.getWithdrawn() != null) {
					bw.setWithdrawn(bwValue.getWithdrawn());
				}
				LOG.debug("BCStatusBWIssued is >>>>>>>>>>>>>>" + bw.getBcStatusBwIssued());
				bw.setUpdated(userDisplayName);
				
				return true;
			}
		} catch (FinderException e) {
			throw new BwHistoryControllerException("bwhistorycontroller.notfound",
					"BwHistory " + bwValue.getId() + " not found", e);
		}
	}
	
	/**
	 * Update the bench warrant history table
	 * 
	 * @param Integer 
	 *            defendantOnCaseID
	 * @param String
	 *            status
	 * @param String 
	 *            userDisplayName
	 * @throws BwHistoryControllerException
	 */
	@SuppressWarnings("unchecked")
	public void updateBWHistoryStatusEnded(Integer defendantOnCaseID, String statusBwEnded, String userDisplayName) 
			throws BwHistoryControllerException {
		if (statusBwEnded != null) {
			try {
				Collection<BwHistoryValue> bwHistoryValues;	
				try {
					bwHistoryValues = findByDefendantOnCaseId(defendantOnCaseID);
				} catch (FinderException ex) {
					bwHistoryValues = null;
				}
				if (bwHistoryValues != null && !bwHistoryValues.isEmpty()) {
					for (BwHistoryValue bwHistoryValue : bwHistoryValues ) {
						if (bwHistoryValue.getBwEndDate() != null &&
								bwHistoryValue.getBcStatusBwEnded() == null) {
							bwHistoryValue.setBcStatusBwEnded(statusBwEnded);
							updateBwHistory(bwHistoryValue, userDisplayName);
						}
					}
				}
			} catch (BwHistoryControllerException ex) {
				throw ex;
			}
		}
	}

	/**
	 * Delete the court log entry and update the bench warrant history
	 * 
	 * @param Long 
	 *            logEntryId
	 * @param Integer 
	 *            defendantOnCaseID
	 * @param Boolean 
	 *            isEndWarrantEvent
	 * @param String 
	 *            userDisplayName
	 * @throws BwHistoryControllerException
	 */	
	public void deleteCourtLogEntry(final Long logEntryId, final Integer defendantOnCaseId, final Boolean isEndWarrantEvent,  
			final String userDisplayName) throws BwHistoryControllerException {
		LOG.debug("deleteCourtLogEntry:" + logEntryId);
		try {
			// Get the latest Bench Warrant record
			BwHistoryValue bwHistoryValue = getLatestBenchWarrant(defendantOnCaseId);
			
			if (bwHistoryValue != null) {
				// if we are deleting an end warrant and the current warrant has ended... 
				if (isEndWarrantEvent && bwHistoryValue.getBwEndDate() != null) {
					// ...Remove the End date and the end status for the bench warrant
					reissueBenchWarrant(bwHistoryValue, userDisplayName);
				}
			}

			// Delete the court log entry
			CourtLogWorkFlow.deleteEntry(logEntryId, false);
			
		} catch (CourtLogBusinessException ex) {
			throw new BwHistoryControllerException("CourtLogBusinessException","Error deleting court log", ex);
		}	
	}

	/**
	 * Update the bench warrant to clear the end status and end date
	 * 
	 * @param BwHistoryValue 
	 *            bwHistoryValue
	 * @param String 
	 *            userDisplayName
	 * @throws BwHistoryControllerException
	 */	
	private void reissueBenchWarrant(BwHistoryValue bwHistoryValue, String userDisplayName)
			throws BwHistoryControllerException {
		LOG.debug("reissueBenchWarrant");		
		if (bwHistoryValue != null && bwHistoryValue.getBwEndDate() != null) {
			try {
				BwHistoryHome bwHome = (BwHistoryHome) CSServices.getServiceLocator().getLocalHome(BwHistoryHome.class);
				BwHistory bw = bwHome.findByPrimaryKey(bwHistoryValue.getBwHistoryId());
				if(!bw.getVersion().equals(bwHistoryValue.getVersion())) {
					throw new OptimisticLockException("Optimistic Lock Error");
				}
				
				bw.setBcStatusBwEnded(null);
				bw.setBwEndDate(null);
				bw.setWithdrawn(null);
				bw.setAbsconding(null);
				bw.setUpdated(userDisplayName);
				
			} catch (FinderException e) {
				throw new BwHistoryControllerException("bwhistorycontroller.notfound",
						"BwHistory " + bwHistoryValue.getId() + " not found", e);
			}	
		}
	}
	
	/**
	 * Get the latest bench warrant for the defendant on case  
	 * 
	 * @param Integer 
	 *            defendantOnCaseId
	 */	
	@SuppressWarnings("unchecked")
	private BwHistoryValue getLatestBenchWarrant(Integer defendantOnCaseId) {
		LOG.debug("getLatestBenchWarrant");
		List<BwHistoryValue> bwHistoryValues;
		try {
			bwHistoryValues = (List<BwHistoryValue>) findByDefendantOnCaseId(defendantOnCaseId);
			Collections.sort(bwHistoryValues, BwHistoryValue.getSortByIssueDateDESC());
		} catch (FinderException ex) {
			bwHistoryValues = null;
		}
		return bwHistoryValues != null && !bwHistoryValues.isEmpty() ? bwHistoryValues.get(0) : null;
	}
}