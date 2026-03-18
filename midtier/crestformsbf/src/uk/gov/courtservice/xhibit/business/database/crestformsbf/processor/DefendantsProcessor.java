package uk.gov.courtservice.xhibit.business.database.crestformsbf.processor;

import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003)
 * 
 */

public class DefendantsProcessor extends AbstractRowProcessor {

    // column names
    private static final String DEFENDANT_ID = "defendant_id";

    private static final String FIRST_NAME = "first_name";

    private static final String MIDDLE_NAME = "middle_name";

    private static final String SURNAME = "surname";

    private ArrayList defendants = new ArrayList();

    /**
     * Instantiates the object
     * 
     * @param courtId
     */
    public DefendantsProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     */
    public void processRow(Row row) {
        Integer id = new Integer(row.getInt(DEFENDANT_ID));
        String firstName = row.getString(FIRST_NAME);
        String middleName = row.getString(MIDDLE_NAME);
        String surname = row.getString(SURNAME);

        CrestFormsBFDefendant defandant = new CrestFormsBFDefendant(id, firstName, middleName, surname);
        defendants.add(defandant);
    }

    /**
     * Returns the case ids
     * 
     * @return
     */
    public CrestFormsBFDefendant[] getDefendants() {
        CrestFormsBFDefendant[] defendantsarray = new CrestFormsBFDefendant[defendants.size()];
        for (int i = 0; i < defendantsarray.length; i++) {
            defendantsarray[i] = (CrestFormsBFDefendant) defendants.get(i);
        }
        return defendantsarray;
    }

}