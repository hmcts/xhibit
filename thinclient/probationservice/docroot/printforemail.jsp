<%--
  - Title:       printforemail.jsp
  -
  - Description: This file delivers html for adding as an attchment to an email.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
  - $Revision: 1.3 $
  - $Log: printforemail.jsp,v $
  - Revision 1.3  2005/04/27 08:26:55  bzjrnl
  - Manual Merge From BRANCH_7_X
  -
  - Revision 1.1.2.1  2005/04/25 13:37:26  bzjrnl
  - Changes to move jspc into the framework.
  -
  - Revision 1.3.108.1  2005/04/20 09:06:18  bzjrnl
  - Changes to error handling to improve display of error messages.
  -
  - Revision 1.3  2003/05/08 15:35:37  fz0n8j
  - Bug fix (/> on table elements) and formatting
  -
  - Revision 1.2  2003/03/26 16:54:47  fz0n8j
  - Bug fixes.
  -
  - Revision 1.1  2003/03/24 16:45:22  fz0n8j
  - Added/Modified for Email functionality.
  -
  -
  -
  --%>
<%--
  - Import the core (common) and fmt (i18n) JSTL tag libraries
  --%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>
<style type="text/css">

td.verticalBorder {
    background-color: 0C397D;
    width: 1;
}
td.horizontalBorder {
    background-color: 0C397D;
    height: 1;
}
td.bodyHeight {
    vertical-align: top;
    height: 350;
}

/*
 * Header Styles
 */

td.headerText {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 18pt;    
    text-align: center;
}

/*
 * Footer Styles
 */

td.footerText {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-size: 8pt;
    text-align: center;
}

/*
 * Holding Styles
 */

td.holdingBodyText {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-size: 12pt;
    text-align: center;
}

/*
 * Error Styles
 */ 

td.errorBodyText {
    color: DD0000;
    font-family: verdana, arial, sans serif; 
    font-size: 12pt;
    text-align: left;
}
pre.errorStackTraceText {
    color: DD0000;
    font-size: 8pt;
    text-align: left;
}


/*
 * PS Page Style
 */

td.psPageHorizontalSpace { /* Top and Bottom */
    height: 5;
}

td.psPageVerticalSpace { /* Left and Right */
    width: 5;
}

td.psPageTitleHorizontalSpace { /* Top and Bottom */
    height: 15;
}

td.psPageTitleVerticalSpace { /* Left and Right */
    width: 0;
}

td.psPageTitle {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 12pt;    
    text-align: left;
}

td.psPageErrorTitle {
    color: FF0000;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 12pt;    
    text-align: left;
}

td.psPageMessage {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 10pt;    
    text-align: left;
}

td.psPageActionHorizontalSpace { /* Top and Bottom */
    height: 15;
}

td.psPageActionVerticalSpace { /* Left and Right */
    width: 0;
}

td.psPageAction {
    text-align: left;
}


/*
 * Print specific stuff
 */

td.psPrintTitle {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 12pt;    
    text-align: center;
}

td.psPrintText {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 10pt;    
    text-align: left;
}

td.psPrintTextData {
    background-color: ff0000;
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 10pt;    
    text-align: left;
}

span.psPrintTextData {
    color: ff0000;
    font-family: verdana, arial, sans serif; 
    font-weight: bold;
    font-size: 10pt;    
    text-align: left;
}

/*
 * PS Form Style
 */

td.psFormHeaderHorizontalBorder { /* Top and Bottom */
    background-color: 0C397D;
    height: 1;
}

td.psFormHeaderVerticalBorder { /* Left and Right */
    background-color: 0C397D;
    width: 1;
}

td.psFormHeaderSpace { /* Left and Right */
    background-color:0C397D;
    width=5;
}

td.psFormHeader {
    text-align: center;
    background-color: 0C397D;
    color: FFCC00;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
    font-weight: bold;
    text-align: left;
}

td.psFormMainInset { /* Left */
    width=100;
}

td.psFormVerticalSpace { /* Left and Right*/
    width=5;
}

td.psFormHorizontalSpace { /* Top, inbetween rows and Bottom */
    height=5;
}

td.psFormName {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
    font-weight: bold;
    text-align: right;
}

td.psFormValue {
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
    text-align: left
}

td.psFormError {
    color: FF0000;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
}

/*
 * PS Table Style
 */

td.psTableVerticalBorder {
    background-color: 0C397D;
    width: 1;
}
td.psTableVerticalSpace {
    width: 5;
}
td.psTableHorizontalBorder {
    background-color: 0C397D;
    height: 1;
}
td.psTableHorizontalSpace {
    height: 5;
}
td.psTableHeader {
    text-align: center;
    background-color: 0C397D;
    color: FFCC00;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
    font-weight: bold;
}
td.psTableHeaderSpace {
    background-color:0C397D;
    width=5;
}
td.psTableMain {
    background-color: ffffcc;
    color: 0C397D;
    font-family: verdana, arial, sans serif; 
    font-size: 10pt; 
}
td.psTableMainSpace {
    background-color:ffffcc;
    width=5;
}

form.psForm {
    margin-bottom:0;
}
</style>

<html>
<head>
    <title>Print</title>
</head>
<body bottommargin="5" topmargin="5" leftmargin="5" rightmargin="5">
<table width="600" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPrintTitle">
            <fmt:message key="print.title"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="600" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td width="245" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="294">
                            <tr>
                                <td colspan="2" class="psFormHorizontalSpace"></td>
                            </tr>
                            <tr>
                                <td colspan="2" class="psPrintText">
                                    <fmt:message key="print.seniorcrowncourtliasonofficer"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
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
                                <td colspan="2" class="psFormHorizontalSpace"></td>
                            </tr>
                        </table>
                    </td>
                    <td class="psTableVerticalSpace"></td>
                    <td class="psTableVerticalBorder"></td>
                    <td class="psTableVerticalSpace"></td>
                    <td width="295" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="295">
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.probationliasondepartment"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
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
                                <td class="psFormHorizontalSpace"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                        <fmt:formatDate value="${psrRequestDetail.hearingDate}" dateStyle="short"/>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="600" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td width="294" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="294">
                            <tr>
                                <td colspan="2" class="psFormHorizontalSpace"></td>
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
                                    <c:out value="${psrRequestDetail.defendantAge}"/>
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
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="telephone"/>
                                </td>
                                <td class="psPrintText">
                                    <span class="psPrintTextData">
                                    <c:out value="${psrRequestDetail.defendantTelephone.value}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" class="psFormHorizontalSpace"></td>
                            </tr>
                        </table>
                    </td>
                    <td class="psTableVerticalSpace"></td>
                    <td class="psTableVerticalBorder"></td>
                    <td class="psTableVerticalSpace"></td>
                    <td width="295" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="295">
                            <tr>
                                <td colspan="2" class="psFormHorizontalSpace"></td>
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
                                <td colspan="2" class="psFormHorizontalSpace"></td>
                            </tr>
                        </table>
                    </td>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormHorizontalSpace"></td>
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
                    <td class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
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
                                <td class="psFormHorizontalSpace"></td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.nolaterthan"/>
                                    <span class="psPrintTextData">
                                    <fmt:formatDate value="${psrRequestDetail.noLaterThan}" dateStyle="short"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
                            </tr>
                            <tr>
                                <td class="psPrintText">
                                    <fmt:message key="print.letusknow"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
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
                                <td class="psFormHorizontalSpace"></td>
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
                                <td class="psFormHorizontalSpace"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalBorder"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td width="50%" valign="top">
                        <table cellpadding="0" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td class="psFormHorizontalSpace"></td>
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
                                <td class="psFormHorizontalSpace"></td>
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
                    <td colspan="2" class="psFormHorizontalSpace"></td>
                </tr>
                <tr>
                    <td class="psPrintText" colspan="2">
                        <fmt:message key="print.willsupply"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="psFormHorizontalSpace"></td>
                </tr>
                <tr>
                    <td class="psPrintText" colspan="2">
                        <fmt:message key="print.defendantknown"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="psFormHorizontalSpace"></td>
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
                    <td colspan="2" class="psFormHorizontalSpace"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"></td>
    </tr>
</table>
</body>
</html>

