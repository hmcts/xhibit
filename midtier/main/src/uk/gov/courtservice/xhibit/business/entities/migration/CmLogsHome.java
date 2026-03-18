package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CmLogsHome extends javax.ejb.EJBLocalHome {
        
    public CmLogs create(Integer cmLogsId, String originalUploadedFilename, Long originalUploadedFileClobId,
			String processingStatus, Date processingDatetime, Long logsClobId, String fileChecksum, Integer uploadSize,
			Integer noOfCasesInFile, String userDisplayName) throws CreateException;

    public CmLogs findByPrimaryKey(Integer id) throws FinderException;
}