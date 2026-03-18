package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.xhibit.business.services.charge.UncodedOffenceInterface;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITDefaultTableModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class AddedOffencesModel extends XHIBITDefaultTableModel {
    public static final int CODE_COLUMN = 0;

    public static final int DESCRIPTION_COLUMN = 1;

    private String offenceCode;

    private String offenceDesc;

    private String uncodedOffenceCodeMask;

    public AddedOffencesModel(Object[] data) {
        super();
        setupTableHeaders();
        super.setData(data);
    }

    public Object getValueAt(int r, int c) {
        Object cellValue;

        if (_data.length <= 0)
            return null;

        switch (c) {
        case CODE_COLUMN:
            offenceCode = ((OffenceValue) _data[r]).getOffenceCode();
            cellValue = offenceCode.equalsIgnoreCase(UncodedOffenceInterface.UNCODED_OFFENCE_REFERENCE_CODE) ? uncodedOffenceCodeMask
                    : offenceCode;
            break;
        case DESCRIPTION_COLUMN:
            offenceDesc = ((OffenceValue) _data[r]).getOffenceDescription();
            cellValue = (offenceDesc == null ? "" : offenceDesc);
            break;
        default:
            cellValue = null;
            break;
        }
        return cellValue;
    }

    private void setupTableHeaders() {
        String resources = XhibitBundles.MaintainCharges;
        String[] columnNames = { ResourceBundleHelper.getResource(resources, "columnOffenceCode"),
                ResourceBundleHelper.getResource(resources, "columnOffenceDesc") };
        super.setColumnNames(columnNames);
        uncodedOffenceCodeMask = ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources,
                "uncodedOffenceCodeMask");
    }
}