package uk.gov.courtservice.xhibit.client.results.disposals.datacomponent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalUtil;
import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: DelegateDataComponent
 * </p>
 * <p>
 * Description: Implementation of the common functionality, the implementations
 * of DataComponent needs to call this class where possible for deletes and
 * mandatory methods, must provide own implementation of set data,
 * DataComponentImpl uses getMain and setMain as call backs. The delegator
 * should call the paint delegator here after calling the paint delegator in the
 * super class.
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: DelegateDataComponent.java,v 1.26 2005/06/02 17:47:33 bzjrnl
 *          Exp $
 */
public class DelegateDataComponent implements ActionListener, DocumentListener {

    /**
     * The listener list
     */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * The comoponent that is delegating functionality
     */
    private final DelegatorDataComponent delegator;

    /**
     * Cache of main delegator color
     */
    private Color backgroundCache = null;

    /**
     * The name of delete group 1
     */
    private String nameG1 = null;

    /**
     * The name of delete group 2
     */
    private String nameG2 = null;

    /**
     * True if the field is mandatory
     */
    private boolean mandatory = false;

    /**
     * True if the component should be displayed
     */
    private boolean screenPrint = true;

    /**
     * True if the delegator has been deleted as part of group 1
     */
    private boolean deletedG1 = false;

    /**
     * True if the delegator has been deleted as part of group 2
     */
    private boolean deletedG2 = false;

    /**
     * If not null the color to use for the deleted g1 strike through
     */
    private XColor colorG1 = null;

    /**
     * If not null the color to use for the deleted g2 strike through
     */
    private XColor colorG2 = null;

    /**
     * The max number of chars that can be returned by this component
     */
    private int maxChars = DataComponent.DEFAULT_MAX_CHARS;

    /**
     * Construct a new instance from the given delegator
     */
    public DelegateDataComponent(DelegatorDataComponent delegator) {
        if (delegator == null) {
            throw new IllegalArgumentException("delegator: null");
        }
        this.delegator = delegator;
    }

    /**
     * Set the data
     */
    public String getData() {
        return delegator.getDataImpl();
    }

    /**
     * Get the data
     */
    public void setData(String data) {
        delegator.setDataImpl(data);
    }

    /**
     * Return true if the delegator is mandatory
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Return true if the delegator has an error
     */
    public boolean hasError() {
        return delegator.hasError();
    }

    /**
     * Return true if the component should be displayed
     */
    public boolean isScreenPrint() {
        return screenPrint;
    }

    /**
     * Set to true to display the component
     */
    public void setScreenPrint(boolean screenPrint) {
        this.screenPrint = screenPrint;
    }

    /**
     * Return mandatory tool tip
     */
    public String createMandatoryToolTipText() {
        if (isMandatory()) {
            return DisposalUtil.getToolTipText("mandatoryToolTip");
        }
        return null;
    }

    /**
     * Return mandatory tool tip
     */
    public String createDeleteToolTipText() {
        if (nameG1 == null) {
            return null;
        } else {
            if (nameG2 == null) {
                if (colorG1 == null) {
                    return DisposalUtil.getToolTipText("deleteSingleGroupMemberToolTip");
                } else {
                    return DisposalUtil.getToolTipText("deleteGroupMemberToolTip", colorG1.getName());
                }
            } else {
                if (colorG1 == null) {
                    if (colorG2 == null) {
                        return DisposalUtil.getToolTipText("deleteSingleGroupMemberToolTip");
                    } else {
                        return DisposalUtil.getToolTipText("deleteGroupAndSingleMemberToolTip", colorG2.getName());
                    }
                } else {
                    if (colorG2 == null) {
                        return DisposalUtil.getToolTipText("deleteGroupAndSingleMemberToolTip", colorG1.getName());
                    } else {
                        return DisposalUtil.getToolTipText("deleteTwoGroupMemberToolTip", colorG1.getName(), colorG2
                                .getName());
                    }
                }
            }
        }
    }

    private String createToolTipText() {
        String mandatory = createMandatoryToolTipText();
        String component = delegator.createToolTipTextImpl();
        String delete = createDeleteToolTipText();

        return mandatory == null ? component == null ? delete == null ? null : delete : delete == null ? component
                : component + " " + delete : component == null ? delete == null ? mandatory : mandatory + " " + delete
                : delete == null ? mandatory + " " + component : mandatory + " " + component + " " + delete;
    }

    /**
     * Set mandatory flag to indicate delegator is required, update the
     * delegators main color
     */
    public void setMandatory(boolean mandatory) {
        if (this.mandatory != mandatory) {
            this.mandatory = mandatory;
            update(isDeletedG1(), isDeletedG2(), mandatory);
        }
    }

    /**
     * Set the maximum number of chars that can be returned by this component
     */
    public void setMaxChars(int maxChars) {
        int newMaxChars = maxChars < 0 ? DataComponent.DEFAULT_MAX_CHARS : maxChars;
        String data = delegator.getDataImpl();
        if (data != null && data.length() > newMaxChars) {
            throw new IllegalStateException("data: " + data + " newMaxChars: " + newMaxChars);
        }
        this.maxChars = newMaxChars;
        updateTooltip();
    }

    /**
     * Get the maximum number of chars that can be returned by this component
     */
    public int getMaxChars() {
        return maxChars;
    }

    /**
     * Set name of group 1
     */
    public void setNameG1(String nameG1) {
        this.nameG1 = nameG1;
        updateTooltip();
    }

    /**
     * Set name of group 2
     */
    public void setNameG2(String nameG2) {
        this.nameG2 = nameG2;
        updateTooltip();
    }

    /**
     * Get name of group 1
     */
    public String getNameG1() {
        return nameG1;
    }

    /**
     * Get name of group 2
     */
    public String getNameG2() {
        return nameG2;
    }

    /**
     * Get name of group 1
     */
    public void setColorG1(XColor colorG1) {
        this.colorG1 = colorG1;
        updateTooltip();
    }

    /**
     * Get name of group 2
     */
    public void setColorG2(XColor colorG2) {
        this.colorG2 = colorG2;
        updateTooltip();
    }

    /**
     * Get name of group 1
     */
    public XColor getColorG1() {
        return colorG1;
    }

    /**
     * Get name of group 2
     */
    public XColor getColorG2() {
        return colorG2;
    }

    /**
     * Return true if the field is complete, note non mandatory fields are
     * allways complete
     */
    public boolean isComplete() {
        String data = getData();
        return !hasError()
                && (!isMandatory() || isDeletedG1() || isDeletedG2() || (data != null && data.trim().length() > 0));
    }

    /**
     * Return true if the delegator has been deleted as part of group 1
     */
    public boolean isDeletedG1() {
        return deletedG1;
    }

    /**
     * Return true if the delegator has been deleted as part of group 2
     */
    public boolean isDeletedG2() {
        return deletedG2;
    }

    /**
     * Delete / Restore the delegator in group 1
     */
    public void setDeletedG1(boolean deletedG1) {
        if (this.deletedG1 != deletedG1) {
            this.deletedG1 = deletedG1;
            update(deletedG1, isDeletedG2(), isMandatory());
            fireDeletedG1Changed();
        }
    }

    /**
     * Delete / Restore the delegator in group 2
     */
    public void setDeletedG2(boolean deletedG2) {
        if (this.deletedG2 != deletedG2) {
            this.deletedG2 = deletedG2;
            update(isDeletedG1(), deletedG2, isMandatory());
            fireDeletedG2Changed();
        }
    }

    /**
     * Update the delegator
     */
    private void update(boolean deletedG1, boolean deletedG2, boolean mandatory) {
        if (deletedG1 || deletedG2) {
            delegator.getComponent().setEnabled(false);
            if (backgroundCache != null) {
                delegator.setBackgroundImpl(backgroundCache);
            }
            delegator.getComponent().repaint();
        } else {
            delegator.getComponent().setEnabled(true);
            if (mandatory) {
                if (backgroundCache == null) {
                    backgroundCache = delegator.getBackgroundImpl();
                }
                delegator.setBackgroundImpl(DataComponent.MANDATORY_COLOR);
            } else if (backgroundCache != null) {
                delegator.setBackgroundImpl(backgroundCache);
            }
            delegator.getComponent().repaint();
        }
        updateTooltip();
    }

    /**
     * Update the tooltip
     */
    private void updateTooltip() {
        Component component = delegator.getComponent();
        if (component instanceof JComponent) {
            ((JComponent) component).setToolTipText(createToolTipText());
        }
    }

    /**
     * Paint the delegator delete overlay.
     */
    public void paint(Graphics g) {
        delegator.paintImpl(g);
        if (isDeletedG1()) {
            if (isDeletedG2()) {
                paintDoubleStrikeThrough(g, colorG1, colorG2);
            } else {
                paintSingleStrikeThrough(g, colorG1);
            }
        } else {
            if (isDeletedG2()) {
                paintSingleStrikeThrough(g, colorG2);
            }
        }
    }

    /**
     * Paint the delegator delete overlay.
     */
    public void paintSingleStrikeThrough(Graphics g, Color color1) {
        Color oldColor = g.getColor();

        Insets insets = delegator.getPaintInsetsImpl();
        int height = delegator.getComponent().getHeight();
        int width = delegator.getComponent().getWidth();

        int x1 = insets.left;
        int y1 = height / 2;
        int x2 = width - insets.right;
        int y2 = height / 2;

        g.setColor(DataComponent.DELETE_COLOR); // color1 != null ? color1 :
        // DataComponent.DELETE_COLOR);
        g.drawLine(x1, y1, x2, y2);

        g.setColor(oldColor);
    }

    /**
     * Paint the delegator delete overlay.
     */
    public void paintDoubleStrikeThrough(Graphics g, Color color1, Color color2) {
        Color oldColor = g.getColor();

        Insets insets = delegator.getPaintInsetsImpl();
        int height = delegator.getComponent().getHeight();
        int width = delegator.getComponent().getWidth();

        int x1 = insets.left;
        int y1 = height / 2;
        int x2 = width - insets.right;
        int y2 = height / 2;

        g.setColor(DataComponent.DELETE_COLOR); // color1 != null ? color1 :
        // DataComponent.DELETE_COLOR);
        g.drawLine(x1, y1 - 2, x2, y2 - 2);

        g.setColor(DataComponent.DELETE_COLOR); // color2 != null ? color2 :
        // DataComponent.DELETE_COLOR);
        g.drawLine(x1, y1 + 2, x2, y2 + 2);

        g.setColor(oldColor);
    }

    /**
     * Add the listener
     */
    public void addDataComponentListener(DataComponentListener listener) {
        listenerList.add(DataComponentListener.class, listener);
    }

    /**
     * Remove the listener
     */
    public void removeDataComponentListener(DataComponentListener listener) {
        listenerList.remove(DataComponentListener.class, listener);
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    public void fireDataChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            DataComponentEvent event = null;
            if (listeners[i] == DataComponentListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new DataComponentEvent(delegator);
                }
                ((DataComponentListener) listeners[i + 1]).dataChanged(event);
            }
        }
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    public void fireDeletedG1Changed() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            DataComponentEvent event = null;
            if (listeners[i] == DataComponentListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new DataComponentEvent(delegator);
                }
                ((DataComponentListener) listeners[i + 1]).deletedG1Changed(event);
            }
        }
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    public void fireDeletedG2Changed() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            DataComponentEvent event = null;
            if (listeners[i] == DataComponentListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new DataComponentEvent(delegator);
                }
                ((DataComponentListener) listeners[i + 1]).deletedG2Changed(event);
            }
        }
    }

    /**
     * ActionListener Implemenation
     */
    public void actionPerformed(ActionEvent e) {
        fireDataChanged();
    }

    /**
     * DocumentListener Implemenation
     */
    public void insertUpdate(DocumentEvent e) {
        fireDataChanged();
    }

    /**
     * DocumentListener Implemenation
     */
    public void removeUpdate(DocumentEvent e) {
        fireDataChanged();
    }

    /**
     * DocumentListener Implemenation
     */
    public void changedUpdate(DocumentEvent e) {
        fireDataChanged();
    }

    /**
     * @return true if the data component contains the value 1
     */
    public boolean isSingular(DataComponent dataComponent) {
        if (dataComponent != null) {
            String data = dataComponent.getData();
            if (data != null) {
                try {
                    return Integer.parseInt(data) == 1;
                } catch (NumberFormatException nfe) {
                    // fall through
                }
            }
        }
        return false;
    }
}
