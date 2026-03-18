<%@ taglib uri='http://java.sun.com/jstl/core' prefix='c' %>
<html>
    <head>
        <title>Public Display: Display Selection</title>
        <link rel="stylesheet" type="text/css" href="/PublicDisplay/css/general.css"/>
        <style>
                A.toggle-button {
                    text-decoration: none;
                    font-weight:800;
                    width:30;
                   	vertical-align: top;
                }

                A {
                    text-decoration : none;
                    font-weight : bolder;
                    color:blue;
                }

                A:hover {
                    color:red;
                }

                TD {
                    vertical-align:top;
                }

                TD.court-site {
                    font-size: 10pt;
                    font-weight : bold;
                }

                TD.display-location {
                    font-size: 8pt;
                    padding-top:0;
                    padding-bottom:0;
                    margin-top:0;
                    margin-bottom:0;
                }

                .url {
                    font-size: 8pt;
                }


        </style>
        <script language="JavaScript">
            function toggle(link) {
                var display= link.parentElement.parentElement.parentElement.rows[1].cells[1].style.display;
                if(display == "none") {
                    link.parentElement.parentElement.parentElement.rows[1].cells[1].style.display= "block";
                    link.innerText="-";
                } else {
                    link.parentElement.parentElement.parentElement.rows[1].cells[1].style.display= "none";
                    link.innerText="+"
                }
            }

            function selectDisplay(display) {
                mainForm.display.value= display;
                mainForm.submit();
            }

        </script>
    </head>
    </body>
        <h2><c:out value="${drillDown.name}"/></h2>
        <p>
        <p> Select the options below and then pick the display to use.<p>

        <form id="mainForm" action="../display.html" method="GET">
            <input type="hidden" name="display" value="publicdisplay://display/islew/isleworth/OUTSIDE_COURT_ROOMS_10_AND_14/UseThisForTests">
            Logging: <input type="checkbox" name="logging" checked="true"  value="true"/><br/>
            Ignore Browser Type: <input type="checkbox" name="ignoreBrowser" value="true"/><br/>
            Ignore OS Type: <input type="checkbox" name="ignoreOS" value="true"/><br/>
            LHS frame width: <input type="text" name="frameWidth" value="0"><br/>
            Check for rotation set changes every: <input type="text" size="4" name="managerReloadMinutes" value="30"> minutes<br/>
            Apply additional stylesheet: <input type="text" size="10" name="stylesheet" value="">.<br/>
        </form>

        <table>
          <tr>
            <c:forEach items="${drillDown.values}" var="courtSite">
              <td>
                <table>
                    <tr>
                        <td class="toggle-button"><a  href="#" onclick="toggle(this)">+</a></td>
                        <td class="court-site"><c:out value="${courtSite.name}"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td style="display:none">
                            <c:forEach items="${courtSite.values}" var="displayLocation">
                            <table cellspacing="0">
                               <tr>
                                    <td class="toggle-button"><a class="toggle-button" href="#" onclick="toggle(this)">+</a></td>
                                    <td class="display-location" ><c:out value="${displayLocation.name}"/></td>
                               </tr>
                               <tr>
                                    <td></td>
                                    <td style="display:none">
                                        <c:forEach items="${displayLocation.values}" var="display">
                                            <table cellspacing="0">
                                                <tr>
                                                    <td>&nbsp;</td>
                                                    <td>
                                                            <a href="#" onClick='selectDisplay("<c:out value="${display}"/>")'>
                                                                <c:out value="${display.display}"/>
                                                            </a>
                                                                <br/>
                                                                <input type="text" size="50" class="url" value='<c:out value="${display}"/>'/>
                                                                <br/>
                                                   </td>
                                                </tr>
                                            </table>
                                        </c:forEach>
                                    </td>
                                </tr>
                              </table>
                            </c:forEach>
                        </td>
                    </tr>
                </table>
             </td>
            </c:forEach>
          <tr>
        </table>
    </body>
</html>
