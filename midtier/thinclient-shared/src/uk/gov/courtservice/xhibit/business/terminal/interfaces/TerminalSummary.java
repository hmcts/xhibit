package uk.gov.courtservice.xhibit.business.terminal.interfaces;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment 2003
 * 
 * $Revision: 1.5 $
 * 
 * $Log: TerminalSummary.java,v $
 * Revision 1.5  2006/06/05 12:29:57  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:22:54 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.3 2003/06/13 10:47:49 fz0n8j Removed import and fully qualified Serilizable
 * reference to remove middlegen error.
 * 
 * Revision 1.2 2003/04/29 16:54:45 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.1.2.1 2003/04/29 15:01:51 fz0n8j Added classes used for selecting
 * terminals.
 * 
 * 
 */
public interface TerminalSummary extends java.io.Serializable {
    /**
     * @return the terminal name.
     */
    public String getName();

    /**
     * @return the terminal id.
     */
    public Integer getId();

    /**
     * @return the court room name.
     */
    public String getCourtRoomName();

    /**
     * @return the court site name.
     */
    public String getCourtSiteName();

}
