package datamigration1745.database.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

import datamigration1745.vos.HOPoliceForceVO;

/**
 * <p>
 * Title: HOPoliceForceRowProcessor
 * </p>
 * <p>
 * Description: Class to process the HO Police Force rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author GJS
 * @version 1.0
 */
public class HOPoliceForceRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<HOPoliceForceVO> hoPoliceForceList = new ArrayList<HOPoliceForceVO>();

    /**
     * Returns the data
     *
     * @return Collection
     */
    public HOPoliceForceVO[] getHoPoliceForceList() {
        return (HOPoliceForceVO[]) hoPoliceForceList.toArray(new HOPoliceForceVO[hoPoliceForceList.size()]);
    }

    /**
     * Implementation of row processor
     *
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        HOPoliceForceVO item = new HOPoliceForceVO();

        // Put the data into a VO
        item.setCourtId(row.getInteger("court_id"));
        item.setRefCode(row.getString("code"));

        // Add the VO to the ArrayList
        hoPoliceForceList.add(item);
    }

}
