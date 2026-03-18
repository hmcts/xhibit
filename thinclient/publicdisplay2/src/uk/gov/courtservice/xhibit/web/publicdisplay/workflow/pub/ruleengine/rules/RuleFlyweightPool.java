package uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.rules;

import java.util.HashMap;

import org.xml.sax.Attributes;

import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.ruleengine.exceptions.RulesConfigurationException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class RuleFlyweightPool {

    /**
     * Pool to store rules that have been loaded from the XML
     */
    private static final RuleFlyweightPool POOL = new RuleFlyweightPool();

    private static final String RULE_ID = "id";

    private static final String RULE_CLASS = "class";

    /**
     * Internal map to store rules by rule id
     */
    private HashMap ruleMap = new HashMap();

    /**
     * Returns a singleton instance of the pool
     * 
     * @return
     */
    public static RuleFlyweightPool getInstance() {
        return POOL;
    }

    private RuleFlyweightPool() {
    }

    /**
     * For the attributes passed in remove an instance of the rule and store in
     * the hashmap. This method will not check if the key is already used or if
     * the class has previosly been created. It will just replace it in the map.
     * 
     * @param attributes
     * @throws RulesConfigurationException
     */
    public void loadRule(Attributes attributes) throws RulesConfigurationException {
        // Get the rule id from the XML and check it has a value
        String ruleId = attributes.getValue(RULE_ID);
        if (ruleId == null || ruleId.trim().length() == 0) {
            throw new RulesConfigurationException("Invalid document structure. No rule Id specified");
        }

        // Get the class name and check it has a value
        String ruleClassName = attributes.getValue(RULE_CLASS);
        if (ruleClassName == null || ruleClassName.trim().length() == 0) {
            throw new RulesConfigurationException("Invalid document structure. No rule ClassName Id specified");
        }

        Rule rule;
        try {
            // Instantiate a rule
            rule = (Rule) Class.forName(ruleClassName).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new RulesConfigurationException("Rule Class not found for id:" + ruleId, ex);
        } catch (IllegalAccessException ex) {
            throw new RulesConfigurationException("Rule constructor not public for id:" + ruleId, ex);
        } catch (InstantiationException ex) {
            throw new RulesConfigurationException("Rule failed to instantiate for id:" + ruleId, ex);
        }
        // add to map
        ruleMap.put(ruleId, rule);
    }

    /**
     * Get the rule for the rule Id passed in.
     * 
     * @param ruleId
     * @return Rule
     * @throws RulesConfigurationException
     *             if the rule has not been declared.
     */
    public Rule getRule(String ruleId) throws RulesConfigurationException {
        Rule rule = (Rule) ruleMap.get(ruleId);
        if (rule == null)
            throw new RulesConfigurationException("Attempt to get a rule that has not been declared, rule id=" + ruleId);
        return rule;
    }
}