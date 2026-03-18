package uk.gov.courtservice.xhibit.common.results.vos;

// JDK
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * Title: SortedList
 * </p>
 * <p>
 * Description: A self sorting list.
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
public class SortedList extends ResultValue implements Serializable {
	
	static final long serialVersionUID = -2658988685010212040L;
	
    private final List list = new ArrayList();

    private final Comparator comparator;

    private boolean sorted = true;

    public SortedList() {
        this(null);
    }

    public SortedList(Comparator comparator) {
        this.comparator = comparator;
    }

    public void add(Object object) {
        if (object == null || (comparator == null && !(object instanceof Comparable))) {
            throw new IllegalArgumentException("object: " + null);
        }
        list.add(object);
        sorted = false;
    }

    public void set(int index, Object object) {
        if (object == null || (comparator == null && !(object instanceof Comparable))) {
            throw new IllegalArgumentException("object: " + null);
        }
        try {
            list.set(index, object);
            sorted = false;
        } catch (IndexOutOfBoundsException iobe) {
            throw new IllegalArgumentException("index: " + index);
        }
    }

    public int size() {
        return list.size();
    }

    public Object get(int index) {
        if (!sorted) {
            sort();
        }
        return list.get(index);
    }

    // Debug
    public void appendDebug(StringBuffer buffer, int indent) {
        int s = size();
        if (0 < s) {
            buffer.append("{");
            indent += 1;
            appendLine(buffer, indent);
            appendValue(buffer, indent, get(0));
            for (int i = 1; i < s; i++) {
                buffer.append(", ");
                appendLine(buffer, indent);
                appendValue(buffer, indent, get(i));
            }
            indent -= 1;
            appendLine(buffer, indent);
            buffer.append("}");
        } else {
            buffer.append("{}");
        }
    }

    private void sort() {
        if (comparator == null) {
            Collections.sort(list);
        } else {
            Collections.sort(list, comparator);
        }
        sorted = true;
    }

}
