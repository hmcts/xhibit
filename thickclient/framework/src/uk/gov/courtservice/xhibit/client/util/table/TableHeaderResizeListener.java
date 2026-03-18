package uk.gov.courtservice.xhibit.client.util.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.helpers.PropertyHelper;

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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class TableHeaderResizeListener implements MouseListener // HierarchyBoundsListener,
{
    private static Logger log;

    private JTableHeader _header;

    private String _propertyName;

    public TableHeaderResizeListener(JTableHeader header, String propertyName) {
        log = CSServices.getLogger(TableHeaderResizeListener.class);
        _header = header;
        _propertyName = propertyName;
    }

    // public void ancestorMoved(HierarchyEvent e) {}
    // public void ancestorResized(HierarchyEvent e)
    // {
    // log.debug("Resized");
    // resized();
    // }

    private boolean isResizing = false;

    public void mousePressed(MouseEvent e) {
        if (_header.getResizingColumn() != null) {
            isResizing = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isResizing) {
            isResizing = false;
            resized();
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void resized() {
        StringBuffer sb = new StringBuffer();

        TableColumnModel tcm = _header.getColumnModel();
        Enumeration enumeration = tcm.getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn item = (TableColumn) enumeration.nextElement();
            sb.append(item.getWidth());
            if (enumeration.hasMoreElements())
                sb.append(",");
        }
        log.debug("Col widths = " + sb.toString());

        try {
            Properties p = getLocalProperties(XTable.XTABLESIZES);
            p.setProperty(_propertyName, sb.toString());
            PropertyHelper.storeUserHomeProperties(p, XTable.XTABLESIZES);
        } catch (CSRecoverableException ex) {
            log.error("Could not save new table widths");
        }
    }

    private Properties getLocalProperties(String propertyName) throws CSRecoverableException {
        Properties localProps = PropertyHelper.getUserHomeProperties(propertyName);
        if (localProps == null) {
            PropertyHelper.createUserHomeProperties(propertyName, "myName=" + propertyName);
            localProps = PropertyHelper.getUserHomeProperties(propertyName);
        }
        return localProps;
    }
}