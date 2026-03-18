package uk.gov.courtservice.framework.business.services;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Message driven bean template class. Removes the common code of all message
 * beans into a well-defined framework class.
 * 
 * @author tz0d5m
 * @version $Id: CSMessageBean.java,v 1.4 2006/06/05 12:30:14 bzjrnl Exp $
 */
public abstract class CSMessageBean implements MessageDrivenBean {
    /** The log4j <code>Logger</code> instance */
    protected final Logger log = CSServices.getLogger(getClass());

    private MessageDrivenContext messageDrivenContext = null;

    /**
     * Required by the EJB specification. Called upon message bean creation.
     * 
     * @throws CreateException
     *             Required as per the EJB spec, in case ant overriding classes
     *             require creation exceptions.
     */
    public void ejbCreate() throws CreateException {
        if (log.isDebugEnabled()) {
            log.debug("ejbCreate()");
        }
    }

    /**
     * Accessor method for the <code>MessageDrivenContext</code>.
     * 
     * @return The current <code>MessageDrivenContext</code>.
     */
    public MessageDrivenContext getMessageDrivenContext() {
        return this.messageDrivenContext;
    }

    /**
     * Sets the message context.
     * 
     * @param messageDrivenContext
     *            The <code>MessageDrivenContext</code>.
     * 
     * @see javax.ejb.MessageDrivenBean#setMessageDrivenContext
     *      (javax.ejb.MessageDrivenContext)
     */
    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        if (log.isDebugEnabled()) {
            log.debug("setMessageDrivenContext(..)");
        }

        this.messageDrivenContext = messageDrivenContext;
    }

    /**
     * Required by the EJB specification, this is a stubbed implementation that
     * does nothing but log access.
     * 
     * @see javax.ejb.MessageDrivenBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException {
        if (log.isDebugEnabled()) {
            log.debug("ejbRemove()");
        }
    }
}
