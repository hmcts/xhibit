package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Component Util
 * </p>
 * <p>
 * Description: Util methods for manipulating the xac component
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */

public final class ComponentUtil {

    public static final Logger log = CSServices.getLogger(ComponentUtil.class);

    private ComponentUtil() {
        // Stop unnecesary creation of util class
    }

    public static JLabel createValue() {
        return createValue("Unknown!");
    }

    public static JLabel createValue(float value, String fmt) {
        return createValue();
    }

    public static JLabel createValue(String text) {
        JLabel value = new JLabel(text);
        value.setFont(value.getFont().deriveFont(Font.BOLD));
        return value;
    }

    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    public static JPanel createPanel(boolean border) {
        return createPanel(new GridBagLayout(), border);
    }

    public static JPanel createPanel(LayoutManager layout, boolean border) {
        JPanel panel = new JPanel(layout);
        if (border) {
            panel.setBorder(BorderFactory.createEtchedBorder());
        }
        return panel;
    }

    public static JButton createButtonWithOwner(XhibitApplicationController xac, String actionName, Object owner) {
        XAction action = XhibitActions.getAction(xac, actionName);
        action.setModel(owner);
        return createButton(action);
    }

    public static JButton createButton(XhibitApplicationController xac, String actionName) {
        return createButton(XhibitActions.getAction(xac, actionName));
    }

    public static JButton createButton(XAction action) {
        JButton jb = new JButton();
        jb.setAction(action);
        // jb.setMnemonic(action.getMnemonicKey().intValue());
        return jb;
    }

    public static XTable createSortableTable(TableModel model) {
        XTable table = XTableFactory.getInstance().createDefaultTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.makeSortable();
        return table;
    }

    public static JScrollPane createScrollPane(Component component) {
        return new JScrollPane(component);
    }

}
