package datamigration1745;

import java.util.ArrayList;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.CourtError;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.CourtsGroupError;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.DataMigrationError;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.DefendantOnCaseError;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.DefendantsGroupError;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.Court;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.CourtsGroup;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.DataMigrationReport;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.DefendantOnOffence;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.DefendantsGroup;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.OffencesGroup;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.PTIURN;

import datamigration1745.database.DataMigrationDatabaseFactory;
import datamigration1745.database.DataMigrationDatabaseInterface;
import datamigration1745.helpers.AsnHelper;
import datamigration1745.helpers.PtiurnHelper;
import datamigration1745.vos.CourtVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.OffenceByDefendantVO;

public class DataMigration {
    Logger log = CSServices.getLogger(DataMigration.class);

    // Instance variables
    DataMigrationDatabaseInterface dbManager = DataMigrationDatabaseFactory.getInstance().getDataMigrationDatabase();
    ArrayList<Integer> courtList;
    AsnHelper asnHelper;
    PtiurnHelper ptiurnHelper;

    static int MAX_SEQ_NO_VALUE = 999;
    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Start DataMigration main method, args.length:" + args.length);
        
        DataMigrationProperties.processArguments(args);
        
        DataMigration dataMigration = new DataMigration();
        dataMigration.executeBehaviour();
    }
    
    public DataMigration() {
        executeInitialisation();
    }

    private void executeInitialisation() {
        // Initialise Database Properties
        try {
            System.setProperty("database.driver", DataMigrationProperties.getProperties().getDatabaseDriver());
            System.setProperty("database.url", DataMigrationProperties.getProperties().getDatabaseUrl());
            System.setProperty("database.user", DataMigrationProperties.getProperties().getDatabaseUser());
            System.setProperty("database.password", DataMigrationProperties.getProperties().getDatabasePassword());

            courtList = DataMigrationProperties.getProperties().getCourts();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    private void executeBehaviour() {
        boolean inUpdateMode = inUpdateMode();
        
        DataMigrationReport dataMigrationReporting = new DataMigrationReport();
        CourtsGroup         courtsGroupReporting   = new CourtsGroup();

        DataMigrationError dataMigrationExceptions = new DataMigrationError();
        CourtsGroupError   courtsGroupExceptions   = new CourtsGroupError();
        
        for(Integer courtId : courtList) {
            CourtVO courtVO = dbManager.getCourt(courtId);
            if (courtVO == null) {
                System.out.println("**** Unrecognised court id: " + courtId + " ****");
                continue;
            }
            
            DataMigrationObject dmo = new DataMigrationObject(courtId,"updateStatusI",courtId,java.util.Calendar.getInstance());
            dbManager.updateStatus(courtId.intValue(), "I");
            DataMigrationStatistics.addObject(dmo);
            
            dmo = new DataMigrationObject(courtId,"getPoliceForceData",courtId,java.util.Calendar.getInstance());
            HOPoliceForceVO[] policeForceCodes = dbManager.getPoliceForceData(courtId);
            DataMigrationStatistics.addObject(dmo);

            asnHelper = new AsnHelper(policeForceCodes);
            ptiurnHelper = new PtiurnHelper(policeForceCodes);
            
            int totalDefOnCase    = 0;
            int totalDefOnOffence = 0;           
            
            dmo = new DataMigrationObject(courtId,"getCourt",courtId,java.util.Calendar.getInstance());
            DataMigrationStatistics.addObject(dmo);
            
            Court           court4Reporting          = buildCourtReporting(courtVO);
            DefendantsGroup defendantsGroupReporting = new DefendantsGroup();
            
            CourtError           court4Exceptions          = buildCourtError(courtVO);
            DefendantsGroupError defendantsGroupExceptions = new DefendantsGroupError();

            try {
                court4Reporting.setMode(DataMigrationProperties.getProperties().getMode());
                court4Exceptions.setMode(DataMigrationProperties.getProperties().getMode());
            }
            catch( Exception e ) {
                e.printStackTrace();
            }
          
            dmo = new DataMigrationObject(courtId,"getDefendantsByCourt",courtId,java.util.Calendar.getInstance());
            DefendantByCourtVO[] defendantsByCourt = dbManager.getDefendantsByCourt(courtId);
            DataMigrationStatistics.addObject(dmo);

            for(DefendantByCourtVO defOnCase : defendantsByCourt) {
                totalDefOnCase++;
                boolean noValidASNFound    = true;
                boolean invalidPTIRUNFound = false;
                
                DefendantOnCase doc4Reporting = buildDefendantOnCaseReporting(defOnCase);
                OffencesGroup offencesGroupReporting = new OffencesGroup();

                DefendantOnCaseError doc4Exceptions = buildDefendantOnCaseError(defOnCase);

                if (defOnCase.getPtiurn() == null || defOnCase.getPtiurn().equals("")) {
                    doc4Reporting.getPTIURN().setOldValue(null);
                    doc4Reporting.getPTIURN().setNewValue(null);
                }
                else if( isValidPtiurn(defOnCase.getPtiurn()) ) {
                    doc4Reporting.getPTIURN().setOldValue(null);
                    doc4Reporting.getPTIURN().setNewValue(defOnCase.getPtiurn());
                }
                else {
                    invalidPTIRUNFound = true;
                    doc4Reporting.getPTIURN().setOldValue(defOnCase.getPtiurn());
                    doc4Reporting.getPTIURN().setNewValue(null);
                }
                
                dmo = new DataMigrationObject(defOnCase.getId(),"getOffencesByDefendant",defOnCase.getId(),java.util.Calendar.getInstance());
                OffenceByDefendantVO[] offencesByDefendant = dbManager.getOffencesByDefendant(defOnCase.getId());
                DataMigrationStatistics.addObject(dmo);
                
                int seqNr = 0;
                for(OffenceByDefendantVO defOnOffence : offencesByDefendant) {
                    totalDefOnOffence++;
                    seqNr++;
                    String asn = obtainASNFromCRNId(defOnOffence.getCrnId());
                    
                    if( noValidASNFound ) {
                        if( isValidAsn(asn)) {
                            noValidASNFound = false;
                            doc4Reporting.setASN(asn);
                        }
                    }
                    
                    DefendantOnOffence doo4Reporting = buildDefendantOnOffenceReporting(defOnOffence);
                    doo4Reporting.setSeqNo(String.format("%1$03d", new Integer(Math.min(MAX_SEQ_NO_VALUE, seqNr))));
                    offencesGroupReporting.addDefendantOnOffence(doo4Reporting);
                    
                    if( inUpdateMode ) {
                        dmo = new DataMigrationObject(defOnOffence.getId(),"updateDefendantOnOffence",doo4Reporting,java.util.Calendar.getInstance());
                        dbManager.updateDefendantOnOffence(defOnOffence.getId(), doo4Reporting.getSeqNo());
                        DataMigrationStatistics.addObject(dmo);
                    }
                }
                
                if( noValidASNFound || invalidPTIRUNFound ) {
                    doc4Exceptions.setErrorMessage(buildErrorMessage(
                            noValidASNFound, invalidPTIRUNFound, defOnCase.getPtiurn()));
                    defendantsGroupExceptions.addDefendantOnCaseError(doc4Exceptions);
                }
                
                doc4Reporting.setOffencesGroup(offencesGroupReporting);
                
                defendantsGroupReporting.addDefendantOnCase(doc4Reporting);
                
                if( inUpdateMode ) {
                    dmo = new DataMigrationObject(new Integer(doc4Reporting.getId()),"updateDefendantOnCase",doc4Reporting,java.util.Calendar.getInstance());
                    dbManager.updateDefendantOnCase(new Integer(doc4Reporting.getId()), doc4Reporting.getASN(), doc4Reporting.getPTIURN().getNewValue());
                    DataMigrationStatistics.addObject(dmo);
                }                
            }
            
            court4Reporting.setDefendantsGroup(defendantsGroupReporting);
            court4Exceptions.setDefendantsGroupError(defendantsGroupExceptions);
            
            court4Reporting.setTotalDefendantOnCase(totalDefOnCase);
            court4Reporting.setTotalDefendantOnOffence(totalDefOnOffence);

            court4Exceptions.setTotalDefendantOnCaseError(totalDefOnCase);
            court4Exceptions.setTotalDefendantOnOffenceError(totalDefOnOffence);
            
            courtsGroupReporting.addCourt(court4Reporting);
            courtsGroupExceptions.addCourtError(court4Exceptions);
            
            dmo = new DataMigrationObject(courtId,"updateStatusC",courtId,java.util.Calendar.getInstance());
            dbManager.updateStatus(courtId.intValue(), "C");
            DataMigrationStatistics.addObject(dmo);
            
            dmo = new DataMigrationObject(courtId,"updateDataMigrationTotals",courtId,java.util.Calendar.getInstance());
            dbManager.updateDataMigrationTotals(courtId, new Integer(totalDefOnCase), new Integer(totalDefOnOffence));
            DataMigrationStatistics.addObject(dmo);
        }
        
        dataMigrationReporting.setCourtsGroup(courtsGroupReporting);
        dataMigrationExceptions.setCourtsGroupError(courtsGroupExceptions);
        
        DataMigrationStatistics.generateStatisticsReport();
        DataMigrationReportGenerator.generateReport(dataMigrationReporting);
        DataMigrationReportGenerator.generateErrorReport(dataMigrationExceptions);
    }
    
    private String buildErrorMessage(
            boolean noValidASNFound, 
            boolean invalidPTIRUNFound,
            String ptiurn) {
        StringBuilder sb = new StringBuilder();
        if( noValidASNFound ) {
            sb.append("No Valid ASN Found");
        }
        if( invalidPTIRUNFound ) {
            if(sb.length() > 0) {
                sb.append(". ");
            }
            sb.append("Invalid PTIURN (");
            sb.append(ptiurn);
            sb.append(")");
        } else if ( ptiurn == null || ptiurn.equals("")) {
            if(sb.length() > 0) {
                sb.append(". ");
            }
            sb.append("Empty PTIURN");
        }
        
        return sb.toString();
    }
    
    private boolean inUpdateMode() {
        boolean returnValue = false;
        try {
            String mode = DataMigrationProperties.getProperties().getMode();
            returnValue = mode.equalsIgnoreCase("U");
        }
        catch( Exception e) {
            returnValue = false;
        }
        return returnValue;
    }
    
    private String obtainASNFromCRNId(String crnId) {
        return crnId == null ? null : crnId.substring(0, (crnId.length() >= AsnHelper.LENGTH ? AsnHelper.LENGTH : crnId.length()));
    }
    
    private boolean isValidAsn(String asn) {
        try {
            return asnHelper.isValidAsn(asn);
        }
        catch(Exception e) {
            return false;
        }
    }
    
    private boolean isValidPtiurn(String ptiurn) {
        try {
            return ptiurnHelper.isValidPtiurn(ptiurn);
        }
        catch(Exception e) {
            return false;
        }
    }
    
    private DefendantOnCase buildDefendantOnCaseReporting(DefendantByCourtVO defOnCase) {
        DefendantOnCase doc = new DefendantOnCase();
        
        doc.setASN(null);
        doc.setCaseId(defOnCase.getCaseId().intValue());
        doc.setCaseType(defOnCase.getCaseType());
        doc.setCaseNumber(defOnCase.getCaseNumber().toString());
        doc.setCaseSubType(defOnCase.getCaseSubType() == null || defOnCase.getCaseSubType().trim().length() == 0 ? 
            null : 
            defOnCase.getCaseSubType().trim()
        );
        doc.setDefendantId(defOnCase.getDefendantId().intValue());
        doc.setName(buildTidyName(defOnCase));
        PTIURN ptiurn = new PTIURN();
        ptiurn.setOldValue(defOnCase.getPtiurn());
        ptiurn.setNewValue(null);
        doc.setPTIURN(ptiurn);
        doc.setId(defOnCase.getId().intValue());
        doc.setCrestDefendantId(defOnCase.getCrestDefendantId() == null ? 0 : defOnCase.getCrestDefendantId().intValue());
        
        return doc;
    }
       
    private DefendantOnCaseError buildDefendantOnCaseError(DefendantByCourtVO defOnCase) {
        DefendantOnCaseError doc = new DefendantOnCaseError();

        doc.setCaseId(defOnCase.getCaseId().intValue());
        doc.setCaseType(defOnCase.getCaseType());
        doc.setCaseNumber(defOnCase.getCaseNumber().toString());
        doc.setCaseSubType(defOnCase.getCaseSubType() == null || defOnCase.getCaseSubType().trim().length() == 0 ? 
            null : 
            defOnCase.getCaseSubType().trim()
        );
        doc.setDefendantId(defOnCase.getDefendantId().intValue());
        doc.setName(buildTidyName(defOnCase));
        doc.setId(defOnCase.getId().intValue());
        doc.setCrestDefendantId(defOnCase.getCrestDefendantId() == null ? 0 : defOnCase.getCrestDefendantId().intValue());
                
        return doc;
    }
    
    private Court buildCourtReporting(CourtVO courtVO) {
        Court court = new Court();
        
        court.setCrestCourtId(courtVO.getCrestCourtId());
        court.setId(courtVO.getId().intValue());
        court.setName(courtVO.getDisplayName());
        
        return court;
    }
    
    private CourtError buildCourtError(CourtVO courtVO) {
        CourtError court = new CourtError();
        
        court.setCrestCourtId(courtVO.getCrestCourtId());
        court.setId(courtVO.getId().intValue());
        court.setName(courtVO.getDisplayName());
        
        return court;
    }
    
    private DefendantOnOffence buildDefendantOnOffenceReporting(OffenceByDefendantVO defOnOffence) {
        DefendantOnOffence doo = new DefendantOnOffence();
        
        doo.setChargeId(defOnOffence.getChargeId().intValue());
        doo.setCrestChargeId(defOnOffence.getCrestChargeId() == null ? 0 : defOnOffence.getCrestChargeId().intValue());
        doo.setChargeType(defOnOffence.getChargeType());
        doo.setCrestOffenceSeqNo(defOnOffence.getCrestOffenceSeqNo() == null ? 0 : defOnOffence.getCrestOffenceSeqNo().intValue());
        doo.setCrestChargeSeqNo(defOnOffence.getCrestChargeSeqNo() == null ? 0 : defOnOffence.getCrestChargeSeqNo().intValue());
        doo.setCrnId(defOnOffence.getCrnId());
        doo.setId(defOnOffence.getId().intValue());
        doo.setCrestOffenceId(defOnOffence.getCrestOffenceId() == null ? 0 : defOnOffence.getCrestOffenceId().intValue());
        doo.setSeqNo(null);
        doo.setOffenceId(defOnOffence.getOffenceId().intValue());
        
        return doo;
    }
    
    private String buildTidyName(DefendantByCourtVO defOnCase) {
        StringBuilder buf = new StringBuilder();
        
        buf.append(defOnCase.getSurname() == null ? "" : defOnCase.getSurname().trim());
        
        if( defOnCase.getFirstName() != null ) {
            buf.append(buf.length() == 0 ? "" : ", ");
            buf.append(defOnCase.getFirstName().trim());
        }
        
        return buf.toString();
    }
}


