package uk.gov.courtservice.xhibit.xmlbinding.systemtests.util;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Return a Dom representation of an order xml string.
 */
public class DomConvertor
{
   private Document order;

	/**
	 * Constructor for DomFactory.
	 */
	public DomConvertor(String s)
	{
		setDom(s);
	}

	public void setDom(String s)
    {

        try
        {
            // Get Document Builder Factory
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // Turn on validation, and turn off namespaces
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();
            System.out.println("ABOUT TO PARSE STRING");
            ByteArrayInputStream stream = new ByteArrayInputStream(s.getBytes());
            order = builder.parse(stream);
            System.out.println("PARSED STRING");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public Document getDom()
    {
    	return this.order;
    }

    public Object getValue(String name)
    {
        Node node = findNodeValue(name);
        System.out.println("node value was "+node);
        if (node == null)
            return null;
        return node.getNodeValue();
    }

    private Node findNodeValue(String name)
    {
        try
        {
            if(name  == null) {
                throw new NullPointerException("name for node was null");
            }

            Node node = XPathAPI.selectSingleNode(order, name);

            if (node == null)
            {
                return null;
            }


            return node.getFirstChild();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
