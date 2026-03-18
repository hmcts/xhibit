package uk.gov.courtservice.xhibit.business.services.results.populater;

import uk.gov.courtservice.xhibit.business.database.results.ResultsDatabase;
import uk.gov.courtservice.xhibit.business.services.results.ReferencePopulater;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsReferenceValue;

/**
 * <p>
 * Title: RecordSheetDisposalsReferencePopulater
 * </p>
 * <p>
 * Description: This classe populates Reference data for Record Sheet Disposals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */
public class RecordSheetDisposalsReferencePopulater implements ReferencePopulater {
    /**
     * Populate ResultsReferenceValue with specifc results information
     */
    public void populate(ResultsReferenceValue rrv) throws ResultsControllerException {
        DisposalMenuReferenceValue root = ResultsDatabase.getReferenceDisposalMenuRoot(rrv.getCourtId(),
                DisposalMenuReferenceValue.RECORD_SHEET_MENU_GROUP);
        DisposalReferenceValue[] disposals = ResultsDatabase.getReferenceDisposals(rrv.getCourtId(),
                DisposalMenuReferenceValue.RECORD_SHEET_MENU_GROUP);

        rrv.setRecordSheetDisposalData(root, disposals);
    }
}
