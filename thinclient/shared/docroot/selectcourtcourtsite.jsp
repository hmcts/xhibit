<%--
  - Title:       selectcourtcomplete.jsp (jsp page fragment)
  -
  - Copyright:   Copyright (c) 2003
  - Company:     EDS
  -
  - Author:      Kevin Buckthorpe
  - Version:     V1.0.0
  --%>
<%--
  - The format tag lib is used to i18n messages  
  --%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%--
  - Get the page urls from the Pages resource bundle
  --%>


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
                                    <fmt:message key="selectcourt.title"/>
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
                    <c:when test="${requestScope.courtsites != null}">
                <tr>
                    <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                   <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="court"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="site"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableHeader">
                        <fmt:message key="crestId"/>
                    </td>
                    <td class="psTableHeaderSpace"><img src="/Static/images/blank.gif"></td>                                        
                    
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <c:forEach var="item" items="${requestScope.courtsites}">
                <tr>
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                                             
                            <a href="<c:url value="/updatecourtcourtsite"/>?courtid=<c:out value="${item.courtId}"/>&courtsiteid=<c:out value="${item.courtSiteId}"/>">
                            <img src="/Static/images/selecticon.gif" border="0"></a>
                        
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.courtName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                       <c:out value="${item.courtSiteName}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                    <td class="psTableMain">
                        <c:out value="${item.crestCourtId}"/>
                    </td>
                    <td class="psTableMainSpace"><img src="/Static/images/blank.gif"></td>
                                                            
                    <td class="psTableVerticalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                <tr>
                    <td colspan="17" class="psTableHorizontalBorder"><img src="/Static/images/blank.gif"></td>
                </tr>
                </c:forEach>
                </c:when>
                <c:otherwise>
                <tr>
                    <td  class="psPageMessage" colspan="17">
                        <fmt:message key="norecords"/>
                    </td>
                </tr>
                </c:otherwise>
                </c:choose>
            </table>
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
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
                                <td class="psPageMessage">
                                    <fmt:message key="selectterminaltext"/>
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
        </td>
        <td class="psPageVerticalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
    <tr>
        <td colspan="3" class="psPageHorizontalSpace"><img src="/Static/images/blank.gif"></td>
    </tr>
</table>

