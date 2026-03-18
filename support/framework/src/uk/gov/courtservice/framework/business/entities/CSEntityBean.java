package uk.gov.courtservice.framework.business.entities;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * CSEntityBean
 * </p>
 * <p>
 * Description: The superclass for all Exhibit entities. This class should be
 * subclassed by each entity.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymnd
 * @version 1.0
 * @history 19.02.2003 Ian Hannaford Removed setLastUpdated method from
 *          <code>ejbStore()</code> and added new method
 *          <code>public void setUpdated()</code>, as this is causing version
 *          number trigger to fire even on read mehtods.
 */
public abstract class CSEntityBean implements EntityBean {

    protected Logger log = CSServices.getLogger(getClass());

    protected EntityContext ctx;

    /**
     * Sets the EntityContext for the EJBean.
     * 
     * @param ctx
     *            EntityContext
     */
    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Unsets the EntityContext for the EJBean.
     * 
     */
    public void unsetEntityContext() {
        ctx = null;
    }

    /**
     * This method is required by the EJB Specification,
     * 
     */
    public void ejbActivate() {
    }

    /**
     * This method is required by the EJB Specification,
     * 
     */
    public void ejbPassivate() {
    }

    /**
     * 
     */
    public void ejbLoad() {
    }

    /**
     * 
     */
    public void ejbStore() {
    }

    /**
     * Called by the maintainers for a spcefic entity, when creating or
     * updating.
     */
    public void setUpdated(String userDisplayName) {
        setLastUpdatedBy(userDisplayName);
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     * 
     * @exception javax.ejb.RemoveException
     *                if the EJBean does not allow removing the EJBean
     */
    public void ejbRemove() throws RemoveException {
    }

    // -------------------CMP
    // Fields-----------------------------------------------
    public abstract void setCreatedBy(String createdBy);

    public abstract void setLastUpdatedBy(String lastUpdatedBy);

    public abstract void setVersion(Integer version);

    public abstract String getCreatedBy();

    public abstract String getLastUpdatedBy();

    public abstract Integer getVersion();

}
