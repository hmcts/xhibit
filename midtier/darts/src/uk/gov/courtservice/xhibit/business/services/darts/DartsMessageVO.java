package uk.gov.courtservice.xhibit.business.services.darts;

import java.util.Date;

public class DartsMessageVO {
    
    private int id;
    private String _xhibitMessageCode;
    private String _exissMessageCode;
    private String _payload;
    private int retryCount;
    private Date nextRetryTime;
    private Date lastUpdateDate;
    private Date creationDate;
    
   
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DartsMessageVO[id=");
        builder.append(id);
        builder.append(",_xhibitMessageCode=");
        builder.append(_xhibitMessageCode);
        builder.append(",_exissMessageCode=");
        builder.append(_exissMessageCode);
        builder.append(",_payload=");
        builder.append(_payload);
        builder.append(",retryCount=");
        builder.append(retryCount);
        builder.append(",nextRetryTime=");
        builder.append(nextRetryTime);
        builder.append(",lastUpdateDate=");
        builder.append(lastUpdateDate);
        builder.append(",_exissMessageCode=");
        builder.append(creationDate);
        builder.append("]");
        return builder.toString();
    }
    
    public String get_exissMessageCode() {
        return _exissMessageCode;
    }
    public void set_exissMessageCode(String eMessageCode) {
        _exissMessageCode = eMessageCode;
    }
    public String get_payload() {
        return _payload;
    }
    public void set_payload(String _payload) {
        this._payload = _payload;
    }
    public String get_xhibitMessageCode() {
        return _xhibitMessageCode;
    }
    public void set_xhibitMessageCode(String xMessageCode) {
        _xhibitMessageCode = xMessageCode;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Date getNextRetryTime() {
        return nextRetryTime;
    }
    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    
 
}
