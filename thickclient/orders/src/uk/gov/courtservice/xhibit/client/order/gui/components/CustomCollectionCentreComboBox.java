package uk.gov.courtservice.xhibit.client.order.gui.components;

import java.awt.Dimension;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.order.gui.entry.collectioncentrelist.CollectionCentreList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.collectioncentrelist.CollectionCentreListFactory;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtList;
import uk.gov.courtservice.xhibit.client.order.gui.entry.courtlist.CourtListFactory;

/**
 * <p>
 * Title: CustomComboBox
 * </p>
 * <p>
 * Description: A custom ComboBox class that contains court names or collection centres
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Duncan
 * @version 1.0
 */

public class CustomCollectionCentreComboBox extends JComboBox {
    private CollectionCentreList collectionCentreDisplayNames;

    private Dimension collectionCentreSize = new Dimension();

   
    /**
     * Creates an editable CustomComboBox to display collection centres
     * 
     * @param list
     *            the object that contains collection centres
     */
    public CustomCollectionCentreComboBox(CollectionCentreList list) {
        super(list.getCollectionCentreDisplayNames());
        this.setEditable(true);
        this.collectionCentreDisplayNames = list;
        collectionCentreSize.setSize(250, 22);
        this.setEditable(false);
        this.setPreferredSize(collectionCentreSize);
    }

    /**
     * Creates a CustomComboBox and instantiates the courts list objects
     */
    public CustomCollectionCentreComboBox() {
        this(CollectionCentreListFactory.createCollectionCentreList());
    }

    /**
     * Method to return collection centre names from the collectioncentrelist object
     * 
     * @return collectioncentrelist
     */
    public CollectionCentreList getCollectionCentreDisplayNames() {
        return this.collectionCentreDisplayNames;
    }

    /**
     * Method to set the values for the collectioncentrelist object
     * 
     * @param list
     *            the object that contains collection centre names
     */
    public void setCollectionCentreNames(CollectionCentreList list) {
        this.collectionCentreDisplayNames = list;
    }

}
