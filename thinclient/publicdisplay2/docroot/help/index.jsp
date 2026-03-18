<%@ page import="uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer,
                 uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer"%><html>

<% request.setAttribute("title", "Help and admin pages.");%>
<%@ include file="header.jsp"%>

        <h2>This is the public display help page.</h2>

        <p>Please make sure you have read the following before configuring the Public Display application or the
           Public Display screens.<p>

        <a href="config.jsp">Configuration Help Page</a> <br/>
        <a href="status.jsp">Public Display web application status.</a> <br/>
        <a href="browserapp.jsp">Description of the Browser application</a> <br/>
        <a href="imaging.jsp">Imaging in the Public Display application</a> <br/>
        <a href="errors.jsp">Error handling and the color codes.</a> <br/>
        <a href="screens.jsp">Public display screens (the physical monitors).</a> <br/>

<%@ include file="footer.jsp"%>
