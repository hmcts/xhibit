<%@ page import="uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer,
                 uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer"%>
<% request.setAttribute("title", "Errors");%>
<%@ include file="header.jsp"%>
        <h2>Error Handling</h2>
        <h3>Intro</h3>

        <p>Most errors in the public display thinclient will show up as a 'No Information to Display' screen. These screens are color-coded and
           if you have the luxury of a mouse pointer interactive also.</p>

        <h3>Error Color-coding</h3>

        <p>The colors and their meanings are, click on the color to see snapshot examples.</p>
            <ul>
            <li><b>Black</b> - There is no connection to the server at all (equivalent to a browser 404 error).</li>
            <li><a href="./errors/grey.GIF">Grey</a> - This is a holding page, the public display browser application has not finished starting up.</li>
            <li><a href="./errors/red.GIF">Red</a> -  The rotation set could not be found or there was an error obtaining the rotation set for the URL.
                              (equivalent to a server 50X error for the rotation set window).</li>
            <li><b>Yellow</b> - A general problem occured, normally within the FileServlet (equivalen to a general 500 server error)..</li>
            <li><b>Purple</b> - The server (or service) is unavailable normally this is a Weblogic 503 error.</li>
            <li><a href="./errors/blue.gif">Blue</a> - Document/page could not be found (equivalent to a server 404 error).</li>
            <li><b>No color bars</b> - There is simply no information to display.</li>
            </ul>

        <h3>Getting a Stack Trace</h3>
        <p>Simply move your mouseover the color bars and if there is any further information it will be displayed.



<%@ include file="footer.jsp"%>
