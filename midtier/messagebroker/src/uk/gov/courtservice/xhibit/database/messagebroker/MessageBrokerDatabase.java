package uk.gov.courtservice.xhibit.database.messagebroker;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.QueueVO;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.RuleVO;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.SelectorVO;
import uk.gov.courtservice.xhibit.database.messagebroker.processor.QueueRowProcessor;
import uk.gov.courtservice.xhibit.database.messagebroker.processor.SelectorRowProcessor;

/**
 * A utility class used to extract all of the database actions performed by the
 * MessageBroker component in the Flow to ExISS sub-projectof RFC1492.
 *
 * @author szn20z
 */

public class MessageBrokerDatabase extends AbstractXhibitDatabase {

    private static final String GET_SELECTORS = "{ call xhb_message_broker_pkg.get_selectors(?) }";

    private static final String GET_QUEUES_BY_SELECTOR_ID = "{ call xhb_message_broker_pkg.get_queues_by_selector_id(?, ?) }";

    /**
     * Obtains the enabled rules from the Rules D/B
     */
    public RuleVO[] getRules() {
        // Get the selectors and sort by precedence
        SelectorVO[] selectors = getSelectors();
        Sorter.sort(selectors, new String[] { "precedence" });

        // For each selector construct a rule, retrieving the specified queues.
        RuleVO[] rules = new RuleVO[selectors.length];
        for (int i = 0; i < rules.length; i++) {
            rules[i] = new RuleVO(selectors[i], getQueuesBySelectorId(selectors[i].getId()));
        }

        if (log.isDebugEnabled()) {
            log.debug("Loaded " + rules.length + " Rules: " + RuleVO.valueOf(rules));
        } else {
            log.info("Foo Bar " + rules.length + " Rules!");
        }

        return rules;
    }

    /**
     * Get the list of selectors.
     *
     * @return A collection containing <code>SelectorVO</code> objects
     *         coresponding to all the selectors in the XHB rules data base
     */
    private SelectorVO[] getSelectors() {
        final SelectorRowProcessor rp = new SelectorRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_SELECTORS);
        sp.registerInTypes(new int[] {});
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {});
        return rp.getSelectors();
    }

    /**
     * Get the list of queues for a given selector.
     *
     * @param selectorId
     *            The ID of the selector to which the queues are linked in the
     *            rule
     *
     * @return A collection containing <code>QueueVO</code> objects
     *         coresponding to all the JNDI queues for a given selector as
     *         registered in the XHB rules data base
     */
    private QueueVO[] getQueuesBySelectorId(Integer selectorId) {
        final QueueRowProcessor rp = new QueueRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_QUEUES_BY_SELECTOR_ID);
        sp.registerInTypes(new int[] { Types.INTEGER });
        sp.setRowProcessor(rp);
        sp.execute(new Object[] { selectorId });
        return rp.getQueues();
    }
}
