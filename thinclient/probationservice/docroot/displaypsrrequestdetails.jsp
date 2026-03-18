<%--
  - Title:       viewpsrrequestdetails.jsp (jsp page fragment)
  -
  - Description: This file displays psrrequest details.
  -              Like all pages it should not be referenced directly but through the 
  -              pages resource bundle.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - Version:     $Id: displaypsrrequestdetails.jsp,v 1.4 2006/05/04 10:18:38 bzjrnl Exp $
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%--
  - The html to displey the details
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
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
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.telephone.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="fax"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.fax.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="email"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.email.value}"/>
                                    </span>
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
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.officeName.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.line1.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.line2.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.line3.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.line4.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.town.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.county.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationDetails.address.postcode.value}"/>
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
                        <span class="psPrintTextData">
                        <fmt:formatDate value="${psrRequestDetail.hearingDate.value}" dateStyle="short"/>
                        </span><br>
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
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.defendantLocation.value}"/>
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
                    <td width="50%" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td colspan="2" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.surname"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.defendantSurname.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.forenames"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.defendantForenames.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.dob"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <fmt:formatDate value="${psrRequestDetail.defendantDOB}" dateStyle="short"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.age"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:choose>
                                            <c:when test="${psrRequestDetail.defendantAge  > 0}">
                                               <c:out value="${psrRequestDetail.defendantAge}"/>
                                            </c:when>
                                   </c:choose>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPrintText" valign="top">
                                    <fmt:message key="print.homeaddress"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.defendantAddress.line1.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <!--tr>
                                <td class="psPrintText">
                                    <fmt:message key="telephone"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.defendantTelephone.value}"/>
                                    </span>
                                </td>
                            </tr-->
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
                                <td class="psPrintText" valign="top">
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
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.probationContact.value}"/>
                                    </span>
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
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.antecedents.value}"/>
                        </span>
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
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.cpsOffice.value}"/>
                        </span>
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
                    <td class="psPrintText">
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.offences.value}"/>
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
                        <fmt:message key="print.codefendants"/>
                    </td>
                </tr>
                <tr>
                    <td class="psPrintText">
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.coDefendants.value}"/>
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
                        <fmt:message key="print.circumstances"/>
                    </td>
                </tr>
                <tr>
                    <td class="psPrintText">
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.circumstances.value}"/>
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
                        <fmt:message key="print.comments"/>
                    </td>
                </tr>
                <tr>
                    <td class="psPrintText">
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.comments.value}"/>
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
                        <fmt:message key="print.interview"/>
                        <span class="psPrintTextData">
                        <c:out value="${psrRequestDetail.available.value}"/>
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
                                    <span class="psPrintTextData">
                                    <fmt:formatDate value="${psrRequestDetail.noLaterThan.value}" dateStyle="short"/>
                                    </span>
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
</table>

