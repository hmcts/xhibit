<%--
  - Title:       error.body.jsp (jsp page fragment)
  -
  - Description: This file is the main part of the error page.
  -              Note that it uses the style sheet imported by skeleton.jsp.
  -              Like all pages it should not be referenced directly but
  -              through the pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  --%>

<%@ page isErrorPage="true" import="uk.gov.courtservice.xhibit.web.framework.util.ExceptionUtil,
                                    uk.gov.courtservice.framework.exception.CSException" %>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>

<%--
  - The html to be included in the main page
  --%>
<%--<html>
<head>
    <META http-equiv="Pragma" content="no-cache"/>
    <META http-equiv="Expires" content="-1"/>
    <script src="/Static/util.js">
    </script>
    <title>
    <fmt:message key="skeleton.title"/>
    :
    <fmt:message key="stacktrace.title"/>
    </title>
    <link href="/Static/style.css" rel="stylesheet" type="text/css"/>
</head>
    <body bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5"> --%>
    <table width="100%" cellpadding="2" cellspacing="2" border="0">
        <tr>
            <td width="100%" class="errorBodyText">
                <fmt:message key="error.text"/>
                <p>
                <%
                    Throwable cause = ExceptionUtil.getRootException((Exception)session.getAttribute("error"));
                    if (cause != null && cause instanceof CSException)
                    {
                        request.setAttribute("errorMessage", ExceptionUtil.buildMessageString(cause));
                    } else {
                        request.setAttribute("errorMessage", "");
                    }
                 %>

            <c:set var="errorMessage" value="${requestScope.errorMessage}"/>
            <c:choose>
                <c:when test="${errorMessage == ''}">
                    <fmt:message key="error.default"/>
                </c:when>
                <c:otherwise>
                <pre class="errorBodyText"><c:out value="${errorMessage}"/></pre>
                </c:otherwise>
            </c:choose>
                </p>
                <c:set scope="request" var="buttonFill" value="false"/>
                <c:set scope="request" var="buttonTextKey" value="error.hidedetails"/>
                <c:set scope="request" var="buttonRequestURL" value="errorsummary"/>
                <c:import url="${menuButtonURL}"/>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0">
        <tr>
            <td width="100%" class="errorBodyText">
                <p>
                    <pre class="errorStackTraceText">
                        <% ExceptionUtil.printStackTraces((Exception)session.getAttribute("error"), out); %>
                    </pre>
                </p>
            </td>
        </tr>
    </table>
<%--    </body>
</html> --%>