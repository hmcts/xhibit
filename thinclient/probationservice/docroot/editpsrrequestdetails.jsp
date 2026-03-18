<%--
  - Title:       editpsrrequestdetails.jsp (jsp page fragment)
  -
  - Description: This file deisplays psrrequest details for editing.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley (2003)
  - Version:     $Id: editpsrrequestdetails.jsp,v 1.4 2006/05/04 10:18:38 bzjrnl Exp $
  --%>

<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="uk.gov.courtservice.xhibit.web.ps.bean.PSRRequestDetailBean,
                 java.util.Date"%>
<fmt:bundle basename="Pages">
    <fmt:message key="button" var="menuButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <c:choose>
        <c:when test="${requestScope.errorMessageKeys != null}">
            <tr>
                <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
            </tr>
            <tr>
                <td colspan="3" class="psPrintTitle">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td>
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                    </tr>

                                    <c:forEach var="errorMessageKey" items="${requestScope.errorMessageKeys}">
                                      <tr>
                                          <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                          <td class="errorBodyText">
                                              <fmt:message key="${errorMessageKey}"/>
                                          </td>
                                          <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                      </tr>
                                    </c:forEach>

                                    <tr>
                                        <td colspan="3" class="psPageTitleHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
            </tr>
            <tr>
                <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
            </tr>
        </c:when>
    </c:choose>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPrintTitle">
            <fmt:message key="print.title"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
    </tr>
    <form method="post" name="editpsrrequestdetails" action="<c:url value="/updatepsrrequestdetails"/>">
      <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
      <input type="hidden" name="senderfax" value="<c:out value="${psrRequestDetail.probationDetails.fax.value}"/>">
      <input type="hidden" name="senderemail" value="<c:out value="${psrRequestDetail.probationDetails.email.value}"/>">
      <input type="hidden" name="senderoffice" value="<c:out value="${psrRequestDetail.probationDetails.officeName.value}"/>">   
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td width="50%" valign="top">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="psPrintText">
                                        <fmt:message key="print.seniorcrowncourtliasonofficer"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="telephone"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.telephone.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="fax"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.fax.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="email"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.email.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td width="50%" valign="top">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.probationliasondepartment"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.officeName.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.line1.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.line2.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.line3.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.line4.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.town.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.county.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationDetails.address.postcode.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.madeby"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.judgeTitle.value}"/>
                            </span>
                            <fmt:message key="print.on"/>
                            <span class="psPrintTextData">
                            <fmt:formatDate value="${psrRequestDetail.creationDate}" dateStyle="short"/>
                            </span>
                            <fmt:message key="print.at"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.courtName.value}"/>
                            </span>
                            <fmt:message key="print.crowncourt"/>
                            <fmt:message key="print.forhearingon"/>                            

                            <%--
                              - Start: Edit Hearing Date
                              --%>
                            <c:choose>
                                <c:when test="${requestScope.hrgDate != null}">
                                    <c:set var="hearingDate" value="${requestScope.hrgDate}"/>
                                </c:when>
                                <c:when test="${psrRequestDetail.hearingDate.value != null}">
                                    <fmt:message var="hearingDateFormat" key="print.dateformat"/>
                                    <fmt:formatDate var="hearingDate" value="${psrRequestDetail.hearingDate.value}" pattern="${hearingDateFormat}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="hearingDate" value="${psrRequestDetail.hearingDate.errorValue}"/>
                                </c:otherwise>
                            </c:choose>                            
                            <input size="8" maxlength="8" type="text" name="hearingDateString" value="<c:out value="${hearingDate}"/>">
                            <fmt:message key="print.dateformat"/>
                            <c:if test="${psrRequestDetail.hearingDate.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                    <fmt:message key="${psrRequestDetail.hearingDate.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Hearing Date
                              --%>

                            <br>
                            <fmt:message key="print.to"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.recipientDetails.officeName.value}"/>                      
                            </span>
                            <fmt:message key="print.address"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.recipientDetails.address.line1.value}"/>
                            </span>
                            <fmt:message key="fax"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.recipientDetails.fax.value}"/>
                            </span>
                            <fmt:message key="email"/>
                            <span class="psPrintTextData">
                            <c:out value="${psrRequestDetail.recipientDetails.email.value}"/>
                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.remandedto"/>
                            <%--
                              - Start: Edit Remand To
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.defendantLocation.value != null}">
                                    <c:set var="defendantLocation" value="${psrRequestDetail.defendantLocation.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="defendantLocation" value="${psrRequestDetail.defendantLocation.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="150" maxlength="100" type="text" name="defendantLocation" value="<c:out value="${defendantLocation}"/>">
                            <c:if test="${psrRequestDetail.defendantLocation.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                    <fmt:message key="${psrRequestDetail.defendantLocation.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Remand To
                              --%>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td width="50%" valign="top">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.surname"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.defendantSurname.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.forenames"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.defendantForenames.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.dob"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <fmt:formatDate value="${psrRequestDetail.defendantDOB}" dateStyle="short"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.age"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:choose>
                                            <c:when test="${psrRequestDetail.defendantAge  > 0}">
                                                 <c:out value="${psrRequestDetail.defendantAge}"/>
                                            </c:when>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText" valign="top">
                                        <fmt:message key="print.homeaddress"/>
                                    </td>
                                    <td class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.defendantAddress.line1.value}"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                        <td width="50%" valign="top">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.solicitors"/>
                                    </td>
                                    <td class="psPrintText">
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.solicitorName.value}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="telephone"/>
                                    </td>
                                    <td class="psPrintText">
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.solicitorTelephone.value}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.probationcontact"/>
                                    </td>
                                    <td class="psPrintText">
                                        <%--
                                          - End: Edit Probation Contact
                                          --%>                                        
                                        <c:choose>
                                            <c:when test="${psrRequestDetail.probationContact.value != null}">
                                                <c:set var="probationContact" value="${psrRequestDetail.probationContact.value}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="probationContact" value="${psrRequestDetail.probationContact.errorValue}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <input size="30" maxlength="30" type="text" name="probationContact" value="<c:out value="${probationContact}"/>">
                                        <c:if test="${psrRequestDetail.probationContact.errorMessageKey != null}">
                                            <span class="psPrintTextError">
                                                <fmt:message key="${psrRequestDetail.probationContact.errorMessageKey}"/>
                                            </span>
                                        </c:if>
                                        <%--
                                          - End: Edit Probation Contact
                                          --%>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.antecedents"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <%--
                              - Start: Edit Antecedents
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.antecedents.value != null}">
                                    <c:set var="antecedents" value="${psrRequestDetail.antecedents.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="antecedents" value="${psrRequestDetail.antecedents.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="150" maxlength="255" type="text" name="antecedents" value="<c:out value="${antecedents}"/>">
                            <c:if test="${psrRequestDetail.antecedents.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                  <fmt:message key="${psrRequestDetail.antecedents.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Antecedents
                              --%>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.cps"/>
                            <%--
                              - Start: Edit CPS Office
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.cpsOffice.errorValue == null}">
                                    <c:set var="cpsOffice" value="${psrRequestDetail.cpsOffice.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="cpsOffice" value="${psrRequestDetail.cpsOffice.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="30" maxlength="30" type="text" name="cpsOffice" value="<c:out value="${cpsOffice}"/>">
                            <c:if test="${psrRequestDetail.cpsOffice.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                  <fmt:message key="${psrRequestDetail.cpsOffice.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - Start: Edit CPS Office
                              --%>
                            <fmt:message key="print.officeforwarded"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.offences"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psPrintTextData">
                            <c:out value="${psrRequestDetail.offences.value}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.codefendants"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psPrintTextData">
                            <c:out value="${psrRequestDetail.coDefendants.value}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.circumstances"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <%--
                              - Start: Edit Circumstances
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.circumstances.value != null}">
                                    <c:set var="circumstances" value="${psrRequestDetail.circumstances.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="circumstances" value="${psrRequestDetail.circumstances.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="150" maxlength="255" type="text" name="circumstances" value="<c:out value="${circumstances}"/>">
                            <c:if test="${psrRequestDetail.circumstances.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                  <fmt:message key="${psrRequestDetail.circumstances.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Circumstances
                              --%>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.comments"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <%--
                              - Start: Edit Comments
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.comments.value != null}">
                                    <c:set var="comments" value="${psrRequestDetail.comments.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="comments" value="${psrRequestDetail.comments.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="150" maxlength="255" type="text" name="comments" value="<c:out value="${comments}"/>">
                            <c:if test="${psrRequestDetail.comments.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                    <fmt:message key="${psrRequestDetail.comments.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Comments
                              --%>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText">
                            <fmt:message key="print.interview"/>
                            <%--
                              - Start: Edit Available For Interview
                              --%>
                            <c:choose>
                                <c:when test="${psrRequestDetail.available.value != null}">
                                    <c:set var="available" value="${psrRequestDetail.available.value}"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="available" value="${psrRequestDetail.available.errorValue}"/>
                                </c:otherwise>
                            </c:choose>
                            <input size="30" maxlength="30" type="text" name="available" value="<c:out value="${available}"/>">
                            <c:if test="${psrRequestDetail.available.errorMessageKey != null}">
                                <span class="psPrintTextError">
                                    <fmt:message key="${psrRequestDetail.available.errorMessageKey}"/>
                                </span>
                            </c:if>
                            <%--
                              - End: Edit Available For Interview
                              --%>
                        </td>
                    </tr>
                    <tr>
                        <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td>
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.send6copies"/>
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.probationAddress.value}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.nolaterthan"/>
                                        <%--
                                          - Start: Edit No Later Than Date
                                          --%>                                       
                                        <c:choose>
                                            <c:when test="${requestScope.nolaterDate != null}">
                                                <c:set var="noLaterThan" value="${requestScope.nolaterDate}"/>
                                            </c:when>
                                            <c:when test="${psrRequestDetail.noLaterThan.value != null}">
                                                <fmt:message var="noLaterDateFormat" key="print.dateformat"/>
                                                <fmt:formatDate var="noLaterThan" value="${psrRequestDetail.noLaterThan.value}" pattern="${noLaterDateFormat}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="noLaterThan" value="${psrRequestDetail.noLaterThan.errorValue}"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <input size="8" maxlength="8" type="text" name="nolaterthanField" value="<c:out value="${noLaterThan}"/>">
                                        <fmt:message key="print.dateformat"/>
                                        <c:if test="${psrRequestDetail.noLaterThan.errorMessageKey != null}">
                                          <span class="psPrintTextError">
                                            <fmt:message key="${psrRequestDetail.noLaterThan.errorMessageKey}"/>
                                          </span>
                                        </c:if>
                                        <%--
                                          - End: Edit No Later Than Date
                                          --%>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.letusknow"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.signed"/>
                                        <fmt:message key="print.officersname"/>
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.userName.value}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="date"/>
                                        <span class="psPrintTextData">
                                        <fmt:formatDate value="${psrRequestDetail.currentDate}" dateStyle="short"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td colspan="3" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
            <td>
                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td width="50%" valign="top">
                            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.to"/>
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.recipientDetails.officeName.value}"/>
                                        <c:out value="${psrRequestDetail.recipientDetails.address.line1.value}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psPrintText">
                                        <fmt:message key="print.re"/>
                                        <span class="psPrintTextData">
                                        <c:out value="${psrRequestDetail.defendantForenames.value}"/>
                                        <c:out value="${psrRequestDetail.defendantSurname.value}"/>
                                        </span>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td  class="psPrintText" width="50%" valign="bottom">
                            <fmt:message key="print.dateline"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText" colspan="2">
                            <fmt:message key="print.willsupply"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td class="psPrintText" colspan="2">
                            <fmt:message key="print.defendantknown"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                    <tr>
                        <td width="50%" class="psPrintText">
                            <fmt:message key="print.signedline"/>
                        </td>
                        <td width="50%" class="psPrintText">
                            <fmt:message key="print.officeline"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                    </tr>
                </table>
            </td>
            <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
        <tr>
            <td colspan="3">
                <table>
                    <tr>
                        <td class="psPrintText">
                            <c:set scope="request" var="buttonTextKey" value="editpsrrequestdetails.form.submit"/>
                            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editpsrrequestdetails')"/>
                            <c:import url="${menuButtonURL}"/>
                        </td>
                        <td class="psPrintText">
                            <c:set scope="request" var="buttonTextKey" value="editpsrrequestdetails.selectrecipient"/>
                            <c:url value="/selectrecipient" var="selectrecipient"/>
                            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitFormAction('editpsrrequestdetails','${selectrecipient}')"/>
                            <c:import url="${menuButtonURL}"/>
                        </td>
                        <td class="psPrintText">
                            <c:set scope="request" var="buttonTextKey" value="editpsrrequestdetails.issuerequest"/>
                            <c:url value="/confirmissue" var="confirmissue"/>
                            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitFormAction('editpsrrequestdetails','${confirmissue}')"/>
                            <c:import url="${menuButtonURL}"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </form>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
