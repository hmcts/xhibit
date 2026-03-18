package uk.gov.courtservice.xhibit.client.util.commonfunctions;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author unascribed
 * @version $Id: SelectAllFunction.java,v 1.3 2006/06/05 12:30:42 bzjrnl Exp $
 */

public interface SelectAllFunction {
    /**
     * When the user selects the menu Edit/Select all, the action will call this
     * method.
     * 
     * @throws CSRecoverableException
     */
    public void selectAll() throws CSRecoverableException;
}
