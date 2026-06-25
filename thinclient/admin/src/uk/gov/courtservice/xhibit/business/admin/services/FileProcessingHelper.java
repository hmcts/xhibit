package uk.gov.courtservice.xhibit.business.admin.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.migration.ClobSummaryValue;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogSummaryValue;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.migration.MigrateCaseControllerBeanBusinessDelegate;

public class FileProcessingHelper {
	
	private static final Logger log = CSServices.getLogger(FileProcessingHelper.class);
	
	private CmLogsControllerBeanBusinessDelegate cmLogsControllerBeanBusinessDelegate;
	private MigrateCaseControllerBeanBusinessDelegate migrateCaseControllerBeanBusinessDelegate;
	
	public FileProcessingHelper(CmLogsControllerBeanBusinessDelegate cmLogsControllerBeanBusinessDelegate,
			MigrateCaseControllerBeanBusinessDelegate migrateCaseControllerBeanBusinessDelegate) {
		this.cmLogsControllerBeanBusinessDelegate = cmLogsControllerBeanBusinessDelegate;
		this.migrateCaseControllerBeanBusinessDelegate = migrateCaseControllerBeanBusinessDelegate;
	}
	
	public String processMigration(CmLogSummaryValue cmLogSummaryValue, String userName) {
		log.debug("entered processMigration(" + cmLogSummaryValue.getFileName() + ")");
		// 1) Get file contents from original_uploaded_file_clob_id
		ClobSummaryValue clobFileContents = 
				(ClobSummaryValue) cmLogsControllerBeanBusinessDelegate.findClobByClobId(cmLogSummaryValue.getOriginalUploadedFileClobId()).iterator().next();
		log.debug("File Contents: " + clobFileContents.getClobData());
		
		// 2) Get the log file contents from logs_clob_id
		ClobSummaryValue clobLogContents = 
				(ClobSummaryValue) cmLogsControllerBeanBusinessDelegate.findClobByClobId(cmLogSummaryValue.getLogsClobId()).iterator().next();
		String clobLogData = clobLogContents.getClobData();
		log.debug("Log Contents: " + clobLogData);
		
		String processResponse;
		
		// 3) Loop through all records
		try {
			BufferedReader reader = new BufferedReader(new StringReader(clobFileContents.getClobData()));
			
			String line;
			boolean isFirst = true;
			
			while ((line = reader.readLine()) != null) {
				// Skip Header line
				if (isFirst) {
					isFirst = false;
					continue;
				}
				// Get the data for this record ([0] CaseNumber, [1] CrestCourtId, [2] MigratedToLocation, [3] CommonPlatformUrn)
				line = line.replaceAll("\"", "");
				String[] record = line.split(",");
				
				// Assign record parts into variables
				String caseType = record[0].substring(0,1);
				Integer caseNumber = Integer.parseInt(record[0].substring(1));
				String migrationTo = record[2];
				String migrationToUrn = "";
				
				// Set the migrationToUrn field only if it exists
				if (record.length > 3) {
					migrationToUrn = record[3];
				}
				
				log.debug("Record Content: " + caseType + "," + caseNumber + "," + migrationTo + "," + migrationToUrn);
				
				// 4) Find court id from crest court id in csv
				Integer courtId;
				try {
					courtId = cmLogsControllerBeanBusinessDelegate.findCourtByCrestCourtId(record[1]);
				} catch (Exception e) {
					String errorMessage = "Failed to Migrate Case: " + caseType + caseNumber + " - courtId not found";
					log.debug(errorMessage);
					clobLogData +=  "\n-- " + errorMessage;
					continue;
				}
				log.debug("CourtId found: " + courtId);
				
				// 5) Find case id from court id, case type and case number
				Integer caseId;
				try {
					caseId = cmLogsControllerBeanBusinessDelegate.findCaseByCaseNumberTypeAndCourtId(caseNumber, caseType, courtId);
				} catch (Exception e) {
					String errorMessage = "Failed to Migrate Case: " + caseType + caseNumber + " - caseId not found";
					log.debug(errorMessage);
					clobLogData +=  "\n-- " + errorMessage;
					continue;
				}
				log.debug("CaseId found: " + caseId);
				
				// 6) Check if this caseId has already been migrated
				try {
					if (migrateCaseControllerBeanBusinessDelegate.isCaseMigrated(caseId)) {
						String errorMessage = "Case: " + caseType + caseNumber + " - is already migrated, skipping this record";
						log.debug(errorMessage);
						clobLogData +=  "\n-- " + errorMessage;
						continue;
					}
				} catch (Exception e) {
					String errorMessage = "Failed to check if case is already migrated: " + caseType + caseNumber;
					log.debug(errorMessage);
					clobLogData +=  "\n-- " + errorMessage;
					continue;
				}
				log.debug("CaseId: " + caseId + " not previously migrated, continuting processing");
				
				// 7) Insert record into XHB_MIGRATE_CASE with new migration record
				try {
					cmLogsControllerBeanBusinessDelegate.saveMigratedCase(caseId, migrationTo, migrationToUrn, userName);
					log.debug("Successfully Migrated Case: "  + caseType + caseNumber);
					clobLogData += "\n-- Successfully Migrated Case: " + caseType + caseNumber;
				} catch (Exception e) {
					log.debug("Failed to Migrate Case: "  + caseType + caseNumber + ", failed to insert record");
					clobLogData += "\n-- Failed to Migrate Case: " + caseType + caseNumber + ", failed to insert record";
					continue;
				}
			}
			
			try {
				// 8) Update Clob Logs record
				cmLogsControllerBeanBusinessDelegate.updateLogsClob(cmLogSummaryValue.getLogsClobId(), clobLogData);
				log.debug("Successfully Updated clob logs, with clobId: " + cmLogSummaryValue.getLogsClobId());
				
				// 9) Update CM Logs status
				cmLogsControllerBeanBusinessDelegate.updateCmLogsStatus(cmLogSummaryValue.getCmLogsId(), true, userName);
				log.debug("Successfully Updated cm Logs status, with cmLogsId: " + cmLogSummaryValue.getCmLogsId());
			} catch (Exception e) {
				log.debug("Failed to Update status (and or) clob logs for: " + cmLogSummaryValue.getFileName());
				e.printStackTrace();
			}
			
			processResponse = "The file: <b>" + cmLogSummaryValue.getFileName() + ".csv</b> has been successfully processed.";
			
		} catch (IOException e) {
			e.printStackTrace();
			// 10) If theres an error then set processing status to failed
			cmLogsControllerBeanBusinessDelegate.updateCmLogsStatus(cmLogSummaryValue.getCmLogsId(), false, userName);
			processResponse = "The file: " + cmLogSummaryValue.getFileName() + ".csv has failed to process.";
		}
		return processResponse;
	}
	
	public String processUnmigration (CmLogSummaryValue cmLogSummaryValue, String userName) {
		log.debug("entered processUnmigration(" + cmLogSummaryValue.getFileName() + ")");
		// 1) Get file contents from original_uploaded_file_clob_id
		ClobSummaryValue clobFileContents = 
				(ClobSummaryValue) cmLogsControllerBeanBusinessDelegate.findClobByClobId(cmLogSummaryValue.getOriginalUploadedFileClobId()).iterator().next();
		log.debug("File Contents: " + clobFileContents.getClobData());
		
		// 2) Get the log file contents from logs_clob_id
		ClobSummaryValue clobLogContents = 
				(ClobSummaryValue) cmLogsControllerBeanBusinessDelegate.findClobByClobId(cmLogSummaryValue.getLogsClobId()).iterator().next();
		String clobLogData = clobLogContents.getClobData();
		log.debug("Log Contents: " + clobLogData);
		
		String processResponse;
		
		// 3) Loop through all records
		try {
			BufferedReader reader = new BufferedReader(new StringReader(clobFileContents.getClobData()));
			
			String line;
			boolean isFirst = true;
			
			while ((line = reader.readLine()) != null) {
				// Skip Header line
				if (isFirst) {
					isFirst = false;
					continue;
				}
				// Get the data for this record ([0] CaseNumber, [1] CrestCourtId, [2] MigratedToLocation, [3] CommonPlatformUrn)
				line = line.replaceAll("\"", "");
				String[] record = line.split(",");
				
				// Assign record parts into variables (For unmigration we only need the CaseNumber[0] and CrestCourtId[1])
				String caseType = record[0].substring(0,1);
				Integer caseNumber = Integer.parseInt(record[0].substring(1));
				
				log.debug("Record Content: " + caseType + "," + caseNumber + ", " + record[1]);
				
				// 4) Find court id from crest court id in csv
				Integer courtId;
				try {
					courtId = cmLogsControllerBeanBusinessDelegate.findCourtByCrestCourtId(record[1]);
				} catch (Exception e) {
					String errorMessage = "Failed to Unmigrate Case: " + caseType + caseNumber + " - courtId not found";
					log.debug(errorMessage);
					clobLogData +=  "\n-- " + errorMessage;
					continue;
				}
				log.debug("CourtId found: " + courtId);
				
				// 5) Find case id from court id, case type and case number
				Integer caseId;
				try {
					caseId = cmLogsControllerBeanBusinessDelegate.findCaseByCaseNumberTypeAndCourtId(caseNumber, caseType, courtId);
				} catch (Exception e) {
					String errorMessage = "Failed to Unmigrate Case: " + caseType + caseNumber + " - caseId not found";
					log.debug(errorMessage);
					clobLogData +=  "\n-- " + errorMessage;
					continue;
				}
				log.debug("CaseId found: " + caseId);
				
				// 6) Delete record from XHB_MIGRATE_CASE
				try {
					cmLogsControllerBeanBusinessDelegate.deleteMigrateCase(caseId, userName);
					log.debug("Successfully Unmigrated Case: "  + caseType + caseNumber);
					clobLogData += "\n-- Successfully Unmigrated Case: " + caseType + caseNumber;
				} catch (Exception e) {
					log.debug("Failed to Unmigrate Case: "  + caseType + caseNumber + ", failed to delete record");
					clobLogData += "\n-- Failed to Unmigrate Case: " + caseType + caseNumber + ", failed to delete record";
					continue;
				}
			}
			
			try {
				// 8) Update Clob Logs record
				cmLogsControllerBeanBusinessDelegate.updateLogsClob(cmLogSummaryValue.getLogsClobId(), clobLogData);
				log.debug("Successfully Updated clob logs, with clobId: " + cmLogSummaryValue.getLogsClobId());
				
				// 9) Update CM Logs status
				cmLogsControllerBeanBusinessDelegate.updateCmLogsStatus(cmLogSummaryValue.getCmLogsId(), true, userName);
				log.debug("Successfully Updated cm Logs status, with cmLogsId: " + cmLogSummaryValue.getCmLogsId());
			} catch (Exception e) {
				log.debug("Failed to Update status (and or) clob logs for: " + cmLogSummaryValue.getFileName());
				e.printStackTrace();
			}
			
			processResponse = "The file: <b>" + cmLogSummaryValue.getFileName() + ".csv</b> has been successfully processed.";
			
		} catch (IOException e) {
			e.printStackTrace();
			// 10) If theres an error then set processing status to failed
			cmLogsControllerBeanBusinessDelegate.updateCmLogsStatus(cmLogSummaryValue.getCmLogsId(), false, userName);
			processResponse = "The file: " + cmLogSummaryValue.getFileName() + ".csv has failed to process.";
		}
		return processResponse;
	}
}
