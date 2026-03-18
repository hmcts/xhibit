<%--
  - Title:       recipientdetailsfrom.jsp (jsp page fragment)
  -
  - Description: This file holds the JSP whic describes the form for editing
  - recipent details, doesn't have the form tags so the parent jsp can define them.
  - The code seems to be happy not gicing errors when a bean is not passed so
  - we also use it for the first page.
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.4 $
  - $Log: recipientdetailsform.jsp,v $
  - Revision 1.4  2006/05/04 10:18:38  bzjrnl
  - Change: TI901
  - Comment: Weblogic Upgrade - Moved resouces into a static application to ensure they can be accessed from all the applications at login.
  -
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:26  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.11  2004/06/02 15:56:00  xzmw8n
  - PR 55938. Removed post as prefered contact option.
  -
  - Revision 1.10  2003/05/14 15:40:17  fz0n8j
  - bug fix.
  -
  - Revision 1.9  2003/05/12 08:40:30  rz3jq5
  - Fixed JSP compilation issue.
  -
  - Revision 1.8  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.7  2003/03/26 16:54:48  fz0n8j
  - Bug fixes.
  -
  - Revision 1.6  2003/03/21 19:48:51  fz0n8j
  - Late changes
  -
  - Revision 1.5  2003/03/19 21:54:15  fz0n8j
  - Allow context free
  -
  - Revision 1.4  2003/03/17 11:31:55  fz0n8j
  - Added revision cvs comments. ecawley
  -
  - Revision 1.3  2003/03/14 20:50:40  fz0n8j
  - Uses new object id in hidden field. ecawley.
  -
  - Revision 1.2  2003/03/11 16:31:43  fz0n8j
  - Added CVS log comments - ecawley
  -
  --%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - The html to be included in the main page
  --%>
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
                            <c:set var="officeNameText" value="${psrRecipientDetailsBean.officeName.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="officeNameText" value="${psrRecipientDetailsBean.officeName.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="officeName" value="<c:out value="${officeNameText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.officeName.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.officeName.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.line1.errorValue == null}">
                            <c:set var="line1Text" value="${psrRecipientDetailsBean.address.line1.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="line1Text" value="${psrRecipientDetailsBean.address.line1.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="line1" value="<c:out value="${line1Text}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.line1.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.line1.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.line2.errorValue == null}">
                            <c:set var="line2Text" value="${psrRecipientDetailsBean.address.line2.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="line2Text" value="${psrRecipientDetailsBean.address.line2.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="line2" value="<c:out value="${line2Text}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.line2.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.line2.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.line3.errorValue == null}">
                            <c:set var="line3Text" value="${psrRecipientDetailsBean.address.line3.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="line3Text" value="${psrRecipientDetailsBean.address.line3.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="line3" value="<c:out value="${line3Text}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.line3.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.line3.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.line4.errorValue == null}">
                            <c:set var="line4Text" value="${psrRecipientDetailsBean.address.line4.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="line4Text" value="${psrRecipientDetailsBean.address.line4.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="line4" value="<c:out value="${line4Text}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.line4.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.line4.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.town.errorValue == null}">
                            <c:set var="townText" value="${psrRecipientDetailsBean.address.town.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="townText" value="${psrRecipientDetailsBean.address.town.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="town" value="<c:out value="${townText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.town.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.town.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.county.errorValue == null}">
                            <c:set var="countyText" value="${psrRecipientDetailsBean.address.county.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="countyText" value="${psrRecipientDetailsBean.address.county.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="30" type="text" name="county" value="<c:out value="${countyText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.county.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.county.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.address.postcode.errorValue == null}">
                            <c:set var="postcodeText" value="${psrRecipientDetailsBean.address.postcode.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="postcodeText" value="${psrRecipientDetailsBean.address.postcode.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="8" type="text" name="postcode" value="<c:out value="${postcodeText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.postcode.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.postcode.errorMessageKey}"/>
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
                        <fmt:message key="country"/>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <c:choose>
                        <c:when test="${psrRecipientDetailsBean.address.country.errorValue == null}">
                            <c:set var="countryText" value="${psrRecipientDetailsBean.address.country.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="countryText" value="${psrRecipientDetailsBean.address.country.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="255" type="text" name="country" value="<c:out value="${countryText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.address.country.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.address.country.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.telephone.errorValue == null}">
                            <c:set var="telephoneText" value="${psrRecipientDetailsBean.telephone.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="telephoneText" value="${psrRecipientDetailsBean.telephone.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="255" type="text" name="telephone" value="<c:out value="${telephoneText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.telephone.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.telephone.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.fax.errorValue == null}">
                            <c:set var="faxText" value="${psrRecipientDetailsBean.fax.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="faxText" value="${psrRecipientDetailsBean.fax.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="255" type="text" name="fax" value="<c:out value="${faxText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.fax.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.fax.errorMessageKey}"/>
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
                        <c:when test="${psrRecipientDetailsBean.email.errorValue == null}">
                            <c:set var="emailText" value="${psrRecipientDetailsBean.email.value}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="emailText" value="${psrRecipientDetailsBean.email.errorValue}"/>
                        </c:otherwise>
                    </c:choose>
                    <td class="psFormValue">
                        <input size="30" maxlength="255" type="text" name="email" value="<c:out value="${emailText}"/>">
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:if test="${psrRecipientDetailsBean.email.errorMessageKey != null}">
                            <fmt:message key="${psrRecipientDetailsBean.email.errorMessageKey}"/>
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
                        <fmt:message key="prefered"/>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormValue"></td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError"></td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormName">
                        <fmt:message key="column.email"/>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormValue">
                        <INPUT TYPE="RADIO" NAME="method" VALUE="Email" <c:if test="${psrRecipientDetailsBean.method != 'Fax' && psrRecipientDetailsBean.method != 'Post'}">CHECKED</c:if>>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError"></td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormName">
                        <fmt:message key="column.fax"/>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormValue">
                        <INPUT TYPE="RADIO" NAME="method" VALUE="Fax" <c:if test="${psrRecipientDetailsBean.method == 'Fax'}">CHECKED</c:if>>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError"></td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
                <!--tr>
                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormName">
                        <fmt:message key="postal"/>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormValue">
                        <INPUT TYPE="RADIO" NAME="method" VALUE="Post" <c:if test="${psrRecipientDetailsBean.method == 'Post'}">CHECKED</c:if>>
                    </td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError"></td>
                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr-->
                <tr>
                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">

