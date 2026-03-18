package uk.gov.courtservice.xhibit.common.results.vos;

// JDK
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * Title: SortedList
 * </p>
 * <p>
 * Description: A map of self sorting list, this is optimized to reduce
 * serialization overhead for the case where there is only one item in the list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class SortedListMap extends ResultValue implements Serializable {
    
	static final long serialVersionUID = -7072958613867489195L;
	
	// Used for creating Debug text
    private static String NL = System.getProperty("line.separator", "\n");

    private static String TAB = System.getProperty("line.tab", "    ");

    private final Map map = new HashMap();

    private final Comparator comparator;

    public SortedListMap() {
        this(null);
    }

    public SortedListMap(Comparator comparator) {
        this.comparator = comparator;
    }

    public void add(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key: " + key);
        }
        if (value == null) {
            throw new IllegalArgumentException("value: " + value);
        }

        Object object = map.get(key);
        if (object instanceof SortedList) {
            ((SortedList) object).add(value);
        } else if (object != null) {
            SortedList list = new SortedList(comparator);
            list.add(object);
            list.add(value);
            map.put(key, list);
        } else {
            map.put(key, value);
        }
    }

    public void set(Object key, int index, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key: " + key);
        }
        if (value == null) {
            throw new IllegalArgumentException("value: " + value);
        }

        Object object = map.get(key);
        if (object instanceof SortedList) {
            ((SortedList) object).set(index, value);
        } else if (object != null) {
            if (index == 0) {
                map.put(key, value);
            } else {
                throw new IllegalArgumentException("index: " + index);
            }
        } else {
            throw new IllegalArgumentException("index: " + index);
        }
    }

    public void clear() {
        map.clear();
    }

    public int getCount(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("key: " + key);
        }

        Object object = map.get(key);
        if (object instanceof SortedList) {
            return ((SortedList) object).size();
        } else if (object != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public Object get(Object key, int index) {
        Object object = map.get(key);
        if (object instanceof SortedList) {
            try {
                return ((SortedList) object).get(index);
            } catch (IndexOutOfBoundsException iobe) {
                throw new IllegalArgumentException("index: " + index);
            }
        } else if (object != null) {
            if (index == 0) {
                return object;
            } else {
                throw new IllegalArgumentException("index: " + index);
            }
        } else {
            throw new IllegalArgumentException("key: " + key);
        }
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        Iterator keys = map.keySet().iterator();
        if (keys.hasNext()) {
            buffer.append("{");
            Object key = keys.next();
            indent += 1;
            appendLine(buffer, indent);
            buffer.append(key);
            buffer.append(":");
            appendValue(buffer, indent, map.get(key));
            while (keys.hasNext()) {
                key = keys.next();
                buffer.append(",");
                appendLine(buffer, indent);
                buffer.append(key);
                buffer.append(":");
                appendValue(buffer, indent, map.get(key));
            }
            indent -= 1;
            appendLine(buffer, indent);
            buffer.append("}");
        } else {
            buffer.append("{}");
        }
    }

    public Map getMap() {
    	return map;
    }
}
