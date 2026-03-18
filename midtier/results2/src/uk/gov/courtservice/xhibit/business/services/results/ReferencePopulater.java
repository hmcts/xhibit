package uk.gov.courtservice.xhibit.business.services.results;

// xhibit
import uk.gov.courtservice.xhibit.common.results.vos.ResultsReferenceValue;

/**
 * <p>
 * Title: ReferencePopulater
 * </p>
 * <p>
 * Description: Classes implementing this interface are used to populate
 * populate ResultsReferenceValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public interface ReferencePopulater {
    /**
     * Populate ResultsReferenceValue with specifc results information
     */
    public void populate(ResultsReferenceValue rrv) throws ResultsControllerException;
}
