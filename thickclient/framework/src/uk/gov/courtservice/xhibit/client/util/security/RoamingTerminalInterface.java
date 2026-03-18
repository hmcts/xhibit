package uk.gov.courtservice.xhibit.client.util.security;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description: Interface to allow roaming terminals to be updated back into the
 * user session object
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: RoamingTerminalInterface.java,v 1.1 2004/12/09 15:21:17 sz0t7n
 *          Exp $
 */

public interface RoamingTerminalInterface {
    public void updateRoamingTerminalProperties(RoamingTerminalValue value);
}