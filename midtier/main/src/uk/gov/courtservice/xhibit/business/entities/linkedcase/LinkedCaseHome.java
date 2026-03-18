package uk.gov.courtservice.xhibit.business.entities.linkedcase;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface LinkedCaseHome extends javax.ejb.EJBLocalHome {
    public LinkedCase create(String userDisplayName) throws CreateException;

    public LinkedCase findByPrimaryKey(Integer linkedCaseId) throws FinderException;
}