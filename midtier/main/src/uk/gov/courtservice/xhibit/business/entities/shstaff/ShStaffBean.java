package uk.gov.courtservice.xhibit.business.entities.shstaff;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ShStaffBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(java.lang.String staffRole, java.lang.String staffName, String userDisplayName) throws CreateException {

        setStaffRole(staffRole);
        setStaffName(staffName);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(java.lang.String staffRole, java.lang.String staffName, String userDisplayName) throws CreateException {
    }

    public abstract void setShStaffId(Integer shStaffId);

    public abstract void setStaffRole(java.lang.String staffRole);

    public abstract void setStaffName(java.lang.String staffName);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    // ------------------------------------------------------------------------------------------------

    public abstract Integer getShStaffId();

    public abstract java.lang.String getStaffRole();

    public abstract java.lang.String getStaffName();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();

    // ------------------------------CMR----------------------------------------------------------

    public abstract void setScheduledHearingAttendee(java.util.Collection scheduledHearingAttendee);

    public abstract java.util.Collection getScheduledHearingAttendee();

}