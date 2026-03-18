package uk.gov.courtservice.framework.business.services;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

/**
 * Session bean template class. Removes the common code of all session beans
 * into a well-defined framework class.
 * 
 * @author Unknown
 * @version $Id: CSSessionBean.java,v 1.5 2006/06/05 12:30:14 bzjrnl Exp $
 */
public abstract class CSSessionBean implements SessionBean {
    /** Logger instance */
    protected final Logger log = Logger.getLogger(getClass());

    /** The context */
    protected SessionContext ctx = null;

    /**
     * <p>
     * Required by the EJB specification, this is a stubbed implementation that
     * does nothing but log access.
     * </p>
     * 
     * <p>
     * Note that it is actually illegal for an EJB container to invoke this
     * method, but it is required that we implement it. Also, although no reason
     * to override in any subclass, cannot declare as final as this would
     * violate the EJB spec.
     * </p>
     * 
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException {
        // perhaps throw an IllegalAccessException instead?
        if (log.isDebugEnabled()) {
            log.debug("ejbActivate()");
        }
    }

    /**
     * <p>
     * Required by the EJB specification, this is a stubbed implementation that
     * does nothing but log access.
     * </p>
     * 
     * <p>
     * Note that it is actually illegal for an EJB container to invoke this
     * method, but it is required that we implement it. Also, although no reason
     * to override in any subclass, cannot declare as final as this would
     * violate the EJB spec.
     * </p>
     * 
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException {
        // perhaps throw an IllegalAccessException instead?
        if (log.isDebugEnabled()) {
            log.debug("ejbPassivate()");
        }
    }

    /**
     * Required by the EJB specification, this is a stubbed implementation that
     * does nothing but log access.
     * 
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException {
        if (log.isDebugEnabled()) {
            log.debug("ejbRemove()");
        }
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext ctx) throws EJBException {
        if (log.isDebugEnabled()) {
            log.debug("setSessionContext(SessionContext ctx)");
        }

        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        if (log.isDebugEnabled()) {
            log.debug("ejbCreate()");
        }
    }
}
