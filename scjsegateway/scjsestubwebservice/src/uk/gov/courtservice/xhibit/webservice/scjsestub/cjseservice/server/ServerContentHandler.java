package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;

public class ServerContentHandler extends DefaultHandler
{
        private static final Logger log = CSServices.getLogger(ServerContentHandler.class);

        public ServerContentHandler()
        {
        }

        public void log(String o)
        {
           log.debug("************* " + o + " ************");
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
          log.warn("Warning: "+ saxpe);
        }

        public void error(SAXParseException saxpe) throws SAXException
        {
          log.error("Error: "+ saxpe);
          throw saxpe;
        }

        public void fatalError(SAXParseException saxpe) throws SAXException
        {
          log.fatal("Fatal Error: "+ saxpe);
          throw saxpe;
        }
}
