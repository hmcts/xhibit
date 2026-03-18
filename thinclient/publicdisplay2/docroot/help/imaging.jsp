<% request.setAttribute("title", "Imaging help page.");%>
<%@ include file="header.jsp"%>
        <h2>Imaging</h2>
        <h3>Intro</h3>

        <p>The Public Display application makes use of dynamic images; that is images which are produced on demand on
            the presentation server rather than being created by hand in Photoshop etc. These images are returned
            with appropriate caching headers to ensure that they are not constantly requested by the web browser on
            each refresh. If the browser has a copy in it's cache it will not request another.</p>
        <p>The current dynamic images are :
            <ul>
            <li>The court heading image. This is the pretty graphic at the top of the page. This image is generated from
                a gif in the images directory of WEB-INF with a textual overlay which uses the standard font color and adds
                a fairly transparent white shadow to distinguish it from the background.
                    Here is an example: <img src="/PublicDisplay/heading?width=800&height=30&x=5&y=24&fontSize=20&text=Example"/></li>
            <li>The document title eg. 'Court List' this a simple textual image which uses the standard font color and a light
                grey shadow. The image is supplied a maximum width setting and is then automatically cropped to the right
                sze when it is returned.</li>
            </ul>
       <p>
          Some links:
           <li>

          <li><a href="http://www.visualmining.com/support/server/solarisxvfb.tar.gz">XVFB</a><li>

          <li><a href="http://www.idevelopment.info/data/Unix/General_UNIX/GENERAL_XvfbforSolaris.shtml">XVFB for solaris</a><br/></li>

          </li>

          An article explaining server-side imaging and mentioning the dependency on X windows:<br/></br>

         <a href=" http://developers.sun.com/solaris/tech_topics/java/articles/awt.html">Serverside Imaging</a> <br/>

      </p>

<%@ include file="footer.jsp"%>
