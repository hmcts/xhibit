package uk.gov.courtservice.xhibit.business.vos.messagebroker;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>Title: QueueVO</p>
 * <p>Description: A VO that describes a Queue.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
public class QueueVO extends CSAbstractValue {
    private static final long serialVersionUID = 1L;
    
    private String jndiName;
    private String description;

    public QueueVO(Integer queueId, String jndiName, String description) {
        setId(queueId);
        setJndiName(jndiName);
        setDescription(description);
    }
    
    /**
     * Empty constructor
     */
    public QueueVO() { }

    public String getJndiName() { return this.jndiName; }
    public String getDescription() { return this.description; }

    public void setJndiName(String param) { this.jndiName = param; }
    public void setDescription(String param) { this.description = param; }
    
    /**
     * Return a debug object containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Queue[jndiName=");
        builder.append(jndiName);
//        builder.append(",description=");
//        builder.append(description);
        builder.append("]");
        return builder.toString();
    }
    
   
    
    /**
     * Return a debug string containing info about the rules
     */
    public static String valueOf(QueueVO[] queues) {
        if(queues == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder(); 
        builder.append("{");
        if (0 < queues.length) {
            builder.append(queues[0]);
            for (int i = 1; i < queues.length; i++) {
                builder.append(",");
                builder.append(queues[i]);
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
