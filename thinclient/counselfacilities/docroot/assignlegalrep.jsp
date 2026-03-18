<%--
  - Title:       assignlegalrep.jsp (jsp page fragment)
  -
  - Description: This file displays grid that allows ....
  -
  -  Modes:
  -  (I) assign (+ sorted/reordered col views)
  -  (II) overview  (+ ?)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Frederik Vandendriessche
  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>



<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>

<form name="xhibitForm" method="POST" action="./assignlegrep">
<input type="hidden" name="trxCode" value=""/>
<input type="hidden" name="assignlegalrepScreenMode" value==""/>
<input type="hidden" name="assignlegalrepColumnSort" value==""/>
<input type="hidden" name="onlyRoom" value = ""/>
<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
     <!--start Table 1-->
     <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr><td colspan="3" class="cfTableHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
            <tr><td class="cfTableVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                <td>
                    <!--start table 2-->
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <!--start table3-->
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td colspan="3" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfPageTitle">
                                        <c:choose>
                                         <c:when test="${trxCode == 'DISPLAY4INPUT' }">
                                          <fmt:message key="assignlegalrep.title"/>
                                         </c:when>
                                         <c:otherwise>
                                          <fmt:message key="assignlegalrep.overviewTitle"/>
                                         </c:otherwise>
                                         </c:choose>
                                        </td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                </table>
                                <!--end table 3-->
                            </td>
                        </tr>
                    </table>
                    <!--end table 2-->

                    <!--start table 4-->
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                            <!--start table 5-->
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr><td colspan="4" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfFormLabel"><fmt:message key="assignlegalrep.sortBy"/></td>
                                        <td class="cfFormData">
                                            <input type="button" value="<fmt:message key="assignlegalrep.courtRoomBtn" />" name="courtModeBtn"     class="cfFormButton" onclick="doSubmit('sortCourt');"/>
                                            <input type="button" value="<fmt:message key="assignlegalrep.defendantBtn" />" name="defendantModeBtn" class="cfFormButton" onclick="doSubmit('sortDefendant');"/>
                                        </td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfFormLabel"><fmt:message key="assignlegalrep.selectCourtRoom"/></td>

                                        <td class="cfFormData">
                                            <select size="1" name="courtNumber" class="cfFormDropDown" onchange="doSubmit2(this.value)">
                                                <option value="0"><fmt:message key="assignlegalrep.selectOption"/></option>
                                                <c:forEach var="index" items="${sessionScope.Key_CourtRooms_Collection}">
                                                 <option value='<c:out value="${index[0]}" />' <c:if test="${index[0] eq sessionScope.onlyRoom}">SELECTED</c:if>> <c:out value="${index[2]}"/> </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfFormLabel" colspan="2"><hr noshade="true" size="1"/></td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <c:if test="${trxCode == 'DISPLAY4INPUT' }">
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfFormLabel"><fmt:message key="assignlegalrep.fullName"/></td>
                                        <td class="cfFormData"><input type="text" name="legalRep" size="20" class="cfFormTextBoxRead" readonly="readonly" value="<c:out value="${sessionScope.Key_LegalRepresentative.fullName}"/>"/></td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    <tr>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                        <td class="cfFormLabel" colspan="2"><hr noshade="true" size="1"/></td>
                                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                                    </tr>
                                    </c:if>
                                    <tr><td colspan="4" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                                </table>
                                <!--end table 5-->
                            </td>
                        </tr>
                    </table>
                    <!--end table 4-->
                </td>
                <td class="cfTableVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
            </tr>
            <tr>
                <td colspan="3" class="cfTableHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
            </tr>
            <c:choose>
            <c:when test="${mode == 'signin' }">
                <c:import url="signin.jsp"/>
             </c:when>
             <c:otherwise>
                <c:import url="overview.jsp"/>
             </c:otherwise>
             </c:choose>

        </table>

        <!--end table 1-->
    </form>

    <script language="JavaScript">
function doSubmit2(courtroomid)
{
//  if (courtroomid > 0)
//  {
        xhibitForm.onlyRoom.value = courtroomid;
        xhibitForm.action                         = xhibitForm.submitparam.value;;
        xhibitForm.assignlegalrepScreenMode.value = "<c:out value="${trxCode}"/>"
        xhibitForm.trxCode.value="<c:out value="${trxCode}"/>";
        xhibitForm.submit();
//  }
}

assignBtnClicked = false;
sortCourtBtnClicked = false;
sortDefBtnClicked = false;
resetBtnClicked = false;

function doSubmit(action)
{
    if (action == "assign")
    {

        if (checkSubmit())
        {
            if (!assignBtnClicked)
            {
                assignBtnClicked = true;
                disableBtns();
                xhibitForm.action        = "./assignlegrep";
                submitForm();
            }
        }
    }
    else if (action == "sortCourt")
    {

        if (!sortCourtBtnClicked)
        {
            sortCourtBtnClicked = true;
            disableBtns();
            xhibitForm.action                         = xhibitForm.submitparam.value;
            xhibitForm.assignlegalrepColumnSort.value = "1"
            xhibitForm.assignlegalrepScreenMode.value = "<c:out value="${trxCode}"/>"
            submitForm();
        }

    }
    else if (action == "sortDefendant")
    {

        if (!sortDefBtnClicked)
        {
            sortDefBtnClicked = true;
            disableBtns();
            xhibitForm.action                         = xhibitForm.submitparam.value;
            xhibitForm.assignlegalrepColumnSort.value = "4"
            xhibitForm.assignlegalrepScreenMode.value = "<c:out value="${trxCode}"/>"
            submitForm();
        }

    }
    else if (action == "reset")
    {

        if (!resetBtnClicked)
        {
            resetBtnClicked = true;
            disableBtns();
            xhibitForm.action = "./cancelselection"
            xhibitForm.reset( );
            submitForm();
        }
    }

}

function disableBtns()
{
    xhibitForm.courtModeBtn.disabled = true;
    xhibitForm.defendantModeBtn.disabled = true;
    if (xhibitForm.okBtn1 != null)
    {
        xhibitForm.okBtn1.disabled = true;
        xhibitForm.okBtn2.disabled = true;
        xhibitForm.cancelBtn1.disabled = true;
        xhibitForm.cancelBtn2.disabled = true;
    }
}

function submitForm()
{

    xhibitForm.trxCode.value="<c:out value="${trxCode}"/>";
    xhibitForm.submit( );
}

function checkSubmit()
{
    for(var i=0; i<document.xhibitForm.elements.length; ++i)
    {
        if(document.xhibitForm.elements[i].type == "checkbox")
        {
            if (document.xhibitForm.elements[i].checked )
            {
                return true;
            }
        }
    }
    alert('Please make selection before assigning Legal Representative(s)');
    return false;
}

</script>