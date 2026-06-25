package uk.gov.courtservice.xhibit.business.services.migration;


import java.util.List;

import java.util.Collection;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;

/**
 * <p>
 * Title: CmLogsControllerBean
 * </p>
 * <p>
 * Description: The Cm Logs Controller Bean
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
 * @ejb.bean name="CmLogsController" description="Cm Logs
 *           Controller Bean" type="Stateless" view-type="both"
 *           jndi-name="CmLogsControllerHome"
 *           local-jndi-name="CmLogsControllerLocalHome"
 * @ejb.transaction type="Required"
 */
public class CmLogsControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

    private CmLogsHelper cmLogsHelper = new CmLogsHelper();

    /**
     * Checks if there is an existing entry in the XHB_CM_LOGS table with the same file name.
     * Returns true if an entry exists with the same file name and the status is one of PENDING, RUNNING, SUCCESS,
     * 
     * @return a boolean.
     * @ejb.interface-method view-type="both"
     */
    public boolean findByFileNameInActiveStatuses(String fileName) {
    	return cmLogsHelper.findByFileNameInActiveStatuses(fileName);
    }
    
    /**
     * Saves a validated migrated csv file to the CLOB table with a LOG entry. Creates and entry in LOGS to link the two. 
     * 
     * @return void.
     * @ejb.interface-method view-type="both"
     */
    public void saveValidatedUpload(String fileName, String fileContents, int uploadSize, int noOfCasesInFile, String checkSum, String userDisplayName) {
    	cmLogsHelper.saveValidatedUpload(fileName, fileContents, uploadSize, noOfCasesInFile, checkSum, userDisplayName);
    }
    
    /**
     * Retrieves configured max file size from xhb_config_prop
     * 
     * @return long.
     * @ejb.interface-method view-type="both"
     */
    public long getMaxFileSizeMb() {
    	return cmLogsHelper.getMaxFileSizeMb();
    }
    
    /**
     * Retrieves configured max file rows from xhb_config_prop
     * 
     * @return int.
     * @ejb.interface-method view-type="both"
     */
    public int getMaxFileRows() {
    	return cmLogsHelper.getMaxFileRows();
    }
    
    /**
     * Retrieves all logs from xhb_cm_logs
     * 
     * @return int.
     * @ejb.interface-method view-type="both"
     */
	public List<CmLogSummaryValue> getAllLogs() {
		return cmLogsHelper.getAllLogs();
	}
	
    /**
     * Retrieves the data for one log in xhb_cm_logs
     * 
     * @return int.
     * @ejb.interface-method view-type="both"
     */
	public CmLogSummaryValue getLogSummary(int cmLogsId) {
		return cmLogsHelper.getLogSummary(cmLogsId);
	}
	
    /**
     * Retrieves the contents of a log from xhb_clob using the xhb_cm_logs log id.
     * 
     * @return int.
     * @ejb.interface-method view-type="both"
     */
	public String getLogContents(int cmLogsId) {
		return cmLogsHelper.getLogContents(cmLogsId);
	}

    /**
     * Retrieves Cm Logs with status set to the one passed in
     * 
     * @return Collection CmLogSummaryValue.
     * @ejb.interface-method view-type="both"
     */
    public Collection<CmLogSummaryValue> findByProcessingStatus(String processingStatus) {
    	return cmLogsHelper.findByProcessingStatus(processingStatus);
    }
    
    /**
     * Retrieves Clob from clobId
     * 
     * @return Collection ClobSummaryValue.
     * @ejb.interface-method view-type="both"
     */
    public Collection<ClobSummaryValue> findClobByClobId(Long clobId) {
    	return cmLogsHelper.findClobByClobId(clobId);
    }
    
    /**
     * Retrieves CourtId from Crest Court Id
     * 
     * @return Integer
     * @ejb.interface-method view-type="both"
     */
    public Integer findCourtByCrestCourtId(String crestCourtId) {
    	return cmLogsHelper.findCourtByCrestCourtId(crestCourtId);
    }
    
    /**
     * Retrieves CaseId from CaseNumber, CaseType and CourtId
     * 
     * @return Integer
     * @ejb.interface-method view-type="both"
     */
    public Integer findCaseByCaseNumberTypeAndCourtId(Integer caseNumber, String caseType, Integer courtId) {
    	return cmLogsHelper.findCaseByCaseNumberTypeAndCourtId(caseNumber, caseType, courtId);
    }
    
    /**
     * Saves a new migrate case record
     * 
     * @return boolean.
     * @ejb.interface-method view-type="both"
     */
    public boolean saveMigratedCase(Integer caseId, String migrationTo, String migrationToUrn, String userDisplayName) {
    	return cmLogsHelper.saveMigratedCase(caseId, migrationTo, migrationToUrn, userDisplayName);
    }
    
    /**
     * Updates CmLogs status
     * 
     * @return void.
     * @ejb.interface-method view-type="both"
     */
    public void updateCmLogsStatus(Integer cmLogsId, boolean successfullyMigrated, String userDisplayName) {
    	cmLogsHelper.updateCmLogsStatus(cmLogsId, successfullyMigrated, userDisplayName);
    }
    
    /**
     * Updates Clob logs
     * 
     * @return void.
     * @ejb.interface-method view-type="both"
     */
    public void updateLogsClob(Long logsClobId, String updatedClobData) {
    	cmLogsHelper.updateLogsClob(logsClobId, updatedClobData);
    }
    
    /**
     * Delete MigrateCase record
     * 
     * @return void.
     * @ejb.interface-method view-type="both"
     */
    public void deleteMigrateCase(Integer caseId, String userDisplayName) {
    	cmLogsHelper.deleteMigrateCase(caseId, userDisplayName);
    }
}