package uk.gov.courtservice.xhibit.business.services.resetdata.processor;

import java.util.ArrayList;

/**
 * <p>Title: StringRowProcessor</p>
 * <p>Description: Class to process the rows featuring one String.</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: CGI</p>
 * @author Scott Atwell
 * @version 1.0
 */

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.services.datareset.vo.ManagedCase;

public class CheckCaseAlreadyAddedResultsRowProcessor extends AbstractRowProcessor {

	private final ArrayList<String> retValue = new ArrayList<String>();

	public String[] getRetValueAsArray() {
        return retValue.toArray(new String[retValue.size()]);
    }
    
	public ArrayList<String> getRetValue() {
        return retValue;
    }

    /**
     * Implementation of row processor
     * 
     * @param row from the resultset
     */
    public void processRow(Row row) {
    	retValue.add(row.getString("v_recordexists"));
    }
}
