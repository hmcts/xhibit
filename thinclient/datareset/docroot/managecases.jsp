<%--
  - Title:       managecases.jsp
  -
  - Copyright:   Copyright (c) 2015
  - Company:     CGI
  -
  - Author:      Scott Atwell
  - Change: NLE Enhancements Project 2015
  -
  - Comment: Screen to allow testers to manage the cases that can be maintained by the reset_data screen.
  -
  --%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script>

	function validcasenumberForSubmission(casenumber) {
		// Must be at least characters
		if (casenumber.length != 8) {
			return false;
		}
		
		// Next check its numeric
		if (!isNaN(casenumber)) {
			// Get the first 4 characters to validate
			var half1 = casenumber.slice(0,4);
			if ((half1>2000) && (half1<2099)) {
				return true;
			}
		}
		return false;
	}
	
	function validcasenumber(casenumber) {
		// Check if its empty, in which case thats fine whilst its in focus as user may just be tabbing
		if (casenumber.length == 0) {
			return true;
		}
		
		// Must be at least characters
		if (casenumber.length != 8) {
			return false;
		}
		
		// Next check its numeric
		if (!isNaN(casenumber)) {
			// Get the first 4 characters to validate
			var half1 = casenumber.slice(0,4);
			if ((half1>2000) && (half1<2099)) {
				return true;
			}
		}
		return false;
	}
	
	function setFormFieldsAndValidate() {
		// Which form is being submitted?
		if (document.managecases.formsubmitted.value == 'addcase') {
			document.addcase.courtnameselected.value = document.managecases.courtlist.value;
			document.addcase.casetypeselected.value = document.managecases.casetype.value;
			document.addcase.casenumberselected.value = document.managecases.casenumber.value;

			return true;
		} else if (document.managecases.formsubmitted.value == 'removecase') {
			document.removecase.caseidselected.value = document.managecases.caseidselected.value;
			return true;
		}
		return false;
	}
	
	function doRemove(caseId) {
		document.removecase.caseidselected.value=caseId;
		document.removecase.submit();
	}
</script>

<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<!--  Some forms used for adding and removing -->
	<form class="psForm" name="addcase" action="<c:url value="/addcase"/>" onsubmit="alert('submitting');return setFormFieldsAndValidate();" method="post">
	     <input type="hidden" name="courtnameselected" value="SWANSEA"/>
	     <input type="hidden" name="casetypeselected" value="T"/>
	     <input type="hidden" name="casenumberselected" value=""/>
    </form>
    
    <form class="psForm" name="removecase" action="<c:url value="/removecase"/>" method="post">
	     <input type="hidden" name="courtnameselected" value="SWANSEA"/>
	     <input type="hidden" name="caseidselected" value=""/>
    </form>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form class="psForm" name="managecases" action="<c:url value="/managecases"/>" onsubmit="setFormFieldsAndValidate();" method="post">
            	<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
              	<input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
              	<input type="hidden" name="formsubmitted" value=""/>
              	<input type="hidden" name="courtnameselected" value="SWANSEA"/>
              
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
                                        <fmt:message key="managecases.title"/>
                                    </td>
                                    <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormValue">
                                        <fmt:message key="managecasesnote"/>
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
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                
                                <!--  List all available courts -->
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td colspan="2" class="psFormName">
                                        <fmt:message key="selectCourt"/>
                                        :</td>

                                    <td class="psFormValue">
                                        <select name="courtlist" onchange="document.managecases.courtnameselected.value=document.managecases.courtlist.value; document.addcase.courtnameselected.value=document.managecases.courtlist.value; document.removecase.courtnameselected.value=document.managecases.courtlist.value; psSubmitForm('managecases');">
                                        	<c:choose>
                                                <c:when test="${requestScope.selectedCourtName == 'ISLEWORTH'}">
                                                    <option value="ISLEWORTH" selected>
                                                    	<fmt:message key="Isleworth"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="ISLEWORTH">
                                                    	<fmt:message key="Isleworth"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.selectedCourtName == 'SNARESBROOK'}">
                                                    <option value="SNARESBROOK" selected>
                                                    	<fmt:message key="Snaresbrook"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="SNARESBROOK">
                                                    	<fmt:message key="Snaresbrook"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.selectedCourtName == 'SWANSEA'}">
                                                    <option value="SWANSEA" selected>
                                                    	<fmt:message key="Swansea"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="SWANSEA">
                                                    	<fmt:message key="Swansea"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <!--  Add Button -->
                                <tr>
                                	<td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
	                                
	                                <td class="psFormValue">
                                        <select name="casetype" onchange="document.addcase.casetypeselected.value=document.managecases.casetype.value">
                                        	<option value="T" selected>T</option>
                                            <option value="S">S</option>
                                            <option value="A">A</option>
                                            <c:choose>
                                                <c:when test="${requestScope.values.casetype == 'T'}">
                                                    <option value="T" selected>T</option>
                                                </c:when>
                                                <c:when test="${requestScope.values.casetype == 'S'}">
                                                    <option value="S" selected>S</option>
                                                </c:when>
                                                <c:when test="${requestScope.values.casetype == 'A'}">
                                                    <option value="A" selected>A</option>
                                                </c:when>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormValue">
                                        <input name="casenumber" type="text" size="8" maxlength="8" value="" onblur="if (validcasenumber(document.managecases.casenumber.value)) {document.addcase.casenumberselected.value=document.managecases.casenumber.value} else { alert('Invalid case number. Case number must be 8 digits; the first 4 digits must be a year between 2000 and 2099.');document.managecases.casenumber.focus();}"/>
                                    </td>
                                    <td class="psPageAction">
	                                	<c:set scope="request" var="buttonTextKey" value="add"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:if (validcasenumberForSubmission(document.managecases.casenumber.value)) {document.managecases.formsubmitted.value='addcase';setFormFieldsAndValidate();psSubmitForm('addcase')} else {alert('Invalid case number. Case number must be 8 digits; the first 4 digits must be a year between 2000 and 2099.');}"/>
                                        <c:import url="${menuButtonURL}"/>
	                                </td>
	                                <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>    
                            <table class="psTableMain">
                                
                                <!-- ------------------------------------------ -->
                                <!--  Display table of cases for selected court -->
                                <!-- ------------------------------------------ -->
                                
                                <tr>
                                    <td colspan="14" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                
                                <!--  Headers next -->
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psTableHeader">
                                        <fmt:message key="courtname"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    
                                    <td class="psTableHeader">
                                        <fmt:message key="casenumber"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    
                                    <td class="psTableHeader">
                                        <fmt:message key="defendantname"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    
                                    <td class="psTableHeader">
                                        <fmt:message key="defendantdob"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.caselist != null}">
                                            <fmt:message key="${requestScope.errors.caselist}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                
                                <!--  Table data next -->
                                <c:choose>
	                                <c:when test="${requestScope.managedCases != null}">
		                                <c:forEach items="${requestScope.managedCases}" var="item">
		                                	<c:if test="${item.courtName == requestScope.selectedCourtName}">
				                                <tr>
				                                	<td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
				                                	<td class="psTableMain">
				                                		<c:out value="${item.courtName}" />
				                                	</td>
				                                	
				                                	<td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
				                                	
				                                	<td class="psTableMain">
				                                		<c:out value="${item.caseType}" /><c:out value="${item.caseNumber}" />
				                                	</td>
				                                	
				                                	<td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
				                                	
				                                	<td class="psTableMain">
				                                		<c:out value="${item.defendantName}" />
				                                	</td>
				                                	
				                                	<td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
				                                	
				                                	<td class="psTableMain">
				                                		<c:out value="${item.defendantDOB}" />
				                                	</td>
				                                	
				                                	<td class="psPageAction">
					                                    <c:set scope="request" var="buttonTextKey" value="remove"/>
					                                    <c:set scope="request" var="buttonRequestURL" value="javascript:doRemove(${item.caseId})"/>
					                                    <c:import url="${menuButtonURL}"/>
					                                </td>
				                                </tr>
				                            </c:if>
		                                </c:forEach>
		                            </c:when>
		                            <c:otherwise>
		                            	<tr>
		                                	<td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
		                                	<td class="psTableMain">
		                                		No cases are currently being managed.
		                                	</td>
		                                	<td colspan="4" class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
		                                </tr>
		                            </c:otherwise>
	                            </c:choose>
                                                                
                                <tr>
                                    <td colspan="14" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
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
        	
        		<input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
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
                                        <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                        <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                        <input type="hidden" name="pagesource" value="<c:out value="${requestScope.pagesource}"/>">
                                    </form>
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