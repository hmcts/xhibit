<%--
  - Title:       normal.jsp
  -
  - Description: This page is the control page for the pages
  -              which update the cookie, its like normal but
  -              without a menu.  
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  -
  --%>

<%--
  - Declare the page error page to use
  --%>

<%@ page errorPage="error.jsp" %>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the skeleton url from the Pages resource bundle
  --%>
 
<fmt:bundle basename="Pages">
    <fmt:message key="skeleton" var="skeletonURL"/>
</fmt:bundle>

<%--
  - Set the default skeleton parameters then include skeleton  
  --%>

<c:set scope="request" var="skeletonHeaderKey" value="header"/>
<c:set scope="request" var="skeletonMenuKey" value="loginMenu"/><!-- blank menu -->
<c:set scope="request" var="skeletonFooterKey" value="footer"/>

<c:import url="${skeletonURL}"/>



