package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;

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
 * @author Bal Bhamra
 * @version 1.0
 */

public class OffencePanelModel {
    private ResultsRowValue rrv;

    private DisposalController disposalController;

    public OffencePanelModel() {
    }

    public DisposalController getDisposalController() {
        return disposalController;
    }

    public void setDisposalController(DisposalController disposalController) {
        this.disposalController = disposalController;
    }

    public ResultsRowValue getResultRowValue() {
        return rrv;
    }

    public void setResultRowValue(ResultsRowValue rrv) {
        this.rrv = rrv;
    }
}