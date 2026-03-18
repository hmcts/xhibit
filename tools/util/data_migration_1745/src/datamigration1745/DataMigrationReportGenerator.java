package datamigration1745;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationreport.*;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.datamigrationerror.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.List;
import java.util.Arrays;

import datamigration1745.database.DataMigrationDatabaseFactory;

/**
 * This class is used for help when creating files with Data Migration reports and error data
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: DataMigrationReportGenerator.java,v 1.8 2009/03/16 17:43:24 hewittm Exp $ Exp $
 */
public class DataMigrationReportGenerator
{
    private static DataMigrationPropsStructure dataMigrationPropsStructure = null;
   
    private static String REPORT_FORMAT = "datamigration.reportformat";
    
    private static String DEFAULT_REPORT_FORMAT = DataMigrationDatabaseFactory.DEFAULT_REPORT_FORMAT;
        
    static {
        try
        {
            dataMigrationPropsStructure = DataMigrationProperties.getProperties();
        }
        catch(java.io.IOException io)
        {
            out("<< ERROR: IOException during Data Migration Report Generator initialisation:" + io); 
            System.exit(0);
        }
        catch(Exception e)
        {
            out("<< ERROR: Exception during Data Migration Report Generator initialisation:" + e);
            System.exit(0);
        }
    }
    
	/**
     * <p>Title: generateReport </p>
     * <p>Description: Creates a Report containing the Data Migration Report passed in</p>
     * @param DataMigrationReport     
     * @param boolean: true or false depending on success or failure of the operation
     */
	public static boolean generateReport(DataMigrationReport report)
	{
        boolean success = false;
        
		String fileName  = null;
		File   directory = null;
		File   file      = null;

		FileOutputStream fileOS = null;
		PrintWriter pw = null;
 
        if(report == null)
        {
            out("<< Data Migration Report is null >>");
            return false;
        }
        
		try
		{
            directory = createDirectory(dataMigrationPropsStructure.getReportDir());
            
            CourtsGroup courtsGroup = report.getCourtsGroup();
            Court[] courtsArray = courtsGroup.getCourt();
            List<Court> courts = Arrays.asList(courtsArray);
            
            if(System.getProperty(REPORT_FORMAT, DEFAULT_REPORT_FORMAT).equalsIgnoreCase("COURT"))
            {
                for(Court court:courts)
                {
                    fileName = court.getCrestCourtId() + "_" + dataMigrationPropsStructure.getReportFile();

                    out("<< Writing to an XML file called >>: " + fileName);

                    file = new File(directory, fileName);

                    out("<< Create FileOS >>");

                    fileOS = new FileOutputStream (file);

                    out("<< Write Data Migration Report to fileOS >>");
                
                    pw = new PrintWriter(fileOS, false);
                    
                    court.marshal(pw);
                }
            }
            else
            {
                fileName = dataMigrationPropsStructure.getReportFile();

	            out("<< Writing to an XML file called >>: " + fileName);

		        file = new File(directory, fileName);

     	        out("<< Create FileOS >>");

	            fileOS = new FileOutputStream (file);

	            out("<< Write Data Migration Report to fileOS >>");
            
                pw = new PrintWriter(fileOS, false);
                
                report.marshal(pw);
            }
            
            for(Court court:courts)
            {
                out("<< Generating CSV reports for:" + court.getId() + " where Crest Court Id is:" + court.getCrestCourtId());
                success = generateCsvReport(court);
            }
            
	        out("<< Completed Writing Data Migration Report Message to fileOS >>");
            success = true;
		}
		catch(Exception e)
		{
            out("<< ERROR: Exception during creation of the Data Migration Report:" + e);
		}
		finally
		{
			try
			{
				if (pw!=null)
			    {
					out("<< Close PrintWriter >>: ");
					pw.close();
			    }
			}
			catch(Exception e)
			{
				out("<< ERROR: Exception during closing of Data Migration Report Print Writer:" + e);
			}

			try
			{
				if (fileOS!=null)
			    {
					out("<< Close file os >>: ");
					fileOS.close();
			    }
			}
			catch(Exception e)
			{
                out("<< ERROR: Exception during closing of Data Migration Report file output stream:" + e);
			}
		}
        
        return success;
	}


    /**
     * <p>Title: generateErrorReport </p>
     * <p>Description: Creates an Error Report containing the Data Migration Error passed in</p>
     * @param DataMigrationError     
     * @param boolean: true or false depending on success or failure of the operation
     */
    public static boolean generateErrorReport(DataMigrationError error)
    {
        out("<< generateErrorReport >>");
        
        boolean success = false;
        
        String fileName  = null;
        File   directory = null;
        File   file      = null;

        FileOutputStream fileOS = null;
        PrintWriter pw = null;
 
        if(error == null)
        {
            out("<< Data Migration Error is null >>");
            return false;
        }
        
        try
        {
            directory = createDirectory(dataMigrationPropsStructure.getErrorDir());

            CourtsGroupError courtsGroupError = error.getCourtsGroupError();
            CourtError[] courtsArrayError = courtsGroupError.getCourtError();
            List<CourtError> courtErrors = Arrays.asList(courtsArrayError);
            
            if(System.getProperty(REPORT_FORMAT, DEFAULT_REPORT_FORMAT).equalsIgnoreCase("COURT"))
            {
                for(CourtError courtError:courtErrors)
                {
                    fileName = courtError.getCrestCourtId() + "_" + dataMigrationPropsStructure.getErrorFile();

                    out("<< Writing to an XML file called >>: " + fileName);

                    file = new File(directory, fileName);

                    out("<< Create FileOS >>");

                    fileOS = new FileOutputStream (file);

                    out("<< Write Data Migration Report to fileOS >>");
                
                    pw = new PrintWriter(fileOS, false);
                    
                    courtError.marshal(pw);
                }
            }
            else
            {
                fileName = dataMigrationPropsStructure.getErrorFile();

                out("<< Writing to an XML file called >>: " + fileName);

                file = new File(directory, fileName);

                out("<< Create FileOS >>");

                fileOS = new FileOutputStream (file);

                out("<< Write Data Migration Error to fileOS >>");
            
                pw = new PrintWriter(fileOS, false);
                
                error.marshal(pw);
            }
            
            out("<< Completed Writing Data Migration Error to fileOS >>");
            success = true;
        }
        catch(Exception e)
        {
            out("<< ERROR: Exception during creation of the Data Migration Error:" + e);
        }
        finally
        {
            try
            {
                if (pw!=null)
                {
                    out("<< Close PrintWriter >>: ");
                    pw.close();
                }
            }
            catch(Exception e)
            {
                out("<< ERROR: Exception during closing of Data Migration Error Print Writer:" + e);
            }

            try
            {
                if (fileOS!=null)
                {
                    out("<< Close file os >>: ");
                    fileOS.close();
                }
            }
            catch(Exception e)
            {
                out("<< ERROR: Exception during closing of Data Migration Error file output stream:" + e);
            }
        }
        
        return success;
    }

    /**
     * Generates a comma separated list version of the report for the specified court
     * Files:
     * One per element of the CourtsGroup
     * <Court.crestCourtId>_data_migration.csv
     * All values will be available
     * DOC Records:
     * One per element of the DefendantsGroup within a Court
     * 01,<Court.crestCourtId>,<Court.DefendantOnCase.caseType>,<Court.DefendantOnCase.caseNumber>,<Court.DefendantOnCase.crestDefendantId>,<Court.DefendantOnCase.PTIURN.newValue>,<Court.DefendantOnCase.ASN>
     * PTIURN may be null
     * ASN may be null
     * All values should be available in so far as they may be business mandatory but are not necessarily manadtory on the D/B
     * DOO Records:
     * One per element of the OffencesGroup within a DefendantOnCase. Type 03 will be Indictments, 02 all other offence types
     * 02 or 03,<Court.crestCourtId>,<Court.DefendantOnCase.caseType>,<Court.DefendantOnCase.caseNumber>,<Court.DefendantOnCase.crestDefendantId>,<Court.DefendantOnCase.DefendantOnOffence.chargeType>,
     * continued <Court.DefendantOnCase.DefendantOnOffence.crestChargeId>,<Court.DefendantOnCase.DefendantOnOffence.crestChargeSeqNo>,<Court.DefendantOnCase.DefendantOnOffence.crestOffenceId>,<Court.DefendantOnCase.DefendantOnOffence.crestOffenceSeqNo>,<Court.DefendantOnCase.DefendantOnOffence.seqNo>
     * All values should be available in so far as they may be business mandatory but are not necessarily manadtory on the D/B
     */
    private static boolean generateCsvReport(Court court) 
    {        
        String csvString = "";
        boolean success = false;
        String fileName  = null;
        File   file      = null;
        FileOutputStream fileOS = null;
        BufferedWriter bw = null;
        String crestCourtId = court.getCrestCourtId();
        
        try
        {
            if (court!=null) {

                fileName = court.getCrestCourtId() + "_" + dataMigrationPropsStructure.getReportFile();

                if(fileName.endsWith(".xml"))
                {
                    fileName = fileName.replace("xml", "csv");
                }

                file = new File(DataMigrationProperties.getProperties().getReportDir(), fileName);

                System.out.println("<< Write Data Migration Stats to XML file: >>" + fileName);
                bw = new BufferedWriter(new FileWriter(file));

                
                DefendantsGroup defendantsGroup = court.getDefendantsGroup();
                DefendantOnCase[] docArray = defendantsGroup.getDefendantOnCase();
                List<DefendantOnCase> docs = Arrays.asList(docArray);
                
                
                String ptiurn = null;
                String asn = "";
                String seqNo = "";
                String chargeType = "";
                String crestChargeId = "";
                String crestChargeSeqNo = "";
                String crestOffenceId = "";
                String crestOffenceSeqNo = "";
                String csvOffenceType = "";
                Integer crestChargeIdInteger = null;
                Integer crestChargeSeqNoInteger = null;
                Integer crestOffenceIdInteger = null;
                Integer crestOffenceSeqNoInteger = null;
                
                for(DefendantOnCase doc:docs)
                {
                    if(doc.getPTIURN()!=null && doc.getPTIURN().getNewValue()!=null)
                    {
                        ptiurn=doc.getPTIURN().getNewValue();
                    }
                    else
                    {
                        ptiurn="";
                    }
                    if(doc.getASN()!=null)
                    {
                        asn=doc.getASN();
                    }
                    else
                    {
                        asn="";
                    }
                    
                    bw.newLine();
                    csvString = "01" + "," + crestCourtId + ","  + doc.getCaseType() + "," + doc.getCaseNumber() + "," + doc.getCrestDefendantId() + "," + ptiurn + "," + asn;
                    bw.write(csvString);
                    //csvString = csvString + "\n" + "01" + "," + crestCourtId + ","  + doc.getCaseType() + "," + doc.getCaseNumber() + "," + doc.getCrestDefendantId() + "," + ptiurn + "," + asn;
                    
                    ptiurn="";
                    asn="";
                    
                    OffencesGroup offencesGroup = doc.getOffencesGroup();
                    DefendantOnOffence[] dooArray = offencesGroup.getDefendantOnOffence();
                    List<DefendantOnOffence> doos = Arrays.asList(dooArray);
                    
                    for(DefendantOnOffence doo:doos)
                    {
                        if(doo.getSeqNo()!=null )
                        {
                            seqNo=doo.getSeqNo();
                        }
                        else
                        {
                            seqNo="";
                        }
                        if(doo.getChargeType()!=null )
                        {
                            chargeType=doo.getChargeType();
                        }
                        else
                        {
                            chargeType="";
                        }
                        
                        if(chargeType.trim().equalsIgnoreCase("I"))
                        {
                            csvOffenceType = "03";  
                        }
                        else
                        {
                            csvOffenceType = "02"; 
                        }
                        
                        crestChargeIdInteger = doo.getCrestChargeId();
                        
                        if(crestChargeIdInteger!=null && crestChargeIdInteger != 0  )
                        {
                            crestChargeId=crestChargeIdInteger.toString();
                        }
                        else
                        {
                            crestChargeId="";
                        }
                        
                        crestChargeSeqNoInteger = doo.getCrestChargeSeqNo();
                        
                        if(crestChargeSeqNoInteger!=null && crestChargeSeqNoInteger != 0  )
                        {
                            crestChargeSeqNo=crestChargeSeqNoInteger.toString();
                        }
                        else
                        {
                            crestChargeSeqNo="";
                        }
                        
                        crestOffenceIdInteger = doo.getCrestOffenceId();
                        
                        if(crestOffenceIdInteger!=null && crestOffenceIdInteger != 0  )
                        {
                            crestOffenceId=crestOffenceIdInteger.toString();
                        }
                        else
                        {
                            crestOffenceId="";
                        }
                        
                        crestOffenceSeqNoInteger = doo.getCrestOffenceSeqNo();
                        
                        if(crestOffenceSeqNoInteger!=null && crestOffenceSeqNoInteger != 0  )
                        {
                            crestOffenceSeqNo=crestOffenceSeqNoInteger.toString();
                        }
                        else
                        {
                            crestOffenceSeqNo="";
                        }
                        
                        bw.newLine();
                        csvString = csvOffenceType + "," + crestCourtId + "," +  doc.getCaseType() +  "," + doc.getCaseNumber() + "," + doc.getCrestDefendantId() + "," + chargeType + "," + crestChargeId + "," + crestChargeSeqNo + "," + crestOffenceId + "," + crestOffenceSeqNo + "," + seqNo; 
                        bw.write(csvString);
                        //csvString = csvString + "\n" + csvOffenceType + "," + crestCourtId + "," +  doc.getCaseType() +  "," + doc.getCaseNumber() + "," + doc.getCrestDefendantId() + "," + chargeType + "," + crestChargeId + "," + crestChargeSeqNo + "," + crestOffenceId + "," + crestOffenceSeqNo + "," + seqNo; 
                        
                        seqNo="";
                        chargeType="";
                        crestChargeId = "";
                        crestChargeSeqNo = "";
                        crestOffenceId = "";
                        crestOffenceSeqNo = "";
                        csvOffenceType= "";
                        crestChargeIdInteger = null;
                        crestChargeSeqNoInteger = null;
                        crestOffenceIdInteger = null;
                        crestOffenceSeqNoInteger = null;
                    }
                }
            }
            success=true;
        }
        catch(Exception e)
        {
            System.out.println("<< ERROR: Exception during creation of the Data Migration CSV Report:" + e);
        }
        finally
        {
            try
            {
                if (bw!=null)
                {
                    System.out.println("<< Close BufferedWriter >>: ");
                    bw.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< ERROR: Exception during closing of Data Migration CSV Report Buffered Writer:" + e);
            }

            try
            {
                if (fileOS!=null)
                {
                    System.out.println("<< Close file os >>: ");
                    fileOS.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("<< ERROR: Exception during closing of Data Migration CSV Report file output stream:" + e);
            }
        }    
        return success;
    }
    
    /**
     * <p>
     * Title: createDirectory
     * </p>
     * <p>
     * Description: Creates a directory to put files in 
     * </p>
     *
     * @param dirName
     * @return File
     */
    private static File createDirectory(String dirName) {

        File directory = null;
        
        directory = new File(dirName);

        if (!directory.exists()) {
            out("<< Creating directory >>: " + dirName);
            boolean isDirCreated = directory.mkdir();
            if (!isDirCreated) {
                out("<< ERROR Directory not created for Data Migration:" + dirName);
                System.exit(0);
            }
        }

        return directory;
    }

    private static void out(String in)
    {
        System.out.println(in+"\n\n");
    }

}