<%--
  - Title:       holding.jsp (jsp page fragment)
  -
  - Description: This file is the main part of the holding page this is used in.
  -              development to indicate that a link is not broken (stops stack trace)
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: holding.jsp,v $
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
  - Revision 1.3  2003/03/17 11:31:50  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.2  2003/03/11 16:31:42  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="2" cellspacing="2" border="0">
    <tr>
        <td width="100%" class="holdingBodyText">
            <fmt:message key="holding.text">
                <fmt:param value="${requestScope.skeletonBodyKey}"/>
            </fmt:message>
        </td>
    </tr>
</table>
