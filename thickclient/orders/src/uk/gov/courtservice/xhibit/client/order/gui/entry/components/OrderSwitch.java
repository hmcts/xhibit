package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.gov.courtservice.xhibit.client.order.gui.components.OrderPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;

/**
 * <p>
 * Title: OrderSwitch
 * </p>
 * 
 * @deprecated Use OrderSection with swiutchable
 * @see OrderSection
 *      <p>
 *      Copyright: Copyright (c) 2002
 *      </p>
 *      <p>
 *      Company: EDS
 *      </p>
 * @author Neil & Neil
 * @version 1.0
 */
public class OrderSwitch extends AbstractOrderComponent implements ChangeListener {
    private JComponent panel;

    private GridBagConstraints constraints;

    public OrderSwitch() {
        this.constraints = getDefaultConstraints();
    }

    public void initComponent() {
        Border b = BorderFactory.createTitledBorder(getHelper().getAttribute("borderLabel"));
        panel = new OrderPanel();
        this.setBorder(b);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(panel, constraints);
        String switchLabel = getHelper().getAttribute("switchLabel");
        JCheckBox jCheckBox = new JCheckBox(switchLabel != null ? switchLabel : "");
        jCheckBox.addChangeListener(this);
        constraints.gridy = 0;
        add(jCheckBox, constraints);
        jCheckBox.setSelected(false);
        panel.setVisible(false);
        setVisualComponent(this);
    }

    public boolean isLabelled() {
        return false;
    }

    public void stateChanged(ChangeEvent e) {
        panel.setVisible(((JCheckBox) e.getSource()).isSelected());
        panel.validate();
    }

    public JComponent getVisualLeafComponent() {
        return panel;
    }

}
