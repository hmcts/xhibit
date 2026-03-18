<%--
  - Title:       confirmassignment.jsp (jsp page fragment)
  -
  - Description: This file displays a confirmation message
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Laurent Bossard (2003)
  - Date:        16/04/2003
  -
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
<script language="JavaScript">
	function redirect( )
	{
	 window.location = "./cancelselection";
	}
	
	function checkSubmit( )
	{
	 xhibitForm.action        = "./cancelselection";
	 xhibitForm.submit( );	
	}
</script>

            <table cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#0C397D" width="100%">
              <form name="xhibitForm" method="POST" enctype="text/plain">
			  <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
              <tr>
                <td class="cfFormMessage">
                    <div align="left">
                    <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#0C397D" width="90%">
                      <tr>
                        <td class="cfPageTitle" nowrap colspan="2">&nbsp;<fmt:message key="confirmassignment.title"/></td>
                      </tr>
                      <tr>
                        <td class="cfFormLabel" nowrap>&nbsp;</td>
                        <td class="cfFormData" align=left>
                        </td>
                      </tr>
					  <tr>
                        <td class="cfFormLabel" nowrap><fmt:message key="confirmassignment.text1"/> <fmt:message key="signinlegalrep.title"/> <fmt:message key="confirmassignment.text2"/></td>
                      </tr>
                      <tr>
                        <td class="cfFormLabel" nowrap>&nbsp;</td>
                      </tr>					  
                      <tr>
                        <td align=center>
                        <input type="button" value="<fmt:message key="confirmassignment.okBtn"/>" name="okBtn" class="cfFormButton" onclick="checkSubmit( );">
                        </td>
                      </tr>
                      </table>
                    </div>
                    </td>
              </tr>
              </form>
              </table>

<script language="JavaScript">
	setTimeout("redirect();", 5000);
</script>






