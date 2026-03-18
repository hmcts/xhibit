<%--
  - Title:       editprobationdetails.jsp (jsp page fragment)
  -
  - Description: A form for editing the probation liason details.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - Version:     V1.0.0
  - $Log: editprobationdetails.jsp,v $
  - Revision 1.5  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.4  2005/06/29 10:47:13  tzj8k5
  - PR 57282 - Thin Client Probation - edit Probation details and issue PSR request
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:25  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.18  2004/10/18 09:39:03  tzj8k5
  - 56587 - Resolve error when no post code for the court
  -
  - Revision 1.17  2003/12/10 14:35:03  xzmw8n
  - Implemented check token functionality
  -
  - Revision 1.16  2003/09/23 08:28:21  tzj8k5
  - Added call to display Saved
  -
  - Revision 1.15  2003/05/08 15:35:36  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.14  2003/03/20 11:37:05  fz0n8j
  - Changed form actions to use the c:url tag
  -
  - Revision 1.13  2003/03/19 21:54:14  fz0n8j
  - Allow context free
  -
  - Revision 1.12  2003/03/17 11:13:14  fz0n8j
  - Uses id for value objects. ecawley
  -
  - Revision 1.11  2003/03/12 15:32:18  fz0n8j
  - Address no longer editable
  -
  - Revision 1.10  2003/03/11 16:31:41  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

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
            <form name="editprobationdetails" action="<c:url value="/updateprobationdetails"/>" method="post">
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
                                        <fmt:message key="editprobationdetails.title"/>
                                        <c:if test="${statusKey != null}">
                         : <fmt:message key="${statusKey}"/>
                                        </c:if>
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
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="officeName"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.officeName.errorValue == null}">
                                            <c:set var="officeNameText" value="${probationServiceDetails.officeName.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="officeNameText" value="${probationServiceDetails.officeName.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <input size="50" maxlength="50" type="text" name="officeName" value="<c:out value="${officeNameText}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.officeName.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.officeName.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="address1"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.line1.errorValue == null}">
                                            <c:set var="line1Text" value="${probationServiceDetails.address.line1.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="line1Text" value="${probationServiceDetails.address.line1.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${line1Text}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.line1.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.line1.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="address2"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.line2.errorValue == null}">
                                            <c:set var="line2Text" value="${probationServiceDetails.address.line2.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="line2Text" value="${probationServiceDetails.address.line2.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${line2Text}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.line2.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.line2.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="address3"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.line3.errorValue == null}">
                                            <c:set var="line3Text" value="${probationServiceDetails.address.line3.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="line3Text" value="${probationServiceDetails.address.line3.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${line3Text}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.line3.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.line3.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="address4"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.line4.errorValue == null}">
                                            <c:set var="line4Text" value="${probationServiceDetails.address.line4.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="line4Text" value="${probationServiceDetails.address.line4.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${line4Text}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.line4.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.line4.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="town"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.town.errorValue == null}">
                                            <c:set var="townText" value="${probationServiceDetails.address.town.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="townText" value="${probationServiceDetails.address.town.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${townText}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.town.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.town.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="county"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.county.errorValue == null}">
                                            <c:set var="countyText" value="${probationServiceDetails.address.county.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="countyText" value="${probationServiceDetails.address.county.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${countyText}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.county.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.county.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="postcode"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.postcode.errorValue == null}">
                                            <c:set var="postcodeText" value="${probationServiceDetails.address.postcode.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="postcodeText" value="${probationServiceDetails.address.postcode.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${postcodeText}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                             <%--   <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.postcode.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.postcode.errorMessageKey}"/>
                                        </c:if>
                                    </td> --%>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="country"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.address.country.errorValue == null}">
                                            <c:set var="countryText" value="${probationServiceDetails.address.country.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="countryText" value="${probationServiceDetails.address.country.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <c:out value="${countryText}"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.address.country.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.address.country.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="telephone"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.telephone.errorValue == null}">
                                            <c:set var="telephoneText" value="${probationServiceDetails.telephone.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="telephoneText" value="${probationServiceDetails.telephone.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <input size="30" maxlength="255" type="text" name="telephone" value="<c:out value="${telephoneText}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.telephone.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.telephone.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="fax"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.fax.errorValue == null}">
                                            <c:set var="faxText" value="${probationServiceDetails.fax.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="faxText" value="${probationServiceDetails.fax.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <input size="30" maxlength="255" type="text" name="fax" value="<c:out value="${faxText}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.fax.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.fax.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="email"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${probationServiceDetails.email.errorValue == null}">
                                            <c:set var="emailText" value="${probationServiceDetails.email.value}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="emailText" value="${probationServiceDetails.email.errorValue}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <td class="psFormValue">
                                        <input size="30" maxlength="255" type="text" name="email" value="<c:out value="${emailText}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${probationServiceDetails.email.errorMessageKey != null}">
                                            <fmt:message key="${probationServiceDetails.email.errorMessageKey}"/>
                                        </c:if>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
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
                                    <td colspan="3" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psPageAction">
                                        <c:set scope="request" var="buttonTextKey" value="probationdetails.form.submit"/>
                                        <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editprobationdetails')"/>
                                        <c:import url="${menuButtonURL}"/>
                                    </td>
                                    <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
            </form>
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

