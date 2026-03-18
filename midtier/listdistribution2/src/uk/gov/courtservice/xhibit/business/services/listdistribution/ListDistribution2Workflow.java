package uk.gov.courtservice.xhibit.business.services.listdistribution;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue;

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
 * <p>
 * Declared as package private so as to reduce the visibility of the class, and
 * to help prevent external classes using the methods that should only be called
 * from the session bean.
 * </p>
 * 
 * @author tz0d5m
 * @version $Id: ListDistribution2Workflow.java,v 1.9 2005/02/08 13:56:44 bzjrnl
 *          Exp $
 * 
 * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
 *      ListDistribution2ControllerBean
 */
final class ListDistribution2Workflow {
    /**
     * The <code>ListDistribution2ControllerLocal</code> local home interface
     */
    private static final ListDistribution2ControllerLocal listDistributionController = (ListDistribution2ControllerLocal) CSServices
            .getEJBServices().createLocalSession(ListDistribution2ControllerLocalHome.class);

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#getNextListId()
     */
    protected Integer getNextListId() {
        return listDistributionController.getNextListId();
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#processList(java.lang.Integer)
     */
    protected void processList(final Integer listId) {
        listDistributionController.processList(listId);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#updateListStatus( java.lang.Integer,
     *      boolean)
     */
    protected void updateListStatus(final Integer listId, final boolean success) {
        listDistributionController.updateListStatus(listId, success);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#setDeliveryMethodPersist(
     *      uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue[])
     */
    public WllRecipientComplexValue[] setDeliveryMethodPersist(WllRecipientComplexValue[] recipients) {
        return listDistributionController.setDeliveryMethodPersist(recipients);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#clearDeliveryMethodPersist(
     *      uk.gov.courtservice.xhibit.business.vos.listdistribution.WllRecipientComplexValue[])
     */
    public WllRecipientComplexValue[] clearDeliveryMethodPersist(WllRecipientComplexValue[] recipients) {
        return listDistributionController.clearDeliveryMethodPersist(recipients);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#printedListPersist(
     *      uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue)
     */
    public WllControlComplexValue printedListPersist(WllControlComplexValue value) {
        return listDistributionController.printedListPersist(value);
    }

    /**
     * @see uk.gov.courtservice.xhibit.business.services.listdistribution.
     *      ListDistribution2ControllerBean#printedListPersist(
     *      uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue[])
     */
    public WllControlComplexValue[] authorizeListsPersist(WllControlComplexValue[] values) {
        return listDistributionController.authorizeListsPersist(values);
    }

}
