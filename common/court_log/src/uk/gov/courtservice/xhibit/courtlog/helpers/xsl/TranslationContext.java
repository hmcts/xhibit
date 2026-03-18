package uk.gov.courtservice.xhibit.courtlog.helpers.xsl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author pznwc5
 * 
 * Context for passing attributes
 */
public class TranslationContext {

    // Map of values
    private Map vals = new HashMap();

    /**
     * Gets a named attribute
     * 
     * @param name
     *            Name of the attribute
     * @return Value of the attribute
     */
    public Object get(String name) {
        return vals.get(name);
    }

    /**
     * Puts a named attribute
     * 
     * @param name
     *            Name of the attribute
     * @param val
     *            Value of the attribute
     */
    public void put(String name, Object val) {
        vals.put(name, val);
    }

    /**
     * Return a string representation of the translation context
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TranslationContext[");
        List keyList = new ArrayList();
        keyList.addAll(vals.keySet());
        Collections.sort(keyList);
        Iterator keys = keyList.iterator();
        if (keys.hasNext()) {
            Object key = keys.next();
            buffer.append(key);
            buffer.append("=");
            buffer.append(vals.get(key));
            while (keys.hasNext()) {
                key = keys.next();
                buffer.append(",");
                buffer.append(key);
                buffer.append("=");
                buffer.append(vals.get(key));
            }
        }
        buffer.append("]");
        return buffer.toString();
    }
}
