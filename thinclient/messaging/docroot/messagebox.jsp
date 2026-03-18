<%--
  - Declare the page error page to use
  --%>

<%@ page errorPage="messageerror.jsp" %>

<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the skeleton url from the Pages resource bundle
  --%>
 
<fmt:bundle basename="Pages">
    <fmt:message key="messageskeleton" var="skeletonURL"/>
</fmt:bundle>

<%--
  - Set the default skeleton parameters then include skeleton  
  --%>

<c:set scope="request" var="skeletonHeaderKey" value="header"/>
<c:set scope="request" var="skeletonMenuKey" value="menu"/>
<c:set scope="request" var="skeletonFooterKey" value="footer"/>

<c:import url="${skeletonURL}"/>



