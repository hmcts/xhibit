package uk.gov.courtservice.xhibit.client.order.gui.entry.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;

import com.asn1c.core.String16;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBasicValue;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomCollectionCentreComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.components.CustomComboBox;
import uk.gov.courtservice.xhibit.client.order.gui.entry.AbstractOrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.collectioncentrelist.CollectionCentreListMidTier;
import uk.gov.courtservice.xhibit.client.order.xml.MOClientXMLHelper;

/**
 * <p>
 * Title: OrderCollectionCentre
 * </p>
 * <p>
 * Description: ComboBox component used to display a list of collection centres. This
 * class implements ItemListener to detect changes to the selected collection centre value,
 * and updates the underlying order data with the newly selected collection centre name.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author SA
 * @version 1.0
 */

public class OrderCollectionCentre extends AbstractOrderComponent implements ItemListener {

    private CustomCollectionCentreComboBox box;
    
    private Logger log = CSServices.getLogger(OrderCollectionCentre.class);

    /**
     * Construct the ComboBox component with default collection centre name.
     */
    public void initComponent() {
        box = new CustomCollectionCentreComboBox();
        String defaultCollectionCentre = getHelper().getAttribute("default");
        
        // However default collection centre may be different for copied orders
        java.util.Vector v = this.getHelper().getChildReferences();
        MOClientXMLHelper mx = new MOClientXMLHelper();
        defaultCollectionCentre = mx.getDefaultCollectionCentreNameForComboBox(v);
        if ((defaultCollectionCentre != null) && (box.getCollectionCentreDisplayNames().isCollectionCentreExists(defaultCollectionCentre))) {
            box.setSelectedItem(defaultCollectionCentre);
        }
        setVisualComponent(box);
        box.addItemListener(this);
        String text = getHelper().getValue();
        if ((text != null) && (text.length() > 0)) {
            box.setSelectedItem(text);
        }
        setCollectionCentreName();
    }

    /**
     * Uses helper class to set the value of the collection centre name in order data to the
     * newly selected value.
     * 
     * @param e
     *            an item event.
     */
    public void itemStateChanged(ItemEvent e) {
        setCollectionCentreName();
    }

    /**
     * Sets court name value in order data.
     */
    private void setCollectionCentreName() {
        String ccName = ((JComboBox) getVisualComponent()).getSelectedItem().toString();
        
        String collectionCentreId = box.getCollectionCentreDisplayNames().getCollectionCentreIdFromDisplayName(ccName);
        XhbCollectionCentreBasicValue thisCollectionCentre = new XhbCollectionCentreBasicValue();
        XhbAddressBasicValue theAddress = new XhbAddressBasicValue();
        try {
            thisCollectionCentre = (XhbCollectionCentreBasicValue) box.getCollectionCentreDisplayNames().getCollectionCentre(new Integer(collectionCentreId).intValue());
            int addressId = thisCollectionCentre.getAddressId().intValue(); 
            theAddress = box.getCollectionCentreDisplayNames().getAddressFromAddressId(addressId);
        } catch (NumberFormatException nfe) {
            log.error("OrderCollectionCentre.setCollectionCentreName: number format error when error parsing collectionCentreId or addressId:");
            log.error("Coll centre id = " +thisCollectionCentre.getCollectionCentreId());
            log.error("Address id = " +thisCollectionCentre.getAddressId());
        } catch (NullPointerException npe) {
            log.error("OrderCollectionCentre.setCollectionCentreName: null pointer when error parsing collectionCentreId or addressId:");
            log.error("Coll centre id = " +thisCollectionCentre.getCollectionCentreId());
            log.error("Address id = " +thisCollectionCentre.getAddressId());
        }
            
        String ccFullName = thisCollectionCentre.getFullName();
        // Format for display - use full name and not the display name from the combo box (although they may well be the same)
        StringBuffer formattedAddress = new StringBuffer();
        formattedAddress.append("\t"+ccFullName+"\n");
        if (theAddress.getAddress1() != null && theAddress.getAddress1().length() >0) {
            formattedAddress.append("\t"+theAddress.getAddress1()+"\n");
        }
        if (theAddress.getAddress2() != null && theAddress.getAddress2().length() >0) {
            formattedAddress.append("\t"+theAddress.getAddress2()+"\n");
        }
        if (theAddress.getAddress3() != null && theAddress.getAddress3().length() >0) {
            formattedAddress.append("\t"+theAddress.getAddress3()+"\n");
        }
        if (theAddress.getAddress4() != null && theAddress.getAddress4().length() >0) {
            formattedAddress.append("\t"+theAddress.getAddress4()+"\n");
        }
        if (theAddress.getPostcode() != null && theAddress.getPostcode().length() >0) {
            formattedAddress.append("\t"+theAddress.getPostcode());
        }
        
        getHelper().setValue(formattedAddress.toString());
    }
    
}
