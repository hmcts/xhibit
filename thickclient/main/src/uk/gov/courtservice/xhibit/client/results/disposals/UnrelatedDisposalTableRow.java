package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

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

public class UnrelatedDisposalTableRow {
    private OffenceValue offenceValue;

    private DefendantValue defendantValue;

    private String count;

    private String offenceDescription;

    private String defendant;

    private String status;

    /** @todo Update with actual column valaes */
    public UnrelatedDisposalTableRow(OffenceValue ov, DefendantValue dv, String count, String offenceDescription,
            String defendant, String status) {
        this.offenceValue = ov;
        this.defendantValue = dv;
        this.count = count;
        this.offenceDescription = offenceDescription;
        this.defendant = defendant;
        this.status = status;
    }

    public OffenceValue getOffenceValue() {
        return offenceValue;
    }

    public DefendantValue getDefendantValue() {
        return defendantValue;
    }

    public String getCount() {
        return count;
    }

    public String getOffenceDescription() {
        return offenceDescription;
    }

    public String getDefendant() {
        return defendant;
    }

    public String getStatus() {
        return status;
    }

    public void setOffenceValue(OffenceValue offenceValue) {
        this.offenceValue = offenceValue;
    }

    public void setDefendantValue(DefendantValue defendantValue) {
        this.defendantValue = defendantValue;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setOffenceDescription(String offenceDescription) {
        this.offenceDescription = offenceDescription;
    }

    public void setDefendant(String defendant) {
        this.defendant = defendant;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}