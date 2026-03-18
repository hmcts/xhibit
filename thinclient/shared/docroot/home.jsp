<%--
  - Title:       home.body.jsp (jsp page fragment)
  -
  - Description: This file is the main part of the home (default) page.
  -              Note that it uses the style sheet imported by skeleton.jsp. 
  -              Like all pages it should not be referenced directly but 
  -              through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: home.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:32  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.2  2003/05/08 15:57:00  fz0n8j
  - Fixed bug formatting
  -
  - Revision 1.1  2003/03/26 17:49:02  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.6  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.5  2003/03/19 20:13:31  fz0n8j
  - Added better response functionality
  -
  - Revision 1.4  2003/03/17 11:31:50  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.3  2003/03/11 16:31:42  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageTitle">
                                    <fmt:message key="home.text"/>
                                </td>
                                <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

