<%--
  - Title:       printpsrrequestdetails.jsp (jsp page fragment)
  -
  - Description: This file deisplays psrrequest details for printing.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: printpsrrequestdetails.jsp,v $
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:26  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.11  2003/06/16 13:34:57  cawleye
  - Bug fixes
  -
  - Revision 1.10  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.9  2003/03/26 16:54:48  fz0n8j
  - Bug fixes.
  -
  - Revision 1.8  2003/03/24 09:26:26  fz0n8j
  - Fixed print size
  -
  - Revision 1.7  2003/03/20 17:41:13  fz0n8j
  - *** empty log message ***
  -
  - Revision 1.6  2003/03/20 11:37:06  fz0n8j
  - Changed form actions to use the c:url tag
  -
  - Revision 1.5  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.4  2003/03/17 11:31:53  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.3  2003/03/13 15:58:27  fz0n8j
  - More Formatting changes, ecawley
  -
  - Revision 1.2  2003/03/12 18:10:08  fz0n8j
  - Now calls the bean for data, ecawley
  -
  - Revision 1.1  2003/03/12 15:31:24  fz0n8j
  - Added after name change
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
<table align="center" width="800" height="600" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td>
      <c:import url="${displaypsrrequestdetails}"/>
    </td>
  </tr>
</table>

