<%--
  - Title:       skeleton.jsp
  -
  - Description: This file implements the structure of the pages, this allows for
  -              common positioning. This is particulary important when switching 
  -              bettween pages with similar headers and footers. Like all pages it 
  -              should not be referenced directly but through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: skeleton.jsp,v $
  - Revision 1.4  2006/05/04 10:18:39  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:57  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:34  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.3  2003/03/28 13:23:04  hzf3bb
  - added onLoad() call to place focus on first field of a form
  -
  - Revision 1.2  2003/03/27 09:24:18  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.1  2003/03/26 17:49:03  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.8  2003/03/26 16:54:48  fz0n8j
  - Bug fixes.
  -
  - Revision 1.7  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.6  2003/03/18 17:44:56  fz0n8j
  - Changed to create login pages
  -
  - Revision 1.5  2003/03/17 17:24:03  fz0n8j
  - Added no cache meta tags.
  -
  - Revision 1.4  2003/03/17 11:31:57  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.3  2003/03/11 16:31:43  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
    <fmt:message key="${requestScope.skeletonHeaderKey}" var="skeletonHeaderURL"/>
    <fmt:message key="${requestScope.skeletonMenuKey}" var="skeletonMenuURL"/>    
    <fmt:message key="${requestScope.skeletonBodyKey}" var="skeletonBodyURL"/>
    <fmt:message key="${requestScope.skeletonFooterKey}" var="skeletonFooterURL"/>
</fmt:bundle>

<%--
  - The html for laying out the pages
  --%>
<html>
<head>
    <META http-equiv="Pragma" content="no-cache"/>
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js"></script>
    <title><fmt:message key="skeleton.title"/>: <fmt:message key="${requestScope.skeletonTitleKey}"/></title>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
<body onLoad="focusFirstField()" bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td><c:import url="${skeletonHeaderURL}"/></td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td><c:import url="${skeletonMenuURL}"/></td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="bodyHeight"><c:import url="${skeletonBodyURL}"/></td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td ><c:import url="${skeletonFooterURL}"/></td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
</body>
</html>

