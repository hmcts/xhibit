package uk.gov.courtservice.xhibit.client.results.appealresults;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: To implemented by Appeal Panels to populate the results save value
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
 * @author Rakesh Lakhani
 * @version $Id: AppealSaver.java,v 1.3 2006/06/05 12:31:32 bzjrnl Exp $
 */

public interface AppealSaver {
    public void populateResultsSaveValue(ResultsSaveValue resultsSaveValue) throws CSRecoverableException;
}