 <%--
  - Title:       login.jsp
  -
  - Description: This page is used by the jsp's form logon
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  -
  - $Revision: 1.3 $
  - $Log: login.jsp,v $
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:32  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.2  2003/05/01 08:47:04  fz0n8j
  - bug fix
  -
  - Revision 1.1  2003/03/26 17:49:02  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.2  2003/03/18 17:44:56  fz0n8j
  - Changed to create login pages
  -
  -
  --%>

<%--
  - Declare the error page to use
  --%>

<%@ page errorPage="error.jsp" %>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page url from the Pages resource bundle
  --%>
 
<fmt:bundle basename="Pages">
    <fmt:message key="skeleton" var="skeletonURL"/>
</fmt:bundle>

<%--
  - Set the skeleton parameters then include
  --%>

<c:set scope="request" var="skeletonTitleKey" value="logon.title"/>
<c:set scope="request" var="skeletonHeaderKey" value="header"/>
<c:set scope="request" var="skeletonMenuKey" value="loginMenu"/>
<c:set scope="request" var="skeletonBodyKey" value="loginBody"/>
<c:set scope="request" var="skeletonFooterKey" value="footer"/>

<%--
  - Set the skeleton parameters
  --%>

<c:set scope="request" var="loginTextClass" value="psPageTitle"/>
<c:set scope="request" var="loginTextKey" value="logon.text"/>

<%--
  - Include the skeleton
  --%>

<c:import url="${skeletonURL}"/>

