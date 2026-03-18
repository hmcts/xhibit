 <%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<script lang="javascript">
removeLegRepBtnClicked = false;
function removeLegReps(colId)
{
    if (!removeLegRepBtnClicked)
    {
        removeLegRepBtnClicked = true;
        document.xhibitForm.action = "./deletelegrep";
        document.xhibitForm.delColId.value=colId
        document.xhibitForm.submit();
    }
}
</script>

  <input type="hidden" name="submitparam" value="./counselsignin"/>
  <input type="hidden" name="legalRepId" value = "<c:out value="${legalRepId}"/>"/>
  <input type="hidden" name="messageselect" value="<option value='0'><fmt:message key='assignlegalrep.selectOption'/></option>
                                                    <fmt:setLocale value='en_GB'/>
                                                    <fmt:bundle basename='XHIBITCourtClerkInformationMessages'>
                                                    <c:forEach begin='1' end='12' var='indexer'>
                                                    <option value='<c:out value='${indexer}'/>'><fmt:message key='${indexer}'/></option></c:forEach>
                                                    </fmt:bundle></select><br>">
 <input type="hidden" name="delColId" value="-1">
  <tr>
    <td colspan="26" class="cfTableHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/>
    </td>
  </tr>
  <tr>
  <td class="cfTableVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  <td>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
  <tr>
    <td class="cfFormData" colspan="26"><fmt:message key="signinlegalrep.clickSaveMsg"/></td>
  </tr>
  <tr>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfFormLabel" colspan="26"><input type="button" value="Save" name="okBtn1" class="cfFormButton" onclick="doSubmit('assign');"> <input type="button" value="Cancel" name="cancelBtn1" class="cfFormButton" onclick="doSubmit('reset');"></td>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr>
  <tr>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" rowspan="2"><c:out value="${col1}"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" rowspan="2"><c:out value="${col2}"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" rowspan="2"><c:out value="${col3}"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" nowrap="true" rowspan="2"><c:out value="${col4}"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" nowrap="true" rowspan="2"><c:out value="${col5}"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" rowspan="2"><fmt:message key="assignlegalrep.caseNumber"/></td>
     <td class="cfTableHeaderSpace" rowspan="2><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" colspan="1"><fmt:message key="assignlegalrep.ccInfo"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" rowspan="2"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr>
  <!-- end first row of table headers -->
  <tr>
  <!--td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td-->
     <td class="cfTableHeader" colspan="1"><fmt:message key="assignlegalrep.legalReps"/></td>
  </tr>
<SCRIPT LANGUAGE="JavaScript">

    function printRow(col1, col2, col3, col4, selectId, disable, caseNumber, legalRep, colId)
   {
      document.write("<tr><td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write(col1);
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write(col2);
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write(col3);
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write(col4);
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' rowspan='2'><p align='center'><input type='checkbox' name='select_");
      document.write(selectId);
      document.write("' onclick='docheckbox()' ");
      document.write(disable);
      document.write("></p></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write(caseNumber);
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");
      document.write("<td class='cfTableMainTop' >");
      document.write("<select size='1' name='ccInfo_");
      document.write(selectId);
      document.write("' class='cfFormDropDown' ");
      document.write(disable);
      document.write(">");
      document.write(document.xhibitForm.messageselect.value);
      document.write("<br>");
      document.write(legalRep);
      document.write("</td><td class='cfTableMainSpace'>");
       if(legalRep.length > 0 && legalRep != "Please register for this case with the usher in court")
      { document.write("<a href=\"javascript:removeLegReps('");
        document.write(colId);
        document.write("')\"><IMG src='/Static/images/deleteicon.gif' alt='Delete' border='0'></a></td>");
      }
      else
      {
         document.write("</td>");
      }
      document.write("<td class='cfTableMainSpace' ></td><td class='cfTableVerticalBorder' ></td></tr>");
      document.write("<tr><td colspan='27' class='cfTableHorizontalBorder'><img src='/Static/images/blank.gif' width='1' height='1'/></td></tr>");
   }
  </SCRIPT>


<SCRIPT LANGUAGE="JavaScript">
  <c:forEach var="itemIndex" items="${displayList}">
    <c:out escapeXml="false" value="${itemIndex}"/>
  </c:forEach>
</SCRIPT>
<!--tr><td colspan="26" class="cfTableHorizontalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr-->
 <tr>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfFormLabel" colspan="26"><input type="button" value="Save" name="okBtn2" class="cfFormButton" onclick="doSubmit('assign');"> <input type="button" value="Cancel" name="cancelBtn2" class="cfFormButton" onclick="doSubmit('reset');"></td>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr>
</table>

<SCRIPT LANGUAGE="JavaScript">
function docheckbox()
{
    xhibitForm.okBtn1.disabled = false;
    xhibitForm.okBtn2.disabled = false;
}
</SCRIPT>
