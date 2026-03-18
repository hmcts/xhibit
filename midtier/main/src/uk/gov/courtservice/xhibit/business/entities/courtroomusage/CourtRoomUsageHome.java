package uk.gov.courtservice.xhibit.business.entities.courtroomusage;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface CourtRoomUsageHome extends javax.ejb.EJBLocalHome {
    
    public CourtRoomUsage create(Integer courtRoomId, Integer amHours,
			 Integer amMins, Integer pmHours,
			 Integer pmMins, Date sittingDate,
			 String userDisplayName) throws CreateException;
    
    public CourtRoomUsage findByPrimaryKey(Integer judgeUsageId) throws FinderException;
    public Collection findBySittingDateAndCourtRoom(Integer courtRoomId, Date sittingStartDate, Date sittingEndDate) throws FinderException;
}