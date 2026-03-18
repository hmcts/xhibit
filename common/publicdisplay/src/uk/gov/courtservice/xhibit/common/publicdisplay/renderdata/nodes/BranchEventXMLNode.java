package uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Object representing a Branch node of an xml document. This node will contain
 * other Branch and leaf nodes.
 * 
 * @author szfnvt
 * 
 */
public class BranchEventXMLNode extends EventXMLNode {
	
	static final long serialVersionUID = 7660114920405929035L;
	
    /**
     * Logger
     */
    private static final Logger _log = CSServices.getLogger(BranchEventXMLNode.class);
    
    private final Map map = new HashMap();

    /**
     * Constructor taking in name of node.
     * 
     * @param nameIn
     */
    public BranchEventXMLNode(String nameIn) {
        super(nameIn);
    }

    /**
     * Method returning a single value. This can be the first item in a list.
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        Object value = map.get(key);
        if (value instanceof List) {
            if (((List) value).size() > 0) {
                return ((List) value).get(0);
            } else {
                return null;
            }
        } else {
            return value;
        }
    }

    /**
     * method return a list containing one or more values.
     * 
     * @param key
     * @return
     */
    public List getList(String key) {
        Object value = map.get(key);
        if (_log.isDebugEnabled()){
            _log.debug(key + ": " + value);
        }
        if (value instanceof List) {
            return (List) value;
        } else if (value != null) {
            List list = new ArrayList(1);
            list.add(value);
            return list;
        } else {
            return null;
        }
    }

    /**
     * Add EventXMLNode to internal Map either as a single node or as part of a
     * list.
     * 
     * @param node
     */
    public void add(EventXMLNode node) {
        // Check if node already exists in
        Object oldNode = null;
        oldNode = map.get(node.getName());
        if (oldNode == null) {
            if (_log.isInfoEnabled()){
                _log.info("Another Value does not exist for key: " + node.getName());
            }
            map.put(node.getName(), node);
        } else if (oldNode instanceof List) {
            if (_log.isDebugEnabled()){
                _log.debug("Value of type List does exists for key: " + node.getName());
            }
            ((List) oldNode).add(node);
        } else {
            map.remove(node.getName());
            if (_log.isDebugEnabled()){
                _log.debug("Value of " + oldNode + " exists for key: " + node.getName());
                _log.debug("Old Node Removed!");
                _log.debug("Value of exists for key: " + node.getName());
            }
            List values = new ArrayList();
            values.add(oldNode);
            values.add(node);
            map.put(node.getName(), values);
            if (_log.isDebugEnabled()){
                _log.debug("Added old and new values to List and stored against key.");
            }
        }
    }

    /**
     * Returns map for branch containing branch and leaf nodes.
     * 
     * @return
     */
    public Map getMap() {
        return map;
    }

    protected void appendDebug(StringBuffer buffer, int depth) {
        appendIndent(buffer, depth);
        buffer.append(getName());
        buffer.append(":");

        List keyList = new ArrayList();
        keyList.addAll(map.keySet());
        Collections.sort(keyList);
        Iterator keys = keyList.iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            Object value = map.get(key);
            if (value instanceof List) {
                Iterator values = ((List) value).iterator();
                while (values.hasNext()) {
                    buffer.append(NL);
                    ((EventXMLNode) values.next()).appendDebug(buffer, depth + 1);
                }
            } else {
                buffer.append(NL);
                ((EventXMLNode) value).appendDebug(buffer, depth + 1);
            }
        }
    }

}
