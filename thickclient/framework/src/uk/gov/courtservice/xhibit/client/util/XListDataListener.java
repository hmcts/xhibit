package uk.gov.courtservice.xhibit.client.util;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XListDataListener implements ListDataListener {

    private boolean xIsChanged = false;

    public boolean xIsChanged() {
        return this.xIsChanged;
    }

    public void setChanged(boolean flag) {
        this.xIsChanged = flag;
    }

    public void contentsChanged(ListDataEvent e) {
        this.xIsChanged = true;
        // Sent when the contents of the list has changed in a way that's too
        // complex to characterize with the previous methods.
    }

    public void intervalAdded(ListDataEvent e) {
        this.xIsChanged = true;
        // Sent after the indices in the index0,index1 interval have been
        // inserted in the data model.
    }

    public void intervalRemoved(ListDataEvent e) {
        this.xIsChanged = true;
        // Sent after the indices in the index0,index1 interval have been
        // removed from the data model.
    }
}
