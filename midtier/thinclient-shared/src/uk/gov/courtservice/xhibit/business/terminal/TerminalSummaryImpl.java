package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;

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
 * $Log: TerminalSummaryImpl.java,v $
 * Revision 1.5  2006/06/05 12:29:57  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:22:53 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.3 2003/08/15 09:01:29 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.2 2003/04/29 16:54:45 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.1.2.1 2003/04/29 15:01:51 fz0n8j Added classes used for selecting
 * terminals.
 * 
 * 
 */
public class TerminalSummaryImpl implements TerminalSummary {

    private String name;

    private Integer id;

    private String roomName;

    private String siteName;
    
    private static final long serialVersionUID = -2568954144242106662L;

    public TerminalSummaryImpl(String newName, Integer newId, String newRoomName, String newSiteName) {
        name = newName;
        id = newId;
        roomName = newRoomName;
        siteName = newSiteName;
    }

    /**
     * @return the terminal name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the terminal id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the court room name.
     */
    public String getCourtRoomName() {
        return roomName;
    }

    /**
     * @return the court site name.
     */
    public String getCourtSiteName() {
        return siteName;
    }

}
