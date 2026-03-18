<%--
  - Title:       print.jsp
  -
  - Description: This file delivers a jsp for printing.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: print.jsp,v $
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:26  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.3  2003/03/24 16:45:22  fz0n8j
  - Added/Modified for Email functionality.
  -
  - Revision 1.2  2003/03/21 18:35:56  fz0n8j
  - more print functionality
  -
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
    <fmt:message key="${requestScope.skeletonBodyKey}" var="skeletonBodyURL"/>
</fmt:bundle>

<html>
<head>
    <META http-equiv="Pragma" content="no-cache"/>
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js"></script>
    <title>Print</title>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
<body bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5" onload="javascript:window.print()">
<c:import url="${skeletonBodyURL}"/>
</body>
</html>

