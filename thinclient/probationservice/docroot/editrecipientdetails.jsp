<%--
  - Title:       editrecipientdetails.jsp (jsp page fragment)
  -
  - Description: This file allows editing of psr recipient details.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: editrecipientdetails.jsp,v $
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:25  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.8  2003/12/10 14:35:03  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.7  2003/05/08 15:35:36  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.6  2003/03/20 11:37:05  fz0n8j
  - Changed form actions to use the c:url tag
  -
  - Revision 1.5  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.4  2003/03/17 11:31:48  fz0n8j
  - Added revision cvs comments. ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<fmt:bundle basename="Pages">
    <fmt:message key="recipientdetailsform" var="recipientdetailsform"/>
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form method="post" name="editrecipientdetails" action="<c:url value="/updaterecipientdetails"/>">
              <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageTitle">
                                        <fmt:message key="editrecipientdetails.title"/>
                                    </td>
                                    <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <c:import url="${recipientdetailsform}"/>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="7" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageAction">
                                        <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                        <c:set scope="request" var="buttonRequestURL" value="/viewrecipientsummaries"/>
                                        <c:import url="${menuButtonURL}"/>
                                    </td>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageAction">
                                        <c:set scope="request" var="buttonTextKey" value="recipientdetails.form.submit"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editrecipientdetails')"/>
                                        <c:import url="${menuButtonURL}"/>
                                    </td>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

