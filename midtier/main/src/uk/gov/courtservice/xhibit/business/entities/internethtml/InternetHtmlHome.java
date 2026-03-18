package uk.gov.courtservice.xhibit.business.entities.internethtml;

import java.sql.Timestamp;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface InternetHtmlHome extends javax.ejb.EJBLocalHome {
    public InternetHtml create(String status, Integer courtId, Long htmlBlobId, String userDisplayName) throws CreateException;

    public InternetHtml findByPrimaryKey(Integer listId) throws FinderException;

}