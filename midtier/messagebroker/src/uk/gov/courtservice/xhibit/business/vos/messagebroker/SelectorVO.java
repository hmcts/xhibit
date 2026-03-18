package uk.gov.courtservice.xhibit.business.vos.messagebroker;

import javax.jms.InvalidSelectorException;
import javax.jms.Message;

import org.exolab.jms.selector.Selector;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: SelectorVO </p>
 * <p>Description: A VO that describes a Selector.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Electronic Data Systems</p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class SelectorVO extends CSAbstractValue {
    
    private static final long serialVersionUID = 1L;
        
    private String selectorQuery;
    private String description;
    private String enabled;
    private Integer precedence;

    private transient Selector selector;
    
    /**
     * Empty constructor
     */
    public SelectorVO() { }

    public SelectorVO( Integer selectorId, String selectorQuery, String description, String enabled, Integer precedence ) {
        setId(selectorId);
        setSelectorQuery(selectorQuery);
        setDescription(description);
        setEnabled(enabled);
        setPrecedence(precedence);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Integer getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Integer precedence) {
        this.precedence = precedence;
    }

    public String getSelectorQuery() {
        return selectorQuery;
    }

    public void setSelectorQuery(String selectorQuery) {
        this.selectorQuery = selectorQuery; 
    }
    
    public boolean selects(Message message) throws InvalidSelectorException {        
        return getSelector().selects(message);
    }
        
    // ensures only one instance is created
    private synchronized Selector getSelector() throws InvalidSelectorException {
        if(selector == null) {
            selector = new Selector(selectorQuery);
        }
        return selector;
    }    

    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Selector[selector=");
        builder.append(selectorQuery);
//        builder.append(",description=");
//        builder.append(description);
        builder.append(",enabled=");
        builder.append(enabled);
        builder.append(",precedence=");
        builder.append(precedence);
        builder.append("]");
        return builder.toString();
    }
}
