<%--
  - Title:       maintainwitnessdetails.jsp (jsp page fragment)
  -
  - Description: This file displays witnessdetails.
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Edward Cawley, Xdevelopment LLP (2003)
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
  <% boolean varCPS = false; if (request.isUserInRole("XHBCPS")){varCPS=true;pageContext.setAttribute("varCPS",new Boolean(varCPS));}
                        %>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
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
                                    <fmt:message key="maintainwitnessdetailsfuture.title"/>
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
                <c:choose>
                    <c:when test="${requestScope.caseDetail != null}">
                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="court"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="defendantname"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="casenumber"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="policeofficer"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="cpscaseworker"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                </tr>

                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.courtName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:forEach var="defendant" items="${requestScope.caseDetail.defendantNames}">
                            <c:out value="${defendant}"/>
                            <br>
                        </c:forEach>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.caseNumber}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.policeOfficerAttending}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="wfTableMainTop">
                        <c:out value="${requestScope.caseDetail.cpsCaseWorker}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="21" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:when>
                </c:choose>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <!-- <table width="100%" cellpadding="0" cellspacing="0" border="0"> old version
                <c:choose>
                    <c:when test="${requestScope.witnesses != null}">
                <tr>
                    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="name"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="status"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="age"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="dueat"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="arrived"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="released"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="totaltime"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>


                </tr>

                <tr>
                    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.witnesses}">

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.name}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.status}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.age}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.dueAt}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.dueAt}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.arrived}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.arrived}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.released}" dateStyle="short"/>
                        &nbsp;
                        <fmt:formatDate value="${item.released}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:message key="witnesstotaltime">
                            <fmt:param value="${item.totalTimeHours}"/>
                            <fmt:param value="${item.totalTimeMinutes}"/>
                        </fmt:message>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                </tr>
                <tr>
                    <td colspan="29" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="33">
                        <fmt:message key="nowitnesses"/>
                    </td>
                </tr>
                </c:otherwise>
                </c:choose>
            </table> -->
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <c:choose>
                    <c:when test="${requestScope.witnesses != null}">
                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="trialsession"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="name"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="status"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="age"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="dueat"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="arrived"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="released"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="totaltime"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>

                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.witnesses}">

                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.dayNumber}"/>
                        -
                        <fmt:formatDate value="${item.trialSession.appearanceDate}" dateStyle="short"/>
                        -
                        <c:out value="${item.sessionType}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.name}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.status}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:if test="${item.age != '-1'}">
                            <c:out value="${item.age}"/>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.dueAt}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.arrived}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:formatDate value="${item.released}" pattern="HH:mm"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <fmt:message key="witnesstotaltime">
                            <fmt:param value="${item.totalTimeHours}"/>
                            <fmt:param value="${item.totalTimeMinutes}"/>
                        </fmt:message>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>

                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:choose>
                            <c:when test="${varCPS}">
                                <form class="psForm" action="<c:url value="/editwitness"/>" method="post" name="editwitness<c:out value="${item.id}"/>">
                                    <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    <input type="hidden" name="pagesource" value="maintainwitnessdetailsfuture">
                                    <a href="javascript:psSubmitForm('editwitness<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="editwitness"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="editwitness"/>" border="0"></a>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form class="psForm" action="<c:url value="/witnesssignin"/>" method="post" name="editform<c:out value="${item.id}"/>">
                                    <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                    <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                    <a href="javascript:psSubmitForm('editform<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="witnesssignin"/>')"
                    onMouseout="return psSetStatus(' ')"><IMG src="/Static/images/editicon.gif" alt="<fmt:message key="witnesssignin"/>" border="0"></a>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:if test="${item.type != 'Defence' && varCPS}">
                            <form class="psForm" action="<c:url value="/deletewitness"/>" method="post" name="deletewitness<c:out value="${item.id}"/>">
                                <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                                <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                                <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                <input type="hidden" name="pagesource" value="maintainwitnessdetailsfuture">
                                <a href="javascript:psSubmitForm('deletewitness<c:out value="${item.id}"/>')"
                    onMouseover="return psSetStatus('<fmt:message key="deletewitness"/>')"
                    onMouseout="return psSetStatus('')"><IMG src="/Static/images/deleteicon.gif" alt="<fmt:message key="deletewitness"/>" border="0"></a>
                            </form>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain" width="12">
                        <c:if test="${item.type != 'Defence' && item.hasNumber}">
                            <input type="hidden" value="<c:out value="${item.id}"/>" name="id"/>
                            <input type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid"/>
                            <a href="javascript:void window.open('/Messaging/sendwitnessadhocmessage?witnessid=<c:out value="${item.id}"/>','SendWitnessAdHocMessage','fullscreen=no,toolbar=no,status=no,menubar=no,scrollbars=yes,resizable=no,directories=no,location=no,width=640,height=240,left=200,top=200')"
                    onMouseover="return psSetStatus('<fmt:message key="messagewitness"/>')"
                    onMouseout="return psSetStatus('')"><IMG src="/Static/images/messageicon.gif" alt="<fmt:message key="messagewitness"/>" border="0"></a>
                        </c:if>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="41" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="37">
                        <fmt:message key="nowitnesses"/>
                    </td>
                </tr>
                </c:otherwise>
                </c:choose>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psTableHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
        <td>
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                            <tr>
                                <td>
                                    <table cellpadding="0" cellspacing="0" border="0">
                                        <tr>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/maintainwitnessdetails"/>" method="post" name="today">
                                                    <c:set scope="request" var="buttonTextKey" value="today"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('today')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/maintainwitnessdetailsall"/>" method="post" name="all">
                                                    <c:set scope="request" var="buttonTextKey" value="all"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('all')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <c:url value="/printwitnessdetailsfuture" var="printwitnessdetails"/>
                                                <c:set scope="request" var="buttonTextKey" value="print"/>
                                                <c:set scope="request" var="buttonRequestURL" value="javascript:void window.open('${printwitnessdetails}?caseid=${caseid}','Print','WIDTH=800,HEIGHT=600,statusbar=no,menubar=no,toolbar=no,scrollbars=yes')"/>
                                                <c:import url="${menuButtonURL}"/>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <c:choose>
                                                <c:when test="${varCPS}">
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            <td class="psPageAction">
                                                <form class="psForm" action="<c:url value="/editcasedetails"/>" method="post" name="editcasedetails">
                                                    <c:set scope="request" var="buttonTextKey" value="editcasedetails"/>
                                                    <c:set scope="request" var="buttonRequestURL" value="javascript:psSubmitForm('editcasedetails')"/>
                                                    <c:import url="${menuButtonURL}"/>
                                                    <INPUT type="hidden" value="<c:out value="${requestScope.caseid}"/>" name="caseid">
                                                    <input type="hidden" name="rtoken" value="<c:out value="${stoken}"/>">
                                                </form>
                                            </td>
                                            <td class="psPageActionVerticalSpace"><img src="/Static/images/blank.gif"></td>
                                            </c:when>
                                            </c:choose>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td class="psPageActionHorizontalSpace"><img src="/Static/images/blank.gif"></td>
                            </tr>
                        </table>
                    </td>
                    <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
                </tr>
            </table>
        </td>
        <td class="psTableVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

