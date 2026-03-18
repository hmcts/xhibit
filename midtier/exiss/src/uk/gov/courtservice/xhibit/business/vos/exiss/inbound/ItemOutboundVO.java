package uk.gov.courtservice.xhibit.business.vos.exiss.inbound;

import java.io.Serializable;
import java.util.Date;

public class ItemOutboundVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long   itemId;
    private String refTypeInternalCode;
    private String identifier;
    private String crestCourtId;
    private String description;
    private String clobData;
    private Date   itemCreated;
    private Date   itemExpires;
    
    public ItemOutboundVO () { }
    
    public ItemOutboundVO(String refTypeInternalCode, String identifier, String crestCourtId, String description, String clobData, Date itemCreated, Date itemExpires) {
        this(null, refTypeInternalCode, identifier, crestCourtId, description, clobData, itemCreated, itemExpires);
    }
    
    public ItemOutboundVO(Long itemId, String refTypeInternalCode, String identifier, String crestCourtId, String description, String clobData, Date itemCreated, Date itemExpires) {
        setItemId(itemId);
        setRefTypeInternalCode(refTypeInternalCode);
        setIdentifier(identifier);
        setCrestCourtId(crestCourtId);
        setDescription(description);
        setClobData(clobData);
        setItemCreated(itemCreated);
        setItemExpires(itemExpires);
    }
    
    public String getClobData() {
        return clobData;
    }
    public void setClobData(String clobData) {
        this.clobData = clobData;
    }
    public String getCrestCourtId() {
        return crestCourtId;
    }
    public void setCrestCourtId(String crestCourtId) {
        this.crestCourtId = crestCourtId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getRefTypeInternalCode() {
        return refTypeInternalCode;
    }
    public void setRefTypeInternalCode(String internalCode) {
        this.refTypeInternalCode = internalCode;
    }
    public Date getItemCreated() {
        return itemCreated;
    }
    public void setItemCreated(Date itemCreated) {
        this.itemCreated = itemCreated;
    }
    public Date getItemExpires() {
        return itemExpires;
    }
    public void setItemExpires(Date itemExpires) {
        this.itemExpires = itemExpires;
    }
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
