<%--
  - Title:       confirmissue.jsp (jsp page fragment)
  -
  - Description: This file displays psrrequest details for confirmation before printing.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: confirmissue.jsp,v $
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:24  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.5  2004/10/14 13:06:28  tzj8k5
  - PRE 305 - Missing dates entered when selecting a recipient
  -
  - Revision 1.4  2003/12/10 14:35:02  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.3  2003/09/26 10:47:37  xzmw8n
  - Added print button to page
  -
  - Revision 1.2  2003/03/26 16:54:47  fz0n8j
  - Bug fixes.
  -
  - Revision 1.1  2003/03/20 17:40:38  fz0n8j
  - Added to cvs
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
  - The html to be included in the main page
  --%>
<c:import url="${displaypsrrequestdetails}"/>
<table>
    <tr>
        <td class="psPrintText">
            <form class="psForm" action="<c:url value="/editpsrrequestdetails"/>" method="post" name="cancel">
            <c:set scope="request" var="buttonTextKey" value="cancel"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
            <c:import url="${menuButtonURL}"/>
            <INPUT type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
			<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
            <INPUT type="hidden" value="<c:out value="View2Edit"/>" name="hrgdate">
            <INPUT type="hidden" value="<c:out value="View2Edit"/>" name="nolaterdate">
            </form>
        </td><td>
            <form class="psForm" action="<c:url value="/issuerequest"/>" method="post" name="issue">
            <c:set scope="request" var="buttonTextKey" value="editpsrrequestdetails.issuerequest"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('issue')"/>
            <c:import url="${menuButtonURL}"/>
            <INPUT type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
			<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
            </form>
        </td>
		<td class="psPrintText">
            <c:url value="/printpsrrequestdetails" var="printpsrrequestdetails"/>
            <c:set scope="request" var="buttonTextKey" value="alt.print"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printpsrrequestdetails}?id=${requestScope.id}&objectid=${requestScope.objectid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
            <c:import url="${menuButtonURL}"/>
        </td>
    </tr>
</table>

