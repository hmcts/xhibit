package uk.gov.courtservice.xhibit.business.entities.shstaff;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ShStaffHome extends javax.ejb.EJBLocalHome {
    public ShStaff create(String staffRole, String staffName, String userDisplayName) throws CreateException;

    public ShStaff findByPrimaryKey(Integer shStaffId) throws FinderException;
}