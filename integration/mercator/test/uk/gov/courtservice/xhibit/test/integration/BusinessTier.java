package uk.gov.courtservice.xhibit.test.integration;



// Court Service
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.integration.services.IntegrationFacadeImpl;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;
import uk.gov.courtservice.xhibit.integration.services.MercatorExecutionException;
import uk.gov.courtservice.xhibit.integration.services.MercatorValidationException;
//import uk.gov.courtservice.xhibit.integration.services.caseretrieval.CaseAccessException;
//import uk.gov.courtservice.xhibit.integration.services.caseretrieval.CaseRetrievalIntControllerException;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Cag Onganer
 * @version 1.0
 */

public class BusinessTier
{
  AddressValue address;
  uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendant;
  OffenceValue offence;
  uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue delOffence;
  uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breach;
  uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue charge;
  uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue delCharge;
  Integer caseId;
  String chargeImportIndicator;
  uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue signInd;
  Collection defendantCountList;
  String newStatus;

  public BusinessTier()
  {
    /*IntegrationFacadeImpl ifi = new IntegrationFacadeImpl();
    Calendar cal = Calendar.getInstance();

    // Value Objects
    address = new AddressValue("EDS",
        "Solution Consulting", "4 Roundwood Avenue",
        "Stockley Park", "Uxbridge", "Middlesex", "UB11 1BQ", "England");
    defendant = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(1), new Integer(2),
                                   "Marie", "Ulrika", "Holmberg", "UE",
                                   cal, new Integer(2), cal, new Integer(1),
                                   address);
    offence = new OffenceValue(new Integer(1), new Integer(1), new Integer(1),null,
                               "Crest Offence Free Text", new Integer(1), new Integer(1), new Integer(1),
                               "Offence Description");

    delOffence = new uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue(new Integer(1), new Integer(1), new Integer(1), new Integer(1),
                                     null,true);

    breach = new uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue(new Integer(1), "Original Sentence", cal, "Magistrate's", new Integer(1),
                             cal, "Breach Type", "Bring Back", "Home Office Code", "Home Office Description",
                             true);

    //NOTE: Since the constructor for ChargeType is no longer public this will not run.
    //We will also get a null pointer exeception when trying to run the code.
    //ChargeType chargeType = new ChargeType("Charge Type Description", "Charge Type Value");
    // charge = new ChargeValue(new Integer(1),new Integer(1), null ,new Integer(1), new Integer(1),cal,breach,null,new Integer(1),new Integer(1), cal, cal, "indResp");
    delCharge = new uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue(new Integer(1), new Integer(1), new Integer(1), new Integer(1),
                                   new Integer(1), true);
    signInd = new uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue(new Integer(1), new Integer(1), new Integer(1),
                               new Integer(1),cal, true,true);

    // Default Values

    caseId= new Integer(1);
    chargeImportIndicator="O";
    defendantCountList = new LinkedList();
    defendantCountList.add(defendant);
    newStatus = "C";


      try
      {
// Defendant Controller
        ifi.updateDefendant(defendant);
// Prehearing Controller
        ifi.addOffence(offence);
        ifi.deleteOffence(delOffence);
        ifi.updateBreach(breach);
        ifi.updateOffence(offence);
//      ifi.addChargeToCase(charge);
        ifi.deleteCharge(delCharge);
        ifi.openCase(caseId);
        ifi.checkCaseAccess(caseId);
        ifi.refreshLeaseTime(caseId);
        ifi.closeCase(caseId);
        ifi.exportCharges(caseId);
        ifi.setChargeImportIndicator(caseId,chargeImportIndicator);
        ifi.signIndictment(signInd);
        ifi.updateDefendantOnCountStatus(defendantCountList, newStatus);
      }
      catch (CaseAccessException ex)
      {
        System.out.println(ex.getMessage());
      }
      catch (CaseRetrievalIntControllerException ex)
      {
        System.out.println(ex.getMessage());
      }
      catch (MercatorExecutionException ex)
      {
        System.out.println(ex.getMessage());
      }
      catch (MercatorValidationException ex)
      {
        System.out.println(ex.getMessage());
      }
      catch (MercatorException ex)
      {
        System.out.println(ex.getMessage());
      }*/
  }

  public static void main(String[] args)
  {
    BusinessTier businessTier1 = new BusinessTier();
    System.out.println("The Buiness Tier has invoked the interface!");
  }

}