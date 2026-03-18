package uk.gov.courtservice.xhibit.business.vos.exiss.inbound;

import java.io.Serializable;
//import java.sql.Date;
import java.util.Date;

/**
 * Value object for transporting data to and from a data base manager
 * @author szn20z
 *
 */
public class ItemInboundVO implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long   itemId;
    private String message;
    private Date   dateCreated;

    public ItemInboundVO(String message) {
        setMessage(message);
        setItemId(null);
        setDateCreated(null);
    }
    public ItemInboundVO(Long itemId, String message) {
        setItemId(itemId);
        setMessage(message);
        setDateCreated(null);
    }
    public ItemInboundVO(Long itemId, String message, Date dateCreated) {
        setItemId(itemId);
        setMessage(message);
        setDateCreated(dateCreated);
    }
    
    /**
     * Empty constructor
     */
    public ItemInboundVO() { }
    
    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
