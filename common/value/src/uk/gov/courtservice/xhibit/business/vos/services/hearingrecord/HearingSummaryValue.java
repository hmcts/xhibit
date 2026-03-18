package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import java.util.Collection;

import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;

/**
 * Provides meta-data about the HearingListSummaryValues. Attributes are a
 * Collection of HearingListSummaryValues, and an ExportAValue where the latter
 * provides information about the export status of the former. The ExportAValue
 * may be null, when the HearingListSummaryValues have yet to be exported.
 */
public class HearingSummaryValue implements HRValueObject {

    private ExportAValue exportAValue;
    private static final long serialVersionUID = -5398996193282026887L;
    
    private Collection hearingListSummaryValues;

    public ExportAValue getExportAValue() {
        return exportAValue;
    }

    public Collection getHearingListSummaryValues() {
        return hearingListSummaryValues;
    }

    public void setExportAValue(ExportAValue exportAValue) {
        this.exportAValue = exportAValue;
    }

    public void setHearingListSummaryValues(Collection hearingListSummaryValues) {
        this.hearingListSummaryValues = hearingListSummaryValues;
    }

}
