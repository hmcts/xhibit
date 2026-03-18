package uk.gov.courtservice.xhibit.business.entities.listdistribution;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RecipientHome extends javax.ejb.EJBLocalHome {
    public Recipient create(String recipientName, String faxNumber, String emailAddress, Integer courtId,
            String prefDistributionType, String prefMimeType, String userDisplayName) throws CreateException;

    public Recipient findByPrimaryKey(Integer recipientId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}