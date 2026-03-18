package uk.gov.courtservice.xhibit.business.entities.ccinfo;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CcInfoHome extends javax.ejb.EJBLocalHome {
    public CcInfo create(String ccInfoText, String userDisplayName) throws CreateException;

    public CcInfo findByPrimaryKey(Integer ccInfoId) throws FinderException;

}