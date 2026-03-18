package uk.gov.courtservice.xhibit.client.results.disposals.disposalcomponent;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import uk.gov.courtservice.xhibit.client.results.disposals.DataComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DataComponentListener;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalListener;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentEvent;
import uk.gov.courtservice.xhibit.client.results.disposals.InsertComponentListener;
import uk.gov.courtservice.xhibit.client.results.disposals.PromptComponent;
import uk.gov.courtservice.xhibit.client.util.XColor;

/**
 * <p>
 * Title: DefaultDisposalBody
 * </p>
 * <p>
 * Description: Allow disposal fields to be edited
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.19 $
 */
public class DefaultDisposalBody extends JScrollPane {
    // Delete group colors
    private static final XColor[] DELETE_COLORS = new XColor[] { XColor.RED, XColor.BLUE, XColor.GREEN, XColor.ORANGE,
            XColor.MAGENTA, XColor.PINK, XColor.CYAN };

    // constraints - These are templates the y part changes as components
    // are
    // added
    private static final GridBagConstraints createPromptConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(2, 4, 2, 2);
        return constraints;
    }

    private static final GridBagConstraints createDeleteG1Constraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = gridy;
        constraints.insets = new Insets(2, 0, 2, 0);
        return constraints;
    }

    private static final GridBagConstraints createDeleteG2Constraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = gridy;
        constraints.insets = new Insets(2, 0, 2, 0);
        return constraints;
    }

    private static final GridBagConstraints createFixedDataConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static final GridBagConstraints createDynamicDataConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static final GridBagConstraints createInsertConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        // constraints.weighty = 1.0;
        // constraints.fill = GridBagConstraints.BOTH;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 4);
        return constraints;
    }

    private static final GridBagConstraints createSpacerConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.BOTH;
        return constraints;
    }
    private static final GridBagConstraints createLastItemConstraints(int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = gridy;
        constraints.weighty = 1.0;
        constraints.gridwidth = 4;
        constraints.anchor = GridBagConstraints.WEST;
        return constraints;
    }
    

    // Spacer
    private final Component spacer = new JComponent() {
        // Min Weight Spacer
    };

    // Map of Group 1 Delete components keyed by group name
    private final Map group1DeleteComponents = new HashMap();

    // Map of Group 2 Delete components keyed by group name
    private final Map group2DeleteComponents = new HashMap();

    // Listeners stops polution of interface
    private final DefaultDisposalBodyHelper helper = new DefaultDisposalBodyHelper();

    // The disposal component, required for result delegation
    private final DisposalComponent parentDisposalComponent;


    // The current y
    private int gridy = 0;
    
    // The index of the next delete group color
    private int nextDeleteColorIndex = 0;

    /**
     * Construct a new instance of the disposal body, do not add things directly
     * add to JPanel instead
     */
    public DefaultDisposalBody(DisposalComponent parentDisposalComponent) {
        super(new JPanel(new GridBagLayout()));
        if (parentDisposalComponent == null) {
            throw new IllegalArgumentException("parentDisposalComponent: null");
        }
        this.parentDisposalComponent = parentDisposalComponent;
        getPanel().add(spacer, createSpacerConstraints(0));
        
    }
    
    
    /**
     * DisposalComponent Implementation
     */
    public void add(PromptComponent prompt, DataComponent data, InsertComponent insert) {
        JPanel panel = getPanel();

        gridy += 1; // May waste if no prompt or data
        // Add Prompt
        if (prompt != null) {
            Component component = prompt.getComponent();
            if (component != null) {
                panel.add(component, createPromptConstraints(gridy));
            }
        }
        // Add Data
        if (data != null) {
            data.addDataComponentListener(helper);

            Component component = data.getComponent();
            if (component != null) {
                // Add To Group 1 Delete adding component if first member
                // (discards single (no group deletes))
                String nameG1 = data.getNameG1();
                if (nameG1 != null) {
                    if (nameG1.equals(DataComponent.SINGLE_GROUP1_NAME)) {
                        Group1DeleteComponent group1DeleteComponent = new Group1DeleteComponent(nameG1);
                        panel.add(group1DeleteComponent, createDeleteG1Constraints(gridy));
                        group1DeleteComponent.add(data);
                    } else {
                        Group1DeleteComponent group1DeleteComponent = (Group1DeleteComponent) group1DeleteComponents
                                .get(nameG1);
                        if (group1DeleteComponent == null) {
                            group1DeleteComponent = new Group1DeleteComponent(nameG1);
                            group1DeleteComponents.put(nameG1, group1DeleteComponent);
                            if (data.isScreenPrint()) {
                                panel.add(group1DeleteComponent, createDeleteG1Constraints(gridy));
                            }
                        } else {
                            if (group1DeleteComponent.getGroupColor() == null) {
                                group1DeleteComponent.setGroupColor(getNextDeleteColor());
                            }
                            Component marker = group1DeleteComponent.createDeleteMarker();
                            if (marker != null) {
                                if (data.isScreenPrint()) {
                                    panel.add(marker, createDeleteG1Constraints(gridy));
                                }
                            }
                        }
                        group1DeleteComponent.add(data);
                        group1DeleteComponent.add(insert);
                    }
                }

                // Add To Group 2 Delete adding component if first member
                String nameG2 = data.getNameG2();
                if (nameG2 != null) {
                    Group2DeleteComponent group2DeleteComponent = (Group2DeleteComponent) group2DeleteComponents
                            .get(nameG2);
                    if (group2DeleteComponent == null) {
                        group2DeleteComponent = new Group2DeleteComponent(nameG2);
                        group2DeleteComponents.put(nameG2, group2DeleteComponent);
                        if (data.isScreenPrint()) {
                            panel.add(group2DeleteComponent, createDeleteG2Constraints(gridy));
                        }
                    } else {
                        if (group2DeleteComponent.getGroupColor() == null) {
                            group2DeleteComponent.setGroupColor(getNextDeleteColor());
                        }
                        Component marker = group2DeleteComponent.createDeleteMarker();
                        if (marker != null) {
                            if (data.isScreenPrint()) {
                                panel.add(marker, createDeleteG2Constraints(gridy));
                            }
                        }
                    }
                    group2DeleteComponent.add(data);
                    group2DeleteComponent.add(insert);
                }

                // Add component
                if (data.isScreenPrint()) {
                    if (data.isFixedSize()) {
                        panel.add(component, createFixedDataConstraints(gridy));
                    } else {
                        panel.add(component, createDynamicDataConstraints(gridy));
                    }
                }
            }
        }

        // Add Insert
        if (insert != null) {
            gridy += 1;

            insert.addInsertComponentListener(helper);

            Component component = insert.getComponent();
            if (component != null) {
                panel.add(component, createInsertConstraints(gridy));
            }
        }

        // Move Spacer to bottom
        ((GridBagLayout) getPanel().getLayout()).setConstraints(spacer, createSpacerConstraints(gridy + 1));
    }
    
    /**
     * Get the the next delete group color
     */
    public XColor getNextDeleteColor() {
        XColor color = DELETE_COLORS[nextDeleteColorIndex++];

        if (nextDeleteColorIndex >= DELETE_COLORS.length) {
            nextDeleteColorIndex = 0;
        }

        return color;
    }

    /**
     * Return true if all mandatory fields are complete
     */
    public boolean isComplete() {
        JPanel panel = getPanel();
        for (Component component : panel.getComponents()) {
            if (component instanceof DataComponent) {
                DataComponent dataComponent = (DataComponent) component;
                if (!dataComponent.isComplete()) {
                    return false;
                }
            }else if (component instanceof InsertComponent) {
            	InsertComponent insertComponent = (InsertComponent) component;
            	if (!insertComponent.isComplete()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Add the listener
     */
    public void addDisposalListener(DisposalListener listener) {
        listenerList.add(DisposalListener.class, listener);
    }

    /**
     * Remove the listener
     */
    public void removeDisposalListener(DisposalListener listener) {
        listenerList.remove(DisposalListener.class, listener);
    }

    /**
     * Notifies all interested listeners that the line length has changed
     */
    protected void fireDisposalChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            DisposalEvent event = null;
            if (listeners[i] == DisposalListener.class) {
                // Lazily create the event:
                if (event == null) {
                    // We are effectivelyu doing this for the
                    // parentDisposalComponent
                    event = new DisposalEvent(parentDisposalComponent, parentDisposalComponent.isComplete());
                }
                ((DisposalListener) listeners[i + 1]).disposalChanged(event);
            }
        }
    }

    /**
     * Get the main panel from the scrollpane
     */
    private JPanel getPanel() {
        return (JPanel) getViewport().getView();
    }

    /**
     * Implement listeners
     */
    private class DefaultDisposalBodyHelper implements DataComponentListener, InsertComponentListener {
        // DataComponentListener
        public void deletedG1Changed(DataComponentEvent e) {
            fireDisposalChanged();
        }

        // DataComponentListener
        public void deletedG2Changed(DataComponentEvent e) {
            fireDisposalChanged();
        }

        // DataComponentListener
        public void dataChanged(DataComponentEvent e) {
            fireDisposalChanged();
        }

        // InsertComponentListener
        public void insertChanged(InsertComponentEvent e) {
            fireDisposalChanged();
        }
    }
}
