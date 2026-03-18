package uk.gov.courtservice.xhibit.business.entities.refjudgeticket;

import java.util.Date;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

/**
 * RefJudgeTicket main class.
 * 
 * @author Jasvir Boparai
 *
 */
public interface RefJudgeTicket extends CSEntityLocal {
	
    public Integer getRefJudgeTicketId();
    
    public void setRefJudgeTicketId(Integer refJudgeTicketId);

    public Integer getJudgeId();
    
    public void setJudgeId(Integer judgeId);
    
    public String getTicketType();
    
    public void setTicketType(String ticketType);

	public Date getLastUpdateDate();
	
	public void setLastUpdateDate(Date lastUpdateDate);
    
	public Date getCreationDate();
	
	public void setCreationDate(Date creationDate);
	
	public String getCreatedBy();
	
	public void setCreatedBy(String createdBy);
	
	public String getLastUpdatedBy();
	
	public void setLastUpdatedBy(String lastUpdatedBy);
	
	public Integer getVersion();
	
	public void setVersion(Integer version);
    
    public String getObsInd();
    
    public void setObsInd(String obsInd);
    
    public Integer getCourtId();
    
    public void setCourtId(Integer courtId);

}