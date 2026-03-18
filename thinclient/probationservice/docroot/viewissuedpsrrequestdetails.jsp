<%--
  - Title:       viewissuedpsrrequestdetails.jsp (jsp page fragment)
  -
  - Description: This file displays psrrequest details.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: viewissuedpsrrequestdetails.jsp,v $
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:27  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.1  2003/04/02 10:35:29  fz0n8j
  - Seperate response and jsp so now there is no edit option on issued psrs.
  -
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<fmt:bundle basename="Pages">
    <fmt:message key="displaypsrrequestdetails" var="displaypsrrequestdetails"/>
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to displey the details
  --%>
<c:import url="${displaypsrrequestdetails}"/>
<table>
    <tr>
        <td class="psPrintText">
            <c:url value="/printpsrrequestdetails" var="printpsrrequestdetails"/>
            <c:set scope="request" var="buttonTextKey" value="alt.print"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printpsrrequestdetails}?id=${requestScope.id}&objectid=${requestScope.objectid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
            <c:import url="${menuButtonURL}"/>
        </td>
    </tr>
</table>
