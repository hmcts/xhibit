package uk.gov.courtservice.xhibit.business.services.results;

// xhibit
import uk.gov.courtservice.xhibit.common.results.vos.MoveResultsValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title: Base class Results for Populating results
 * </p>
 * <p>
 * Description: Contains method called to populate ResultsCompositeValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public interface ResultsPopulater {
    /**
     * Populate ResultsCompositeValue with specifc results information
     */
    public void populate(ResultsCompositeValue rcv) throws ResultsControllerException;

    /**
     * Populate ResultsSaveValue with specifc results information, currently
     * used for joinder indictments
     */

    public void populateSaveValues(ResultsSaveValue results, MoveResultsValue oldMoveValue,
            MoveResultsValue newMoveValue) throws ResultsControllerException;
}