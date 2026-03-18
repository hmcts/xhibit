<%--                                                                              
  - Title:       buttonspacer.jsp (jsp page fragment)
  -
  - Description: This file should create exactly the same amount of space as a 
  -              normal button but be invisible
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: buttonspacer.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:31  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.1  2003/03/26 17:49:02  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.2  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.1  2003/03/18 17:44:56  fz0n8j
  - Changed to create login pages
  -
  - Revision 1.5  2003/03/17 11:31:47  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.4  2003/03/11 16:31:41  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>

<%--
  - The html to be included in the main page
  --%>
<table cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="5" class="buttonSpacerHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="buttonSpacerVerticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="buttonSpacerSpace"><img src="/Static/images/blank.gif"></td>
        <td class="buttonSpacerMain">&nbsp;</td>
        <td class="buttonSpacerSpace"><img src="/Static/images/blank.gif"></td>
        <td class="buttonSpacerVerticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="5" class="buttonSpacerHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

