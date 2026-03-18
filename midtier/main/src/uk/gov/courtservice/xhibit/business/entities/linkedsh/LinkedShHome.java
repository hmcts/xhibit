package uk.gov.courtservice.xhibit.business.entities.linkedsh;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface LinkedShHome extends javax.ejb.EJBLocalHome {

    public LinkedSh create(String userDisplayName) throws CreateException;

    public LinkedSh findByPrimaryKey(Integer linkedShId) throws FinderException;
}