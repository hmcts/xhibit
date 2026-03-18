package uk.gov.courtservice.xhibit.courtlog.async;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.OperationType;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.Subscriber;

/**
 * @author pznwc5
 * 
 * The class represents a unit of court log subscription work that needs to be
 * executed asynchronously. The class identifies the subscriber that needs to be
 * executed, the context in which the subscription is executed and the operation
 * that is executed.
 * 
 * The valid operations are,
 * 
 * <pre>
 *       PRE_CREATE
 *       POST_CREATE
 *       PRE_CREATE_LINKED
 *       POST_CREATE_LINKED
 *       PRE_UPDATE
 *       POST_UPDATE
 *       PRE_DELETE
 *       POST_DELETE
 * </pre>
 */
public class CourtLogWork implements Runnable {

    // Logger
    private static final Logger LOG = Logger.getLogger(CourtLogWork.class);

    // Subscriber
    private Subscriber subscriber;

    // Operation type
    private OperationType type;

    // Context
    private OperationContext context;

    /**
     * The constructor initializes the subscriber, context and operation type.
     * To avoid loitering references, the operation context is deep copied
     * (cloned).
     * 
     * @param subscriber
     *            The subscriber to execute
     * @param context
     *            The context in which the subscriber is executed
     * @param type
     *            The operation that is executed on the subscriber
     */
    public CourtLogWork(Subscriber subscriber, OperationContext context, OperationType type) {
        this.subscriber = subscriber;
        this.type = type;
        this.context = context.deepCopy();

    }

    /**
     * The method executes the requested operation on the requested subscriber
     * in the requested context. The method is executed by a thread from the
     * thread pool
     */
    public void run() {
        try {
            if (type == OperationType.PRE_CREATE)
                subscriber.doPreCreate(context);
            else if (type == OperationType.POST_CREATE)
                subscriber.doPostCreate(context);
            else if (type == OperationType.PRE_UPDATE)
                subscriber.doPreUpdate(context);
            else if (type == OperationType.POST_UPDATE)
                subscriber.doPostUpdate(context);
            else if (type == OperationType.PRE_DELETE)
                subscriber.doPreDelete(context);
            else if (type == OperationType.POST_DELETE)
                subscriber.doPostDelete(context);
            else if (type == OperationType.PRE_CREATE_LINKED)
                subscriber.doPreCreateLinked(context);
            else if (type == OperationType.POST_CREATE_LINKED)
                subscriber.doPostCreateLinked(context);
        } catch (Throwable th) {
            LOG.fatal(th.getMessage(), th);
        } finally {
            context = null;
        }

    }

    /**
     * Pretty print
     */
    public String toString() {
        return subscriber.toString() + "->" + type;
    }
}
