package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes;

import java.io.Serializable;

/**
 * Abstract class representing a value of a node of in processed xml document
 * 
 * @author szfnvt
 */
public abstract class EventXMLNode implements Serializable {
	
	static final long serialVersionUID = -6652192030869054552L;
	
    protected static final String NL = System.getProperty("line.seperator", "\n");

    protected static final String TAB = "    ";

    private final String name;

    /**
     * Constructor taking in node name. Called by child nodes.
     * 
     * @param nameIn
     */
    public EventXMLNode(String nameIn) {
        name = nameIn;
    }

    /**
     * Returns name of node.
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Append contents of node for debug.
     * 
     * @return
     */
    public String toDebug() {
        StringBuffer buffer = new StringBuffer();
        appendDebug(buffer, 0);
        return buffer.toString();
    }

    protected void appendDebug(StringBuffer buffer, int depth) {
    }

    protected void appendIndent(StringBuffer buffer, int depth) {
        for (int i = 0; i < depth; i++) {
            buffer.append(TAB);
        }
    }
}