
package mseries.nationality;

import java.util.Collection;
import java.util.MissingResourceException;
import java.util.Vector;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;


/**
 * The business end of the NationalitySelector component. This class maintains the
 * state of the the component.
 * Analogous to MMonth.java.
 */
public class MNationality extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String nationality; // Working nationality

    protected Vector<MNationalityListener> listeners = new Vector<MNationalityListener>();

    private Vector<String> nationalityCode;
    
    private Vector<String> nationalityDescription;
    
    private final static String DELIMITER = ": ";
    
    private MNationalitySelectorConstraints panelConstraints = new MDefaultPullDownConstraints();
    

    public MNationality(Collection<String> nationalities) {
        this.nationalityCode = new Vector<String>(nationalities.size());
        this.nationalityDescription = new Vector<String>(nationalities.size());
        for (String nationality: nationalities) {
            if (nationality == null || nationality.length() < 1 + DELIMITER.length()) {
                //log.error("nationality string too short: " + nationality);
                continue;
            }
            int delimiterStart = nationality.indexOf(DELIMITER);
            if (delimiterStart > 0) {
                this.nationalityCode.add(nationality.substring(0, delimiterStart));
                this.nationalityDescription.add(nationality.substring(delimiterStart + DELIMITER.length()));
            } else {
                //log.error("cannot separate nationality code from description");
            }
        }
    }

    public void setNationality(String nationality) {
        if (nationality.length() >= 3) {
            this.nationality = nationality.substring(0, 3);
        } else {
            this.nationality = "";
        }
        // Fire MNationalityEvent as all data has changed
        notifyListeners(new MNationalityEvent(
                this, MNationalityEvent.NEW_NATIONALITY, this.nationality));
    }

    public String getNationality() {
        return nationality;
    }

    /**
     * Registers the listeners of the table changes
     * 
     * @param listener -
     *            MNationalityListener
     */
    public void addMNationalityListener(MNationalityListener listener) {
        listeners.addElement(listener);

    }

    /**
     * Removes the listener from the registered list of listeners
     * 
     * @param listener -
     *            MNationalityListener
     */
    public void removeMNationalityListener(MNationalityListener listener) {
        listeners.removeElement(listener);

    }

    /**
     * Notifies registered listeners of a change to the data model
     * 
     * @param event -
     *            a MNationalityEvent describing the change
     */
    private void notifyListeners(MNationalityEvent event) {
        // Pass these events on to the registered listener

        Vector list = (Vector) listeners.clone();
        for (int i = 0; i < list.size(); i++) {
            MNationalityListener l = listeners.elementAt(i);
            l.dataChanged(event);
        }

    }

    public void exitEvent() {
        notifyListeners(new MNationalityEvent(this, MNationalityEvent.SELECTED, nationality));
    }

    public void lostFocus() {
        notifyListeners(new MNationalityEvent(this, MNationalityEvent.EXITED, nationality));
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    public int getRowCount() {
        return nationalityCode.size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return nationalityCode.get(rowIndex);
        } else if (columnIndex == 1) {
            return nationalityDescription.get(rowIndex);
        }
        return null;
    }
    
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return getString("nationality.header.codecolumnname", "Code");
        } else if (columnIndex == 1) {
            return getString("nationality.header.nationalitycolumnname", "Nationality");
        } else {
            return "";
        }
    }
    
    public String getModelNationalityAt(int rowIndex) {
        return nationalityCode.get(rowIndex);
    }
    
    public int getModelNationalityIndex(String nationality) {
        return nationalityCode.indexOf(nationality);
    }
    
    public int getModelNationalityIndexStartingWith(String prefix) {
        int i = 0;
        for (String str : nationalityCode) {
            if (str.startsWith(prefix)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private String getString(String source, String def) {
        String newString;
        ResourceBundle rb = panelConstraints.getResourceBundle();
        if (rb == null) {
            return def;
        }
        try {
            newString = rb.getString(source);
        } catch (MissingResourceException e) {
            newString = def;
        }
        if (newString == null || newString.length() == 0) {
            return def;
        }
        return newString;
    }
}
