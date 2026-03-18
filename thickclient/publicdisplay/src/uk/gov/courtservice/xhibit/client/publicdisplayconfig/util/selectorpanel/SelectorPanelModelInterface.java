package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel;

import javax.swing.event.ListSelectionListener;

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
 * @version $Id: SelectorPanelModelInterface.java,v 1.1 2004/01/15 10:42:31
 *          sz0t7n Exp $
 */

public interface SelectorPanelModelInterface {

    /**
     * The item passed in will be added to the model. The implementing class
     * should add the object, sort if required and fire the appropriate
     * notification so that the view can refresh
     * 
     * @param objectToAdd
     */
    public void addElement(Object objectToAdd);

    /**
     * The id represents the location in the list of the item to remove. The
     * implementing class should remove the object from the model, fire the
     * appropriate notification so that the view can refresh.
     * 
     * @param objectToRemoveLocationId
     */
    public void removeElementAt(int objectToRemoveLocationId);

    /**
     * The implementing class should remove all the objects from the model and
     * fire the appropriate notification so that the view can refresh.
     */
    public void removeAllElements();

    /**
     * The implementing class should return the locations of the selected items.
     * Typically this will be used for getting the selected object using
     * <code>getItemAt(int index)</code> and removing objects using
     * <code>remove(int objectToRemoveLocationId)</code>.
     * 
     * @return
     */
    public int[] getSelectedIndeces();

    /**
     * The implementing class should return the item from the model at the
     * specified index. Note: this is the index as visible in the view, so any
     * sorting must be accounted for.
     * 
     * @param index
     *            the location in the list
     * @return the object from the model for the supplied index.
     */
    public Object getElementAt(int index);

    /**
     * Return the number of elements in the model
     * 
     * @return number of elements in model
     */
    public int getModelSize();

    /**
     * Returns the objects in the model
     * 
     * @return array of objects in model
     */
    public Object[] getData();

    /**
     * Add the list selection listener to the appropriate view
     * 
     * @param event
     */
    public void addListSelectionListener(ListSelectionListener listen);
}