package uk.gov.courtservice.xhibit.courtlog.subscriptions;

import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;
import uk.gov.courtservice.xhibit.courtlog.OperationType;
import uk.gov.courtservice.xhibit.courtlog.async.CourtLogWorkQueue;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.CreateHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.CreateHelperFactory;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.DeleteHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.UpdateHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.crud.UpdateHelperFactory;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * The class represents a chain of subscribers for a given court log entry
 * operation.
 * 
 * @author pznwc5
 * @version $Revision: 1.30 $
 */
public class SubscriberChain {
	/** Logger */
	private static final Logger LOG = Logger.getLogger(SubscriberChain.class);

	/** Iterator for the list of subscribers */
	private List subscribers;

	/** Current court log operation context */
	private OperationContext context;

	/**
	 * Package access conctructor stops instantiation outside the package
	 * 
	 * @param subscribers
	 *            The list of subscribers for the current court log operation
	 * @param context
	 *            Operation context for shared state between the subscribers
	 */
	SubscriberChain(List subscribers, OperationContext context) {
		this.subscribers = subscribers;
		this.context = context;
	}

	/**
	 * Processes the next subscriber in the chain
	 */
	public CourtLogViewValue[] processCreate() throws CourtLogBusinessException {
		performPreProcessing(OperationType.PRE_CREATE);

		// Create the entry
		CreateHelper createHelper = CreateHelperFactory
				.getCreateHelper(context);
		CourtLogViewValue[] newViewValues = createHelper.newEntry();
		context.setNewViewValues(newViewValues);

		performPostProcessing(OperationType.POST_CREATE);

		// Return the created entries
		return newViewValues;
	}

	/**
	 * Processes the next subscriber in the chain
	 */
	public CourtLogViewValue[] processCreateLinked()
			throws CourtLogBusinessException {
		performPreProcessing(OperationType.PRE_CREATE_LINKED);

		// Create the linked entries
		CreateHelper createHelper = CreateHelperFactory
				.getCreateHelper(context);
		CourtLogViewValue[] newViewValues = createHelper.newEntry();

		performPostProcessing(OperationType.POST_CREATE_LINKED);

		// Return the created entries
		return newViewValues;
	}

	/**
	 * Processes the linked subscriber in the chain
	 */
	public CourtLogViewValue[] processUpdateLinked()
			throws CourtLogBusinessException {
		performPreProcessing(OperationType.PRE_UPDATE_LINKED);

		// Create the linked entries
		UpdateHelper updateHelper = UpdateHelperFactory
				.getUpdateHelper(context);
		CourtLogViewValue[] newViewValues = updateHelper.updateEntry(context);
		context.setNewViewValues(newViewValues);
		performPostProcessing(OperationType.POST_UPDATE_LINKED);

		// Return the created entries
		return newViewValues;
	}

	/**
	 * Processes the next subscriber in the chain
	 */
	public CourtLogViewValue[] processUpdate() throws CourtLogBusinessException {
		LOG.debug("into processUpdate");
		performPreProcessing(OperationType.PRE_UPDATE);

		// Make the updated value available in the context for post operations
		UpdateHelper updateHelper = UpdateHelperFactory
				.getUpdateHelper(context);
		CourtLogViewValue[] newViewValues = updateHelper.updateEntry(context);
		context.setNewViewValues(newViewValues);

		performPostProcessing(OperationType.POST_UPDATE);

		// Return the updated entries
		return newViewValues;
	}

	/**
	 * Processes the next subscriber in the chain
	 */
	public void processDelete() throws CourtLogBusinessException {
		performPreProcessing(OperationType.PRE_DELETE);

		DeleteHelper.deleteEntry(context);

		performPostProcessing(OperationType.POST_DELETE);
	}

	private void performPreProcessing(OperationType operationType)
			throws CourtLogBusinessException {
		LOG.debug("into performPreProcessing");
		long now = 0L;
		final int size = subscribers.size();

		for (int i = 0; i < size; i++) {
			Subscriber sub = (Subscriber) subscribers.get(i);
			if (LOG.isDebugEnabled()) {
				now = System.currentTimeMillis();
				LOG.debug("-----------------> START " + operationType + " FOR "
						+ sub.getName());
			}

			if (sub.isAsync()) {
				// Pre processing not available for asynchronous subscribers
				LOG
						.debug("Skipping pre-processing for asynchronous subscriber");
			} else if (operationType == OperationType.PRE_CREATE) {
				sub.doPreCreate(context);
			} else if (operationType == OperationType.PRE_UPDATE) {
				sub.doPreUpdate(context);
			} else if (operationType == OperationType.PRE_DELETE) {
				sub.doPreDelete(context);
			} else if (operationType == OperationType.PRE_CREATE_LINKED) {
				sub.doPreCreateLinked(context);
			} else if (operationType == OperationType.PRE_UPDATE_LINKED) {
				sub.doPreUpdateLinked(context);
			}

			if (LOG.isDebugEnabled()) {
				final long duration = System.currentTimeMillis() - now;
				LOG.debug("-----------------> END " + operationType + " FOR "
						+ sub.getName() + ":" + duration + " milliseconds");
			}
		}
	}

	private void performPostProcessing(OperationType operationType)
			throws CourtLogBusinessException {
		long now = 0L;

		for (int i = subscribers.size() - 1; i >= 0; i--) {
			Subscriber sub = (Subscriber) subscribers.get(i);
			if (LOG.isDebugEnabled()) {
				now = System.currentTimeMillis();
				LOG.debug("-----------------> START " + operationType + " FOR "
						+ sub.getName());
			}

			if (sub.isAsync()) {
				CourtLogWorkQueue.getInstance().pushWork(sub, context,
						operationType);
			} else if (operationType == OperationType.POST_CREATE) {
				sub.doPostCreate(context);
			} else if (operationType == OperationType.POST_UPDATE) {
				sub.doPostUpdate(context);
			} else if (operationType == OperationType.POST_DELETE) {
				sub.doPostDelete(context);
			} else if (operationType == OperationType.POST_CREATE_LINKED) {
				sub.doPostCreateLinked(context);
			} else if (operationType == OperationType.POST_UPDATE_LINKED) {
				sub.doPostUpdateLinked(context);
			}

			if (LOG.isDebugEnabled()) {
				final long duration = System.currentTimeMillis() - now;
				LOG.debug("-----------------> END " + operationType + " FOR "
						+ sub.getName() + ":" + duration + " milliseconds");
			}
		}
	}
}
