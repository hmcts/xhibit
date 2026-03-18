package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.*;



/**
 * <p>
 * Title: DartsSwitchSenderControllerBeanWS
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This session bean is an extension of the DartsSenderControllerBeanBase, it only differs from 
 * DartsPrioritySenderControllerBean and DartsNormalSenderControllerBean as these beans 
 * have the potential to be either Priority (when there are priority messages to process) or Normal
 * when priority table is empty.
 * </p>
 * 
 * <p>
 * For more information see the superclass
 * </p>
 * 
 * @author Nia Walters
 * @version 1.1 20170208 
 * 
 * @ejb.bean name="DartsSwitchSenderWS" description="Darts Switch Sender bean WS" type="Stateless"
 *           view-type="remote" jndi-name="DartsSwitchSenderSessionHomeWS"
 *           local-jndi-name="DartsSwitchSenderSessionLocalHomeWS"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 */
public class WSDartsSwitchSenderControllerBean extends WSDartsSenderControllerBean implements SessionBean {

    private static final long serialVersionUID = 2L;
    private static final Logger log = CSServices.getLogger(WSDartsSwitchSenderControllerBean.class);

    
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
     * Method to return the correct DB connection, 
     * the database used is dependant on whether there are 
     * messages in the priority table.
     * 
     * @return CommonDartsNewMessageDB
     * @overide 
     */
    
    
    protected CommonDartsNewMessageDB getNewMessageDB(){
    	
        return DartsSwitchMessageDatabase.getInstance();

    }
    
}// end of Class

