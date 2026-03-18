package uk.gov.courtservice.xhibit.business.vos.messagebroker;

import java.io.Serializable;

import javax.jms.InvalidSelectorException;
import javax.jms.Message;

/**
 * <p>
 * Title: RuleVO
 * </p>
 * <p>
 * Description: A VO that describes a Rule. A Rule contains a Selector and a set
 * of Queues.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class RuleVO implements Serializable {
    /**
     * Used for creating Debug text
     */
    private static final String NL = System.getProperty("line.separator", "\n");

    private static final String TAB = "    ";    
    
    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;    
    
    private SelectorVO selector;

    private QueueVO[] queues;

    public RuleVO() {
    }

    public RuleVO(SelectorVO selectorVO, QueueVO[] queueVOs) {
        setSelector(selectorVO);
        setQueues(queueVOs);
    }

    public SelectorVO getSelector() {
        return selector;
    }

    public QueueVO[] getQueues() {
        return queues;
    }

    public void setSelector(SelectorVO param) {
        selector = param;
    }

    public void setQueues(QueueVO[] param) {
        queues = param;
    }

    public boolean selects(Message message) throws InvalidSelectorException {
        return selector.selects(message);
    }
    
    /**
     * Builds a comma seperated string of the JNDI queue names
     * 
     * @return A comma seperated string of the JNDI queue names
     */
    public String getQueueJndiNames() {
        StringBuffer buf = new StringBuffer();
        if(0 < queues.length) {
            buf.append(queues[0].getJndiName());
            for(int i = 1; i < queues.length; i++) {
                buf.append(", ");
                buf.append(queues[i].getJndiName());    
            }
        }
        return buf.toString();
    }

    /**
     * Return a debug string containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rule[selector=");
        builder.append(selector);
        builder.append(",queues=");
        builder.append(QueueVO.valueOf(queues));
        builder.append("]");
        return builder.toString();
    }
    
    /**
     * Return a debug string containing info about the rules
     */
    public static String valueOf(RuleVO[] rules) {
        if(rules == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (RuleVO rule : rules) {
            builder.append(NL);
            builder.append(TAB);
            builder.append(rule);
        }
        builder.append("}");
        return builder.toString();
    }
}
