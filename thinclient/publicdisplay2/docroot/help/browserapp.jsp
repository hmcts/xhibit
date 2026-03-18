<% request.setAttribute("title", "Browser app");%>
<%@ include file="header.jsp"%>


        <h2>The Browser Application</h2>
        <h3>The Frames</h3>

        <p>The screen on the Public Display browser application is divided into several sections. On the right hand side
        is the document to be displayed. On the left hand side are four sub frames.</p>
        <p>The four sub frames are :
            <ul>
            <li>The Controller - this keeps everything moving and working, it is a static HTML page with associated JavaScript.</li>
            <li>The Manager - this manages the rotation sets, in fact all it does is refresh itself periodically. It is a JSP
                which is filled with the JavaScript required to set the current rotation set for the controller to use.</li>
            <li>The Error Frame - not used will be removed.</li>
            <li>The Log - this is a scrolling and finite length log which all the frames write to, allowing the application to be
                debugged.</li>
            </ul>

       <h3>Using the LHS frame</h3>
       <p>To specify the width of the LHS frame pass the argument <b>frameWidth</b> typical values could be 20 or 80 they are always a percentage value (ie less than 100).</p>
<%@ include file="footer.jsp"%>
