<%--
  - Title:       normal.jsp
  -
  - Description: This page is used to load the correct skeleton for the request
  -              this sits at the same level as the error page. This page unlike 
  -              other pages is referenced directly by the framework.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: normal.jsp,v $
  - Revision 1.3  2005/04/27 08:26:56  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:33  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.2  2003/04/30 10:03:01  fz0n8j
  - Added messaging to probation service. (EFC)
  -
  - Revision 1.1  2003/03/26 17:49:03  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.4  2003/03/18 17:44:56  fz0n8j
  - Changed to create login pages
  -
  - Revision 1.3  2003/03/17 11:31:52  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.2  2003/03/11 16:31:43  fz0n8j
  - Added CVS log comments - ecawley
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
    <fmt:message key="skeletonwithmessages" var="skeletonURL"/>
</fmt:bundle>

<%--
  - Set the default skeleton parameters then include skeleton  
  --%>

<c:set scope="request" var="skeletonHeaderKey" value="header"/>
<c:set scope="request" var="skeletonMenuKey" value="menu"/>
<c:set scope="request" var="skeletonFooterKey" value="footer"/>

<c:import url="${skeletonURL}"/>



