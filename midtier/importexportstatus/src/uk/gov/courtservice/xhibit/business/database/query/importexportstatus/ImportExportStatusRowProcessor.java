package uk.gov.courtservice.xhibit.business.database.query.importexportstatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.ImportExportStatusVO;

/**
 * <p>
 * Title: ImportExportStatusRowProcessor
 * </p>
 * <p>
 * Description: Class to process the import/export status rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

class ImportExportStatusRowProcessor extends AbstractRowProcessor {
    // The logger for this class.
    private static final Logger log = CSServices.getLogger(ImportExportStatusRowProcessor.class);

    // Instance cache
    private final Collection data = new ArrayList();

    /**
     * Default constructor
     */
    ImportExportStatusRowProcessor() {
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        log.debug("ImportExportStatusRowProcessor.processRow() called");

        ImportExportStatusVO item = new ImportExportStatusVO();

        // Put the data into a VO
        item.setImportExportStatusId(getInteger(row, "id"));
        item.setTypeCode(getString(row, "type_code"));
        item.setStatusCode(getString(row, "status_code"));
        item.setMessage(getString(row, "message"));
        item.setCaseId(getInteger(row, "case_id"));
        item.setCourtId(getInteger(row, "court_id"));
        item.setDefendantOnCaseId(getInteger(row, "defendant_on_case_id"));
        item.setLastUpdateDate(getDate(row, "last_update_date"));
        item.setCreationDate(getDate(row, "creation_date"));
        item.setCreatedBy(getString(row, "created_by"));
        item.setLastUpdatedBy(getString(row, "last_updated_by"));
        item.setVersion(getInteger(row, "version"));

        // Add the VO to the ArrayList
        this.data.add(item);

        log.debug("ImportExportStatusRowProcessor.processRow() finished");
    }

    /**
     * Returns the data
     * 
     * @return Collection
     */
    Collection getData() {
        return data;
    }

    private Integer getInteger(Row row, String column) {
        return row.getInteger(column);
    }

    private String getString(Row row, String column) {
        return row.getString(column);
    }

    private Date getDate(Row row, String column) {
        return row.getTimestamp(column);
    }
}
