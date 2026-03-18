package uk.gov.courtservice.xhibit.business.services.results.saver;

// JDK
import java.util.ListResourceBundle;

import uk.gov.courtservice.xhibit.common.results.vos.CaseAppReasonSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.PleaSaveValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictSaveValue;

/**
 * <p>
 * Title: ResultsSaverBundle
 * </p>
 * <p>
 * Description: This class lists the VOs that can be transformed
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version 1.0
 */

public class ResultsSaverBundle extends ListResourceBundle {
    public ResultsSaverBundle() {
    }

    protected Object[][] getContents() {
        return new Object[][] { { PleaSaveValue.class.getName(), new PleaResultsSaver() },
                { VerdictSaveValue.class.getName(), new VerdictResultsSaver() },
                { CaseAppReasonSaveValue.class.getName(), new CaseAppReasonResultsSaver() },
                { DisposalSaveValue.class.getName(), new DisposalResultsSaver() } };
    }
}
