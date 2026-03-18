<%--
  - Title:       identifylegalrep.jsp (jsp page fragment)
  -
  - Description: This file displays the Find Legal Representative screen
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Steve Tully (2003)
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
function stepUpdateViewState( ) {
    setEnabled( xhibitForm.fullNameText, !(xhibitForm.legalRepTypeRadio[0].checked) );
    setEnabled( xhibitForm.firstNameText,  xhibitForm.legalRepTypeRadio[0].checked  );
    setEnabled( xhibitForm.surnameText,    xhibitForm.legalRepTypeRadio[0].checked  );
    setEnabled( xhibitForm.chambersNameText, !(xhibitForm.legalRepTypeRadio[0].checked) );

    xhibitForm.searchBtn.disabled = false;

    if( xhibitForm.cancelBtn ) {
        xhibitForm.cancelBtn.disabled = false;
    }

    if( xhibitForm.okBtn ) {
        xhibitForm.okBtn.disabled = !( isMandatoryFieldsComplete( ) );
    }
}

function setEnabled( item, state ) {
    item.disabled  = !(state);
    item.className = ( state ? 'cfFormTextBox' : 'cfFormTextBoxRead' );
}

function doRadioClicked( ) {
    clearScreen( );

    stepUpdateViewState( );
}

function clearScreen( ) {
    xhibitForm.fullNameText.value      = "";
    xhibitForm.firstNameText.value     = "";
    xhibitForm.surnameText.value       = "";
    xhibitForm.chambersNameText.value  = "";
    xhibitForm.legalRepId.value        = "";
}

function isMandatoryFieldsComplete( ) {
    result = false;

    if( xhibitForm.legalRepId.value == "" ) {
        result = false;
    }
    else {
        result = true;
    }

    return result;
}

function rowSelected( param ) {
    xhibitForm.legalRepId.value = param.value;

    stepUpdateViewState( );
}

okBtnClicked = false;
function checkSubmit( ) {
    if( xhibitForm.legalRepId.value == "" ) {
        alert( '<fmt:message key="signinlegalrep.noLegalRepSelected"/>' );

        return false;
    }
    else {
        if (!okBtnClicked)
        {
            disableBtns( );
            okBtnClicked = true;
            xhibitForm.trxCode.value = "DISPLAY4INPUT";
            xhibitForm.action        = "./counselsignin";
            xhibitForm.submit( );
        }
    }
}

cancelBtnClicked = false;
function doReset( ) {
    if (!cancelBtnClicked)
    {
        disableBtns( );
        cancelBtnClicked = true;
        xhibitForm.action = "./cancelselection";
        xhibitForm.reset( );
        stepUpdateViewState( );
        xhibitForm.submit( );
    }
}

searchBtnClicked = false;
function doSearch( ) {
    if( xhibitForm.fullNameText.value.length     == 0
    &&  xhibitForm.firstNameText.value.length    == 0
    &&  xhibitForm.surnameText.value.length      == 0
    &&  xhibitForm.chambersNameText.value.length == 0 ) {
        alert( '<fmt:message key="signinlegalrep.noSearchCriteria"/>' );
    }
    else {
        if (!searchBtnClicked)
        {
            disableBtns( );
            searchBtnClicked = true;
            xhibitForm.trxCode.value = "SEARCH";
            xhibitForm.action        = "./searchlegrep";
            xhibitForm.submit( );
        }
    }
}

function disableBtns( )
{
    xhibitForm.searchBtn.disabled = true;
    if (xhibitForm.okBtn != null)
    {
        xhibitForm.okBtn.disabled = true;
        xhibitForm.cancelBtn.disabled = true;
    }
}

function checkKey(){
   if (event.keyCode==13)
        doSearch();
}
</script>

<form name="xhibitForm" method="POST">
<input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
<table border="0" width="100%">
  <tr>
    <td width="100%">
      <table border="0">
        <tr>
          <td width="100%" class="cfPageTitle"><fmt:message key="signinlegalrep.title"/></td>
        </tr>
      </table>
      <table border="0" width="100%">
        <tr>
          <td width="100%">
            <table border="0">
              <tr>
                <td align="right" valign="top" class="cfFormData" colspan="2">&nbsp;</td>
              </tr>

              <tr>
                <td align="right" valign="top" class="cfFormLabel" nowrap><fmt:message key="signinlegalrep.legalRepType"/></td>
                <td>
                  <table border="0">
                    <tr>
                      <td width="50%" class="cfFormData" nowrap><input class="cfFormRadio" type="radio" value="BARRADIO" <c:if test="${requestScope.legalRepTypeRadio == 'BARRADIO' || requestScope.legalRepTypeRadio == null}"> checked </c:if> name="legalRepTypeRadio" onclick="doRadioClicked( );"><fmt:message key="signinlegalrep.barrister"/></td>
                      <td width="50%" class="cfFormData" nowrap><input class="cfFormRadio" type="radio" value="SOLRADIO" <c:if test="${requestScope.legalRepTypeRadio == 'SOLRADIO'}"> checked </c:if>                                           name="legalRepTypeRadio" onclick="doRadioClicked( );"><fmt:message key="signinlegalrep.solicitor"/></td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td align="right" class="cfFormLabel" nowrap><fmt:message key="signinlegalrep.fullName"/></td>
                <td class="cfFormData">
                          <input class="cfFormTextBox" name="fullNameText" type="text" length="30" size="20" onKeyPress="checkKey(event);" value="<c:out value="${requestScope.fullNameText}"/>">
                </td>
              </tr>
              <tr>
                <td align="right" class="cfFormLabel" nowrap><fmt:message key="signinlegalrep.firstName"/></td>
                <td class="cfFormData">
                          <input class="cfFormTextBox" name="firstNameText" type="text" length="30" size="20" onKeyPress="checkKey(event);" value="<c:out value="${requestScope.firstNameText}"/>">
                </td>
              </tr>
              <tr>
                <td align="right" class="cfFormLabel" nowrap><fmt:message key="signinlegalrep.surname"/></td>
                <td class="cfFormData">
                          <input class="cfFormTextBox" name="surnameText" type="text" length="30" size="20" onKeyPress="checkKey(event);" value="<c:out value="${requestScope.surnameText}"/>">
                </td>
              </tr>
              <tr>
                <td align="right" class="cfFormLabel" nowrap><fmt:message key="signinlegalrep.chambersName"/></td>
                <td class="cfFormData">
                          <input class="cfFormTextBox" name="chambersNameText" type="text" length="30" size="20" onKeyPress="checkKey(event);" value="<c:out value="${requestScope.chambersNameText}"/>">
                </td>
              </tr>
              <tr>
                <td class="cfFormLabel"></td>
                <td class="cfFormData">
                          <input type="button" value="<fmt:message key="signinlegalrep.searchBtn"/>" name="searchBtn" class="cfFormButton" onclick="doSearch( );">
                </td>
              </tr>
              <tr>
                <td class="cfFormLabel"></td>
                <td class="cfFormData">&nbsp;</td>
              </tr>
              <tr>
                <td class="cfFormLabel"></td>
                <td class="cfFormData">
                  <c:choose>
                    <c:when test="${requestScope.trxCode == 'SEARCH'}">
                      <c:choose>
                        <c:when test="${empty sessionScope.legalRepCollection}">
                          <fmt:message key="signinlegalrep.noRecords"/>
                        </c:when>
                        <c:otherwise>
              <table border="0">
                <tr>
                  <td width="100%" class="cfFormData">
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                  <tr>
                    <td colspan="21" class="cfTableHorizontalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                  </tr>
                  <tr>
                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeader"><fmt:message key="signinlegalrep.selectCol"/></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeader"><fmt:message key="signinlegalrep.fullNameCol"/></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeader"><fmt:message key="signinlegalrep.chambersCol"/></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeader"><fmt:message key="signinlegalrep.addressCol"/></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                    <td class="cfTableHeader"><fmt:message key="signinlegalrep.postCodeCol"/></td>
                    <td class="cfTableHeaderSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                    <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                  </tr>
                  <c:forEach var="item" items="${sessionScope.legalRepCollection}">
                      <tr>
                       <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td bgcolor="#FFFFFF" class="cfTableMainTopCentered">
                        <input class="cfFormRadioInTable" type="radio" value="<c:out value="${item.legalRepId}"/>" name="selectedLegalRepId" onclick="rowSelected( this );"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td bgcolor="#FFFFFF" class="cfTableMainTop"><c:out value="${item.fullName}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td bgcolor="#FFFFFF" class="cfTableMainTop"><c:out value="${item.chambersName}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td bgcolor="#FFFFFF" class="cfTableMainTop"><c:out value="${item.addressLine01}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                        <td bgcolor="#FFFFFF" class="cfTableMainTop" nowrap><c:out value="${item.postCode}"/></td>
                        <td class="cfTableMainSpace"><img src="/Static/images/blank.gif" width="1" height="1"></td>

                        <td class="cfTableVerticalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                      </tr>
                      <tr>
                        <td colspan="21" class="cfTableHorizontalBorder"><img src="/Static/images/blank.gif" width="1" height="1"></td>
                      </tr>
                  </c:forEach>
                </table>
                <div align="right">
                  <table border="0">
                    <tr>
                      <td width="100%" colspan="2" class="cfFormData">&nbsp;</td>
                    </tr>
                    <tr>
                      <td width="50%"><input type="button" value="<fmt:message key="signinlegalrep.okBtn"/>" name="okBtn" class="cfFormButton" onclick="checkSubmit( );">
                      </td>
                      <td width="50%"><input type="button" value="<fmt:message key="signinlegalrep.cancelBtn"/>" name="cancelBtn" class="cfFormButton" onclick="doReset( );">
                      </td>
                    </tr>
                  </table>
                </div>
                  </td>
                </tr>
              </table>
                </c:otherwise>
              </c:choose>
            </c:when>
                  </c:choose>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<input type="hidden" name="trxCode" value="">
<input type="hidden" name="legalRepId" value="">
</form>

<script language="JavaScript">
stepUpdateViewState( );
</script>

<%--
  - Set the default skeleton parameters then include skeleton
  --%>



