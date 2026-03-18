<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


  <input type="hidden" name="submitparam" value="./counselsigninoverview"/>
  
  <tr>
    <td colspan="26" class="cfTableHorizontalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/>
    </td>
  </tr>
  <tr>
  <td class="cfTableVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  <td>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
  <!--tr>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfFormLabel" colspan="22"><input type="button" value="OK" name="okBtn" class="cfFormButton" onclick="doSubmit('assign');"> <input type="button" value="Cancel" name="cancelBtn" class="cfFormButton" onclick="doSubmit('reset');"></td>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr-->
  <tr>
    <td colspan="26" class="psTableHorizontalBorder" />
  </tr>
  <tr>  
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" ><c:out value="${col1}"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" ><c:out value="${col2}"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" ><c:out value="${col3}"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" nowrap="true" ><c:out value="${col4}"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" align="center"><fmt:message key="assignlegalrep.caseNumber"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableHeader" align="center"><fmt:message key="assignlegalrep.legalReps"/></td>
     <td class="cfTableHeaderSpace" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfTableVerticalBorder" ><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr>
  <tr>
      <td colspan="26" class="psTableHorizontalBorder" />
  </tr>
<SCRIPT LANGUAGE="JavaScript">

    function printRow(col1, col2, col3, col4, caseNumber, legalRep)
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
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");      
      document.write("<td class='cfTableMainTop' nowrap='true' >");      
      document.write(caseNumber);      
      document.write("</td><td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");      
      document.write("<td class='cfTableVerticalBorder' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");      
      document.write("<td class='cfTableMainSpace' ><img src='/Static/images/blank.gif' width='1' height='1'/></td>");      
      document.write("<td class='cfTableMainTop' nowrap='true' >");      
      document.write(legalRep);           
      document.write("</td><td class='cfTableMainSpace' ></td><td class='cfTableVerticalBorder' ></td></tr>"); 
      document.write("<tr><td colspan='26' class='cfTableHorizontalBorder'><img src='/Static/images/blank.gif' width='1' height='1'/></td></tr>");   
   }
  </SCRIPT>


<SCRIPT LANGUAGE="JavaScript">  
  <c:forEach var="itemIndex" items="${requestScope.displayList}">  
    <c:out escapeXml="false" value="${itemIndex}"/>
  </c:forEach>
</SCRIPT>
<!--tr><td colspan="23" class="cfTableHorizontalBorder"><img src="/Static/images/blank.gif" width="1" height="1"/></td></tr-->
 <!--tr>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
     <td class="cfFormLabel" colspan="22"><input type="button" value="OK" name="okBtn" class="cfFormButton" onclick="doSubmit('assign');"> <input type="button" value="Cancel" name="cancelBtn" class="cfFormButton" onclick="doSubmit('reset');"></td>
     <td class="cfPageTitleVerticalSpace"><img src="/Static/images/blank.gif" width="1" height="1"/></td>
  </tr-->
</table>




