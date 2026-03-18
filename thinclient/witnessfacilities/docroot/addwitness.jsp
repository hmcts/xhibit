<%--
  - Title:       addwitness.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      David Duncan
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
  <% boolean varCPS = false; if (request.isUserInRole("XHBCPS")){varCPS=true;pageContext.setAttribute("varCPS",new Boolean(varCPS));}
                        %>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <form name="addwitness" action="<c:url value="/insertwitness"/>" method="post">
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
                                        <fmt:message key="addwitness.title"/>
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
                                    <td colspan="33" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="casetype"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <c:out value="${requestScope.casetype}"/>
                                        <INPUT type="hidden" value="<c:out value="${requestScope.casetype}"/>" name="casetype">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">

                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="age"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <input size="3" maxlength="3" type="text" name="age" value="<c:out value="${requestScope.values.age}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.age != null}">
                                            <fmt:message key="${requestScope.errors.age}"/>
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
                                        <fmt:message key="trialsession"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <c:choose>
                                        <c:when test="${requestScope.ts != null}">
                                            <td class="psFormValue">
                                                <select name="trialsession">
                                                    <option value="-1">-</option>
                                                    <c:forEach var="item" items="${requestScope.ts}">
                                                        <option value="<c:out value="${item.id}"/>"
                                                        <c:if test="${item.id == requestScope.values.trialsession}">SELECTED
                                                        </c:if>
                                                        >
                                                        <c:out value="${item.dayNumber}"/>
                                                        -
                                                        <fmt:formatDate value="${item.appearanceDate}" dateStyle="short"/>
                                                        -
                                                        <c:out value="${item.sessionType}"/>
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psFormError">
                                                <c:if test="${requestScope.errors.trialsession != null}">
                                                    <fmt:message key="${requestScope.errors.trialsession}"/>
                                                </c:if>
                                            </td>
                                            <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${requestScope.errors.trialsession != null}">
                                                    <td class="psFormError" colspan="3">
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="psFormValue" colspan="3">
                                                </c:otherwise>
                                            </c:choose>

                                                <fmt:message key="notrialsessions"/>
                                                <INPUT type="hidden" value="-1" name="trialsession">
                                            </td>
                                        </c:otherwise>

                                        <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>                                        
                                    </c:choose>
                                   
                                </tr>
                                <tr>
                                    <td colspan="7" class="psFormHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                </tr>
                                <tr>
                                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormName">
                                        <fmt:message key="witnessname"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <input size="30" maxlength="30" type="text" name="witnessname" value="<c:out value="${requestScope.values.witnessname}"/>">
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.witnessname != null}">
                                            <fmt:message key="${requestScope.errors.witnessname}"/>
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
                                        <fmt:message key="witnessstatus"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <select name="witnessstatus">
                                            <option value="-">-</option>
                                            <c:choose>
                                                <c:when test="${requestScope.values.witnessstatus == 'Ordinary'}">
                                                    <option selected>
                                                    <fmt:message key="ordinary"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>
                                                    <fmt:message key="ordinary"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.values.witnessstatus == 'Expert'}">
                                                    <option selected>
                                                    <fmt:message key = "expert"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>
                                                    <fmt:message key = "expert"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.values.witnessstatus == 'Interpreter'}">
                                                    <option selected>
                                                    <fmt:message key = "interpreter"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>
                                                    <fmt:message key = "interpreter"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.values.witnessstatus == 'Juvenile'}">
                                                    <option selected>
                                                    <fmt:message key = "juvenile"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>
                                                    <fmt:message key = "juvenile"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${requestScope.values.witnessstatus == 'Police Officer'}">
                                                    <option selected>
                                                    <fmt:message key = "policeofficer"/>
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option>
                                                    <fmt:message key = "policeofficer"/>
                                                    </option>
                                                </c:otherwise>
                                            </c:choose>
                                        </select>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.witnessstatus != null}">
                                            <fmt:message key="${requestScope.errors.witnessstatus}"/>
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
                                        <fmt:message key="expectedarrivaltime"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue">
                                        <input size="5" maxlength="5" type="text" name="expectedarrivaltime" value="<c:out value="${requestScope.values.expectedarrivaltime}"/>">
                                        <fmt:message key="timeformat"/>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.expectedarrivaltime != null}">
                                            <fmt:message key="${requestScope.errors.expectedarrivaltime}"/>
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
                                        <fmt:message key="notes"/>
                                        :</td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>

                                    <td class="psFormValue"><TEXTAREA name="notes" cols="80" rows="2" wrap="OFF" width="95%" offsetLeft="true"><c:out value="${requestScope.values.notes}"/></TEXTAREA>
                                    </td>
                                    <td class="psFormVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                    <td class="psFormError">
                                        <c:if test="${requestScope.errors.notes != null}">
                                            <fmt:message key="${requestScope.errors.notes}"/>
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
                <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                    <c:if test="${requestScope.week != null}">
                        <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week">
                    </c:if>
            </form>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psFormMainInset"><img src="/Static/images/blank.gif"></td>
                    <td class="psFormError">
                        <c:choose>
                            <c:when test="${requestScope.errormessage != null}">
                                <c:out value="${requestScope.errormessage}"/>
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
                                            <td colspan="3" class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                                        </tr>
                <tr>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:choose>
                                                    <c:when test="${requestScope.week != null}">
                                                        <form class="psForm" action="<c:url value="/caseskeletonschedule"/>" method="post" name="cancel">                                                       
                                                    </c:when>
                                                            <c:otherwise>
                                                                <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="cancel">                                                             
                                                            </c:otherwise>
                                                </c:choose>
                                                                    <c:set scope="request" var="buttonTextKey" value="cancel"/>
                                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('cancel')"/>
                                                                    <c:import url="${menuButtonURL}"/> 
                                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="id">
                                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                                    <c:if test="${requestScope.week != null}">
                                                                        <input type="hidden" value="<c:out value="${requestScope.week}"/>" name="week">
                                                                    </c:if>
                                                                    
                                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:set scope="request" var="buttonTextKey" value="save"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('addwitness')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <%--
                                            - <c:if test="${requestScope.week == null}">
                                            - <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            - <td class="psPageAction">
                                            -     <c:set scope="request" var="buttonTextKey" value="signin"/>
                                            -     <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('')"/>
                                            -     <c:import url="${menuButtonURL}"/>
                                            - </td>
                                            - <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            - </c:if>
                                            - <c:if test="${varCPS && requestScope.week == null}">
                                            --%>
                                            
                                            <c:if test="${varCPS}">
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/viewtrialsession"/>" method="post" name="viewts">
                                                    <c:set scope="request" var="buttonTextKey" value="viewts"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('viewts')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                    <input type="hidden" name="pagesource" value="addwitness">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            </c:if>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>
            <input type="hidden" value="<c:out value="${requestScope.objectid}"/>" name="objectid">
            </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>
