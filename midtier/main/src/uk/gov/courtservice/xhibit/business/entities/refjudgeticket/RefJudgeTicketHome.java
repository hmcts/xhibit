package uk.gov.courtservice.xhibit.business.entities.refjudgeticket;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Local home interface for XhbRefJudgeTicket.

 * 
 * @author Jasvir Boparai
 *
 */
public interface RefJudgeTicketHome extends EJBLocalHome { 

    public RefJudgeTicket create(Integer judgeId, String ticketType, String userDisplayName, String obsInd, Integer courtId
    		) throws CreateException;
    
    public RefJudgeTicket findByPrimaryKey(Integer refJudgeTicketId) throws FinderException;

    public Collection findJudgeTicketsByJudgeId(Integer judgeId) throws FinderException;

    
}