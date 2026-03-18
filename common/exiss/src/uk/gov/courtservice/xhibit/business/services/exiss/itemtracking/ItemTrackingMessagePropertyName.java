package uk.gov.courtservice.xhibit.business.services.exiss.itemtracking;

public enum ItemTrackingMessagePropertyName {
    ITEM_ID("XHBItemId"),
    INTERNAL_CODE("XHBInternalCode");

    private final String name;
    
    private ItemTrackingMessagePropertyName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }    
}
