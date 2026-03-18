package datamigration1745.database.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

import datamigration1745.vos.CourtVO;

/**
 * <p>
 * Title: CourtRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Court rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Simon Gilmore
 * @version 1.0
 */
public class CourtRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<CourtVO> courtList = new ArrayList<CourtVO>();

    /**
     * Returns the data
     *
     * @return Collection
     */
    public CourtVO[] getCourts() {
        return courtList.toArray(new CourtVO[courtList.size()]);
    }

    /**
     * Implementation of row processor
     *
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        CourtVO item = new CourtVO();

        // Put the data into a VO
        item.setId(row.getInteger("court_id"));
        item.setDisplayName(row.getString("display_name"));
        item.setCrestCourtId(row.getString("crest_court_id"));
        item.setStatus(row.getString("status"));

        // Add the VO to the ArrayList
        courtList.add(item);
    }
}
