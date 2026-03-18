<%@ taglib uri='http://java.sun.com/jstl/core' prefix='c' %>
<html>
    <head>
               <title>Public Display: Court Selection</title>
               <link rel="stylesheet" type="text/css" href="/PublicDisplay/css/general.css"/>
    </head>
    </body>
        <h1>Display Configuration</h1>
        <h2>IMPORTANT</h2>
        Please make sure you have read all the relevant <a href="../help">administrators help pages</a> before configuring the
        Public Display application or any of the Public Display screens as they contain important configuration information.<br/><br/><br/>
        <h2>Courts</h2>
            <table>
              <c:forEach items="${courts}" var="court">
                 <tr>
                    <td>
                        <a href='./DisplaySelectorServlet?courtId=<c:out value="${court.primaryKey}"/>'><c:out value="${court.displayName}"/></a></td>
                 </tr>
                </c:forEach>
             </table>
    </body>
</html>
