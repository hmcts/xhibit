package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.PublicNoticeValue;

/**
 * 
 * Abstract row processor for public display
 * 
 * @author pznwc5
 * 
 */
public class PublicNoticeProcessor extends AbstractRowProcessor implements ColumnNames {

    private final List<PublicNoticeValue> publicNotices = new ArrayList<PublicNoticeValue>();

    /**
     * Creates a public notice record
     */
    public void processRow(Row row) {
        PublicNoticeValue value = new PublicNoticeValue();
        value.setPublicNoticeDesc(row.getString(PUBLIC_NOTICE_DESC));
        value.setPriority(row.getInt(PRIORITY));
        value.setActive(row.getBoolean(IS_ACTIVE));

        publicNotices.add(value);
    }

    /**
     * Returns all the public notices
     * 
     * @return
     */
    public PublicNoticeValue[] getPublicNotices() {
        return publicNotices.toArray(new PublicNoticeValue[publicNotices.size()]);
    }

}
