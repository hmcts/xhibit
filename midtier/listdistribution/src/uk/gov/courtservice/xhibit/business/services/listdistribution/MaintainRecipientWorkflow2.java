package uk.gov.courtservice.xhibit.business.services.listdistribution;

import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;

/**
 * <p>
 * This workflow class is provided for the sole purpose of allowing the session
 * bean to call its own methods within different transactions. All methods have
 * exactly the same signatures as those defined for the methods defined as local
 * in the <code>ListDistribution2ControllerBean</code>. The local home is
 * acquired at object creation time, and used to call the methods on the session
 * bean (and thus allowing the containers EJB container to handle the
 * transactions).
 * </p>
 * 
 * @author Bal Bhamra
 * @version $Id: MaintainRecipientWorkflow2.java,v 1.1 2005/08/26 11:27:12
 *          szfnvt Exp $
 * 
 * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
 *      MaintainRecipientControllerBean
 */
final class MaintainRecipientWorkflow2 extends CSSessionBean implements SessionBean {
    /**
     * The <code>MaintainRecipientControllerLocal</code> local home interface
     */
    private static final MaintainRecipientControllerLocal maintainRecipientController = (MaintainRecipientControllerLocal) CSServices
            .getEJBServices().createLocalSession(MaintainRecipientControllerLocalHome.class);

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      MaintainRecipientControllerBean#updateRecipient(
     *      uk.gov.courtservice.xhibit.business.vos.entities.listdistribution.RecipientComplexValue)
     */
    public void updateRecipient(RecipientComplexValue recObject, String userDisplayName) throws MaintainRecipientException {
        maintainRecipientController.updateRecipientPersist(recObject, userDisplayName);
    }
}
