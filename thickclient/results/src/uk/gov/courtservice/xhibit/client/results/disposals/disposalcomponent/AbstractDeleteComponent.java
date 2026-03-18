package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: AbstractDeleteComponent
 * </p>
 * <p>
 * Description: Common delete component functionality
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 */
public abstract class AbstractDeleteComponent extends XCheckBox implements ActionListener, DataComponentListener {

    /**
     * List of components in this group
     */
    private final List components = new ArrayList();

    /**
     * List of inserts in this group
     */
    private final List inserts = new ArrayList();

    private XColor groupColor;

    /**
     * Construct a new component delete group
     * 
     * @param groupName,
     *            currently unused
     */
    public AbstractDeleteComponent(String groupName) {
        this.groupColor = null;
        addActionListener(this);
        setToolTipText(DisposalUtil.getToolTipText("deleteComponentIndividualToolTip"));
    }

    /**
     * Set the group color
     */
    public void setGroupColor(XColor groupColor) {
        this.groupColor = groupColor;
        for (int i = 0, s = components.size(); i < s; i++) {
            setGroupColor((DataComponent) components.get(i), groupColor);
        }

        if (groupColor != null) {
            setToolTipText(DisposalUtil.getToolTipText("deleteComponentGroupToolTip", groupColor.getName()));
        } else {
            setToolTipText(DisposalUtil.getToolTipText("deleteComponentIndividualToolTip"));
        }
    }

    /**
     * Get the group color
     */
    public XColor getGroupColor() {
        return groupColor;
    }

    /**
     * ActionListener Implementation
     */
    public void actionPerformed(ActionEvent e) {
        boolean deleted = isDeleted();
        // Mark components as deleted
        for (int i = 0, s = components.size(); i < s; i++) {
            setDeleted((DataComponent) components.get(i), deleted);
        }
        // Disable inserts
        for (int i = 0, s = inserts.size(); i < s; i++) {
            ((InsertComponent) inserts.get(i)).setEnabled(!deleted);
        }
    }

    /**
     * Add a new component
     */
    public void add(DataComponent component) {
        if (component != null) {
            components.add(component);
            component.addDataComponentListener(this);
            setGroupColor(component, groupColor);
        }
    }

    /**
     * Add a new component
     */
    public void add(InsertComponent insert) {
        if (insert != null) {
            insert.setEnabled(!isDeleted());
            inserts.add(insert);
        }
    }

    /**
     * Return true if this group has been deleted
     */
    public boolean isDeleted() {
        return isSelected();
    }

    /**
     * Set this group to deleted
     */
    public void setDeleted(boolean deleted) {
        setSelected(deleted);
        // Disable inserts
        for (int i = 0, s = inserts.size(); i < s; i++) {
            ((InsertComponent) inserts.get(i)).setEnabled(!deleted);
        }
    }

    /**
     * DataComponentListener listener
     */
    public void dataChanged(DataComponentEvent e) {
        // By default do nothing, concrete implementaions can react to this
        // event
    }

    /**
     * Return a new delete marker
     */
    public Component createDeleteMarker() {
        return groupColor == null ? null : new JComponent() {
            {
                setPreferredSize(new java.awt.Dimension(6, 6));
                setOpaque(true);
            }

            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Paint oldPaint = g2.getPaint();
                g2.setPaint(new GradientPaint(0, 0, getParent().getBackground(), getWidth(), getHeight(), groupColor));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setPaint(oldPaint);
            }
        };
    }

    /**
     * Paint the group Color
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (groupColor != null) {
            g.setColor(new Color(groupColor.getRed(), groupColor.getGreen(), groupColor.getBlue(), 24));
            g.fillRect(4, 4, getWidth() - 8, getHeight() - 8);
        }
    }

    /**
     * Return true if this component has been deleted in this group
     */
    protected abstract boolean getDeleted(DataComponent component);

    /**
     * Set the component to deleted in this group
     */
    protected abstract void setDeleted(DataComponent component, boolean deleted);

    /**
     * Set the component to deleted in this group
     */
    protected abstract void setGroupColor(DataComponent component, XColor groupColor);

}
