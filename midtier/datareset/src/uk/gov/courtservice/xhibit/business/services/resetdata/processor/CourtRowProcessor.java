package uk.gov.courtservice.xhibit.business.services.resetdata.processor;

/**
 * <p>Title: QueueRowProcessor</p>
 * <p>Description: Class to process the DefendantOnCase rows.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Steve Tully
 * @version 1.0
 */
import java.util.ArrayList;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;

public class CourtRowProcessor extends AbstractRowProcessor {

    private final ArrayList<CourtBasicValue> docList = new ArrayList<CourtBasicValue>();

    /**
     * Returns the data
     * 
     * @return DefendantOnCaseVO[]
     */
    public CourtBasicValue[] getCourt() {
        return docList.toArray(new CourtBasicValue[docList.size()]);
    }
    
    public ArrayList<CourtBasicValue> getDocList() {
        return docList;
    }

    /**
     * Implementation of row processor
     * 
     * @param row from the resultset
     */
    public void processRow(Row row) {
    	CourtBasicValue doc = new CourtBasicValue();

        // Populate the vo
    	doc.setCourtName(row.getString("court_name"));
        doc.setId(row.getInteger("court_id"));
        doc.setDisplayName(row.getString("display_name"));

        // Add the VO to the ArrayList
        docList.add(doc);
    }
}
