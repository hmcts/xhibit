package uk.gov.hmcts.datagenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.SerializationUtils;

import uk.gov.courtservice.xhibit.business.vos.services.publicnotice.DisplayablePublicNoticeValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ActivateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.AddCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.CaseStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.ConfigurationChangeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.HearingStatusEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.MoveCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicNoticeEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.UpdateCaseEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseChangeInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CaseCourtLogInformation;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.CourtRoomIdentifier;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtConfigurationChange;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.configuration.CourtDisplayConfigurationChange;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

@SuppressWarnings("PMD")
public class PublicDisplayEventGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private PublicDisplayEventGenerator() {
        // Prevent instantiation
    }

    public static void main(String[] args) {
        String outputPath = args.length > 0 ? args[0] : "output";
        int count = args.length > 1 ? Integer.parseInt(args[1]) : 10;
        Integer overrideCourtId = args.length > 2 ? Integer.parseInt(args[2]) : null;
        Integer overrideCrestCourtId = args.length > 3 ? Integer.parseInt(args[3]) : null;
        Integer overrideCourtRoomId = args.length > 4 ? Integer.parseInt(args[4]) : null;

        File outputDir = new File(outputPath);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        Calendar baseCal = Calendar.getInstance();
        baseCal.add(Calendar.SECOND, -count);

        for (int i = 0; i < count; i++) {
            PublicDisplayEvent event = generateRandomEvent(overrideCourtId, overrideCourtRoomId);
            byte[] serialized = SerializationUtils.serialize(event);
            String encodedEvent = PddaSerializationUtils.encodePublicEvent(serialized);

            Calendar ts = (Calendar) baseCal.clone();
            ts.add(Calendar.SECOND, i);
            String filename = generateFilename(ts.getTime(), overrideCrestCourtId);
            File file = new File(outputDir, filename);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(encodedEvent.getBytes());
            } catch (IOException e) {
                System.err.println("Error writing file: " + file.getName());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        System.out.println("Serialized " + count + " events to: " + outputDir.getAbsolutePath());
    }

    private static String generateFilename(Date timestamp, Integer fixedPart3) {
        int part1 = 1 + RANDOM.nextInt(9999);
        int part2 = 1 + RANDOM.nextInt(99);
        int part3 = (fixedPart3 != null) ? fixedPart3 : 401 + RANDOM.nextInt(80);
        String timeStr = TIMESTAMP_FORMAT.format(timestamp);
        return "PDDA_XPD_" + part1 + "_" + part2 + "_" + part3 + "_" + timeStr;
    }

    private static PublicDisplayEvent generateRandomEvent(Integer overrideCourtId, Integer overrideCourtRoomId) {
        int eventType = RANDOM.nextInt(8);
        int courtId = (overrideCourtId != null) ? overrideCourtId : 1 + RANDOM.nextInt(81);
        int courtRoomId = (overrideCourtRoomId != null) ? overrideCourtRoomId : 1 + RANDOM.nextInt(900);
        String courtName = "Isleworth";
        int courtRoomNo = 1;
        CourtRoomIdentifier cri = new CourtRoomIdentifier(courtId, courtRoomId, courtName, courtRoomNo, new DisplayablePublicNoticeValue[0]);

        switch (eventType) {
            case 0:
                return new AddCaseEvent(cri, new CaseChangeInformation(true));
            case 1:
                return new ActivateCaseEvent(cri, new CaseChangeInformation(false));
            case 2:
                return new HearingStatusEvent(cri, new CaseChangeInformation(RANDOM.nextBoolean()));
            case 3:
                CourtRoomIdentifier toCri = new CourtRoomIdentifier(
                    (overrideCourtId != null) ? overrideCourtId : 1 + RANDOM.nextInt(81),
                    100 + RANDOM.nextInt(900),
                    "Isleworth",
                    100 + RANDOM.nextInt(900), new DisplayablePublicNoticeValue[0]);
                return new MoveCaseEvent(cri, toCri, new CaseChangeInformation(true));
            case 4:
                return new UpdateCaseEvent(cri, new CaseChangeInformation(false));
            case 5:
                return new PublicNoticeEvent(cri, RANDOM.nextBoolean());
            case 6:
                return new ConfigurationChangeEvent(generateDummyConfigurationChange(courtId));
            case 7:
                return new CaseStatusEvent(cri, generateDummyCaseCourtLogInformation(courtId, courtRoomId));
            default:
                throw new IllegalStateException("Unhandled event type index: " + eventType);
        }
    }

    private static CourtConfigurationChange generateDummyConfigurationChange(int courtId) {
        int displayId = 1 + RANDOM.nextInt(50);
        boolean forceRecreate = RANDOM.nextBoolean();
        String courtName = "Test";
        return new CourtDisplayConfigurationChange(courtId, courtName, displayId, forceRecreate);
    }

    private static CaseCourtLogInformation generateDummyCaseCourtLogInformation(int courtId, int courtRoomId) {
        CourtLogViewValue viewValue = new CourtLogViewValue();
        viewValue.setLogEntry("Test log entry " + RANDOM.nextInt(100));
        viewValue.setDateAmended(RANDOM.nextBoolean());
        viewValue.setEventType(CourtLogViewValue.EventTypes.BW_HISTORY_ISSUE_WARRANT_EVENT.intValue());

        CourtLogSubscriptionValue log = new CourtLogSubscriptionValue(viewValue);
        log.setCourtRoomId(courtRoomId);
        log.setCourtSiteId(courtId);
        log.setHearingId(5000 + RANDOM.nextInt(5000));
        log.setPnEventType(1 + RANDOM.nextInt(5));
        log.setCourtURN("URN" + RANDOM.nextInt(999999));

        return new CaseCourtLogInformation(log, RANDOM.nextBoolean());
    }
}
