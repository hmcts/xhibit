package uk.gov.courtservice.xhibit.client.order.gui.entry.collectioncentrelist;

import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;

public interface CollectionCentreList {
    String[] getCollectionCentreNames();
    
    String[] getCollectionCentreDisplayNames();

    String[] getOrderTypes();

    boolean isCollectionCentreExists(String name);

    public Object getCollectionCentre(int collectionCentreId);
    
    public String getCollectionCentreIdFromDisplayName(String displayName);
    
    public XhbAddressBasicValue getAddressFromAddressId(int addressId);
}
