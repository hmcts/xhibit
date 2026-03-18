package uk.gov.courtservice.xhibit.business.entities.shjudge;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ShJudgeBean extends CSEntityBean implements EntityBean {

    public Integer ejbCreate(java.lang.String deputyHcj, Integer refJudgeId, Integer shAttendeeId, String userDisplayName)
            throws CreateException {
        setDeputyHcj(deputyHcj);
        setRefJudgeId(refJudgeId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setShAttendeeId(shAttendeeId);
        return null;
    }

    public void ejbPostCreate(java.lang.String deputyHcj, Integer refJudgeId, Integer shAttendeeId, String userDisplayName)
            throws CreateException {

    }

    public abstract void setShJudgeId(Integer shJudgeId);

    public abstract void setDeputyHcj(java.lang.String deputyHcj);

    public abstract void setRefJudgeId(Integer refJudgeId);

    public abstract void setLastUpdatedBy(java.lang.String lastUpdatedBy);

    public abstract void setCreatedBy(java.lang.String createdBy);

    public abstract void setShAttendeeId(Integer shAttendeeId);

    // --------------------------------------------------------------------------------------------

    public abstract Integer getShJudgeId();

    public abstract java.lang.String getDeputyHcj();

    public abstract Integer getRefJudgeId();

    public abstract java.lang.String getLastUpdatedBy();

    public abstract java.lang.String getCreatedBy();

    public abstract Integer getShAttendeeId();

    // -------------------------CMR-----------------------------------------------------------------

}