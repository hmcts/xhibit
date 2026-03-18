package uk.gov.courtservice.xhibit.business.entities.linkedhearing;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface LinkedHearingHome extends javax.ejb.EJBLocalHome {
    public LinkedHearing create(String userDisplayName) throws CreateException;

    public LinkedHearing findByPrimaryKey(Integer linkedHearingId) throws FinderException;
}