package mseries.Calendar;

import java.text.DateFormat;

public class MDateTimeValueEditor extends MDateValueEditor {
    public MDateTimeValueEditor() {
        df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
    }
}
