package uk.gov.courtservice.xhibit.business.entities.pdda;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface PddaBatchHome extends javax.ejb.EJBLocalHome {
    public PddaBatch create(Integer pddaBatchId, Integer noOfRecordsInBatch, 
			Date batchOpenedDatetime, Date batchClosedDatetime,
			Integer batchStatusId, String batchMessage, 
			Integer batchNoResends, Date batchSentTime,
			String obsInd, String userDisplayName) throws CreateException;

    public PddaBatch findByPrimaryKey(Integer id) throws FinderException;
    public Collection<PddaBatch> findOpenBatch(Integer refStatusCodesId) throws FinderException;
    public Collection<PddaBatch> findReadyToSendBatches(Integer refStatusCodesId) throws FinderException;
    public Collection<PddaBatch> findBatchesToResend(Integer maxResends, Integer refStatusCodesId) throws FinderException;
}