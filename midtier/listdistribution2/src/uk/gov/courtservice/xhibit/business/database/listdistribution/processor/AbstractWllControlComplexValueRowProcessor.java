package uk.gov.courtservice.xhibit.business.database.listdistribution.processor;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.entities.xhb_wll_control.XhbWllControlBasicValue;
import uk.gov.courtservice.xhibit.business.vos.listdistribution.WllControlComplexValue;

/**
 * <p>
 * Title: WllControlComplexValueRowProcessor
 * </p>
 * <p>
 * Description: Create an object representing the distribution list control
 * data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: AbstractWllControlComplexValueRowProcessor.java,v 1.4
 *          2005/12/01 15:20:48 bzjrnl Exp $
 */
public abstract class AbstractWllControlComplexValueRowProcessor extends AbstractRowProcessor {
    protected WllControlComplexValue createComplexValue(Row row) {
        return new WllControlComplexValue(createBasicValue(row), row.getString("document_type"), row
                .getString("document_title"), row.getString("language"), row.getString("country"), row
                .getInteger("major_schema_version"), row.getInteger("minor_schema_version"), row
                .getTimestamp("date_created"), row.getInteger("letter_ready_count"), row
                .getInteger("letter_faxed_count"), row.getInteger("letter_emailed_count"), row
                .getInteger("letter_print_required_count"), row.getInteger("letter_printed_count"), row
                .getInteger("letter_error_count"), row.getInteger("letter_deleted_count"), row
                .getInteger("letter_archived_count"));
    }

    private XhbWllControlBasicValue createBasicValue(Row row) {
        XhbWllControlBasicValue basicValue = new XhbWllControlBasicValue();
        basicValue.setWllControlId(row.getInteger("wll_control_id"));
        basicValue.setStatus(row.getString("status"));
        basicValue.setExpiryDate(row.getTimestamp("expiry_date"));
        basicValue.setLastUpdateDate(row.getTimestamp("last_update_date"));
        basicValue.setCreationDate(row.getTimestamp("creation_date"));
        basicValue.setCreatedBy(row.getString("created_by"));
        basicValue.setLastUpdatedBy(row.getString("last_updated_by"));
        basicValue.setVersion(row.getInteger("version"));
        basicValue.setXmlDocumentId(row.getInteger("xml_document_id"));
        return basicValue;
    }
}
