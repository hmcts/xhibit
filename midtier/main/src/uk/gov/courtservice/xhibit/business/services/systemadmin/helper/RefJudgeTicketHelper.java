package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicket;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicketMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketComplexValue;

/**
 * This Helper channels all RefJudgeTicketHelper related queries.
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Jasvir Boparai
 * @version $Revision: 1.0 $
 */
public class RefJudgeTicketHelper extends AbstractHelper {
	
	private RefJudgeTicketMaintainer refJudgeTicketMaintainer = null;

    public RefJudgeTicketHelper() {
    	refJudgeTicketMaintainer = new RefJudgeTicketMaintainer();
    }
    
    @SuppressWarnings("unchecked")
	public Collection findJudgeTicketsByJudgeId(Integer judgeId) throws SysRefControllerException {
        final String METHOD_NAME = "::findJudgeTicketsByJudgeId ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = this.newCollection();
        try {
            Collection<RefJudgeTicket> locals = refJudgeTicketMaintainer.findJudgeTicketsByJudgeId(judgeId);
            Collection<RefJudgeTicketComplexValue> values = refJudgeTicketMaintainer.getComplexValues(locals);
            results.addAll(values);	
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw new EJBException(anException);
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
        
    }
 
}
