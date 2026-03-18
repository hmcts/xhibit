package uk.gov.courtservice.xhibit.business.database.originalcharges;

import javax.sql.DataSource;
import org.apache.log4j.BasicConfigurator;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class RunOriginalChargesDatabaseManager {
    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();

        // Initialise Database Properties
        System.setProperty("database.driver", "oracle.jdbc.xa.client.OracleXADataSource");
        System.setProperty("database.url", "jdbc:oracle:thin:@130.177.4.44:1521:CSDBDEV3");
        System.setProperty("database.user", "xhibit");
        System.setProperty("database.password", "xhibit");
    }

    public static void main(String[] args) {
        // Initialise database, override to create standalone datasource
        OriginalChargesDatabaseManager database = new OriginalChargesDatabaseManager() {
            @Override
            protected DataSource getDataSource() {
                return new StandAloneDataSource();
            }
        };

        DefendantOnCaseVO[] docvos = database.getDefendantsOnCase(new Integer(1));
        for( DefendantOnCaseVO docItem : docvos ) {
            System.out.println("DefendantOnCase Record");
            System.out.println("======================");
            System.out.println("courtId             : " + docItem.getCourtId());
            System.out.println("defendantOnCaseId   : " + docItem.getDefendantOnCaseId());
            System.out.println("defendantId         : " + docItem.getDefendantId());
            System.out.println("surname             : " + docItem.getSurname().toUpperCase());
            System.out.println("totalIndictments    : " + docItem.getTotalIndictments());
            System.out.println("totalOriginalCharges: " + docItem.getTotalOriginalCharges());

            ChargeVO[] cvos = database.getCharges(docItem.getDefendantOnCaseId(), "G");
            System.out.println("\tOriginal Charge Records");
            System.out.println("\t=======================");
            for( ChargeVO chgItem : cvos ) {
                printCharge(chgItem);
            }
            System.out.println("\t=======================\n");
        }
    }

    private static void printCharge(ChargeVO chgItem) {
        System.out.println("\tchargeId            : " + chgItem.getChargeId());
        System.out.println("\toffenceId           : " + chgItem.getOffenceId());
        System.out.println("\tcrestOffenceFreetext: " + chgItem.getCrestOffenceFreetext());
        System.out.println("\tdefendantOnOffenceId: " + chgItem.getDefendantOnOffenceId());
        System.out.println("\tseqNo               : " + chgItem.getSeqNo());
        System.out.println("\tcrestChargeSeqNo    : " + chgItem.getCrestChargeSeqNo());
        System.out.println("\tcrestOffenceSeqNo   : " + chgItem.getCrestOffenceSeqNo());
        System.out.println("\trefOffenceId        : " + chgItem.getRefOffenceId());
        System.out.println("\toffenceCode         : " + chgItem.getOffenceCode());
        System.out.println("\toffenceDesc         : " + chgItem.getOffenceDesc());
        System.out.println("\n");

    }
}
