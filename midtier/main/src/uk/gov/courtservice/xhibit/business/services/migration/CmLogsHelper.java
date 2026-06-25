package uk.gov.courtservice.xhibit.business.services.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.migration.CmLogs;
import uk.gov.courtservice.xhibit.business.entities.migration.CmLogsMaintainer;
import uk.gov.courtservice.xhibit.business.entities.migration.MigrateCase;
import uk.gov.courtservice.xhibit.business.entities.migration.MigrateCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClob;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.vos.entities.CmLogsBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.MigrateCaseBasicValue;

/**
 * <p>
 * Title: CmLogsHelper
 * </p>
 * <p>
 * Description: Set of methods interacting with cm logs in the DB
 * </p>
 * <p>
 * Copyright: Copyright (c) 2026
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Luke Gittins
 * @version 1.0
 */
public class CmLogsHelper {
	private static final String STATUS_PENDING = "PENDING";
	private static final String STATUS_RUNNING = "RUNNING";
	private static final String STATUS_SUCCEEDED = "SUCCESS";
	private static final String STATUS_FAILED = "FAILED";
	private static final String MAX_FILE_SIZE_MB = "CM_MAX_FILE_SIZE_MB";
	private static final String MAX_FILE_ROWS = "CM_MAX_FILE_ROWS";
	private static final String YES = "Y";

	private CmLogsMaintainer cmLogsMaintainer;
	private MigrateCaseMaintainer migrateCaseMaintainer;
	
	private static Logger log = CSServices.getLogger(CmLogsHelper.class);
	
	public CmLogsHelper() { 
		cmLogsMaintainer = new CmLogsMaintainer();
		migrateCaseMaintainer = new MigrateCaseMaintainer();
	}
	
	public boolean findByFileNameInActiveStatuses(String fileName) {
		return cmLogsMaintainer.hasExistingLogForFilename(fileName);
	}
	
	public void saveValidatedUpload(String fileName, String fileContents, int uploadSize, int noOfCasesInFile, String checkSum, String userDisplayName) {		
		// save uploaded file in CLOB
		XhbClobBasicValue clobValue = new XhbClobBasicValue();
		clobValue.setClobData(fileContents);
		XhbClobBasicValue createdClob = XhbClobBeanHelper2.create(clobValue);
		
		// create log file for CLOB 
		XhbClobBasicValue clobLogValue = new XhbClobBasicValue();
		clobLogValue.setClobData("-- validation succeeded.");
		XhbClobBasicValue createdLogClob = XhbClobBeanHelper2.create(clobLogValue);
		
		// create LOG entry to link to CLOBS and save
		CmLogsBasicValue cmLogsValue = new CmLogsBasicValue();
		cmLogsValue.setOriginalUploadedFilename(fileName);
		cmLogsValue.setOriginalUploadedFileClobId(createdClob.getClobId());
		cmLogsValue.setProcessingStatus(STATUS_PENDING);
		cmLogsValue.setProcessingDatetime(new java.util.Date());
		cmLogsValue.setLogsClobId(createdLogClob.getClobId());
		cmLogsValue.setFileChecksum(checkSum);
		cmLogsValue.setUploadSize(uploadSize);
		cmLogsValue.setNoOfCasesInFile(noOfCasesInFile);
		
		cmLogsMaintainer.create(cmLogsValue, userDisplayName);
	}
	
	public long getMaxFileSizeMb() {
		return getIntConfigValue(MAX_FILE_SIZE_MB, 10);
	}
	
	public int getMaxFileRows() {
		return getIntConfigValue(MAX_FILE_ROWS, 50000);
	}
	
	public List<CmLogSummaryValue> getAllLogs() {
		try {
			Collection logs = cmLogsMaintainer.findAll();
			List<CmLogSummaryValue> result = new ArrayList<CmLogSummaryValue>();
			
			for (Iterator it = logs.iterator(); it.hasNext();) {
				CmLogs log = (CmLogs) it.next();
				result.add(toSummaryValue(log));
			}
			
			return result;
		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}
	
	public CmLogSummaryValue getLogSummary(int cmLogsId) {
		try {
			CmLogs log = cmLogsMaintainer.findByPrimaryKey(cmLogsId);
			
			return toSummaryValue(log);
		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}
	
	public String getLogContents(int cmLogsId) {
		try {
			CmLogs log = cmLogsMaintainer.findByPrimaryKey(cmLogsId);
			
			if (log == null || log.getLogsClobId() == null) {
				return null;
			}
			
			XhbClobBasicValue clob = XhbClobBeanHelper2.findByPrimaryKeyValue(log.getLogsClobId());
			return clob.getClobData();
			
		} catch (Exception e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}
	
    private int getIntConfigValue(String propertyName, int defaultValue) {
    	XhbConfigPropBasicValue[] props = XhbConfigPropBeanHelper2.findByPropertyNameValue(propertyName);
    	
    	if (props != null && props.length > 0 && props[0].getPropertyValue() != null) {
    		return Integer.parseInt(props[0].getPropertyValue());
    	}
    	
    	return defaultValue;
    }

    private CmLogSummaryValue toSummaryValue(CmLogs log) {
    	return new CmLogSummaryValue(log.getCmLogsId(), log.getOriginalUploadedFilename(), log.getProcessingStatus(), log.getCreationDate(), log.getOriginalUploadedFileClobId(), log.getLogsClobId());
    }

	public Collection<CmLogSummaryValue> findByProcessingStatus(String processingStatus){
		try {
			Collection<CmLogs> cmLogs = cmLogsMaintainer.findByProcessingStatus(processingStatus);
			log.debug("CmLogs retrieved: " + cmLogs.size());
			if (cmLogs != null && !cmLogs.isEmpty()) {
				// Create a CmLogSummaryValue Collection
				Collection<CmLogSummaryValue> cmLogSummaryValues = new ArrayList();
				// Loop through and set each CmLogSummaryValue
				for (CmLogs cmLog : cmLogs) {
					CmLogSummaryValue cmLogSummaryValue = new CmLogSummaryValue(
							cmLog.getCmLogsId(),
							cmLog.getOriginalUploadedFilename(),
							cmLog.getProcessingStatus(),
							cmLog.getCreationDate(),
							cmLog.getOriginalUploadedFileClobId(),
							cmLog.getLogsClobId());
					// Add this to the collection
					cmLogSummaryValues.add(cmLogSummaryValue);
				}
				log.debug("CmLogSummaryValues to send for processing: " + cmLogSummaryValues.size());
				return cmLogSummaryValues;
			}
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}

	public Collection<ClobSummaryValue> findClobByClobId(Long clobId) {
		XhbClob clob = XhbClobBeanHelper2.findByPrimaryKey(clobId);
		if (clob != null) {
			log.debug("XhbClob found with Id: " + clob.getClobId());
			Collection<ClobSummaryValue> clobSummaryValues = new ArrayList();
			clobSummaryValues.add(new ClobSummaryValue(clob.getClobId(), clob.getClobData()));
			return clobSummaryValues;
		}
		return null;
	}
	
	public Integer findCourtByCrestCourtId(String crestCourtId) {
		XhbCourtBasicValue[] courts = XhbCourtBeanHelper2.findByCrestCourtIdValue(crestCourtId);
		if (courts != null) {
			log.debug("XhbCourtBasicValue found with CourtId: " + courts[0].getCourtId());
			return courts[0].getCourtId();
		}
		return null;
	}
	
	public Integer findCaseByCaseNumberTypeAndCourtId(Integer caseNumber, String caseType, Integer courtId) {
		XhbCase caze = XhbCaseBeanHelper2.findByNumberTypeAndCourt(caseNumber, caseType, courtId);
		if (caze != null) {
			log.debug("XhbCase found with CaseId: " + caze.getCaseId());
			return caze.getCaseId();
		}
		return null;
	}
	
	public boolean saveMigratedCase(Integer caseId, String migrationTo, String migrationToUrn, String userDisplayName) {
		// Create MigrateCase entry and save
		MigrateCaseBasicValue migrateCaseBasicValue = new MigrateCaseBasicValue();
		migrateCaseBasicValue.setCaseId(caseId);
		migrateCaseBasicValue.setMigrated(YES);
		migrateCaseBasicValue.setMigrationTo(migrationTo);
		migrateCaseBasicValue.setMigrationToUrn(migrationToUrn);
		migrateCaseBasicValue.setMigrationDate(new java.util.Date());
		
		MigrateCase migrateCase = (MigrateCase) migrateCaseMaintainer.create(migrateCaseBasicValue, userDisplayName);
		if (migrateCase != null) {
			return true;
		}
		return false;
	}
	
	public void updateCmLogsStatus(Integer cmLogsId, Boolean successfullyMigrated, String userDisplayName) {
		try {
			// First get the existing record
			CmLogs cmLogs = cmLogsMaintainer.findByPrimaryKey(cmLogsId);
			CmLogsBasicValue cmLogsBasicValue = cmLogsMaintainer.getBasicValue(cmLogs);
			cmLogsBasicValue.setVersion(cmLogs.getVersion());
			log.debug("Version on CMLogsBasicValue: " + cmLogsBasicValue.getVersion());
			// Set the new status and update
			if (successfullyMigrated) {
				cmLogsBasicValue.setProcessingStatus(STATUS_SUCCEEDED);
			} else {
				cmLogsBasicValue.setProcessingStatus(STATUS_FAILED);
			}
			cmLogsMaintainer.update(cmLogsBasicValue ,userDisplayName);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void updateLogsClob(Long logsClobId, String updatedClobData) {
		try {
			// First get the existing record
			XhbClobBasicValue xhbClobBasicValue = XhbClobBeanHelper2.findByPrimaryKeyValue(logsClobId);
			// Set updated clob data and update
			xhbClobBasicValue.setClobData(updatedClobData);
			XhbClobBeanHelper2.update(xhbClobBasicValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteMigrateCase(Integer caseId, String userDisplayName) {
		try {
			// First get the existing record
			Collection<MigrateCase> migrateCase = migrateCaseMaintainer.findByCaseId(caseId);
			log.debug("MigrateCases retrieved: " + migrateCase.size());
			if (migrateCase != null && !migrateCase.isEmpty()) {
				// Get the first record as this should only return 1 case
				MigrateCase mc = migrateCase.iterator().next();
				log.debug("MigrateCase to delete: " + mc.getMigrateCaseId() + ", Version: " + mc.getVersion());
				// Delete this record
				migrateCaseMaintainer.delete(mc.getMigrateCaseId(), mc.getVersion(), userDisplayName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
