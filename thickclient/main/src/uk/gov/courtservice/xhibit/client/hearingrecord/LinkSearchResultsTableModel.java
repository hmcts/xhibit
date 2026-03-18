package uk.gov.courtservice.xhibit.client.hearingrecord;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sherie De Silva
 * @version 1.0
 */

public class LinkSearchResultsTableModel extends XHIBITTableModel {
    HearingRecordModel model;

    public LinkSearchResultsTableModel(HearingRecordModel model) {
        super();
        this.model = model;

        setColumnNames(new String[] { XHIBITConstant.getResource(XhibitBundles.HearingRecord, "hearing"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "startDate"),
                XHIBITConstant.getResource(XhibitBundles.HearingRecord, "endDate") });

        if ((model.getLinkResultsTableData() != null) && (!model.getLinkResultsTableData().isEmpty())) {
            this.setData(model.getLinkResultsTableData());
        }
    }
}