<%--
  - Title:       messageskeleton.jsp
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
        <td><c:import url="${skeletonBodyURL}"/></td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
</body>
</html>

