
package mseries.nationality;

import java.util.ListResourceBundle;

/*
 * Modelled on DateSelectorRB.java
 */
public class NationalitySelectorRB extends ListResourceBundle {

    String contents[][] = { 
            { "nationality.header.codecolumnname", "Code" }, 
            { "nationality.header.nationalitycolumnname", "Nationality" }, };

    public Object[][] getContents() {
        return contents;
    }
    
}
