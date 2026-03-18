package uk.gov.courtservice.xhibit.business.services.userterminal;

import uk.gov.courtservice.framework.exception.CSBusinessException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: TerminalNotFoundException.java,v 1.2 2005/02/11 16:36:21 sz0t7n
 *          Exp $
 */

public class TerminalNotFoundException extends CSBusinessException {
    private static final long serialVersionUID = 7820521530822106111L;

    private static final String TERMINAL_NOT_FOUND = "terminal.not.found";

    public TerminalNotFoundException(String terminalName) {
        super(TERMINAL_NOT_FOUND, new Object[] {}, "Terminal not found with name: " + terminalName);
    }
}