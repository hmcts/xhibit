<%--
  - Title:     deletelegrep.jsp (jsp page fragment)
  -
  - Description: Presents gui elements to deletet representatives from a specified case.
  -
  - Copyright:   Copyright (c) 2004
  - Company:     EDS
  -
  - Author:      Kevin Buckthorpe
  - Data:   2 Jan 2004

  --%>

<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<script lang="javascript">
okBtnClicked = false;
cancelBtnClicked = false;

    function checkSubmit(button)
    {
          if( button == "ok" )
          {
            if (!okBtnClicked)
            {
                for(var i=0; i<document.deleterepsform.elements.length; ++i)
                {
                      if(document.deleterepsform.elements[i].type == "checkbox")
                      {
                                if (document.deleterepsform.elements[i].checked )
                                 {
                                        okBtnClicked = true;
                                        disableBtns();
                                        return true;
                                 }
                      }
                }
            }
            alert('Please select legal representative(s) first');
            return false;
          }
        if( button == "cancel" )
         {
            if (!cancelBtnClicked)
            {
                disableBtns();
                cancelBtnClicked = true;
                return true;
            }
         }
   }

    function doSubmit(button)
    {
       if (checkSubmit(button))
           {
                  document.deleterepsform.submitbutton.value = button;
                  document.deleterepsform.submit();
           }

    }

    function disableBtns()
    {
        deleterepsform.okBtn.disabled = true;
        deleterepsform.cancelBtn.disabled = true;
    }

</script>


<form name="deleterepsform" action="./completerepdelete" method="post">
<input type="hidden" name="submitbutton" value="">
<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">

<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td colspan="3" class="cfTableHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
    <tr><td class="cfTableVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr><td>
                <table cellpadding="0" cellspacing="0" border="0">
                    <tr><td colspan="3" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                    <tr><td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfPageTitle"><fmt:message key="deletelegalrep.title"/></td>
                        <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                    </tr>
                    <tr><td colspan="3" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                </table>
            </td></tr>
            <tr><td align="center">
                <table width="80%" cellpadding="0" cellspacing="0" border="0">
                    <tr><td colspan="21" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                    <tr>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeader"><fmt:message key="assignlegalrep.courtCol"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeader"><fmt:message key="assignlegalrep.timeCol"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeader"><fmt:message key="assignlegalrep.roleCol"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeader"><fmt:message key="assignlegalrep.defendantCol"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableHeader"><fmt:message key="assignlegalrep.caseNumber"/></td>
                        <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                    </tr>
                    <tr>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainTop"><c:out value="${courtname}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainTop"><c:out value="${time}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainTop"><c:out value="${role}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainTop"><c:out escapeXml="false" value="${defendant}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableMainTop"><c:out value="${case}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
                    </tr>
                    <tr><td colspan="21" class="cfTableHorizontalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                    <tr><td colspan="21" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                    <tr><td class="cfFormData" colspan="21"><fmt:message key="deletelegalrep.instruction"/></td></tr>
                    <tr><td colspan="21" class="cfPageTitleHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr>
                    <tr><td colspan="21" class="psFormValue">
                      <table width="100%">
                        <tr>
                          <c:forEach items="${removereps}"  var="line" varStatus="s">
                              <td class="psFormValue">
                                 <input type="checkbox" name="CHB_<c:out value="${s.count}"/>">
                                  <c:out  escapeXml="false" value="${line}"/>
                              </td>
                              <c:if test="${s.count % 3 == 0}">
                                </tr><tr>
                              </c:if>
                          </c:forEach>
                        </tr>
                        <tr><td colspan="3" align="right">
                          <input type="button" value="<fmt:message key="signinlegalrep.okBtn"/>" name="okBtn" class="cfFormButton" onclick="doSubmit('ok');">
                          <input type="button" value="<fmt:message key="signinlegalrep.cancelBtn"/>" name="cancelBtn" class="cfFormButton" onclick="doSubmit('cancel');">
                      </td>
                        </td></tr>
                      </table>
                    </td></tr>
                </table>
            </td></tr>
            </table>
        </td>
    </tr>
</table>
