package uk.gov.courtservice.xhibit.business.entities.judgeusage;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface JudgeUsageHome extends javax.ejb.EJBLocalHome {
    public JudgeUsage create(Integer courtRoomId, String courtChambersInd,
		Integer judgeId, Date sittingDate, String userDisplayName) throws CreateException;
    
    
    public JudgeUsage findByPrimaryKey(Integer judgeUsageId) throws FinderException;
    public Collection findBySittingDateAndCourtRoom(Integer courtRoomId, Date sittingStartDate, Date sittingEndDate) throws FinderException;
}