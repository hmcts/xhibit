package uk.gov.courtservice.xhibit.client.courtlog;

/**
 * This interface defines the methods which must be implementedby any class
 * wishing to receive notifications from the TakenIntoConsiderationTableModel
 * 
 * This listener was introduced as a mechanism for relaying custom events from
 * the table model to its view so that the model doesn't have to do anything
 * that should really be done by the listener (ie view)
 * 
 * @author Jon Powell (Electronic Data Systems)
 * @date 22-Aug-2003
 */
public interface TakenIntoConsiderationTableModelListener {
    /**
     * fired when the user enters a value which in non-numeric or too long
     * 
     * @param defendantName
     *            the name of the defendant for whom the user entered an invalid
     *            value
     */
    public void invalidTicValue(String defendantName);
}
