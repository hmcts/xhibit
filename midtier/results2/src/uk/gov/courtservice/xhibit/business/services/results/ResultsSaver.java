package uk.gov.courtservice.xhibit.business.services.results;

import uk.gov.courtservice.xhibit.common.results.vos.ResultsSaveValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This interface defines what the ResultsSaver can do.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @version $Id: ResultsSaver.java,v 1.6 2006/06/05 12:29:55 bzjrnl Exp $
 */
public interface ResultsSaver {
    public void preprocess(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException;

    public void save(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException;

    public void saveCrestKeys(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException;

    public void log(ResultsSaveValue resultsSaveValue, int index) throws ResultsControllerException;
}