package uk.gov.courtservice.xhibit.business.services.results.populater;

import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.services.results.ResultsPopulater;
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: AbstractResultsPopulater
 * </p>
 * <p>
 * Description: Implement common ResultsPopulater technology.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: AbstractResultsPopulater.java,v 1.3 2005/01/25 08:01:00 tz0d5m
 *          Exp $
 */
public abstract class AbstractResultsPopulater implements ResultsPopulater {
    public abstract void populate(ResultsCompositeValue rcv) throws ResultsControllerException;

    public void populateSaveValues(ResultsSaveValue results, MoveResultsValue oldMoveValue,
            MoveResultsValue newMoveValue) throws ResultsControllerException {
    }
}