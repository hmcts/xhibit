package uk.gov.courtservice.xhibit.courtlog.subscriptions;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.courtlog.OperationContext;

/**
 * @author pznwc5
 * 
 * Test subscriber.
 */
public class TestSubscriber extends Subscriber {

    /** Logger */
    private static final Logger LOG = Logger.getLogger(TestSubscriber.class);

    public void preCreate(OperationContext context) {
        LOG.debug("Test pre create");
    }

    public void postCreate(OperationContext context) {
        LOG.debug("Test post create");
    }

    public void preDelete(OperationContext context) {
        LOG.debug("Test pre delete");
    }

    public void postDelete(OperationContext context) {
        LOG.debug("Test post delete");
    }

    public void preUpdate(OperationContext context) {
        LOG.debug("Test pre update");
    }

    public void postUpdate(OperationContext context) {
        LOG.debug("Test post update");
    }

}
