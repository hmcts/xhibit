package uk.gov.courtservice.xhibit.business.services.joinder;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.JoinderIndictmentValue;

/**
 * <p>
 * Title: JoinderControllerBean
 * </p>
 * <p>
 * Description: Session Bean providing functionality for accessing and updating
 * case results
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @ejb.bean name="JoinderController" description="Joinder Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="JoinderControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @author Bal Bhamra, Will Fardell, Abdul Hussain
 * @version $Id: JoinderControllerBean.java,v 1.7 2014/06/20 16:51:14 atwells Exp $
 */
public class JoinderControllerBean extends CSSessionBean implements SessionBean {
    /**
     * Creates joinder indictment
     * 
     * @ejb.interface-method view-type="remote"
     * 
     * @param joinderIndictment
     * @param userDisplayName
     * @throws ChargeControllerException
     *             if an error occures
     */
    public void createJoinderIndictment(JoinderIndictmentValue joinderIndictment, String userDisplayName) throws ChargeControllerException {
        log.debug("START: createJoinderIndictment()");

        try {
            CreateJoinderHelper jHelper = new CreateJoinderHelper();
            jHelper.createJoinderIndictment(joinderIndictment, userDisplayName);
        } catch (ChargeControllerException ex) {
            ctx.setRollbackOnly();
            log.debug("createJoinderIndictment: Transaction ROLLBACK");
            throw ex;
        }

        log.debug("END: createJoinderIndictment()");
    }
}