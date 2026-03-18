package uk.gov.courtservice.xhibit.client.order.gui.entry.collectioncentrelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.scheduling.AbstractLoadable;
import uk.gov.courtservice.framework.services.scheduling.AsynchronousLoader;
import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentre;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_collection_centre.XhbCollectionCentreBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.orders.CollectionCentreControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CollectionCentreListMidTier
 * </p>
 * <p>
 * Description:.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author SA
 */

public class CollectionCentreListMidTier extends AbstractLoadable implements CollectionCentreList {

    private static CollectionCentreListMidTier collectionCentreList;

    private CollectionCentreControllerBeanBusinessDelegate ccDelegate;
    
    private XhbCollectionCentreBasicValue[] collectionCentres;

    private Hashtable fullNameAndIdsHash = new Hashtable();
    
    private Hashtable displayNamesHash = new Hashtable();
    
    private Hashtable addressAndIdsHash = new Hashtable();
    
    private Hashtable allCollectionCentresHash = new Hashtable();

    private Logger log = CSServices.getLogger(CollectionCentreListMidTier.class);

    private String[] fullNameAndIdsStrArr;

    private String[] displayNamesStrArr;

    static {
        collectionCentreList = new CollectionCentreListMidTier();
    }

    /**
     * Constructor
     */
    public CollectionCentreListMidTier() {
        log.info("Initialising.");
        AsynchronousLoader.getInstance().addToLoader(this);

    }

    /**
     * Return an instance of collectionCentreListMidTier
     * 
     * @return collectionCentreListMidTier
     */
    public static CollectionCentreListMidTier getInstance() {
        return collectionCentreList;
    }

    /**
     * Load the reference data
     */
    public void load() {
        if (FunctionList.hasAccess(FunctionList.VViewOrder)) {
            ccDelegate = XhibitDelegateHelper.getCollectionCentreDelegate();
            initialiseTables();
            initialiseArrays();
        }
    }

    /**
     * Return a string representing the loader
     * 
     * @return the description of the loader
     */
    public String getName() {
        return "Court list reference data.";
    }

    /**
     * Initialise the arrays to hold the reference data.
     */
    private void initialiseArrays() {
        if (fullNameAndIdsHash != null) {
            fullNameAndIdsStrArr = tableToArray(fullNameAndIdsHash);
        }
        if (displayNamesHash != null) {
            displayNamesStrArr = tableToArray(displayNamesHash);
        }
    }

    /**
     * Initialise the HashTables to hold the reference data
     */
    private void initialiseTables() {

        try {
            collectionCentres = ccDelegate.getCollectionCentres();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        if (collectionCentres != null) {
            int collectionCentreLength = collectionCentres.length;
    
            log.debug("collectionCentreListMidTier length = " + collectionCentreLength);
    
            try {
                Integer addressId;
                XhbAddressBasicValue address;
                
                for (int i = 0; i < collectionCentreLength; i++) {
                    XhbCollectionCentreBasicValue currentCollectionCentre;
                    currentCollectionCentre = (XhbCollectionCentreBasicValue) collectionCentres[i];
                    Integer collectionCentreId = currentCollectionCentre.getCollectionCentreId();
        
                    try {
                        allCollectionCentresHash.put(collectionCentreId.intValue(), currentCollectionCentre);
                        fullNameAndIdsHash.put(currentCollectionCentre.getFullName() + " (" + collectionCentreId + ")", collectionCentreId.intValue());
                        displayNamesHash.put(currentCollectionCentre.getDisplayName(), collectionCentreId.intValue());
                        
                        // Get the address for this collection centre; addresses are displayed on the order too
                        addressId = currentCollectionCentre.getAddressId();
                        if (addressId != null) {
                            address = ccDelegate.getCollectionCentreAddress(addressId);
                            addressAndIdsHash.put(addressId.intValue(), address);
                        }
                    } catch (NumberFormatException n) {
                        n.printStackTrace();
                        log.error(n);
                        throw n;
                    }
                }
            } catch (Exception e) {
                log.error("Error with the list of collection centres.");
                e.printStackTrace();
            }
        } else {
            log.error("No collection centres returned");
            // Carry on anyway
        }

        if (displayNamesHash != null) {
            log.debug("DISPLAY COUNT :" + displayNamesHash.size());
        }
        if (fullNameAndIdsHash != null) {
            log.debug("ALL COLLECTION CENTRE COUNT :" + fullNameAndIdsHash.size());
        }
    }

    // Only Monetary Orders at present use Collection Centres
    private String[] orderTypes = { "MO" };

    
    /**
     * Retrun a String [] of the display names for the collection centres
     * 
     * @return
     */
    public String[] getDisplayNames() {
        return displayNamesStrArr;
    }

    /**
     * Return a String [] of the order types
     * 
     * @return
     */
    public String[] getOrderTypes() {
        waitOnLoad();
        return this.orderTypes;
    }

    /**
     * Return a string [] of the Collection Centre names
     * 
     * @return
     */
    public String[] getCollectionCentreNames() {
        waitOnLoad();
        return fullNameAndIdsStrArr;
    }
    
    /**
     * Return a string [] of the Collection Centre display names
     * 
     * @return
     */
    public String[] getCollectionCentreDisplayNames() {
        waitOnLoad();
        return displayNamesStrArr;
    }

    /**
     * Checks if a given name exists within the list.
     * 
     * @param name
     *            Collection Centre name.
     * @return True if exists.
     */
    public boolean isCollectionCentreExists(String name) {
        waitOnLoad();
        return displayNamesHash.containsKey(name);
    }

    private String[] tableToArray(Hashtable table) {
        String[] result = new String[table.size()];
        Enumeration e = table.keys();
        int i = 0;
        while (e.hasMoreElements()) {
            result[i++] = (String) e.nextElement();

        }
        Arrays.sort(result);
        return result;
    }

    /**
     * Return a Collection Centre Object given the Collection Centre id as a key
     * 
     * @param key
     *            The collection centre code
     * @return The collection centre
     */
    public Object getCollectionCentre(int collectionCentreId) {
        return allCollectionCentresHash.get(collectionCentreId);
    }
    
    /**
     * 
     * @param displayName
     * @return
     */
    public String getCollectionCentreIdFromDisplayName(String displayName) {
        return displayNamesHash.get(displayName).toString();
    }
    
    /**
     * 
     * @param addressId
     * @return
     */
    public XhbAddressBasicValue getAddressFromAddressId(int addressId) {
        return (XhbAddressBasicValue) addressAndIdsHash.get(addressId);
    }
}