package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel;

import javax.swing.JPanel;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AbstractSelectorPanel.java,v 1.4 2006/06/05 12:32:08 bzjrnl Exp $
 */

public abstract class AbstractSelectorPanel extends JPanel implements SelectorPanelModelInterface {
    /**
     * The item passed in will be added to the underlying model. The
     * implementing class should add the object, sort if required and fire the
     * appropriate notification so that the view can refresh
     * 
     * @param objectToAdd
     */
    public abstract void addElement(Object objectToAdd);

    /**
     * The id represents the location in the list of the item to remove. The
     * implementing class should remove the object from the model, fire the
     * appropriate notification so that the view can refresh.
     * 
     * @param objectToRemoveLocationId
     */
    public abstract void removeElementAt(int objectToRemoveLocationId);

    /**
     * The implementing class should remove all the objects from the model and
     * fire the appropriate notification so that the view can refresh.
     */
    public abstract void removeAllElements();

    /**
     * The implementing class should return the locations of the selected items.
     * Typically this will be used for getting the selected object using
     * <code>getItemAt(int index)</code> and removing objects using
     * <code>remove(int objectToRemoveLocationId)</code>.
     * 
     * @return
     */
    public abstract int[] getSelectedIndeces();

    /**
     * The implementing class should return the item from the model at the
     * specified index. Note: this is the index as visible in the view, so any
     * sorting must be accounted for.
     * 
     * @param index
     *            the location in the list
     * @return the object from the model for the supplied index.
     */
    public abstract Object getElementAt(int index);

    /**
     * Return the number of elements in the model
     * 
     * @return number of elements in model
     */
    public abstract int getModelSize();

    /**
     * Returns the objects in the model
     * 
     * @return array of objects in model
     */
    public abstract Object[] getData();
}