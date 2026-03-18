package datamigration1745;

import java.util.Calendar;
import java.io.Serializable;

/**
 * Class that represents the Data Migration stats
 */

public class DataMigrationObject {

	private Calendar startTime = Calendar.getInstance();

    private Calendar endTime = null;

    private Long elapsedTime = null;

    private Serializable object = null;

    private Integer size = null;
    
    private String objectName = null;
    
    private Integer objectId = null;

    public DataMigrationObject(Integer objectId, String objectName, Serializable object,Calendar startTime) {
        setObjectId(objectId);
        setObjectName(objectName);
        setObject(object);
        setStartTime(startTime);
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
	    this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Serializable getObject() {
        return object;
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
    
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}