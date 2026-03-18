
package mseries.nationality;


/**
 * An Event class used to notify listeners that ther has been a change in the
 * nationality data model
 */
public class MNationalityEvent extends java.util.EventObject {

    private static final long serialVersionUID = 1L;

    public static final int SELECTED = 0;

    public static final int EXITED = 1;
    
    public static final int NEW_NATIONALITY = 2;

    private int type;

    private String nationality;

    public MNationalityEvent(Object source, int type, String nationality) {
        super(source);
        this.type = type;
        this.nationality = nationality;
    }

    public int getType() {
        return type;
    }

    public String getNewNationality() {
        return nationality;
    }
}
