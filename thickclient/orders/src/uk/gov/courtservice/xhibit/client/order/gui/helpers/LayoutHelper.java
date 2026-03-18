package uk.gov.courtservice.xhibit.client.order.gui.helpers;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.JComponentHelper;

//No longer implemented, can be deleted!!!!!!!!!!!!!!!!!!!!!!!

public class LayoutHelper {

    private DataEntryPanel panel;

    private Component[] comps;

    private Vector allJTextComponents = new Vector();

    private int preferredSize;

    private JComponentHelper jHelp = new JComponentHelper();

    /**
     * Constructs a layout helper containing all child components within the
     * DataEntryPanel.
     * 
     * @param panel
     */
    public LayoutHelper(DataEntryPanel panel) {
        this.panel = panel;
        this.preferredSize = 0;
        getAllComponents();
    }

    private int getPreferredSize() {
        return this.preferredSize;
    }

    private void setPreferredSize(int i) {
        if (getPreferredSize() < i) {
            this.preferredSize = i;
        }
    }

    private void getAllComponents() {
        this.comps = panel.getComponents();
        for (int i = 0; i <= comps.length - 1; i++) {
            Dimension d = comps[i].getMinimumSize();
            double w = d.getWidth();
            double h = d.getHeight();
            w = w + 10;
            comps[0].setSize((int) w, (int) h);
        }
    }

    private void calculatePreferredSize() {
        for (int i = 0; i < comps.length; i++) {
            setPreferredSize(jHelp.getPreferredSize(comps[i]));
            allJTextComponents.add(comps[i]);
        }
    }

    /**
     * Iterates through all child components, calculates the preferred size of
     * all text fields/areas and resizes them to improve alignment.
     */
    public void resizeComponents() {
        calculatePreferredSize();
        for (int i = 0; i < allJTextComponents.size(); i++) {
            jHelp.resizeComponents(allJTextComponents.get(i), getPreferredSize());
        }
    }

    private void setPanelBorder(String title) {
        Border b = BorderFactory.createTitledBorder(title);
        this.panel.setBorder(b);
    }

}