package uk.gov.courtservice.xhibit.business.database.results.processor;

// JDK
import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalLineReferenceValue;

/**
 * <p>
 * Title: RefDisposalLineProcessor
 * </p>
 * <p>
 * Description: Process the disposal line reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class RefDisposalLinesProcessor extends AbstractRowProcessor {

    /**
     * List of of line items keyed by lineItemId
     */
    private final List lineList = new ArrayList();

    /**
     * Process the row storing the DispsoalLineReferenceValue in the list
     * 
     * @param row
     *            the row to process
     */
    public void processRow(Row row) {
        lineList.add(new DisposalLineReferenceValue(row.getInteger("ref_disposal_line_id"), row
                .getInteger("ref_disposal_type_id"), row.getString("disposal_code"),
                row.getInteger("template_version"), row.getInteger("dil_seq_no"), row.getString("data"), row
                        .getString("input_flag"), row.getString("screen_print"), row.getString("form_print"), row
                        .getString("dbdestin"), row.getString("prompt"), row.getString("format"), row
                        .getString("mandatory"), row.getString("dbsource"), row.getString("validation"), row
                        .getString("multiple_choice"), row.getString("mcgroup1"), row.getString("mcgroup2"), row
                        .getInteger("char_max"), row.getString("line_insert")));
    }

    /**
     * Access the Line List
     * 
     * @return the line list
     */
    public List getLineList() {
        return lineList;
    }
}