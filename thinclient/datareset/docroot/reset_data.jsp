<%--
  - Title:       reset_data.jsp
  -
  - Copyright:   Copyright (c) 2015
  - Company:     CGI
  -
  - Author:      Scott Atwell
  - Change: NLE Enhancements Project 2015
  -
  - Comment: Screen to allow testers to reset designated data elements for using testing baseline data.
  - Initially this will only allow dates of birth to be reset for whichever cases are selected by user.
  - The date to reset to will default to as they were as of 01/04/2015 when the data baseline was created, however
  - an option will be available to allow user defined baseline date.
  -
  -
  --%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script>
	function validyear(day, month, year) {
		if ((year>2000) && (year<2099)) {
			if (validday(day,month,year)) {
				return true;
			}
		}
		return false;
	}
	
	function validday(day, month, year) {
		if ((day>0) && (day<29)) { // Always ok
			return true;
		} else {
			if (day==31) {
				if (month=='January' || month=='March' || month=='May' || month=='July' || month=='August' || month=='October' || month=='December') {
					return true;
				}
			}
			
			if (day==30) {
				if (month!='February') {
					return true;
				}
			}
			
			if (day==29) {
				if (month!='February') {
					return true;
				} else {
					if (year%4 == 0) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
</script>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form name="resetdata" action="<c:url value="/resetdata"/>" method="post">
              <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
              <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
              
              	<!--  Page heading -->
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
                                        <fmt:message key="resetdata.title"/>
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
                
                <!-- Main content -->
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="17" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                
                                <!--  List all available data types to be reset -->
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="datatypes"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <select name="datatypelist">
                                        	<option value="DOB">
                                            	<fmt:message key="DOB"/>
                                            </option>
                                            <c:choose>
                                                <c:when test="${requestScope.values.datatype == 'DOB'}">
                                                    <option value="DOB" selected>
                                                    	<fmt:message key="DOB"/>
                                                    </option>
                                                </c:when>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                
                                <!--  List all available courts -->
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="courts"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <select name="courtlist" onchange="document.update.courtnameselected.value=document.resetdata.courtlist.value">
                                        	<option value="ALL">
                                            	<fmt:message key="All"/>
                                            </option>
                                            <option value="ISLEWORTH">
                                            	<fmt:message key="Isleworth"/>
                                            </option>
                                            <option value="SNARESBROOK">
                                            	<fmt:message key="Snaresbrook"/>
                                            </option>
                                            <option value="SWANSEA" selected>
                                            	<fmt:message key="Swansea"/>
                                            </option>
                                            <c:choose>
                                                <c:when test="${requestScope.values.courts == 'ALL'}">
                                                    <option value="ALL" selected>
                                                    	<fmt:message key="All"/>
                                                    </option>
                                                </c:when>
                                                <c:when test="${requestScope.values.courts == 'ISLEWORTH'}">
                                                    <option value="ISLEWORTH" selected>
                                                    	<fmt:message key="Isleworth"/>
                                                    </option>
                                                </c:when>
                                                <c:when test="${requestScope.values.courts == 'SNARESBROOK'}">
                                                    <option value="SNARESBROOK" selected>
                                                    	<fmt:message key="Snaresbrook"/>
                                                    </option>
                                                </c:when>
                                                <c:when test="${requestScope.values.courts == 'SWANSEA'}">
                                                    <option value="SWANSEA" selected>
                                                    	<fmt:message key="Swansea"/>
                                                    </option>
                                                </c:when>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td colspan="7" class="psFormName">
                                        <fmt:message key="theresetdate"/>
                                    </td>
                                    
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td colspan="2" class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                    	<fmt:message key="day"/>
                                        <input name="theresetday" type="text" size="2" maxlength="2" value="1" onblur="if (validday(document.resetdata.theresetday.value, document.resetdata.theresetmonth.value, document.resetdata.theresetyear.value)) {document.update.dayselected.value=document.resetdata.theresetday.value} else { alert('Invalid day. Day must be between 1 and 31 and valid for the month and year combination.');document.resetdata.theresetday.focus();}"/>
                                        
                                        <fmt:message key="month"/>
                                        <select name="theresetmonth" onchange="if (validday(document.resetdata.theresetday.value, document.resetdata.theresetmonth.value, document.resetdata.theresetyear.value)) {document.update.monthselected.value=document.resetdata.theresetmonth.value} else { alert('Invalid month. Month must be valid for the day and year combination.');document.resetdata.theresetmonth.focus();}">
                                        	<option value="January">
                                        		<fmt:message key="January"/>
                                        	</option>
                                        	<option value="February">
                                        		<fmt:message key="February"/>
                                        	</option>
                                        	<option value="March">
                                        		<fmt:message key="March"/>
                                        	</option>
                                        	<option value="April" selected>
                                        		<fmt:message key="April"/>
                                        	</option>
                                        	<option value="May">
                                        		<fmt:message key="May"/>
                                        	</option>
                                        	<option value="June">
                                        		<fmt:message key="June"/>
                                        	</option>
                                        	<option value="July">
                                        		<fmt:message key="July"/>
                                        	</option>
                                        	<option value="August">
                                        		<fmt:message key="August"/>
                                        	</option>
                                        	<option value="September">
                                        		<fmt:message key="September"/>
                                        	</option>
                                        	<option value="October">
                                        		<fmt:message key="October"/>
                                        	</option>
                                        	<option value="November">
                                        		<fmt:message key="November"/>
                                        	</option>
                                        	<option value="December">
                                        		<fmt:message key="December"/>
                                        	</option>
                                        	<c:choose>
                                                <c:when test="${requestScope.values.theresetmonth == 'January'}">
                                                    <option value="January" selected>
                                                    	<fmt:message key="January"/>
                                                    </option>
                                                    <option value="February" selected>
                                                    	<fmt:message key="February"/>
                                                    </option>
                                                    <option value="March" selected>
                                                    	<fmt:message key="March"/>
                                                    </option>
                                                    <option value="April" selected>
                                                    	<fmt:message key="April"/>
                                                    </option>
                                                    <option value="May" selected>
                                                    	<fmt:message key="May"/>
                                                    </option>
                                                    <option value="June" selected>
                                                    	<fmt:message key="June"/>
                                                    </option>
                                                    <option value="July" selected>
                                                    	<fmt:message key="July"/>
                                                    </option>
                                                    <option value="August" selected>
                                                    	<fmt:message key="August"/>
                                                    </option>
                                                    <option value="September" selected>
                                                    	<fmt:message key="September"/>
                                                    </option>
                                                    <option value="October" selected>
                                                    	<fmt:message key="October"/>
                                                    </option>
                                                    <option value="November" selected>
                                                    	<fmt:message key="November"/>
                                                    </option>
                                                    <option value="December" selected>
                                                    	<fmt:message key="December"/>
                                                    </option>
                                                </c:when>
                                            </c:choose>
                                        </select>
                                        
                                        <fmt:message key="year"/>
                                        <input name="theresetyear" type="text" size="4" maxlength="4" value="2015" onblur="if (validyear(document.resetdata.theresetday.value, document.resetdata.theresetmonth.value, document.resetdata.theresetyear.value)) {document.update.yearselected.value=document.resetdata.theresetyear.value} else { alert('Invalid year. Year must be between 2000 and 2099 and valid for the day, month combination.');document.resetdata.theresetyear.focus();}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                        	</table>

                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psFormError">

                        </td>
                    </tr>
                </table>
        	
        		<INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
			</form>
        </td>
    </tr>
</table>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
        <td class="psFormError">
            <c:choose>
                <c:when test="${requestScope.responseText != null}">
                    <c:out value="${requestScope.responseText}"/>
                </c:when>
                <c:otherwise>
                    &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                </c:otherwise>
            </c:choose>
        </td>
    <tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
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
                                    <form class="psForm" action="<c:url value="/home"/>" method="post" name="cancel">
                                        <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                        <c:import url="${menuButtonURL}"/>
                                        <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                        <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                    </form>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                <td class="psPageAction">
                                	<form class="psForm" action="<c:url value="/updatedata"/>" method="post" name="update">
	                                    <c:set scope="request" var="buttonTextKey" value="updatebutton"/>
	                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('update')"/>
	                                    <input type="hidden" name="courtnameselected" value="SWANSEA"/>
	                                    <input type="hidden" name="dayselected" value="1"/>
	                                    <input type="hidden" name="monthselected" value="April"/>
	                                    <input type="hidden" name="yearselected" value="2015"/>
	                                    <c:import url="${menuButtonURL}"/>
	                                </form>
                                </td>
                                <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
</td>
<td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>
