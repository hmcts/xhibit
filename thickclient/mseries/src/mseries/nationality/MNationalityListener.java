
package mseries.nationality;

/**
 * Interface to which any object must conform if it is to be informed of changes
 * to the nationality data model.
 */
public interface MNationalityListener {
    public void dataChanged(MNationalityEvent event);
}
