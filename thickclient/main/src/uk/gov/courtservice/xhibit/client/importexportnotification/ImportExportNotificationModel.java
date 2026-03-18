package uk.gov.courtservice.xhibit.client.importexportnotification;

//Xhibit client framework
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ImportExportNotificationModel
 * </p>
 * <p>
 * Description: The model for Import Export Notification. This only contains a
 * reference to the application controller.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */
public class ImportExportNotificationModel implements Cloneable {

    // The application controller
    private XhibitApplicationController _xac;

    /**
     * Empty default constructor.
     */
    public ImportExportNotificationModel() {
    }

    // get and set for the application controller.
    public XhibitApplicationController getXac() {
        return _xac;
    }

    public void setXac(XhibitApplicationController param) {
        _xac = param;
    }

    /**
     * Clone method that will just call the super class's clone method.
     * 
     * @return Object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Clear this model. It will set the application controller to null.
     */
    public void clearmodel() {
        setXac(null);
    }
}