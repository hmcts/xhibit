package uk.gov.courtservice.xhibit.business.services.messagebroker;

import javax.jms.InvalidSelectorException;
import javax.jms.Message;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.RuleVO;

public class RulesManager {
    private static final Logger log = CSServices.getLogger(RulesManager.class);

    private final RuleVO[] rules;

    public RulesManager(RuleVO[] rules) {
        if(rules == null) {
            throw new IllegalArgumentException("rules: null");
        }
        this.rules = rules;
    }

    /**
     * Finds the Rule where the Selector matches the message header properties
     * 
     * @param message -
     *            the TextMessage to match
     * @return a RuleVO containing the SelectorVO and a set of QueueVOs
     *         representing the queues registered with it
     */
    public RuleVO findMatchingRule(Message message) throws InvalidSelectorException {
        for (RuleVO rule : rules) {
            if (rule.selects(message)) {
                if(log.isDebugEnabled()) {
                    log.debug("Matched " + rule + ".");    
                }                
                return rule;
            }
        }
        return null;
    }
}
