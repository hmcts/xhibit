package uk.gov.courtservice.xhibit.courtlog.subscriptions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogRuntimeException;

/**
 * The super class for subscribers interested in listening to court log entry
 * create, update and delete operations. The subscribers are notified before and
 * after the operations. The subscriber instances may be used concurrently used
 * by multiple request. Hence it is important to write the concrete sub classes
 * as thread safe if the instances store any state.
 * 
 * @author pznwc5
 * @version $Revision: 1.21 $
 */
public abstract class Subscriber implements Comparable {
    /** Logger */
    protected final Logger log = Logger.getLogger(getClass());

    /** Name of the subscriber */
    private String name;

    /** Subscriber type */
    private SubscriberType type;

    /** Subscriber type */
    private boolean async;

    /**
     * Asynchronous
     * 
     * @return
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Set the async name
     * 
     * @param name
     */
    public void setAsync(boolean async) {
        this.async = async;
    }

    /**
     * Name of the subscriber
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the subscriber name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Type of the subscriber
     * 
     * @return
     */
    public SubscriberType getType() {
        return type;
    }

    /**
     * Set the subscriber type
     * 
     * @param name
     */
    public void setType(SubscriberType type) {
        this.type = type;
    }

    /**
     * Pretty print
     */
    public String toString() {
        return getType() + "-" + getName() + "-" + getClass() + "-async:" + isAsync();
    }

    /**
     * Pre create
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPreCreate(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            preCreate(context);
            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Post create
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPostCreate(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            postCreate(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Pre create linked
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPreCreateLinked(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            preCreateLinked(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }
    
    /**
     * Pre update linked
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPreUpdateLinked(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            preUpdateLinked(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Post create linked
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPostCreateLinked(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            postCreateLinked(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }
    
    /**
     * Post update linked
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPostUpdateLinked(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            postUpdateLinked(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Pre update
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPreUpdate(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            preUpdate(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Post update
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPostUpdate(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            postUpdate(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Pre delete
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPreDelete(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            preDelete(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Post delete
     * 
     * @param context
     * @throws CourtLogBusinessException
     */
    public final void doPostDelete(OperationContext context) throws CourtLogBusinessException {
        // Start the transaction in asynchronous mode
        startTx();

        try {
            postDelete(context);

            // Commit the transaction in asynchronous mode
            commitTx();
        } catch (Throwable ex) {
            handleThrowable(ex);
        }
    }

    /**
     * Should be overridden by subscribers interested in pre create logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed pre create");
    }

    /**
     * Should be overridden by subscribers interested in post create logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void postCreate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed post create");
    }

    /**
     * Should be overridden by subscribers interested in pre create logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preCreateLinked(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed pre create linked");
    }
    
    /**
     * Should be overridden by subscribers interested in pre Update logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preUpdateLinked(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed pre update linked");
    }

    /**
     * Should be overridden by subscribers interested in post create logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void postCreateLinked(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed post create linked");
    }
    
    /**
     * Should be overridden by subscribers interested in post update logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void postUpdateLinked(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed post update linked");
    }

    /**
     * Should be overridden by subscribers interested in pre delete logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed pre delete");
    }

    /**
     * Should be overridden by subscribers interested in post delete logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void postDelete(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed post delete");
    }

    /**
     * Should be overridden by subscribers interested in pre update logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void preUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed pre update");
    }

    /**
     * Should be overridden by subscribers interested in post update logic
     * 
     * @param context
     *            Subscription context for shared state
     */
    public void postUpdate(OperationContext context) throws CourtLogBusinessException {
        log.debug("Stubbed post update");
    }

    private void startTx() {
        if (async) {
            try {
                CSServices.getServiceLocator().getUserTx().begin();
            } catch (Throwable e) {
                throw new CourtLogRuntimeException(e);
            }
        }
    }

    private void commitTx() {
        if (async) {
            try {
                CSServices.getServiceLocator().getUserTx().commit();
            } catch (Throwable e) {
                throw new CourtLogRuntimeException(e);
            }
        }
    }

    private void handleThrowable(Throwable ex) throws CourtLogBusinessException {

        // Rollback the transaction in asynchronous mode
        if (async) {
            rollbackTx();
        } else if (ex instanceof CourtLogBusinessException) {
            throw (CourtLogBusinessException) ex;
        } else if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
    }

    private void rollbackTx() {
        try {
            CSServices.getServiceLocator().getUserTx().rollback();
        } catch (Throwable e) {
            throw new CourtLogRuntimeException(e);
        }
    }

    /**
     * The code makes the subscibers sortable with asynchronous ones at the end
     */
    public int compareTo(Object o) {
        return isAsync() == ((Subscriber) o).isAsync() ? 0 : isAsync() ? 1 : -1;
    }
}
