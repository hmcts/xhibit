<% request.setAttribute("title", "Imaging help page.");%>
<%@ include file="header.jsp"%>
        <h2>The Screens</h2>

        <h3>Correct Resolutions</h3>

        <p>The Public Display application is designed to work at the following resolutions:
         <ul>
            <li>42 Inch - 1280 x 1024</li>
            <li>18 Inch - 1280 x 1024</li>
         </ul>
        </p>

        <h3>Ant-aliased fonts</h3>

        <p>It is recommended that the Windows machines that run the public displays are using Clear Type fonts (otherwise
           known as anti-aliased fonts. This can be done by applying the setting found by right-clicking the desktop and selecting:</p>
           <pre>
               Display Properties - Appearance - Effects
           </pre>

        <p align="center"><img src="./antialiased/clear-type-box.JPG"/><br/></p>
        </p>

        <p> Anti aliasing reduces the lack of clarity of low resolution bit map fonts especially when they are placed on
            a non-contrasting background color.
        </p>

        <p>The only side effect is that this might increase CPU consumption. So again I would recommend that as the new
            public display is rolled out that we try the system with clear-type and should (which I doubt) this effect
            performance it is very simple to change back at this stage.
        </p>

        <h3>Caching Settings</h3>
        <p>
            It is extremely important to the workings of the Public Display application that the caching settings on the
            browser are correct. If they are incorrect then the display <b style="red">will</b> show out of date information.
            Below are the correct settings found under Interner Explorers Tools->Internet Options->Temporary Internet Files->Settings.
        </p>
        <img src="browser_settings/caching.JPG"/>
        <p>
           If any screens are not set up correctly the log file will start to fill with WARN log4j lines mentioning the offending screens
           hostnames so that they can be fixed.
        </p>
        <p>Another caching issue is the time on the client (web browser) and the server (presentation tier) must be in sync, a delta
        of a few seconds or even a couple of minutes may be acceptable though.</p>
        <p>
           If he browser in use is not generating an 'If-Modified-Since' header. This would cause the system to
           perform excessive re-rendering. You must change the settings of the browser such that it does produce the
           'If-Modified-Since' header. See W3C RFC2616 at http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html (section 14.25)
           for further clarification regarding this header and it's usage.
        </p>


<%@ include file="footer.jsp"%>
