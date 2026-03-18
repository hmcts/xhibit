<%@ page import="uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer,
                 uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer"%>
<html>
<% request.setAttribute("title", "Configuration");%>

<%@ include file="header.jsp"%>

        <h2>This is the public display configuration help page.</h2>

        <h3>General configuration tips.</h3>

        When navigating directly to a url the public display browser application will make several integrity checks.<br/>
        <ol>
        <li>The browser you are using is supported, the current code checks for IE 6.0+ and Opera 7.0+ the two supported browser platforms. If you
            need for any reason to overide this behaviour then include ignoreBrowser=true in the query section of the URL.</li>
        <li>The operating system you are using is supported, this should not be a real issue it is just a sanity check to avoid errors
            being raised on an unsuitable platform. This also can be overriden with  ignoreOS=true in the query section of the URL.</li>
        </ol>



        <h3>Properties used by public display presentation tier.</h3>

            <br/>
            The property that sets the file store top level directory is <%=FileStorer.STORE_DIRECTORY_PROPERTY%>
            it's current value is <b><%=System.getProperty(FileStorer.STORE_DIRECTORY_PROPERTY)%></b>.
            <br/>

<%@ include file="footer.jsp"%>
