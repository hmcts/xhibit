package uk.gov.courtservice.xhibit.business.entities.shjudge;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ShJudgeHome extends javax.ejb.EJBLocalHome {
    public ShJudge create(String deputyHcj, Integer refJudgeId, Integer shAttendeeId, String userDisplayName) throws CreateException;

    public ShJudge findByPrimaryKey(Integer shJudgeId) throws FinderException;

    public ShJudge findByShAttendeeId(Integer shAttendeeId) throws FinderException;

}