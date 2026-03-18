package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.*;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.documentum.*;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.DARTSService.*;

import com.sun.xml.rpc.client.ClientTransportException;



/**
 * <p>
 * Title: DartsNormalSenderControllerBean
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
 * @ejb.bean name="DartsNormalSender" description="Darts Normal Sender bean" type="Stateless"
 *           view-type="remote" jndi-name="DartsNormalSenderSessionHome"
 *           local-jndi-name="DartsNormalSenderSessionLocalHome"
 * @ejb.interface extends="uk.gov.courtservice.framework.scheduler.RemoteTask,javax.ejb.EJBObject"
 * 
 */
public class DartsNormalSenderControllerBean extends DartsSenderControllerBean implements SessionBean {

    private static final long serialVersionUID = 2L;
    private static final Logger log = CSServices.getLogger(DartsNormalSenderControllerBean.class);

    
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

