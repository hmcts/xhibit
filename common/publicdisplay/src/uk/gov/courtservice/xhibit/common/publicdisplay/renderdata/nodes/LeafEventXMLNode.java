package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Object representing a value of a leaf node of a processed xml document
 * 
 * @author szfnvt
 */
public class LeafEventXMLNode extends EventXMLNode {
	
	static final long serialVersionUID = 499986711326206014L;
	
	private static final Logger log = CSServices.getLogger(LeafEventXMLNode.class);
	
    private final String value;

    /**
     * Construter taking in Name and Value of node
     * 
     * @param nameIn
     * @param valueIn
     */
    public LeafEventXMLNode(String nameIn, String valueIn) {
        super(nameIn);
        value = valueIn;
    }

    /**
     * Retrieve leaf node value
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    protected void appendDebug(StringBuffer buffer, int depth) {
        appendIndent(buffer, depth);
        buffer.append(getName());
        buffer.append(": ");
        buffer.append(value);
    }

    /**
     * Get Day component of date
     * 
     * @return
     */
    public String getDay() {
    	String day = value.substring(0, indexOf("-"));
    	if ( day == null || "".equals(day.trim()) ) {
    		log.debug("Unable to retrieve day from date value: " + value);
    		day = "";
    	}
        return day;
    }

    /**
     * Get Month component of date
     * 
     * @return
     */
    public String getMonth() {
    	String month = value.substring(indexOf("-") + 1, lastIndexOf("-"));
    	if ( month == null || "".equals(month.trim()) ) {
    		log.debug("Unable to retrieve month from date value: " + value);
    		month = "";
    	}
        return month;
    }

    /**
     * Get Year component of date
     * 
     * @return
     */
    public String getYear() {
    	String year = value.substring(lastIndexOf("-") + 1, value.length());
    	if ( year == null || "".equals(year.trim()) ) {
    		log.debug("Unable to retrieve year from date value: " + value);
    		year = "";
    	}
        return year;
    }

    private int indexOf(String string) {
        return value.indexOf(string);
    }

    private int lastIndexOf(String string) {
        return value.lastIndexOf(string);
    }

}