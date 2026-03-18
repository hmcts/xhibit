package uk.gov.courtservice.xhibit.business.entities.refjudgeticket;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

 
public abstract class RefJudgeTicketBean extends CSEntityBean {
	
	private static final long serialVersionUID = 1L;

	public Integer ejbCreate(Integer judgeId, String ticketType, String userDisplayName, String obsInd, Integer courtId
			) throws CreateException {
		setJudgeId(judgeId);
		setTicketType(ticketType);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
		setCourtId(courtId);

		return null;
	}

	public void ejbPostCreate(Integer judgeId , String ticketType, String userDisplayName, String obsInd, Integer courtId
			) throws CreateException {
	}

	public abstract Integer getRefJudgeTicketId();

	public abstract void setRefJudgeTicketId(Integer refJudgeTicketId);

	public abstract Integer getJudgeId();

	public abstract void setJudgeId(Integer judgeId);
	
	public abstract String getTicketType();

	public abstract void setTicketType(String ticketType);
	
	public abstract Date getLastUpdateDate( );
	
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	
	public abstract Date getCreationDate( );
	
	public abstract void setCreationDate(Date creationDate );
	
	public abstract String getCreatedBy();
	    
	public abstract void setCreatedBy(String createdBy);

	public abstract String getLastUpdatedBy();
		
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	   
	public abstract Integer getVersion();
		
	public abstract void setVersion(Integer version);
	
	public abstract String getObsInd();

	public abstract void setObsInd(String obsInd);
	
	public abstract Integer getCourtId();

	public abstract void setCourtId(Integer courtId);
	

}
