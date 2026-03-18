package uk.gov.hmcts.datagenerator;

import org.w3c.dom.Document;
import uk.gov.hmcts.datagenerator.util.XmlUtils;

import java.io.File;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DailyListXmlUpdater is a utility class that updates an XML file with the current date and time,
 * and saves it to a specified output directory.
 */
@SuppressWarnings("PMD")
public class DailyListXmlUpdater {

    private DailyListXmlUpdater() {
        // Prevent instantiation
    }

    public static void main(String[] args) throws Exception {
        final String inputFile = args.length > 0 ? args[0] : "DailyList.xml";
        String outputFolder = args.length > 1 ? args[1] : "output";

        Date overrideDate = null;
        if (args.length > 2) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                overrideDate = dateFormat.parse(args[2]);
            } catch (ParseException e) {
                System.err.println("Invalid date format for override date (expected yyyy-MM-dd), using today.");
            }
        }

        String mode = args.length > 3 ? args[3].toUpperCase() : "CPP";
        if (!"CPP".equals(mode) && !"XHIBIT".equals(mode)) {
            System.err.println("Error: Mode must be either 'CPP' or 'XHIBIT'");
            System.exit(1);
        }

        File outDir = new File(outputFolder);
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new RuntimeException("Failed to create output directory: " + outputFolder);
        }

        Calendar now = Calendar.getInstance();
        Date currentDate = now.getTime();
        Date effectiveDate = (overrideDate != null) ? overrideDate : currentDate;

        SimpleDateFormat fileTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat pubTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat docNameFormat = new SimpleDateFormat("dd-MMM-yy");

        final String timestamp = timestampFormat.format(currentDate);
        final String pubTime = pubTimeFormat.format(currentDate);
        final String fileTimestamp = fileTimeFormat.format(currentDate);
        final String docName = "Daily List FINAL v1 " + docNameFormat.format(currentDate).toUpperCase();
        final String todayDate = dateFormat.format(effectiveDate);

        Document doc = XmlUtils.loadSecureXmlDocument(new File(inputFile));

        XmlUtils.updateTagText(doc, "DocumentName", docName);
        XmlUtils.updateTagText(doc, "TimeStamp", timestamp);
        XmlUtils.updateTagText(doc, "StartDate", todayDate);
        XmlUtils.updateTagText(doc, "EndDate", todayDate);
        XmlUtils.updateTagText(doc, "PublishedTime", pubTime);
        XmlUtils.updateTagText(doc, "HearingDate", todayDate);

        String courtCode = doc.getElementsByTagNameNS("*", "CourtHouseCode").item(0).getTextContent().trim();

        String outputFileName;
        if ("XHIBIT".equals(mode)) {
            int fourDigitRand = new SecureRandom().nextInt(9000) + 1000;
            int twoDigitRand = new SecureRandom().nextInt(90) + 10;
            outputFileName = "PDDA_XDL_" + fourDigitRand + "_" + twoDigitRand + "_" + courtCode + "_" + fileTimestamp;
        } else {
            outputFileName = "DailyList_" + courtCode + "_" + fileTimestamp + ".xml";
        }

        File outputFile = new File(outDir, outputFileName);
        XmlUtils.writeXmlToFile(doc, outputFile);

        System.out.println("Updated file saved as: " + outputFile.getAbsolutePath());
    }
}
