package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.*;



/**
 * <p>
 * Title: DartsNormalSenderControllerBeanWS
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This session bean is an extension of the DartsSenderControllerBeanBase, it only differs from 
 * DartsPrioritySenderControllerBean in that the connection to the database 
 * is via DartsNewMessageDatabase which means it only services the normal priority 
 * messages.
 * </p>
 * 
 * <p>
 * For more information see the superclass
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20100611 
 * 
 * @ejb.bean name="DartsNormalSenderWS" description="Darts Normal Sender bean WS" type="Stateless"
 *           view-type="remote" jndi-name="DartsNormalSenderSessionHomeWS"
 *           local-jndi-name="DartsNormalSenderSessionLocalHomeWS"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 */
public class WSDartsNormalSenderControllerBean extends WSDartsSenderControllerBean implements SessionBean {

    private static final long serialVersionUID = 2L;
    private static final Logger log = CSServices.getLogger(WSDartsNormalSenderControllerBean.class);

    
    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException 
    {   
        super.ejbCreate();
        dartsNewMessageDatabase = getNewMessageDB();
    }    
    
    /** 
     * Method to return the correct DB connection, this is overidden in
     * this version of the bean to reference  the DAR_NEW_MESSAGES table.
     * 
     * @return CommonDartsNewMessageDB
     * @overide 
     */
    protected CommonDartsNewMessageDB getNewMessageDB(){
        return DartsNewMessageDatabase.getInstance();
    }
    
}// end of Class

