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
  - @version $Id: skeletonwithmessages.jsp,v 1.4 2006/05/04 10:18:39 bzjrnl Exp $
  --%>
<%@ page import="uk.gov.courtservice.xhibit.web.framework.util.FrameworkException" %>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
    <fmt:message key="${requestScope.skeletonHeaderKey}" var="skeletonHeaderURL" scope="request"/>
    <fmt:message key="${requestScope.skeletonMenuKey}" var="skeletonMenuURL" scope="request"/>
    <fmt:message key="${requestScope.skeletonBodyKey}" var="skeletonBodyURL" scope="request"/>
    <fmt:message key="${requestScope.skeletonFooterKey}" var="skeletonFooterURL" scope="request"/>
</fmt:bundle>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", -1); //prevents caching at the proxy server
    String key = (String)request.getAttribute("skeletonHeaderKey");
    String url = (String)request.getAttribute("skeletonHeaderURL");
    if(url.equals("???" + key + "???")) {
        throw new FrameworkException("web.skeleton.PageNotFoundException", "Pages bundle resource " + key + " not found.");
    }
    key = (String)request.getAttribute("skeletonMenuKey");
    url = (String)request.getAttribute("skeletonMenuURL");
    if(url.equals("???" + key + "???")) {
        throw new FrameworkException("web.skeleton.PageNotFoundException", "Pages bundle resource " + key + " not found.");
    }
    key = (String)request.getAttribute("skeletonBodyKey");
    url = (String)request.getAttribute("skeletonBodyURL");
    if(url.equals("???" + key + "???")) {
        throw new FrameworkException("web.skeleton.PageNotFoundException", "Pages bundle resource " + key + " not found.");
    }
    key = (String)request.getAttribute("skeletonFooterKey");
    url = (String)request.getAttribute("skeletonFooterURL");
    if(url.equals("???" + key + "???")) {
        throw new FrameworkException("web.skeleton.PageNotFoundException", "Pages bundle resource " + key + " not found.");
    }
%>

<%--
  - The html for laying out the pages
  --%>
<html>
<head>
    <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js">
    </script>
    <title>
    <fmt:message key="skeleton.title"/>
    :
    <fmt:message key="${requestScope.skeletonTitleKey}"/>
    </title>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
<body onLoad='focusFirstFieldAndCheckMessages()' bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td>
            <c:import url="${requestScope.skeletonHeaderURL}"/>
        </td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td>
            <c:import url="${requestScope.skeletonMenuURL}"/>
        </td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td class="bodyHeight">
            <c:import url="${requestScope.skeletonBodyURL}"/>
        </td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
        <td >
            <c:import url="${requestScope.skeletonFooterURL}"/>
        </td>
        <td class="verticalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="horizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
</body>
</html>

