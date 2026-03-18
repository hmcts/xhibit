<%--
  - Title:       error.jsp
  -
  - Description: This page is used to show information about an error
  -              this sits at the same level as the controller page. This page unlike 
  -              other pages is referenced directly by the framework.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      William Fardell, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: messageerror.jsp,v $
  - Revision 1.3  2005/04/27 08:26:54  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:22  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.1  2003/05/15 13:38:55  fz0n8j
  - Added to cvs.
  -
  - Revision 1.1  2003/03/26 17:49:02  fz0n8j
  - Moved files to shared html dir
  -
  - Revision 1.4  2003/03/18 17:44:56  fz0n8j
  - Changed to create login pages
  -
  - Revision 1.3  2003/03/17 11:31:48  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.2  2003/03/11 16:31:41  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>

<%--
  - Declare the page as an error page
  --%>
<%@ page isErrorPage="true" %>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page url from the Pages resource bundle
  --%>
 
<fmt:bundle basename="Pages">
    <fmt:message key="messageskeleton" var="skeletonURL"/>
</fmt:bundle>

<%--
  - Set the skeleton parameters then include skeleton  
  --%>

<c:set scope="request" var="skeletonTitleKey" value="error.title"/>
<c:set scope="request" var="skeletonHeaderKey" value="header"/>
<c:set scope="request" var="skeletonMenuKey" value="menu"/>
<c:set scope="request" var="skeletonBodyKey" value="error"/>
<c:set scope="request" var="skeletonFooterKey" value="footer"/>

<c:import url="${skeletonURL}"/>




