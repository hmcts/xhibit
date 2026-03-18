/**
 * Created by IntelliJ IDEA.
 * User: qzd3k3
 * Date: Mar 13, 2003
 * Time: 1:45:07 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.framework.services.scheduling;

public interface Loadable {
    public int TIMEOUT = 100000;

    /**
     * This method is overloaded by subclasses to provide a callback method that
     * performs the loading task.
     */
    public void load();

    /**
     * This method is overloaded by subclasses to supply a name for the task for
     * logging purposes.
     */
    public String getName();

    /**
     * A callback method for the loader.
     * 
     * @param loaded
     */
    public void setLoaded(boolean loaded);

    /**
     * Method used to determine whether the job has loaded successfully
     * 
     * @return whether this Loadable has loaded successfully.
     */
    public boolean isLoaded();
}
