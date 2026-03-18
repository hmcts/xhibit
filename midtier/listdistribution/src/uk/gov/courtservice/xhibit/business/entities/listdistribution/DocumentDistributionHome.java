package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface DocumentDistributionHome extends javax.ejb.EJBLocalHome {
    public DocumentDistribution create(String distributionType, String documentType, String mimeType, Integer courtId,
            Integer recipientId, Integer wllRecipientId, String usePrefDistType, String userDisplayName) throws CreateException;

    public DocumentDistribution findByPrimaryKey(Integer docDistributionId) throws FinderException;

    public Collection findByRecipientId(Integer recipientId) throws FinderException;

    public DocumentDistribution findByWllRecipientId(Integer wllRecipientId) throws FinderException;

    public DocumentDistribution findByRecipientIdAndDocumentType(Integer recipientId, String documentType)
            throws FinderException;
}