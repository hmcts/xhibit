<%--
 - Title:       loginbody.jsp
 -
 - Description: This page is included by the login page
 -
 - Copyright:   Copyright (c) 2003
 - Company:     EDS
 -
 - Author:      William Fardell, Xdevelopment LLP (2003)
 -
 - $Revision: 1.4 $
 - $Log: loginbody.jsp,v $
 - Revision 1.4  2006/05/04 10:18:39  bzjrnl
 - Change: TI901
 - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
 -
 - Revision 1.3  2005/04/27 08:26:56  bzjrnl
 - Manual Merge From BRANCH_7_X
 -
 - Revision 1.1.2.1  2005/04/25 13:37:33  bzjrnl
 - Changes to move jspc into the framework.
 -
 - Revision 1.3  2003/05/21 15:01:43  fz0n8j
 - Added message for password change.
 -
 - Revision 1.2  2003/05/08 15:57:00  fz0n8j
 - Fixed bug formatting
 -
 - Revision 1.1  2003/03/26 17:49:02  fz0n8j
 - Moved files to shared html dir
 -
 - Revision 1.5  2003/03/26 16:54:47  fz0n8j
 - Bug fixes.
 -
 - Revision 1.4  2003/03/24 08:23:25  fz0n8j
 - Added view daily list, and public display
 -
 - Revision 1.3  2003/03/19 21:54:14  fz0n8j
 - Allow context free
 -
 - Revision 1.2  2003/03/18 20:32:10  fz0n8j
 - Added temp user id
 -
 - Revision 1.1  2003/03/18 17:44:56  fz0n8j
 - Changed to create login pages
 -
 -
 --%>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - The html
  --%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form name="j_logon_form" class="psForm" method="POST" action="j_security_check">
                <!--
                  - Page Text
                  -->
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="<c:out value="${requestScope.loginTextClass}"/>">
                            <fmt:message key="${requestScope.loginTextKey}"/>
                        </td>
                        <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
                <!--
                - Fields
                -->
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormName">
                            <fmt:message key="logon.userfield"/>
                        </td>
                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormValue">
                            <input type="text" name="j_username">
                        </td>
                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormName">
                            <fmt:message key="logon.passwordfield"/>
                        </td>
                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormValue">
                            <input type="password" name="j_password" onKeyPress="submitOnEnter('j_logon_form',event)">
                        </td>
                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
                <!--
                  - Button                  
                  -->
                <fmt:bundle basename="Pages">
                    <fmt:message key="button" var="menuButtonURL"/>
                </fmt:bundle>

                <c:set scope="request" var="buttonTextKey" value="logon.button"/>
                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('j_logon_form')"/>
                <c:import url="${menuButtonURL}"/>
            </form>
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageMessage">
                                    <fmt:message key="changepassword"/>
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
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

