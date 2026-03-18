package uk.gov.courtservice.xhibit.courtlog.helpers.xsl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Helper class used to provide several delegate methods that perform XML
 * transformation using XSL.
 * 
 * @author tz0d5m
 * @version $Revision: 1.3 $
 * @see uk.gov.courtservice.xhibit.courtlog.helpers.xsl.Translator
 */
public class RunCourtLogXslHelper {
    private static final Logger log = CSServices.getLogger(CourtLogXslHelper.class);

    private RunCourtLogXslHelper() {
        // private constructor to prevent instantiation...
    }

    /**
     * Process the command line arguments, for more detail see help method
     * output.
     * 
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // initialise arguments
        String inFileName = null;
        String outFileName = null;
        String xslResourceName = null;

        Integer eventType = null;
        Date eventDate = null;
        TranslationType translationType = null;
        String language = "en";
        String country = "GB";
        String encoding = "UTF8";
        TranslationContext context = null;

        // process arguments
        for (int i = 0; i < args.length; i++) {
            // help
            if ("-?".equals(args[i])) {
                help(0);
            }

            // event type
            else if ("-e".equals(args[i])) {
                if (++i < args.length) {
                    try {
                        eventType = new Integer(args[i]);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Invalid event type \"" + args[i] + "\" specified for -e.");
                        help(1);
                    }
                } else {
                    System.out.println("No event type specified for -e.");
                    help(1);
                }
            }
            // event date
            else if ("-d".equals(args[i])) {
                if (++i < args.length) {
                    try {
                        eventDate = parseDate(args[i]);
                    } catch (ParseException pe) {
                        System.out.println("Invalid date \"" + args[i] + "\" specified for -d.");
                        help(1);
                    }
                } else {
                    System.out.println("No date specified for -d.");
                    help(1);
                }
            }
            // translation type
            else if ("-t".equals(args[i])) {
                if (++i < args.length) {
                    try {
                        translationType = TranslationType.valueOf(args[i]);
                    } catch (IllegalArgumentException nfe) {
                        System.out.println("Invalid translation type \"" + args[i] + "\" specified for -e.");
                        help(1);
                    }
                } else {
                    System.out.println("No translation type specified for -e.");
                    help(1);
                }
            }
            // langugage
            else if ("-l".equals(args[i])) {
                if (++i < args.length) {
                    language = args[i];
                } else {
                    System.out.println("No language specified for -l.");
                    help(1);
                }
            }
            // country
            else if ("-c".equals(args[i])) {
                if (++i < args.length) {
                    country = args[i];
                } else {
                    System.out.println("No country specified for -c.");
                    help(1);
                }
            }
            // country
            else if ("-u".equals(args[i])) {
                if (++i < args.length) {
                    encoding = args[i];
                } else {
                    System.out.println("No encoding specified for -u.");
                    help(1);
                }
            }
            // context property
            else if ("-p".equals(args[i])) {
                if (++i < args.length) {
                    String key = args[i];
                    if (++i < args.length) {
                        if (context == null) {
                            context = new TranslationContext();
                        }
                        context.put(key, args[i]);
                    } else {
                        System.out.println("No value specified for -p.");
                        help(1);
                    }
                } else {
                    System.out.println("No key specified for -p.");
                    help(1);
                }
            }
            // outFileName
            else if ("-o".equals(args[i])) {
                if (++i < args.length) {
                    outFileName = args[i];
                } else {
                    System.out.println("No name specified for -o.");
                    help(1);
                }
            }
            // xslFileName
            else if ("-x".equals(args[i])) {
                if (++i < args.length) {
                    xslResourceName = args[i];
                } else {
                    System.out.println("No name specified for -x.");
                    help(1);
                }
            }
            // Input File
            else {
                inFileName = args[i];
            }
        }

        // check arguments
        boolean mandatoryArgumentsComplete = true;
        if (inFileName == null) {
            System.out.println("Required XML file not specified.");
            mandatoryArgumentsComplete = false;
        }
        if (translationType == null) {
            System.out.println("Required translation type not specified.");
            mandatoryArgumentsComplete = false;
        }
        if (eventType == null) {
            System.out.println("Required event type not specified.");
            mandatoryArgumentsComplete = false;
        }
        if (eventDate == null) {
            System.out.println("Required event date not specified.");
            mandatoryArgumentsComplete = false;
        }
        if (!mandatoryArgumentsComplete) {
            help(1);
        }

        // process arguments
        process(inFileName, outFileName, xslResourceName, eventType, eventDate, translationType, language, country,
                encoding, context);
    }

    private static void process(String inFilePath, String outFilePath, String xslResourceName, Integer eventType,
            Date eventDate, TranslationType translationType, String language, String country, String fileEncoding,
            TranslationContext context) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Processing Event:\n" + "    inFilePath = " + inFilePath + "\n" + "    outFilePath = "
                    + outFilePath + "\n" + "    xslResourceName = " + xslResourceName + "\n" + "    eventType = "
                    + eventType + "\n" + "    eventDate = " + formatDate(eventDate) + "\n" + "    translationType = "
                    + translationType + "\n" + "    language = " + language + "\n" + "country = " + country + "\n"
                    + "    fileEncoding = " + fileEncoding + "\n" + "     context = " + context);
        }

        String in = readFile(inFilePath, fileEncoding);

        if (log.isDebugEnabled()) {
            log.debug("Input: \n" + in);
        }

        String out = CourtLogXslHelper.translateEvent(in, eventDate, eventType, getLocale(language, country),
                translationType, context, xslResourceName);

        if (log.isDebugEnabled()) {
            log.debug("Output: \n" + out);
        }

        writeFile(getOutFilePath(inFilePath, outFilePath), fileEncoding, out);
    }

    private static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(date);
    }

    private static String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }

    private static String getOutFilePath(String inFilePath, String outFilePath) {
        if (outFilePath != null) {
            return outFilePath;
        }
        int extIndex = inFilePath.lastIndexOf('.');
        if (extIndex == -1) {
            return inFilePath + ".out";
        }
        return inFilePath.substring(0, extIndex) + ".out" + inFilePath.substring(extIndex);
    }

    private static final String readFile(String filePath, String fileEncoding) throws IOException {
        // process arguments
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), fileEncoding));
        try {
            StringBuffer buffer = new StringBuffer();

            // If UTF8 strip off BOM if first char, Java Bug 4508058
            // workaround
            int c = reader.read();
            if (c != -1 && (!fileEncoding.startsWith("UTF") || c != 0xFEFF)) {
                buffer.append((char) c);
            }

            // Append rest of file
            c = reader.read();
            while (c != -1) {
                buffer.append((char) c);
                c = reader.read();
            }

            return buffer.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                log.error("An error occured closing file \"" + filePath + "\".", ioe);
            }
        }
    }

    private static final void writeFile(String filePath, String fileEncoding, String fileContent) throws IOException {
        // process arguments
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), fileEncoding));
        try {
            // If UTF8 write BOM, Java Bug 4508058 workaround
            if (fileEncoding.startsWith("UTF")) {
                writer.write(0xFEFF);
            }
            writer.write(fileContent);
        } finally {
            try {
                writer.close();
            } catch (IOException ioe) {
                log.error("An error occured closing file \"" + filePath + "\".", ioe);
            }
        }
    }

    private static Locale getLocale(String language, String country) {
        if (country != null) {
            return new Locale(language, country);
        }
        return new Locale(language, "");
    }

    private static void help(int returnCode) {
        System.out.println("Usage: java " + CourtLogXslHelper.class.getName() + "[-options] xml");
        System.out.println("    (to process xml)");
        System.out.println("where options include:");
        System.out.println("    -d date            (required, the event date dd/MM/yyyy HH:mm:ss)");
        System.out.println("    -e eventType       (required, the numeric event number)");
        System.out.println("    -t translationType (required, one of gui, internet, cjse, display or notice)");
        System.out.println("    -x xsl             (optional, will determine from type)");
        System.out.println("    -o output          (optional, will transform file.xml -> file.out.xml)");
        System.out.println("    -p name value      (optional, will add name value pairs to translation context)");
        System.out.println("    -u encoding        (optional, will default to UTF8)");
        System.out.println("    -l language        (optional, will default to en)");
        System.out.println("    -c country         (optional, will default to GB)");
        System.out.println("    -?                 (optional, print this help and exit)");

        System.exit(returnCode);
    }

}
