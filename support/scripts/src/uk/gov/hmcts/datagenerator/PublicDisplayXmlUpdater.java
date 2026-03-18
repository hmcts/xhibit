package uk.gov.hmcts.datagenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.gov.hmcts.datagenerator.util.XmlUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * PublicDisplayXmlUpdater is a utility class that updates an XML file with the current date and
 * time, and saves it to a specified output directory.
 */
@SuppressWarnings("PMD")
public class PublicDisplayXmlUpdater {

    private PublicDisplayXmlUpdater() {
        // Prevent instantiation
    }

    public static void main(String[] args) throws Exception {
        final String inputFile = args.length > 0 ? args[0] : "PublicDisplay.xml";
        String outputFolder = args.length > 1 ? args[1] : "output";

        File outDir = new File(outputFolder);
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new RuntimeException("Failed to create output directory: " + outputFolder);
        }

        Calendar now = Calendar.getInstance();
        Calendar adjustedTime = (Calendar) now.clone();
        adjustedTime.add(Calendar.MINUTE, -15);
        Date adjustedDate = adjustedTime.getTime();
        Date currentDate = now.getTime();

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.UK);
        SimpleDateFormat dayOfMonthFormat = new SimpleDateFormat("dd");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.UK);
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat fileTimestampFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        final String dayOfWeek = dayFormat.format(adjustedDate);
        final String day = dayOfMonthFormat.format(adjustedDate);
        final String month = monthFormat.format(adjustedDate);
        final String year = yearFormat.format(adjustedDate);
        final String hour = hourFormat.format(adjustedDate);
        final String min = minuteFormat.format(adjustedDate);
        final String shortDate = shortDateFormat.format(adjustedDate);
        final String shortTime = shortTimeFormat.format(adjustedDate);
        final String fileTimestamp = fileTimestampFormat.format(currentDate);

        Document doc = XmlUtils.loadSecureXmlDocument(new File(inputFile));

        XmlUtils.updateTagText(doc, "dayofweek", dayOfWeek);
        XmlUtils.updateTagText(doc, "date", day);
        XmlUtils.updateTagText(doc, "month", month);
        XmlUtils.updateTagText(doc, "year", year);
        XmlUtils.updateTagText(doc, "hour", hour);
        XmlUtils.updateTagText(doc, "min", min);

        NodeList eventNodes = doc.getElementsByTagNameNS("*", "event");
        for (int i = 0; i < eventNodes.getLength(); i++) {
            Node event = eventNodes.item(i);
            if (event.getNodeType() == Node.ELEMENT_NODE) {
                Element eventElem = (Element) event;
                NodeList children = eventElem.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    Node child = children.item(j);
                    if ("date".equals(child.getNodeName())) {
                        child.setTextContent(shortDate);
                    } else if ("time".equals(child.getNodeName())) {
                        child.setTextContent(shortTime);
                    }
                }
            }
        }

        String courtCode = "457";
        String inputFilename = new File(inputFile).getName();
        if (inputFilename.matches("PublicDisplay_4\\d{2}_.*\\.xml")) {
            courtCode = inputFilename.substring(14, 17);
        }

        String outputFileName = "PublicDisplay_" + courtCode + "_" + fileTimestamp + ".xml";
        File outputFile = new File(outDir, outputFileName);

        XmlUtils.writeXmlToFile(doc, outputFile);

        System.out.println("Updated file saved as: " + outputFile.getAbsolutePath());
    }
}
