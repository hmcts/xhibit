package uk.gov.courtservice.xhibit.client.util.commonfunctions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;

/**
 * <p>
 * Title: Print interface for panels that print using the print icon on the XAC
 * </p>
 * <p>
 * Description: Note: replaces CommonFunctions interface
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: PrintFunction.java,v 1.4 2006/06/05 12:30:42 bzjrnl Exp $
 */

public interface PrintFunction {
    /**
     * @return true if the print action should automatically create a PDF
     *         document
     */
    public boolean autoSaveToFile();

    /**
     * When the user selects print, the action will call this method.
     * 
     * @return a string containing xslFO formatted text
     */
    public String[] print() throws UserCancelException, CSRecoverableException;

}