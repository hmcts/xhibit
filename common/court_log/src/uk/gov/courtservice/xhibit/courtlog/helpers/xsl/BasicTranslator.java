package uk.gov.courtservice.xhibit.courtlog.helpers.xsl;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.Document;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.XMLServices;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * @author pznwc5
 * 
 * Basic translator
 */
public class BasicTranslator extends Translator {

    private static final String XML_TAG_COURT_LOG_EVENT = "event";

    private static final String XSL_SOURCE_PATH = "config/courtlog/transformer/";

    private String xsl;

    public BasicTranslator(int type) {
        super(type);
    }

    /**
     * @param type
     * @param xsl
     */
    public BasicTranslator(int type, String xsl) {
        super(type);
        this.xsl = xsl;
    }

    public String translate(TranslationContext context, Locale locale, Document input, Date entryDate, Integer eventType) {
        formatDateAndTime(locale, input, entryDate);

        if (xsl == null) {
            xsl = XSL_SOURCE_PATH + XSL_TYPE[type] + "/" + eventType + ".xsl";
        }
        return transform(input, xsl);
    }

    /**
     * Translates a date and a time into the required locale format
     * 
     * @throws CourtLogException
     */
    private void formatDateAndTime(final Locale locale, final Document input, final Date entryDate) {

        XMLServices xmlServices = CSServices.getXMLServices();

        // Format the date and time, for the current locale, prior to
        // adding to the xml entry.
        final String displayDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(entryDate);
        final String displayTime = DateFormat.getTimeInstance(DateFormat.SHORT, locale).format(entryDate);

        xmlServices.addElementByTagName(input, CourtLogCRUDValue.ENTRY_DATE, displayDate, XML_TAG_COURT_LOG_EVENT);
        xmlServices.addElementByTagName(input, CourtLogCRUDValue.ENTRY_TIME, displayTime, XML_TAG_COURT_LOG_EVENT);
    }

    /**
     * Applies an xsl template to xml and send result to requested stream.
     * 
     * @param input
     *            The document to transform
     * 
     * @param xslFileName
     *            the transformation to apply
     * 
     * @return The transformed document as a string
     * 
     * @throws EDSException
     *             if anything fails.
     */
    private static String transform(final Document input, final String xslFileName) {
        return CSServices.getXSLServices().transform(input, xslFileName, null, null);
    }

}
