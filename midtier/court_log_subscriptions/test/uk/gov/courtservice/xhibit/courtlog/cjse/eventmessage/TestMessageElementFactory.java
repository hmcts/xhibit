package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.TestCase;
import java.util.Properties;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageElement;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.MessageElementFactory;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.StringMessageElement;

import java.util.Enumeration;
import java.util.ArrayList;

/**
 * <p>Title: Test the message element factory</p>
 * <p>Description: </p>
 * <p>
 * This class provides testing for the MessageElementFactory.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Eds</p>
 * @author Bob Boothby
 * @version 1.0
 */
public class TestMessageElementFactory extends TestCase
{
    //Cache the message element properties.
    private Properties elementProperties;

    /**
     * Create an instance of TestMessageElementFactory.
     * @param name The name given to the test.
     */
    public TestMessageElementFactory(String name)
    {
        super(name);
        //Get the message element properties to drive the testing.
        elementProperties = CSServices.getConfigServices().getProperties(
                MessageElementFactory.PROPERTIES_FILE_NAME);
    }

    /**
     * Checks that individual elements can be retrieved for a given name.
     */
    public void testGetElementForString()
    {
        String elementString = null;

        //Step through all the defined message elements.
        Enumeration messageElementKeys = elementProperties.keys();
        while(messageElementKeys.hasMoreElements())
        {
            //Get the next element string.
            elementString = (String) messageElementKeys.nextElement();
            //Lookup the class we expect to get.
            String expectedClassName = elementProperties.getProperty(elementString);
            //Get the MessageElement.
            MessageElement messageElement =
                    MessageElementFactory.getElementForString(elementString);
            String elementClassName = messageElement.getClass().getName();
            //Check that it is what we expect.
            assertEquals("Class expected does not match class retrieved",
                         expectedClassName, elementClassName);
        }
    }

    /**
     * This test checks that the basic behaviour for parsing a message text string
     * into message elements works in the basic case of StringMessageElements.
     */
    public void testGetElementsForMessageTextSimple()
    {
        String messageText=  "ABC" +
                             MessageElementFactory.ELEMENT_SEPARATOR +
                             "DEF" +
                             MessageElementFactory.ELEMENT_SEPARATOR +
                             "GHI" +
                             MessageElementFactory.ELEMENT_SEPARATOR +
                             "JKL";
        //At the moment we expect to get 4 elements.
        MessageElement[] messageElements =
                MessageElementFactory.getElementsForMessageText(messageText);
        assertEquals("The number of elements produced does not match the number expected.",
                     messageElements.length,
                     4);

        //Should still get 4
        messageText +=  MessageElementFactory.ELEMENT_SEPARATOR;
        messageElements =
                MessageElementFactory.getElementsForMessageText(messageText);
        assertEquals("The number of elements produced does not match the number expected.",
                     messageElements.length,
                     4);

        //Should still get 4
        messageText +=  MessageElementFactory.ELEMENT_SEPARATOR;
        messageElements =
                MessageElementFactory.getElementsForMessageText(messageText);
        assertEquals("The number of elements produced does not match the number expected.",
                     messageElements.length,
                     4);

        //Should now get 5
        messageText +=  "MNO";
        messageElements =
                MessageElementFactory.getElementsForMessageText(messageText);
        assertEquals("The number of elements produced does not match the number expected.",
                     messageElements.length,
                     5);

    }

    /**
     * Checks that the message factory successfully (and sequentially)
     * builds up the message elements into an array in the case of both
     * StringMessageElements and other defined message elements.
     */
    public void testGetElementsForMessageTextComplex()
    {
        //Both to set up a buffer for succeeding MessageElement keys
        //and to create an initial StringMessageElement.
        StringBuffer messageText = new StringBuffer("Test rubbish");

        //To record the element class names in order.
        ArrayList elementClassNames = new ArrayList();

        //Add the initial StringMessageElement class name.
        elementClassNames.add(StringMessageElement.class.getName());

        //Step through all the defined message element keys.
        String elementKey = null;
        Enumeration messageElementKeys = elementProperties.keys();
        while(messageElementKeys.hasMoreElements())
        {
            elementKey = (String) messageElementKeys.nextElement();

            //Add the key to the message text being constructed.
            messageText.append(MessageElementFactory.ELEMENT_SEPARATOR);
            messageText.append(elementKey);

            //Add the expected class name to the list.
            elementClassNames.add(elementProperties.getProperty(elementKey));
        }

        //Build the message element array.
        MessageElement[] messageElements =
                MessageElementFactory.getElementsForMessageText(messageText.toString());
        assertEquals("The number of elements produced does not match the number expected",
                     messageElements.length,
                     elementClassNames.size());

        //Check that we have the right classes in order.
        for(int i = 0; i < messageElements.length; i++)
        {
            assertEquals("Expected message element class does not match actual.",
                         elementClassNames.get(i),
                         messageElements[i].getClass().getName());
        }
    }

}