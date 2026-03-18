package uk.gov.xhibit.courtservice.xmlbinding.systemtests;

import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: tzj8k5
 * Date: 03-Mar-2003
 * Time: 14:54:52
 * To change this template use Options | File Templates.
 */
public class XMLBindingXMLServlet extends HttpServlet
{
    private XmlHelperFactory helper;
    private boolean propertiesLoadError = false;
    private Enumeration e;
    private java.util.Properties helperProperties = new java.util.Properties();

    XMLBindingXMLServlet()
    {
        // get properties from file
        InputStream is = this.getServletContext().getResourceAsStream("WEB-INF/testxmlbindings.properties");
        Properties helperProperties = loadProperties(is);
        helper = new XmlHelperFactory(helperProperties);
        produceHtmlOptionTag(helperProperties);
    }

    public Properties loadProperties(InputStream is)
    {
        if (is != null)
        {
            try
            {
                helperProperties.load(is);
            }
            catch (IOException e)
            {
                propertiesLoadError=true;
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
        return helperProperties;
    }

    public String produceHtmlOptionTag(Properties helperProperties)
    {
        // build list of order from properties file to pass to JSP
        e = helperProperties.keys();
        StringBuffer sb = null;
        sb.append("<select name='orders'>");
        while (e.hasMoreElements())
        {
            sb.append("<option value='"+ e.toString()+ "'>"+ e.toString()+"</option>");
            e.nextElement();
        }
        System.out.println("SB: " + sb);
        return sb.toString();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {

        Integer  defId = new Integer(request.getParameter ("defendant"));
        String orderType = request.getParameter("orders");

        String rtnXml = null;
        try
        {
          rtnXml = helper.getDataXmlForOrder(defId,orderType, null, "");
        } catch (OrderNotSupportedException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (OrderXMLException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }


        if (propertiesLoadError==true)
        {
        System.out.println("Error loading Properties File!");
        }
        else
        {
        System.out.println(rtnXml);
        }
        response.setContentType("text/xml");
    }
}
