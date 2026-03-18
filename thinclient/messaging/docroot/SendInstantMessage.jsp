<%@page import="uk.gov.courtservice.xhibit.web.messaging.bean.FlattenedTree,uk.gov.courtservice.xhibit.web.messaging.bean.DisplayLineNode"%>
<%--
  - The format tag lib is used to i18n messages
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>

<fmt:bundle basename="Pages">
<fmt:message key="button" var="buttonURL"/>
<fmt:message key="closewindowbutton" var="closeButtonURL"/>
</fmt:bundle>
<%--
  - The html to be included in the main page
  --%>
<table width="100%" cellpadding="0" cellspacing="0" border="0"/>
<tr>
  <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td width="20"><img src="/Static/images/blank.gif"></td>
  <td>
    <form name="sendinstantmessage" action="<c:url value="/instantmessagesent"/>" method="post">
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
                  <fmt:message key="sendinstantmessage.title"/>
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
          <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psFormNameLeft">
            <fmt:message key="destinations"/>
          </td>
          <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
          <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
          <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psFormValue">
            <table width="100%">
            <tr>
              <c:forEach items="${requestScope.lines}" var="line" varStatus="s">
                  <c:choose>
                      <c:when test="${line.type == 'context'}">
                          <tr>
                              <td class="psFormNameLeft">
                                  <c:out value="${line.displayName}"/>
                              </td>
                          </tr>
                      </c:when>
                      <c:when test="${line.type == 'topic'}">
                      <c:if test="${s.count % 3 == 0}"></tr><tr></c:if>
                          <td class="psFormValue">
                              <img src="/Static/images/blank.gif" HEIGHT="1" WIDTH="<c:out value="${line.depth * 10}"/>">
                              <input type="checkbox" name="CHB_<c:out value="${line.jndiName}"/>">
                              <c:out value="${line.displayName}"/>
                          </td>
                    </c:when>
                  </c:choose>
              </c:forEach>
               </tr>
            </table>
          </td>
        </tr>
      </table>
  </td>
  <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td width="20"><img src="/Static/images/blank.gif"></td>
  <td class="psFormError">
    <c:if test="${requestScope.errors.recipientError != null}">
    <fmt:message key="${requestScope.errors.recipientError}"/>
    </c:if>
  </td>
  <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td width="20"><img src="/Static/images/blank.gif"></td>
  <td class="psFormNameLeft">
    <fmt:message key="message"/>
  </td>
  <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td width="20"><img src="/Static/images/blank.gif"></td>
  <td class="psFormValue"><textarea rows="5" cols="60" type="textarea" name="message"><c:if test="${requestScope.errors.messageValue != null}"><c:out value="${requestScope.errors.messageValue}"/></c:if></textarea></td>
  <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
  <td width="20"><img src="/Static/images/blank.gif"></td>
  <td class="psFormError">
    <c:if test="${requestScope.errors.messageError != null}">
    <fmt:message key="${requestScope.errors.messageError}"/>
    </c:if>
  </td>
  <td class="psPageTitleVerticalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
<tr>
    <td width="20"><img src="/Static/images/blank.gif"></td>
    <td>
      <table cellpadding="0" cellspacing="0" border="0">
        <tr>
          <td colspan="8" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psPageAction">
            <c:set scope="request" var="buttonTextKey" value="sendinstantmessage.form.submit"/>
            <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('sendinstantmessage')"/>
            <c:import url="${buttonURL}"/>
          </td>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
          <td class="psPageAction">
            <c:set scope="request" var="buttonTextKey" value="sendinstantmessage.form.close"/>
            <c:import url="${closeButtonURL}"/>
          </td>
          <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
        <tr>
          <td colspan="8" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
        </tr>
      </table>
    </td>
</tr>
<input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
</form>
<td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
<tr>
  <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
</tr>
</table>

