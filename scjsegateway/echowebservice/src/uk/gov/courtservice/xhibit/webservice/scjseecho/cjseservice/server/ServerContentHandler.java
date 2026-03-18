package uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ServerContentHandler extends DefaultHandler
{

        public ServerContentHandler()
        {
        }

        public void log(String o)
        {
                System.out.println("************* " + o + " ************");
        }

        public void startDocument()
        {
           log("Starting the document");
        }

        public void endDocument()
        {
           log("Document end");
        }

        public void startElement(java.lang.String namespaceURI,
                                 java.lang.String localName,
                                 java.lang.String qName,
                                 Attributes atts)
                  throws SAXException
         {
             log("startElement");
             log("namespaceURI: "+ namespaceURI);
             log("localName: "+ localName);
             log("qName: "+ qName);
             log("atts: "+ atts);

         }


         public void endElement(java.lang.String namespaceURI,
                                java.lang.String localName,
                                java.lang.String qName)
                throws SAXException
        {
             log("endElement");
             log("namespaceURI: "+ namespaceURI);
             log("localName: "+ localName);
             log("qName: "+ qName);
        }


// Error Handling
        public void warning(SAXParseException saxpe)
        {
          log("Warning: "+ saxpe);
        }

        public void error(SAXParseException saxpe) throws SAXException
        {
          log("Error: "+ saxpe);
          throw saxpe;
        }

        public void fatalError(SAXParseException saxpe) throws SAXException
        {
          log("Fatal Error: "+ saxpe);
          throw saxpe;
        }

}
