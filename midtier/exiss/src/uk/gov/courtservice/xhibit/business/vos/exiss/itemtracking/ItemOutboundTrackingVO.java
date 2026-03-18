package uk.gov.courtservice.xhibit.business.vos.exiss.itemtracking;

import java.util.Date;

public class ItemOutboundTrackingVO {
    private Long trackingId;
    private Long itemId;
    private Date trackingDate;
    private String refTrackingStatusInternalCode;
    
    public ItemOutboundTrackingVO() {
        // Empty constructor
    }
    
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public Date getTrackingDate() {
        return trackingDate;
    }
    public void setTrackingDate(Date trackingDate) {
        this.trackingDate = trackingDate;
    }
    public Long getTrackingId() {
        return trackingId;
    }
    public void setTrackingId(Long trackingId) {
        this.trackingId = trackingId;
    }
    public String getRefTrackingStatusInternalCode() {
        return refTrackingStatusInternalCode;
    }
    public void setRefTrackingStatusInternalCode(String refTrackingStatusInternalCode) {
        this.refTrackingStatusInternalCode = refTrackingStatusInternalCode;
    }
}
