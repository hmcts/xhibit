package uk.gov.courtservice.xhibit.business.admin.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogSummaryValue;
import uk.gov.courtservice.xhibit.business.services.migration.CmLogsControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.migration.MigrateCaseControllerBeanBusinessDelegate;


public class BulkUpdateHelper {
	
	private static final String STATUS_PENDING = "PENDING";
	
	private static final Logger log = CSServices.getLogger(BulkUpdateHelper.class);
	
	public List<String> validate(FileItem file, String userName) {		
		// 1) Check file is a valid CSV
		CmLogsControllerBeanBusinessDelegate cmLogsBusinessDelegate = CmLogsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
		FileValidatorHelper validator = new FileValidatorHelper(cmLogsBusinessDelegate);
		List<String> errors = new ArrayList<String>(); 
		String invalidCsvError = validator.validateIsCsv(file);
		
		if (invalidCsvError != null) {
			errors.add(invalidCsvError);
			return errors;
		}
		
		// 2) Check file hasn't already been uploaded and is either PENDING/SUCCEEDED/RUNNING
		String fileName = file.getName().replace(".csv", "");
		boolean fileExists = cmLogsBusinessDelegate.findByFileNameInActiveStatuses(fileName);
		
		if (fileExists) {
			errors.add("This file has already been uploaded"); 
			return errors;
		}
		
		// 3) Validate CSV rows
		FileProcessingResult result = validator.validateFile(file);
				
		if (result.getErrors() != null && !result.getErrors().isEmpty()) {
			return result.getErrors();
		}
		
		// 4) Save file to XHB_CM_LOGS and XHB_CLOB
		cmLogsBusinessDelegate.saveValidatedUpload(fileName, result.getContent(), (int)file.getSize(), result.getRowCount(), result.getChecksum(), userName);

		return errors;
	}
	
	public List<String> process(String userName) {
		List<String> processResponse = new ArrayList<String>();
		try {
			CmLogsControllerBeanBusinessDelegate cmLogsBusinessDelegate = 
					CmLogsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
			MigrateCaseControllerBeanBusinessDelegate migrateCaseControllerBeanBusinessDelegate = 
					MigrateCaseControllerBeanBusinessDelegate.DelegateFactory.getInstance();
			
			// 1) Find all XHB_CM_LOGS records that are set to PENDING
			Collection<CmLogSummaryValue> cmLogSummaryValues = cmLogsBusinessDelegate.findByProcessingStatus(STATUS_PENDING);
			FileProcessingHelper fileProcessingHelper = new FileProcessingHelper(cmLogsBusinessDelegate, migrateCaseControllerBeanBusinessDelegate);
			
			
			// 2) Loop through each one
			if (cmLogSummaryValues != null && !cmLogSummaryValues.isEmpty()) {
				for (CmLogSummaryValue cmLogSummaryValue : cmLogSummaryValues) {
					// 3) Check filename to see if the cases need to be migrated or unmigrated
					if (cmLogSummaryValue.getFileName().contains("unmigration")) {
						// Call processUnmigration
						log.debug("processUnmigration called");
						processResponse.add(fileProcessingHelper.processUnmigration(cmLogSummaryValue, userName));
					} else {
						// Call processMigration
						log.debug("processMigration called");
						processResponse.add(fileProcessingHelper.processMigration(cmLogSummaryValue, userName));
					}
				}
				processResponse.add("You can view the logs if you choose to by pressing the <b>View Logs</b> button");
			} else {
				processResponse.add("No pending files to process");
			}
		} catch (Exception e) {
			processResponse.add(e.getMessage());
		}
		return processResponse;
	}
	
	public List<CmLogSummaryValue> getLogs() {
		CmLogsControllerBeanBusinessDelegate cmLogsBusinessDelegate = CmLogsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
		return cmLogsBusinessDelegate.getAllLogs();
	}
	
	public CmLogSummaryValue getSelectedLog(int cmLogId) {
		CmLogsControllerBeanBusinessDelegate cmLogsBusinessDelegate = CmLogsControllerBeanBusinessDelegate.DelegateFactory.getInstance();
		return cmLogsBusinessDelegate.getLogSummary(cmLogId);
	}
}