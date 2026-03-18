package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.Collection;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicDisplayValue;

/**
 * 
 * Abstract row processor for public display
 * 
 * @author pznwc5
 * 
 */
public abstract class PublicDisplayRowProcessor extends AbstractRowProcessor implements ColumnNames {

    /**
     * Returns the processed data
     * 
     * @return
     */
    abstract Collection getData();

    protected void populateCourtSiteRoomData(Row row, PublicDisplayValue value) {
        value.setCourtRoomName(row.getString(COURT_ROOM_NAME));
        value.setCourtSiteName(row.getString(COURT_SITE_NAME));
        value.setCourtSiteShortName(row.getString(COURT_SITE_SHORT_NAME));
        value.setCourtSiteCode(row.getString(COURT_SITE_CODE));
        value.setCrestCourtRoomNo(row.getInt(CREST_COURT_ROOM_NO));
    }

    /**
     * Populates a PublicDisplayValue
     * 
     * @param row
     * @return
     */
    protected void populateData(Row row, PublicDisplayValue value) {
        populateCourtSiteRoomData(row, value);
        value.setMovedFromCourtSiteShortName(row.getString(MOVED_FROM_CS_SHORT_NAME));
        value.setMovedFromCourtRoomName(row.getString(MOVED_FROM_COURT_ROOM_NAME));
        value.setCourtRoomId(row.getInt(COURT_ROOM_ID));
        value.setMovedFromCourtRoomId(row.getInt(MOVED_FROM_COURT_ROOM_ID));
        value.setNotBeforeTime(row.getTimestamp(NOT_BEFORE_TIME));
    }

}
