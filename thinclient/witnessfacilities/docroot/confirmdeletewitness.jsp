<%--
- Title:       confirmdeletewitness.jsp (jsp page fragment)
-
- Copyright:   Copyright (c) 2003
- Company:     EDS
-
- Author:      David Duncan
- $Revision: 1.4 $
- $Log: confirmdeletewitness.jsp,v $
- Revision 1.4  2006/05/04 10:18:40  bzjrnl
- Change: TI901
- Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
-
- Revision 1.3  2005/04/27 08:26:57  bzjrnl
- Manual Merge From BRANCH_7_X
-
- Revision 1.1.2.1  2005/04/25 13:37:38  bzjrnl
- Changes to move jspc into the framework.
-
- Revision 1.3  2003/05/08 15:36:08  fz0n8j
- bug fix (/> on table elements) and formatting
-
- Revision 1.2  2003/04/02 15:40:36  hzf3bb
- *** empty log message ***
-
--%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<fmt:bundle basename="Pages">
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
                                    <fmt:message key="confirmdeletewitness.title"/>
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
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="casetype"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="appearance"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="day"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="witnessname"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="witnessstatus"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="expectedarrivaltime"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormName">
                                    <fmt:message key="notes"/>
                                    :</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormValue">data</td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psFormError"></td>
                                <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td colspan="9" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
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
                                    <c:set scope="request" var="buttonRequestURL" value="/deletewitness"/>
                                    <c:import url="${menuButtonURL}"/>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                    <c:set scope="request" var="buttonTextKey" value="save"/>
                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('')"/>
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

