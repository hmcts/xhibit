package uk.gov.courtservice.xhibit.client.actions.search;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

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
 * @author unascribed
 * @version 1.0
 */

public interface SearchProcessHandler {
    public abstract void processResults(AbstractSearchAction asa) throws CSRecoverableException;
}